package plugin.dialogue;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.Starter;
import org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.World;
import org.gielinor.utilities.string.TextUtils;

/**
 * The dialogue plugin for a player's starter package.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class StarterDialogue extends DialoguePlugin {

    /**
     * The starter package selected.
     */
    private StarterPackage starterPackage;

    /**
     * Constructs a new {@link StarterDialogue} {@link DialoguePlugin}.
     */
    public StarterDialogue() {
    }

    /**
     * Constructs a new {@link StarterDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public StarterDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new StarterDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        this.starterPackage = (StarterPackage) args[0];
        if (args.length == 2) {
            stage = (int) args[1];
            handle(0, null);
            return true;
        }
        interpreter.sendOptions("Select an Option", "View description", "Preview inventory", "Preview bank", "Use this package", "Choose another package");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player.getDialogueInterpreter().close();
                int childId = 47907;
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        String packageName = starterPackage.name().toLowerCase();
                        player.getActionSender().sendQuestInterface(TextUtils.formatDisplayName(packageName.replaceAll("_", " ")),
                            new CloseEvent() {

                                @Override
                                public void close(Player player, Component component) {
                                    player.getDialogueInterpreter().open("StarterDialogue", starterPackage);
                                }

                                @Override
                                public boolean canClose(Player player, Component component) {
                                    return true;
                                }
                            }, DataShelf.fetchStringArray(packageName.toLowerCase() + "_starter_package_menu"));
                        break;
                    case FIVE_OPTION_TWO:
                        player.getActionSender().sendString(TextUtils.formatDisplayName(starterPackage.name().replaceAll("_", " ")) + " package inventory preview", 47905);
                        for (int index = 0; index <= 90; index++) {
                            player.getActionSender().sendUpdateItem(null, (childId + index), 0);
                        }
                        if (starterPackage.getInventory() != null) {
                            for (Item inventory : starterPackage.getInventory()) {
                                player.getActionSender().sendUpdateItem(inventory, (childId++), 0);
                            }
                        }
                        Component inventoryPreviewComponent = new Component(47900);
                        inventoryPreviewComponent.setCloseEvent(new CloseEvent() {

                            @Override
                            public void close(Player player, Component component) {
                                player.getDialogueInterpreter().open("StarterDialogue", starterPackage);
                            }

                            @Override
                            public boolean canClose(Player player, Component component) {
                                return true;
                            }
                        });
                        player.getInterfaceState().open(inventoryPreviewComponent);
                        break;
                    case FIVE_OPTION_THREE:
                        player.getActionSender().sendString(TextUtils.formatDisplayName(starterPackage.name().replaceAll("_", " ")) + " package bank preview", 47905);
                        for (int index = 0; index <= 90; index++) {
                            player.getActionSender().sendUpdateItem(null, (childId + index), 0);
                        }
                        if (starterPackage.getBank() != null) {
                            for (Item bank : starterPackage.getBank()) {
                                player.getActionSender().sendUpdateItem(bank, (childId++), 0);
                            }
                        }
                        Component bankPreviewComponent = new Component(47900);
                        bankPreviewComponent.setCloseEvent(new CloseEvent() {

                            @Override
                            public void close(Player player, Component component) {
                                player.getDialogueInterpreter().open("StarterDialogue", starterPackage);
                            }

                            @Override
                            public boolean canClose(Player player, Component component) {
                                return true;
                            }
                        });
                        player.getInterfaceState().open(bankPreviewComponent);
                        break;
                    case FIVE_OPTION_FOUR:
                        close();
                        player.unlock();
                        player.getSavedData().getGlobalData().setStarterPackage(starterPackage);
                        starterPackage.additional(player);
                        boolean placeInBank = false;
                        if (starterPackage.getInventory() != null) {
                            if (player.getInventory().freeSlots() > starterPackage.getInventory().length) {
                                for (Item inventory : starterPackage.getInventory()) {
                                    player.getInventory().add(inventory);
                                }
                            } else {
                                placeInBank = true;
                            }
                        }
                        if (starterPackage.getBank() != null) {
                            for (Item bank : starterPackage.getBank()) {
                                player.getBank().add(bank);
                            }
                        }
                        if (placeInBank) {
                            player.getActionSender().sendMessage("Your inventory starter items have been placed in your bank.");
                            if (starterPackage.getInventory() != null) {
                                for (Item inventory : starterPackage.getInventory()) {
                                    player.getBank().add(inventory);
                                }
                            }
                        }
                        if (!World.getConfiguration().isBetaEnabled()) {
                            ClanCommunication clanCommunication = ClanCommunication.DEFAULT;
                            player.setAttribute("NEW_PLAYER_CLAN", 1);
                            if (clanCommunication != null && !clanCommunication.getName().equals("Chat disabled")) {
                                boolean entered = clanCommunication.enter(player);
                                if (entered) {
                                    player.getCommunication().setClan(clanCommunication);
                                    player.getCommunication().getClan().update();
                                }
                                player.getSavedData().getGlobalData().setLastClanChat("help");
                            }
                        }
                        player.removeAttribute("STARTER_PACKAGE_SELECTION");
                        Component characterDesign = new Component(3559);

                        //if (!World.getSettings().isBeta()) {
                        characterDesign.setCloseEvent(new CloseEvent() {

                            @Override
                            public void close(Player player, Component component) {
                                player.getDialogueInterpreter().open("StarterGuide", true);
                            }

                            @Override
                            public boolean canClose(Player player, Component component) {
                                return true;
                            }
                        });
                        // }
                        player.getInterfaceState().open(characterDesign);
                        return true;
                    case FIVE_OPTION_FIVE:
                        end();
                        player.lock();
                        player.setAttribute("STARTER_PACKAGE_SELECTION", 1);
                        player.getActionSender().sendString(27259, "Select your starter package");
                        int index = 27167;
                        int slot = 27197;
                        for (int clearId = index; clearId < 27197; clearId++) {
                            player.getActionSender().sendString(clearId, "");
                        }
                        for (int slotId = 0; slotId < 30; slotId++) {
                            player.getActionSender().sendUpdateItem(slot + slotId, 0, null);
                        }
                        for (Starter.StarterPackage starterPackage : Starter.StarterPackage.values()) {
                            player.getActionSender().sendString(index, TextUtils.formatDisplayName(starterPackage.name().replaceAll("_", " ")) + " starter package");
                            if (starterPackage.getItem() != null) {
                                player.getActionSender().sendUpdateItem(slot, 0, starterPackage.getItem());
                            }
                            index++;
                            slot++;
                        }
                        player.getInterfaceState().open(new Component(27135));
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("StarterDialogue") };
    }
}
