package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_document", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class IdDocument implements Serializable {
    private static final long serialVersionUID = 6635953120270389185L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @Column(name = "doc_series")
    private String seria;


    @Column(name = "doc_number")
    private String number;

    @Column(name = "doc_date")
    @Temporal( TemporalType.DATE )
    private Date date;

    @Column(name = "doc_organization")
    private String issuedBy;

    @Column(name = "organization_code")
    private String issuedByCode;

    @ManyToOne
    @JoinColumn(name = "id_s_docperson_type")
    private IdDocumentType type;

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

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy( String issuedBy ) {
        this.issuedBy = issuedBy;
    }

    public IdDocumentType getType() {
        return type;
    }

    public void setType( IdDocumentType type ) {
        this.type = type;
    }

    public String getIssuedByCode() {
        return issuedByCode;
    }

    public void setIssuedByCode( String issuedByCode ) {
        this.issuedByCode = issuedByCode;
    }
}
