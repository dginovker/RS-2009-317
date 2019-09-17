package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the mage of zamorak plugin dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MageOfZamorakPlugin extends DialoguePlugin {

    /**
     * Represents the animation to use for the zammy mage.
     */
    private static final Animation ANIMATION = new Animation(1979);

    /**
     * Represents the graphics used for the zammy mage.
     */
    private static final Graphics GRAPHICS = new Graphics(4);

    /**
     * Constructs a new {@code MageOfZamorakPlugin} {@code Object}.
     */
    public MageOfZamorakPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MageOfZamorakPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public MageOfZamorakPlugin(Player player) {
        super(player);
    }

    /**
     * Represents the teleporting to the abyss.
     *
     * @param player the player.
     */
    public static void teleport(final Player player, NPC npc) {
        player.lock(3);
        npc.animate(ANIMATION);
        npc.graphics(GRAPHICS);
        npc.sendChat("Veniens! Sallakar! Rinnesset!");
        player.getSkills().decrementPrayerPoints(100);
        player.getSkullManager().checkSkull(player);
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                player.getProperties().setTeleportLocation(Location.create(3021, 4847, 0));
                return true;
            }
        });
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MageOfZamorakPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "How can I help a fellow Zamorak follower?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Can you teleport me to the abyss?", "Can  I see your shop?", "I'm not a Zamorak follower!");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you teleport me to the abyss?");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I see your shop?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm not a Zamorak follower!");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, null, "Yes I can.");
                stage = 11;
                break;
            case 11:
                end();
                teleport(player, npc);
                break;
            case 20:
                end();
                // TODO 317
                //	Shops.BATTLE_RUNES.open(player);
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.ANGRY, "Then get out of my sight!");
                stage = 31;
                break;
            case 31:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2259 };
    }
}
