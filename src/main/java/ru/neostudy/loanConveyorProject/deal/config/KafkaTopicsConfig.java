package ru.neostudy.loanConveyorProject.deal.config;

import lombok.ToString;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic doFinishRegistrationTopic(){
        return TopicBuilder.name("finish-registration").build();
    }

    @Bean
    public NewTopic doCreateDocumentsTopic(){
        return TopicBuilder.name("create-documents").build();
    }

    @Bean
    public NewTopic doSendDocumentsTopic(){
        return TopicBuilder.name("send-documents").build();
    }

    @Bean
    public NewTopic doSendSESTopic(){
        return TopicBuilder.name("send-ses").build();
    }

    @Bean
    public NewTopic doCreditIssuedTopic(){
        return TopicBuilder.name("credit-issued").build();
    }

    @Bean
    public NewTopic doApplicationDeniedTopic(){
        return TopicBuilder.name("application-denied").build();
    }

}
