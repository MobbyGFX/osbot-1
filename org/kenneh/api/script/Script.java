package org.kenneh.api.script;

import org.kenneh.api.script.context.MethodContext;
import org.kenneh.api.util.Timer;

import java.awt.*;

/**
 * Created by Kenneth on 1/20/14.
 */
public abstract class Script extends org.osbot.script.Script {

    public MethodContext ctx;
    private final Timer RUNTIME_TIMER = new Timer(0);
    private String status = "Starting up";

    public MethodContext getContext() {
        return ctx;
    }

    public String getRuntime() {
        return RUNTIME_TIMER.toElapsedString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public int onLoop() throws InterruptedException {
        if(ctx == null) {
            log("Set context.");
            ctx  = new MethodContext(this);
        }
        try {
            return loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 300;
    }

    @Override
    public void onExit() throws InterruptedException {
        onStop();
    }

    @Override
    public void onPaint(Graphics a) {
        final Graphics2D graphics2D = (Graphics2D) a;
        onRepaint(graphics2D);
    }

    public abstract void onStart();
    public abstract void onRepaint(Graphics2D graphics2D);
    public abstract int loop() throws Exception;
    public abstract void onStop();
}
