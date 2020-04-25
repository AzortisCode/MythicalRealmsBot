package com.azortis.mythicalrealmsbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class Config implements Serializable {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private String token;
    private ArrayList<Long> ownerIds;
    private String prefix;
    private String defaultActivity;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Long> getOwnerIds() {
        return ownerIds;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDefaultActivity() {
        return defaultActivity;
    }

    public void setDefaultActivity(String defaultActivity) {
        this.defaultActivity = defaultActivity;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
