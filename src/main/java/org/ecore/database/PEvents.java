package org.ecore.database;

import arc.Events;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.net.Packets;
import net.dv8tion.jda.api.EmbedBuilder;
import org.ecore.discord.Bot;

public class PEvents {
    public PEvents() {
        Events.on(EventType.PlayerChatEvent.class, e -> {
            var data = Cache.forceGet(e.player.uuid());
            if (data == null) return;
            data.messagesent++;
            Bot.sendLogsMessage("`[" + e.player.plainName().replace("`", "") + "]: " + e.message.replace("`", "") + " `");
        });
        Events.on(EventType.BlockBuildEndEvent.class, e -> {
            if (e.unit == null || e.unit.getPlayer() == null) return;
            var data = Cache.forceGet(e.unit.getPlayer().uuid());
            if (data == null) return;
            if (e.breaking) data.blockbroken++;
            else data.blockplaced++;
        });
        Timer.schedule(() -> Groups.player.each(player -> {
            var date = Cache.forceGet(player.uuid());
            if (date == null) return;
            date.timeplayed+=25;
            date.save();
        }),0, 25);
        Events.on(EventType.PlayerConnect.class, e -> {
            var data = Cache.forceGet(e.player.uuid());
            if (data.banned && (data.banneduntil > Time.millis() || data.banneduntil == -1)) e.player.kick(Packets.KickReason.banned, 1);
            else if (data.banned) {
                data.banned = false;
            }
            data.save();
            Bot.sendLogsMessage("`" + e.player.plainName().replace("`", "") + " connected! " + e.player.uuid() + "`");
        });
        Events.on(EventType.PlayerLeave.class, e -> {
            if (Cache.contais(e.player)) {
                Cache.remove(e.player).saveh();
                Bot.sendLogsMessage("`" + e.player.plainName().replace("`", "") + " left! " + e.player.uuid() + "`");
            }
        });

        Events.on(EventType.AdminRequestEvent.class, e -> {
            if (e.action == Packets.AdminAction.ban) {
                Bot.sendBan(new EmbedBuilder()
                        .setColor(0xff0000)
                        .setTitle("Ban")
                        .addField("UUID", e.other.uuid(), false)
                        .addField("IP", String.valueOf(Cache.forceGet(e.other.uuid()).id), false)
                        .addField("IP", e.other.ip(), false)
                        .addField("Name", e.other.plainName(), false)
                        .addField("Admin", e.player.plainName(), false)
                );
            }
        });
    }
}
