package com.yrog.apijeuxolympiques.security.response;

import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
