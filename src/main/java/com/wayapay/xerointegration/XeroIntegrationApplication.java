package com.wayapay.xerointegration;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.request.WayaTransactionRequest;
import com.wayapay.xerointegration.kafka.KafkaListeners;
import com.wayapay.xerointegration.service.XeroIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@Slf4j
public class XeroIntegrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(XeroIntegrationApplication.class, args);
	}

}
