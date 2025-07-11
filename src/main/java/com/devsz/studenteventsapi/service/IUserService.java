package com.devsz.studenteventsapi.service;

import java.util.concurrent.ExecutionException;
import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.entity.UserEntity;

public interface IUserService extends ICRUD<UserEntity, String> {

    Boolean registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException;
}
