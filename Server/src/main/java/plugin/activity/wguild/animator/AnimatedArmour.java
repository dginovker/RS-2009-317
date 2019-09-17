package plugin.activity.wguild.animator;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;

import plugin.activity.wguild.animator.AnimationRoom.ArmourSet;

/**
 * Represents an animated armour.
 *
 * @author Emperor
 */
public final class AnimatedArmour extends NPC {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The armour set.
     */
    private final ArmourSet set;

    /**
     * If the set is running.
     */
    private boolean running;

    /**
     * Constructs a new {@code AnimatedArmour} {@code Object}.
     *
     * @param player   The player.
     * @param location The location to spawn.
     * @param set      The armour set.
     */
    protected AnimatedArmour(Player player, Location location, ArmourSet set) {
        super(set.getNpcId(), location);
        this.player = player;
        this.set = set;
    }

    @Override
    public void init() {
        super.init();
        animate(Animation.create(4166));
        sendChat("I'M ALIVE!");
        getProperties().getCombatPulse().attack(player);
        HintIconManager.registerHintIcon(player, this);
    }

    @Override
    public void clear() {
        super.clear();
        player.getHintIconManager().clear();
    }

    @Override
    public void handleTickActions() {
        if (!running && !getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        if (!running) {
            if (getSkills().getLifepoints() < (getSkills().getMaximumLifepoints() / 10) && RandomUtil.randomize(10) < 2) {
                running = true;
                getProperties().getCombatPulse().stop();
                Pathfinder.find(location, Location.create(2849, 3534, 0)).walk(this);
                return;
            }
            super.onImpact(entity, state);
        }
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity != player) {
            if (entity instanceof Player) {
                ((Player) entity).getActionSender().sendMessage("This isn't your armour to attack.");
            }
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        clear();
        boolean takenPiece = false;
        boolean canTake = RandomUtil.random(200) == 1;
        int index = 0;
        int takeIndex = 0;
        if (canTake) {
            takeIndex = RandomUtil.random(set.getPieces().length);
        }
        for (int piece : set.getPieces()) {
            if (canTake && !takenPiece && index == takeIndex) {
                takenPiece = true;
                player.getActionSender().sendMessage("Your armour was destroyed in the fight.");
                continue;
            }
            GroundItemManager.create(new Item(piece), location, player);
            index++;
        }
        if (killer != null) { //Indicates the player actually killed the armour.
            GroundItemManager.create(new Item(8851, set.getTokenAmount()), location, player);
        }
        player.removeAttribute("animated_set");
    }
}