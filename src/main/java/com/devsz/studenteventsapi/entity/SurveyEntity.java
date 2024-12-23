package com.devsz.studenteventsapi.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class SurveyEntity extends BaseEntity {

    private String title;
    private Boolean isActive; 
    private String organizedId;
    private String eventId;
    private List<Question> questions;

}