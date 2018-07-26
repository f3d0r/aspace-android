package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {

    @JsonProperty("res_info")
    private ResponseInfo responseInfo;

    @JsonProperty("res_content")
    private AccessCode accessCode;

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "responseInfo=" + responseInfo +
                ", accessCode=" + accessCode +
                '}';
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
}