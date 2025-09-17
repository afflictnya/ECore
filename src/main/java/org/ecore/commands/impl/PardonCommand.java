package org.ecore.commands.impl;

import mindustry.Vars;
import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;

public class PardonCommand extends AbstractCommand {
    public PardonCommand(){
        super("pardon", "Pardon player by ip", "<ip>");
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        String ip = args[0];
        Vars.netServer.admins.kickedIPs.remove(ip);
        Vars.netServer.admins.dosBlacklist.remove(ip);
        player.sendMessage("done.");
    }
}
