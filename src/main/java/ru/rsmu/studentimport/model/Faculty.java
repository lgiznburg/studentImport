package ru.rsmu.studentimport.model;

/**
 * @author leonid.
 */
public enum Faculty {
    PSYCHO(1,"Клинической психологии и социальной работы"),
    MEDICAL(3, "Лечебный"),
    PEDIATRIC(4, "Педиатрический"),
    MED_BIO(5, "Медико-биологический"),
    FOREIGN(10, "Международный"),
    STOMAT(8, "Стоматологический");

    private long id;

    private String title;

    Faculty( long id, String title ) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    static public Faculty findFaculty( String name ) {
        for ( Faculty faculty : values() ) {
            if ( faculty.getTitle().equals( name ) ) return faculty;
        }
        return null;
    }
}
