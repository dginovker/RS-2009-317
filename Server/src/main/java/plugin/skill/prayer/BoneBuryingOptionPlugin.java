package plugin.skill.prayer;

import java.util.HashMap;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the bone bury option plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BoneBuryingOptionPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(827);

    /**
     * Represents the sound to use.
     */
    private static final Audio AUDIO = new Audio(2738, 10, 1);

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (player.getAttribute("delay:bury", -1) > World.getTicks()) {
            return true;
        }
        player.setAttribute("delay:bury", World.getTicks() + 2);
        final Item item = (Item) node;
        Bones bone = Bones.forId(item.getId());
        if (bone == null) {
            bone = Bones.BONES;
        }
        if (item.getSlot() < 0) {
            return false;
        }
        boolean savedBones = player.getPerkManager().isTriggered(Perk.BONE_SAVIOR);
        if (!savedBones) {
            if (player.getInventory().replace(null, item.getSlot()) != item) {
                return false;
            }
        } else {
            player.getActionSender().sendMessage("Your Bone savior perk saves the bones from being used!", 1);
        }
        player.lock(2);
        player.animate(ANIMATION);
        player.getActionSender().sendMessage("You dig a hole in the ground...", 1);
        player.getActionSender().sendSound(AUDIO);
        final Bones bonee = bone;
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                player.getActionSender().sendMessage("You bury the bones.", 1);
                player.getSkills().addExperience(Skills.PRAYER, bonee.getExperience());
                if (bonee == Bones.BONES) {
                    AchievementDiary.decrease(player, AchievementTask.BONE_SHORTAGE, 1);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("bury", this);
        return this;
    }

    /**
     * Represents an enumeration of bones.
     *
     * @author Emperor
     */
    public enum Bones {
        BONES(526, 4.5),
        WOLF_BONES(2859, 4.5),
        BURNST_BONES(528, 4.5),
        MONKEY_BONES(3183, 5),
        MONKEY_BONES2(3179, 5),
        BAT_BONES(530, 5.3),
        BIG_BONES(532, 15),
        JOGRE_BONES(3125, 15),
        ZOGRE_BONES(4812, 12.5),
        SHAIKAHAN_BONES(3123, 25),
        BABY_DRAGON_BONES(534, 30),
        WYVERN_BONES(6812, 50),
        DRAGON_BONES(536, 72),
        FAYRG(4830, 84),
        RAURG_BONES(4832, 96),
        DAGANNOTH(6729, 125),
        OURG_BONES(4834, 140);

        /**
         * The bone item id.
         */
        private int itemId;

        /**
         * The experience given by burying the bone.
         */
        private double experience;

        /**
         * Construct a new {@code Bones} {@code Object}.
         *
         * @param itemId     The item id.
         * @param experience The experience given by burying the bone.
         */
        private Bones(int itemId, double experience) {
            this.itemId = itemId;
            this.experience = experience;
        }

        /**
         * Gets the id of the bone.
         *
         * @return The id of the bone.
         */
        public int getId() {
            return itemId;
        }

        /**
         * Get the bone experience given when you bury the bone.
         *
         * @return The experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Holds all bones.
         */
        private static HashMap<Integer, Bones> bones = new HashMap<Integer, Bones>();

        /**
         * Get the bone.
         *
         * @param itemId The item id.
         * @return The bone.
         */
        public static Bones forId(int itemId) {
            return bones.get(itemId);
        }

        /**
         * Construct the bones.
         */
        static {
            for (Bones bone : Bones.values()) {
                bones.put(bone.itemId, bone);
            }
        }
    }
}
