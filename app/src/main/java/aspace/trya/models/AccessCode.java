package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessCode {

    @JsonProperty("access_code")
    private String accessCode;

    @JsonProperty("expiry")
    private String expiry;

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
        return
                "AccessCode{" +
                        "access_code = '" + accessCode + '\'' +
                        ",expiry = '" + expiry + '\'' +
                        "}";
    }
}