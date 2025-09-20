package org.ecore.discord.commands;

import arc.struct.Seq;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class DiscordCommand {
    public String name;
    public String description;
    public Seq<OptionData> options;

    public Seq<Long> allowedRoles = new Seq<>();
    public DiscordCommand(String name, String desc){
        this.name = name;
        this.description = desc;
    }


    public abstract void run(SlashCommandInteractionEvent event) throws DiscordCommandEx;
}
