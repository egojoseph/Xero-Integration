package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroBankTransactionResponsePayload extends WayaBankBaseResponsePayload
{
    private List<XeroBankTransaction> data;
}
