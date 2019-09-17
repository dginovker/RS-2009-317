package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The plugin used to load the minotaur familiar npcs.
 *
 * @author Vexia
 * @version 1.0
 */
public final class MinotaurFamiliarNPC implements Plugin<Object> {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        PluginManager.definePlugin(new BronzeMinotaurNPC());
        PluginManager.definePlugin(new IronMinotaurNPC());
        PluginManager.definePlugin(new SteelMinotaurNPC());
        PluginManager.definePlugin(new MithrilMinotaurNPC());
        PluginManager.definePlugin(new AdamantMinotaurNPC());
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Handles the bullrush special.
     *
     * @param familiar the familiar.
     * @param special  the special.
     * @param maxHit   the max hit.
     */
    public boolean bullRush(final Familiar familiar, final FamiliarSpecial special, final int maxHit) {
        final Entity target = (Entity) special.getNode();
        if (!familiar.canCombatSpecial(target)) {
            return false;
        }
        familiar.sendFamiliarHit(target, RandomUtil.random(maxHit));
        Projectile.magic(familiar, target, 1497, 80, 36, 70, 10).send();
        familiar.visualize(Animation.create(8026), Graphics.create(1496));
        if (!(familiar instanceof BronzeMinotaurNPC && familiar instanceof RuneMinotaurNPC) && RandomUtil.random(10) < 6) {
            final int ticks = 2 + (int) Math.floor(familiar.getLocation().getDistance(target.getLocation()) * 0.5);
            World.submit(new Pulse(ticks) {

                @Override
                public boolean pulse() {
                    target.getStateManager().set(EntityState.STUNNED, 4);
                    return true;
                }
            });
        }
        return true;
    }

    /**
     * Represents the Bronze Minotaur familiar.
     *
     * @author Aero
     */
    public class BronzeMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code BronzeMinotaurNPC} {@code Object}.
         */
        public BronzeMinotaurNPC() {
            this(null, 6853);
        }

        /**
         * Constructs a new {@code BronzeMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public BronzeMinotaurNPC(Player owner, int id) {
            super(owner, id, 3000, 12073, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new BronzeMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 4);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 6853, 6854 };
        }

    }

    /**
     * Represents the Iron Minotaur familiar.
     *
     * @author Aero
     */
    public class IronMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code IronMinotaurNPC} {@code Object}.
         */
        public IronMinotaurNPC() {
            this(null, 6855);
        }

        /**
         * Constructs a new {@code IronMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public IronMinotaurNPC(Player owner, int id) {
            super(owner, id, 3700, 12075, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new IronMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 6);
        }


        @Override
        public boolean isPoisonImmune() {
            return true;
        }


        @Override
        public int[] getIds() {
            return new int[]{ 6855, 6856 };
        }

    }

    /**
     * Represents the Steel Minotaur familiar.
     *
     * @author Aero
     */
    public class SteelMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code SteelMinotaurNPC} {@code Object}.
         */
        public SteelMinotaurNPC() {
            this(null, 6857);
        }

        /**
         * Constructs a new {@code SteelMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public SteelMinotaurNPC(Player owner, int id) {
            super(owner, id, 4600, 12077, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new SteelMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 9);
        }


        @Override
        public boolean isPoisonImmune() {
            return true;
        }


        @Override
        public int[] getIds() {
            return new int[]{ 6857, 6858 };
        }

    }

    /**
     * Represents the Mithril Minotaur familiar.
     *
     * @author Aero
     */
    public class MithrilMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code MithrilMinotaurNPC} {@code Object}.
         */
        public MithrilMinotaurNPC() {
            this(null, 6859);
        }

        /**
         * Constructs a new {@code MithrilMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public MithrilMinotaurNPC(Player owner, int id) {
            super(owner, id, 5500, 12079, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new MithrilMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 13);
        }


        @Override
        public boolean isPoisonImmune() {
            return true;
        }


        @Override
        public int[] getIds() {
            return new int[]{ 6859, 6860 };
        }

    }

    /**
     * Represents the Adamant Minotaur familiar.
     *
     * @author Aero
     */
    public class AdamantMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code AdamantMinotaurNPC} {@code Object}.
         */
        public AdamantMinotaurNPC() {
            this(null, 6861);
        }

        /**
         * Constructs a new {@code AdamantMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public AdamantMinotaurNPC(Player owner, int id) {
            super(owner, id, 6600, 12081, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new AdamantMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 16);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 6861, 6862 };
        }

    }

    /**
     * Represents the Rune Minotaur familiar.
     *
     * @author Aero
     */
    public class RuneMinotaurNPC extends Familiar {

        /**
         * Constructs a new {@code RuneMinotaurNPC} {@code Object}.
         */
        public RuneMinotaurNPC() {
            this(null, 6863);
        }

        /**
         * Constructs a new {@code RuneMinotaurNPC} {@code Object}.
         *
         * @param owner The owner.
         * @param id    The id.
         */
        public RuneMinotaurNPC(Player owner, int id) {
            super(owner, id, 15100, 12083, 6);
        }

        @Override
        public Familiar construct(Player owner, int id) {
            return new RuneMinotaurNPC(owner, id);
        }

        @Override
        protected boolean specialMove(FamiliarSpecial special) {
            return bullRush(this, special, 110);
        }

        @Override
        public boolean isPoisonImmune() {
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 6863, 6864 };
        }

    }
}
