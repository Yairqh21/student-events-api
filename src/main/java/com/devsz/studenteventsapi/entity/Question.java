package com.devsz.studenteventsapi.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question{

     private String id;
     private String questionText;
     private String questionType;
     private List<String> options;
}
