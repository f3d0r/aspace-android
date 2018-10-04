package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class AccessCode implements Serializable {

    @JsonProperty("access_code")
    public String accessCode;

    @JsonProperty("expiry")
    public String expiry;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "AccessCode{" +
            "accessCode='" + accessCode + '\'' +
            ", expiry='" + expiry + '\'' +
            '}';
    }
}