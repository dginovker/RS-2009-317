package com.runescape.util;

import main.java.com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.InterfaceChild;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.net.packet.PacketSender;

/**
 * Handles Summoning tasks.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class Summoning {

    /**
     * The {@link Game} instance.
     */
    private final Game game;

    /**
     * The current spell usable on.
     */
    private int spellUsableOn;

    /**
     * The spell name.
     */
    private String spellName;

    /**
     * The original special move bar full.
     */
    private Sprite specialMoveBarFull;

    /**
     * The special move bars.
     */
    public Sprite[] specialMoveBars;

    /**
     * Constructs a new <code>Summoning</code>.
     *
     * @param game The {@link Game} instance.
     */
    public Summoning(Game game) {
        this.game = game;
    }

    /**
     * Processes actions from the Summoning orb.
     *
     * @param action The id of the action.
     */
    public void processAction(int action, int id, int first, int secondaryClick, int clicked) {
        String actionName = game.menuActionName[id];
        if (actionName.contains("Attack")) {
            game.spellSelected = 1;
            game.anInt1137 = 25943;
            game.spellUsableOn = 14;
            game.itemSelected = 0;
            game.spellTooltip = "Attack ->";
            return;
        }
        if (actionName.contains("Special attack") && spellUsableOn != 1) {
            game.spellSelected = 1;
            game.anInt1137 = 25942;
            game.spellUsableOn = spellUsableOn;
            game.itemSelected = 0;
            game.spellTooltip = spellName + "</col> ->";
            return;
        }
        game.getPacketSender().send(PacketSender.SUMMONING_ORB_ACTION, (byte) (action - 4000));
        game.spellSelected = 0;
        game.itemSelected = 0;
    }

    /**
     * The black box id.
     */
    private static final int BLACK_BOX_ID = 25942;
    /**
     * The black box configs.
     */
    private static final int[][] BLACK_BOX_CONFIGS = new int[][]{
            {93, 0, 0},
            {106, -13, 10, 3},
            {119, -26, 23, 3},
            {132, -39, 36}
    };

    /**
     * Updates the Summoning special move tab.
     * TODO Move
     *
     * @param specialName        The name of the scroll being casted.
     * @param levelRequired      The level required.
     * @param scrollId           The id of the scroll item.
     * @param scrollAmount       The amount of the scroll item required.
     * @param specialDescription The description.
     */
    public void updateInformationTab(String specialName, String fullName, int levelRequired, int scrollId, int scrollAmount, String specialDescription, byte interfaceType) {
        RSComponent rsComponent = RSComponent.forId(BLACK_BOX_ID);
        String tooltip = null;
        if (fullName.contains(":") && fullName.contains("(")) {
            tooltip = fullName.substring(fullName.indexOf(":") + 2, fullName.indexOf("(") - 1);
        }
        if (tooltip == null) {
            tooltip = specialName;
        }
        rsComponent.tooltip = "Cast <col=00FF00>" + tooltip;
        spellName = "Cast <col=00FF00>" + tooltip;
        rsComponent.spellName = "<col=00FF00>" + tooltip + "</col>";
        rsComponent.spellUsableOn = interfaceType;
        this.spellUsableOn = interfaceType;
        rsComponent.optionType = interfaceType == 1 ? RSComponent.BUTTON_ACTION_TYPE : RSComponent.SPELL_ACTION_TYPE;
        RSComponent.forId(25915).scripts[0][2] = scrollId;
        rsComponent.scriptDefaults[0] = (scrollAmount - 1);
        rsComponent.scriptDefaults[1] = levelRequired;
        rsComponent.scripts[0][2] = scrollId;
        String name = TextConstants.wrap(fullName, 160, Game.INSTANCE.regularFont);
        if (name == null) {
            name = fullName;
        }
        RSComponent.forId(BLACK_BOX_ID + 5).disabledMessage = name;
        String description = TextConstants.wrap(specialDescription, 150, Game.INSTANCE.smallFont);
        RSComponent.forId(BLACK_BOX_ID + 6).disabledMessage = description;
        updateBlackBox(description.split("\\\\n").length);
        rsComponent = RSComponent.forId(BLACK_BOX_ID + 8);
        rsComponent.scriptDefaults[0] = (scrollAmount - 1);
        rsComponent.scripts[0][2] = scrollId;
        rsComponent.disabledMessage = "%1/" + scrollAmount;
        rsComponent = RSComponent.forId(BLACK_BOX_ID + 7);
        if (scrollId != -1) {
            rsComponent.mediaID = scrollId;
            rsComponent.enabledMediaID = scrollId;
            ItemDefinition itemDefinition = ItemDefinition.forId(scrollId);
            rsComponent.modelRotation1 = itemDefinition.modelRotationX;
            rsComponent.modelRotation2 = itemDefinition.modelRotationY;
            rsComponent.modelZoom = 1050;
        }
    }

    /**
     * Adjusts the summoning tab's black box.
     */
    public void updateBlackBox(int length) {
        int index = length - 1;
        if (index > 3) {
            index = 0;
        }
        RSComponent rsComponent = RSComponent.forId(25904);
        rsComponent.getInterfaceChild(BLACK_BOX_ID + 1).setY(163 + BLACK_BOX_CONFIGS[index][1]);
        for (InterfaceChild interfaceChild : RSComponent.forId(BLACK_BOX_ID + 1).getInterfaceChildren()) {
            if (interfaceChild.getId() >= (BLACK_BOX_ID + 2) && interfaceChild.getId() <= (BLACK_BOX_ID + 4)) {
                interfaceChild.getRSComponent().height = (BLACK_BOX_CONFIGS[index][0] - (interfaceChild.getId() == (BLACK_BOX_ID + 3) ? 2 : interfaceChild.getId() == (BLACK_BOX_ID + 4) ? 4 : 0));
            }
            if (interfaceChild.getId() == (BLACK_BOX_ID + 7)) {
                interfaceChild.setY(46 + BLACK_BOX_CONFIGS[index][2]);
            }
            if (interfaceChild.getId() == (BLACK_BOX_ID + 8)) {
                interfaceChild.setY(78 + BLACK_BOX_CONFIGS[index][2] + (BLACK_BOX_CONFIGS[index].length == 4 ? BLACK_BOX_CONFIGS[index][3] : 0));
            }
        }
    }

    /**
     * Sets the special move bar sprites.
     *
     * @param specialMoveBars The sprites.
     */
    public void setSpecialMoveBars(Sprite[] specialMoveBars) {
        this.specialMoveBars = specialMoveBars;
        this.specialMoveBarFull = ImageLoader.forName("special-move-bar-full2");
    }

    /**
     * Gets a special move bar {@link com.runescape.cache.media.Sprite}.
     *
     * @param full If the bar is full.
     * @return The sprite.
     */
    public Sprite getSpecialMoveBar(boolean full) {
        return specialMoveBars[full ? 0 : 1];
    }

    /**
     * Sets the special move bar width.
     *
     * @param specialMovePoints The special move points.
     */
    public void setSpecialAttackWidth(int specialMovePoints) {
        RSComponent.forId(25975).disabledMessage = specialMovePoints + "/60 special move\\npoints remaining";
        specialMoveBars[0] = new Sprite(specialMoveBarFull);
        RSComponent rsComponent = RSComponent.forId(25974);
        rsComponent.getSpriteSet().setDisabled(specialMoveBars[0]);
        rsComponent.getSpriteSet().setEnabled(specialMoveBars[0]);
        if (specialMovePoints == 60) {
            return;
        }
        int percentage = 1;
        if (specialMovePoints >= 55 && specialMovePoints < 60) {
            percentage = 130;
        } else if (specialMovePoints >= 50 && specialMovePoints < 55) {
            percentage = 125;
        } else if (specialMovePoints >= 45 && specialMovePoints < 50) {
            percentage = 115;
        } else if (specialMovePoints >= 40 && specialMovePoints < 45) {
            percentage = 110;
        } else if (specialMovePoints >= 35 && specialMovePoints < 40) {
            percentage = 100;
        } else if (specialMovePoints >= 30 && specialMovePoints < 35) {
            percentage = 71;
        } else if (specialMovePoints >= 25 && specialMovePoints < 30) {
            percentage = 64;
        } else if (specialMovePoints >= 20 && specialMovePoints < 25) {
            percentage = 44;
        } else if (specialMovePoints >= 15 && specialMovePoints < 20) {
            percentage = 32;
        } else if (specialMovePoints >= 10 && specialMovePoints < 15) {
            percentage = 18;
        }
        rsComponent.getSpriteSet().getDisabled().cropImage2(0, 0, percentage, 0);
    }
}
