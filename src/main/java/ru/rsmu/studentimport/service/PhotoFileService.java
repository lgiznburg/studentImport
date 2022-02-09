package ru.rsmu.studentimport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.rsmu.studentimport.model.PersonPhoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author leonid.
 */
@Component
public class PhotoFileService {
    protected Logger logger = LoggerFactory.getLogger( PhotoFileService.class );

    private String sourceStorage = "";  // path to source directory
    private String destStorage = "";    // path to destination directory

    public String getSourceStorage() {
        return sourceStorage;
    }

    public void setSourceStorage( String sourceStorage ) {
        this.sourceStorage = sourceStorage;
    }

    public String getDestStorage() {
        return destStorage;
    }

    public void setDestStorage( String destStorage ) {
        this.destStorage = destStorage;
    }

    public boolean isSourceCorrect() {
        if ( StringUtils.isEmpty( sourceStorage ) ) return false;
        File file = new File(sourceStorage);
        return file.exists() && file.isDirectory();
    }

    public boolean isDestCorrect() {
        if ( StringUtils.isEmpty( destStorage ) ) return false;
        File file = new File( destStorage );
        return file.exists() && file.isDirectory();
    }

    public PersonPhoto createImages( String sourceName ) {
        try {
            File source = new File( sourceStorage + File.separator + sourceName );
            if ( !source.exists() ) {
                return null;
            }
            String[] nameParts = sourceName.split( "\\." );
            String clearSourceName = "";
            if ( nameParts.length >= 2 ) {
                clearSourceName = sourceName.replace( "."+ nameParts[ nameParts.length -1 ], "" );
            }

            String nameNew = UUID.randomUUID().toString();

            File dest = new File( destStorage + File.separator + nameNew + ".jpg" );

            Files.copy( source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING );  // copy image

            BufferedImage src = ImageIO.read( source );
            int type = src.getType() == 0? BufferedImage.TYPE_INT_ARGB : src.getType();

            BufferedImage thumb = new BufferedImage( 60, 80, type );

            thumb.getGraphics().drawImage( src.getScaledInstance( 60, 80, Image.SCALE_SMOOTH ), 0, 0, null );
            // tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_AREA_AVERAGING), 0, 0, null);

            FileOutputStream out = new FileOutputStream(destStorage + File.separator + nameNew + "_thumb.jpg");
            ImageIO.write( thumb, "jpg", out );
            out.close();

            PersonPhoto personPhoto = new PersonPhoto();

            personPhoto.setFileName( clearSourceName );
            personPhoto.setFileNameNew( nameNew );
            personPhoto.setFileSize( source.length() );

            return personPhoto;

        } catch (IOException e) {
            logger.error( "Can't create images", e );
        }

        return null;
    }

    // this code found on internet - https://www.mkyong.com/java/how-to-resize-an-image-in-java/
/*
    public static void reduceImg(String imgsrc, String imgdist,String type, int widthdist,
                                 int heightdist) {
        try {
            File srcfile = new File(imgsrc);
            if (!srcfile.exists()) {
                return;
            }
            BufferedImage src = ImageIO.read(srcfile);

            BufferedImage tag= new BufferedImage( widthdist, heightdist, BufferedImage.TYPE_INT_RGB);

            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
            // tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_AREA_AVERAGING), 0, 0, null);

            FileOutputStream out = new FileOutputStream(imgdist);
            ImageIO.write( tag, type, out );
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
*/
}
