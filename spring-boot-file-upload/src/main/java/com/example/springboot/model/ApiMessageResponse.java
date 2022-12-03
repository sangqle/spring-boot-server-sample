package com.example.springboot.model;

import com.example.springboot.enums.ApiMessageCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import lombok.Data;

@Data
@NoArgsConstructor
public class ApiMessageResponse {
    @JsonProperty(value = "err")
    private int error;
    private String msg;
    private Object data;

    public ApiMessageResponse(ApiMessageCode apiMessage) {
        error = apiMessage.getError();
        msg = apiMessage.getMsg();
    }

    public void setApiMessageCode(ApiMessageCode code) {
        this.error = code.getError();
        this.msg = code.getMsg();
    }

    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();
        jObj.put("msg", this.msg);
        jObj.put("error", this.error);
        return jObj;
    }
}
