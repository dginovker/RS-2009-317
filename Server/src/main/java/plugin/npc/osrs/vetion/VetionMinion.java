package plugin.npc.osrs.vetion;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;

/**
 * Created by Stan van der Bend on 11/01/2018.
 * project: GielinorGS
 * package: plugin.npc.osrs.vetion
 */
class VetionMinion extends NPC {

    private final static int
        NPC_ID_CB_214 = 6613,
        NPC_ID_CB_281 = 6614;

    private final VetionNPC instance;

    VetionMinion(VetionNPC vetionNPC) {
        super(vetionNPC.isMorphed() ? NPC_ID_CB_281 : NPC_ID_CB_214, vetionNPC.getLocation().setRandomizer(3));
        this.instance = vetionNPC;
        setAggressive(true);
    }


    void spawn(){

        init();

        sendChat("GRRRRRRR");

    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        instance.onMinionKill();
    }

}
