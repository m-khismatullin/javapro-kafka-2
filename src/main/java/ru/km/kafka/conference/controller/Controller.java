package ru.km.kafka.conference.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.km.kafka.conference.dto.ConferenceDto;
import ru.km.kafka.conference.dto.ConferenceVisitorDto;
import ru.km.kafka.conference.dto.ResponseDto;
import ru.km.kafka.conference.dto.VisitorDto;
import ru.km.kafka.conference.service.ConferenceService;


@RestController
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final ConferenceService service;

    public Controller(ConferenceService service) {
        this.service = service;
    }

    @PutMapping("/register")
    public ResponseDto<VisitorDto> register(@RequestBody VisitorDto visitorDto) {
        logger.debug("Запрос на регистрацию посетителя {} на конференцию {}", visitorDto.name(), visitorDto.conferenceId());
        service.register(visitorDto);
        return new ResponseDto<>(
                "Посетитель " + visitorDto.name() + " успешно зарегистрирован на конференцию " + visitorDto.conferenceId(),
                visitorDto);
    }

    @GetMapping("getNewRegisters/{conferenceId}")
    public ResponseDto<ConferenceVisitorDto> getNewRegisters(@PathVariable int conferenceId) {
        logger.debug("Запрос списка участников конференции {}", conferenceId);
        return new ResponseDto<>(
                "ok",
                service.getNewRegisters(conferenceId));
    }

    @PutMapping("/addConference")
    public ResponseDto<ConferenceDto> addConference(@RequestBody ConferenceDto conferenceDto) {
        logger.debug("Запрос на добавление конференции {}", conferenceDto.toString());
        service.addConference(conferenceDto);
        return new ResponseDto<>(
                "ok",
                conferenceDto);
    }
}
