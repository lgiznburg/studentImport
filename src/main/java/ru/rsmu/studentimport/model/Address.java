package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_address", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Address implements Serializable {
    private static final long serialVersionUID = 1705427441042181186L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_l_person")
    private Student student;

    @Column(name = "id_s_address_type")
    private int addressType = 2;  //registration address

    @ManyToOne
    @JoinColumn(name = "id_s_oksm")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "id_s_subjekt")
    private SpecialSubject subject;             // RUS region

    @Column(name = "post_index")
    private String postIndex;

    @Column(name = "area")
    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "street_house")
    private String street;

    @Column(name = "address_comment")
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

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType( int addressType ) {
        this.addressType = addressType;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry( Country country ) {
        this.country = country;
    }

    public SpecialSubject getSubject() {
        return subject;
    }

    public void setSubject( SpecialSubject subject ) {
        this.subject = subject;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex( String postIndex ) {
        this.postIndex = postIndex;
    }

    public String getArea() {
        return area;
    }

    public void setArea( String area ) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }
}
