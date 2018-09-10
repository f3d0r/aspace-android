package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessCode {

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