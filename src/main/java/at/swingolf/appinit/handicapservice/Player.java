package at.swingolf.appinit.handicapservice;

public class Player {
    private String license;
    private String firstname;
    private String lastname;
    private double handicap;
    private String category;

    public Player(String license, String firstname, String lastname, double handicap, String category) {
        this.license = license;
        this.firstname = firstname;
        this.lastname = lastname;
        this.handicap = handicap;
        this.category = category;
    }

    public String getLicense() {
        return license;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public double getHandicap() {
        return handicap;
    }

    public String getCategory() {
        return category;
    }
}
