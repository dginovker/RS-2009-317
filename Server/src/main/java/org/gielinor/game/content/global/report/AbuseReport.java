package org.gielinor.game.content.global.report;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents an abuse report to file.
 *
 * @author 'Vexia
 */
public final class AbuseReport {

    /**
     * The name of the reporter.
     */
    private final String reporter;

    /**
     * The victims name.(recieved by packet?).
     */
    private final String victim;

    /**
     * The rule broken by the {@link #victim}.
     */
    private final Rule rule;

    /**
     * Constructs a new {@code AbuseReport {@code Object}.
     *
     * @param reporter the reporter.
     * @param victim   the victim.
     * @param rule     the rule broken.
     */
    public AbuseReport(String reporter, String victim, Rule rule) {
        this.reporter = reporter;
        this.victim = victim;
        this.rule = rule;
    }

    /**
     * Constructs the report and dispatches it to the database.
     *
     * @param player the player.
     */
    public void construct(Player player, boolean mute) {
        /*Player target = Repository.getPlayerByName(victim);
        if (target == null) {
            target = PlayerLoader.getPlayerFile(victim);
        }
//		final List<ChatEntry> records = ChatPacket.getChatEntryLogger().getOrganized(player, target);
        player.getActionSender().sendMessage("Thank-you, your abuse report has been received.");
        if (target == null) {
            return;
        }
//		MysqlManager.getGlobalProtocol().send(player.getDetails(), new ReportStatement(),
// player.getUsername(), target.getUsername(), mute, records);
        // TODO SQL
        if (!new File("reports.txt").exists()) {
            try {
                new File("reports.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int ruleId = rule == null ? -1 : rule.ordinal();
        String append = player.getUsername() + " : " + target.getUsername() + " " + ruleId + "\r\n";
        try {
            FileUtils.writeStringToFile(new File("reports.txt"), append, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean moderator = player.getDetails().getRights().equals(Rights.PLAYER_MODERATOR) || player.getDetails().getRights().isAdministrator();
        if (moderator && mute) {
            target.getDetails().getPortal().getMute().punish(target, 2);
            target.getDetails().save();
        }*/
    }

    /**
     * Gets the reporter.
     *
     * @return The reporter.
     */
    public String getReporter() {
        return reporter;
    }

    /**
     * Gets the victim.
     *
     * @return The victim.
     */
    public String getVictim() {
        return victim;
    }

    /**
     * Gets the rule.
     *
     * @return The rule.
     */
    public Rule getRule() {
        return rule;
    }

}
