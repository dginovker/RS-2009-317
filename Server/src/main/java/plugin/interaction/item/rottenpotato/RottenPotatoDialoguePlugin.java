package plugin.interaction.item.rottenpotato;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.rs2.config.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import plugin.interaction.item.rottenpotato.impl.ActionClass;

/**
 * Handles the dialogue for the {@link plugin.interaction.item.rottenpotato.RottenPotato}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RottenPotatoDialoguePlugin extends DialoguePlugin {

    private static final Logger log = LoggerFactory.getLogger(RottenPotatoDialoguePlugin.class);

    /**
     * The mapping of {@link plugin.interaction.item.rottenpotato.impl.ActionClass} classes.
     */
    private final Map<String, ActionClass> ACTION_CLASSES = new HashMap<>();

    /**
     * The currently used class names mapped by the option index.
     */
    private static final Map<Integer, String> OPTION_MAP = new HashMap<>();

    /**
     * Constructs a new {@link plugin.interaction.item.rottenpotato.RottenPotatoDialoguePlugin}.
     */
    public RottenPotatoDialoguePlugin() {
    }

    /**
     * Constructs a new {@link plugin.interaction.item.rottenpotato.RottenPotatoDialoguePlugin}.
     *
     * @param player The player.
     */
    public RottenPotatoDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        if (!player.getDetails().getRights().isAdministrator()) {
            Marker notifyAdmin = MarkerFactory.getMarker("NOTIFY_ADMIN");
            log.warn(notifyAdmin, "[{}] attempted to use rotten potato. How did they get one?", player.getName());
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        load();
        String OPTION = (String) args[0];
        String[] options = DataShelf.fetchStringArray("rotten_potato_" + OPTION.toLowerCase());
        for (int index = 0; index < options.length; index++) {
            OPTION_MAP.put(index, options[index]);
        }
        switch (OPTION) {
            case "eat":
                Collection<String> values = OPTION_MAP.values();
                interpreter.sendOptions("Op1", values.toArray(new String[values.size()]));
                stage = 100;
                break;
            case "slice":
                interpreter.sendOptions("Op2", DataShelf.fetchStringArray("rotten_potato_slice"));
                stage = 200;
                break;
            case "peel":
                interpreter.sendOptions("Op3", DataShelf.fetchStringArray("rotten_potato_peel"));
                stage = 300;
                break;
            case "mash":
                interpreter.sendOptions("Op4", DataShelf.fetchStringArray("rotten_potato_mash"));
                stage = 400;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        String actionClassVariable = OPTION_MAP.get(optionSelect.getIndex()).replaceAll(" ", "_").toLowerCase();
        ActionClass actionClass = ACTION_CLASSES.get(actionClassVariable);
        switch (stage) {
            /**
             * Eat.
             */
            case 100:
                if (actionClass == null) {
                    player.getActionSender().sendMessage("Could not load class for variable : " + actionClassVariable + "!");
                    return true;
                }
                return actionClass.execute(player);
        }
        return false;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RottenPotatoDialoguePlugin(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("RottenPotatoDialoguePlugin") };
    }

    public void load() {
        if (OPTION_MAP.size() > 0) {
            return;
        }
        String packageName = this.getClass().getPackage().toString().replace("package ", "") + ".impl.";
        File classDirectory = new File(Constants.CLASS_DIRECTORY + packageName.replaceAll("\\.", "/"));
        File[] files = classDirectory.listFiles();
        assert files != null;
        for (File f : files) {
            if (f == null) {
                continue;
            }
            if (f.getName().contains("$")) {
                continue;
            }
            if (!f.getName().endsWith(".class")) {
                continue;
            }
            if (f.getName().startsWith("ActionClass")) {
                continue;
            }
            String fileName = packageName + f.getName().substring(0, f.getName().length() - 6);
            try {
                Class c = Class.forName(fileName);
                ActionClass actionClass = (ActionClass) c.newInstance();
                ACTION_CLASSES.put(actionClass.getVariableName(), actionClass);
            } catch (ClassNotFoundException ex) {
                String name = f.getName().substring(0, f.getName().length() - 6);
                log.error("Unable to load class [{}].", name, ex);
            } catch (InstantiationException | IllegalAccessException ex) {
                String name = f.getName().substring(0, f.getName().length() - 6);
                log.error("Unable to instantiate class [{}].", name, ex);
            }
        }
        log.info("Loaded {} rotten potato action classes.", ACTION_CLASSES.size());
    }
}
