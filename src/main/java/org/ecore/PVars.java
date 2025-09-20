package org.ecore;

import org.ecore.database.Database;

import java.util.regex.Pattern;

public class PVars {
    public static Database database;
    public static final Pattern ipPattern = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
}
