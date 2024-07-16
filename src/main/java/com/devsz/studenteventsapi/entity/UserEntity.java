package com.devsz.studenteventsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private String id;
    private String username;
    private String password;
    private String email;
    //otros datos
    private String fullName;
    private String numberPhone;
    private String career;
    private String academicCycle;
    private String userImg;

}
