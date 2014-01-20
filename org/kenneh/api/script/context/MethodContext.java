package org.kenneh.api.script.context;

import org.kenneh.api.script.Script;

import org.kenneh.api.wrappers.interactive.Npc;
import org.osbot.script.rs2.Client;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;

/**
 * Created by Kenneth on 1/20/14.
 */
public class MethodContext {

    private Script script;
    public MethodContext(final Script script) {
        this.script = script;
        this.npcs = new Npc(this);
        this.inventory = script.client.getInventory();
        this.bank = script.client.getBank();
    }

    public Script getScript() {
        return script;
    }

    public Client getClient() {
        return script.client;
    }

    public Bank bank;
    public Npc npcs;
    public Inventory inventory;
}
