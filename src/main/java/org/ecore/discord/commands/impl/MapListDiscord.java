package org.ecore.discord.commands.impl;

import mindustry.Vars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import java.awt.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class MapListDiscord extends DiscordCommand {
    public MapListDiscord() {
        super("maplist", "list of all maps on the server");
        allowedRoles.add(1144573850688966736L);
        options.add(new OptionData(STRING, "server", "name of server", true));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        event.replyEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Maps")
                        .setDescription(Vars.maps.customMaps().map(m -> m.plainName() + " [" + m.file.name() + "]" + " by " + m.plainAuthor()).toString("\n"))
                        .build()
        ).queue();
    }
}
