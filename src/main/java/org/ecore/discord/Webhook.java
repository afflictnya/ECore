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

    private String format(String s) {
        return s == null ? "" : s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r");
    }

    private String formatFields(ObjectMap<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        final boolean[] first = {true};
        fields.each((k, v) -> {
            if (!first[0]) sb.append(",");
            sb.append("{\"name\":\"").append(format(k))
                    .append("\",\"value\":\"").append(format(v)).append("\"}");
            first[0] = false;
        });
        return sb.toString();
    }
}
