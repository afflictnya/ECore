package org.ecore.discord.commands;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.awt.*;

public class DiscordCommandLoader {
    protected static Seq<DiscordCommand> allCommands = new Seq<>();
    public static void loadCommands(JDA jda, DiscordCommand... commands){
        CommandListUpdateAction updater = jda.updateCommands();
        for (var cmd : commands){
            var jdaCommand = Commands.slash(cmd.name, cmd.description).setContexts(InteractionContextType.GUILD);
            cmd.options.each(jdaCommand::addOptions);
            updater.addCommands(jdaCommand);
            allCommands.add(cmd);
        }
        updater.queue();
    }

    public static void handleInteraction(SlashCommandInteractionEvent event){
        var server = event.getOption("server");
        if (server == null) return;
        if(!server.getAsString().equals(Core.settings.get("name", ""))) return;
        var command = allCommands.find(c -> c.name.equals(event.getName()));
        if (command == null){
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Error")
                            .setDescription("Command not found!")
                            .build()
            ).queue();
            return;
        }
        var member = event.getMember();
        if (member == null) return;
        if (!command.allowedRoles.isEmpty()){
            boolean allow = false;
            for (var role: member.getRoles()){
                if (command.allowedRoles.contains(role.getIdLong())){
                    allow = true;
                    break;
                }
            }
            if (!allow){
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Error")
                                .setDescription("You don't have permissions to use this command!")
                                .build()
                ).queue();
                return;
            }
        }
        try {
            command.run(event);
        } catch (DiscordCommandEx e) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Error")
                            .setDescription(e.getMessage())
                            .build()
            ).queue();
        } catch (Exception e) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Error")
                            .setDescription("An internal error occurred while executing this command.")
                            .build()
            ).queue();
            Log.err(e);
        }
    }
}
