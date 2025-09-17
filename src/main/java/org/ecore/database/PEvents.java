package org.ecore.database;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.net.Packets;

public class PEvents {
    public PEvents() {
        Events.on(EventType.PlayerChatEvent.class, e -> {
            var data = Cache.forceGet(e.player.uuid());
            if (data == null) return;
            data.messagesent++;
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
        });
        Events.on(EventType.PlayerLeave.class, e -> {
            if (Cache.contais(e.player)) {
                Cache.remove(e.player).save();
            }
        });
    }
}
