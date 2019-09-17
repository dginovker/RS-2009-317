package plugin.npc;

import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the lumber kittens at the lumber yard.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumberKittenNPC extends AbstractNPC {

    /**
     * Represents if the kitten is hidden.
     */
    private boolean hidden = true;

    /**
     * Represents the next time you can do a speak.
     */
    private int nextSpeak;

    /**
     * Represents the delay of hiding again.
     */
    private int hideDelay;

    /**
     * Constructs a new {@code LumberKittenNPC} {@code Object}.
     */
    public LumberKittenNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code LumberKittenNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private LumberKittenNPC(int id, Location location) {
        super(id, location, false);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new LumberKittenNPC(id, location);
    }

    @Override
    public void init() {
        setWalks(false);
        super.init();
    }

    @Override
    public void tick() {
        if (nextSpeak < World.getTicks()) {
            hidden = false;
            nextSpeak = World.getTicks() + RandomUtil.random(10, 40);
            hideDelay = World.getTicks() + 4;
            sendChat("Mew!");
        }
        if (hideDelay < World.getTicks()) {
            hidden = true;
            int rand = RandomUtil.random(20, 40);
            hideDelay = World.getTicks() + rand;
            nextSpeak = World.getTicks() + rand;
        }
        super.tick();
    }

    @Override
    public boolean isHidden(final Player player) {
        Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
        if (hidden) {
            return true;
        }
        if (quest.getStage() < 20 || quest.getStage() > 50) {
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 767 };
    }

}
