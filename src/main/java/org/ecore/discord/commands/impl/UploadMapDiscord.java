package org.ecore.discord.commands.impl;

import arc.util.Http;
import arc.util.Threads;
import mindustry.Vars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class UploadMapDiscord extends DiscordCommand {

    private static final byte[] header = {'M', 'S', 'A', 'V'};
    public  UploadMapDiscord() {
        super("uploadmap", "Uploads a map to server");
        allowedRoles.add(1253359340547739648L);
        options.add(new OptionData(OptionType.ATTACHMENT, "file", "the map to be uploaded", true));
        options.add(new OptionData(STRING, "server", "name of server", true));
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        var attachment = event.getOption("file").getAsAttachment();
        if (!attachment.getFileExtension().equals("msav")) throw new  DiscordCommandEx("You can upload only maps!");
        event.deferReply().queue();
        Http.get(attachment.getUrl())
                .error(err -> event.getHook().sendMessage(err.getMessage()).queue())
                .submit(resp -> {
                    byte[] result = resp.getResult();
                    if (result.length <= header.length) {
                        event.getHook().sendMessage("Invalid or corrupted map file").queue();
                        return;
                    }
                    for (int i = 0; i < header.length; i++){
                        if (result[i] != header[i]) {
                            event.getHook().sendMessage("Invalid or corrupted map file").queue();
                            return;
                        }
                    }
                    Vars.customMapDirectory.child(attachment.getFileName()).writeBytes(result);
                    Vars.maps.reload();
                    event.getHook().sendMessage("Successfully uploaded map " + attachment.getFileName()).queue();
                });
    }
}
