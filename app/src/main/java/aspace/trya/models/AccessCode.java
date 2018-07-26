package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessCode {

    @JsonProperty("access_code")
    private String accessCode;

    @JsonProperty("expiry")
    private String expiry;

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getExpiry() {
        return expiry;
    }

    @Override
    public String toString() {
        return
                "ResContent{" +
                        "access_code = '" + accessCode + '\'' +
                        ",expiry = '" + expiry + '\'' +
                        "}";
    }
}