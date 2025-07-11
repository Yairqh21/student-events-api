package com.devsz.studenteventsapi.dto.analytics;

import java.util.List;

import lombok.Data;
@Data
public class AnalyticsDTOs<T> {
    private List<T> data;

    public AnalyticsDTOs(List<T> data) {
        this.data = data;
    }
}

