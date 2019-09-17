package org.gielinor.game.world.map.zone;

import org.gielinor.game.world.callback.CallBack;
import org.gielinor.game.world.map.zone.impl.BankZone;
import org.gielinor.game.world.map.zone.impl.DarkZone;
import org.gielinor.game.world.map.zone.impl.KaramjaZone;
import org.gielinor.game.world.map.zone.impl.ModeratorZone;
import org.gielinor.game.world.map.zone.impl.MultiwayCombatZone;
import org.gielinor.game.world.map.zone.impl.WildernessZone;

import plugin.activity.clanwars.ClanWarsChallengeZone;
import plugin.activity.duelarena.DuelArenaChallengeZone;

/**
 * Loads all the default zones.
 *
 * @author Emperor
 */
public class ZoneBuilder implements CallBack {

    @Override
    public boolean call() {
        configure(WildernessZone.Companion.getInstance());
        configure(MultiwayCombatZone.getInstance());
        configure(new ModeratorZone());
        configure(new DarkZone());
        configure(new KaramjaZone());
        configure(new BankZone());
        configure(new ClanWarsChallengeZone());
        configure(DuelArenaChallengeZone.getInstance());
        return true;
    }

    /**
     * Configures the map zone.
     *
     * @param zone The map zone.
     */
    public static void configure(MapZone zone) {
        zone.setUid(zone.getName().hashCode());
        zone.configure();
    }
}
