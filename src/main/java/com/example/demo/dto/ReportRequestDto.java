package com.example.demo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReportRequestDto {
    private List<Long> userIds;
    private String status;

    public ReportRequestDto(List<Long> userIds, String status) {
        this.userIds = userIds;
        this.status = status;
    }
}
