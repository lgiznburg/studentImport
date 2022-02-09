package ru.rsmu.studentimport.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 * Year of enrollment
 */
@Entity
@Table(name = "s_recruitment", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Recruitment implements Serializable {

    private static final long serialVersionUID = 1419184321058151387L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long year1;

    @Column
    private String name;

    @Column(name = "is_active",columnDefinition = "smallint", nullable = true)
    @Type( type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getYear1() {
        return year1;
    }

    public void setYear1( long year1 ) {
        this.year1 = year1;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive( Boolean active ) {
        this.active = active;
    }
}
