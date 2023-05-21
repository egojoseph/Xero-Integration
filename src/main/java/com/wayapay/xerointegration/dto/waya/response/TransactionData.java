package com.wayapay.xerointegration.dto.waya.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TransactionData {
    public int version;
    public int id;
    public boolean del_flg;
    public boolean posted_flg;
    public String tranId;
    public String acctNum;
    public double tranAmount;
    public String tranType;
    public String partTranType;
    public String tranNarrate;
    public String tranDate;
    public String tranCrncyCode;
    public String paymentReference;
    public String tranGL;
    public int tranPart;
    public int relatedTransId;
    public String createdAt;
    public String updatedAt;
    public String tranCategory;
    public String createdBy;
    public String createdEmail;
    public String senderName;
    public String receiverName;
    public String transChannel;
    public boolean channel_flg;
}
