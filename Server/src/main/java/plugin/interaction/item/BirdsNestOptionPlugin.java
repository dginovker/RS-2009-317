package plugin.interaction.item;

import java.security.SecureRandom;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for bird's nests.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BirdsNestOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(5070).getConfigurations().put("option:search", this);
        ItemDefinition.forId(5071).getConfigurations().put("option:search", this);
        ItemDefinition.forId(5072).getConfigurations().put("option:search", this);
        ItemDefinition.forId(5073).getConfigurations().put("option:search", this);
        ItemDefinition.forId(5074).getConfigurations().put("option:search", this);
        //TODO: more seeds
        //ItemDefinition.forId(7413).getConfigurations().put("option:search", this);
        //TODO: ravens egg
        //ItemDefinition.forId(11966).getConfigurations().put("option:search", this);
        return null;
    }

    private final int[] SEED_REWARDS = { 5312, 5313, 5314, 5315, 5316, 5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317 };
    private final int[] RING_REWARDS = { 1635, 1637, 1639, 1641, 1643 };

    /**
     * Searches the nest.
     */

    private boolean searchNest(Player player, int itemId) {
        if (eggNest(player, itemId)) {
            player.getInventory().remove(new Item(itemId, 1));
            player.getInventory().add(new Item(5075, 1));
            return true;
        }
        if (seedNest(player, itemId)) {
            player.getInventory().remove(new Item(itemId, 1));
            player.getInventory().add(new Item(5075, 1));
            return true;
        }
        if (ringNest(player, itemId)) {
            player.getInventory().remove(new Item(itemId, 1));
            player.getInventory().add(new Item(5075, 1));
            return true;
        }
        return false;
    }

    private boolean ringNest(Player player, int itemId) {
        if (itemId == 5074) {
            int random = new SecureRandom().nextInt(1000);
            if (random >= 0 && random <= 340) {
                player.getInventory().add(new Item(RING_REWARDS[0], 1));
            } else if (random >= 341 && random <= 750) {
                player.getInventory().add(new Item(RING_REWARDS[1], 1));
            } else if (random >= 751 && random <= 910) {
                player.getInventory().add(new Item(RING_REWARDS[2], 1));
            } else if (random >= 911 && random <= 989) {
                player.getInventory().add(new Item(RING_REWARDS[3], 1));
            } else if (random >= 990) {
                player.getInventory().add(new Item(RING_REWARDS[4], 1));
            }
            return true;
        }
        return false;
    }

    private boolean seedNest(Player player, int itemId) {
        if (itemId == 5073) {
            int random = new SecureRandom().nextInt(1000);
            if (random >= 0 && random <= 220) {
                player.getInventory().add(new Item(SEED_REWARDS[0], 1));
            } else if (random >= 221 && random <= 350) {
                player.getInventory().add(new Item(SEED_REWARDS[1], 1));
            } else if (random >= 351 && random <= 400) {
                player.getInventory().add(new Item(SEED_REWARDS[2], 1));
            } else if (random >= 401 && random <= 430) {
                player.getInventory().add(new Item(SEED_REWARDS[3], 1));
            } else if (random >= 431 && random <= 440) {
                player.getInventory().add(new Item(SEED_REWARDS[4], 1));
            } else if (random >= 441 && random <= 600) {
                player.getInventory().add(new Item(SEED_REWARDS[5], 1));
            } else if (random >= 601 && random <= 700) {
                player.getInventory().add(new Item(SEED_REWARDS[6], 1));
            } else if (random >= 701 && random <= 790) {
                player.getInventory().add(new Item(SEED_REWARDS[7], 1));
            } else if (random >= 791 && random <= 850) {
                player.getInventory().add(new Item(SEED_REWARDS[8], 1));
            } else if (random >= 851 && random <= 900) {
                player.getInventory().add(new Item(SEED_REWARDS[9], 1));
            } else if (random >= 901 && random <= 930) {
                player.getInventory().add(new Item(SEED_REWARDS[10], 1));
            } else if (random >= 931 && random <= 950) {
                player.getInventory().add(new Item(SEED_REWARDS[11], 1));
            } else if (random >= 951 && random <= 970) {
                player.getInventory().add(new Item(SEED_REWARDS[12], 1));
            } else if (random >= 971 && random <= 980) {
                player.getInventory().add(new Item(SEED_REWARDS[13], 1));
            } else {
                player.getInventory().add(new Item(SEED_REWARDS[0], 1));
            }
            return true;
        }
        return false;
    }

    /**
     * Egg nests
     */
    private boolean eggNest(Player player, int itemId) {
        if (itemId == 5070) {
            player.getInventory().add(new Item(5076, 1));
            return true;
        }
        if (itemId == 5071) {
            player.getInventory().add(new Item(5078, 1));
            return true;
        }
        if (itemId == 5072) {
            player.getInventory().add(new Item(5077, 1));
            return true;
        }
        return false;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!(node instanceof Item)) {
            return false;
        }
        Item nest = new Item(node.getId(), 1);
        if (!player.getInventory().contains(nest)) {
            return true;
        }
        if (player.getAttribute("BUSY") != null) {
            return true;
        }
        player.lock(1);
        if (searchNest(player, node.getId())) {
            player.getActionSender().sendMessage("You search the nest... and find something in it!");
            return true;
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}
