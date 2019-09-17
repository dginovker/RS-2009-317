package plugin.activity.ffa;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.activity.minigame.*;
import org.gielinor.game.content.dialogue.DialogueBuilder;
import org.gielinor.game.content.dialogue.DialogueLayout;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.Players;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.node.entity.player.link.prayer.PrayerBook;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.update.flag.player.AppearanceFlag;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.PulseBuilder;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

import java.util.*;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.gielinor.game.content.dialogue.DialogueLayout.OPTION;
import static org.gielinor.utilities.TimeUtil.GAME_CYCLES;
import static org.gielinor.game.content.activity.minigame.StagedMinigame.Stage.*;

/**
 * Created by Stan van der Bend on 24/11/2017.
 *
 * project: runeworld-game-server
 * package: runeworld.world.global.content.minigames
 */
public class FreeForAllGame extends StagedMinigame{

    private final static String NAME = "Champions Guild";
    private final static ZoneBorders ZONE = new ZoneBorders(3145, 9738, 3191, 9778);

    private final static int
            MINIMAL_PARTICIPANTS = 1,
            ENTRY_ITEM_ID = 995,
            ENTRY_AMOUNT = 20000000,
            CHAMPIONS_STATUE = 10556,
            CHAMPIONS_STATUE_OPEN = 10557,
            CHAMPIONS_DOOR = 10553;
    private final static Location
            LOBBY = new Location(3186, 9758),
            WAIT_POSITION = new Location(3167, 9757).setRandomizer(3);

    private static Set<Item> eventRewards = new HashSet<>();
    private static boolean IS_EVENT = false;

    private int pot_amount;
    private GameType type;
    private List<GameType> votes = new ArrayList<>();

    public FreeForAllGame() {
        super(NAME, false, true, true, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS);
    }

    private void restart() {
        reset();
        buildGameTask();
    }

    public void reset(){
        pot_amount = 0;
        votes.clear();
        set(PRE_GAME).accept(this);
        IS_EVENT = false;
        eventRewards.clear();
    }

    private void determineType() {
        if (votes.isEmpty()){
            type = RandomUtil.getRandomElement(GameType.values());
        } else {
            GameType leadingType = null;
            int previousFrequency = 0;
            for(GameType gameType: GameType.values()){
                int frequency = Collections.frequency(votes, gameType);

                if(frequency > previousFrequency)
                    leadingType = gameType;

                previousFrequency = frequency;

            }
            if(Objects.nonNull(leadingType)){
                type = leadingType;
            } else {
                votes.clear();
                determineType();
                return;
            }
        }
        getParticipatingPlayers().forEach(player -> player.getActionSender().sendMessage("[ChampionsGuild]: Leading type is "+type.toString()+"!"));
    }

    private Consumer<Player> incrementKills(){
        return player -> player.getGameAttributes().saveAttribute("ffa_kills", player.getGameAttributes().getAttribute("ffa_kills", 0) + 1);
    }

    private void prepare(Player player){
        if(Objects.nonNull(player)){
            player.debug("[ChampionsGuild]: Overheads are "+(type.isOverheadsAllowed() ? "" : "not")+" allowed.");
            player.debug("[ChampionsGuild]: Loading "+ StringUtils.capitalize(type.name().toLowerCase())+" setup:");
            if(player.getInventory().contains(ENTRY_ITEM_ID))
            {
                player.getBank().add(new Item(ENTRY_ITEM_ID, player.getInventory().getCount(ENTRY_ITEM_ID)));
                player.getActionSender().sendMessage("Stored your coins in the bank :)");
            }

            player.getInventory().clear();
            player.getInventory().add(type.getInventory());
            player.getInventory().refresh();

            player.debug("[ChampionsGuild-SETUP]: Inventory Set.   | "+player.getInventory().itemCount());

            for (int i = 0; i < type.getEquipment().length; i++) {
                int slot = type.getEquipment()[i][0], id = type.getEquipment()[i][1];
                ItemDefinition definition = ItemDefinition.forId(id);
                player.getEquipment().set(slot, new Item(id, definition.getName().contains("arrow")  || definition.getName().contains("bolt") ? 300 : 1), true);
            }

            player.getEquipment().refresh();
            Equipment.updateBonuses(player);
            player.getUpdateMasks().register(new AppearanceFlag(player));
            player.debug("[ChampionsGuild-SETUP]: Equipment Set.   | "+player.getEquipment().itemCount());

            player.getSpellBookManager().setSpellBook(type.getSpellBookManager());
            player.getActionSender().sendSidebarTab(Sidebar.MAGIC_TAB.ordinal());
            player.debug("[ChampionsGuild-SETUP]: Spellbook Set.   | "+player.getSpellBookManager().getSpellBook());

            player.getPrayer().toggleAll(true);
            player.getSkills().rechargePrayerPoints();
            player.getSavedData().getGlobalData().setPrayerBook(type.getPrayerBook().getBookID());
            player.getInterfaceState().openTab(Sidebar.PRAYER_TAB.ordinal(), new Component(player.getSavedData().getGlobalData().getPrayerBook()));
            player.debug("[ChampionsGuild-SETUP]: PrayerBook Set.   | "+player.getSavedData().getGlobalData().getPrayerBook());

            restorePlayer(player);
        }
    }

    private void restorePlayer(Player player){
        player.fullRestore();
        player.getActionSender().sendMessage("Your special attack, prayer, and health has been restored.");
    }

    private void refundEntry(Player player){
        player.debug("[ChampionsGuild]: refunding entry");

        pot_amount-=ENTRY_AMOUNT;

        player.getBank().add(new Item(ENTRY_ITEM_ID, ENTRY_AMOUNT));
        player.getActionSender().sendMessage("Your entry refunded is added to your bank.");
        message(TextUtils.formatDisplayName(player.getUsername())+" left the game, new pot = "+pot_amount);
    }
    private void payOutTo(Player winner){
        final int inPot = pot_amount;
        new PulseBuilder<>(winner)
                .add(0, Players.sendAnim(6382).andThen(Players.forceSpeak("Victory is mine!")))
                .add(1, player->  message(TextUtils.formatDisplayName(player.getUsername()) + " won the Champions Guild match! The pot was "+ (IS_EVENT ? "a custom reward!" : TextUtils.formatCoinValue(inPot))+"."))
                .add(2, this::leave)
                .start();


        winner.getGameAttributes().saveAttribute("ffa_played", winner.getGameAttributes().getAttribute("ffa_played", 0) + 1);
        winner.getGameAttributes().saveAttribute("ffa_won", winner.getGameAttributes().getAttribute("ffa_won", 0) + 1);

        if (IS_EVENT) {
            eventRewards.forEach(winner.getInventory()::add);
        } else {
            winner.getBank().add(new Item(ENTRY_ITEM_ID, inPot));
            winner.getActionSender().sendMessage("Stored your winnings in the bank!");
            if (inPot <= 0)
                World.sendAdminMessage("Something went wrong with a champions guild match, winner=" + winner.getUsername() + ", pot=" + inPot);
        }
    }
    private void payEntry(Player player){
        if(player.getInventory().getCount(ENTRY_ITEM_ID) >= ENTRY_AMOUNT)
            player.getInventory().remove(new Item(ENTRY_ITEM_ID, ENTRY_AMOUNT));
        pot_amount += ENTRY_AMOUNT;
    }

    private void check(Player player){
        if(!type.isOverheadsAllowed()) {
            for(PrayerType prayer : player.getPrayer().getActive()){
                if(prayer.getIcon(player, prayer) != -1){
                    player.getPrayer().toggle(prayer);
                    player.getActionSender().sendMessage("@red@You cannot use overheads in this match.");
                }
            }
        }
    }

    private void showAttack(Player player){
        player.getInteraction().set(Option._P_ATTACK);
        player.getActionSender().sendString(19000, "Combat level: " + player.getSkills().calculateCombatLevel());
        player.getUpdateMasks().register(new AppearanceFlag(player));
    }

    private void message(String text){
        World.sendCustomWorldMessage("<img=10> @red@[ChampionsGuild]@bla@ "+text);
    }

    private void vote(Player player, GameType type){
        votes.add(type);
        player.getInterfaceState().close();
        enter(player);
        getParticipatingPlayers().forEach(Players.sendMessage(TextUtils.formatDisplayName(player.getUsername())+" voted for "+type.toString()));
    }

    public PulseBuilder<StagedMinigame> create(){
        return new PulseBuilder<StagedMinigame>(this)
            .onStart(set(GAME)
                    .andThen(minigame -> getParticipatingPlayers()
                        .forEach(this::showAttack)))
            .add(minigame -> minigame.getParticipatingPlayers()
                .forEach(this::check))
            .add(minigame -> minigame.getParticipatingPlayers().stream()
                .filter(ZONE::insideBorder)
                .peek(Players.sendDebug("Out of champion guild bounds!"))
                .forEach(this::leave))
            .stopWhen(
                hasStarted().and(minigame -> minigame.getParticipatingPlayers().size() <= 1))
            .onStop(
                set(POST_GAME)
                        .andThen(minigame -> minigame.getParticipatingPlayers().stream()
                                .findFirst()
                                .ifPresent(this::leave))
                        .andThen(minigame1 -> restart())
        );
    }
    private DialogueBuilder createVoteDialgoue(){
        return new DialogueBuilder(OPTION)
                .firstOption(GameType.DHAROK.toString(), player1 -> vote(player1, GameType.DHAROK))
                .secondOption(GameType.ZERKER.toString(), player1 -> vote(player1, GameType.ZERKER))
                .thirdOption(GameType.PURE.toString(), player1 -> vote(player1, GameType.PURE))
                .fourthOption(GameType.F2P.toString(), player1 -> vote(player1, GameType.F2P))
                .fifthOption("Next Page", new DialogueBuilder(DialogueLayout.OPTION)
                    .firstOption(GameType.DDS.toString(), player2 -> vote(player2, GameType.DDS))
                    .secondOption(GameType.RANGED.toString(), player2 -> vote(player2, GameType.RANGED))
                    .thirdOption(GameType.BRID.toString(), player2 -> vote(player2, GameType.BRID))
                    .fourthOption("Previous Page", player2 -> createVoteDialgoue().start(player2)
                    )::start
                );
    }


    private static void leaveArena(Player player) {
        player.getEquipment().clear(true);
        player.getInventory().removeAllBut(995);
        player.getInventory().refresh();
        player.getUpdateMasks().register(new AppearanceFlag(player));
    }

    private static boolean canEnter(Player player){
        for(Item t : player.getEquipment().getItems()) {
            if(t != null && t.getId() > 0) {
                player.getActionSender().sendMessage("You must bank your equipment");
                return false;
            }
        }
        for(Item t : player.getInventory().getItems()) {
            if(t != null && t.getId() > 0 && t.getId() != ENTRY_ITEM_ID) {
                player.getActionSender().sendMessage("You must bank your inventory");
                return false;
            }
        }
        if (player.getInventory().contains(ENTRY_ITEM_ID) && player.getInventory().getCount(ENTRY_ITEM_ID) >= ENTRY_AMOUNT)
            return true;
        else {
            player.getActionSender().sendMessage("You need "+ENTRY_AMOUNT+"x "+ ItemDefinition.forId(ENTRY_ITEM_ID).getName()+" to enter.");
            return false;
        }
    }

    private static Optional<Player> getFFAKiller(Player player) {
        return Optional.ofNullable(player.getImpactHandler().getMostDamageEntity().asPlayer());
    }

    @Override public String identifier() {
        return NAME;
    }
    @Override public boolean contains(Entity gameActor) { return ZONE.insideBorder(gameActor); }
    @Override public boolean death(Entity e, Entity killer) {
        if(e instanceof Player) {

            final Player player = e.asPlayer();

            getFFAKiller(player)
                .ifPresent(incrementKills()
                    .andThen(this::prepare)
                    .andThen(player1 -> {
                        if (getParticipatingPlayers().size() <= 1) payOutTo(player1);
                    })
                );
            player.getGameAttributes().saveAttribute("ffa_deaths", player.getGameAttributes().getAttribute("ffa_deaths", 0) + 1);
            player.getGameAttributes().saveAttribute("ffa_played", player.getGameAttributes().getAttribute("ffa_played", 0) + 1);
            leave(player);

            return true;
        }
        return false;
    }
    @Override public Optional<ZoneBorders[]> borders(){ return Optional.of(new ZoneBorders[]{ZONE}); }
    @Override public PulseBuilder<Minigame> buildGameTask() {
        return new PulseBuilder<Minigame>(this)
                .stopWhen(game -> getParticipants().size() >= MINIMAL_PARTICIPANTS)
                .onStop(game -> determineType())
                .onStop(game -> message("The game type is "+type.toString()+"!"))
                .onStop(game ->
                        new PulseBuilder<>(game)
                                .add(2, game2 -> message("game will initialize in "+ MILLISECONDS.toSeconds(GAME_CYCLES.toMillis(18))+"!"))
                                .add(11, game2 -> message("game will initialize in "+MILLISECONDS.toSeconds(GAME_CYCLES.toMillis(9))+"!"))
                                .add(17, game2 -> message("game will initialize in "+MILLISECONDS.toSeconds(GAME_CYCLES.toMillis(6))+"!"))
                                .add(20, game2 -> message("game will initialize in "+MILLISECONDS.toSeconds(GAME_CYCLES.toMillis(3))+"!"))
                                .stopAfter(21)
                                .onStop(championsGuild -> create().start())
                            .start()
                );
    }
    @Override protected void enter(Player player) {

        if(!IS_EVENT) {
            if (!canEnter(player)) {
                player.getActionSender().sendMessage("You need " + TextUtils.formatCoinValue(ENTRY_AMOUNT) + " in order to enter.");
                player.getInterfaceState().close();
                return;
            }
            payEntry(player);
        }

        addParticipant(player);

        determineType();

        getParticipatingPlayers().forEach(this::prepare);

        new PulseBuilder<>(player)
                .onStart(Players.sendMessage("You enter the Guild."))
                .add(1, Players.sendAnim(828))
                .add(2, Players.moveTo(WAIT_POSITION.randomize())
                        .andThen(Players.closeWindows())
                ).add(3,
                p -> getParticipatingPlayers().size() < MINIMAL_PARTICIPANTS,
                p -> message("Need "+(MINIMAL_PARTICIPANTS- getParticipatingPlayers().size())+" more players to initialize game.")
        ).start();
    }
    @Override protected void leave(Player player) {
        leaveArena(player);

        player.getInteraction().setDefault();
        player.teleport(LOBBY);

        switch (getStage()){
            case PRE_GAME:
                if(!IS_EVENT && getParticipatingPlayers().contains(player))
                    refundEntry(player);
                break;
            case GAME:
                restorePlayer(player);
                if(player.getSkills().getLevel(Skills.CONSTRUCTION) <= 0)
                    player.getActionSender().sendMessage("You died and got moved out of the game.");
                break;
            case POST_GAME:
                payOutTo(player);
                restorePlayer(player);
                break;
        }

        getParticipants().remove(player);
        getParticipatingPlayers().remove(player);

        player.getPrayer().toggleAll(true);

        Equipment.updateBonuses(player);

        player.getEquipment().update(true);
        player.getUpdateMasks().register(new AppearanceFlag(player));


    }

    @Override public boolean interact(Entity e, Node target, Option option) {
        e.sendChat("INTERACTING");
        if(target instanceof GameObject){
            GameObject gameObject = target.asObject();
            switch (gameObject.getId()){
                case 3192:
                    Scoreboards.open(player, Scoreboards.TOP_FFA_WIN_KDR);
                    return true;
                case CHAMPIONS_STATUE:
                    ObjectBuilder.replace(gameObject, new GameObject(10555, gameObject.getLocation(), gameObject.getRotation()));
                    return true;
                case CHAMPIONS_STATUE_OPEN:

                    if(IS_EVENT)
                        return true;

                    if(getStage().equals(PRE_GAME)) {
                        if(IS_EVENT){
                            player.debug("Joining event.");
                            enter(player);
                        } else if (canEnter(player))
                            new DialogueBuilder(DialogueLayout.NPC_STATEMENT).setDialogueText(
                                "Please vote for your preferred game type."
                            ).add(createVoteDialgoue())
                                .start(player);
                    }
                    return true;
                case CHAMPIONS_DOOR:
                    if(getStage().equals(PRE_GAME))
                        leave(player);
                    else player.getActionSender().sendMessage("You cannot do this while a game is started.");
                    return true;
            }
        }
        return super.interact(e, target, option);
    }

    @Override public ActivityPlugin newInstance(Player p) throws Throwable { return this; }
    @Override public Location getSpawnLocation() { return null; }
    enum GameType {
        DHAROK(false, new Item[]{
            new Item(14484,     1),
            new Item(4718,      1),
            new Item(2442,      1),
            new Item(2436,      1),
            new Item(2440,      1),
            new Item(6685,      1),
            new Item(3024,      2),
            new Item(9075,      200),
            new Item(560,       100),
            new Item(557,       500),
            new Item(15272,     20)
        },  new int[][] {
            { Equipment.SLOT_HAT,       4716 },
            { Equipment.SLOT_CAPE,      19111 },
            { Equipment.SLOT_AMULET,    6585 },
            { Equipment.SLOT_WEAPON,    4151 },
            { Equipment.SLOT_CHEST,     4720 },
            { Equipment.SLOT_SHIELD,    20072 },
            { Equipment.SLOT_LEGS,      4722 },
            { Equipment.SLOT_HANDS,     7462 },
            { Equipment.SLOT_FEET,      11732 },
            { Equipment.SLOT_RING,      6737 },
            { Equipment.SLOT_AMMO,      9244 }
        }, PrayerBook.CURSES,
            SpellBookManager.SpellBook.LUNAR),
        ZERKER(false, new Item[]{
            new Item(11694,     1),
            new Item(18353,     1),
            new Item(2442,      1),
            new Item(2436,      1),
            new Item(2440,      1),
            new Item(6685,      1),
            new Item(3024,      2),
            new Item(9075,      200),
            new Item(560,       100),
            new Item(557,       500),
            new Item(15272,     20)
        }, new int[][] {
            { Equipment.SLOT_HAT,       3751 },
            { Equipment.SLOT_CAPE,      19111 },
            { Equipment.SLOT_AMULET,    6585 },
            { Equipment.SLOT_WEAPON,    4151 },
            { Equipment.SLOT_CHEST,     10551 },
            { Equipment.SLOT_SHIELD,    8850 },
            { Equipment.SLOT_LEGS,      1079 },
            { Equipment.SLOT_HANDS,     7462 },
            { Equipment.SLOT_FEET,      4131 },
            { Equipment.SLOT_RING,      6737 },
            { Equipment.SLOT_AMMO,      9244 }
        }, PrayerBook.CURSES,
            SpellBookManager.SpellBook.LUNAR),
        PURE(false, new Item[]{
            new Item(5680,      1),
            new Item(9185,      1),
            new Item(2442,      1),
            new Item(2436,      1),
            new Item(2440,      1),
            new Item(6685,      1),
            new Item(3024,      2),
            new Item(15272,     20)
        }, new int[][] {
            { Equipment.SLOT_HAT,       1153 },
            { Equipment.SLOT_CAPE,      6570 },
            { Equipment.SLOT_AMULET,    6585 },
            { Equipment.SLOT_WEAPON,    4151 },
            { Equipment.SLOT_CHEST,     1115 },
            { Equipment.SLOT_SHIELD,    3842 },
            { Equipment.SLOT_LEGS,      2497 },
            { Equipment.SLOT_HANDS,     7459 },
            { Equipment.SLOT_FEET,      2577 },
            { Equipment.SLOT_RING,      2550 },
            { Equipment.SLOT_AMMO,      9244 } }
            , PrayerBook.CURSES,
            SpellBookManager.SpellBook.LUNAR),
        F2P(false, new Item[]{
            new Item(1333,      1),
            new Item(1319,      1),
            new Item(113,       1),
            new Item(2434,      1),
            new Item(373,       24)
        }, new int[][] {
            { Equipment.SLOT_HAT,       1169 },
            { Equipment.SLOT_CAPE,      6568 },
            { Equipment.SLOT_AMULET,    1725 },
            { Equipment.SLOT_WEAPON,    853 },
            { Equipment.SLOT_CHEST,     544 },
            { Equipment.SLOT_LEGS,      1099 },
            { Equipment.SLOT_HANDS,     1065 },
            { Equipment.SLOT_FEET,      1061 },
            { Equipment.SLOT_RING,      2550 },
            { Equipment.SLOT_AMMO,      890 }
        }, PrayerBook.NORMAL,
            SpellBookManager.SpellBook.LUNAR),

        DDS(false, new Item[]{
            new Item(2440,      1),
            new Item(2436,      1),
        },new int[][] {
            { Equipment.SLOT_HAT,       -1 },
            { Equipment.SLOT_CAPE,      -1 },
            { Equipment.SLOT_AMULET,    -1 },
            { Equipment.SLOT_WEAPON,    1215 },
            { Equipment.SLOT_CHEST,     -1 },
            { Equipment.SLOT_SHIELD,    -1 },
            { Equipment.SLOT_LEGS,      -1 },
            { Equipment.SLOT_HANDS,     7462 },
            { Equipment.SLOT_FEET,      -1 },
            { Equipment.SLOT_RING,      -1 },
            { Equipment.SLOT_AMMO,      -1 }
        }, PrayerBook.CURSES,
            SpellBookManager.SpellBook.LUNAR),

        RANGED(false, new Item[]{
            new Item(11235,     1),
            new Item(11212,     50),
            new Item(15324,     1),
            new Item(6685,      1),
            new Item(3024,      2),
            new Item(9075,      200),
            new Item(560,       100),
            new Item(557,       500),
            new Item(15272,     20)
        }, new int[][] {
            { Equipment.SLOT_HAT,       3749 },
            { Equipment.SLOT_CAPE,      10499 },
            { Equipment.SLOT_AMULET,    15126 },
            { Equipment.SLOT_WEAPON,    13051 },
            { Equipment.SLOT_CHEST,     4736 },
            { Equipment.SLOT_SHIELD,    3842 },
            { Equipment.SLOT_LEGS,       4738 },
            { Equipment.SLOT_HANDS,     7462 },
            { Equipment.SLOT_FEET,      10696 },
            { Equipment.SLOT_RING,      15019 },
            { Equipment.SLOT_AMMO,      9244 }
        }, PrayerBook.CURSES,
            SpellBookManager.SpellBook.LUNAR),

        BRID(false, new Item[]{
            new Item(11694,     1),
            new Item(4151,      1),
            new Item(20072,     1),
            new Item(10551,     1),
            new Item(4736,      1),
            new Item(4722,      1),
            new Item(11732,     1),
            new Item(14176,     1),
            new Item(14188,     1),
            new Item(14427,     1),
            new Item(14415,     1),
            new Item(555,       4500),
            new Item(560,       3000),
            new Item(565,       1500),
            new Item(15272,     16)
        },  new int[][] {
            { Equipment.SLOT_HAT,       10828 },
            { Equipment.SLOT_CAPE,      19111 },
            { Equipment.SLOT_AMULET,    6585 },
            { Equipment.SLOT_WEAPON,    6914 },
            { Equipment.SLOT_CHEST,     4712 },
            { Equipment.SLOT_SHIELD,    3842 },
            { Equipment.SLOT_LEGS,      4714 },
            { Equipment.SLOT_HANDS,     7462 },
            { Equipment.SLOT_FEET,      6920 },
            { Equipment.SLOT_RING,      6737 },
            { Equipment.SLOT_AMMO,      -1 }
        }, PrayerBook.CURSES,
            SpellBookManager.SpellBook.ANCIENT)
        ;

        boolean overheadsAllowed;
        Item[] inventory;
        int[][] equipment;

        PrayerBook prayerbook;
        SpellBookManager.SpellBook magicSpellbook;

        GameType(boolean overheadsAllowed, Item[] inventory, int[][] equipment, PrayerBook prayerbook, SpellBookManager.SpellBook magicSpellbook) {
            this.overheadsAllowed = overheadsAllowed;
            this.inventory = inventory;
            this.equipment = equipment;
            this.prayerbook = prayerbook;
            this.magicSpellbook = magicSpellbook;
        }



        public boolean isOverheadsAllowed() {
            return overheadsAllowed;
        }

        public Item[] getInventory() {
            return inventory;
        }

        public int[][] getEquipment() {
            return equipment;
        }

        public PrayerBook getPrayerBook() {
            return prayerbook;
        }

        public SpellBookManager.SpellBook getSpellBookManager() {
            return magicSpellbook;
        }

        @Override
        public String toString() {
            return StringUtils.capitalize(name().toLowerCase());
        }
    }
}
