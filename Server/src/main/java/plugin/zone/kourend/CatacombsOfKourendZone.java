package plugin.zone.kourend;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.travel.Teleport;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Represents the enclosed zone Catacombs of Kourend.
 *
 * wiki: http://oldschoolrunescape.wikia.com/wiki/Catacombs_of_Kourend
 *
 * Created by Stan van der Bend on 13/01/2018.
 *
 * project: Gielinor-Server
 * package: plugin.zone.kourend
 */
public class CatacombsOfKourendZone extends MapZone implements Plugin<Object> {

    private final static double DROP_RATE_MULTIPLIER = 10.0D; // 10 times easier than OSRS.

    private final static Location SKOTIZO_LAIR = Location.create(1693, 9886, 0);

    private final static Item DARK_TOTEM = new Item(19685, 1);

    private final static Item[] TOTEM_PART_ITEMS = new Item[]{
        new Item(19679, 1), // Dark totem base
        new Item(19681, 1), // Dark totem middle
        new Item(19683, 1), // Dark totem top
    };

    public CatacombsOfKourendZone(boolean overlappable, ZoneRestriction... restrictions) {
        super("Catacombs of Kourend", overlappable, restrictions);
    }

    @Override
    public boolean enter(Entity e) {
        if(e instanceof Player)
            e.asPlayer().getActionSender().sendMessage("Entered catacombs");
        return super.enter(e);
    }

    @Override
    public void configure() {

        final ZoneBorders catacombsBorders = new ZoneBorders(1590,  9979, 1745,10112);

        register(catacombsBorders);

    }

    @Override
    public boolean death(Entity e, Entity killer) {

        handleDrop(e, killer);

        return super.death(e, killer);

    }

    private void handleDrop(Entity killed, Entity killer){

        if(killed instanceof NPC && killer instanceof Player){

            final Player killingPlayer = killer.asPlayer();
            final int NPCMaxHitpoints = killed.getSkills().getMaximumLifepoints();

            RandomUtil
                .findChanceItem(generateChanceItems(killingPlayer, NPCMaxHitpoints))
                .map(ChanceItem::getRandomItem)
                .ifPresent(generateDropFor(killingPlayer, killed.getLocation()));
        }

    }

    /**
     * Creates a {@link GroundItem}.
     *
     * @param player    the player for whom the item will be visible.
     * @param location  the location of the item.
     *
     * @return a consumer implementation for generating a {@link GroundItem} from the accepted {@link Item}.
     */
    private Consumer<Item> generateDropFor(Player player, Location location){
        return item -> GroundItemManager.create(item, location, player);
    }

    /**
     * Generates a {@link ChanceItem} array of the {@link CatacombsOfKourendZone#TOTEM_PART_ITEMS}.
     * The filter applied is predicated on the {@link Item} already being present in the {@link Player#inventory}.
     *
     * @param player    the player whose inventory will be predicated upon.
     * @param hitPoints the {@link Skills#getMaximumLifepoints()} of the killed {@link NPC}.
     *
     * @return a filtered {@link ChanceItem} array.
     */
    private ChanceItem[] generateChanceItems(Player player, int hitPoints){
        return Arrays
            .stream(TOTEM_PART_ITEMS)
            .filter(item -> !player.getInventory().containsItem(item))

            .peek(item -> player.getActionSender().sendMessage("Potential totem part ("+item.getName()+")"))

            .map(item -> new ChanceItem(item.getId(), item.getCount(), getDropRate(hitPoints)))
            .toArray(ChanceItem[]::new);
    }

    /**
     * Official drop rate formula for totem drops.
     *
     * wiki:
     *      http://oldschoolrunescape.wikia.com/wiki/Dark_totem_base
     *      http://oldschoolrunescape.wikia.com/wiki/Dark_totem_middle
     *      http://oldschoolrunescape.wikia.com/wiki/Dark_totem_top
     *
     * @param hitPoints the {@link Skills#getMaximumLifepoints()} of the killed {@link NPC}.
     *
     * @return the drop chance of one of the totem pieces.
     */
    private double getDropRate(int hitPoints){
        return (DROP_RATE_MULTIPLIER * (1 / (500 - hitPoints)));
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {

        if (e instanceof Player) {

            final Player player = (Player) e;

            switch (target.getId()) {

                case 28900: // skotizo lair entrance altar

                    if(player.getInventory().containsItem(DARK_TOTEM)) {

                        activateAltar(player);


                    } else
                        player.getActionSender().sendMessage("You need a Dark Totem in order to activate the altar.");

                    return true;

            }
        }
        return super.interact(e, target, option);
    }


    public static void activateAltar(Player player) {

        final GameObject altar = RegionManager.getObject(1663, 10047, player.getLocation().getZ());

        player.getActionSender().sendObjectAnimation(altar, Animation.create(1471));

        player.getTeleporter().send(SKOTIZO_LAIR, Teleport.TeleportType.KOUREND_ALTAR);

        player.getInventory().remove(DARK_TOTEM);
    }
}
