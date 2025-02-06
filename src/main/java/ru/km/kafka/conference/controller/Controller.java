package ru.km.kafka.conference.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.km.kafka.conference.dto.ConferenceDto;
import ru.km.kafka.conference.dto.ConferenceVisitorDto;
import ru.km.kafka.conference.dto.ResponseDto;
import ru.km.kafka.conference.dto.VisitorDto;


@RestController
public class Controller {
    private final KafkaTemplate<Object, Object> template;
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(KafkaTemplate<Object, Object> template) {
        this.template = template;
    }

    @PutMapping("/register")
    public ResponseDto<VisitorDto> register(@RequestBody VisitorDto visitorDto) {
        logger.debug("Запрос на регистрацию посетителя {} на конференцию {}", visitorDto.name(), visitorDto.conferenceId());
        return new ResponseDto<>(
                "Посетитель " + visitorDto.name() + " успешно зарегистрирован на конференцию " + visitorDto.conferenceId(),
                visitorDto);
    }

    @GetMapping("getNewRegisters/{conferenceId}")
    public ResponseDto<ConferenceVisitorDto> getNewRegisters(@PathVariable long conferenceId) {
        logger.debug("Запрос списка участников конференции {}", conferenceId);
        return new ResponseDto<>(
                "ok",
                null);
    }

    @PutMapping("/addConference")
    public ResponseDto<ConferenceDto> addConference(@RequestBody ConferenceDto conferenceDto) {
        logger.debug("Запрос на добавление конференции {}", conferenceDto.toString());
        return new ResponseDto<>(
                "ok",
                conferenceDto);
    }
}
