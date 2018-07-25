package aspace.trya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInfo {

    @JsonProperty("missing_parameter")
    private String missingParameter;

    @JsonProperty("code")
    private int code;

    @JsonProperty("code_info")
    private String codeInfo;

    public String getMissingParameter() {
        return missingParameter;
    }

    public void setMissingParameter(String missingParameter) {
        this.missingParameter = missingParameter;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    @Override
    public String toString() {
        return
                "ResInfo{" +
                        "missing_parameter = '" + missingParameter + '\'' +
                        ",code = '" + code + '\'' +
                        ",code_info = '" + codeInfo + '\'' +
                        "}";
    }
}