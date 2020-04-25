package com.azortis.mythicalrealmsbot.command.commands;

import com.azortis.mythicalrealmsbot.command.Command;
import com.azortis.mythicalrealmsbot.command.CommandCategory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutdownCMD implements Command {

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public String getDescription() {
        return "Shut downs the bot";
    }

    @Override
    public String getUsage() {
        return "shutdown";
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
        return false;
    }
}
