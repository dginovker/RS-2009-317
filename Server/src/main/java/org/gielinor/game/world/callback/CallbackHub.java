package org.gielinor.game.world.callback;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.AnimationDefinition;
import org.gielinor.game.content.eco.grandexchange.offer.GEOfferDispatch;
import org.gielinor.game.content.global.shop.Shop.RestockPulse;
import org.gielinor.game.content.skill.member.farming.FarmingPulse;
import org.gielinor.game.content.skill.member.hunter.ImpetuousImpulses;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.net.packet.in.CommandPacketHandler;
import org.gielinor.spring.service.impl.SkillMenuService;
import org.gielinor.utilities.donation.DonatePulse;
import org.gielinor.utilities.game.IPBanRepository;
import org.gielinor.utilities.game.MacBanRepository;
import org.gielinor.utilities.voting.ModifiersPulse;
import org.gielinor.utilities.voting.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a crappy class by vexia.
 *
 * @author 'Vexia
 */
public final class CallbackHub {

    private static final Logger log = LoggerFactory.getLogger(CallbackHub.class);

    /**
     * Constructs a new {@code CallbackHub} {@code Object}.
     */
    public CallbackHub() {
    }

    /**
     * Method used to initializes the call back hub.
     *
     * @return the value <code>True</code> if {@link CallBack#call()} is <code>True</code>.
     */
    public static boolean call() {
        List<CallBack> calls = new ArrayList<>();
        calls.add(new ZoneBuilder());
        calls.add(new GEOfferDispatch());
        calls.add(IPBanRepository.getRepository());
        calls.add(MacBanRepository.getRepository());
        calls.add(new FarmingPulse());
        calls.add(new ImpetuousImpulses());
        calls.add(new RestockPulse());
        calls.add(new CommandPacketHandler());
        calls.add(new ModifiersPulse());
        calls.add(new Vote());
        calls.add(new DonatePulse());
        calls.add(new DataShelf());
        calls.add(new AnimationDefinition());
        calls.add(new SkillMenuService());
        for (CallBack call : calls) {
            if (!call.call()) {
                log.error("Callback was unsuccessful: [{}].", call.getClass().getName());
                return false;
            }
        }
        return true;
    }
}
