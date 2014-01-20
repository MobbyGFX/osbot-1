package org.kenneh.scripts;

import org.kenneh.api.script.Script;
import org.kenneh.api.util.Time;
import org.osbot.script.ScriptManifest;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.utility.Condition;

import java.awt.*;

/**
 * Created by Kenneth on 1/20/14.
 */
@ScriptManifest(
        name = "Test Script",
        author = "Kenneh",
        info = "Debugs stuff, I guess.",
        version = 0.000000001
)

public class TestScript extends Script {

    @Override
    public void onStart() {

    }

    @Override
    public void onRepaint(Graphics2D graphics2D) {

    }

    @Override
    public int loop() throws Exception {
        if(ctx.inventory.isFull()) {
            if(!ctx.bank.isOpen()) {
                final NPC banker = ctx.npcs.getNearest("Banker");
                if(banker != null) {
                    if(banker.interact("Bank")) {
                        Time.sleep(new Condition() {
                            @Override
                            public boolean evaluate() {
                                return ctx.bank.isOpen();
                            }
                        }, 2000);
                    } else {
                        // misclicked
                    }
                } else {
                    // banker is null
                }
            } else {
                if(ctx.bank.depositAll()) {
                    Time.sleep(new Condition() {
                        @Override
                        public boolean evaluate() {
                            return ctx.inventory.isEmpty();
                        }
                    }, 2000);
                }
            }
        } else {
            if(ctx.bank.isOpen()) {
                if(ctx.bank.close()) {
                    Time.sleep(new Condition() {
                        @Override
                        public boolean evaluate() {
                            return !ctx.bank.isOpen();
                        }
                    }, 2000);
                }
            }
        }
        return 1000;
    }

    @Override
    public void onStop() {

    }
}
