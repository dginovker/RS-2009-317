package org.gielinor.game.content.skill.member.hunter.falconry;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;


/**
 * Represents the skill pulse used to catch a kebbit.
 *
 * @author Vexia
 */
public final class FalconryCatchPulse extends SkillPulse<NPC> {

    /**
     * Represents the falcon catch.
     */
    private final FalconCatch falconCatch;

    /**
     * Represents the falcon item.
     */
    private static final Item FALCON = new Item(10024);

    /**
     * Represents the falcon glove.
     */
    private static final Item GLOVE = new Item(10023);

    /**
     * Represents the original location.
     */
    private final Location originalLocation;

    /**
     * If the falcon has been checked.
     */
    private boolean checked;

    /**
     * The ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code FalconryCatchPulse.java} {@code Object}.
     *
     * @param player
     * @param node
     */
    public FalconryCatchPulse(final Player player, final NPC node, final FalconCatch falconCatch) {
        super(player, node);
        this.falconCatch = falconCatch;
        this.originalLocation = node.getLocation();
    }

    @Override
    public void start() {
        player.faceTemporary(node, 1);
        node.getWalkingQueue().reset();
        player.getWalkingQueue().reset();
        super.start();
    }

    @Override
    public boolean checkRequirements() {
        if (!checked) {
            checked = true;
            if (node.getLocation().getDistance(player.getLocation()) > 15) {
                player.getActionSender().sendMessage("You can't catch a kebbit that far away.");
                return false;
            }
            if (player.getSkills().getLevel(Skills.HUNTER) < falconCatch.getLevel()) {
                player.getActionSender().sendMessage("You need a Hunter level of at least " + falconCatch.getLevel() + " to catch this kebbit.");
                return false;
            }
            if (player.getEquipment().get(Equipment.SLOT_HANDS) != null || player.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                player.getDialogueInterpreter().sendDialogue("Sorry, free your hands, weapon, and shield slot first.");
                return false;
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) == null || !player.getEquipment().containsItem(FALCON)) {
                player.getActionSender().sendMessage("You need a falcon to catch a kebbit.");
                return false;
            }
            if (player.getEquipment().remove(FALCON)) {
                player.getEquipment().add(GLOVE, true, false);
                sendProjectile();
            }
            player.lock(getDistance());
        }
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        player.unlock();
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        // 3 = getDistance()
        if (++ticks % 3 != 0) {
            return false;
        }
        final boolean success = success();
        player.lock();
        player.getActionSender().sendMessage(success ? "The falcon successfully swoops down and captures the kebbit." : "The falcon swoops down on the kebbit, but just misses catching it.");
        if (success) {
            node.finalizeDeath(player);
            final NPC falcon = NPC.create(5094, node.getLocation());
            falcon.setAttribute("falcon:owner", player.getUsername());
            falcon.setAttribute("falcon:catch", falconCatch);
            falcon.init();
            HintIconManager.registerHintIcon(player, falcon);
            World.submit(new Pulse(100, falcon) {

                @Override
                public boolean pulse() {
                    if (!falcon.isActive()) {
                        return true;
                    }
                    Projectile projectile = Projectile.create(node, Repository.findNPC(5093), 918);
                    projectile.setSpeed(80);
                    projectile.send();
                    player.getActionSender().sendMessage("Your falcon has left its prey. You see it heading back toward the falconer.");
                    falcon.clear();
                    return true;
                }
            });
        } else {
            if (player.getEquipment().remove(GLOVE)) {
                player.getEquipment().add(FALCON, true, false);
            }
        }
        player.unlock();
        player.face(null);
        return true;
    }

    /**
     * Sends the projectile.
     */
    private void sendProjectile() {
        Projectile projectile = Projectile.create(player, node, 918);
        projectile.setSpeed(80);
        projectile.setStartHeight(26);
        projectile.setEndHeight(1);
        projectile.send();
    }

    /**
     * Gets the distance of the npc.
     *
     * @return the distance.
     */
    public int getDistance() {
        // TODO 0 was 2
        return (int) (0 + (player.getLocation().getDistance(node.getLocation())) * 0.5);
    }

    /**
     * Checks if the catch was successful.
     *
     * @return {@code True} if so.
     */
    public boolean success() {
        if (originalLocation != node.getLocation()) {
            return RandomUtil.random(1, 3) == 2;
        }
        return ((RandomUtil.getRandom(2) * player.getSkills().getLevel(Skills.HUNTER)) / 4) > (falconCatch.getLevel() / 2);
    }

}
