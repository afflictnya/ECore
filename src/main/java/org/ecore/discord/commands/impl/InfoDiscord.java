package org.ecore.discord.commands.impl;

import arc.util.Strings;
import mindustry.Vars;
import mindustry.net.Administration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.database.Cache;
import org.ecore.database.PlayerData;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;
import org.ecore.usefull.Utils;

import static java.lang.Integer.parseInt;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class InfoDiscord extends DiscordCommand {
    public InfoDiscord() {
        super("info", "get info of player by name");
        options.add(new OptionData(STRING, "name", "name or uuid or ID")
                .setRequired(true));
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        allowedRoles.add(1144573850688966736L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        String text = event.getOption("name").getAsString();
        if (Strings.canParseInt(text)) {
            PlayerData data = PVars.database.getByID(parseInt(text));
            if (data == null) throw new DiscordCommandEx("Can`t find player with this ID!");
            var sdata = Vars.netServer.admins.getInfo(data.uuid);
            sendInfo(event, data, sdata);
        } else if (text.length() == 24 && text.endsWith("==")) {
            PlayerData data = PVars.database.getByUUID(text);
            if (data == null) throw new DiscordCommandEx("Can`t find player with this UUID!");
            var sdata = Vars.netServer.admins.getInfo(data.uuid);
            sendInfo(event, data, sdata);
        } else {
            var set = Vars.netServer.admins.findByName(text);
            if (set.isEmpty()) throw new DiscordCommandEx("Can`t find this player!");
            var sdata = set.first();
            var data = Cache.forceGet(sdata.id);
            sendInfo(event, data, sdata);
        }
    }

    private void sendInfo(SlashCommandInteractionEvent event, PlayerData data, Administration.PlayerInfo sdata) {
        event.reply(Utils.d(Strings.format("""
                    UUID: @
                    ID: @
                    IPs: @
                    Names: @
                    placed: @
                    broken: @
                    Messages sent: @
                    joined: @
                    kicked: @
                    Banned: @
                    Banned until: @
                    Rank: @
                    """,
                data.uuid, data.id, sdata.ips.toString(" "), sdata.names.toString(" "), data.blockplaced, data.blockbroken,
                data.messagesent, sdata.timesJoined, sdata.timesKicked, (data.banned || sdata.banned),
                data.banneduntil, data.rank
        ))).setEphemeral(true).queue();
    }
}
