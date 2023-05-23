package com.wayapay.xerointegration.service;

import org.springframework.stereotype.Service;

@Service
public interface XeroAuthorizationService
{
    String getXeroAccessToken();

}
