package org.ecore.discord;

import arc.Core;
import arc.struct.Seq;
import arc.util.Timer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.ecore.discord.commands.DiscordCommandLoader;
import org.ecore.discord.commands.impl.*;
import org.ecore.usefull.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;

public class Bot extends ListenerAdapter {
    protected static JDA jda;
    protected static TextChannel bans;
    protected static TextChannel logs;
    protected static TextChannel importantLogs;

    protected static Seq<String> lbuf = new Seq<>();
    public static void load(String token){
        jda = JDABuilder.createLight(token, EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(new Bot())
                .build();


        DiscordCommandLoader.loadCommands(jda,
                new BanDiscord(),
                new UnbanDiscord(),
                new InfoDiscord(),
                new FindDiscord(),
                new SqlDiscord(),
                new JsDiscord(),
                new UploadMapDiscord(),
                new DeleteMapDiscord(),
                new MapListDiscord(),
                new RemoveAdminDiscord()
        );
    }

    @Override
    public void onReady(@NotNull ReadyEvent e){
        long channelId = Core.settings.getLong("logs", 1344529439614042183L);
        logs = jda.getChannelById(TextChannel.class, channelId);
        channelId = Core.settings.getLong("bans", 1344532704422658088L);
        bans = jda.getChannelById(TextChannel.class, channelId);
        importantLogs = jda.getTextChannelById(1422255795269140521L);
        Timer.schedule(() -> {
            if (lbuf.isEmpty()) return;
            Utils.sendLongMessage(importantLogs, "```\n" + lbuf.toString("\n").replace("```", "``\\`") + "\n```");
            lbuf.clear();
        }, 0, 3);
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
    public static void sendJsLog(String caller, String code) {
        lbuf.add("[" + caller + "]: " + code);
    }
    public static void importantLog(String content){
        importantLogs.sendMessageEmbeds(
                new EmbedBuilder()
                        .setTitle("INFO")
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }

    public static void sendLogsMessage(String message){
        logs.sendMessage(message).queue();
    }
}
