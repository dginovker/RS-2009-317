package plugin.interaction.item.withplayer;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles using a Christmas cracker on another player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ChristmasCrackerPlugin extends UseWithHandler {

    private static final Logger log = LoggerFactory.getLogger(ChristmasCrackerPlugin.class);

    private static final int CHRISTMAS_CRACKER = 962;

    /* Partyhat constants */

    private static final int RED_PARTYHAT = 1038;
    private static final int YELLOW_PARTYHAT = 1040;
    private static final int BLUE_PARTYHAT = 1042;
    private static final int GREEN_PARTYHAT = 1044;
    private static final int PURPLE_PARTYHAT = 1046;
    private static final int WHITE_PARTYHAT = 1048;

    /* Consolation prize constants */

    private static final int LAW_RUNE = 563;
    private static final int BLACK_DAGGER = 1217;
    private static final int GOLD_RING = 1635;
    private static final int HOLY_SYMBOL = 1718;
    private static final int SILK = 950;
    private static final int CHOCOLATE_CAKE = 1897;
    private static final int IRON_ORE_NOTE = 441;
    private static final int SPINACH_ROLL = 1969;
    private static final int SILVER_BAR = 2355;
    private static final int CHOCOLATE_BAR = 1973;

    @Override
    public Plugin<Object> newInstance(Object arg) {
        addHandler(CHRISTMAS_CRACKER, PLAYER_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Item item = (Item) event.getUsed();
        Player player = event.getPlayer();
        Player target = (Player) event.getUsedWith();
        if (target == null || !target.isActive() || target.getLocks().isInteractionLocked()) {
            event.getPlayer().getActionSender().sendMessage("The other player is currently busy.");
            return true;
        }
        if (player.isIronman()) {
            player.getActionSender().sendMessage(target.getUsername() + " is an "
                + target.getSavedData().getGlobalData().getStarterPackage().name().replaceAll("_MODE", "")
                .toLowerCase().replaceAll("_", " ") + " and cannot do that.");
            return true;
        }
        if (!target.getSettings().isAcceptAid()) {
            player.getActionSender().sendMessage(target.getUsername() + " is not accepting aid.");
            return true;
        }
        if (!player.canInteractWith(target)) {
            player.getActionSender().sendMessage("You cannot use this on that player.");
            return true;
        }
        log.info("[{}] pulled christmas cracker with [{}].", player.getName(), target.getName());
        if (event.getPlayer().getInventory().remove(item)) {
            player.getActionSender().sendMessage("You pull a Christmas cracker...");
            target.getActionSender().sendMessage("You pull a Christmas cracker...");
            World.submit(new Pulse(1) {

                @Override
                public boolean pulse() {
                    boolean keepPartyHat = RandomUtil.random(2) == 1;
                    final Item partyhat = generateRandomPartyhat();
                    final Item consolation = generateRandomConsolationPrize();
                    player.getActionSender().sendMessage(keepPartyHat ? "Hey! I got the cracker!" : "The person you pull the cracker with gets the prize.");
                    target.getActionSender().sendMessage(!keepPartyHat ? "You get the prize from the cracker" : "The person you pull the cracker with gets the prize.");
                    if (!player.getInventory().add(keepPartyHat ? partyhat : consolation, player)) {
                        player.getActionSender().sendMessage("Your item falls to the floor.");
                    }
                    if (!target.getInventory().add(!keepPartyHat ? partyhat : consolation, target)) {
                        target.getActionSender().sendMessage("Your item falls to the floor.");
                    }
                    Player hatReceiver = keepPartyHat ? player : target;
                    log.info("[{}] received the [{}].", hatReceiver.getName(), partyhat.getName());
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    private Item generateRandomPartyhat() {
        final int roll = RandomUtil.random(128);
        Item result;
        if (roll < 10) result = new Item(PURPLE_PARTYHAT); // 10:128
        else if (roll < 25) result = new Item(BLUE_PARTYHAT);   // 15:128
        else if (roll < 45) result = new Item(GREEN_PARTYHAT);  // 20:128
        else if (roll < 68) result = new Item(WHITE_PARTYHAT);  // 23:128
        else if (roll < 96) result = new Item(YELLOW_PARTYHAT); // 28:128
        else/*(roll < 128)*/result = new Item(RED_PARTYHAT);    // 32:128
        log.debug("Generated colored hat [{}] with roll: {}.", result.getName(), roll);
        return result;
    }

    private Item generateRandomConsolationPrize() {
        final int roll = RandomUtil.random(128);
        Item result;
        if (roll < 5) result = new Item(LAW_RUNE);         //  5:128
        else if (roll < 10) result = new Item(BLACK_DAGGER);     //  5:128
        else if (roll < 19) result = new Item(GOLD_RING);        //  9:128
        else if (roll < 30) result = new Item(HOLY_SYMBOL);      // 11:128
        else if (roll < 41) result = new Item(SILK);             // 11:128
        else if (roll < 55) result = new Item(CHOCOLATE_CAKE);   // 14:128
        else if (roll < 70) result = new Item(IRON_ORE_NOTE, 5); // 15:128
        else if (roll < 86) result = new Item(SPINACH_ROLL);     // 16:128
        else if (roll < 103) result = new Item(SILVER_BAR);       // 17:128
        else/*(roll < 128)*/ result = new Item(CHOCOLATE_BAR);    // 25:128
        log.debug("Generated consolation prize [{}] with roll: {}.", result.getName(), roll);
        return result;
    }

}
