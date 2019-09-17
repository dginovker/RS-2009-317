package plugin.interaction.player;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManifest;
import org.gielinor.rs2.plugin.PluginType;

/**
 * Validates a player login.
 *
 * @author Emperor
 */
@PluginManifest(type = PluginType.LOGIN)
public final class LoginValidationPlugin implements Plugin<Player> {

    @Override
    public Plugin<Player> newInstance(Player player) throws Throwable {
		/*if (player.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("data/mac-addresses.txt"));
			String text;
			List<String> list = new ArrayList<>();
			while ((text = bufferedReader.readLine()) != null) {
				list.add(text.replaceAll("\",", "").replaceAll("\"", ""));
			}
			String[] macAddresses = list.toArray(new String[list.size()]);
			boolean allowed = false;
			for (String mac : macAddresses) {
				if (player.getDetails().getMacAddress().equals(mac)) {
					allowed = true;
					break;
				}
			}
			boolean allowed = true;
			if (World.getSettings().isDevMode()) {
				allowed = true;
			}
			if (!allowed) {
				if (!player.getDetails().getIp().equals("127.0.0.1")) {
					logger.log(Level.INFO, "Invalid moderator : [username={0}, ip={1}, mac={2}]", new Object[] {
							player.getName(), player.getDetails().getIp(), player.getDetails().getMacAddress() });
					player.getActionSender().sendLogout();
					player.getSession().disconnect();
				}
			}
		}*/
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}
