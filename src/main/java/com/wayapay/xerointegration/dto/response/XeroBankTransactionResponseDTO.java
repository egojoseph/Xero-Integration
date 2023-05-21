package com.wayapay.xerointegration.dto.response;

import com.wayapay.xerointegration.pojos.BankAccount;
import com.wayapay.xerointegration.pojos.Contacts;
import com.wayapay.xerointegration.pojos.LineItems;
import com.wayapay.xerointegration.pojos.XeroBankTransaction;
import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransactionResponseDTO
{
    List<XeroBankTransaction> BankTransactions;
}
