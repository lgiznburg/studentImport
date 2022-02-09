package ru.rsmu.studentimport.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.rsmu.studentimport.service.LoadStudentsService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("loadStudents.htm")
public class LoadStudents {
    protected Logger logger = LoggerFactory.getLogger( LoadStudents.class );

    @Autowired
    LoadStudentsService loadStudentsService;

    public static final String showPage = "/WEB-INF/pages/LoadStudents.jsp";

    @RequestMapping(method = RequestMethod.GET)
    public String onShowPage() {
        return showPage;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmitPage( HttpServletRequest request, ModelMap model,
                                @RequestParam(value = "firstRecord", required = false, defaultValue = "0") Integer startFrom,
                                @RequestParam(value = "totalRecords", required = false, defaultValue = "0") Integer totals,
                                @RequestParam(value = "anglophones", required = false, defaultValue = "false") Boolean anglophones,
                                @RequestParam(value = "doPhotos", required = false, defaultValue = "false") Boolean doPhotos) {

        try {
            if ( request instanceof MultipartHttpServletRequest ) {
                MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
                if ( multipart.getFileMap().containsKey( "studentsFile" ) && !multipart.getFile( "studentsFile" ).isEmpty() ) {
                    MultipartFile file = multipart.getFile( "studentsFile" );
                    if ( file.getOriginalFilename().matches( ".*\\.xls" ) ) {
                        List<String> messages = loadStudentsService.readFromFile( file.getInputStream(),
                                startFrom, totals, anglophones, doPhotos );
                        model.put( "messages", messages );
                    }
                }
            }
        } catch ( IOException e ) {
            logger.error( "Can't upload information from Excel file", e );
        }

        return showPage;
    }
}
