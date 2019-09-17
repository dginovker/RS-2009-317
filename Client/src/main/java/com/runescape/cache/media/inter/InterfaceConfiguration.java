package com.runescape.cache.media.inter;

import com.runescape.Game;

/**
 * Represents an {@code InterfaceConfiguration}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum InterfaceConfiguration {
    WILDERNESS_WARNING(1),
    SKILL_MENU_SUBSECTION(2),
    SKILL_MENU_SUBSECTION_HEIGHT(3),
    DUEL_ARENA_ACCEPT_BUTTON(27),
    HIDE_COMPONENT(50),
    SHOW_COMPONENT(51),
    HIDE_COMPONENT_SPRITE(52),
    DRAW_COMPONENT_SPRITE(53),
    MODE_FIXED(100),
    MODE_RESIZABLE(101),
    MODE_FULLSCREEN(102),
    TRANSPARENT_SIDE_PANEL(103),
    REMAINING_XP(104),
    HIDE_ROOFS(105),
    ORBS_ENABLED(106),
    TRANSPARENT_CHAT(107),
    SIDE_STONES_ARRANGEMENT(109),
    HOTKEY_CLOSE(110),
    XP_DROP_LOCK(200),
    XP_DROPS(201),
    CLICK_THROUGH_CHAT(234),
    TELEPORT_TAB(235),
    UNKNOWN(236),
    TELEPORT_BUTTONS(237),
    HIT_ICONS(238),
    HEALTH_BARS(239),
    TWEENING(240),
    GAMEFRAME(241),
    HITSPLATS(242),
    CURSORS(243),
    HD_TEXTURES(244),
    FOG(245),
    CONTEXT_MENU(246),
    STAMINA_POTION(247),
    QUICK_PRAYERS(256),
    AUTO_ATTACK_OPTION(315),
    LEFT_CLICK_ATTACK_OPTION(316),
    RIGHT_CLICK_ATTACK_OPTION(317),
    //    UNKNOWN_1(321),
//    UNKNOWN_2(322),
//    UNKNOWN_3(323),
//    UNKNOWN_4(324),
//    UNKNOWN_5(325),
    GRAND_EXCHANGE_OFFER_TYPE(326),
    UNKNOWN_6(327),
    UNKNOWN_7(328),
    UNKNOWN_8(329),
    UNKNOWN_9(330),
    GRAND_EXCHANGE_SLOTS_DISABLED(331),
    GRAND_EXCHANGE_VIEW_SCREEN(332),
    CURRENT_BANK_TAB(334),
    BANK_TAB_COUNT(335),
    BANK_SEARCH_MODE(336),
    BANK_ICON_TYPE(337),
    BANK_NUMERAL_TYPE(338),
    BANK_ROMAN_TYPE(339),
    BANK_LAST_X(340),
    HAS_FAMILIAR(350),
    SUMMONING_SPECIAL_MOVE(351),
    SUMMONING_PET(352),
    SUMMONING_FAMILIAR(353),
    SUMMONING_BOB(354),
    SUMMONING_LEFT_CLICK_OPTION(355),
    SUMMONING_SPECIAL_MOVE_POINTS(356); // 350+ SUMMONING

    /**
     * The config id.
     */
    private final int id;

    /**
     * Constructs a new {@code Configuration} {@code Object}.
     *
     * @param id The config id.
     */
    private InterfaceConfiguration(int id) {
        this.id = id;
    }

    /**
     * Gets the configuration by id.
     *
     * @return id The id.
     */
    public static InterfaceConfiguration forId(int id) {
        for (InterfaceConfiguration interfaceConfiguration : values()) {
            if (interfaceConfiguration.getId() == id) {
                return interfaceConfiguration;
            }
        }
        return null;
    }


    /**
     * Gets the configuration by id.
     *
     * @return id The id.
     */
    public int get() {
        return Game.INSTANCE.getInterfaceConfig(this);
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }
}