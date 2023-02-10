package ru.neostudy.loanConveyorProject.dossier;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {


    @KafkaListener(topics = "finish-registration", groupId = "group_id")
    public void listenToFinishRegistrationTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }


    @KafkaListener(topics = "create-documents", groupId = "group_id")
    public void listenToCreateDocumentsKafkaTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }

    @KafkaListener(topics = "send-documents", groupId = "group_id")
    public void listenToSendDocumentsKafkaTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }


    @KafkaListener(topics = "send-ses", groupId = "group_id")
    public void listenToSendSESTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }


    @KafkaListener(topics = "credit-issued", groupId = "group_id")
    public void listenToCreditIssuedTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }


    @KafkaListener(topics = "application-denied", groupId = "group_id")
    public void listenToApplicationDeniedTopic(String messageReceived) {
        System.out.println("Message received is " + messageReceived);
    }


}
