package plugin.activity.duelarena;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InterfaceConfigContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InterfaceConfig;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a duel session.
 *
 * @author Emperor
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         "Duel Stake addition : 0 x Coins added!", "Duel Stake removal : 0 x Coins removed!"
 */
public final class DuelSession extends ComponentPlugin {

    /**
     * The duel interface.
     */
    private static final Component DUEL_COMPONENT = new Component(48000).setCloseEvent(new DuelCloseEvent());
    /**
     * The duel confirm interface.
     */
    private static final Component ACCEPT_COMPONENT = new Component(48172).setCloseEvent(new DuelCloseEvent());
    /**
     * The inventory component.
     */
    private static final Component INVENTORY_COMPONENT = new Component(6573);
    /**
     * The first player.
     */
    private final Player player;
    /**
     * The opponent player.
     */
    private final Player opponent;
    /**
     * The currently enforced {@link plugin.activity.duelarena.DuelRule}s.
     */
    private final List<DuelRule> duelRules = new ArrayList<>();
    /**
     * The {@link plugin.activity.duelarena.DuelStage} of this session.
     */
    private DuelStage duelStage = DuelStage.WAITING;
    /**
     * The accept timeout pulse.
     */
    private Pulse acceptTimeoutPulse;
    /**
     * Whether or not any rules were changed from the beginning.
     */
    private boolean rulesChanged;
    /**
     * The {@link plugin.activity.duelarena.StakeContainer}.
     */
    private StakeContainer container;
    /**
     * The duel waiting {@link org.gielinor.rs2.pulse.Pulse}.
     */
    private Pulse pulse;
    /**
     * If the duel has started.
     */
    private boolean duelStarted = false;
    /**
     * The Attack {@link org.gielinor.game.interaction.Option}.
     */
    public static final Option ATTACK_OPTION = Option._P_ATTACK.setHandler(new OptionHandler() {

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (DuelSession.getExtension(player) != null && DuelSession.getExtension(player).getPulse() != null) {
                if (DuelSession.getExtension(player).getPulse().isRunning()) {
                    player.faceLocation(node.getLocation());
                    return false;
                }
            }
            player.getPulseManager().clear("interaction:attack:" + node.hashCode());
            player.getProperties().getCombatPulse().attack(node);
            return true;
        }

        @Override
        public boolean isWalk() {
            return false;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            return this;
        }

        @Override
        public boolean isDelayed(Player player) {
            return false;
        }

    });

    /**
     * Constructs a new {@code DuelSession} {@code Object}.
     *
     * @param player   The first player.
     * @param opponent The second player.
     */
    public DuelSession(Player player, Player opponent) {
        this.player = player;
        this.opponent = opponent;
        this.container = new StakeContainer(player);
    }

    /**
     * Forfeits a duel.
     *
     * @param player The player forfeiting.
     */
    public static void forfeit(Player player) {
        DuelSession duelSession = player.getExtension(DuelSession.class);
        if (duelSession == null) {
            return;
        }
        DuelSession opponentSession = DuelSession.getExtension(duelSession.getOpponent());
        if (opponentSession == null) {
            return;
        }
        DuelArenaActivityPlugin.handleVictory(opponentSession.getPlayer(), player);
        if (player.isActive() && player.isPlaying()) {
            player.getActionSender().sendMessage("You forfeited the duel.");
        }
    }

    /**
     * Declines the duel.
     *
     * @param player The player declining.
     */
    public static void decline(Player player) {
        DuelSession duelSession = player.getExtension(DuelSession.class);
        if (duelSession == null) {
            return;
        }
        if (duelSession.getDuelStage() == DuelStage.IN_PROGRESS ||
            DuelSession.getExtension(duelSession.getOpponent()).getDuelStage() == DuelStage.IN_PROGRESS) {
            forfeit(player);
            return;
        }
        if (duelSession.getDuelStage() != DuelStage.IN_PROGRESS &&
            DuelSession.getExtension(duelSession.getOpponent()).getDuelStage() != DuelStage.IN_PROGRESS) {
            duelSession.retainContainer(player);
            DuelSession.getExtension(duelSession.getOpponent()).retainContainer(duelSession.getOpponent());
        }
        duelSession.player.removeExtension(DuelSession.class);
        duelSession.opponent.removeExtension(DuelSession.class);
        duelSession.player.removeAttribute("duel:partner");
        duelSession.opponent.removeAttribute("duel:partner");
        duelSession.player.removeAttribute("duel:staked");
        duelSession.opponent.removeAttribute("duel:staked");
        player.getInterfaceState().closeSingleTab();
        player.getInterfaceState().openDefaultTabs();
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 24, new Item[]{}, 27, false));
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 23, new Item[]{}, 27, false));
        if (player == duelSession.opponent) {
            duelSession.player.getInterfaceState().close();
            duelSession.player.getActionSender().sendMessage("Other player has declined the duel.");
        } else {
            duelSession.opponent.getInterfaceState().close();
            duelSession.opponent.getActionSender().sendMessage("Other player has declined the duel.");
        }
        player.getInterfaceState().close();
        player.getActionSender().sendMessage("You declined the duel.");
    }

    /**
     * Opens the rules interface.
     */
    public void openRules() {
        player.setAttribute("duel:partner", opponent);
        opponent.setAttribute("duel:partner", player);
        openRules(player, opponent);
        openRules(opponent, player);
    }

    /**
     * Opens the rules for a player.
     *
     * @param player The player.
     */
    private void openRules(Player player, Player opponent) {
        player.getActionSender().sendString(opponent.getUsername(), 48003);
        String combatDiffColor = TextUtils.getCombatDifferenceColour(player.getProperties().getCurrentCombatLevel(), opponent.getProperties().getCurrentCombatLevel());
        player.getActionSender().sendString(combatDiffColor + Integer.toString(opponent.getProperties().getCurrentCombatLevel()), 48005);
        player.getActionSender().sendString("", 48108);
        for (DuelRule duelRule : DuelRule.values()) {
            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule.getConfigIndex(), 0));
        }
        for (int index = 0; index < 28; index++) {
            player.getActionSender().sendUpdateItem(null, (48110 + index), 0);
        }
        for (int index = 0; index < 28; index++) {
            player.getActionSender().sendUpdateItem(null, (48140 + index), 0);
        }
        player.getInterfaceState().closeDefaultTabs();
        DUEL_COMPONENT.setCloseEvent(new DuelCloseEvent());
        player.getInterfaceState().setOpened(DUEL_COMPONENT);
        player.getInterfaceState().setOverlay(INVENTORY_COMPONENT);
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 6574, 0, 0, player.getInventory().toArray(), 28, false));
        player.getInventory().refresh();
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, DUEL_COMPONENT, INVENTORY_COMPONENT));
    }

    /**
     * Toggles a rule.
     *
     * @param p        The player toggling the rule.
     * @param buttonId The rule index.
     */
    public void toggleRule(Player p, int buttonId) {
        DuelRule duelRule = DuelRule.forId(buttonId);
        if (duelRule == null) {
            player.getActionSender().sendMessage("Something went wrong, please report this on the forums : " + buttonId);
            return;
        }
        if (duelRules.contains(duelRule)) {
            for (int index = 0; index < duelRules.size(); index++) {
                if (duelRule == duelRules.get(index)) {
                    duelRule.deactivate(player, false);
                    duelRule.deactivate(opponent, true);
                    duelRules.remove(index);
                    PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule.getConfigIndex(), 0));
                    PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(opponent, duelRule.getConfigIndex(), 0));
                    break;
                }
            }
        } else {
            if (!duelRule.canActivate(player, true)) {
                PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule.getConfigIndex(), 0));
                PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(opponent, duelRule.getConfigIndex(), 0));
                return;
            }
            duelRule.activate(player, false); // TODO 317
            duelRule.activate(opponent, true);
            duelRules.add(duelRule);
        }
        for (DuelRule duelRule1 : duelRules) {
            if (duelRule1 == null) {
                continue;
            }
            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule1.getConfigIndex(), 1));
            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(opponent, duelRule1.getConfigIndex(), 1));
        }
        DuelSession opponentSession = getExtension(opponent);
        opponentSession.setDuelStage(DuelStage.ACCEPT_TIMEOUT);
        setDuelStage(DuelStage.ACCEPT_TIMEOUT);
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int buttonId, int slot, int itemId) {
        DuelSession session = player.getExtension(DuelSession.class);
        if (session == null) {
            return false;
        }
        DuelSession opponentSession = getExtension(session.getOpponent());
        if (opponentSession == null) {
            return false;
        }
        switch (component.getId()) {
            /**
             * Stake inventory.
             */
            case 6574:
                switch (opcode) {
                    /**
                     * Offer 1.
                     */
                    case OperationCode.OPTION_OFFER_ONE:
                        session.getContainer().stake(slot, 1);
                        return true;
                    /**
                     * Offer 5.
                     */
                    case OperationCode.OPTION_OFFER_FIVE:
                        session.getContainer().stake(slot, 5);
                        return true;
                    /**
                     * Offer 10.
                     */
                    case OperationCode.OPTION_OFFER_TEN:
                        session.getContainer().stake(slot, 10);
                        return true;
                    /**
                     * Offer All.
                     */
                    case OperationCode.OPTION_OFFER_ALL:
                        session.getContainer().stake(slot, player.getInventory().getCount(player.getInventory().get(slot)));
                        return true;
                    /**
                     * Offer X.
                     */
                    case OperationCode.OPTION_OFFER_X:
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                session.getContainer().stake(slot, (int) getValue());
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                        return true;
                }
                break;
            /**
             * Remove item stake.
             */
            case 48110:
                switch (opcode) {
                    /**
                     * Remove 1.
                     */
                    case OperationCode.OPTION_OFFER_ONE:
                        session.getContainer().withdraw(slot, 1);
                        return true;
                    /**
                     * Remove 5.
                     */
                    case OperationCode.OPTION_OFFER_FIVE:
                        session.getContainer().withdraw(slot, 5);
                        return true;
                    /**
                     * Remove 10.
                     */
                    case OperationCode.OPTION_OFFER_TEN:
                        session.getContainer().withdraw(slot, 10);
                        return true;
                    /**
                     * Remove All.
                     */
                    case OperationCode.OPTION_OFFER_ALL:
                        session.getContainer().withdraw(slot, session.getContainer().getCount(session.getContainer().get(slot)));
                        return true;
                    /**
                     * Remove X.
                     */
                    case OperationCode.OPTION_OFFER_X:
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                session.getContainer().withdraw(slot, (int) getValue());
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                        return true;
                }
                break;
            /**
             * The main rule screen.
             */
            case 48000:
                switch (buttonId) {
                    /**
                     * Save preset.
                     */
                    case 48067:
                        session.player.getSavedData().getGlobalData().setPresetDuelRules(session.getDuelRules());
                        player.getActionSender().sendMessage("Saved.");
                        return true;
                    /**
                     * Load preset.
                     */
                    case 48069:
                        if (player.getSavedData().getGlobalData().getPresetDuelRules() == null) {
                            player.getActionSender().sendMessage("You have no preset rules saved.");
                            return true;
                        }
                        for (DuelRule duelRule : DuelRule.values()) {
                            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule.getConfigIndex(), 0));
                            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(opponentSession.getPlayer(), duelRule.getConfigIndex(), 0));
                        }
                        session.duelRules.clear();
                        opponentSession.duelRules.clear();
                        for (DuelRule duelRule : session.player.getSavedData().getGlobalData().getPresetDuelRules()) {
                            session.toggleRule(player, duelRule.getButtonId());
                            opponentSession.toggleRule(player, duelRule.getButtonId());
                        }
                        return true;
                    /**
                     * Load previous.
                     */
                    case 48071:
                        if (player.getSavedData().getGlobalData().getLastDuelRules() == null) {
                            player.getActionSender().sendMessage("There are no previous rules.");
                            return true;
                        }
                        for (DuelRule duelRule : DuelRule.values()) {
                            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, duelRule.getConfigIndex(), 0));
                            PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(opponentSession.getPlayer(), duelRule.getConfigIndex(), 0));
                        }
                        session.duelRules.clear();
                        opponentSession.duelRules.clear();
                        for (DuelRule duelRule : session.player.getSavedData().getGlobalData().getLastDuelRules()) {
                            session.toggleRule(player, duelRule.getButtonId());
                            opponentSession.toggleRule(player, duelRule.getButtonId());
                        }
                        return true;
                    /**
                     * Accept.
                     */
                    case 48169:
                        session.updateStatus(player);
                        return true;
                    /**
                     * Decline.
                     */
                    case 48073:
                        decline(player);
                        return true;
                }
                session.toggleRule(player, buttonId);
                opponentSession.toggleRule(player, buttonId);
                return true;
            /**
             * The confirm interface.
             */
            case 48172:
                switch (buttonId) {
                    /**
                     * Accept.
                     */
                    case 48189:
                        session.updateStatus(player);
                        break;
                    /**
                     * Decline.
                     */
                    case 48192:
                        decline(player);
                        return true;
                }
                return false;
        }
        return false;
    }

    /**
     * Updates the stake container.
     */
    public void update() {

    }

    /**
     * Updates the status of the duel.
     *
     * @param p The player.
     */
    public void updateStatus(Player p) {
        DuelSession opponentSession = getExtension(opponent);
        switch (getDuelStage()) {
            case WAITING:
            case ACCEPT_TIMEOUT:
                if (opponentSession.getDuelStage() == DuelStage.ACCEPT_TIMEOUT || getDuelStage() == DuelStage.ACCEPT_TIMEOUT) {
                    break;
                }
                setDuelStage(DuelStage.ACCEPTED_WAITING);
                if (opponentSession.getDuelStage() == DuelStage.ACCEPTED_WAITING) {
                    setDuelStage(DuelStage.ACCEPTED);
                    opponentSession.setDuelStage(DuelStage.ACCEPTED);
                    player.getActionSender().sendString(opponent.getUsername(), 48175);
                    String combatDiffColor = TextUtils.getCombatDifferenceColour(player.getProperties().getCurrentCombatLevel(), opponent.getProperties().getCurrentCombatLevel());
                    player.getActionSender().sendString(combatDiffColor + Integer.toString(opponent.getProperties().getCurrentCombatLevel()), 48177);
                    player.getActionSender().sendString("", 48108);
                    opponent.getActionSender().sendString(player.getUsername(), 48175);
                    String combatDiffColor2 = TextUtils.getCombatDifferenceColour(opponent.getProperties().getCurrentCombatLevel(), player.getProperties().getCurrentCombatLevel());
                    opponent.getActionSender().sendString(combatDiffColor2 + Integer.toString(player.getProperties().getCurrentCombatLevel()), 48177);
                    opponent.getActionSender().sendString("", 48108);
                    Item[] opponentItems = DuelSession.getExtension(DuelSession.getExtension(player).getOpponent()).
                        getContainer().toArray();
                    for (int index = 0; index < 28; index++) {
                        Item item = index > container.toArray().length ? null : container.toArray()[index];
                        player.getActionSender().sendString((48207 + index), item == null ? "" : item.getName() + " <col=FFFFFF>x <col=" + TextUtils.getStackColor(item.getCount()) + ">" + TextUtils.intToKOrMil(item.getCount()));
                    }
                    for (int index = 0; index < 28; index++) {
                        Item item = index > opponentItems.length ? null : opponentItems[index];
                        player.getActionSender().sendString((48237 + index), item == null ? "" : item.getName() + " <col=FFFFFF>x <col=" + TextUtils.getStackColor(item.getCount()) + ">" + TextUtils.intToKOrMil(item.getCount()));
                    }
                    for (int index = 0; index < 28; index++) {
                        Item item = index > container.toArray().length ? null : container.toArray()[index];
                        opponent.getActionSender().sendString((48237 + index), item == null ? "" : item.getName() + " <col=FFFFFF>x <col=" + TextUtils.getStackColor(item.getCount()) + ">" + TextUtils.intToKOrMil(item.getCount()));
                    }
                    for (int index = 0; index < 28; index++) {
                        Item item = index > opponentItems.length ? null : opponentItems[index];
                        opponent.getActionSender().sendString((48207 + index), item == null ? "" : item.getName() + " <col=FFFFFF>x <col=" + TextUtils.getStackColor(item.getCount()) + ">" + TextUtils.intToKOrMil(item.getCount()));
                    }
                    player.getInterfaceState().closeDefaultTabs();
                    // TODO 317 Set tab to friends, and send basic player inventory
                    opponent.getInterfaceState().closeDefaultTabs();
                    player.getActionSender().sendString(48183, getStakeAmount() + " gp");
                    player.getActionSender().sendString(48184, opponentSession.getStakeAmount() + " gp");
                    opponent.getActionSender().sendString(48184, getStakeAmount() + " gp");
                    opponent.getActionSender().sendString(48183, opponentSession.getStakeAmount() + " gp");
                    DUEL_COMPONENT.setCloseEvent(null);
                    player.getInterfaceState().open(ACCEPT_COMPONENT);
                    opponent.getInterfaceState().open(ACCEPT_COMPONENT);
                    break;
                }
                player.getActionSender().sendString(48108, (rulesChanged ? "<col=F80000>" : "<col=AAAAAA>") + "Waiting for other player...");
                opponent.getActionSender().sendString(48108, (rulesChanged ? "<col=F80000>" : "<col=AAAAAA>") + "Other player has accepted");
                break;

            case ACCEPTED:
                setDuelStage(DuelStage.ACCEPTED_2_WAITING);
                if (opponentSession.getDuelStage() == DuelStage.ACCEPTED_2_WAITING) {
                    startDuel(p);
                    break;
                }
                // TODO 317
                player.getActionSender().sendString(48108, (rulesChanged ? "<col=F80000>" : "<col=AAAAAA>") + "Waiting for other player...");
                opponent.getActionSender().sendString(48108, (rulesChanged ? "<col=F80000>" : "<col=AAAAAA>") + "Other player has accepted");
                break;
        }
    }

    /**
     * Starts the duel, sending the players into the right arenas and enforcing rules.
     *
     * @param p The player.
     */
    public void startDuel(Player p) {
        DuelSession opponentSession = getExtension(opponent);
        setDuelStage(DuelStage.IN_PROGRESS);
        opponentSession.setDuelStage(DuelStage.IN_PROGRESS);
        resetInformation(player);
        resetInformation(opponent);
        player.getSavedData().getGlobalData().setLastDuelRules(getDuelRules());
        opponent.getSavedData().getGlobalData().setLastDuelRules(getDuelRules());
        // Remove worn items TODO 317 : Make sure we check upon accepting that the players have space to hold the items!
        for (DuelRule duelRule : duelRules) {
            if (!duelRule.isActivated(p)) {
                continue;
            }
            if (duelRule.getEquipmentSlot() == -1) {
                continue;
            }
            Item remove = player.getEquipment().get(duelRule.getEquipmentSlot());
            if (remove != null) {
                if (player.getEquipment().remove(remove, true)) {
                    player.getInventory().add(remove);
                }
            }
            remove = opponent.getEquipment().get(duelRule.getEquipmentSlot());
            if (remove != null) {
                if (opponent.getEquipment().remove(remove, true)) {
                    opponent.getInventory().add(remove);
                }
            }
        }
        setup(player, opponent);
        setup(opponent, player);
        List<Location> locationList = DuelArea.getSpawnLocations(DuelRule.OBSTACLES.isActivated(p), DuelRule.NO_MOVEMENT.isActivated(p));
        player.setTeleportTarget(locationList.get(0));
        opponent.setTeleportTarget(locationList.get(1));
        player.face(opponent);
        opponent.face(player);
        World.submit(pulse = new Pulse(2) {

            int timer = 3;

            @Override
            public boolean pulse() {
                // TODO Check nulls / logouts / etc
                player.sendChat(String.valueOf(timer));
                opponent.sendChat(String.valueOf(timer));
                timer--;
                if (timer < 0) {
                    // Duel started
                    player.sendChat("FIGHT!");
                    opponent.sendChat("FIGHT!");
                    getExtension(player).duelStarted = true;
                    getExtension(opponent).duelStarted = true;
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Sets the duel up for the player.
     *
     * @param player The player.
     */
    public void setup(Player player, Player opponent) {
        player.getInterfaceState().restoreTabs(); // TODO ?
        opponent.getInterfaceState().restoreTabs();
        player.getInterfaceState().close();
        opponent.getInterfaceState().close();
        player.getSkullManager().setSkullCheckDisabled(true);
        player.getSkullManager().setWilderness(true);
        player.getInteraction().remove(Option._P_CHALLENGE);
        player.getInteraction().set(ATTACK_OPTION);
        HintIconManager.registerHintIcon(player, opponent);
    }

    /**
     * Resets the player's duel.
     *
     * @param player The player.
     */
    public static void reset(Player player) {
        player.getSkullManager().setSkullCheckDisabled(false);
        player.getSkullManager().setWilderness(false);
        player.getInteraction().remove(ATTACK_OPTION);
        player.getInteraction().set(Option._P_CHALLENGE);
        player.getSkills().restore();
        player.getSettings().setSpecialEnergy(100);
        player.getHintIconManager().clear();
        player.removeAttribute("combat-time");
        player.getAppearance().sync();
        player.removeExtension(DuelSession.class);
    }

    /**
     * Resets the player's information.
     *
     * @param player The player.
     */
    public void resetInformation(Player player) {
        player.getPrayer().reset();
        player.getSkills().restore();
        player.getSkills().rechargePrayerPoints();
        player.getStateManager().reset();
    }

    /**
     * Gets the total amount the current stake is worth.
     *
     * @return The amount.
     */
    public long getStakeAmount() {
        long amount = 0;
        for (Item item : container.toArray()) {
            if (item == null) {
                continue;
            }
            amount += item.getValue();
        }
        return amount < 0 ? 0 : amount;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(48000, this);
        ComponentDefinition.put(48172, this);
        ComponentDefinition.put(637, this);
        ComponentDefinition.put(6574, this);
        return this;
    }

    /**
     * Gets the current player.
     *
     * @return The {@link #player}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current opponent.
     *
     * @return The {@link #opponent}.
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * Gets the currently enforced {@link plugin.activity.duelarena.DuelRule}s.
     *
     * @return The rules.
     */
    public List<DuelRule> getDuelRules() {
        return duelRules;
    }

    /**
     * Gets the {@link plugin.activity.duelarena.DuelStage} of this session.
     *
     * @return The duel stage.
     */
    public DuelStage getDuelStage() {
        return duelStage;
    }

    /**
     * Sets the {@link plugin.activity.duelarena.DuelStage} of this session.
     *
     * @param duelStage The duel stage.
     */
    public void setDuelStage(DuelStage duelStage) {
        if (duelStage == DuelStage.ACCEPT_TIMEOUT) {
            if (acceptTimeoutPulse != null) {
                acceptTimeoutPulse.restart();
                return;
            }
            player.getActionSender().sendString(48108, "<col=F80000>An option or stake has changed - check<br>before accepting!");
            opponent.getActionSender().sendString(48108, "<col=F80000>An option or stake has changed - check<br>before accepting!");
            player.getActionSender().sendString(48108, "");
            player.getActionSender().sendInterfaceConfig(27, 1);
            opponent.getActionSender().sendInterfaceConfig(27, 1);
            rulesChanged = true;
            getExtension(opponent).rulesChanged = true;
            World.submit(acceptTimeoutPulse = (new Pulse(3, opponent, player) {

                @Override
                public boolean pulse() {
                    acceptTimeoutPulse = null;
                    if (getExtension(opponent) != null) {
                        getExtension(opponent).acceptTimeoutPulse = null;
                        getExtension(opponent).setDuelStage(DuelStage.WAITING);
                    }
                    setDuelStage(DuelStage.WAITING);
                    player.getActionSender().sendString(48108, "");
                    opponent.getActionSender().sendString(48108, "");
                    player.getActionSender().sendInterfaceConfig(27, 0);
                    opponent.getActionSender().sendInterfaceConfig(27, 0);
                    return true;
                }
            }));
        }
        this.duelStage = duelStage;
    }

    /**
     * Gets the {@link plugin.activity.duelarena.StakeContainer}.
     *
     * @return The {@link #container}.
     */
    public StakeContainer getContainer() {
        return container;
    }

    /**
     * Gets a list of spoils from this duel.
     *
     * @return The item list.
     */
    public List<Item> getSpoils() {
        List<Item> items = new ArrayList<>();
        for (Item item : container.toArray()) {
            if (item == null) {
                continue;
            }
            items.add(item);
        }
        return items;
    }

    /**
     * Gets the duel waiting {@link org.gielinor.rs2.pulse.Pulse}.
     *
     * @return The pulse.
     */
    public Pulse getPulse() {
        return pulse;
    }

    /**
     * Gets an extension of this class for the player.
     *
     * @param player The player.
     * @return The session.
     */
    public static DuelSession getExtension(final Player player) {
        return player.getExtension(DuelSession.class);
    }

    public long getSpoilsValue() {
        long value = 0;
        for (Item item : getSpoils()) {
            value += item.getValue();
        }
        return value;
    }

    /**
     * Method used to retain the trade container.
     *
     * @param player the player.
     */
    public void retainContainer(final Player player) {
        final DuelSession duelSession = getExtension(player);
        player.getInventory().addAll(duelSession.getContainer());
    }

    /**
     * Gets if the duel has started.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasDuelStarted() {
        return duelStarted;
    }
}
