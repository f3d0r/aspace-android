package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Lane implements Serializable {

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("indications")
    private List<String> indications;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getIndications() {
        return indications;
    }

    public void setIndications(List<String> indications) {
        this.indications = indications;
    }

    @Override
    public String toString() {
        return
                "Lane{" +
                        "valid = '" + valid + '\'' +
                        ",indications = '" + indications + '\'' +
                        "}";
    }
}