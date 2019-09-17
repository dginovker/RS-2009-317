package bot.listeners;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listener.server.ServerMemberAddListener;

public class NewGielinorianListener implements ServerMemberAddListener {

    @Override
    public void onServerMemberAdd(DiscordAPI discordAPI, User user, Server server) {
        user.sendMessage("Welcome to Gielinor's Discord! Gielinor is a brilliant RSPS, one where you can really enjoy yourself and make your mark on our community. One of the best ways to do this is to get in-game and start playing!\n" +
            "\n" +
            "Just so you know, your game account is linked to your forum account. This means if you create an account on either the forum or on the game, you can use it to login on both services. This helps keep our community together.\n" +
            "\n" +
            "**Useful Links**\n" +
            "https://gielinor.org\n" +
            "https://gielinor.org/play\n" +
            "https://gielinor.org/community\n" +
            "\n" +
            "Thank you for being a part of our wonderful community, we hope you enjoy it!\n" +
            "\n" +
            "All the best,\n" +
            "The Gielinor Team");
    }
}
