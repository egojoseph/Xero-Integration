package com.wayapay.xerointegration.payload.request;

import com.wayapay.xerointegration.pojos.Param;
import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransactionRequestPayload
{
    List<Param> filters;
    int page = 0;
}
