package org.gielinor.game.content.global.distraction.treasuretrail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a clue scroll plugin.
 *
 * @author Vexia
 */
public abstract class ClueScrollPlugin extends MapZone implements Plugin<Object> {

    private static final Logger log = LoggerFactory.getLogger(ClueScrollPlugin.class);

    /**
     * The mapping of clue scrolls.
     */
    private static final Map<Integer, ClueScrollPlugin> CLUE_SCROLLS = new HashMap<>();

    /**
     * A map of pre organized clue scrolls.
     */
    private static final Map<ClueLevel, List<ClueScrollPlugin>> ORGANIZED = new HashMap<>();

    /**
     * The id of the clue scroll.
     */
    protected final int clueId;

    /**
     * The clue scroll level.
     */
    protected final ClueLevel level;

    /**
     * The interface id of the clue.
     */
    protected final int interfaceId;

    /**
     * The zone borders.
     */
    protected final ZoneBorders[] borders;

    /**
     * Constructs a new {@Code ClueScrollPlugin} {@Code Object}
     *
     * @param clueId      the id.
     * @param level       the level.
     * @param interfaceId the id.
     */
    public ClueScrollPlugin(final String name, final int clueId, ClueLevel level, final int interfaceId, final ZoneBorders... borders) {
        super(name, true);
        this.clueId = clueId;
        this.level = level;
        this.interfaceId = interfaceId;
        this.borders = borders;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Rewards the player with a casket.
     *
     * @param player the player.
     * @param casket if we give a ckaset.
     */
    public void reward(Player player, boolean casket) {
        Item clue = player.getInventory().getItem(new Item(getClueId()));
        if (clue == null) {
            return;
        }
        nextStage(player, clue);
        if (casket) {
            player.getInventory().replace(level.getCasket(), clue.getSlot());
        } else {
            player.getInventory().remove(clue);
        }
    }

    /**
     * Rewards the player.
     *
     * @param player the player.
     */
    public void reward(Player player) {
        reward(player, true);
    }

    /**
     * Increments the next stage of the clue.
     *
     * @param player the player.
     * @param clue   the clue.
     */
    public void nextStage(Player player, Item clue) {
        if (!player.getTreasureTrailManager().hasTrail() ||
            player.getTreasureTrailManager().hasTrail() &&
                clue.getId() != player.getTreasureTrailManager().getClueId()) {
            player.getTreasureTrailManager().startTrail(this);
            player.getActionSender().sendMessage("You have started a clue scroll!");
        }
        int currentStage = player.getTreasureTrailManager().getTrailStage();
        if (currentStage >= player.getTreasureTrailManager().getTrailLength()) {
            player.getTreasureTrailManager().clearTrail();
        } else {
            player.getTreasureTrailManager().incrementStage();
        }
    }

    /**
     * Reads a clue scroll.
     *
     * @param player the player.
     */
    public void read(Player player) {
        if (player.getInterfaceState().isOpened()) {
            player.getActionSender().sendMessage("Please finish what you're doing first.");
            return;
        }
        player.getInterfaceState().open(new Component(interfaceId));
    }

    /**
     * Reigsters a clue scroll into the repository.
     *
     * @param clue the plugin.
     */
    public void register(ClueScrollPlugin clue) {
        if (CLUE_SCROLLS.containsKey(clue.getClueId())) {
            ClueScrollPlugin other = CLUE_SCROLLS.get(clue.getClueId());
            log.warn("Attempted to define duplicate clue scroll plugin for [{}].", clue.getClueId());
            log.warn("Existing plugin: [{}]", other.getClass().getName());
            log.warn("Attempted plugin: [{}]", clue.getClass().getName());
            log.warn("The request was ignored, and only the existing plugin is being used.");
            return;
        }
        List<ClueScrollPlugin> organized = ORGANIZED.get(clue.getLevel());
        if (organized == null) {
            organized = new ArrayList<>();
        }
        organized.add(clue);
        ZoneBuilder.configure(clue);
        CLUE_SCROLLS.put(clue.getClueId(), clue);
        ORGANIZED.put(clue.getLevel(), organized);
    }

    @Override
    public void configure() {
        if (borders == null) {
            return;
        }
        for (ZoneBorders border : borders) {
            register(border);
        }
    }

    /**
     * Gets a clue item.
     *
     * @param clueLevel the level.
     * @return the item.
     */
    public static Item getClue(ClueLevel clueLevel) {
        List<ClueScrollPlugin> clues = ORGANIZED.get(clueLevel);
        if (clues == null) {
            log.warn("There are no clues for level [{}].", clueLevel);
            return null;
        }
        ClueScrollPlugin clue = clues.get(RandomUtil.random(clues.size()));
        return new Item(clue.getClueId());
    }

    /**
     * Checks if the player has equipment.
     *
     * @param player    the player.
     * @param equipment the equipment.
     * @return {@code True} if so.
     */
    public boolean hasEquipment(Player player, int[][] equipment) {
        if (equipment == null || equipment.length == 0) {
            return true;
        }
        int hasAmt = 0;
        for (int[] anEquipment : equipment) {
            for (int anAnEquipment : anEquipment) {
                if (player.getEquipment().contains(anAnEquipment, 1)) {
                    hasAmt++;
                    break;
                }
            }
        }
        return hasAmt == equipment.length;
    }

    /**
     * Gets the bclueId.
     *
     * @return the clueId
     */
    public int getClueId() {
        return clueId;
    }

    /**
     * Gets the blevel.
     *
     * @return the level
     */
    public ClueLevel getLevel() {
        return level;
    }

    /**
     * Gets the bclueScrolls.
     *
     * @return the clueScrolls
     */
    public static Map<Integer, ClueScrollPlugin> getClueScrolls() {
        return CLUE_SCROLLS;
    }

    /**
     * Gets the binterfaceId.
     *
     * @return the interfaceId
     */
    public int getInterfaceId() {
        return interfaceId;
    }

}
