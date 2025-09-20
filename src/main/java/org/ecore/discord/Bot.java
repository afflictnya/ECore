package org.ecore.discord;

import arc.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.ecore.discord.commands.DiscordCommandLoader;
import org.ecore.discord.commands.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Bot extends ListenerAdapter {
    protected static JDA jda;
    protected static TextChannel bans;
    protected static TextChannel logs;
    public static void load(String token){
        jda = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
                .addEventListeners(new Bot())
                .build();

        long channelId = (long) Core.settings.get("logs", 1344529439614042183L);
        if(jda.getChannelById(TextChannel.class, channelId) != null) {
            logs = jda.getChannelById(TextChannel.class, channelId);
        }
        channelId = (long)Core.settings.get("bans", 1344532704422658088L);
        if(jda.getChannelById(TextChannel.class, channelId) != null) {
            bans = jda.getChannelById(TextChannel.class, channelId);
        }
        DiscordCommandLoader.loadCommands(jda,
                new BanDiscord(),
                new UnbanDiscord(),
                new InfoDiscord(),
                new FindDiscord(),
                new SqlDiscord()
        );
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        DiscordCommandLoader.handleInteraction(event);
    }

    public static void sendBan(EmbedBuilder b){
        bans.sendMessageEmbeds(
                b.build()
        ).queue();
    }

    public static void sendLogsMessage(String message){
        logs.sendMessage(message).queue();
    }
}
