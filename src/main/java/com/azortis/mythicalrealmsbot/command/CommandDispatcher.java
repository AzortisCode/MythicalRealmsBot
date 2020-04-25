package com.azortis.mythicalrealmsbot.command;

import com.azortis.mythicalrealmsbot.MythicalRealmsBot;
import com.azortis.mythicalrealmsbot.command.commands.SetPresenceCMD;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher extends ListenerAdapter {

    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandDispatcher(){
        // Bot owner.
        commandMap.put("setpresence", new SetPresenceCMD());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String commandString = event.getMessage().getContentRaw().split(" ")[0].trim();
        String commandStringNoPrefix = commandString.replaceFirst(MythicalRealmsBot.getConfig().getPrefix(), "");
        if(commandString.startsWith(MythicalRealmsBot.getConfig().getPrefix())
                && commandMap.containsKey(commandStringNoPrefix)){
            Command command = commandMap.get(commandStringNoPrefix);
            if(command.getCategory() == CommandCategory.BOT_OWNER
                    && !MythicalRealmsBot.getConfig().getOwnerIds().contains(event.getAuthor().getIdLong())){
                MessageEmbed noPermissionsEmbed = new EmbedBuilder()
                        .setTitle("No permission!")
                        .setColor(Color.decode("#FF5555"))
                        .setDescription("You have no permission to run this!")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setFooter("Executed by: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                        .setTimestamp(Instant.now()).build();
                event.getChannel().sendMessage(noPermissionsEmbed).queue();
                return;
            }
            if(command.isGuildOnly() && !event.getMessage().isFromGuild()){
                MessageEmbed guildOnlyEmbed = new EmbedBuilder()
                        .setTitle("Guild only!")
                        .setColor(Color.decode("#FF5555"))
                        .setDescription("This command can only be executed in a server!")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setFooter("Executed by: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                        .setTimestamp(Instant.now()).build();
                event.getChannel().sendMessage(guildOnlyEmbed).queue();
                return;
            }
            String[] arguments = event.getMessage().getContentRaw().replaceFirst(commandString, "").trim().split(" ");
            if(!command.dispatch(event, arguments, event.isFromGuild())){
                MessageEmbed invalidUsageEmbed = new EmbedBuilder()
                        .setTitle("Invalid usage!")
                        .setColor(Color.decode("#FF5555"))
                        .addField("The correct usage is:", "-" + command.getUsage(), false)
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .setFooter("Executed by: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                        .setTimestamp(Instant.now()).build();
                event.getChannel().sendMessage(invalidUsageEmbed).queue();
            }
        }
    }

}
