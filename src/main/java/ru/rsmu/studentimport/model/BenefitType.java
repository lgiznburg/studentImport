package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
public enum  BenefitType {
    ORPHANS(1,"сироты"),
    INCAPABLE(2,"инвалиды"),
    RADIATION(5,"воздействие ЧАЭС");

    private int id;

    private String title;

    BenefitType( int id, String title ) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    static public BenefitType findBenefitType( String name ) {
        for ( BenefitType type : values() ) {
            if ( name != null && name.contains( type.getTitle() ) ) return type;
        }
        return null;
    }
}
