package org.ecore.commands;

import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.gen.Player;
import org.ecore.database.Cache;

public class CommandManager {
    public static void registerCommands(CommandHandler handler, AbstractCommand... commands) {
        for (var command: commands){
            handler.<Player>register(command.name, command.desc, command.args, (args, player) -> {
                if (command.adminOnly && !player.admin) {
                    player.sendMessage("[#ff]You must be an admin to use this command.");
                    return;
                }
                if (command.requiredRank != null){
                    var data = Cache.forceGet(player.uuid());
                    if (data.rank.equals(command.requiredRank) || data.rank.equals("ultra") || (command.requiredRank.equals("adding") && data.rank.equals("js"))) {
                        try {
                            command.run(player, args);
                        } catch (CommandException ex){
                            player.sendMessage("[#ff]" + ex.getMessage());
                        } catch (Exception e) {
                            player.sendMessage("[#ff]An internal error occurred while running this command.");
                            Log.err(e);
                        }
                    } else player.sendMessage("[#ff]You don`t have permissions to run this command!");
                }
            });
        }
    }
}
