package org.gielinor.game.content.global.quest.impl;

import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the Gertrude's cat quest.
 *
 * @author 'Vexia
 */
public class GertrudesCat extends Quest {

    /**
     * The name of this quest.
     */
    public static final String NAME = "Gertrude's Cat";

    /**
     * Constructs a new {@code GertrudesCat} {@code Object}.
     *
     * @param player The player to construct the class for.
     */
    public GertrudesCat(final Player player) {
        super(player);
    }

    @Override
    public void update() {
        super.clear();
        switch (stage) {
            case 0:
                getLines().add(getBlue() + "I can start this quest by speaking to " + getRed() + "Gertrude.");
                getLines().add(getBlue() + "She can be found to the " + getRed() + "west of Varrock.");
                break;
            case 10:
                getLines().add("<str>I accepted the challenge of finding Gertrude's lost cat.");
                getLines().add(getBlue() + "I need " + getRed() + "to speak to Shilop and Wilough" + getBlue() + " at the " + getRed() + "marketplace.");
                break;
            case 20:
                getLines().add("<str>I accepted the challenge of finding Gertrude's lost cat.");
                getLines().add("<str>I spoke to Shilop, Gertrude's Son.");
                getLines().add(getBlue() + "I need to " + getRed() + "go to their play area " + getBlue() + "and " + getRed() + "find the lost cat and");
                getLines().add(getRed() + "return it to Gertrude.");
                break;
            case 30:
                getLines().add("<str>I accepted the challenge of finding Gertrude's lost cat.");
                getLines().add("<str>I spoke to Shilop, Gertrude's Son.");
                getLines().add("<str>I found the lost cat but it won't come back.</str>");
                getLines().add("");
                getLines().add(getRed() + "I still need to " + getRed() + "get her to follow me home.");
                break;
            case 40:
            case 50:
                getLines().add("<str>I accepted the challenge of finding Gertrude's lost cat.");
                getLines().add("<str>I spoke to Shilop, Gertrude's Son.");
                getLines().add("<str>I found the lost cat but it won't come back.</str>");
                getLines().add("<str>I gave the cat milk and sardines.</str>");
                getLines().add("");
                getLines().add(getBlue() + "I still need " + getRed() + "get her to follow me home.");
                break;
            case 60:
                getLines().add("<str>I accepted the challenge of finding Gertrude's lost cat.");
                getLines().add("<str>I spoke to Shilop, Gertrude's Son.");
                getLines().add("<str>I found the lost cat but it won't come back.</str>");
                getLines().add("<str>I gave the cat milk and sardines.</str>");
                getLines().add("");
                getLines().add(getBlue() + "She ran off home.");
                break;
            case 100:
                getLines().add("<str>I helped Gertrude to find her lost cat,");
                getLines().add("<str>I fed it and returned her missing kitten,");
                getLines().add("<str>Gertrude gave me my very own pet for a reward.");
                getLines().add("");
                getLines().add("<col=FF0000>QUEST COMPLETE!");
                break;
        }
        player.getQuestMenuManager().setLines(getLines());
    }

    @Override
    public void finish() {
        final Item kitten = getKitten();
        player.getConfigManager().set(101, player.getQuestRepository().getPoints());
        super.completeQuest("Gertrude's Cat", 100, kitten.getId(), "1 Quest Point",
            "A kitten!", "1525 Cooking XP",
            "A chocolate cake", "A bowl of stew", "Raise cats.");
        player.getSkills().addExperience(Skills.COOKING, 1525);
        setStage(100);
        setState(QuestState.COMPLETED);
        player.getInventory().add(kitten);
        player.getFamiliarManager().summon(kitten, true, true);
        final Item cake = new Item(1897);
        final Item stew = new Item(2003);
        if (!player.getInventory().add(cake)) {
            GroundItemManager.create(cake, player.getLocation(), player);
        }
        if (!player.getInventory().add(stew)) {
            GroundItemManager.create(stew, player.getLocation(), player);
        }
    }

    /**
     * Method used to get a random kitten.
     *
     * @return the item.
     */
    public Item getKitten() {
        return new Item(RandomUtil.random(1555, 1560));
    }

    @Override
    public int getIndex() {
        return 29162;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getQuestPoints() {
        return 1;
    }

    @Override
    public int getId() {
        return 2;
    }
}
