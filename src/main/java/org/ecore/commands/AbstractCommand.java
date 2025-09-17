package org.ecore.commands;

import mindustry.gen.Player;

public abstract class AbstractCommand {
    public String name;
    public String desc;
    public String args = "";
    public String requiredRank = null;
    public boolean adminOnly;

    public AbstractCommand(String name, String desc, String args){
        this.name = name;
        this.desc = desc;
        this.args = args;
    }
    public AbstractCommand(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public abstract void run(Player player, String[] args) throws CommandException;
}
