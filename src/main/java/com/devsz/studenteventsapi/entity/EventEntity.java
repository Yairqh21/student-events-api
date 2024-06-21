package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    private String id;
    private String eventType;
    private String modality;
    private String urlEventPhoto;
    private String eventName;
    private String eventDateTime;
    private String ubication;

}
