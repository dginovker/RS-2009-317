package plugin.ttrail;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.content.global.distraction.treasuretrail.impl.EmoteClueScroll;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Uri npc.
 *
 * @author Vexia
 */
public final class UriNPC extends AbstractNPC {

    /**
     * The npc ids.
     */
    private static final int[] IDS = new int[]{ 5141, 5142, 5143, 5144, 5145 };

    /**
     * The clue scroll.
     */
    private ClueScrollPlugin clueScroll;

    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new <code>UriNPC</code> <code>Object</code>
     */
    public UriNPC() {
        super(0, null);
    }

    /**
     * Constructs a new <code>UriNPC</code> <code>Object</code>
     *
     * @param id       the id.
     * @param location the location.
     */
    public UriNPC(int id, Location location) {
        super(id, location, false);
        this.setRespawn(false);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new UriNPC(id, location);
    }

    @Override
    public void init() {
        Player player = getAttribute("player", null);
        ClueScrollPlugin clueScroll = getAttribute("clue", null);
        if (player != null) {
            this.player = player;
        }
        if (clueScroll != null) {
            this.clueScroll = clueScroll;
        }
        if (player != null) {
            location = RegionManager.getSpawnLocation(player, this);
            if (location == null) {
                location = player.getLocation();
            }
        }
        super.init();
    }

    @Override
    public void finalizeDeath(Entity killer) {
        if (killer instanceof Player) {
            Player p = killer.asPlayer();
            if (p == player && isDoubleAgent()) {
                p.setAttribute("killed-agent", clueScroll.getClueId());
            }
        }
        super.finalizeDeath(killer);
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (player != null) {
            if (player.getLocation().getDistance(getLocation()) > 10 || !player.isActive()) {
                clear();
            }
            if (isDoubleAgent()) {
                if (!getProperties().getCombatPulse().isAttacking()) {
                    getProperties().getCombatPulse().attack(player);
                }
            }
        }
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (!(entity instanceof Player)) {
            return false;
        }
        if (player != null) {
            Player p = entity.asPlayer();
            return p == player;
        }
        return super.isAttackable(entity, style);
    }


    @Override
    public boolean canSelectTarget(Entity target) {
        return target == player;

    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        PluginManager.definePlugin(new UriDialogue());
        return super.newInstance(arg);
    }

    @Override
    public int[] getIds() {
        return IDS;
    }

    /**
     * Checks if uri is a double agent.
     *
     * @return <code>True} if so.
     */
    public boolean isDoubleAgent() {
        return getAttribute("double-agent", false);
    }

    /**
     * Gets the bplayer.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the baplayer.
     *
     * @param player the player to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the bclueScroll.
     *
     * @return the clueScroll
     */
    public ClueScrollPlugin getClueScroll() {
        return clueScroll;
    }

    /**
     * Handles the uri dialogue.
     *
     * @author Vexia
     */
    public static final class UriDialogue extends DialoguePlugin {

        /**
         * The quotes uri says.
         */
        private static final String[] QUOTES = new String[]{
            "Once, I was a poor man, but then I found a party hat.",
            "There were three goblins in a bar, which one<br> left first?",
            "Would you like to buy a pewter spoon?",
            "In the end, only the three-legged survive.",
            "I heard that the tall man fears only strong winds.",
            "In Canifis the men are known for eating much spam.",
            "I am the egg man, are you one of the egg men?",
            "I believe that it is very rainy in Varrock.",
            "The slowest of fishermen catch the swiftest of fish.",
            "It is quite easy being green.",
            "Don't forget to find the jade monkey.",
            "Don't forget to find the jade monkey.",
            "Do you want ants? Because that's how you get ants.",
            "I once named a duck after a girl. Big mistake.",
            "Loser says what.",
            "I don't like pineapple, it has that bone in it.",
            "There's this guy I sit next to. Makes weird faces<br> and sounds. Kind of an odd fellow.",
            "I'm looking for a girl named Molly. I can't find her.",
            "Guys, let's lake dive!",
            "Mate, mate... I'm the best.",
            "I gave you what you needed; not what you think<br> you needed.",
            "Want to see me bend a spoon?",
            "Is that Deziree?",
            "This is the last night you'll spend alone.",
            "(Breathing intensifies)",
            "The Ankou's are a lie.",
            "Hurry, there's a bee sticking out of my arm!",
            "Connection lost. Please wait - attempting to reestablish.",
            "Init doe. Lyk, I hope yer reward iz goodd aye?"
        };

        /**
         * Constructs a new <code>UriDialogue} <code>Object}
         */
        public UriDialogue() {
        }

        /**
         * Constructs a new <code>UriDialogue} <code>Object}
         *
         * @param player
         */
        public UriDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new UriDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            if (!canSpeak()) {
                npc("I do not believe we have any business, Comrade.");
                stage = -1;
                return true;
            }
            npc(RandomUtil.getRandomElement(QUOTES));
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case -1:
                    end();
                    break;
                case 0:
                    interpreter.sendDialogues(player, FacialExpression.ANNOYED, "What?");
                    asUri().getClueScroll().reward(player);
                    stage++;
                    break;
                case 1:
                    interpreter.sendItemMessage(405, "You've been given a casket!");
                    stage++;
                    break;
                case 2:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public boolean close() {
            if (stage >= 1) {
                npc.clear();
                player.removeAttribute("killed-agent");
            }
            return super.close();
        }

        /**
         * Checks if the player can speak to the npc.
         *
         * @return <code>True} if so.
         */
        public boolean canSpeak() {
            EmoteClueScroll scroll = (EmoteClueScroll) asUri().getClueScroll();
            if (asUri().getPlayer() != player || !player.getAttribute("commence-emote", !scroll.hasCommencEmote())) {
                return false;
            }
            return scroll.hasEquipment(player, scroll.getEquipment());
        }

        /**
         * Casts to the uri npc.
         *
         * @return the npc.
         */
        public UriNPC asUri() {
            return (UriNPC) npc;
        }

        @Override
        public int[] getIds() {
            return IDS;
        }


    }
}