package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class CodeResponse implements Serializable {

    @JsonProperty("res_info")
    private ResponseInfo resInfo;

    public ResponseInfo getResInfo() {
        return resInfo;
    }

    public void setResInfo(ResponseInfo resInfo) {
        this.resInfo = resInfo;
    }

    @Override
    public String toString() {
        return
            "CodeResponse{" +
                "res_info = '" + resInfo + '\'' +
                "}";
    }
}