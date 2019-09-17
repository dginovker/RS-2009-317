package org.gielinor.spring.service.impl;

import org.gielinor.game.node.entity.player.Player;
import org.springframework.stereotype.Service;

/**
 * Handles checking the donation table for new donations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO
 */
@Service("donationService")
public class DonationService {

    /**
     * Checks if a player has donated and the donation is unclaimed.
     *
     * @return <code>True</code> if so.
     */
    public boolean checkDonation(Player player) {
        return false;
    }

}