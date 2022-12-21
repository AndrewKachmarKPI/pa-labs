package com.labs.serviceImpl;

import com.labs.domain.GameProperties;
import com.labs.service.GameService;

public class GameServiceImpl implements GameService {
    private static  GameServiceImpl gameInstance;
    private GameProperties gameProperties;

    public static GameServiceImpl getInstance(){
        if(gameInstance==null){
            gameInstance = new GameServiceImpl();
        }
        return gameInstance;
    }

    @Override
    public void startGame(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    @Override
    public GameProperties getGameProperties() {
        return gameProperties;
    }
}
