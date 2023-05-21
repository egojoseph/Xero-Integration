package com.wayapay.xerointegration.dto.xero.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponseDTO
{
    @JsonProperty("access_token")
    @SerializedName("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    @SerializedName("expires_in")
    private long expiresIn;

    @JsonProperty("token_type")
    @SerializedName("token_type")
    private String tokenType;
}
