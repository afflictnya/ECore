package org.ecore.commands.impl;

import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.Player;
import mindustry.net.Administration;
import org.ecore.PVars;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.database.Cache;
import org.ecore.database.Database;
import org.ecore.database.PlayerData;

import static java.lang.Integer.parseInt;

public class StatsCommand extends AbstractCommand {
    public StatsCommand(){
        super("stats", "See player stats", "<idoruuidorname...>");
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        if (!player.admin) {
            sendInfo(player, Cache.forceGet(player.uuid()), Vars.netServer.admins.getInfo(player.uuid()));
            return;
        }
        String text = args[0];
        if (Strings.canParseInt(text)) {
            PlayerData data = PVars.database.getByID(parseInt(text));
            if (data == null) throw new CommandException("Can`t find player with this ID!");
            var sdata = Vars.netServer.admins.getInfo(data.uuid);
            sendInfo(player, data, sdata);
        } else if (text.length() == 24 && text.endsWith("==")) {
            PlayerData data = PVars.database.getByUUID(text);
            if (data == null) throw new CommandException("Can`t find player with this UUID!");
            var sdata = Vars.netServer.admins.getInfo(data.uuid);
            sendInfo(player, data, sdata);
        } else {
            var set = Vars.netServer.admins.findByName(text);
            if (set.isEmpty()) throw new CommandException("Can`t find this player!");
            var sdata = set.first();
            var data = PVars.database.getByUUID(sdata.id);
            sendInfo(player, data, sdata);
        }
    }

    private void sendInfo(Player req, PlayerData data, Administration.PlayerInfo sdata) {
        req.sendMessage(Strings.format("""
                    UUID: @
                    IP: @
                    Name: @
                    Blocks placed: @
                    Blocks broken: @
                    Messages sent: @
                    Times joined: @
                    Times kicked: @
                    Banned: @
                    Banned until: @
                    Rank: @
                    """,
                data.uuid, sdata.lastIP, sdata.lastName, data.blockplaced, data.blockbroken,
                data.messagesent, sdata.timesJoined, sdata.timesKicked, (data.banned || sdata.banned),
                data.banneduntil, data.rank
        ));
    }
}
