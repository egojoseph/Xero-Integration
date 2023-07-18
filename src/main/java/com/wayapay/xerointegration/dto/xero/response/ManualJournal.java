package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualJournal {
    @JsonProperty("Date")
    public String date;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("LineAmountTypes")
    public String lineAmountTypes;
    @JsonProperty("UpdatedDateUTC")
    public String updatedDateUTC;
    @JsonProperty("ManualJournalID")
    public String manualJournalID;
    @JsonProperty("Narration")
    public String narration;
    @JsonProperty("JournalLines")
    public List<?> journalLines;
    @JsonProperty("ShowOnCashBasisReports")
    public boolean showOnCashBasisReports;
    @JsonProperty("HasAttachments")
    public boolean hasAttachments;
}
