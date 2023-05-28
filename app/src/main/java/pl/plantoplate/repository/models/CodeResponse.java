package pl.plantoplate.repository.models;

import com.google.gson.annotations.SerializedName;

public class CodeResponse {

    @SerializedName("code")
    private String code;

    public CodeResponse() {
    }

    public CodeResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
