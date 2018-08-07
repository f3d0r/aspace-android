package aspace.trya.realm;

import aspace.trya.models.AccessCode;
import io.realm.RealmObject;

public class AppUser extends RealmObject {
    public String phoneNumber;
    public AccessCode accessCode;
    public String deviceId;

    public AppUser() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AccessCode getAccessCode() {
        return accessCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", accessCode=" + accessCode +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
