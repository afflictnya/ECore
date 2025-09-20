package org.ecore.discord.commands.impl;

import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.net.Administration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class FindDiscord extends DiscordCommand {
    public FindDiscord() {
        super("search", "search players by name");
        options.add(new OptionData(STRING, "name", "player name")
                .setRequired(true));
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        allowedRoles.add(1144573850688966736L);

    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        String name = event.getOption("name").getAsString();
        ObjectSet<Administration.PlayerInfo> infos = Vars.netServer.admins.searchNames(name);
        StringBuilder out = new StringBuilder("```Empty.");
        if(infos.size > 0){
            out = new StringBuilder("```Players found: " + infos.size);
            int i = 0;
            for(Administration.PlayerInfo info : infos){
                out.append("- [")
                   .append(i)
                   .append("] '")
                   .append(info.plainLastName())
                   .append("' / ")
                   .append(info.id);
            }
        }
        event.reply(out + "```").setEphemeral(true).queue();
    }
}
