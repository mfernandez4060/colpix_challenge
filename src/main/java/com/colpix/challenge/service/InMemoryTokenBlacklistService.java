package com.colpix.challenge.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

	private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

	@Override
	public void invalidate(String token) {
		blacklist.add(token);
	}

	@Override
	public boolean isInvalid(String token) {
		return blacklist.contains(token);
	}
}
