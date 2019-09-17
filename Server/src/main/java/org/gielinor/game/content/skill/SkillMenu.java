package org.gielinor.game.content.skill;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.menu.SkillMenuDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.context.InterfaceScrollPositionContext;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.InterfaceScrollPosition;

/**
 * Represents a skill menu.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkillMenu {

    /**
     * The id of the skill.
     */
    private final int skillId;

    /**
     * The subsections for this <code>SkillMenu</code>.
     */
    private final String[] subsections;

    /**
     * The <code>SkillMenu</code> list.
     */
    private static final List<SkillMenu> SKILL_MENUS = new ArrayList<>();

    /**
     * The {@link org.gielinor.game.content.skill.menu.SkillMenuDefinition}s for this <code>SkillMenu</code>.
     */
    private final List<SkillMenuDefinition> skillMenuDefinitions = new ArrayList<>();

    /**
     * Creates a new <code>SkillMenu</code>.
     *
     * @param skillId     The id of the skill.
     * @param subsections The subsections for this <code>SkillMenu</code>.
     */
    public SkillMenu(int skillId, String[] subsections) {
        this.skillId = skillId;
        this.subsections = subsections;
    }

    /**
     * Gets the id of the skill.
     *
     * @return The id.
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * Gets the subsections for this <code>SkillMenu</code>.
     *
     * @return The subsections.
     */
    public String[] getSubsections() {
        return subsections;
    }

    /**
     * Gets a skill menu by the skill id.
     *
     * @param id The id of the skill.
     * @return The skill menu.
     */
    public static SkillMenu forId(int id) {
        for (SkillMenu skillMenu : getSkillMenus()) {
            if (skillMenu.getSkillId() == id) {
                return skillMenu;
            }
        }
        return null;
    }

    /**
     * Gets the <code>SkillMenu</code> list.
     *
     * @return The list.
     */
    public static List<SkillMenu> getSkillMenus() {
        return SKILL_MENUS;
    }

    public boolean open(Player player, int subsection, boolean open) {
        int length = 0;
        clear(player);
        int childId = 50024;
        PacketRepository.send(InterfaceScrollPosition.class, new InterfaceScrollPositionContext(player, 50023, 0));
        for (SkillMenuDefinition skillMenuDefinition : getSkillMenuDefinitions()) {
            if (skillMenuDefinition.getSubsection() == subsection) {
                int lines = skillMenuDefinition.getMessage().split("<br>").length;
                length += (skillMenuDefinition.hasText() ? (lines * 14) : 0);
                if ((skillMenuDefinition.getItemId() == -1) || (skillMenuDefinition.getLevel() >= 0 && skillMenuDefinition.getItemId() >= 0)) {
                    length += 36;
                }
                player.getActionSender().sendString(childId, skillMenuDefinition.getLevel() == -1 ? "" : String.valueOf(skillMenuDefinition.getLevel()));
                player.getActionSender().sendUpdateItem((childId + 100), 0, skillMenuDefinition.getItemId() == -1 ? null : new Item(skillMenuDefinition.getItemId()));
                player.getActionSender().sendString((childId + 200), skillMenuDefinition.getMessage());
                if (skillMenuDefinition.hasText()) {
                    childId++;
                    player.getActionSender().sendString(childId, "");
                    player.getActionSender().sendUpdateItem((childId + 100), 0, null);
                    player.getActionSender().sendString((childId + 200), "");
                    length += (skillMenuDefinition.getMessage().split("<br>").length * 14);
                    childId++;
                    continue;
                }
                childId++;
            }
        }
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 50023, length + 32));
        player.setAttribute("SKILL_MENU", this);
        for (int clear = 0; clear < 20; clear++) {
            player.getActionSender().sendString(childId, "");
            player.getActionSender().sendUpdateItem((childId + 100), 0, null);
            player.getActionSender().sendString((childId + 200), "");
            childId++;
        }

        player.getActionSender().sendString(50021, Skills.SKILL_NAME[skillId]);
        player.getActionSender().sendString(50022, getSubsections()[subsection]);
        player.getActionSender().sendInterfaceConfig(2, getSubsections().length == 1 ? 0 : (getSubsections().length + 1));
        childId = 50005;
        for (String sub : getSubsections()) {
            player.getActionSender().sendString(childId, sub);
            childId++;
        }
        if (open) {
            player.getInterfaceState().open(new Component(50000));
        }
        return true;
    }

    /**
     * Clears the skill menu's basic rows.
     *
     * @param player The player.
     */
    public static void clear(Player player) {
        for (int childId = 50024; childId < 50040; childId++) {
            player.getActionSender().sendString(childId, "");
            player.getActionSender().sendUpdateItem((childId + 100), 0, null);
            player.getActionSender().sendString((childId + 200), "");
            player.getActionSender().sendString((childId + 300), "");
        }
    }

    /**
     * Gets the {@link org.gielinor.game.content.skill.menu.SkillMenuDefinition}s.
     *
     * @return The definitions.
     */
    public List<SkillMenuDefinition> getSkillMenuDefinitions() {
        return skillMenuDefinitions;
    }
}