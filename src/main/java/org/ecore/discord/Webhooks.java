package org.ecore.discord;

import arc.util.serialization.JsonReader;
import mindustry.Vars;

public class Webhooks {
    public static Webhook bans;
    public static Webhook chat;

    public static void load(){
        var cfg = new JsonReader().parse(Vars.dataDirectory.child("cfg.json"));
        bans = new Webhook(cfg.getString("bans"));
        chat = new Webhook(cfg.getString("chat"));
    }
}
