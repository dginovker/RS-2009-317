package plugin.dialogue;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Mi-Gor.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MiGorDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MiGorDialogue} {@code Object}.
     */
    public MiGorDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MiGorDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MiGorDialogue(final Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MiGorDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (player.getInventory().contains(10888)) {
            npc("*Wheeze* Well... I see you have defeated him.", "The least I could do for you is fix the anchor.");
            stage = 100;
            return true;
        }
        player("Hello.");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("*Wheeze* Hello " + TextUtils.formatDisplayName(player.getUsername()) + ". I've created the perfect being", "to destroy all who attempts to slay it.");
                stage++;
                break;
            case 1:
                npc("*Wheeze* Do you dare attempt to defeat him? I will", "let you and anyone else have a go at him.");//, "I can even offer you your own instance to fight him.");
                stage++;
                break;
            case 2:
                npc("*Wheeze* So what do you say? Would you like to", "fight him?");// or even have your own instance to fight him?");
                stage++;
                break;
            case 3:
                options("Yes, I want to fight him!", /*"Fight him in my own instance",*/ "No thanks! I don't want to die!");
                stage++;
                break;
            case 4:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, I want to fight him!");
                        stage = 5;
                        break;
//                    case THREE_OPTION_TWO:
//                        player("Yes, I want to fight him in my own instance!");
//                        stage = 7;
//                        break;
                    case TWO_OPTION_TWO:
                        player("No thanks! I don't want to die!");
                        stage = END;
                        break;
                }
                break;
            case 5:
                npc("*Wheeze* Very well, good luck!");
                stage = 6;
                break;
            case 6:
                end();
                player.getTeleporter().send(Location.create(2762, 9185, 0));
                break;
            case 7:
                npc("*Wheeze* Very well, I will give you one little warning!", "If you die there, there is no chance to retrieve your items!");
                stage = 8;
                break;
            case 8:
                options("Yes, take me to him!", "Never mind.");
                stage = 9;
                break;
            case 9:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        ActivityManager.start(player, "barrelchest", false);
//                        player.lock(7);
//                        World.submit(new Pulse(2) {
//                            int count = 0;
//
//                            @Override
//                            public boolean pulse() {
//                                this.setDelay(1);
//                                switch (count++) {
//                                    case 0:
//                                        player.getInterfaceState().openComponent(8677);
//                                        player.saveAttribute("barrelchest", true);
//                                        break;
//                                    case 3:
//                                        DynamicRegion dynamicRegion = DynamicRegion.create(11151);
//                                        RegionManager.getRegionCache().put(dynamicRegion.getId(), dynamicRegion);
//                                        Location finalLocation = dynamicRegion.getBaseLocation().transform(10, 33, 0);
//                                        player.getProperties().setTeleportLocation(finalLocation);
//                                        NPC npc1 = new BarrelchestNPC(5666, finalLocation.transform(4, 8, 0));
//                                        npc1.init();
//                                        break;
//                                    case 5:
//                                        player.unlock();
//                                        player.getInterfaceState().close();
//                                        player.getInterfaceState().openDefaultTabs();
//                                        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
//                                        stage = END;
//                                        return true;
//                                }
//                                return false;
//                            }
//                        });
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 100:
                npc("*Wheeze* Of course for a price!", "I will fix it like new for you, for... 300,000 coins!", "What do you say?");
                stage = 101;
                break;
            case 101:
                interpreter.sendOptions("Fix?", "Yes, fix it for 300k coins.", "No way!");
                stage = 102;
                break;
            case 102:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (!player.getInventory().contains(new Item(Item.COINS, 300000))) {
                            npc("*Wheeze* You can't pull a fast one on me!", "I said 300,000 coins.");
                            stage = END;
                            break;
                        }
                        if (!player.getInventory().contains(10888)) {
                            npc("*Wheeze* You need the anchor for me to fix it!");
                            stage = END;
                            break;
                        }
                        npc("*Wheeze* Hm, yes... Lets see, hm, oh, there it is.");
                        stage = 103;
                        break;
                    case TWO_OPTION_TWO:
                        player("No way!");
                        stage = END;
                        break;
                }
                break;
            case 103:
                npc("*Wheeze* Where does... Hm, I think I have it....", "Uh oh.....");
                stage = 104;
                break;
            case 104:
                npc("*Wheeze* Just... move this over to there...", "Yes... I think.");
                stage = 105;
                break;
            case 105:
                if (player.getInventory().remove(new Item(Item.COINS, 30000), new Item(10888))) {
                    player.getInventory().add(new Item(10887));
                    interpreter.sendItemMessage(10887, "Mi-Gor fixes the anchor for 300,000 coins!");
                }
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5613 };
    }

}

