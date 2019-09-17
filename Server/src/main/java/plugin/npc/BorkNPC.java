package plugin.npc;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.activity.CutscenePlugin;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the bork npc.
 *
 * @author Vexia
 */
public class BorkNPC extends AbstractNPC {

    /**
     * The chats the legion can say.
     */
    private static final String[] LEGION_CHATS = new String[]{
        "For bork!",
        "Die human!",
        "Resistance is futile!",
        "We are the collective!",
        "From a triangle!!",
        "Hup! 2... 3... 4!!"
    };

    /**
     * The chance items.
     */
    private static final ChanceItem[] DROPS = new ChanceItem[]{
        new ChanceItem(532, 1, 1, 0.0),
        new ChanceItem(Item.COINS, 10000, 50000, 0.0),
        new ChanceItem(1619, 1, 0.0),
        new ChanceItem(1621, 1, 1, 0.0),
        new ChanceItem(1623, 1, 1, 0.0)
    };

    /**
     * The ring drops.
     */
    private static final ChanceItem[] RING_DROPS = new ChanceItem[]{
        new ChanceItem(532, 1, 1, 0.0),
        new ChanceItem(1601, 1, 1, 0.0),
        new ChanceItem(Item.COINS, 60000, 150000, 0.0),
        new ChanceItem(1619, 2, 2, 0.0),
        new ChanceItem(1621, 3, 3, 0.0)
    };

    /**
     * The Dagon'hai robe possible items.
     */
    public static final ChanceItem[] ROBE_DROPS = new ChanceItem[]{
        new ChanceItem(14499, 1, 1, 0.0),
        new ChanceItem(14497, 1, 1, 0.0),
        new ChanceItem(14501, 1, 1, 0.0)
    };
    /**
     * The list of the legion npc.
     */
    private final List<NPC> legions = new ArrayList<>();

    /**
     * If the legion is spawned.
     */
    private boolean spawnedLegion;

    /**
     * The player.
     */
    private Player player;

    /**
     * The bork cutscene.
     */
    private BorkCutscene cutscene;

    /**
     * Constructs a new {@Code BorkNPC} {@Code Object}
     */
    public BorkNPC() {
        super(-1, null);
    }

    /**
     * Constructs a new {@Code BorkNPC} {@Code Object}
     *
     * @param id       the id.
     * @param location the location.
     */
    public BorkNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new BorkNPC(id, location);
    }

    @Override
    public void handleTickActions() {
        if (player == null) {
            return;
        }
        super.handleTickActions();
        if (!getLocks().isMovementLocked() && player != null) {
            if (!getProperties().getCombatPulse().isAttacking()) {
                getProperties().getCombatPulse().attack(player);
            }
        }
    }

    @Override
    public void commenceDeath(Entity killer) {
        super.commenceDeath(killer);
        for (NPC l : legions) {
            l.clear();
        }
        player.lock();
        cutscene.wizard.clear();
        cutscene.wizard.lock();

        if (killer instanceof Player) {
            killer.asPlayer().getSavedData().getBossKillLog().increaseBorkKills(1);
        }
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
        if (player.getDialogueInterpreter().getDialogue() != null) {
            player.getDialogueInterpreter().getDialogue().end();
        }
        World.submit(new Pulse(5, player) { // TODO When to send after death?

            @Override
            public boolean pulse() {
                player.unlock();
                player.getDialogueInterpreter().sendDialogues(player, FacialExpression.ANGRY, "That monk - he called to Zamorak for revenge!");
                player.getActionSender().sendMessage("Something is shaking the whole cavern! You should get out of here quick!");
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.SHAKE, 3, 2, 2, 2, 2));
                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                player.getInterfaceState().close();
                return true;
            }

        });
    }

    @Override
    public void handleDrops(Player p, Entity killer) {
        if (player.getAttribute("first-bork", false)) {
            player.removeAttribute("first-bork");
            player.getSkills().addExperience(Skills.SLAYER, 5000);
            player.getActionSender().sendMessage("Slaying Bork for the first time, you have recieved additional Slayer experience.");
        } else {
            player.getSkills().addExperience(Skills.SLAYER, 1500);
        }
        boolean row = player.getEquipment().contains(2572, 1);
        ChanceItem[] chanceItems = row ? RING_DROPS : DROPS;
        for (ChanceItem drop : chanceItems) {
            Item item = new Item(drop.getId(), RandomUtil.random(drop.getMinimumAmount(), drop.getMaximumAmount()));
            if (item.getId() == Item.COINS) {
                item.setCount(item.getCount() * Constants.COIN_DROP_MULTIPLIER);
            }
            GroundItemManager.create(item, getLocation(), player);
        }
        if (RandomUtil.random(5) == 1) {
            super.handleDrops(p, killer);
        }
        if (row) {
            if (Math.random() < 0.6) { // 30% Chance
                GroundItemManager.create(ROBE_DROPS[RandomUtil.random(0, ROBE_DROPS.length)], getLocation(), player);
                p.getActionSender().sendMessage("<col=ff7000><shad=000>Your ring of wealth shines more brightly!");
                Graphics.send(Graphics.create(671), getLocation());
            }
        }
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        if (!spawnedLegion && getSkills().getLifepoints() < (getSkills().getStaticLevel(Skills.HITPOINTS) / 2)) {
            spawnLegion();
        }
    }

    /**
     * Spawns the legion.
     */
    private void spawnLegion() {
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
        spawnedLegion = true;
        player.lock();
        player.getImpactHandler().setDisabledTicks(5);
        lock();
        cutscene.wizard.lock();
        getProperties().getCombatPulse().stop();
        player.getProperties().getCombatPulse().stop();
        getAnimator().forceAnimation(Animation.create(8757));
        World.submit(new Pulse(1, player, this) {

            @Override
            public boolean pulse() {
                getAnimator().forceAnimation(Animation.create(8757));
                sendChat("Come to my aid, brothers!");
                player.getActionSender().sendMessage("Bork strikes the ground with his axe.");
                World.submit(new Pulse(5, player) {

                    @Override
                    public boolean pulse() {
                        player.getInterfaceState().close();
                        for (int i = 0; i < 3; i++) {
                            NPC legion = NPC.create(7135, getLocation().transform(RandomUtil.random(1, 3),
                                RandomUtil.random(1, 3), 0), player);
                            legion.init();
                            // legion.graphics(Graphics.create(1314));
                            legion.setAggressive(true);
                            legion.setRespawn(false);
                            legion.attack(player);
                            legions.add(legion);
                            legion.sendChat(RandomUtil.getRandomElement(LEGION_CHATS));
                        }
                        player.unlock();
                        cutscene.wizard.unlock();
                        unlock();
                        if (player != null) {
                            attack(player);
                        }
                        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                        for (NPC n : legions) {
                            n.getProperties().getCombatPulse().attack(player);
                        }
                        return true;
                    }

                });
                return true;
            }

        });
    }

    /**
     * Sets the player.
     *
     * @param player the player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        PluginManager.definePlugin(new BorkCutscene());
        PluginManager.definePlugin(new DagonDialogue());
        PluginManager.definePlugin(new OrkLegion());
        PluginManager.definePlugin(new DagonElite());
        return super.newInstance(arg);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7133 };
    }

    /**
     * Handles the ork legion.
     *
     * @author Vexia
     */
    public class OrkLegion extends AbstractNPC {

        private Player player;

        /**
         * The last talk.
         */
        private int lastTalk = World.getTicks() + 30;

        /**
         * Constructs a new {@Code OrkLegion} {@Code Object}
         *
         * @param id       the id.
         * @param location the location.
         */
        public OrkLegion(int id, Location location) {
            super(id, location);
            super.setAggressive(true);
        }

        /**
         * Constructs a new {@Code OrkLegion} {@Code Object}
         */
        public OrkLegion() {
            super(-1, null);
        }

        @Override
        public void handleTickActions() {
            if (player == null) {
                return;
            }

            if (lastTalk < World.getTicks()) {
                sendChat(LEGION_CHATS[RandomUtil.random(LEGION_CHATS.length)]);
                lastTalk = World.getTicks() + 30;
            }
        }

        @Override
        public boolean isIgnoreMultiBoundaries(Entity victim) {
            return true;
        }

        @Override
        public boolean canAttack(Entity e) {
            return true;
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            OrkLegion legion = new OrkLegion(id, location);
            legion.player = (Player) objects[0];
            return legion;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 7135 };
        }

    }

    /**
     * Handles the dagone lite npc.
     *
     * @author Vexia
     */
    public class DagonElite extends AbstractNPC {

        private Player player;

        /**
         * Constructs a new {@Code DagonElite} {@Code Object}
         *
         * @param id       the id.
         * @param location the location.
         */
        public DagonElite(int id, Location location) {
            super(id, location);
        }

        /**
         * Constructs a new {@Code DagonElite} {@Code Object}
         */
        public DagonElite() {
            super(-1, null);
        }

        @Override
        public void checkImpact(BattleState state) {
            state.neutralizeHits();
        }

        @Override
        public boolean isIgnoreMultiBoundaries(Entity victim) {
            return true;
        }

        @Override
        public boolean canAttack(Entity e) {
            return true;
        }

        @Override
        public boolean isAttackable(Entity e, CombatStyle style) {
            return false;
        }

        @Override
        public void handleTickActions() {
            if (player == null) {
                return;
            }
            super.handleTickActions();
            if (!getProperties().getCombatPulse().isAttacking()) {
                getProperties().getCombatPulse().attack(player);
            }
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            DagonElite elite = new DagonElite(id, location);
            elite.player = (Player) objects[0];
            return elite;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 7137 };
        }


    }

    /**
     * Handles the bork cutscene.
     *
     * @author Vexia
     */
    public class BorkCutscene extends CutscenePlugin {

        /**
         * The bork npc.
         */
        private BorkNPC bork;

        /**
         * The wizard npc.
         */
        private NPC wizard;

        /**
         * The torches.
         */
        private final GameObject[] torches = new GameObject[2];

        /**
         * Constructs a new {@Code BorkCutscene} {@Code Object}
         */
        public BorkCutscene() {
            super("Bork cutscene");
            this.setMulticombat(true);
        }

        /**
         * Constructs a new {@Code BorkCutscene} {@Code Object}
         *
         * @param player the player.
         */
        public BorkCutscene(Player player) {
            this();
            this.player = player;
        }

        @Override
        public boolean interact(Entity e, Node target, Option option) {
            if (e instanceof Player) {
//                switch (target.getId()) {
//                    case 29537:
//                        e.asPlayer().graphics(Graphics.create(110));
//                        e.asPlayer().getProperties().setTeleportLocation(Location.create(3143, 5545, 0));
//                        return true;
//                }
            }
            return super.interact(e, target, option);
        }

        @Override
        public boolean leave(Entity entity, boolean logout) {
            if (entity instanceof Player) {
                PacketRepository.send(CameraViewPacket.class, new CameraContext(entity.asPlayer(), CameraType.RESET, 3, 2, 2, 2, 2));
            }
            return super.leave(entity, logout);
        }

        @Override
        public boolean start(Player player, boolean login, Object... args) {
            player.lock();
            bork = new BorkNPC(7133, base.transform(48, 16, 0));
            bork.init();
            bork.setWalkRadius(20);
            bork.setPlayer(player);
            bork.setRespawn(false);
            bork.lock();
            bork.cutscene = this;
            wizard = NPC.create(7137, base.transform(39, 23, 0), player);
            wizard.init();
            wizard.lock();
            wizard.faceTemporary(player, 2);
            player.getImpactHandler().setDisabledTicks(5);
            player.faceTemporary(wizard, 2);
            torches[0] = new GameObject(745, base.transform(38, 27, 0));
            torches[1] = new GameObject(745, base.transform(38, 28, 0));
            ObjectBuilder.add(torches[0]);
            ObjectBuilder.add(torches[1]);
            return super.start(player, login, args);
        }

        @Override
        public void stop(boolean fade) {
            end();
        }

        @Override
        public void end() {
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
            if (isRemoveTabs()) {
                player.getInterfaceState().openDefaultTabs();
            }
            player.unlock();
            player.getWalkingQueue().reset();
            ObjectBuilder.remove(torches[0]);
            ObjectBuilder.remove(torches[1]);
        }

        /**
         * Commences the fight.
         */
        public void commenceFight() {
            bork.unlock();
            wizard.unlock();
            player.unlock();
            wizard.attack(player);
            bork.attack(player);
            bork.setAggressive(true);
            wizard.setAggressive(true);
            wizard.setRespawn(false);
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
        }

        @Override
        public void open() {
            super.open();
            bork.cutscene.player = player;
            player.lock();
            World.submit(new Pulse(3, player) {

                @Override
                public boolean pulse() {
                    player.getInterfaceState().close();
                    player.getDialogueInterpreter().open("dagon-dialogue", wizard, BorkCutscene.this);
                    return true;
                }

            });
        }

        @Override
        public ActivityPlugin newInstance(Player p) throws Throwable {
            return new BorkCutscene(player);
        }

        @Override
        public Location getStartLocation() {
            return base.transform(39, 27, 0);
        }

        @Override
        public Location getSpawnLocation() {
            return null;
        }

        @Override
        public void configure() {
            region = DynamicRegion.create(13209);
            region.setMulticombat(true);
            setRegionBase();
            registerRegion(region.getId());
        }

        @Override
        public boolean isRemoveTabs() {
            return false;
        }
    }

    /**
     * Handles the dagon dialogue.
     *
     * @author Vexia
     */
    public class DagonDialogue extends DialoguePlugin {

        /**
         * The bork cutscene.
         */
        private BorkCutscene cutscene;

        /**
         * Constructs a new {@Code DagonDialogue} {@Code Object}
         */
        public DagonDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@Code DagonDialogue} {@Code Object}
         *
         * @param player the player.
         */
        public DagonDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new DagonDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            cutscene = (BorkCutscene) args[1];
            npc("Our Lord Zamorak has power over life and death,", player.getUsername() + "! He has seen fit to resurrect Bork to", "continue his great work...and now you will fall before him");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    boolean played = player.getSavedData().getActivityData().hasKilledBork();
                    player(played ? "Uh-oh! Here we go again." : "Oh boy...");
                    stage++;
                    break;
                case 1:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public void end() {
            super.end();
            cutscene.commenceFight();
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("dagon-dialogue") };
        }

    }
}
