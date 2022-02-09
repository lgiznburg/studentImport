package ru.rsmu.studentimport.model;

/**
 * @author leonid.
 */
public enum CountryMatch {
    RUS("РФ","RUS"),
    KAZ("РК","KAZ"),
    BLR("РБ","BLR"),
    KGZ("КР","KGZ"),
    TJK("РТ","TJK"),
    TKM("Т","TKM"),
    UZB("РУ","UZB"),
    UKR("У","UKR"),
    OST("РЮО","OST"),
    ABH("РАб","ABH"),
    AZE("РАз","AZE"),
    ARM("РАр","ARM"),
    GEO("Г","GEO"),
    MDA("РМ","MDA");

    private String ruCode;

    private String alpha3;

    CountryMatch( String ruCode, String alpha3 ) {
        this.ruCode = ruCode;
        this.alpha3 = alpha3;
    }

    public String getRuCode() {
        return ruCode;
    }

    public void setRuCode( String ruCode ) {
        this.ruCode = ruCode;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public void setAlpha3( String alpha3 ) {
        this.alpha3 = alpha3;
    }

    public static CountryMatch findMatch( String countryName ) {
        for ( CountryMatch match : values() ) {
            if ( match.getRuCode().equals( countryName ) ) return match;
        }
        return null;
    }
}
