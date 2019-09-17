package plugin.activity.gwd;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.skill.member.summoning.pet.Pets;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.rs2.pulse.impl.CombatPulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles a god wars boss NPC.
 *
 * @author Emperor
 */
public final class GodwarsBossNPC extends AbstractNPC {

    /**
     * Handles the combat swing of Commander Zilyana.
     */
    private static final CombatSwingHandler ZILYANA_COMBAT = new GWDZilyanaSwingHandler();

    /**
     * Handles the combat swing of Kree'arra.
     */
    private static final CombatSwingHandler KREE_ARRA_COMBAT = new GWDKreeArraSwingHandler();

    /**
     * Handles the combat swing of General Graardor.
     */
    private static final CombatSwingHandler GRAARDOR_COMBAT = new GWDGraardorSwingHandler();

    /**
     * Handles the combat swing of General Graardor.
     */
    private static final CombatSwingHandler TSUTSAROTH_COMBAT = new GWDTsutsarothSwingHandler();
    final SecureRandom secureRandom = new SecureRandom();
    /**
     * The battle cries.
     */
    private static final String[][] BATTLE_CRIES = {
        { // Zamorak
            "Attack them, you dogs!",
            "Forward!",
            "Death to Saradomin's dogs!",
            "Kill them, you cowards!",
            "The Dark One will have their souls!",
            "Zamorak curse them!",
            "Rend them limb from limb!",
            "No retreat!",
            "Flay them all!"
        },
        { // Armadyl
            "Kraaaw!"
        },
        { // Saradomin
            "Death to the enemies of the light!", "Slay the evil ones!", "Saradomin lend me strength!",
            "By the power of Saradomin!", "May Saradomin be my sword.", "Good will always triumph!",
            "Forward! Our allies are with us!", "Saradomin is with us!", "In the name of Saradomin!",
            "Attack! Find the Godsword!"
        },
        { // Bandos
            "Death to our enemies!",
            "Brargh!",
            "Break their bones!",
            "For the glory of Bandos!",
            "Split their skulls!",
            "We feast on the bones of our enemies tonight!",
            "CHAAARGE!",
            "Crush them underfoot!",
            "All glory to Bandos!",
            "GRAAAAAAAAAAAR!",
            "FOR THE GLORY OF THE BIG HIGH WAR GOD!"
        }
    };

    /**
     * The minions.
     */
    private NPC[] minions;

    /**
     * The boss chamber.
     */
    private ZoneBorders chamber;

    /**
     * The next battle cry tick.
     */
    private int nextBattleCry;

    /**
     * The combat swing handler.
     */
    private CombatSwingHandler handler;

    /**
     * If the NPC focuses on one target.
     */
    private boolean targetFocus;

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     */
    public GodwarsBossNPC() {
        super(6203, null);
    }

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public GodwarsBossNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new GodwarsBossNPC(id, location);
    }

    @Override
    public void init() {
        setAggressive(false);
        super.init();
        switch (getId()) {
            case 6203:
                chamber = new ZoneBorders(2918, 5318, 2936, 5331);
                handler = TSUTSAROTH_COMBAT;
                targetFocus = true;
                break;
            case 6222:
                chamber = new ZoneBorders(2824, 5296, 2842, 5308);
                handler = KREE_ARRA_COMBAT;
                break;
            case 6247:
                chamber = new ZoneBorders(2889, 5258, 2907, 5276);
                handler = ZILYANA_COMBAT;
                break;
            case 6260:
                chamber = new ZoneBorders(2864, 5351, 2876, 5369);
                handler = GRAARDOR_COMBAT;
                targetFocus = true;
                break;
        }
        AggressiveBehavior behavior = null;
        if (chamber != null) {
            final ZoneBorders borders = chamber;
            behavior = new AggressiveBehavior() {

                @Override
                public boolean canSelectTarget(Entity entity, Entity target) {
                    if (!target.isActive() || DeathTask.isDead(target)) {
                        return false;
                    }
                    if (target instanceof Player) {
                        if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                            return false;
                        }
                    }
                    if (!target.getProperties().isMultiZone() && target.inCombat()) {
                        return false;
                    }
                    return borders.insideBorder(target.getLocation());
                }
            };
            super.setAggressiveHandler(new AggressiveHandler(this, behavior));
            if (chamber.insideBorder(getLocation())) {
                minions = new NPC[3];
                for (int i = 0; i < 3; i++) {
                    int npcId = getId() + 1 + (i << 1);
                    AbstractNPC npc = (AbstractNPC) (minions[i] = NPC.create(npcId, getSpawnLocation(npcId)));
                    npc.init();
                    npc.fireEvent("set_boss", this);
                    if (behavior != null) {
                        npc.setAggressiveHandler(new AggressiveHandler(npc, behavior));
                        npc.getAggressiveHandler().setChanceRatio(6);
                        npc.getAggressiveHandler().setAllowTolerance(false);
                        npc.getAggressiveHandler().setRadius(28);
                    }
                }
            }
        }
        getProperties().setNPCWalkable(true);
        getProperties().setCombatTimeOut(2);
        getAggressiveHandler().setChanceRatio(6);
        getAggressiveHandler().setRadius(28);
        getAggressiveHandler().setAllowTolerance(false);
        walkRadius = 28;
    }

    @Override
    public void tick() {
        CombatPulse pulse = getProperties().getCombatPulse();
        if (chamber != null && pulse.isAttacking()) {
            Entity e = pulse.getVictim();
            if (!targetFocus) {
                Entity target = getImpactHandler().getMostDamageEntity();
                if (target != null && target != e && target instanceof Player) {
                    pulse.setVictim(target);
                }
            }
            if (!chamber.insideBorder(e.getLocation().getX(), e.getLocation().getY())) {
                getPulseManager().clear();
            }
            if (nextBattleCry < World.getTicks()) {
                String[] cries = BATTLE_CRIES[(getId() - 6203) >> 4];
                sendChat(cries[RandomUtil.randomize(cries.length)]);
                nextBattleCry = World.getTicks() + 7 + RandomUtil.randomize(20);
            }
        }
        super.tick();
        if (getRespawnTick() == World.getTicks() && minions != null) {
            for (NPC npc : minions) {
                npc.setRespawnTick(-1);
            }
        }
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        if (targetFocus) {
            if (getProperties().getCombatPulse().getNextAttack() < World.getTicks() - 3) {
                getProperties().getCombatPulse().attack(entity);
                return;
            }
        }
        super.onImpact(entity, state);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = (Player) killer;
            int chance = 2500;
            // TODO chance for members
            /*if (player.getDonorManager().isExtremeDonor()) {
                chance -= player.getDonorManager().getAdditional(chance, 25);
            }*/
            boolean dropPet = secureRandom.nextInt(chance) == 1;
            boolean canObtain = true;
            Map<Integer, Object[]> petList = new HashMap<>();
            petList.put(6260, new Object[]{
                Pets.GENERAL_GRAARDOR_JR.getBabyItemId(), Pets.GENERAL_GRAARDOR_JR.getBabyNpcId(), "general graardor"
            });
            petList.put(6203, new Object[]{
                Pets.KRIL_TSUTSAROTH_JR.getBabyItemId(), Pets.KRIL_TSUTSAROTH_JR.getBabyNpcId(), "k'ril tsutsaroth"
            });
            petList.put(6222, new Object[]{
                Pets.KREE_ARRA_JR.getBabyItemId(), Pets.KREE_ARRA_JR.getBabyNpcId(), "kree'arra"
            });
            petList.put(6247, new Object[]{
                Pets.ZILYANA_JR.getBabyItemId(), Pets.ZILYANA_JR.getBabyNpcId(), "zilyana"
            });
            Object[] pet = petList.get(getId());
            if (pet != null && dropPet) {
                int itemId = (int) pet[0];
                int npcId = (int) pet[1];
                String name = (String) pet[2];
                if ((player.getInventory().contains(itemId) || player.getBank().contains(itemId)) ||
                    player.getFamiliarManager().getFamiliar() != null && player.getFamiliarManager().getFamiliar().getId() == npcId) {
                    canObtain = false;
                }
                if (canObtain) {
                    if (player.getFamiliarManager().getFamiliar() == null) {
                        player.getFamiliarManager().summon(new Item(itemId), true, false);
                        player.getActionSender().sendMessage("You have a funny feeling like you're being followed.");
                    } else {
                        if (!player.getInventory().hasRoomFor(new Item(itemId))) {
                            player.getActionSender().sendMessage("A Pet " + name + " has been dropped.");
                        }
                        player.getInventory().add(new Item(itemId), true);
                    }
                }
            }
            switch (getId()) {
                case 6222:
                    killer.asPlayer().getSavedData().getBossKillLog().increaseKreeArraKills(1);
                    break;
                case 6247:
                    killer.asPlayer().getSavedData().getBossKillLog().increaseZilyanaKills(1);
                    break;
                case 6260:
                    killer.asPlayer().getSavedData().getBossKillLog().increaseGraardorKills(1);
                    break;
                case 6203:
                    killer.asPlayer().getSavedData().getBossKillLog().increaseKrilTsutKills(1);
                    break;
            }
        }
        if (minions == null) {
            return;
        }
        for (NPC minion : minions) {
            if (minion.getRespawnTick() >= World.getTicks()) {
                minion.setRespawnTick(getRespawnTick());
            }
        }
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (getId() == 6222 && style == CombatStyle.MELEE && entity instanceof Player) {
            ((Player) entity).getActionSender().sendMessage("The aviansie is flying too high for you to attack using melee.");
            return false;
        }
        return super.isAttackable(entity, style);
    }

    /**
     * Gets the spawn location for the given NPC id.
     *
     * @param id The NPC id.
     * @return The spawn location.
     */
    private Location getSpawnLocation(int id) {
        switch (id) {
            case 6208:
                return Location.create(2920, 5320, 2);
            case 6206:
                return Location.create(2919, 5327, 2);
            case 6204:
                return Location.create(2930, 5328, 2);
            case 6223:
                return Location.create(2828, 5298, 2);
            case 6225:
                return Location.create(2837, 5299, 2);
            case 6227:
                return Location.create(2838, 5305, 2);
            case 6248:
                return Location.create(2893, 5271, 0);
            case 6250:
                return Location.create(2901, 5268, 0);
            case 6252:
                return Location.create(2895, 5260, 0);
            case 6265:
                return Location.create(2866, 5363, 2);
            case 6261:
                return Location.create(2867, 5354, 2);
            case 6263:
                return Location.create(2873, 5354, 2);
        }
        return null;
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        if (handler != null) {
            return handler;
        }
        return super.getSwingHandler(swing);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6203, 6222, 6247, 6260 };
    }

}
