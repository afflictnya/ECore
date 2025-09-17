package org.ecore.commands.impl;

import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;

public class AdminCommand extends AbstractCommand {
    public AdminCommand(){
        super("admin", "give/remove admin", "<uuidorname...>");
        adminOnly = true;
        requiredRank = "adding";
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        String text = args[0];
        if (text.length() == 24 && text.endsWith("==")){
            var sdata = Vars.netServer.admins.getInfo(text);
            if (sdata == null) throw new CommandException("Cant find this player");
            boolean admin = sdata.admin;
            if (admin) {
                Vars.netServer.admins.unAdminPlayer(text);
            } else Vars.netServer.admins.adminPlayer(text, sdata.adminUsid);
            var target = Groups.player.find(p -> p.uuid().equals(text));
            if (target != null) {
                player.admin(!admin);
            }
        } else {
            var target = Groups.player.find(p -> p.plainName().equalsIgnoreCase(Strings.stripColors(text)));
            if (target == null)throw new CommandException("Cant find this player");
            if (target.admin) {
                Vars.netServer.admins.unAdminPlayer(target.uuid());
                target.admin(false);
            } else {
                Vars.netServer.admins.adminPlayer(target.uuid(), target.usid());
                target.admin(true);
            }
        }
        player.sendMessage("Done.");
    }
}
