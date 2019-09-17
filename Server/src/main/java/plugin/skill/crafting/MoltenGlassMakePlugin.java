package plugin.skill.crafting;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to make molten glass.
 *
 * @author 'Vexia
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class MoltenGlassMakePlugin extends UseWithHandler {

    /**
     * Represents the bucket of sand item.
     */
    private static final Item BUCKET_OF_SAND = new Item(1783);

    /**
     * Represents the soda ash item.
     */
    private static final Item SODA_ASH = new Item(1781);

    /**
     * Represents the molten glass item.
     */
    private static final Item MOLTEN_GLASS = new Item(1775);

    /**
     * Represents the bucket item.
     */
    private static final Item BUCKET = new Item(1925);

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(899);

    /**
     * Constructs a new {@code MoltenGlassMakePlugin} {@code Object}.
     */
    public MoltenGlassMakePlugin() {
        super(1783, 1781);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2966, OBJECT_TYPE, this);
        addHandler(3044, OBJECT_TYPE, this);
        addHandler(4304, OBJECT_TYPE, this);
        addHandler(6189, OBJECT_TYPE, this);
        addHandler(11010, OBJECT_TYPE, this);
        addHandler(11666, OBJECT_TYPE, this);
        addHandler(12100, OBJECT_TYPE, this);
        addHandler(12809, OBJECT_TYPE, this);
        addHandler(18497, OBJECT_TYPE, this);
        addHandler(26814, OBJECT_TYPE, this);
        addHandler(30021, OBJECT_TYPE, this);
        addHandler(30510, OBJECT_TYPE, this);
        addHandler(36956, OBJECT_TYPE, this);
        addHandler(37651, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        World.submit(new Pulse(1) {

            @Override
            public boolean pulse() {
                if (player == null) {
                    this.stop();
                    return true;
                }
                if (!player.getInventory().contains(1783, 1)) {
                    this.stop();
                    player.getActionSender().sendMessage("You have run out of sand.");
                    return true;
                }
                if (!player.getInventory().contains(1781, 1)) {
                    this.stop();
                    player.getActionSender().sendMessage("You have run out of soda ash.");
                    return true;
                }
                player.playAnimation(ANIMATION);
                player.lock(2);
                player.getActionSender().sendMessage("You heat the sand and soda ash in the furnace to make glass.", 1);
                player.getInventory().remove(BUCKET_OF_SAND, SODA_ASH);
                player.getInventory().add(MOLTEN_GLASS, BUCKET);
                player.getSkills().addExperience(Skills.CRAFTING, 20);
                this.setDelay(2);
                return false;
            }
        });
        return true;
    }

}
