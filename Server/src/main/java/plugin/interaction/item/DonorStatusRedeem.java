package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.string.TextUtils;

/**
 * The {@link org.gielinor.game.interaction.OptionHandler} for redeeming donor status.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DonorStatusRedeem extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(Item.DONOR_STATUS).getConfigurations().put("option:redeem", this);
        ItemDefinition.forId(Item.SUPER_DONOR_STATUS).getConfigurations().put("option:redeem", this);
        ItemDefinition.forId(Item.EXTREME_DONOR_STATUS).getConfigurations().put("option:redeem", this);
        PluginManager.definePlugin(new DonorRedeemDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        DonorStatus donorStatus = DonorStatus.Companion.forItemId(node.getId());
        if (donorStatus == null) {
            return false;
        }
        return player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("donor-redeem"), donorStatus);
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for redeeming donor status.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class DonorRedeemDialogue extends DialoguePlugin {

        /**
         * The {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus} for the item.
         */
        private DonorStatus donorStatus;

        public DonorRedeemDialogue() {
        }

        public DonorRedeemDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new DonorRedeemDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            stage = 0;
            donorStatus = (DonorStatus) args[0];
            if (donorStatus == null) {
                return false;
            }
            interpreter.sendPlaneMessage(false, "<col=8A0808>Warning!", "If you redeem this status, the item will be lost forever.", "Are you sure you want to do this?");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes, redeem the status.", "No, I wish to keep it.");
                    stage = 1;
                    break;
                case 1:
                    if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                        end();
                        return true;
                    }
                    String statusName = donorStatus.name().toLowerCase().replaceAll("_", " ");
                    String playerStatus = player.getDonorManager().getDonorStatus().name().toLowerCase().replaceAll("_", " ");
                    if (player.getDonorManager().getDonorStatus().ordinal() >= donorStatus.ordinal()) {
                        interpreter.sendPlaneMessage(false, "You cannot redeem this, as you are already a" +
                            (TextUtils.isPlusN(playerStatus) ? "n" : "") + " <col=" + player.getDonorManager().getDonorStatus().getColor() + ">"
                            + playerStatus + "</col>.");
                        stage = END;
                        break;
                    }
                    if (player.getInventory().remove(new Item(donorStatus.getItemId()))) {
                        player.getDonorManager().setDonorStatus(donorStatus);
                        interpreter.sendPlaneMessage(false, "Congratulations! You are now a" + (TextUtils.isPlusN(donorStatus.name()) ? "n" : "")
                            + " <col=" + donorStatus.getColor() + ">" + statusName + "</col>!");
                        stage = 2;
                        break;
                    }
                    end();
                    break;
                case 2:
                    interpreter.sendPlaneMessage(false, "See the website for additional benefits that", "you have unlocked!");
                    stage = END;
                    break;
                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("donor-redeem") };
        }
    }
}
