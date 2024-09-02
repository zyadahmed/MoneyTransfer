package com.example.moneytransferapi.exception.customResonse;

import lombok.Builder;

@Builder
public record ViolationErrors(String fieldName, String message) {
}
