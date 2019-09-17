package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.trade.TradeModule;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the interface plugin used to handle all trade related functions.
 *
 * @author 'Vexia
 */
public final class TradeInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(3323, this);
        ComponentDefinition.put(335, this);
        ComponentDefinition.put(336, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, final int slot, int itemId) {
        final TradeModule module = player.getExtension(TradeModule.class);
        if (module == null) {
            return false;
        }
        switch (component.getId()) {
            /**
             * First stage inventory.
             */
            case 3322:
                switch (opcode) {
                    /**
                     * Offer 1.
                     */
                    case OperationCode.OPTION_OFFER_ONE:
                        module.getContainer().offer(slot, 1);
                        return true;
                    /**
                     * Offer 5.
                     */
                    case OperationCode.OPTION_OFFER_FIVE:
                        module.getContainer().offer(slot, 5);
                        return true;
                    /**
                     * Offer 10.
                     */
                    case OperationCode.OPTION_OFFER_TEN:
                        module.getContainer().offer(slot, 10);
                        return true;
                    /**
                     * Offer All.
                     */
                    case OperationCode.OPTION_OFFER_ALL:
                        module.getContainer().offer(slot, player.getInventory().getCount(player.getInventory().get(slot)));
                        return true;
                    /**
                     * Offer X.
                     */
                    case OperationCode.OPTION_OFFER_X:
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                module.getContainer().offer(slot, (int) getValue());
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                        return true;
                }
                /**
                 * First stage trade.
                 */
            case 3415:
                switch (opcode) {
                    /**
                     * Remove 1.
                     */
                    case OperationCode.OPTION_OFFER_ONE:
                        module.getContainer().withdraw(slot, 1);
                        return true;
                    /**
                     * Remove 5.
                     */
                    case OperationCode.OPTION_OFFER_FIVE:
                        module.getContainer().withdraw(slot, 5);
                        return true;
                    /**
                     * Remove 10.
                     */
                    case OperationCode.OPTION_OFFER_TEN:
                        module.getContainer().withdraw(slot, 10);
                        return true;
                    /**
                     * Remove All.
                     */
                    case OperationCode.OPTION_OFFER_ALL:
                        module.getContainer().withdraw(slot, module.getContainer().getCount(module.getContainer().get(slot)));
                        return true;
                    /**
                     * Remove X.
                     */
                    case OperationCode.OPTION_OFFER_X:
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                module.getContainer().withdraw(slot, (int) getValue());
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                        return true;
                }
                return false;
            default:
                switch (opcode) {
                    case 185:
                        /**
                         * Accept first.
                         */
                        if (button == 3420) {
                            module.setAccepted(true, true);
                            return true;
                        }
                        /**
                         * Accept second.
                         */
                        if (button == 3546 && component.getId() == 3443) {
                            module.setAccepted(true, true);
                            return true;
                        }
                        return false;
                }
        }
        return false;
    }
}
