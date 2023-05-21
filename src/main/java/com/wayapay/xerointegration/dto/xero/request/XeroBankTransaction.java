package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroBankTransaction {
    private Contacts Contact;
    private String DateString;
    private String Date;
    private String Status;
    private String LineAmountTypes;
    private List<LineItems> LineItems;
    private String SubTotal;
    private String TotalTax;
    private String Total;
    private String UpdatedDateUTC;
    private String CurrencyCode;
    private String BankTransactionID;
    private BankAccount BankAccount;
    private String Type;
    private String Reference;
    private String IsReconciled;
}
