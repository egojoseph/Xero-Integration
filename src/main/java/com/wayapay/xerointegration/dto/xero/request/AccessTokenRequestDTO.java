package com.wayapay.xerointegration.dto.xero.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AccessTokenRequestDTO
{
    @JsonProperty("grant_type")
    @SerializedName("grant_type")
    private String grantType;

    private String scope;
}
