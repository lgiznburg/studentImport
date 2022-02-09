package ru.rsmu.studentimport.model;

/**
 * @author leonid.
 */
public enum ForeignLanguageLevel {
    FREE(6, "Владеет свободно"),
    READ_N_SPEAK(3, "Читает и может объясняться"),
    READ_WITH_DICT(1002, "Читает и переводит со словарем");

    private int id;

    private String title;

    ForeignLanguageLevel( int id, String title ) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    static public ForeignLanguageLevel findLevel( String levelName ) {
        for ( ForeignLanguageLevel level : values() ) {
            if ( level.getTitle().equals( levelName.trim() ) ) return level;
        }
        return READ_WITH_DICT;
    }
}
