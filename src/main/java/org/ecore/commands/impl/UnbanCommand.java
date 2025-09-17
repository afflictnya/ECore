package org.ecore.commands.impl;

import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.Player;
import org.ecore.PVars;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.database.Cache;

public class UnbanCommand extends AbstractCommand {
    public UnbanCommand(){
        super("unban", "unban player", "<idoruuidorip...>");
        adminOnly = true;
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        String text = args[0];
        if (text.length() == 24 && text.endsWith("==")){
            Vars.netServer.admins.unbanPlayerID(text);
            var data = Cache.forceGet(text);
            data.banned = false;
            data.save();
        } else if (Strings.canParseInt(text)){
            var data = PVars.database.getByID(Integer.parseInt(text));
            if (data == null) throw new CommandException("not found");
            data.banned = false;
            data.save();
        } else {
            Vars.netServer.admins.unbanPlayerIP(text);
        }
        player.sendMessage("[lime]Unbanned.");
    }
}
