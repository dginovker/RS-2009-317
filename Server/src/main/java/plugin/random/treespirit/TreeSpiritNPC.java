package plugin.random.treespirit;

import java.security.SecureRandom;

import org.gielinor.game.content.skill.member.farming.patch.Herbs;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The Tree spirit NPC.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class TreeSpiritNPC extends AbstractNPC {

    /**
     * The player.
     */
    protected Player player;

    /**
     * The random event instance.
     */
    public TreeSpiritRandomEvent event;

    /**
     * Constructs a new {@code TreeSpiritNPC} {@code Object}.
     */
    public TreeSpiritNPC() {
        this(438, null);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = ((Player) killer);
            SecureRandom secureRandom = new SecureRandom();
            int[] RANDOM_AXE = new int[]{ 1353, 1355, 1357, 1359 };
            Item[] SEED_DROPS = new Item[]{ Herbs.TOAD_FLAX.getFarmingNode().getSeed(), Herbs.IRIT.getFarmingNode().getSeed(), new Item(5298), Herbs.KWUARM.getFarmingNode().getSeed(), Herbs.SNAPDRAGON.getFarmingNode().getSeed(), Herbs.CADANTINE.getFarmingNode().getSeed(), Herbs.LANTADYME.getFarmingNode().getSeed(), Herbs.DWARF.getFarmingNode().getSeed(), Herbs.TORSTOL.getFarmingNode().getSeed() };
            GroundItemManager.create(new Item(RANDOM_AXE[secureRandom.nextInt(RANDOM_AXE.length)], 1), getLocation(), player);
            GroundItemManager.create(SEED_DROPS[secureRandom.nextInt(SEED_DROPS.length)], getLocation(), player);
            if (secureRandom.nextBoolean()) {
                GroundItemManager.create(SEED_DROPS[secureRandom.nextInt(SEED_DROPS.length)], getLocation(), player);
            }
        }
    }

    /**
     * Constructs a new {@code TreeSpiritNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public TreeSpiritNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        setRespawn(false);
        graphics(Graphics.create(179, 100));
        sendChat("Leave these woods and never return!");
        World.submit(new Pulse(4) {

            @Override
            public boolean pulse() {
                getProperties().getCombatPulse().attack(player);
                return true;
            }
        });
    }

    @Override
    public void handleTickActions() {
        if (player == null) {
            return;
        }
        if (!getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
        if (!player.isActive() || !getLocation().withinDistance(player.getLocation(), 10)) {
            clear();
        }
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return new MeleeSwingHandler();
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity instanceof Player && entity != player) {
            ((Player) entity).getActionSender().sendMessage("It's not after you.");
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        return victim == player;
    }

    @Override
    public void onRegionInactivity() {
        super.onRegionInactivity();
        clear();
    }

    @Override
    public void clear() {
        super.clear();
        if (event != null) {
            event.terminate();
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new TreeSpiritNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 438, 439, 440, 441, 442, 443 };
    }

}
