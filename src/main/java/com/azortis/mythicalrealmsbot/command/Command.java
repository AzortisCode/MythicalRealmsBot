package com.azortis.mythicalrealmsbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

    String getName();

    String getDescription();

    String getUsage();

    boolean isGuildOnly();

    CommandCategory getCategory();

    boolean dispatch(MessageReceivedEvent event, String[] arguments, boolean isGuild);

}
