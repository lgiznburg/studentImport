package ru.rsmu.studentimport.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rsmu.studentimport.model.PersonPhoto;
import ru.rsmu.studentimport.service.PhotoFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("checkStorage.htm")
/**
 * Используется, чтобы установить и проверить пути, где расположены исходные файлы с фотографиями и куда будут записаны
 * обработанные фото и уменьшенные иконки
 */
public class CheckStorage {

    protected Logger logger = LoggerFactory.getLogger( LoadStudents.class );

    public static final String showPage = "/WEB-INF/pages/CheckStorage.jsp";

    @Autowired
    private PhotoFileService photoFileService;

    @RequestMapping(method = RequestMethod.GET)
    public String onShowPage() {
        return showPage;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmitPage( ModelMap modelMap,
                                @RequestParam(value = "sourceDir", required = false) String sourceDir,
                                @RequestParam(value = "destDir", required = false) String destDir,
                                @RequestParam(value = "sourceFile", required = false) String sourceFile) {
        if ( !StringUtils.isEmpty( sourceDir ) ) {
            photoFileService.setSourceStorage( sourceDir );
        }
        if ( !StringUtils.isEmpty( destDir ) ) {
            photoFileService.setDestStorage( destDir );
        }
        if ( !StringUtils.isEmpty( sourceFile ) ) {
            //try to copy file
            PersonPhoto photo = photoFileService.createImages( sourceFile );
            if ( photo == null ) {
                modelMap.addAttribute( "message", "Cant create images" );
            }
            else {
                modelMap.addAttribute( "message", "Success!" );
            }
        }

        return showPage;
    }
    @ModelAttribute("photoFileService")
    public PhotoFileService getPhotoFileService() {
        return photoFileService;
    }

    @ModelAttribute("filesList")
    public String[] getFilesList( Model model,
                                @RequestParam(value = "sourceDir", required = false) String sourceDir ) {
        if ( photoFileService.isSourceCorrect() ) {
            File file = new File( photoFileService.getSourceStorage() );

            return Arrays.copyOfRange( file.list(), 0, 10 );
        }
        return new String[]{};
    }

}
