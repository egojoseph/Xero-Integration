package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XeroJournalResponsePayload {

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
