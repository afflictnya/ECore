package org.ecore;

import arc.files.Fi;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.mod.Plugin;
import org.ecore.commands.CommandManager;
import org.ecore.commands.impl.*;
import org.ecore.database.Database;
import org.ecore.database.PEvents;
import org.ecore.discord.Bot;


public class Main extends Plugin {
    @Override
    public void init(){
        Log.info("&lm[ECORE]:&lw loaded.");
        try {
            Bot.load(new Fi("./token.txt").readString());
            PVars.database = new Database(Vars.dataDirectory.child("db.sql").absolutePath());
            PVars.database.create();
            new PEvents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void registerClientCommands(CommandHandler handler) {
        CommandManager.registerCommands(handler,
                new JsCommand(),
                new AdminCommand(),
                new StatsCommand(),
                new ToggleJsCommand(),
                new ToggleAddingCommand(),
                new SkipMapCommand(),
                new SmvoteCommand(),
                new BanCommand(),
                new UnbanCommand(),
                new PardonCommand()
                );
    }
}