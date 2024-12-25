package com.devsz.studenteventsapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;

import lombok.Data;


@Data
public abstract class BaseEntity {

    @DocumentId
    private String id;

    @ServerTimestamp
    @JsonIgnore
    private Timestamp createdAt; 

    @JsonIgnore
    private Timestamp updatedAt; 

    public void onUpdate() {
        this.updatedAt = Timestamp.now();
    }

}
