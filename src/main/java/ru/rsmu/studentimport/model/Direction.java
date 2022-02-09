package ru.rsmu.studentimport.model;

/**
 * @author leonid.
 */
public enum Direction {
    PEDIATRIA(1, "31.05.02 Педиатрия"),
    PSYCHO(2 ,"37.05.01 Клиническая психология"),
    MEDICAL(3 ,"31.05.01 Лечебное дело"),
    SOCIAL(4 ,"39.03.02 Социальная работа"),
    BIO_CHEMI(5 ,"30.05.01 Медицинская биохимия"),
    BIO_PHYSIC(6 ,"30.05.02 Медицинская биофизика"),
    CYBERNETIC(7 ,"30.05.03 Медицинская кибернетика"),
    ORDINATOR(8 ,"Ординатура"),
    STOMAT(9 ,"31.05.03 Стоматология"),
    PHARMA(10 ,"33.05.01 Фармация"),
    BIO(11 ,"06.03.01 Биология"),
    ANGLOPHONE(300 ,"31.05.01 Лечебное дело"),
    M_PSYCHO(14,"37.04.01 Психология"),
    M_BIO(13,"06.04.01 Биология"),
    M_SOCIAL(12, "39.04.02 Социальная работа"),
    ANGLO_STOMAT(301 ,"31.05.03 Стоматология");

    private long id;

    private String title;

    Direction( long id, String title ) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    static public Direction findDirection(String name) {
        for ( Direction dir : values() ) {
            if ( dir.getTitle().equals( name ) ) return dir;
        }
        return null;
    }
}











