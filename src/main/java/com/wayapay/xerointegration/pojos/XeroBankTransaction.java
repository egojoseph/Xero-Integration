package com.wayapay.xerointegration.pojos;

import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransaction {
    private String type;
    private Contacts contactId;
    private List<LineItems> lineItems;
    private BankAccount bankAccount;
}
