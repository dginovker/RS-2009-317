package plugin.zone.rellekka;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The dialogue plugin for Lokar Searunner.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class LokarSearunnerDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link LokarSearunnerDialogue} {@link DialoguePlugin}.
     */
    public LokarSearunnerDialogue() {
    }

    /**
     * Constructs a new {@link LokarSearunnerDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public LokarSearunnerDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LokarSearunnerDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (hasRequirements(player)) {
            if (player.getInventory().contains(9083) || player.getEquipment().contains(9083)) {
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Would you like to visit Lunar Isle?", "Make sure you have your Seal of Passage on you.");
                stage = 1000;
                return true;
            }
        }
        interpreter.sendDialogues(npc, FacialExpression.ANGRY, "Don't talk to me outerlander!", "I don't have time for the likes of you!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1000:
                options("Yes, please.", "No thank you.");
                stage = 1001;
                break;
            case 1001:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, please.", "I have my Seal of Passage on me.");
                        stage = 1002;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case 1002:
                if (player.getInventory().contains(9083) || player.getEquipment().contains(9083)) {
                    player.getInterfaceState().close();
                    player.lock(7);
                    World.submit(new Pulse(2) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            this.setDelay(1);
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 3:
                                    player.getProperties().setTeleportLocation(Location.create(2111, 3915, 0));
                                    break;
                                case 5:
                                    player.unlock();
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                    interpreter.sendPlaneMessage("You arrive at Lunar Isle.", "You should keep your Seal of Passage on you at all times whilst here.");
                                    stage = END;
                                    return true;
                            }
                            return false;
                        }
                    });
                    break;
                }
                npc("You need a Seal of Passage!", "Talk to Brundt the Chieftain to get one.");
                stage = END;
                break;
            case 0:
                if (!hasRequirements(player)) {
                    interpreter.sendPlaneMessage(false, "You need the following stats to travel to Lunar Island:", "61 Crafting, 40 Defence, 49 Firemaking, 5 Herblore,", "65 Magic, 60 Mining and 55 Woodcutting");
                    stage = END;
                    break;
                }
                player("How rude.. Maybe I should talk to Brundt...");
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    /**
     * Checks if the player has the requirements to start this quest.
     *
     * @return Whether or not they meet the requirements.
     */
    public static boolean hasRequirements(Player player) {
        return (player.getSkills().getStaticLevel(Skills.CRAFTING) >= 61 &&
            player.getSkills().getStaticLevel(Skills.DEFENCE) >= 40 &&
            player.getSkills().getStaticLevel(Skills.FIREMAKING) >= 49 &&
            player.getSkills().getStaticLevel(Skills.HERBLORE) >= 5 &&
            player.getSkills().getStaticLevel(Skills.MAGIC) >= 65 &&
            player.getSkills().getStaticLevel(Skills.MINING) >= 60 &&
            player.getSkills().getStaticLevel(Skills.WOODCUTTING) >= 55);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4536, 4537 };
    }
}
