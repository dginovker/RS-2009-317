package org.gielinor.game.content.bot.impl;

import java.security.SecureRandom;

import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.bot.BotMessageListener;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;

/**
 * A simple script for pickpocketing the master farmer in Draynor.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AutoMasterThiever extends Bot implements BotMessageListener {

    /**
     * The amount of experience gained;
     */
    private long experienceGained = 0;

    /**
     * Creates the master thiever instance.
     */
    public AutoMasterThiever() {
        super(null);
    }

    /**
     * Creates the master thiever instance.
     *
     * @param player The player.
     */
    public AutoMasterThiever(Player player) {
        super(player);
    }

    @Override
    public void messageReceived(String message) {
        if (message.toLowerCase().contains("you pick the master ")) {
            experienceGained += 860;
        }
    }

    @Override
    public boolean onStart() {
        super.onStart();
        World.submit(new Pulse(6, getPlayer()) {

            NPC npc = getMethod().getClosestNpc("Master Farmer");

            @Override
            public boolean pulse() {
                if (isToEnd()) {
                    return true;
                }
                if (isPaused()) {
                    return false;
                }
                if (npc == null) {
                    npc = getMethod().getClosestNpc("Master Farmer");
                    return false;
                }
                if (DeathTask.isDead(npc)) {
                    npc = getMethod().getClosestNpc("Master Farmer");
                    return false;
                }
                if (npc.getInteraction().get(2) == null || npc.getInteraction().get(2).getHandler() == null) {
                    npc = getMethod().getClosestNpc("Master Farmer");
                    return false;
                }
                if (getPlayer().getInventory().freeSlots() == 0) {
                    for (int i = 0; i < getPlayer().getInventory().toArray().length; i++) {
                        if (getPlayer().getInventory().get(i) == null || getPlayer().getInventory().get(i).getId() == -1) {
                            continue;
                        }
                        getPlayer().getBank().addItem(i, getPlayer().getInventory().getCount(getPlayer().getInventory().get(i)), null);
                    }
                }
                if (getPlayer().getLocation().getDistance(npc.getLocation()) > 1) {
                    this.setTicksPassed(4);
                    getMethod().walkTo(new SecureRandom().nextInt(2) == 1 ? npc.getLocation().getSouth() : npc.getLocation().getWest());
                    return false;
                }
                npc.getInteraction().get(2).getHandler().handle(getPlayer(), npc, "pickpocket");
                return false;
            }
        });
        return false;
    }


    @Override
    public void onFinish() {

    }

    @Override
    public String getAuthor() {
        return "Gielinor";
    }

    @Override
    public double getVersion() {
        return 1.0D;
    }


    @Override
    public String getCategory() {
        return "Thieving";
    }

    @Override
    public String getScriptName() {
        return "AutoMasterThiever";
    }

    @Override
    public String getDescription() {
        return "Pickpockets master farmers.";
    }

    @Override
    public void end() {
        super.end();
    }

    /**
     * Whether or not the script requires additional entries.
     *
     * @return Whether or not.
     */
    @Override
    public boolean additional() {
        return false;
    }

    /**
     * Sets additional entries.
     *
     * @param additional The additional entries.
     */
    @Override
    public void setAdditional(Object[] additional) {
    }

}
