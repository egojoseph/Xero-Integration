package com.wayapay.xerointegration.kafka;

import com.wayapay.xerointegration.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaListeners {

    @Autowired
    private UploadService uploadService;

    @KafkaListener(topics = "transactions", groupId = "waya" )
    void listener(Object data) {


        System.out.println("<<<----------------Listener received------------------>>>: " + data);
    }
}
