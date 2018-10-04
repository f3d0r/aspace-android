package aspace.trya.models.osrm_directions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata implements Serializable {

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