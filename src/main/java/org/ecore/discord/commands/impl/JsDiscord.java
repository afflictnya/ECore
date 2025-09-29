package org.ecore.discord.commands.impl;

import arc.Core;
import mindustry.Vars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.discord.Bot;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import java.awt.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class JsDiscord extends DiscordCommand {
    public JsDiscord() {
        super("js", "Execute JavaScript code on server.");
        options.add(new OptionData(STRING, "server", "name of server", true));
        options.add(new OptionData(OptionType.STRING, "code", "JS code", true));
        allowedRoles.add(1144575307609804940L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        try {
            Core.app.post(()->{
                String code = event.getOption("code").getAsString();
                Bot.sendJsLog(event.getUser().getName(), code);
                String resp = Vars.mods.getScripts().runConsole(code);
                event.replyEmbeds(
                        new EmbedBuilder().setColor(Color.MAGENTA).setTitle("JS").setDescription(resp).build()
                ).queue();
            });
        } catch (Exception e) {
            throw new DiscordCommandEx(e.getMessage());
        }
    }
}
