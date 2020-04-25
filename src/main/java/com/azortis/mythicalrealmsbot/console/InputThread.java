package com.azortis.mythicalrealmsbot.console;

public class InputThread extends Thread{

    private final InputListener inputListener;
    private boolean run = true;

    protected InputThread(InputListener inputListener){
        this.inputListener = inputListener;
    }

    @Override
    public void run() {
        while(run){
            String input = System.console().readLine();
            inputListener.processInput(input);
        }
    }

    public void terminate(){
        run = false;
    }

}
