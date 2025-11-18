package com.colpix.challenge.service;

public interface TokenBlacklistService {
    void invalidate(String token);
    boolean isInvalid(String token);
}
