package ru.rsmu.studentimport.service;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author leonid.
 */
public class ServiceUtils {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd.MM.yyyy" );
    public static final DateFormat YEAR_FORMAT = new SimpleDateFormat( "yyyy" );
    public static final Locale RU_LOCALE = Locale.forLanguageTag( "ru_RU" );

    public static String getCellValue( HSSFRow row, short cellN ) {
        HSSFCell cell = row.getCell( cellN );
        if ( cell != null ) {
            String value;
            switch ( cell.getCellType() ) {
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString().trim();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    value = Long.toString( Math.round( cell.getNumericCellValue() ) );
                    break;
                default:
                    return null;
            }
            return value;
        }
        return null;
    }

    public static boolean includeIn( String bigStr, String smallStr ) {
        return includeIn( bigStr, smallStr, false );
    }

    public static boolean includeIn( String bigStr, String smallStr, boolean strict ) {
        String[] parts = smallStr.split( "[\\- ]" );
        int factor = parts.length;
        int score = 0;
        for ( String part : parts ) {
            if ( part.trim().length() == 0 ) {
                factor--;
                continue;
            }
            if ( bigStr.toLowerCase( RU_LOCALE ).contains( part.trim().toLowerCase( RU_LOCALE ) ) ) score++;
        }
        if ( score == factor || ( !strict && factor > 1 && score >= 1 )  ) {
            return true;
        }
        return false;
    }

}
