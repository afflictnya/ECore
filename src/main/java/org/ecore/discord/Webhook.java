package org.ecore.discord;

import arc.struct.ObjectMap;
import arc.util.Http;
import arc.util.Strings;

public class Webhook {
    protected final String webhookUrl;
    public Webhook(String url) {
        this.webhookUrl = url;
    }
    public void sendMessage(String message){
        Http.post(webhookUrl,"{\"content\": \"" + format(message) + "\"}")
                .header("Content-Type", "application/json")
                .error(e -> {})
                .submit(r -> {});
    }
    public void sendEmbed(String title, int color, ObjectMap<String, String> fields) {
        Http.post(webhookUrl, Strings.format("""
                        {
                          "embeds": [{
                            "title": "@",
                            "color": @,
                            "fields": [
                                @
                            ]
                          }]
                        }
                        """, title, color, formatFields(fields)))
                .header("Content-Type", "application/json")
                .error(e -> {})
                .submit(r -> {});
    }

    private String format(String s){
        s = s.replace("\"", "").replace("`", "");
        if (s.endsWith("\\")) s += ".";
        return s;
    }

    private String formatFields(ObjectMap<String, String> fields){
        StringBuilder sb = new StringBuilder();
        fields.each((k, v) -> {
            sb.append("{\"name\": \"").append(format(k)).append("\",\"value\":\"").append(format(v)).append("\"},");
        });
        return sb.toString();
    }
}
