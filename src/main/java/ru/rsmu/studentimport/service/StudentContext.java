package ru.rsmu.studentimport.service;

import org.apache.poi.hssf.usermodel.HSSFRow;
import ru.rsmu.studentimport.dao.CommonDao;
import ru.rsmu.studentimport.model.*;

import java.text.ParseException;
import java.util.*;

/**
 * @author leonid.
 */
public class StudentContext implements ExcelLayout {

    private CommonDao dao;

    private boolean freshStudent = true;

    private Student student = null;
    private StudentDFS dfs = null;
    private OrderInfo orderInfo ;
    private Address address = null;
    private EduDocument eduDocument = null;
    private IdDocument idDocument = null;
    private List<StudentLanguage> languages = new ArrayList<>();
    private PersonBenefit benefit = null;

    public StudentContext( CommonDao dao ) {
        this.dao = dao;
    }

    public Student getStudent() {
        return student;
    }

    public StudentDFS getDfs() {
        return dfs;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public Address getAddress() {
        return address;
    }

    public EduDocument getEduDocument() {
        return eduDocument;
    }

    public IdDocument getIdDocument() {
        return idDocument;
    }

    public List<StudentLanguage> getLanguages() {
        return languages;
    }

    public PersonBenefit getBenefit() {
        return benefit;
    }

    public Student readStudent( HSSFRow row, List<Country> countries ) {
        student = new Student();

        student.setLastName( ServiceUtils.getCellValue( row, LAST_NAME ) );
        student.setFirstName( ServiceUtils.getCellValue( row, FIRST_NAME ) );
        student.setMiddleName( ServiceUtils.getCellValue( row, MIDDLE_NAME ) );

        String gender = ServiceUtils.getCellValue( row, SEX );
        if ( "Мужской".equals( gender ) ) {
            student.setGender( 1 );
        } else {
            student.setGender( 2 );
        }

        String birthDate = ServiceUtils.getCellValue( row, BIRTH_DATE );
        try {
            student.setBirthDate( ServiceUtils.DATE_FORMAT.parse( birthDate ) );
        } catch (ParseException e) {
            // logger.error( "Birth date year", e );
        }

        student.setBirthPalce( ServiceUtils.getCellValue( row, BIRTH_PLACE ) );  // 2 symbols code

        String country = ServiceUtils.getCellValue( row, CITIZENSHIP );
        student.setCountry( findCountry( country, countries ) );

        student.setEmail( ServiceUtils.getCellValue( row, EMAIL ) );

        student.setMobilePhone( ServiceUtils.getCellValue( row, MOBILE_PHONE ) );
        student.setHomePhone( ServiceUtils.getCellValue( row, HOME_PHONE ) );
        student.setContactPhone( ServiceUtils.getCellValue( row, CONTACT_PHONE ) );

        student.setSnils( ServiceUtils.getCellValue( row, SNILS ) );

        return student;
    }

    private Country findCountry( String countryName, List<Country> countries ) {
        CountryMatch match = CountryMatch.findMatch( countryName );
        String code = match == null ? countryName : match.getAlpha3();

        for ( Country country : countries ) {
            if ( country.getAlpha3() != null && country.getAlpha3().equals( code ) ) return country;
        }
        // гражданство не найдено - без гражданства
        for ( Country country : countries ) {
            if ( country.getAlpha3() == null ) return country;
        }
        return null;
    }

    public IdDocument readIdDocument( HSSFRow row, List<IdDocumentType> idDocumentTypes ) {
        idDocument = new IdDocument();
        idDocument.setStudent( student );
        idDocument.setSeria( ServiceUtils.getCellValue( row, ID_SERIA ) );
        idDocument.setNumber( ServiceUtils.getCellValue( row, ID_NUMBER ) );
        idDocument.setIssuedBy( ServiceUtils.getCellValue( row, ID_ISSUED_BY ) );
        idDocument.setIssuedByCode( ServiceUtils.getCellValue( row, ID_ISSUED_BY_CODE ) );
        idDocument.setType( findIdType( ServiceUtils.getCellValue( row, ID_TYPE ), idDocumentTypes ) );
        try {
            idDocument.setDate( ServiceUtils.DATE_FORMAT.parse( ServiceUtils.getCellValue( row, ID_DATE ) ) );
        } catch (ParseException e) {
            //logger.error( "ID date", e );
        }
        return idDocument;
    }

    private IdDocumentType findIdType( String name, List<IdDocumentType> idDocumentTypes ) {
        for ( IdDocumentType type : idDocumentTypes ) {
            if ( type.getName().equalsIgnoreCase( name ) ) return type;
        }
        return null;
    }

    public void checkExistedPerson() {
        IdDocument prevYearDocument = dao.findPrevYearDocument( idDocument, student );
        if ( prevYearDocument != null ) {
            Student tempStudent = student;
            
            student = prevYearDocument.getStudent();
            idDocument = prevYearDocument;
            student.update( tempStudent );
            freshStudent = false;
        }
    }

    public List<String> readStudentDfs( HSSFRow row, boolean anglophones, Map<String, FacultyProgram> programMap, List<SpecialSubject> subjects ) {
        List<String> resultLog = new ArrayList<>();

        dfs = new StudentDFS();
        dfs.setStudent( student );

        dfs.setCaseNumber( ServiceUtils.getCellValue( row, CASE_NUMBER ) );


        String finance = ServiceUtils.getCellValue( row, COMPENSATION_TYPE );
        if ( "бюджет".equals( finance ) ) {
            dfs.setFinance( StudentDFS.FINANCE_BUDGET );
        } else {
            dfs.setFinance( StudentDFS.FINANCE_CONTRACT );
        }

        String subjectName = ServiceUtils.getCellValue( row, TARGET_REGION );
        if ( subjectName != null && subjectName.trim().length() > 0 ) {
            SpecialSubject specialSubject = findSpecialSubject( subjectName, subjects );
            if ( specialSubject != null ) {
                dfs.setSubject( specialSubject );
            } else {
                //logger.warn( String.format( "Student (%s №%s) with special targeting program should have organization name.", student.getLastName(), dfs.getCaseNumber() ) );
                resultLog.add( String.format( "Student (%s №%s) - special targeting organization (%s) not found.", student.getLastName(), dfs.getCaseNumber(), subjectName ) );
            }
        }

        String orgUnit = ServiceUtils.getCellValue( row, FACULTY );
        String direction = ServiceUtils.getCellValue( row, SPECIALITY );
        FacultyProgram facultyProgram = findFacultyProgram( orgUnit, direction, student, anglophones, programMap );
        if ( facultyProgram != null ) {
            dfs.setFacultyProgram( facultyProgram );
        } else {
            //logger.warn( String.format( "Student (%s №%s) should tied to faculty and speciality.", student.getLastName(), dfs.getCaseNumber() ) );
            resultLog.add( String.format( "Student (%s №%s) should tied to faculty (%s) and speciality (%s).", student.getLastName(), dfs.getCaseNumber(), orgUnit, direction ) );
        }
        return resultLog;
    }

    private FacultyProgram findFacultyProgram( String orgUnit, String directionName, Student student, boolean anglophones, Map<String, FacultyProgram> programMap ) {
        Direction direction = Direction.findDirection( directionName );
        Faculty faculty = "RUS".equals( student.getCountry().getAlpha3() ) ? Faculty.findFaculty( orgUnit ) : Faculty.FOREIGN;

        // hack for anglopones
        // изменяем код направления для получения правильных данных для англофонов
        if ( anglophones && direction == Direction.MEDICAL && faculty == Faculty.FOREIGN ) {
            direction = Direction.ANGLOPHONE;
        }
        if ( anglophones && direction == Direction.STOMAT && faculty == Faculty.FOREIGN ) {
            direction = Direction.ANGLO_STOMAT;
        }

        //FacultyProgram facultyProgram = dao.findFacultyProgram( faculty, direction );
        FacultyProgram facultyProgram = null;
        if ( faculty != null && direction != null ) {
            facultyProgram = programMap.get( String.format( "%d - %d", faculty.getId(), direction.getId() ) );
        }

        return facultyProgram;
    }

    // используется для определения и субъекта федерации, и организации целевого приема
    private SpecialSubject findSpecialSubject( String subjectName, List<SpecialSubject> subjects ) {
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

    public OrderInfo readOrderInfo( HSSFRow row ) {

        String orderNumber = ServiceUtils.getCellValue( row, ORDER_NUMBER );
        String orderDateStr = ServiceUtils.getCellValue( row, ORDER_DATE );
        Date orderDate = null;
        try {
            orderDate = ServiceUtils.DATE_FORMAT.parse( orderDateStr );
        } catch (ParseException e) {
            //logger.error( "Date of order", e );
        }

        OrderInfo order = dao.findOrderInfo( orderNumber, orderDate, dfs.getFacultyProgram() );
        if ( order == null ) {
            order = new OrderInfo();
            order.setOrderNumber( orderNumber );
            order.setOrderDate( orderDate );
            order.setFacultyProgram( dfs.getFacultyProgram() );
            order.setFaculty( dfs.getFacultyProgram().getFaculty() );
            dao.saveEntity( order );
        }
        if ( order.getFaculty() == null ) {
            order.setFaculty( dfs.getFacultyProgram().getFaculty() );
            dao.saveEntity( order );
        }

        dfs.setOrderInfo( order );

        return order;
    }

    public Address readAddress( HSSFRow row, List<Country> countries, List<SpecialSubject> subjects ) {
        if ( !freshStudent ) {
            address = dao.findAddress( student );
        } 
        if ( address == null ) {
            address = new Address();
            address.setStudent( student );
        }
        address.setCountry( findCountry( ServiceUtils.getCellValue( row, ADDRESS_COUNTRY ), countries ) );
        String region = ServiceUtils.getCellValue( row, RU_REGION );
        if ( region != null ) {
            address.setSubject( findSpecialSubject( region, subjects ) );
        }
        address.setPostIndex( ServiceUtils.getCellValue( row, POST_CODE ) );
        address.setArea( ServiceUtils.getCellValue( row, LOCAL_AREA ) );
        address.setCity( ServiceUtils.getCellValue( row, CITY ) );
        if ( address.getCity() != null && address.getSubject() != null
                && address.getCity().equalsIgnoreCase( address.getSubject().getName() ) ) {
            address.setCity( "" );  // clear city if city name equals to region name
        }
        short startStreetCell = WILLAGE;
        if ( address.getCity() == null || address.getCity().trim().length() == 0 ) {
            address.setCity( ServiceUtils.getCellValue( row, WILLAGE ) );
            startStreetCell = STREET;
        }
        StringBuilder street = new StringBuilder();
        for ( short i = startStreetCell; i <= APPARTMENT; i++) {
            String tmp = ServiceUtils.getCellValue( row, i );
            if ( tmp != null && tmp.trim().length() > 0 ) {
                switch ( i ) {
                    case BUILDING:  // house
                        street.append( "д." );
                        break;
                    case FACILITY:
                        street.append( "корп." );
                        break;
                    case APPARTMENT:  //
                        street.append( "кв." );
                        break;
                }
                street.append( tmp );
                street.append( " " );
            }
        }

        address.setStreet( street.toString().trim() );
        address.setComment( ServiceUtils.getCellValue( row, ADDRESS_COMMENT ) );

        return address;
    }

    public void readEduDocument( HSSFRow row, List<EduDocumentType> eduDocumentTypes ) {
        if ( !freshStudent ) {
            eduDocument = dao.findEduDocument( student );
        }
        if ( eduDocument == null ) {
            eduDocument = new EduDocument();
            eduDocument.setStudent( student );
        }
        String docType = ServiceUtils.getCellValue( row, EDU_DOC_TYPE );
        eduDocument.setType( findDocumentType( docType, eduDocumentTypes ) );

        eduDocument.setSeria( ServiceUtils.getCellValue( row, EDU_DOC_SERIA ) );
        eduDocument.setNumber( ServiceUtils.getCellValue( row, EDU_DOC_NUMBER ) );
        eduDocument.setEduOrganization( ServiceUtils.getCellValue( row, EDU_ORG_NAME ) );
        eduDocument.setSpecialMark( ServiceUtils.getCellValue( row, ACHIEVEMENT_LEVEL ) );
        try {
            eduDocument.setDate( ServiceUtils.YEAR_FORMAT.parse( ServiceUtils.getCellValue( row, GRADUATION_DATE ) ) );
        } catch (ParseException e) {
            //logger.error( "Document year", e );
        }

    }

    private EduDocumentType findDocumentType( String docType, List<EduDocumentType> eduDocumentTypes ) {
        for ( EduDocumentType type : eduDocumentTypes ) {
            if ( ServiceUtils.includeIn( docType, type.getName(), true ) ) return type;
        }
        return null;
    }


    public void readBenefit( HSSFRow row ) {
        BenefitType type = BenefitType.findBenefitType( ServiceUtils.getCellValue( row, BENEFIT_TYPE ) );
        if ( type != null ) {
            if ( !freshStudent ) {
                benefit = dao.findBenefit( student );
            }
            if ( benefit == null ) {
                benefit = new PersonBenefit();
                benefit.setStudent( student );
            }
            benefit.setType( type.getId() );
            benefit.setDocumentName( ServiceUtils.getCellValue( row, BENEFIT_DOC_TYPE ) );
            benefit.setSeria( ServiceUtils.getCellValue( row, BENEFIT_DOC_SERIA ) );
            benefit.setNumber( ServiceUtils.getCellValue( row, BENEFIT_DOC_NUMBER ) );
            benefit.setIssuedBy( ServiceUtils.getCellValue( row, BENEFIT_DOC_ISSUED_BY ) );
            if ( benefit.getSeria().length() > 20 ) {
                benefit.setSeria( benefit.getSeria().substring( 0, 19 ) );
            }
            if ( benefit.getNumber().length() > 20 ) {
                benefit.setNumber( benefit.getNumber().substring( 0, 19 ) );
            }
            if ( benefit.getIssuedBy().length() > 255 ) {
                benefit.setIssuedBy( benefit.getIssuedBy().substring( 0, 254 ) );
            }
        }

    }

    public void readLanguages( HSSFRow row, List<ForeignLanguage> foreignLanguages ) {
        languages = new ArrayList<>();
        String languageSource = ServiceUtils.getCellValue( row, FOREIGN_LANGUAGE );
        String levelSource = ServiceUtils.getCellValue( row, LANGUAGE_LEVEL );

        if ( languageSource != null && levelSource != null ) {
            String[] languagesStr = languageSource.split( ", " );
            String[] levelStr = levelSource.split( ", " );
            for ( int i = 0; i < languagesStr.length; i++  ) {
                ForeignLanguage language = findLanguage( languagesStr[i], foreignLanguages );
                if ( language == null ) continue;
                ForeignLanguageLevel level = ForeignLanguageLevel.READ_WITH_DICT;;
                if ( i < levelStr.length ) {
                    level = ForeignLanguageLevel.findLevel( levelStr[i] );
                }
                StudentLanguage studentLanguage = null;
                if ( !freshStudent ) {
                    studentLanguage = dao.findStudentLanguage( student, language );
                }
                if ( studentLanguage == null ) {
                    studentLanguage = new StudentLanguage();
                    studentLanguage.setStudent( student );
                    studentLanguage.setLanguage( language );
                }
                studentLanguage.setLevel( level.getId() );

                languages.add( studentLanguage );
            }
        }
    }

    private ForeignLanguage findLanguage( String name, List<ForeignLanguage> foreignLanguages ) {
        for ( ForeignLanguage language : foreignLanguages ) {
            if ( language.getName().contains( name ) ) return language;
        }
        return null;
    }
}
