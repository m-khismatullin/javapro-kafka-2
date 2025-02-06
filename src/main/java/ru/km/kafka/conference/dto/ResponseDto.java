package ru.km.kafka.conference.dto;

public record ResponseDto<T>(String message, T data) {
}
