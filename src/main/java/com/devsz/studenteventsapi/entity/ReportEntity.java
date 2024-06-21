package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportEntity {

    private String id;
    private String reportName;
    private String surveyName;
    private int assistanceAmount;
    private int attendanceAmountEvent;
    private int assistanceAmountModality;
    private int attendaceAmountRace;
    private int amountAssistanceCycle;

}
