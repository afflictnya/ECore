package org.ecore.discord.commands.impl;

import arc.Core;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Packets;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.database.Cache;
import org.ecore.discord.Bot;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;
import org.ecore.usefull.Utils;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Pattern;

import static arc.util.Strings.parseInt;
import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static org.ecore.PVars.database;
import static org.ecore.PVars.ipPattern;

public class BanDiscord extends DiscordCommand {

    public  BanDiscord() {
        super("ban", "ban someone by uuid or ip");
        options.add(new OptionData(STRING, "id", "ip/uuid")
                .setRequired(true));
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        options.add(new OptionData(STRING, "time", "time of ban (empty == perm)"));
        allowedRoles.add(1144573850688966736L);
    }
    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        String id = event.getOption("id").getAsString();
        long time = -1;
        if (event.getOption("time") != null){
            time = Utils.toMillis(event.getOption("time").getAsString());
        }
        if (id.length() == 24 && id.endsWith("==")){
            var data = Cache.forceGet(id);
            data.banned = true;
            data.banneduntil = (time == -1?-1:Time.millis() + time);
            data.save();
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Success")
                            .setDescription("Successfully banned " + data.uuid)
                            .build()
            ).queue();
            Player target = Groups.player.find(p -> p.uuid().equals(data.uuid));
            if (target != null) target.kick(Packets.KickReason.banned);
            Bot.sendBan(
                    new EmbedBuilder()
                            .setColor(0xff0000)
                            .setTitle("Ban")
                            .addField("UUID", data.uuid, false)
                            .addField("ID", String.valueOf(data.id), false)
                            .addField("Name", Optional.ofNullable(Vars.netServer.admins.getInfo(data.uuid)).map(i -> i.lastName).orElse("???"), false)
                            .addField("Admin", event.getMember().getEffectiveName(), false)
                            .addField("Unban time", Utils.formatTime(data.banneduntil), false)
                            .addField("Server", Core.settings.getString("name", "???"), false)
            );
        } else if (ipPattern.matcher(id).matches()){
            Vars.netServer.admins.banPlayerIP(id);
            Player target = Groups.player.find(p -> p.ip().equals(id));
            if (target != null) target.kick(Packets.KickReason.banned);
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Success")
                            .setDescription("Successfully banned " + id + ". \n\n**NOTE: ban is permanent.**")
                            .build()
            ).queue();
            Bot.sendBan(
                    new EmbedBuilder()
                            .setColor(0xff0000)
                            .setTitle("Ban")
                            .addField("IP", id, false)
                            .addField("Name", Optional.ofNullable(Vars.netServer.admins.findByIP(id)).map(i -> i.lastName).orElse("???"), false)
                            .addField("Admin", event.getMember().getEffectiveName(), false)
                            .addField("Server", Core.settings.getString("name", "???"), false)
            );
        } else if (Strings.canParseInt(id)) {
            var data = database.getByID(parseInt(id));
            if (data == null) throw new DiscordCommandEx("cant find player with id " + id);
            data.banned = true;
            data.banneduntil = (time == -1?-1:Time.millis() + time);
            data.save();
            Player target = Groups.player.find(p -> p.uuid().equals(data.uuid));
            if (target != null) target.kick(Packets.KickReason.banned);
            Bot.sendBan(
                    new EmbedBuilder()
                            .setColor(0xff0000)
                            .setTitle("Ban")
                            .addField("UUID", data.uuid, false)
                            .addField("ID", String.valueOf(data.id), false)
                            .addField("Name", Optional.ofNullable(Vars.netServer.admins.getInfo(data.uuid)).map(i -> i.lastName).orElse("???"), false)
                            .addField("Admin", event.getMember().getEffectiveName(), false)
                            .addField("Unban time", Utils.formatTime(data.banneduntil), false)
                            .addField("Server", Core.settings.getString("name", "???"), false));
            event.reply("successfully banned " + data.uuid).queue();
        } else {
            event.reply("Incorrect input!").queue();
        }
    }
}
