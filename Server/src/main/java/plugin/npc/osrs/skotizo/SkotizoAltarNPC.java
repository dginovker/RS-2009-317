package plugin.npc.osrs.skotizo;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;

import java.util.Optional;

/**
 * Skotizo's area has 4 altars that he may activate during combat.
 * Players must defeat the altar to disable it.
 * For each activated altar it will decrease the player's defence by 5%.
 * The Arc Light weapon one hits the altars.
 *
 * todo: verify this is merely a NPC
 * todo: verify that {@link Skills#drainLevel(int, double, double)} should be used/requires additional actions.
 * Created by Stan van der Bend on 09/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.skotizo.object
 */
public class SkotizoAltarNPC extends NPC {

    final static double DEFENCE_MODIFIER = 0.05D;

    private final SkotizoNPC skotizoNPC;
    private final AwakendAltarType awakendAltarType;

    SkotizoAltarNPC(SkotizoNPC skotizoNPC, AwakendAltarType awakendAltarType) {
        super(awakendAltarType.getUnAwakendNPCID(), awakendAltarType.getLocation());
        super.setWalks(false);
        super.setRespawn(false);
        super.getProperties().setRetaliating(false);
        super.init();
        this.skotizoNPC = skotizoNPC;
        this.awakendAltarType = awakendAltarType;
        getProperties().setAttackAnimation(Animation.create(-1));
        getProperties().setDefenceAnimation(Animation.create(-1));
        getProperties().setDeathAnimation(Animation.create(-1));
    }

    @Override
    public void finalizeDeath(Entity killer) {

        transform(awakendAltarType.getUnAwakendNPCID());

        skotizoNPC.updateDefence();
    }

    public void activate(){

        //todo: update altar interface
        transform(awakendAltarType.getAwakendNPCID());

        //same effect as curse spell.
        Optional.ofNullable(skotizoNPC.getProperties().getCombatPulse().getVictim())
            .filter(entity -> entity instanceof Player)
            .map(Entity::asPlayer)
            .ifPresent(player -> player.getSkills().drainLevel(Skills.DEFENCE, DEFENCE_MODIFIER, DEFENCE_MODIFIER));

        skotizoNPC.updateDefence();
    }



    /**
     * Determines if this {@link SkotizoAltarNPC} is in its awakened state.
     *
     * @return {@code true} if {@link SkotizoAltarNPC#getId()} equals {@link AwakendAltarType#getAwakendNPCID()}.
     */
    public boolean isAwakened() {
        return getId() == awakendAltarType.getAwakendNPCID();
    }
}
