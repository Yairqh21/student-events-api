package com.devsz.studenteventsapi.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponsesEntity extends BaseEntity {

    private String surveyId;
    private String userId;
    private List<Answer> answers;

}
