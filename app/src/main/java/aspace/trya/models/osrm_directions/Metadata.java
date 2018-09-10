package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Metadata {

    @JsonProperty("datasource_names")
    private List<String> datasourceNames;

    public List<String> getDatasourceNames() {
        return datasourceNames;
    }

    public void setDatasourceNames(List<String> datasourceNames) {
        this.datasourceNames = datasourceNames;
    }

    @Override
    public String toString() {
        return
            "Metadata{" +
                "datasource_names = '" + datasourceNames + '\'' +
                "}";
    }
}