package plugin.activity.barrow;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.skill.free.smithing.smelting.Bar;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.link.ActivityData;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.Arrays;
import java.util.Objects;

/**
 * Handles the barrows activity plugin.
 *
 * @author Emperor
 */
public final class BarrowsActivityPlugin extends ActivityPlugin {

    /**
     * The tunnel configuration values.
     */
    private static final int[] TUNNEL_CONFIGS = {
        55328769,
        2867201,
        44582944,
        817160,
        537688072,
        40763408,
        44320784,
        23478274
    };

    /**
     * The overlay.
     */
    private static final Component OVERLAY = new Component(4535);

    /**
     * The activity handling pulse.
     */
    private static final Pulse PULSE = new Pulse(3) {

        @Override
        public boolean pulse() {
            boolean end = true;
            for (Player p : RegionManager.getRegionPlayers(14231)) {
                end = false;
                int index = p.getAttribute("barrow:drain-index", -1);
                if (index > -1) {
                    p.removeAttribute("barrow:drain-index");
                    //p.getActionSender().sendItemOnInterface(-1, 1, 24, index);
                    // TODO 317 Face reset
                    continue;
                }
                if (p.getLocation().getZ() == 0 && p.getAttribute("barrow:looted", false)) {
                    if (RandomUtil.random(15) == 0) {
                        p.getImpactHandler().manualHit(p, RandomUtil.random(5), ImpactHandler.HitsplatType.NORMAL);
                        Graphics.send(Graphics.create(405), p.getLocation());
                    }
                }
                if (p.getLocks().isLocked("barrow:drain")) {
                    continue;
                }
                int drain = 8;
                for (boolean killed : p.getSavedData().getActivityData().getBarrowsKilled()) {
                    if (killed) {
                        drain += 1;
                    }
                }
//				if (p.hasPerk(Perks.BARROWS_BEFRIENDER)) {
//					drain /= 2;
//				}
                p.getSkills().decrementPrayerPoints(drain);
                p.getLocks().lock("barrow:drain", (3 + RandomUtil.random(15)) * 3);
                index = 1 + RandomUtil.random(6);
                p.setAttribute("barrow:drain-index", index);
                // TODO 317 face pop
                //p.getActionSender().sendItemZoomOnInterface(4761 + RandomFunction.random(12), 100, 24, index);
                //p.getActionSender().sendAnimationInterface(9810, 24, index);
            }
            return end;
        }
    };

    /**
     * Constructs a new {@code BarrowsActivityPlugin} {@code Object}.
     */
    public BarrowsActivityPlugin() {
        super("Barrows", false, false, false);
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
            player.getInterfaceState().openOverlay(OVERLAY);
            player.getConfigManager().set(0, 1);
            if (player.getConfigManager().get(452) == 0) {
                shuffleCatacombs(player);
            }
            sendConfiguration(player);
            if (!PULSE.isRunning()) {
                PULSE.restart();
                PULSE.start();
                World.submit(PULSE);
            }
        } else {
            ((NPC) e).setAggressive(true);
            ((NPC) e).setAggressiveHandler(new AggressiveHandler(e, new AggressiveBehavior() {

                @Override
                public boolean canSelectTarget(Entity entity, Entity target) {
                    if (!target.isActive() || DeathTask.isDead(target)) {
                        return false;
                    }
                    if (target instanceof Player) {
                        if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                            return false;
                        }
                    }
                    if (!target.getProperties().isMultiZone() && target.inCombat()) {
                        return false;
                    }
                    return true;
                }
            }));
        }
        return super.enter(e);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            Player player = (Player) e;
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
            player.getInterfaceState().closeOverlay();
            NPC npc = player.getAttribute("barrow:npc");
            if (npc != null && !DeathTask.isDead(npc)) {
                npc.clear();
            }
            player.removeAttribute("barrow:solvedpuzzle");
            player.removeAttribute("barrow:opened_chest");
            if (!logout && player.getAttribute("barrow:looted", false)) {
                for (int i = 0; i < 6; i++) {
                    player.removeAttribute("brother:" + i);
                    player.getSavedData().getActivityData().getBarrowsKilled()[i] = false;
                }
                player.removeAttribute("barrow:looted");
                shuffleCatacombs(player);
                player.getSavedData().getActivityData().setBarrowTunnelIndex(RandomUtil.random(6));
                player.getSavedData().getActivityData().resetBarrowsKillCount();
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 0, 0, 0));
            }
        }
        return super.leave(e, logout);
    }

    /**
     * "Shuffles" the catacomb gates.
     *
     * @param player The player.
     */
    public static void shuffleCatacombs(Player player) {
        int value = TUNNEL_CONFIGS[RandomUtil.random(TUNNEL_CONFIGS.length)];
        value |= 1 << (6 + RandomUtil.random(4));
        player.getConfigManager().set(452, value);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        Player player = null;
        if (killer instanceof Player) {
            player = (Player) killer;
        } else if (killer instanceof Familiar) {
            player = ((Familiar) killer).getOwner();
        }
        if (player != null && e instanceof NPC) {
            NPC npc = (NPC) e;
            int combatLevel = npc.getDefinition().getCombatLevel();
            if (combatLevel > 0) {
                player.getSavedData().getActivityData().addBarrowsKillcount(combatLevel);
            }
            sendConfiguration(player);
        }
        return false;
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player)
            player = (Player) e;
        if (target instanceof GameObject) {
            GameObject object = (GameObject) target;
            if (object.getId() >= 20667 && object.getId() <= 20672) {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, BarrowsCrypt.getCrypt(object.getId() - 20667).getExitLocation());
                return true;
            }
            if (object.getId() >= 6708 && object.getId() <= 6712) {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, BarrowsCrypt.getCrypt(player.getSavedData().getActivityData().getBarrowTunnelIndex()).getEnterLocation());
                return true;
            }
            switch (object.getId()) {
                case 6720:
                case 6721:
                case 6725:
                case 6726:
                case 6728:
                case 6729:
                case 6730:
                case 6731:
                case 6732:
                case 6734:
                case 6736:
                case 6737:
                case 6738:
                case 6739:
                case 6740:
                case 6741:
                case 6744:
                case 6745:
                case 6747:
                case 6748:
                case 6749:
                case 6727:
                case 6724:
                case 6746:
                    // end puzzle
                case 6714:
                case 6715:
                case 6735:
                case 6733:
                case 6722:
                case 6723:
                case 6742:
                    int index = -1;
                    for (int i = 0; i < player.getSavedData().getActivityData().getBarrowsKilled().length; i++) {
                        if (!player.getSavedData().getActivityData().getBarrowsKilled()[i] && RandomUtil.random(15) == 0 &&
                            !player.getAttribute("brother:" + i, false)) {
                            index = i;
                            break;
                        }
                    }
                    if (index > -1) {
                        BarrowsCrypt.getCrypt(index).spawnBrother(player, RegionManager.getTeleportLocation(target.getLocation(), 1));
                    }
                    boolean betweenDoors = player.getAttribute("barrows:between-doors") != null;
                    if (!betweenDoors) {
                        player.getConfigManager().set(1270, 1);
                        player.setAttribute("barrows:between-doors", true);
                    } else {
                        player.getConfigManager().set(1270, 0);
                        player.removeAttribute("barrows:between-doors");
                    }
                    DoorActionHandler.handleAutowalkDoor(e, (GameObject) target);
                    return true;
                case 20770:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.AHRIM).openSarcophagus((Player) e, object);
                    return true;
                case 20720:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.DHAROK).openSarcophagus((Player) e, object);
                    return true;
                case 20722:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.GUTHAN).openSarcophagus((Player) e, object);
                    return true;
                case 20771:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.KARIL).openSarcophagus((Player) e, object);
                    return true;
                case 20721:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.TORAG).openSarcophagus((Player) e, object);
                    return true;
                case 20772:
                    BarrowsCrypt.getCrypt(BarrowsCrypt.VERAC).openSarcophagus((Player) e, object);
                    return true;
                case 20723:
                    if (player.getAttribute("barrow:looted", false)) {
                        return true;
                    }
                    player.lock(1);
                    if (!player.getSavedData().getActivityData().getBarrowsKilled()[player.getSavedData().getActivityData().getRandomBarrowsBrother()]) {
                        BarrowsCrypt.getCrypt(player.getSavedData().getActivityData().getRandomBarrowsBrother()).spawnBrother(player, RegionManager.getTeleportLocation(target.getCenterLocation(), 4));
                    } else {
                        for (int i = 0; i < player.getSavedData().getActivityData().getBarrowsKilled().length; i++) {
                            if (!player.getSavedData().getActivityData().getBarrowsKilled()[i] &&
                                !player.getAttribute("brother:" + i, false)) {
                                BarrowsCrypt.getCrypt(i).spawnBrother(player, RegionManager.getTeleportLocation(target.getCenterLocation(), 4));
                                break; // TODO Return true?
                            }
                        }
                    }
                    player.setAttribute("barrow:opened_chest", true);
                    sendConfiguration(player);
                    return true;
                case 20724:
                    if (option.getName().equals("Close")) {
                        player.removeAttribute("barrow:opened_chest");
                        sendConfiguration(player);
                        return true;
                    }
                    if (!option.getName().equals("Search")) {
                        // Only other option we handle here. If it's not Search we skip.
                        return false;
                    }
                    if (player.getAttribute("barrow:looted", false)) {
                        player.getActionSender().sendMessage("The chest is empty.");
                        return true;
                    }


                    final Container loot =  BarrowsLootGenerator.createFor(player).generateLoot();

                    if(Perk.TRY_AGAIN_BRO.enabled(player)){

                       final  DialoguePlugin dial = new BarrowsActivityPlugin.BarrowsChestRetryDialogue(player, loot.getItems());

                        if (dial.open()) {
                            player.getDialogueInterpreter().setDialogue(dial);
                            return true;
                        }
                    }

                    for (int i = 0; i < loot.itemCount(); i++) {
                        Item item = loot.get(i);
                        if (item == null) continue;
                        player.getInventory().add(item, player);
                    }

                    player.saveAttribute("barrow:looted", true);
                    player.getSavedData().getActivityData().increaseBarrowsChestCount();
                    int chestCount = player.getSavedData().getActivityData().getBarrowsChestCount();
                    player.getActionSender().sendMessage("Your Barrows chest count is: <col=B80000>" + chestCount + "</col>.");
                    PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.SHAKE, 3, 2, 2, 2, 2));
                    return true;
            }
        }
        return false;
    }

    /**
     * Sends the kill count configuration.
     *
     * @param player The player.
     */
    public static void sendConfiguration(Player player) {
        ActivityData data = player.getSavedData().getActivityData();
        int config = data.getBarrowKills() << 17;
        for (int i = 0; i < data.getBarrowsKilled().length; i++) {
            if (data.getBarrowsKilled()[i]) { //This actually wasn't in 498 but we'll keep it anyways.
                config |= 1 << i;
            }
        }
        if (player.getAttribute("barrow:opened_chest", false)) {
            config |= 1 << 16;
        }
        player.getConfigManager().set(453, config);
    }

    @Override
    public boolean actionButton(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        return false;
    }

    @Override
    public boolean continueAttack(Entity e, Node target, CombatStyle style, boolean message) {
        if (target instanceof BarrowBrother) {
            Player p = null;
            if (e instanceof Player) {
                p = (Player) e;
            } else if (e instanceof Familiar) {
                p = ((Familiar) e).getOwner();
            }
            if (p != null && p != ((BarrowBrother) target).getPlayer()) {
                p.getActionSender().sendMessage("He's not after you.");
                return false;
            }
        }
        return super.continueAttack(e, target, style, message);
    }

    @Override
    public ActivityPlugin newInstance(Player p) {
        return this;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        BarrowsEquipment.init();
        PluginManager.definePlugin(new TunnelEntranceDialogue());
        PluginManager.definePlugin(BarrowsPuzzle.SHAPES);
        registerRegion(14231);
        BarrowsCrypt.init();
        PULSE.stop();
    }

    public static class BarrowsChestRetryDialogue extends DialoguePlugin{

        private final Item[] generatedLoot;

        public BarrowsChestRetryDialogue(Player player, Item[] generatedLoot) {
            super(player);
            this.generatedLoot = generatedLoot;
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BarrowsChestRetryDialogue(player, BarrowsLootGenerator.createFor(player).generateLoot().getItems());
        }

        @Override
        public boolean open(Object... args) {
            player.getActionSender().sendUpdateItems(6963, generatedLoot);
            player.getInterfaceState().openOverlay(new Component(6960));
            interpreter.sendPlaneMessage("Would you like to use up one usage of your 'try again bro' perk?",
                    "Doing so will enable you to retry your luck on the chest.",
                    "Usages: ("+player.getAttribute("try_again_bro_perk")+"/"+Perk.TRY_AGAIN_BRO.getTime()+')'
                );
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage){
                case 0:
                    interpreter.sendOptions("Use up one usage of the 'try again bro' perk?", "Yes do it!", "No thanks, I'll keep this.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect){
                        case TWO_OPTION_ONE:

                            player.getPerkManager().handlePerk(Perk.TRY_AGAIN_BRO);
                            interpreter.sendDialogue("Used up one of the perk's usages.",  "Usages: ("+player.getAttribute("try_again_bro_perk")+"/"+Perk.TRY_AGAIN_BRO.getTime()+")");

                            final Container loot =  BarrowsLootGenerator.createFor(player).generateLoot();
                            final DialoguePlugin plugin = new BarrowsChestRetryDialogue(player, loot.getItems());

                            end();

                            if (plugin.open())
                                player.getDialogueInterpreter().setDialogue(plugin);

                            break;
                        case TWO_OPTION_TWO:
                            Arrays.stream(generatedLoot).filter(Objects::nonNull).forEach(player.getInventory()::add);
                            player.getInterfaceState().closeOverlay();
                            end();
                            break;
                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{666};
        }
    }

}
