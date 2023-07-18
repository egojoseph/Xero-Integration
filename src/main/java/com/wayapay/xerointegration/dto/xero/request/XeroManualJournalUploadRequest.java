package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class XeroManualJournalUploadRequest {
    @JsonProperty("Date")
    public String date;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("Narration")
    public String narration;
    @JsonProperty("LineAmountTypes")
    public String lineAmountTypes;
    @JsonProperty("JournalLines")
    public List<JournalLine> journalLines;
    @JsonProperty("ShowOnCashBasisReports")
    public String showOnCashBasisReports;
}
