package aspace.trya.api;

public class WaypointNames {
    String originName;
    String destinationName;

    public WaypointNames(String originName, String destinationName) {
        this.originName = originName;
        this.destinationName = destinationName;
    }

    @Override
    public String toString() {
        return originName + ";" + destinationName;
    }
}
