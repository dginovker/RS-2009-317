package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the Bunyip familiar.
 *
 * @author Aero
 */
public class BunyipNPC extends Familiar {

    /**
     * Constructs a new {@code BunyipNPC} {@code Object}.
     */
    public BunyipNPC() {
        this(null, 6813);
    }

    /**
     * Constructs a new {@code BunyipNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public BunyipNPC(Player owner, int id) {
        super(owner, id, 4400, 12029, 3);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new BunyipNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        visualize(Animation.create(8288), Graphics.create(1414));//TODO: Swallow whole scroll.
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1306));
    }

    @Override
    protected void handleFamiliarTick() {
    }

    @Override
    protected void configureFamiliar() {
        UseWithHandler.addHandler(6813, UseWithHandler.NPC_TYPE, new UseWithHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(NodeUsageEvent event) {
                //TODO: Raw fish.
                return true;
            }
        });
        UseWithHandler.addHandler(6814, UseWithHandler.NPC_TYPE, new UseWithHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(NodeUsageEvent event) {
                return true;
            }
        });
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6813, 6184 };
    }

}
