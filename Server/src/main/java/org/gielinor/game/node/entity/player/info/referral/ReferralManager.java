package org.gielinor.game.node.entity.player.info.referral;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gielinor.cache.misc.buffer.ByteBufferUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.misc.PlayerLoader;

/**
 * Represents the referral manager for a player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ReferralManager implements SavingModule {

    /**
     * The xp token {@link org.gielinor.game.node.item.Item}.
     */
    private static final Item XP_TOKEN = new Item(10942);
    /**
     * The coins {@link org.gielinor.game.node.item.Item} for the referred player.
     */
    private static final Item REFERRED_COINS = new Item(Item.COINS, 100000);
    /**
     * The coins {@link org.gielinor.game.node.item.Item}.
     */
    private static final Item COINS = new Item(Item.COINS, 500000);
    /**
     * The player.
     */
    private final Player player;
    /**
     * The {@link java.util.List} of referraled players by the player.
     */
    private final List<Referred> referrals = new ArrayList<>();
    /**
     * If the player has been referred already.
     */
    private boolean referred;

    /**
     * Constructs a new <code>ReferralManager</code>.
     *
     * @param player The player.
     */
    public ReferralManager(Player player) {
        this.player = player;
    }

    /**
     * Sends a referral to a player.
     *
     * @param referral The player being referred.
     */
    public void refer(Player referral) {
        player.getActionSender().sendMessage("<shad=1><col=9B701D>You have sent " + referral.getUsername() + " a referral!");
        referrals.add(new Referred(referral.getPidn(), referral.getUsername(), System.currentTimeMillis(), false));
        referral.getReferralManager().getReferrals().add(new Referred(player.getPidn(), player.getUsername(),
            System.currentTimeMillis(), true));
        referral.getActionSender().sendMessage("<shad=1><col=9B701D>" + player.getUsername() + " has sent you a referral request!");
        referral.getActionSender().sendMessage("<shad=1><col=9B701D>Check the \"requests\" option on your referral ticket!");
    }

    /**
     * Accepts a referral.
     *
     * @param username The name of the user to accept.
     */
    public void accept(String username) {
        if (username == null || username.isEmpty() || username.length() < 1 || username.equalsIgnoreCase(player.getName())) {
            player.getDialogueInterpreter().sendPlaneMessage("Please provide a valid username.");
            return;
        }
        Player otherPlayer = Repository.getPlayerByName(username);
        if (otherPlayer == null) {
            otherPlayer = PlayerLoader.getPlayerFile(username);
        }
        if (otherPlayer == null) {
            player.getDialogueInterpreter().sendPlaneMessage("The player \"" + username + "\" could not be found.");
            return;
        }
        if (otherPlayer == player || Objects.equals(otherPlayer.getDetails().getMacAddress(), player.getDetails().getMacAddress()) ||
            Objects.equals(otherPlayer.getDetails().getIp(), player.getDetails().getIp())) {
            player.getDialogueInterpreter().sendPlaneMessage("You cannot refer yourself.");
            return;
        }
        Referred referred = null;
        for (Referred referred1 : referrals) {
            if (!referred1.isRequested()) {
                continue;
            }
            if (referred1.getUsername().equalsIgnoreCase(otherPlayer.getUsername())) {
                referred = referred1;
                break;
            }
        }
        if (referred == null) {
            player.getDialogueInterpreter().sendPlaneMessage("No referral request found for " + otherPlayer.getUsername() + ".");
            return;
        }
        Referred otherReferred = null;
        for (Referred referred1 : otherPlayer.getReferralManager().getReferrals()) {
            if (referred1.isRequested()) {
                continue;
            }
            if (referred1.getUsername().equalsIgnoreCase(player.getUsername())) {
                otherReferred = referred1;
                break;
            }
        }
        if (otherReferred == null) {
            player.getDialogueInterpreter().sendPlaneMessage("No referral request found for " + otherPlayer.getUsername() + ".");
            return;
        }
        if (otherReferred.isDeclined()) {
            player.getDialogueInterpreter().sendPlaneMessage("You have already declined this referral.");
            return;
        }
        this.referred = true;
        otherReferred.setReferredTime(System.currentTimeMillis());
        String s = otherPlayer.getUsername().endsWith("s") ? "'" : "'s";
        player.getActionSender().sendMessage("<shad=1><col=9B701D>You have accepted " + otherPlayer.getUsername() + s + " referral!");
        reward(true);
        if (!otherPlayer.isPlaying() || !otherPlayer.isActive()) {
            World.getWorld().getAccountService().savePlayer(otherPlayer);
            return;
        }
        otherReferred.setNotified(true);
        otherPlayer.getActionSender().sendMessage("<shad=1><col=9B701D>" + player.getUsername() + " has accepted your referral!");
        otherPlayer.getReferralManager().reward(false);
    }

    /**
     * Declines a referral.
     *
     * @param username The name of the user to decline.
     */
    public void decline(String username) {
        if (username == null || username.isEmpty() || username.length() < 1 || username.equalsIgnoreCase(player.getName())) {
            player.getDialogueInterpreter().sendPlaneMessage("Please provide a valid username.");
            return;
        }
        Player otherPlayer = Repository.getPlayerByName(username);
        if (otherPlayer == null) {
            otherPlayer = PlayerLoader.getPlayerFile(username);
        }
        if (otherPlayer == null) {
            player.getDialogueInterpreter().sendPlaneMessage("The player \"" + username + "\" could not be found.");
            return;
        }
        Referred referred = null;
        for (Referred referred1 : referrals) {
            if (!referred1.isRequested()) {
                continue;
            }
            if (referred1.getUsername().equalsIgnoreCase(otherPlayer.getUsername())) {
                referred = referred1;
                break;
            }
        }
        if (referred == null) {
            player.getDialogueInterpreter().sendPlaneMessage("No referral request found for " + otherPlayer.getUsername() + ".");
            return;
        }
        Referred otherReferred = null;
        for (Referred referred1 : otherPlayer.getReferralManager().getReferrals()) {
            if (referred1.isRequested()) {
                continue;
            }
            if (referred1.getUsername().equalsIgnoreCase(player.getUsername())) {
                otherReferred = referred1;
                break;
            }
        }
        if (otherReferred == null) {
            player.getDialogueInterpreter().sendPlaneMessage("No referral request found for " + otherPlayer.getUsername() + ".");
            return;
        }
        referrals.remove(referred);
        String s = otherPlayer.getUsername().endsWith("s") ? "'" : "'s";
        player.getActionSender().sendMessage("<shad=1><col=9B701D>You have declined " + otherPlayer.getUsername() + s + " referral!");

        otherReferred.setDeclined(true);
        // TODO Benefits
        if (!otherPlayer.isPlaying() || !otherPlayer.isActive()) {
            World.getWorld().getAccountService().savePlayer(otherPlayer);
            return;
        }
        otherReferred.setNotified(true);
        otherPlayer.getActionSender().sendMessage("<shad=1><col=9B701D>" + player.getUsername() + " has declined your referral!");
    }

    /**
     * Checks if the player has the given username already referred.
     *
     * @param username The username to check.
     * @return <code>True</code> if so.
     */
    public boolean hasReferred(String username) {
        for (Referred referral : referrals) {
            if (referral == null) {
                continue;
            }
            if (!referral.isRequested() && referral.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player has been referred.
     *
     * @return <code>True</code> if so.
     */
    public boolean isReferred() {
        return referred;
    }

    /**
     * Gets the {@link java.util.List} of referrals.
     *
     * @return The list.
     */
    public List<Referred> getReferrals() {
        return referrals;
    }

    /**
     * Gets the count of all sent referrals.
     *
     * @return The count.
     */
    public int getSentReferralCount() {
        final int[] count = { 0 };
        player.getReferralManager().getReferrals().stream().filter(referred -> !referred.isRequested()).forEach(referred -> {
            count[0]++;
        });
        return count[0];
    }

    /**
     * Gets the count of accepted referrals.
     *
     * @return The count.
     */
    public int getReferralCount() {
        final int[] count = { 0 };
        player.getReferralManager().getReferrals().stream().filter(referred -> referred.getReferredTime() > 0
            && !referred.isRequested()).forEach(referred -> {
            count[0]++;
        });
        return count[0];
    }

    /**
     * Gets the count of requested referrals.
     *
     * @return The count.
     */
    public int getReferralRequests() {
        final int[] count = { 0 };
        player.getReferralManager().getReferrals().stream().filter(referred -> referred.getReferredTime() < 1
            && referred.isRequested()).forEach(referred -> {
            count[0]++;
        });
        return count[0];
    }

    /**
     * Rewards the player for referring another.
     */
    public void reward(boolean referred) {
        if (referred) {
            player.getActionSender().sendMessage("<shad=1><col=FFCA00>You have earned 100k coins and an xp token worth 100k to be spent in any skills!");
            XP_TOKEN.setCharge(100);
            if (player.getInventory().hasRoomFor(REFERRED_COINS)) {
                player.getInventory().add(REFERRED_COINS);
            } else if (player.getBank().hasRoomFor(REFERRED_COINS)) {
                player.getBank().add(REFERRED_COINS);
                player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your coins have been added to your bank.");
            } else {
                if (!player.getInventory().add(REFERRED_COINS, player)) {
                    player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your coins have been dropped on the ground.");
                }
            }
            if (player.getInventory().hasRoomFor(XP_TOKEN)) {
                player.getInventory().add(XP_TOKEN);
            } else if (player.getBank().hasRoomFor(XP_TOKEN)) {
                player.getBank().add(XP_TOKEN);
                player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your xp token has been added to your bank.");
            } else {
                if (!player.getInventory().add(XP_TOKEN, player)) {
                    player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your xp token has been dropped on the ground.");
                }
            }
            return;
        }
        player.getActionSender().sendMessage("<shad=1><col=FFCA00>You have earned 500k coins and an xp token worth 300k to be spent in any skills!");
        XP_TOKEN.setCharge(300);
        if (player.getInventory().hasRoomFor(COINS)) {
            player.getInventory().add(COINS);
        } else if (player.getBank().hasRoomFor(COINS)) {
            player.getBank().add(COINS);
            player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your coins have been added to your bank.");
        } else {
            if (!player.getInventory().add(COINS, player)) {
                player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your coins have been dropped on the ground.");
            }
        }
        if (player.getInventory().hasRoomFor(XP_TOKEN)) {
            player.getInventory().add(XP_TOKEN);
        } else if (player.getBank().hasRoomFor(XP_TOKEN)) {
            player.getBank().add(XP_TOKEN);
            player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your xp token has been added to your bank.");
        } else {
            if (!player.getInventory().add(XP_TOKEN, player)) {
                player.getActionSender().sendMessage("<shad=1><col=FFCA00>Your xp token has been dropped on the ground.");
            }
        }
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) (referred ? 1 : 0));
        if (referrals.isEmpty()) {
            byteBuffer.putInt(-1);
            return;
        }
        int length = 0;
        for (Referred referral : referrals) {
            if (referral == null) {
                continue;
            }
            length++;
        }
        byteBuffer.putInt(length);
        for (Referred referral : referrals) {
            if (referral == null) {
                continue;
            }
            byteBuffer.putInt(referral.getPidn());
            ByteBufferUtils.putRS2String(byteBuffer, referral.getUsername());
            byteBuffer.putLong(referral.getReferredRequestTime());
            byteBuffer.putLong(referral.getReferredTime());
            byteBuffer.put((byte) (referral.isRequested() ? 1 : 0));
            byteBuffer.put((byte) (referral.isNotified() ? 1 : 0));
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        referred = byteBuffer.get() == 1;
        int length = byteBuffer.getInt();
        if (length == -1) {
            return;
        }
        while (length > 0) {
            Referred referral = new Referred(byteBuffer.getInt(), ByteBufferUtils.getRS2String(byteBuffer), byteBuffer.getLong(), byteBuffer.getLong(), (byteBuffer.get() == 1));
            referral.setNotified(byteBuffer.get() == 1);
            referrals.add(referral);
            length--;
        }
        byteBuffer.get();
    }
}
