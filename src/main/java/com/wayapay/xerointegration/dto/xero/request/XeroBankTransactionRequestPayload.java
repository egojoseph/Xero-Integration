package com.wayapay.xerointegration.dto.xero.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class XeroBankTransactionRequestPayload
{
    List<Param> filters;
    int page = 0;
}
