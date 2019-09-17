package org.gielinor.game.content.skill.free.prayer;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;

/**
 * Handles Quick Prayers.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class QuickPrayer {

    /**
     * The player.
     */
    private Player player;
    /**
     * The quick prayers selected.
     */
    private final List<PrayerType> quickPrayers = new ArrayList<>();
    /**
     * The beginning configuration id.
     */
    private final int CONFIG = 205;
    /**
     * If quick prayers are toggled on.
     */
    private boolean toggledOn = false;

    /**
     * Constructs the {@link QuickPrayer} instance.
     *
     * @param player The player.
     */
    public QuickPrayer(Player player) {
        this.player = player;
    }

    /**
     * Opens the quick prayer selection interface.
     */
    public void open() {
        player.getInterfaceState().setViewedTab(Sidebar.PRAYER_TAB.ordinal());
        player.getInterfaceState().openSingleTab(new Component(17200), Sidebar.PRAYER_TAB.ordinal());
        for (PrayerType prayerType : PrayerType.values()) {
            if (prayerType.isCurses()) {
                continue;
            }
            player.getInterfaceState().set(CONFIG + prayerType.ordinal(), quickPrayers.contains(prayerType) ? 1 : 0, false);
        }
    }

    /**
     * Toggles the player's quick prayers on and off.
     */
    public void toggle() {
        if (toggledOn) {
            toggledOn = false;
            player.getPrayer().reset();
            player.getActionSender().sendInterfaceConfig(256, 0);
            return;
        }
        if (player.getSkills().getPrayerPoints() < 1) {
            player.getActionSender().sendMessage("You have run out of prayer points, you must recharge at an altar.", 1);
            player.getPrayer().reset();
            return;
        }
        for (PrayerType prayerType : PrayerType.values()) {
            if (quickPrayers.contains(prayerType)) {
                if (!prayerType.permitted(player)) {
                    return;
                }
            }
        }
        boolean canToggle = false;
        for (PrayerType prayerType : PrayerType.values()) {
            if (quickPrayers.contains(prayerType)) {
                if (prayerType.permitted(player)) {
                    prayerType.toggle(player, true);
                    canToggle = true;
                }
            }
        }
        if (canToggle) {
            toggledOn = true;
            player.getActionSender().sendInterfaceConfig(256, 1);
            return;
        }
        player.getActionSender().sendMessage("You do not have any quick prayers selected.");
    }

    /**
     * Toggles a quick prayer on or off.
     *
     * @param childId The child id.
     */
    public boolean toggleQuickPrayer(int childId) {
        if (childId >= 17202 && childId <= 17227) {
            PrayerType prayerType = PrayerType.forQuick(childId);
            if (prayerType == null) {
                return false;
            }
            if (quickPrayers.contains(prayerType)) {
                quickPrayers.remove(prayerType);
                player.getInterfaceState().set(CONFIG + prayerType.ordinal(), 0, false);
                return true;
            }
            if (!prayerType.permitted(player, false)) {
                return true;
            }
            // Deactivate restrictions
            deactivateRestrictions(prayerType);
            //
            quickPrayers.add(prayerType);
            player.getInterfaceState().set(CONFIG + prayerType.ordinal(), 1, false);
            return true;
        }
        return false;
    }

    /**
     * Deactivates restricted prayers.
     *
     * @param prayerType The prayer to enable.
     */
    public void deactivateRestrictions(PrayerType prayerType) {
        final List<PrayerType> activePrayers = getQuickPrayers();
        final List<PrayerType> removePrayers = new ArrayList<>();
        for (PrayerType quickPrayer : activePrayers) {
            if (quickPrayer.getRestriction() == prayerType.getRestriction() || quickPrayer.getSecondRestriction() != null
                && prayerType.getSecondRestriction() != null && quickPrayer.getSecondRestriction() == prayerType.getSecondRestriction()) {
                removePrayers.add(quickPrayer);
                continue;
            }
            for (SkillBonus b : quickPrayer.getBonuses()) {
                for (SkillBonus bb : prayerType.getBonuses()) {
                    if ((bb.getSkillId() == b.getSkillId()) || (b.getSkillId() == Skills.STRENGTH ||
                        b.getSkillId() == Skills.ATTACK) && (bb.getSkillId() == Skills.MAGIC ||
                        bb.getSkillId() == Skills.RANGE) || (b.getSkillId() == Skills.RANGE ||
                        b.getSkillId() == Skills.MAGIC) && (bb.getSkillId() == Skills.ATTACK ||
                        bb.getSkillId() == Skills.STRENGTH) || (b.getSkillId() == Skills.DEFENCE &&
                        bb.getSkillId() == Skills.DEFENCE)) {
                        removePrayers.add(quickPrayer);
                    }
                }
            }
        }
        for (PrayerType quickPrayer : removePrayers) {
            getQuickPrayers().remove(quickPrayer);
            player.getInterfaceState().set(CONFIG + quickPrayer.ordinal(), 0, false);
        }
    }

    /**
     * Gets the {@link java.util.List} of quick prayers.
     *
     * @return The list.
     */
    public List<PrayerType> getQuickPrayers() {
        return quickPrayers;
    }
}
