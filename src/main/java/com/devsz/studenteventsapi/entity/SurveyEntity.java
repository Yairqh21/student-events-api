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
    private String description;
    private Boolean isActive; 
    private String userId;
    private String eventId;
    private List<Question> questions;

}