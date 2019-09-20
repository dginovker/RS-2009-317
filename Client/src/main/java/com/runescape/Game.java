package main.java.com.runescape;

import com.runescape.cache.CacheUtil;
import com.runescape.cache.Index;
import com.runescape.cache.config.MessageCensor;
import com.runescape.cache.config.VariableBits;
import com.runescape.cache.config.VariableParameter;
import com.runescape.cache.config.ge.GrandExchange;
import com.runescape.cache.config.ge.GrandExchangeOffer;
import main.java.com.runescape.cache.def.*;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.def.item.OSRSItemDefinition;
import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.def.npc.OSRSNPCDefinition;
import com.runescape.cache.def.object.OSRSObjectDefinition;
import com.runescape.cache.def.object.ObjectDefinition;
import main.java.com.runescape.cache.media.*;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.texture.TextureAnimating;
import com.runescape.cache.texture.TextureLoader317;
import com.runescape.chat.ChatMessage;
import com.runescape.chat.ChatMessageType;
import com.runescape.chat.ClanChatMember;
import com.runescape.chat.MessageType;
import com.runescape.collection.Deque;
import com.runescape.collection.Linkable;
import com.runescape.media.Animation;
import com.runescape.media.ImageProducer;
import com.runescape.media.Raster;
import com.runescape.media.Scrollbar;
import com.runescape.media.component.ComponentDrawing;
import com.runescape.media.font.GameFont;
import com.runescape.media.gameframe.GameCursor;
import com.runescape.media.gameframe.Gameframe;
import com.runescape.media.gameframe.StatusOrb;
import com.runescape.media.gameframe.chat.ChannelButton;
import com.runescape.media.gameframe.impl.Gameframe474;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;
import com.runescape.media.renderable.StaticObject;
import com.runescape.media.renderable.entity.Entity;
import com.runescape.media.renderable.entity.Item;
import com.runescape.media.renderable.entity.NPC;
import com.runescape.media.renderable.entity.Player;
import main.java.com.runescape.net.*;
import com.runescape.net.packet.PacketSender;
import com.runescape.net.protocol.ProtocolConstants;
import com.runescape.net.requester.Resource;
import com.runescape.net.requester.ResourceProvider;
import com.runescape.net.security.ISAACCipher;
import com.runescape.scene.SceneObject;
import com.runescape.scene.SceneProjectile;
import com.runescape.scene.SceneSpotAnim;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.graphic.TextureLoader667;
import com.runescape.scene.map.CollisionMap;
import com.runescape.scene.map.MapRegion;
import com.runescape.scene.map.SceneGraph;
import com.runescape.scene.map.object.GroundDecoration;
import com.runescape.scene.map.object.SpawnedObject;
import com.runescape.scene.map.object.Wall;
import com.runescape.scene.map.object.WallDecoration;
import com.runescape.scene.map.object.tile.Floor;
import com.runescape.scene.map.object.tile.FloorOverlay;
import com.runescape.sign.SignLink;
import com.runescape.sound.SoundPlayer;
import com.runescape.sound.Track;
import main.java.com.runescape.util.*;
import com.runescape.world.WorldDefinition;
import org.nikkii.alertify4j.Alertify;
import org.nikkii.alertify4j.AlertifyBuilder;
import org.nikkii.alertify4j.AlertifyType;

import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class Game extends GameShell {

    public static String[] latestCommandArray = new String[100];
    public static int latestCommandCount = 0;
    public static int latestCommandCaret = 0;

    public static int[] antialiasingPixels;
    public static int[] antialiasingOffsets;
    public static boolean notifications = true;
    public static boolean antialiasing = false;// xd
    public static boolean shiftClickDrop = true;
    public static final int[][] anIntArrayArray1003 = {
            {6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983,
                    54193},
            {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153,
                    56621, 4783, 1341, 16578, 35003, 25239},
            {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094,
                    10153, 56621, 4783, 1341, 16578, 35003},
            {4626, 11146, 6439, 12, 4758, 10270},
            {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
    public static final int[] tabInterfaceIDs = {-1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public static final int[] anIntArray1204 = {9104, 10275, 7595, 3610, 7975,
            8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991,
            25486};

	/* End of Music Packer */
    /**
     * The {@link java.util.logging.Logger} instance for debugging.
     */
    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private static final long serialVersionUID = 5707517957054703648L;
    private static final int[] SKILL_EXPERIENCE;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    public static Game INSTANCE;
    public static String loadingText = "";
    public static int loadingPercentage;
    public static int portOffset;
    public static int chatScrollPosition;
    public static int spellId = 0;
    public static boolean loggedIn;
    public static int loopCycle;
    public static int chatScrollMax;
    public static int currentTabId;
    public static boolean needDrawTabArea;
    public static boolean inputTaken;
    public static int[] BIT_MASKS;
    public static int[] fullScreenTextureArray;
    public static int screenAreaWidth = 512;
    public static int screenAreaHeight = 334;
    public static int cameraZoom = 600;
    public static boolean showChatComponents = true;
    public static boolean showTabComponents = true;
    public static boolean tabAreaAltered;
    public static int[] anIntArray1181;
    public static int[] anIntArray1182;
    public static int[] anIntArray1180;
    public static int anInt1117;
    public int worldId = 10;
    private static int anInt849;
    private static int regionTimer;
    private static boolean isMembers = true;
    private static boolean lowMemory = false;
    private static int anInt986;
    private static int anInt1005;
    private static int anInt1051;
    private static int anInt1097;
    private static Player localPlayer;
    private static int anInt1134;
    private static int anInt1142;
    private static int anInt1155;
    private static int anInt1175;
    private static boolean flagged;
    private static int anInt1226;
    private static int anInt1288;
    private static boolean NOCLIP = false;
    private static ScreenMode frameMode = ScreenMode.FIXED;
    private static int frameWidth = 765;
    private static int frameHeight = 503;
    public final int[] anIntArray968;
    public final Index[] indices;
    public final int[] minimapLeft;
    public final int[] anIntArray1057;
    public final RSComponent scrollbarComponent;
    public final int[] minimapLineWidth;
    public final int[] currentStats;
    public final int[] maximumLevels;
    public final int[] characterDesignColours;
    public final boolean aBoolean994;
    public final int[] anIntArray1065;
    private final int[] currentExp;
    private final int[] verticalSpeeds;
    private final boolean[] cameraViews;
    private final int maxPlayers;
    private final int internalLocalPlayerIndex;
    private final long[] ignores;
    private final int[] horizontalSpeeds;
    private final Map<Integer, Gameframe> GAMEFRAME_MAP = new HashMap<>();
    private final int[] anIntArray965 = {0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff};
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] textColourEffect;
    private final int[] anIntArray981;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private final int[] hotizontalSpeed;
    private final int[] anIntArray1045;
    private final int[] archiveCRCs;
    private final String[] atPlayerActions;
    private final boolean[] atPlayerArray;
    private final int[][][] anIntArrayArrayArray1129;
    private final int[] anIntArray1177 = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    private final int[] horizontalAmounts;
    private final int[] tracks;
    private final int[] privateMessageIds;
    private final int[] trackLoops;
    private final int[] soundDelay;
    private final boolean rsAlreadyLoaded;
    private final int[] soundVolume;
    private final ClientSetting clientSetting;
    private final PacketSender packetSender;

    public PacketSender getPacketSender() {
        return packetSender;
    }

    private final ComponentDrawing componentDrawing;
    public boolean[] spriteAlphaForwards = new boolean[20];
    public int[][] spriteAlphaValues = new int[20][20];
    public int[][] spriteTimers = new int[20][20];
    public ArrayList<String> SUMMONING_OPTIONS;
    public int tabHover = -1;
    public CacheArchive mediaCacheArchive;
    public int rights;
    public int chatTypeView;
    public int clanChatMode;
    public int[] friendsNodeIDs;
    public Deque[][][] groundItems;
    public NPC[] npcs;
    public int npcCount;
    public int[] npcIndices;
    public String prayerBook;
    public int gameFilterMode;
    public int hintIconDrawType;
    public Sprite mapFlag;
    public Sprite mapMarker;
    public Sprite arrowUp;
    public Sprite arrowDown;
    public Sprite arrowLeft;
    public Sprite arrowRight;
    public Sprite questTabIcon;
    public Sprite achievementTabIcon;
    public Sprite informationTabIcon;
    public Player[] players;
    public int playerCount;
    public int[] playerIndices;
    public int anInt897;
    public int friendsCount;
    public int plane;
    public int hintIconPlayerId;
    public int hintIconX;
    public int hintIconY;
    public long[] friendsListAsLongs;
    public int[] settings;
    public int anInt988;
    public int minimapState;
    public static int regionBaseX;
    public static int regionBaseY;
    public int dialogueId;
    public ResourceProvider resourceProvider;
    public int minimapCount;
    public int[] minimapHintX;
    public int[] minimapHintY;
    public Sprite mapDotItem;
    public Sprite mapDotNPC;
    public Sprite mapDotPlayer;
    public Sprite mapDotFriend;
    public Sprite mapDotTeam;
    public Sprite mapDotClan;
    public int[] secondMenuAction;
    public int[] menuActionId;
    public int assistMode;
    public Sprite compass;
    public int anInt1132;
    public Sprite[] minimapHint;
    public int minimapZoom;
    public int anInt1171;
    public int cameraHorizontal;
    public String[] menuActionName;
    public String[] menuActionName2;
    public int minimapRotation;
    public int anInt1210;
    public int hintIconNpcId;
    public int inputState;
    public int destX;
    public int destY;
    public Sprite minimapImage;
    public GameFont smallFont, regularFont, boldFont, fancyFont, smallArialFont, forumLoginFont, bigArialFont;
    public GameFont rsFancyFont;
    public int backDialogueId;
    public int anInt1279;
    public int drawCount;
    public int fullscreenInterfaceID;
    public int anInt1044;
    public int anInt1129;
    public int anInt1315;
    public int anInt1500;
    public int anInt1501;
    public boolean isPoisoned;
    public ChannelButton currentHoveredButton;
    public ChannelButton currentActiveButton;
    public SceneGraph scene;
    public Sprite[] mapFunctions;
    public Sprite[] mapFunctions2;
    public Background[] mapScenes;
    public int overlayInterfaceId;
    public int minimapInterfaceId;
    public boolean messagePromptRaised;
    public byte[][][] tileFlags;
    public boolean rememberMe;
    public boolean runToggled;
    public String clickToContinueString;
    public String inputContext;
    public String amountOrNameInput;
    public Sprite mapEdge;
    public long xpDropped = 0;
    public int[] xpDrops;
    public int[] floatingDrops;
    public int[] xpDropPosition = new int[Skills.SKILL_COUNT];
    public int[] xpDrawing = new int[Skills.SKILL_COUNT];
    public int xpDrawn = 0;
    public Sprite[] skillIcons = new Sprite[Skills.SKILL_COUNT];
    public CollisionMap[] collisionMaps;
    public int menuActionRow;
    public int loginScreenCursorPos;
    public int cycleTimer;
    public int anInt1264;
    public boolean runHover;
    public String reportAbuseInput;
    public Sprite playerDisabledSprite;
    public Sprite playerEnabledSprite;
    public int dragCycle;
    public int daysSinceLastLogin;
    public Sprite scrollbar1;
    public Sprite scrollbar2;
    public Sprite scrollbar3;
    public Sprite scrollbar4;
    public Sprite scrollbarLeft;
    public Sprite scrollbarRight;
    public Sprite[] horizontalScrollbar;
    public Sprite[] newScrollbar;
    public boolean aBoolean1031;
    public boolean maleCharacter;
    public int focusedComponent;
    public int dragFromSlot;
    public int activeInterfaceType;
    public int pressX;
    public int pressY;
    public int spellSelected;
    public boolean continuedDialogue;
    public boolean canMute;
    public int anInt1193;
    public int atInventoryInterface;
    public int atInventoryIndex;
    public int atInventoryInterfaceType;
    public int itemSelected;
    public int lastItemSelectedSlot;
    public int lastItemSelectedInterface;
    public int[] bankInvTemp = new int[952];
    public int[] bankStackTemp = new int[952];
    public int[] tabAmounts = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int[] tabFirstItems = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public Sprite bankNewTab;
    public int anInt886;
    public boolean aBoolean972;
    public int[] firstMenuAction;
    public int[] selectedMenuActions;
    public int dragPosition;
    public int mouseInvInterfaceIndex;
    public int lastActiveInvInterface;
    public int spellUsableOn;
    public String spellTooltip;
    public String selectedItemName;
    boolean loginButtonHover, input1Hover, input2Hover, rememberMeButtonHover;
    private int openInterfaceId;
    private int runInterfaceId;
    private java.util.List<RSComponent> tooltopComponents;
    private GrandExchange grandExchange;
    private Summoning summoning;

    public Summoning getSummoning() {
        return summoning;
    }

    /**
     * The background sprite.
     */
    private Sprite background;
    /**
     * The loading bar sprite.
     */
    private Sprite loadingBar;
    /**
     * The loading back sprite.
     */
    private Sprite loadingBack;
    /**
     * The loading text sprite.
     */
    private Sprite loadingTextSprite;
    /**
     * The login box sprite.
     */
    private Sprite loginBox;
    /**
     * The login button sprite.
     */
    private Sprite loginButton, loginButtonHoverSprite;
    /**
     * The logo sprite.
     */
    private Sprite logo;
    /**
     * The username input sprite.
     */
    private Sprite usernameInputBox;
    /**
     * The password input sprite.
     */
    private Sprite passwordInputBox;
    /**
     * The login message sprite.
     */
    private Sprite loginMessage;
    /**
     * The remember off sprite.
     */
    private Sprite rememberOff;
    /**
     * The remember on sprite.
     */
    private Sprite rememberOn;
    /**
     * The select a world sprite.
     */
    private Sprite selectAWorld;
    /**
     * The world header sprite.
     */
    private Sprite worldHeader;
    /**
     * The world switch sprite.
     */
    private Sprite worldSwitch;
    /**
     * The world sort ascending green sprite.
     */
    private Sprite worldAscendGreen;
    /**
     * The world sort ascending red sprite.
     */
    private Sprite worldAscendRed;
    /**
     * The world sort descending green sprite.
     */
    private Sprite worldDescendGreen;
    /**
     * The world sort descending red sprite.
     */
    private Sprite worldDescendRed;
    /**
     * The world select country sprites.
     */
    private Sprite[] worldCountrySprites = new Sprite[8];
    /**
     * The free-to-play world select button.
     */
    private Sprite worldSelectFree;
    /**
     * The hovered free-to-play world select button.
     */
    private Sprite worldSelectFreeHover;
    /**
     * The pay-to-play world select button.
     */
    private Sprite worldSelectMembers;
    /**
     * The hovered pay-to-play world select button.
     */
    private Sprite worldSelectMembersHover;
    /**
     * The skill menu select sprite.
     */
    private Sprite skillMenuSelect;
    /**
     * The skill menu select bottom sprite.
     */
    private Sprite skillMenuSelectBottom;
    /**
     * The hp empty sprite.
     */
    private Sprite hpEmpty;
    /**
     * The hp full sprite.
     */
    private Sprite hpFull;
    /**
     * The hit damage sprite.
     */
    private Sprite hitDamage;
    /**
     * The hit defend sprite.
     */
    private Sprite hitDefend;
    /**
     * The hit disease sprite.
     */
    private Sprite hitDisease;
    /**
     * The hit poison sprite.
     */
    private Sprite hitPoison;
    /**
     * The accept button disabled sprite.
     */
    private Sprite acceptButtonDisabled;
    /**
     * The accept button disabled hover sprite.
     */
    private Sprite acceptButtonDisabledHover;
    private Sprite bankTabEmpty;
    private Sprite chatButtons;
    private Sprite chatTab;
    private Sprite chatTabOn;
    private Sprite chatTabHover;
    private ImageProducer worldSelectImageProducer;
    private ImageProducer chatSettingImageProducer;
    private ImageProducer titleScreen;
    private ImageProducer leftFrame;
    private ImageProducer topFrame;
    private ImageProducer rightFrame;
    private boolean loggingIn;
    private GameCursor gameCursor;
    private int highestAmtToLoad = 0;
    private Gameframe gameframe;
    private int setChannel;
    private int currentTrackTime;
    private long trackTimer;
    @SuppressWarnings("unused")
    private int currentTrackLoop;
    private boolean mediaMode;
    private int ignoreCount;
    private long aLong824;
    private int[][] anIntArrayArray825;
    private int[] friendsClanRankIDs;
    private Socket jaggrab;
    private RSStream chatBuffer;
    private int anInt839;
    private int[] anIntArray840;
    private int lastOpcode;
    private int secondLastOpcode;
    private int thirdLastOpcode;
    private int privateChatMode;
    private RSStream login;
    private boolean aBoolean848;
    private int xCameraPos;
    private int zCameraPos;
    private int yCameraPos;
    private int yCameraCurve;
    private int xCameraCurve;
    private int myPrivilege;
    public int[] chatIcons = new int[8]; // max of 8 icons
    private ClanChatMember[] clanChatMembers;
    private String chatOwnerName;
    private String clanChatName;
    private int clanChatSize;
    private byte chatKickRights;
    private byte currentUserClanRights;
    private int weight;
    private MouseDetection mouseDetection;
    private int localPlayerIndex;
    private boolean menuOpen;
    private String inputString;
    private int mobsAwaitingUpdateCount;
    private int[] mobsAwaitingUpdate;
    private RSStream[] playerSynchronizationBuffers;
    private int cameraRotation;
    private int friendServerStatus;
    private int[][] anIntArrayArray901;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    private boolean loadingError;
    private boolean worldError;
    private int[][] anIntArrayArray929;
    private int hintIconZ;
    private int anInt937;
    private int anInt938;
    private ChatMessage[] chatMessages;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int menuHeight;
    private long aLong953;
    private boolean screenFocused;
    private int currentSong;
    private int spriteDrawX;
    private int spriteDrawY;
    private Sprite[] hitTypeIcon;
    private int[] interfaceConfig;
    private int anInt984;
    private int anInt985;
    private Sprite[] hitMarks;
    private int anInt995;
    private int anInt996;
    private int anInt997;
    private int anInt998;
    private int anInt999;
    private Sprite multiOverlay;
    private int packetSize;
    private int opcode;
    private int timeoutCounter;
    private int anInt1010;
    private int anInt1011;
    private Deque projectiles;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int openWalkableInterface;
    private int duplicateClickCount;
    private int loadingStage;
    private Sprite reportButton;
    private int anInt1026;
    private int anInt1036;
    private int anInt1037;
    private int loginFailures;
    private int anInt1039;
    private int member;
    private int anInt1048;
    private CacheArchive titleCacheArchive;
    private int flashingSidebarId;
    private int multiCombat;
    private Deque incompleteAnimables;
    private int trackCount;
    private int friendsListAction;
    public static int currentRegionX;
    public static int currentRegionY;
    private boolean validLocalMap;
    private String[] friendsList;
    private RSStream inStream;
    private Sprite[] headIcons;
    private Sprite[] skullIcons;
    private Sprite[] headIconsHint;
    private int x;
    private int y;
    private int height;
    private int speed;
    private int angle;
    private int systemUpdateTime;
    private int membersInt;
    private String promptMessage;
    private int cameraY;
    public int anInt1137;
    private boolean inPlayerOwnedHouse;
    private int runEnergy;
    private Sprite[] crosses;
    private boolean constructedViewport;
    private boolean oriented;
    private int daysSinceRecovChange;
    private BufferedConnection socketStream;
    private int privateMessageCount;
    private String loginUsername;
    private String loginPassword;
    private boolean genericLoadingError;
    private int reportAbuseInterfaceID;
    private Deque spawns;
    private byte[][] aByteArrayArray1183;
    private int anInt1184;
    private int anInt1186;
    private int anInt1187;
    private RSStream outgoing;
    private int splitPrivateChat;
    private Background mapBack;
    private String promptInput;
    private int[][][] tileHeights;
    private long serverSeed;
    private long aLong1220;
    private int nextSong;
    private boolean fadeMusic;
    private int[] anIntArray1234;
    private int[] anIntArray1235;
    private int[] anIntArray1236;
    private int anInt1237;
    private int anInt1238;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private byte[][] aByteArrayArray1247;
    private int tradeMode;
    private int anInt1249;
    private boolean worldSelect;
    private boolean drawingWorldSelect;
    private WorldDefinition worldDefinition;
    private int anInt1253;
    private boolean welcomeScreenRaised;
    private int prevSong;
    private int anInt1265;
    private String loginMessage1;
    private String loginMessage2;
    private String loginMessage3;
    private int localX;
    private int localY;
    private Image[] loadingSprite = new Image[5];
    private int cameraX;
    private int[] bigX;
    private int[] bigY;
    private int anInt1285;
    private int publicChatMode;
    private CRC32 indexCrc = new CRC32();
    private int lastWorldUpdateStamp = 0x42DFA7E;

    public Game() {
        Gameframe[] gameFrames = new Gameframe[]{
                //  new Gameframe530(this),
                new Gameframe474(this)
        };
        for (Gameframe gameframe1 : gameFrames) {
            GAMEFRAME_MAP.put(gameframe1.getId(), gameframe1);
        }
        this.gameframe = GAMEFRAME_MAP.get(474);
        clientSetting = new ClientSetting(this);
        SUMMONING_OPTIONS = new ArrayList<>();
        fullscreenInterfaceID = -1;
        soundVolume = new int[50];
        chatTypeView = 0;
        clanChatMode = 0;
        currentHoveredButton = null;
        currentActiveButton = ChannelButton.ALL;
        anIntArrayArray825 = new int[104][104];
        friendsNodeIDs = new int[200];
        friendsClanRankIDs = new int[200];
        groundItems = new Deque[4][104][104];
        chatBuffer = new RSStream(new byte[5000]);
        npcs = new NPC[16384];
        npcIndices = new int[16384];
        anIntArray840 = new int[1000];
        login = RSStream.create();
        aBoolean848 = true;
        openInterfaceId = -1;
        runInterfaceId = -1;
        tooltopComponents = new ArrayList<>();
        currentExp = new int[Skills.SKILL_COUNT];
        xpDrops = new int[Skills.SKILL_COUNT];
        floatingDrops = new int[Skills.SKILL_COUNT];
        xpDropped = 0;
        verticalSpeeds = new int[5];
        cameraViews = new boolean[5];
        reportAbuseInput = "";
        localPlayerIndex = -1;
        menuOpen = false;
        inputString = "";
        maxPlayers = 2048;
        internalLocalPlayerIndex = 2047;
        players = new Player[maxPlayers];
        playerIndices = new int[maxPlayers];
        mobsAwaitingUpdate = new int[maxPlayers];
        playerSynchronizationBuffers = new RSStream[maxPlayers];
        anInt897 = 1;
        anIntArrayArray901 = new int[104][104];
        currentStats = new int[Skills.SKILL_COUNT];
        ignores = new long[100];
        loadingError = false;
        horizontalSpeeds = new int[5];
        anIntArrayArray929 = new int[104][104];
        chatMessages = new ChatMessage[500];
        screenFocused = true;
        friendsListAsLongs = new long[200];
        hitTypeIcon = new Sprite[6];
        currentSong = -1;
        spriteDrawX = -1;
        spriteDrawY = -1;
        anIntArray968 = new int[33];
        indices = new Index[Constants.CACHE_INDICES];
        settings = new int[2000];
        interfaceConfig = new int[2000];
        aBoolean972 = false;
        anInt975 = 50;
        anIntArray976 = new int[anInt975];
        anIntArray977 = new int[anInt975];
        anIntArray978 = new int[anInt975];
        anIntArray979 = new int[anInt975];
        textColourEffect = new int[anInt975];
        anIntArray981 = new int[anInt975];
        anIntArray982 = new int[anInt975];
        aStringArray983 = new String[anInt975];
        anInt985 = -1;
        hitMarks = new Sprite[20];
        characterDesignColours = new int[5];
        aBoolean994 = false;
        inputContext = "";
        amountOrNameInput = "";
        projectiles = new Deque();
        aBoolean1017 = false;
        openWalkableInterface = -1;
        hotizontalSpeed = new int[5];
        aBoolean1031 = false;
        mapFunctions = new Sprite[100];
        mapFunctions2 = new Sprite[100];
        dialogueId = -1;
        maximumLevels = new int[Skills.SKILL_COUNT];
        anIntArray1045 = new int[2000];
        maleCharacter = true;
        minimapLeft = new int[152];
        minimapLineWidth = new int[152];
        flashingSidebarId = -1;
        incompleteAnimables = new Deque();
        anIntArray1057 = new int[33];
        scrollbarComponent = new RSComponent();
        mapScenes = new Background[100];
        anIntArray1065 = new int[7];
        minimapHintX = new int[1000];
        minimapHintY = new int[1000];
        validLocalMap = false;
        friendsList = new String[200];
        inStream = RSStream.create();
        archiveCRCs = new int[9];
        firstMenuAction = new int[500];
        secondMenuAction = new int[500];
        menuActionId = new int[500];
        selectedMenuActions = new int[500];
        headIcons = new Sprite[18];
        skullIcons = new Sprite[20];
        headIconsHint = new Sprite[20];
        tabAreaAltered = false;
        promptMessage = "";
        atPlayerActions = new String[5];
        atPlayerArray = new boolean[5];
        anIntArrayArrayArray1129 = new int[4][13][13];
        anInt1132 = 2;
        minimapHint = new Sprite[1000];
        inPlayerOwnedHouse = false;
        continuedDialogue = false;
        crosses = new Sprite[8];
        loggedIn = false;
        canMute = false;
        constructedViewport = false;
        oriented = false;
        anInt1171 = 1;
        loginUsername = "";
        loginPassword = "";
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        spawns = new Deque();
        anInt1184 = 128;
        overlayInterfaceId = -1;
        minimapInterfaceId = -1;
        outgoing = RSStream.create();
        menuActionName = new String[500];
        menuActionName2 = new String[500];
        horizontalAmounts = new int[5];
        tracks = new int[50];
        anInt1210 = 2;
        chatScrollMax = 78;
        promptInput = "";
        currentTabId = 3;
        inputTaken = false;
        fadeMusic = true;
        collisionMaps = new CollisionMap[4];
        privateMessageIds = new int[100];
        trackLoops = new int[50];
        aBoolean1242 = false;
        soundDelay = new int[50];
        rsAlreadyLoaded = false;
        welcomeScreenRaised = false;
        messagePromptRaised = false;
        loginMessage1 = "";
        loginMessage2 = "Login to Gielinor";
        loginMessage3 = "";
        backDialogueId = -1;
        anInt1279 = 2;
        bigX = new int[4000];
        bigY = new int[4000];
        packetSender = new PacketSender(this);
        componentDrawing = new ComponentDrawing(this);
        summoning = new Summoning(this);
    }

    public static boolean isFixed() {
        return frameMode == ScreenMode.FIXED;
    }

    public static void rebuildFrameSize(Game game, ScreenMode screenMode, int screenWidth,
                                        int screenHeight) {
        try {
            screenAreaWidth = (screenMode == ScreenMode.FIXED) ? 512
                    : screenWidth;
            screenAreaHeight = (screenMode == ScreenMode.FIXED) ? 334
                    : screenHeight;
            frameWidth = screenWidth;
            frameHeight = screenHeight;
            INSTANCE.refreshFrameSize(screenMode == ScreenMode.FULLSCREEN,
                    screenWidth, screenHeight,
                    screenMode == ScreenMode.RESIZABLE,
                    screenMode != ScreenMode.FIXED);
            game.gameframe.updateGameArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String intToKOrMilLongName(int i) {
        String s = String.valueOf(i);
        for (int k = s.length() - 3; k > 0; k -= 3) {
            s = s.substring(0, k) + "," + s.substring(k);
        }
        if (s.length() > 8) {
            s = "<col=00FF00>" + s.substring(0, s.length() - 8) + " million <col=FFFFFF>(" + s + ")";
        } else if (s.length() > 4) {
            s = "<col=00FFFF>" + s.substring(0, s.length() - 4) + "K <col=FFFFFF>(" + s + ")";
        }
        return " " + s;
    }

    public static String intToKOrMil(int j) {
        if (j < 0x186a0) {
            return String.valueOf(j);
        }
        if (j < 0x989680) {
            return j / 1000 + "K";
        } else {
            return j / 0xf4240 + "M";
        }
    }

    private static void setHighMem() {
        SceneGraph.lowMem = false;
        Rasterizer.lowMem = false;
        lowMemory = false;
        MapRegion.lowMem = false;
        ObjectDefinition.lowMemory = false;
        OSRSObjectDefinition.lowMemory = false;
    }

    public static void main(String args[]) {
        try {
            if (args != null) {
                if (args.length >= 1) {
                    Constants.DEBUG_MODE = args[0].equalsIgnoreCase("debug");
                }
                if (args.length >= 2) {
                    Constants.DEFAULT_WORLD_ID = Integer.parseInt(args[1]);
                }
                if (args.length >= 3) {
                    Constants.AUTO_LOGIN = args[2].equalsIgnoreCase("autologin");
                }
            }
            portOffset = 0;
            setHighMem();
            isMembers = true;
            SignLink.storeid = 32;
            SignLink.startpriv(InetAddress.getLocalHost());
            INSTANCE = new Game();
            frameMode(INSTANCE, ScreenMode.FIXED);
            INSTANCE.createClientFrame(frameWidth, frameHeight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setTab(int id, boolean hotkey) {
        if (!isFixed() && INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HOTKEY_CLOSE) && hotkey && currentTabId == id) {
            showTabComponents = !showTabComponents;
        }
        currentTabId = id;
        tabAreaAltered = true;
    }

    public static Player getLocalPlayer() {
        return localPlayer;
    }

    public static int[] getAnIntArray1180() {
        return anIntArray1180;
    }

    public static int[] getAnIntArray1182() {
        return anIntArray1182;
    }

    public static ScreenMode getScreenMode() {
        return frameMode;
    }

    public static boolean isScreenMode(ScreenMode screenMode) {
        return frameMode == screenMode;
    }

    public static int getFrameWidth() {
        return frameWidth;
    }

    public static int getFrameHeight() {
        return frameHeight;
    }

    public static void frameMode(Game game, ScreenMode screenMode) {
        if (frameMode != screenMode) {
            frameMode = screenMode;
            if (screenMode == ScreenMode.FIXED) {
                frameWidth = 765;
                frameHeight = 503;
                cameraZoom = 600;
                SceneGraph.viewDistance = 9;
                INSTANCE.interfaceConfig[107] = 0;
                INSTANCE.interfaceConfig[109] = 0;
                INSTANCE.interfaceConfig[234] = 0;
            } else if (screenMode == ScreenMode.RESIZABLE) {
                frameWidth = 766;
                frameHeight = 529;
                cameraZoom = 850;
                SceneGraph.viewDistance = 10;
            } else if (screenMode == ScreenMode.FULLSCREEN) {
                cameraZoom = 600;
                SceneGraph.viewDistance = 10;
                frameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                frameHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            }
            rebuildFrameSize(game, screenMode, frameWidth, frameHeight);
            game.gameframe.updateGameArea();
        }
        showChatComponents = screenMode == ScreenMode.FIXED || showChatComponents;
        showTabComponents = screenMode == ScreenMode.FIXED || showTabComponents;
    }

    public int getRunInterfaceId() {
        return runInterfaceId;
    }

    public int[] getFriendsClanRankIds() {
        return friendsClanRankIDs;
    }

    public int getFriendServerStatus() {
        return friendServerStatus;
    }

    public int getAnInt1026() {
        return anInt1026;
    }

    public int getAnInt1048() {
        return anInt1048;
    }

    public int getAnInt1039() {
        return anInt1039;
    }

    public String[] getFriendsList() {
        return friendsList;
    }

    public ComponentDrawing getComponentDrawing() {
        return componentDrawing;
    }

    public int getOpenInterfaceId() {
        return openInterfaceId;
    }

    public GrandExchange getGrandExchange() {
        return grandExchange;
    }

    public void setLoggingIn(boolean loggingIn) {
        this.loggingIn = loggingIn;
    }

    public BufferedConnection getSocketStream() {
        return socketStream;
    }

    public void setLoginMessage1(String loginMessage1) {
        this.loginMessage1 = loginMessage1;
    }

    public void setLoginMessage2(String loginMessage2) {
        this.loginMessage2 = loginMessage2;
    }

    public void setLoginMessage3(String loginMessage3) {
        this.loginMessage3 = loginMessage3;
    }

    public Sprite getChatTab() {
        return chatTab;
    }

    public Sprite getChatTabOn() {
        return chatTabOn;
    }

    public Sprite getChatTabHover() {
        return chatTabHover;
    }

    public Sprite getReportButton() {
        return reportButton;
    }

    public RSStream getChatBuffer() {
        return chatBuffer;
    }

    public void setMenuOffsetX(int menuOffsetX) {
        this.menuOffsetX = menuOffsetX;
    }

    public void setMenuOffsetY(int menuOffsetY) {
        this.menuOffsetY = menuOffsetY;
    }

    public void setMenuWidth(int menuWidth) {
        this.menuWidth = menuWidth;
    }

    public void setMenuHeight(int menuHeight) {
        this.menuHeight = menuHeight;
    }

    public int getMyPrivilege() {
        return myPrivilege;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public int getRegionBaseX() {
        return regionBaseX;
    }

    public int getRegionBaseY() {
        return regionBaseY;
    }

    public void refreshFrameSize() {
        if (frameMode == ScreenMode.RESIZABLE) {
            if (frameWidth != (appletClient() ? getGameComponent().getWidth()
                    : mainFrame.getFrameWidth())) {
                frameWidth = (appletClient() ? getGameComponent().getWidth()
                        : mainFrame.getFrameWidth());
                screenAreaWidth = frameWidth;
                gameframe.updateGameArea();
            }
            if (frameHeight != (appletClient() ? getGameComponent().getHeight()
                    : mainFrame.getFrameHeight())) {
                frameHeight = (appletClient() ? getGameComponent().getHeight()
                        : mainFrame.getFrameHeight());
                screenAreaHeight = frameHeight;
                gameframe.updateGameArea();
            }
        }
    }

    public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
        return super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1
                && super.mouseY <= y2;
    }

    public boolean mouseMapPosition() {
        return !(super.mouseX >= frameWidth - 21 && super.mouseX <= frameWidth
                && super.mouseY >= 0 && super.mouseY <= 21);
    }

    private void drawLoadingMessages(int used, String s, String s1) {
        int width = regularFont.getTextWidth(used == 1 ? s : s1);
        int height = s1 == null ? 25 : 38;
        Raster.drawPixels(height, 1, 1, 0, width + 6);
        Raster.drawPixels(1, 1, 1, 0xFFFFFF, width + 6);
        Raster.drawPixels(height, 1, 1, 0xFFFFFF, 1);
        Raster.drawPixels(1, height, 1, 0xFFFFFF, width + 6);
        Raster.drawPixels(height, 1, width + 6, 0xFFFFFF, 1);
        regularFont.drawCenteredString(s, (width / 2) + 5, 18, 0xFFFFFF, 0);
        if (s1 != null) {
            regularFont.drawCenteredString(s1, (width / 2) + 5, 31, 0xFFFFFF, 0);
        }
    }

    public final String formatStackable(int value, boolean thousand) {
        if (value < 0) {
            return String.valueOf(value);
        }
        if (value >= 0 && value < 10000) {
            if (thousand) {
                if (value > 999) {
                    return (value / 1000) + "K";
                }
            }
            return String.valueOf(value);
        }
        if (value >= 10000 && value < 10000000) {
            return value / 1000 + "K";
        }
        if (value >= 10000000 && value < 999999999) {
            return value / 1000000 + "M";
        }
        if (value >= 999999999) {
            return "*";
        } else {
            return "?";
        }
    }

    private boolean menuHasAddFriend(int j) {
        if (j < 0) {
            return false;
        }
        int k = menuActionId[j];
        if (k >= 2000) {
            k -= 2000;
        }
        return k == 337;
    }

    public void drawChannelButtons(int xOffset, int yOffset) {
        chatButtons.drawSprite(5 + xOffset, 142 + yOffset);
        for (ChannelButton channelButton : ChannelButton.values()) {
            channelButton.draw(this, xOffset, yOffset);
        }
    }

    public boolean chatStateCheck() {
        return messagePromptRaised || inputState != 0
                || clickToContinueString != null || backDialogueId != -1
                || dialogueId != -1;
    }

    /**
     * Initializes the client for startup
     */
    public void initialize() {
        try {
            worldId = 10;
            portOffset = 0;
            setHighMem();
            isMembers = true;
            SignLink.storeid = 32;
            SignLink.startpriv(InetAddress.getLocalHost());
            initClientFrame(frameWidth, frameHeight);
            INSTANCE = this;
        } catch (Exception exception) {
            return;
        }
    }

    public void startRunnable(Runnable runnable, int priority) {
        if (priority > 10) {
            priority = 10;
        }
        if (SignLink.mainapp != null) {
            SignLink.startthread(runnable, priority);
        } else {
            super.startRunnable(runnable, priority);
        }
    }

    public Socket openSocket(String host, int port) throws IOException {
        return new Socket(InetAddress.getByName(host), port);
    }

    private void processMenuClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        int j = super.clickMode3;
        if (spellSelected == 1 && super.saveClickX >= 516
                && super.saveClickY >= 160 && super.saveClickX <= 765
                && super.saveClickY <= 205) {
            j = 0;
        }
        if (menuOpen) {
            if (j != 1) {
                int k = super.mouseX;
                int j1 = super.mouseY;
                if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10
                        || j1 < menuOffsetY - 10
                        || j1 > menuOffsetY + menuHeight + 10) {
                    menuOpen = false;
                }
            }
            if (j == 1) {
                int smallXClick = menuOffsetX;
                int menuClickHeight = menuOffsetY;
                int menuClickWidth = menuWidth + 4;
                int savedX = super.saveClickX;
                int savedY = super.saveClickY;
                int selectedMenuIndex = -1;
                for (int optionIndex = 0; optionIndex < menuActionRow; optionIndex++) {
                    int smallYClick = menuClickHeight + 34 + (menuActionRow - 1 - optionIndex) * 15;
                    if (savedX > smallXClick && savedX < smallXClick + menuClickWidth && savedY > smallYClick - 13 && savedY < smallYClick + 3) {
                        selectedMenuIndex = optionIndex;
                    }
                }
                if (selectedMenuIndex != -1) {
                    processMenuActions(selectedMenuIndex);
                }
                menuOpen = false;
            }
        } else {
            if (j == 1 && menuActionRow > 0) {
                int i1 = menuActionId[menuActionRow - 1];
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53
                        || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493
                        || i1 == 847 || i1 == 447 || i1 == 1125) {
                    int l1 = firstMenuAction[menuActionRow - 1];
                    int j2 = secondMenuAction[menuActionRow - 1];
                    RSComponent class9 = RSComponent.forId(j2);
                    if (class9.deleteOnDrag || class9.replaceItems) {
                        aBoolean1242 = false;
                        dragCycle = 0;
                        focusedComponent = j2;
                        dragFromSlot = l1;
                        activeInterfaceType = 2;
                        pressX = super.saveClickX;
                        pressY = super.saveClickY;
                        if (RSComponent.forId(j2).parentId == openInterfaceId) {
                            activeInterfaceType = 1;
                        }
                        if (RSComponent.forId(j2).parentId == backDialogueId) {
                            activeInterfaceType = 3;
                        }
                        return;
                    }
                }
            }
            if (j == 1
                    && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1))
                    && menuActionRow > 2) {
                j = 2;
            }
            if (j == 1 && menuActionRow > 0) {
                processMenuActions(menuActionRow - 1);
            }
            if (j == 2 && menuActionRow > 0) {
                gameframe.determineMenuSize();
            }
            gameframe.processMainScreenClick();
            gameframe.processTabAreaClick();
            processChatModeClick();
            gameframe.processHoverAndClicks();
        }
    }

    private void saveMidi(boolean flag, byte abyte0[]) {
        SignLink.fadeMidi = flag ? 1 : 0;
        SignLink.saveMidi(abyte0, abyte0.length);
    }

    private void loadRegion() {
        try {
            anInt985 = -1;
            incompleteAnimables.clear();
            projectiles.clear();
            TextureLoader317.clearTextureCache();
            TextureLoader667.clearTextureCache();
            unlinkCaches();
            scene.initToNull();
            System.gc();
            for (int i = 0; i < 4; i++) {
                collisionMaps[i].initialize();
            }
            for (int l = 0; l < 4; l++) {
                for (int k1 = 0; k1 < 104; k1++) {
                    for (int j2 = 0; j2 < 104; j2++) {
                        tileFlags[l][k1][j2] = 0;
                    }
                }
            }

            MapRegion objectManager = new MapRegion(tileFlags, tileHeights);
            int k2 = aByteArrayArray1183.length;
            outgoing.putOpcode(0);
            if (!constructedViewport) {
                for (int i3 = 0; i3 < k2; i3++) {
                    int i4 = (anIntArray1234[i3] >> 8) * 64 - regionBaseX;
                    int k5 = (anIntArray1234[i3] & 0xff) * 64 - regionBaseY;
                    byte abyte0[] = aByteArrayArray1183[i3];
                    if (abyte0 != null) {
                        objectManager.method180(abyte0, k5, i4,
                                (currentRegionX - 6) * 8, (currentRegionY - 6) * 8,
                                collisionMaps);
                    }
                }
                for (int j4 = 0; j4 < k2; j4++) {
                    int l5 = (anIntArray1234[j4] >> 8) * 64 - regionBaseX;
                    int k7 = (anIntArray1234[j4] & 0xff) * 64 - regionBaseY;
                    byte abyte2[] = aByteArrayArray1183[j4];
                    if (abyte2 == null && currentRegionY < 800) {
                        objectManager.method174(k7, 64, 64, l5);
                    }
                }
                anInt1097++;
                if (anInt1097 > 160) {
                    anInt1097 = 0;
                    outgoing.putOpcode(238);
                    outgoing.writeByte(96);
                }
                outgoing.putOpcode(0);
                for (int i6 = 0; i6 < k2; i6++) {
                    byte abyte1[] = aByteArrayArray1247[i6];
                    if (abyte1 != null) {
                        int l8 = (anIntArray1234[i6] >> 8) * 64 - regionBaseX;
                        int k9 = (anIntArray1234[i6] & 0xff) * 64 - regionBaseY;
                        objectManager.parseMapscape(l8, collisionMaps, k9, scene, abyte1);
                    }
                }

            }
            if (constructedViewport) {
                for (int j3 = 0; j3 < 4; j3++) {
                    for (int k4 = 0; k4 < 13; k4++) {
                        for (int j6 = 0; j6 < 13; j6++) {
                            int l7 = anIntArrayArrayArray1129[j3][k4][j6];
                            if (l7 != -1) {
                                int i9 = l7 >> 24 & 3;
                                int l9 = l7 >> 1 & 3;
                                int j10 = l7 >> 14 & 0x3ff;
                                int l10 = l7 >> 3 & 0x7ff;
                                int j11 = (j10 / 8 << 8) + l10 / 8;
                                for (int l11 = 0; l11 < anIntArray1234.length; l11++) {
                                    if (anIntArray1234[l11] != j11
                                            || aByteArrayArray1183[l11] == null) {
                                        continue;
                                    }
                                    objectManager.method179(i9, l9,
                                            collisionMaps, k4 * 8,
                                            (j10 & 7) * 8,
                                            aByteArrayArray1183[l11],
                                            (l10 & 7) * 8, j3, j6 * 8);
                                    break;
                                }

                            }
                        }
                    }
                }
                for (int l4 = 0; l4 < 13; l4++) {
                    for (int k6 = 0; k6 < 13; k6++) {
                        int i8 = anIntArrayArrayArray1129[0][l4][k6];
                        if (i8 == -1) {
                            objectManager.method174(k6 * 8, 8, 8, l4 * 8);
                        }
                    }
                }

                outgoing.putOpcode(0);
                for (int l6 = 0; l6 < 4; l6++) {
                    for (int j8 = 0; j8 < 13; j8++) {
                        for (int j9 = 0; j9 < 13; j9++) {
                            int i10 = anIntArrayArrayArray1129[l6][j8][j9];
                            if (i10 != -1) {
                                int k10 = i10 >> 24 & 3;
                                int i11 = i10 >> 1 & 3;
                                int k11 = i10 >> 14 & 0x3ff;
                                int i12 = i10 >> 3 & 0x7ff;
                                int j12 = (k11 / 8 << 8) + i12 / 8;
                                for (int k12 = 0; k12 < anIntArray1234.length; k12++) {
                                    if (anIntArray1234[k12] != j12
                                            || aByteArrayArray1247[k12] == null) {
                                        continue;
                                    }
                                    objectManager.parseLandscape(collisionMaps,
                                            scene, k10, j8 * 8, (i12 & 7) * 8,
                                            l6, aByteArrayArray1247[k12],
                                            (k11 & 7) * 8, i11, j9 * 8);
                                    break;
                                }

                            }
                        }
                    }
                }
            }
            outgoing.putOpcode(0);
            objectManager.addTile(collisionMaps, scene);
            if (loggedIn) {
                gameframe.getGameScreenImageProducer().initDrawingArea();
            }
            outgoing.putOpcode(0);
            int k3 = MapRegion.highestPlane;
            if (k3 > plane) {
                k3 = plane;
            }
            if (k3 < plane - 1) {
                k3 = plane - 1;
            }
            if (lowMemory) {
                scene.initTiles(MapRegion.highestPlane);
            } else {
                scene.initTiles(0);
            }
            for (int i5 = 0; i5 < 104; i5++) {
                for (int i7 = 0; i7 < 104; i7++) {
                    spawnGroundItem(i5, i7);
                }

            }

            anInt1051++;
            if (anInt1051 > 98) {
                anInt1051 = 0;
                outgoing.putOpcode(150);
            }
            method63();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ObjectDefinition.baseModels.clear();
        OSRSObjectDefinition.baseModels.clear();
        if (super.mainFrame != null) {
            packetSender.send(PacketSender.REGION_LOADED);
        }
        if (lowMemory && SignLink.cache_dat != null) {
            int modelCount = resourceProvider.getModelCount();
            for (int modelId = 0; modelId < modelCount; modelId++) {
                int modelIndex = resourceProvider.getModelIndex(modelId);
                if ((modelIndex & 0x79) == 0) {
                    Model.clearModel(modelId, false);
                    Model.clearModel(modelId, true);
                }
            }

        }
        System.gc();
        TextureLoader317.resetTextures();
        resourceProvider.clearExtras();
        int k = (currentRegionX - 6) / 8 - 1;
        int j1 = (currentRegionX + 6) / 8 + 1;
        int i2 = (currentRegionY - 6) / 8 - 1;
        int l2 = (currentRegionY + 6) / 8 + 1;
        if (inPlayerOwnedHouse) {
            k = 49;
            j1 = 50;
            i2 = 49;
            l2 = 50;
        }
        for (int l3 = k; l3 <= j1; l3++) {
            for (int j5 = i2; j5 <= l2; j5++) {
                if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
                    int j7 = resourceProvider.resolve(0, j5, l3);
                    if (j7 != -1) {
                        resourceProvider.loadExtra(j7, 3);
                    }
                    int k8 = resourceProvider.resolve(1, j5, l3);
                    if (k8 != -1) {
                        resourceProvider.loadExtra(k8, 3);
                    }
                }
            }

        }

    }

    private void unlinkCaches() {
        ObjectDefinition.baseModels.clear();
        ObjectDefinition.models.clear();
        OSRSObjectDefinition.baseModels.clear();
        OSRSObjectDefinition.models.clear();
        NPCDefinition.models.clear();
        ItemDefinition.models.clear();
        ItemDefinition.osrsModels.clear();
        ItemDefinition.sprites.clear();
        Player.models.clear();
        SpotAnimation.models.clear();
        SpotAnimation.osrsModels.clear();
    }

    private void spawnGroundItem(int i, int j) {
        Deque itemDeque = groundItems[plane][i][j];
        if (itemDeque == null) {
            scene.removeItemPileFromTile(plane, i, j);
            return;
        }
        int k = 0xfa0a1f01;
        Object obj = null;
        for (Item item = (Item) itemDeque.reverseGetFirst(); item != null; item = (Item) itemDeque
                .reverseGetNext()) {
            ItemDefinition itemDef = ItemDefinition.forId(item.ID);
            int l = itemDef.value;
            if (itemDef.stackingType) {
                l *= item.anInt1559 + 1;
            }
            if (l > k) {
                k = l;
                obj = item;
            }
        }

        itemDeque.insertTail(((Linkable) (obj)));
        Object obj1 = null;
        Object obj2 = null;
        for (Item class30_sub2_sub4_sub2_1 = (Item) itemDeque.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item) itemDeque
                .reverseGetNext()) {
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID
                    && obj1 == null) {
                obj1 = class30_sub2_sub4_sub2_1;
            }
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID
                    && class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID
                    && obj2 == null) {
                obj2 = class30_sub2_sub4_sub2_1;
            }
        }

        int i1 = i + (j << 7) + 0x60000000;
        scene.addItemPileTile(i, i1, ((Renderable) (obj1)),
                getFloorDrawHeight(plane, j * 128 + 64, i * 128 + 64),
                ((Renderable) (obj2)), ((Renderable) (obj)), plane, j);
    }

    private void showNPCs(boolean flag) {
        for (int j = 0; j < npcCount; j++) {
            NPC npc = npcs[npcIndices[j]];
            int k = 0x20000000 + (npcIndices[j] << 14);
            if (npc == null || !npc.isVisible()
                    || npc.desc.priorityRender != flag) {
                continue;
            }
            int l = npc.x >> 7;
            int i1 = npc.y >> 7;
            if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104) {
                continue;
            }
            if (npc.boundDim == 1 && (npc.x & 0x7f) == 64
                    && (npc.y & 0x7f) == 64) {
                if (anIntArrayArray929[l][i1] == anInt1265) {
                    continue;
                }
                anIntArrayArray929[l][i1] = anInt1265;
            }
            if (!npc.desc.clickable) {
                k += 0x80000000;
            }
            scene.addMutipleTileEntity(plane, npc.anInt1552,
                    getFloorDrawHeight(plane, npc.y, npc.x), k, npc.y,
                    (npc.boundDim - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
        }
    }

    /**
     * Draws a yellow tooltip.
     *
     * @param rsComponent  The parent {@link RSComponent}.
     * @param rsComponent1 The child {@link RSComponent}.
     */
    public void drawYellowTooltip(RSComponent rsComponent, RSComponent rsComponent1, int interfaceX, int interfaceY, int childX, int childY) {
        int boxWidth = 0;
        int boxHeight = 0;
        String tooltipMessage = rsComponent1.enabledPopupString;
        if (tooltipMessage == null || !interfaceIsSelected(rsComponent1)) {
            tooltipMessage = rsComponent1.popupString;
        }
        for (String s1 = tooltipMessage; s1.length() > 0; ) {
            if (s1.indexOf("%") != -1) {
                do {
                    int k7 = s1.indexOf("%1");
                    if (k7 == -1) {
                        break;
                    }
                    s1 = s1.substring(0, k7) + interfaceIntToString(executeCS1Script(rsComponent1, 0)) + s1.substring(k7 + 2);
                } while (true);
                do {
                    int l7 = s1.indexOf("%2");
                    if (l7 == -1) {
                        break;
                    }
                    s1 = s1.substring(0, l7) + interfaceIntToString(executeCS1Script(rsComponent1, 1)) + s1.substring(l7 + 2);
                } while (true);
                do {
                    int i8 = s1.indexOf("%3");
                    if (i8 == -1) {
                        break;
                    }
                    s1 = s1.substring(0, i8) + interfaceIntToString(executeCS1Script(rsComponent1, 2)) + s1.substring(i8 + 2);
                } while (true);
                do {
                    int j8 = s1.indexOf("%4");
                    if (j8 == -1) {
                        break;
                    }
                    s1 = s1.substring(0, j8) + interfaceIntToString(executeCS1Script(rsComponent1, 3)) + s1.substring(j8 + 2);
                } while (true);
                do {
                    int k8 = s1.indexOf("%5");
                    if (k8 == -1) {
                        break;
                    }
                    s1 = s1.substring(0, k8) + interfaceIntToString(executeCS1Script(rsComponent1, 4)) + s1.substring(k8 + 2);
                } while (true);
            }
            int l7 = s1.indexOf("\\n");
            String s4;
            if (l7 != -1) {
                s4 = s1.substring(0, l7);
                s1 = s1.substring(l7 + 2);
            } else {
                s4 = s1;
                s1 = "";
            }
            int j10 = regularFont.getTextWidth(s4);
            if (j10 > boxWidth) {
                boxWidth = j10;
            }
            boxHeight += regularFont.baseCharacterHeight + 1;
        }
        boxWidth += 6;
        boxHeight += 7;
        int xPos = (childX + rsComponent1.width) - 5 - boxWidth;
        int yPos = childY + rsComponent1.height + 5;
//        if (xPos < childX + 5) {
//            xPos = childX + 5;
//        }
        if (isFixed()) {
            if (xPos < childX + 5) {
                xPos = childX + 5;
            }
            if (xPos + boxWidth > interfaceX + rsComponent.width) {
                xPos = (interfaceX + rsComponent.width) - boxWidth;
            }
            if (yPos + boxHeight > interfaceY + rsComponent.height) {
                yPos = (childY - boxHeight);
            }
            if (xPos < 0) {
                xPos = 0;
            }
        } else {
            if (xPos < childX + 5) {
                xPos = childX + 5;
            }
            if (xPos > 1560 && xPos < 1600) {
                xPos -= 40;
            } else if (xPos >= 1600) {
                xPos -= 140;
            }
        }
        Raster.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
        Raster.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
        String s2 = tooltipMessage;
        for (int j11 = yPos + regularFont.baseCharacterHeight + 2; s2.length() > 0; j11 += regularFont.baseCharacterHeight + 1) {
            if (s2.indexOf("%") != -1) {
                do {
                    int k7 = s2.indexOf("%1");
                    if (k7 == -1) {
                        break;
                    }
                    s2 = s2.substring(0, k7) + interfaceIntToString(executeCS1Script(rsComponent1, 0)) + s2.substring(k7 + 2);
                } while (true);
                do {
                    int l7 = s2.indexOf("%2");
                    if (l7 == -1) {
                        break;
                    }
                    s2 = s2.substring(0, l7) + interfaceIntToString(executeCS1Script(rsComponent1, 1)) + s2.substring(l7 + 2);
                } while (true);
                do {
                    int i8 = s2.indexOf("%3");
                    if (i8 == -1) {
                        break;
                    }
                    s2 = s2.substring(0, i8) + interfaceIntToString(executeCS1Script(rsComponent1, 2)) + s2.substring(i8 + 2);
                } while (true);
                do {
                    int j8 = s2.indexOf("%4");
                    if (j8 == -1) {
                        break;
                    }
                    s2 = s2.substring(0, j8) + interfaceIntToString(executeCS1Script(rsComponent1, 3)) + s2.substring(j8 + 2);
                } while (true);
                do {
                    int k8 = s2.indexOf("%5");
                    if (k8 == -1) {
                        break;
                    }
                    s2 = s2.substring(0, k8) + interfaceIntToString(executeCS1Script(rsComponent1, 4)) + s2.substring(k8 + 2);
                } while (true);
            }
            int l11 = s2.indexOf("\\n");
            String tooltip;
            if (l11 != -1) {
                tooltip = s2.substring(0, l11);
                s2 = s2.substring(l11 + 2);
            } else {
                tooltip = s2;
                s2 = "";
            }
            if (rsComponent1.centerText) {
                regularFont.drawCenteredString(tooltip, xPos + rsComponent1.width / 2, j11, 0, -1);
            } else {
                if (tooltip.contains("\\r")) {
                    String text = tooltip.substring(0, tooltip.indexOf("\\r"));
                    String text2 = tooltip.substring(tooltip.indexOf("\\r") + 2);
                    regularFont.drawBasicString(text, xPos + 3, j11, 0, -1);
                    int rightX = boxWidth + xPos - regularFont.getTextWidth(text2) - 2;
                    regularFont.drawBasicString(text2, rightX, j11, 0, -1);
                } else {
                    regularFont.drawBasicString(tooltip, xPos + 3, j11, 0, -1);
                }
            }
        }
    }

    /**
     * Draws a yellow tooltip.
     *
     * @param tooltipMessage
     * @param drawX
     * @param drawY
     */
    public void drawYellowTooltip(String tooltipMessage, int drawX, int drawY) {
        int boxWidth = 0;
        int boxHeight = 0;
        for (String s1 = tooltipMessage; s1.length() > 0; ) {
            int l7 = s1.indexOf("\\n");
            String s4;
            if (l7 != -1) {
                s4 = s1.substring(0, l7);
                s1 = s1.substring(l7 + 2);
            } else {
                s4 = s1;
                s1 = "";
            }
            int j10 = regularFont.getTextWidth(s4);
            if (j10 > boxWidth) {
                boxWidth = j10;
            }
            boxHeight += regularFont.baseCharacterHeight + 1;
        }
        boxWidth += 6;
        boxHeight += 7;
        int xPos = drawX - 5 - boxWidth;
        int yPos = drawY + 5;
        if (xPos < drawX + 5) {
            xPos = drawX + 5;
        }
        if (isFixed()) {
            if (xPos < drawX + 5) {
                xPos = drawX + 5;
            }
        } else {
            if (xPos < drawX + 5) {
                xPos = drawX + 5;
            }
            if (xPos > 1560 && xPos < 1600) {
                xPos -= 40;
            } else if (xPos >= 1600) {
                xPos -= 140;
            }
        }
        Raster.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
        Raster.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
        String s2 = tooltipMessage;
        for (int j11 = yPos + regularFont.baseCharacterHeight + 2; s2.length() > 0; j11 += regularFont.baseCharacterHeight + 1) {
            int l11 = s2.indexOf("\\n");
            String tooltip;
            if (l11 != -1) {
                tooltip = s2.substring(0, l11);
                s2 = s2.substring(l11 + 2);
            } else {
                tooltip = s2;
                s2 = "";
            }
            if (tooltip.contains("\\r")) {
                String text = tooltip.substring(0, tooltip.indexOf("\\r"));
                String text2 = tooltip.substring(tooltip.indexOf("\\r") + 2);
                regularFont.drawBasicString(text, xPos + 3, j11, 0, -1);
                int rightX = boxWidth + xPos - regularFont.getTextWidth(text2) - 2;
                regularFont.drawBasicString(text2, rightX, j11, 0, -1);
            } else {
                regularFont.drawBasicString(tooltip, xPos + 3, j11, 0, -1);
            }
        }
    }

    private void updateNPCs(RSStream stream, int i) {
        anInt839 = 0;
        mobsAwaitingUpdateCount = 0;
        method139(stream);
        updateNPCMovement(i, stream);
        npcUpdateMask(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (npcs[l].anInt1537 != loopCycle) {
                npcs[l].desc = null;
                npcs[l] = null;
            }
        }

        if (stream.currentPosition != i) {
            SignLink.reporterror(loginUsername
                    + " size mismatch in getnpcpos - pos:"
                    + stream.currentPosition + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < npcCount; i1++) {
            if (npcs[npcIndices[i1]] == null) {
                SignLink.reporterror(loginUsername
                        + " null entry in npc list - pos:" + i1 + " size:"
                        + npcCount);
                throw new RuntimeException("eek");
            }
        }

    }

    private void processChatModeClick() {
        int[] x = {5, 62, 119, 176, 233, 290, 347, 404};
        if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.ALL;
            inputTaken = true;
        } else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.GAME;
            inputTaken = true;
        } else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.PUBLIC;
            inputTaken = true;
        } else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.PRIVATE;
            inputTaken = true;
        } else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.CLAN;
            inputTaken = true;
        } else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.TRADE;
            inputTaken = true;
        } else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.ASSIST;
            inputTaken = true;
        } else if (super.mouseX >= x[7] && super.mouseX <= x[7] + 111
                && super.mouseY >= frameHeight - 23
                && super.mouseY <= frameHeight) {
            currentHoveredButton = ChannelButton.REPORT;
            inputTaken = true;
        } else {
            currentHoveredButton = null;
            inputTaken = true;
        }
        if (super.clickMode3 == 1) {
            if (super.saveClickX >= x[0] && super.saveClickX <= x[0] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 0) {
                        currentActiveButton = ChannelButton.ALL;
                        chatTypeView = 0;
                        inputTaken = true;
                        setChannel = 0;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.ALL;
                    chatTypeView = 0;
                    inputTaken = true;
                    setChannel = 0;
                }
            } else if (super.saveClickX >= x[1]
                    && super.saveClickX <= x[1] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 1 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.GAME;
                        chatTypeView = 5;
                        inputTaken = true;
                        setChannel = 1;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.GAME;
                    chatTypeView = 5;
                    inputTaken = true;
                    setChannel = 1;
                }
            } else if (super.saveClickX >= x[2]
                    && super.saveClickX <= x[2] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 2 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.PUBLIC;
                        chatTypeView = 1;
                        inputTaken = true;
                        setChannel = 2;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.PUBLIC;
                    chatTypeView = 1;
                    inputTaken = true;
                    setChannel = 2;
                }
            } else if (super.saveClickX >= x[3]
                    && super.saveClickX <= x[3] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 3 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.PRIVATE;
                        chatTypeView = 2;
                        inputTaken = true;
                        setChannel = 3;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.PRIVATE;
                    chatTypeView = 2;
                    inputTaken = true;
                    setChannel = 3;
                }
            } else if (super.saveClickX >= x[4]
                    && super.saveClickX <= x[4] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 4 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.CLAN;
                        chatTypeView = 11;
                        inputTaken = true;
                        setChannel = 4;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.CLAN;
                    chatTypeView = 11;
                    inputTaken = true;
                    setChannel = 4;
                }
            } else if (super.saveClickX >= x[5]
                    && super.saveClickX <= x[5] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 5 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.TRADE;
                        chatTypeView = 3;
                        inputTaken = true;
                        setChannel = 5;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.TRADE;
                    chatTypeView = 3;
                    inputTaken = true;
                    setChannel = 5;
                }
            } else if (super.saveClickX >= x[6]
                    && super.saveClickX <= x[6] + 56
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (!isScreenMode(ScreenMode.FIXED)) {
                    if (setChannel != 6 && !isScreenMode(ScreenMode.FIXED)) {
                        currentActiveButton = ChannelButton.ASSIST;
                        chatTypeView = 6;
                        inputTaken = true;
                        setChannel = 6;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    currentActiveButton = ChannelButton.ASSIST;
                    chatTypeView = 6;
                    inputTaken = true;
                    setChannel = 6;
                }
            } else if (super.saveClickX >= 404 && super.saveClickX <= 515
                    && super.saveClickY >= frameHeight - 23
                    && super.saveClickY <= frameHeight) {
                if (openInterfaceId == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = "";
                    canMute = false;
                    for (int i = 0;
                         i < RSComponent.getComponentCache().length;
                         i++) {
                        if (RSComponent.forId(i) == null
                                || RSComponent.forId(i).contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = openInterfaceId = RSComponent.forId(i).parentId;
                        break;
                    }
                } else {
                    pushMessage("Please finish what you're doing first.", 0, "");
                }
            }
            if (!showChatComponents) {
                currentActiveButton = null;
            }
        }
    }

    private void adjustVolume(int variableParam) {
        if (variableParam > VariableParameter.parameters.length) {
            return;
        }
        int j = VariableParameter.parameters[variableParam].anInt709;
        if (j == 0) {
            return;
        }
        int k = settings[variableParam];
        if (j == 1) {
            if (k == 1) {
                Rasterizer.calculatePalette(0.90000000000000002D);
            }
            if (k == 2) {
                Rasterizer.calculatePalette(0.80000000000000004D);
            }
            if (k == 3) {
                Rasterizer.calculatePalette(0.69999999999999996D);
            }
            if (k == 4) {
                Rasterizer.calculatePalette(0.59999999999999998D);
            }
            ItemDefinition.sprites.clear();
            welcomeScreenRaised = true;
        }

        if (j == 3) {
            boolean flag1 = Constants.enableMusic;
            if (k == 0) {
                if (SignLink.music != null) {
                    adjustVolume(Constants.enableMusic, 500);
                }
                Constants.enableMusic = true;
            }
            if (k == 1) {
                if (SignLink.music != null) {
                    adjustVolume(Constants.enableMusic, 300);
                }
                Constants.enableMusic = true;
            }
            if (k == 2) {
                if (SignLink.music != null) {
                    adjustVolume(Constants.enableMusic, 100);
                }
                Constants.enableMusic = true;
            }
            if (k == 3) {
                if (SignLink.music != null) {
                    adjustVolume(Constants.enableMusic, 0);
                }
                Constants.enableMusic = true;
            }
            if (k == 4) {
                Constants.enableMusic = false;
            }
            if (Constants.enableMusic != flag1 && !lowMemory) {
                if (Constants.enableMusic) {
                    nextSong = currentSong;
                    fadeMusic = true;
                    resourceProvider.provide(2, nextSong);
                } else {
                    stopMidi();
                }
                prevSong = 0;
            }
        }

        if (j == 4) {
            SoundPlayer.setVolume(k);
            if (k == 0) {
                aBoolean848 = true;
                setWaveVolume(0);
            }
            if (k == 1) {
                aBoolean848 = true;
                setWaveVolume(-400);
            }
            if (k == 2) {
                aBoolean848 = true;
                setWaveVolume(-800);
            }
            if (k == 3) {
                aBoolean848 = true;
                setWaveVolume(-1200);
            }
            if (k == 4) {
                aBoolean848 = false;
            }
        }

        if (j == 5) {
            anInt1253 = k;
        }
        if (j == 6) {
            anInt1249 = k;
        }
        if (j == 8) {
            splitPrivateChat = k;
            inputTaken = true;
        }
        if (j == 9) {
            anInt913 = k;
        }
    }

    public void updateEntities() {
        try {
            int messageLength = 0;
            for (int j = -1; j < playerCount + npcCount; j++) {
                Object obj;
                if (j == -1) {
                    obj = localPlayer;
                } else if (j < playerCount) {
                    obj = players[playerIndices[j]];
                } else {
                    obj = npcs[npcIndices[j - playerCount]];
                }
                if (obj == null || !((Entity) (obj)).isVisible()) {
                    continue;
                }
                if (obj instanceof NPC) {
                    NPCDefinition entityDef = ((NPC) obj).desc;
                    if (entityDef.childrenIDs != null) {
                        entityDef = entityDef.morph();
                    }
                    if (entityDef == null) {
                        continue;
                    }
                }
                if (j < playerCount) {
                    int l = 30;
                    Player player = (Player) obj;
                    if (player.headIcon >= 0) {
                        npcScreenPos(((Entity) (obj)),
                                ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            if (player.skullIcon < 2) {
                                skullIcons[player.skullIcon].drawSprite(
                                        spriteDrawX - 12, spriteDrawY - l);
                                l += 25;
                            }
                            if (player.headIcon < 18) {
                                headIcons[player.headIcon].drawSprite(
                                        spriteDrawX - 12, spriteDrawY - l);
                                l += 18;
                            }
                        }
                    }
                    if (j >= 0 && hintIconDrawType == 10
                            && hintIconPlayerId == playerIndices[j]) {
                        npcScreenPos(((Entity) (obj)),
                                ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIconsHint[player.hintIcon].drawSprite(
                                    spriteDrawX - 12, spriteDrawY - l);
                        }
                    }
                } else {
                    NPCDefinition entityDef_1 = ((NPC) obj).desc;
                    if (entityDef_1.headIcon >= 0
                            && entityDef_1.headIcon < headIcons.length) {
                        npcScreenPos(((Entity) (obj)),
                                ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIcons[entityDef_1.headIcon].drawSprite(
                                    spriteDrawX - 12, spriteDrawY - 30);
                        }
                    }
                    if (hintIconDrawType == 1
                            && hintIconNpcId == npcIndices[j - playerCount]
                            && loopCycle % 20 < 10) {
                        npcScreenPos(((Entity) (obj)),
                                ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
                        }
                    }
                }
                if (((Entity) (obj)).spokenText != null && (j >= playerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) obj).name))) {
                    npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
                    if (spriteDrawX > -1 && messageLength < anInt975) {
                        anIntArray979[messageLength] = boldFont.getWidth(((Entity) (obj)).spokenText) / 2;
                        anIntArray978[messageLength] = boldFont.baseCharacterHeight;
                        anIntArray976[messageLength] = spriteDrawX;
                        anIntArray977[messageLength] = spriteDrawY;
                        textColourEffect[messageLength] = ((Entity) (obj)).textColour;
                        anIntArray981[messageLength] = ((Entity) (obj)).textEffect;
                        anIntArray982[messageLength] = ((Entity) (obj)).textCycle;
                        aStringArray983[messageLength++] = ((Entity) (obj)).spokenText;
                        if (anInt1249 == 0 && ((Entity) (obj)).textEffect >= 1
                                && ((Entity) (obj)).textEffect <= 3) {
                            anIntArray978[messageLength] += 10;
                            anIntArray977[messageLength] += 5;
                        }
                        if (anInt1249 == 0 && ((Entity) (obj)).textEffect == 4) {
                            anIntArray979[messageLength] = 60;
                        }
                        if (anInt1249 == 0 && ((Entity) (obj)).textEffect == 5) {
                            anIntArray978[messageLength] += 5;
                        }
                    }
                }
                if (((Entity) (obj)).loopCycleStatus > loopCycle) {
                    try {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (!getInterfaceConfiguration(InterfaceConfiguration.HEALTH_BARS)) {
                            if (spriteDrawX > -1) {
                                int i1 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;
                                if (i1 > 30) {
                                    i1 = 30;
                                }
                                Raster.drawPixels(5, spriteDrawY - 3, spriteDrawX - 15, 65280, i1);
                                Raster.drawPixels(5, spriteDrawY - 3, (spriteDrawX - 15) + i1, 0xFF0000, 30 - i1);
                            }
                        } else {
                            if (spriteDrawX > -1) {
                                int hitpointPercent;
                                if (((Entity) (obj)).currentHealth > 0) {
                                    int i1 = (((Entity) (obj)).currentHealth * 30)
                                            / ((Entity) (obj)).maxHealth;
                                    if (i1 > 30) {
                                        i1 = 30;
                                    }
                                    int hpPercent = (((Entity) (obj)).currentHealth * 90)
                                            / ((Entity) (obj)).maxHealth;
                                    if (hpPercent > 90) {
                                        hpPercent = 90;
                                    }
                                    hitpointPercent = (((Entity) (obj)).currentHealth * 56)
                                            / ((Entity) (obj)).maxHealth;
                                    if (hitpointPercent > 56) {
                                        hitpointPercent = 56;
                                    }
                                } else {
                                    hitpointPercent = 0;
                                }
                                Sprite hpFull = this.hpFull;
                                this.hpEmpty.drawSprite(spriteDrawX - 28, spriteDrawY - 5);
                                Sprite s = Sprite.getCut(hpFull, hitpointPercent, hpFull.myHeight);
                                s.drawSprite(spriteDrawX - 28, spriteDrawY - 5);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                drawHitMaskUpdates((Entity) obj);
            }
            for (int defaultText = 0; defaultText < messageLength; defaultText++) {
                int k1 = anIntArray976[defaultText];
                int l1 = anIntArray977[defaultText];
                int j2 = anIntArray979[defaultText];
                int k2 = anIntArray978[defaultText];
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (int l2 = 0; l2 < defaultText; l2++) {
                        if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2 && k1 - j2 < anIntArray976[l2] + anIntArray979[l2] && k1 + j2 > anIntArray976[l2] - anIntArray979[l2] && anIntArray977[l2] - anIntArray978[l2] < l1) {
                            l1 = anIntArray977[l2] - anIntArray978[l2];
                            flag = true;
                        }
                    }

                }
                spriteDrawX = anIntArray976[defaultText];
                spriteDrawY = anIntArray977[defaultText] = l1;
                String lastInputText = aStringArray983[defaultText];
                if (anInt1249 == 0) {
                    int i3 = 0xFFFF00;
                    if (textColourEffect[defaultText] < 6) {
                        i3 = anIntArray965[textColourEffect[defaultText]];
                    }
                    if (textColourEffect[defaultText] == 6) {
                        i3 = anInt1265 % 20 >= 10 ? 0xFFFF00 : 0xFF0000;
                    }
                    if (textColourEffect[defaultText] == 7) {
                        i3 = anInt1265 % 20 >= 10 ? 65535 : 255;
                    }
                    if (textColourEffect[defaultText] == 8) {
                        i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
                    }
                    if (textColourEffect[defaultText] == 9) {
                        int j3 = 150 - anIntArray982[defaultText];
                        if (j3 < 50) {
                            i3 = 0xFF0000 + 1280 * j3;
                        } else if (j3 < 100) {
                            i3 = 0xFFFF00 - 0x50000 * (j3 - 50);
                        } else if (j3 < 150) {
                            i3 = 65280 + 5 * (j3 - 100);
                        }
                    }
                    if (textColourEffect[defaultText] == 10) {
                        int k3 = 150 - anIntArray982[defaultText];
                        if (k3 < 50) {
                            i3 = 0xFF0000 + 5 * k3;
                        } else if (k3 < 100) {
                            i3 = 0xFF00FF - 0x50000 * (k3 - 50);
                        } else if (k3 < 150) {
                            i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                        }
                    }
                    if (textColourEffect[defaultText] == 11) {
                        int l3 = 150 - anIntArray982[defaultText];
                        if (l3 < 50) {
                            i3 = 0xFFFFFF - 0x50005 * l3;
                        } else if (l3 < 100) {
                            i3 = 65280 + 0x50005 * (l3 - 50);
                        } else if (l3 < 150) {
                            i3 = 0xFFFFFF - 0x50000 * (l3 - 100);
                        }
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_NORMAL) {
                        boldFont.drawCenteredString(lastInputText, spriteDrawX, spriteDrawY, i3, 0, false);
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_WAVE) {
                        boldFont.drawCenteredStringMoveY(lastInputText, spriteDrawX, spriteDrawY, i3, 0, anInt1265, false);
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_WAVE_2) {
                        boldFont.drawCenteredStringMoveXY(lastInputText, spriteDrawX, spriteDrawY, i3, 0, anInt1265, false);
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_SHAKE) {
                        boldFont.drawStringMoveY(lastInputText, spriteDrawX, spriteDrawY, i3, 0, anInt1265, 150 - anIntArray982[defaultText], false);
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_SCROLL) {
                        int i4 = boldFont.getWidth(lastInputText);
                        int k4 = ((150 - anIntArray982[defaultText]) * (i4 + 100)) / 150;
                        Raster.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
                        boldFont.drawBasicString(lastInputText, (spriteDrawX + 50) - k4, spriteDrawY + 1, 0, -1, false);
                        boldFont.drawBasicString(lastInputText, (spriteDrawX + 50) - k4, spriteDrawY, i3, -1, false);
                        Raster.defaultDrawingAreaSize();
                    }
                    if (anIntArray981[defaultText] == TextConstants.EFFECT_SLIDE) {
                        int j4 = 150 - anIntArray982[defaultText];
                        int l4 = 0;
                        if (j4 < 25) {
                            l4 = j4 - 25;
                        } else if (j4 > 125) {
                            l4 = j4 - 125;
                        }
                        Raster.setDrawingArea(spriteDrawY + 5, 0, 512, spriteDrawY - boldFont.baseCharacterHeight - 1);
                        boldFont.drawCenteredString(lastInputText, spriteDrawX, spriteDrawY + l4, i3, 0, false);
                        Raster.defaultDrawingAreaSize();
                    }
                } else {
                    boldFont.drawCenteredString(lastInputText, spriteDrawX, spriteDrawY, 0xFFFF00, 0, false);
                }
            }
        } catch (Exception e) {
        }
    }

    public void drawHitMaskUpdates(Entity entity) {
        for (int mask = 0; mask < 4; mask++) {
            if (entity.hitsLoopCycle[mask] > loopCycle) {
                npcScreenPos(entity, entity.height / 2);
                int hitDamage1 = entity.hitDamages[mask];
                int hitIcon = entity.hitIcons[mask];
                // Draw icon
                if (getInterfaceConfiguration(InterfaceConfiguration.HIT_ICONS)) {
                    if (hitIcon != -1 && hitIcon != 255 && hitIcon < hitTypeIcon.length && hitDamage1 > 0 && entity.hitmarkTrans[mask] < 25) {
                        int hitDrawX = 0;
                        entity.hitmarkTrans[mask] += 1;
                        switch (String.valueOf(hitDamage1).length()) {
                            case 1:
                                hitDrawX = 8;
                                break;
                            case 2:
                                hitDrawX = 4;
                                break;
                            case 3:
                                hitDrawX = 1;
                                break;
                        }
                        int drawPos = 0;
                        if (mask == 0) {
                            entity.hitMarkPos[0] = spriteDrawY + entity.hitmarkMove[mask];
                            drawPos = entity.hitMarkPos[0];
                        }
                        if (mask != 0) {
                            entity.hitMarkPos[mask] = entity.hitMarkPos[0] + (19 * mask);
                            drawPos = entity.hitMarkPos[mask];
                        }
                        hitTypeIcon[hitIcon].drawSprite(spriteDrawX - 38 + hitDrawX, drawPos - 18 - entity.hitmarkTrans[mask]);
                    }
                }
                if (spriteDrawX > -1) {
                    if (mask == 1) {
                        spriteDrawY -= 20;
                    }
                    if (mask == 2) {
                        spriteDrawX -= 15;
                        spriteDrawY -= 10;
                    }
                    if (mask == 3) {
                        spriteDrawX += 15;
                        spriteDrawY -= 10;
                    }
                }
                if (getInterfaceConfiguration(InterfaceConfiguration.HITSPLATS)) {
                    if (spriteDrawX > -1) {
                        int hitDamage = entity.hitDamages[mask];
                        if (hitDamage > 0) {
                            Sprite hitsplat = this.hitDamage;
                            if (entity.hitMarkTypes[mask] == 2) {
                                hitsplat = this.hitPoison;
                            }
                            if (entity.hitMarkTypes[mask] == 3) {
                                hitsplat = this.hitDisease;
                            }
                            hitsplat.drawSprite(spriteDrawX - 11, spriteDrawY - 12);
                        } else {
                            this.hitDefend.drawSprite(spriteDrawX - 12, spriteDrawY - 13);
                        }
                        smallFont.drawCenteredString(String.valueOf(entity.hitDamages[mask]), spriteDrawX - 1, spriteDrawY + 3, 0xFFFFFF, 0);
                    }
                } else {
                    if (spriteDrawX > -1) {
                        hitMarks[entity.hitMarkTypes[mask]].drawSprite(spriteDrawX - 12, spriteDrawY - 12);
                        smallFont.drawCenteredString(String.valueOf(entity.hitDamages[mask]), spriteDrawX, spriteDrawY + 4, 0, 0);
                        smallFont.drawCenteredString(String.valueOf(entity.hitDamages[mask]), spriteDrawX - 1, spriteDrawY + 3, 0xFFFFFF, 0);
                    }
                }
            }
        }
    }

    private void resetSpokenText() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1) {
                j = internalLocalPlayerIndex;
            } else {
                j = playerIndices[i];
            }
            Player player = players[j];
            if (player != null && player.textCycle > 0) {
                player.textCycle--;
                if (player.textCycle == 0) {
                    player.spokenText = null;
                }
            }
        }
        for (int k = 0; k < npcCount; k++) {
            int l = npcIndices[k];
            NPC npc = npcs[l];
            if (npc != null && npc.textCycle > 0) {
                npc.textCycle--;
                if (npc.textCycle == 0) {
                    npc.spokenText = null;
                }
            }
        }
    }

    private void calculateCameraPosition() {
        int i = x * 128 + 64;
        int j = y * 128 + 64;
        int k = getFloorDrawHeight(plane, j, i) - height;
        if (xCameraPos < i) {
            xCameraPos += speed + ((i - xCameraPos) * angle) / 1000;
            if (xCameraPos > i) {
                xCameraPos = i;
            }
        }
        if (xCameraPos > i) {
            xCameraPos -= speed + ((xCameraPos - i) * angle) / 1000;
            if (xCameraPos < i) {
                xCameraPos = i;
            }
        }
        if (zCameraPos < k) {
            zCameraPos += speed + ((k - zCameraPos) * angle) / 1000;
            if (zCameraPos > k) {
                zCameraPos = k;
            }
        }
        if (zCameraPos > k) {
            zCameraPos -= speed + ((zCameraPos - k) * angle) / 1000;
            if (zCameraPos < k) {
                zCameraPos = k;
            }
        }
        if (yCameraPos < j) {
            yCameraPos += speed + ((j - yCameraPos) * angle) / 1000;
            if (yCameraPos > j) {
                yCameraPos = j;
            }
        }
        if (yCameraPos > j) {
            yCameraPos -= speed + ((yCameraPos - j) * angle) / 1000;
            if (yCameraPos < j) {
                yCameraPos = j;
            }
        }
        i = anInt995 * 128 + 64;
        j = anInt996 * 128 + 64;
        k = getFloorDrawHeight(plane, j, i) - anInt997;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if (l1 < 128) {
            l1 = 128;
        }
        if (l1 > 383) {
            l1 = 383;
        }
        if (yCameraCurve < l1) {
            yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
            if (yCameraCurve > l1) {
                yCameraCurve = l1;
            }
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
            if (yCameraCurve < l1) {
                yCameraCurve = l1;
            }
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 1024) {
            j2 -= 2048;
        }
        if (j2 < -1024) {
            j2 += 2048;
        }
        if (j2 > 0) {
            xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        if (j2 < 0) {
            xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 1024) {
            k2 -= 2048;
        }
        if (k2 < -1024) {
            k2 += 2048;
        }
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0) {
            xCameraCurve = i2;
        }
    }

    public void drawMenu(int x, int y) {
        int xPos = menuOffsetX - (x - 4);
        int yPos = (-y + 4) + menuOffsetY;
        int w = menuWidth;
        int h = menuHeight + 1;
        inputTaken = true;
        tabAreaAltered = true;
        if (!getInterfaceConfiguration(InterfaceConfiguration.CONTEXT_MENU)) {
            int menuColor = 0x5d5447;
            Raster.drawPixels(h, yPos, xPos, menuColor, w);
            Raster.drawPixels(16, yPos + 1, xPos + 1, 0, w - 2);
            Raster.fillPixels(xPos + 1, w - 2, h - 19, 0, yPos + 18);
            boldFont.drawBasicString("Choose Option", xPos + 3, yPos + 14, menuColor, -1);
        } else {
            Raster.drawPixels(h - 4, yPos + 2, xPos, 0x706a5e, w);
            Raster.drawPixels(h - 2, yPos + 1, xPos + 1, 0x706a5e, w - 2);
            Raster.drawPixels(h, yPos, xPos + 2, 0x706a5e, w - 4);
            Raster.drawPixels(h - 2, yPos + 1, xPos + 3, 0x2d2822, w - 6);
            Raster.drawPixels(h - 4, yPos + 2, xPos + 2, 0x2d2822, w - 4);
            Raster.drawPixels(h - 6, yPos + 3, xPos + 1, 0x2d2822, w - 2);
            Raster.drawPixels(h - 22, yPos + 19, xPos + 2, 0x524a3d, w - 4);
            Raster.drawPixels(h - 22, yPos + 20, xPos + 3, 0x524a3d, w - 6);
            Raster.drawPixels(h - 23, yPos + 20, xPos + 3, 0x2b271c, w - 6);
            Raster.fillPixels(xPos + 3, w - 6, 1, 0x2a291b, yPos + 2);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x2a261b, yPos + 3);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x252116, yPos + 4);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x211e15, yPos + 5);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x1e1b12, yPos + 6);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x1a170e, yPos + 7);
            Raster.fillPixels(xPos + 2, w - 4, 2, 0x15120b, yPos + 8);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x100d08, yPos + 10);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x090a04, yPos + 11);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x080703, yPos + 12);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x090a04, yPos + 13);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x070802, yPos + 14);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x090a04, yPos + 15);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x070802, yPos + 16);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x090a04, yPos + 17);
            Raster.fillPixels(xPos + 2, w - 4, 1, 0x2a291b, yPos + 18);
            Raster.fillPixels(xPos + 3, w - 6, 1, 0x564943, yPos + 19);
            boldFont.drawBasicString("Choose Option", xPos + 3, yPos + 14, 0xC6B895, -1);
        }
        int mouseX = super.mouseX - (x);
        int mouseY = (-y) + super.mouseY;
        for (int menuIndex = 0; menuIndex < menuActionRow; menuIndex++) {
            int textY = yPos + 31 + (menuActionRow - 1 - menuIndex) * 15;
            int textColor = !getInterfaceConfiguration(InterfaceConfiguration.CONTEXT_MENU) ? 0xFFFFFF : 0xC6B895;
            if (mouseX > xPos && mouseX < xPos + w && mouseY > textY - 13 && mouseY < textY + 3) {
                textColor = !getInterfaceConfiguration(InterfaceConfiguration.CONTEXT_MENU) ? 0xFFFF00 : 0xC6B895;
                // draw hover
                if (getInterfaceConfiguration(InterfaceConfiguration.CONTEXT_MENU)) {
                    if (menuActionName[menuIndex].equalsIgnoreCase("Cancel")) {
                        Raster.drawPixels(12, textY - 9, xPos + 3, 0x6F695D, menuWidth - 6);
                    } else {
                        Raster.drawPixels(16, textY - 10, xPos + 3, 0x6F695D, menuWidth - 6);
                    }
                }
                setCursor(GameCursor.forText(menuActionName[menuIndex]));
            }
            String s = menuActionName[menuIndex] == null ? "" : menuActionName[menuIndex];
            boldFont.drawBasicString(StringUtility.cleanString(s), xPos + 3, textY +
                    (!getInterfaceConfiguration(InterfaceConfiguration.CONTEXT_MENU) ? 0 : 2), textColor, 0);
        }
    }

    public void setCursor(GameCursor gameCursor) {
        if (!getInterfaceConfiguration(InterfaceConfiguration.CURSORS) &&
                mainFrame.getCursor() != new Cursor(Cursor.DEFAULT_CURSOR) || loading) {
            mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.gameCursor = null;
            return;
        }
        if (this.gameCursor == gameCursor) {
            return;
        }
        if (gameCursor == null) {
            gameCursor = GameCursor.NONE;
        }
        this.gameCursor = gameCursor;
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(ImageLoader.forName("cursor_" + gameCursor.getId()).spriteData);
            if (image == null) {
                return;
            }
            mainFrame.setCursor(mainFrame.getToolkit().createCustomCursor(image, new Point(0, 0), null));
        } catch (Exception e) {
            this.gameCursor = null;
            mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            e.printStackTrace();
        }
    }

    private void addFriend(long l) {
        try {
            if (l == 0L) {
                return;
            }
            if (friendsCount >= 100 && member != 1) {
                pushMessage(
                        "Your friendlist is full. Max of 100 for free users, and 200 for members",
                        0, "");
                return;
            }
            if (friendsCount >= 200) {
                pushMessage(
                        "Your friendlist is full. Max of 100 for free users, and 200 for members",
                        0, "");
                return;
            }
            String s = StringUtility.formatUsername(StringUtility.decodeBase37(l));
            for (int i = 0; i < friendsCount; i++) {
                if (friendsListAsLongs[i] == l) {
                    pushMessage(s + " is already on your friend list", 0, "");
                    return;
                }
            }
            for (int j = 0; j < ignoreCount; j++) {
                if (ignores[j] == l) {
                    pushMessage("Please remove " + s
                            + " from your ignore list first", 0, "");
                    return;
                }
            }

            if (s.equals(localPlayer.name)) {
                return;
            } else {
                friendsList[friendsCount] = s;
                friendsListAsLongs[friendsCount] = l;
                friendsNodeIDs[friendsCount] = 0;
                friendsCount++;
                outgoing.putOpcode(188);
                outgoing.writeLong(l);
                return;
            }
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("15283, " + (byte) 68 + ", " + l + ", "
                    + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private int getFloorDrawHeight(int z, int y, int x) {
        int worldX = x >> 7;
        int worldY = y >> 7;
        if (worldX < 0 || worldY < 0 || worldX > 103 || worldY > 103) {
            return 0;
        }
        int plane = z;
        if (plane < 3 && (tileFlags[1][worldX][worldY] & 2) == 2) {
            plane++;
        }
        int sizeX = x & 0x7f;
        int sizeY = y & 0x7f;
        int i2 = tileHeights[plane][worldX][worldY] * (128 - sizeX)
                + tileHeights[plane][worldX + 1][worldY] * sizeX >> 7;
        int j2 = tileHeights[plane][worldX][worldY + 1] * (128 - sizeX)
                + tileHeights[plane][worldX + 1][worldY + 1] * sizeX >> 7;
        return i2 * (128 - sizeY) + j2 * sizeY >> 7;
    }

    private void resetLogout() {
        try {
            if (socketStream != null) {
                socketStream.close();
            }
        } catch (Exception _ex) {
        }

        clientSetting.save();
        socketStream = null;
        loggedIn = false;
        SUMMONING_OPTIONS = new ArrayList<>();
        settings = new int[2000];
        interfaceConfig[256] = 0;
        super.mainFrame.setTitle(Constants.CLIENT_NAME);
        unlinkCaches();
        scene.initToNull();
        for (int i = 0; i < 4; i++) {
            collisionMaps[i].initialize();
        }
        Arrays.fill(chatMessages, null);
        System.gc();
        stopMidi();
        NOCLIP = false;
        currentSong = -1;
        nextSong = -1;
        prevSong = 0;
        chatIcons = new int[8];
        xpDropped = 0;
        mediaMode = false;
        resetClanChat();
        if (getConsole().isOpen()) {
            getConsole().toggle();
        }
        frameMode(this, ScreenMode.FIXED);
        loginMessage1 = "";
        loginMessage2 = "Enter your username & password.";
        loginMessage3 = "";
        grandExchange.reset();
    }

    private void changeCharacterGender() {
        aBoolean1031 = true;
        for (int j = 0; j < 7; j++) {
            anIntArray1065[j] = -1;
            for (int k = 0; k < IdentityKit.length; k++) {
                if (IdentityKit.kits[k].validStyle
                        || IdentityKit.kits[k].part != j
                        + (maleCharacter ? 0 : 7)) {
                    continue;
                }
                anIntArray1065[j] = k;
                break;
            }
        }
    }

    private void updateNPCMovement(int i, RSStream stream) {
        while (stream.bitPosition + 21 < i * 8) {
            int k = stream.readBits(14);
            if (k == 16383) {
                break;
            }
            if (npcs[k] == null) {
                npcs[k] = new NPC();
            }
            NPC npc = npcs[k];
            npcIndices[npcCount++] = k;
            npc.anInt1537 = loopCycle;
            int l = stream.readBits(5);
            if (l > 15) {
                l -= 32;
            }
            int i1 = stream.readBits(5);
            if (i1 > 15) {
                i1 -= 32;
            }
            int j1 = stream.readBits(1);
            int faceDirection = stream.readBits(3);
            int npcId = stream.readBits(Constants.NPC_BITS);
            npc.desc = NPCDefinition.forId(npcId);
            int k1 = stream.readBits(1);
            if (k1 == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = k;
            }
            npc.boundDim = npc.desc.size;
            npc.degreesToTurn = npc.desc.degreesToTurn;
            npc.walkAnimIndex = npc.desc.walkAnimationId;
            npc.turn180AnimIndex = npc.desc.turn180Animation;
            npc.turn90CWAnimIndex = npc.desc.turn90CWAnimation;
            npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimation;
            npc.standAnimIndex = npc.desc.standAnimation;
            npc.turnDirection = npc.anInt1552 = faceDirection > Constants.FACE_DIRECTIONS.length ? 0 : Constants.FACE_DIRECTIONS[faceDirection];
            npc.setPos(localPlayer.pathX[0] + i1, localPlayer.pathY[0] + l, j1 == 1);
        }
        stream.finishBitAccess();
    }

    public void processGameLoop() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            return;
        }
        loopCycle++;
        if (!loggedIn || worldSelect) {
            processLoginScreenInput();
        } else {
            mainGameProcessor();
        }
        processOnDemandQueue();
    }

    private void showOtherPlayers(boolean flag) {
        if (localPlayer.x >> 7 == destX && localPlayer.y >> 7 == destY) {
            destX = 0;
        }
        int j = playerCount;
        if (flag) {
            j = 1;
        }
        for (int l = 0; l < j; l++) {
            Player player;
            int i1;
            if (flag) {
                player = localPlayer;
                i1 = internalLocalPlayerIndex << 14;
            } else {
                player = players[playerIndices[l]];
                i1 = playerIndices[l] << 14;
            }
            if (player == null || !player.isVisible()) {
                continue;
            }
            player.aBoolean1699 = (lowMemory && playerCount > 50 || playerCount > 200)
                    && !flag
                    && player.movementAnimation == player.standAnimIndex;
            int j1 = player.x >> 7;
            int k1 = player.y >> 7;
            if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
                continue;
            }
            if (player.playerModel != null && loopCycle >= player.anInt1707
                    && loopCycle < player.anInt1708) {
                player.aBoolean1699 = false;
                player.anInt1709 = getFloorDrawHeight(plane, player.y, player.x);
                scene.addSingleTileEntity(plane, player.y, player, player.anInt1552,
                        player.anInt1722, player.x, player.anInt1709,
                        player.anInt1719, player.anInt1721, i1,
                        player.anInt1720);
                continue;
            }
            if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                if (anIntArrayArray929[j1][k1] == anInt1265) {
                    continue;
                }
                anIntArrayArray929[j1][k1] = anInt1265;
            }
            player.anInt1709 = getFloorDrawHeight(plane, player.y, player.x);
            scene.addMutipleTileEntity(plane, player.anInt1552, player.anInt1709, i1,
                    player.y, 60, player.x, player, player.aBoolean1541);
        }
    }

    private boolean promptUserForInput(RSComponent class9) {
        int j = class9.contentType;
        if (friendServerStatus == 2) {
            if (j == 201) {
                inputTaken = true;
                inputState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 1;
                promptMessage = "Enter name of friend to add to list";
            }
            if (j == 202) {
                inputTaken = true;
                inputState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 2;
                promptMessage = "Enter name of friend to delete from list";
            }
        }
        if (j == 205) {
            anInt1011 = 250;
            return true;
        }
        if (j == 501) {
            inputTaken = true;
            inputState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 4;
            promptMessage = "Enter name of player to add to list";
        }
        if (j == 502) {
            inputTaken = true;
            inputState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 5;
            promptMessage = "Enter name of player to delete from list";
        }
        if (j == 550) {
            inputTaken = true;
            inputState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 6;
            promptMessage = "Enter the name of the chat you wish to join";
        }
        if (j >= 300 && j <= 313) {
            int k = (j - 300) / 2;
            int j1 = j & 1;
            int i2 = anIntArray1065[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0) {
                        i2 = IdentityKit.length - 1;
                    }
                    if (j1 == 1 && ++i2 >= IdentityKit.length) {
                        i2 = 0;
                    }
                } while (IdentityKit.kits[i2].validStyle
                        || IdentityKit.kits[i2].part != k
                        + (maleCharacter ? 0 : 7));
                anIntArray1065[k] = i2;
                aBoolean1031 = true;
            }
        }
        if (j >= 314 && j <= 323) {
            int l = (j - 314) / 2;
            int k1 = j & 1;
            int j2 = characterDesignColours[l];
            if (k1 == 0 && --j2 < 0) {
                j2 = anIntArrayArray1003[l].length - 1;
            }
            if (k1 == 1 && ++j2 >= anIntArrayArray1003[l].length) {
                j2 = 0;
            }
            characterDesignColours[l] = j2;
            aBoolean1031 = true;
        }
        if (j == 324 && !maleCharacter) {
            maleCharacter = true;
            changeCharacterGender();
        }
        if (j == 325 && maleCharacter) {
            maleCharacter = false;
            changeCharacterGender();
        }
        if (j == 326) {
            outgoing.putOpcode(101);
            outgoing.writeByte(maleCharacter ? 0 : 1);
            for (int i1 = 0; i1 < 7; i1++) {
                outgoing.putShort(anIntArray1065[i1]);
            }

            for (int l1 = 0; l1 < 5; l1++) {
                outgoing.writeByte(characterDesignColours[l1]);
            }
            return true;
        }
        if (j == 613) {
            canMute = !canMute;
        }
        if (j >= 601 && j <= 612) {
            clearTopInterfaces();
            if (reportAbuseInput.length() > 0) {
                outgoing.putOpcode(218);
                outgoing.writeLong(StringUtility.encodeBase37(reportAbuseInput));
                outgoing.writeByte(j - 601);
                outgoing.writeByte(canMute ? 1 : 0);
            }
        }
        return false;
    }

    private void refreshUpdateMasks(RSStream stream) {
        for (int j = 0; j < mobsAwaitingUpdateCount; j++) {
            int k = mobsAwaitingUpdate[j];
            Player player = players[k];
            int l = stream.getByte();
            if ((l & 0x40) != 0) {
                l += stream.getByte() << 8;
            }
            appendPlayerUpdateMask(l, k, stream, player);
        }
    }

    private void loadingStages() {
        if (lowMemory && loadingStage == 2 && MapRegion.anInt131 != plane) {
            gameframe.getGameScreenImageProducer().initDrawingArea();
            int todo = resourceProvider.remaining();
            drawLoadingMessages(1, "Loading - please wait.", (todo > 0 ? "(" + todo + "%)" : null));
            gameframe.getGameScreenImageProducer().drawGraphics(isScreenMode(ScreenMode.FIXED) ? 4 : 0,
                    super.graphics, isScreenMode(ScreenMode.FIXED) ? 4 : 0);
            loadingStage = 1;
            aLong824 = System.currentTimeMillis();
        }
        if (loadingStage == 1) {
            int todo = resourceProvider.remaining();
            if (todo > highestAmtToLoad) {
                highestAmtToLoad = todo;
            }
            if (gameframe.getGameScreenImageProducer() != null) {
                gameframe.getGameScreenImageProducer().drawGraphics(isScreenMode(ScreenMode.FIXED) ? 4 : 0, super.graphics, isScreenMode(ScreenMode.FIXED) ? 4 : 0);
            }
            int percent = (100 - todo);
            drawLoadingMessages(1, "Loading - please wait.", ((percent <= 100 && percent >= 0) ? "(" + percent + "%)" : "(100%)"));
            int j = method54();
            if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
                SignLink.reporterror(loginUsername + " glcfb " + serverSeed + ","
                        + j + "," + lowMemory + "," + indices[0] + ","
                        + resourceProvider.remaining() + "," + plane + ","
                        + currentRegionX + "," + currentRegionY);
                aLong824 = System.currentTimeMillis();
            }
        }
        if (loadingStage == 2 && plane != anInt985) {
            anInt985 = plane;
            gameframe.renderMapScene(plane);
        }
    }

    private int method54() {
        for (int i = 0; i < aByteArrayArray1183.length; i++) {
            if (aByteArrayArray1183[i] == null && anIntArray1235[i] != -1) {
                return -1;
            }
            if (aByteArrayArray1247[i] == null && anIntArray1236[i] != -1) {
                return -2;
            }
        }
        boolean flag = true;
        for (int j = 0; j < aByteArrayArray1183.length; j++) {
            byte abyte0[] = aByteArrayArray1247[j];
            if (abyte0 != null) {
                int k = (anIntArray1234[j] >> 8) * 64 - regionBaseX;
                int l = (anIntArray1234[j] & 0xff) * 64 - regionBaseY;
                if (constructedViewport) {
                    k = 10;
                    l = 10;
                }
                flag &= MapRegion.method189(k, abyte0, l);
            }
        }
        if (!flag) {
            return -3;
        }
        if (validLocalMap) {
            return -4;
        } else {
            loadingStage = 2;
            MapRegion.anInt131 = plane;
            loadRegion();
            packetSender.send(PacketSender.REGION_CHANGED);
            return 0;
        }
    }

    private void createProjectiles() {
        for (SceneProjectile class30_sub2_sub4_sub4 = (SceneProjectile) projectiles
                .reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (SceneProjectile) projectiles
                .reverseGetNext()) {
            if (class30_sub2_sub4_sub4.anInt1597 != plane
                    || loopCycle > class30_sub2_sub4_sub4.anInt1572) {
                class30_sub2_sub4_sub4.unlink();
            } else if (loopCycle >= class30_sub2_sub4_sub4.anInt1571) {
                if (class30_sub2_sub4_sub4.anInt1590 > 0) {
                    NPC npc = npcs[class30_sub2_sub4_sub4.anInt1590 - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312
                            && npc.y >= 0 && npc.y < 13312) {
                        class30_sub2_sub4_sub4.method455(
                                loopCycle,
                                npc.y,
                                getFloorDrawHeight(class30_sub2_sub4_sub4.anInt1597,
                                        npc.y, npc.x)
                                        - class30_sub2_sub4_sub4.anInt1583,
                                npc.x);
                    }
                }
                if (class30_sub2_sub4_sub4.anInt1590 < 0) {
                    int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
                    Player player;
                    if (j == localPlayerIndex) {
                        player = localPlayer;
                    } else {
                        player = players[j];
                    }
                    if (player != null && player.x >= 0 && player.x < 13312
                            && player.y >= 0 && player.y < 13312) {
                        class30_sub2_sub4_sub4.method455(
                                loopCycle,
                                player.y,
                                getFloorDrawHeight(class30_sub2_sub4_sub4.anInt1597,
                                        player.y, player.x)
                                        - class30_sub2_sub4_sub4.anInt1583,
                                player.x);
                    }
                }
                class30_sub2_sub4_sub4.method456(cycleTimer);
                scene.addMutipleTileEntity(plane, class30_sub2_sub4_sub4.anInt1595,
                        (int) class30_sub2_sub4_sub4.aDouble1587, -1,
                        (int) class30_sub2_sub4_sub4.aDouble1586, 60,
                        (int) class30_sub2_sub4_sub4.aDouble1585,
                        class30_sub2_sub4_sub4, false);
            }
        }

    }

    public AppletContext getAppletContext() {
        if (SignLink.mainapp != null) {
            return SignLink.mainapp.getAppletContext();
        } else {
            return super.getAppletContext();
        }
    }

    private void processOnDemandQueue() {
        do {
            Resource resource;
            do {
                resource = resourceProvider.next();
                if (resource == null) {
                    return;
                }
                if (resource.dataType == 0 || resource.dataType == Constants.NEW_MODEL_INDEX - 1) {
                    if (resource.dataType == 0) {
                        Model.setOSRSModelData(resource.buffer, resource.ID);
                    } else {
                        Model.setModelData(resource.buffer, resource.ID);
                    }
                    if (backDialogueId != -1) {
                        inputTaken = true;
                    }
                }
                if (resource.dataType == 1) {
                    SequenceFrame.loadAnimationData(resource.ID, true, resource.buffer);
                }
                if (resource.dataType == 2 && resource.ID == nextSong && resource.buffer != null) {
                    saveMidi(fadeMusic, resource.buffer);
                }
                if ((resource.dataType == 3 || resource.dataType == 7)&& loadingStage == 1) { // maps
                    for (int i = 0; i < aByteArrayArray1183.length; i++) {
                        if (anIntArray1235[i] == resource.ID) {
                            aByteArrayArray1183[i] = resource.buffer;
                            if (resource.buffer == null) {
                                anIntArray1235[i] = -1;
                            }
                            break;
                        }
                        if (anIntArray1236[i] != resource.ID) {
                            continue;
                        }
                        aByteArrayArray1247[i] = resource.buffer;
                        if (resource.buffer == null) {
                            anIntArray1236[i] = -1;
                        }
                        break;
                    }
                }
               /* if (resource.dataType == Constants.TEXTURE_INDEX - 1) {
                    Texture.load(resource.ID, resource.buffer);
                }*/
                if (resource.dataType == Constants.NEW_ANIMATION_INDEX - 1) {
                    SequenceFrame.loadAnimationData(resource.ID, false, resource.buffer);
                }
            } while (resource.dataType != 93 || !resourceProvider.landscapePresent(resource.ID));
            MapRegion.configureLandscape(new RSStream(resource.buffer), resourceProvider); // parse landscape
        } while (true);
    }

    private void resetAnimation(int componentId) {
        RSComponent rsComponent = RSComponent.getComponentCache()[componentId];
        for (InterfaceChild interfaceChild : rsComponent.getInterfaceChildren()) {
            if (interfaceChild.getId() == -1) {
                break;
            }
            RSComponent childComponent = RSComponent.getComponentCache()[interfaceChild.getId()];
            if (childComponent == null) {
                if (Constants.DEBUG_MODE) {
                    logger.log(Level.WARNING, "Child RSComponent null: {0}.", interfaceChild.getId());
                }
                continue;
            }
            if (childComponent.type == 1) {
                resetAnimation(childComponent.id);
            }
            childComponent.animationFrames = 0;
            childComponent.animationLength = 0;
        }
    }

    private void drawHeadIcon() {
        if (hintIconDrawType != 2) {
            return;
        }
        calcEntityScreenPos((hintIconX - regionBaseX << 7) + anInt937,
                hintIconZ * 2, (hintIconY - regionBaseY << 7) + anInt938);
        if (spriteDrawX > -1 && loopCycle % 20 < 10) {
            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
        }
    }

    private void mainGameProcessor() {
        refreshFrameSize();
        if (systemUpdateTime > 1) {
            systemUpdateTime--;
        }
        if (anInt1011 > 0) {
            anInt1011--;
        }
        for (int index = 0; index < spriteTimers.length; index++) {
            if (spriteTimers[index][0] == 0 || spriteTimers[index][1] == 0) {
                continue;
            }
            if (spriteAlphaValues[index][0] <= 1) {
                spriteAlphaForwards[index] = true;
            }
            if (spriteAlphaValues[index][0] >= 255) {
                spriteAlphaForwards[index] = false;
            }
            if ((Game.loopCycle % spriteTimers[index][0]) < spriteTimers[index][1]) {
                if (spriteAlphaForwards[index]) {
                    spriteAlphaValues[index][0] += spriteAlphaValues[index][1];
                } else {
                    spriteAlphaValues[index][0] -= spriteAlphaValues[index][1];
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            if (!parsePacket()) {
                break;
            }
        }

        if (!loggedIn) {
            return;
        }
        synchronized (mouseDetection.syncObject) {
            if (flagged) {
                if (super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {
                    outgoing.putOpcode(45);
                    outgoing.writeByte(0);
                    int j2 = outgoing.currentPosition;
                    int j3 = 0;
                    for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
                        if (j2 - outgoing.currentPosition >= 240) {
                            break;
                        }
                        j3++;
                        int l4 = mouseDetection.coordsY[j4];
                        if (l4 < 0) {
                            l4 = 0;
                        } else if (l4 > 502) {
                            l4 = 502;
                        }
                        int k5 = mouseDetection.coordsX[j4];
                        if (k5 < 0) {
                            k5 = 0;
                        } else if (k5 > 764) {
                            k5 = 764;
                        }
                        int i6 = l4 * 765 + k5;
                        if (mouseDetection.coordsY[j4] == -1
                                && mouseDetection.coordsX[j4] == -1) {
                            k5 = -1;
                            l4 = -1;
                            i6 = 0x7ffff;
                        }
                        if (k5 == anInt1237 && l4 == anInt1238) {
                            if (duplicateClickCount < 2047) {
                                duplicateClickCount++;
                            }
                        } else {
                            int j6 = k5 - anInt1237;
                            anInt1237 = k5;
                            int k6 = l4 - anInt1238;
                            anInt1238 = l4;
                            if (duplicateClickCount < 8 && j6 >= -32
                                    && j6 <= 31 && k6 >= -32 && k6 <= 31) {
                                j6 += 32;
                                k6 += 32;
                                outgoing.putShort((duplicateClickCount << 12)
                                        + (j6 << 6) + k6);
                                duplicateClickCount = 0;
                            } else if (duplicateClickCount < 8) {
                                outgoing.writeTriByte(0x800000
                                        + (duplicateClickCount << 19) + i6);
                                duplicateClickCount = 0;
                            } else {
                                outgoing.putInt(0xc0000000
                                        + (duplicateClickCount << 19) + i6);
                                duplicateClickCount = 0;
                            }
                        }
                    }

                    outgoing.writeBytes(outgoing.currentPosition - j2);
                    if (j3 >= mouseDetection.coordsIndex) {
                        mouseDetection.coordsIndex = 0;
                    } else {
                        mouseDetection.coordsIndex -= j3;
                        for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
                            mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5
                                    + j3];
                            mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5
                                    + j3];
                        }

                    }
                }
            } else {
                mouseDetection.coordsIndex = 0;
            }
        }
        if (super.clickMode3 != 0) {
            long l = (super.aLong29 - aLong1220) / 50L;
            if (l > 4095L) {
                l = 4095L;
            }
            aLong1220 = super.aLong29;
            int k2 = super.saveClickY;
            if (k2 < 0) {
                k2 = 0;
            } else if (k2 > 502) {
                k2 = 502;
            }
            int k3 = super.saveClickX;
            if (k3 < 0) {
                k3 = 0;
            } else if (k3 > 764) {
                k3 = 764;
            }
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if (super.clickMode3 == 2) {
                j5 = 1;
            }
            int l5 = (int) l;
            outgoing.putOpcode(241);
            outgoing.putInt((l5 << 20) + (j5 << 19) + k4);
        }
        if (anInt1016 > 0) {
            anInt1016--;
        }
        if (super.keyArray[1] == 1 || super.keyArray[2] == 1
                || super.keyArray[3] == 1 || super.keyArray[4] == 1) {
            aBoolean1017 = true;
        }
        if (aBoolean1017 && anInt1016 <= 0) {
            anInt1016 = 20;
            aBoolean1017 = false;
            outgoing.putOpcode(86);
            outgoing.putShort(anInt1184);
            outgoing.putShortA(cameraHorizontal);
        }
        if (super.awtFocus && !screenFocused) {
            screenFocused = true;
            packetSender.send(PacketSender.CLIENT_FOCUS_OPCODE, screenFocused);
        }
        if (!super.awtFocus && screenFocused) {
            screenFocused = false;
            packetSender.send(PacketSender.CLIENT_FOCUS_OPCODE, screenFocused);
        }
        loadingStages();
        method115();
        timeoutCounter++;
        if (timeoutCounter > 750) {
            dropClient();
        }
        updatePlayerInstances();
        forceNPCUpdateBlock();
        processTrackUpdates();
        resetSpokenText();
        cycleTimer++;
        if (crossType != 0) {
            crossIndex += 20;
            if (crossIndex >= 400) {
                crossType = 0;
            }
        }
        if (atInventoryInterfaceType != 0) {
            atInventoryLoopCycle++;
            if (atInventoryLoopCycle >= 15) {
                if (atInventoryInterfaceType == 2) {
                }
                if (atInventoryInterfaceType == 3) {
                    inputTaken = true;
                }
                atInventoryInterfaceType = 0;
            }
        }
        if (activeInterfaceType != 0) {
            dragCycle++;
            if (super.mouseX > pressX + 5 || super.mouseX < pressX - 5
                    || super.mouseY > pressY + 5
                    || super.mouseY < pressY - 5) {
                aBoolean1242 = true;
            }
            if (super.clickMode2 == 0) {
                if (activeInterfaceType == 2) {
                }
                if (activeInterfaceType == 3) {
                    inputTaken = true;
                }
                activeInterfaceType = 0;
                if (aBoolean1242 && dragCycle >= 15) {
                    lastActiveInvInterface = -1;
                    processRightClick();
                    //bankItemDragSprite = null;
                    int x = isFixed() ? 0 : (frameWidth / 2) - 256;
                    int y = isFixed() ? 40 : 40 + (frameHeight / 2) - 167;
                    // Move items to tabs
                    if (Constants.BANK_TABS && focusedComponent == 5382 && super.mouseY >= y && super.mouseY <= y + 40) {
                        int offsetX = 0;
                        for (int index = 0; index < 9; index++) { // TODO index < 10 ?
                            if (super.mouseX >= (62 + offsetX) + x && super.mouseX <= (100 + offsetX) + x) {
                                packetSender.send(PacketSender.SWITCH_ITEM, focusedComponent, 2, dragFromSlot, index);
                                break;
                            }
                            offsetX += 42;
                        }
                    }
                    if (lastActiveInvInterface == focusedComponent && mouseInvInterfaceIndex != dragFromSlot) {
                        RSComponent childInterface = RSComponent.forId(focusedComponent);
                        int switchType = 0;
                        if (anInt913 == 1 && childInterface.contentType == 206) {
                            switchType = 1;
                        }
                        if (childInterface.inventory[mouseInvInterfaceIndex] <= 0) {
                            switchType = 0;
                        }
                        if (childInterface.replaceItems) {
                            int l2 = dragFromSlot;
                            int l3 = mouseInvInterfaceIndex;
                            childInterface.inventory[l3] = childInterface.inventory[l2];
                            childInterface.inventoryValue[l3] = childInterface.inventoryValue[l2];
                            childInterface.inventory[l2] = -1;
                            childInterface.inventoryValue[l2] = 0;
                        } else if (switchType == 1) {
                            int i3 = dragFromSlot;
                            for (int i4 = mouseInvInterfaceIndex; i3 != i4; ) {
                                if (i3 > i4) {
                                    childInterface.swapInventoryItems(i3, i3 - 1);
                                    i3--;
                                } else if (i3 < i4) {
                                    childInterface.swapInventoryItems(i3, i3 + 1);
                                    i3++;
                                }
                            }
                        } else {
                            childInterface.swapInventoryItems(dragFromSlot, mouseInvInterfaceIndex);
                        }
                        packetSender.send(PacketSender.SWITCH_ITEM, focusedComponent, switchType, dragFromSlot, mouseInvInterfaceIndex);
                    }
                } else if ((anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2) {
                    gameframe.determineMenuSize();
                } else if (menuActionRow > 0) {
                    processMenuActions(menuActionRow - 1);
                }
                atInventoryLoopCycle = 10;
                super.clickMode3 = 0;
            }
        }
        if (SceneGraph.clickedTileX != -1) {
            int k = SceneGraph.clickedTileX;
            int k1 = SceneGraph.clickedTileY;
            // AGILITY
            boolean flag = doWalkTo(0, 0, 0, 0, localPlayer.pathY[0], 0, 0, k1,
                    localPlayer.pathX[0], true, k);
            SceneGraph.clickedTileX = -1;
            if (flag) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 1;
                crossIndex = 0;
            }
        }
        if (super.clickMode3 == 1 && clickToContinueString != null) {
            clickToContinueString = null;
            inputTaken = true;
            super.clickMode3 = 0;
        }
        processMenuClick();
        if (super.clickMode2 == 1 || super.clickMode3 == 1) {
            dragPosition++;
        }
        if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
            if (anInt1501 < 0 && !menuOpen) {
                anInt1501++;
                if (anInt1501 == 0) {
                    if (anInt1500 != 0) {
                        inputTaken = true;
                    }
                    if (anInt1044 != 0) {
                    }
                }
            }
        } else if (anInt1501 > 0) {
            anInt1501--;
        }
        if (loadingStage == 2) {
            checkForGameUsages();
        }
        if (loadingStage == 2 && oriented) {
            calculateCameraPosition();
        }
        for (int i1 = 0; i1 < 5; i1++) {
            hotizontalSpeed[i1]++;
        }

        processInput();
        super.idleTime++;
        if (super.idleTime > 25000) {
            anInt1011 = 250;
            super.idleTime -= 500;
            outgoing.putOpcode(202);
        }
        anInt1010++;
        if (anInt1010 > 50) {
            outgoing.putOpcode(0);
        }
        try {
            if (socketStream != null && outgoing.currentPosition > 0) {
                socketStream.queueBytes(outgoing.currentPosition,
                        outgoing.payload);
                outgoing.currentPosition = 0;
                anInt1010 = 0;
            }
        } catch (IOException _ex) {
            dropClient();
        } catch (Exception exception) {
            resetLogout();
        }
    }

    private void method63() {
        SpawnedObject spawnedObject = (SpawnedObject) spawns.reverseGetFirst();
        for (; spawnedObject != null; spawnedObject = (SpawnedObject) spawns
                .reverseGetNext()) {
            if (spawnedObject.getLongetivity == -1) {
                spawnedObject.delay = 0;
                assignOldValuesToNewRequest(spawnedObject);
            } else {
                spawnedObject.unlink();
            }
        }

    }

    private void setupLoginScreen() {
        super.fullGameScreen = null;
        gameframe.resetImageProducers();
        Raster.clear();
        new ImageProducer(360, 132);
        Raster.clear();
        titleScreen = new ImageProducer(frameWidth, frameHeight);
        Raster.clear();
        new ImageProducer(202, 238);
        Raster.clear();
        new ImageProducer(203, 238);
        Raster.clear();
        new ImageProducer(74, 94);
        Raster.clear();
        new ImageProducer(75, 94);
        Raster.clear();
        welcomeScreenRaised = true;
    }

    public void setLoadingText(int percent, String text) {
        Game.loadingPercentage = percent;
        Game.loadingText = text;
    }

    public void drawLoadingText(int percent, String text) {
        if (!loading) {
            return;
        }
        if (super.graphics == null || loadingSprite[0] == null) {
            super.prepareGraphics();
            return;
        }
        setLoadingAndLoginHovers();
        super.graphics.drawImage(loadingSprite[0], 0, 0, null);
        super.graphics.drawImage(loadingSprite[4], 174, 43, null);
        super.graphics.drawImage(loadingSprite[1], 254, 241, null);
        super.graphics.drawImage(loadingSprite[3], 254, 200, null);
        Graphics2D g2 = (Graphics2D) super.graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Arial", Font.PLAIN, 16);
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        super.graphics.setColor(Color.WHITE);
        Rectangle rect = new Rectangle(0, 20, 765, 420);
        FontMetrics metrics = super.graphics.getFontMetrics(font);
        int x = (loadingPercentage == 20) ? 345 : (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        super.graphics.drawString(loadingText, x, y);
        g2.drawString(loadingText, x, y);
        if (loadingPercentage > 0) {
            int percentDraw = (loadingPercentage + 30) * 2;
            Image resized = Sprite.getScaledImage(loadingSprite[2], percentDraw, 28, 256, 20);
            super.graphics.drawImage(resized, 254, 241, null);
        }
        setLoadingAndLoginHovers();
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
        // g.dispose();
    }

    public void setLoadingAndLoginHovers() {
        loginButtonHover = input1Hover = rememberMeButtonHover = input2Hover = false;

        if (!loading) {
            if (super.mouseX >= 275 && super.mouseX <= 366 && super.mouseY >= 317 && super.mouseY <= 347) {
                loginButtonHover = !loggingIn;
            } else if (super.mouseX >= 236 && super.mouseX <= 590) {
                if (super.mouseY >= 219 && super.mouseY <= 237) {
                    input1Hover = !loggingIn;
                } else if (super.mouseY >= 249 && super.mouseY <= 263) {
                    input2Hover = !loggingIn;
                }
            }
            if (super.mouseX >= 231 && super.mouseX <= 318 && super.mouseY >= 286 && super.mouseY <= 297) {
                rememberMeButtonHover = !loggingIn;
            }
        }
        if (gameCursor != null && gameCursor != GameCursor.NONE) {
            setCursor(GameCursor.NONE);
        }
    }

    private boolean clickObject(int i, int j, int k) {
        int i1 = i >> 14 & 0x7fff;
        int j1 = scene.getIDTagForXYZ(plane, k, j, i);
        if (j1 == -1) {
            return false;
        }
        int k1 = j1 & 0x1f;
        int l1 = j1 >> 6 & 3;
        if (k1 == 10 || k1 == 11 || k1 == 22) {
            ObjectDefinition objectDefinition = ObjectDefinition.forId(i1);
            int i2;
            int j2;
            if (l1 == 0 || l1 == 2) {
                i2 = objectDefinition.sizeX;
                j2 = objectDefinition.sizeY;
            } else {
                i2 = objectDefinition.sizeY;
                j2 = objectDefinition.sizeX;
            }
            int k2 = objectDefinition.surroundings;
            if (l1 != 0) {
                k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
            }
            doWalkTo(2, 0, j2, 0, localPlayer.pathY[0], i2, k2, j,
                    localPlayer.pathX[0], false, k);
        } else {
            doWalkTo(2, l1, 0, k1 + 1, localPlayer.pathY[0], 0, 0, j,
                    localPlayer.pathX[0], false, k);
        }
        crossX = super.saveClickX;
        crossY = super.saveClickY;
        crossType = 2;
        crossIndex = 0;
        return true;
    }

    public void playSong(int id) {
        if (id != currentSong && Constants.enableMusic && !lowMemory
                && prevSong == 0) {
            nextSong = id;
            fadeMusic = true;
            resourceProvider.provide(2, nextSong);
            currentSong = id;
        }
    }

    public void stopMidi() {
        if (SignLink.music != null) {
            SignLink.music.stop();
        }
        SignLink.fadeMidi = 0;
        SignLink.midi = "stop";
    }

    private void adjustVolume(boolean updateMidi, int volume) {
        SignLink.setVolume(volume);
        if (updateMidi) {
            SignLink.midi = "voladjust";
        }
    }

    private boolean saveWave(byte data[], int id) {
        return data == null || SignLink.wavesave(data, id);
    }

    private void processTrackUpdates() {
        for (int count = 0; count < trackCount; count++) {
            boolean replay = false;
            try {
                RSStream stream = Track.data(trackLoops[count], tracks[count]);
                new SoundPlayer(new ByteArrayInputStream(
                        stream.payload, 0, stream.currentPosition),
                        soundVolume[count], soundDelay[count]);
                if (System.currentTimeMillis()
                        + (long) (stream.currentPosition / 22) > trackTimer
                        + (long) (currentTrackTime / 22)) {
                    currentTrackTime = stream.currentPosition;
                    trackTimer = System.currentTimeMillis();
                    if (saveWave(stream.payload, stream.currentPosition)) {
                        currentTrackLoop = trackLoops[count];
                    } else {
                        replay = true;
                    }
                }
            } catch (Exception exception) {
            }
            if (!replay || soundDelay[count] == -5) {
                trackCount--;
                for (int index = count; index < trackCount; index++) {
                    tracks[index] = tracks[index + 1];
                    trackLoops[index] = trackLoops[index + 1];
                    soundDelay[index] = soundDelay[index + 1];
                    soundVolume[index] = soundVolume[index + 1];
                }
                count--;
            } else {
                soundDelay[count] = -5;
            }
        }

        if (prevSong > 0) {
            prevSong -= 20;
            if (prevSong < 0) {
                prevSong = 0;
            }
            if (prevSong == 0 && Constants.enableMusic && !lowMemory) {
                nextSong = currentSong;
                fadeMusic = true;
                resourceProvider.provide(2, nextSong);
            }
        }
    }

    private CacheArchive createArchive(int file, String displayedName, String name, int expectedCRC, int x) {
        byte archiveBuffer[] = null;
        int reconnectionDelay = 5;
        try {
            if (indices[0] != null) {
                archiveBuffer = indices[0].decompress(file);
            }
        } catch (Exception _ex) {
        }
        if (archiveBuffer != null && Constants.JAGGRAB_ENABLED) {
            indexCrc.reset();
            indexCrc.update(archiveBuffer);
            int crc = (int) indexCrc.getValue();
            if (crc != expectedCRC) {
                archiveBuffer = null;
            }
        }
        if (archiveBuffer != null) {
            CacheArchive cacheArchive = new CacheArchive(archiveBuffer);
            return cacheArchive;
        }
        int errors = 0;
        while (archiveBuffer == null) {
            String message = "Unknown error";
            drawLoadingText(x, "Requesting " + displayedName);
            try {
                int k1 = 0;
                DataInputStream dataInputStream = requestCacheIndex(name + expectedCRC);
                byte abyte1[] = new byte[6];
                dataInputStream.readFully(abyte1, 0, 6);
                RSStream rsStream = new RSStream(abyte1);
                rsStream.currentPosition = 3;
                int length = rsStream.getTri() + 6;
                int offset = 6;
                archiveBuffer = new byte[length];
                System.arraycopy(abyte1, 0, archiveBuffer, 0, 6);

                while (offset < length) {
                    int finalLength = length - offset;
                    if (finalLength > 2000) {
                        finalLength = 2000;
                    }
                    int j3 = dataInputStream.read(archiveBuffer, offset, finalLength);
                    if (j3 < 0) {
                        message = "Length error: " + offset + "/" + length;
                        throw new IOException("EOF");
                    }
                    offset += j3;
                    int k3 = (offset * 100) / length;
                    if (k3 != k1) {
                        drawLoadingText(x, "Loading " + displayedName + " - "
                                + k3 + "%");
                    }
                    k1 = k3;
                }
                dataInputStream.close();
                try {
                    if (indices[0] != null) {
                        indices[0].put(archiveBuffer.length,
                                archiveBuffer, file);
                    }
                } catch (Exception _ex) {
                    indices[0] = null;
                }
                indexCrc.reset();
                indexCrc.update(archiveBuffer);
                int crc = (int) indexCrc.getValue();
                if (crc != expectedCRC && Constants.JAGGRAB_ENABLED) {
                    archiveBuffer = null;
                    errors++;
                    message = "Checksum error: " + crc;
                }
            } catch (IOException ioexception) {
                if (message.equals("Unknown error")) {
                    message = "Connection error";
                }
                archiveBuffer = null;
            } catch (NullPointerException _ex) {
                message = "Null error";
                archiveBuffer = null;
                if (!SignLink.reporterror) {
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException _ex) {
                message = "Bounds error";
                archiveBuffer = null;
                if (!SignLink.reporterror) {
                    return null;
                }
            } catch (Exception _ex) {
                message = "Unexpected error";
                archiveBuffer = null;
                if (!SignLink.reporterror) {
                    return null;
                }
            }
            if (archiveBuffer == null) {
                for (int l1 = reconnectionDelay; l1 > 0; l1--) {
                    if (errors >= 3) {
                        drawLoadingText(x, "Game updated - please reload page");
                        l1 = 10;
                    } else {
                        drawLoadingText(x, message + " - Retrying in " + l1);
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }

                reconnectionDelay *= 2;
                if (reconnectionDelay > 60) {
                    reconnectionDelay = 60;
                }
                Constants.JAGGRAB_ENABLED = !Constants.JAGGRAB_ENABLED;
            }
        }
        return new CacheArchive(archiveBuffer);
    }

    public void sendLogout(boolean force) {
        if (!force && (openInterfaceId != -1 || backDialogueId != -1 || dialogueId != -1)) {
            return;
        }
        outgoing.putOpcode(185);
        outgoing.putShort(2458);
    }

    public void sendLogout() {
        sendLogout(false);
    }

    private void dropClient() {
        if (anInt1011 > 0) {
            resetLogout();
            return;
        }
        fullscreenInterfaceID = -1;
        Raster.fillPixels(2, 229, 39, 0xFFFFFF, 2);
        Raster.drawPixels(37, 3, 3, 0, 227);
        regularFont.drawCenteredString("Connection lost.", 120, 19, 0xFFFFFF, 0);
        regularFont.drawCenteredString("Please wait - attempting to reestablish.", 117, 34, 0xFFFFFF, 0);
        if (fullscreenInterfaceID == -1) {
            gameframe.getGameScreenImageProducer().drawGraphics(frameMode == ScreenMode.FIXED ? 4 : 0, super.graphics, frameMode == ScreenMode.FIXED ? 4 : 0);
        }
        minimapState = 0;
        destX = 0;
        BufferedConnection rsSocket = socketStream;
        loggedIn = false;
        loginFailures = 0;
        login(loginUsername, loginPassword, true);
        if (!loggedIn) {
            resetLogout();
        }
        try {
            rsSocket.close();
        } catch (Exception _ex) {
        }
    }

    public void setNorth() {
        cameraX = 0;
        cameraY = 0;
        cameraRotation = 0;
        cameraHorizontal = 0;
        minimapRotation = 0;
        minimapZoom = 0;
    }

    public void processMenuActions(int id) {
        if (id < 0) {
            return;
        }
        if (inputState != 3 && inputState != 0) {
            inputState = 0;
            inputTaken = true;
        }
        int first = firstMenuAction[id];
        int secondaryClick = secondMenuAction[id];
        int action = menuActionId[id];
        if (Constants.DEBUG_MODE && action != GameConstants.ACTION_CANCEL) {
            logger.log(Level.INFO, "Action performed: {0}/{1}", new Object[]{action, (action >= 2000 ? (action - 2000) : action)});
        }
        int clicked = selectedMenuActions[id];
        if (action >= 4000 && action <= 4010) {
            summoning.processAction(action, id, first, secondaryClick, clicked);
            return;
        }
        if (action >= 2000) {
            action -= 2000;
        }
        if (action == 1251) {
            grandExchange.buttonClicked = false;
            outgoing.putOpcode(204);
            outgoing.putLEShort(grandExchange.grandExchangeSelection);
            outgoing.writeByte(openInterfaceId == 25626 ? 2 : 1);
            inputState = 0;
        }
        /**
         * Hides button.
         */
        if (action == 1100) {
            RSComponent button = RSComponent.getComponentCache()[secondaryClick];
            button.setMenuVisible(!button.isMenuVisible());
        }
        /**
         * Adds an item?
         */
        if (action == 1200) {
            outgoing.putOpcode(185);
            outgoing.putShort(secondaryClick);
            RSComponent item = RSComponent.getComponentCache()[secondaryClick];
            RSComponent menu = RSComponent.getComponentCache()[item.getMOverInterToTrigger()];
            // menu.setMenuItem(item.getMenuItem());
            menu.setMenuVisible(false);
        }
        /**
         * Bank tab. TODO Remove
         */
        if (action == 1700) {
            outgoing.putOpcode(185);
            outgoing.putShort(secondaryClick);
        }
        if (action == 1750) {
            outgoing.putOpcode(93);
            outgoing.writeByte((byte) secondaryClick);
            outgoing.writeByte(id);
        }
        if (action == 1800) {
            int friendId = -1;
            int index = 0;
            for (int friendIndex = 0; friendIndex < 200; friendIndex++) {
                if ((32349 + index) == secondaryClick) {
                    friendId = friendIndex;
                    break;
                }
                index += 2;
            }
            if (friendId != -1) {
                outgoing.putOpcode(242);
                outgoing.writeByte((byte) first);
                outgoing.writeLong(friendsListAsLongs[friendId]);
            }
        }
        if (action == 851) {
            outgoing.putOpcode(185);
            outgoing.putShort(155);
        }
        if (action == 700) {
            if (tabInterfaceIDs[10] != -1) {
                if (currentTabId == 10) {
                    showTabComponents = !showTabComponents;
                } else {
                    showTabComponents = true;
                }
                currentTabId = 10;
                tabAreaAltered = true;
            }
        }
        if (action == 696) {
            setNorth();
        }
        if (action == 697) {
            if (backDialogueId == -1 && inputState != 3 && openInterfaceId == -1) {
                processCommand("teleports");
            } else {
                pushMessage("Please finish what you're doing first.", 0, "");
            }
        }
        if (action == 1076) {
            if (menuOpen) {
                needDrawTabArea = true;
                currentTabId = tabHover;
                tabAreaAltered = true;
            }
        }
        if (action == 1506 && (interfaceConfig[106] == 1)) {
            outgoing.putOpcode(185);
            outgoing.putShort(5001);
        }
        if (action == 1500 && (interfaceConfig[106] == 1)) {
            outgoing.putOpcode(185);
            outgoing.putShort(5000);
        }
        if (action == 104) {
            RSComponent widget = RSComponent.forId(secondaryClick);
            spellId = widget.id;
        }
        if (action == 582) {
            NPC npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, npc.pathY[0],
                        localPlayer.pathX[0], false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(57);
                outgoing.putShortA(anInt1285);
                outgoing.putShortA(clicked);
                outgoing.putLEShort(lastItemSelectedSlot);
                outgoing.putShortA(lastItemSelectedInterface);
            }
        }
        if (action == 234) {
            boolean walkTo = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!walkTo) {
                walkTo = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(236);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.putShort(clicked);
            outgoing.putLEShort(first + regionBaseX);
        }
        if (action == 62 && clickObject(clicked, secondaryClick, first)) {
            outgoing.putOpcode(192);
            outgoing.putShort(lastItemSelectedInterface);
            outgoing.putLEShort(clicked >> 14 & 0x7fff);
            outgoing.writeLEShortA(secondaryClick + regionBaseY);
            outgoing.putLEShort(lastItemSelectedSlot);
            outgoing.writeLEShortA(first + regionBaseX);
            outgoing.putShort(anInt1285);
        }
        if (action == 511) {
            boolean flag2 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag2) {
                flag2 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(25);
            outgoing.putLEShort(lastItemSelectedInterface);
            outgoing.putShortA(anInt1285);
            outgoing.putShort(clicked);
            outgoing.putShortA(secondaryClick + regionBaseY);
            outgoing.writeLEShortA(lastItemSelectedSlot);
            outgoing.putShort(first + regionBaseX);
        }
        if (action == 74) {
            outgoing.putOpcode(122);
            outgoing.writeLEShortA(secondaryClick);
            outgoing.putShortA(first);
            outgoing.putLEShort(clicked);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 315) {
            RSComponent mainScreenComponent = RSComponent.forId(secondaryClick);
            boolean promptInput = true;
            if (mainScreenComponent.contentType > 0) {
                promptInput = promptUserForInput(mainScreenComponent);
            }
            if (promptInput) {
                if (secondaryClick == 25054 || secondaryClick == 25466) {
                    grandExchange.clearResults();
                }
                switch (secondaryClick) {
                    case 19144:
                        inventoryOverlay(15106, 3213);
                        resetAnimation(15106);
                        inputTaken = true;
                        break;
                    default:
                        outgoing.putOpcode(185);
                        outgoing.putShort(secondaryClick);
                        break;

                }
            }
        }
        if (action == 561) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0], false,
                        player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(128);
                outgoing.putShort(clicked);
            }
        }
        if (action == 20) {
            NPC class30_sub2_sub4_sub1_sub1_1 = npcs[clicked];
            if (class30_sub2_sub4_sub1_sub1_1 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_1.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub1_1.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(155);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 779) {
            Player class30_sub2_sub4_sub1_sub2_1 = players[clicked];
            if (class30_sub2_sub4_sub1_sub2_1 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_1.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub2_1.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(153);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 519) {
            if (getConsole().isOpen() && getConsole().isUsingScroll() && (super.mouseX >= 494 && super.mouseX <= 515 && super.mouseY >= 26 && super.mouseY <= 296)) {
                return;
            } else if (!menuOpen) {
                first = super.saveClickX - 4;
                secondaryClick = super.saveClickY - 4;
                //scene.request2DTrace(super.saveClickY - 4, super.saveClickX - 4);
            } else {
                first = first - 4;
                secondaryClick = secondaryClick - 4;
               // scene.request2DTrace(secondaryClick - 4, first - 4);
            }
            if (antialiasing) {
                first <<= 1;
                secondaryClick <<= 1;
            }

            scene.request2DTrace(secondaryClick, first);

        }
        if (action == 1062) {
            regionTimer += regionBaseX;
            if (regionTimer >= 113) {
                outgoing.putOpcode(183);
                outgoing.writeTriByte(0xe63271);
                regionTimer = 0;
            }
            clickObject(clicked, secondaryClick, first);
            outgoing.putOpcode(228);
            outgoing.putShortA(clicked >> 14 & 0x7fff);
            outgoing.putShortA(secondaryClick + regionBaseY);
            outgoing.putShort(first + regionBaseX);
        }
        if (action == 679 && !continuedDialogue) {
            outgoing.putOpcode(40);
            outgoing.putShort(secondaryClick);
            continuedDialogue = true;
        }
        if (action == 431) {
            outgoing.putOpcode(129);
            outgoing.putShortA(first);
            outgoing.putShort(secondaryClick);
            outgoing.putShortA(clicked);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 337 || action == 42 || action == 792 || action == 322 || action == 492) {
            String s = menuActionName[id];
            int k1 = s.indexOf("<col=FFFFFF>");
            if (k1 != -1) {
                long l3 = StringUtility.encodeBase37(s.substring(k1 + 12).trim());
                if (action == 337) {
                    addFriend(l3);
                }
                if (action == 42) {
                    addIgnore(l3);
                }
                if (action == 792) {
                    removeFriend(l3);
                }
                if (action == 322) {
                    removeIgnore(l3);
                }
                if (action == 492) {
                    // TODO 317 clan chat kick
                    //kickClanChatUser(l3);
                }
            }
        }
        if (action == 53) {
            outgoing.putOpcode(135);
            outgoing.putLEShort(first);
            outgoing.putShortA(secondaryClick);
            outgoing.putLEShort(clicked);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 539) {
            outgoing.putOpcode(16);
            outgoing.putShortA(clicked);
            outgoing.writeLEShortA(first);
            outgoing.writeLEShortA(secondaryClick);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 484 || action == 6) {
            String s1 = menuActionName[id];
            int l1 = s1.indexOf("<col=FFFFFF>");
            if (l1 != -1) {
                s1 = s1.substring(l1 + 12).trim();
                String s7 = StringUtility.formatUsername(StringUtility
                        .decodeBase37(StringUtility.encodeBase37(s1)));
                boolean flag9 = false;
                for (int j3 = 0; j3 < playerCount; j3++) {
                    Player class30_sub2_sub4_sub1_sub2_7 = players[playerIndices[j3]];
                    if (class30_sub2_sub4_sub1_sub2_7 == null
                            || class30_sub2_sub4_sub1_sub2_7.name == null
                            || !class30_sub2_sub4_sub1_sub2_7.name
                            .equalsIgnoreCase(s7)) {
                        continue;
                    }
                    doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                            class30_sub2_sub4_sub1_sub2_7.pathY[0],
                            localPlayer.pathX[0], false,
                            class30_sub2_sub4_sub1_sub2_7.pathX[0]);
                    if (action == 484) {
                        outgoing.putOpcode(139);
                        outgoing.putLEShort(playerIndices[j3]);
                    }
                    if (action == 6) {
                        outgoing.putOpcode(128);
                        outgoing.putShort(playerIndices[j3]);
                    }
                    flag9 = true;
                    break;
                }

                if (!flag9) {
                    pushMessage("Unable to find " + s7, 0, "");
                }
            }
        }
        if (action == 915) {
            String urlMessage = menuActionName2[id];
            if (urlMessage.startsWith("::")) {
                processCommand(urlMessage.substring(2));
            } else {
                Constants.loadURL(urlMessage);
            }
        }
        if (action == 870) {
            outgoing.putOpcode(53);
            outgoing.putShort(first);
            outgoing.putShortA(lastItemSelectedSlot);
            outgoing.writeLEShortA(clicked);
            outgoing.putShort(lastItemSelectedInterface);
            outgoing.putLEShort(anInt1285);
            outgoing.putShort(secondaryClick);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 847) {
            outgoing.putOpcode(87);
            outgoing.putShortA(clicked);
            outgoing.putShort(secondaryClick);
            outgoing.putShortA(first);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        // useable spells
        if (action == 626) {
            RSComponent widget = RSComponent.forId(secondaryClick);
            spellSelected = 1;
            spellId = widget.id;
            anInt1137 = secondaryClick;
            spellUsableOn = widget.spellUsableOn;
            itemSelected = 0;
            String actionName = widget.selectedActionName;
            if (actionName.indexOf(" ") != -1) {
                actionName = actionName.substring(0, actionName.indexOf(" "));
            }
            String s8 = widget.selectedActionName;
            if (s8.indexOf(" ") != -1) {
                s8 = s8.substring(s8.indexOf(" ") + 1);
            }
            spellTooltip = actionName + " " + widget.spellName + " " + s8;
            if (spellUsableOn == 16) {
                currentTabId = 3;
                tabAreaAltered = true;
            }
            return;
        }
        if (action == 78) {
            outgoing.putOpcode(117);
            outgoing.writeLEShortA(secondaryClick);
            outgoing.writeLEShortA(clicked);
            outgoing.putLEShort(first);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 300) {
            outgoing.putOpcode(141);
            outgoing.writeLEShortA(secondaryClick);
            outgoing.writeLEShortA(clicked);
            outgoing.putLEShort(first);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 291) {
            outgoing.putOpcode(182);
            outgoing.writeLEShortA(secondaryClick);
            outgoing.writeLEShortA(clicked);
            outgoing.putLEShort(first);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 27) {
            Player class30_sub2_sub4_sub1_sub2_2 = players[clicked];
            if (class30_sub2_sub4_sub1_sub2_2 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_2.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub2_2.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt986 += clicked;
                if (anInt986 >= 54) {
                    outgoing.putOpcode(189);
                    outgoing.writeByte(234);
                    anInt986 = 0;
                }
                outgoing.putOpcode(73);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 213) {
            boolean flag3 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag3) {
                flag3 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(79);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.putShort(clicked);
            outgoing.putShortA(first + regionBaseX);
        }
        if (action == 632) {
            packetSender.send(PacketSender.THIRD_CLICK_ITEM_OPCODE, secondaryClick, first, clicked);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 1050) {
            if (StatusOrb.getLevel(RSComponent.forId(24635)) > 0) {
                runToggled = !runToggled;
                sendConfiguration(429, runToggled ? 1 : 0);
                outgoing.putOpcode(185);
                outgoing.putShort(152);
            }
        }
        if (action == 1051) {
            outgoing.putOpcode(185);
            outgoing.putShort(5004);
        }
        if (action == 1052) {
            outgoing.putOpcode(185);
            outgoing.putShort(5002);
        }
        if (action == 1053) {
            outgoing.putOpcode(185);
            outgoing.putShort(5003);
        }
//        if (menuActionName[id].contains("Toggle Run")) {
//            if (StatusOrb.getLevel(RSComponent.forId(24635)) > 0) {
//                runToggled = !runToggled;
//                sendConfiguration(429, runToggled ? 0 : 1);
//            }
//        }
        if (action == 1004) {
            if (tabInterfaceIDs[10] != -1) {
                currentTabId = 10;
                tabAreaAltered = true;
            }
        }
        if (action == 1005) {
            gameFilterMode = gameFilterMode == 1 ? 0 : 1;
        }
        if (action == 1003) {
            clanChatMode = 2;
            inputTaken = true;
        }
        if (action == 1002) {
            clanChatMode = 1;
            inputTaken = true;
        }
        if (action == 1001) {
            clanChatMode = 0;
            inputTaken = true;
        }
        if (action == 1000) {
            currentActiveButton = ChannelButton.CLAN;
            chatTypeView = 11;
            inputTaken = true;
        }
        if (action == 999) {
            currentActiveButton = ChannelButton.ALL;
            chatTypeView = 0;
            inputTaken = true;
        }
        if (action == 998) {
            currentActiveButton = ChannelButton.GAME;
            chatTypeView = 5;
            inputTaken = true;
        }
        if (action == 997) {
            publicChatMode = 3;
            inputTaken = true;
        }
        if (action == 996) {
            publicChatMode = 2;
            inputTaken = true;
        }
        if (action == 995) {
            publicChatMode = 1;
            inputTaken = true;
        }
        if (action == 994) {
            publicChatMode = 0;
            inputTaken = true;
        }
        if (action == 993) {
            currentActiveButton = ChannelButton.PUBLIC;
            chatTypeView = 1;
            inputTaken = true;
        }
        if (action == 992) {
            privateChatMode = 2;
            inputTaken = true;
        }
        if (action == 991) {
            privateChatMode = 1;
            inputTaken = true;
        }
        if (action == 990) {
            privateChatMode = 0;
            inputTaken = true;
        }
        if (action == 989) {
            currentActiveButton = ChannelButton.PRIVATE;
            chatTypeView = 2;
            inputTaken = true;
        }
        if (action == 987) {
            tradeMode = 2;
            inputTaken = true;
        }
        if (action == 986) {
            tradeMode = 1;
            inputTaken = true;
        }
        if (action == 985) {
            tradeMode = 0;
            inputTaken = true;
        }
        if (action == 984) {
            currentActiveButton = ChannelButton.TRADE;
            chatTypeView = 3;
            inputTaken = true;
        }
        if (action == 982) {
            assistMode = 2;
            inputTaken = true;
        }
        if (action == 981) {
            assistMode = 1;
            inputTaken = true;
        }
        if (action == 980) {
            assistMode = 0;
            inputTaken = true;
        }
        if (action == 493) {
            outgoing.putOpcode(75);
            outgoing.writeLEShortA(secondaryClick);
            outgoing.putLEShort(first);
            outgoing.putShortA(clicked);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 652) {
            boolean flag4 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag4) {
                flag4 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(156);
            outgoing.putShortA(first + regionBaseX);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.writeLEShortA(clicked);
        }
        if (action == 94) {
            boolean flag5 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag5) {
                flag5 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(181);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.putShort(clicked);
            outgoing.putLEShort(first + regionBaseX);
            outgoing.putShortA(anInt1137);
        }

        if (action == 646) {
            outgoing.putOpcode(185);
            outgoing.putShort(secondaryClick);
            RSComponent rsComponent = RSComponent.forId(secondaryClick);
            if (rsComponent.automaticConfig) {
                if (rsComponent.scripts != null && rsComponent.scripts[0][0] == 5) {
                    if (rsComponent.interfaceConfig) {
                        int configFrame = rsComponent.scripts[0][1];
                        if (interfaceConfig[configFrame] != rsComponent.scriptDefaults[0]) {
                            interfaceConfig[configFrame] = rsComponent.scriptDefaults[0];
                        }
                    } else {
                        int i2 = rsComponent.scripts[0][1];
                        if (settings[i2] != rsComponent.scriptDefaults[0]) {
                            settings[i2] = rsComponent.scriptDefaults[0];
                            adjustVolume(i2);
                        }
                    }
                }
            }
        }

        if (action == 225) {
            NPC class30_sub2_sub4_sub1_sub1_2 = npcs[clicked];
            if (class30_sub2_sub4_sub1_sub1_2 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_2.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub1_2.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1226 += clicked;
                if (anInt1226 >= 85) {
                    outgoing.putOpcode(230);
                    outgoing.writeByte(239);
                    anInt1226 = 0;
                }
                outgoing.putOpcode(17);
                outgoing.writeLEShortA(clicked);
            }
        }
        if (action == 965) {
            NPC class30_sub2_sub4_sub1_sub1_3 = npcs[clicked];
            if (class30_sub2_sub4_sub1_sub1_3 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_3.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub1_3.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1134++;
                if (anInt1134 >= 96) {
                    outgoing.putOpcode(152);
                    outgoing.writeByte(88);
                    anInt1134 = 0;
                }
                outgoing.putOpcode(21);
                outgoing.putShort(clicked);
            }
        }
        if (action == 413) {
            NPC npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, npc.pathY[0],
                        localPlayer.pathX[0], false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(131);
                outgoing.writeLEShortA(clicked);
                outgoing.putShortA(anInt1137);
            }
        }
        if (action == GameConstants.ACTION_CLOSE_INTERFACE) {
            clearTopInterfaces();
        }
        if (action == 1025) {
            NPC examined = npcs[clicked];
            if (examined != null) {
                outgoing.putOpcode(213);
                outgoing.putShort((int) examined.desc.npcId);
            }
        }
        if (action == 900) {
            clickObject(clicked, secondaryClick, first);
            outgoing.putOpcode(252);
            outgoing.writeLEShortA(clicked >> 14 & 0x7fff);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.putShortA(first + regionBaseX);
        }
        if (action == 412) {
            NPC class30_sub2_sub4_sub1_sub1_6 = npcs[clicked];
            if (class30_sub2_sub4_sub1_sub1_6 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_6.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub1_6.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(72);
                outgoing.putShortA(clicked);
            }
        }
        if (action == 365) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0], false,
                        player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(249);
                outgoing.putShortA(clicked);
                outgoing.putLEShort(anInt1137);
            }
        }
        if (action == 729) {
            Player class30_sub2_sub4_sub1_sub2_4 = players[clicked];
            if (class30_sub2_sub4_sub1_sub2_4 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_4.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub2_4.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(39);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 63) {
            outgoing.putOpcode(221);
            outgoing.putShortA(secondaryClick);
            RSComponent class9_2 = RSComponent.forId(secondaryClick);
            if (class9_2.scripts != null && class9_2.scripts[0][0] == 5) {
                if (class9_2.interfaceConfig) {
                    int configFrame = class9_2.scripts[0][1];
                    if (interfaceConfig[configFrame] != class9_2.scriptDefaults[0]) {
                        interfaceConfig[configFrame] = class9_2.scriptDefaults[0];
                    }
                } else {
                    int i2 = class9_2.scripts[0][1];
                    if (settings[i2] != class9_2.scriptDefaults[0]) {
                        settings[i2] = class9_2.scriptDefaults[0];
                        adjustVolume(i2);
                    }
                }
            }
        }
        if (action == 577) {
            Player class30_sub2_sub4_sub1_sub2_5 = players[clicked];
            if (class30_sub2_sub4_sub1_sub2_5 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_5.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub2_5.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(139);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 956 && clickObject(clicked, secondaryClick, first)) {
            outgoing.putOpcode(35);
            outgoing.putLEShort(first + regionBaseX);
            outgoing.putShortA(anInt1137);
            outgoing.putShortA(secondaryClick + regionBaseY);
            outgoing.putLEShort(clicked >> 14 & 0x7fff);
        }
        if (action == 567) {
            boolean flag6 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag6) {
                flag6 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(23);
            outgoing.putLEShort(secondaryClick + regionBaseY);
            outgoing.putLEShort(clicked);
            outgoing.putLEShort(first + regionBaseX);
        }
        if (action == 867) {
            if ((clicked & 3) == 0) {
                anInt1175++;
            }
            if (anInt1175 >= 59) {
                outgoing.putOpcode(200);
                outgoing.putShort(25501);
                anInt1175 = 0;
            }
            outgoing.putOpcode(43);
            outgoing.putLEShort(secondaryClick);
            outgoing.putShortA(clicked);
            outgoing.putShortA(first);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 543) {
            outgoing.putOpcode(237);
            outgoing.putShort(first);
            outgoing.putShortA(clicked);
            outgoing.putShort(secondaryClick);
            outgoing.putShortA(anInt1137);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 606) {
            String s2 = menuActionName[id];
            int j2 = s2.indexOf("<col=FFFFFF>");
            if (j2 != -1) {
                if (openInterfaceId == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = s2.substring(j2 + 12).trim();
                    canMute = false;
                    for (int i3 = 0; i3 < RSComponent.getComponentCache().length; i3++) {
                        if (RSComponent.getComponentCache()[i3] == null || RSComponent.getComponentCache()[i3].contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = openInterfaceId = RSComponent.getComponentCache()[i3].parentId;
                        break;
                    }
                } else {
                    pushMessage("Please finish what you're doing first.", 0);
                }
            }
        }
        if (action == 607) {
            if (openInterfaceId == -1) {
                clearTopInterfaces();
                pushMessage("Coming soon.", 0);
            } else {
                pushMessage("Please finish what you're doing first.", 0);
            }
        }
        if (action == 607) {
            if (openInterfaceId == -1) {
                clearTopInterfaces();
                pushMessage("Coming soon.", 0);
            } else {
                pushMessage("Please finish what you're doing first.", 0);
            }
        }
        if (action == 607) {
            if (openInterfaceId == -1) {
                clearTopInterfaces();
                reportAbuseInput = "";
                canMute = false;
                for (int i = 0; i < RSComponent.getComponentCache().length; i++) {
                    if (RSComponent.getComponentCache()[i] == null || RSComponent.getComponentCache()[i].contentType != 600) {
                        continue;
                    }
                    reportAbuseInterfaceID = openInterfaceId = RSComponent.getComponentCache()[i].parentId;
                    break;
                }
            } else {
                pushMessage("Please finish what you're doing first.", 0);
            }
        }
        if (action == 491) {
            Player class30_sub2_sub4_sub1_sub2_6 = players[clicked];
            if (class30_sub2_sub4_sub1_sub2_6 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_6.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub2_6.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                outgoing.putOpcode(14);
                outgoing.putShortA(lastItemSelectedInterface);
                outgoing.putShort(clicked);
                outgoing.putShort(anInt1285);
                outgoing.putLEShort(lastItemSelectedSlot);
            }
        }
        if (action == 153) {
            outgoing.putOpcode(169);
            outgoing.putLEShort(secondaryClick);
            RSComponent rsComponent = RSComponent.forId(secondaryClick);
            if (rsComponent.scripts != null && rsComponent.scripts[0][0] == 5) {
                if (rsComponent.interfaceConfig) {
                    int configFrame = rsComponent.scripts[0][1];
                    if (interfaceConfig[configFrame] != rsComponent.scriptDefaults[0]) {
                        interfaceConfig[configFrame] = rsComponent.scriptDefaults[0];
                    }
                } else {
                    int configFrame = rsComponent.scripts[0][1];
                    if (settings[configFrame] != rsComponent.scriptDefaults[0]) {
                        settings[configFrame] = rsComponent.scriptDefaults[0];
                        adjustVolume(configFrame);
                    }
                }
            }
        }
        if (action == 639) {
            String s3 = menuActionName[id];
            int k2 = s3.indexOf("<col=FFFFFF>");
            if (k2 != -1) {
                long l4 = StringUtility.encodeBase37(s3.substring(k2 + 12).trim());
                int k3 = -1;
                for (int i4 = 0; i4 < friendsCount; i4++) {
                    if (friendsListAsLongs[i4] != l4) {
                        continue;
                    }
                    k3 = i4;
                    break;
                }

                if (k3 != -1 && friendsNodeIDs[k3] > 0) {
                    inputTaken = true;
                    inputState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    friendsListAction = 3;
                    aLong953 = friendsListAsLongs[k3];
                    promptMessage = "Enter message to send to " + friendsList[k3];
                }
            }
        }
        if (action == 454) {
            packetSender.send(PacketSender.WIELD_ITEM_OPCODE, clicked, first, secondaryClick);
            atInventoryLoopCycle = 0;
            atInventoryInterface = secondaryClick;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (RSComponent.forId(secondaryClick).parentId == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (RSComponent.forId(secondaryClick).parentId == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 478) {
            NPC class30_sub2_sub4_sub1_sub1_7 = npcs[clicked];
            if (class30_sub2_sub4_sub1_sub1_7 != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_7.pathY[0],
                        localPlayer.pathX[0], false,
                        class30_sub2_sub4_sub1_sub1_7.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                if ((clicked & 3) == 0) {
                    anInt1155++;
                }
                if (anInt1155 >= 53) {
                    outgoing.putOpcode(85);
                    outgoing.writeByte(66);
                    anInt1155 = 0;
                }
                outgoing.putOpcode(18);
                outgoing.putLEShort(clicked);
            }
        }
        if (action == 113) {
            clickObject(clicked, secondaryClick, first);
            outgoing.putOpcode(70);
            outgoing.putLEShort(first + regionBaseX);
            outgoing.putShort(secondaryClick + regionBaseY);
            outgoing.writeLEShortA(clicked >> 14 & 0x7fff);
        }
        if (action == 872) {
            clickObject(clicked, secondaryClick, first);
            outgoing.putOpcode(234);
            outgoing.writeLEShortA(first + regionBaseX);
            outgoing.putShortA(clicked >> 14 & 0x7fff);
            outgoing.writeLEShortA(secondaryClick + regionBaseY);
        }
        if (action == 502) {
            clickObject(clicked, secondaryClick, first);
            outgoing.putOpcode(132);
            outgoing.writeLEShortA(first + regionBaseX);
            outgoing.putShort(clicked >> 14 & 0x7fff);
            outgoing.putShortA(secondaryClick + regionBaseY);
        }
        if (action == 1125) {
            outgoing.putOpcode(235);
            outgoing.putShort(clicked);
        }
        if (action == 169) {
            if (secondaryClick == 24895) {
                processInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES.getId(), getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES) ? 0 : 1);
                return;
            }
            outgoing.putOpcode(185);
            outgoing.putShort(secondaryClick);
            RSComponent secondaryComponent = RSComponent.forId(secondaryClick);
            if (secondaryComponent.automaticConfig) {
                if (secondaryComponent.interfaceConfig) {
                    if (secondaryComponent.scripts != null && secondaryComponent.scripts[0][0] == 5) {
                        int configFrame = secondaryComponent.scripts[0][1];
                        interfaceConfig[configFrame] = interfaceConfig[configFrame] == 1 ? 0 : 1;
                    }
                } else {
                    if (secondaryComponent.scripts != null && secondaryComponent.scripts[0][0] == 5) {
                        int l2 = secondaryComponent.scripts[0][1];
                        if (l2 == 166) {
                            if (settings[l2] == (1 - settings[l2])) {
                                settings[l2] = 1 - settings[l2];
                                adjustVolume(l2);
                            }
                        } else {
                            settings[l2] = 1 - settings[l2];
                            adjustVolume(l2);
                        }
                    }
                }
            }
        }
        if (action == 447) {
            itemSelected = 1;
            lastItemSelectedSlot = first;
            lastItemSelectedInterface = secondaryClick;
            anInt1285 = clicked;
            selectedItemName = ItemDefinition.forId(clicked).name;
            spellSelected = 0;
            return;
        }
        if (action == 1226) {
            outgoing.putOpcode(211);
            outgoing.putShort(clicked >> 14 & 0x7fff);
        }
        if (action == 244) {
            boolean flag7 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0,
                    secondaryClick, localPlayer.pathX[0], false, first);
            if (!flag7) {
                flag7 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        secondaryClick, localPlayer.pathX[0], false, first);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            outgoing.putOpcode(253);
            outgoing.putLEShort(first + regionBaseX);
            outgoing.writeLEShortA(secondaryClick + regionBaseY);
            outgoing.putShortA(clicked);
        }
        if (action == 1448) {
            outgoing.putOpcode(235);
            outgoing.putShort(clicked);
        }
        itemSelected = 0;
        spellSelected = 0;
    }

    public void run() {
        super.run();
    }

    private void createMenu() {
        if (openInterfaceId == 15244) {
            return;
        }
        if (itemSelected == 0 && spellSelected == 0) {
            menuActionName[menuActionRow] = "Walk here";
            menuActionId[menuActionRow] = GameConstants.HOVER_MENU_TOOLTIP;
            firstMenuAction[menuActionRow] = super.mouseX;
            secondMenuAction[menuActionRow] = super.mouseY;
            menuActionRow++;
        }
        int addedObjectData = -1;
        for (int k = 0; k < Model.objectsRendered; k++) {
            int objectData = Model.mapObjectData[k];
            int i1 = objectData & 0x7f;
            int j1 = objectData >> 7 & 0x7f;
            int k1 = objectData >> 29 & 3;
            int objectId = objectData >> 14 & 0x7fff;
            if (objectData == addedObjectData) {
                continue;
            }
            addedObjectData = objectData;
            if (k1 == 2 && scene.getIDTagForXYZ(plane, i1, j1, objectData) >= 0) {
                ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                if (objectDefinition.childrenIds != null) {
                    objectDefinition = objectDefinition.getChild();
                }
                if (objectDefinition == null) {
                    continue;
                }
                if (itemSelected == 1) {
                    menuActionName[menuActionRow] = "Use " + selectedItemName
                            + " with <col=00FFFF>" + objectDefinition.name;
                    menuActionId[menuActionRow] = 62;
                    selectedMenuActions[menuActionRow] = objectData;
                    firstMenuAction[menuActionRow] = i1;
                    secondMenuAction[menuActionRow] = j1;
                    menuActionRow++;
                } else if (spellSelected == 1) {
                    if ((spellUsableOn & 4) == 4) {
                        menuActionName[menuActionRow] = spellTooltip + " <col=00FFFF>" + objectDefinition.name;
                        menuActionId[menuActionRow] = 956;
                        selectedMenuActions[menuActionRow] = objectData;
                        firstMenuAction[menuActionRow] = i1;
                        secondMenuAction[menuActionRow] = j1;
                        menuActionRow++;
                    }
                } else {
                    if (objectDefinition.interactions != null) {
                        for (int type = 4; type >= 0; type--) {
                            if (objectDefinition.interactions[type] != null) {
                                menuActionName[menuActionRow] = objectDefinition.interactions[type] + " <col=00FFFF>" + objectDefinition.name;
                                if (type == 0) {
                                    menuActionId[menuActionRow] = 502;
                                }
                                if (type == 1) {
                                    menuActionId[menuActionRow] = 900;
                                }
                                if (type == 2) {
                                    menuActionId[menuActionRow] = 113;
                                }
                                if (type == 3) {
                                    menuActionId[menuActionRow] = 872;
                                }
                                if (type == 4) {
                                    menuActionId[menuActionRow] = 1062;
                                }
                                selectedMenuActions[menuActionRow] = objectData;
                                firstMenuAction[menuActionRow] = i1;
                                secondMenuAction[menuActionRow] = j1;
                                menuActionRow++;
                            }
                        }
                    }
                    if (Constants.ENABLE_IDS
                            && (myPrivilege >= 2 || myPrivilege <= 3)) {
                        menuActionName[menuActionRow] = "Examine <col=00FFFF>"
                                + objectDefinition.name + " <col=00FF00>(<col=FFFFFF>" + objectId
                                + "<col=00FF00>) (<col=FFFFFF>" + (i1 + regionBaseX) + ","
                                + (j1 + regionBaseY) + "<col=00FF00>)";
                    } else {
                        menuActionName[menuActionRow] = "Examine <col=00FFFF>"
                                + objectDefinition.name;
                    }
                    menuActionId[menuActionRow] = GameConstants.RIGHT_CLICK_MENU_TOOLTIP;
                    selectedMenuActions[menuActionRow] = objectDefinition.type << 14;
                    firstMenuAction[menuActionRow] = i1;
                    secondMenuAction[menuActionRow] = j1;
                    menuActionRow++;
                }
            }
            if (k1 == 1) {
                NPC npc = npcs[objectId];
                try {
                    if (npc.desc.size == 1 && (npc.x & 0x7f) == 64
                            && (npc.y & 0x7f) == 64) {
                        for (int j2 = 0; j2 < npcCount; j2++) {
                            NPC npc2 = npcs[npcIndices[j2]];
                            if (npc2 != null && npc2 != npc
                                    && npc2.desc.size == 1
                                    && npc2.x == npc.x && npc2.y == npc.y) {
                                buildAtNPCMenu(npc2.desc, npcIndices[j2], j1,
                                        i1);
                            }
                        }
                        for (int l2 = 0; l2 < playerCount; l2++) {
                            Player player = players[playerIndices[l2]];
                            if (player != null && player.x == npc.x
                                    && player.y == npc.y) {
                                buildAtPlayerMenu(i1, playerIndices[l2],
                                        player, j1);
                            }
                        }
                    }
                    buildAtNPCMenu(npc.desc, objectId, j1, i1);
                } catch (Exception e) {
                }
            }
            if (k1 == 0) {
                Player player = players[objectId];
                if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                    for (int k2 = 0; k2 < npcCount; k2++) {
                        NPC shownNPC = npcs[npcIndices[k2]];
                        if (shownNPC != null
                                && shownNPC.desc.size == 1
                                && shownNPC.x == player.x
                                && shownNPC.y == player.y) {
                            buildAtNPCMenu(shownNPC.desc,
                                    npcIndices[k2], j1, i1);
                        }
                    }

                    for (int i3 = 0; i3 < playerCount; i3++) {
                        Player class30_sub2_sub4_sub1_sub2_2 = players[playerIndices[i3]];
                        if (class30_sub2_sub4_sub1_sub2_2 != null
                                && class30_sub2_sub4_sub1_sub2_2 != player
                                && class30_sub2_sub4_sub1_sub2_2.x == player.x
                                && class30_sub2_sub4_sub1_sub2_2.y == player.y) {
                            buildAtPlayerMenu(i1, playerIndices[i3],
                                    class30_sub2_sub4_sub1_sub2_2, j1);
                        }
                    }

                }
                buildAtPlayerMenu(i1, objectId, player, j1);
            }
            if (k1 == 3) {
                Deque class19 = groundItems[plane][i1][j1];
                if (class19 != null) {
                    for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19
                            .getNext()) {
                        ItemDefinition itemDef = ItemDefinition.forId(item.ID);
                        if (itemSelected == 1) {
                            menuActionName[menuActionRow] = "Use "
                                    + selectedItemName + " with <col=FF9040>"
                                    + itemDef.name;
                            menuActionId[menuActionRow] = 511;
                            selectedMenuActions[menuActionRow] = item.ID;
                            firstMenuAction[menuActionRow] = i1;
                            secondMenuAction[menuActionRow] = j1;
                            menuActionRow++;
                        } else if (spellSelected == 1) {
                            if ((spellUsableOn & 1) == 1) {
                                menuActionName[menuActionRow] = spellTooltip
                                        + " <col=FF9040>" + itemDef.name;
                                menuActionId[menuActionRow] = 94;
                                selectedMenuActions[menuActionRow] = item.ID;
                                firstMenuAction[menuActionRow] = i1;
                                secondMenuAction[menuActionRow] = j1;
                                menuActionRow++;
                            }
                        } else {
                            for (int j3 = 4; j3 >= 0; j3--) {
                                if (itemDef.floorOptions != null
                                        && itemDef.floorOptions[j3] != null) {
                                    menuActionName[menuActionRow] = itemDef.floorOptions[j3]
                                            + " <col=FF9040>" + itemDef.name;
                                    if (j3 == 0) {
                                        menuActionId[menuActionRow] = 652;
                                    }
                                    if (j3 == 1) {
                                        menuActionId[menuActionRow] = 567;
                                    }
                                    if (j3 == 2) {
                                        menuActionId[menuActionRow] = 234;
                                    }
                                    if (j3 == 3) {
                                        menuActionId[menuActionRow] = 244;
                                    }
                                    if (j3 == 4) {
                                        menuActionId[menuActionRow] = 213;
                                    }
                                    selectedMenuActions[menuActionRow] = item.ID;
                                    firstMenuAction[menuActionRow] = i1;
                                    secondMenuAction[menuActionRow] = j1;
                                    menuActionRow++;
                                } else if (j3 == 2) {
                                    menuActionName[menuActionRow] = "Take <col=FF9040>"
                                            + itemDef.name;
                                }
                            }
                            menuActionId[menuActionRow] = 234;
                            selectedMenuActions[menuActionRow] = item.ID;
                            firstMenuAction[menuActionRow] = i1;
                            secondMenuAction[menuActionRow] = j1;
                            menuActionRow++;
                        }
                        if (Constants.ENABLE_IDS
                                && (myPrivilege >= 2 && myPrivilege <= 3)) {
                            menuActionName[menuActionRow] = "Examine <col=FF9040>"
                                    + itemDef.name + " <col=00FF00> (<col=FFFFFF>" + item.ID
                                    + "<col=00FF00>)";
                        } else {
                            menuActionName[menuActionRow] = "Examine <col=FF9040>"
                                    + itemDef.name;
                        }
                        menuActionId[menuActionRow] = 1448;
                        selectedMenuActions[menuActionRow] = item.ID;
                        firstMenuAction[menuActionRow] = i1;
                        secondMenuAction[menuActionRow] = j1;
                        menuActionRow++;
                    }
                }
            }
        }
    }

    private void spriteNullPointers() {
        multiOverlay = null;
        background = null;
        loadingBar = null;
        loadingBack = null;
        loadingTextSprite = null;
        loginBox = null;
        loginButton = null;
        loginButtonHoverSprite = null;
        logo = null;
        usernameInputBox = null;
        passwordInputBox = null;
        loginMessage = null;
        rememberOff = null;
        rememberOn = null;
        selectAWorld = null;
        worldHeader = null;
        worldSwitch = null;
        worldAscendGreen = null;
        worldDescendGreen = null;
        worldAscendRed = null;
        worldDescendRed = null;
        skillMenuSelect = null;
        skillMenuSelectBottom = null;
        hpEmpty = null;
        hpFull = null;
        hitDamage = null;
        hitDefend = null;
        hitDisease = null;
        hitPoison = null;
        acceptButtonDisabled = null;
        acceptButtonDisabledHover = null;
        bankNewTab = null;
        bankTabEmpty = null;
        bankNewTab = null;
        chatButtons = null;
        reportButton = null;
        chatTab = null;
        chatTabOn = null;
        chatTabHover = null;
        mapBack = null;
        compass = null;
        hitMarks = null;
        headIcons = null;
        skullIcons = null;
        headIconsHint = null;
        crosses = null;
        mapDotItem = null;
        mapDotNPC = null;
        mapDotPlayer = null;
        mapDotFriend = null;
        mapDotTeam = null;
        mapScenes = null;
    }

    public void cleanUpForQuit() {
        SignLink.reporterror = false;
        try {
            if (socketStream != null) {
                socketStream.close();
            }
        } catch (Exception _ex) {
        }
        socketStream = null;
        stopMidi();
        if (mouseDetection != null) {
            mouseDetection.running = false;
        }
        mouseDetection = null;
        resourceProvider.disable();
        resourceProvider = null;
        chatBuffer = null;
        outgoing = null;
        login = null;
        inStream = null;
        anIntArray1234 = null;
        aByteArrayArray1183 = null;
        aByteArrayArray1247 = null;
        anIntArray1235 = null;
        anIntArray1236 = null;
        tileHeights = null;
        tileFlags = null;
        scene = null;
        collisionMaps = null;
        anIntArrayArray901 = null;
        anIntArrayArray825 = null;
        bigX = null;
        bigY = null;
        leftFrame = null;
        rightFrame = null;
        topFrame = null;
        gameframe.resetImageProducers();
        loadingSprite = null;
        chatSettingImageProducer = null;
        mapFunctions = null;
        mapFunctions2 = null;
        anIntArrayArray929 = null;
        players = null;
        playerIndices = null;
        mobsAwaitingUpdate = null;
        playerSynchronizationBuffers = null;
        anIntArray840 = null;
        npcs = null;
        npcIndices = null;
        groundItems = null;
        spawns = null;
        projectiles = null;
        incompleteAnimables = null;
        firstMenuAction = null;
        secondMenuAction = null;
        menuActionId = null;
        selectedMenuActions = null;
        menuActionName = null;
        menuActionName2 = null;
        settings = null;
        interfaceConfig = null;
        minimapHintX = null;
        minimapHintY = null;
        minimapHint = null;
        minimapImage = null;
        friendsList = null;
        friendsListAsLongs = null;
        friendsNodeIDs = null;
        friendsClanRankIDs = null;
        spriteNullPointers();
        titleScreen = null;
        nullLoader();
        ObjectDefinition.nullLoader();
        OSRSObjectDefinition.nullLoader();
        NPCDefinition.nullLoader();
        ItemDefinition.clearCache();
        Floor.cache = null;
        IdentityKit.kits = null;
        RSComponent.setComponentCache(null);
        Animation.animations = null;
        SpotAnimation.cache = null;
        SpotAnimation.models = null;
        SpotAnimation.osrsModels = null;
        VariableParameter.parameters = null;
        super.fullGameScreen = null;
        Player.models = null;
        Rasterizer.nullLoader();
        SceneGraph.nullLoader();
        Model.nullLoader();
        SequenceFrame.nullLoader();
        System.gc();
    }

    public Component getGameComponent() {
        if (SignLink.mainapp != null) {
            return SignLink.mainapp;
        }
        if (super.mainFrame != null) {
            return super.mainFrame;
        } else {
            return this;
        }
    }

    /**
     * Processes commands without colons ("::").
     *
     * @param command The command.
     */
    public void processCommand(String command) {
        String[] args = command.split(" ");
        String commandEntered = args.length == 0 ? command : args[0].startsWith("::") ? args[0].replaceFirst("::", "") : args[0];
        switch (commandEntered) {
            case "clearchat":
                for (int i = 0; i < chatMessages.length; i++) {
                    chatMessages[i] = null;
                }
                pushMessage("Welcome to Gielinor", 0);
                return;
            case "toggleshiftdrop":
                Game.shiftClickDrop = !Game.shiftClickDrop;
                pushMessage("Shift click drop: " +
                            (Game.shiftClickDrop ? "<col=15a850>Enabled</col>" : "<col=cc226c>Disabled</col>") + ".", 0);
                return;
            case "dataon":
            case "dataoff":
                Constants.clientData = command.equalsIgnoreCase("dataon");
                return;
            case "aa1337":
                Game.antialiasing = !Game.antialiasing;
                return;
            case "distance":
                try {
                    int distance = Integer.parseInt(args[1]);
                    if (distance < 25 || distance > 100) {
                        pushMessage("Pick a number 25 through 100", 0);
                    }
                    SceneGraph.mapViewDistance = distance;
                    SceneGraph.tile_visibility_maps = new boolean[8][32][(SceneGraph.mapViewDistance << 1) | 1][(SceneGraph.mapViewDistance << 1) | 1];
                    gameframe.updateGameArea();
                    loadRegion();
                } catch (Exception e) {
                    e.printStackTrace();
                    pushMessage("Wrong args idiot", 0);
                }
                return;
            case "media":
                this.mediaMode = !mediaMode;
                break;
        }
        if (myPrivilege >= 2) {
            switch (commandEntered.toLowerCase()) {
                case "getstack":
                    try {
                        int ited = Integer.parseInt(args[1]);
                        ItemDefinition itemDefinition = ItemDefinition.forId(ited);
                        System.out.println(Arrays.toString(itemDefinition.stackIds));
                    } catch (Exception e) {

                    }
                    return;
                case "ii":
                    //ImageUtil.dumpItemImages();
                    for (int i = 20000; i < 41427; i++) {
                        ItemDefinition itemDef = ItemDefinition.forId(i);
                        if (itemDef == null) {
                            continue;
                        }
                        if (itemDef.name == null || itemDef.name.trim().equalsIgnoreCase("null")) {
                            continue;
                        }
                        ImageUtil.dumpItemImages(itemDef);
                    }
                    return;
                case "osname":
                    System.out.println(OSRSObjectDefinition.forId(Integer.parseInt(args[1])).name);
                    break;
                case "osid":
                    for (int id = 0; id < OSRSObjectDefinition.getObjectCount(); id++) {
                        if (OSRSObjectDefinition.forId(id).name != null && OSRSObjectDefinition.forId(id).name.toLowerCase().contains(args[1])) {
                            System.out.println(OSRSObjectDefinition.forId(id).name + ": " + id);
                        }
                    }
                    break;
                case "scrollmax":
                    RSComponent.forId(Integer.parseInt(args[1])).scrollMax = Integer.parseInt(args[2]);
                    return;
                case "tx":
                    Constants.TEMP_X = Integer.parseInt(args[1]);
                    return;
                case "ty":
                    Constants.TEMP_Y = Integer.parseInt(args[1]);
                    return;
                case "dumpme":
                    Model characterDisplay = localPlayer.prepareAnimatedModel();
                    for (int l2 = 0; l2 < 5; l2++) {
                        if (characterDesignColours[l2] != 0) {
                            characterDisplay
                                    .recolor(
                                            anIntArrayArray1003[l2][0],
                                            anIntArrayArray1003[l2][characterDesignColours[l2]]);
                            if (l2 == 1) {
                                characterDisplay.recolor(anIntArray1204[0],
                                        anIntArray1204[characterDesignColours[l2]]);
                            }
                        }
                    }
                    int staticFrame = localPlayer.standAnimIndex;
                    characterDisplay.skin();
                    characterDisplay.apply(Animation.animations[staticFrame].frames[0], Animation.animations[staticFrame].osrs);
                    ImageUtil.dumpImage(ImageUtil.toSprite(characterDisplay, 500, 500), loginUsername + "_" + System.currentTimeMillis());
                    return;
                case "dodbg":
                    super.shouldDebug = true;
                    return;
                case "mods":
                    System.out.println(Arrays.toString(NPCDefinition.forId(Integer.parseInt(args[1])).modelIds));
                    System.out.println(Arrays.toString(NPCDefinition.forId(Integer.parseInt(args[1])).headModelIds));
                    return;
                case "imods":
                    System.out.println(Arrays.toString(ItemDefinition.forId(Integer.parseInt(args[1])).getEquipModels()));
                    return;
                case "invis":
                    localPlayer.visible = !localPlayer.visible;
                    return;
                case "noclip":
                    NOCLIP = !NOCLIP;
                    if (!NOCLIP) {
                        for (int i = 0; i < 4; i++) {
                            collisionMaps[i].initialize();
                        }
                    } else {
                        for (int k1 = 0; k1 < 4; k1++) {
                            for (int i2 = 1; i2 < 103; i2++) {
                                for (int k2 = 1; k2 < 103; k2++) {
                                    collisionMaps[k1].adjacencies[i2][k2] = 0;
                                }
                            }
                        }
                    }
                    return;
            }
        }
        command = "::" + command;
        packetSender.send(PacketSender.COMMAND_OPCODE, command);
    }

    /**
     * Checks if the entered character is valid for the chat.
     *
     * @param keyCode The key code.
     * @return <code>True</code> if so.
     */
    public boolean isValidKeyCode(int keyCode) {
        return (keyCode >= 32 && keyCode <= 125);
    }

    public void processInput() {
        do {
            int keyCode = getKeyCode();
            if (keyCode == -1) {
                break;
            }
            if (getConsole().isOpen() && loggedIn) {
                getConsole().processConsoleInput(keyCode);
            } else if (openInterfaceId != -1 && openInterfaceId == reportAbuseInterfaceID) {
                if (keyCode == 8 && reportAbuseInput.length() > 0) {
                    reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
                }
                if ((keyCode >= 97 && keyCode <= 122 || keyCode >= 65 && keyCode <= 90 || keyCode >= 48 && keyCode <= 57 || keyCode == 32) && reportAbuseInput.length() < 12) {
                    reportAbuseInput += (char) keyCode;
                }
            } else if (messagePromptRaised) {
                if (isValidKeyCode(keyCode) && promptInput.length() < 80) {
                    promptInput += (char) keyCode;
                    inputTaken = true;
                }
                if (keyCode == 8 && promptInput.length() > 0) {
                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                    inputTaken = true;
                }
                if (keyCode == 13 || keyCode == 10) {
                    messagePromptRaised = false;
                    inputTaken = true;
                    if (friendsListAction == 1) {
                        long l = StringUtility.encodeBase37(promptInput);
                        addFriend(l);
                    }
                    if (friendsListAction == 2 && friendsCount > 0) {
                        long l1 = StringUtility.encodeBase37(promptInput);
                        removeFriend(l1);
                    }
                    // Private message
                    if (friendsListAction == 3 && promptInput.length() > 0) {
                        outgoing.putOpcode(126);
                        outgoing.writeByte(0);
                        int k = outgoing.currentPosition;
                        outgoing.writeLong(aLong953);
                        ChatMessageCodec.encode(promptInput, outgoing);
                        outgoing.writeBytes(outgoing.currentPosition - k);
                        promptInput = ChatMessageCodec.processText(promptInput);
                        if (settings[311] == 1) {
                            promptInput = MessageCensor.apply(promptInput);
                        }
                        pushMessage(promptInput, 6, StringUtility.formatUsername(StringUtility.decodeBase37(aLong953)));
                        if (privateChatMode == 2) {
                            privateChatMode = 1;
                            packetSender.send(PacketSender.CHAT_SETTING_OPCODE);
                        }
                    }
                    if (friendsListAction == 4 && ignoreCount < 100) {
                        long l2 = StringUtility.encodeBase37(promptInput);
                        addIgnore(l2);
                    }
                    if (friendsListAction == 5 && ignoreCount > 0) {
                        long l3 = StringUtility.encodeBase37(promptInput);
                        removeIgnore(l3);
                    }
                    if (friendsListAction == 6) {
                        long l3 = StringUtility.encodeBase37(promptInput);
                        chatJoin(l3);
                    }
                }
            } else if (inputState == 1) {
                if (keyCode >= 48 && keyCode <= 57 && amountOrNameInput.length() < 10) {
                    amountOrNameInput += (char) keyCode;
                    inputTaken = true;
                }
                if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m") && !amountOrNameInput.toLowerCase().contains("b")) && (keyCode == 107 || keyCode == 109) || keyCode == 98) {
                    amountOrNameInput += (char) keyCode;
                    inputTaken = true;
                }
                if (keyCode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if (keyCode == 13 || keyCode == 10) {
                    if (amountOrNameInput.length() > 0) {
                        if (amountOrNameInput.toLowerCase().contains("k")) {
                            amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
                        } else if (amountOrNameInput.toLowerCase().contains("m")) {
                            amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
                        } else if (amountOrNameInput.toLowerCase().contains("b")) {
                            amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
                        }
                        int amount = 0;
                        try {
                            amount = Integer.parseInt(amountOrNameInput);
                        } catch (Exception e) {
                            amount = 0;
                        }
                        outgoing.putOpcode(208);
                        outgoing.putInt(amount);
                    }
                    inputState = 0;
                    inputTaken = true;
                }
            } else if (inputState == 2) {
                int maxInputLength = 12;

                if (getChatInputContext().equals("Enter current password:") || getChatInputContext().equals("Enter new password:")) {
                    maxInputLength = 20;
                } else if (getChatInputContext().equals("Mute reason:") || getChatInputContext().equals("Ban reason:")) {
                    maxInputLength = 64;
                }

                if (isValidKeyCode(keyCode) && amountOrNameInput.length() < maxInputLength) {
                    amountOrNameInput += (char) keyCode;
                    inputTaken = true;
                    if (openInterfaceId == 5292 && settings[1012] == 1) {
                        RSComponent bank = RSComponent.forId(5382);
                        Arrays.fill(bankInvTemp, 0);
                        Arrays.fill(bankStackTemp, 0);
                        for (int slot = 0, bankSlot = 0; slot < bank.inventory.length; slot++) {
                            if (bank.inventory[slot] - 1 > 0) {
                                if (ItemDefinition.forId(bank.inventory[slot] - 1) == null || ItemDefinition.forId(bank.inventory[slot] - 1).name == null) {
                                    continue;
                                }
                                if (ItemDefinition.forId(bank.inventory[slot] - 1).name.toLowerCase().contains(amountOrNameInput.toLowerCase())) {
                                    bankInvTemp[bankSlot] = bank.inventory[slot];
                                    bankStackTemp[bankSlot++] = bank.inventoryValue[slot];
                                }
                            }
                        }
                    }
                }
                if (keyCode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if (keyCode == 13 || keyCode == 10) {
                    if (amountOrNameInput.length() > 0) {
                        outgoing.putOpcode(60);
                        outgoing.writeByte(amountOrNameInput.length() + 1);
                        outgoing.putString(amountOrNameInput);
                    }
                    inputState = 0;
                    inputTaken = true;
                }
            } else if (inputState == 3) {
                grandExchange.typing = true;
                if (keyCode == 10) {
                    grandExchange.totalItemResults = 0;
                    grandExchange.scrollMax = 0;
                    grandExchange.scrollPosition = 0;
                    amountOrNameInput = "";
                    inputState = 0;
                    inputTaken = true;
                }
                if (keyCode == 13 || keyCode == 10) {
                    if (amountOrNameInput.length() == 0) {
                        grandExchange.buttonClicked = false;
                    }
                }
                if (keyCode >= 32 && keyCode <= 122 && amountOrNameInput.length() < 40) {
                    amountOrNameInput += (char) keyCode;
                    inputTaken = true;
                }
                if (keyCode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
            } else if (backDialogueId == -1) {
                if (isValidKeyCode(keyCode) && inputString.length() < 80) {
                    inputString += (char) keyCode;
                    inputTaken = true;
                }
                if (keyCode == 8 && inputString.length() > 0) {
                    inputString = inputString.substring(0, inputString.length() - 1);
                    inputTaken = true;
                }
                if (keyCode == 9) {
                    automaticReply();
                }
                if ((keyCode == 13 || keyCode == 10) && inputString.length() > 0) {
                    if (inputString.startsWith("/") && getClanChatName() != null) {
                        packetSender.send(PacketSender.CHAT_OPCODE, 0, 0, inputString);
                        inputString = ChatMessageCodec.processText(inputString);
                        if (settings[311] == 1) {
                            inputString = MessageCensor.apply(inputString);
                        }
                        localPlayer.textCycle = 150;
                        if (publicChatMode == 2) {
                            publicChatMode = 3;
                            packetSender.send(PacketSender.CHAT_SETTING_OPCODE);
                        }
                        inputString = "";
                        inputTaken = true;
                    } else if (inputString.startsWith("::")) {
                        processCommand(inputString.substring(2));
                    } else {
                        String chatText = inputString.toLowerCase();
                        int chatTextColor = 0;
                        if (chatText.startsWith("yellow:")) {
                            chatTextColor = 0;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("red:")) {
                            chatTextColor = 1;
                            inputString = inputString.substring(4);
                        } else if (chatText.startsWith("green:")) {
                            chatTextColor = 2;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("cyan:")) {
                            chatTextColor = 3;
                            inputString = inputString.substring(5);
                        } else if (chatText.startsWith("purple:")) {
                            chatTextColor = 4;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("white:")) {
                            chatTextColor = 5;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("flash1:")) {
                            chatTextColor = 6;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("flash2:")) {
                            chatTextColor = 7;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("flash3:")) {
                            chatTextColor = 8;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("glow1:")) {
                            chatTextColor = 9;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("glow2:")) {
                            chatTextColor = 10;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("glow3:")) {
                            chatTextColor = 11;
                            inputString = inputString.substring(6);
                        }
                        chatText = inputString.toLowerCase();
                        int chatTextEffect = 0;
                        if (chatText.startsWith("wave:")) {
                            chatTextEffect = 1;
                            inputString = inputString.substring(5);
                        } else if (chatText.startsWith("wave2:")) {
                            chatTextEffect = 2;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("shake:")) {
                            chatTextEffect = 3;
                            inputString = inputString.substring(6);
                        } else if (chatText.startsWith("scroll:")) {
                            chatTextEffect = 4;
                            inputString = inputString.substring(7);
                        } else if (chatText.startsWith("slide:")) {
                            chatTextEffect = 5;
                            inputString = inputString.substring(6);
                        }
                        packetSender.send(PacketSender.CHAT_OPCODE, chatTextEffect, chatTextColor, inputString);
                        inputString = ChatMessageCodec.processText(inputString);
                        if (settings[311] == 1) {
                            inputString = MessageCensor.apply(inputString);
                        }
                        localPlayer.spokenText = inputString;
                        localPlayer.textColour = chatTextColor;
                        localPlayer.textEffect = chatTextEffect;
                        localPlayer.textCycle = 150;
                        pushMessage(new ChatMessage(2, localPlayer.name, localPlayer.spokenText, localPlayer.getTitle(), localPlayer.isTitleSuffix(), chatIcons));
                        if (publicChatMode == 2) {
                            publicChatMode = 3;
                            packetSender.send(PacketSender.CHAT_SETTING_OPCODE);
                        }
                    }
                    inputString = "";
                    inputTaken = true;
                }
            }
        }
        while (true);
    }

    private void buildPublicChat(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) {
                continue;
            }
            if (chatTypeView != 1) {
                continue;
            }
            int chatType = chatMessages[i1].getType();
            String chatMessageName = chatMessages[i1].getNameClean();
            int k1 = (70 - l * 14 + 42) + chatScrollPosition + 4 + 5;
            if (k1 < -23) {
                break;
            }
            if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 &&
                    isFriendOrSelf(chatMessageName))) {
                assert chatMessageName != null;
                if (j > k1 - 14 && j <= k1 && !chatMessageName.equals(localPlayer.name)) {
                    menuActionName[menuActionRow] = "Report <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 606;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildFriendChat(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) {
                continue;
            }
            if (chatTypeView != 2) {
                continue;
            }
            int chatType = chatMessages[i1].getType();
            String chatMessageName = chatMessages[i1].getNameClean();
            int k1 = (70 - l * 14 + 42) + chatScrollPosition + 4 + 5;
            if (k1 < -23) {
                break;
            }

            if ((chatType == 5 || chatType == 6) && (splitPrivateChat == 0 || chatTypeView == 2) &&
                    (chatType == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(chatMessageName))) {
                l++;
            }
            if ((chatType == 3 || chatType == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Report <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 606;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildAcceptMessage(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) {
                continue;
            }
            if (chatTypeView != 3 && chatTypeView != 4) {
                continue;
            }
            int chatType = chatMessages[i1].getType();
            String chatMessageName = chatMessages[i1].getName();
            String images = chatMessages[i1].getChatIcons();
            int k1 = (70 - l * 14 + 42) + chatScrollPosition + 4 + 5;
            if (k1 < -23) {
                break;
            }
            if (chatMessageName != null && images.length() > 0) {
                chatMessageName = chatMessageName.substring(6 + images.length());
            }
            if (chatTypeView == 3 && chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Accept trade <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 484;
                    menuActionRow++;
                }
                l++;
            }
            if (chatTypeView == 4 && chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Accept challenge <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
            // TODO
            if (chatTypeView == 4 && chatType == 17 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Accept challenge <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildChatAreaMenu(int mX, int mY) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) {
                continue;
            }
            int chatType = chatMessages[i1].getType();
            int k1 = (70 - l * 14 + 42) + chatScrollPosition + 4 + 5;
            String chatMessageName = chatMessages[i1].getNameClean();
            if (chatTypeView == 1) {
                buildPublicChat(mY);
                break;
            }
            if (chatTypeView == 2) {
                buildFriendChat(mY);
                break;
            }
            if (chatTypeView == 3 || chatTypeView == 4) {
                buildAcceptMessage(mY);
                break;
            }
            if (inputState == 3) {
                grandExchange.buildItemSearch(mY);
                break;
            }
            if (chatTypeView == 5) {
                break;
            }
            if (chatMessages[i1].getType() == 12 && chatMessages[i1].getUrlText() != null && chatMessages[i1].getUrl() != null) {
                int start = chatMessageName.indexOf(chatMessages[i1].getUrlText());
                int xPosition = regularFont.getTextWidth(chatMessageName.substring(0, start)) + 8;
                int xPositionEnd = xPosition + regularFont.getTextWidth(chatMessages[i1].getUrlText()) + 6;
                if (mY > k1 - 14 && mY <= k1 && mX >= xPosition && mX <= xPositionEnd) {
                    menuActionName[menuActionRow] = chatMessages[i1].getUrl().startsWith("::") ? "View" : "Visit website";
                    menuActionName2[menuActionRow] = chatMessages[i1].getUrl();
                    menuActionId[menuActionRow] = 915;
                    menuActionRow++;
                }
                l++;
            }
            if (chatType == 0) {
                l++;
            }
            if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (mY > k1 - 14 && mY <= k1 && (localPlayer.name != null && chatMessageName != null &&
                        !chatMessageName.equals(localPlayer.name))) {
                    menuActionName[menuActionRow] = "Report <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 606;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
            if ((chatType == 3 || chatType == 7) && splitPrivateChat == 0 && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (mY > k1 - 14 && mY <= k1) {
                    menuActionName[menuActionRow] = "Report <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 606;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
            if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (mY > k1 - 14 && mY <= k1) {
                    menuActionName[menuActionRow] = "Accept trade <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 484;
                    menuActionRow++;
                }
                l++;
            }
            if ((chatType == 5 || chatType == 6) && splitPrivateChat == 0 && privateChatMode < 2) {
                l++;
            }
            if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (mY > k1 - 14 && mY <= k1) {
                    menuActionName[menuActionRow] = "Accept challenge <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
            if (chatType == 17 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatMessageName))) {
                if (mY > k1 - 14 && mY <= k1) {
                    menuActionName[menuActionRow] = "Accept challenge <col=FFFFFF>" + chatMessageName;
                    menuActionId[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
            if (chatMessages[i1].getType() == 16 && (clanChatMode == 0 || clanChatMode == 1 && isFriendOrSelf(chatMessageName))) {
                assert chatMessageName != null;
                if (!chatMessageName.equalsIgnoreCase(getChatOwnerName())) {
                    if (mY > k1 - 14 && mY <= k1 && !chatMessageName.equalsIgnoreCase(localPlayer.name)) {
                        // TODO 317 get correct kick rights
                        if (getClanChatName() != null && getCurrentUserClanRights() >= getChatKickRights()) {
                            menuActionName[menuActionRow] = "Kick <col=FFFFFF>" + StringUtility.formatUsername(chatMessageName);
                            menuActionId[menuActionRow] = 492;
                            menuActionRow++;
                        }
                        menuActionName[menuActionRow] = "Report <col=FFFFFF>" + StringUtility.formatUsername(chatMessageName);
                        menuActionId[menuActionRow] = 606;
                        menuActionRow++;
                        menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + StringUtility.formatUsername(chatMessageName);
                        menuActionId[menuActionRow] = 42;
                        menuActionRow++;
                        menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + StringUtility.formatUsername(chatMessageName);
                        menuActionId[menuActionRow] = 337;
                        menuActionRow++;
                    }
                    l++;
                }
            }
        }
    }

    private void drawSplitPrivateChat() {
        if (splitPrivateChat == 0) {
            return;
        }
        int i = 0;
        if (systemUpdateTime != 0) {
            i = 1;
        }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                ChatMessageType chatMessageType = chatMessages[j].getChatMessageType();
                if (chatMessageType != null) {
                    if (chatMessageType.canDrawSplit(chatMessages[j], this)) {
                        int drawingOffsetY = 329 - i * 13;
                        if (frameMode != ScreenMode.FIXED) {
                            drawingOffsetY = frameHeight - 170 - i * 13;
                        }
                        chatMessageType.drawSplit(chatMessages[j], this, drawingOffsetY);
                        if (++i >= 5) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void pushMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() == 0 && dialogueId != -1) {
            clickToContinueString = chatMessage.getText();
            super.clickMode3 = 0;
        }
        if (backDialogueId == -1) {
            inputTaken = true;
        }
        System.arraycopy(chatMessages, 0, chatMessages, 1, 499);
        chatMessages[0] = chatMessage;
    }

    public void pushMessage(String message, int chatType, String chatName, String playerTitle, boolean titleSuffix) {
        pushMessage(new ChatMessage(chatType, chatName, message, playerTitle, titleSuffix, chatIcons));
    }

    public void pushMessage(String message, int chatType, long clearDelay) {
        pushMessage(new ChatMessage(chatType, "", message, "", false, clearDelay, 0, chatIcons));
    }

    public void pushMessage(String message, int chatType, long clearDelay, int filterType) {
        pushMessage(new ChatMessage(chatType, "", message, "", false, clearDelay, filterType, chatIcons));
    }

    public void pushMessage(String message, int chatType) {
        pushMessage(message, chatType, -1);
    }

    public void pushMessage(String message, int chatType, String chatName) {
        pushMessage(new ChatMessage(chatType, chatName, message, null, false, chatIcons));
    }

    private void setupGameplayScreen() {
        if (gameframe.getChatImageProducer() != null) {
            return;
        }
        nullLoader();
        super.fullGameScreen = null;
        titleScreen = null;
        gameframe.setImageProducers();
        Raster.clear();
        new ImageProducer(269, 37); // TODO Can be removed?
        chatSettingImageProducer = new ImageProducer(249, 45);
        welcomeScreenRaised = true;
    }

    public void rightClickChatButtons() {
        int[] x = {5, 62, 119, 176, 233, 290, 347, 404};
        if (mouseY >= frameHeight - 22 && mouseY <= frameHeight) {
            if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Switch tab";
                menuActionId[1] = 999;
                menuActionRow = 2;
            } else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[2] = "<col=FFFF00>Game:</col> Filter";
                menuActionId[2] = 1005;
                menuActionName[1] = "Switch tab";
                menuActionId[1] = 998;
                menuActionRow = 3;
            } else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Hide public";
                menuActionId[1] = 997;
                menuActionName[2] = "Off public";
                menuActionId[2] = 996;
                menuActionName[3] = "Friends public";
                menuActionId[3] = 995;
                menuActionName[4] = "On public";
                menuActionId[4] = 994;
                menuActionName[5] = "Switch tab";
                menuActionId[5] = 993;
                menuActionRow = 6;
            } else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Off private";
                menuActionId[1] = 992;
                menuActionName[2] = "Friends private";
                menuActionId[2] = 991;
                menuActionName[3] = "On private";
                menuActionId[3] = 990;
                menuActionName[4] = "Switch tab";
                menuActionId[4] = 989;
                menuActionRow = 5;
            } else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Off clan chat";
                menuActionId[1] = 1003;
                menuActionName[2] = "Friends clan chat";
                menuActionId[2] = 1002;
                menuActionName[3] = "On clan chat";
                menuActionId[3] = 1001;
                menuActionName[4] = "Switch tab";
                menuActionId[4] = 1000;
                menuActionRow = 5;
            } else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Off trade";
                menuActionId[1] = 987;
                menuActionName[2] = "Friends trade";
                menuActionId[2] = 986;
                menuActionName[3] = "On trade";
                menuActionId[3] = 985;
                menuActionName[4] = "Switch tab";
                menuActionId[4] = 984;
                menuActionRow = 5;
            } else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
                    && super.mouseY >= frameHeight - 23
                    && super.mouseY <= frameHeight) {
                menuActionName[1] = "Off Assist";
                menuActionId[1] = 982;
                menuActionName[2] = "Friends Assist";
                menuActionId[2] = 981;
                menuActionName[3] = "On Assist";
                menuActionId[3] = 980;
                menuActionName[4] = "Switch tab";
                menuActionId[4] = 979;
                menuActionRow = 5;
            } else if (super.mouseX >= 404 && super.mouseX <= 515) {
                menuActionName[1] = "Report game bug";
                menuActionId[1] = 608;
                menuActionName[2] = "Toggle player option";
                menuActionId[2] = 607;
                menuActionName[3] = "Report abuse";
                menuActionId[3] = 609;
                menuActionRow = 4;
            }
        }
    }

    public void setMenuAction(int[] types, String... names) {
        int length = names.length;
        for (int row = 0; row < length; row++) {
            menuActionName[row + 1] = names[row];
            menuActionId[row + 1] = types[row];
        }
        menuActionRow = length + 1;
    }

    public void processRightClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        menuActionName[0] = "Cancel";
        menuActionId[0] = GameConstants.ACTION_CANCEL;
        menuActionRow = 1;
        if (showChatComponents && !isMediaMode()) {
            buildSplitPrivateChatMenu();
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (frameMode == ScreenMode.FIXED) {
            if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338) {
                if (openInterfaceId != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(openInterfaceId), 4, 4, super.mouseX, super.mouseY, 0, 0);
                } else {
                    createMenu();
                }
            }
        } else if (frameMode != ScreenMode.FIXED) {
            if (gameframe.canClickArea()) {
                if (super.mouseX > (frameWidth / 2) - 356
                        && super.mouseY > (frameHeight / 2) - 230
                        && super.mouseX < ((frameWidth / 2) + 356)
                        && super.mouseY < (frameHeight / 2) + 230
                        && openInterfaceId != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(openInterfaceId), (frameWidth / 2) - 356,
                            (frameHeight / 2) - 230, super.mouseX,
                            super.mouseY, 0, 0);
                } else {
                    createMenu();
                }
            }
        }
        if (anInt886 != anInt1026) {
            anInt1026 = anInt886;
        }
        if (anInt1315 != anInt1129) {
            anInt1129 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (isFixed() || !getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT)) {
            final int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 503;
            final int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 765;
            if (super.mouseX > 548 + xOffset && super.mouseX < 740 + xOffset
                    && super.mouseY > 207 + yOffset
                    && super.mouseY < 468 + yOffset) {
                if (overlayInterfaceId != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(overlayInterfaceId), 548 + xOffset,
                            207 + yOffset, super.mouseX, super.mouseY, 0, 0);
                } else if (tabInterfaceIDs[currentTabId] != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(tabInterfaceIDs[currentTabId]), 548 + xOffset,
                            207 + yOffset, super.mouseX, super.mouseY, 0, 0);
                }
            }
        } else if (!isFixed()) {
            final int yOffset = frameWidth >= 1000 ? 37 : 74;
            if (super.mouseX > frameWidth - 197
                    && super.mouseY > frameHeight - yOffset - 267
                    && super.mouseX < frameWidth - 7
                    && super.mouseY < frameHeight - yOffset - 7
                    && showTabComponents && !isMediaMode()) {
                if (overlayInterfaceId != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(overlayInterfaceId), frameWidth - 197,
                            frameHeight - yOffset - 267, super.mouseX,
                            super.mouseY, 0, 0);
                } else if (tabInterfaceIDs[currentTabId] != -1) {
                    componentDrawing.buildInterfaceMenu(RSComponent.forId(tabInterfaceIDs[currentTabId]), frameWidth - 197,
                            frameHeight - yOffset - 267, super.mouseX,
                            super.mouseY, 0, 0);
                }
            }
        }
        if (anInt886 != anInt1048) {
            tabAreaAltered = true;
            anInt1048 = anInt886;
        }
        if (anInt1315 != anInt1044) {
            tabAreaAltered = true;
            anInt1044 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (super.mouseX > 0
                && super.mouseY > (frameMode == ScreenMode.FIXED ? 338
                : frameHeight - 165)
                && super.mouseX < 490
                && super.mouseY < (frameMode == ScreenMode.FIXED ? 463
                : frameHeight - 40) && showChatComponents) {
            if (backDialogueId != -1) {
                componentDrawing.buildInterfaceMenu(RSComponent.forId(backDialogueId), 20,
                        (frameMode == ScreenMode.FIXED ? 358
                                : frameHeight - 145), super.mouseX, super.mouseY, 0, 0);
            } else if (super.mouseY < (frameMode == ScreenMode.FIXED ? 463
                    : frameHeight - 40) && super.mouseX < 490) {
                buildChatAreaMenu(super.mouseX, super.mouseY - (frameMode == ScreenMode.FIXED ? 338 : frameHeight - 165));
            }
        }
        if (backDialogueId != -1 && anInt886 != anInt1039) {
            inputTaken = true;
            anInt1039 = anInt886;
        }
        if (backDialogueId != -1 && anInt1315 != anInt1500) {
            inputTaken = true;
            anInt1500 = anInt1315;
        }
        if (super.mouseX > 4 && super.mouseY > 480 && super.mouseX < 516
                && super.mouseY < frameHeight) {
            rightClickChatButtons();
        }
        gameframe.processHoverAndClicks();
        gameframe.processTabAreaHovers();
        boolean setMenu = false;
        while (!setMenu) {
            setMenu = true;
            for (int menuRow = 0; menuRow < menuActionRow - 1; menuRow++) {
                if (menuActionId[menuRow] < 1000 && menuActionId[menuRow + 1] > 1000) {
                    String menuName = menuActionName[menuRow];
                    menuActionName[menuRow] = menuActionName[menuRow + 1];
                    menuActionName[menuRow + 1] = menuName;
                    int actionId = menuActionId[menuRow];
                    menuActionId[menuRow] = menuActionId[menuRow + 1];
                    menuActionId[menuRow + 1] = actionId;
                    actionId = firstMenuAction[menuRow];
                    firstMenuAction[menuRow] = firstMenuAction[menuRow + 1];
                    firstMenuAction[menuRow + 1] = actionId;
                    actionId = secondMenuAction[menuRow];
                    secondMenuAction[menuRow] = secondMenuAction[menuRow + 1];
                    secondMenuAction[menuRow + 1] = actionId;
                    actionId = selectedMenuActions[menuRow];
                    selectedMenuActions[menuRow] = selectedMenuActions[menuRow + 1];
                    selectedMenuActions[menuRow + 1] = actionId;
                    setMenu = false;
                }
            }
        }
    }

    private void drawWorldSelect(boolean show) {
        worldSelect = show;
        if (!show) {
            resetAllImageProducers();
            drawingWorldSelect = false;
            return;
        }
        resetAllImageProducers();
        welcomeScreenRaised = true;
        worldSelectImageProducer = new ImageProducer(765, 503);
        worldSelectImageProducer.initDrawingArea();

        selectAWorld.drawSprite(0, 0);
        worldHeader.drawSprite(125, 0);
        // Draw orders
        (WorldDefinition.isAscending("WORLD") ? worldAscendGreen : worldAscendRed).drawSprite(280, 4);
        (WorldDefinition.isDescending("WORLD") ? worldDescendGreen : worldDescendRed).drawSprite(295, 4);
        (WorldDefinition.isAscending("PLAYERS") ? worldAscendGreen : worldAscendRed).drawSprite(390, 4);
        (WorldDefinition.isDescending("PLAYERS") ? worldDescendGreen : worldDescendRed).drawSprite(405, 4);
        (WorldDefinition.isAscending("LOCATION") ? worldAscendGreen : worldAscendRed).drawSprite(500, 4);
        (WorldDefinition.isDescending("LOCATION") ? worldDescendGreen : worldDescendRed).drawSprite(515, 4);
        (WorldDefinition.isAscending("TYPE") ? worldAscendGreen : worldAscendRed).drawSprite(610, 4);
        (WorldDefinition.isDescending("TYPE") ? worldDescendGreen : worldDescendRed).drawSprite(625, 4);
        // End orders
        int xOffset = 0;
        int yOffset = 0;
        int index = 0;
        for (WorldDefinition worldDefinition : WorldDefinition.getWorldList()) {
            boolean hover = (mouseX > (199 + xOffset) && mouseX < (199 + xOffset + 88)
                    && mouseY > (49 + yOffset) && mouseY <= (49 + yOffset + 19));
            if (worldDefinition.getFlag() > 0) {
                (hover ? worldSelectMembersHover : worldSelectMembers).drawSprite(199 + xOffset, 49 + yOffset);
            } else {
                (hover ? worldSelectFreeHover : worldSelectFree).drawSprite(199 + xOffset, 49 + yOffset);
            }
            boldFont.drawCenteredString(String.valueOf(worldDefinition.getId()), 214 + xOffset, 63 + yOffset, 0);
            worldCountrySprites[worldDefinition.getCountry()].drawSprite(228 + xOffset, 49 + yOffset);
            smallFont.drawCenteredString(String.valueOf(worldDefinition.getPlayers()), 259 + xOffset, 63 + yOffset, 0xFFFFFF);
            yOffset += 24;
            index++;
            if (index % 18 == 0) {
                yOffset = 0;
                xOffset += 94;
            }
        }

        xOffset = 0;
        yOffset = 0;
        index = 0;
        for (WorldDefinition worldDefinition : WorldDefinition.getWorldList()) {
            boolean hover = (mouseX > (199 + xOffset) && mouseX < (199 + xOffset + 88)
                    && mouseY > (49 + yOffset) && mouseY <= (49 + yOffset + 19));
            if (hover) {
                drawYellowTooltip(worldDefinition.getActivity().isEmpty() ? "-" : worldDefinition.getActivity(), mouseX - 15, mouseY + 25);
            }
            yOffset += 24;
            index++;
            if (index % 18 == 0) {
                yOffset = 0;
                xOffset += 94;
            }
        }
        worldSelectImageProducer.drawGraphics(0, super.graphics, 0);
        if (!drawingWorldSelect) {
            drawingWorldSelect = true;
        }
    }

    /**
     * Requests worlds from the server.
     */
    private void requestWorlds() {
        WorldDefinition.sort("WORLD", "DESC");
        try {
            // TODO Login server world port
            socketStream = new BufferedConnection(this, openSocket(Constants.LOGIN_SERVER_ADDRESS, 43594 + portOffset));
            outgoing.currentPosition = 0;
            outgoing.writeByte(ProtocolConstants.GAME_SERVER_WORLDS);
            outgoing.putInt(lastWorldUpdateStamp);
            socketStream.queueBytes(5, outgoing.payload);
            socketStream.read();
            int available = socketStream.available();
            if (available > 1) {
                socketStream.flushInputStream(inStream.payload, 2);
                inStream.currentPosition = 0;
                packetSize = inStream.getShort();
                available -= 2;
            }
            if (available < packetSize) {
                return;
            }
            socketStream.flushInputStream(inStream.payload, packetSize);
            ByteBuffer byteBuffer = ByteBuffer.wrap(inStream.payload);
            byteBuffer.get(); // Discarded
            boolean updateWorldList = byteBuffer.get() == 1;
            if (updateWorldList) {
                WorldDefinition.getWorldList().clear();
                int worldCount = byteBuffer.getShort();
                while (worldCount > 0) {
                    byte worldId = byteBuffer.get();
                    int flag = byteBuffer.getInt();
                    String activity = ByteBufferUtils.getJagString(byteBuffer);
                    String address = ByteBufferUtils.getJagString(byteBuffer);
                    int port = byteBuffer.getInt();
                    int playerCount = byteBuffer.getShort();
                    int country = ByteBufferUtils.getSmart(byteBuffer);
                    String region = ByteBufferUtils.getJagString(byteBuffer);
                    WorldDefinition worldDefinition = new WorldDefinition(worldId, activity, country, flag, address, port, region);
                    worldDefinition.setPlayers(playerCount);
                    WorldDefinition.addWorld(worldDefinition);
                    worldCount--;
                }
                lastWorldUpdateStamp = byteBuffer.getInt();
            } else {
                for (WorldDefinition worldDefinition : WorldDefinition.getWorldList()) {
                    int playerCount = byteBuffer.getShort();
                    worldDefinition.setPlayers(playerCount);
                }
            }
        } catch (IOException e) {
            //  worldError = true;
            loading = false;
            System.out.println("error_world_connect");
            e.printStackTrace();
            if (WorldDefinition.forId(1) == null) {
                WorldDefinition worldDefinition = new WorldDefinition(1, "Main - Free", 0, 0, Constants.SERVER_ADDRESS, 43594, "North America");
                WorldDefinition.addWorld(worldDefinition);
            }
        }
        WorldDefinition.sort("WORLD", "DESC");
    }

    /**
     * The login method for the 317 protocol.
     *
     * @param name         The name of the user trying to login.
     * @param password     The password of the user trying to login.
     * @param reconnecting The flag for the user indicating to attempt to reconnect.
     */
    public void login(String name, String password, boolean reconnecting) {
        SignLink.setError(name);
        drawWorldSelect(false);
        try {
            if (!reconnecting) {
                loginMessage1 = "";
                loginMessage2 = "Connecting to server...";
                loginMessage3 = "";
                loggingIn = true;
                drawLoginScreen(false);
            }
            socketStream = new BufferedConnection(this, openSocket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT + portOffset));
            long encoded = StringUtility.encodeBase37(name);
            int nameHash = (int) (encoded >> 16 & 31L);
            outgoing.currentPosition = 0;
            //client -> server
            outgoing.writeByte(ProtocolConstants.GAME_SERVER_OPCODE);
            outgoing.writeByte(nameHash);
            socketStream.queueBytes(2, outgoing.payload);
            // server -> client
            for (int j = 0; j < 8; j++) {
                socketStream.read();
            }
            /*
             * Returns numeric values indicated the state of a users
			 * login session.
			 */
            int response = socketStream.read();
            int copyResponse = response;

            if (response == 0) {
                socketStream.flushInputStream(inStream.payload, 8);
                inStream.currentPosition = 0;
                serverSeed = inStream.getLong(); //aka server session key
                int seed[] = new int[4];
                seed[0] = (int) (Math.random() * 99999999D);
                seed[1] = (int) (Math.random() * 99999999D);
                seed[2] = (int) (serverSeed >> 32);
                seed[3] = (int) serverSeed;
                outgoing.currentPosition = 0;
                outgoing.writeByte(10);
                outgoing.putInt(seed[0]);
                outgoing.putInt(seed[1]);
                outgoing.putInt(seed[2]);
                outgoing.putInt(seed[3]);
                /*
                 * User identification, in the 317 protocol a random number would
				 * be generated for a player as an Integer, and this integer would
				 * be stored on the users computer in a file called uid.dat within
				 * the clients cache files.
				 */
                outgoing.putInt(SignLink.uid);
                outgoing.putString(name);
                outgoing.putString(password);
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
                byte[] mac = null;

                if (networkInterface != null)
                    mac = networkInterface.getHardwareAddress();
                mac = mac == null ? "Invalid".getBytes() : mac;
                String computerName = "Invalid";
                if (SignLink.socketAddress.getHostName() != null) {
                    computerName = SignLink.socketAddress.getHostName();
                }
                outgoing.putString(computerName);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                outgoing.putString(stringBuilder.toString());

                /*
                 * RSA is an algorithm used by modern computers to encrypt and decrypt messages.
				 * It is an asymmetric cryptographic algorithm. Asymmetric means that there are two different keys.
				 * This is also called public key cryptography, because one of them can be given to everyone.
				 * The other key must be kept private. RSA works because it generates number so large that it is
				 * virtually impossible to find the modulus of given number.
				 */
                outgoing.encodeRSA(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS);
                login.currentPosition = 0;
                login.writeByte(reconnecting ? ProtocolConstants.RECONNECTION_OPCODE : ProtocolConstants.NEW_CONNECTION_OPCODE);
                login.writeByte(outgoing.currentPosition + ProtocolConstants.LOGIN_BLOCK_ENCRYPTION_KEY); // size of the login block
                login.writeByte(ProtocolConstants.MAGIC_NUMBER_OPCODE);
                login.putShort(ProtocolConstants.PROTOCOL_REVISION);
                /*
                 * Client version
				 *
				 * 0 indicates low memory : 1 indicates high memory.
				 */
                login.writeByte(lowMemory ? 1 : 0); // client version
                /*
                 * Crc keys used for the update server.
				 */
                for (int index = 0; index < 9; index++) {
                    login.putInt(archiveCRCs[index]);
                }

                login.writeBytes(outgoing.payload, outgoing.currentPosition, 0);
                outgoing.encryption = new ISAACCipher(seed);
                for (int index = 0; index < 4; index++) {
                    seed[index] += 50;
                }
                socketStream.queueBytes(login.currentPosition, login.payload);
                response = socketStream.read();
            }
            if (response == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception _ex) {
                }
                login(name, password, reconnecting);
                return;
            }
            if (response == 2) {
                clientSetting.save();
                myPrivilege = socketStream.read();

                for (int i = 0; i < chatIcons.length; i++) {
                    chatIcons[i] = socketStream.read();
                    chatIcons[i] = chatIcons[i] == 255 ? -1 : chatIcons[i];
                }

                flagged = socketStream.read() == 1;
                aLong1220 = 0L;
                duplicateClickCount = 0;
                mouseDetection.coordsIndex = 0;
                super.awtFocus = true;
                screenFocused = true;
                loggedIn = true;
                outgoing.currentPosition = 0;
                inStream.currentPosition = 0;
                opcode = -1;
                lastOpcode = -1;
                secondLastOpcode = -1;
                thirdLastOpcode = -1;
                packetSize = 0;
                timeoutCounter = 0;
                systemUpdateTime = 0;
                anInt1011 = 0;
                hintIconDrawType = 0;
                menuActionRow = 0;
                menuOpen = false;
                super.idleTime = 0;
                for (int index = 0; index < 100; index++) {
                    chatMessages[index] = null;
                }
                itemSelected = 0;
                spellSelected = 0;
                loadingStage = 0;
                trackCount = 0;
                setNorth();
                minimapState = 0;
                anInt985 = -1;
                destX = 0;
                destY = 0;
                playerCount = 0;
                npcCount = 0;
                for (int index = 0; index < maxPlayers; index++) {
                    players[index] = null;
                    playerSynchronizationBuffers[index] = null;
                }
                for (int index = 0; index < 16384; index++) {
                    npcs[index] = null;
                }
                localPlayer = players[internalLocalPlayerIndex] = new Player();
                projectiles.clear();
                incompleteAnimables.clear();
                for (int z = 0; z < 4; z++) {
                    for (int x = 0; x < 104; x++) {
                        for (int y = 0; y < 104; y++) {
                            groundItems[z][x][y] = null;
                        }
                    }
                }
                spawns = new Deque();
                fullscreenInterfaceID = -1;
                friendServerStatus = 0;
                friendsCount = 0;
                dialogueId = -1;
                backDialogueId = -1;
                openInterfaceId = -1;
                overlayInterfaceId = -1;
                minimapInterfaceId = -1;
                openWalkableInterface = -1;
                runInterfaceId = -1;
                for (RSComponent rsComponent : tooltopComponents) {
                    rsComponent.tooltips = null;
                }
                tooltopComponents.clear();
                continuedDialogue = false;
                currentTabId = 3;
                inputState = 0;
                menuOpen = false;
                messagePromptRaised = false;
                clickToContinueString = null;
                multiCombat = 0;
                flashingSidebarId = -1;
                maleCharacter = true;
                changeCharacterGender();
                for (int index = 0; index < 5; index++) {
                    characterDesignColours[index] = 0;
                }
                for (int index = 0; index < 5; index++) {
                    atPlayerActions[index] = null;
                    atPlayerArray[index] = false;
                }
                anInt1175 = 0;
                anInt1134 = 0;
                anInt986 = 0;
                anInt1288 = 0;
                regionTimer = 0;
                anInt1155 = 0;
                anInt1226 = 0;
                grandExchange.reset();
                sendConfiguration(429, 1);
                this.stopMidi();
                setupGameplayScreen();
                setCursor(GameCursor.NONE);
                loggingIn = false;
                return;
            }
            loggingIn = false;
            if (response == -1) {
                if (copyResponse == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception _ex) {
                        }
                        loginFailures++;
                        login(name, password, reconnecting);
                        return;
                    } else {
                        loginMessage1 = "No response from loginserver";
                        loginMessage2 = "Please wait 1 minute and try again.";
                        loginMessage3 = "";
                        return;
                    }
                } else {
                    loginMessage1 = "No response from loginserver";
                    loginMessage2 = "Please wait 1 minute and try again.";
                    loginMessage3 = "";
                    return;
                }
            } else {
                Response res = Response.getResponse(response);
                res.handle(this);
                return;
            }
        } catch (IOException _ex) {
            loginMessage1 = "";
            loggingIn = false;
            //no need for stacktrace, cannot open socket
            //_ex.printStackTrace();
        }
        loggingIn = false;
        loginMessage1 = "";
        loginMessage2 = "Error connecting to server.";
        loginMessage3 = "";
    }

    public boolean doWalkTo(int clickType, int objectRotation, int objectSizeY, int objectType, int fromLocalY, int objectSizeX,
                            int objectFace, int toLocalY, int fromLocalX, boolean arbitrary, int toLocalX) {
        byte byte0 = 104;
        byte byte1 = 104;
        for (int l2 = 0; l2 < byte0; l2++) {
            for (int i3 = 0; i3 < byte1; i3++) {
                anIntArrayArray901[l2][i3] = 0;
                anIntArrayArray825[l2][i3] = 0x5f5e0ff;
            }
        }

        if (myPrivilege >= 2 && super.ctrlShiftPressed) {
            processCommand("telecs " + toLocalX + " " + toLocalY);
        }
        int j3 = fromLocalX;
        int k3 = fromLocalY;
        anIntArrayArray901[fromLocalX][fromLocalY] = 99;
        anIntArrayArray825[fromLocalX][fromLocalY] = 0;
        int l3 = 0;
        int i4 = 0;
        bigX[l3] = fromLocalX;
        bigY[l3++] = fromLocalY;
        boolean flag1 = false;
        int j4 = bigX.length;
        int[][] collisionAdjacenies = collisionMaps[plane].adjacencies;
        while (i4 != l3) {
            j3 = bigX[i4];
            k3 = bigY[i4];
            i4 = (i4 + 1) % j4;
            if (j3 == toLocalX && k3 == toLocalY) {
                flag1 = true;
                break;
            }
            if (objectType != 0) {
                if ((objectType < 5 || objectType == 10)
                        && collisionMaps[plane].method219(toLocalX, j3, k3, objectRotation,
                        objectType - 1, toLocalY)) {
                    flag1 = true;
                    break;
                }
                if (objectType < 10
                        && collisionMaps[plane].method220(toLocalX, toLocalY, k3, objectType - 1,
                        objectRotation, j3)) {
                    flag1 = true;
                    break;
                }
            }
            if (objectSizeX != 0
                    && objectSizeY != 0
                    && collisionMaps[plane]
                    .method221(toLocalY, toLocalX, j3, objectSizeY, objectFace, objectSizeX, k3)) {
                flag1 = true;
                break;
            }
            int l4 = anIntArrayArray825[j3][k3] + 1;
            if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0
                    && (collisionAdjacenies[j3 - 1][k3] & 0x1280108) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3] = 2;
                anIntArrayArray825[j3 - 1][k3] = l4;
            }
            if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0
                    && (collisionAdjacenies[j3 + 1][k3] & 0x1280180) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3] = 8;
                anIntArrayArray825[j3 + 1][k3] = l4;
            }
            if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0
                    && (collisionAdjacenies[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 - 1] = 1;
                anIntArrayArray825[j3][k3 - 1] = l4;
            }
            if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0
                    && (collisionAdjacenies[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 + 1] = 4;
                anIntArrayArray825[j3][k3 + 1] = l4;
            }
            if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0
                    && (collisionAdjacenies[j3 - 1][k3 - 1] & 0x128010e) == 0
                    && (collisionAdjacenies[j3 - 1][k3] & 0x1280108) == 0
                    && (collisionAdjacenies[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 - 1] = 3;
                anIntArrayArray825[j3 - 1][k3 - 1] = l4;
            }
            if (j3 < byte0 - 1 && k3 > 0
                    && anIntArrayArray901[j3 + 1][k3 - 1] == 0
                    && (collisionAdjacenies[j3 + 1][k3 - 1] & 0x1280183) == 0
                    && (collisionAdjacenies[j3 + 1][k3] & 0x1280180) == 0
                    && (collisionAdjacenies[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 - 1] = 9;
                anIntArrayArray825[j3 + 1][k3 - 1] = l4;
            }
            if (j3 > 0 && k3 < byte1 - 1
                    && anIntArrayArray901[j3 - 1][k3 + 1] == 0
                    && (collisionAdjacenies[j3 - 1][k3 + 1] & 0x1280138) == 0
                    && (collisionAdjacenies[j3 - 1][k3] & 0x1280108) == 0
                    && (collisionAdjacenies[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 + 1] = 6;
                anIntArrayArray825[j3 - 1][k3 + 1] = l4;
            }
            if (j3 < byte0 - 1 && k3 < byte1 - 1
                    && anIntArrayArray901[j3 + 1][k3 + 1] == 0
                    && (collisionAdjacenies[j3 + 1][k3 + 1] & 0x12801e0) == 0
                    && (collisionAdjacenies[j3 + 1][k3] & 0x1280180) == 0
                    && (collisionAdjacenies[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 + 1] = 12;
                anIntArrayArray825[j3 + 1][k3 + 1] = l4;
            }
        }
        anInt1264 = 0;
        if (!flag1) {
            if (arbitrary) {
                int i5 = 100;
                for (int k5 = 1; k5 < 2; k5++) {
                    for (int i6 = toLocalX - k5; i6 <= toLocalX + k5; i6++) {
                        for (int l6 = toLocalY - k5; l6 <= toLocalY + k5; l6++) {
                            if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104
                                    && anIntArrayArray825[i6][l6] < i5) {
                                i5 = anIntArrayArray825[i6][l6];
                                j3 = i6;
                                k3 = l6;
                                anInt1264 = 1;
                                flag1 = true;
                            }
                        }
                    }
                    if (flag1) {
                        break;
                    }
                }
            }
            if (!flag1) {
                return false;// !isClipped();
            }
        }
        i4 = 0;
        bigX[i4] = j3;
        bigY[i4++] = k3;
        int l5;
        for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != fromLocalX || k3 != fromLocalY; j5 = anIntArrayArray901[j3][k3]) {
            if (j5 != l5) {
                l5 = j5;
                bigX[i4] = j3;
                bigY[i4++] = k3;
            }
            if ((j5 & 2) != 0) {
                j3++;
            } else if ((j5 & 8) != 0) {
                j3--;
            }
            if ((j5 & 1) != 0) {
                k3++;
            } else if ((j5 & 4) != 0) {
                k3--;
            }
        }
        if (i4 > 0) {
            int k4 = i4;
            if (k4 > 25) {
                k4 = 25;
            }
            i4--;
            int k6 = bigX[i4];
            int i7 = bigY[i4];
            anInt1288 += k4;
            if (anInt1288 >= 92) {
                outgoing.putOpcode(36);
                outgoing.putInt(0);
                anInt1288 = 0;
            }
            if (clickType == 0) {
                outgoing.putOpcode(164);
                outgoing.writeByte(k4 + k4 + 3);
            }
            if (clickType == 1) {
                outgoing.putOpcode(248);
                outgoing.writeByte(k4 + k4 + 3 + 14);
            }
            if (clickType == 2) {
                outgoing.putOpcode(98);
                outgoing.writeByte(k4 + k4 + 3);
            }
            outgoing.writeLEShortA(k6 + regionBaseX);
            destX = bigX[0];
            destY = bigY[0];
            for (int j7 = 1; j7 < k4; j7++) {
                i4--;
                outgoing.writeByte(bigX[i4] - k6);
                outgoing.writeByte(bigY[i4] - i7);
            }
            outgoing.putLEShort(i7 + regionBaseY);
            outgoing.writeNegatedByte(super.keyArray[5] != 1 ? 0 : 1);
            return true;
        }
        return clickType != 1;
    }

    private void npcUpdateMask(RSStream stream) {
        for (int j = 0; j < mobsAwaitingUpdateCount; j++) {
            int k = mobsAwaitingUpdate[j];
            NPC npc = npcs[k];
            int l = stream.getByte();
            if ((l & 0x10) != 0) {
                int i1 = stream.readLEUShort();
                if (i1 == 65535) {
                    i1 = -1;
                }
                int i2 = stream.getByte();
                if (i1 == npc.emoteAnimation && i1 != -1) {
                    int l2 = Animation.animations[i1].anInt365;
                    if (l2 == 1) {
                        npc.displayedEmoteFrames = 0;
                        npc.emoteTimeRemaining = 0;
                        npc.animationDelay = i2;
                        npc.currentAnimationLoops = 0;
                    }
                    if (l2 == 2) {
                        npc.currentAnimationLoops = 0;
                    }
                } else if (i1 == -1
                        || npc.emoteAnimation == -1
                        || Animation.animations[i1].anInt359 >= Animation.animations[npc.emoteAnimation].anInt359) {
                    npc.emoteAnimation = i1;
                    npc.displayedEmoteFrames = 0;
                    npc.emoteTimeRemaining = 0;
                    npc.animationDelay = i2;
                    npc.currentAnimationLoops = 0;
                    npc.anInt1542 = npc.smallXYIndex;
                }
            }
            if ((l & 8) != 0) {
                int damage = stream.getByteA();
                int markType = stream.getByteC();
                int icon = stream.getByte();
                npc.updateHitData(markType, damage, loopCycle, icon);
                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = stream.getByteA();
                npc.maxHealth = stream.getByte();
            }
            if ((l & 0x80) != 0) {
                npc.gfxId = stream.getShort();
                int k1 = stream.getInt();
                npc.graphicHeight = k1 >> 16;
                npc.graphicDelay = loopCycle + (k1 & 0xffff);
                npc.currentAnimation = 0;
                npc.anInt1522 = 0;
                if (npc.graphicDelay > loopCycle) {
                    npc.currentAnimation = -1;
                }
                if (npc.gfxId == 65535) {
                    npc.gfxId = -1;
                }
            }
            if ((l & 0x20) != 0) {
                npc.interactingEntity = stream.getShort();
                if (npc.interactingEntity == 65535) {
                    npc.interactingEntity = -1;
                }
            }
            if ((l & 1) != 0) {
                npc.spokenText = stream.getString();
                npc.textCycle = 100;
            }
            if ((l & 0x40) != 0) {
                int damage = stream.getByteC();
                int markType = stream.getByteS();
                int icon = stream.getByte();
                npc.updateHitData(markType, damage, loopCycle, icon);
                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = stream.getByteS();
                npc.maxHealth = stream.getByteC();
            }
            if ((l & 2) != 0) {
                npc.desc = NPCDefinition.forId(stream.readLEUShortA());
                npc.boundDim = npc.desc.size;
                npc.degreesToTurn = npc.desc.degreesToTurn;
                npc.walkAnimIndex = npc.desc.walkAnimationId;
                npc.turn180AnimIndex = npc.desc.turn180Animation;
                npc.turn90CWAnimIndex = npc.desc.turn90CWAnimation;
                npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimation;
                npc.standAnimIndex = npc.desc.standAnimation;
            }
            if ((l & 4) != 0) {
                npc.faceX = stream.readLEUShort();
                npc.faceY = stream.readLEUShort();
            }
        }
    }

    private void buildAtNPCMenu(NPCDefinition entityDef, int i, int j, int k) {
        if (openInterfaceId == 15244) {
            return;
        }
        if (menuActionRow >= 400) {
            return;
        }
        if (entityDef.childrenIDs != null) {
            entityDef = entityDef.morph();
        }
        if (entityDef == null) {
            return;
        }
        if (!entityDef.clickable) {
            return;
        }
        String s = entityDef.name;
        if (entityDef.combatLevel != 0) {
            s = s + TextConstants.getCombatDifferenceColour(localPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
        }
        if (itemSelected == 1) {
            menuActionName[menuActionRow] = "Use " + selectedItemName
                    + " with <col=FFFF00>" + s;
            menuActionId[menuActionRow] = 582;
            selectedMenuActions[menuActionRow] = i;
            firstMenuAction[menuActionRow] = k;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
            return;
        }
        if (spellSelected == 1) {
            if ((spellUsableOn & 2) == 2) {
                menuActionName[menuActionRow] = spellTooltip + " <col=FFFF00>" + s;
                menuActionId[menuActionRow] = 413;
                selectedMenuActions[menuActionRow] = i;
                firstMenuAction[menuActionRow] = k;
                secondMenuAction[menuActionRow] = j;
                menuActionRow++;
            }
        } else {
            if (entityDef.actions != null) {
                for (int l = 4; l >= 0; l--) {
                    if (entityDef.actions[l] != null
                            && !entityDef.actions[l].equalsIgnoreCase("attack")) {
                        menuActionName[menuActionRow] = entityDef.actions[l]
                                + " <col=FFFF00>" + s;
                        if (l == 0) {
                            menuActionId[menuActionRow] = 20;
                        }
                        if (l == 1) {
                            menuActionId[menuActionRow] = 412;
                        }
                        if (l == 2) {
                            menuActionId[menuActionRow] = 225;
                        }
                        if (l == 3) {
                            menuActionId[menuActionRow] = 965;
                        }
                        if (l == 4) {
                            menuActionId[menuActionRow] = 478;
                        }
                        selectedMenuActions[menuActionRow] = i;
                        firstMenuAction[menuActionRow] = k;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }
                }

            }
            if (entityDef.actions != null) {
                for (int i1 = 4; i1 >= 0; i1--) {
                    if (entityDef.actions[i1] != null
                            && entityDef.actions[i1].equalsIgnoreCase("attack")) {
                        char c = '\0';
                        if (entityDef.combatLevel > localPlayer.combatLevel) {
                            c = '\u07D0';
                        }
                        menuActionName[menuActionRow] = entityDef.actions[i1]
                                + " <col=FFFF00>" + s;
                        if (i1 == 0) {
                            menuActionId[menuActionRow] = 20 + c;
                        }
                        if (i1 == 1) {
                            menuActionId[menuActionRow] = 412 + c;
                        }
                        if (i1 == 2) {
                            menuActionId[menuActionRow] = 225 + c;
                        }
                        if (i1 == 3) {
                            menuActionId[menuActionRow] = 965 + c;
                        }
                        if (i1 == 4) {
                            menuActionId[menuActionRow] = 478 + c;
                        }
                        selectedMenuActions[menuActionRow] = i;
                        firstMenuAction[menuActionRow] = k;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }
                }

            }
            if (Constants.ENABLE_IDS
                    && (myPrivilege >= 2 && myPrivilege <= 3)) {
                menuActionName[menuActionRow] = "Examine <col=FFFF00>" + s
                        + " <col=00FF00>(<col=FFFFFF>" + entityDef.npcId + "<col=00FF00>)";
            } else {
                menuActionName[menuActionRow] = "Examine <col=FFFF00>" + s;
            }
            menuActionId[menuActionRow] = 1025;
            selectedMenuActions[menuActionRow] = i;
            firstMenuAction[menuActionRow] = k;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
        }
    }

    private void buildAtPlayerMenu(int i, int j, Player player, int k) {
        if (openInterfaceId == 15244) {
            return;
        }
        if (player == localPlayer) {
            return;
        }
        if (menuActionRow >= 400) {
            return;
        }
        String playerDetail;
        String playerName = (!player.isTitleSuffix() ? (player.getTitle() + player.name) :
                (player.name + player.getTitle()));
        if (player.skill == 0) {
            playerDetail = playerName + TextConstants.getCombatDifferenceColour(localPlayer.combatLevel,
                    player.combatLevel) + " (level-" + player.combatLevel + ((player.wilderness && player.summoningLevel > 0) ? ("+" + player.summoningLevel) : "") + ")";
        } else {
            playerDetail = playerName + " (skill-" + player.skill + ")";
        }
        if (itemSelected == 1) {
            menuActionName[menuActionRow] = "Use " + selectedItemName
                    + " with <col=FFFFFF>" + playerDetail;
            menuActionId[menuActionRow] = 491;
            selectedMenuActions[menuActionRow] = j;
            firstMenuAction[menuActionRow] = i;
            secondMenuAction[menuActionRow] = k;
            menuActionRow++;
        } else if (spellSelected == 1) {
            if ((spellUsableOn & 8) == 8) {
                menuActionName[menuActionRow] = spellTooltip + " <col=FFFFFF>" + playerDetail;
                menuActionId[menuActionRow] = 365;
                selectedMenuActions[menuActionRow] = j;
                firstMenuAction[menuActionRow] = i;
                secondMenuAction[menuActionRow] = k;
                menuActionRow++;
            }
        } else {
            for (int l = 4; l >= 0; l--) {
                if (atPlayerActions[l] != null) {
                    menuActionName[menuActionRow] = atPlayerActions[l]
                            + " <col=FFFFFF>" + playerDetail;
                    char c = '\0';
                    if (atPlayerActions[l].equalsIgnoreCase("attack")) {
                        if (player.combatLevel > localPlayer.combatLevel) {
                            c = '\u07D0';
                        }
                        if (localPlayer.team != 0 && player.team != 0) {
                            if (localPlayer.team == player.team) {
                                c = '\u07D0';
                            } else {
                                c = '\0';
                            }
                        }
                    } else if (atPlayerArray[l]) {
                        c = '\u07D0';
                    }
                    if (l == 0) {
                        menuActionId[menuActionRow] = 561 + c;
                    }
                    if (l == 1) {
                        menuActionId[menuActionRow] = 779 + c;
                    }
                    if (l == 2) {
                        menuActionId[menuActionRow] = 27 + c;
                    }
                    if (l == 3) {
                        menuActionId[menuActionRow] = 577 + c;
                    }
                    if (l == 4) {
                        menuActionId[menuActionRow] = 729 + c;
                    }
                    selectedMenuActions[menuActionRow] = j;
                    firstMenuAction[menuActionRow] = i;
                    secondMenuAction[menuActionRow] = k;
                    menuActionRow++;
                }
            }
        }
        for (int i1 = 0; i1 < menuActionRow; i1++) {
            if (menuActionId[i1] == 519) {
                menuActionName[i1] = "Walk here <col=FFFFFF>" + playerDetail;
                return;
            }
        }
    }

    private void assignOldValuesToNewRequest(SpawnedObject spawnedObject) {
        int uid = 0;
        int objectId = 0;
        int j = -1;
        int type = 0;
        int face = 0;
        if (spawnedObject.group == 0) {
            uid = scene.getWallKey(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
            objectId = scene.fetchWallNewUID(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
        }
        if (spawnedObject.group == 1) {
            uid = scene.getWallDecorationKey(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
            objectId = scene.fetchWallDecorationNewUID(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
        }
        if (spawnedObject.group == 2) {
            uid = scene.getStaticObjectKey(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
            objectId = scene.fetchObjectMeshNewUID(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
        }
        if (spawnedObject.group == 3) {
            uid = scene.getGroundDecorationUID(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
            objectId = scene.getFloorDecorationKey(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY);
        }
        if (uid != 0) {
            int i1 = scene.getIDTagForXYZ(spawnedObject.plane, spawnedObject.tileX, spawnedObject.tileY, uid);
            j = objectId;
            type = i1 & 0x1f;
            face = i1 >> 6;
        }
        spawnedObject.getPreviousId = j;
        //        spawnedObject.type = type;
        spawnedObject.previousType = type;
        spawnedObject.previousOrientation = face;
    }

    @Override
    void startUp() {
        Thread loaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (loading) {
                    try {
                        if (!overrideLoading) {
                            drawLoadingText(loadingPercentage, loadingText);
                        }
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Loader");

        loaderThread.setPriority(10);
        loaderThread.start();
        load();
        if (Constants.AUTO_LOGIN && loginUsername.length() > 0 && loginPassword.length() > 0) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            login(loginUsername, loginPassword, false);
        }
    }

    private void loadTitleSprites() {
        this.background = ImageLoader.forName("background");
        this.loadingBar = ImageLoader.forName("LOADING");
        this.loadingBack = ImageLoader.forName("LOADING_BACK");
        this.loadingTextSprite = ImageLoader.forName("LOADING_TEXT");
        this.loginBox = ImageLoader.forName("LOGIN_BOX");
        this.loginButton = ImageLoader.forName("LOGIN_BUTTON");
        this.loginButtonHoverSprite = ImageLoader.forName("LOGIN_BUTTON_HOVER");
        this.logo = ImageLoader.forName("LOGO");
        this.usernameInputBox = ImageLoader.forName("USERNAME_INPUT");
        this.passwordInputBox = ImageLoader.forName("PASSWORD_INPUT");
        this.loginMessage = ImageLoader.forName("LOGIN_MESSAGE");
        this.rememberOff = ImageLoader.forName("REMEMBER_OFF");
        this.rememberOn = ImageLoader.forName("REMEMBER_ON");
        this.selectAWorld = ImageLoader.forName("select_a_world");
        this.worldHeader = ImageLoader.forName("world_header");
        this.worldSwitch = ImageLoader.forName("WORLD_SWITCH");
        this.worldAscendGreen = ImageLoader.forName("WORLD_ASCEND_GREEN");
        this.worldDescendGreen = ImageLoader.forName("WORLD_DESCEND_GREEN");
        this.worldAscendRed = ImageLoader.forName("WORLD_ASCEND_RED");
        this.worldDescendRed = ImageLoader.forName("WORLD_DESCEND_RED");
        for (int index = 0; index < worldCountrySprites.length; index++) {
            this.worldCountrySprites[index] = ImageLoader.forName("WORLD_COUNTRY_" + index);
        }
        this.worldSelectFree = ImageLoader.forName("WORLD_SELECT_F2P");
        this.worldSelectFreeHover = ImageLoader.forName("WORLD_SELECT_F2P_HOVER");
        this.worldSelectMembers = ImageLoader.forName("WORLD_SELECT_P2P");
        this.worldSelectMembersHover = ImageLoader.forName("WORLD_SELECT_P2P_HOVER");
    }

    private void loadSprites(CacheArchive mediaArchive) {
        grandExchange = new GrandExchange(this, ImageLoader.forName("GE_OFFER_COMPLETE"), ImageLoader.forName("GE_OFFER_PENDING"), ImageLoader.forName("GE_OFFER_ABORT"), ImageLoader.forName("GE_OFFER_COMPLETE_LONG"), ImageLoader.forName("GE_OFFER_PENDING_LONG"), ImageLoader.forName("GE_OFFER_ABORT_LONG"), ImageLoader.forName("GE_OFFER_COMPLETE_SHORT"), ImageLoader.forName("GE_OFFER_PENDING_SHORT"), ImageLoader.forName("GE_OFFER_ABORT_SHORT"));
        for (int skillId = 0; skillId < Skills.SKILL_COUNT; skillId++) {
            skillIcons[skillId] = ImageLoader.forName("xpdrop_" + skillId);
        }
        multiOverlay = new Sprite(mediaArchive, "overlay_multiway", 0);
        mapBack = new Background(mediaArchive, "mapback", 0);
        compass = new Sprite(mediaArchive, "compass", 0);
        for (int index = 0; index < 6; index++) {
            hitTypeIcon[index] = ImageLoader.forName("HIT_ICON_" + index);
        }
        try {
            for (int k3 = 0; k3 < 100; k3++) {
                mapScenes[k3] = new Background(mediaArchive, "mapscene", k3);
            }
        } catch (Exception e) {
        }
        try {
            for (int l3 = 0; l3 < 100; l3++) {
                mapFunctions[l3] = new Sprite(mediaArchive, "mapfunction", l3);
            }
        } catch (Exception e) {
        }
        try {
            for (int l3 = 0; l3 < 74; l3++) {
                mapFunctions2[l3] = new Sprite(mediaArchive, "mapfunctions2", l3);
            }
        } catch (Exception e) {
        }
        try {
            for (int i4 = 0; i4 < 20; i4++) {
                hitMarks[i4] = new Sprite(mediaArchive, "hitmarks", i4);
            }
        } catch (Exception e) {
        }
        hitMarks[4] = ImageLoader.forName("venom");
        try {
            for (int h1 = 0; h1 < 6; h1++) {
                headIconsHint[h1] = new Sprite(mediaArchive, "headicons_hint", h1);
            }
        } catch (Exception e) {
        }
        for (int j4 = 0; j4 < 18; j4++) {
            headIcons[j4] = new Sprite(mediaArchive, "headicons_prayer", j4);
        }

        for (int j45 = 0; j45 < 3; j45++) {
            skullIcons[j45] = new Sprite(mediaArchive, "headicons_pk", j45);
        }
        mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
        mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
        for (int k4 = 0; k4 < 8; k4++) {
            crosses[k4] = new Sprite(mediaArchive, "cross", k4);
        }
        mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
        mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
        mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
        mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
        mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
        mapDotClan = new Sprite(mediaArchive, "mapdots", 5);
        scrollbar1 = new Sprite(mediaArchive, "scrollbar", 0);
        scrollbar2 = new Sprite(mediaArchive, "scrollbar", 1);
        scrollbar3 = ImageLoader.forName("scrollbar6");
        scrollbar4 = ImageLoader.forName("scrollbar7");
        scrollbarLeft = ImageLoader.forName("scrollbar-left");
        scrollbarRight = ImageLoader.forName("scrollbar-right");
        horizontalScrollbar = new Sprite[4];
        for (int index = 0; index < horizontalScrollbar.length; index++) {
            horizontalScrollbar[index] = ImageLoader.forName("scrollbar-horizontal-" + index);
        }
        chatButtons = new Sprite(mediaArchive, "chatbutton", 0);
        chatTab = new Sprite(mediaArchive, "chatbuttons", 0);
        chatTabOn = new Sprite(mediaArchive, "chatbuttons", 1);
        chatTabHover = new Sprite(mediaArchive, "chatbuttons", 2);
        reportButton = new Sprite(mediaArchive, "chatbuttons", 3);
        this.skillMenuSelect = ImageLoader.forName("SKILL_MENU_SELECT");
        this.skillMenuSelectBottom = ImageLoader.forName("SKILL_MENU_SELECT_BOTTOM");
        this.hpEmpty = ImageLoader.forName("HP_EMPTY");
        this.hpFull = ImageLoader.forName("HP_FULL");
        this.hitDamage = ImageLoader.forName("HIT_DAMAGE");
        this.hitDefend = ImageLoader.forName("HIT_DEFEND");
        this.hitDisease = ImageLoader.forName("hit_disease");
        this.hitPoison = ImageLoader.forName("hit_poison");
        this.acceptButtonDisabled = ImageLoader.forName("ACCEPT_BUTTON_DISABLED");
        this.acceptButtonDisabledHover = ImageLoader.forName("ACCEPT_BUTTON_DISABLED_HOVER");
        this.bankNewTab = ImageLoader.forName("BANK_NEW_TAB");
        this.bankTabEmpty = ImageLoader.forName("BANK_TAB_EMPTY");
        this.bankTabEmpty = ImageLoader.forName("BANK_TAB_EMPTY");
        this.arrowUp = ImageLoader.forName("arrow_up");
        this.arrowDown = ImageLoader.forName("arrow_down");
        this.arrowLeft = ImageLoader.forName("arrow_left");
        this.arrowRight = ImageLoader.forName("arrow_right");
        this.questTabIcon = ImageLoader.forName("QUEST_TAB_ICON");
        this.achievementTabIcon = ImageLoader.forName("ACHIEVEMENT_TAB_ICON");
        this.informationTabIcon = ImageLoader.forName("INFORMATION_TAB_ICON");

        newScrollbar = new Sprite[6];
        for (int index = 0; index < 6; index++) {
            newScrollbar[index] = ImageLoader.forName("new_scroll_" + index);
        }
        for (Gameframe gameframe1 : GAMEFRAME_MAP.values()) {
            gameframe1.registerSprites(mediaArchive);
        }
        summoning.setSpecialMoveBars(new Sprite[]{ImageLoader.forName("special-move-bar-full"), ImageLoader.forName("special-move-bar-empty")});
    }

    private void load() {
        logger.log(Level.INFO, "Starting up" + (Constants.DEBUG_MODE ? " debug:true" : ""));
        logger.log(Level.INFO, "Cache path: {0}.", Constants.getCachePath(true));
        setLoadingText(20, "Starting up");
        loadTitleSprites();
        loadingSprite[0] = Toolkit.getDefaultToolkit().createImage(this.background.spriteData);
        loadingSprite[1] = Toolkit.getDefaultToolkit().createImage(this.loadingBack.spriteData);
        loadingSprite[2] = Toolkit.getDefaultToolkit().createImage(this.loadingBar.spriteData);
        loadingSprite[3] = Toolkit.getDefaultToolkit().createImage(this.loadingTextSprite.spriteData);
        //loadingSprite[4] = Toolkit.getDefaultToolkit().createImage(this.logo.spriteData);
        super.graphics.drawImage(loadingSprite[0], 0, 0, null);
        //super.graphics.drawImage(loadingSprite[4], 80, 43, null);
        super.graphics.drawImage(loadingSprite[1], 254, 241, null);
        if (mainFrame != null) {
            mainFrame.setClientIcon();
        }
        if (SignLink.cache_dat != null) {
            for (int i = 0; i < Constants.CACHE_INDICES; i++) {
                indices[i] = new Index(SignLink.cache_dat, SignLink.indices[i], i + 1);
            }
        }
       //CacheUtil.repackCacheIndexes();

        try {
            requestWorlds();
        } catch (Exception e) {
            worldError = true;
            loading = false;
            System.out.println("error_world_connect_load");
            return;
        }
        try {
            if (worldError) {
                return;
            }
            if (Constants.JAGGRAB_ENABLED) {
                requestCrcs();
            }
            titleCacheArchive = createArchive(1, "title screen", "title", archiveCRCs[1], 25);

            smallArialFont = new GameFont(false, "login_arial_small", titleCacheArchive);
            forumLoginFont = new GameFont(false, "login_forum", titleCacheArchive);
            bigArialFont = new GameFont(false, "login_arial", titleCacheArchive);

            smallFont = new GameFont(false, "p11_full", titleCacheArchive);
            regularFont = new GameFont(false, "p12_full", titleCacheArchive);
            boldFont = new GameFont(false, "b12_full", titleCacheArchive);
            fancyFont = new GameFont(true, "q8_full", titleCacheArchive);
            rsFancyFont = new GameFont(true, "o11_full", titleCacheArchive);
            setupLoginScreen();
            CacheArchive configArchive = createArchive(2, "config", "config", archiveCRCs[2], 30);
            CacheArchive interfaceArchive = createArchive(3, "interface", "interface", archiveCRCs[3], 35);
            CacheArchive mediaArchive = createArchive(4, "2d graphics", "media", archiveCRCs[4], 40);
            this.mediaCacheArchive = mediaArchive;
            CacheArchive textureArchive = createArchive(6, "textures", "textures", archiveCRCs[6], 45);
            CacheArchive chatArchive = createArchive(7, "chat system", "wordenc", archiveCRCs[7], 50);
            createArchive(8, "sound effects", "sounds", archiveCRCs[8], 55);
            tileFlags = new byte[4][104][104];
            tileHeights = new int[4][105][105];
            scene = new SceneGraph(tileHeights);
            for (int j = 0; j < 4; j++) {
                collisionMaps[j] = new CollisionMap();
            }

            minimapImage = new Sprite(512, 512);
            CacheArchive versionList = createArchive(5, "update list", "versionlist", archiveCRCs[5], 60);
            setLoadingText(60, "Connecting to update server");
            worldDefinition = WorldDefinition.getWorldList().get(0);
            resourceProvider = new ResourceProvider();
            resourceProvider.initialize(versionList, this);
            SequenceFrame.method528(9351);
            Model.init(resourceProvider.getModelCount(), resourceProvider);
            setLoadingText(80, "Unpacking media");
            clientSetting.load();
            CacheArchive soundArchive = createArchive(8, "sound effects", "sounds", archiveCRCs[8], 55);
            byte[] soundArchiveEntry = soundArchive.getEntry("sounds.dat");
            RSStream stream = new RSStream(soundArchiveEntry);
            Track.unpack(stream);
            if (Constants.DEBUG_MODE) {
                CacheUtil.repackCacheIndexes();
                CacheUtil.unpackCacheIndexes();
            }
            loadSprites(mediaArchive);
            Sprite[] chatImages = new Sprite[33];
            for (int l4 = 0; l4 < chatImages.length; l4++) {
                chatImages[l4] = new Sprite(mediaArchive, "chat_icons", l4);
            }
            Sprite sprite = new Sprite(mediaArchive, "screenframe", 0);
            leftFrame = new ImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            sprite = new Sprite(mediaArchive, "screenframe", 1);
            topFrame = new ImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            sprite = new Sprite(mediaArchive, "screenframe", 2);
            rightFrame = new ImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            int i5 = (int) (Math.random() * 21D) - 10;
            int j5 = (int) (Math.random() * 21D) - 10;
            int k5 = (int) (Math.random() * 21D) - 10;
            int l5 = (int) (Math.random() * 41D) - 20;
            for (int index = 0; index < 100; index++) {
                if (mapFunctions[index] != null) {
                    mapFunctions[index].method344(i5 + l5, j5 + l5, k5 + l5);
                }
                if (mapFunctions2[index] != null) {
                    mapFunctions2[index].method344(i5 + l5, j5 + l5, k5 + l5);
                }
                if (mapScenes[index] != null) {
                    mapScenes[index].method360(i5 + l5, j5 + l5, k5 + l5);
                }
            }
            try {
                setLoadingText(83, "Unpacking textures");
                TextureLoader317.unpackTextures(textureArchive);
                Rasterizer.calculatePalette(0.80000000000000004D);
                TextureLoader317.resetTextures();
                setLoadingText(86, "Unpacking config");
                Animation.unpackConfig(configArchive);
                ObjectDefinition.unpackConfig(configArchive);
                Floor.unpackConfig(configArchive);
                FloorOverlay.unpackConfig(configArchive);
                ItemDefinition.unpackConfig(configArchive);
                NPCDefinition.unpackConfig(configArchive);
                IdentityKit.unpackConfig(configArchive);
                SpotAnimation.unpackConfig(configArchive);
                VariableParameter.unpackConfig(configArchive);
                VariableBits.unpackConfig(configArchive);
               // TextureDefinition.unpackConfig(configArchive);
                OSRSItemDefinition.unpackConfig(configArchive);
                OSRSNPCDefinition.unpackConfig(configArchive);
                OSRSObjectDefinition.unpackConfig(configArchive);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            for (int i = 0; i < ItemDefinition.getItemCount(); i++) {
//                if (ItemDefinition.forId(i) != null && ItemDefinition.forId(i).interfaceModelId == 19219) {
//                    System.out.println(i + ": " + ItemDefinition.forId(i).osrs);
//                }
//            }
//            for (int i = 0; i < ObjectDefinition.getObjectCount(); i++) {
//                if (ObjectDefinition.forId(i) != null && ObjectDefinition.forId(i).modelIds != null) {
//                    for (int modelid : ObjectDefinition.forId(i).modelIds) {
//                        if (modelid == 19219) {
//                            System.out.println("OBJ: " + i);
//                        }
//                    }
//                }
//            }
//            for (int i = 0; i < NPCDefinition.getNpcCount(); i++) {
//                if (NPCDefinition.forId(i) != null && NPCDefinition.forId(i).modelIds != null) {
//                    for (int modelid : NPCDefinition.forId(i).modelIds) {
//                        if (modelid == 19219) {
//                            System.out.println("NPC: " + i);
//                        }
//                    }
//                }
//            }
            TextureLoader667.initTextures(1419, resourceProvider);
            ItemDefinition.isMembers = isMembers;
            setLoadingText(95, "Unpacking interfaces");
            GameFont[] rsFonts = {smallFont, regularFont, boldFont, fancyFont, rsFancyFont};
            for (GameFont rsFont : rsFonts) {
                rsFont.unpackImages(chatImages);
            }
            RSComponent.parse(interfaceArchive, rsFonts, mediaArchive);
            setLoadingText(100, "Preparing game engine");
            // RSComponent.save();
            for (int j6 = 0; j6 < 33; j6++) {
                int k6 = 999;
                int i7 = 0;
                for (int k7 = 0; k7 < 34; k7++) {
                    if (mapBack.aByteArray1450[k7 + j6 * mapBack.anInt1452] == 0) {
                        if (k6 == 999) {
                            k6 = k7;
                        }
                        continue;
                    }
                    if (k6 == 999) {
                        continue;
                    }
                    i7 = k7;
                    break;
                }
                anIntArray968[j6] = k6;
                anIntArray1057[j6] = i7 - k6;
            }
            for (int l6 = 1; l6 < 153; l6++) {
                int j7 = 999;
                int l7 = 0;
                for (int j8 = 24; j8 < 177; j8++) {
                    if (mapBack.aByteArray1450[j8 + l6 * mapBack.anInt1452] == 0
                            && (j8 > 34 || l6 > 34)) {
                        if (j7 == 999) {
                            j7 = j8;
                        }
                        continue;
                    }
                    if (j7 == 999) {
                        continue;
                    }
                    l7 = j8;
                    break;
                }
                minimapLeft[l6 - 1] = j7 - 24;
                minimapLineWidth[l6 - 1] = l7 - j7;
            }
            gameframe.updateGameArea();
            MessageCensor.load(chatArchive);
            mouseDetection = new MouseDetection(this);
            startRunnable(mouseDetection, 10);
            SceneObject.clientInstance = this;
            ObjectDefinition.clientInstance = this;
            OSRSObjectDefinition.clientInstance = this;
            NPCDefinition.clientInstance = this;
            loading = false;
            loadTime = System.currentTimeMillis() + 3000;
            worldDefinition = WorldDefinition.forId(Constants.DEFAULT_WORLD_ID);
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            loading = false;
            logger.log(Level.SEVERE, "Failed loading {0} {1}.", new Object[]{loadingText, loadingPercentage});
        }
        loadingError = true;
    }

    private void updateOtherPlayerMovement(RSStream stream, int i) {
        while (stream.bitPosition + 10 < i * 8) {
            int j = stream.readBits(11);
            if (j == 2047) {
                break;
            }
            if (players[j] == null) {
                players[j] = new Player();
                if (playerSynchronizationBuffers[j] != null) {
                    players[j].updatePlayer(playerSynchronizationBuffers[j]);
                }
            }
            playerIndices[playerCount++] = j;
            Player player = players[j];
            player.anInt1537 = loopCycle;
            int k = stream.readBits(1);
            if (k == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j;
            }
            int l = stream.readBits(1);
            int i1 = stream.readBits(5);
            if (i1 > 15) {
                i1 -= 32;
            }
            int j1 = stream.readBits(5);
            if (j1 > 15) {
                j1 -= 32;
            }
            player.setPos(localPlayer.pathX[0] + j1, localPlayer.pathY[0] + i1,
                    l == 1);
        }
        stream.finishBitAccess();
    }

    public boolean inCircle(int circleX, int circleY, int clickX, int clickY,
                            int radius) {
        return java.lang.Math.pow((circleX + radius - clickX), 2)
                + java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math
                .pow(radius, 2);
    }

    public String interfaceIntToString(int number) {
        if (number < 0x3b9ac9ff) {
            return new DecimalFormat("#,###,##0").format(number);
        } else {
            return "*";
        }
    }

    private void showErrorScreen() {
        Graphics g = getGameComponent().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 765, 503);
        method4(1);
        if (loadingError) {
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occurred whilst loading "
                    + Constants.CLIENT_NAME, 30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString(
                    "1: Try closing ALL open web-browser windows, and reloading",
                    30, k);
            k += 30;
            g.drawString(
                    "2: Try clearing your web-browsers cache from tools->internet options",
                    30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString(
                    "5: Try selecting a different version of Java from the play-game menu",
                    30, k);
        }
        if (genericLoadingError) {
            g.setFont(new Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play " + Constants.CLIENT_NAME
                    + " make sure you play from", 50, 100);
            g.drawString("http://Gielinor.org/", 50, 150);
        }
        if (rsAlreadyLoaded) {
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString("Error a copy of " + Constants.CLIENT_NAME
                    + " already appears to be loaded", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString(
                    "1: Try closing ALL open web-browser windows, and reloading",
                    30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    public URL getCodeBase() {
        try {
            return new URL(Constants.SERVER_ADDRESS + ":" + (80 + portOffset));
        } catch (Exception _ex) {
        }
        return null;
    }

    private void forceNPCUpdateBlock() {
        for (int j = 0; j < npcCount; j++) {
            int k = npcIndices[j];
            NPC npc = npcs[k];
            if (npc != null) {
                entityUpdateBlock(npc);
            }
        }
    }

    private void entityUpdateBlock(Entity entity) {
        if (entity.x < 128 || entity.y < 128 || entity.x >= 13184
                || entity.y >= 13184) {
            entity.emoteAnimation = -1;
            entity.gfxId = -1;
            entity.startForceMovement = 0;
            entity.endForceMovement = 0;
            entity.x = entity.pathX[0] * 128 + entity.boundDim * 64;
            entity.y = entity.pathY[0] * 128 + entity.boundDim * 64;
            entity.resetPath();
        }
        if (entity == localPlayer
                && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
            entity.emoteAnimation = -1;
            entity.gfxId = -1;
            entity.startForceMovement = 0;
            entity.endForceMovement = 0;
            entity.x = entity.pathX[0] * 128 + entity.boundDim * 64;
            entity.y = entity.pathY[0] * 128 + entity.boundDim * 64;
            entity.resetPath();
        }
        if (entity.startForceMovement > loopCycle) {
            refreshEntityPosition(entity);
        } else if (entity.endForceMovement >= loopCycle) {
            refreshEntityFaceDirection(entity);
        } else {
            getDegreesToTurn(entity);
        }
        appendFocusDestination(entity);
        appendEmote(entity);
    }

    private void refreshEntityPosition(Entity entity) {
        int i = entity.startForceMovement - loopCycle;
        int j = entity.initialX * 128 + entity.boundDim * 64;
        int k = entity.initialY * 128 + entity.boundDim * 64;
        entity.x += (j - entity.x) / i;
        entity.y += (k - entity.y) / i;
        entity.anInt1503 = 0;
        if (entity.direction == 0) {
            entity.turnDirection = 1024;
        }
        if (entity.direction == 1) {
            entity.turnDirection = 1536;
        }
        if (entity.direction == 2) {
            entity.turnDirection = 0;
        }
        if (entity.direction == 3) {
            entity.turnDirection = 512;
        }
    }

    private void refreshEntityFaceDirection(Entity entity) {
        if (entity.endForceMovement == loopCycle
                || entity.emoteAnimation == -1
                || entity.animationDelay != 0
                || entity.emoteTimeRemaining + 1 > Animation.animations[entity.emoteAnimation]
                .fetchAnimationLength(entity.displayedEmoteFrames)) {
            int i = entity.endForceMovement - entity.startForceMovement;
            int j = loopCycle - entity.startForceMovement;
            int k = entity.initialX * 128 + entity.boundDim * 64;
            int l = entity.initialY * 128 + entity.boundDim * 64;
            int i1 = entity.destinationX * 128 + entity.boundDim * 64;
            int j1 = entity.destinationY * 128 + entity.boundDim * 64;
            entity.x = (k * (i - j) + i1 * j) / i;
            entity.y = (l * (i - j) + j1 * j) / i;
        }
        entity.anInt1503 = 0;
        if (entity.direction == 0) {
            entity.turnDirection = 1024;
        }
        if (entity.direction == 1) {
            entity.turnDirection = 1536;
        }
        if (entity.direction == 2) {
            entity.turnDirection = 0;
        }
        if (entity.direction == 3) {
            entity.turnDirection = 512;
        }
        entity.anInt1552 = entity.turnDirection;
    }

    private void getDegreesToTurn(Entity entity) {
        entity.movementAnimation = entity.standAnimIndex;
        if (entity.smallXYIndex == 0) {
            entity.anInt1503 = 0;
            return;
        }
        if (entity.emoteAnimation != -1 && entity.animationDelay == 0) {
            Animation animation = Animation.animations[entity.emoteAnimation];
            if (entity.anInt1542 > 0 && animation.anInt363 == 0) {
                entity.anInt1503++;
                return;
            }
            if (entity.anInt1542 <= 0 && animation.anInt364 == 0) {
                entity.anInt1503++;
                return;
            }
        }
        int i = entity.x;
        int j = entity.y;
        int k = entity.pathX[entity.smallXYIndex - 1] * 128 + entity.boundDim
                * 64;
        int l = entity.pathY[entity.smallXYIndex - 1] * 128 + entity.boundDim
                * 64;
        if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
            entity.x = k;
            entity.y = l;
            return;
        }
        if (i < k) {
            if (j < l) {
                entity.turnDirection = 1280;
            } else if (j > l) {
                entity.turnDirection = 1792;
            } else {
                entity.turnDirection = 1536;
            }
        } else if (i > k) {
            if (j < l) {
                entity.turnDirection = 768;
            } else if (j > l) {
                entity.turnDirection = 256;
            } else {
                entity.turnDirection = 512;
            }
        } else if (j < l) {
            entity.turnDirection = 1024;
        } else {
            entity.turnDirection = 0;
        }
        int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if (i1 > 1024) {
            i1 -= 2048;
        }
        int j1 = entity.turn180AnimIndex;
        if (i1 >= -256 && i1 <= 256) {
            j1 = entity.walkAnimIndex;
        } else if (i1 >= 256 && i1 < 768) {
            j1 = entity.turn90CCWAnimIndex;
        } else if (i1 >= -768 && i1 <= -256) {
            j1 = entity.turn90CWAnimIndex;
        }
        if (j1 == -1) {
            j1 = entity.walkAnimIndex;
        }
        entity.movementAnimation = j1;
        int k1 = 4;
        if (entity.anInt1552 != entity.turnDirection
                && entity.interactingEntity == -1 && entity.degreesToTurn != 0) {
            k1 = 2;
        }
        if (entity.smallXYIndex > 2) {
            k1 = 6;
        }
        if (entity.smallXYIndex > 3) {
            k1 = 8;
        }
        if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
            k1 = 8;
            entity.anInt1503--;
        }
        if (entity.pathRun[entity.smallXYIndex - 1]) {
            k1 <<= 1;
        }
        if (k1 >= 8 && entity.movementAnimation == entity.walkAnimIndex
                && entity.runAnimIndex != -1) {
            entity.movementAnimation = entity.runAnimIndex;
        }
        if (i < k) {
            entity.x += k1;
            if (entity.x > k) {
                entity.x = k;
            }
        } else if (i > k) {
            entity.x -= k1;
            if (entity.x < k) {
                entity.x = k;
            }
        }
        if (j < l) {
            entity.y += k1;
            if (entity.y > l) {
                entity.y = l;
            }
        } else if (j > l) {
            entity.y -= k1;
            if (entity.y < l) {
                entity.y = l;
            }
        }
        if (entity.x == k && entity.y == l) {
            entity.smallXYIndex--;
            if (entity.anInt1542 > 0) {
                entity.anInt1542--;
            }
        }
    }

    private void appendFocusDestination(Entity entity) {
        if (entity.degreesToTurn == 0) {
            return;
        }
        if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
            NPC npc = npcs[entity.interactingEntity];
            if (npc != null) {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if (i1 != 0 || k1 != 0) {
                    entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if (entity.interactingEntity >= 32768) {
            int j = entity.interactingEntity - 32768;
            if (j == localPlayerIndex) {
                j = internalLocalPlayerIndex;
            }
            Player player = players[j];
            if (player != null) {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if (l1 != 0 || i2 != 0) {
                    entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if ((entity.faceX != 0 || entity.faceY != 0)
                && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
            int k = entity.x - (entity.faceX - regionBaseX - regionBaseX) * 64;
            int j1 = entity.y - (entity.faceY - regionBaseY - regionBaseY) * 64;
            if (k != 0 || j1 != 0) {
                entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
            }
            entity.faceX = 0;
            entity.faceY = 0;
        }
        int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if (l != 0) {
            if (l < entity.degreesToTurn || l > 2048 - entity.degreesToTurn) {
                entity.anInt1552 = entity.turnDirection;
            } else if (l > 1024) {
                entity.anInt1552 -= entity.degreesToTurn;
            } else {
                entity.anInt1552 += entity.degreesToTurn;
            }
            entity.anInt1552 &= 0x7ff;
            if (entity.movementAnimation == entity.standAnimIndex
                    && entity.anInt1552 != entity.turnDirection) {
                if (entity.standTurnAnimIndex != -1) {
                    entity.movementAnimation = entity.standTurnAnimIndex;
                    return;
                }
                entity.movementAnimation = entity.walkAnimIndex;
            }
        }
    }

    public void appendEmote(Entity entity) {
        entity.aBoolean1541 = false;
        if (entity.movementAnimation != -1) {
            if (entity.movementAnimation > Animation.animations.length) {
                entity.movementAnimation = 0;
            }
            Animation animation = Animation.animations[entity.movementAnimation];
            entity.anInt1519++;
            if (entity.displayedMovementFrames < animation.frameCount
                    && entity.anInt1519 > animation
                    .fetchAnimationLength(entity.displayedMovementFrames)) {
                entity.anInt1519 = 1;
                entity.displayedMovementFrames++;
                entity.nextIdleAnimationFrame++;
            }
            entity.nextIdleAnimationFrame = entity.displayedMovementFrames + 1;
            if (entity.nextIdleAnimationFrame >= animation.frameCount) {
                if (entity.nextIdleAnimationFrame >= animation.frameCount) {
                    entity.nextIdleAnimationFrame = 0;
                }
            }
            if (entity.displayedMovementFrames >= animation.frameCount) {
                entity.anInt1519 = 1;
                entity.displayedMovementFrames = 0;
            }
        }
        if (entity.gfxId > SpotAnimation.cache.length) {
            entity.gfxId = -1;
        }
        if (entity.gfxId > -1) {
            if (SpotAnimation.cache[entity.gfxId] == null ||
                    SpotAnimation.cache[entity.gfxId].animationSequence == null) {
                entity.gfxId = -1;
            }
        }
        if (entity.gfxId != -1 && loopCycle >= entity.graphicDelay) {
            if (entity.currentAnimation < 0) {
                entity.currentAnimation = 0;
            }

            Animation animation_1 = SpotAnimation.cache[entity.gfxId].animationSequence;
            for (entity.anInt1522++;
                 entity.currentAnimation < animation_1.frameCount
                         && entity.anInt1522 > animation_1.fetchAnimationLength(entity.currentAnimation);
                 entity.currentAnimation++) {
                entity.anInt1522 -= animation_1
                        .fetchAnimationLength(entity.currentAnimation);
            }

            if (entity.currentAnimation >= animation_1.frameCount
                    && (entity.currentAnimation < 0 || entity.currentAnimation >= animation_1.frameCount)) {
                entity.gfxId = -1;
            }
            entity.nextGraphicsAnimationFrame = entity.currentAnimation + 1;
            if (entity.nextGraphicsAnimationFrame >= animation_1.frameCount) {
                if (entity.nextGraphicsAnimationFrame < 0
                        || entity.nextGraphicsAnimationFrame >= animation_1.frameCount) {
                    entity.gfxId = -1;
                }
            }
        }
        if (entity.emoteAnimation != -1 && entity.animationDelay <= 1) {
            if (entity.emoteAnimation >= Animation.animations.length) {
                entity.emoteAnimation = -1;
            }
            Animation animation_2 = Animation.animations[entity.emoteAnimation];
            if (animation_2 == null) {
                if (Constants.DEBUG_MODE) {
                    logger.log(Level.WARNING, "Invalid entity emote animation: {0}.", entity.emoteAnimation);
                }
                return;
            }
            if (animation_2.anInt363 == 1 && entity.anInt1542 > 0
                    && entity.startForceMovement <= loopCycle
                    && entity.endForceMovement < loopCycle) {
                entity.animationDelay = 1;
                return;
            }
        }
        if (entity.emoteAnimation != -1 && entity.animationDelay == 0) {
            Animation animation_3 = Animation.animations[entity.emoteAnimation];
            for (entity.emoteTimeRemaining++; entity.displayedEmoteFrames < animation_3.frameCount
                    && entity.emoteTimeRemaining > animation_3
                    .fetchAnimationLength(entity.displayedEmoteFrames); entity.displayedEmoteFrames++) {
                entity.emoteTimeRemaining -= animation_3
                        .fetchAnimationLength(entity.displayedEmoteFrames);
            }

            if (entity.displayedEmoteFrames >= animation_3.frameCount) {
                entity.displayedEmoteFrames -= animation_3.loopDelay;
                entity.currentAnimationLoops++;
                if (entity.currentAnimationLoops >= animation_3.anInt362) {
                    entity.emoteAnimation = -1;
                }
                if (entity.displayedEmoteFrames < 0
                        || entity.displayedEmoteFrames >= animation_3.frameCount) {
                    entity.emoteAnimation = -1;
                }
            }
            entity.nextAnimationFrame = entity.displayedEmoteFrames + 1;
            if (entity.nextAnimationFrame >= animation_3.frameCount) {
                if (entity.currentAnimationLoops >= animation_3.anInt362) {
                    entity.nextAnimationFrame = entity.displayedEmoteFrames + 1;
                }
                if (entity.nextAnimationFrame < 0
                        || entity.nextAnimationFrame >= animation_3.frameCount) {
                    entity.nextAnimationFrame = entity.displayedEmoteFrames;
                }
            }
            entity.aBoolean1541 = animation_3.aBoolean358;
        }
        if (entity.animationDelay > 0) {
            entity.animationDelay--;
        }
    }

    private void drawGameScreen() {
        if (fullscreenInterfaceID != -1
                && (loadingStage == 2 || super.fullGameScreen != null)) {
            if (loadingStage == 2) {
                animateRSInterface(cycleTimer, fullscreenInterfaceID);
                if (openInterfaceId != -1) {
                    animateRSInterface(cycleTimer, openInterfaceId);
                }
                cycleTimer = 0;
                resetAllImageProducers();
                super.fullGameScreen.initDrawingArea();
                Rasterizer.lineOffsets = fullScreenTextureArray;
                Raster.clear();
                welcomeScreenRaised = true;
                if (openInterfaceId != -1) {
                    RSComponent rsComponent1 = RSComponent.forId(openInterfaceId);
                    if (rsComponent1.width == 512 && rsComponent1.height == 334 && rsComponent1.type == 0) {
                        rsComponent1.width = 765;
                        rsComponent1.height = 503;
                    }
                    componentDrawing.drawComponent(rsComponent1, 0, 8, 0, 0);
                }
                RSComponent rsComponent = RSComponent.forId(fullscreenInterfaceID);
                if (rsComponent.width == 512 && rsComponent.height == 334 && rsComponent.type == 0) {
                    rsComponent.width = 765;
                    rsComponent.height = 503;
                }
                componentDrawing.drawComponent(rsComponent, 0, 8, 0, 0);
                if (!menuOpen) {
                    processRightClick();
                    drawTooltip();
                } else {
                    drawMenu(frameMode == ScreenMode.FIXED ? 4 : 0, frameMode == ScreenMode.FIXED ? 4 : 0);
                }
            }
            drawCount++;
            super.fullGameScreen.drawGraphics(0, super.graphics, 0);
            return;
        } else {
            if (drawCount != 0) {
                setupGameplayScreen();
            }
        }
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            if (frameMode == ScreenMode.FIXED) {
                topFrame.drawGraphics(0, super.graphics, 0);
                leftFrame.drawGraphics(4, super.graphics, 0);
                if (gameframe.drawRightFrame()) {
                    rightFrame.drawGraphics(3, super.graphics, 516);
                }
            }
            inputTaken = true;
            tabAreaAltered = true;
            if (loadingStage != 2) {
                if (frameMode == ScreenMode.FIXED) {
                    if (gameframe.getGameScreenImageProducer() != null) { // TODO GAMEFRAME REMOVE?
                        gameframe.getGameScreenImageProducer().drawGraphics(isScreenMode(ScreenMode.FIXED) ? 4 : 0,
                                super.graphics, isScreenMode(ScreenMode.FIXED) ? 4 : 0);
                    }
                    if (isScreenMode(ScreenMode.FIXED)) {
                        if (gameframe.getMinimapImageProducer() != null) {
                            gameframe.getMinimapImageProducer().drawGraphics(0, super.graphics, gameframe.getMinimapProducerWidth());
                        }
                    }
                }
            }
        }
        if (overlayInterfaceId != -1) {
            animateRSInterface(cycleTimer, overlayInterfaceId);
        }
        gameframe.drawTabArea();
        if (tabInterfaceIDs[Game.currentTabId] != -1) {
            animateRSInterface(cycleTimer, tabInterfaceIDs[currentTabId]);
        }
        if (backDialogueId == -1) {
            int yPos = super.mouseY - (frameMode == ScreenMode.FIXED ? 345 : frameHeight - 158);
            if (inputState == 3) {
                if (grandExchange.scrollMax > GrandExchange.SEARCH_HEIGHT) {
                    scrollbarComponent.scrollPosition = grandExchange.scrollMax - grandExchange.scrollPosition - GrandExchange.SEARCH_HEIGHT;
                    if (super.mouseX >= 496 && super.mouseX <= 511 && super.mouseY > (frameMode == ScreenMode.FIXED ? 345 : frameHeight - 158)) {
                        Scrollbar.repositionScrollbar(this, 494, 107, super.mouseX, yPos, scrollbarComponent, 0, false, grandExchange.scrollMax, true);
                    }
                    int scrollPosition = grandExchange.scrollMax - GrandExchange.SEARCH_HEIGHT - scrollbarComponent.scrollPosition;
                    if (scrollPosition < 0) {
                        scrollPosition = 0;
                    }
                    if (scrollPosition > grandExchange.scrollMax - GrandExchange.SEARCH_HEIGHT) {
                        scrollPosition = grandExchange.scrollMax - GrandExchange.SEARCH_HEIGHT;
                    }
                    if (grandExchange.scrollPosition != scrollPosition) {
                        grandExchange.scrollPosition = scrollPosition;
                        inputTaken = true;
                    }
                }
            } else {
                scrollbarComponent.scrollPosition = chatScrollMax - chatScrollPosition - 110;
                if (super.mouseX >= 496 && super.mouseX <= 511 && super.mouseY > (frameMode == ScreenMode.FIXED ? 345 : frameHeight - 158)) {
                    Scrollbar.repositionScrollbar(this, 494, 110, super.mouseX, yPos, scrollbarComponent, 0, false, chatScrollMax, false);
                }
                int scrollPosition = chatScrollMax - 110 - scrollbarComponent.scrollPosition;
                if (scrollPosition < 0) {
                    scrollPosition = 0;
                }
                if (scrollPosition > chatScrollMax - 110) {
                    scrollPosition = chatScrollMax - 110;
                }
                if (chatScrollPosition != scrollPosition) {
                    chatScrollPosition = scrollPosition;
                    inputTaken = true;
                }
            }
        }
        if (backDialogueId != -1) {
            boolean flag2 = animateRSInterface(cycleTimer, backDialogueId);
            if (flag2) {
                inputTaken = true;
            }
        }
        if (atInventoryInterfaceType == 3) {
            inputTaken = true;
        }
        if (activeInterfaceType == 3) {
            inputTaken = true;
        }
        if (clickToContinueString != null) {
            inputTaken = true;
        }
        if (inputTaken) {
            gameframe.drawChatArea();
            inputTaken = false;
        }
        if (loadingStage == 2) {
            moveCameraWithPlayer();
            if (isScreenMode(ScreenMode.FIXED)) {
                gameframe.drawMinimapArea();
                gameframe.getMinimapImageProducer().drawGraphics(0, super.graphics, gameframe.getMinimapProducerWidth());
            }
        }
        if (flashingSidebarId != -1) {
            tabAreaAltered = true;
        }
        if (tabAreaAltered) {
            if (flashingSidebarId != -1 && flashingSidebarId == currentTabId) {
                flashingSidebarId = -1;
                outgoing.putOpcode(120);
                outgoing.writeByte(currentTabId);
            }
            tabAreaAltered = false;
            if (isScreenMode(ScreenMode.FIXED) && gameframe.drawRightFrame()) {
                rightFrame.drawGraphics(3, super.graphics, 516);
            }
            chatSettingImageProducer.initDrawingArea();
            gameframe.getGameScreenImageProducer().initDrawingArea();
        }
        cycleTimer = 0;
    }

    public boolean buildFriendsListMenu(RSComponent class9) {
        int i = class9.contentType;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801) {
                i -= 701;
            } else if (i >= 701) {
                i -= 601;
            } else if (i >= 101) {
                i -= 101;
            } else {
                i--;
            }
            menuActionName[menuActionRow] = "Remove <col=FFFFFF>" + friendsList[i];
            menuActionId[menuActionRow] = 792;
            menuActionRow++;
            menuActionName[menuActionRow] = "Message <col=FFFFFF>" + friendsList[i];
            menuActionId[menuActionRow] = 639;
            menuActionRow++;
            return true;
        }
        if (i >= 401 && i <= 500) {
            menuActionName[menuActionRow] = "Remove <col=FFFFFF>" + class9.disabledMessage;
            menuActionId[menuActionRow] = 322;
            menuActionRow++;
            return true;
        } else {
            return false;
        }
    }

    private void createStationaryGraphics() {
        SceneSpotAnim class30_sub2_sub4_sub3 = (SceneSpotAnim) incompleteAnimables
                .reverseGetFirst();
        for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (SceneSpotAnim) incompleteAnimables
                .reverseGetNext()) {
            if (class30_sub2_sub4_sub3.anInt1560 != plane
                    || class30_sub2_sub4_sub3.aBoolean1567) {
                class30_sub2_sub4_sub3.unlink();
            } else if (loopCycle >= class30_sub2_sub4_sub3.anInt1564) {
                class30_sub2_sub4_sub3.method454(cycleTimer);
                if (class30_sub2_sub4_sub3.aBoolean1567) {
                    class30_sub2_sub4_sub3.unlink();
                } else {
                    scene.addMutipleTileEntity(class30_sub2_sub4_sub3.anInt1560, 0,
                            class30_sub2_sub4_sub3.anInt1563, -1,
                            class30_sub2_sub4_sub3.anInt1562, 60,
                            class30_sub2_sub4_sub3.anInt1561,
                            class30_sub2_sub4_sub3, false);
                }
            }
        }
    }

    public void updateClanChat() {
        if (getClanChatMembers() == null) {
            resetClanChat();
            return;
        }
        // Where else do we update?
        for (int childId = 32114; childId <= 32125; childId++) {
            RSComponent.getComponentCache()[childId].tooltips = null;
            RSComponent.getComponentCache()[childId].disabledMessage = "";
        }
        for (int childId = 32114; childId <= (32114 + getClanChatMembers().length); childId++) {
            RSComponent.getComponentCache()[childId].hidden = false;
        }
        RSComponent.getComponentCache()[32105].disabledMessage = "Leave Chat";
        RSComponent.getComponentCache()[32111].disabledMessage = "Talking in: <col=ffff64>" + StringUtility.formatUsername(getClanChatName()); // Yellow?
        RSComponent.getComponentCache()[32112].disabledMessage = "Owner: <col=ffffff>" + StringUtility.formatUsername(getChatOwnerName());
        int index = 0;
        int scrollMax = 0;
        for (ClanChatMember clanChatMember : getClanChatMembers()) {
            String[] tooltips = new String[3];
            if (clanChatMember == null) {
                continue;
            }
            String name = StringUtility.formatUsername(StringUtility.decodeBase37(clanChatMember.getName()));
            RSComponent.getComponentCache()[32114 + index].disabledMessage = clanChatMember.getIcon() + name;
            RSComponent.getComponentCache()[32215 + index].disabledMessage = "";// "<col=01FF00>" + clanChatMember.getWorldString() + " " + clanChatMember.getWorldId();
            if (!localPlayer.name.equalsIgnoreCase(name)) {
                if (!isOnFriendsList(clanChatMember.getName(), true) && !isOnFriendsList(clanChatMember.getName(), false)) {
                    tooltips[2] = "Add friend " + name;
                    tooltips[1] = "Add ignore " + name;
                    if (getCurrentUserClanRights() >= getChatKickRights() && clanChatMember.getRankId() <= getCurrentUserClanRights()) {
                        tooltips[0] = "Kick user " + name;
                    }
                } else if (isOnFriendsList(clanChatMember.getName(), false)) {
                    tooltips[1] = "Remove ignore " + name;
                    if (getCurrentUserClanRights() >= getChatKickRights() && clanChatMember.getRankId() <= getCurrentUserClanRights()) {
                        tooltips[0] = "Kick user " + name;
                    }
                } else if (isOnFriendsList(clanChatMember.getName(), true)) {
                    tooltips[1] = "Remove friend " + name;
                    if (getCurrentUserClanRights() >= getChatKickRights() && clanChatMember.getRankId() <= getCurrentUserClanRights()) {
                        tooltips[0] = "Kick user " + name;
                    }
                }
                RSComponent.getComponentCache()[32114 + index].tooltips = tooltips;
                scrollMax += 14;
            }
            RSComponent.getComponentCache()[32113].scrollMax = (scrollMax < 157 ? 157 : scrollMax);
            index++;
        }
    }

    public boolean isOnFriendsList(long playerNameAsLong, boolean friend) {
        for (long name : (friend ? friendsListAsLongs : getIgnores())) {
            if (name == playerNameAsLong) {
                return true;
            }
        }
        return false;
    }

    public void resetClanChat() {
        setClanChatMembers(null);
        setChatOwnerName(null);
        setClanChatName(null);
        setClanChatSize(-1);
        setChatKickRights((byte) -1);
        setCurrentUserClanRights((byte) -1);
        RSComponent.getComponentCache()[32102].tooltip = "Join Chat";
        RSComponent.getComponentCache()[32105].disabledMessage = "Join Chat";
        RSComponent.getComponentCache()[32111].disabledMessage = "Talking in: Not in chat";
        RSComponent.getComponentCache()[32112].disabledMessage = "Owner: None";
        RSComponent.getComponentCache()[32113].scrollMax = 157;
        for (int childId = 32114; childId <= 32125; childId++) {
            RSComponent.getComponentCache()[childId].tooltips = null;
            RSComponent.getComponentCache()[childId].disabledMessage = "";
            RSComponent.getComponentCache()[childId + 100].disabledMessage = "";
        }
    }

    private void appendPlayerUpdateMask(int mask, int index, RSStream buffer,
                                        Player player) {
        if ((mask & 0x400) != 0) {
            player.initialX = buffer.getByteS();
            player.initialY = buffer.getByteS();
            player.destinationX = buffer.getByteS();
            player.destinationY = buffer.getByteS();
            player.startForceMovement = buffer.readLEUShortA() + loopCycle;
            player.endForceMovement = buffer.getShortA() + loopCycle;
            player.direction = buffer.getByteS();
            player.resetPath();
        }
        if ((mask & 0x100) != 0) {
            player.gfxId = buffer.readLEUShort();
            int info = buffer.getInt();
            player.graphicHeight = info >> 16;
            player.graphicDelay = loopCycle + (info & 0xffff);
            player.currentAnimation = 0;
            player.anInt1522 = 0;
            if (player.graphicDelay > loopCycle) {
                player.currentAnimation = -1;
            }
            if (player.gfxId == 65535) {
                player.gfxId = -1;
            }
        }
        if ((mask & 8) != 0) {
            int animation = buffer.readLEUShort();
            if (animation == 65535) {
                animation = -1;
            }
            int delay = buffer.getByteC();
            if (animation == player.emoteAnimation && animation != -1) {
                int replayMode = Animation.animations[animation].anInt365;
                if (replayMode == 1) {
                    player.displayedEmoteFrames = 0;
                    player.emoteTimeRemaining = 0;
                    player.animationDelay = delay;
                    player.currentAnimationLoops = 0;
                }
                if (replayMode == 2) {
                    player.currentAnimationLoops = 0;
                }
            } else if (animation == -1
                    || player.emoteAnimation == -1
                    || Animation.animations[animation].anInt359 >= Animation.animations[player.emoteAnimation].anInt359) {
                player.emoteAnimation = animation;
                player.displayedEmoteFrames = 0;
                player.emoteTimeRemaining = 0;
                player.animationDelay = delay;
                player.currentAnimationLoops = 0;
                player.anInt1542 = player.smallXYIndex;
            }
        }
        if ((mask & 4) != 0) {
            player.spokenText = buffer.getString();
            player.textColour = 0;
            player.textEffect = 0;
            player.textCycle = 150;
            if (player.spokenText.charAt(0) == '~') {
                player.spokenText = player.spokenText.substring(1);
                pushMessage(player.spokenText, 2, player.name, "", false);
            } else if (player == localPlayer) {
                pushMessage(player.spokenText, 2, player.name, "", false);
            }
        }
        if ((mask & 0x80) != 0) {
            int textInfo = buffer.readLEUShort();
            int privilege = buffer.getByte();
            int[] playerChatIcons = new int[8];
            for (int i = 0; i < playerChatIcons.length; i++) {
                playerChatIcons[i] = buffer.getByte();
                playerChatIcons[i] = playerChatIcons[i] == 255 ? -1 : playerChatIcons[i];
            }
            int offset = buffer.getByteC();
            int off = buffer.currentPosition;
            if (player.name != null && player.visible) {
                long name = StringUtility.encodeBase37(player.name);
                boolean ignored = false;
                if (privilege <= 1) {
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignores[count] != name) {
                            continue;
                        }
                        ignored = true;
                        break;
                    }

                }
                if (!ignored) {
                    try {
                        chatBuffer.currentPosition = 0;
                        buffer.readReverseData(chatBuffer.payload, offset, 0);
                        chatBuffer.currentPosition = 0;
                        String text = ChatMessageCodec.decode(offset,
                                chatBuffer);
                        if (settings[311] == 1) {
                            text = MessageCensor.apply(text);
                        }
                        player.spokenText = text;
                        player.textColour = textInfo >> 8;
                        player.privelage = privilege;
                        player.textEffect = textInfo & 0xff;
                        player.textCycle = 150;
                        pushMessage(new ChatMessage((privilege > 0 ? 1 : 2), player.name, text,
                                player.getTitle(), player.isTitleSuffix(), playerChatIcons));
                    } catch (Exception exception) {
                        SignLink.reporterror("cde2");
                    }
                }
            }
            buffer.currentPosition = off + offset;
        }
        if ((mask & 1) != 0) {
            player.interactingEntity = buffer.readLEUShort();
            if (player.interactingEntity == 65535) {
                player.interactingEntity = -1;
            }
        }
        if ((mask & 0x10) != 0) {
            int length = buffer.getByteC();
            byte data[] = new byte[length];
            RSStream appearanceBuffer = new RSStream(data);
            buffer.readBytes(length, 0, data);
            playerSynchronizationBuffers[index] = appearanceBuffer;
            player.updatePlayer(appearanceBuffer);
        }
        if ((mask & 2) != 0) {
            player.faceX = buffer.readLEUShortA();
            player.faceY = buffer.readLEUShort();
        }
        if ((mask & 0x20) != 0) {
            int damage = buffer.getByte();
            int type = buffer.getByteA();
            int icon = buffer.getByte();
            player.updateHitData(type, damage, loopCycle, icon);
            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = buffer.getByteC();
            player.maxHealth = buffer.getByte();
        }
        if ((mask & 0x200) != 0) {
            int damage = buffer.getByte();
            int type = buffer.getByteS();
            int icon = buffer.getByte();
            player.updateHitData(type, damage, loopCycle, icon);
            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = buffer.getByte();
            player.maxHealth = buffer.getByteC();
        }
    }

    private void checkForGameUsages() {
        try {
            int j = localPlayer.x + cameraX;
            int k = localPlayer.y + cameraY;
            if (anInt1014 - j < -500 || anInt1014 - j > 500
                    || anInt1015 - k < -500 || anInt1015 - k > 500) {
                anInt1014 = j;
                anInt1015 = k;
            }
            if (anInt1014 != j) {
                anInt1014 += (j - anInt1014) / 16;
            }
            if (anInt1015 != k) {
                anInt1015 += (k - anInt1015) / 16;
            }
            if (super.keyArray[1] == 1) {
                anInt1186 += (-24 - anInt1186) / 2;
            } else if (super.keyArray[2] == 1) {
                anInt1186 += (24 - anInt1186) / 2;
            } else {
                anInt1186 /= 2;
            }
            if (super.keyArray[3] == 1) {
                anInt1187 += (12 - anInt1187) / 2;
            } else if (super.keyArray[4] == 1) {
                anInt1187 += (-12 - anInt1187) / 2;
            } else {
                anInt1187 /= 2;
            }
            cameraHorizontal = cameraHorizontal + anInt1186 / 2 & 0x7ff;
            anInt1184 += anInt1187 / 2;
            if (anInt1184 < 128) {
                anInt1184 = 128;
            }
            if (anInt1184 > 383) {
                anInt1184 = 383;
            }
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = getFloorDrawHeight(plane, anInt1015, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = plane;
                        if (l2 < 3 && (tileFlags[1][l1][k2] & 2) == 2) {
                            l2++;
                        }
                        int i3 = j1 - tileHeights[l2][l1][k2];
                        if (i3 > k1) {
                            k1 = i3;
                        }
                    }

                }

            }
            anInt1005++;
            if (anInt1005 > 1512) {
                anInt1005 = 0;
                outgoing.putOpcode(77);
                outgoing.writeByte(0);
                int i2 = outgoing.currentPosition;
                outgoing.writeByte((int) (Math.random() * 256D));
                outgoing.writeByte(101);
                outgoing.writeByte(233);
                outgoing.putShort(45092);
                if ((int) (Math.random() * 2D) == 0) {
                    outgoing.putShort(35784);
                }
                outgoing.writeByte((int) (Math.random() * 256D));
                outgoing.writeByte(64);
                outgoing.writeByte(38);
                outgoing.putShort((int) (Math.random() * 65536D));
                outgoing.putShort((int) (Math.random() * 65536D));
                outgoing.writeBytes(outgoing.currentPosition - i2);
            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00) {
                j2 = 0x17f00;
            }
            if (j2 < 32768) {
                j2 = 32768;
            }
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
            }
        } catch (Exception _ex) {
            SignLink.reporterror("glfc_ex " + localPlayer.x + ","
                    + localPlayer.y + "," + anInt1014 + "," + anInt1015 + ","
                    + currentRegionX + "," + currentRegionY + "," + regionBaseX + ","
                    + regionBaseY);
            throw new RuntimeException("eek");
        }
    }

    public void processDrawing() {
        if (worldError) {
            return;
        }
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        if (!loggedIn) {
            drawLoginScreen(worldSelect);
            if (worldSelect) {
                drawWorldSelect(true);
                dragPosition = 0;
                return;
            }
        } else {
            drawGameScreen();
        }
        dragPosition = 0;
    }

    public boolean isFriendOrSelf(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < friendsCount; i++) {
            if (s.equalsIgnoreCase(friendsList[i])) {
                return true;
            }
        }
        return s.equalsIgnoreCase(localPlayer.name);
    }

    private void setWaveVolume(int i) {
        SignLink.wavevol = i;
    }

    /**
     * Processes interface configurations.
     *
     * @param configId    The id of the config.
     * @param configValue The value of the config.
     *                    TODO Cs2
     */
    public void processInterfaceConfiguration(int configId, int configValue) {
        InterfaceConfiguration interfaceConfiguration = InterfaceConfiguration.forId(configId);
        if (interfaceConfiguration != null) {
            switch (interfaceConfiguration) {
                case MODE_FIXED:
                    if (configValue == 1) {
                        frameMode(this, ScreenMode.FIXED);
                    }
                    break;
                case MODE_RESIZABLE:
                    if (configValue == 1) {
                        frameMode(this, ScreenMode.RESIZABLE);
                    }
                    break;
                case MODE_FULLSCREEN:
                    if (configValue == 1) {
                        frameMode(this, ScreenMode.FULLSCREEN);
                    }
                    break;
                case TRANSPARENT_SIDE_PANEL:
                    RSComponent.forId(26393).popupString = "Resizable mode side-panel:\\n" + ((configValue == 1) ? "Transparent" : "Opaque");
                    break;
                case REMAINING_XP:
                    RSComponent.forId(26397).popupString = "Stats panel shows XP to next level (currently " + ((configValue == 1) ? "on" : "off") + ")";
                    break;
                case HIDE_ROOFS:
                    RSComponent.forId(26401).popupString = "Always hide roofs (currently " + ((configValue == 1) ? "on" : "off") + ")";
                    break;
                case ORBS_ENABLED:
                    RSComponent.forId(26405).popupString = "Data orbs (currently " + ((configValue == 1) ? "on" : "off") + ")";
                    break;
                case TRANSPARENT_CHAT:
                    RSComponent.forId(26409).popupString = "Resizable mode chatbox:\\n" + ((configValue == 1) ? "Transparent" : "Opaque");
                    RSComponent.forId(26413).textColor = (configValue == 1) ? 0xFF981F : 0x8F8F8F;
                    RSComponent.forId(26418).textColor = (configValue == 1) ? 0xFF981F : 0x8F8F8F;
                    RSComponent.forId(26413).disabledMouseOverColor = (configValue == 1) ? 0xFFB82F : 0x8F8F8F;
                    RSComponent.forId(26418).disabledMouseOverColor = (configValue == 1) ? 0xFFB82F : 0x8F8F8F;
                    break;
                case SIDE_STONES_ARRANGEMENT:
                    RSComponent.forId(26419).popupString = "Resizable mode stone buttons:\\n" + ((configValue == 1) ? "'Bottom Line'\\nThe hotkeys will not close the side-panels." : "'Old School Box'");
                    RSComponent.forId(26423).textColor = (configValue == 1) ? 0xFF981F : 0x8F8F8F;
                    RSComponent.forId(26425).textColor = (configValue == 1) ? 0xFF981F : 0x8F8F8F;
                    RSComponent.forId(26423).disabledMouseOverColor = (configValue == 1) ? 0xFFB82F : 0x8F8F8F;
                    RSComponent.forId(26425).disabledMouseOverColor = (configValue == 1) ? 0xFFB82F : 0x8F8F8F;
                    break;

                case GAMEFRAME:
                    setGameFrame(configValue, loggedIn);
                    break;

                case CURSORS:
                    if (interfaceConfig[configId] == configValue) {
                        break;
                    }
                    setCursor(GameCursor.NONE);
                    break;

                case HD_TEXTURES:
                    if (interfaceConfig[configId] == configValue || !loggedIn) {
                        break;
                    }
                    interfaceConfig[configId] = configValue;
                    CacheArchive cacheArchive = createArchive(2, "config", "config", archiveCRCs[2], 30);
                    FloorOverlay.unpackConfig(cacheArchive);
                    loadRegion();
                    return;

                case TELEPORT_BUTTONS:
                    int index = 0;
                    for (int buttonId = 24823; buttonId < (24823 + 60); buttonId++) {
                        boolean enabled = index >= configValue;
                        RSComponent.forId(buttonId).hidden = enabled;
                        RSComponent.forId(buttonId + 1).hidden = enabled;
                        RSComponent.forId(buttonId + 2).hidden = enabled;
                        buttonId += 4;
                        index++;
                    }
                    break;

                case GRAND_EXCHANGE_OFFER_TYPE:
                    grandExchange.updateOfferComponent(configValue);
                    break;

                case SKILL_MENU_SUBSECTION:
                    if (configValue < 2) {
                        // Hide menu completely
                        RSComponent.getComponentCache()[50003].getSpriteSet().setDisabled(SpriteRepository.EMPTY);
                        RSComponent.getComponentCache()[50004].getSpriteSet().setDisabled(SpriteRepository.EMPTY);
                        for (index = 0; index < 13; index++) {
                            RSComponent.getComponentCache()[50005 + index].hidden = true;
                        }
                        break;
                    }
                    int shown = configValue - 2;
                    RSComponent.getComponentCache()[50003].getSpriteSet().setDisabled(this.skillMenuSelect);
                    RSComponent.getComponentCache()[50004].getSpriteSet().setDisabled(this.skillMenuSelectBottom);
                    for (index = 0; index < 13; index++) {
                        RSComponent.getComponentCache()[50005 + index].hidden = shown < index;
                    }
                    break;

                case DUEL_ARENA_ACCEPT_BUTTON:
                    Sprite sprite = (configValue == 1) ? this.acceptButtonDisabled : SpriteRepository.ACCEPT_BUTTON;
                    Sprite hoverSprite = (configValue == 1) ? this.acceptButtonDisabledHover : SpriteRepository.ACCEPT_BUTTON_HOVER;
                    RSComponent.forId(48169).setSpriteSet(new SpriteSet(sprite, sprite));
                    RSComponent.forId(48171).setSpriteSet(new SpriteSet(hoverSprite, hoverSprite));
                    break;

                case HIDE_COMPONENT:
                case SHOW_COMPONENT:
                    if (RSComponent.forId(configValue) != null) {
                        RSComponent.forId(configValue).hidden = configId == 50;
                    }
                    break;

                case HIDE_COMPONENT_SPRITE:
                case DRAW_COMPONENT_SPRITE:
                    if (RSComponent.forId(configValue) != null) {
                        RSComponent.forId(configValue).drawSprite = configId == 53;
                    }
                    break;

                case AUTO_ATTACK_OPTION:
                    interfaceConfig[316] = 0;
                    interfaceConfig[317] = 0;
                    break;

                case LEFT_CLICK_ATTACK_OPTION:
                    interfaceConfig[315] = 0;
                    interfaceConfig[317] = 0;
                    break;

                case RIGHT_CLICK_ATTACK_OPTION:
                    interfaceConfig[315] = 0;
                    interfaceConfig[316] = 0;
                    break;

                case SUMMONING_SPECIAL_MOVE_POINTS:
                    summoning.setSpecialAttackWidth(configValue);
                    break;
            }
        } else {
            switch (configId) {
                /**
                 * Hide or show a Grand Exchange slot.
                 */
                case 320:
                case 321:
                    int[] hiddenButtons = Constants.getSlotButtons(configValue);
                    if (hiddenButtons == null) {
                        break;
                    }
                    for (int buttonId : hiddenButtons) {
                        RSComponent rsComponent = RSComponent.forId(buttonId);
                        if (rsComponent == null) {
                            continue;
                        }
                        RSComponent.forId(24913 + configValue).hidden = (configId == 320); // wait replace 24913 + configValue with buttonId ?
                        rsComponent.hidden = (configId == 320);
                    }
                    break;

                /**
                 * Hides / shows the progress bar on the Grand Exchange interface.
                 */
                case 322: // hide
                case 323: // show
                    int[] hideProgress = Constants.getSlotProgress(configValue);
                    if (hideProgress == null) {
                        break;
                    }
                    RSComponent.forId(hideProgress[0]).hidden = (configId == 322);
                    RSComponent.forId(hideProgress[1]).hidden = (configId == 323);
                    break;

                /**
                 * Temporarily hide/show an offer slot to "Empty"
                 */
                case 324: // hide
                case 325: // show
                    int[] hiddenSlots = Constants.getSlots(configValue);
                    if (hiddenSlots == null) {
                        break;
                    }
                    for (int buttonId : hiddenSlots) {
                        RSComponent rsComponent = RSComponent.forId(buttonId);
                        if (rsComponent == null) {
                            continue;
                        }
                        rsComponent.hidden = (configId == 324);
                    }
                    break;
            }
        }
        if (interfaceConfig[configId] != configValue) {
            interfaceConfig[configId] = configValue;
        }
    }

    /**
     * Processes configurations.
     *
     * @param configId    The id of the config.
     * @param configValue The value of the config.
     */
    public void processConfiguration(int configId, int configValue) {
        switch (configId) {
            /**
             * Run clicked.
             */
            case 173:
                runToggled = (configValue == 1);
                break;
            /**
             * Private chat notification timer.
             */
            case 312:
                for (ChatMessage chatMessage : chatMessages) {
                    if (chatMessage == null) {
                        continue;
                    }
                    if (chatMessage.getType() == 5) {
                        if (chatMessage.getClearDelay() == 0) {
                            continue;
                        }
                        chatMessage.setClearDelay((configValue == 1) ? Constants.PRIVATE_CHAT_NOTIFICATION_TIMER : -1);
                    }
                }
                break;

            /**
             * Bank tabs used.
             * TODO Tab 9 not showing, RSInterface needs new child for tab 9
             */
            case 710:
                try {
                    int childId = 58032;
                    for (int i8 = 0; i8 < 9; i8++) {
                        RSComponent.getComponentCache()[childId].hidden = (configValue <= i8);
                        if ((configValue <= i8)) {
                            RSComponent.getComponentCache()[childId].tooltips = null;
                        }
                        childId++;
                    }
                    // All used
                    if (configValue == 9) {
                        childId = 58032;
                        for (int index = 0; index < (configValue); index++) {
                            RSComponent.getComponentCache()[childId].getSpriteSet().setEnabled(this.bankTabEmpty);
                            RSComponent.getComponentCache()[childId].getSpriteSet().setDisabled(this.bankTabEmpty);
                            RSComponent.getComponentCache()[childId].tooltips = new String[]{"Collapse tab <col=FF9040>" + (index + 1), "View tab <col=FF9040>" + (index + 1)};
                            childId++;
                        }
                    } else {
                        childId = 58032;
                        for (int index = 0; index < (configValue - 1); index++) {
                            RSComponent.getComponentCache()[childId].getSpriteSet().setEnabled(this.bankTabEmpty);
                            RSComponent.getComponentCache()[childId].getSpriteSet().setDisabled(this.bankTabEmpty);
                            RSComponent.getComponentCache()[childId].tooltips = new String[]{"Collapse tab <col=FF9040>" + (index + 1), "View tab <col=FF9040>" + (index + 1)};
                            childId++;
                        }
                        RSComponent.getComponentCache()[58031 + configValue].getSpriteSet().setEnabled(ImageLoader.forName("BANK_TAB_4"));
                        RSComponent.getComponentCache()[58031 + configValue].getSpriteSet().setDisabled(ImageLoader.forName("BANK_TAB_4"));
                        RSComponent.getComponentCache()[58031 + configValue].tooltips = new String[]{"New tab"};
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void draw3dScreen() {
        if (showChatComponents && !isMediaMode()) {
            drawSplitPrivateChat();
        }
        if (interfaceConfig[201] == 1) {
            gameframe.drawXPTrackerFrame(frameMode == ScreenMode.FIXED ? 0 : (frameWidth - 762));
        }
        if (openWalkableInterface != -1) {
            animateRSInterface(cycleTimer, openWalkableInterface);
            if (openWalkableInterface == 197 && frameMode != ScreenMode.FIXED) {
                skullIcons[0].drawSprite(frameWidth - 157, 168);
                String text = RSComponent.forId(199).disabledMessage.replace("<col=FFFF00>", "");
                regularFont.drawBasicString(text, frameWidth - 165, 207, 0xE1981D, 0); // TODO Get correct color and position
            } else if (openWalkableInterface == 201 && frameMode != ScreenMode.FIXED) {
                componentDrawing.drawComponent(RSComponent.forId(openWalkableInterface), frameWidth - 560, -109, 0, 0);
            } else {
                componentDrawing.drawComponent(RSComponent.forId(openWalkableInterface), frameMode == ScreenMode.FIXED ? 0
                        : (frameWidth / 2) - 356, frameMode == ScreenMode.FIXED ? 0
                        : (frameHeight / 2) - 230, 0, 0);
            }
        }
        if (openInterfaceId != -1) {
            animateRSInterface(cycleTimer, openInterfaceId);
            componentDrawing.drawComponent(RSComponent.forId(openInterfaceId), frameMode == ScreenMode.FIXED ? 0
                    : (frameWidth / 2) - 356, frameMode == ScreenMode.FIXED ? 0 : (frameHeight / 2) - 230, 0, 0);
        }
        grandExchange.update(openInterfaceId);
        if (!menuOpen) {
            processRightClick();
            drawTooltip();
        } else {
            drawMenu(frameMode == ScreenMode.FIXED ? 4 : 0,
                    frameMode == ScreenMode.FIXED ? 4 : 0);
        }
        if (multiCombat == 1) {
            multiOverlay.drawSprite(frameMode == ScreenMode.FIXED ? 472
                    : frameWidth - 85, frameMode == ScreenMode.FIXED ? 296
                    : 186);
        }
        int x = regionBaseX + (localPlayer.x - 6 >> 7);
        int y = regionBaseY + (localPlayer.y - 6 >> 7);
        if (Constants.clientData) {
            int textColour = 0xFFFF00;
            regularFont.drawBasicString("Client Zoom: " + cameraZoom, 5, frameHeight - 215, textColour, -1);
            regularFont.drawBasicString("Mouse X: " + mouseX + ", Mouse Y: " + mouseY, 5, frameHeight - 201, textColour, -1);
            regularFont.drawBasicString("Coordinates: " + x + ", " + y, 5, frameHeight - 187, textColour, -1);
            regularFont.drawBasicString("Client Resolution: [" + frameMode.name() + "] " + frameWidth + "x" + frameHeight, 5, frameHeight - 173, textColour, -1);
        }
        if (systemUpdateTime != 0) {
            int j = systemUpdateTime / 50;
            int l = j / 60;
            int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 498;
            j %= 60;
            regularFont.drawBasicString("System update in : " + l + ":" + (j < 10 ? "0" : "") + j, 4, 329 + yOffset, 0xFFFF00, -1);
            anInt849++;
            if (anInt849 > 75) {
                anInt849 = 0;
                outgoing.putOpcode(148);
            }
        }
        if (crossType == 1) {
            int clickOffset = frameMode == ScreenMode.FIXED ? 4 : 0;
            crosses[crossIndex / 100].drawSprite(crossX - 8 - clickOffset, crossY - 8 - clickOffset);
            anInt1142++;
            if (anInt1142 > 67) {
                anInt1142 = 0;
                outgoing.putOpcode(78);
            }
        }
        if (crossType == 2) {
            int clickOffset = frameMode == ScreenMode.FIXED ? 4 : 0;
            crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - clickOffset, crossY - 8 - clickOffset);
        }
    }

    private void addIgnore(long l) {
        try {
            if (l == 0L) {
                return;
            }
            if (ignoreCount >= 100) {
                pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
                return;
            }
            String s = StringUtility.formatUsername(StringUtility.decodeBase37(l));
            for (int j = 0; j < ignoreCount; j++) {
                if (ignores[j] == l) {
                    pushMessage(s + " is already on your ignore list", 0, "");
                    return;
                }
            }
            for (int k = 0; k < friendsCount; k++) {
                if (friendsListAsLongs[k] == l) {
                    pushMessage("Please remove " + s
                            + " from your friend list first", 0, "");
                    return;
                }
            }

            ignores[ignoreCount++] = l;
            outgoing.putOpcode(133);
            outgoing.writeLong(l);
            return;
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("45688, " + l + ", " + 4 + ", "
                    + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void updatePlayerInstances() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1) {
                j = internalLocalPlayerIndex;
            } else {
                j = playerIndices[i];
            }
            Player player = players[j];
            if (player != null) {
                entityUpdateBlock(player);
            }
        }

    }

    private void method115() {
        if (loadingStage == 2) {
            for (SpawnedObject spawnedObject = (SpawnedObject) spawns
                    .reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
                    .reverseGetNext()) {
                if (spawnedObject.getLongetivity > 0) {
                    spawnedObject.getLongetivity--;
                }
                if (spawnedObject.getLongetivity == 0) {
                    if (spawnedObject.getPreviousId < 0
                            || MapRegion.modelReady(
                            spawnedObject.getPreviousId,
                            spawnedObject.previousType)) {
                        removeObject(spawnedObject.tileY, spawnedObject.plane,
                                spawnedObject.previousOrientation,
                                spawnedObject.previousType, spawnedObject.tileX,
                                spawnedObject.group,
                                spawnedObject.getPreviousId);
                        spawnedObject.unlink();
                    }
                } else {
                    if (spawnedObject.delay > 0) {
                        spawnedObject.delay--;
                    }
                    if (spawnedObject.delay == 0
                            && spawnedObject.tileX >= 1
                            && spawnedObject.tileY >= 1
                            && spawnedObject.tileX <= 102
                            && spawnedObject.tileY <= 102
                            && (spawnedObject.id < 0 || MapRegion.modelReady(
                            spawnedObject.id, spawnedObject.type))) {
                        removeObject(spawnedObject.tileY, spawnedObject.plane,
                                spawnedObject.orientation, spawnedObject.type,
                                spawnedObject.tileX, spawnedObject.group,
                                spawnedObject.id);
                        spawnedObject.delay = -1;
                        if (spawnedObject.id == spawnedObject.getPreviousId
                                && spawnedObject.getPreviousId == -1) {
                            spawnedObject.unlink();
                        } else if (spawnedObject.id == spawnedObject.getPreviousId
                                && spawnedObject.orientation == spawnedObject.previousOrientation
                                && spawnedObject.type == spawnedObject.previousType) {
                            spawnedObject.unlink();
                        }
                    }
                }
            }

        }
    }

    private void updatePlayerMovement(RSStream stream) {
        stream.initBitAccess();
        int update = stream.readBits(1);
        if (update == 0) {
            return;
        }
        int type = stream.readBits(2);
        if (type == 0) {
            mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            return;
        }
        if (type == 1) {
            int direction = stream.readBits(3);
            localPlayer.moveInDir(false, direction);
            int updateRequired = stream.readBits(1);
            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }
            return;
        }
        if (type == 2) {
            int firstDirection = stream.readBits(3);
            localPlayer.moveInDir(true, firstDirection);
            int secondDirection = stream.readBits(3);
            localPlayer.moveInDir(true, secondDirection);
            int updateRequired = stream.readBits(1);
            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }
            return;
        }
        if (type == 3) {
            plane = stream.readBits(2);
            int teleport = stream.readBits(1);
            int updateRequired = stream.readBits(1);
            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }
            int y = stream.readBits(7);
            int x = stream.readBits(7);
            localPlayer.setPos(x, y, teleport == 1);
        }
    }

    private void nullLoader() {
    }

    public boolean animateRSInterface(int animationFrame, int componentId) {
        boolean loopAnimation = false;
        RSComponent rsComponent = RSComponent.getComponentCache()[componentId];
        if (rsComponent == null || rsComponent.getInterfaceChildren() == null) {
            return false;
        }
        for (InterfaceChild interfaceChild : rsComponent.getInterfaceChildren()) {
            if (interfaceChild.getId() == -1) {
                break;
            }
            RSComponent rsComponent_1 = RSComponent.getComponentCache()[interfaceChild.getId()];
            assert rsComponent_1 != null;
            if (rsComponent_1.type == 1) {
                loopAnimation |= animateRSInterface(animationFrame, rsComponent_1.id);
            }
            if (rsComponent_1.type == 6 && (rsComponent_1.disabledAnimationId != -1 || rsComponent_1.enabledAnimationId != -1)) {
                boolean flag2 = interfaceIsSelected(rsComponent_1);
                int animationId;
                if (flag2) {
                    animationId = rsComponent_1.enabledAnimationId;
                } else {
                    animationId = rsComponent_1.disabledAnimationId;
                }
                if (animationId != -1) {
                    Animation animation = Animation.animations[animationId];
                    for (rsComponent_1.animationLength += animationFrame; rsComponent_1.animationLength > animation.fetchAnimationLength(rsComponent_1.animationFrames); ) {
                        rsComponent_1.animationLength -= animation.fetchAnimationLength(rsComponent_1.animationFrames) + 1;
                        rsComponent_1.animationFrames++;
                        if (rsComponent_1.animationFrames >= animation.frameCount) {
                            rsComponent_1.animationFrames -= animation.loopDelay;
                            if (rsComponent_1.animationFrames < 0 || rsComponent_1.animationFrames >= animation.frameCount) {
                                rsComponent_1.animationFrames = 0;
                            }
                        }
                        loopAnimation = true;
                    }
                }
            }
        }
        return loopAnimation;
    }

    private int setCameraLocation() {
        if (interfaceConfig[105] == 1) {
            return plane;
        }
        int j = 3;
        if (yCameraCurve < 310) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = localPlayer.x >> 7;
            int j1 = localPlayer.y >> 7;
            if ((tileFlags[plane][k][l] & 4) != 0) {
                j = plane;
            }
            int k1;
            if (i1 > k) {
                k1 = i1 - k;
            } else {
                k1 = k - i1;
            }
            int l1;
            if (j1 > l) {
                l1 = j1 - l;
            } else {
                l1 = l - j1;
            }
            if (k1 > l1) {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1) {
                        k++;
                    } else if (k > i1) {
                        k--;
                    }
                    if ((tileFlags[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1) {
                            l++;
                        } else if (l > j1) {
                            l--;
                        }
                        if ((tileFlags[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            } else {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1) {
                        l++;
                    } else if (l > j1) {
                        l--;
                    }
                    if ((tileFlags[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1) {
                            k++;
                        } else if (k > i1) {
                            k--;
                        }
                        if ((tileFlags[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            }
        }
        if ((tileFlags[plane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0) {
            j = plane;
        }
        return j;
    }

    private int resetCameraHeight() {
        if (interfaceConfig[105] == 1) {
            return plane;
        }
        int orientation = getFloorDrawHeight(plane, yCameraPos, xCameraPos);
        if (orientation - zCameraPos < 800
                && (tileFlags[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0) {
            return plane;
        } else {
            return 3;
        }
    }

    private void removeFriend(long name) {
        try {
            if (name == 0L) {
                return;
            }
            for (int index = 0; index < friendsCount; index++) {
                if (friendsListAsLongs[index] != name) {
                    continue;
                }
                friendsCount--;
                for (int count = index; count < friendsCount; count++) {
                    friendsList[count] = friendsList[count + 1];
                    friendsNodeIDs[count] = friendsNodeIDs[count + 1];
                    friendsListAsLongs[count] = friendsListAsLongs[count + 1];
                }

                outgoing.putOpcode(215);
                outgoing.writeLong(name);
                break;
            }
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("18622, " + false + ", " + name + ", "
                    + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    private void removeIgnore(long name) {
        try {
            if (name == 0L) {
                return;
            }
            for (int index = 0; index < ignoreCount; index++) {
                if (ignores[index] == name) {
                    ignoreCount--;
                    System.arraycopy(ignores, index + 1,
                            ignores, index, ignoreCount - index);

                    outgoing.putOpcode(74);
                    outgoing.writeLong(name);
                    return;
                }
            }

            return;
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("47229, " + 3 + ", " + name + ", "
                    + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void chatJoin(long l) {
        try {
            if (l == 0L) {
                return;
            }
            outgoing.putOpcode(60);
            outgoing.writeLong(l);
            return;
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("47229, " + 3 + ", " + l + ", "
                    + runtimeexception.toString());
        }
        throw new RuntimeException();

    }

    public final void requestCrcs() {
        int delay = 5;
        archiveCRCs[8] = 0;
        int k = 0;
        while (archiveCRCs[8] == 0) {
            String error = "Unknown problem";
            drawLoadingText(20, "Connecting to web server");
            try (DataInputStream dataInputStream = requestCacheIndex("crc" + (int) (Math.random() * 99999999D) + "-" + 317)) {
                RSStream buffer = new RSStream(new byte[40]);
                dataInputStream.readFully(buffer.payload, 0, 40);
                dataInputStream.close();
                for (int index = 0; index < 9; index++) {
                    archiveCRCs[index] = buffer.getInt();
                }

                int expected = buffer.getInt();
                int calculated = 1234;
                for (int index = 0; index < 9; index++) {
                    calculated = (calculated << 1) + archiveCRCs[index];
                }

                if (expected != calculated) {
                    error = "checksum problem";
                    archiveCRCs[8] = 0;
                }
            } catch (EOFException ex) {
                error = "EOF problem";
                archiveCRCs[8] = 0;
            } catch (IOException ex) {
                ex.printStackTrace();
                error = "connection problem";
                archiveCRCs[8] = 0;
            } catch (Exception ex) {
                error = "logic problem";
                archiveCRCs[8] = 0;
                if (!SignLink.reporterror) {
                    return;
                }
            }

            if (archiveCRCs[8] == 0) {
                k++;
                for (int remaining = delay; remaining > 0; remaining--) {
                    if (k >= 10) {
                        drawLoadingText(10, "Game updated - please reload page");
                        remaining = 10;
                    } else {
                        drawLoadingText(10, error + " - Will retry in "
                                + remaining + " secs.");
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ex) {
                    }
                }

                delay *= 2;
                if (delay > 60) {
                    delay = 60;
                }
                Constants.JAGGRAB_ENABLED = !Constants.JAGGRAB_ENABLED;
            }
        }
    }

    public String getParameter(String s) {
        if (SignLink.mainapp != null) {
            return SignLink.mainapp.getParameter(s);
        } else {
            return super.getParameter(s);
        }
    }

    public int executeCS1Script(RSComponent rsComponent, int id) {
        if (rsComponent.scripts == null || id >= rsComponent.scripts.length) {
            return -2;
        }
        try {
            int[] script = rsComponent.scripts[id];
            int accumulator = 0;
            int counter = 0;
            int operator = 0;
            do {
                int instruction = script[counter++];
                int value = 0;
                byte next = 0;
                if (instruction == 0) {
                    return accumulator;
                }
                if (instruction == 1) {
                    value = currentStats[script[counter++]];
                }
                if (instruction == 2) {
                    value = maximumLevels[script[counter++]];
                }
                if (instruction == 3) {
                    value = currentExp[script[counter++]];
                }
                if (instruction == 4) {
                    RSComponent inventoryComponent = RSComponent.forId(script[counter++]);
                    int item = script[counter++];
                    if (item >= 0 && item < ItemDefinition.getItemCount() && (!ItemDefinition.forId(item).members || isMembers)) {
                        for (int slot = 0; slot < inventoryComponent.inventory.length; slot++) {
                            if (inventoryComponent.inventory[slot] == item + 1) {
                                value += inventoryComponent.inventoryValue[slot];
                            }
                        }
                        if (inventoryComponent.id == 3214) { // TODO MOVE TO OWN INSTRUCTION
                            RSComponent runePouchComponent = RSComponent.forId(25040);

                            for (int slot = 0; slot < runePouchComponent.inventory.length; slot++)
                                if (runePouchComponent.inventory[slot] == item + 1)
                                    value += runePouchComponent.inventoryValue[slot];

                        }
                    }
                }
                if (instruction == 5)
                    value = rsComponent.interfaceConfig ? interfaceConfig[script[counter++]] : settings[script[counter++]];

                if (instruction == 6)
                    value = SKILL_EXPERIENCE[maximumLevels[script[counter++]] - 1];

                if (instruction == 7)
                    value = rsComponent.interfaceConfig ? ((interfaceConfig[script[counter++]] * 100) / 46875) : (settings[script[counter++]] * 100) / 46875;

                if (instruction == 8)
                    value = localPlayer.combatLevel;

                if (instruction == 9)
                    for (int skill = 0; skill < Skills.SKILL_COUNT; skill++)
                        value += maximumLevels[skill];

                if (instruction == 10) {
                    RSComponent other = RSComponent.forId(script[counter++]);
                    int item = script[counter++] + 1;
                    if (item >= 0 && item < ItemDefinition.getItemCount() && isMembers) {
                        for (int stored = 0; stored < other.inventory.length; stored++) {

                            if (other.inventory[stored] != item)
                                continue;

                            value = 0x3b9ac9ff;
                            break;
                        }
                    }
                }
                if (instruction == 11) {
                    value = runEnergy;
                }
                if (instruction == 12) {
                    value = weight;
                }
                if (instruction == 13) {
                    int bool = rsComponent.interfaceConfig ? interfaceConfig[script[counter++]] : settings[script[counter++]];
                    int shift = script[counter++];
                    value = (bool & 1 << shift) == 0 ? 0 : 1;
                }
                if (instruction == 14) {
                    int index = script[counter++];
                    VariableBits bits = VariableBits.cache[index];
                    int setting = bits.getSetting();
                    int low = bits.getLow();
                    int high = bits.getHigh();
                    int mask = BIT_MASKS[high - low];
                    value = settings[setting] >> low & mask;
                }
                if (instruction == 15) {
                    next = 1;
                }
                if (instruction == 16) {
                    next = 2;
                }
                if (instruction == 17) {
                    next = 3;
                }
                if (instruction == 18) {
                    value = (localPlayer.x >> 7) + regionBaseX;
                }
                if (instruction == 19) {
                    value = (localPlayer.y >> 7) + regionBaseY;
                }
                if (instruction == 20) {
                    value = script[counter++];
                }
                if (instruction == 21) {
                    int skill = script[counter++];
                    int currentExperience = currentExp[skill];
                    int levelIndex = 0;
                    for (int levels = 0; levels < SKILL_EXPERIENCE.length; levels++) {
                        if (currentExperience < SKILL_EXPERIENCE[levels]) {
                            levelIndex = levels;
                            break;
                        }
                    }
                    int remainingExperience = maximumLevels[skill] == 99 ? 0 : SKILL_EXPERIENCE[levelIndex] - currentExperience;
                    value = remainingExperience;
                }
                if (instruction == 22) {
                    value = tabAmounts[script[counter++]];
                }
                if (next == 0) {
                    if (operator == 0) {
                        accumulator += value;
                    }
                    if (operator == 1) {
                        accumulator -= value;
                    }
                    if (operator == 2 && value != 0) {
                        accumulator /= value;
                    }
                    if (operator == 3) {
                        accumulator *= value;
                    }
                    operator = 0;
                } else {
                    operator = next;
                }
            } while (true);
        } catch (Exception _ex) {
            return -1;
        }
    }

    private void drawTooltip() {
        if (mediaMode && frameMode != ScreenMode.FIXED) {
            return;
        }
        setCursor(GameCursor.forText(menuActionName[menuActionRow - 1]));
        if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) {
            return;
        }
        String s;
        if (itemSelected == 1 && menuActionRow < 2) {
            s = "Use " + selectedItemName + " with...";
        } else if (spellSelected == 1 && menuActionRow < 2) {
            s = spellTooltip;
        } else {
            s = menuActionName[menuActionRow - 1];
        }
        if (menuActionRow > 2) {
            s = s + "<col=FFFFFF> / " + (menuActionRow - 2) + " more options";
        }
        s = s == null ? "" : s;
        boldFont.drawBasicString(StringUtility.cleanString(s), 4, 15, 0xFFFFFF, 0);
    }

    private void npcScreenPos(Entity entity, int i) {
        calcEntityScreenPos(entity.x, i, entity.y);
    }

    private void calcEntityScreenPos(int i, int j, int l) {
        if (i < 128 || l < 128 || i > 13056 || l > 13056) {
            spriteDrawX = -1;
            spriteDrawY = -1;
            return;
        }
        int i1 = getFloorDrawHeight(plane, l, i) - j;
        i -= xCameraPos;
        i1 -= zCameraPos;
        l -= yCameraPos;
        int j1 = Model.SINE[yCameraCurve];
        int k1 = Model.COSINE[yCameraCurve];
        int l1 = Model.SINE[xCameraCurve];
        int i2 = Model.COSINE[xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if (l >= 50) {
            spriteDrawX = Rasterizer.textureInt1
                    + (i << SceneGraph.viewDistance) / l;
            spriteDrawY = Rasterizer.textureInt2
                    + (i1 << SceneGraph.viewDistance) / l;
        } else {
            spriteDrawX = -1;
            spriteDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu() {
        if (splitPrivateChat == 0) {
            return;
        }
        int i = 0;
        if (systemUpdateTime != 0) {
            i = 1;
        }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int chatType = chatMessages[j].getType();
                String chatMessageName = chatMessages[j].getNameClean();
                if ((chatType == 3 || chatType == 7) && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 &&
                        isFriendOrSelf(chatMessageName))) {
                    int offSet = frameMode == ScreenMode.FIXED ? 4 : 0;
                    int l = 329 - i * 13;
                    if (frameMode != ScreenMode.FIXED) {
                        l = frameHeight - 170 - i * 13;
                    }
                    if (super.mouseX > 4 && super.mouseY - offSet > l - 10 && super.mouseY - offSet <= l + 3) {
                        int i1 = regularFont.getTextWidth("From:  " + chatMessageName + chatMessages[j].getText()) + 25;
                        if (i1 > 450) {
                            i1 = 450;
                        }
                        if (super.mouseX < 4 + i1) {
                            menuActionName[menuActionRow] = "Report <col=FFFFFF>" + chatMessageName;
                            menuActionId[menuActionRow] = 2606;
                            menuActionRow++;
                            menuActionName[menuActionRow] = "Add ignore <col=FFFFFF>" + chatMessageName;
                            menuActionId[menuActionRow] = 2042;
                            menuActionRow++;
                            menuActionName[menuActionRow] = "Add friend <col=FFFFFF>" + chatMessageName;
                            menuActionId[menuActionRow] = 2337;
                            menuActionRow++;
                        }
                    }
                    if (++i >= 5) {
                        return;
                    }
                }
                if ((chatType == 5 || chatType == 6) && privateChatMode < 2 && ++i >= 5) {
                    return;
                }
            }
        }
    }

    private void spawnObject(int longetivity, int id, int orientation,
                             int group, int y, int type, int plane, int x, int delay) {
        SpawnedObject object = null;
        for (SpawnedObject node = (SpawnedObject) spawns.reverseGetFirst(); node != null; node = (SpawnedObject) spawns
                .reverseGetNext()) {
            if (node.plane != plane || node.tileX != x || node.tileY != y
                    || node.group != group) {
                continue;
            }
            object = node;
            break;
        }

        if (object == null) {
            object = new SpawnedObject();
            object.plane = plane;
            object.group = group;
            object.tileX = x;
            object.tileY = y;
            assignOldValuesToNewRequest(object);
            spawns.insertHead(object);
        }
        object.id = id;
        object.type = type;
        object.orientation = orientation;
        object.delay = delay;
        object.getLongetivity = longetivity;
    }

    public boolean interfaceIsSelected(RSComponent rsComponent) {
        if (rsComponent.scriptOperators == null) {
            return false;
        }
        for (int scriptIndex = 0; scriptIndex < rsComponent.scriptOperators.length; scriptIndex++) {
            int value = executeCS1Script(rsComponent, scriptIndex);
            int defaultValue = rsComponent.scriptDefaults[scriptIndex];
            if (rsComponent.scriptOperators[scriptIndex] == 2) {
                if (value >= defaultValue) {
                    return false;
                }
            } else if (rsComponent.scriptOperators[scriptIndex] == 3) {
                if (value <= defaultValue) {
                    return false;
                }
            } else if (rsComponent.scriptOperators[scriptIndex] == 4) {
                if (value == defaultValue) {
                    return false;
                }
            } else if (rsComponent.scriptOperators[scriptIndex] == 5) {
                return true;
            } else if (value != defaultValue) {
                return false;
            }
        }
        return true;
    }

    private DataInputStream requestCacheIndex(String request) throws IOException {
        if (!Constants.JAGGRAB_ENABLED) {
            if (SignLink.mainapp != null) {
                return SignLink.openUrl(request);
            }
            return new DataInputStream(new URL(getCodeBase(), request).openStream());
        }
        if (jaggrab != null) {
            try {
                jaggrab.close();
            } catch (Exception _ex) {
            }
            jaggrab = null;
        }
        jaggrab = openSocket(Constants.JAGGRAB_HOST, Constants.JAGGRAB_PORT);
        jaggrab.setSoTimeout(10000);
        java.io.InputStream inputStream = jaggrab.getInputStream();
        OutputStream outputstream = jaggrab.getOutputStream();
        outputstream.write(("JAGGRAB /" + request + "\n\n").getBytes());
        return new DataInputStream(inputStream);
    }

    private void method134(RSStream stream) {
        int j = stream.readBits(8);
        if (j < playerCount) {
            for (int k = j; k < playerCount; k++) {
                anIntArray840[anInt839++] = playerIndices[k];
            }

        }
        if (j > playerCount) {
            SignLink.reporterror(loginUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        playerCount = 0;
        for (int l = 0; l < j; l++) {
            int i1 = playerIndices[l];
            Player player = players[i1];
            int j1 = stream.readBits(1);
            if (j1 == 0) {
                playerIndices[playerCount++] = i1;
                player.anInt1537 = loopCycle;
            } else {
                int k1 = stream.readBits(2);
                if (k1 == 0) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = i1;
                } else if (k1 == 1) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    int l1 = stream.readBits(3);
                    player.moveInDir(false, l1);
                    int j2 = stream.readBits(1);
                    if (j2 == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = i1;
                    }
                } else if (k1 == 2) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    int i2 = stream.readBits(3);
                    player.moveInDir(true, i2);
                    int k2 = stream.readBits(3);
                    player.moveInDir(true, k2);
                    int l2 = stream.readBits(1);
                    if (l2 == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = i1;
                    }
                } else if (k1 == 3) {
                    anIntArray840[anInt839++] = i1;
                }
            }
        }
    }

    public void automaticReply() {
        String pmUsername = null;
        for (int pmIndex = 0; pmIndex < 100; pmIndex++) {
            if (chatMessages[pmIndex] == null) {
                continue;
            }
            int chatType = chatMessages[pmIndex].getType();
            if (chatType == 3 || chatType == 7) {
                pmUsername = chatMessages[pmIndex].getNameClean();
                break;
            }
        }

        if (pmUsername == null) {
            pushMessage("You haven't received any messages to which you can reply.", 0, "");
            return;
        }

        long nameAsLong = StringUtility.encodeBase37(pmUsername);
        int friendIndex = -1;
        for (int index = 0; index < friendsCount; index++) {
            if (friendsListAsLongs[index] != nameAsLong) {
                continue;
            }
            friendIndex = index;
            break;
        }
        if (friendIndex == -1) {
            pushMessage("That player is not on your friends list.", 0, "");
            return;
        }
        if (friendsNodeIDs[friendIndex] > 0) {
            inputTaken = true;
            inputState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 3;
            aLong953 = friendsListAsLongs[friendIndex];
            promptMessage = "Enter message to send to " + friendsList[friendIndex];
        } else {
            pushMessage("That player is currently offline.", 0, "");
        }
    }

    public void raiseWelcomeScreen() {
        welcomeScreenRaised = true;
    }

    private void parseRegionPackets(RSStream stream, int j) {
        if (j == 84) {
            int k = stream.getByte();
            int j3 = localX + (k >> 4 & 7);
            int i6 = localY + (k & 7);
            int l8 = stream.getShort();
            int k11 = stream.getShort();
            int l13 = stream.getShort();
            if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
                Deque class19_1 = groundItems[plane][j3][i6];
                if (class19_1 != null) {
                    for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1
                            .reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1
                            .reverseGetNext()) {
                        if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff)
                                || class30_sub2_sub4_sub2_3.anInt1559 != k11) {
                            continue;
                        }
                        class30_sub2_sub4_sub2_3.anInt1559 = l13;
                        break;
                    }

                    spawnGroundItem(j3, i6);
                }
            }
            return;
        }
        if (j == 105) {
            int l = stream.getByte();
            int k3 = localX + (l >> 4 & 7);
            int j6 = localY + (l & 7);
            int i9 = stream.getShort();
            int l11 = stream.getByte();
            int i14 = l11 >> 4 & 0xf;
            int i16 = l11 & 7;
            if (localPlayer.pathX[0] >= k3 - i14
                    && localPlayer.pathX[0] <= k3 + i14
                    && localPlayer.pathY[0] >= j6 - i14
                    && localPlayer.pathY[0] <= j6 + i14 && aBoolean848
                    && !lowMemory && trackCount < 50) {
                tracks[trackCount] = i9;
                trackLoops[trackCount] = i16;
                soundDelay[trackCount] = Track.delays[i9];
                trackCount++;
            }
        }
        if (j == 215) {
            int i1 = stream.getShortA();
            int l3 = stream.getByteS();
            int k6 = localX + (l3 >> 4 & 7);
            int j9 = localY + (l3 & 7);
            int i12 = stream.getShortA();
            int j14 = stream.getShort();
            if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104
                    && i12 != localPlayerIndex) {
                Item class30_sub2_sub4_sub2_2 = new Item();
                class30_sub2_sub4_sub2_2.ID = i1;
                class30_sub2_sub4_sub2_2.anInt1559 = j14;
                if (groundItems[plane][k6][j9] == null) {
                    groundItems[plane][k6][j9] = new Deque();
                }
                groundItems[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
                spawnGroundItem(k6, j9);
            }
            return;
        }
        if (j == 156) {
            int j1 = stream.getByteA();
            int i4 = localX + (j1 >> 4 & 7);
            int l6 = localY + (j1 & 7);
            int k9 = stream.getShort();
            if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
                Deque class19 = groundItems[plane][i4][l6];
                if (class19 != null) {
                    for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19
                            .reverseGetNext()) {
                        if (item.ID != (k9 & 0x7fff)) {
                            continue;
                        }
                        item.unlink();
                        break;
                    }

                    if (class19.reverseGetFirst() == null) {
                        groundItems[plane][i4][l6] = null;
                    }
                    spawnGroundItem(i4, l6);
                }
            }
            return;
        }
        if (j == 160) {
            int k1 = stream.getByteS();
            int j4 = localX + (k1 >> 4 & 7);
            int i7 = localY + (k1 & 7);
            int l9 = stream.getByteS();
            int j12 = l9 >> 2;
            int k14 = l9 & 3;
            int j16 = anIntArray1177[j12];
            int j17 = stream.getShortA();
            if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
                int j18 = tileHeights[plane][j4][i7];
                int i19 = tileHeights[plane][j4 + 1][i7];
                int l19 = tileHeights[plane][j4 + 1][i7 + 1];
                int k20 = tileHeights[plane][j4][i7 + 1];
                if (j16 == 0) {
                    Wall class10 = scene.getWall(plane, j4, i7);
                    if (class10 != null) {
                        int k21 = class10.uid >> 14 & 0x7fff;
                        if (j12 == 2) {
                            class10.node1 = new SceneObject(
                                    k21, 4 + k14, 2, i19, l19, j18, k20, j17,
                                    false);
                            class10.node2 = new SceneObject(
                                    k21, k14 + 1 & 3, 2, i19, l19, j18, k20,
                                    j17, false);
                        } else {
                            class10.node1 = new SceneObject(
                                    k21, k14, j12, i19, l19, j18, k20, j17,
                                    false);
                        }
                    }
                }
                if (j16 == 1) {
                    WallDecoration class26 = scene.getWallDecoration(j4, i7, plane);
                    if (class26 != null) {
                        class26.node = new SceneObject(
                                class26.uid >> 14 & 0x7fff, 0, 4, i19, l19,
                                j18, k20, j17, false);
                    }
                }
                if (j16 == 2) {
                    StaticObject class28 = scene.getStaticObject(j4, i7, plane);
                    if (j12 == 11) {
                        j12 = 10;
                    }
                    if (class28 != null) {
                        class28.node = new SceneObject(
                                class28.uid >> 14 & 0x7fff, k14, j12, i19, l19,
                                j18, k20, j17, false);
                    }
                }
                if (j16 == 3) {
                    GroundDecoration class49 = scene.getGroundDecoration(i7, j4, plane);
                    if (class49 != null) {
                        class49.node = new SceneObject(
                                class49.uid >> 14 & 0x7fff, k14, 22, i19, l19,
                                j18, k20, j17, false);
                    }
                }
            }
            return;
        }
        if (j == 147) {
            int l1 = stream.getByteS();
            int k4 = localX + (l1 >> 4 & 7);
            int j7 = localY + (l1 & 7);
            int i10 = stream.getShort();
            byte byte0 = stream.readByteS();
            int l14 = stream.readLEUShort();
            byte byte1 = stream.readNegByte();
            int k17 = stream.getShort();
            int k18 = stream.getByteS();
            int j19 = k18 >> 2;
            int i20 = k18 & 3;
            int l20 = anIntArray1177[j19];
            byte byte2 = stream.getSignedByte();
            int l21 = stream.getShort();
            byte byte3 = stream.readNegByte();
            Player player;
            if (i10 == localPlayerIndex) {
                player = localPlayer;
            } else {
                player = players[i10];
            }
            if (player != null) {
                ObjectDefinition objectDefinition = ObjectDefinition.forId(l21);
                int i22 = tileHeights[plane][k4][j7];
                int j22 = tileHeights[plane][k4 + 1][j7];
                int k22 = tileHeights[plane][k4 + 1][j7 + 1];
                int l22 = tileHeights[plane][k4][j7 + 1];
                Model model = objectDefinition.modelAt(j19, i20, i22, j22, k22, l22, -1);
                if (model != null) {
                    spawnObject(k17 + 1, -1, 0, l20, j7, 0, plane, k4, l14 + 1);
                    player.anInt1707 = l14 + loopCycle;
                    player.anInt1708 = k17 + loopCycle;
                    player.playerModel = model;
                    int i23 = objectDefinition.sizeX;
                    int j23 = objectDefinition.sizeY;
                    if (i20 == 1 || i20 == 3) {
                        i23 = objectDefinition.sizeY;
                        j23 = objectDefinition.sizeX;
                    }
                    player.anInt1711 = k4 * 128 + i23 * 64;
                    player.anInt1713 = j7 * 128 + j23 * 64;
                    player.anInt1712 = getFloorDrawHeight(plane, player.anInt1713,
                            player.anInt1711);
                    if (byte2 > byte0) {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if (byte3 > byte1) {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = k4 + byte2;
                    player.anInt1721 = k4 + byte0;
                    player.anInt1720 = j7 + byte3;
                    player.anInt1722 = j7 + byte1;
                }
            }
        }
        if (j == 151) {
            int i2 = stream.getByteA();
            int l4 = localX + (i2 >> 4 & 7);
            int k7 = localY + (i2 & 7);
            int j10 = stream.readLEUShort();
            int k12 = stream.getByteS();
            int i15 = k12 >> 2;
            int k16 = k12 & 3;
            int l17 = anIntArray1177[i15];
            if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104) {
                spawnObject(-1, j10, k16, l17, k7, i15, plane, l4, 0);
            }
            return;
        }
        if (j == 4) {
            int locationHash = stream.getByte();
            int i5 = localX + (locationHash >> 4 & 7);
            int l7 = localY + (locationHash & 7);
            int k10 = stream.getShort();
            int l12 = stream.getByte();
            int j15 = stream.getShort();
            if (i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
                i5 = i5 * 128 + 64;
                l7 = l7 * 128 + 64;
                SceneSpotAnim class30_sub2_sub4_sub3 = new SceneSpotAnim(plane,
                        loopCycle, j15, k10, getFloorDrawHeight(plane, l7, i5) - l12, l7,
                        i5);
                incompleteAnimables.insertHead(class30_sub2_sub4_sub3);
            }
            return;
        }
        if (j == 44) {
            int k2 = stream.readLEUShortA();
            int j5 = stream.getShort();
            int i8 = stream.getByte();
            int l10 = localX + (i8 >> 4 & 7);
            int i13 = localY + (i8 & 7);
            if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
                Item class30_sub2_sub4_sub2_1 = new Item();
                class30_sub2_sub4_sub2_1.ID = k2;
                class30_sub2_sub4_sub2_1.anInt1559 = j5;
                if (groundItems[plane][l10][i13] == null) {
                    groundItems[plane][l10][i13] = new Deque();
                }
                groundItems[plane][l10][i13]
                        .insertHead(class30_sub2_sub4_sub2_1);
                spawnGroundItem(l10, i13);
            }
            return;
        }
        if (j == 101) {
            int l2 = stream.getByteC();
            int k5 = l2 >> 2;
            int j8 = l2 & 3;
            int i11 = anIntArray1177[k5];
            int j13 = stream.getByte();
            int k15 = localX + (j13 >> 4 & 7);
            int l16 = localY + (j13 & 7);
            if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104) {
                spawnObject(-1, -1, j8, i11, l16, k5, plane, k15, 0);
            }
            return;
        }
        if (j == 117) {
            int i3 = stream.getByte();
            int l5 = localX + (i3 >> 4 & 7);
            int k8 = localY + (i3 & 7);
            int j11 = l5 + stream.getSignedByte();
            int k13 = k8 + stream.getSignedByte();
            int l15 = stream.getSignedShort();
            int i17 = stream.getShort();
            int i18 = stream.getByte() * 4;
            int l18 = stream.getByte() * 4;
            int k19 = stream.getShort();
            int j20 = stream.getShort();
            int start_slope = stream.getByte();
            int start_distance = stream.getByte();
            if (l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0
                    && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535) {
                l5 = l5 * 128 + 64;
                k8 = k8 * 128 + 64;
                j11 = j11 * 128 + 64;
                k13 = k13 * 128 + 64;
                SceneProjectile class30_sub2_sub4_sub4 = new SceneProjectile(
                        start_slope, l18, k19 + loopCycle, j20 + loopCycle, start_distance, plane,
                        getFloorDrawHeight(plane, k8, l5) - i18, k8, l5, l15, i17);
                class30_sub2_sub4_sub4.method455(k19 + loopCycle, k13,
                        getFloorDrawHeight(plane, k13, j11) - l18, j11);
                projectiles.insertHead(class30_sub2_sub4_sub4);
            }
        }
    }

    private void method139(RSStream stream) {
        stream.initBitAccess();
        int k = stream.readBits(8);
        if (k < npcCount) {
            for (int l = k; l < npcCount; l++) {
                anIntArray840[anInt839++] = npcIndices[l];
            }

        }
        if (k > npcCount) {
            SignLink.reporterror(loginUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        npcCount = 0;
        for (int i1 = 0; i1 < k; i1++) {
            int j1 = npcIndices[i1];
            NPC npc = npcs[j1];
            int k1 = stream.readBits(1);
            if (k1 == 0) {
                npcIndices[npcCount++] = j1;
                npc.anInt1537 = loopCycle;
            } else {
                int l1 = stream.readBits(2);
                if (l1 == 0) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                } else if (l1 == 1) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    int i2 = stream.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = stream.readBits(1);
                    if (k2 == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                    }
                } else if (l1 == 2) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    int j2 = stream.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = stream.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = stream.readBits(1);
                    if (i3 == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                    }
                } else if (l1 == 3) {
                    anIntArray840[anInt839++] = j1;
                }
            }
        }

    }

    public void drawLoginScreen(boolean worldSelect) {
        setupLoginScreen();
        if (!worldSelect) {
            titleScreen.initDrawingArea();
            setLoadingAndLoginHovers();
            this.background.drawAdvancedSprite(0, 0);
            this.logo.drawAdvancedSprite(80, 30);
            this.loginBox.drawAdvancedSprite(212, 173);

            this.usernameInputBox.drawAdvancedSprite(231, 220);
            this.passwordInputBox.drawAdvancedSprite(231, 255);

            this.loginMessage.drawAdvancedSprite(428, 222);

            loginScreenAccessories();
            if (!loggingIn) {
                smallFont.drawCenteredString(resourceProvider.loadingMessage, 332, 330, 0x75A9A9, 0);
                if (loginButtonHover) {
                    this.loginButtonHoverSprite.drawAdvancedSprite(275, 317);
                } else {
                    this.loginButton.drawAdvancedSprite(275, 317);
                }
            }
            int messageX = 382;
            int messageY = 208;
            if (loginMessage1.length() > 0) {
                bigArialFont.drawCenteredString(loginMessage1, messageX, messageY - 15, 0xb6b6b6, 0);
                bigArialFont.drawCenteredString(loginMessage3, messageX, messageY + 8, 0xb6b6b6, 0);
            } else {
                bigArialFont.drawCenteredString(loginMessage2, messageX, messageY - 7, 0xb6b6b6, 0);
            }
            if (rememberMe) {
                this.rememberOn.drawAdvancedSprite(231, 286);
            } else {
                this.rememberOff.drawAdvancedSprite(231, 286);
            }

            String usernameText = /*"Login: " + */loginUsername + ((loginScreenCursorPos == 0) & (loopCycle % 40 < 20) ? "<col=FFFFFF>|" : "");
            boldFont.drawBasicString(usernameText, 240, 240, 0xb6b6b6, 0);
            String passwordText = /*"Password: " +*/ StringUtility.passwordAsterisks(loginPassword) + ((loginScreenCursorPos == 1) & (loopCycle % 40 < 20) ? "<col=FFFFFF>|" : "");
            boldFont.drawBasicString(passwordText, 240, 274, 0xb6b6b6, 0);
            smallArialFont.drawBasicString("Remember me?", 247, 295, 0x666565, 0);
            smallArialFont.drawBasicString("Lost password?", 336, 295, 0x666565, 0);
            titleScreen.drawGraphics(0, super.graphics, 0);
        }
    }


    public void loginScreenAccessories() {
        /**
         * World-selection
         */
        this.worldSwitch.drawSprite(6, 463);
        boldFont.drawCenteredString("World " + worldDefinition.getId(), 55, 479, 0xFFFFFF, 0);
        smallFont.drawCenteredString("Click to switch", 56, 493, 0xFFFFFF, 0);
        Constants.SERVER_ADDRESS = worldDefinition.getAddress();
        Constants.SERVER_PORT = worldDefinition.getPort();
        worldId = worldDefinition.getId() + 9;
    }

    private void processLoginScreenInput() {
        if (super.clickMode3 == 1) {
            if (worldSelect) {
                int xOffset = 0;
                int yOffset = 0;
                int index = 0;
                for (WorldDefinition worldDefinition : WorldDefinition.getWorldList()) {
                    boolean hover = (mouseX > (199 + xOffset) && mouseX < (199 + xOffset + 88)
                            && mouseY > (49 + yOffset) && mouseY <= (49 + yOffset + 19));
                    if (hover) {
                        this.worldDefinition = worldDefinition;
                        drawWorldSelect(false);
                        break;
                    }
                    yOffset += 24;
                    index++;
                    if (index % 18 == 0) {
                        yOffset = 0;
                        xOffset += 94;
                    }
                }
                if (super.saveClickY >= 4 && super.saveClickY <= 18) {
                    if (super.saveClickX >= 280 && super.saveClickX <= 294) {
                        WorldDefinition.sort("WORLD", "ASC");
                        return;
                    }
                    if (super.saveClickX >= 296 && super.saveClickX <= 309) {
                        WorldDefinition.sort("WORLD", "DESC");
                        return;
                    }

                    if (super.saveClickX >= 390 && super.saveClickX <= 404) {
                        WorldDefinition.sort("PLAYERS", "ASC");
                        return;
                    }
                    if (super.saveClickX >= 406 && super.saveClickX <= 419) {
                        WorldDefinition.sort("PLAYERS", "DESC");
                        return;
                    }

                    if (super.saveClickX >= 500 && super.saveClickX <= 514) {
                        WorldDefinition.sort("LOCATION", "ASC");
                        return;
                    }
                    if (super.saveClickX >= 516 && super.saveClickX <= 529) {
                        WorldDefinition.sort("LOCATION", "DESC");
                        return;
                    }

                    if (super.saveClickX >= 611 && super.saveClickX <= 624) {
                        WorldDefinition.sort("TYPE", "ASC");
                        return;
                    }
                    if (super.saveClickX >= 626 && super.saveClickX <= 639) {
                        WorldDefinition.sort("TYPE", "DESC");
                        return;
                    }
                    if (super.saveClickX >= 708 && super.saveClickX <= 758) {
                        drawWorldSelect(false);
                        return;
                    }
                }
            } else {
                if (super.saveClickX >= 7
                        && super.saveClickX <= 104 && super.saveClickY >= 464
                        && super.saveClickY <= 493) {
                    requestWorlds();
                    drawWorldSelect(true);
                    return;
                }
            }
            if (input1Hover) {
                loginScreenCursorPos = 0;
            } else if (input2Hover) {
                loginScreenCursorPos = 1;
            } else if (loginButtonHover) {
                if (loginUsername.length() < 1) {
                    loginMessage3 = "";
                    loginMessage2 = "Please enter your username.";
                    loginMessage1 = "";
                    return;
                }
                if (loginPassword.length() < 1) {
                    loginMessage3 = "";
                    loginMessage2 = "Please enter your password.";
                    loginMessage1 = "";
                    return;
                }
                login(loginUsername, loginPassword, false);
            } else if (rememberMeButtonHover) {
                rememberMe = !rememberMe;
                clientSetting.save();
            } else if (super.mouseX >= 336 && super.mouseX <= 412 && super.mouseY >= 288 && super.mouseY <= 297) {
                Constants.loadURL(Constants.FORGOTTEN_PASSWORD_URL);
            }
            if (loggedIn) {
                return;
            }
        }
        if (loading) {
            return;
        }
        do {
            int keyCode = getKeyCode();
            if (keyCode == -1) {
                break;
            }
            boolean flag1 = false;
            for (int i2 = 0;
                 i2 < validUserPassChars.length();
                 i2++) {
                if (keyCode != validUserPassChars.charAt(i2)) {
                    continue;
                }
                flag1 = true;
                break;
            }

            if (loginScreenCursorPos == 0) {
                if (keyCode == 8 && loginUsername.length() > 0) {
                    loginUsername = loginUsername.substring(0,
                            loginUsername.length() - 1);
                }
                if (keyCode == 9 || keyCode == 10 || keyCode == 13) {
                    loginScreenCursorPos = 1;
                }
                if (flag1) {
                    loginUsername += (char) keyCode;
                }
                if (loginUsername.length() > 12) {
                    loginUsername = loginUsername.substring(0, 12);
                }
            } else if (loginScreenCursorPos == 1) {
                if (keyCode == 8 && loginPassword.length() > 0) {
                    loginPassword = loginPassword.substring(0,
                            loginPassword.length() - 1);
                }
                if (keyCode == 9 || keyCode == 10 || keyCode == 13 && !loading && loadTime > (System.currentTimeMillis() + 3000)) {
                    if (loginUsername.length() > 0 && loginPassword.length() > 0 && keyCode != 9) {
                        login(loginUsername, loginPassword, false);
                    } else {
                        loginScreenCursorPos = 0;
                    }
                }
                if (flag1) {
                    loginPassword += (char) keyCode;
                }
                if (loginPassword.length() > 20) {
                    loginPassword = loginPassword.substring(0, 20);
                }
            }
        } while (true);
    }

    private void removeObject(int y, int z, int k, int l, int x, int group,
                              int previousId) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (lowMemory && z != plane) {
                return;
            }
            int key = 0;
            if (group == 0) {
                key = scene.getWallKey(z, x, y);
            }
            if (group == 1) {
                key = scene.getWallDecorationKey(z, x, y);
            }
            if (group == 2) {
                key = scene.getStaticObjectKey(z, x, y);
            }
            if (group == 3) {
                key = scene.getFloorDecorationKey(z, x, y);
            }
            if (key != 0) {
                int config = scene.getIDTagForXYZ(z, x, y, key);
                int id = key >> 14 & 0x7fff;
                int objectType = config & 0x1f;
                int orientation = config >> 6;
                if (group == 0) {
                    scene.removeWall(x, z, y);
                    ObjectDefinition objectDef = ObjectDefinition.forId(id);
                    if (objectDef.projectileClipped) {
                        collisionMaps[z].removeObject(orientation, objectType,
                                objectDef.impenetrable, x, y);
                    }
                }
                if (group == 1) {
                    scene.removeWallDecoration(y, z, x);
                }
                if (group == 2) {
                    scene.removeStaticObject(z, x, y);
                    ObjectDefinition objectDef = ObjectDefinition.forId(id);
                    if (x + objectDef.sizeX > 103 || y + objectDef.sizeX > 103
                            || x + objectDef.sizeY > 103
                            || y + objectDef.sizeY > 103) {
                        return;
                    }
                    if (objectDef.projectileClipped) {
                        collisionMaps[z].removeObject(orientation,
                                objectDef.sizeX, x, y, objectDef.sizeY,
                                objectDef.impenetrable);
                    }
                }
                if (group == 3) {
                    scene.removeGroundDecoration(z, y, x);
                    ObjectDefinition objectDef = ObjectDefinition.forId(id);
                    if (objectDef.projectileClipped && objectDef.isInteractive) {
                        collisionMaps[z].removeFloorDecoration(y, x);
                    }
                }
            }
            if (previousId >= 0) {
                int plane = z;
                if (plane < 3 && (tileFlags[1][x][y] & 2) == 2) {
                    plane++;
                }
                MapRegion.placeObject(scene, k, y, l, plane, collisionMaps[z],
                        tileHeights, x, previousId, z);
            }
        }
    }

    private void updatePlayers(int packetSize, RSStream stream) {
        anInt839 = 0;
        mobsAwaitingUpdateCount = 0;
        updatePlayerMovement(stream);
        method134(stream);
        updateOtherPlayerMovement(stream, packetSize);
        refreshUpdateMasks(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (players[l].anInt1537 != loopCycle) {
                players[l] = null;
            }
        }

        if (stream.currentPosition != packetSize) {
            SignLink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentPosition + " expected packet size:" + packetSize + " opcode: " + opcode);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < playerCount; i1++) {
            if (players[playerIndices[i1]] == null) {
                SignLink.reporterror(loginUsername
                        + " null entry in pl list - pos:" + i1 + " size:"
                        + playerCount);
                throw new RuntimeException("eek");
            }
        }

    }

    private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
        int l1 = 2048 - k & 0x7ff;
        int i2 = 2048 - j1 & 0x7ff;
        int j2 = 0;
        int k2 = 0;
        int l2 = j;
        if (l1 != 0) {
            int i3 = Model.SINE[l1];
            int k3 = Model.COSINE[l1];
            int i4 = k2 * k3 - l2 * i3 >> 16;
            l2 = k2 * i3 + l2 * k3 >> 16;
            k2 = i4;
        }
        if (i2 != 0) {
            int j3 = Model.SINE[i2];
            int l3 = Model.COSINE[i2];
            int j4 = l2 * j3 + j2 * l3 >> 16;
            l2 = l2 * l3 - j2 * j3 >> 16;
            j2 = j4;
        }
        xCameraPos = l - j2;
        zCameraPos = i1 - k2;
        yCameraPos = k1 - l2;
        yCameraCurve = k;
        xCameraCurve = j1;
    }

    /**
     * This method updates default messages upon login to the desired text of
     * the interface text.
     */
    public void updateStrings(String message, int index) {
        switch (index) {
            case 1675:
                sendString(message, 17508);
                break;// Stab
            case 1676:
                sendString(message, 17509);
                break;// Slash
            case 1677:
                sendString(message, 17510);
                break;// Crush
            case 1678:
                sendString(message, 17511);
                break;// Magic
            case 1679:
                sendString(message, 17512);
                break;// Range
            case 1680:
                // sendString(message, 17513);
                break;// Stab
            case 1681:
                // sendString(message, 17514);
                break;// Slash
            case 1682:
                // sendString(message, 17515);
                break;// Crush
            case 1683:
                sendString(message, 17516);
                break;// Magic
            case 1684:
                sendString(message, 17517);
                break;// Range
            case 1686:
                sendString(message, 17518);
                break;// Strength
            case 1687:
                sendString(message, 17519);
                break;// Prayer
        }
    }

    /**
     * Sends a string
     */
    public void sendString(String text, int index) {
        RSComponent.forId(index).disabledMessage = text;
        if (RSComponent.forId(index).parentId == tabInterfaceIDs[currentTabId]) {
        }
    }

    public void sendButtonClick(int button, int toggle, int type) {
        RSComponent widget = RSComponent.forId(button);
        switch (type) {
            case 135:
                boolean flag8 = true;
                if (widget.contentType > 0) {
                    flag8 = promptUserForInput(widget);
                }
                if (flag8) {
                    outgoing.putOpcode(185);
                    outgoing.putShort(button);
                }
                break;
            case 646:
                outgoing.putOpcode(185);
                outgoing.putShort(button);

                if (widget.scripts != null && widget.scripts[0][0] == 5) {
                    if (settings[toggle] != widget.scriptDefaults[0]) {
                        settings[toggle] = widget.scriptDefaults[0];
                        adjustVolume(toggle);
                    }
                }
                break;
            case 169:
                outgoing.putOpcode(185);
                outgoing.putShort(button);
                if (widget.scripts != null && widget.scripts[0][0] == 5) {
                    settings[toggle] = 1 - settings[toggle];
                    adjustVolume(toggle);
                }
                break;
        }
    }

    /**
     * Sets button configurations on interfaces.
     */
    public void sendConfiguration(int id, int state) {
        anIntArray1045[id] = state;
        if (settings[id] != state) {
            settings[id] = state;
            adjustVolume(id);
            if (dialogueId != -1) {
                inputTaken = true;
            }
        }
    }

    /**
     * Displays an interface over the sidebar area.
     */
    public void inventoryOverlay(int interfaceId, int sideInterfaceId) {
        if (backDialogueId != -1) {
            backDialogueId = -1;
            inputTaken = true;
        }
        if (inputState != 0) {
            inputState = 0;
            inputTaken = true;
        }
        openInterfaceId = interfaceId;
        overlayInterfaceId = sideInterfaceId;
        tabAreaAltered = true;
        continuedDialogue = false;
    }

    private boolean parsePacket() {
        if (socketStream == null) {
            return false;
        }
        try {
            int available = socketStream.available();
            if (available == 0) {
                return false;
            }
            if (opcode == -1) {
                socketStream.flushInputStream(inStream.payload, 1);
                opcode = inStream.payload[0] & 0xff;
//                if (encryption != null) {
//                    opcode = opcode - encryption.getNextKey() & 0xff;
//                }
                packetSize = PacketConstants.PACKET_SIZES[opcode];
                available--;
            }
            if (packetSize == -1) {
                if (available > 0) {
                    socketStream.flushInputStream(inStream.payload, 1);
                    packetSize = inStream.payload[0] & 0xff;
                    available--;
                } else {
                    return false;
                }
            }
            if (packetSize == -2) {
                if (available > 1) {
                    socketStream.flushInputStream(inStream.payload, 2);
                    inStream.currentPosition = 0;
                    packetSize = inStream.getShort();
                    available -= 2;
                } else {
                    return false;
                }
            }
            if (available < packetSize) {
                return false;
            }
            inStream.currentPosition = 0;
            socketStream.flushInputStream(inStream.payload, packetSize);
            timeoutCounter = 0;
            thirdLastOpcode = secondLastOpcode;
            secondLastOpcode = lastOpcode;
            lastOpcode = opcode;
            Pattern pattern = null;
            Matcher matcher = null;
            if (opcode == 224) {
                long clanOwnerName = inStream.getLong();
                if (clanOwnerName == 0) {
                    setClanChatMembers(null);
                    setChatOwnerName(null);
                    setClanChatName(null);
                    setClanChatSize(0);
                    updateClanChat();
                    opcode = -1;
                    return true;
                }
                long clanName = inStream.getLong();
                setClanChatName(StringUtility.decodeBase37(clanName));
                setChatOwnerName(StringUtility.decodeBase37(clanOwnerName));
                setChatKickRights((byte) inStream.getByte());
                int chatSize = (byte) inStream.getByte();
                if (chatSize == 255) {
                    updateClanChat();
                    opcode = -1;
                    return true;
                }
                setClanChatSize(chatSize);
                ClanChatMember[] clanChatMembers1 = new ClanChatMember[100];
                for (int memberId = 0; memberId < getClanChatSize(); memberId++) {
                    long playerName = inStream.getLong();
                    short worldId = (short) inStream.getShort();
                    byte rankId = (byte) inStream.getByte();
                    String worldString = inStream.getString();
                    int otherPrivilege = inStream.getByte();
                    clanChatMembers1[memberId] = new ClanChatMember(playerName, rankId, worldId, worldString, otherPrivilege);
                    if (StringUtility.encodeBase37(localPlayer.name) == clanChatMembers1[memberId].getName()) {
                        setCurrentUserClanRights(clanChatMembers1[memberId].getRankId());
                    }
                }
                boolean membersAdded = false;
                int clanSize = getClanChatSize();
                while (clanSize > 0) {
                    clanSize--;
                    membersAdded = true;
                    for (int size = 0; size < clanSize; size++) {
                        if ((StringUtility.method151(StringUtility.decodeBase37(clanChatMembers1[size].getName()),
                                StringUtility.decodeBase37(clanChatMembers1[size + 1].getName())) ^ 0xFFFFFFff) < -1) {
                            membersAdded = false;
                            ClanChatMember nextPlayer = clanChatMembers1[size];
                            clanChatMembers1[size] = clanChatMembers1[size - -1];
                            clanChatMembers1[size + 1] = nextPlayer;
                        }
                    }
                    if (membersAdded) {
                        break;
                    }
                }
                setClanChatMembers(clanChatMembers1);
                updateClanChat();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAYER_UPDATING) {
                updatePlayers(packetSize, inStream);
                validLocalMap = false;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.OPEN_WELCOME_SCREEN) {
                daysSinceRecovChange = inStream.getByteC();
                /* unread messages */  inStream.getShortA();
                membersInt = inStream.getByte();
                anInt1193 = inStream.readIMEInt();
                daysSinceLastLogin = inStream.getShort();
                if (anInt1193 != 0 && openInterfaceId == -1) {
                    SignLink.dnslookup(StringUtility.decodeIp(anInt1193));
                    clearTopInterfaces();
                    char character = '\u028A';
                    if (daysSinceRecovChange != 201 || membersInt == 1) {
                        character = '\u028F';
                    }
                    reportAbuseInput = "";
                    canMute = false;
                    for (int interfaceId = 0; interfaceId < RSComponent.getComponentCache().length; interfaceId++) {
                        if (RSComponent.forId(interfaceId) == null
                                || RSComponent.forId(interfaceId).contentType != character) {
                            continue;
                        }
                        openInterfaceId = RSComponent.forId(interfaceId).parentId;

                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.DELETE_GROUND_ITEM) {
                localX = inStream.getByteC();
                localY = inStream.getByteS();
                for (int x = localX; x < localX + 8; x++) {
                    for (int y = localY; y < localY + 8; y++) {
                        if (groundItems[plane][x][y] != null) {
                            groundItems[plane][x][y] = null;
                            spawnGroundItem(x, y);
                        }
                    }
                }
                for (SpawnedObject object = (SpawnedObject) spawns
                        .reverseGetFirst(); object != null; object = (SpawnedObject) spawns
                        .reverseGetNext()) {
                    if (object.tileX >= localX && object.tileX < localX + 8
                            && object.tileY >= localY && object.tileY < localY + 8
                            && object.plane == plane) {
                        object.getLongetivity = 0;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_PLAYER_HEAD_ON_INTERFACE) {
                int componentId = inStream.readLEUShortA();
                RSComponent rsComponent = RSComponent.forId(componentId);
                rsComponent.mediaType = 3;
                // TODO Item / Object
                if (localPlayer.playerNPCDefinition == null) {
                    rsComponent.mediaID = (localPlayer.anIntArray1700[0] << 25)
                            + (localPlayer.anIntArray1700[4] << 20)
                            + (localPlayer.equipment[0] << 15)
                            + (localPlayer.equipment[8] << 10)
                            + (localPlayer.equipment[11] << 5)
                            + localPlayer.equipment[1];
                    rsComponent.modelZoom = 2000;
                } else {
                    RSComponent.forId(componentId).mediaID = (int) (0x12345678L + localPlayer.playerNPCDefinition.npcId);
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.RESET_CAMERA) {
                oriented = false;
                for (int l = 0; l < 5; l++) {
                    cameraViews[l] = false;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLEAN_ITEMS_OF_INTERFACE) {
                int id = inStream.readLEUShort();
                RSComponent widget = RSComponent.forId(id);
                for (int slot = 0; slot < widget.inventory.length; slot++) {
                    widget.inventory[slot] = -1;
                    widget.inventory[slot] = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_IGNORE_NAMES) {
                ignoreCount = packetSize / 8;
                for (int index = 0; index < ignoreCount; index++) {
                    ignores[index] = inStream.getLong();
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SPIN_CAMERA) {
                oriented = true;
                x = inStream.getByte();
                y = inStream.getByte();
                height = inStream.getShort();
                speed = inStream.getByte();
                angle = inStream.getByte();
                if (angle >= 100) {
                    xCameraPos = x * 128 + 64;
                    yCameraPos = y * 128 + 64;
                    zCameraPos = getFloorDrawHeight(plane, yCameraPos, xCameraPos)
                            - height;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_SKILL) {
                int skill = inStream.getByte();
                int experience = inStream.getIntA();
                int level = inStream.getByte();
                inStream.getByte(); // "XP drop"
                if (skill < currentExp.length) {
                    currentExp[skill] = experience;
                    currentStats[skill] = level;
                    maximumLevels[skill] = 1;
                    for (int index = 0; index < 98; index++) {
                        if (experience >= SKILL_EXPERIENCE[index]) {
                            maximumLevels[skill] = index + 2;
                        }
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 144) {
                int skill = inStream.getByte();
                long experience = inStream.getLong();
                boolean totalValue = inStream.getByte() == 1;
                if (totalValue) {
                    xpDropped = experience;
                    opcode = -1;
                    return true;
                }
                if (floatingDrops[skill] > 1) {
                    floatingDrops[skill] = 0;
                    xpDropPosition[skill] = 0;
                    xpDrawing[skill] = 0;
                }
                xpDrops[skill] = (int) experience;
                if (interfaceConfig[200] == 0) {
                    xpDropped += experience;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLAN_CHAT) {
                long name = inStream.getLong();
                inStream.getSignedByte();
                long clanChatName = inStream.getLong();
                inStream.getShort();
                inStream.getTri();

                int[] ccChatIcons = new int[8];
                for (int i = 0; i < ccChatIcons.length; i++) {
                    ccChatIcons[i] = inStream.getByte();
                    ccChatIcons[i] = ccChatIcons[i] == 255 ? -1 : ccChatIcons[i];
                }

                String clanMessage = inStream.getString();
                if (settings[311] == 1) {
                    clanMessage = MessageCensor.apply(clanMessage);
                }
                pushMessage(new ChatMessage(16, StringUtility.decodeBase37(name), clanMessage, StringUtility.decodeBase37(clanChatName), ccChatIcons));
                opcode = -1;
                return true;
            }
            if (opcode == PacketConstants.GRAND_EXCHANGE_OFFER) {
                int offerIndex = (inStream.getByte() - 1);
                if (inStream.getByte() == 0) {
                    grandExchange.reset(offerIndex);
                    opcode = -1;
                    return true;
                }
                inStream.currentPosition--;
                grandExchange.setGrandExchangeOffer(offerIndex, new GrandExchangeOffer(inStream));
                opcode = -1;
                return true;
            }
            // todo 317 launchURL
            if (opcode == 135) {
                String urlMessage = inStream.getString();
                pattern = Pattern.compile("href=\"(.*?)\"");
                matcher = pattern.matcher(urlMessage);
                if (!matcher.find()) {
                    opcode = -1;
                    return true;
                }
                String url = matcher.group(0).replaceAll("href=\"", "").replaceAll("\"", "");
                boolean launch = inStream.getSignedByte() == 1;
                if (launch) {
                    Constants.loadURL(url);
                    opcode = -1;
                    return true;
                }
                // TODO
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_SIDE_TAB) {
                int id = inStream.getShort();
                int tab = inStream.getByteA();
                if (id == 65535) {
                    id = -1;
                }
                tabInterfaceIDs[tab] = id;
                tabAreaAltered = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAY_SONG) {
                int id = inStream.readLEUShort();
                if (id == 65535) {
                    id = -1;
                }
                if (id != currentSong && Constants.enableMusic && !lowMemory
                        && prevSong == 0) {
                    nextSong = id;
                    fadeMusic = true;
                    resourceProvider.provide(2, nextSong);
                }
                currentSong = id;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.NEXT_OR_PREVIOUS_SONG) {
                int id = inStream.readLEUShortA();
                int delay = inStream.getShortA();
                if (Constants.enableMusic && !lowMemory) {
                    nextSong = id;
                    fadeMusic = false;
                    resourceProvider.provide(2, nextSong);
                    prevSong = delay;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.LOGOUT) {
                resetLogout();
                opcode = -1;
                return false;
            }

            if (opcode == PacketConstants.MOVE_COMPONENT) { // TODO Server side - RepositionChild
                int horizontalOffset = inStream.getSignedShort();
                int verticalOffset = inStream.getLEShort();
                int id = inStream.readLEUShort();
                RSComponent widget = RSComponent.forId(id);
                widget.xOffset = horizontalOffset;
                widget.yOffset = verticalOffset;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MAP_REGION
                    || opcode == PacketConstants.SEND_REGION_MAP_REGION) {
                int regionX = currentRegionX;
                int regionY = currentRegionY;
                if (opcode == 73) {
                    regionX = inStream.getShortA();
                    regionY = inStream.getShort();
                    constructedViewport = false;
                }
                if (opcode == 241) {
                    regionY = inStream.getShortA();
                    inStream.initBitAccess();
                    for (int j16 = 0; j16 < 4; j16++) {
                        for (int l20 = 0; l20 < 13; l20++) {
                            for (int j23 = 0; j23 < 13; j23++) {
                                int i26 = inStream.readBits(1);
                                if (i26 == 1) {
                                    anIntArrayArrayArray1129[j16][l20][j23] = inStream.readBits(26);
                                } else {
                                    anIntArrayArrayArray1129[j16][l20][j23] = -1;
                                }
                            }
                        }
                    }
                    inStream.finishBitAccess();
                    regionX = inStream.getShort();
                    constructedViewport = true;
                }
                if (currentRegionX == regionX && currentRegionY == regionY
                        && loadingStage == 2) {
                    opcode = -1;
                    return true;
                }
                currentRegionX = regionX;
                currentRegionY = regionY;
                regionBaseX = (currentRegionX - 6) * 8;
                regionBaseY = (currentRegionY - 6) * 8;
                inPlayerOwnedHouse = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49)
                        && currentRegionY / 8 == 48;
                if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148) {
                    inPlayerOwnedHouse = true;
                }
                loadingStage = 1;
                aLong824 = System.currentTimeMillis();
                gameframe.getGameScreenImageProducer().initDrawingArea();
                int todo = resourceProvider.remaining();
                if (todo > highestAmtToLoad) {
                    highestAmtToLoad = todo;
                }
                if (gameframe.getGameScreenImageProducer() != null) {
                    gameframe.getGameScreenImageProducer().drawGraphics(isScreenMode(ScreenMode.FIXED) ? 4 : 0, super.graphics, isScreenMode(ScreenMode.FIXED) ? 4 : 0);
                }
                int percent = (100 - todo);
                drawLoadingMessages(1, "Loading - please wait.", ((percent <= 100 && percent >= 0) ? "(" + percent + "%)" : "(100%)"));
                gameframe.getGameScreenImageProducer().drawGraphics(
                        frameMode == ScreenMode.FIXED ? 4 : 0, super.graphics,
                        frameMode == ScreenMode.FIXED ? 4 : 0);
                if (opcode == 73) {
                    int k16 = 0;
                    for (int i21 = (currentRegionX - 6) / 8; i21 <= (currentRegionX + 6) / 8; i21++) {
                        for (int k23 = (currentRegionY - 6) / 8; k23 <= (currentRegionY + 6) / 8; k23++) {
                            k16++;
                        }
                    }
                    aByteArrayArray1183 = new byte[k16][];
                    aByteArrayArray1247 = new byte[k16][];
                    anIntArray1234 = new int[k16];
                    anIntArray1235 = new int[k16];
                    anIntArray1236 = new int[k16];
                    k16 = 0;
                    for (int l23 = (currentRegionX - 6) / 8; l23 <= (currentRegionX + 6) / 8; l23++) {
                        for (int j26 = (currentRegionY - 6) / 8; j26 <= (currentRegionY + 6) / 8; j26++) {
                            anIntArray1234[k16] = (l23 << 8) + j26;
                            if (inPlayerOwnedHouse
                                    && (j26 == 49 || j26 == 149 || j26 == 147
                                    || l23 == 50 || l23 == 49
                                    && j26 == 47)) {
                                anIntArray1235[k16] = -1;
                                anIntArray1236[k16] = -1;
                                k16++;
                            } else {
                                int k28 = anIntArray1235[k16] = resourceProvider.resolve(0, j26, l23);

                                if (k28 != -1) {
                                    resourceProvider.provide(3, k28); // map?
                                }

                                int j30 = anIntArray1236[k16] = resourceProvider.resolve(1, j26, l23);

                                if (j30 != -1) {
                                    resourceProvider.provide(3, j30); // land?
                                }
                                k16++;
                            }
                        }
                    }
                }
                if (opcode == 241) {
                    int l16 = 0;
                    int ai[] = new int[676];
                    for (int i24 = 0; i24 < 4; i24++) {
                        for (int k26 = 0; k26 < 13; k26++) {
                            for (int l28 = 0; l28 < 13; l28++) {
                                int k30 = anIntArrayArrayArray1129[i24][k26][l28];
                                if (k30 != -1) {
                                    int k31 = k30 >> 14 & 0x3ff;
                                    int i32 = k30 >> 3 & 0x7ff;
                                    int k32 = (k31 / 8 << 8) + i32 / 8;
                                    for (int j33 = 0; j33 < l16; j33++) {
                                        if (ai[j33] != k32) {
                                            continue;
                                        }
                                        k32 = -1;

                                    }
                                    if (k32 != -1) {
                                        ai[l16++] = k32;
                                    }
                                }
                            }
                        }
                    }
                    aByteArrayArray1183 = new byte[l16][];
                    aByteArrayArray1247 = new byte[l16][];
                    anIntArray1234 = new int[l16];
                    anIntArray1235 = new int[l16];
                    anIntArray1236 = new int[l16];
                    for (int l26 = 0; l26 < l16; l26++) {
                        int i29 = anIntArray1234[l26] = ai[l26];
                        int l30 = i29 >> 8 & 0xff;
                        int l31 = i29 & 0xff;
                        int j32 = anIntArray1235[l26] = resourceProvider.resolve(0, l31, l30);
                        if (j32 != -1) {
                            resourceProvider.provide(3, j32);
                        }
                        int i33 = anIntArray1236[l26] = resourceProvider.resolve(1, l31, l30);
                        if (i33 != -1) {
                            resourceProvider.provide(3, i33);
                        }
                    }
                }
                int i17 = regionBaseX - anInt1036;
                int j21 = regionBaseY - anInt1037;
                anInt1036 = regionBaseX;
                anInt1037 = regionBaseY;
                for (int j24 = 0; j24 < 16384; j24++) {
                    NPC npc = npcs[j24];
                    if (npc != null) {
                        for (int j29 = 0; j29 < 10; j29++) {
                            npc.pathX[j29] -= i17;
                            npc.pathY[j29] -= j21;
                        }
                        npc.x -= i17 * 128;
                        npc.y -= j21 * 128;
                    }
                }
                for (int i27 = 0; i27 < maxPlayers; i27++) {
                    Player player = players[i27];
                    if (player != null) {
                        for (int i31 = 0; i31 < 10; i31++) {
                            player.pathX[i31] -= i17;
                            player.pathY[i31] -= j21;
                        }
                        player.x -= i17 * 128;
                        player.y -= j21 * 128;
                    }
                }
                validLocalMap = true;
                byte byte1 = 0;
                byte byte2 = 104;
                byte byte3 = 1;
                if (i17 < 0) {
                    byte1 = 103;
                    byte2 = -1;
                    byte3 = -1;
                }
                byte byte4 = 0;
                byte byte5 = 104;
                byte byte6 = 1;
                if (j21 < 0) {
                    byte4 = 103;
                    byte5 = -1;
                    byte6 = -1;
                }
                for (int k33 = byte1; k33 != byte2; k33 += byte3) {
                    for (int l33 = byte4; l33 != byte5; l33 += byte6) {
                        int i34 = k33 + i17;
                        int j34 = l33 + j21;
                        for (int k34 = 0; k34 < 4; k34++) {
                            if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104) {
                                groundItems[k34][k33][l33] = groundItems[k34][i34][j34];
                            } else {
                                groundItems[k34][k33][l33] = null;
                            }
                        }
                    }
                }
                for (SpawnedObject class30_sub1_1 = (SpawnedObject) spawns
                        .reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (SpawnedObject) spawns
                        .reverseGetNext()) {
                    class30_sub1_1.tileX -= i17;
                    class30_sub1_1.tileY -= j21;
                    if (class30_sub1_1.tileX < 0 || class30_sub1_1.tileY < 0
                            || class30_sub1_1.tileX >= 104
                            || class30_sub1_1.tileY >= 104) {
                        class30_sub1_1.unlink();
                    }
                }
                if (destX != 0) {
                    destX -= i17;
                    destY -= j21;
                }
                oriented = false;
                opcode = -1;
                return true;
            }

            if (opcode == 208) {
                int componentId = inStream.getLEShort();
                if (componentId >= 0) {
                    resetAnimation(componentId);
                }
                openWalkableInterface = componentId;
                opcode = -1;
                return true;
            }

            if (opcode == 99) {
                minimapState = inStream.getByte();
                opcode = -1;
                return true;
            }

            if (opcode == 75) {
                int npc = inStream.readLEUShortA();
                int componentId = inStream.readLEUShortA();
                RSComponent rsComponent = RSComponent.forId(componentId);
                rsComponent.mediaType = 2;
                rsComponent.mediaID = npc;
                if (componentId == 4883 || componentId == 4888 || componentId == 4894 || componentId == 4901) {
                    if (npc != 8599 && npc != 8602 && npc != 8591 && npc != 8592 && npc != 8593 && npc != 8594) {
                        rsComponent.modelZoom = 2000;
                    } else {
                        rsComponent.modelZoom = 796;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SYSTEM_UPDATE) {
                int timer = inStream.readLEUShort();
                systemUpdateTime = (timer == 0 ? 0 : timer * 30);
                opcode = -1;
                return true;
            }

            if (opcode == 60) {
                localY = inStream.getByte();
                localX = inStream.getByteC();
                while (inStream.currentPosition < packetSize) {
                    int k3 = inStream.getByte();
                    parseRegionPackets(inStream, k3);
                }
                opcode = -1;
                return true;
            }

            if (opcode == 35) {
                // TODO
                int verticalAmount = inStream.getByte();
                int verticalSpeed = inStream.getByte();
                int horizontalAmount = inStream.getByte();
                int horizontalSpeed = inStream.getByte();
                cameraViews[verticalAmount] = true;
                verticalSpeeds[verticalAmount] = verticalSpeed;
                horizontalAmounts[verticalAmount] = horizontalAmount;
                horizontalSpeeds[verticalAmount] = horizontalSpeed;
                hotizontalSpeed[verticalAmount] = 0;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAY_SOUND_EFFECT) {
                int soundId = inStream.getShort();
                int type = inStream.getByte();
                int delay = inStream.getShort();
                int volume = inStream.getShort();
                tracks[trackCount] = soundId;
                trackLoops[trackCount] = type;
                soundDelay[trackCount] = delay + Track.delays[soundId];
                soundVolume[trackCount] = volume;
                trackCount++;
                opcode = -1;
                return true;
            }

            if (opcode == 104) {
                int slot = inStream.getByteC();
                int primary = inStream.getByteA();
                String message = inStream.getString();
                if (slot >= 1 && slot <= 5) {
                    if (message.equalsIgnoreCase("null")) {
                        message = null;
                    }
                    atPlayerActions[slot - 1] = message;
                    atPlayerArray[slot - 1] = primary == 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == 78) {
                destX = 0;
                opcode = -1;
                return true;
            }

            if (opcode == 253) {
                int filterType = inStream.getByte() & 0xFF;
                String gameMessage = inStream.getString();
                pattern = Pattern.compile("<a[^>]*href=\"([^>\"]*)\"[^>]*>([^<]*)</a>");
                matcher = pattern.matcher(gameMessage);
                while (matcher.find()) {
                    String url = matcher.group(1);
                    ChatMessage chatMessage = new ChatMessage(12, gameMessage, url, null, false, -1, filterType, -1, -1, -1);
                    chatMessage.setUrlText(matcher.group(0).replaceAll("<a href=\"" + url + "\">", "").replaceAll("</a>", ""));
                    chatMessage.setUrl(url);
                    pushMessage(chatMessage);
                    opcode = -1;
                    return true;
                }
                for (MessageType messageType : MessageType.values()) {
                    if (gameMessage.endsWith(":" + messageType.getName() + ":")) {
                        messageType.sendMessage(this, gameMessage, 0);
                        opcode = -1;
                        return true;
                    }
                }
                MessageType.GAME_MESSAGE.sendMessage(this, gameMessage, filterType);
                opcode = -1;
                return true;
            }

            if (opcode == 1) {
                for (int index = 0; index < players.length; index++) {
                    if (players[index] != null) {
                        players[index].emoteAnimation = -1;
                    }
                }
                for (int index = 0; index < npcs.length; index++) {
                    if (npcs[index] != null) {
                        npcs[index].emoteAnimation = -1;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 50) {
                long playerNameLong = inStream.getLong();
                int onlineWorld = inStream.getByte();
                int clanRank = inStream.getByte();
                String friendUsername = StringUtility.formatUsername(StringUtility.decodeBase37(playerNameLong));
                for (int friendIndex = 0; friendIndex < friendsCount; friendIndex++) {
                    if (playerNameLong != friendsListAsLongs[friendIndex]) {
                        continue;
                    }
                    if (friendsNodeIDs[friendIndex] != onlineWorld) {
                        friendsNodeIDs[friendIndex] = onlineWorld;
                        pushMessage(friendUsername + " has logged " + (onlineWorld >= 2 ? "in" : "out") + ".", 5,
                                settings[312] == 1 ?
                                        Constants.PRIVATE_CHAT_NOTIFICATION_TIMER : -1);
                    }
                    if (friendsClanRankIDs[friendIndex] != clanRank) {
                        friendsClanRankIDs[friendIndex] = clanRank; // TODO 317 clanRank
                    }
                    friendUsername = null;
                }
                if (friendUsername != null && friendsCount < 200) {
                    friendsListAsLongs[friendsCount] = playerNameLong;
                    friendsList[friendsCount] = friendUsername;
                    friendsNodeIDs[friendsCount] = onlineWorld;
                    friendsClanRankIDs[friendsCount] = clanRank;
                    friendsCount++;
                }
                for (boolean flag6 = false; !flag6; ) {
                    flag6 = true;
                    for (int k29 = 0; k29 < friendsCount - 1; k29++) {
                        if (friendsNodeIDs[k29] != worldId && friendsNodeIDs[k29 + 1] == worldId ||
                                friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
                            int j31 = friendsNodeIDs[k29];
                            friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
                            friendsNodeIDs[k29 + 1] = j31;
                            String s10 = friendsList[k29];
                            friendsList[k29] = friendsList[k29 + 1];
                            friendsList[k29 + 1] = s10;
                            long l32 = friendsListAsLongs[k29];
                            friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
                            friendsListAsLongs[k29 + 1] = l32;
                            int clanRank1 = friendsClanRankIDs[k29];
                            friendsClanRankIDs[k29] = friendsClanRankIDs[k29 + 1];
                            friendsClanRankIDs[k29 + 1] = clanRank1;
                            flag6 = false;
                        }
                    }
                }
                componentDrawing.updateClanChatSetup(); // TODO Send packet over from server
                opcode = -1;
                return true;
            }

            if (opcode == 110) {
                if (currentTabId == 12) {
                }
                runEnergy = inStream.getByte();
                opcode = -1;
                return true;
            }

            if (opcode == 254) {
                hintIconDrawType = inStream.getByte();
                if (hintIconDrawType == 1) {
                    hintIconNpcId = inStream.getShort();
                }
                if (hintIconDrawType >= 2 && hintIconDrawType <= 6) {
                    if (hintIconDrawType == 2) {
                        anInt937 = 64;
                        anInt938 = 64;
                    }
                    if (hintIconDrawType == 3) {
                        anInt937 = 0;
                        anInt938 = 64;
                    }
                    if (hintIconDrawType == 4) {
                        anInt937 = 128;
                        anInt938 = 64;
                    }
                    if (hintIconDrawType == 5) {
                        anInt937 = 64;
                        anInt938 = 0;
                    }
                    if (hintIconDrawType == 6) {
                        anInt937 = 64;
                        anInt938 = 128;
                    }
                    hintIconDrawType = 2;
                    hintIconX = inStream.getShort();
                    hintIconY = inStream.getShort();
                    hintIconZ = inStream.getByte();
                }
                if (hintIconDrawType == 10) {
                    hintIconPlayerId = inStream.getShort();
                }
                opcode = -1;
                return true;
            }

            if (opcode == 248) {
                int componentId = inStream.getShortA();
                int overlay = inStream.getShort();
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    inputTaken = true;
                }
                if (inputState != 0) {
                    inputState = 0;
                    inputTaken = true;
                }
                openInterfaceId = componentId;
                overlayInterfaceId = overlay;
                tabAreaAltered = true;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 79) {
                int id = inStream.readLEUShort();
                int scrollPosition = inStream.getShortA();
                RSComponent rsComponent = RSComponent.forId(id);
                if (rsComponent != null && rsComponent.type == 0) {
                    if (scrollPosition < 0) {
                        scrollPosition = 0;
                    }
                    if (scrollPosition > rsComponent.scrollMax - rsComponent.height) {
                        scrollPosition = rsComponent.scrollMax - rsComponent.height;
                    }
                    rsComponent.scrollPosition = scrollPosition;
                }
                opcode = -1;
                return true;
            }

            if (opcode == 68) {
                for (int k5 = 0; k5 < settings.length; k5++) {
                    if (settings[k5] != anIntArray1045[k5]) {
                        settings[k5] = anIntArray1045[k5];
                        adjustVolume(k5);
                    }
                }
                opcode = -1;
                return true;
            }

            /**
             *
             */
            if (opcode == 192) {
                for (int index = 0; index < 10; index++) {
                    tabAmounts[inStream.getSignedByte()] = inStream.getInt();
                }
                if (settings[1012] == 1) {
                    RSComponent bank = RSComponent.forId(5382);
                    Arrays.fill(bankInvTemp, 0);
                    Arrays.fill(bankStackTemp, 0);
                    for (int slot = 0, bankSlot = 0; slot < bank.inventory.length; slot++) {
                        if (bank.inventory[slot] - 1 > 0) {
                            if (ItemDefinition.forId(bank.inventory[slot] - 1).name.toLowerCase().contains(promptInput.toLowerCase())) {
                                bankInvTemp[bankSlot] = bank.inventory[slot];
                                bankStackTemp[bankSlot++] = bank.inventoryValue[slot];
                            }
                        }
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 58) {
                int componentId = inStream.getShortA();
                runInterfaceId = inStream.getIntA();
                RSComponent rsComponent = RSComponent.forId(componentId);
                int length = inStream.getByte();
                if (length == 0) {
                    rsComponent.runTooltips = null;
                    runInterfaceId = -1;
                    opcode = -1;
                    return true;
                }
                tooltopComponents.add(rsComponent);
                rsComponent.runTooltips = new String[length];
                for (int index = 0; index < length; index++) {
                    rsComponent.runTooltips[index] = inStream.getString();
                    if (rsComponent.runTooltips[index].isEmpty()) {
                        rsComponent.runTooltips[index] = null;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 196) {
                long encodedName = inStream.getLong();
                int messageId = inStream.getInt();
                boolean hasTitle = inStream.getByte() == 1;
                boolean titleSuffix = false;
                String title = "";
                int removeSize = 21;
                if (hasTitle) {
                    titleSuffix = inStream.getByte() == 1;
                    removeSize += (inStream.getInt());
                    title = inStream.getString();
                    String titleColor = StringUtility.formatUsername(StringUtility.decodeBase37(inStream.getLong()));
                    title = "<col=" + titleColor + ">" + title + "</col>";
                    title = !titleSuffix ? (title + " ") : (" " + title);
                }

                int[] icons = new int[8];
                for (int i = 0; i < icons.length; i++) {
                    icons[i] = inStream.getByte();
                    icons[i] = icons[i] == 255 ? -1 : icons[i];
                }

                int rankIcon = icons[0];

                boolean isIgnored = false;
                if (rankIcon <= 1) { // TODO != Mod && != Admin
                    for (int ignoreIndex = 0; ignoreIndex < ignoreCount; ignoreIndex++) {
                        if (ignores[ignoreIndex] != encodedName) {
                            continue;
                        }
                        isIgnored = true;
                    }
                }
                if (!isIgnored) {
                    try {
                        privateMessageIds[privateMessageCount] = messageId;
                        privateMessageCount = (privateMessageCount + 1) % 100;
                        String username = StringUtility.formatUsername(StringUtility.decodeBase37(encodedName));
                        String privateMessage = ChatMessageCodec.decode(packetSize - removeSize, inStream);
                        if (settings[311] == 1) {
                            privateMessage = MessageCensor.apply(privateMessage);
                        }
                        pushMessage(new ChatMessage((rankIcon > 0 ? 7 : 3),
                                username, privateMessage, title, titleSuffix, icons));

                        if (!screenFocused) {
                            if (notifications) {
                                Alertify.show(new AlertifyBuilder()
                                        .type(AlertifyType.INFO)
                                        .text("[PM] " + username + ": " + privateMessage)
                                        .autoClose(5000)
                                        .build());
                            }
                        }
                    } catch (Exception exception1) {
                        logger.log(Level.WARNING, "Error in private message reported: {0}", exception1.getMessage());
                        exception1.printStackTrace();
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 85) {
                localY = inStream.getByteC();
                localX = inStream.getByteC();
                opcode = -1;
                return true;
            }

            if (opcode == 24) {
                flashingSidebarId = inStream.getByteS();
                if (flashingSidebarId == currentTabId) {
                    if (flashingSidebarId == 3) {
                        currentTabId = 1;
                    } else {
                        currentTabId = 3;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 246) {
                int componentId = inStream.readLEUShort();
                int scale = inStream.getShort();
                int item = inStream.getShort();
                if (item == 65535) {
                    RSComponent.forId(componentId).mediaType = 0;
                    opcode = -1;
                    return true;
                } else {
                    ItemDefinition definition = ItemDefinition.forId(item);
                    RSComponent.forId(componentId).mediaType = 4;
                    RSComponent.forId(componentId).mediaID = item;
                    RSComponent.forId(componentId).modelRotation2 = definition.modelRotationY;
                    RSComponent.forId(componentId).modelRotation1 = definition.modelRotationY;
                    RSComponent.forId(componentId).modelZoom = (definition.modelZoom * 100) / scale;
                    opcode = -1;
                    return true;
                }
            }

            if (opcode == 171) {
                boolean componentHidden = inStream.getByte() == 1;
                int componentId = inStream.getShort();
                if (RSComponent.forId(componentId) == null) {
                    opcode = -1;
                    return true;
                }
                RSComponent.forId(componentId).hoverOnly = componentHidden;
                opcode = -1;
                return true;
            }

            if (opcode == 142) {
                int id = inStream.readLEUShort();
                resetAnimation(id);
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    inputTaken = true;
                }
                if (inputState != 0) {
                    inputState = 0;
                    inputTaken = true;
                }
                overlayInterfaceId = id;
                tabAreaAltered = true;
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 126) {
                try {
                    String text = inStream.getString();
                    text = StringUtility.cleanString(text);
                    int id = inStream.getShortA();
                    // TODO Configs
                    if (text.startsWith(":prayer:")) {
                        prayerBook = text.substring(8);
                    }
                    updateStrings(text, id);
                    sendString(text, id);
                } catch (Exception e) {
                }
                opcode = -1;
                return true;
            }

            if (opcode == 206) {
                publicChatMode = inStream.getByte();
                privateChatMode = inStream.getByte();
                tradeMode = inStream.getByte();
                inputTaken = true;
                opcode = -1;
                return true;
            }

            if (opcode == 240) {
                weight = inStream.getSignedShort();
                opcode = -1;
                return true;
            }

            if (opcode == 8) {
                int id = inStream.readLEUShortA();
                int model = inStream.getShort();
                RSComponent.forId(id).mediaType = 1;
                RSComponent.forId(id).mediaID = model;
                opcode = -1;
                return true;
            }

            if (opcode == 122) {
                int componentId = inStream.readLEUShortA();
                int color = inStream.readLEUShortA();
                int red = color >> 10 & 0x1f;
                int green = color >> 5 & 0x1f;
                int blue = color & 0x1f;
                RSComponent.forId(componentId).textColor = (red << 19) + (green << 11) + (blue << 3);
                opcode = -1;
                return true;
            }

            if (opcode == 123) {
                int componentId = inStream.readLEUShortA();
                int textColor = inStream.getInt();
                RSComponent.getComponentCache()[componentId].textColor = textColor;
                opcode = -1;
                return true;
            }

            if (opcode == 53) {
                int containerComponentId = inStream.getShort();
                // TODO Fix server side
                if (containerComponentId == 65535 || containerComponentId == -1) {
                    opcode = -1;
                    return true;
                }
                RSComponent rsComponent = RSComponent.forId(containerComponentId);
                int itemCount = inStream.getShort();
                for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                    int amount = inStream.getByte();
                    if (amount == 255) {
                        amount = inStream.readIMEInt();
                    }
                    rsComponent.inventory[itemIndex] = inStream.readLEUShortA();
                    rsComponent.inventoryValue[itemIndex] = amount;
                }
                for (int slot = itemCount; slot < rsComponent.inventory.length; slot++) {
                    rsComponent.inventory[slot] = 0;
                    rsComponent.inventoryValue[slot] = 0;
                }
//                if (rsComponent.contentType == 206) {
//                    for (int tab = 0; tab < 10; tab++) {
//                        int amount = inStream.getShort();
//                        tabAmounts[tab] = amount;
//                    }
//                }
                opcode = -1;
                return true;
            }

            if (opcode == 230) {
                int scale = inStream.getShortA();
                int id = inStream.getShort();
                int pitch = inStream.getShort();
                int roll = inStream.readLEUShortA();
                RSComponent.forId(id).modelRotation2 = pitch;
                RSComponent.forId(id).modelRotation1 = roll;
                RSComponent.forId(id).modelZoom = scale;
                opcode = -1;
                return true;
            }

            if (opcode == 221) {
                friendServerStatus = inStream.getByte();
                opcode = -1;
                return true;
            }

            if (opcode == 177) {
                oriented = true;
                anInt995 = inStream.getByte();
                anInt996 = inStream.getByte();
                anInt997 = inStream.getShort();
                anInt998 = inStream.getByte();
                anInt999 = inStream.getByte();
                if (anInt999 >= 100) {
                    int k7 = anInt995 * 128 + 64;
                    int k14 = anInt996 * 128 + 64;
                    int i20 = getFloorDrawHeight(plane, k14, k7) - anInt997;
                    int l22 = k7 - xCameraPos;
                    int k25 = i20 - zCameraPos;
                    int j28 = k14 - yCameraPos;
                    int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
                    yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
                    xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
                    if (yCameraCurve < 128) {
                        yCameraCurve = 128;
                    }
                    if (yCameraCurve > 383) {
                        yCameraCurve = 383;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 249) {
                member = inStream.getByteA();
                localPlayerIndex = inStream.readLEUShortA();
                opcode = -1;
                return true;
            }

            if (opcode == 65) {
                updateNPCs(inStream, packetSize);
                opcode = -1;
                return true;
            }

            if (opcode == 237) {
                inputContext = inStream.getString();
                opcode = -1;
                return true;
            }

            if (opcode == 238) {
                inputState = inStream.getByte();
                if (inputState != 3) {
                    grandExchange.clearResults();
                }
                opcode = -1;
                return true;
            }

            if (opcode == 27) {
                messagePromptRaised = false;
                inputState = 1;
                amountOrNameInput = "";
                inputTaken = true;
                opcode = -1;
                return true;
            }

            if (opcode == 187) {
                messagePromptRaised = false;
                inputState = 2;
                amountOrNameInput = "";
                inputTaken = true;
                opcode = -1;
                return true;
            }

            if (opcode == 97) {
                int interfaceId = inStream.getShort();
                fullscreenInterfaceID = inStream.getSignedShort();
                if (fullscreenInterfaceID == 65535) {
                    fullscreenInterfaceID = -1;
                }
                resetAnimation(interfaceId);
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    inputTaken = true;
                }
                if (inputState != 0) {
                    inputState = 0;
                    inputTaken = true;
                }
                if (interfaceId == 15244) {
                    fullscreenInterfaceID = 17511;
                    openInterfaceId = 15244;
                }
                openInterfaceId = interfaceId;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 218) {
                dialogueId = inStream.readLEShortA();
                inputTaken = true;
                opcode = -1;
                return true;
            }

            if (opcode == 87) {
                int configId = inStream.readLEUShort();
                int configValue = inStream.getIntA();
                processConfiguration(configId, configValue);
                anIntArray1045[configId] = configValue;
                if (settings[configId] != configValue) {
                    settings[configId] = configValue;
                    adjustVolume(configId);
                    if (dialogueId != -1) {
                        inputTaken = true;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 36) {
                int configId = inStream.readLEUShort();
                byte configValue = inStream.getSignedByte();
                processConfiguration(configId, configValue);
                anIntArray1045[configId] = configValue;
                if (settings[configId] != configValue) {
                    settings[configId] = configValue;
                    adjustVolume(configId);
                    if (dialogueId != -1) {
                        inputTaken = true;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 37) {// INTERFACE CONFIG
                try {
                    int configId = inStream.readLEUShort();
                    int configValue = inStream.getIntA();
                    processInterfaceConfiguration(configId, configValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == 61) {
                multiCombat = inStream.getByte();
                opcode = -1;
                return true;
            }

            if (opcode == 200) {
                int id = inStream.getShort();
                int animation = inStream.getSignedShort();
                RSComponent rsComponent = RSComponent.forId(id);
                rsComponent.disabledAnimationId = animation;
                if (animation == 591 || animation == 588) {
                   // if (rsComponent.method206(rsComponent.mediaType, rsComponent.mediaID).osrs) {
                        rsComponent.modelZoom = 900; //Zoom 900 is a good size.
                   // }
                }
                if (animation == -1) {
                    rsComponent.animationFrames = 0;
                    rsComponent.animationLength = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == 219) {
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    inputTaken = true;
                }
                if (inputState != 0) {
                    inputState = 0;
                    inputTaken = true;
                }
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 220) {
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    inputTaken = true;
                }
                if (inputState != 0) {
                    inputState = 0;
                    inputTaken = true;
                }
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 34) {
                int interfaceId = inStream.getShort();
                RSComponent rsComponent = RSComponent.forId(interfaceId);
                while (inStream.currentPosition < packetSize) {
                    int slot = inStream.getSmartB();
                    int id = inStream.getShort();
                    int amount = inStream.getByte();
                    if (amount == 255) {
                        amount = inStream.getInt();
                    }
                    if (slot >= 0 && slot < rsComponent.inventory.length) {
                        rsComponent.inventory[slot] = id;
                        rsComponent.inventoryValue[slot] = amount;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 4 || opcode == 44 || opcode == 84 || opcode == 101
                    || opcode == 105 || opcode == 117 || opcode == 147
                    || opcode == 151 || opcode == 156 || opcode == 160
                    || opcode == 215) {
                parseRegionPackets(inStream, opcode);
                opcode = -1;
                return true;
            }

            if (opcode == 127) {
                int scrollMax = inStream.getShortA();
                int componentId = inStream.getShort();
                RSComponent.getComponentCache()[componentId].scrollMax = scrollMax;
                opcode = -1;
                return true;
            }

            if (opcode == 106) {
                currentTabId = inStream.getByteC();
                tabAreaAltered = true;
                opcode = -1;
                return true;
            }
            if (opcode == 164) {
                int id = inStream.readLEUShort();
                resetAnimation(id);
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                backDialogueId = id;
                inputTaken = true;
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }
            if (opcode == PacketConstants.SUMMONING_OPTIONS) {
                SUMMONING_OPTIONS.clear();
                int length = inStream.getByteC();
                while (length > 0) {
                    length--;
                    SUMMONING_OPTIONS.add(inStream.getString());
                }
                Collections.reverse(SUMMONING_OPTIONS);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SUMMONING_INFORMATION) {
                int level = inStream.getShortA();
                String specialName = inStream.getNewString();
                String specialDescription = inStream.getNewString();
                int scrollId = inStream.getLEShort();
                int scrollAmount = inStream.getShort();
                String fullName = inStream.getNewString();
                byte interfaceType = (byte) inStream.getByte();
                summoning.updateInformationTab(specialName, fullName, level, scrollId, scrollAmount, specialDescription, interfaceType);
                opcode = -1;
                return true;
            }
            logger.log(Level.WARNING, "Unknown opcode: {0}, Size: {1}, Second opcode: {2}, Last opcode: {3}", new Object[]{opcode, packetSize, secondLastOpcode, thirdLastOpcode});
        } catch (IOException _ex) {
            dropClient();
        } catch (Exception exception) {
            exception.printStackTrace();
            String s2 = "T2 - " + opcode + "," + secondLastOpcode + ","
                    + thirdLastOpcode + " - " + packetSize + ","
                    + (regionBaseX + localPlayer.pathX[0]) + ","
                    + (regionBaseY + localPlayer.pathY[0]) + " - ";
            for (int j15 = 0; j15 < packetSize && j15 < 50; j15++) {
                s2 = s2 + inStream.payload[j15] + ",";
            }
            SignLink.reporterror(s2);
            // resetLogout();
        }
        opcode = -1;
        return true;
    }

    private void moveCameraWithPlayer() {
        anInt1265++;
        showOtherPlayers(true);
        showNPCs(true);
        showOtherPlayers(false);
        showNPCs(false);
        createProjectiles();
        createStationaryGraphics();
        if (!oriented) {
            int i = anInt1184;
            if (anInt984 / 256 > i) {
                i = anInt984 / 256;
            }
            if (cameraViews[4] && horizontalAmounts[4] + 128 > i) {
                i = horizontalAmounts[4] + 128;
            }
            int k = cameraHorizontal + cameraRotation & 0x7ff;
            setCameraPos(cameraZoom
                            + i
                            * ((SceneGraph.viewDistance == 9)
                            && (frameMode == ScreenMode.RESIZABLE) ? 2
                            : SceneGraph.viewDistance == 10 ? 5 : 3), i,
                    anInt1014,
                    getFloorDrawHeight(plane, localPlayer.y, localPlayer.x) - 50, k,
                    anInt1015);
        }
        int j;
        if (!oriented) {
            j = setCameraLocation();
        } else {
            j = resetCameraHeight();
        }
        int l = xCameraPos;
        int i1 = zCameraPos;
        int j1 = yCameraPos;
        int k1 = yCameraCurve;
        int l1 = xCameraCurve;
        for (int i2 = 0; i2 < 5; i2++) {
            if (cameraViews[i2]) {
                int j2 = (int) ((Math.random()
                        * (double) (verticalSpeeds[i2] * 2 + 1) - (double) verticalSpeeds[i2]) + Math
                        .sin((double) hotizontalSpeed[i2]
                                * ((double) horizontalSpeeds[i2] / 100D))
                        * (double) horizontalAmounts[i2]);
                if (i2 == 0) {
                    xCameraPos += j2;
                }
                if (i2 == 1) {
                    zCameraPos += j2;
                }
                if (i2 == 2) {
                    yCameraPos += j2;
                }
                if (i2 == 3) {
                    xCameraCurve = xCameraCurve + j2 & 0x7ff;
                }
                if (i2 == 4) {
                    yCameraCurve += j2;
                    if (yCameraCurve < 128) {
                        yCameraCurve = 128;
                    }
                    if (yCameraCurve > 383) {
                        yCameraCurve = 383;
                    }
                }
            }
        }
        Model.aBoolean1684 = true;
        Model.objectsRendered = 0;
        Model.anInt1685 = super.mouseX - (frameMode == ScreenMode.FIXED ? 4 : 0);
        Model.anInt1686 = super.mouseY - (frameMode == ScreenMode.FIXED ? 4 : 0);
        int[] pixels = null, offsets = null;
        if (antialiasing) {
            Model.anInt1685 <<= 1;
            Model.anInt1686 <<= 1;
            SceneGraph.viewDistance += 1;
            pixels = Rasterizer.pixels;
            Rasterizer.pixels = antialiasingPixels;
            offsets = Rasterizer.lineOffsets;
            Rasterizer.lineOffsets = antialiasingOffsets;
            Rasterizer.bottomX <<= 1;
            Rasterizer.bottomY <<= 1;
            Raster.width <<= 1;
            Raster.height <<= 1;
            Raster.viewport_centerX <<= 1;
            Raster.viewport_centerY <<= 1;
            Raster.viewportRX <<= 1;
            Raster.anInt1387 <<= 1;

            Rasterizer.textureInt1 <<= 1;
            Rasterizer.textureInt2 <<= 1;
        }
        Raster.clear();
        try {
            scene.render(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (antialiasing) {
            Model.anInt1685 >>= 1;
            Model.anInt1686 >>= 1;
            SceneGraph.viewDistance -= 1;
            Rasterizer.pixels = pixels;
            Rasterizer.lineOffsets = offsets;
            Rasterizer.bottomX >>= 1;
            Rasterizer.bottomY >>= 1;
            Raster.width >>= 1;
            Raster.height >>= 1;
            Raster.viewport_centerX >>= 1;
            Raster.viewport_centerY >>= 1;
            Raster.viewportRX >>= 1;
            Rasterizer.textureInt1 >>= 1;
            Rasterizer.textureInt2 >>= 1;
            Raster.anInt1387 >>= 1;

            int w = Raster.width;
            int h = Raster.height;
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int x2 = x << 1;
                    int y2 = y << 1;
                    int w2 = w << 1;
                    int c1 = antialiasingPixels[x2 + y2 * w2];
                    int c2 = antialiasingPixels[(x2 + 1) + y2 * w2];
                    int c3 = antialiasingPixels[x2 + (y2 + 1) * w2];
                    int c4 = antialiasingPixels[(x2 + 1) + (y2 + 1) * w2];
                    int r = (c1 >> 16 & 0xff) + (c2 >> 16 & 0xff) + (c3 >> 16 & 0xff) + (c4 >> 16 & 0xff) >> 2;
                    int g = (c1 >> 8 & 0xff) + (c2 >> 8 & 0xff) + (c3 >> 8 & 0xff) + (c4 >> 8 & 0xff) >> 2;
                    int b = (c1 & 0xff) + (c2 & 0xff) + (c3 & 0xff) + (c4 & 0xff) >> 2;
                    Raster.pixels[x + y * Raster.width] = r << 16 | g << 8 | b;
                }
            }
        }

        scene.clearStaticObjects();
        updateEntities();
        drawHeadIcon();
        TextureAnimating.animateTexture();
        if (loggedIn) {
            if (!isScreenMode(ScreenMode.FIXED)) {
                if (!isMediaMode()) {
                    gameframe.drawChatArea();
                    gameframe.drawMinimapArea();
                    gameframe.drawTabArea();
                    if (tabInterfaceIDs[Game.currentTabId] != -1) {
                        animateRSInterface(cycleTimer, tabInterfaceIDs[currentTabId]);
                    }
                }
            }
            draw3dScreen();

            if (getConsole().isOpen()) {
                getConsole().draw();
            }
        }
        gameframe.getGameScreenImageProducer().drawGraphics(isScreenMode(ScreenMode.FIXED) ? 4 : 0, super.graphics,
                isScreenMode(ScreenMode.FIXED) ? 4 : 0);
        xCameraPos = l;
        zCameraPos = i1;
        yCameraPos = j1;
        yCameraCurve = k1;
        xCameraCurve = l1;
    }

    public void closeGameInterfaces() {
        if (openInterfaceId == -1) {
            setTab(10, false);
            return;
        }
        outgoing.putOpcode(130);
        outgoing.putShort(openInterfaceId);
        openInterfaceId = -1;
        fullscreenInterfaceID = -1;
        runInterfaceId = -1;
        for (RSComponent rsComponent : tooltopComponents) {
            rsComponent.tooltips = null;
        }
        tooltopComponents.clear();
    }

    public void clearTopInterfaces() {
        outgoing.putOpcode(130);
        outgoing.putShort(openInterfaceId);
        if (overlayInterfaceId != -1) {
            overlayInterfaceId = -1;
            continuedDialogue = false;
            tabAreaAltered = true;
        }
        if (backDialogueId != -1) {
            backDialogueId = -1;
            inputTaken = true;
            continuedDialogue = false;
        }
        openInterfaceId = -1;
        fullscreenInterfaceID = -1;
        runInterfaceId = -1;
        for (RSComponent rsComponent : tooltopComponents) {
            rsComponent.tooltips = null;
        }
        tooltopComponents.clear();
    }

    public String getClickToContinueString() {
        return clickToContinueString;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setMenuOpen(boolean menuOpen) {
        this.menuOpen = menuOpen;
    }

    public String getInputString() {
        return inputString;
    }

    public void setGameFrame(int gameFrameId, boolean update) {
        if (GAMEFRAME_MAP.get(gameFrameId) == null) {
            if (Constants.DEBUG_MODE) {
                logger.log(Level.WARNING, "No gameframe found for #{0}, using default.", gameFrameId);
            }
            gameFrameId = Constants.DEFAULT_GAMEFRAME;
        }
        this.gameframe = GAMEFRAME_MAP.get(gameFrameId);
        if (update) {
            this.gameframe.setup();
            RSComponent.forId(24901).disabledMessage = String.valueOf(this.gameframe.getId());
            setupGameplayScreen();
        }
    }

    public ChatMessage[] getChatMessages() {
        return chatMessages;
    }

    public int[] getInterfaceConfig() {
        return interfaceConfig;
    }

    public int getInterfaceConfig(InterfaceConfiguration interfaceConfiguration) {
        return interfaceConfig[interfaceConfiguration.getId()];
    }

    public boolean getInterfaceConfiguration(InterfaceConfiguration interfaceConfiguration) {
        return getInterfaceConfig(interfaceConfiguration) >= 1;
    }

    public void setInterfaceConfig(InterfaceConfiguration interfaceConfiguration, int value) {
        interfaceConfig[interfaceConfiguration.getId()] = value;
    }

    public String getChatInputContext() {
        return inputContext;
    }

    public String getAmountOrNameInput() {
        return amountOrNameInput;
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public RSStream getOutgoing() {
        return outgoing;
    }

    public String getPromptInput() {
        return promptInput;
    }

    public boolean isMessagePromptRaised() {
        return messagePromptRaised;
    }

    public boolean isOptionDialogue() {
        return backDialogueId == 2459 || backDialogueId == 2469 || backDialogueId == 2480 || backDialogueId == 2492;
    }

    public boolean isMediaMode() {
        return frameMode != ScreenMode.FIXED && mediaMode;
    }

    /**
     * Gets the current chat type being viewed.
     *
     * @return The chat type being viewed.
     */
    public int getChatTypeView() {
        return chatTypeView;
    }

    /**
     * Gets the public chat mode.
     *
     * @return The public chat mode.
     */
    public int getPublicChatMode() {
        return publicChatMode;
    }

    /**
     * Gets the private chat mode.
     *
     * @return The private chat mode.
     */
    public int getPrivateChatMode() {
        return privateChatMode;
    }

    /**
     * Gets the trade mode.
     *
     * @return The trade mode.
     */
    public int getTradeMode() {
        return tradeMode;
    }

    /**
     * Gets the split private chat status.
     *
     * @return The split private chat.
     */
    public int getSplitPrivateChat() {
        return splitPrivateChat;
    }

    /**
     * Gets the player's ignore count.
     *
     * @return The ignore count.
     */
    public int getIgnoreCount() {
        return ignoreCount;
    }

    /**
     * Gets the player's ignore list.
     *
     * @return The array of ignores.
     */
    public long[] getIgnores() {
        return ignores;
    }

    public void resetAllImageProducers() {
        if (super.fullGameScreen != null) {
            return;
        }
        gameframe.resetImageProducers();
        chatSettingImageProducer = null;
        worldSelectImageProducer = null;
        super.fullGameScreen = new ImageProducer(765, 503);
        welcomeScreenRaised = true;
    }

    public ClanChatMember[] getClanChatMembers() {
        return clanChatMembers;
    }

    public void setClanChatMembers(ClanChatMember[] clanChatMembers) {
        this.clanChatMembers = clanChatMembers;
    }

    public String getChatOwnerName() {
        return chatOwnerName;
    }

    public void setChatOwnerName(String chatOwnerName) {
        this.chatOwnerName = chatOwnerName;
    }

    public String getClanChatName() {
        return clanChatName;
    }

    public void setClanChatName(String clanChatName) {
        this.clanChatName = clanChatName;
    }

    public int getClanChatSize() {
        return clanChatSize;
    }

    public void setClanChatSize(int clanChatSize) {
        this.clanChatSize = clanChatSize;
    }

    public byte getChatKickRights() {
        return chatKickRights;
    }

    public void setChatKickRights(byte chatKickRights) {
        this.chatKickRights = chatKickRights;
    }

    public byte getCurrentUserClanRights() {
        return currentUserClanRights;
    }

    public void setCurrentUserClanRights(byte currentUserClanRights) {
        this.currentUserClanRights = currentUserClanRights;
    }

    public void mouseWheelDragged(int i, int j) {
        if (!mouseWheelDown) {
            return;
        }
        if (settings[314] != 1) {
            return;
        }
        this.anInt1186 += i * 3;
        this.anInt1187 += (j << 1);
    }

    static {
        SKILL_EXPERIENCE = new int[99];
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int l = j + 1;
            int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
            i += i1;
            SKILL_EXPERIENCE[j] = i / 4;
        }
        BIT_MASKS = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            BIT_MASKS[k] = i - 1;
            i += i;
        }
    }

    public enum ScreenMode {
        FIXED,
        RESIZABLE,
        FULLSCREEN
    }
}
