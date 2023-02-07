package ru.neostudy.loanConveyorProject.deal;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageToTopic(NewTopic topic, String message) {
        kafkaTemplate.send(String.valueOf(topic), message);
    }

}
