package com.wayapay.xerointegration.dto.xero.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO
{
    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("refresh_token")
    private String refreshToken;
}
