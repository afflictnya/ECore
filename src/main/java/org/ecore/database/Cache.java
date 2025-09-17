package org.ecore.database;

import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.gen.Player;
import org.ecore.PVars;

public class Cache {
    public static ObjectMap<String, PlayerData> cache = new ObjectMap<>();
    public static PlayerData forceGet(String uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        var d =  PVars.database.getByUUID(uuid);
        if (d == null) {
            d = new PlayerData(uuid);
            d.create();
        }
        cache.put(uuid, d);
        return d;
    }
    public static boolean contais(Player p) {
        return cache.containsKey(p.uuid());
    }
    public static PlayerData remove(Player p) {
        return cache.remove(p.uuid());
    }
}
