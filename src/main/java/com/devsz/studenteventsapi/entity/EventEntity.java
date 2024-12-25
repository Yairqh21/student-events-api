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
public class EventEntity  extends BaseEntity{

    private String userCreatedId;
    private String eventType;
    private String eventModality;
    private String linkUrl;
    private String imgUrl;
    private String eventName;
    private String description;
    private String eventDateTime;
    private String location;
    private List<String > targetAudience;
    private Boolean status;
    private String cancellationReason;
    private String sponsor;

}
