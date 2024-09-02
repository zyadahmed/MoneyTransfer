package com.example.moneytransferapi.exception.customResonse;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ValidationFailedResponse {

    private final List<ViolationErrors> violations = new ArrayList<>();
    private final LocalDateTime timeStamp;
    private final HttpStatus httpStatus;

}
