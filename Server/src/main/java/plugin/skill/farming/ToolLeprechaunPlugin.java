package plugin.skill.farming;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.Teleport.TeleportType;
import org.gielinor.game.content.skill.member.farming.FarmingNode;
import org.gielinor.game.content.skill.member.farming.FarmingPatch;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the option handler used for a tool leprachaun.
 *
 * @author 'Vexia
 */
public final class ToolLeprechaunPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(0).getConfigurations().put("option:exchange", this);
        NPCDefinition.forId(0).getConfigurations().put("option:teleport", this);
//        NPCDefinition.forId(3021).getConfigurations().put("option:exchange", this);
//        NPCDefinition.forId(4965).getConfigurations().put("option:exchange", this);
//        NPCDefinition.forId(3021).getConfigurations().put("option:teleport", this);
//        NPCDefinition.forId(4965).getConfigurations().put("option:teleport", this);
        new ToolLeprechaunDialogue().init();
        new BankNotePlugin().newInstance(arg);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "exchange":
                player.getFarmingManager().getEquipment().open(player);
                break;
            case "teleport":
                player.getTeleporter().send(Location.create(1638, 4709, 0), TeleportType.NORMAL, 3);
                break;
        }
        return true;
    }

    /**
     * Represents the plugin used to exchange farming products in bank notes.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankNotePlugin extends UseWithHandler {

        /**
         * Represents clean herbs.
         */
        private static final int[] CLEAN_HERBS = new int[]{ 249, 251, 253, 255, 257, 2998, 12172, 259, 261, 263, 3000, 265, 2481, 267, 269 };

        /**
         * Constructs a new {@code BankNotePlugin} {@code Object}.
         */
        public BankNotePlugin() {
            super(getProducts());
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(3021, NPC_TYPE, this);
            addHandler(4965, NPC_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            event.getUsedWith().asNpc().faceLocation(event.getPlayer().getLocation());
            FarmingNode node = null;
            for (FarmingPatch patch : FarmingPatch.values()) {
                for (FarmingNode n : patch.getNodes()) {
                    if (n.getProduct().getId() == event.getUsedItem().getId()) {
                        node = n;
                        break;
                    }
                }
            }
            if (node == null) {
                for (int cleanHerb : CLEAN_HERBS) {
                    if (event.getUsedItem().getId() == cleanHerb) {
                        if (event.getUsedItem().getDefinition().getNoteId() == -1) {
                            event.getPlayer().getDialogueInterpreter().sendDialogues((NPC) event.getUsedWith(), null, "Nay, I can't turn that into a banknote.");
                            return true;
                        }
                        int amount = event.getPlayer().getInventory().getCount(event.getUsedItem());
                        if (event.getPlayer().getInventory().remove(new Item(event.getUsedItem().getId(), amount))) {
                            event.getPlayer().getInventory().add(new Item(event.getUsedItem().getDefinition().getNoteId(), amount));
                        }
                        event.getPlayer().getDialogueInterpreter().sendItemMessage(event.getUsedItem(), "The leprechaun exchanges your items for banknotes.");
                        return true;
                    }
                }
            }
            if (node == null || event.getUsedItem().getDefinition().getNoteId() == -1) {
                event.getPlayer().getDialogueInterpreter().sendDialogues((NPC) event.getUsedWith(), null, "Nay, I can't turn that into a banknote.");
            } else {
                if (event.getUsedWith().getLocation().getRegionId() == 10548 && event.getUsedItem().getName().toLowerCase().contains("logs")) {
                    event.getPlayer().getDialogueInterpreter().sendDialogues((NPC) event.getUsedWith(), null, "Nay, " + TextUtils.formatDisplayName(event.getPlayer().getName()) + ", you can do that yourself!");
                    return true;
                }
                int amount = event.getPlayer().getInventory().getCount(event.getUsedItem());
                if (event.getPlayer().getInventory().remove(new Item(event.getUsedItem().getId(), amount))) {
                    event.getPlayer().getInventory().add(new Item(event.getUsedItem().getDefinition().getNoteId(), amount));
                }
                event.getPlayer().getDialogueInterpreter().sendItemMessage(event.getUsedItem(), "The leprechaun exchanges your items for banknotes.");
            }
            return true;
        }

        /**
         * Gets the products.
         *
         * @return the ids.
         */
        public static int[] getProducts() {
            final List<Integer> ids = new ArrayList<>();
            for (FarmingPatch patch : FarmingPatch.values()) {
                for (FarmingNode node : patch.getNodes()) {
                    ids.add(node.getProduct().getId());
                }
            }
            for (int herb : CLEAN_HERBS) {
                ids.add(herb);
            }
            final int[] array = new int[ids.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ids.get(i);
            }
            return array;
        }

        // @Override
        // public boolean isWalks() {
        //     return true;
        // }
    }

    /**
     * Represents the dialogue plugin used for the tool leprachaun.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class ToolLeprechaunDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code ToolLeprechaunDialogue} {@code Object}.
         */
        public ToolLeprechaunDialogue() {
            /**
             * empty.
             */
        }

        public ToolLeprechaunDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ToolLeprechaunDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            npc("Ah, 'tis a foine day to be sure! Were yez wantin' me to", "store yer tools, or maybe ye might be wantin' yer stuff", "back from me?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes please.", "What can you store?", "What do you do with the tools you're storing?", "No thanks, I'll keep hold of my stuff.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            player("Yes please.");
                            stage = 10;
                            break;
                        case FOUR_OPTION_TWO:
                            player("What can you store?");
                            stage = 20;
                            break;
                        case FOUR_OPTION_THREE:
                            player("What do you do with the tools you're storing?", "They can't possibly all fit in your pockets!");
                            stage = 30;
                            break;
                        case FOUR_OPTION_FOUR:
                            player("No thanks, I'll keep hold of my stuff.");
                            stage = 40;
                            break;
                    }
                    break;
                case 10:
                    end();
                    player.getFarmingManager().getEquipment().open(player);
                    break;
                case 20:
                    npc("We'll hold onto yer rake, yer seed dibber, yer spade,", "yer secateurs, yer waterin' can and yer trowel - but", "mind it's not one of them fancy trowels only", "achaeologists use!");
                    stage = 21;
                    break;
                case 21:
                    npc("We'll take a few buckets off yer hands too, and even", "yer compost and yer supercompost. There's room in", "our shed for plenty of compost, so bring it on!");
                    stage = 22;
                    break;
                case 22:
                    npc("Also, if you hand me yer farming produce, I might be", "able to change it into banknotes.");
                    stage = 23;
                    break;
                case 23:
                    end();
                    break;
                case 30:
                    npc("We leprechauns have a shed where we keep 'em. It's a", "magic shed, so ye can get yer items back from any of", "us leprechauns whenever ye want. Saves ye havin' to", "carry loads of stuff around the country!");
                    stage = 31;
                    break;
                case 31:
                    end();
                    break;
                case 40:
                    npc("Ye must be dafter then ye look if ye likes luggin' yer", "tools everywhere ye goes!");
                    stage = 41;
                    break;
                case 41:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 3021, 4965 };
        }
    }
}
