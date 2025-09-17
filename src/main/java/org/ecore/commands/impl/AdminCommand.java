package org.ecore.commands.impl;

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

        }
    }
}
