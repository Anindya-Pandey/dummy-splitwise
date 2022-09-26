package com.anindya.dummysplitwise.controllers.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseResponse {
    private Object data;
    private String error;

    public BaseResponse(Object data, String error) {
        this.data = data;
        this.error = error;
    }
}
