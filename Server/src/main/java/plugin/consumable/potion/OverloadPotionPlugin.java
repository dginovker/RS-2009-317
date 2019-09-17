package plugin.consumable.potion;

import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Potion;
import org.gielinor.game.content.global.consumable.PotionEffect;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the {@code OverloadPotionPlugin} {@link Potion} {@link Plugin}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class OverloadPotionPlugin extends Potion {

    /**
     * Constructs a new {@code OverloadPotionPlugin} {@link Potion} {@link Plugin}.
     */
    public OverloadPotionPlugin() {
        /**
         * empty.
         */
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Consumables.add(new OverloadPotionPlugin(new PotionEffect.Effect("Overload potion", new int[]{ 11939, 11940, 11941, 11942 }, null)));
        return this;
    }

    /**
     * Constructs a new {@code OverloadPotionPlugin} {@code Object}.
     *
     * @param effect the effect.
     */
    public OverloadPotionPlugin(PotionEffect.Effect effect) {
        super(effect);
        super.emptyItem = VIAL;
    }

    @Override
    public void message(final Player player, final Item item, final int initial, String... messages) {
        if (player.getSkills().getLifepoints() < 51) {
            return;
        }
        super.message(player, item, initial, messages);
    }

    @Override
    public void remove(final Player player, final Item item, final int doses, boolean message) {
        if (player.getSkills().getLifepoints() < 51) {
            return;
        }
        super.remove(player, item, doses, true);
    }

    @Override
    public void effect(final Player player, final Item item) {
        if (player.getSkills().getLifepoints() < 51) {
            player.getActionSender().sendMessage("You need at least 51 hitpoints to use this.");
            return;
        }
        // TODO "You suffered recently"
        effect(player, new SkillBonus(Skills.ATTACK, 0.22, 5));
        effect(player, new SkillBonus(Skills.STRENGTH, 0.22, 5));
        effect(player, new SkillBonus(Skills.DEFENCE, 0.22, 5));
        effect(player, new SkillBonus(Skills.RANGE, 0.22, 2));
        effect(player, new SkillBonus(Skills.MAGIC, 0.0, 7));
        Pulse pulse = player.getAttribute("overload");
        if (pulse != null) {
            pulse.stop();
        }
        World.submit(pulse = new Pulse(2, player) {

            int count = 0;

            @Override
            public boolean pulse() {
                if (++count < 6) {
                    player.animate(Animation.create(3170));
                    player.getImpactHandler().manualHit(player, 10, ImpactHandler.HitsplatType.NORMAL);
                } else if (count == 6) {
                    setTicksPassed(10);
                    setDelay(25);
                } else if (count == 26) {
                    player.getActionSender().sendMessage("<col=FF0000>The effects of overload have worn off, and you feel normal again.");
                    player.getSkills().heal(50);
                    for (int i = 0; i < 7; i++) {
                        if (i == 5 || i == 3) {
                            continue;
                        }
                        if (player.getSkills().getLevel(i) > player.getSkills().getStaticLevel(i)) {
                            player.getSkills().setLevel(i, player.getSkills().getStaticLevel(i));
                        }
                    }
                    return true;
                } else {
                    effect(player, new SkillBonus(Skills.ATTACK, 0.22, 5));
                    effect(player, new SkillBonus(Skills.STRENGTH, 0.22, 5));
                    effect(player, new SkillBonus(Skills.DEFENCE, 0.22, 5));
                    effect(player, new SkillBonus(Skills.RANGE, 0.22, 2));
                    effect(player, new SkillBonus(Skills.MAGIC, 0.0, 7));
                }
                return false;
            }
        });
        player.setAttribute("overload", pulse);
        //remove(player, item);
    }
}
