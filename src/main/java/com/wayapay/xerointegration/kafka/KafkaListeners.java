package com.wayapay.xerointegration.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaListeners {

    @KafkaListener(topics = "waya", groupId = "waya" )
    void listener(String data) {

        System.out.println("----------------Listener received------------------: " + data);
    }
}
