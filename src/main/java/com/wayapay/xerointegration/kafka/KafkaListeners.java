package com.wayapay.xerointegration.kafka;

import com.wayapay.xerointegration.service.XeroIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaListeners {

    @Autowired
    private XeroIntegrationService uploadService;

    @KafkaListener(topics = "transactions", groupId = "waya" )
    void listener(Object data) {


        System.out.println("<<<----------------Listener received------------------>>>: " + data);
    }
}
