package com.wayapay.xerointegration.dto.xero.response;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UploadErrorResponse {
    public ArrayList<XeroError> error;
}
