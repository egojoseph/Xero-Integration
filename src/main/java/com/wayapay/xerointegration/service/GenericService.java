package com.wayapay.xerointegration.service;

import java.util.Map;

public interface GenericService
{
    String getForObject(String url, Map<String, String> headers, Map<String, Object> params);
    String getForObject(String url, Map<String, String> headers);
    String getForObject(String url);
    String postForObject(String url, String requestJson, Map<String, String> headers, Map<String, Object> params);

    String postForForm(String url, Map<String, Object> fields, Map<String, String> headers, Map<String, Object> params);

    String postForObject(String url, Object requestObject, Map<String, String> headers, Map<String, Object> params);
    String postForObject(String url, Object requestObject);

    String getXeroAuthAccessToken();
}
