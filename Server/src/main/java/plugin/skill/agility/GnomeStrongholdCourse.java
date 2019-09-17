package plugin.skill.agility;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.member.agility.AgilityCourse;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.task.impl.LocationLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Handles the gnome stronghold agility course.
 *
 * @author Emperor
 */
public final class GnomeStrongholdCourse extends AgilityCourse {

    /**
     * The pipes currently in usage.
     */
    private static final int[] USED_PIPES = new int[2];

    /**
     * The trainer NPCs.
     */
    private static final NPC[] TRAINERS = new NPC[5];

    /**
     * The mark of grace spawn locations.
     */
    private static final Location[] GRACE_SPAWNS = new Location[]{
        Location.create(2471, 3427, 0),
        Location.create(2476, 3422, 1),
        Location.create(2475, 3418, 2),
        Location.create(2488, 3420, 2),
        Location.create(2481, 3429, 0),
        Location.create(2487, 3439, 0)
    };

    /**
     * Constructs a new {@code GnomeStrongholdCourse} {@code Object}.
     */
    public GnomeStrongholdCourse() {
        this(null);
    }

    /**
     * Constructs a new {@code GnomeStrongholdCourse} {@code Object}.
     *
     * @param player The player.
     */
    public GnomeStrongholdCourse(Player player) {
        super(player, 6, 39.0);
    }

    /**
     * Creates a mark of grace drop.
     *
     * @param player The player.
     * @param index  The course index.
     */
    public static void dropMark(Player player, int index) {
        if (!AgilityHandler.spawnMarkOfGrace(player, "gnome", 1)) {
            return;
        }
        GroundItem groundItem = new GroundItem(new Item(14729), GRACE_SPAWNS[index], 1000, player);
        groundItem.setRemainPrivate(true);
        GroundItemManager.create(groundItem);
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        getCourse(player); //Sets the extension.
        GameObject object = (GameObject) node;
        switch (object.getId()) {
            case 23145:
                TRAINERS[0].sendChat("Okay get over that log, quick quick!");
                player.getActionSender().sendMessage("You walk carefully across the slippery log...", 1);
                AgilityHandler.walk(player, 0, Location.create(2474, 3436, 0), Location.create(2474, 3429, 0), Animation.create(762), 7.5, "...You make it safely to the other side.");
                checkLast(player, 1);
                dropMark(player, 0);
                return true;
            case 23134:
                TRAINERS[1].sendChat("Move it, move it, move it!");
                player.getActionSender().sendMessage("You climb the netting...", 1);
                AgilityHandler.climb(player, 1, Animation.create(828), object.getLocation().transform(0, -1, 1), 7.5, null);
                checkLast(player, 2);
                dropMark(player, 1);
                return true;
            case 23559:
                TRAINERS[2].sendChat("That's it - straight up.");
                player.getActionSender().sendMessage("You climb the tree..", 1);
                AgilityHandler.climb(player, 2, Animation.create(828), Location.create(2473, 3420, 2), 5.0, "...To the platform above.");
                checkLast(player, 3);
                dropMark(player, 2);
                return true;
            case 23557:
                TRAINERS[3].sendChat("Come on scaredy cat, get across that rope!");
                player.getActionSender().sendMessage("You carefully cross the tightrope.", 1);
                AgilityHandler.walk(player, 3, Location.create(2477, 3420, 2), Location.create(2483, 3420, 2), Animation.create(762), 7.5, null);
                checkLast(player, 4);
                dropMark(player, 3);
                return true;
            case 23558:
                player.getActionSender().sendMessage("You can't do that from here.", 1);
                return true;
            case 23560:
            case 23561:
                player.getActionSender().sendMessage("You climb down the tree..", 1);
                AgilityHandler.climb(player, 4, Animation.create(828), Location.create(2487, 3420, 0), 5.0, "You land on the ground.");
                checkLast(player, 5);
                dropMark(player, 4);
                return true;
            case 23135:
                TRAINERS[4].sendChat("My Granny can move faster than you.");
                player.faceLocation(player.getLocation().transform(0, 2, 0));
                player.getActionSender().sendMessage("You climb the netting...", 1);
                AgilityHandler.climb(player, 5, Animation.create(828), player.getLocation().transform(0, 2, 0), 7.5, null);
                checkLast(player, 6);
                return true;
            case 23139:
            case 23138:
                if (object.getLocation().getY() == 3435) {
                    player.getActionSender().sendMessage("You can't do that from here.");
                    return true;
                }
                final int index = object.getId() == 23138 ? 0 : 1;
                if (USED_PIPES[index] > World.getTicks()) {
                    player.getActionSender().sendMessage("The pipe is being used.");
                    return true;
                }
                USED_PIPES[index] = World.getTicks() + 10;
                final int x = 2484 + (index * 3);
                player.lock(12);
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3430, 0), Location.create(x, 3433, 0), Animation.create(20749), 10, 0, null);
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3433, 0), Location.create(x, 3435, 0), Animation.create(844), 10, 0, null, 5);
                AgilityHandler.forceWalk(player, 6, Location.create(x, 3435, 0), Location.create(x, 3437, 0), Animation.create(20748), 20, 7.5, null, 8);
                player.addExtension(LogoutTask.class, new LocationLogoutTask(12, Location.create(x, 3430, 0)));
                checkLast(player, 7);
                dropMark(player, 5);
                return true;
        }
        return false;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        GameObject object = (GameObject) n;
        switch (object.getId()) {
            case 23145:
                return Location.create(2474, 3436, 0);
            case 23135:
                int x = node.getLocation().getX();
                if (x < n.getLocation().getX()) {
                    x = n.getLocation().getX();
                } else if (x > n.getLocation().getX() + 1) {
                    x = n.getLocation().getX() + 1;
                }
                return Location.create(x, n.getLocation().getY() - 1, 0);
            case 23139:
            case 23138:
                if (n.getLocation().getY() == 3431) {
                    return n.getLocation().transform(0, -1, 0);
                }
        }
        return null;
    }

    /**
     * Checks if the player has the last index for an achievement.
     *
     * @param index The index of the lap.
     */
    public void checkLast(Player player, int index) {
        if (player.getAttribute("GNOME_COURSE") == null) {
            player.setAttribute("GNOME_COURSE", 1);
        }
        if (player.getAttribute("GNOME_COURSE", 1) == 7) {
            AchievementDiary.finalize(player, AchievementTask.SMALL_AGILE);
            player.removeAttribute("GNOME_COURSE");
            return;
        }
        if (player.getAttribute("GNOME_COURSE", 1) == index) {
            player.setAttribute("GNOME_COURSE", (index + 1));
            AchievementDiary.decrease(player, AchievementTask.SMALL_AGILE, 1);
        }
    }

    @Override
    public void configure() {
        TRAINERS[0] = NPC.create(162, Location.create(2473, 3438, 0));
        TRAINERS[1] = NPC.create(162, Location.create(2478, 3426, 0));
        TRAINERS[2] = NPC.create(162, Location.create(2474, 3422, 1));
        TRAINERS[3] = NPC.create(162, Location.create(2472, 3419, 2));
        TRAINERS[4] = NPC.create(162, Location.create(2489, 3425, 0));
        for (NPC npc : TRAINERS) {
            npc.init();
            npc.setWalkRadius(3);
        } // + 20850
        ObjectDefinition.forId(23145).getConfigurations().put("option:walk-across", this);
        ObjectDefinition.forId(23134).getConfigurations().put("option:climb-over", this);
        ObjectDefinition.forId(23559).getConfigurations().put("option:climb", this);
        ObjectDefinition.forId(23557).getConfigurations().put("option:walk-on", this);
        ObjectDefinition.forId(23558).getConfigurations().put("option:walk-on", this);
        ObjectDefinition.forId(23560).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(23561).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(23135).getConfigurations().put("option:climb-over", this);
        ObjectDefinition.forId(23139).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(23138).getConfigurations().put("option:squeeze-through", this);
    }

    @Override
    public AgilityCourse createInstance(Player player) {
        return new GnomeStrongholdCourse(player);
    }
}
