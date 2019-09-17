package org.gielinor.game.content.global.quest.impl;

import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the Curse of the Undead {@link org.gielinor.game.content.global.quest.Quest}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CurseOfTheUndead extends Quest {

    /**
     * The name of this quest.
     */
    public static final String NAME = "Curse of the Undead";

    /**
     * Constructs a new <code>CurseOfTheUndead</code> {@link org.gielinor.game.content.global.quest.Quest}.
     */
    public CurseOfTheUndead(final Player player) {
        super(player);
    }


    @Override
    public void update() {
        super.clear();
        switch (stage) {
            case 0:
                getLines().add("<col=08088A>I can start this quest by talking to the <col=8A0808>Strange old man</col><col=08088A> who is");
                getLines().add("<col=08088A>located at the</col>" + getRed() + " barrows mounds.");
                if (!hasRequirements()) {
                    getLines().add("To start this quest I need:");
                    getLines().add((player.getSkills().getStaticLevel(Skills.PRAYER) >= 50 ? "<str>" : "") + "<col=08088A>50+ Prayer");
                    getLines().add((player.getSkills().getStaticLevel(Skills.MAGIC) >= 94 ? "<str>" : "") + "<col=08088A>94+ Magic");
                    getLines().add((player.getSavedData().getActivityData().getBarrowsChestCount() >= 5 ? "<str>" : "") + "<col=08088A>I must also have looted at least 5 Barrows chests.");
                }
                break;
            case 1:
                getLines().add("<col=08088A>The strange old man told me that, people who have been");
                getLines().add("<col=08088A>visiting the barrows mounds");
                getLines().add("<col=08088A>are suddenly becoming sick.");
                getLines().add("");
                getLines().add("<col=08088A>He told me he wasn't sure if it were the mounds");
                getLines().add("<col=08088A>until he himself started to feel ill.");
                getLines().add("");
                getLines().add("<col=08088A>He feels it has something to do with the undead brothers lying");
                getLines().add("<col=08088A>beneath the mounds.");
                getLines().add("");
                getLines().add("<col=08088A>He has given me " + getRed() + "notes, which I am to deliver to the");
                getLines().add(getRed() + "Apothecary</col><col=08088A> who is located in " + getRed() + "Varrock.");
                break;
            case 2:
                getLines().add("<str><col=08088A>The strange old man told me that, people who have been");
                getLines().add("<str><col=08088A>visiting the barrows mounds");
                getLines().add("<str><col=08088A>are suddenly becoming sick.");
                getLines().add("");
                getLines().add("<str><col=08088A>He told me he wasn't sure if it were the mounds");
                getLines().add("<str><col=08088A>until he himself started to feel ill.");
                getLines().add("");
                getLines().add("<str><col=08088A>He feels it has something to do with the undead brothers lying");
                getLines().add("<str><col=08088A>beneath the mounds.");
                getLines().add("");
                getLines().add("<str><col=08088A>He has given me " + getRed() + "notes, which I am to deliver to the");
                getLines().add("<str>" + getRed() + "Apothecary</col><col=08088A> who is located in " + getRed() + "Varrock.");
                getLines().add("<col=08088A>I have given the notes to the apothecary.");
                break;
            case 3:
                getLines().add("<str><col=08088A>The strange old man told me that, people who have been");
                getLines().add("<str><col=08088A>visiting the barrows mounds");
                getLines().add("");
                getLines().add("<str><col=08088A>He feels it has something to do with the undead brothers lying");
                getLines().add("<str><col=08088A>beneath the mounds.");
                getLines().add("");
                getLines().add("<col=08088A>I have given the notes to the apothecary.");
                getLines().add("");
                getLines().add("<col=08088A>He has asked me to obtain the ashes of the " + getRed() + " barrows brothers</col><col=08088A>.");
                getLines().add("<col=08088A>I can do this by defeating each of them.");
                getLines().add("<col=08088A>The ashes I still need are:");
                getLines().add(has(Item.AHRIM_ASHES) ? "<str>" : "<col=FF00FF>" + "Ahrim the Blighted");
                getLines().add(has(Item.DHAROK_ASHES) ? "<str>" : "<col=FF00FF>" + "Dharok the Wretched");
                getLines().add(has(Item.GUTHAN_ASHES) ? "<str>" : "<col=FF00FF>" + "Guthan the Infested");
                getLines().add(has(Item.KARIL_ASHES) ? "<str>" : "<col=FF00FF>" + "Karil the Tainted");
                getLines().add(has(Item.TORAG_ASHES) ? "<str>" : "<col=FF00FF>" + "Torag the Corrupted");
                getLines().add(has(Item.VERAC_ASHES) ? "<str>" : "<col=FF00FF>" + "Verac the Defiled");
                if (player.getInventory().contains(Item.AHRIM_ASHES, Item.DHAROK_ASHES,
                    Item.GUTHAN_ASHES, Item.KARIL_ASHES, Item.TORAG_ASHES,
                    Item.VERAC_ASHES)) {
                    getLines().add("<col=08088A>I have all of the ashes, I should go see the Apothecary.");
                }
                break;
            case 4:
                getLines().add("<col=08088A>The apothecary has given me a potion to give to the");
                getLines().add("<col=08088A>strange old man. The apothecary feels it is the cure he needs.");
                break;
            case 5:
                getLines().add("<str><col=08088A>The apothecary has given me a potion to give to the");
                getLines().add("<str><col=08088A>strange old man. The apothecary feels it is the cure he needs.");
                getLines().add("");
                getLines().add("<col=08088A>After giving the potion to the strange old man, Surok told me");
                getLines().add("<col=08088A>that it was his doing all along, I must defeat him in attempt");
                getLines().add("<col=08088A>to free everyone of this disease.");
                break;
            case 10:
                getLines().add("<str><col=08088A>The apothecary has given me a potion to give to the");
                getLines().add("<str><col=08088A>strange old man. The apothecary feels it is the cure he needs.");
                getLines().add("");
                getLines().add("<str><col=08088A>After giving the potion to the strange old man, Surok told me");
                getLines().add("<str><col=08088A>that it was his doing all along, I must defeat him in attempt");
                getLines().add("<str><col=08088A>to free everyone of this disease.");
                getLines().add("<col=08088A>I have defeated Surok, I should now go see the " + getRed() + "Strange old man");
                break;
            case 100:
                getLines().add("<str><col=08088A>The apothecary has given me a potion to give to the");
                getLines().add("<str><col=08088A>strange old man. The apothecary feels it is the cure he needs.");
                getLines().add("");
                getLines().add("<str><col=08088A>After giving the potion to the strange old man, Surok told me");
                getLines().add("<str><col=08088A>that it was his doing all along, I must defeat him in attempt");
                getLines().add("<str><col=08088A>to free everyone of this disease.");
                getLines().add("");
                getLines().add("<col=FF0000>QUEST COMPLETE!");
                getLines().add("<col=08088A>Completing the quest, I have unlocked the ability to use the Ancient Curses");
                getLines().add("<col=08088A>prayer book, which I can switch to by using the ancient altar at home.");
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
        super.completeQuest("Curse of the Undead", 100, 11139, "2 Quest Points",
            "1 Antique lamp for any skill 40+", "25,000 Prayer experience",
            "50,000 Magic experience", "Access to Ancient Curses");
        player.getInventory().add(new Item(11139), true);
        player.getSkills().addExperienceNoMod(Skills.PRAYER, 25000);
        player.getSkills().addExperienceNoMod(Skills.MAGIC, 50000);
    }

    @Override
    public int getIndex() {
        return 29161;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getQuestPoints() {
        return 2;
    }

    @Override
    public boolean hasRequirements() {
        return player.getSkills().getStaticLevel(Skills.PRAYER) >= 50 &&
            player.getSkills().getStaticLevel(Skills.MAGIC) >= 94 &&
            player.getSavedData().getActivityData().getBarrowsChestCount() >= 5;
    }

    @Override
    public int getId() {
        return 1;
    }
}
