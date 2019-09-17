package org.gielinor.game.system;

import org.gielinor.content.periodicity.PeriodicityExtensionsKt;
import org.gielinor.content.periodicity.PeriodicityPulseManager;
import org.gielinor.database.DataSource;
import org.gielinor.database.DatabaseVariable;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.content.eco.grandexchange.offer.GEOfferDispatch;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * Handles the terminating of the system.
 *
 * @author Emperor
 */
public final class SystemTermination {

    private static final Logger log = LoggerFactory.getLogger(SystemTermination.class);

    /**
     * The data directory.
     */
    public static final String DATA_DIRECTORY = "data/";

    /**
     * The backup directory.
     */
    public static final String BACKUP_DIRECTORY = "data/backup/";

    /**
     * Constructs a new {@code SystemTermination} {@code Object}.
     */
    protected SystemTermination() {
    }

    /**
     * Terminates the system safely.
     */
    public void terminate() {
        if (Constants.KILL_SERVER) {
            log.info("Shutting down.");
            System.exit(1);
            return;
        }
        try {
            log.info("Attempting to save current states to: [{}].", DATA_DIRECTORY);
            save(DATA_DIRECTORY);
        } catch (Throwable e) {
            log.error("Failed to save current states to: [{}].", DATA_DIRECTORY);
            try {
                log.info("Making an attempt to save to [{}] instead.", BACKUP_DIRECTORY);
                save(BACKUP_DIRECTORY);
                log.info("Succeeded in saving to [{}].", BACKUP_DIRECTORY);
            } catch (Throwable t) {
                log.error("Attempt to save to [{}] failed aswell. Nothing more we can do.", t);
            }
        }
        log.info("Shutting down.");
        System.exit(0);
    }

    /**
     * Generates a player backup.
     */
    public static void generatePlayerBackup() {
        if (!SystemManager.isActive()) {
            return;
        }
        String directory = BACKUP_DIRECTORY + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        File playerDirectory = new File(DATA_DIRECTORY + "players/");
        for (File file : playerDirectory.listFiles()) {
            if (!file.isDirectory()) {
                copyFile(file, new File(directory + "/" + file.getName()));
            }
        }
    }

    /**
     * Generates a backup.
     */
    public void generateBackup() {
        if (!SystemManager.isActive()) {
            return;
        }
        try {
            save(DATA_DIRECTORY);
        } catch (Throwable ex) {
            log.error("Failed to save backup.", ex);
        }
        Executors.newSingleThreadExecutor().submit(() -> {
            String directory = BACKUP_DIRECTORY + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdir();
                dir = new File(directory + "/eco/");
                dir.mkdir();
                dir = new File(directory + "/store/");
                dir.mkdir();
            }
            for (File file : new File(DATA_DIRECTORY + "players/").listFiles()) {
                if (!file.isDirectory()) {
                    copyFile(file, new File(directory + "/" + file.getName()));
                }
            }
            for (File file : new File(DATA_DIRECTORY + "clans/").listFiles()) {
                if (!file.isDirectory()) {
                    copyFile(file, new File(directory + "/" + file.getName()));
                }
            }
            File file = new File(DATA_DIRECTORY + "eco/grand_exchange_db.emp");
            if (file.exists()) {
                copyFile(file, new File(directory + "/eco/" + file.getName()));
            }
            file = new File(DATA_DIRECTORY + "eco/offer_dispatch_db.emp");
            if (file.exists()) {
                copyFile(file, new File(directory + "/eco/" + file.getName()));
            }
            file = new File(DATA_DIRECTORY + "world/store/dynamic_cache.arios");
            if (file.exists()) {
                copyFile(file, new File(directory + "/world/store/" + file.getName()));
            }
            log.info("Generated backup.");
        });
    }

    /**
     * Saves all system data on the directory.
     *
     * @param directory The base directory.
     */
    public void save(String directory) {
        File file = new File(directory);
        log.info("Saving data to: [{}].", file.getAbsolutePath());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        DataSource.getDatabaseVariables().forEach(DatabaseVariable::save);
        log.info("Saved all database variables...");
        GrandExchangeDatabase.save();
        log.info("Saved Grand Exchange database...");
        GEOfferDispatch.dump();
        log.info("Saved Grand Exchange dispatch database...");
        PeriodicityExtensionsKt.save(PeriodicityPulseManager.Companion.getPULSES());
        log.info("Saved global pulses...");
        Repository.getDisconnectionQueue().clear();
        log.info("Cleared disconnection queue...");
        //Should never be null.
        Repository.getPlayers().stream().filter(p -> p != null && !p.isArtificial()).forEach(player -> { //Should never be null.
            player.removeAttribute("combat-time");
            player.getActionSender().sendLogout();
            player.clear();
            World.getWorld().getAccountService().savePlayer(player);
            if (!player.isArtificial()) {
                player.getDetails().save();
            }
        });
        log.info("Saved player accounts.");
        log.info("Successfully saved server state.");
    }

    /**
     * Saves all system data on the directory.
     *
     * @param directory The base directory.
     * @throws Throwable When an exception occurs.
     */
    public void saveOnline(String directory) {
        File file = new File(directory);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        Repository.getDisconnectionQueue().clear();
        //Should never be null.
        Repository.getPlayers().stream().filter(player -> player != null && !player.isArtificial()).forEach(player -> { //Should never be null.
            World.getWorld().getAccountService().savePlayer(player);
            player.getDetails().save();
        });
    }

    /**
     * Copies a file.
     *
     * @param target  The file to be copied.
     * @param destination The file to copy to.
     */
    private static void copyFile(File target, File destination) {
        try (FileChannel channel = new FileInputStream(target).getChannel();
             FileChannel output = new FileOutputStream(destination).getChannel()) {
            channel.transferTo(0, channel.size(), output);
        } catch (IOException ex) {
            log.error("File copy from [{}] to [{}] failed.",
                target.getAbsolutePath(), destination.getAbsolutePath(), ex);
        }
    }

}
