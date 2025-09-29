package org.ecore.commands.impl;

import mindustry.gen.Player;
import org.ecore.PVars;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.discord.Bot;

public class ToggleAddingCommand extends AbstractCommand {
    public ToggleAddingCommand(){
        super("toggleadding", "huy chlen", "<uuid>");
        adminOnly = true;
        requiredRank = "ultra";
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        var data = PVars.database.getByUUID(args[0]);
        if (data == null) throw new CommandException("Cant find this player");
        if (data.rank.equals("ultra") || data.rank.equals("js")) throw new CommandException("player`s rank higher then adding;");
        data.rank = (data.rank.equals("adding")?"def":"adding");
        data.save();
        player.sendMessage("successfully edited rank");
        Bot.importantLog(player.plainName() + "(" + player.uuid() + ") changed " + data.uuid + " (" + data.id +")'s rank to " + data.rank);
    }
}
