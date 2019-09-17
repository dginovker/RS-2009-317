package plugin.npc.osrs.vetion;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * Created by Stan van der Bend on 11/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.vetion
 */
public class VetionNPC extends AbstractNPC{

    private final static int
        FIRST_FORM = 26611,
        SECOND_FORM = 26612;
    private final static Location
        SPAWN_LOCATION = new Location(3291, 3830, 0);

   // private final VetionCombat vetionCombat = new VetionCombat();
    private boolean morphed = false;
    private boolean hasSpawnedMinions = false;
    private int aliveMinions = 0;



    public VetionNPC() {
        super(FIRST_FORM, SPAWN_LOCATION);
    }

    private void spawnHellHounds(){

        sendChat("Bahh! Go, Dogs!!");

        hasSpawnedMinions = true;
        aliveMinions += 2;

        Stream.of(new VetionMinion(this), new VetionMinion(this))
            .forEach(VetionMinion::spawn);
    }

    public void onMinionKill() {
        aliveMinions--;
    }

//    @Override
//    public CombatSwingHandler getSwingHandler(boolean swing) {
//        return vetionCombat;
//    }

    @Override
    public void onImpact(Entity entity, BattleState state) {

        if(aliveMinions > 0){
            Optional.ofNullable(entity)
                .filter(victim -> victim instanceof Player)
                .map(Entity::asPlayer)
                .ifPresent(player -> player.getActionSender().sendMessage("Your attacks are useless whilst vet'ion has minions spawned."));
            return;
        }

        if(!hasSpawnedMinions && getSkills().getLifepoints() <= getSkills().getMaximumLifepoints() / 2)
            spawnHellHounds();

        super.onImpact(entity, state);

    }

    @Override
    public void finalizeDeath(Entity killer) {

        if(morphed){
            super.finalizeDeath(killer);
            return;
        }

        sendChat("Do it again!!!");

        getSkills().heal(getSkills().getMaximumLifepoints());
        transform(SECOND_FORM);

        hasSpawnedMinions = false;
        morphed = true;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new VetionNPC();
    }

    @Override
    public int[] getIds() {
        return new int[]{FIRST_FORM, SECOND_FORM};
    }

    public boolean isMorphed() {
        return morphed;
    }
}
class VetionCombat extends MultiSwingHandler {

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        super.visualize(entity, victim, state);
    }



}
