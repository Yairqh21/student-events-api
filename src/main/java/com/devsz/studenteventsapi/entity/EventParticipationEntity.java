package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class EventParticipationEntity extends BaseEntity {

    private String eventId;
    private String participantId;
    private String ticketCode;
    private Boolean isAttended;
    private String registrationDate;

}
