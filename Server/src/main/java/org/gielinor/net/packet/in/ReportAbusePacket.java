package org.gielinor.net.packet.in;

import org.gielinor.game.content.global.report.AbuseReport;
import org.gielinor.game.content.global.report.Rule;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.utilities.misc.PlayerLoader;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the incoming packet to handle a report against a player.
 *
 * @author 'Vexia
 */
public class ReportAbusePacket implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(ReportAbusePacket.class);

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        String target = TextUtils.longToString(buffer.getLong());
        int ruleId = buffer.get();
        final Rule rule = Rule.forId(ruleId);
        final boolean mute = buffer.get() == 1;

        log.info("[{}] submitted abuse report against [{}]. Mute={}, RuleId={}.",
            player.getName(), target, mute, ruleId);

        if (target.equalsIgnoreCase(player.getUsername())) {
            player.getActionSender().sendMessage("You can't report yourself!");
            return;
        }
        AbuseReport abuse = new AbuseReport(player.getUsername(), target, rule);
        if (rule != Rule.BUG_ABUSE && rule != Rule.ACCCOUNT_SHARING &&
            rule != Rule.MACROING && rule != Rule.MULTIPLE_LOGGING &&
            !abuse.getRule().canRequest(Repository.getPlayerByName(target) == null
                ? PlayerLoader.getPlayerFile(target) : Repository.getPlayerByName(target))) {
            player.getActionSender().sendMessage("For that rule you can only report players who have spoken or traded recently.");
            return;
        }
        abuse.construct(player, mute);
    }

}
