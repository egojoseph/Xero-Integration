package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroUploadRequest {

        public String Type;
        public Contacts Contact;
         public String LineAmountTypes;
         public List<LineItems> LineItems;
        public BankAccount BankAccount;
        public String Url;



}
