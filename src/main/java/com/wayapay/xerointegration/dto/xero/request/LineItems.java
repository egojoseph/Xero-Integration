package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItems {

    private String Description;
    private String UnitAmount;
    private String AccountCode;
    private List<Tracking> Tracking;
}
