package org.ecore.commands.impl;

import arc.struct.ObjectMap;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Packets;
import net.dv8tion.jda.api.EmbedBuilder;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;
import org.ecore.database.Cache;
import org.ecore.database.PlayerData;
import org.ecore.discord.Bot;
import org.ecore.usefull.Utils;

import java.util.Optional;

public class BanCommand extends AbstractCommand {
    public BanCommand(){
        super("ban", "ban player", "<iporuuid> <time>");
        adminOnly = true;
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        String text = args[0];
        long unbanTimestamp = Time.millis() + Utils.toMillis(args[1]);
        PlayerData data;
        if (text.length() == 24 && text.endsWith("==")) {
            data = Cache.forceGet(text);
        } else {
            try {
                data = Cache.forceGet(Vars.netServer.admins.findByIP(text).id);
            } catch (Exception e) {
                throw new CommandException("cant find player with this ip");
            }
        }
        data.banned = true;
        data.banneduntil = unbanTimestamp;
        data.save();
        Player target = Groups.player.find(p -> p.uuid().equals(data.uuid));
        if (target != null) target.kick(Packets.KickReason.banned);
        Bot.sendBan(
                new EmbedBuilder()
                        .setColor(0xff0000)
                        .setTitle("Ban")
                        .addField("UUID", data.uuid, false)
                        .addField("ID", String.valueOf(data.id), false)
                        .addField("Name", Optional.ofNullable(target).map(Player::plainName).orElse("???"), false)
                        .addField("Admin", player.plainName(), false)
                        .addField("Unban time", Utils.formatTime(unbanTimestamp), false)
        );

        player.sendMessage("successfully banned " + data.id);
    }
}
