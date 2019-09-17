package org.gielinor.spring.service;

/**
 * Represents an interface for spawning various world entities on start-up.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public interface SpawnService {

    /**
     * Initializes the spawns.
     */
    public abstract void loadSpawns();
}
