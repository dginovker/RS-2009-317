package plugin.npc;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the elemental wizard npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ElementalWizardNPC extends AbstractNPC {

    /**
     * Represents the spells to use.
     */
    private static final int[][] SPELLS = new int[][]{ { 8, 7 }, { 4, 7 }, { 6, 7 }, { 1, 7 } };

    /**
     * Constructs a new {@code ElementalWizardNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    public ElementalWizardNPC(int id, Location location) {
        super(id, location, true);
        getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
    }

    /**
     * Constructs a new {@code ElementalWizardNPC} {@code Object}.
     */
    public ElementalWizardNPC() {
        super(0, null);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ElementalWizardNPC(id, location);
    }

    @Override
    public void onImpact(Entity entity, BattleState state) {
        if (state.getSpell() != null && isSpellType(state.getSpell())) {
            state.setEstimatedHit(0);
            state.setMaximumHit(0);
            sendChat("Gratias tibi ago");
            getSkills().heal(getSkills().getStaticLevel(Skills.HITPOINTS));
        }
        if (getAttribute("switch", false)) {
            setBaseSpell();
            removeAttribute("switch");
        }
        if (RandomUtil.random(6) > 4) {
            setSpell();
        }
        super.onImpact(entity, state);
    }

    /**
     * Sets the spell.
     */
    private void setSpell() {
        int[] spells = SPELLS[getSpellIndex()];
        getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(spells[RandomUtil.random(spells.length)]));
        setAttribute("switch", true);
    }

    /**
     * Sets the base spell type.
     */
    private void setBaseSpell() {
        getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(SPELLS[getSpellIndex()][0]));
    }

    /**
     * Checks if it is the correct spell type.
     *
     * @param spell the spell.
     * @return {@code True} if so.
     */
    private boolean isSpellType(MagicSpell spell) {
        switch (getSpellIndex()) {
            case 0: // Fire
                return spell.getClass().getSimpleName().startsWith("Fire");
            case 1: // Water
                return spell.getClass().getSimpleName().startsWith("Water");
            case 2: // Earth
                return spell.getClass().getSimpleName().startsWith("Earth");
            case 3: // Air
                return spell.getClass().getSimpleName().startsWith("Air");
        }
        return false;
    }

    /**
     * Gets the spell index.
     *
     * @return the index.
     */
    private int getSpellIndex() {
        for (int i = 0; i < getIds().length; i++) {
            if (getIds()[i] == getId()) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new MaligniusMortifer().init();
        return super.newInstance(arg);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2709, 2710, 2711, 2712 };
    }

    /**
     * Represents the malignius mortifer dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class MaligniusMortifer extends DialoguePlugin {

        /**
         * Constructs a new {@code MaligniusMortifer} {@code Object}.
         */
        public MaligniusMortifer() {
        }

        /**
         * Constructs a new {@code MaligniusMortifer} {@code Object}.
         *
         * @param player the player.
         */
        public MaligniusMortifer(final Player player) {
            super(player);
        }


        @Override
        public DialoguePlugin newInstance(Player player) {
            return new MaligniusMortifer(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.CONFUSED, "So, " + player.getUsername() + ", your curiosity leads you to speak to me?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Who are you and what are you doing here?", "Can you teach me something about magic?", "Where can I get clothes like those?", "Actually, I don't want to talk to you.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.CONFUSED, "Who are you and what are you doing here?");
                            stage = 10;
                            break;
                        case FOUR_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.CONFUSED, "Can you teach me something about magic?");
                            stage = 20;
                            break;
                        case FOUR_OPTION_THREE:
                            interpreter.sendDialogues(player, FacialExpression.CONFUSED, "Where can I get clothes like those?");
                            stage = 30;
                            break;
                        case FOUR_OPTION_FOUR:
                            player("Actually, I don't want to talk to you.");
                            stage = 40;
                            break;
                    }
                    break;
                case 10:
                    npc("I am the great Malignius Mortifer, wielder of strange", "and terrible powers. These lowly followers of mine are", "dedicated students of the magical arts. Their business is", "to follow me and learn all they can.");
                    stage = 11;
                    break;
                case 11:
                    player("There don't look very tough.");
                    stage = 12;
                    break;
                case 12:
                    npc("You may believe that, but even if you strike one down,", "another will rise up within minutes.");
                    stage = 13;
                    break;
                case 13:
                    player("Yeah, right.");
                    stage = 14;
                    break;
                case 14:
                    npc("Each of my followers is a master of his chosen element.", "His life becomes bound to that element in a way you", "could not hope to understand.");
                    stage = 15;
                    break;
                case 15:
                    interpreter.sendDialogues(player, FacialExpression.CONFUSED, "And what do you do?");
                    stage = 16;
                    break;
                case 16:
                    npc("I am mastering a branch of magic that few dare to", "attempt: Necromancy! THe fools in the Guild of", "Wizards shun anyone who practices this art, but there", "are a few across the lands who know the rudiments.");
                    stage = 17;
                    break;
                case 17:
                    npc("Grayzag and Invrigar... Even Melzar studied the", "methods of necromancy, until an accident affected his", "mind. Now his spells tend to result in...");
                    stage = 18;
                    break;
                case 18:
                    npc("... well, let us simply say that he does NOT raise", "armies of undead minions.");
                    stage = 19;
                    break;
                case 19:
                    end();
                    break;
                case 20:
                    npc("Ah, you are an inquisitive young fellow. I shall speak of", "the great Wizards' Tower, destroyed by fire many", "years ago...");
                    stage = 21;
                    break;
                case 21:
                    npc("Many say it was the greatest building in the history of", "Gielinor, a magnificent monument to human ingenuity.");
                    stage = 22;
                    break;
                case 22:
                    npc("Yet when humans are offered great power, they so", "often buy it at the cost of their principles.");
                    stage = 23;
                    break;
                case 23:
                    npc("Wizards who claimed allegiance to Saradomin began to", "insist that magic be restriced to the few they deemed", "'worthy' of such powers.");
                    stage = 24;
                    break;
                case 24:
                    npc("Before long, those who did not share their fatuous", "obsession with Saradomin were excluded from the", "Tower comletely. This state of affairs could not", "continue.");
                    stage = 25;
                    break;
                case 25:
                    end();
                    break;
                case 30:
                    npc("Bah! Our garments are an outward sign of our", "dominance of the magical arts. You cannot simply buy", "them in a shop.");
                    stage = 31;
                    break;
                case 31:
                    interpreter.sendDialogues(player, FacialExpression.CONFUSED, "What happens if I kill you and take them?");
                    stage = 32;
                    break;
                case 32:
                    interpreter.sendDialogues(npc, FacialExpression.ANGRY_YELLING, "Try it and see!");
                    stage = 33;
                    break;
                case 33:
                    player("How about if you teach me enough about magic so I", "can wear those clothes too?");
                    stage = 34;
                    break;
                case 34:
                    interpreter.sendDialogues(npc, FacialExpression.LAUGHING, "How about if I turn you into a mushroom to make you", "stop bothering me?");
                    stage = 35;
                    break;
                case 35:
                    transform();
                    break;
                case 40:
                    interpreter.sendDialogues(npc, FacialExpression.ANGRY, "Bah! Then go away!");
                    stage = 41;
                    break;
                case 41:
                    end();
                    break;
            }
            return true;
        }

        /**
         * Method used to transform the player into a mushroom.
         */
        private void transform() {
            interpreter.sendDialogues(player, null, true, "MMMmmph!");
            npc.animate(Animation.create(811));
            player.getAppearance().transformNPC(3345);
            player.graphics(Graphics.create(453));
            player.lock(8);
            player.getLocks().lockMovement(10000);
            World.submit(new Pulse(12) {

                @Override
                public boolean pulse() {
                    player.getWalkingQueue().reset();
                    player.getLocks().unlockMovement();
                    player.getAppearance().transformNPC(-1);
                    end();
                    return true;
                }
            });
        }

        @Override
        public int[] getIds() {
            return new int[]{ 2713 };
        }

    }
}
