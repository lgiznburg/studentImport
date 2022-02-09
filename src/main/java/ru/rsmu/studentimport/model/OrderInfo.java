package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_order", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -2376221734439328771L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "order_date")
    @Temporal( TemporalType.DATE )
    private Date orderDate;

    @Column(name = "id_s_ordertype")
    private int orderType = 3;

    @Column(name = "id_s_orderreason")
    private int reason = 1;

    @ManyToOne
    @JoinColumn(name = "id_s_direction_form_sem_enroll")
    private FacultyProgram facultyProgram;

    @Column(name = "id_s_faculty")
    private Long faculty;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber( String orderNumber ) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate( Date orderDate ) {
        this.orderDate = orderDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType( int orderType ) {
        this.orderType = orderType;
    }

    public int getReason() {
        return reason;
    }

    public void setReason( int reason ) {
        this.reason = reason;
    }

    public FacultyProgram getFacultyProgram() {
        return facultyProgram;
    }

    public void setFacultyProgram( FacultyProgram facultyProgram ) {
        this.facultyProgram = facultyProgram;
    }

    public Long getFaculty() {
        return faculty;
    }

    public void setFaculty( long faculty ) {
        this.faculty = faculty;
    }
}
