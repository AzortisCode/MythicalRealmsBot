package com.azortis.mythicalrealmsbot;

import ch.qos.logback.classic.Logger;
import com.azortis.mythicalrealmsbot.command.CommandDispatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public final class MythicalRealmsBot {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(MythicalRealmsBot.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static JDA client;
    private static String directory;
    private static Config config;
    private static File configFile;

    public static void main(String[] args) throws Exception {
        directory = new File(MythicalRealmsBot.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        directory = directory.replace("%20", " ");
        loadConfig();
        configSetup();
        client = JDABuilder.createDefault(config.getToken()).build();
        client.addEventListener(new CommandDispatcher());
        if(!config.getDefaultActivity().equals("none"))client.getPresence().setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, config.getDefaultActivity()));
        client.awaitReady();
    }

    private static void loadConfig(){
        configFile = new File(directory, "config.json");
        if(!configFile.exists())copy(MythicalRealmsBot.class.getClassLoader().getResourceAsStream("config.json"), configFile);
        try{
            config = gson.fromJson(new FileReader(configFile), Config.class);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void configSetup(){
        boolean changesMade = false;
        if(config.getToken().equals("")) {
            logger.info("No token is present in the configuration, insert bot token");
            String token = System.console().readLine();
            config.setToken(token);
            changesMade = true;
        }
        if(config.getOwnerIds().isEmpty()){
            logger.info("No ownerIds present in the configuration, insert ownerIds, when you're done execute stop");
            boolean stopped = false;
            while(!stopped){
                String input = System.console().readLine();
                if(!input.equals("stop")) {
                    config.getOwnerIds().add(Long.valueOf(input));
                    logger.info("Added ownerId: " + input);
                }else{
                    stopped = true;
                }
            }
            changesMade = true;
        }
        if(config.getPrefix().equals("")){
            logger.info("No prefix is present in the configuration, insert bot prefix");
            String prefix = System.console().readLine();
            config.setToken(prefix);
            changesMade = true;
        }
        if(config.getDefaultActivity().equals("")){
            logger.info("No defaultActivity present in the configuration, insert default activity, for nothing insert: none");
            String defaultActivity = System.console().readLine();
            config.setDefaultActivity(defaultActivity);
            changesMade = true;
        }
        if(changesMade)saveConfig();
    }

    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static JDA getClient() {
        return client;
    }

    public static String getDirectory() {
        return directory;
    }

    public static Config getConfig() {
        return config;
    }

    public static void saveConfig(){
        try{
            final String json = config.toString();
            if(configFile.delete()) {
                Files.write(configFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                logger.info("Config file saved successfully!");
            }else{
                logger.error("Failed to save config file!");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void reloadConfig(){
        try {
            config = gson.fromJson(new FileReader(configFile), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
