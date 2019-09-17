package plugin.npc.osrs.zulrah;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Zulrah NPC.
 *
 * @author Vexia
 */
public class ZulrahNPC extends AbstractNPC {

    /**
     * The animation used for shooting projectiles.
     */
    public static final Animation SHOOT_ANIMAION = new Animation(25069);

    /**
     * The animation used for going into water.
     */
    public static final Animation DOWN_ANIMATION = new Animation(25072);

    /**
     * The animation used for coming out of water.
     */
    public static final Animation UP_ANIMATION = new Animation(25071);

    /**
     * The snakeling projectile id.
     */
    public static final int SNAKELING_PROJECTILE = 21047;

    /**
     * The toxic cloud projectile id.
     */
    public static final int TOXIC_PROJECTILE = 21045;

    /**
     * The swing handler.
     */
    private final ZulrahCombatHandler swingHandler = new ZulrahCombatHandler();

    /**
     * The pattern zulrah will use.
     */
    private final ZulrahPattern pattern;

    /**
     * A list of snakes.
     */
    private final List<ZulrahSnake> snakes = new ArrayList<>();

    /**
     * The delay between switching patterns.
     */
    private int patternDelay;

    /**
     * The base.
     */
    private Location base;

    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new @{Code ZulrahNPC} object.
     */
    public ZulrahNPC() {
        this(-1, null, null);
    }

    /**
     * Constructs a new @{Code ZulrahNPC} object.
     *
     * @param id       The id.
     * @param location The location.
     */
    public ZulrahNPC(int id, Location location, ZulrahPattern pattern) {
        super(id, location);
        this.pattern = pattern;
    }

    @Override
    public void init() {
        super.init();
        setPatternDelay();
        animate(UP_ANIMATION);
        //pattern.toxic(this);
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (patternDelay < World.getTicks()) {
            setPatternDelay();
            pattern.next(this);
        }
    }

    @Override
    public void handleDrops(Player p, Entity killer) {
        if (getDefinition().getDropTables() != null) {
            getDefinition().getDropTables().drop(this, killer, p.getLocation());
            getDefinition().getDropTables().createDrop(new Item(32938), p, this, p.getLocation());
        }
    }

    @Override
    public void sendImpact(BattleState state) {
        super.sendImpact(state);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        killSnakes();
        super.finalizeDeath(killer);
    }

    void killSnakes() {
        for (ZulrahSnake zulrahSnake : snakes) {
            zulrahSnake.clear();
        }
        snakes.clear();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        ZulrahPattern pattern = objects.length > 0 && objects[0] instanceof ZulrahPattern ? (ZulrahPattern) objects[0] : ZulrahPattern.getPattern();
        return new ZulrahNPC(id, location, pattern);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return swingHandler;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 22043, 22044, 22042 };
    }

    /**
     * Sets the pattern delay.
     */
    private void setPatternDelay() {
        patternDelay = World.getTicks() + RandomUtil.random(20, 30);
    }

    /**
     * Gets the type of zulrah.
     *
     * @return The zulrah type.
     */
    public ZulrahType getType() {
        return pattern.getCurrentSpot().getType();
    }

    /**
     * Gets the zulrah spot.
     *
     * @return The zulrah spot.
     */
    public ZulrahSpot getSpot() {
        return pattern.getCurrentSpot();
    }

    /**
     * @return the pattern
     */
    public ZulrahPattern getPattern() {
        return pattern;
    }

    /**
     * @return The base.
     */
    public Location getBase() {
        return base;
    }

    /**
     * Sets the base.
     *
     * @param location The location.
     */
    public void setBase(Location location) {
        this.base = location;
    }

    /**
     * @return the X offset.
     */
    public int getXOffset() {
        return 2240 - base.getX();
    }

    /**
     * @return The y offset.
     */
    public int getYOffset() {
        return 3008 - base.getY();
    }

    public void setPlayer(Player player) {
        this.player = player;
        pattern.setPlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public List<ZulrahSnake> getSnakes() {
        return snakes;
    }
}
