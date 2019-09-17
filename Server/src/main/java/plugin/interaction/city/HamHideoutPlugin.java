package plugin.interaction.city;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the ham hide out node interaction plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HamHideoutPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(827);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(5490).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(5490).getConfigurations().put("option:pick-lock", this);
        ObjectDefinition.forId(5501).getConfigurations().put("option:open", this); // jail door
        ObjectDefinition.forId(5501).getConfigurations().put("option:pick-lock", this); // jail door

        ObjectDefinition.forId(5491).getConfigurations().put("option:close", this);
        ObjectDefinition.forId(5491).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(5493).getConfigurations().put("option:climb-up", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final int id = ((GameObject) node).getId();
        switch (id) {
            case 5501:
                switch (option) {
                    case "open":
                        player.getActionSender().sendMessage("The door seems to be locked.");
                        return true;
                    case "pick-lock":
                        if (player.getLocation().getX() < 3183) {
                            player.getActionSender().sendMessage("There's no point in doing that.");
                            return true;
                        }
                        player.lock(3);
                        player.animate(Animation.create(2246));
                        player.getActionSender().sendMessage("You attempt to pick the lock.");
                        World.submit(new Pulse(2, player) {

                            @Override
                            public boolean pulse() {
                                boolean success = RandomUtil.random(2) == 1;
                                player.getActionSender().sendMessage(success ? ("You pick the lock.") : "You fail to pick the lock.");
                                player.unlock();
                                if (success) {
                                    World.submit(new Pulse(1, player) {

                                        @Override
                                        public boolean pulse() {
                                            DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                                            return true;
                                        }
                                    });
                                }
                                return true;
                            }
                        });
                        return true;
                }
                return true;
            case 5493:
                if (player.getLocation().withinDistance(Location.create(3149, 9652, 0))) {
                    ClimbActionHandler.climb(player, new Animation(828), new Location(3165, 3251, 0));
                    return true;
                }
                ClimbActionHandler.climbLadder(player, (GameObject) node, option);
                return true;
            case 5490:
            case 5491:
                switch (option) {
                    case "open":
                        if (player.getConfigManager().get(174) == 0) {
                            player.getActionSender().sendMessage("This trapdoor seems totally locked.");
                            player.getConfigManager().set(346, 0);
                            player.getConfigManager().set(174, 0);
                        } else {
                            player.getConfigManager().set(346, 272731282);
                            ClimbActionHandler.climb(player, new Animation(827), new Location(3149, 9652, 0));
                            World.submit(new Pulse(2, player) {

                                @Override
                                public boolean pulse() {
                                    player.getConfigManager().set(174, 0);
                                    return true;
                                }
                            });
                        }
                        break;
                    case "close":
                        player.getConfigManager().set(174, 0);
                        break;
                    case "climb-down":
                        switch (id) {
                            case 5491:
                                player.getProperties().setTeleportLocation(Location.create(3149, 9652, 0));
                                AchievementDiary.finalize(player, AchievementTask.HAM);
                                break;
                        }
                        break;
                    case "pick-lock":
                        player.lock(3);
                        player.animate(ANIMATION);
                        player.getActionSender().sendMessage("You attempt to pick the lock on the trap door.");
                        World.submit(new Pulse(2, player) {

                            @Override
                            public boolean pulse() {
                                player.animate(ANIMATION);
                                player.getActionSender().sendMessage("You attempt to pick the lock on the trap door.");
                                boolean success = RandomUtil.random(3) == 1;
                                player.getActionSender().sendMessage(success ? ("You pick the lock on the trap door.") : "You fail to pick the lock - your fingers get numb from fumbling with the lock.");
                                player.unlock();
                                if (success) {
                                    player.getConfigManager().set(174, 1 << 14);
                                    World.submit(new Pulse(40, player) {

                                        @Override
                                        public boolean pulse() {
                                            player.getConfigManager().set(174, 0);
                                            return true;
                                        }
                                    });
                                }
                                return true;
                            }
                        });
                        break;
                }
                break;
        }
        return true;
    }

}
