package org.gielinor.game.content.global.quest.impl;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

import plugin.interaction.object.KingGjukiChestPlugin;

/**
 * Represents The Lost Kingdom {@link org.gielinor.game.content.global.quest.Quest}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class TheLostKingdom extends Quest {

    /**
     * The name of this quest.
     */
    public static final String NAME = "The Lost Kingdom";
    /**
     * Represents the Silly jester hat item.
     */
    private static final Item SILLY_JESTER_HAT = new Item(10836);
    /**
     * Represents the Silly jester top item.
     */
    private static final Item SILLY_JESTER_TOP = new Item(10837);
    /**
     * Represents the Silly jester tights item.
     */
    private static final Item SILLY_JESTER_TIGHTS = new Item(10838);
    /**
     * Represents the Silly jester boots item.
     */
    private static final Item SILLY_JESTER_BOOTS = new Item(10839);
    /**
     * Represents the jester stick item.
     */
    private static final Item JESTER_STICK = new Item(10840);

    /**
     * Constructs a new <code>TheLostKingdom</code> {@link org.gielinor.game.content.global.quest.Quest}.
     */
    public TheLostKingdom(final Player player) {
        super(player);
    }


    @Override
    public void update() {
        super.clear();
        switch (stage) {
            case 0:
                getLines().add("<col=08088A>I can start this quest by talking to <col=8A0808>King Roald</col><col=08088A> who can be");
                getLines().add("<col=08088A>found in the</col>" + getRed() + " Varrock palace.");
                getLines().add("");
                getLines().add("<col=8A0808>I will have to defeat a high level enemy and");
                getLines().add("<col=8A0808>evade many level-45 guards");
                break;
            case 10:
            case 15:
                getLines().add("<col=08088A>King Roald's Kingdom has fallen under attack,");
                getLines().add("<col=08088A>he wants me to go to Jatizso and try to");
                getLines().add("<col=08088A>figure out what King Gjuki has planned for him.");
                break;
            case 20:
                getLines().add("<col=08088A>King Roald's Kingdom has fallen under attack,");
                getLines().add("<col=08088A>he wants me to go to Jatizso and try to");
                getLines().add("<col=08088A>figure out what King Gjuki has planned for him.");
                getLines().add("");
                getLines().add("<col=08088A>I tried talking to King Gjuki, and he ignored me.");
                getLines().add("<col=08088A>I should tell King Roald about this.");
                break;
            case 21:
                getLines().add("<col=08088A>King Roald's Kingdom has fallen under attack,");
                getLines().add("<col=08088A>he wants me to go to Jatizso and try to");
                getLines().add("<col=08088A>figure out what King Gjuki has planned for him.");
                getLines().add("");
                getLines().add("<col=08088A>I tried talking to King Gjuki, and he ignored me.");
                getLines().add("<col=08088A>I should tell King Roald about this.");
                getLines().add("");
                getLines().add("<col=08088A>King Roald suggested I pose as a Jester, which");
                getLines().add("<col=08088A>I can find the outfit in his chest.");
                break;
            case 25:
                getLines().add("<str><col=08088A>King Roald suggested I pose as a Jester, which");
                getLines().add("<str><col=08088A>I can find the outfit in his chest.");
                getLines().add("");
                getLines().add("<col=08088A>After doing some tricks and posing as a Jester");
                getLines().add("<col=08088A>I found out a few things about the attack plot...");
                getLines().add("<col=08088A>I should report my findings to <col=8A0808>King Roald</col><col=08088A>.");
                break;
            case 30:
                getLines().add("<str><col=08088A>King Roald suggested I pose as a Jester, which");
                getLines().add("<str><col=08088A>I can find the outfit in his chest.");
                getLines().add("");
                getLines().add("<str><col=08088A>After doing some tricks and posing as a Jester");
                getLines().add("<str><col=08088A>I found out a few things about the attack plot...");
                getLines().add("<str><col=08088A>I should report my findings to <col=8A0808>King Roald</col><col=08088A>.");
                getLines().add("");
                getLines().add("<col=08088A>As I was spying on King Gjuki, he figured it out!");
                getLines().add("<col=08088A>Aeonisig was working with King Gjuki all along, and");
                getLines().add("<col=08088A>stole the Varrock Palace deed!");
                if (player.hasItem(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    getLines().add("<col=08088A>I have the <col=8A0808>Varrock Palace deed</col><col=08088A>! I should return");
                    getLines().add("<col=08088A>it to King Roald!");
                } else {
                    getLines().add("<col=08088A>King Roald wants me to go back and take the deed");
                    getLines().add("<col=08088A>back, which is located in a chest behind King Gjuki's");
                    getLines().add("<col=08088A>throne.");
                    getLines().add("<col=08088A>I should prepare to fight anything...");
                }
                break;
            case 100:
                getLines().add("<str><col=08088A>King Roald's Kingdom has fallen under attack,");
                getLines().add("<str><col=08088A>he wants me to go to Jatizso and try to");
                getLines().add("<str><col=08088A>figure out what King Gjuki has planned for him.");
                getLines().add("");
                getLines().add("<str><col=08088A>After doing some tricks and posing as a Jester");
                getLines().add("<str><col=08088A>I found out a few things about the attack plot...");
                getLines().add("<str><col=08088A>I should report my findings to <col=8A0808>King Roald</col><col=08088A>.");
                getLines().add("");
                getLines().add("<str><col=08088A>As I was spying on King Gjuki, he figured it out!");
                getLines().add("<str><col=08088A>Aeonisig was working with King Gjuki all along, and");
                getLines().add("<str><col=08088A>stole the Varrock Palace deed!");
                getLines().add("");
                getLines().add("<col=FF0000>QUEST COMPLETE!");
                getLines().add("<col=08088A>Completing the quest, I can now purchase gloves from");
                getLines().add("<col=08088A>King Roald's chest in the Varrock Palace.");
                break;
        }
        player.getQuestMenuManager().setLines(getLines());
    }

    /**
     * Checks if the player has the required ashes.
     *
     * @param id The ashes id.
     * @return <code>True</code> if so.
     */
    public boolean has(int id) {
        return player.getInventory().contains(id) || player.getBank().contains(id);
    }

    @Override
    public void finish() {
        super.completeQuest("The Lost Kingdom", 100, 10840, "3 Quest Points",
            "A jester stick",
            "Access to King Roald's chest for", "armoured gloves");
        player.getInventory().add(new Item(10840), true);
    }

    @Override
    public int getIndex() {
        return 29164;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getQuestPoints() {
        return 3;
    }

    @Override
    public boolean hasRequirements() {
        return true;
    }

    @Override
    public int getId() {
        return 3;
    }

    /**
     * Checks if the player is wearing full Jester.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public static boolean isWearingJester(Player player) {
        return player.getEquipment().contains(10836) && player.getEquipment().contains(10837) &&
            player.getEquipment().contains(10838) && player.getEquipment().contains(10839);
    }

    /**
     * Gets the Jester items the player needs.
     *
     * @param player The player.
     * @return The items, if any.
     */
    public static Item[] getJesterItems(Player player) {
        List<Item> jester = new ArrayList<>();
        if (!player.hasItem(SILLY_JESTER_HAT)) {
            jester.add(SILLY_JESTER_HAT);
        }
        if (!player.hasItem(SILLY_JESTER_TOP)) {
            jester.add(SILLY_JESTER_TOP);
        }
        if (!player.hasItem(SILLY_JESTER_TIGHTS)) {
            jester.add(SILLY_JESTER_TIGHTS);
        }
        if (!player.hasItem(SILLY_JESTER_BOOTS)) {
            jester.add(SILLY_JESTER_BOOTS);
        }
        if (!player.hasItem(JESTER_STICK) && player.getQuestRepository().getQuest(NAME).getState() == QuestState.COMPLETED) {
            jester.add(JESTER_STICK);
        }
        if (jester.size() == 0) {
            return null;
        }
        return jester.toArray(new Item[jester.size()]);
    }
}
