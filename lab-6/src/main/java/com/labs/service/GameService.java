package com.labs.service;

import com.labs.domain.GameProperties;

public interface GameService {
    void startGame(GameProperties gameProperties);

    GameProperties getGameProperties();
}
