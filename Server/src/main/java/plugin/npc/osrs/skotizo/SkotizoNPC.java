package plugin.npc.osrs.skotizo;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents the Skotizo boss.
 *
 * reference material:
 * - https://www.reddit.com/r/2007scape/comments/4ocrmi/all_you_need_to_know_about_the_new_boss_skotizo/
 * - http://oldschoolrunescape.wikia.com/wiki/Skotizo
 *
 * Created by Stan van der Bend on 04/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.skotizo
 */
public class SkotizoNPC extends AbstractNPC{

    private final static int NPC_ID = 7286;

    public SkotizoNPC(Location location) {
        super(NPC_ID, location);
    }

    private final SkotizoAltarNPC
        ALTAR_NORTH = new SkotizoAltarNPC(this, AwakendAltarType.NORTH),
        ALTAR_EAST = new SkotizoAltarNPC(this, AwakendAltarType.EAST),
        ALTAR_SOUTH = new SkotizoAltarNPC(this, AwakendAltarType.SOUTH),
        ALTAR_WEST = new SkotizoAltarNPC(this, AwakendAltarType.WEST);

    private final static Predicate<SkotizoAltarNPC> nonAwakend = skotizoAltarNPC -> !skotizoAltarNPC.isAwakened();

    public void updateDefence() {
        getSkills().updateLevel(Skills.DEFENCE, getModifiedDefenceLevel());
    }

    @Override
    public void tick() {
        super.tick();

        if(RandomUtil.getRandom(100) <= 5){

            altarNPCStream()
                .filter(nonAwakend)
                .findAny()
                .ifPresent(SkotizoAltarNPC::activate);

        }

    }

    @Override
    public void init() {
        super.init();
        altarNPCStream().forEach(NPC::init);
    }

    private Stream<SkotizoAltarNPC> altarNPCStream(){
        return Stream.of(ALTAR_NORTH, ALTAR_EAST, ALTAR_SOUTH, ALTAR_WEST);
    }

    private int getModifiedDefenceLevel(){
        return (int) (getSkills().getLevel(Skills.DEFENCE) * (1.0D + (altarNPCStream().filter(SkotizoAltarNPC::isAwakened).count() * SkotizoAltarNPC.DEFENCE_MODIFIER)));
    }

    @Override public AbstractNPC construct(int id, Location location, Object... objects) {
        return new SkotizoNPC(location);
    }

    @Override public int[] getIds() {
        return new int[]{NPC_ID};
    }


}
