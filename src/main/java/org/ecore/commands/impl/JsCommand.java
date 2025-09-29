package org.ecore.commands.impl;

import mindustry.Vars;
import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.discord.Bot;

public class JsCommand extends AbstractCommand {
    public JsCommand() {
        super("js", "Execute JavaScript code", "<code...>");
        this.requiredRank = "js";
        this.adminOnly = true;
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        try {
            player.sendMessage("[gold]" + Vars.mods.getScripts().runConsole(args[0]));
            Bot.sendJsLog(player.plainName() + "(" + player.uuid() + ")", args[0]);
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
