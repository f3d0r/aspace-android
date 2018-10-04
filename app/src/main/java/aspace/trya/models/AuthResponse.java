package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class AuthResponse implements Serializable {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private AccessCode accessCode;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public AccessCode getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(AccessCode accessCode) {
        this.accessCode = accessCode;
    }

    public int getResponseCode() {
        return responseInfo.getCode();
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
            "responseInfo=" + responseInfo +
            ", accessCode=" + accessCode +
            '}';
    }
}