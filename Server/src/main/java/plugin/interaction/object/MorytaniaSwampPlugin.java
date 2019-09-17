package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
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

/**
 * Represents the Morytania swamp plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MorytaniaSwampPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(3506).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(5005).getConfigurations().put("option:climb up", this);
        ObjectDefinition.forId(5005).getConfigurations().put("option:climb down", this);
        ObjectDefinition.forId(5002).setOptionHandler("walk-here", this);
        ObjectDefinition.forId(6970).setOptionHandler("board", this);
        ObjectDefinition.forId(6969).setOptionHandler("board", this);
        ObjectDefinition.forId(6969).setOptionHandler("board ( pay 10 )", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (node instanceof GameObject) {
            GameObject gameObject = (GameObject) node;
            switch (gameObject.getId()) {
                case 6970:
                    player.lock(7);
                    World.submit(new Pulse(1, player) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 2:
                                    player.getActionSender().sendMinimapState(2);
                                    break;
                                case 3:
                                    player.setTeleportTarget(Location.create(3522, 3285));
                                    break;
                                case 5:
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    player.getActionSender().sendMinimapState(0);
                                    return true;
                            }
                            return false;
                        }

                    });
                    return true;
                case 6969:
                    player.lock(7);
                    World.submit(new Pulse(1, player) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 2:
                                    player.getActionSender().sendMinimapState(2);
                                    break;
                                case 3:
                                    player.setTeleportTarget(Location.create(3498, 3380));
                                    break;
                                case 5:
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    player.getActionSender().sendMinimapState(0);
                                    return true;
                            }
                            return false;
                        }

                    });
                    return true;
                case 3506:
//					player.getDialogueInterpreter().sendPlaneMessage("There's a message attached to this gate, it reads:~",
//							"~ Mort Myre is a dangerous Ghast infested swamp. ~",
//							"~ Do not enter if you value your life. ~",
//							"~ All persons wishing to enter must see Drezel. ~");
                    DoorActionHandler.handleAutowalkDoor(player, gameObject);
                    return true;
                case 5005:
                    if (option.toLowerCase().contains("climb up")) {
                        if (player.getLocation().getY() != 3432 && player.getLocation().getY() != 3425) {
                            return true;
                        }
                        int y = gameObject.getLocation().getY() == 3426 ? 3427 : 3430;
                        ClimbActionHandler.climb(player, new Animation(828), Location.create(3502, y, 0));
                        return true;
                    }
                    if (option.toLowerCase().contains("climb down")) {
                        if (player.getLocation().getY() != 3430 && player.getLocation().getY() != 3427) {
                            return true;
                        }
                        int y = gameObject.getLocation().getY() == 3426 ? 3425 : 3432;
                        player.faceLocation(Location.create(3502, y, 0));
                        ClimbActionHandler.climb(player, new Animation(827), Location.create(3502, y, 0));
                        return true;
                    }
                    return false;
                case 5002:
                    player.lock(3);
                    int y = 3427;
                    if (player.getLocation().getY() < 3430) {
                        y = 3430;
                    }
                    player.getWalkingQueue().addPath(3502, y);
                    break;
            }
        }
        return true;
    }

}
