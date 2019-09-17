package org.gielinor.game.system.script;

import java.io.File;

import org.gielinor.game.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class used to manage the loading of scripts.
 *
 * @author 'Vexia
 */
public final class ScriptManager {

    private static final Logger log = LoggerFactory.getLogger(ScriptManager.class);

    /**
     * The directory to load scripts from.
     */
    private static final String DIRECTORY = "scripts/";

    /**
     * The amount of scripts loaded.
     */
    private static int amount;

    /**
     * Method used to load the script manager.
     *
     * @param args the arguments.
     */
    public static void main(String... args) {
        load();
    }

    /**
     * Runs the script and returns the current script context after executing.
     *
     * @param context The script to run.
     * @param args    The arguments.
     * @return The last script context executed.
     */
    public static ScriptContext run(ScriptContext context, Object... args) {
        ScriptContext ctx = context;
        do {
            ctx = ctx.run(args);
        } while (ctx != null && ctx.isInstant());
        return ctx;
    }

    /**
     * Initiates the chain reaction of script loading.
     */
    public static void load() {
        amount = 0;
        load(new File(DIRECTORY));
        log.info("Parsed {} scripts.", amount);
    }

    /**
     * Method used to load a script by its path.
     *
     * @param path the path.
     */
    public static void load(final String path) {
        load(new File(path));
    }

    /**
     * Loads scripts from a directory.
     *
     * @param directory the directory.
     * @throws Throwable the throwable.
     */
    public static void load(final File directory) {
        try {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    load(file);
                    continue;
                }
                ScriptContext context = ScriptCompiler.parseRaw(file);
                if (ScriptCompiler.getBuilder() != null) {
                    ScriptCompiler.getBuilder().configureScript(context);
                }
                amount++;
            }
        } catch (Throwable e) {
            World.handleError(e);
        }
    }


    /**
     * Runs the script and returns the current script context after executing.
     *
     * @param context The script to run.
     * @param name    The method name.
     * @return The method script context.
     */
    public static ScriptContext getMethod(ScriptContext context, String name) {
        ScriptContext ctx = context;
        while (ctx != null) {
            if (ctx.getCondition() != null) {
                context = getMethod(ctx.getCondition(), name);
                if (context != null) {
                    return context;
                }
            }
            if (ctx.getName().equals(name)) {
                return ctx;
            }
            ctx = ctx.getInstruction();
        }
        return null;
    }

}
