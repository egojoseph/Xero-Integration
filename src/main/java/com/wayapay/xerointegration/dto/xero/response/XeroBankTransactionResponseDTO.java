package com.wayapay.xerointegration.dto.xero.response;

import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;

import java.util.List;

@Data
public class XeroBankTransactionResponseDTO
{
    List<XeroBankTransaction> BankTransactions;
}
