package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracking {
    public String Name;
    public String Option;
    public String TrackingCategoryID;
    public String TrackingOptionID;

    public Tracking(String name, String option) {
        Name = name;
        Option = option;
    }
}
