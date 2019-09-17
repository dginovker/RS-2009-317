package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.ConfigurationManager.Configuration;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SettingTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(31000, this);
        ComponentDefinition.put(0, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 0) {
            //System.out.println("Component with ID of 0! Button: " + button);
        }
        switch (button) {
            case 31005:
                player.getInterfaceState().openSingleTab(new Component(31040), 11);
                return true;
            case 31008:
                player.getInterfaceState().openSingleTab(new Component(31060), 11);
                return true;
            case 31011:
                player.getInterfaceState().openSingleTab(new Component(31080), 11);
                return true;

            case 31002:
            case 31041:
                player.getInterfaceState().openTab(11, new Component(31000));
                return true;
            /**
             * Brightness.
             */
            case 31037: // 1
            case 31109: // 2
            case 31113: // 3
            case 31117: // 4
                player.getSettings().setBrightness(button == 31117 ? 3 : button == 31113 ? 2 : button == 31109 ? 1 : 0);
                return true;
            /**
             * Music.
             */
            case 930:
            case 931:
            case 932:
            case 933:
            case 934:
                // TODO 317 - Reverse 930 = 4, 931 = 3, etc
                player.getSettings().setMusicVolume(button - 930);
                return true;

            // TODO
            /**
             * Music off.
             */
            case 31128:
                player.getSettings().setMusicVolume(4);
                return true;
            /**
             * Music 1.
             */
            case 31132:
                player.getSettings().setMusicVolume(3);
                return true;
            /**
             * Music 2.
             */
            case 31136:
                player.getSettings().setMusicVolume(2);
                return true;
            /**
             * Music 3.
             */
            case 31140:
                player.getSettings().setMusicVolume(1);
                return true;
            /**
             * Effects off.
             */
            case 31144:
                player.getSettings().setSoundEffectVolume(4);
                return true;
            /**
             * Effects 1.
             */
            case 31148:
                player.getSettings().setSoundEffectVolume(3);
                return true;
            /**
             * Effects 2.
             */
            case 31152:
                player.getSettings().setSoundEffectVolume(2);
                return true;
            /**
             * Effects 3.
             */
            case 31156:
                player.getSettings().setSoundEffectVolume(1);
                return true;
            /**
             * Area off.
             */
            case 31160:
                player.getSettings().setAreaSoundVolume(4);
                return true;
            /**
             * Area 1.
             */
            case 31164:
                player.getSettings().setAreaSoundVolume(3);
                return true;
            /**
             * Area 2.
             */
            case 31168:
                player.getSettings().setAreaSoundVolume(2);
                return true;
            /**
             * Area 3.
             */
            case 31172:
                player.getSettings().setAreaSoundVolume(1);
                return true;
            /**
             * Mouse buttons.
             */
            case 914:
                player.getSettings().toggleMouseButton();
                return true;
            /**
             * Chat effects.
             */
            case 31064:
                player.getSettings().toggleChatEffects();
                return true;
            /**
             * Private chat.
             */
            case 31068:
                player.getSettings().toggleSplitPrivateChat();
                return true;
            /**
             * Profanity filter.
             */
            case 31072:
                player.getConfigManager().set(Configuration.PROFANITY_FILTER,
                    player.getConfigManager().get(Configuration.PROFANITY_FILTER.getId()) == 0 ? 1 : 0, true);
                return true;
            /**
             * Private chat notification timer.
             */
            case 31076:
                player.getConfigManager().set(Configuration.PRIVATE_CHAT_NOTIFICATION_TIMER,
                    player.getConfigManager().get(Configuration.PRIVATE_CHAT_NOTIFICATION_TIMER.getId()) == 0 ? 1 : 0, true);
                return true;
            /**
             * Accept aid.
             */
            case 31014:
                player.getSettings().toggleAcceptAid();
                return true;
            /**
             * Toggle run.
             */
            case 31018:
            case 152:
                player.getSettings().toggleRun();
                return true;
            /**
             * Toggle singlemouse button.
             */
            case 31084:
                player.getSettings().toggleMouseButton();
                break;
            /**
             * Toggle mouse movement.
             */
            case 31088:
                player.getSettings().toggleMouseMovement();
                return true;
            /**
             * Toggle orbs.
             */
            case 31024:
                player.getConfigManager().set(Configuration.LOAD_ORBS,
                    player.getConfigManager().get(Configuration.LOAD_ORBS.getId()) == 0 ? 1 : 0, true);
                return true;
            /**
             * Graphic settings.
             */
            case 31028:
                player.getInterfaceState().open(new Component(23580));
                return true;
            /**
             * Advanced options.
             */
            case 31032:
                player.getInterfaceState().open(new Component(26387));
                return true;
            /**
             * House options.
             */
            case 31022:
                player.getActionSender().sendMessage("Coming soon.");
                //player.getInterfaceState().openSingleTab(new Component(398));
                return true;
            /**
             * Bonds.
             */
            case 31023:
                player.getActionSender().sendMessage("Coming soon.");
                return true;
        }
        return false;
    }
}
