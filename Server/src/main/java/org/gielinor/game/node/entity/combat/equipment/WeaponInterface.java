package org.gielinor.game.node.entity.combat.equipment;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.context.SidebarInterfaceContext;
import org.gielinor.net.packet.context.StringContext;
import org.gielinor.net.packet.out.SidebarInterface;
import org.gielinor.net.packet.out.StringPacket;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * Represents the weapon interface component.
 *
 * @author Emperor
 */
public final class WeaponInterface extends Component {

    /**
     * The slayer staff spell ids.
     */
    private static final int[] SLAYER_SPELL_IDS = {
        12052, 12051, 12053, 12054,
        12055, 12056,
    };

    /**
     * The default attack animations.
     */
    public static final Animation[] DEFAULT_ANIMS = { new Animation(422, Priority.HIGH), new Animation(423, Priority.HIGH), new Animation(422, Priority.HIGH), new Animation(422, Priority.HIGH) };

    /**
     * The stab equipment bonus index.
     */
    public static final int BONUS_STAB = 0;

    /**
     * The slash equipment bonus index.
     */
    public static final int BONUS_SLASH = 1;

    /**
     * The crush equipment bonus index.
     */
    public static final int BONUS_CRUSH = 2;

    /**
     * The magic equipment bonus index.
     */
    public static final int BONUS_MAGIC = 3;

    /**
     * The range equipment bonus index.
     */
    public static final int BONUS_RANGE = 4;

    /**
     * The accurate melee attack style
     */
    public static final int STYLE_ACCURATE = 0;

    /**
     * The aggressive attack style
     */
    public static final int STYLE_AGGRESSIVE = 1;

    /**
     * The controlled attack style
     */
    public static final int STYLE_CONTROLLED = 2;

    /**
     * The defensive attack style
     */
    public static final int STYLE_DEFENSIVE = 3;

    /**
     * The accurate range attack style
     */
    public static final int STYLE_RANGE_ACCURATE = 4;

    /**
     * The rapid range attack style
     */
    public static final int STYLE_RAPID = 5;

    /**
     * The long range attack style
     */
    public static final int STYLE_LONG_RANGE = 6;

    /**
     * The defensive spell cast attack style
     */
    public static final int STYLE_DEFENSIVE_CAST = 7;

    /**
     * The spell cast attack style
     */
    public static final int STYLE_CAST = 8;

    /**
     * The player.
     */
    private final Player player;

    /**
     * The current weapon interface.
     */
    private WeaponInterfaces current;

    /**
     * If the player has the special attack bar shown.
     */
    private boolean specialBar;

    /**
     * The player's attack animations.
     */
    private Animation[] attackAnimations;

    /**
     * Constructs a new {@code WeaponInterface} {@code Object}.
     *
     * @param player The player.
     */
    public WeaponInterface(Player player) {
        super(WeaponInterfaces.UNARMED.interfaceId);
        this.player = player;
        player.addExtension(WeaponInterface.class, this);
    }

    @Override
    public void open(Player player) {
        current = null;
        updateInterface();
    }

    /**
     * Opens the interface.
     */
    private void open() {
        ComponentDefinition definition = ComponentDefinition.forId(WeaponInterfaces.UNARMED.interfaceId);
        // TODO 317
        // if (definition.getSidebarContext() != null) {
        PacketRepository.send(SidebarInterface.class, new SidebarInterfaceContext(player, id, 0));
        // }
        int slot = ensureStyleIndex(player, player.getSettings().getAttackStyleIndex());
        if (slot != player.getSettings().getAttackStyleIndex()) {
            player.getSettings().toggleAttackStyleIndex(slot);
        }
        player.getProperties().setAttackStyle(current.getAttackStyles()[slot]);
        checkStaffConfigs(slot);
    }

    /**
     * Ensures the style index.
     *
     * @param player The player.
     * @param slot   The attack style index.
     * @return The index, ensured to be smaller than styles.length and larger than 0.
     */
    private int ensureStyleIndex(Player player, int slot) {
        AttackStyle style = player.getProperties().getAttackStyle();
        if (slot >= current.getAttackStyles().length) {
            slot = current.getAttackStyles().length - 1;
            if (style != null) {
                for (int i = slot; i >= 0; i--) {
                    if (current.getAttackStyles()[i].style == style.style) {
                        return i;
                    }
                }
            }
            return slot;
        }
        if (style != null && current.getAttackStyles()[slot].style != style.style) {
            for (int i = current.getAttackStyles().length - 1; i >= 0; i--) {
                if (current.getAttackStyles()[i].style == style.style) {
                    return i;
                }
            }
        }
        return slot;
    }

    /**
     * Updates the interface.
     */
    public void updateInterface() {
        player.getInterfaceState().getTabs()[0] = this;
        Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
        WeaponInterfaces inter = getWeaponInterface(weapon);
        String name;
        if (weapon != null) {
            name = weapon.getDefinition().getName();
            specialBar = weapon.getDefinition().getConfiguration(ItemConfiguration.HAS_SPECIAL, false);
            attackAnimations = weapon.getDefinition().getConfiguration(ItemConfiguration.ATTACK_ANIMS, DEFAULT_ANIMS);
        } else {
            name = "Unarmed";
            specialBar = false;
            attackAnimations = DEFAULT_ANIMS;
        }
        if (inter != current) {
            id = inter.interfaceId;
            current = inter;
            open();
            player.getProperties().getCombatPulse().updateStyle();
        }
        if (player.getSettings().getAttackStyleIndex() < attackAnimations.length && !player.getAppearance().isNpc()) {
            player.getProperties().setAttackAnimation(attackAnimations[player.getSettings().getAttackStyleIndex()]);
        }
        if (current != WeaponInterfaces.STAFF) {
            selectAutoSpell(-1, false);
            int[] children = new int[]{ 12323, 7474, 7674, 7574, 7599, 7549, 8493, 7499, 7624, 7699, 7800 };
            for (int child : children) {
                player.getActionSender().sendHideComponent(child, !isSpecialBar());
            }
        }
        if (!canAutocast(false)) {
            if (current == WeaponInterfaces.STAFF && player.getAttribute("autocast_select", false)) {
                open();
            }
            selectAutoSpell(-1, true);
        }
        if (inter.getTextId() != -1) {
            PacketRepository.send(StringPacket.class, new StringContext(player, name, inter.getTextId()));
        }
        if (inter.getModelId() != -1) {
            // player.getActionSender().sendInterfaceModel(12291, 200, id);
            //PacketRepository.send(StringPacket.class, new StringContext(player, name, inter.getModelId()));
        }
        if (player.getSettings().isSpecialToggled()) {
            player.getSettings().toggleSpecialBar();
        }
    }

    /**
     * Sets the current attack style.
     *
     * @param button The button the player has pressed.
     * @return <code>True</code> if the attack style got set.
     */
    public boolean setAttackStyle(int button) {
        int slot = button - 1;
        for (int i = 0; i < current.getSlotIds().length; i++) {
            if (current.getSlotIds()[i] == button) {
                slot = i + 1;
            }
        }
        if (current != WeaponInterfaces.STAFF) {
            slot--;
        }
        if (current == WeaponInterfaces.WARHAMMER_MAUL ||
            (current.attackStyles.length > 2 && current.attackStyles[2].bonusType ==
                BONUS_RANGE && current.getInterfaceId() != 1749)) {
            slot = button == 432 ? 1 : button == 431 ? 2 : 0;
        } else if (current == WeaponInterfaces.CLAWS) {
            slot = button == 7771 ? 1 : button == 7769 ? 3 : slot;
        }
        switch (current) {
            case AXE:
                slot = button == 1707 ? 1 : button == 1705 ? 3 : slot;
                break;
            case STAFF:
                slot = button == 24111 ? 3 : button == 349 ? 4 : slot;
                break;
            case BOW:
            case CROSSBOW:
                slot = button == 1771 ? 1 : button == 1770 ? 2 : 0;
                break;
        }
        if (slot < 0 || slot >= current.getAttackStyles().length) {
            return false;
        }
        AttackStyle style = current.getAttackStyles()[slot];
        player.getProperties().setAttackStyle(style);
        player.getSettings().toggleAttackStyleIndex(slot);
        if (slot < attackAnimations.length && !player.getAppearance().isNpc()) {
            player.getProperties().setAttackAnimation(attackAnimations[slot]);
        }
        checkStaffConfigs(slot);
        return true;
    }

    /**
     * Checks the staff configurations.
     *
     * @param slot The slot of the current attack style selected.
     */
    private void checkStaffConfigs(int slot) {
        if (current != WeaponInterfaces.STAFF) {
            selectAutoSpell(-1, false);
            return;
        }
        boolean defensive = slot == 3;
        player.getConfigManager().set(108, defensive ? 2 : 1);
        if (slot > 2) {
            player.getConfigManager().set(43, defensive ? -1 : 3);
        }
    }

    /**
     * Selects an autocast spell.
     *
     * @param buttonId          The button id.
     * @param adjustAttackStyle If the attack style should be adjusted.
     */
    public void selectAutoSpell(int buttonId, boolean adjustAttackStyle) {
        if (buttonId < 0) {
            player.getProperties().setAutocastSpell(null);
            if (adjustAttackStyle && current != null) {
                setAttackStyle(3);
                player.getProperties().getCombatPulse().updateStyle();
            }
            return;
        }
        boolean modern = player.getSpellBookManager().getSpellBook() == SpellBook.MODERN.getInterfaceId();
        int type = (modern ? (player.getEquipment().contains(4170) ? 2 : 0) : 1);
        AutocastSpell autocastSpell = null;
        if (modern && player.getEquipment().contains(4170)) {
            autocastSpell = AutocastSpell.forSlayer(buttonId);
        }
        if (autocastSpell == null) {
            autocastSpell = modern ? AutocastSpell.forModern(buttonId) : AutocastSpell.forAncients(buttonId);
        }
        if (autocastSpell == null) {
            selectAutoSpell(-1, false);
            player.getActionSender().sendMessage("Something went wrong... Please report this on forums: (" + type + ")(" + buttonId + ").");
            return;
        }
        int configStart = modern ? 45 : 13;
        boolean defensive = player.getSettings().getAttackStyleIndex() == 3;
        player.getActionSender().sendHideComponent(90, 183, defensive);
        player.getActionSender().sendHideComponent(90, 83, !defensive);
        CombatSpell current = player.getProperties().getAutocastSpell();
        current = (CombatSpell) (modern ? SpellBook.MODERN.getSpell(autocastSpell.getSpellId()) :
            SpellBook.ANCIENT.getSpell(autocastSpell.getSpellId()));
        player.getProperties().setAutocastSpell(current);
        player.getActionSender().sendHideComponent(90, (defensive ? 100 : 0) + configStart + (2 * buttonId), false);
    }

    /**
     * Opens the autocast select.
     */
    public void openAutocastSelect() {
        if (current != WeaponInterfaces.STAFF) {
            return;
        }
        if (!canAutocast(true)) {
            setAttackStyle(3);
            return;
        }
        player.setAttribute("autocast_select", true);
        // 12050 = slayer
        int interfaceId = SpellBook.forInterface(player.getSpellBookManager().getSpellBook()).getAutocastId();
        if (interfaceId == -1) {
            return;
        }
        if (player.getEquipment().getNew(Equipment.SLOT_WEAPON).getId() == 4170) {
            if (player.getSpellBookManager().getSpellBook() == SpellBook.MODERN.getInterfaceId()) {
                interfaceId = 12050;
            } else {
                // TODO 317 message?
                return;
            }
        }
        Component component = new Component(interfaceId);
        component.getDefinition().setContext(new InterfaceContext(player, 548, 128, interfaceId, false));
        player.getInterfaceState().openTab(Sidebar.ATTACK_TAB.ordinal(), component);
    }

    /**
     * Checks if the player is currently able to autocast.
     *
     * @param message If we should notify the player if he's unable to autocast.
     * @return <code>True</code> if so.
     */
    public boolean canAutocast(boolean message) {
        if (current != WeaponInterfaces.STAFF) {
            return false;
        }
        if (player.getSpellBookManager().getSpellBook() == SpellBook.LUNAR.getInterfaceId()) {
            if (message) {
                player.getActionSender().sendMessage("You can't autocast Lunar magic.");
            }
            return false;
        }
        String staffName = player.getEquipment().getNew(3).getName();
        boolean ancientStaff = staffName.contains("ncient staff");
        if (staffName.equals("Lunar staff")) {
            if (message) {
                player.getActionSender().sendMessage(ancientStaff ? "You can only autocast ancient magicks with an Ancient staff." :
                    "You cannot autocast with that.");
            }
            return false;
        }
        if ((player.getSpellBookManager().getSpellBook() == SpellBook.MODERN.getInterfaceId() && ancientStaff) ||
            (player.getSpellBookManager().getSpellBook() == SpellBook.ANCIENT.getInterfaceId() && !ancientStaff)) {
            if (message) {
                player.getActionSender().sendMessage("You can only autocast ancient magicks with an Ancient staff.");
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current weapon interface id.
     *
     * @return The component id.
     */
    public static WeaponInterfaces getWeaponInterface(Item weapon) {
        if (weapon == null) {
            return WeaponInterfaces.values()[0];
        }
        int slot = weapon.getDefinition().getConfiguration(ItemConfiguration.WEAPON_INTERFACE, 0);
        if (slot < 0) {
            return WeaponInterfaces.values()[0];
        }
        return WeaponInterfaces.values()[slot];
    }

    /**
     * Represents an attack style.
     *
     * @author Emperor
     */
    public static class AttackStyle {

        /**
         * The style type.
         */
        private final int style;

        /**
         * The bonus type.
         */
        private final int bonusType;

        /**
         * Constructs a new {@code AttackStyle} {@code Object}.
         *
         * @param style     The style type.
         * @param bonusType The bonus type.
         */
        public AttackStyle(int style, int bonusType) {
            this.style = style;
            this.bonusType = bonusType;
        }

        /**
         * Gets the style.
         *
         * @return The style.
         */
        public int getStyle() {
            return style;
        }

        /**
         * Gets the bonusType.
         *
         * @return The bonusType.
         */
        public int getBonusType() {
            return bonusType;
        }
    }

    /**
     * Represents the weapon interfaces.
     *
     * @author Emperor
     */
    public static enum WeaponInterfaces {

        /**
         * The unarmed weapon interface (ordinal=0)
         */
        UNARMED(5855, 5857, -1, new int[]{ 5860, 5862, 5861 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The staff weapon interface (ordinal=1)
         */
        STAFF(328, 355, 329, new int[]{ 336, 335, 334 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE_CAST, BONUS_MAGIC), new AttackStyle(STYLE_CAST, BONUS_MAGIC)),

        /**
         * The (battle) axe weapon interface (ordinal=2)
         */
        AXE(1698, 1701, 1699, new int[]{ 1704, 1707, 1706, 1705 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The scepter weapon interface (ordinal=3)
         */
        SCEPTER(6103, 6132, -1, new int[]{ 6137, 6136, 6135 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The pickaxe weapon interface (ordinal=4)
         */
        PICKAXE(5570, 5573, 5571, new int[]{ 5576, 5579, 5578, 5577 }, new AttackStyle(STYLE_ACCURATE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The swords and daggers weapon interface (ordinal=5)
         */
        SWORD_DAGGER(2276, 2279, 2277, new int[]{ 2282, 2285, 2284, 2283 }, new AttackStyle(STYLE_ACCURATE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The scimitar/silver light/silver sickle/... weapon interface (ordinal=6)
         */
        SCIMITAR(2423, 2426, 2424, new int[]{ 2429, 2432, 2431, 2430 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The 2-h sword weapon interface (ordinal=7)
         */
        TWO_H_SWORD(4705, 4708, -1, new int[]{ 4711, 4714, 4713, 4712 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The mace weapon interface (ordinal=8)
         */
        MACE(3796, 3799, -1, new int[]{ 3802, 3805, 3804, 3803 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The claws weapon interface (ordinal=9)
         */
        CLAWS(7762, 7765, -1, new int[]{ 7768, 7771, 7770, 7769 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The warhammer/maul weapon interface (ordinal=10)
         */
        WARHAMMER_MAUL(425, 428, -1, new int[]{ 433, 432, 431 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The abyssal whip weapon interface (ordinal=11)
         */
        WHIP(12290, 12293, 12291, new int[]{ 12298, 12297, 12296 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The flowers weapon interface (ordinal=12)
         */
        FLOWERS(425, 428, -1, new int[]{ 433, 432, 431 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * The mud pie weapon interface (ordinal=13)
         */
        MUD_PIE(4446, 4449, 4447, new int[]{ 4454, 4453, 4452 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The spear weapon interface (ordinal=14)
         */
        SPEAR(4679, 4682, 4680, new int[]{ 4685, 4688, 4687, 4686 }, new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_CONTROLLED, BONUS_SLASH), new AttackStyle(STYLE_CONTROLLED, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The halberd weapon interface (ordinal=15)
         */
        HALBERD(8460, 8463, 8461, new int[]{ 8466, 8468, 8467 }, new AttackStyle(STYLE_CONTROLLED, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_DEFENSIVE, BONUS_STAB)),

        /**
         * The bow weapon interface (ordinal=16)
         */
        BOW(1764, 1767, 1765, new int[]{ 1772, 1771, 1770 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The crossbow weapon interface (ordinal=17)
         */
        CROSSBOW(1764, 1767, 1765, new int[]{ 1772, 1771, 1770 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The thrown weapons weapon interface (ordinal=18)
         */
        THROWN_WEAPONS(1749, 1752, 1750, new int[]{ 1757, 1756, 1755 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The thrown weapons weapon interface (ordinal=19)
         */
        CHINCHOMPA(24055, 24056, -1, new int[]{ 24059, 24060, 24061 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE)),

        /**
         * The fixed device weapon interface (ordinal=20)
         */
        FIXED_DEVICE(13975, 13977, 13976, new int[]{ 0, 0 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH)),

        /**
         * The salamander weapon interface (ordinal=21)
         */
        SALAMANDER(24074, 24075, -1, new int[]{ 24078, 24079, 24080 }, new AttackStyle(STYLE_AGGRESSIVE, BONUS_SLASH), new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_DEFENSIVE_CAST, BONUS_MAGIC)),

        /**
         * The scythe weapon interface (ordinal=22)
         */
        SCYTHE(776, 779, 780, new int[]{ 782, 784, 785, 783 }, new AttackStyle(STYLE_ACCURATE, BONUS_SLASH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_STAB), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_SLASH)),

        /**
         * The ivandis flail weapon interface (ordinal=23) TODO: Find correct interface id!
         */
        IVANDIS_FLAIL(6103, 6132, -1, new int[]{ 6137, 6136, 6135 }, new AttackStyle(STYLE_ACCURATE, BONUS_CRUSH), new AttackStyle(STYLE_AGGRESSIVE, BONUS_CRUSH), new AttackStyle(STYLE_DEFENSIVE, BONUS_CRUSH)),

        /**
         * Toxic blowpipe. (ordinal=24) TODO correct interface id
         */
        TOXIC_BLOWPIPE(1764, 1767, 1765, new int[]{ 1772, 1771, 1770 }, new AttackStyle(STYLE_RANGE_ACCURATE, BONUS_RANGE), new AttackStyle(STYLE_RAPID, BONUS_RANGE), new AttackStyle(STYLE_LONG_RANGE, BONUS_RANGE));

        /**
         * The interface id.
         */
        private final int interfaceId;

        /**
         * The text ID for the weapon name.
         */
        private int textId = -1;

        /**
         * The id for the model.
         */
        private int modelId = -1;

        /**
         * The slot ids for the {@link #attackStyles}.
         */
        private int[] slotIds;

        /**
         * The attack styles.
         */
        private final AttackStyle[] attackStyles;

        /**
         * Constructs a new {@code WeaponInterface} {@code Object}.
         *
         * @param interfaceId  The interface id.
         * @param attackStyles The attack styles.
         */
        private WeaponInterfaces(int interfaceId, AttackStyle... attackStyles) {
            this.interfaceId = interfaceId;
            this.attackStyles = attackStyles;
        }

        /**
         * Constructs a new {@code WeaponInterface} {@code Object}.
         *
         * @param interfaceId  The interface id.
         * @param attackStyles The attack styles.
         */
        private WeaponInterfaces(int interfaceId, int textId, int modelId, int[] slotIds, AttackStyle... attackStyles) {
            this.interfaceId = interfaceId;
            this.textId = textId;
            this.modelId = modelId;
            this.slotIds = slotIds;
            this.attackStyles = attackStyles;
        }

        /**
         * Gets the interfaceId.
         *
         * @return The interfaceId.
         */
        public int getInterfaceId() {
            return interfaceId;
        }

        /**
         * Gets the textId.
         *
         * @return The textId.
         */
        public int getTextId() {
            return textId;
        }

        /**
         * Gets the modelId.
         *
         * @return The modelId.
         */
        public int getModelId() {
            return modelId;
        }

        /**
         * Gets the slot ids.
         *
         * @return The slot ids.
         */
        public int[] getSlotIds() {
            return slotIds;
        }

        /**
         * Gets the attackStyles.
         *
         * @return The attackStyles.
         */
        public AttackStyle[] getAttackStyles() {
            return attackStyles;
        }
    }

    /**
     * Gets the currently opened weapon interface.
     *
     * @return The current weapon interface.
     */
    public WeaponInterfaces getWeaponInterface() {
        return current;
    }

    /**
     * If the special bar is enabled.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSpecialBar() {
        return specialBar;
    }

}
