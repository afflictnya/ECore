package org.ecore.commands.impl;

import mindustry.Vars;
import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;

import static org.ecore.usefull.SkipMapVoteSession.currentlyMapSkipping;

public class SmvoteCommand extends AbstractCommand {
    public SmvoteCommand(){
        super("smvote", "Проголосовать за/против пропуск карты", "<y/n>");
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        if (currentlyMapSkipping == null) {
            player.sendMessage("[scarlet]Nobody is being voted on.");
        } else {
            if (player.isLocal()) {
                player.sendMessage("[scarlet]Локальные игроки не могут голосовать.");
                return;
            }
            if ((currentlyMapSkipping.voted.contains(player.uuid()) || currentlyMapSkipping.voted
                    .contains(Vars.netServer.admins.getInfo(player.uuid()).lastIP))) {
                player.sendMessage("[scarlet]Ты уже проголосовал. Молчи!");
                return;
            }
            String voteSign = args[0].toLowerCase();
            int sign = 0;
            if (voteSign.equals("y"))
                sign = +1;
            if (voteSign.equals("n"))
                sign = -1;
            if (sign == 0) {
                player.sendMessage("[scarlet]Голосуйте либо \"y\" (да), либо \"n\" (нет)");
                return;
            }
            currentlyMapSkipping.vote(player, sign);
        }
    }
}
