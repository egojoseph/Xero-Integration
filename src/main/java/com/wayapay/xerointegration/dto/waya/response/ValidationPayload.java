package com.wayapay.xerointegration.dto.waya.response;

import lombok.Data;

@Data
public class ValidationPayload
{
    private boolean hasError;
    private String errorJson;
    private Object extraItem;
    private ValidationPayload(){}
    public static ValidationPayload getInstance(){
        return new ValidationPayload();
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtraItem(){
        return (T)this.extraItem;
    }
}
