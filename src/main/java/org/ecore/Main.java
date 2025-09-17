package org.ecore;

import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import org.ecore.commands.CommandManager;
import org.ecore.commands.impl.*;
import org.ecore.database.Cache;
import org.ecore.database.Database;
import org.ecore.database.PEvents;

import java.sql.SQLException;

public class Main extends Plugin {
    @Override
    public void init(){
        Log.info("&lm[ECORE]:&lw loaded.");
        try {
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
                new SqlCommand(),
                new SkipMapCommand(),
                new SmvoteCommand()
                );
    }
}