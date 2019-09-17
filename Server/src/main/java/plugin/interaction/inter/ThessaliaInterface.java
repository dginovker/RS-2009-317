package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the interface plugin to handle thessalia interfaces.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ThessaliaInterface extends ComponentPlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 500);

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ComponentDefinition.put(4731, this);
        ComponentDefinition.put(3038, this);
        ComponentDefinition.put(0, this);
        ComponentDefinition.put(2851, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (component.getId()) {
            case 2851:
                switch (button) {
                    case 3010:
                    case 3011:
                        player.setAttribute("newBody", button - 2992);
                        return true;
                    case 3012:
                    case 3014:
                    case 3016:
                        player.setAttribute("newBody", button - 2991);
                        return true;
                    case 3013:
                    case 3015:
                        player.setAttribute("newBody", button - 2993);
                        return true;
                    case 3017:
                    case 3018:
                    case 3019:
                    case 3020:
                    case 3021:
                    case 3022:
                        player.setAttribute("newArms", button - 2991);
                        return true;
                    case 2943:
                    case 2944:
                    case 2945:
                    case 2946:
                    case 2947:
                    case 2948:
                    case 2949:
                    case 2950:
                    case 2951:
                    case 2952:
                    case 2953:
                    case 2954:
                    case 2955:
                    case 2956:
                    case 2957:
                    case 2958:
                        player.setAttribute("newBodyColour", button - 2943);
                        return true;
                    case 2959:
                        if (!player.getInventory().containsItem(COINS)) {
                            return true;
                        }
                        if (player.getInventory().remove(COINS)) {
                            if (player.getAttribute("newBody") != null) {
                                player.getAppearance().getTorso().setLook(player.getAttribute("newBody"));
                            }
                            if (player.getAttribute("newArms") != null) {
                                player.getAppearance().getArms().setLook(player.getAttribute("newArms"));
                            }
                            if (player.getAttribute("newBodyColour") != null) {
                                player.getAppearance().getTorso().setColour(player.getAttribute("newBodyColour"));
                            }
                            player.getAppearance().sync();
                            player.getInterfaceState().close();
                            player.getDialogueInterpreter().open(548, true, true);
                        }
                        return true;
                }
                return false;
            case 0://male leg
                switch (button) {
                    case 130:
                    case 131:
                    case 132:
                    case 133:
                        player.setAttribute("newLegs", button - 94);
                        return true;
                    case 107:
                        player.setAttribute("newLegsColour", button - 107);
                        return true;
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                        player.setAttribute("newLegsColour", button - 91);
                        return true;
                    case 108:
                        if (player.getInventory().remove(COINS)) {
                            if (player.getAttribute("newLegs") != null) {
                                player.getAppearance().getLegs().setLook((Integer) player.getAttribute("newLegs"));
                            }
                            if (player.getAttribute("newLegsColour") != null) {
                                player.getAppearance().getLegs().setColour((Integer) player.getAttribute("newLegsColour"));
                            }
                            player.getAppearance().sync();
                            player.getInterfaceState().close();
                            player.getDialogueInterpreter().open(548, true, true);
                        }
                        return true;
                }
                return false;
            case 3038:
                switch (button) {
                    case 3186:
                    case 3187:
                    case 3188:
                    case 3189:
                    case 3190:
                        player.setAttribute("newBody", button - 3130);
                        return true;
                    case 3191:
                    case 3192:
                    case 3193:
                    case 3194:
                    case 3195:
                        player.setAttribute("newArms", button - 3130);
                        return true;
                    case 3130:
                    case 3131:
                    case 3132:
                    case 3133:
                    case 3134:
                    case 3135:
                    case 3136:
                    case 3137:
                    case 3138:
                    case 3139:
                    case 3140:
                    case 3141:
                    case 3142:
                    case 3143:
                    case 3144:
                    case 3145:
                        player.setAttribute("newBodyColour", button - 3130);
                        return true;
                    case 3146:
                        if (player.getInventory().remove(COINS)) {
                            if (player.getAttribute("newBody") != null) {
                                player.getAppearance().getTorso().setLook((Integer) player.getAttribute("newBody"));
                            }
                            if (player.getAttribute("newArms") != null) {
                                player.getAppearance().getArms().setLook((Integer) player.getAttribute("newArms"));
                            }
                            if (player.getAttribute("newBodyColour") != null) {
                                player.getAppearance().getTorso().setColour((Integer) player.getAttribute("newBodyColour"));
                            }
                            player.getAppearance().sync();
                            player.getInterfaceState().close();
                            player.getDialogueInterpreter().open(548, true, true);
                        }
                        return true;
                }
                return false;
            case 4731:
                switch (button) {
                    case 4865:
                    case 4866:
                    case 4867:
                    case 4868:
                    case 4869:
                        player.setAttribute("newLegs", button - 4795);
                        return true;
                    case 4871:
                    case 4872:
                        player.setAttribute("newLegs", button - 4795);
                        return true;
                    case 4838:
                        player.setAttribute("newLegsColour", button - 4838);
                        return true;
                    case 4823:
                    case 4824:
                    case 4825:
                    case 4826:
                    case 4827:
                    case 4828:
                    case 4829:
                    case 4830:
                    case 4831:
                    case 4832:
                    case 4833:
                    case 4834:
                    case 4835:
                    case 4836:
                    case 4837:
                        player.setAttribute("newLegsColour", button - 4822);
                        return true;
                    case 4839:
                        if (player.getInventory().remove(COINS)) {
                            if (player.getAttribute("newLegs") != null) {
                                player.getAppearance().getLegs().setLook((Integer) player.getAttribute("newLegs"));
                            }
                            if (player.getAttribute("newLegsColour") != null) {
                                player.getAppearance().getLegs().setColour((Integer) player.getAttribute("newLegsColour"));
                            }
                            player.getAppearance().sync();
                            player.getInterfaceState().close();
                            player.getDialogueInterpreter().open(548, true, true);
                        }
                        return true;
                }
                return false;
        }
        return false;
    }
}