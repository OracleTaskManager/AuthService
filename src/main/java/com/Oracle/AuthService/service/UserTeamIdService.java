package com.Oracle.AuthService.service;

import com.Oracle.AuthService.model.UserTeamId;
import com.Oracle.AuthService.repository.UserTeamIdRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTeamIdService {

    @Autowired
    private UserTeamIdRepository userTeamIdRepository;

    public List<UserTeamId> findAll(){
        return userTeamIdRepository.findAll();
    }
}
