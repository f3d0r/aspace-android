package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;

public class AccessCode extends RealmObject {

    @JsonProperty("access_code")
    public String accessCode;

    @JsonProperty("expiry")
    public String expiry;

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

    public AccessCode() {
    }

    @Override
    public String toString() {
        return "AccessCode{" +
                "accessCode='" + accessCode + '\'' +
                ", expiry='" + expiry + '\'' +
                '}';
    }
}