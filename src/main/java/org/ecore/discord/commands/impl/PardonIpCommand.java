package org.ecore.discord.commands.impl;

import mindustry.Vars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class PardonIpCommand extends DiscordCommand {
    public PardonIpCommand() {
        super("pardonip", "Pardons the IP address");
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        options.add(new OptionData(STRING, "ip", "target IP address")
                .setRequired(true));
        allowedRoles.add(1144573850688966736L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        String ip = event.getOption("ip").getAsString();
        if (!PVars.ipPattern.matcher(ip).matches()) {
            throw new DiscordCommandEx("Invalid IP address");
        }
        Vars.netServer.admins.kickedIPs.remove(ip);
        Vars.netServer.admins.dosBlacklist.remove(ip);
        event.reply("successfully.").queue();
    }
}
