package com.example.Backend.Dto.Requests;

import lombok.Data;

import java.util.List;

@Data
public class NotificationRequest {
    private String notification;
    private List<Long> to;
}
