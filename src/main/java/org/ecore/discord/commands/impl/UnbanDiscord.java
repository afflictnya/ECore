package org.ecore.discord.commands.impl;

import mindustry.Vars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import java.util.Optional;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class UnbanDiscord extends DiscordCommand {
    public UnbanDiscord() {
        super("unban", "unban someone by uuid or ip");
        options.add(new OptionData(STRING, "id", "ip/uuid")
                .setRequired(true));
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        allowedRoles.add(1144573850688966736L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        String id = event.getOption("id").getAsString();
        Vars.netServer.admins.unbanPlayerID(id);
        Vars.netServer.admins.unbanPlayerIP(id);
        Optional.ofNullable(PVars.database.getByUUID(id)).ifPresent(data -> {
            data.banned = false;
            data.save();
        });
        event.reply("Unbanned.").queue();
    }
}
