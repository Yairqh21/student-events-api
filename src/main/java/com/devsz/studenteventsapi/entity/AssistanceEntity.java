package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceEntity {

    private String id;
    private String studentName;
    private String eventName;
    private Boolean attented;

}
