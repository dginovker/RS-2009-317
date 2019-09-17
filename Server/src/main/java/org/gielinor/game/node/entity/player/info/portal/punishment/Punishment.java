package org.gielinor.game.node.entity.player.info.portal.punishment;

import org.gielinor.database.DataSource;
import org.gielinor.database.DatabaseKt;
import org.gielinor.game.node.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Represents a punishment given to a <code>Player</code>. A punishment consists of a Mute or a Ban.
 *
 * @author 'Vexia
 * @date 24/11/2013
 *
 * @author Corey
 * @date 10/09/2017
 */
public abstract class Punishment {

    private static final Logger log = LoggerFactory.getLogger(Punishment.class);

    /**
     * The unique identification number for this punishment.
     */
    protected int id;

    /**
     * Represents the date at which the punishment is terminated.
     */
    protected long expiry;

    /**
     * Represents if this punishment is permanent.
     */
    protected boolean permanent;

    /**
     * Why the player was punished.
     */
    protected String reason;

    public Punishment(int pidn) {
        parse(pidn);
    }

    /**
     * Method used to inflict any other related damage/messages/alters to the player.
     */
    public abstract void inflict(final Player player);

    public abstract void save(final Player player, final Player moderator);

    public abstract void parse(int pidn);

    /**
     * Method used to give restrictions to this punishment.
     *
     * @param duration the expiry to restrict.
     */
    public final boolean punish(final Player player, final Player moderator, long duration, String reason) {
        if (player.specialDetails()) {
            return false;
        }
        if (duration == 0) {
            permanent = true;
        }

        this.expiry = System.currentTimeMillis() + duration;
        this.reason = reason;

        save(player, moderator);
        inflict(player);
        return true;
    }

    public final void reverse() {
        try {
            DatabaseKt.performDatabaseFunction(DataSource.getGameConnection(), "UPDATE player_punishment SET `reversed` = 1 WHERE `id` = " + id);
        } catch (SQLException | IOException ex) {
            log.error("Failed to reverse punishment [{}].", id, ex);
        }
        reset();
    }

    /**
     * Method used to reset a punishment.
     */
    public final void reset() {
        expiry = 0L;
        permanent = false;
    }

    public final long getTimeUntilExpiry() {
        return this.expiry - System.currentTimeMillis();
    }

    /**
     * Method used to check if the player is punished.
     *
     * @return <code>True</code> if punished and <code>False</code> if not.
     */
    public final boolean isPunished() {
        return permanent || expiry > System.currentTimeMillis();
    }

    public static long getPunishmentDuration(String input) {
        String duration = input.split(" ")[0];
        String durationUnit = input.split(" ")[1];

        double durationModifier;
        try {
            durationModifier = Double.parseDouble(duration);
        } catch (NumberFormatException ex) {
            log.warn("Duration modifier is invalid. Assuming 1.", ex);
            durationModifier = 1D;
        }

        long finalDurationUnit;
        long finalDuration;
        TimeUnit unit;
        boolean weeks = false;
        boolean months = false;

        switch (durationUnit) {
            case "s":
            case "sec":
            case "secs":
            case "second":
            case "seconds":
                unit = TimeUnit.SECONDS;
                break;
            case "m":
            case "min":
            case "mins":
            case "minute":
            case "minutes":
                unit = TimeUnit.MINUTES;
                break;
            case "h":
            case "hr":
            case "hrs":
            case "hour":
            case "hours":
                unit = TimeUnit.HOURS;
                break;
            case "d":
            case "day":
            case "days":
                unit = TimeUnit.DAYS;
                break;
            case "w":
            case "week":
            case "weeks":
                unit = TimeUnit.DAYS;
                weeks = true;
                break;
            case "month":
            case "months":
                unit = TimeUnit.DAYS;
                break;
            default:
                // Assume it's days.
                unit = TimeUnit.HOURS;
                log.warn("Invalid time unit [{}]. Assuming we are talking about hours.", durationUnit);
                break;
        }

        int unitAmount = 1;
        if (weeks) {
            unitAmount = 7;
        } else if (months) {
            unitAmount = 30;
        }

        finalDurationUnit = unit.toMillis(unitAmount);
        finalDuration = (long) (durationModifier * finalDurationUnit);
        return finalDuration;
    }

}
