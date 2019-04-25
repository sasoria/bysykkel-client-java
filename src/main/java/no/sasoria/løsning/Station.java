package no.sasoria.løsning;

public class Station {
    private String name;
    private int bikesAvailable;
    private int locksAvailable;

    public Station(String name, int bikesAvailable, int locksAvailable) {
        this.name = name;
        this.bikesAvailable = bikesAvailable;
        this.locksAvailable = locksAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Station: " + getName() + "\n")
                .append("\t-bikes available: " + getBikesAvailable() + "\n")
                .append("\t-locks available: " + getLocksAvailable() + "\n");
        return builder.toString();
    }
}
