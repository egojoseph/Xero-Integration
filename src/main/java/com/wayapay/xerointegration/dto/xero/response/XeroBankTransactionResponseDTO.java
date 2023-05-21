package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wayapay.xerointegration.dto.xero.request.XeroBankTransaction;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroBankTransactionResponseDTO
{
    List<XeroBankTransaction> BankTransactions;
}
