package com.devsz.studenteventsapi.dto;

public record UserParticipationEvent(
        String id,
        String eventId,
        String eventDateTime,
        String eventTitle,
        String eventLocation,
        String eventModality,
        String eventImgUrl,
        String participantId,
        boolean isAttended,
        String ticketCode,
        String registrationDate) {
}
