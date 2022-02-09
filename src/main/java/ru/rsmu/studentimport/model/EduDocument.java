package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_educationdoc", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class EduDocument implements Serializable {
    private static final long serialVersionUID = -8041410813096274090L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_s_edudocperson_type")
    private EduDocumentType type;

    @Column(name = "edudoc_series")
    private String seria;

    @Column(name = "edudoc_number")
    private String number;

    @Column(name = "edudoc_date")
    @Temporal( TemporalType.DATE )
    private Date date;

    @Column(name = "edudoc_organization")
    private String eduOrganization;

    @Column(name = "otlichie")
    private String specialMark;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent( Student student ) {
        this.student = student;
    }

    public EduDocumentType getType() {
        return type;
    }

    public void setType( EduDocumentType type ) {
        this.type = type;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria( String seria ) {
        this.seria = seria;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber( String number ) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( Date date ) {
        this.date = date;
    }

    public String getEduOrganization() {
        return eduOrganization;
    }

    public void setEduOrganization( String eduOrganization ) {
        this.eduOrganization = eduOrganization;
    }

    public String getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark( String specialMark ) {
        this.specialMark = specialMark;
    }
}
