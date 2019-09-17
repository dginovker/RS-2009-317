package plugin.consumable;

import org.gielinor.content.donators.DonatorConfigurations;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Potion;
import org.gielinor.game.content.global.consumable.PotionEffect;
import org.gielinor.game.content.global.consumable.PotionEffect.Effect;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import java.util.concurrent.TimeUnit;

/**
 * Represents the class of all potion plugins.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class PotionPlugin implements Plugin<Object> {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new AntipoisonPotion().newInstance(arg);
        new CombatPotion().newInstance(arg);
        new EnergyPotion().newInstance(arg);
        new FishingPotion().newInstance(arg);
        new PrayerPotion().newInstance(arg);
        new PrayerMix().newInstance(arg);
        new AttackMix().newInstance(arg);
        new RelicymsBalm().newInstance(arg);
        new RestorePotion().newInstance(arg);
        new SaradominBrew().newInstance(arg);
        new SummoningPotion().newInstance(arg);
        new SuperantiPoison().newInstance(arg);
        new SuperenergyPotion().newInstance(arg);
        new SuperestorePlugin().newInstance(arg);
        new ZamorakBrew().newInstance(arg);
        new AntifirePotion().newInstance(arg);
        new StaminaPotion().newInstance(arg);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Represents the plugin used for the anti poison potions.
     *
     * @author 'Vexia
     * @version 1.1
     */
    public final class AntipoisonPotion extends Potion {

        /**
         * Constructs a new {@code AntipoisonPlugin} {@code Object}.
         */
        public AntipoisonPotion() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new AntipoisonPotion(PotionEffect.ANTI_POISON));
            Consumables.add(new AntipoisonPotion(PotionEffect.STRONG_ANTI_POISON));
            Consumables.add(new AntipoisonPotion(PotionEffect.SUPER_STRONG_ANTI_POISON));
            return this;
        }

        /**
         * Constructs a new {@code AntipoisonPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public AntipoisonPotion(PotionEffect effect) {
            super(effect);
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.setAttribute("poison:immunity", World.getTicks() + getTicks(item));
            player.getStateManager().remove(EntityState.POISONED);
        }

        /**
         * Gets the amount of ticks for the potion.
         *
         * @param item the item.
         * @return the ticks.
         */
        private int getTicks(Item item) {
            if (!item.getName().contains("+")) {
                return 143;
            }
            if (!item.getName().contains("++")) {
                return 863;
            }
            return 2000;
        }

    }

    /**
     * Represents the potion used to raise attack and strength by 8%.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class CombatPotion extends Potion {

        /**
         * Constructs a new {@code CombatPotionPlugin} {@code Object}.
         */
        public CombatPotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new CombatPotion(new PotionEffect.Effect("Combat potion", new int[]{ 9739, 9741, 9743, 9745 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code CombatPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public CombatPotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            effect(player, new SkillBonus(Skills.ATTACK, 0.15));
            effect(player, new SkillBonus(Skills.STRENGTH, 0.15));
        }
    }

    /**
     * Represents the energy potion plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class EnergyPotion extends Potion {

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         */
        public EnergyPotion() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new EnergyPotion(new PotionEffect.Effect("Energy potion", new int[]{ 3008, 3010, 3012, 3014 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public EnergyPotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void consume(final Item item, final Player player) {
            player.getSkills().heal(3);
            player.getSettings().updateRunEnergy(-(100 * 0.20));
            super.consume(item, player);
        }

    }
    /**
     * Represents the fishing potion plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public class FishingPotion extends Potion {

        /**
         * Constructs a new {@code FishingPotionPlugin} {@code Object}.
         */
        public FishingPotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(new FishingPotion(new PotionEffect.Effect("Fishing potion", new int[]{ 2438, 151, 153, 155 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code FishingPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public FishingPotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getSkills().updateLevel(Skills.FISHING, 3, player.getSkills().getStaticLevel(Skills.FISHING) + 3);
        }

    }

    /**
     * Represents the attack mix potion.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public class AttackMix extends Potion {

        /**
         * Constructs a new {@code AttackMix} {@code Object}.
         */
        public AttackMix() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(new AttackMix(new PotionEffect.Effect("Attack mix", new int[]{ 11429, 11431 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code AttackMix} {@code Object}.
         *
         * @param effect the effect.
         */
        public AttackMix(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            effect(player, new SkillBonus(Skills.ATTACK, 0.12, 1));
            player.getSkills().heal(3);
        }
    }

    /**
     * Represents the prayer mix potion.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public class PrayerMix extends Potion {

        /**
         * Constructs a new {@code PrayerMix} {@code Object}.
         */
        public PrayerMix() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(new PrayerMix(new PotionEffect.Effect("Prayer mix", new int[]{ 11465, 11467 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code PrayerMix} {@code Object}.
         *
         * @param effect the effect.
         */
        public PrayerMix(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            int level = player.getSkills().getStaticLevel(Skills.PRAYER);
            int amt = 7 + (level / 4);
            player.getSkills().incrementPrayerPoints(amt * DonatorConfigurations.getHealingMultiplier(player));
            player.getSkills().heal(3);
        }
    }


    /**
     * Represents the prayer plugin.
     *
     * @author 'Vexia
     */
    public class PrayerPotion extends Potion {

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         */
        public PrayerPotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(new PrayerPotion(new PotionEffect.Effect("Prayer potion", new int[]{ 2434, 139, 141, 143 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public PrayerPotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            int level = player.getSkills().getStaticLevel(Skills.PRAYER);
            int amt = 7 + (level / 4);
            player.getSkills().incrementPrayerPoints(amt * DonatorConfigurations.getHealingMultiplier(player));
        }

    }

    /**
     * Represents the anti-poison plugin.
     *
     * @author 'Vexia
     * @date 23/12/2013
     */
    public class RelicymsBalm extends Potion {

        /**
         * Constructs a new {@code RelicymsBalmPlugin} {@code Object}.
         */
        public RelicymsBalm() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            Consumables.add(new RelicymsBalm(PotionEffect.RELICYMS_BALM));
            return this;
        }

        /**
         * Constructs a new {@code RelicymsBalmPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public RelicymsBalm(PotionEffect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getStateManager().remove(EntityState.DISEASED);
            player.setAttribute("disease:immunity", World.getTicks() + 300);
        }

    }

    /**
     * Represents the energy potion plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class RestorePotion extends Potion {

        /**
         * Represents the array of skills to restore.
         */
        private static final int[] SKILLS = new int[]{ Skills.DEFENCE, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE };

        /**
         * Constructs a new {@code RestorePotionPlugin} {@code Object}.
         */
        public RestorePotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new RestorePotion(new PotionEffect.Effect("Restore potion", new int[]{ 2430, 127, 129, 131 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code RestorePotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public RestorePotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            for (int skill : SKILLS) {
                int statLevel = player.getSkills().getStaticLevel(skill);
                if (player.getSkills().getLevel(skill) < statLevel) {
                    int boost = (int) (10 + (statLevel * 0.3));
                    player.getSkills().updateLevel(skill, boost, statLevel);
                }
            }
        }

    }

    /**
     * Represents the saradomin brew.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class SaradominBrew extends Potion {

        /**
         * Represents the skill bonuses.
         */
        private static final SkillBonus[] BONUSES = new SkillBonus[]{ new SkillBonus(Skills.HITPOINTS, 0.15), new SkillBonus(Skills.ATTACK, -0.10), new SkillBonus(Skills.STRENGTH, -0.10), new SkillBonus(Skills.MAGIC, -0.10), new SkillBonus(Skills.RANGE, -0.10) };

        /**
         * Constructs a new {@code SaradominBrewPlugin} {@code Object}.
         */
        public SaradominBrew() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new SaradominBrew(new PotionEffect.Effect("Saradomin brew", new int[]{ 6685, 6687, 6689, 6691 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code SaradominBrewPlugin} {@code Object}.
         */
        public SaradominBrew(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void consume(final Item item, final Player player) {
            for (SkillBonus b : BONUSES) {
                addBonus(player, b);
            }
            player.getSkills().updateLevel(Skills.DEFENCE, (int) ((player.getSkills().getStaticLevel(Skills.DEFENCE) * 0.20) + 2));
            super.consume(item, player);
        }

    }

    /**
     * Represents the summoning potion.
     *
     * @author 'Vexia
     * @date 23/12/2013
     */
    public final class SummoningPotion extends Potion {

        /**
         * Constructs a new {@code SummoningPotionPlugin} {@code Object}.
         */
        public SummoningPotion() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new SummoningPotion(new PotionEffect.Effect("Summoning potion", new int[]{ 12140, 12142, 12144, 12146 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public SummoningPotion(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getSkills().updateLevel(Skills.SUMMONING, (int) (7 + player.getSkills().getLevel(Skills.SUMMONING) * 0.25), player.getSkills().getStaticLevel(Skills.SUMMONING));
        }

    }

    /**
     * Represents the super anti-poison plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class SuperantiPoison extends Potion {

        /**
         * Constructs a new {@code SuperantiPoisonPlugin} {@code Object}.
         */
        public SuperantiPoison() {
            /**
             * empty.
             */
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new SuperantiPoison(PotionEffect.SUPER_ANTIPOISON));
            return this;
        }

        /**
         * Constructs a new {@code AntipoisonPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public SuperantiPoison(PotionEffect effect) {
            super(effect);
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getStateManager().remove(EntityState.POISONED);
            player.setAttribute("poison:immunity", World.getTicks() + 386);
        }

    }

    /**
     * Represents the energy potion plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class SuperenergyPotion extends Potion {

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         */
        public SuperenergyPotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new SuperenergyPotion(new PotionEffect.Effect("Super energy", new int[]{ 3016, 3018, 3020, 3022 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code SuperenergyPotion} {@code Object}.
         *
         * @param effect the effect.
         */
        public SuperenergyPotion(PotionEffect.Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getSettings().updateRunEnergy(-20);
        }

    }

    /**
     * Represents the energy potion plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class SuperestorePlugin extends Potion {

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         */
        public SuperestorePlugin() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new SuperestorePlugin(new PotionEffect.Effect("Super restore", new int[]{ 3024, 3026, 3028, 3030 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code PrayerPotionPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public SuperestorePlugin(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            for (int skill = 0; skill < Skills.SKILL_NAME.length; skill++) {
                if (skill == Skills.HITPOINTS) {
                    continue;
                }
                int statLevel = player.getSkills().getStaticLevel(skill);
                if (player.getSkills().getLevel(skill) < statLevel) {
                    int boost = (int) (statLevel * 0.25) + 8;
                    player.getSkills().updateLevel(skill, boost, statLevel);
                }
            }
            int prayerLvl = player.getSkills().getStaticLevel(Skills.PRAYER);
            player.getSkills().incrementPrayerPoints((prayerLvl * 0.25) + 8);
        }
    }


    /**
     * Represents the zamorak brew plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class ZamorakBrew extends Potion {

        /**
         * Reprsents the skill bonuses.
         */
        private static final SkillBonus[] BONUSES = new SkillBonus[]{ new SkillBonus(Skills.ATTACK, 0.25), new SkillBonus(Skills.STRENGTH, 0.15), new SkillBonus(Skills.DEFENCE, -0.10) };

        /**
         * Constructs a new {@code ZamorakBrewPlugin} {@code Object}.
         */
        public ZamorakBrew() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new ZamorakBrew(new PotionEffect.Effect("Zamorak brew", new int[]{ 2450, 189, 191, 193 }, null)));
            Consumables.add(new ZamorakBrew(new PotionEffect.Effect("Zamorak mix", new int[]{ 11521, 11523 }, null)));
            return this;
        }

        /**
         * Constructs a new {@code ZamorakBrewPlugin} {@code Object}.
         *
         * @param effect the effect.
         */
        public ZamorakBrew(Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void consume(final Item item, final Player player) {
            for (SkillBonus b : BONUSES) {
                addBonus(player, b);
            }
            player.getImpactHandler().manualHit(player, (int) (player.getSkills().getLifepoints() * 0.10), HitsplatType.NORMAL);
            super.consume(item, player);
        }

    }

    /**
     * Represents an antifire potion.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class AntifirePotion extends Potion {

        /**
         * Represents the time to resist fire effects.
         */
        private long timeResistant;

        /**
         * Constructs a new {@code AntifirePotion} {@code Object}.
         */
        public AntifirePotion() {
        }

        /**
         * Constructs a new {@code AntifirePotion} {@code Object}.
         *
         * @param effect        the effect.
         * @param timeResistant the time of resistance.
         */
        public AntifirePotion(final PotionEffect.Effect effect, long timeResistant) {
            super(effect);
            this.timeResistant = timeResistant;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new AntifirePotion(new PotionEffect.Effect("Antifire potion", new int[]{ 2452, 2454, 2456, 2458 }, null),
                TimeUnit.MINUTES.toMillis(6)));
            return this;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getStateManager().register(EntityState.FIRE_RESISTANT, true);
        }

    }

    /**
     * Represents the Stamina potion.
     *
     * @author <a href="http://Gielinor.org/">Gielinor</a>
     */
    public final class StaminaPotion extends Potion {

        /**
         * Constructs a new <code>StaminaPotion</code>.
         */
        public StaminaPotion() {
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            Consumables.add(new StaminaPotion(new PotionEffect.Effect("Stamina potion", new int[]{  32625, 32627, 32629, 32631  }, null)));
            return this;
        }

        /**
         * Constructs a new <code>StaminaPotion</code>.
         *
         * @param effect the effect.
         */
        public StaminaPotion(PotionEffect.Effect effect) {
            super(effect);
            super.emptyItem = VIAL;
        }

        @Override
        public void effect(final Player player, final Item item) {
            player.getSettings().updateRunEnergy(-20);
            player.getInterfaceState().force(InterfaceConfiguration.STAMINA_POTION, true, false);
            Pulse pulse = player.getAttribute("stamina-potion");
            if (pulse != null) {
                pulse.stop();
            }
            World.submit(pulse = new Pulse(1, player) {

                int count = 200;

                @Override
                public boolean pulse() {
                    count--;
                    if (count == 25) {
                        player.getActionSender().sendMessage("<col=8F4808>Your stamina potion is about to expire.");
                    }
                    if (count == 0) {
                        player.getActionSender().sendMessage("<col=8F4808>Your stamina potion has expired.");
                        player.getInterfaceState().force(InterfaceConfiguration.STAMINA_POTION, false, false);
                        player.removeAttribute("stamina-time");
                        return true;
                    }
                    player.saveAttribute("stamina-time", count);
                    return false;
                }
            });
            player.setAttribute("stamina-potion", pulse);
        }
    }

}
