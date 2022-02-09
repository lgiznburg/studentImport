package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_language", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class StudentLanguage implements Serializable {
    private static final long serialVersionUID = -2382594839783506292L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_s_language")
    private ForeignLanguage language;

    @Column(name = "id_s_langlevel")
    private int level;

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

    public ForeignLanguage getLanguage() {
        return language;
    }

    public void setLanguage( ForeignLanguage language ) {
        this.language = language;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel( int level ) {
        this.level = level;
    }
}
