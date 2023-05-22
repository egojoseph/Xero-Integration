package com.wayapay.xerointegration.dto.xero.response;

import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransactionResponseData
{
    private String Id;
    private String Status;
    private String ProviderName;
    private String DateTimeUTC;
    private List<XeroBankTransaction> BankTransactions;
}
