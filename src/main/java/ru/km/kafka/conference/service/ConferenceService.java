package ru.km.kafka.conference.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.km.kafka.conference.dto.ConferenceDto;
import ru.km.kafka.conference.dto.ConferenceVisitorDto;
import ru.km.kafka.conference.dto.VisitorDto;
import ru.km.kafka.conference.exception.NotFoundException;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConferenceService {
    private final Logger logger = LoggerFactory.getLogger(ConferenceService.class);
    private final KafkaTemplate<String, VisitorDto> kafkaTemplate;
    private final ConsumerFactory<String, VisitorDto> consumerFactory;
    private final ConcurrentHashMap<Integer, String> conferenceGroupMap = new ConcurrentHashMap<>();
    private final Consumer<String, VisitorDto> consumer;
    private final String topic;

    private ConferenceService(
            KafkaTemplate<String, VisitorDto> kafkaTemplate,
            ConsumerFactory<String, VisitorDto> consumerFactory,
            @Value("${topic}") String topic,
            @Value("${group}") String group) {
        this.kafkaTemplate = kafkaTemplate;
        this.consumerFactory = consumerFactory;
        this.topic = topic;
        this.consumer = consumerFactory.createConsumer(group, "reader");
        this.consumer.subscribe(List.of(topic));
    }

    public void addConference(ConferenceDto conferenceDto) {
        if (conferenceGroupMap.containsKey(conferenceDto.id())) {
            throw new NotFoundException("Conference " + conferenceDto.id() + " exists");
        }
        conferenceGroupMap.putIfAbsent(conferenceDto.id(), conferenceDto.name());
    }

    public void register(VisitorDto visitorDto) {
        if (!conferenceGroupMap.containsKey(visitorDto.conferenceId())) {
            throw new NotFoundException("Conference " + visitorDto.conferenceId() + " not found");
        }
        kafkaTemplate.send(topic, visitorDto);
    }

    synchronized public ConferenceVisitorDto getNewRegisters(int conferenceId) {
        if (!conferenceGroupMap.containsKey(conferenceId)) {
            throw new NotFoundException("Conference " + conferenceId + " not found");
        }

        Set<String> names = new HashSet<>();

        ConsumerRecords<String, VisitorDto> records = consumer.poll(Duration.ofSeconds(10));
        for (ConsumerRecord<String, VisitorDto> record : records) {
            if (record.value().conferenceId() == conferenceId) {
                names.add(record.value().name());
            } else {
                kafkaTemplate.send(topic, record.value());
            }
        }

        logger.debug("all names for conferenceId = {} : {}", conferenceId, names);

        return new ConferenceVisitorDto(names.stream().toList());
    }
}
