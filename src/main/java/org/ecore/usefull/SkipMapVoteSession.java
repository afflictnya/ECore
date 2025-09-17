package org.ecore.usefull;

import arc.Events;
import arc.struct.ObjectSet;
import arc.util.Strings;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;

public class SkipMapVoteSession {
    public static SkipMapVoteSession currentlyMapSkipping;
    float voteDuration = 3 * 60;
    public ObjectSet<String> voted = new ObjectSet<>();
    public SkipMapVoteSession map;
    Timer.Task task;
    int votes;

    int votesRequiredSkipmap;
    public int votesRequiredSkipmap() {
        if (Groups.player.size() == 1)
            return 1;
        if (Groups.player.size() == 2)
            return 2;
        return (int) Math.ceil(Groups.player.size() * 0.45);
    }

    public SkipMapVoteSession(SkipMapVoteSession map) {
        this.map = map;
        votesRequiredSkipmap = votesRequiredSkipmap();
        this.task = Timer.schedule(() -> {
            if (!checkPass()) {
                Call.sendMessage(
                        "[lightgray]Голосование закончилось. Недостаточно голосов, чтобы пропустить карту");
                stopSkipmapVoteSession();
                task.cancel();
            }
        }, voteDuration);
    }

    public void vote(Player player, int d) {
        votes += d;
        voted.addAll(player.uuid(), Vars.netServer.admins.getInfo(player.uuid()).lastIP);
        Call.sendMessage(Strings.format("[" + player.coloredName() + "]@[lightgray] проголосовал "
                        + (d > 0 ? "[green]за" : "[red]против")
                        + "[] пропуска карты[accent] (@/@)\n[lightgray]Напишите[orange] /smvote <y/n>[], чтобы проголосовать [green]за[]/[red]против",
                player.name, votes, votesRequiredSkipmap));
        checkPass();
    }
    boolean checkPass() {
        if (votes >= votesRequiredSkipmap) {
            Call.sendMessage("[gold]Голосование закончилось. Карта успешно пропущена!");
            Events.fire(new EventType.GameOverEvent(Team.derelict));
            map = null;
            task.cancel();
            return true;
        }
        return false;
    }

    void update() {
        votesRequiredSkipmap = votesRequiredSkipmap();
    }
    public void stopSkipmapVoteSession() {
        currentlyMapSkipping = null;
    }
}


