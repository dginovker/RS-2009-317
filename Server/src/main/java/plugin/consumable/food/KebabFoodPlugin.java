package plugin.consumable.food;

import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents a kebab food plugin. I'm sorry emperor :'( i dont want to do it either.
 * @author 'Vexia
 * @version 1.0
 */
public final class KebabFoodPlugin extends Food {

    /**
     * Represents the messages to display when eating a kebab.
     */
    private static final String[] MESSAGES = new String[]{ "That kebab didn't seem to do a lot.", "It restores some life points.", "That was a good kebab. You feel a lot better.", "Wow, that was an amazing kebab! You feel invigorated.", "That tasted a bit dodgy. You feel a bit ill.", "Eating the kebab has damaged your Skills stats." };

    @Override
    public KebabFoodPlugin newInstance(final Object object) {
        Consumables.add(new KebabFoodPlugin());
        return this;
    }

    /**
     * Constructs a new {@code KebabFoodPlugin} {@code Object}.
     */
    public KebabFoodPlugin() {
        super(1971, 1);
    }

    @Override
    public void consume(final Item item, final Player player) {
        int index = RandomUtil.random(MESSAGES.length);
        String message = MESSAGES[index];
        int health = getHealth(player, index);
        super.consume(item, player, health, message);
    }

    /**
     * Gets the health regain.
     * @param player the player.
     * @param index the index.
     * @return the health.
     */
    public int getHealth(Player player, int index) {
        switch (index) {
            case 1:
                return (int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10);
            case 2:
                return RandomUtil.random(10, 20);
            case 3:
                addBonus(player, new SkillBonus(Skills.ATTACK, 0.02));
                addBonus(player, new SkillBonus(Skills.STRENGTH, 0.02));
                addBonus(player, new SkillBonus(Skills.DEFENCE, 0.02));
                return RandomUtil.random(20, 30);
            case 4:
                addBonus(player, new SkillBonus(Skills.ATTACK, -0.02));
                addBonus(player, new SkillBonus(Skills.STRENGTH, -0.02));
                addBonus(player, new SkillBonus(Skills.DEFENCE, -0.02));
                return 0;
            case 5:
                addBonus(player, new SkillBonus(Skills.ATTACK, -0.02));
                addBonus(player, new SkillBonus(Skills.STRENGTH, -0.02));
                addBonus(player, new SkillBonus(Skills.DEFENCE, -0.02));
                break;
        }
        return 0;
    }
}
