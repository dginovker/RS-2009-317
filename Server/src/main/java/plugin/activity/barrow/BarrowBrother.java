package plugin.activity.barrow;

import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;

/**
 * Handles a barrow brother NPC.
 *
 * @author Emperor
 */
public final class BarrowBrother extends NPC {

    /**
     * The player to target.
     */
    private final Player player;

    /**
     * Constructs a new {@code BarrowBrother} {@code Object}.
     *
     * @param player   The target.
     * @param id       The NPC id.
     * @param location The location.
     */
    public BarrowBrother(Player player, int id, Location location) {
        super(id, location);
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        super.setRespawn(false);
        if (location.getZ() == 3) {
            sendChat("You dare disturb my rest!");
        } else {
            sendChat("You dare steal from us!");
        }
        getProperties().getCombatPulse().attack(player);
        HintIconManager.registerHintIcon(player, this);
    }

    @Override
    public void handleTickActions() {
        if (DeathTask.isDead(player)) {
            return;
        }
        if (!player.isActive() || !player.getLocation().withinDistance(location)) {
            clear();
            return;
        }
        if (!getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        int itemId = -1;
        switch (getId()) {
            case 2025:
                itemId = Item.AHRIM_ASHES;
                break;
            case 2026:
                itemId = Item.DHAROK_ASHES;
                break;
            case 2027:
                itemId = Item.GUTHAN_ASHES;
                break;
            case 2028:
                itemId = Item.KARIL_ASHES;
                break;
            case 2029:
                itemId = Item.TORAG_ASHES;
                break;
            case 2030:
                itemId = Item.VERAC_ASHES;
                break;
        }
        if (itemId > -1 && !player.getInventory().contains(itemId) && !player.getBank().contains(itemId) &&
            player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 3) {
            player.getActionSender().sendPositionedGraphic(671, 0, 0, getLocation());
            GroundItemManager.create(new GroundItem(new Item(itemId), getLocation(), player));
        }
        super.finalizeDeath(killer);
        if (killer == player) {
            player.getSavedData().getActivityData().getBarrowsKilled()[getBrotherIndex()] = true;
            BarrowsActivityPlugin.sendConfiguration(player);
        }
    }

    @Override
    public void clear() {
        super.clear();
        if (player.isActive()) {
            player.getHintIconManager().clear();
        }
        player.removeAttribute("barrow:npc");
        player.removeAttribute("brother:" + getBrotherIndex());
    }

    /**
     * Gets the barrow brother index.
     *
     * @return The index.
     */
    private int getBrotherIndex() {
        return getId() - 2025;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }
}
