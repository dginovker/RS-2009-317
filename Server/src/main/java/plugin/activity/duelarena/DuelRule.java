package plugin.activity.duelarena;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * Represents a duel arena rule.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Get OSRS messages for enforce()
 */
public enum DuelRule {

    NO_RANGE(5, "Players cannot use range!", 48012) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            if (NO_MELEE.isActivated(player) && NO_MAGIC.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot use ranged attacks during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_MELEE(6, "Players cannot use melee!", 48017) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            if (NO_RANGE.isActivated(player) && NO_MAGIC.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot use melee attacks during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_MAGIC(7, "Players cannot use mage!", 48022) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            if (NO_MELEE.isActivated(player) && NO_RANGE.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot use magic attacks during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    FUN_WEAPONS(9, "'Fun weapons' will be allowed.", 48032) {
        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_SPECIAL(8, "Players cannot use special attacks!", 48027) {
        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot use special attacks during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_FORFEIT(10, "Players cannot forfeit!", 48037) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            if (NO_MOVEMENT.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have no forfeit and no movement.");
                }
                return false;
            } else if (NO_MELEE.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have no forfeit and no melee.");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_DRINKS(12, "Players cannot use potions!", 48047) {
        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot drink anything during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_FOOD(13, "Players cannot use potions!", 48052) {
        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot eat anything during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_PRAYER(11, "Players cannot use prayer!", 48042) {
        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot use prayers during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    NO_MOVEMENT(14, "Players cannot walk!", 48057) {
        @Override
        public boolean canActivate(Player player, boolean message) {
            if (OBSTACLES.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have No Movement in an arena with obstacles.");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            if (this.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You cannot move during this duel!");
                }
                return true;
            }
            return false;
        }
    },
    OBSTACLES(15, "There will be obstacles in the arena.", 48062) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Obstacles ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Obstacles OFF!");
            }
        }

        @Override
        public boolean canActivate(Player player, boolean message) {
            if (NO_MOVEMENT.isActivated(player)) {
                if (message) {
                    player.getActionSender().sendMessage("You can't have obstacles if you want No Movement.");
                }
                return false;
            }
            return super.canActivate(player, message);
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_HEAD(16, null, 48074, Equipment.SLOT_HAT) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Head Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Head Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_CAPE(17, null, 48077, Equipment.SLOT_CAPE) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Back Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Back Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_AMULET(18, null, 48080, Equipment.SLOT_AMULET) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Neck Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Neck Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_ARROW(19, null, 48083, Equipment.SLOT_AMMO) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Ammo Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Ammo Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_WEAPON(20, null, 48086, Equipment.SLOT_WEAPON) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Right hand Slot ON!");
            }
            player.getActionSender().sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Right hand Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_BODY(21, null, 48089, Equipment.SLOT_CHEST) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Torso Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Torso Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_SHIELD(22, null, 48092, Equipment.SLOT_SHIELD) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Left hand Slot ON!");
            }
            player.getActionSender().sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Left hand Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_LEGS(23, null, 48095, Equipment.SLOT_LEGS) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Leg Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Leg Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_GLOVES(24, null, 48098, Equipment.SLOT_HANDS) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Hand Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Hand Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_BOOTS(25, null, 48101, Equipment.SLOT_FEET) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Feet Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Feet Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },
    NO_RINGS(26, null, 48104, Equipment.SLOT_RING) {
        @Override
        public void activate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Ring Slot ON!");
            }
        }

        @Override
        public void deactivate(Player player, boolean message) {
            if (message) {
                player.getActionSender().sendMessage("Duel Option changed - Disable Ring Slot OFF!");
            }
        }

        @Override
        public boolean enforce(Player player, boolean message) {
            return this.isActivated(player);
        }
    },;
    /**
     * The config index.
     */
    private final int configIndex;
    /**
     * The information to display on the second screen.
     */
    private final String info;
    /**
     * The button id.
     */
    private final int buttonId;
    /**
     * The equipment slot to remove upon starting the duel.
     */
    private int equipmentSlot;

    /**
     * Constructs a new {@code DuelRule} {@code Object}.
     *
     * @param configIndex The config index.
     */
    private DuelRule(int configIndex, String info, int buttonId) {
        this(configIndex, info, buttonId, -1);
    }

    /**
     * Constructs a new {@code DuelRule} {@code Object}.
     *
     * @param configIndex The config index.
     */
    private DuelRule(int configIndex, String info, int buttonId, int equipmentSlot) {
        this.configIndex = configIndex;
        this.info = info;
        this.buttonId = buttonId;
        this.equipmentSlot = equipmentSlot;
    }

    /**
     * Gets the configIndex.
     *
     * @return The configIndex.
     */
    public int getConfigIndex() {
        return configIndex;
    }

    /**
     * Gets the info.
     *
     * @return The info.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Gets the button id.
     *
     * @return The button id.
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Gets the equipment slot to remove.
     *
     * @return The slot.
     */
    public int getEquipmentSlot() {
        return equipmentSlot;
    }

    /**
     * The rule to enforce when the player tries to perform an action with this rule activated.
     *
     * @param player The player.
     * @return Whether or not the rule is enforced.
     */
    public abstract boolean enforce(Player player, boolean message);

    /**
     * The action upon activation.
     *
     * @param player  The player.
     * @param message If the message should be sent.
     */
    public void activate(Player player, boolean message) {

    }

    /**
     * The action upon deactivation.
     *
     * @param player  The player.
     * @param message If the message should be sent.
     */
    public void deactivate(Player player, boolean message) {

    }

    /**
     * If this rule can be activated.
     *
     * @param player  The player.
     * @param message If the message should be sent.
     * @return <code>True</code> if the message should be sent.
     */
    public boolean canActivate(Player player, boolean message) {
        return true;
    }

    /**
     * Whether or not this rule is activated.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public boolean isActivated(Player player) {
        DuelSession duelSession = player.getExtension(DuelSession.class);
        return duelSession != null && duelSession.getDuelRules().contains(this);
    }

    /**
     * Gets a {@link plugin.activity.duelarena.DuelRule} by the button id.
     *
     * @param buttonId The id of the button.
     * @return The {@code DuelRule}.
     */
    public static DuelRule forId(int buttonId) {
        for (DuelRule duelRule : DuelRule.values()) {
            if (duelRule.buttonId == buttonId) {
                return duelRule;
            }
        }
        return null;
    }
}