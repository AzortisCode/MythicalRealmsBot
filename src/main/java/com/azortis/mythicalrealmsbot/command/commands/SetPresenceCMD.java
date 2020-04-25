package com.azortis.mythicalrealmsbot.command.commands;

import com.azortis.mythicalrealmsbot.command.Command;
import com.azortis.mythicalrealmsbot.command.CommandCategory;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetPresenceCMD implements Command {

    private final Map<Integer, List<String>> validArguments = new HashMap<>();

    public SetPresenceCMD(){
        List<String> validFirstArguments = new ArrayList<>();
        validFirstArguments.add("online");
        validFirstArguments.add("offline");
        validFirstArguments.add("invisible");
        validFirstArguments.add("idle");
        validFirstArguments.add("do_not_disturb");
        validFirstArguments.add("unknown");
        validArguments.put(1, validFirstArguments);
        List<String> validSecondArguments = new ArrayList<>();
        validSecondArguments.add("default");
        validSecondArguments.add("listening");
        validSecondArguments.add("watching");
        validSecondArguments.add("streaming");
        validSecondArguments.add("custom_status");
        validArguments.put(2, validSecondArguments);
    }

    @Override
    public String getName() {
        return "setpresence";
    }

    @Override
    public String getDescription() {
        return "Allows to set the bots presence.";
    }

    @Override
    public String getUsage() {
        return "setpresence <online|offline|invisible|idle|do_not_disturb|unknown> <default|listening|watching|streaming|custom_status> <insertURL|none> <message>";
    }

    @Override
    public boolean isGuildOnly() {
        return false;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.BOT_OWNER;
    }

    @Override
    public boolean dispatch(MessageReceivedEvent event, String[] arguments) {
        if(validArguments.get(1).contains(arguments[0]) && validArguments.get(2).contains(arguments[1])){
            if(arguments[2].equals("none") || new UrlValidator().isValid(arguments[2])){
                OnlineStatus onlineStatus = OnlineStatus.valueOf(arguments[0].toUpperCase());
                Activity.ActivityType activityType = Activity.ActivityType.valueOf(arguments[1].toUpperCase());
                StringBuilder messageStringBuilder = new StringBuilder();
                for (int i = 3; i <= arguments.length; i++){
                    messageStringBuilder.append(arguments[i]).append(" ");
                }
                String message = messageStringBuilder.toString().trim();
                if(arguments[2].equals("none")){
                    event.getJDA().getPresence().setPresence(onlineStatus, Activity.of(activityType, message));
                }else {
                    event.getJDA().getPresence().setPresence(onlineStatus, Activity.of(activityType, message, arguments[2]));
                }

            }
        }
        return false;
    }
}
