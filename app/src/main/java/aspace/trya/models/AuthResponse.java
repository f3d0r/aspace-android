package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {

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

    @Override
    public String toString() {
        return
                "AuthResponse{" +
                        "res_info = '" + responseInfo + '\'' +
                        ",res_content = '" + accessCode + '\'' +
                        "}";
    }
}