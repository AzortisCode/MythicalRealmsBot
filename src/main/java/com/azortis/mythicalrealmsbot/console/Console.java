package com.azortis.mythicalrealmsbot.console;

import com.azortis.mythicalrealmsbot.MythicalRealmsBot;

public class Console implements InputListener{

    public InputThread inputThread;

    public Console(){
        inputThread = new InputThread(this);
        inputThread.start();
    }

    @Override
    public void processInput(String input) {
        if(input.equals("shutdown")){
            MythicalRealmsBot.shutdown();
        }
    }

}
