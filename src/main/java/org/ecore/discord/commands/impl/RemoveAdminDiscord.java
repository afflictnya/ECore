package org.ecore.discord.commands.impl;

import arc.util.Strings;
import mindustry.Vars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.database.Cache;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class RemoveAdminDiscord extends DiscordCommand {
    public RemoveAdminDiscord() {
        super("removeadmin", "Removes admin privileges from player");
        allowedRoles.add(1334077407015075840L);
        options.add(new OptionData(STRING, "server", "name of server", true));
        options.add(new OptionData(STRING, "data", "uuid (or id)", true));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        var data = event.getOption("data").getAsString();
        if (data.length() == 24 && data.endsWith("==")) {
            Vars.netServer.admins.unAdminPlayer(data);
            var d = PVars.database.getByUUID(data);
            if (d != null) {
                d.rank = "def";
                d.save();
            }
        } else if (Strings.canParseInt(data)) {
            var d =  PVars.database.getByID(Integer.parseInt(data));
            if (d != null) {
                d.rank = "def";
                Vars.netServer.admins.unAdminPlayer(d.uuid);
                d.save();
            }
        } else throw new DiscordCommandEx("Invalid input");
        event.reply("successfully removed admin");
    }
}
