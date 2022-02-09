package ru.rsmu.studentimport.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "l_person_photo", schema = "public")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PersonPhoto implements Serializable {
    private static final long serialVersionUID = 6829247316367438579L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_master")
    private Student student;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType = "image/jpeg";

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "file_name_new")
    private String fileNameNew;

    @Column(name = "file_extension")
    private String fileExtension = "jpg";

    @Column(name = "fd_filetype")
    private int fdFileType = 1;

    @Column(name = "is_thumb")
    private int thumb = 1;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType( String fileType ) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize( long fileSize ) {
        this.fileSize = fileSize;
    }

    public String getFileNameNew() {
        return fileNameNew;
    }

    public void setFileNameNew( String fileNameNew ) {
        this.fileNameNew = fileNameNew;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension( String fileExtension ) {
        this.fileExtension = fileExtension;
    }

    public int getFdFileType() {
        return fdFileType;
    }

    public void setFdFileType( int fdFileType ) {
        this.fdFileType = fdFileType;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb( int thumb ) {
        this.thumb = thumb;
    }
}
