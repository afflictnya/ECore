package org.ecore.database;

import static org.ecore.PVars.*;
public class PlayerData {
    public String uuid;
    public String rank = "def"; // def, adding, js, ultra
    public int id;
    public int blockplaced = 0;
    public int blockbroken = 0;
    public int timeplayed = 0;
    public int messagesent = 0;
    public long banneduntil = 0;
    public boolean banned = false;
    public PlayerData(String uuid) {
        this.uuid = uuid;

    }
    public PlayerData(int id, String uuid, int blockplaced,int blockbroken,int timeplayed, int messagesent, int banneduntil,  boolean banned, String rank) {
        this.uuid = uuid;
        this.id = id;
        this.blockbroken = blockbroken;
        this.banned = banned;
        this.banneduntil = banneduntil;
        this.timeplayed = timeplayed;
        this.blockplaced = blockplaced;
        this.messagesent = messagesent;
        this.rank = rank;
    }
    public void save() {
        database.update(this.id, this.uuid, this.banned, this.rank, this.blockplaced, this.blockbroken, this.timeplayed,
                this.messagesent, this.banneduntil);
        Cache.set(uuid, this);
    }
    public void create(){
        database.add(this.uuid, this.banned, this.rank, this.blockplaced, this.blockbroken, this.timeplayed,
                this.messagesent, this.banneduntil);

    }
    public void saveh() {
        database.update(this.id, this.uuid, this.banned, this.rank, this.blockplaced, this.blockbroken, this.timeplayed,
                this.messagesent, this.banneduntil);
    }
}
