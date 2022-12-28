package com.labs.service;

import com.labs.domain.GamePlayer;

public interface Observer {
    void onStopGame(GamePlayer gamePlayer);

    void onPlayerChange(GamePlayer gamePlayer);
    void onPlayerScoreChange(String playerTitle, Integer score);
}
