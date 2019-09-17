package org.gielinor.game.content.skill.free.magic;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.MapDistance;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a magic spell.
 *
 * @author Emperor
 */
public abstract class MagicSpell implements Plugin<SpellType> {

    /**
     * The spell book.
     */
    protected final SpellBook book;

    /**
     * The level requirement.
     */
    protected final int level;

    /**
     * The animation.
     */
    protected final Animation animation;

    /**
     * The casting graphics.
     */
    protected final Graphics graphic;

    /**
     * The casting sound.
     */
    protected final Audio audio;

    /**
     * The item-array containing the runes required.
     */
    protected final Item[] runes;

    /**
     * The spell id.
     */
    protected int spellId;

    /**
     * The experience gained.
     */
    private final double experience;

    /**
     * Constructs a new {@code MagicSpell} {@code Object}.
     */
    public MagicSpell() {
        this(SpellBook.MODERN, 0, 0, null, null, null, new Item[0]);
    }

    /**
     * Constructs a new {@code MagicSpell} {@code Object}.
     *
     * @param book      The spell book this spell is from.
     * @param level     The level requirement.
     * @param animation the cast animation.
     * @param graphic   The cast graphic.
     * @param audio     The casting sound.
     * @param runes     The runes required to cast the spell.
     */
    public MagicSpell(SpellBook book, int level, final double experience, Animation animation, Graphics graphic, Audio audio, Item[] runes) {
        this.book = book;
        this.level = level;
        this.experience = experience;
        this.animation = animation;
        this.graphic = graphic;
        this.audio = audio;
        this.runes = runes;
    }

    /**
     * Casts a spell.
     *
     * @param p      The player casting the spell.
     * @param target The target.
     */
    public static boolean castSpell(final Player p, SpellBook book, int spellId, Node target) {
        if (p.getAttribute("magic-delay", 0) > World.getTicks()) {
            return false;
        }
        MagicSpell spell = book.getSpell(spellId);
        if (spell == null) {
            return false;
        }
        if (spell.book != book || p.getSpellBookManager().getSpellBook() != book.getInterfaceId()) {
            return false;
        }
        if (target.getLocation() != null) {
            if (!target.getLocation().withinDistance(p.getLocation(), 15)) {
                return false;
            }
            p.faceLocation(target.getLocation());
        }
        boolean combatSpell = spell instanceof CombatSpell;
        if (!combatSpell && target instanceof Entity) {
            p.faceTemporary((Entity) target, 1);
        }
        if (spell.cast(p, target)) {
            if (book != SpellBook.LUNAR && p.getAttribute("spell:swap", 0) != 0) {
                p.removeAttribute("spell:swap");
                p.getSpellBookManager().setSpellBook(SpellBook.LUNAR);
                p.getInterfaceState().openTab(Sidebar.MAGIC_TAB.ordinal(), new Component(SpellBook.LUNAR.getInterfaceId()));
            }
            if (!combatSpell) {
                p.getSkills().addExperience(Skills.MAGIC, spell.getExperience());
            }
            p.setAttribute("magic-delay", World.getTicks() + 3);
            return true;
        }
        return false;
    }

    /**
     * Starts the effect of this spell (if any).
     *
     * @param entity The entity.
     * @param target The victim.
     */
    public abstract boolean cast(Entity entity, Node target);

    /**
     * Visualizes the spell.
     *
     * @param entity The entity.
     * @param target The target.
     */
    public void visualize(Entity entity, Node target) {
        entity.graphics(graphic);
        entity.animate(animation);
        sendSound(entity);
    }

    /**
     * Checks if the player is holding the staff that will be used instead of the rune.
     *
     * @param p    The player.
     * @param rune The rune item id.
     * @return <code>True</code> if the player is wearing the correct staff for the rune.
     */
    public boolean usingStaff(Player p, int rune) {
        Item weapon = p.getEquipment().get(3);
        if (weapon == null) {
            return false;
        }
        MagicStaff staff = MagicStaff.forId(rune);
        if (staff == null) {
            return false;
        }
        int[] staves = staff.getStaves();
        for (int id : staves) {
            if (weapon.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the casting entity meets the requirements to cast this spell.
     *
     * @param caster  The caster.
     * @param message If a message should be send to the caster if it doesn't meet the requirements.
     * @param remove  If we should remove the runes from the player's inventory.
     * @return <code>True</code> if so.
     */
    public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
        if (caster.getSkills().getLevel(Skills.MAGIC) < levelRequirement()) {
            if (message && caster instanceof Player) {

                final Player castingPlayer = (Player) caster;

                if(castingPlayer.getAttribute("reverse_gun_game", false))
                    return true;
                else
                    castingPlayer.getActionSender().sendMessage("You need a Magic level of " + levelRequirement() + " to cast this spell.");
            }
            return false;
        }

        if (caster instanceof Player) {
            Player p = (Player) caster;
            if (runes == null) {
                return true;
            }
            List<Item> toRemove = new ArrayList<>();
            for (Item item : runes) {
                if (!usingStaff(p, item.getId())) {
                    if (!Runes.hasRunes(p, item)) {
                        if (message) {
                            p.getActionSender().sendMessage("You don't have enough " + item.getName().toLowerCase() + "s to cast this spell.");
                        }
                        return false;
                    }
                    toRemove.add(item);
                }
            }
            if (remove) {
                Runes.removeRunes(p, toRemove.toArray(new Item[toRemove.size()]));
            }
            return true;
        }
        return true;
    }

    /**
     * Adds the experience for casting this spell.
     *
     * @param entity The entity to reward with experience.
     * @param hit    The hit.
     */
    public void addExperience(Entity entity, int hit) {
        if (!(entity instanceof Player)) {
            return;
        }
        if (hit < 1) {
            hit = 0;
        }
        if (hit > 1 && entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_DEFENSIVE_CAST) {
            entity.getSkills().addExperience(Skills.DEFENCE, 1 * hit);
            entity.getSkills().addExperience(Skills.MAGIC, 1.33 * hit);
            return;
        }
        entity.getSkills().addExperience(Skills.MAGIC, hit * (CombatSwingHandler.EXPERIENCE_MOD / 2));
    }

    /**
     * Gets the level requirement to cast this spell.
     *
     * @return The level requirement.
     */
    public int levelRequirement() {
        return level;
    }

    /**
     * Sends the sound packet for all players to hear (in a distance specified by {@link MapDistance#SOUND#getDistance()}).
     *
     * @param entity The entity from where this sound comes from.
     */
    public void sendSound(Entity entity) {
        sendSound(entity, audio);
    }

    /**
     * Sends the sound packet for all players to hear (in a distance specified by {@link MapDistance#SOUND#getDistance()}).
     *
     * @param entity The entity from where this sound comes from.
     * @param audio  The sound to send.
     */
    public void sendSound(Entity entity, Audio audio) {
        if (audio == null || audio.getId() < 0) {
            return;
        }
        for (Player p : RegionManager.getLocalPlayers(entity, MapDistance.SOUND.getDistance())) {
            p.getActionSender().sendSound(audio);
        }
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Gets the spell book for this spell.
     *
     * @return The spell book.
     */
    public SpellBook getBook() {
        return book;
    }

    /**
     * Gets the item array of runes required to cast this spell.
     *
     * @return The item array of runes, or {@code null} if the spell doesn't need any runes.
     */
    public Item[] getCastRunes() {
        return runes;
    }

    /**
     * Gets the spellId.
     *
     * @return The spellId.
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * Sets the spellId.
     *
     * @param spellId The spellId to set.
     */
    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    /**
     * Gets the experience.
     *
     * @return The experience.
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Gets the level requirement.
     *
     * @return The level requirement.
     */
    public int getLevel() {
        return level;
    }
}
