package org.ecore.database;

import arc.Events;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.net.Packets;
import org.ecore.discord.Webhooks;

public class PEvents {
    public PEvents() {
        Events.on(EventType.PlayerChatEvent.class, e -> {
            var data = Cache.forceGet(e.player.uuid());
            if (data == null) return;
            data.messagesent++;
            Webhooks.chat.sendMessage("`[" + e.player.plainName().replace("`", "") + "]: " + e.message.replace("`", "") + " `");
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
            if (data.banned && data.banneduntil > Time.millis()) e.player.kick(Packets.KickReason.banned, 1);
            else if (data.banned) {
                data.banned = false;
            }
            data.save();
            Webhooks.chat.sendMessage("`" + e.player.plainName().replace("`", "") + " connected! " + e.player.uuid() + "`");
        });
        Events.on(EventType.PlayerLeave.class, e -> {
            if (Cache.contais(e.player)) {
                Cache.remove(e.player).save();
                Webhooks.chat.sendMessage("`" + e.player.plainName().replace("`", "") + " left! " + e.player.uuid() + "`");
            }
        });

        Events.on(EventType.AdminRequestEvent.class, e -> {
            if (e.action == Packets.AdminAction.ban) {
                Webhooks.bans.sendEmbed("Ban", 14177041, ObjectMap.of(
                        "Name", e.other.plainName(),
                        "UUID", e.other.uuid(),
                        "IP", e.other.ip(),
                        "ID", Cache.forceGet(e.other.uuid()).id,
                        "Admin", e.player.plainName()
                ));
            }
        });
    }
}
