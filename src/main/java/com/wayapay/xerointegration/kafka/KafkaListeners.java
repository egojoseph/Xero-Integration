package com.wayapay.xerointegration.kafka;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;


@Component
@Slf4j
@EnableKafka
public class KafkaListeners {

    @Autowired
    private  XeroIntegrationService xeroIntegrationService;

    private static final Gson JSON = new Gson();

    @KafkaListener(topics = "transactions", groupId = "waya")
    public void listener(String data) throws URISyntaxException, IOException {
        log.info("<<<----------------Listener received------------------>>>: " + data);
        WayaTransactionRequest transaction = JSON.fromJson(data, WayaTransactionRequest.class);
        log.info("transaction ---->>>> {}", transaction);
        xeroIntegrationService.getTransactionFromWaya(transaction);
    }

}
