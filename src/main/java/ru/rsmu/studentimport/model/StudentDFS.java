package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_dfs", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class StudentDFS implements Serializable {
    public static final long FINANCE_BUDGET=1;
    public static final long FINANCE_CONTRACT=2;
    private static final long serialVersionUID = 657976663674070373L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_s_direction_form_sem")
    private FacultyProgram facultyProgram;

    @ManyToOne
    @JoinColumn(name = "id_s_recruitment")
    private Recruitment recruitment;

    @Column(name = "id_s_edu_finance")
    // type of finance
    private long finance;

    @ManyToOne
    @JoinColumn(name = "id_s_subjekt_target")
    private SpecialSubject subject;

    @Column(name = "id_s_studentstatus")
    private long status = 1;

    @Column(name = "n_dela")
    private String caseNumber;

    @ManyToOne
    @JoinTable(name = "l_order_persondfs", schema = "public",
            joinColumns = { @JoinColumn(name = "id_l_persondfs")},
            inverseJoinColumns = { @JoinColumn(name = "id_l_order")}
    )
    private OrderInfo orderInfo;

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

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public void setRecruitment( Recruitment recruitment ) {
        this.recruitment = recruitment;
    }

    public long getFinance() {
        return finance;
    }

    public void setFinance( long finance ) {
        this.finance = finance;
    }

    public SpecialSubject getSubject() {
        return subject;
    }

    public void setSubject( SpecialSubject subject ) {
        this.subject = subject;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus( long status ) {
        this.status = status;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber( String caseNumber ) {
        this.caseNumber = caseNumber;
    }

    public FacultyProgram getFacultyProgram() {
        return facultyProgram;
    }

    public void setFacultyProgram( FacultyProgram facultyProgram ) {
        this.facultyProgram = facultyProgram;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo( OrderInfo orderInfo ) {
        this.orderInfo = orderInfo;
    }
}
