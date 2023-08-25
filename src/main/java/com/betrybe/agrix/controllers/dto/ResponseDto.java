package com.betrybe.agrix.controllers.dto;

/**
 * ResponseDto.
 */
public record ResponseDto<T>(String message, T data) {
}
