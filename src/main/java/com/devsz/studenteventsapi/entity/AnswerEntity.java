package com.devsz.studenteventsapi.entity;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnswerEntity extends BaseEntity {
     //private String id;
     private String surveyId;
     private String eventId;
     private String userId;
     private List<AnswerQuestion> answers;

}
