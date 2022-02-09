package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Student implements Serializable {

    private static final long serialVersionUID = 7440894638313487004L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fam")
    private String lastName;

    @Column(name = "im")
    private String firstName;

    @Column(name = "ot")
    private String middleName;

    @Column(name = "id_s_gender")
    private int gender;

    @Column(name = "email")
    private String email;

    @Column(name = "m_phone")
    private String mobilePhone;

    @Column(name = "d_phone")
    private String homePhone;

    @Column(name = "k_phone")
    private String contactPhone;

    @Column(name = "birthdate")
    @Temporal( TemporalType.DATE )
    private Date birthDate;

    @Column(name = "birthplace")
    private String birthPalce;

    @ManyToOne
    @JoinColumn(name = "id_s_oksm")
    private Country country;

    @Column(name = "snils")
    private String snils;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName( String middleName ) {
        this.middleName = middleName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender( int gender ) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone( String mobilePhone ) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone( String homePhone ) {
        this.homePhone = homePhone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( Date birthDate ) {
        this.birthDate = birthDate;
    }

    public String getBirthPalce() {
        return birthPalce;
    }

    public void setBirthPalce( String birthPalce ) {
        this.birthPalce = birthPalce;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry( Country country ) {
        this.country = country;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone( String contactPhone ) {
        this.contactPhone = contactPhone;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils( String snils ) {
        this.snils = snils;
    }

    public void update( Student tempStudent ) {
        gender = tempStudent.getGender();
        email = tempStudent.getEmail();
        mobilePhone = tempStudent.getMobilePhone();
        homePhone = tempStudent.getHomePhone();
        birthPalce = tempStudent.getBirthPalce();
        country = tempStudent.getCountry();
        contactPhone = tempStudent.getContactPhone();
    }
}
