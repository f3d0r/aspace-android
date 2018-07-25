package aspace.trya.BodyObjects;

public class LngLat {

    private double lng;

    private double lat;

    public LngLat(String lngLat) {
        this.lng = Double.parseDouble(lngLat.substring(0, lngLat.indexOf('.')));
        this.lat = Double.parseDouble((lngLat.substring(lngLat.indexOf('.') + 1, lngLat.length())));
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return
                "Ne{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}