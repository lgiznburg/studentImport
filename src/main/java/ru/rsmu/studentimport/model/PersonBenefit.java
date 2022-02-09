package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_benefit", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PersonBenefit implements Serializable {
    private static final long serialVersionUID = -3581743304158841036L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @Column(name = "id_s_person_benefit")
    private int type ;

    @Column(name = "doc_benefit")
    private String documentName;

    @Column(name = "series_doc_benefit")
    private String seria;

    @Column(name = "number_doc_benefit")
    private String number;

    @Column(name = "org_benefit")
    private String issuedBy;

    @Column(name = "comment_benefit")
    private String comment;

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

    public int getType() {
        return type;
    }

    public void setType( int type ) {
        this.type = type;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName( String documentName ) {
        this.documentName = documentName;
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

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy( String issuedBy ) {
        this.issuedBy = issuedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }
}
