package aspace.trya.models.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Lane implements Serializable {

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("indications")
    private List<String> indications;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setIndications(List<String> indications) {
        this.indications = indications;
    }

    public List<String> getIndications() {
        return indications;
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