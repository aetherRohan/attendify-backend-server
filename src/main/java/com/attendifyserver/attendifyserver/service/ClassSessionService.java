package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionRequest;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassSessionService {


    @Transactional
    public SyncOfflineSessionResponse processAndSaveOfflineSession(SyncOfflineSessionRequest sessionRequest) {

        return SyncOfflineSessionResponse.builder().build();
    }
}
