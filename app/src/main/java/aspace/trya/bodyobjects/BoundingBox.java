package aspace.trya.bodyobjects;

public class BoundingBox {

    private LngLat sw;

    private LngLat ne;

    public LngLat getSw() {
        return sw;
    }

    public void setSw(LngLat sw) {
        this.sw = sw;
    }

    public LngLat getNe() {
        return ne;
    }

    public void setNe(LngLat ne) {
        this.ne = ne;
    }

    @Override
    public String toString() {
        return
                "BoundingBox{" +
                        "sw = '" + sw + '\'' +
                        ",ne = '" + ne + '\'' +
                        "}";
    }
}