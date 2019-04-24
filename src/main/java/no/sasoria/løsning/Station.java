package no.sasoria.løsning;

public class Station {
    private int id;
    private String name;
    private int bikesAvailable;
    private int locksAvailable;

    public Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public void setBikesAvailable(int bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public int getLocksAvailable() {
        return locksAvailable;
    }

    public void setLocksAvailable(int locksAvailable) {
        this.locksAvailable = locksAvailable;
    }
}
