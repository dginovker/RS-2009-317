package plugin.interaction.inter;

import java.util.concurrent.TimeUnit;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.travel.Teleport;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.entity.player.info.title.LoyaltyTitleManagement;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

import plugin.interaction.item.ReferralTicketPlugin;

/**
 * Handles the quest tab action buttons.
 *
 * @author Emperor
 * @author 'Vexia
 */
public class QuestTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(638, this); //Quests
        ComponentDefinition.put(29300, this); //Achievement diary
        ComponentDefinition.put(23235, this); // Minigames
        return this;
    }

    /**
     * Sends the default information of a player to the tab.
     */
    public static void sendDefaultInformation(Player player) {
        player.getActionSender().sendString(22704, "Name: " + (TextUtils.formatDisplayName(player.getUsername())));
        player.getActionSender().sendString(22705, "Rank: " + (TextUtils.formatDisplayName(player.getRights().name())));
        String playerTitle = "None";
        if (player.getTitleManager().hasTitle()) {
            LoyaltyTitle loyaltyTitle = player.getTitleManager().getLoyaltyTitle();
            playerTitle = ((loyaltyTitle.getColour() == null || loyaltyTitle.getColour().isEmpty())
                ? "" : "<col=" + loyaltyTitle.getColour() + ">") + player.getTitleManager().getTitleName();
        }
        player.getActionSender().sendString(22706, "Title: " + playerTitle);
        DonorStatus donorStatus = player.getDonorManager().getDonorStatus();
        player.getActionSender().sendString(22707, (donorStatus == DonorStatus.NONE) ? "" : "Status: <col=" + donorStatus.getColor() + ">" + TextUtils.formatDisplayName(donorStatus.name()));

        player.getActionSender().sendString(22708, "Voting points: " + player.getSavedData().getGlobalData().getVotingPoints());
        player.getActionSender().sendString(22709, "Gielinor points: " + player.getSavedData().getGlobalData().getLoyaltyPoints());
        player.getActionSender().sendString(22710, "Pest Points: " + player.getSavedData().getActivityData().getPestPoints());
        player.getActionSender().sendString(22711, "Gielinor tokens: " + player.getDonorManager().getGielinorTokens());

        player.getActionSender().sendString(22712, "Boss Kill Log");

        long timeNow = System.currentTimeMillis();
        long lastBorkBattle = (timeNow - player.getSavedData().getActivityData().getLastBorkBattle());
        long nextBorkBattle = (12 * 60 * 60_000) - lastBorkBattle;
        long hours = TimeUnit.MILLISECONDS.toHours(nextBorkBattle);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(nextBorkBattle);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(nextBorkBattle);
        String nextBork = "";
        if (hours > 0) {
            nextBork = "Next Bork: " + hours + " hour" + (hours > 1 ? "s" : "");
        } else if (minutes > 0) {
            nextBork = "Next Bork: " + minutes + " minute" + (minutes > 1 ? "s" : "");
        } else if (seconds > 0) {
            nextBork = "Next Bork: " + seconds + " minute" + (seconds > 1 ? "s" : "");
        }
        player.getActionSender().sendString(22713, nextBork);

        player.getActionSender().sendString(22715, "Player Kills: " + player.getSavedData().getGlobalData().getPlayerKills());
        player.getActionSender().sendString(22716, "Player Deaths: " + player.getSavedData().getGlobalData().getPlayerDeaths());
        player.getActionSender().sendString(22717, "");
        player.getActionSender().sendString(22718, "Referrals: " + player.getReferralManager().getReferralCount());
        player.getActionSender().sendString(22719, "Refer player");
        player.getActionSender().sendHideComponent(22720, player.getReferralManager().isReferred());
        if (!player.getReferralManager().isReferred()) {
            int referralRequests = player.getReferralManager().getReferralRequests();
            player.getActionSender().sendString(22720, (referralRequests > 0 ? "<col=FFD700>" : "") + "Referral requests: " + referralRequests);
        } else {
            player.getActionSender().sendString(22720, "");
        }
        player.getActionSender().sendString(22721, "Special Titles");
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (component.getId()) {
            case 22697:
                switch (button) {
                    /*
                     * Opens referrals list.
                     */
                    case 22718:
                        return ReferralTicketPlugin.openReferrals(player);
                    /*
                     * Opens a referral dialogue.
                     */
                    case 22719:
                        return ReferralTicketPlugin.openReferralDialogue(player);
                    /*
                     * Opens referral requests list.
                     */
                    case 22720:
                        return ReferralTicketPlugin.openReferralRequests(player);
                    /*
                     * Opens the Special Title interface
                     */
                    case 22721:
                        LoyaltyTitleManagement.openPersonalTitles(player);
                        return true;
                    /*
                     * Opens quest tab.
                     */
                    case 22702:
                        player.getInterfaceState().openTab(2, new Component(638));
                        player.saveAttribute("quest_tab", 638);
                        return true;
                    /*
                     * Opens achievement tab.
                     */
                    case 22700:
                        player.getInterfaceState().openTab(2, new Component(29300));
                        player.saveAttribute("quest_tab", 29300);
                        return true;
                    /*
                     * Opens minigame tab.
                     */
                    case 22701:
                        player.getInterfaceState().openTab(2, new Component(23235));
                        player.saveAttribute("quest_tab", 23235);
                        return true;
                    /*
                     * Opens boss kill log.
                     */
                    case 22712:
                        player.getSavedData().getBossKillLog().sendInterface(player);
                        return true;
                }
                break;
            /*
             * Minigame tab.
             */
            case 23235:
                switch (button) {
                    /*
                     * Opens information tab.
                     */
                    case 22696:
                        sendDefaultInformation(player);
                        player.getInterfaceState().openTab(2, new Component(22697));
                        player.saveAttribute("quest_tab", 22697);
                        return true;
                    /*
                     * Opens quest tab.
                     */
                    case 23239:
                        player.getInterfaceState().openTab(2, new Component(638));
                        player.saveAttribute("quest_tab", 638);
                        return true;
                    /*
                     * Opens achievement tab.
                     */
                    case 23238:
                        player.getInterfaceState().openTab(2, new Component(29300));
                        player.saveAttribute("quest_tab", 29300);
                        return true;
                    /*
                     * Teleports to a minigame.
                     */
                    default:
                        // Minigame handle
                        for (MinigameTeleport minigameTeleport : MinigameTeleport.values()) {
                            if ((23241 + minigameTeleport.ordinal()) == button) {
                                if (minigameTeleport.getLocation() == null) {
                                    player.getActionSender().sendMessage("Coming soon.");
                                    return true;
                                }
                                player.getTeleporter().send(minigameTeleport.getLocation(), Teleport.TeleportType.HOME);
                                return true;
                            }
                        }
                }
                break;

            /*
             * Achievement tab.
             */
            case 29300:
                switch (button) {
                    /*
                     * Opens information tab.
                     */
                    case 22695:
                        sendDefaultInformation(player);
                        player.getInterfaceState().openTab(2, new Component(22697));
                        player.saveAttribute("quest_tab", 22697);
                        return true;
                    /*
                     * Opens quest tab.
                     */
                    case 29303:
                        player.getInterfaceState().openTab(2, new Component(638));
                        player.saveAttribute("quest_tab", 638);
                        return true;
                    /*
                     * Opens minigame tab.
                     */
                    case 29304:
                        player.getInterfaceState().openTab(2, new Component(23235));
                        player.saveAttribute("quest_tab", 23235);
                        return true;
                    /*
                     * Views an achievement.
                     */
                    default:
                        AchievementDiary diary = player.getAchievementRepository().forId(button);
                        if (diary != null) {
                            diary.update();
                            //player.getInterfaceState().open(new Component(46750));
                            return true;
                        }
                }
                break;

            /*
             * Quest tab.
             */
            case 638:
                switch (button) {
                    case 22694:
                        sendDefaultInformation(player);
                        player.getInterfaceState().openTab(2, new Component(22697));
                        player.saveAttribute("quest_tab", 22697);
                        return true;
                    /*
                     * Opens achievement tab.
                     */
                    case 29157:
                        player.getInterfaceState().openTab(2, new Component(29300));
                        player.saveAttribute("quest_tab", 29300);
                        return true;
                    /*
                     * Opens minigame tab.
                     */
                    case 29270:
                        player.getInterfaceState().openTab(2, new Component(23235));
                        player.saveAttribute("quest_tab", 23235);
                        return true;
                    /*
                     * Views a quest.
                     */
                    default:
                        player.getInterfaceState().close();
                        Quest quest = player.getQuestRepository().getQuestIndex(button);
                        if (quest != null) {
                            quest.update();
                            player.getQuestMenuManager().send();
                            return true;
                        }
                        return false;
                }
        }
        return false;
    }

    /**
     * Represents a minigame teleport.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum MinigameTeleport {

        CASTLE_WARS(new Location(2442, 3089, 0, 3)),
        CLAN_WARS(null),
        BARBARIAN_ASSAULT(null),
        //new Location(2519, 3570, 0, 1)
        BARROWS(new Location(3566, 3316, 0)),
        BURTHORPE_GAMES_ROOM(new Location(2208, 4939, 0, 1)),
        DUEL_ARENA(new Location(3317, 3234, 0, 5)),
        PEST_CONTROL(new Location(2659, 2676, 0)),
        TZHAAR_FIGHT_CAVE(new Location(2439, 5171, 0, 3)),
        TZHAAR_FIGHT_PIT(new Location(2399, 5178, 0, 1)),
        WARRIORS_GUILD(new Location(2882, 3547, 0, 4));

        /**
         * The {@link org.gielinor.game.world.map.Location} of the Minigame.
         */
        private final Location location;

        /**
         * Creates a new <code>MinigameTeleport</code>.
         *
         * @param location The location of the minigame.
         */
        MinigameTeleport(Location location) {
            this.location = location;
        }

        /**
         * Gets the {@link org.gielinor.game.world.map.Location} of the Minigame.
         *
         * @return The location.
         */
        public Location getLocation() {
            return location;
        }
    }

}
