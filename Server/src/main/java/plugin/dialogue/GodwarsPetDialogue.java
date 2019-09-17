package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for God wars pets.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GodwarsPetDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code GodwarsPetDialogue} {@code Object}.
     */
    public GodwarsPetDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GodwarsPetDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GodwarsPetDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GodwarsPetDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        // TODO Hide familiar context menu via client if not player pet
        if (!(npc instanceof Familiar)) {
            return false;
        }
        final Familiar f = (Familiar) npc;
        if (f.getOwner() != player) {
            player.getActionSender().sendMessage("This is not your follower.");
            return true;
        }
        switch (npc.getId()) {
            case NPCDefinition.GENERAL_GRAARDOR_JR:
                player("Not sure this is going to be worth my time but...", "how are you?");
                break;
            case NPCDefinition.KRIL_TSUTSAROTH_JR:
                player("How's life in the light?");
                break;
            case NPCDefinition.KREE_ARRA_JR:
                player("Huh... that's odd... I thought that would be big news.");
                break;
            case NPCDefinition.ZILYANA_JR:
                if (player.getInventory().contains(11698) || player.getEquipment().contains(11698)) {
                    player("I FOUND THE GODSWORD!");
                    stage = 6;
                    return true;
                }
                player("FIND THE GODSWORD!");
                break;
        }
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (npc.getId()) {
                    case NPCDefinition.GENERAL_GRAARDOR_JR:
                        npc("SFudghoigdfpDSOPGnbSOBNfdbdnopbdnopbddfnopdfpofhd", "ARRRGGGGH");
                        break;
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        npc("Burns slightly.");
                        break;
                    case NPCDefinition.KREE_ARRA_JR:
                        npc("You thought what would be big news?");
                        break;
                    case NPCDefinition.ZILYANA_JR:
                        npc("FIND THE GODSWORD!");
                        stage = END;
                        break;
                }
                stage = 1;
                break;
            case 1:
                switch (npc.getId()) {
                    case NPCDefinition.GENERAL_GRAARDOR_JR:
                        player("Nope. Not worth it.");
                        stage = END;
                        return true;
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        player("You seem much nicer than your father. He's mean.");
                        stage = 2;
                        return true;
                    case NPCDefinition.KREE_ARRA_JR:
                        player("Well there seems to be an absence of a certain", "ornithological piece: a headline regarding mass awareness", "of a certain avian variety.");
                        stage = 2;
                        return true;
                }
                end();
                break;
            case 2:
                switch (npc.getId()) {
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        npc("If you were stuck in a very dark cave for centuries", "you'd be pretty annoyed too.");
                        stage = 3;
                        return true;
                    case NPCDefinition.KREE_ARRA_JR:
                        npc("What are you talking about?");
                        stage = 4;
                        break;
                }
                break;
            case 3:
                switch (npc.getId()) {
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        player("I guess.");
                        stage = 4;
                        return true;
                    case NPCDefinition.KREE_ARRA_JR:
                        player("Oh have you not heard?", "It was my understanding that everyone had heard....");
                        stage = 5;
                        return true;
                }
                break;
            case 4:
                switch (npc.getId()) {
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        npc("He's actually quite mellow really.");
                        stage = 5;
                        return true;
                    case NPCDefinition.KREE_ARRA_JR:
                        npc("Heard wha...... OH NO!!!!?!?!!?!");
                        stage = 5;
                        return true;
                }
                break;
            case 5:
                switch (npc.getId()) {
                    case NPCDefinition.KRIL_TSUTSAROTH_JR:
                        player("Uh.... Yeah.");
                        stage = END;
                        return true;
                    case NPCDefinition.KREE_ARRA_JR:
                        player("OH WELL THE BIRD, BIRD, BIRD, BIRD", "BIRD IS THE WORD. OH WELL THE BIRD,", "BIRD,BIRD, BIRD BIRD IS THE WORD.");
                        stage = END;
                        break;
                }
                break;
            case 6:
                switch (npc.getId()) {
                    case NPCDefinition.ZILYANA_JR:
                        npc("GOOD!!!!!");
                        stage = END;
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ NPCDefinition.GENERAL_GRAARDOR_JR, NPCDefinition.KREE_ARRA_JR, NPCDefinition.KRIL_TSUTSAROTH_JR, NPCDefinition.ZILYANA_JR };
    }
}
