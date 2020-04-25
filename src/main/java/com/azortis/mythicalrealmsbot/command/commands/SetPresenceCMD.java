package com.azortis.mythicalrealmsbot.command.commands;

import com.azortis.mythicalrealmsbot.command.Command;
import com.azortis.mythicalrealmsbot.command.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
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
        validFirstArguments.add("dnd");
        validArguments.put(1, validFirstArguments);
        List<String> validSecondArguments = new ArrayList<>();
        validSecondArguments.add("default");
        validSecondArguments.add("listening");
        validSecondArguments.add("watching");
        validSecondArguments.add("streaming");
        //validSecondArguments.add("custom_status");
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
        return "setpresence <online|offline|invisible|idle|dnd> <default|listening|watching|streaming> <streamingURL|none> <value>";
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
    public boolean dispatch(MessageReceivedEvent event, String[] arguments, boolean isGuild) {
        if(validArguments.get(1).contains(arguments[0]) && validArguments.get(2).contains(arguments[1])){
            if(arguments[2].equals("none") || Activity.isValidStreamingUrl(arguments[2])){
                OnlineStatus onlineStatus = OnlineStatus.fromKey(arguments[0]);
                int activityKey = 0;
                switch (arguments[1]){
                    case "listening":
                        activityKey = 2;
                        break;
                    case "streaming":
                        activityKey = 1;
                        break;
                    case "watching":
                        activityKey = 3;
                        break;
                    default:
                }
                Activity.ActivityType activityType = Activity.ActivityType.fromKey(activityKey);
                StringBuilder messageStringBuilder = new StringBuilder();
                for (int i = 3; i < arguments.length; i++){
                    messageStringBuilder.append(arguments[i]).append(" ");
                }
                String value = messageStringBuilder.toString().trim();
                if(arguments[2].equals("none")){
                    event.getJDA().getPresence().setPresence(onlineStatus, Activity.of(activityType, value));
                    event.getJDA().getPresence().setActivity(Activity.of(activityType, value));
                }else {
                    event.getJDA().getPresence().setPresence(onlineStatus, Activity.of(activityType, value, arguments[2]));
                    event.getJDA().getPresence().setActivity(Activity.of(activityType, value));
                }
                MessageEmbed presenceChangedEmbed = new EmbedBuilder()
                        .setTitle("Presence updated!")
                        .setColor(Color.decode("#FF5555"))
                        .addField("OnlineStatus", onlineStatus.getKey(), false)
                        .addField("ActivityType", activityType.toString().toLowerCase(), false)
                        .addField("URL", arguments[2], false)
                        .addField("Value", value, false)
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setFooter("Executed by: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                        .setTimestamp(Instant.now()).build();
                if(isGuild){
                    event.getPrivateChannel().sendMessage(presenceChangedEmbed).queue();
                }else {
                    event.getTextChannel().sendMessage(presenceChangedEmbed).queue();
                }
                return true;
            }
        }
        return false;
    }
}
