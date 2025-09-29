package org.ecore.discord.commands.impl;

import mindustry.Vars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class DeleteMapDiscord extends DiscordCommand {
    public  DeleteMapDiscord() {
        super("delmap", "Remove map from server");
        allowedRoles.add(1253359340547739648L);
        options.add(new OptionData(STRING, "server", "name of server", true));
        options.add(new OptionData(OptionType.STRING, "mapname", "Map to remove", true));
    }
    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        var mapName = event.getOption("mapname").getAsString();
        var map = Vars.maps.customMaps().find(m -> m.plainName().equalsIgnoreCase(mapName) || m.file.name().equalsIgnoreCase(mapName));
        if (map == null) {
            throw new DiscordCommandEx("Invalid map name");
        }
        Vars.maps.removeMap(map);
        Vars.maps.reload();
        event.reply("Map deleted").queue();
    }
}
