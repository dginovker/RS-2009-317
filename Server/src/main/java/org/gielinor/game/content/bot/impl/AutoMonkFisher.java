package org.gielinor.game.content.bot.impl;

import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.bot.BotMessageListener;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A script for fishing monkfish.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AutoMonkFisher extends Bot implements BotMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AutoMonkFisher.class);

    /**
     * Whether or not we're fishing.
     */
    private boolean fishing;

    /**
     * The amount of fish caught.
     */
    private int fishCaught = 0;

    /**
     * The amount of experience gained;
     */
    private long experienceGained = 0;

    /**
     * Creates the monster fighter instance.
     */
    public AutoMonkFisher() {
        super(null);
    }

    /**
     * Creates the monster fighter instance.
     *
     * @param player The player.
     */
    public AutoMonkFisher(Player player) {
        super(player);
        this.fishing = false;
    }

    @Override
    public void messageReceived(String message) {
        if (message.toLowerCase().contains("you catch a ")) {
            fishCaught += 1;
        }
    }

    @Override
    public boolean onStart() {
        super.onStart();
        World.submit(new Pulse(1, getPlayer()) {

            NPC npc = getMethod().getClosestNpc(3848);
            NPC banker = getMethod().getClosestNpc(3824);

            @Override
            public boolean pulse() {
                if (isToEnd()) {
                    return true;
                }
                if (isPaused()) {
                    this.setDelay(5);
                    return false;
                }
                if (getMethod().isBankOpen()) {
                    return false;
                }
                if (!getPlayer().getInventory().contains(20303)) {
                    log.debug("Withdrawing fishing net from bank.");
                    fishing = false;
                    if (banker == null) {
                        end();
                        return true;
                    }
                    pause();
                    getMethod().walkTo(banker.getLocation());
                    World.submit(new Pulse(5) {

                        @Override
                        public boolean pulse() {
                            if (getPlayer().getLocation().getDistance(banker.getLocation()) > 1) {
                                if (!getPlayer().getWalkingQueue().isMoving()) {
                                    getMethod().walkTo(banker.getLocation());
                                }
                                return false;
                            }
                            if (getPlayer().getInventory().contains(20303)) {
                                unpause();
                                return true;
                            }
                            if (banker.getInteraction().get(3) == null || banker.getInteraction().get(3).getHandler() == null) {
                                return false;
                            }
                            banker.getInteraction().get(3).getHandler().handle(getPlayer(), banker, "bank");
                            World.submit(new Pulse(3) {

                                @Override
                                public boolean pulse() {
                                    if (!getMethod().isBankOpen()) {
                                        return false;
                                    }
                                    if (!getPlayer().getBank().contains(20303)) {
                                        log.debug("No small fishing net in bank. Stopping script.");
                                        end();
                                        return true;
                                    }
                                    if (!getPlayer().getInventory().contains(20303)) {
                                        for (int i = 0; i < getPlayer().getInventory().toArray().length; i++) {
                                            if (getPlayer().getInventory().get(i) == null || getPlayer().getInventory().get(i).getId() == -1) {
                                                continue;
                                            }
                                            getPlayer().getBank().addItem(i, getPlayer().getInventory().getCount(getPlayer().getInventory().get(i)), null);
                                        }
                                        getMethod().withdrawItem(20303);
                                        return false;
                                    }
                                    World.submit(new Pulse(3) {

                                        @Override
                                        public boolean pulse() {
                                            if (isPaused()) {
                                                unpause();
                                            }
                                            return true;
                                        }
                                    });
                                    return true;
                                }
                            });
                            return getPlayer().getInventory().contains(20303);
                        }
                    });
                    return false;
                }
                if (getPlayer().getInventory().freeSlots() == 0) {
                    log.debug("Banking fish.");
                    fishing = false;
                    if (banker == null) {
                        banker = getMethod().getClosestNpc(3824);
                        if (banker == null) {
                            log.debug("Unable to find banker.");
                            end();
                        }
                        return true;
                    }
                    pause();
                    getMethod().walkTo(banker.getLocation());
                    World.submit(new Pulse(5) {

                        @Override
                        public boolean pulse() {
                            if (getPlayer().getLocation().getDistance(banker.getLocation()) > 1) {
                                if (!getPlayer().getWalkingQueue().isMoving()) {
                                    getMethod().walkTo(banker.getLocation());
                                }
                                return false;
                            }
                            if (getPlayer().getInventory().freeSlots() == 27) {
                                unpause();
                                return true;
                            }
                            if (banker.getInteraction().get(3) == null || banker.getInteraction().get(3).getHandler() == null) {
                                return false;
                            }
                            banker.getInteraction().get(3).getHandler().handle(getPlayer(), banker, "bank");
                            World.submit(new Pulse(3) {

                                @Override
                                public boolean pulse() {
                                    if (!getMethod().isBankOpen()) {
                                        return false;
                                    }
                                    if (getPlayer().getInventory().contains(7944)) {
                                        getMethod().bankAllExcept(20303);
                                        return false;
                                    }
                                    World.submit(new Pulse(3) {

                                        @Override
                                        public boolean pulse() {
                                            if (isPaused()) {
                                                unpause();
                                            }
                                            return true;
                                        }
                                    });
                                    return true;
                                }
                            });
                            return getPlayer().getInventory().freeSlots() == 27;
                        }
                    });
                    return false;
                }
                if (npc == null) {
                    npc = getMethod().getClosestNpc(3848);
                    return false;
                }
                if (getPlayer().getLocation().getDistance(npc.getLocation()) > npc.getDefinition().getSize()) {
                    getMethod().walkTo(npc.getLocation().getSouth());
                    this.setDelay((int) (npc.getLocation().getSouth().getDistance(getPlayer().getLocation()) + 1));
                    log.debug("Walking to fishing spot.");
                    return false;
                }
                if (fishing) {
                    return false;
                }
                // FISH
                this.setDelay(2);
                getPlayer().face(npc);
                npc.getInteraction().get(2).getHandler().handle(getPlayer(), npc, "net");
                log.debug("Starting fishing.");
                fishing = true;
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
        return "Fishing";
    }

    @Override
    public String getScriptName() {
        return "AutoMonkFisher";
    }

    @Override
    public String getDescription() {
        return "Fishes Monkfish in Piscatoris.";
    }

    @Override
    public void end() {
        super.end();
        log.debug("Caught a total of {} fish.", fishCaught);
    }

    @Override
    public boolean additional() {
        return false;
    }

    @Override
    public void setAdditional(Object[] additional) {
    }

}
