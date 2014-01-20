package org.kenneh.api.wrappers.interactive;

import org.kenneh.api.script.Script;
import org.kenneh.api.script.context.MethodContext;
import org.kenneh.api.script.context.MethodProvider;
import org.kenneh.api.util.Filter;
import org.osbot.script.rs2.model.NPC;

import java.util.*;

/**
 * Created by Kenneth on 1/20/14.
 */
public class Npc extends MethodProvider {

    public Npc(MethodContext ctx) {
        super(ctx);
    }

    private final Comparator<NPC> NPC_COMPARATOR = new Comparator<NPC>() {
        @Override
        public int compare(NPC o1, NPC o2) {
            return ctx.getScript().distance(o1) - ctx.getScript().distance(o2);
        }
    };

    public NPC[] getLoaded() {
        final List<NPC> npcList = ctx.getClient().getLocalNPCs();
        return npcList.toArray(new NPC[npcList.size()]);
    }

    public NPC[] getLoaded(final Filter<NPC> filter) {
        final NPC[] loaded = getLoaded();
        final List<NPC> accepted = new ArrayList<>();
        for(NPC npc : loaded)
            if(filter.accept(npc) && !accepted.contains(npc))
                accepted.add(npc);
        return accepted.toArray(new NPC[accepted.size()]);
    }

    public NPC[] getLoaded(final String... names) {
        final List<NPC> accepted = new ArrayList<>();
        final Filter<NPC> nameFilter = new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                final List<String> npcNames = Arrays.asList(names);
                if(npcNames.contains(npc.getName())) {
                    return true;
                }
                return false;
            }
        };
        for(NPC npc : getLoaded(nameFilter)) {
            if(!accepted.contains(npc))
                accepted.add(npc);
        }
        Collections.sort(accepted, NPC_COMPARATOR);
        return accepted.toArray(new NPC[accepted.size()]);
    }

    public NPC getNearest(final String... names) {
        final List<NPC> sorted = Arrays.asList(getLoaded(names));
        return sorted.size() > 0 ? sorted.get(0) : null;
    }

}
