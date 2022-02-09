package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "s_direction_form_sem", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FacultyProgram implements Serializable {
    private static final long serialVersionUID = 5477974447469008955L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_s_faculty")
    private long faculty;

    @Column(name = "id_s_direction")
    private long direction;

    @Column(name = "id_s_formeducation")
    private int educationForm = 1;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rc_numbersuffix")
    private String suffix;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getFaculty() {
        return faculty;
    }

    public void setFaculty( long faculty ) {
        this.faculty = faculty;
    }

    public long getDirection() {
        return direction;
    }

    public void setDirection( long direction ) {
        this.direction = direction;
    }

    public int getEducationForm() {
        return educationForm;
    }

    public void setEducationForm( int educationForm ) {
        this.educationForm = educationForm;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix( String suffix ) {
        this.suffix = suffix;
    }
}
