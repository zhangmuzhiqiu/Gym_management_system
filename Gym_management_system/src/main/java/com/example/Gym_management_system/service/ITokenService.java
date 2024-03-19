package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.Trainer;

import javax.servlet.http.HttpServletRequest;

public interface ITokenService {

    Trainer getTrainerOfToken(HttpServletRequest request);

    boolean getTokenOk(HttpServletRequest request);

    boolean getTokenOkByTrainer(HttpServletRequest request);
}
