package org.gielinor.game.content.dialogue;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.builder.Builder;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Stan van der Bend on 28/01/2018.
 * project: Gielinor-Server
 * package: org.gielinor.game.content.builder
 */
public class DialogueBuilder implements Builder<DialoguePlugin> {

    private final DialogueLayout type;
    private final String[]
        dialogue,
        items = new String[3];

    private String optionTitle = "Choose an option.";

    private DialogueOptionHandler optionHandler;
    private FacialExpression expression;
    private DialogueBuilder previous, next;

    private int npcId = -1;

    /**
     * Create a new {@link DialogueBuilder}
     *
     * @param type  The {@link DialogueLayout} of the generated {@link DialoguePlugin}.
     */
    public DialogueBuilder(DialogueLayout type) {
        this.expression = FacialExpression.NO_EXPRESSION;
        this.type = type;
        this.dialogue = new String[type.equals(DialogueLayout.OPTION) ? 5 : 4];
        if(type.equals(DialogueLayout.OPTION))
            this.optionHandler = new DialogueOptionHandler();
    }

    private DialogueBuilder(DialogueLayout type, DialogueBuilder dialogueBuilder) {
        this(type);
        this.previous = dialogueBuilder;
        this.npcId = previous.npcId;
        if(type.equals(DialogueLayout.NPC_STATEMENT)){
            this.expression = previous.expression;
        }
    }
    /**
     * Creates a new {@link DialoguePlugin} and starts it in through the {@link DialogueInterpreter}.
     *
     */
    public void start(Player player) {
        if(Objects.isNull(previous))
            player.getDialogueInterpreter().setDialogue(build());
        else previous.start(player);
    }

    public static DialogueBuilder create(DialogueLayout dialogueLayout){
        return new DialogueBuilder(dialogueLayout);
    }

    public DialogueBuilder setNpcChatHead(int id){
        this.npcId = id;
        return this;
    }

    public DialogueBuilder setExpression(FacialExpression expression){
        this.expression = expression;
        return this;
    }

    public DialogueBuilder setItem(int itemId, int amount){
        items[0] = String.valueOf(itemId);
        items[1] = String.valueOf(amount);
        items[2] = TextUtils.formatDisplayName(ItemDefinition.forId(itemId).getName());
        return this;
    }

    public static Consumer<Player> addInputAfterOption(String headerText, BiConsumer<Player, Object> playerInputConsumer){
        return plr -> {

            plr.getDialogueInterpreter().getDialogue().end();
            plr.getDialogueInterpreter().sendInput(false, headerText);
            plr.setAttribute("runscript", new RunScript() {
                @Override
                public boolean handle() {
                    playerInputConsumer.accept(plr, getValue());
                    return true;
                }
            });
        };
    }

    public DialogueBuilder setDialogueText(String... lines){
        for (int i = 0; i < dialogue.length; i++)
            if(i < lines.length && Objects.isNull(dialogue[i])){
                dialogue[i] = lines[i];
            }
        return this;
    }

    public DialogueBuilder setNext(DialogueBuilder next) {
        this.next = next;
        return this;
    }

    public DialogueBuilder add(){
        DialogueBuilder next = new DialogueBuilder(type,this);
        setNext(next);
        return next;
    }
    public DialogueBuilder add(DialogueLayout type){
        DialogueBuilder next = new DialogueBuilder(type, this);
        setNext(next);
        return next;
    }
    public DialogueBuilder add(DialogueBuilder builder) {
        setNext(builder);
        return this;
    }

    public DialogueBuilder setOptionTitle(String title){
        optionTitle = title;
        return this;
    }
    private DialogueBuilder option(int index, String text, Consumer<Player> consumer) {
        optionHandler.addAction(index, consumer);
        if(index < dialogue.length)
            dialogue[index] = text;
        return this;
    }

    public DialogueBuilder firstOption(String text, Consumer<Player> consumer) {
        return option(0, text, consumer);
    }
    public DialogueBuilder secondOption(String text, Consumer<Player> consumer) {
        return option(1, text, consumer);
    }
    public DialogueBuilder thirdOption(String text, Consumer<Player> consumer) {
        return option(2, text, consumer);
    }
    public DialogueBuilder fourthOption(String text, Consumer<Player> consumer) {
        return option(3, text, consumer);
    }
    public DialogueBuilder fifthOption(String text, Consumer<Player> consumer) {
        return option(4, text, consumer);
    }

    public DialogueBuilder addCancel(String customText){
        int validDialogues = Math.toIntExact(Arrays.stream(dialogue).filter(Objects::nonNull).count());

        if(validDialogues >= dialogue.length)
            validDialogues = dialogue.length;

        return option(validDialogues, customText, player1 -> player1.getInterfaceState().close());
    }
    public DialogueBuilder addCancel(){
        return addCancel("Never mind.");
    }

    public DialoguePlugin build() {
        return new DialoguePlugin() {

            @Override
            public DialoguePlugin newInstance(Player player) {
                return build();
            }

            @Override
            public boolean open(Object... args) {

                if(type.equals(DialogueLayout.OPTION))
                    optionHandler.setPlayer(player);

                switch (type){
                    case OPTION:
                        player.getDialogueInterpreter().sendOptions(optionTitle, dialogue);
                        break;
                    case STATEMENT:
                        player.getDialogueInterpreter().sendPlaneMessage(dialogue);
                        break;
                    case NPC_STATEMENT:
                        player.getDialogueInterpreter().sendDialogues(npcId, expression, dialogue);
                        break;
                    case ITEM_STATEMENT:
                        player.getDialogueInterpreter().sendItemSelectDialogue(items);
                        break;
                    case PLAYER_STATEMENT:
                        player.getDialogueInterpreter().sendDialogues(player, expression, dialogue);
                        break;
                }
                return true;
            }

            @Override
            public int[] getIds() {
                return new int[0];
            }

            @Override
            public boolean handle(int interfaceId, int buttonId) {
                if(Objects.nonNull(getNext())){
                    getNext().start(player);
                    return true;
                }
                return type.equals(DialogueLayout.OPTION) ? optionHandler.handleButton(buttonId) : super.handle(interfaceId, buttonId);
            }
        };
    }

    public DialogueBuilder getPrevious() {
        return previous;
    }
    public DialogueBuilder getNext() {
        return next;
    }
    public DialogueLayout getType() {
        return type;
    }

    public DialogueBuilder getNextOption() {

        final DialogueBuilder next = getNext();

        while (Objects.nonNull(next))
        {
            if(next.getType().equals(DialogueLayout.OPTION))
                return next;
        }
        return this;
    }
}
