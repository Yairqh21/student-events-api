package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {

     private String id;
     private String surveyId;
     private String eventId;
     private String userId;
     private List<AnswerQuestionEntity> answers;

}


