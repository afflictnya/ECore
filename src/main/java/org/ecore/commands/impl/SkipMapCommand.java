package org.ecore.commands.impl;

import mindustry.gen.Player;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.usefull.SkipMapVoteSession;

import static org.ecore.usefull.SkipMapVoteSession.currentlyMapSkipping;

public class SkipMapCommand extends AbstractCommand {
    public SkipMapCommand(){
        super("skipmap", "Начать голосование за пропуск карты");
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        if (currentlyMapSkipping == null) {
            SkipMapVoteSession session = new SkipMapVoteSession(null);
            session.vote(player, 1);
            currentlyMapSkipping = session;
        } else throw new CommandException("Голосование уже идет. [gold]/smvote y/n");
    }
}
