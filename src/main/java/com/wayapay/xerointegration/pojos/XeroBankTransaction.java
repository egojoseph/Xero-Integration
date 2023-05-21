package com.wayapay.xerointegration.pojos;

import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransaction {
    private Contacts Contact;
    private String DateString;
    private String Date;
    private String Status;
    private String LineAmountTypes;
    private List<com.wayapay.xerointegration.pojos.LineItems> LineItems;
    private String SubTotal;
    private String TotalTax;
    private String Total;
    private String UpdatedDateUTC;
    private String CurrencyCode;
    private String BankTransactionID;
    private com.wayapay.xerointegration.pojos.BankAccount BankAccount;
    private String Type;
    private String Reference;
    private String IsReconciled;
}
