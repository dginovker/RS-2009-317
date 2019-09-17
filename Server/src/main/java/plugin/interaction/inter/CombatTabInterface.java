package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the combat tab interface.
 *
 * @author Emperor
 * @author Vexia'
 * @version 1.0
 */
public class CombatTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (WeaponInterfaces inter : WeaponInterfaces.values()) {
            ComponentDefinition.put(inter.getInterfaceId(), this);
        }
        ComponentDefinition.put(5855, this);
        return this;
    }

    @Override
    public boolean handle(Player p, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() == 24794) {
            return false;
        }
        WeaponInterface weaponInterface = p.getExtension(WeaponInterface.class);
        switch (button) {
            case 12311:
            case 7562:
            case 7537:
            case 7667:
            case 7687:
            case 11696:
            case 14484:
            case 33652:
            case 12322:
            case 10003:
            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
            case 7473:
            case 7487:
            case 7788:
            case 8481:
            case 7612:
            case 7587:
            case 7662:
            case 7462:
            case 7548:
            case 7637:
            case 7498:
            case 7623:
                if (weaponInterface != null && weaponInterface.isSpecialBar()) {
                    p.getSettings().toggleSpecialBar();
                    if (p.getSettings().isSpecialToggled()) {
                        CombatSwingHandler handler;
                        if ((handler = CombatStyle.MELEE.getSwingHandler().getSpecial(p.getEquipment().getNew(3).getId())) != null) {
                            @SuppressWarnings("unchecked")
                            Plugin<Object> plugin = (Plugin<Object>) handler;
                            if (plugin.fireEvent("instant_spec", p) == Boolean.TRUE) {
                                handleInstantSpec(p, handler, plugin);
                            }
                        }
                    }
                    return true;
                }
                return false;
            case 22845:
            case 24010:
            case 24115:
            case 24041:
            case 150:
            case 24:
            case 26:
                p.getSettings().toggleRetaliating();
                return true;
            default:
                weaponInterface = p.getExtension(WeaponInterface.class);
                if (weaponInterface == null) {
                    return false;
                }
                boolean canHandle = (button == 24111 || button == 349);
                for (int buttonId : weaponInterface.getWeaponInterface().getSlotIds()) {
                    if (button == buttonId) {
                        canHandle = true;
                        break;
                    }
                }
                if (!canHandle) {
                    return false;
                }
                if (weaponInterface.setAttackStyle(button)) {
                    if (button == 24111 || button == 349) {
                        weaponInterface.openAutocastSelect();
                    } else if (p.getProperties().getAutocastSpell() != null) {
                        weaponInterface.selectAutoSpell(-1, false);
                    }
                    return true;
                }
                return false;
        }
    }

    /**
     * Method used to handle an instance special attack.
     *
     * @param p       the player.
     * @param handler the handler.
     * @param plugin  the plugin.
     */
    private static void handleInstantSpec(Player p, CombatSwingHandler handler, Plugin<Object> plugin) {
        handler.swing(p, p.getProperties().getCombatPulse().getVictim(), null);
    }
}
