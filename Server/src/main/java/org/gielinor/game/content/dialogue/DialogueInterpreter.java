package org.gielinor.game.content.dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.impl.Dialogue;
import org.gielinor.game.content.dialogue.impl.DialogueType;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.script.ScriptContext;
import org.gielinor.game.system.script.ScriptManager;
import org.gielinor.game.system.script.context.ItemMessageInstruction;
import org.gielinor.game.system.script.context.NPCDialInstruction;
import org.gielinor.game.system.script.context.OptionDialInstruction;
import org.gielinor.game.system.script.context.PDialInstruction;
import org.gielinor.game.system.script.context.PlainMessageInstruction;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InputDialogueContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InputDialogue;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.plugin.PluginManifest;
import org.gielinor.rs2.plugin.PluginType;
import org.gielinor.utilities.string.TextUtils;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the dialogues.
 *
 * @author Emperor
 */
@PluginManifest(type = PluginType.DIALOGUE)
public final class DialogueInterpreter {

    private static final Logger log = LoggerFactory.getLogger(DialogueInterpreter.class);

    /**
     * The dialogue plugins.
     */
    private static final Map<Integer, DialoguePlugin> PLUGINS = new HashMap<>();

    /**
     * The dialogue scripts.
     */
    private static final Map<Integer, ScriptContext> SCRIPTS = new HashMap<>();

    /**
     * a List of dialogue actions.
     */
    private final List<DialogueAction> actions = new ArrayList<>();

    /**
     * The currently opened dialogue.
     */
    private DialoguePlugin dialogue;

    /**
     * Scripted dialogue current stage.
     */
    private ScriptContext dialogueStage;

    /**
     * The current dialogue key.
     */
    private int key;

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code DialogueInterpreter} {@code Object}.
     *
     * @param player The player.
     */
    public DialogueInterpreter(Player player) {
        this.player = player;
    }

    /**
     * @param dialogue the dialogue to set.
     */
    public void setDialogue(DialoguePlugin dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * Opens the dialogue for the given dialogue type.
     *
     * @param dialogueType The dialogue type.
     * @param args         the args.
     * @return <code>True</code> if successful.
     */
    public boolean open(String dialogueType, Object... args) {
        return open(getDialogueKey(dialogueType), args);
    }

    /**
     * Opens the dialogue for the given NPC id.
     *
     * @param dialogueKey The dialogue key (usually NPC id).
     * @param args        The arguments.
     * @return <code>True</code> if successful.
     */
    public boolean open(int dialogueKey, Object... args) {
        key = dialogueKey;
        if (args.length > 0 && args[0] instanceof NPC) {
            NPC npc = (NPC) args[0];
            npc.setDialoguePlayer(player);
            npc.getWalkingQueue().reset();
            npc.getPulseManager().clear();
        } else if (args.length < 1) {
            args = new Object[]{ dialogueKey };
        }
        ScriptContext script = SCRIPTS.get(dialogueKey);
        if (script != null) {
            Object[] arguments = new Object[args.length + 1];
            System.arraycopy(args, 0, arguments, 1, args.length);
            arguments[0] = player;
            startScript(script, arguments);
            return true;
        }
        DialoguePlugin plugin = PLUGINS.get(dialogueKey);
        if (plugin == null) {
            return false;
        }
        this.dialogue = plugin.newInstance(player);
        if (dialogue == null || !dialogue.open(args)) {
            dialogue = null;
            return false;
        }
        return true;
    }

    /**
     * Handles a dialogue input.
     *
     * @param componentId  The id of the chatbox component.
     * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
     */
    public boolean handle(int componentId, OptionSelect optionSelect) {
        if (dialogueStage != null) {
            dialogueStage = ScriptManager.run(dialogueStage, player, key, optionSelect);
            return dialogueStage instanceof PDialInstruction || dialogueStage instanceof NPCDialInstruction ||
                dialogueStage instanceof OptionDialInstruction || dialogueStage instanceof PlainMessageInstruction ||
                dialogueStage instanceof ItemMessageInstruction;
        }
        if (getDialogue() == null) {
            player.getInterfaceState().closeChatbox();
            return false;
        }
        return player.getDialogueInterpreter().getDialogue().handle(componentId, optionSelect);
    }

    /**
     * Handles an dialogue input.
     *
     * @param componentId The id of the chatbox component.
     * @param buttonId    The button id.
     */
    public boolean handle(int componentId, int buttonId) {
        if (dialogueStage != null) {
            dialogueStage = ScriptManager.run(dialogueStage, player, key, buttonId);
            return dialogueStage instanceof PDialInstruction || dialogueStage instanceof NPCDialInstruction ||
                dialogueStage instanceof OptionDialInstruction || dialogueStage instanceof PlainMessageInstruction ||
                dialogueStage instanceof ItemMessageInstruction;
        }
        if (getDialogue() == null) {
            player.getInterfaceState().closeChatbox();
            return false;
        }
        return player.getDialogueInterpreter().getDialogue().handle(componentId, buttonId);
    }

    /**
     * Closes the current dialogue.
     *
     * @return <code>True</code> if successful.
     */
    public boolean close() {
        if (dialogue != null || dialogueStage != null) {
            actions.clear();
            if (player.getInterfaceState().getChatbox() != null && player.getInterfaceState().getChatbox().getCloseEvent() != null) {
                return true;
            }
            if (dialogueStage != null) {
                dialogueStage = null;
                player.getInterfaceState().closeChatbox();
            }
            if (dialogue != null && dialogue.close()) {
                dialogue = null;
            }
        }
        return dialogue == null && dialogueStage == null;
    }

    /**
     * Puts a dialogue plugin on the mapping.
     *
     * @param id     The NPC id (or {@code 1 << 16 | dialogueId} when the dialogue isn't for an NPC).
     * @param plugin The plugin.
     */
    public static void add(int id, DialoguePlugin plugin) {
        //if (PLUGINS.containsKey(id)) {
        //throw new IllegalArgumentException("Dialogue " + (id & 0xFFFF) + " is already in use - [old=" + PLUGINS.get(id).getClass().getSimpleName() + ", new=" + plugin.getClass().getSimpleName() + "]!");
        //}
        PLUGINS.put(id, plugin);
    }

    /**
     * Adds a dialogue script for the given key.
     *
     * @param dialogueKey The dialogue key.
     * @param context     The dialogue script.
     */
    public static void add(int dialogueKey, ScriptContext context) {
        //if (SCRIPTS.containsKey(dialogueKey)) {
        //throw new IllegalArgumentException("Dialogue " + dialogueKey + " is already in use - [old=" + SCRIPTS.get(dialogueKey).getClass().getSimpleName() + ", new=" + context.getClass().getSimpleName() + "]!");
        //}
        SCRIPTS.put(dialogueKey, context);
    }

    /**
     * Send plane messages based on the amount of specified messages.
     *
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendDialogue(String... messages) {
        return sendPlaneMessage(messages);
    }

    /**
     * Send plane messages based on the amount of specified messages.
     *
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendPlaneMessage(String... messages) {
        if (messages.length < 1 || messages.length > 4) {
            log.warn("This may send [1..4] plane messages. Attempted {}. Ignored request.", messages.length);
            return null;
        }
        int[][][] statementIds = {
            { { 357 }, { 356 } },
            { { 360, 361 }, { 359 } },
            { { 364, 365, 366 }, { 363 } },
            { { 369, 370, 371, 372 }, { 368 } },
            { { 375, 376, 377, 378, 379 }, { 374 } }
        };
        int j1 = messages.length - 1;
        for (int i = 0; i < statementIds[j1][0].length; i++) {
            player.getActionSender().sendString(statementIds[j1][0][i], messages[i]);
        }
        player.getInterfaceState().openChatbox(statementIds[j1][1][0]);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, false);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Represents children ids for parents with "Click here to continue."
     */
    private static final int[][] CHILD_CONTINUE_IDS = new int[][]{
        { 250, 254 }, { 306, 309 }, { 310, 314 }, { 315, 319 },
        { 321, 325 }, { 356, 358 }, { 359, 362 }, { 363, 367 },
        { 368, 373 }, { 374, 380 }, { 968, 972 }, { 973, 978 },
        { 979, 985 }, { 986, 993 }, { 4261, 4265 }, { 4267, 4270 },
        { 4272, 4275 }, { 4277, 4280 }, { 4282, 4285 }, { 4429, 4440 },
        { 4882, 4886 }, { 4887, 4892 }, { 4893, 4899 }, { 4900, 4907 },
        { 4950, 4954 }, { 5706, 5709 }, { 5710, 5714 }, { 5842, 5846 },
        { 5848, 5852 }, { 6103, 6147 }, { 6206, 6209 }, { 6211, 6214 },
        { 6216, 6219 }, { 6221, 6224 }, { 6226, 6229 }, { 6231, 6234 },
        { 6237, 6240 }, { 6242, 6245 }, { 6247, 6250 }, { 6253, 6256 },
        { 6258, 6261 }, { 6263, 6266 }, { 11859, 11862 }, { 11864, 11868 },
        { 11870, 11874 }, { 12122, 12125 }, { 13929, 13932 }, { 22535, 22563 }
    };

    /**
     * Gets the child id for "Click here to continue".
     *
     * @param id The id of the chatbox.
     * @return The child id.
     */
    public static int getChildId(int id) {
        for (int[] childContinueIds : CHILD_CONTINUE_IDS) {
            if (childContinueIds[0] == id) {
                return childContinueIds[1];
            }
        }
        return 0;
    }

    /**
     * Sends a plane message and hides the continue button.
     *
     * @param hideContinue if we should hide it or not.
     * @param messages     the messages.
     * @return the component.
     */
    public Component sendPlaneMessage(final boolean hideContinue, String... messages) {
        sendPlaneMessage(messages);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, hideContinue ? "" : "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, hideContinue);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send a message stating a minimum requirement skill level.
     *
     * @param skill  The skill name.
     * @param level  The minimum level required.
     * @param action The action to be done with the level.
     * @return The chatbox component.
     */
    public Component sendSkillMessage(String skill, int level, String action) {
        return sendPlaneMessage("You need a " + TextUtils.uppercaseFirst(skill) + " of at least " + level + " to " + action + ".");
    }

    /**
     * Opens the destroy item chatbox interface.
     *
     * @param id The item id.
     * @return The component.
     */
    public Component sendDestroyItem(int id) {
        player.getInterfaceState().openChatbox(14170);
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 14171, 0, 0, new Item[]{ new Item(id) }, 1, false));
        String text = ItemDefinition.forId(id).getConfiguration(ItemConfiguration.DESTROY_MESSAGE, "Are you sure you want to destroy this object?");
        if (text.length() > 60) {
            String[] words = text.split(" ");
            StringBuilder sb = new StringBuilder(words[0]);
            for (int i = 1; i < words.length; i++) {
                if (i == (words.length / 2)) {
                    sb.append("<br>");
                } else {
                    sb.append(" ");
                }
                sb.append(words[i]);
            }
            text = sb.toString();
        }
        String[] messages = new String[]{ text, "", "" };
        if (text.contains("<br>")) {
            messages = new String[text.split("<br>").length];
            for (int i = 0; i < text.split("<br>").length; i++) {
                messages[i] = text.split("<br>")[i];
            }
        }
        switch (messages.length) {
            case 1:
                player.getActionSender().sendString("", 14177);
                player.getActionSender().sendString("", 14182);
                player.getActionSender().sendString(messages[0], 14183);
                break;
            case 2:
                player.getActionSender().sendString(messages[0], 14182);
                player.getActionSender().sendString(messages[1], 14183);
                player.getActionSender().sendString("", 14177);
                break;
            case 3:
                player.getActionSender().sendString(messages[0], 14177);
                player.getActionSender().sendString(messages[1], 14182);
                player.getActionSender().sendString(messages[2], 14183);
                break;
            default:
                player.getActionSender().sendString("", 14177);
                player.getActionSender().sendString("", 14182);
                player.getActionSender().sendString("Once you've clicked yes you will not be able to get this item back.", 14183);
                break;
        }
        player.getActionSender().sendString(ItemDefinition.forId(id).getName(), 14184);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send plane messages with a blue title.
     *
     * @param title    The title.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendBlueTitleMessage(String title, String... messages) {
        player.getActionSender().sendString(title, 6180);
        for (int childId = 6181; childId < 6185; childId++) {
            player.getActionSender().sendString("", childId);
        }
        for (int i = 0; i < messages.length; i++) {
            player.getActionSender().sendString(messages[i], 6181 + i);
        }
        player.getInterfaceState().openChatbox(6179);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send plane messages with scroll and a blue title. - 22535 = parent id
     *
     * @param title    The title.
     * @param messages The messages.
     * @return The chatbox component.
     */
    public Component sendScrollBlueTitleMessage(String title, String... messages) {
        for (int childId = 22545; childId < 22558; childId++) {
            if (childId == 22547) {
                childId = 22550;
            }
            player.getActionSender().sendString(" ", childId);
        }
        player.getActionSender().sendString(title, 22544);
        int childId = 22545;
        for (String message : messages) {
            if (childId == 22547) {
                childId = 22550;
            }
            player.getActionSender().sendString(message, childId);
            childId++;
        }
        player.getInterfaceState().openChatbox(22535);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Displays an item on an interface for selection, with a name.
     *
     * @param title The title of the item.
     * @param item  The item to display.
     */
    public Component sendItemSelectDialogue(String title, Item item) {
        return sendItemSelectDialogue(new String[]{ title }, item);
    }

    /**
     * Displays items on an interface for selection, with set names
     *
     * @param titles The titles of the items
     * @param items  The items to display
     */
    public Component sendItemSelectDialogue(String[] titles, Item... items) {
        Dialogue dialogue = Dialogue.getDialogue(DialogueType.ITEM_SELECT).get(items.length - 1);
        if (dialogue == null) {
            return null;
        }
        player.getInterfaceState().openChatbox(dialogue.getInterfaceId());
        for (int i = 0; i < items.length; i++) {
            String title = "<br><br><br><br>" + titles[i];
            player.getActionSender().sendString(title, dialogue.getTextIds()[i]);
            player.getActionSender().sendItemOnInterface(items[i].getId(), dialogue.getZooms()[i], dialogue.getModelIds()[i], 0);
        }
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Displays items on an interface for selection
     *
     * @param items The items to display
     */
    public Component sendItemSelectDialogue(Item... items) {
        Dialogue dialogue = Dialogue.getDialogue(DialogueType.ITEM_SELECT).get(items.length - 1);
        if (dialogue == null) {
            return null;
        }
        player.getInterfaceState().openChatbox(dialogue.getInterfaceId());
        for (int i = 0; i < items.length; i++) {
            String title = "<br><br><br><br>" + TextUtils.uppercaseFirst(items[i].getDefinition().getName().replace("Unfired ", ""), true);
            player.getActionSender().sendString(title, dialogue.getTextIds()[i]);
            player.getActionSender().sendItemOnInterface(items[i].getId(), dialogue.getZooms()[i], dialogue.getModelIds()[i], 0);
        }
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send a message with an item next to it.
     *
     * @param itemId   The item id.
     * @param messages The messages.
     */
    public Component sendItemMessage(int itemId, String... messages) {
        Dialogue dialogue = Dialogue.getDialogue(DialogueType.MODEL_LEFT).get(messages.length - 1);
        if (dialogue == null) {
            return null;
        }
        player.getInterfaceState().openChatbox(dialogue.getInterfaceId());
        switch (messages.length) { // fk it, hardcode it
            case 1:
                player.getActionSender().sendString(messages[0], 308);
                break;
            case 2:
                player.getActionSender().sendString(messages[0], 313);
                player.getActionSender().sendString(messages[1], 312);
                break;
            case 3:
                player.getActionSender().sendString(messages[0], 318);
                player.getActionSender().sendString(messages[1], 317);
                player.getActionSender().sendString(messages[2], 320);
                break;
            case 4:
                player.getActionSender().sendString(messages[0], 324);
                player.getActionSender().sendString(messages[1], 323);
                player.getActionSender().sendString(messages[2], 326);
                player.getActionSender().sendString(messages[3], 327);
                break;
        }
        player.getActionSender().sendItemOnInterface(itemId, 200, (dialogue.getInterfaceId() + 1), 0);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send a message with an item next to it.
     *
     * @param item     The item.
     * @param messages The messages.
     */
    public Component sendItemMessage(final Item item, String... messages) {
        return sendItemMessage(item.getId(), messages);
    }

    /**
     * Send a message with an item next to it.
     *
     * @param first   The first item id.
     * @param second  The second item id.
     * @param message The message.
     */
    public Component sendDoubleItemMessage(int first, int second, String message) {
        player.getInterfaceState().openChatbox(250);
        String newMessage = "";
        if (!message.contains("<br>") && TextUtils.getTextWidth(message) > 335) {
            for (String split : Splitter.fixedLength(50).split(message)) {
                newMessage += split + "<br>";
            }
        }
        player.getActionSender().sendString(newMessage.isEmpty() ? message : newMessage, 253);
        player.getActionSender().sendItemOnInterface(first, 0, 251, 0);
        player.getActionSender().sendItemOnInterface(second, 0, 252, 0);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Gets the script context for the given dialogue key.
     *
     * @param key The dialogue key.
     * @return The script context.
     */
    public static ScriptContext getScript(int key) {
        return SCRIPTS.get(key);
    }

    /**
     * Starts a dialogue script.
     *
     * @param script The script.
     * @param args   The arguments.
     */
    public void startScript(ScriptContext script, Object... args) {
        startScript(key, script, args);
    }

    /**
     * Starts a dialogue script.
     *
     * @param dialogueKey The dialogue key.
     * @param script      The script.
     * @param args        The arguments.
     */
    public void startScript(int dialogueKey, ScriptContext script, Object... args) {
        key = dialogueKey;
        (dialogueStage = script).execute(args);
        if (script.isInstant()) {
            dialogueStage = script = ScriptManager.run(script, args);
        }
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param entity     The entity.
     * @param expression The entity's facial expression.
     * @param messages   The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, FacialExpression expression, String... messages) {
        return sendDialogues(entity, expression == null ? -1 : expression.getAnimationId(), messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param entity     The entity.
     * @param expression The entity's facial expression.
     * @param messages   The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, int expression, String... messages) {
        return sendDialogues(entity instanceof Player ? -1 : ((NPC) entity).getShownNPC(player).getId(), expression, messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param npcId        The npc id.
     * @param expression   The entity's facial expression.
     * @param messages     The messages.
     * @param hideContinue the continue.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, FacialExpression expression, boolean hideContinue, String... messages) {
        sendDialogues(npcId, expression == null ? -1 : expression.getAnimationId(), messages);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, hideContinue ? "" : "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, hideContinue);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param entity       The entity.
     * @param expression   The entity's facial expression.
     * @param messages     The messages.
     * @param hideContinue the continue.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, FacialExpression expression, boolean hideContinue, String... messages) {
        sendDialogues(entity, expression, messages);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, hideContinue ? "" : "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, hideContinue);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param entity       The entity.
     * @param expression   The entity's facial expression.
     * @param messages     The messages.
     * @param hideContinue the continue.
     * @return The chatbox component.
     */
    public Component sendDialogues(Entity entity, int expression, boolean hideContinue, String... messages) {
        sendDialogues(entity, expression, messages);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, hideContinue ? "" : "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, hideContinue);
        return player.getInterfaceState().getChatbox();
    }


    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param npcId      The npc id.
     * @param expression The entity's facial expression.
     * @param messages   The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, int expression, boolean hideContinue, String... messages) {
        sendDialogues(npcId, expression, messages);
        int childId = getChildId(player.getInterfaceState().getChatbox().getId());
        if (childId != -1) {
            player.getActionSender().sendString(childId, hideContinue ? "" : "Click here to continue");
        }
        player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, hideContinue);
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param npcId      The npc id.
     * @param expression The entity's facial expression.
     * @param messages   The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, FacialExpression expression, String... messages) {
        return sendDialogues(npcId, expression == null ? -1 : expression.getAnimationId(), messages);
    }

    /**
     * Send dialogues based on the amount of specified messages.
     *
     * @param npcId      The npc id.
     * @param expression The entity's facial expression.
     * @param messages   The messages.
     * @return The chatbox component.
     */
    public Component sendDialogues(int npcId, int expression, String... messages) {
        if (messages.length < 1 || messages.length > 4) {
            log.warn("This may send [1..4] dialogue messages. Attempted {}. Ignored request.", messages.length);
            return null;
        }
        boolean npc = npcId > -1;
        if (npc) {
            int[][][] npcIds = {// 0 = animation, 1 = name, 2 = messages, 3 = inter id
                { { 4883 }, { 4884 }, { 4885 }, { 4882 } },
                { { 4888 }, { 4889 }, { 4890, 4891 }, { 4887 } },
                { { 4894 }, { 4895 }, { 4896, 4897, 4898 }, { 4893 } },
                { { 4901 }, { 4902 }, { 4903, 4904, 4905, 4906 }, { 4900 } }
            };
            NPCDefinition npcDef = NPCDefinition.forId(npcId);
            int j2 = messages.length - 1;
            if (expression == -1) {
                expression = FacialExpression.NO_EXPRESSION.getAnimationId();
            }
            player.getActionSender().sendAnimationInterface(expression, npcIds[j2][0][0], 0);

            player.getActionSender().sendString(npcDef.getName(), npcIds[j2][1][0]);
            for (int i = 0; i < npcIds[j2][2].length; i++) {
                player.getActionSender().sendString(messages[i], npcIds[j2][2][i]);
            }
            player.getActionSender().sendNpcOnInterface(npcId, npcIds[j2][0][0], 0);
            player.getInterfaceState().openChatbox(npcIds[j2][3][0]);
        } else {
            int[][][] playerIds = {
                { { 969 }, { 970 }, { 971 }, { 968 } },
                { { 974 }, { 975 }, { 976, 977 }, { 973 } },
                { { 980 }, { 981 }, { 982, 983, 984 }, { 979 } },
                { { 987 }, { 988 }, { 989, 990, 991, 992 }, { 986 } }
            };
            int j2 = messages.length - 1;
            if (expression == -1) {
                expression = FacialExpression.NO_EXPRESSION.getAnimationId();
            }
            player.getActionSender().sendAnimationInterface(expression, playerIds[j2][0][0], 0);

            player.getActionSender().sendString(TextUtils.formatDisplayName(player.getUsername()), playerIds[j2][1][0]);
            for (int i = 0; i < playerIds[j2][2].length; i++) {
                player.getActionSender().sendString(messages[i], playerIds[j2][2][i]);
            }
            player.getActionSender().sendPlayerOnInterface(playerIds[j2][0][0], 0);
            player.getInterfaceState().openChatbox(playerIds[j2][3][0]);
        }
        if (player.getInterfaceState().getChatbox() != null) {
            int childId = getChildId(player.getInterfaceState().getChatbox().getId());
            if (childId != -1) {
                player.getActionSender().sendString(childId, "Click here to continue");
            }
            player.getActionSender().sendHideComponent(childId, childId == -1 ? 0 : -1, false);
        }
        return player.getInterfaceState().getChatbox();
    }

    /**
     * Send options based on the amount of specified options.
     *
     * @param title   The title.
     * @param options The options.
     */
    public void sendOptions(Object title, String... options) {
        if (options.length < 2 || options.length > 5) {
            log.warn("This may send [2..5] options. Attempted {}. Ignored request.", options.length);
            return;
        }
        /*
         * Two options (item?) with custom title
         * 144 = Yes
         * 145 = No
         * 146 = Select an Option
         */
        int[][][] optionIds = {
            { { 2460 }, { 2461, 2462 }, { 2459 } },
            { { 2470 }, { 2471, 2472, 2473 }, { 2469 } },
            { { 2481 }, { 2482, 2483, 2484, 2485 }, { 2480 } },
            { { 2493 }, { 2494, 2495, 2496, 2497, 2498 }, { 2492 } }
        };
        int j = options.length - 2;
        String newTitle = title == null ? "Select an Option" : (String) title;
        player.getActionSender().sendString(newTitle, optionIds[j][2][0] + 1);
        for (int i = 0; i < optionIds[j][1].length; i++) {
            player.getActionSender().sendString(options[i], optionIds[j][1][i]);
        }
        player.getInterfaceState().openChatbox(optionIds[j][2][0]);
    }

    /**
     * Send a input run script.
     *
     * @param string The strings.
     * @param object The question.
     */
    public void sendInput(boolean string, Object object) {
        String context = String.valueOf(object);
        PacketRepository.send(InputDialogue.class, new InputDialogueContext(player, context, !string));
    }

    public void sendPWInput(boolean string, String object) {
        String context = String.valueOf(object);
        PacketRepository.send(InputDialogue.class, new InputDialogueContext(player, context, !string));
    }

    /**
     * Checks if the dialogue for the given id is added.
     *
     * @param id The NPC id/dialogue id.
     * @return <code>True</code> if so.
     */
    public static boolean contains(int id) {
        return PLUGINS.containsKey(id);
    }

    /**
     * Gets the currently opened dialogue.
     *
     * @return The dialogue plugin.
     */
    public DialoguePlugin getDialogue() {
        return dialogue;
    }

    /**
     * Reserves a key for the name.
     *
     * @param name The name.
     * @return The key.
     */
    public static int getDialogueKey(String name) {
        return 1 << 16 | name.hashCode();
    }

    /**
     * Gets the dialogueStage.
     *
     * @return The dialogueStage.
     */
    public ScriptContext getDialogueStage() {
        return dialogueStage;
    }

    /**
     * Sets the dialogueStage.
     *
     * @param dialogueStage The dialogueStage to set.
     */
    public void setDialogueStage(ScriptContext dialogueStage) {
        this.dialogueStage = dialogueStage;
    }

    /**
     * Adds a dialogue action.
     *
     * @param action the action.
     */
    public void addAction(DialogueAction action) {
        actions.clear();
        actions.add(action);
    }

    /**
     * Gets the actions.
     *
     * @return The actions.
     */
    public List<DialogueAction> getActions() {
        return actions;
    }

}
