package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the magic book interface handling of non-combat spells.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MagicBookInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(1151, this);
        ComponentDefinition.put(12855, this);
        ComponentDefinition.put(29999, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 1151 && component.getId() != 12855 && component.getId() != 29999) {
            return false;
        }
        if (teleportClick(button)) {
            TeleportsInterfacePlugin.open(player);
            return true;
        }
        if (World.getTicks() < player.getAttribute("magic:delay", -1)) {
            return true;
        }
        SpellBook spellBook = component.getId() == 1151 ? SpellBook.MODERN :
            component.getId() == 12855 ? SpellBook.ANCIENT : SpellBook.LUNAR;
        MagicSpell magicSpell = spellBook.getSpell(button);
        if (magicSpell == null) {
            return false;
        }
        MagicSpell.castSpell(player, spellBook, button, player);
        return true;
    }

    private boolean teleportClick(int button) {
        switch (button) {
            case 1164:
            case 1167:
            case 1170:
            case 1174:
            case 1540:
            case 1541:
            case 7455:
            case 13035:
            case 13045:
            case 13053:
            case 13061:
            case 13069:
            case 13079:
            case 13087:
            case 13095:
                return true;
        }
        return false;
    }
}
