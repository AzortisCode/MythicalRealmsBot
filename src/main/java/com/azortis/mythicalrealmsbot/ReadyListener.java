package com.azortis.mythicalrealmsbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        MessageEmbed botStartedEmbed = new EmbedBuilder()
                .setTitle("Bot loaded successfully!")
                .setColor(Color.decode("#FF5555"))
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setFooter("© Azortis - MythicalRealms")
                .setTimestamp(Instant.now()).build();
        Objects.requireNonNull(event.getJDA().getTextChannelById(MythicalRealmsBot.getConfig().getBotLogChannelId())).sendMessage(botStartedEmbed).queue();
    }

}
