package org.gielinor.game.content.global.travel;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;

import plugin.interaction.object.KingGjukiChestPlugin;

/**
 * Handles the entity teleport.
 *
 * @author SonicForce41
 * TODO Remove checks for VARROCK_PALACE_DEED
 */
public final class Teleport {

    /**
     * The wildy teleport type.
     */
    public static final int WILDY_TELEPORT = 1 << 16 | 8;

    /**
     * The <b>Entity</b> for this Teleporter
     */
    private Entity entity;

    /**
     * The last teleport of this <b>Entity</b>
     */
    private Pulse lastTeleport;

    /**
     * The current teleport of this <b>Entity</b>
     */
    private Pulse currentTeleport;

    /**
     * Constructs a new {@code Teleporter.java} {@Code Object}.
     *
     * @param entity the Entity
     */
    public Teleport(Entity entity) {
        this.entity = entity;
        lastTeleport = TeleportType.HOME.getPulse(entity, GameConstants.HOME_LOCATION);
    }

    /**
     * Sends the teleport.
     *
     * @param location The location.
     * @return <code>True</code> if the player succesfully started teleporting.
     */
    public boolean send(Location location) {
        if (entity.getAttribute("magic-delay", 0) > World.getTicks()) {
            return false;
        }
        if (!(entity instanceof Player)) {
            return send(location, TeleportType.NORMAL);
        }
        Player player = (Player) entity;
        TeleportType teleportType = player.getSpellBookManager().getSpellBook() ==
            SpellBookManager.SpellBook.MODERN.getInterfaceId()
            ? TeleportType.NORMAL : player.getSpellBookManager().getSpellBook() ==
            SpellBookManager.SpellBook.LUNAR.getInterfaceId() ? TeleportType.LUNAR : TeleportType.ANCIENT;
        if (send(location, teleportType, 0)) {
            player.setAttribute("magic-delay", World.getTicks() + 4);
            return true;
        }
        return false;
    }

    /**
     * Sends the teleport.
     *
     * @param location the Location.
     * @param type     the NodeType.
     * @return <code>True</code> if the player succesfully started teleporting.
     */
    public boolean send(Location location, TeleportType type) {
        return send(location, type, 0);
    }

    /**
     * Sends the teleport.
     *
     * @param location     the Location.
     * @param type         the NodeType.
     * @param teleportType The teleporting type. (0=spell, 1=item, 2=object, 3=npc -1= force)
     * @return <code>True</code> if the player succesfully started teleporting.
     */
    public boolean send(Location location, TeleportType type, int teleportType) {
        if ((teleportType != WILDY_TELEPORT && type != TeleportType.OBELISK)
            && !entity.getZoneMonitor().teleport(teleportType, null)) {
            return false;
        }
        if (teleportType != -1) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                p.getDialogueInterpreter().close();
            }
        }
        entity.lock(4);
        entity.getWalkingQueue().reset();
        lastTeleport = currentTeleport;
        currentTeleport = type.getPulse(entity, location);
        if (type == TeleportType.HOME) {
            entity.getPulseManager().run(type.getPulse(entity, location));
        } else {
            entity.getImpactHandler().setDisabledTicks(teleportType == -1 ? 5 : 12);
            World.submit(currentTeleport);
            if (entity instanceof Player && entity.inCombat() && entity.getSkills().getLifepoints() == 1) {
                AchievementDiary.finalize(((Player) entity), AchievementTask.ESCAPE_ARTIST);
            }
        }
        return true;
    }

    /**
     * Gets the entity.
     *
     * @return the Entity
     */
    public final Entity getEntity() {
        return entity;
    }

    /**
     * Gets the last teleport pulse.
     *
     * @return the Pulse
     */
    public final Pulse getLastTeleport() {
        return lastTeleport;
    }

    /**
     * Gets the current teleport pulse.
     *
     * @return the Pulse
     */
    public final Pulse getCurrentTeleport() {
        return currentTeleport;
    }

    /**
     * Represents a NodeType for Teleporter
     *
     * @author SonicForce41
     */
    public enum TeleportType {

        /**
         * The value types
         */
        NORMAL(new TeleportSettings(8939, 8941, 1576, 1577)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(202);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            //} else if (delay == 2) {
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(201);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                    }
                };
            }
        },
        ANCIENT(new TeleportSettings(1979, -1, 392, 455)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(1048);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 4) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 5) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        },
        LUNAR(new TeleportSettings(1816, -1, 747, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.graphics(new Graphics(getSettings().getStartGfx(), 120));
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                        } else if (delay == 3) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        },
        TELETABS(new TeleportSettings(4731, -1, 678, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(979);
                            }
                            entity.getAnimator().forceAnimation(new Animation(4069));
                        } else if (delay == 2) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote()));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        entity.graphics(new Graphics(-1));
                    }
                };
            }
        },
        HOME(new TeleportSettings(4847, 4857, 800, 804)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int count;
                    Player player;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendAnimation(4847);
                                player.getActionSender().sendGraphic(800);
                                ((Player) entity).getAudioManager().send(193);
                                break;
                            case 6:
                                player.getActionSender().sendGraphic(801);
                                player.getActionSender().sendAnimation(4850);
                                ((Player) entity).getAudioManager().send(194);
                                break;
                            case 8:
                                player.getActionSender().sendGraphic(802);
                                player.getActionSender().sendAnimation(4853);
                                break;
                            case 12:
                                //player.getActionSender().sendGraphic(803); //this graphic is now somehow wrong..?
                                player.getActionSender().sendAnimation(4855);
                                ((Player) entity).getAudioManager().send(195);
                                break;
                            case 16:
                                player.getActionSender().sendGraphic(804);
                                player.getActionSender().sendAnimation(4857);
                                ((Player) entity).getAudioManager().send(196);
                                break;
                            case 20:
                                if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                    if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                        ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                        ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                    }
                                }
                                player.getProperties().setTeleportLocation(location);
                                return true;
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void start() {
                        player = ((Player) entity);
//						if (player.getSavedData().getGlobalData().getHomeTeleportDelay() > System.currentTimeMillis()) {
                        //							long milliseconds = player.getSavedData().getGlobalData().getHomeTeleportDelay() - System.currentTimeMillis();
                        //							int minutes = (int) Math.round(milliseconds / 60000);
                        //							if (minutes != 0) {
                        //								player.getActionSender().sendMessage("You need to wait " + minutes + " " + (minutes == 1 ? "minute" : "minutes") + " to cast this spell.");
                        //								stop();
                        //								return;
                        //							}
                        //						}
                        super.start();
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.getAnimator().forceAnimation(new Animation(-1));
                        player.graphics(new Graphics(-1));
                    }
                };
            }
        },
        OBELISK(new TeleportSettings(8939, 8941, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.lock();
                            entity.getAnimator().forceAnimation(new Animation(1816));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(Animation.RESET);
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        },
        TELE_OTHER(new TeleportSettings(1816, -1, 342, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(199);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        },
        FAIRY_RING(new TeleportSettings(-1, -1, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                entity.graphics(Graphics.create(569));
                return new Pulse(1, entity) {

                    int delay;

                    @Override
                    public boolean pulse() {
                        switch (++delay) {
                            case 2:
                                entity.animate(Animation.create(3265));
                                break;
                            case 4:
//                                Quest quest = null;
//                                if (entity instanceof Player) {
//                                    quest = ((Player) entity).getQuestRepository().getQuest(LostCity.NAME);
//                                }
//                                if (quest != null) {
//                                    if (quest.getStage() == 21) {
//                                        quest.finish();
//                                    }
//                                }
                                entity.animate(Animation.create(3266));
                                entity.getProperties().setTeleportLocation(location);
                                entity.unlock();
                                entity.lock(2);
                                return true;
                        }
                        return false;
                    }

                };
            }
        },
        PURO_PURO(new TeleportSettings(6601, 1118, -1, -1)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(1118));
                        } else if (delay == 9) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            entity.getAnimator().forceAnimation(new Animation(-1));
                            entity.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                };
            }
        },
        ECTOPHIAL(new TeleportSettings(8939, 8941, 1587, 1588)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                //  entity.asPlayer().getAudioManager().send(200);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            if (entity instanceof Player && entity.getLocation().inArea(new ZoneBorders(2371, 3785, 2440, 3840))) {
                                if (((Player) entity).getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                                    ((Player) entity).getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                                    ((Player) entity).getDialogueInterpreter().sendPlaneMessage(false, "The Varrock Palace deed crumbles due to the teleport!");
                                }
                            }
                            entity.getProperties().setTeleportLocation(Location.create(location));
                            // TODO Lost and found
                            //fireRandom(entity, location);
                        } else if (delay == 4) {
                            if (entity instanceof Player) {
                                // entity.asPlayer().getAudioManager().send(201);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        entity.unlock();
                        entity.lock(4);
                    }
                };
            }
        },

        KOUREND_ALTAR(new TeleportSettings(8939, 8941, 1576, 1577)) {
            @Override
            public Pulse getPulse(final Entity entity, final Location location) {
                return new Pulse(1) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 0) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(202);
                            }

                            entity.getAnimator().forceAnimation(new Animation(getSettings().getStartEmote()));
                            //} else if (delay == 2) {
                            entity.graphics(new Graphics(getSettings().getStartGfx()));
                        } else if (delay == 3) {
                            entity.getProperties().setTeleportLocation(Location.create(location));
                        } else if (delay == 4) {
                            if (entity instanceof Player) {
                                ((Player) entity).getAudioManager().send(201);
                            }
                            entity.getAnimator().forceAnimation(new Animation(getSettings().getEndEmote(), Priority.HIGH));
                            entity.graphics(new Graphics(getSettings().getEndGfx()));
                            return true;
                        }
                        delay++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                    }
                };
            }
        },
        ;

        /**
         * The NodeSettings
         */
        private TeleportSettings settings;

        /**
         * @param entity
         * @param location
         * @return the Pulse
         */
        public abstract Pulse getPulse(final Entity entity, final Location location);

        /**
         * Constructs a new {@code Teleporter.java} {@Code Object}.
         *
         * @param settings the NodeSettings
         */
        TeleportType(TeleportSettings settings) {
            this.settings = settings;
        }

        /**
         * Gets the NodeSettings
         *
         * @return the NodeSettings
         */
        public final TeleportSettings getSettings() {
            return settings;
        }
    }

    /**
     * Represents teleport node settings
     *
     * @author SonicForce41
     */
    static class TeleportSettings {

        /**
         * The start animation.
         */
        private int startAnim;

        /**
         * The end animation.
         */
        private int endAnim;

        /**
         * The start graphics.
         */
        private int startGFX;

        /**
         * The end gfx.
         */
        private int endGFX;

        /**
         * Constructs a new {@code Teleporter.java} {@code Object}.
         *
         * @param startAnim the start animation.
         * @param endAnim   the end animation.
         * @param startGfx  the start graphics.
         * @param endGfx    the end graphiics.
         */
        public TeleportSettings(int startAnim, int endAnim, int startGfx, int endGfx) {
            this.startAnim = startAnim;
            this.endAnim = endAnim;
            this.startGFX = startGfx;
            this.endGFX = endGfx;
        }

        /**
         * @return the anim.
         */
        public final int getStartEmote() {
            return startAnim;
        }

        /**
         * @return the anim.
         */
        public final int getEndEmote() {
            return endAnim;
        }

        /**
         * @return the start graphics.
         */
        public final int getStartGfx() {
            return startGFX;
        }

        /**
         * @return the end gfx.
         */
        public final int getEndGfx() {
            return endGFX;
        }
    }

    /**
     * Gets the teleporting type for the player depending on his/her current spellbook.
     *
     * @param player The player.
     * @return The teleport type.
     */
    public static TeleportType getType(Player player) {
        switch (player.getSpellBookManager().getSpellBook()) {
            case 12855:
                return TeleportType.ANCIENT;
            case 29999:
                return TeleportType.LUNAR;
        }
        return TeleportType.NORMAL;
    }
}
