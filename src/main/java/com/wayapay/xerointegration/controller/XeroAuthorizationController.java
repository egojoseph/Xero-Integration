package com.wayapay.xerointegration.controller;

import com.wayapay.xerointegration.dto.xero.response.AuthorizationResponsePayload;
import com.wayapay.xerointegration.service.XeroAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("/xero")
public class XeroAuthorizationController
{

    @Autowired
    private XeroAuthorizationService xeroAuthorizationService;

    @GetMapping(value = "/authorization-url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthorizationResponsePayload> handleAuthUrlRequest(){
        return ResponseEntity.ok(xeroAuthorizationService.processXeroAuthorizationUrlRequest());
    }

    @GetMapping("/redirect")
    public String handleXeroRedirect(@RequestParam Map<String, String> params){
        return xeroAuthorizationService.processApplicationAccessTokenOnRedirect(params);
    }
}
