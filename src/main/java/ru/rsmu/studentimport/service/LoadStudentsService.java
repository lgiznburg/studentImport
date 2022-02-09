package ru.rsmu.studentimport.service;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.rsmu.studentimport.dao.CommonDao;
import ru.rsmu.studentimport.model.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author leonid.
 */
@Component
public class LoadStudentsService implements ExcelLayout {
    protected Logger logger = LoggerFactory.getLogger( LoadStudentsService.class );

    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd.MM.yyyy" );
    protected static final DateFormat YEAR_FORMAT = new SimpleDateFormat( "yyyy" );
    protected static final Locale RU_LOCALE = Locale.forLanguageTag( "ru_RU" );

    @Autowired
    private CommonDao dao;

    @Autowired
    private PhotoFileService photoFileService;

    private List<SpecialSubject> subjects;

    private List<Country> countries;

    private Map<String,FacultyProgram> programMap;

    private List<EduDocumentType> eduDocumentTypes;

    private List<IdDocumentType> idDocumentTypes;

    private List<ForeignLanguage> languages;

    private Recruitment recruitment;

    @PostConstruct
    public void loadDictionaries() {
        subjects = dao.findAllEntity( SpecialSubject.class );

        countries = dao.findAllEntity( Country.class );

        programMap = new HashMap<>();
        for ( FacultyProgram program : dao.findAllFacultyProgram() ) {
            if ( program.getFaculty() == Faculty.FOREIGN.getId() &&
                    (program.getDirection() == Direction.MEDICAL.getId() || program.getDirection() == Direction.STOMAT.getId()) ) {
                if ( program.getComment() != null && program.getComment().equals( "англофоны" ) ) {
                    // hack for anglophones
                    /*
                    необходимо разделить направления для англофонов и рускоговорящих иностранцев, так как в базе АОС
                    коды факультета и направления у них совпадают.
                    для этого созданы "фейковые" элементы справочника Direction.ANGLOPHONE и Direction.ANGLO_STOMAT
                    при загрузке таблицы соответсия programMap, ключи этих направлений формируются с использованием указанных элементов
                    при загрузке студентов производится обратная подстановка
                     */
                    if ( program.getDirection() == Direction.MEDICAL.getId() ) {
                        programMap.put( String.format( "%d - %d", program.getFaculty(), Direction.ANGLOPHONE.getId() ), program );
                    } else {
                        programMap.put( String.format( "%d - %d", program.getFaculty(), Direction.ANGLO_STOMAT.getId() ), program );
                    }
                }
                else if ( (program.getDirection() == Direction.MEDICAL.getId() && program.getComment() != null && program.getComment().equals( "лечебный" ))
                        || (program.getDirection() == Direction.STOMAT.getId())
                ) {
                    programMap.put( String.format( "%d - %d", program.getFaculty(), program.getDirection() ), program );
                }
                continue;
            }
            programMap.put( String.format( "%d - %d", program.getFaculty(), program.getDirection() ), program );
        }

        eduDocumentTypes = dao.findAllEntity( EduDocumentType.class );

        idDocumentTypes = dao.findAllEntity( IdDocumentType.class );

        languages = dao.findAllEntity( ForeignLanguage.class );

        recruitment = dao.findActiveRecruitment();
    }

    public List<String> readFromFile( InputStream file, Integer startFrom, Integer totals, boolean anglophones, boolean photo ) throws IOException {

        List<String> resultLog = new ArrayList<>();

        POIFSFileSystem fs = new POIFSFileSystem( file );
        HSSFWorkbook wb = new HSSFWorkbook( fs );

        HSSFSheet sheet = wb.getSheetAt( 0 );

        int rowN = 1 + startFrom;
        int savedN = 0;
        do {
            HSSFRow row = sheet.getRow( rowN );
            // check if row is valid
            if ( row == null || row.getCell( (short) 0 ) == null ) {
                break;  // end of file
            }

            String caseNumber = ServiceUtils.getCellValue( row, CASE_NUMBER );

            StudentDFS dfs = dao.findDfs( caseNumber );
            if ( dfs != null ) {
                checkSpecialTargeting( row, dfs, resultLog );
                if ( photo ) {
                    createPhoto( row, dfs, resultLog );
                }
                else {
                    resultLog.add( String.format( "Дело №%s уже существует.", caseNumber ) );
                }
                rowN++;
                continue;
            }
            StudentContext context = new StudentContext( dao );

            context.readStudent( row, countries );
            context.readIdDocument( row, idDocumentTypes );
            context.checkExistedPerson();

            resultLog.addAll(  context.readStudentDfs( row, anglophones, programMap, subjects ) );
            context.getDfs().setRecruitment( recruitment );

            context.readOrderInfo( row );

            context.readAddress( row, countries, subjects );

            context.readEduDocument( row, eduDocumentTypes );

            context.readBenefit( row );
            

            context.readLanguages( row, languages );


            dao.saveAllInfo( context );

            createPhoto( row, context.getDfs(), resultLog );
            rowN++;
            savedN++;
        } while ( totals == 0 || savedN < totals );
        resultLog.add( String.format( "Всего обработано %d строк, создано %d записей.", rowN - 1 - startFrom, savedN ) );
        return resultLog;
    }

    


    private void checkSpecialTargeting( HSSFRow row, StudentDFS dfs, List<String> resultLog ) {
        String subjectName = ServiceUtils.getCellValue( row, TARGET_REGION );
        if ( subjectName != null && subjectName.trim().length() > 0 && dfs.getSubject() == null ) {
            SpecialSubject specialSubject = findSpecialSubject( subjectName );
            if ( specialSubject != null ) {
                dfs.setSubject( specialSubject );
                dao.saveEntity( dfs );
            } else {
                logger.warn( String.format( "Student (%s №%s) with special targeting program should have organization name.", dfs.getStudent().getLastName(), dfs.getCaseNumber() ) );
                resultLog.add( String.format( "Student (%s №%s) - special targeting organization (%s) not found.", dfs.getStudent().getLastName(), dfs.getCaseNumber(), subjectName ) );
            }
        }
    }

    private SpecialSubject findSpecialSubject( String subjectName ) {
        List<String> excludes = Arrays.<String>asList( "Республика", "край", "область", "автономный", "округ", "г.", "России", "Чувашия" );

        if ( subjectName.contains( "Минтруд России" )) {
            subjectName = "ФГБУ ФБ МСЭ Минтруда России";
        }
        for ( SpecialSubject subject : subjects ) {
            String[] parts = subject.getName().split( "[\\- ]" );
            int factor = parts.length;
            int score = 0;
            for ( String part : parts ) {
                if ( excludes.contains( part ) || part.matches( "\\(.*\\)" ) ) {
                    factor--;
                    continue;
                }
                if ( subjectName.toLowerCase( ServiceUtils.RU_LOCALE ).contains( part.toLowerCase( ServiceUtils.RU_LOCALE ) ) ) {
                    score++;
                }
            }
            if ( score == factor ) {
                return subject;
            }
        }

        return null;
    }

    private void createPhoto( HSSFRow row, StudentDFS dfs, List<String> resultLog) {
        String photoName = ServiceUtils.getCellValue( row, PHOTO_FILENAME );
        if ( !StringUtils.isEmpty( photoName ) ) {
            PersonPhoto personPhoto = photoFileService.createImages( photoName );
            if ( personPhoto != null ) {
                personPhoto.setStudent( dfs.getStudent() );
                dao.saveEntity( personPhoto );
            }
            else {
                resultLog.add( String.format( "Фотография студента %s № %s не создана", dfs.getStudent().getLastName(), dfs.getCaseNumber() ) );
            }
        }
        else {
            resultLog.add( String.format( "Фотография студента %s № %s отсутствует", dfs.getStudent().getLastName(), dfs.getCaseNumber() ) );
        }

    }
}
