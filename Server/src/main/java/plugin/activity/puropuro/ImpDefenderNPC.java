package plugin.activity.puropuro;

import java.util.List;
import java.util.Random;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.hunter.bnet.BNetTypes;
import org.gielinor.game.content.skill.member.hunter.bnet.ImplingNode;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the imp defender npc.
 * @author Vexia
 *
 */
public final class ImpDefenderNPC extends AbstractNPC {

    /**
     * Constructs a new {@code ImpDefenderNPC} {@code Object}.
     */
    public ImpDefenderNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code ImpDefenderNPC} {@code Object}.
     * @param id the id.
     * @param location the location.
     */
    public ImpDefenderNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (RandomUtil.random(250) <= 3) {
            stealImp();
        }
    }

    /**
     * Attempts to steal an imp.
     */
    private void stealImp() {
        List<Player> players = RegionManager.getLocalPlayers(this, 3);
        for (Player player : players) {
            if (canSteal(player)) {
                steal(player);
            }
            break;
        }
    }

    /**
     * Steals an imp from a player.
     * @param player the player.
     */
    private void steal(Player player) {
        if (player == null) {
            return;
        }
        ImplingNode node = BNetTypes.getImpling(player);
        if (node != null) {
            faceTemporary(player, 1);
            sendChat("Be free!");
            player.getInventory().remove(node.getReward());
            player.setAttribute("imp-steal", World.getTicks() + 500);
        }
    }

    /**
     * Checks if the imp can steal from the player.
     * @param player the player.
     * @return <code>True</code> if so.
     */
    private boolean canSteal(Player player) {
        if (!World.getConfiguration().isDevelopmentEnabled() && player.getAttribute("imp-steal", 0) > World.getTicks()) {
            return false;
        }
        int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
        if (thievingLevel >= 60) {
            return false;
        }
        int level = thievingLevel - 10;
        if (level <= 0) {
            level = 5;
        }
        if (player.getInventory().containsItem(new Item(11262))) {
            thievingLevel += 15;
        }
        int currentLevel = RandomUtil.random(thievingLevel) + 1;
        double ratio = currentLevel / (new Random().nextInt(level + 5) + 1);
        return Math.round(ratio * thievingLevel) < level;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ImpDefenderNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6074 };
    }

}
