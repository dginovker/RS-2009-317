package plugin.activity.duelarena;

/**
 * Represents the current stage of a {@link plugin.activity.duelarena.DuelSession}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum DuelStage {

    WAITING,
    ACCEPT_TIMEOUT,
    ACCEPTED_WAITING,
    ACCEPTED,
    ACCEPTED_2_WAITING,
    ACCEPTED_2,
    IN_PROGRESS
}
