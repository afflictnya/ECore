package org.ecore.commands.impl;

import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.database.Cache;
import org.ecore.database.PlayerData;

public class BanCommand extends AbstractCommand {
    public BanCommand(){
        super("ban", "ban player", "<iporuuid> <days>");
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        String text = args[0];
        if (!Strings.canParseInt(args[1])) throw new CommandException("2nd arg must be int");
        long unbanTimestamp = Time.millis() + Integer.parseInt(args[1]) * 24 * 3600 * 1000L;
        PlayerData data;
        if (text.length() == 24 && text.endsWith("==")) {
            data = Cache.forceGet(text);
        } else {
            try {
                data = Cache.forceGet(Vars.netServer.admins.findByIP(text).id);
            } catch (Exception e) {
                throw new CommandException("cant find player with this ip");
            }
        }
        data.banned = true;
        data.banneduntil = unbanTimestamp;
        data.save();
        player.sendMessage("successfully banned " + data.id);
    }
}
