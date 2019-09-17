package plugin.interaction.item.withobject;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents using a Holy Elixir item on an altar.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class HolyElixirAltarPlugin extends UseWithHandler {

    /**
     * Represents the Holy Elixir item.
     */
    private static final Item HOLY_ELIXIR = new Item(11905);
    /**
     * Represents the Spirit shield item.
     */
    private static final Item SPIRIT_SHIELD = new Item(11899);
    /**
     * Represents the Blessed spirit shield item.
     */
    private static final Item BLESSED_SPIRIT_SHIELD = new Item(11901);
    /**
     * The ids of altars.
     */
    private static final int[] ALTAR_IDS = new int[]{
        61, 409, 410, 411, 412, 2640, 3521, 4008,
        8749, 10639, 10640, 13179, 13180, 13181, 13182,
        13183, 13184, 13185, 13186, 13187, 13188, 13189,
        13190, 13191, 13192, 13193, 13194, 13195, 13196,
        13197, 13198, 13199, 18254, 19145, 20377, 20378,
        24343, 26286, 26287, 26288, 26289, 27306, 27338,
        27339, 27661, 28698, 30726, 32079, 34616, 36972,
        37630, 37985, 37990
    };

    public HolyElixirAltarPlugin() {
        super(11905);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : ALTAR_IDS) {
            addHandler(id, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent nodeUsageEvent) {
        final Player player = nodeUsageEvent.getPlayer();
        if (!player.getInventory().contains(SPIRIT_SHIELD)) {
            return false;
        }
        if (player.getSkills().getLevel(Skills.PRAYER) < 85) {
            player.getActionSender().sendMessage("You need at least 85 Prayer to do that.");
            return true;
        }
        if (player.getInventory().contains(SPIRIT_SHIELD) && player.getInventory().contains(HOLY_ELIXIR)) {
            player.lock(1);
            player.animate(new Animation(645));
            player.getInventory().remove(SPIRIT_SHIELD);
            player.getInventory().remove(nodeUsageEvent.getUsedItem());
            player.getInventory().add(BLESSED_SPIRIT_SHIELD);
            player.getActionSender().sendMessage("You successfully bless the shield using the holy elixir and the powers of Saradomin.");
            player.getSkills().addExperienceNoMod(Skills.PRAYER, 1500);
            return true;
        }
        return false;
    }

    @Override
    public Location getDestination(Player player, Node with) {
        return null;
    }
}
