package com.wayapay.xerointegration.dto.xero.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class XeroUploadRequest {

        public String Type;
        public Contacts Contact;
         public String LineAmountTypes;
         public List<LineItems> LineItems;
        public BankAccount BankAccount;
        public String Url;



}
