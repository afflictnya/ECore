package org.ecore.commands.impl;

import mindustry.gen.Groups;
import mindustry.gen.Player;
import org.ecore.PVars;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;

import java.util.Objects;

public class ToggleJsCommand extends AbstractCommand {
    public ToggleJsCommand(){
        super("togglejs", "Выдать/забрать JS (по UUID)", "<uuid...>");
        adminOnly = true;
        requiredRank = "ultra";
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        var data = PVars.database.getByUUID(args[0]);
        if (data == null) throw new CommandException("Cant find this player");
        data.rank = (Objects.equals(data.rank, "js") ?"def":"js");
        data.save();
        player.sendMessage("successfully updated.");
    }
}
