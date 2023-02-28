package com.abctech.services.kafka;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.abctech.services.event.AssetPromotionEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetPromotionProducer {

    private NewTopic topic;

    private KafkaTemplate<String, AssetPromotionEvent> kafkaTemplate;

    public AssetPromotionProducer(NewTopic topic, KafkaTemplate<String, AssetPromotionEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AssetPromotionEvent event){
   
        log.info("Asset Promotion Event {} ", event.toString());
       
        Message<AssetPromotionEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);
        
        log.info("Asset Promotion Event message sent ");
    }
}