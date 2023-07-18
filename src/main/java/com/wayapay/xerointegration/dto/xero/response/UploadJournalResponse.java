package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UploadJournalResponse {
    @JsonProperty("Id")
    public String id;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("ProviderName")
    public String providerName;
    @JsonProperty("DateTimeUTC")
    public String dateTimeUTC;
    @JsonProperty("ManualJournals")
    public List<ManualJournal> manualJournals;
}
