package plugin.zone.wilderness;

import org.gielinor.game.content.dialogue.DialogueBuilder;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

import static org.gielinor.game.content.dialogue.DialogueLayout.*;

/**
 * Created by Stan van der Bend on 29/01/2018.
 *
 * project: Gielinor-Server
 * package: plugin.zone.wilderness
 */
public class ResourceArea extends MapZone implements Plugin {

    private final static String NAME = "Resource Area";

    private final static int MANDRITH_NPC_ID = 6599;
    private final static int FEE_GATE_OBJECT_ID = 26760;
    public final static int PILES_NPC_ID = 13;
    
    public final static int[] OBTAINABLE_ITEM_IDS = new int[]{};

    private final static Item ENTRACE_FEE = new Item(995, 7_500_000);

    public ResourceArea() {
        super(NAME, true);
    }

    @Override
    public void configure() {
        register(new ZoneBorders(3174, 3924, 3196, 3944));
    }

    @Override
    public Plugin newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) { return null; }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if(e instanceof Player) {

            final Player player = e.asPlayer();

            if (target instanceof NPC)
                if (target.asNpc().getId() == MANDRITH_NPC_ID)
                    createMandrithDialogue().start(player);

            if(target instanceof GameObject)
                if(target.asObject().getId() == FEE_GATE_OBJECT_ID)
                    createFeeGateDialogue(target.asObject()).start(player);

        }
        return super.interact(e, target, option);
    }

    private static DialogueBuilder createFeeGateDialogue(GameObject gate) {
        return DialogueBuilder.create(OPTION)
            .setOptionTitle("Entry costs "+ TextUtils.getFormattedNumber(ENTRACE_FEE.getCount())+" "+ENTRACE_FEE.getDefinition().getName())
            .firstOption("Accept", futurePlayer -> {
                if(futurePlayer.getInventory().containsItem(ENTRACE_FEE)) {
                    futurePlayer.getInventory().remove(ENTRACE_FEE);
                    DoorActionHandler.handleDoor(futurePlayer, gate);
                } else
                    futurePlayer.getActionSender().sendMessage("You need " + ENTRACE_FEE.getCount() + "x " + ENTRACE_FEE.getDefinition().getName() + " in order to enter.");

                futurePlayer.getDialogueInterpreter().close();
            })
            .addCancel("Decline");
    }

    private static DialogueBuilder createMandrithDialogue() {
        return DialogueBuilder.create(PLAYER_STATEMENT)
            .setNpcChatHead(MANDRITH_NPC_ID)
            .setDialogueText(
                "Who are you and what is this place?"
            ).add(NPC_STATEMENT).setDialogueText(
                "My name is Mandrith."
            ).add().setDialogueText(
                "I collect valuable resources and pawn off access to them",
                "to foolish adventurers, like yourself."
            ).add().setDialogueText(
                "You should take a look inside my arena. Theres an",
                "abundance of valuable resources inside."
            ).add(PLAYER_STATEMENT).setDialogueText(
                "And I can take whatever I want?"
            ).add(NPC_STATEMENT).setDialogueText(
                "It's all yours. All I ask is you pay the upfront fee."
            ).add(PLAYER_STATEMENT).setDialogueText(
                "Will others be able to kill me inside?"
            ).add(NPC_STATEMENT).setDialogueText(
                "Yes. These walls will only hold them back for so long."
            ).add(PLAYER_STATEMENT).setDialogueText(
                "You'll stop them though, right?"
            ).add(NPC_STATEMENT).setDialogueText(
                "Haha! For the right price, I won't deny any once access",
                "to my arena. Even if their intention is to kill you."
            ).add(PLAYER_STATEMENT).setDialogueText(
                "Right..."
            ).add(NPC_STATEMENT).setDialogueText(
                "My arena holds many treasures that I've acquired at",
                "great expense, adventurer. Their bounty can come at a",
                "price."
            ).add().setDialogueText(
                "One day, adventurer, I will boast ownership of a much",
                "larger, much more dangerous arena than this. Take",
                "advantage of this offer while it lasts."
            );
    }
}
