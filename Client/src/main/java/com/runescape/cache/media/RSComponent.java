package com.runescape.cache.media;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.cache.media.inter.InterfaceRepository;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.font.GameFont;
import com.runescape.media.renderable.Model;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.StringUtility;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The RSComponent class for in-game interfaces.
 *
 * @author Jagex
 * @author Gielinor
 * TODO Make all non-static voids use non-parent reference (RSComponent rsComponent = addInterface(id))
 */
public class RSComponent {
    /**
     * The button action type.
     */
    public static final int BUTTON_ACTION_TYPE = 1;
    /**
     * The spell action type.
     */
    public static final int SPELL_ACTION_TYPE = 2;
    /**
     * The close interface action type.
     */
    public static final int CLOSE_ACTION_TYPE = 3;
    /**
     * The small text index.
     */
    public static final int SMALL = 0;
    /**
     * The regular text index.
     */
    public static final int REGULAR = 1;
    /**
     * The bold text index.
     */
    public static final int BOLD = 2;
    /**
     * The fancy text index.
     */
    public static final int FANCY = 3;
    /**
     * The new fancy text index for the {@link com.runescape.media.font.GameFont} class.
     */
    public static final int NEW_FANCY = 4;
    public static final int TYPE_CONTAINER = 0;
    public static final int TYPE_MODEL_LIST = 1;
    public static final int TYPE_INVENTORY = 2;
    public static final int TYPE_RECTANGLE = 3;
    public static final int TYPE_TEXT = 4;
    public static final int TYPE_SPRITE = 5;
    public static final int TYPE_MODEL = 6;
    public static final int TYPE_ITEM_LIST = 7;
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(RSComponent.class.getName());
    private static final ReferenceCache modelCache = new ReferenceCache(30);
    public static CacheArchive cacheArchive;
    /**
     * The cache of current {@link RSComponent} classes.
     */
    public static RSComponent[] COMPONENT_CACHE;
    private static ReferenceCache spriteCache;
    private final List<InterfaceChild> interfaceChildren = new ArrayList<>();
    /**
     * The parent id of this component.
     */
    public int parentId;
    /**
     * The id of this component.
     */
    public int id;
    /**
     * If this interface draws with transparency.
     */
    public boolean drawsTransparent;
    /**
     * The animationLength.
     */
    public int animationLength;
    /**
     * The sprites for this interface.
     */
    public Sprite[] sprites;
    /**
     * The x coordinates of {@link com.runescape.cache.media.Sprite} classes.
     */
    public int[] spritesX;
    /**
     * The y coordinates of {@link com.runescape.cache.media.Sprite} classes.
     */
    public int[] spritesY;
    /**
     * The required values for configurations.
     */
    public int[] scriptDefaults;
    /**
     * The value indexes.
     */
    public int[][] scripts;
    /**
     * The content type.
     */
    public int contentType;
    /**
     * The enabled message.
     */
    public String enabledMessage;
    /**
     * The disabled message.
     */
    public String disabledMessage;
    /**
     * The hover colour.
     */
    public int disabledMouseOverColor;
    public int positionX;
    public int positionY;
    public int optionType;
    public String spellName;
    public int enabledColor;
    public int width;
    public String tooltip;
    public String selectedActionName;
    public boolean centerText;
    public int scrollPosition;
    public boolean newScroller;
    public String actions[];
    public boolean filled;
    public int hoverType;
    public int originalHoverType;
    public int shiftHoverType;
    public int invSpritePadX;
    public int textColor;
    public int mediaType;
    public int mediaID;
    public boolean replaceItems;
    public int spellUsableOn;
    public int enabledMouseOverColor;
    public boolean usableItemInterface;
    public GameFont gameFont;
    public int isMouseoverTriggereds;
    public int invSpritePadY;
    public int[] scriptOperators;
    public int animationFrames;
    public boolean isInventoryInterface;
    public int[] inventoryValue;
    public int[] inventory;
    public byte opacity;
    public int disabledAnimationId;
    public int enabledAnimationId;
    public boolean deleteOnDrag;
    public int scrollMax;
    public int horizontalScrollPosition;
    public int horizontalScrollMax;
    public int horizontalScrollHeight;
    public int type;
    public int xOffset;
    public int yOffset;
    public boolean hoverOnly;
    public int height;
    public boolean textShadow;
    public int modelZoom;
    public int modelRotation2;
    public int modelRotation1;
    public boolean interfaceShown;
    public String[] tooltips;
    public String[] runTooltips;
    public String popupString;
    public String enabledPopupString;
    public String hoverText;
    public int transparency;
    public boolean hasExamine = true;
    public boolean hasItemCount = true;
    public boolean showName = true;
    public boolean interfaceConfig;
    public boolean automaticConfig = true;
    public boolean alphaBox;
    public int boxX;
    public int boxY;
    public int boxOffsetY;
    public int boxOffsetX;
    public int boxColour;
    public int boxAlpha;
    public int hasSprites;
    public int rsFontIndex = 0;
    public boolean advancedSprite = false;
    public boolean drawInfinity = false;
    public int customOpacity = 0;
    public int summonReq;
    public boolean greyScale;
    public int itemSpriteId1;
    public int itemSpriteId2;
    public int itemSpriteZoom1;
    public int itemSpriteZoom2;
    public int itemSpriteIndex;
    public Sprite[] savedSprite = new Sprite[10];
    public boolean inventoryHover;
    public boolean rgb;
    //
    public int unknownShort1;
    public int unknownByte1;
    public String spriteData;
    public String disabledSpriteName;
    public String enabledSpriteName;
    public int mediaHash;
    public int mediaHash1;
    public int mediaHash2;
    public int mediaHash3;
    public int randomByte1;
    public int randomByte2;
    public int randomByte3;
    public int randomByte4;
    public int enabledSpriteId;
    public int disabledSpriteId;
    public boolean hidden;
    public boolean drawSprite;
    public boolean flashes;
    public boolean alphaForward;
    public int[] alphaValue;
    public int[] timer;
    public int enabledMediaID;
    private int enabledMediaType;
    private int mOverInterToTrigger;
    private SpriteSet spriteSet = new SpriteSet();
    /**
     * The visibility of the menu of items.
     */
    private boolean menuVisible;

    /**
     * The default constructor.
     */
    public RSComponent() {
        enabledSpriteId = disabledSpriteId = -1;
    }

    /**
     * The default constructor with an ID.
     *
     * @param id The interface id.
     */
    public RSComponent(int id) {
        this.id = id;
        enabledSpriteId = disabledSpriteId = -1;
    }

    public static void fixPositioning() {
        for (RSComponent rsComponent : getComponentCache()) {
            if (rsComponent == null) {
                continue;
            }
            if (rsComponent.interfaceChildren == null) {
                continue;
            }
            for (InterfaceChild interfaceChild : rsComponent.interfaceChildren) {
                if (interfaceChild == null || interfaceChild.getRSComponent() == null) {
                    continue;
                }
                interfaceChild.getRSComponent().positionX = interfaceChild.getX();
                interfaceChild.getRSComponent().positionY = interfaceChild.getY();
            }
        }
    }

    public static void save() {
        try {
            RSStream rsStream = new RSStream(new byte[1999999]);
            rsStream.putShort(getComponentCache().length);
            for (RSComponent rsComponent : getComponentCache()) {
                if (rsComponent == null) {
                    continue;
                }
                int parent = rsComponent.parentId;
                if (rsComponent.parentId != -1) {
                    rsStream.putShort(65535);
                    rsStream.putShort(parent);
                    rsStream.putShort(rsComponent.id);
                } else {
                    rsStream.putShort(rsComponent.id);
                }
                rsStream.writeByte(rsComponent.type);
                rsStream.writeByte(rsComponent.optionType);
                rsStream.putShort(rsComponent.contentType);
                rsStream.putShort(rsComponent.width);
                rsStream.putShort(rsComponent.height);
                rsStream.writeByte(rsComponent.opacity);
                if (rsComponent.hoverType != -1) {
                    rsStream.putSpaceSaver(rsComponent.hoverType);
                } else {
                    rsStream.writeByte(0);
                }
                int scriptOperatorsCount = 0;
                if (rsComponent.scriptOperators != null) {
                    scriptOperatorsCount = rsComponent.scriptOperators.length;
                }
                rsStream.writeByte(scriptOperatorsCount);
                if (scriptOperatorsCount > 0) {
                    for (int i = 0; i < scriptOperatorsCount; i++) {
                        rsStream.writeByte(rsComponent.scriptOperators[i]);
                        rsStream.putShort(rsComponent.scriptDefaults[i]);
                    }
                }
                int valueLength = 0;
                if (rsComponent.scripts != null) {
                    valueLength = rsComponent.scripts.length;
                }
                rsStream.writeByte(valueLength);
                if (valueLength > 0) {
                    for (int index = 0; index < valueLength; index++) {
                        int total = rsComponent.scripts[index].length;
                        rsStream.putShort(total);
                        for (int index2 = 0; index2 < total; index2++) {
                            rsStream.putShort(rsComponent.scripts[index][index2]);
                        }
                    }
                }
                if (rsComponent.type == 0) {
                    rsStream.putShort(rsComponent.scrollMax);
                    rsStream.writeByte(rsComponent.hoverOnly ? 1 : 0);
                    rsStream.putShort(rsComponent.getInterfaceChildren().size());
                    for (InterfaceChild interfaceChild : rsComponent.getInterfaceChildren()) {
                        rsStream.putShort(interfaceChild.getId());
                        rsStream.putShort(interfaceChild.getX());
                        rsStream.putShort(interfaceChild.getY());
                    }
                } else if (rsComponent.type == 1) {
                    rsStream.putShort(0);
                    rsStream.writeByte(0);
                } else if (rsComponent.type == 2) {
                    rsStream.writeByte(rsComponent.deleteOnDrag ? 1 : 0);
                    rsStream.writeByte(rsComponent.isInventoryInterface ? 1 : 0);
                    rsStream.writeByte(rsComponent.usableItemInterface ? 1 : 0);
                    rsStream.writeByte(rsComponent.replaceItems ? 1 : 0);
                    rsStream.writeByte(rsComponent.invSpritePadX);
                    rsStream.writeByte(rsComponent.invSpritePadY);
                    for (int index = 0; index < 20; index++) {
                        rsStream.writeByte(rsComponent.sprites[index] == null ? 0 : 1);
                        if (rsComponent.sprites[index] != null) {
                            rsStream.putShort(rsComponent.spritesX[index]);
                            rsStream.putShort(rsComponent.spritesY[index]);
                            rsStream.putString(rsComponent.sprites[index].spriteName + "," + rsComponent.sprites[index].spriteIndex);
                        }
                    }
                    for (int index = 0; index < 5; index++) {
                        if (rsComponent.actions[index] != null) {
                            rsStream.putString(rsComponent.actions[index]);
                        } else {
                            rsStream.putString("");
                        }
                    }
                } else if (rsComponent.type == 3) {
                    rsStream.writeByte(rsComponent.filled ? 1 : 0);
                }
                if (rsComponent.type == 4 || rsComponent.type == 1) {
                    rsStream.writeByte(rsComponent.centerText ? 1 : 0);
                    rsStream.writeByte(rsComponent.rsFontIndex);
                    rsStream.writeByte(rsComponent.textShadow ? 1 : 0);
                }
                if (rsComponent.type == 4) {
                    if (rsComponent.disabledMessage != null) {
                        rsStream.putString(rsComponent.disabledMessage);
                    } else {
                        rsStream.putString("null");
                    }
                    if (rsComponent.enabledMessage != null) {
                        rsStream.putString(rsComponent.enabledMessage);
                    } else {
                        rsStream.putString("null");
                    }
                }
                if (rsComponent.type == 1 || rsComponent.type == 3 || rsComponent.type == 4) {
                    rsStream.putInt(rsComponent.textColor);
                }
                if (rsComponent.type == 3 || rsComponent.type == 4) {
                    rsStream.putInt(rsComponent.enabledColor);
                    rsStream.putInt(rsComponent.disabledMouseOverColor);
                    rsStream.putInt(rsComponent.enabledMouseOverColor);
                }
                if (rsComponent.type == 5) {
                    if (rsComponent.getSpriteSet().getDisabled() != null) {
                        if (rsComponent.disabledSpriteName != null) {
                            rsStream.putString(rsComponent.disabledSpriteName + "," + rsComponent.disabledSpriteId);
                        } else {
                            rsStream.putString("");
                        }
                    } else {
                        rsStream.putString("");
                    }
                    if (rsComponent.getSpriteSet().getEnabled() != null) {
                        if (rsComponent.enabledSpriteName != null) {
                            rsStream.putString(rsComponent.enabledSpriteName + "," + rsComponent.enabledSpriteId);
                        } else {
                            rsStream.putString("");
                        }
                    } else {
                        rsStream.putString("");
                    }
                } else if (rsComponent.type == 6) {
                    if (rsComponent.mediaType != -1 && rsComponent.mediaID > 0) {
                        rsStream.putSpaceSaver(rsComponent.mediaID);
                    } else {
                        rsStream.writeByte(0);
                    }
                    if (rsComponent.enabledMediaType > 0) {
                        rsStream.putSpaceSaver(rsComponent.enabledMediaID);
                    } else {
                        rsStream.writeByte(0);
                    }
                    if (rsComponent.disabledAnimationId > 0) {
                        rsStream.putSpaceSaver(rsComponent.disabledAnimationId);
                    } else {
                        rsStream.writeByte(0);
                    }
                    if (rsComponent.enabledAnimationId > 0) {
                        rsStream.putSpaceSaver(rsComponent.enabledAnimationId);
                    } else {
                        rsStream.writeByte(0);
                    }
                    rsStream.putShort(rsComponent.modelZoom);
                    rsStream.putShort(rsComponent.modelRotation2); // rotationX
                    rsStream.putShort(rsComponent.modelRotation1); // rotationY
                } else if (rsComponent.type == 7) {
                    rsStream.writeByte(rsComponent.centerText ? 1 : 0);
                    rsStream.writeByte(rsComponent.rsFontIndex);
                    rsStream.writeByte(rsComponent.textShadow ? 1 : 0); // shadowed
                    rsStream.putInt(rsComponent.textColor);
                    rsStream.putShort(rsComponent.invSpritePadX);
                    rsStream.putShort(rsComponent.invSpritePadY);
                    rsStream.writeByte(rsComponent.isInventoryInterface ? 1 : 0);
                    for (int i = 0; i < 5; i++) {
                        if (rsComponent.actions[i] != null) {
                            rsStream.putString(rsComponent.actions[i]);
                        } else {
                            rsStream.putString("");
                        }
                    }
                }
                if (rsComponent.optionType == 2 || rsComponent.type == 2) {
                    rsStream.putString(rsComponent.selectedActionName == null ? "null" : rsComponent.selectedActionName);
                    rsStream.putString(rsComponent.spellName == null ? "null" : rsComponent.spellName);
                    rsStream.putShort(rsComponent.spellUsableOn);
                }
                if (rsComponent.type == 8) {
                    rsStream.putString(rsComponent.disabledMessage == null ? "null" : rsComponent.disabledMessage);
                }
                if (rsComponent.optionType == 1 || rsComponent.optionType == 4 || rsComponent.optionType == 5 || rsComponent.optionType == 6) {
                    rsStream.putString(rsComponent.tooltip == null ? "" : rsComponent.tooltip);
                }
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream("data.dat"));
            out.write(rsStream.payload, 0, rsStream.currentPosition); // TODO offset bitPosition
            out.close();
            JOptionPane.showMessageDialog(null, "Interface cache (data.dat) saved successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while saving the interface cache.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Parses the interface file from the cache.
     *
     * @param interfaceArchive The interface {@link com.runescape.net.CacheArchive}.
     * @param rsFonts          The {@link com.runescape.media.font.GameFont} instances.
     * @param graphicArchive   The graphic {@link com.runescape.net.CacheArchive}.
     */
    public static void parse(CacheArchive interfaceArchive, GameFont[] rsFonts, CacheArchive graphicArchive) {
        spriteCache = new ReferenceCache(50000);
        RSStream stream = new RSStream(interfaceArchive.getEntry("data"));

        int usedParentId = -1;
        int componentCount = stream.getShort();
        COMPONENT_CACHE = new RSComponent[componentCount + 80000];
        while (stream.currentPosition < stream.payload.length) {
            int currentChildId = stream.getShort();
            if (currentChildId == 65535) {
                usedParentId = stream.getShort();
                currentChildId = stream.getShort();
            }
            RSComponent rsComponent = null;
            try {
                rsComponent = COMPONENT_CACHE[currentChildId] = new RSComponent();
            } catch (Exception e) {
                //com.runescape.PrinterOut.println(RSComponent.class, "Failed at " + currentChildId + " / " + usedParentId);
                System.exit(-1);
            }
            rsComponent.id = currentChildId;
            rsComponent.parentId = usedParentId;
            rsComponent.type = stream.getByte();
            rsComponent.optionType = stream.getByte();
            rsComponent.contentType = stream.getShort();
            rsComponent.width = stream.getShort();
            rsComponent.height = stream.getShort();
            rsComponent.opacity = (byte) stream.getByte();
            rsComponent.hoverType = stream.getByte();
            rsComponent.originalHoverType = rsComponent.hoverType;
            if (rsComponent.hoverType != 0) {
                int shift = stream.getByte();
                rsComponent.shiftHoverType = shift;
                rsComponent.hoverType = (rsComponent.hoverType - 1 << 8) + shift;
            } else {
                rsComponent.hoverType = -1;
                rsComponent.originalHoverType = -1;
            }
            int scriptCount = stream.getByte();
            if (scriptCount > 0) {
                rsComponent.scriptOperators = new int[scriptCount];
                rsComponent.scriptDefaults = new int[scriptCount];
                for (int j1 = 0; j1 < scriptCount; j1++) {
                    rsComponent.scriptOperators[j1] = stream.getByte();
                    rsComponent.scriptDefaults[j1] = stream.getShort();
                }
            }
            int scriptsCount = stream.getByte();
            if (scriptsCount > 0) {
                rsComponent.scripts = new int[scriptsCount][];
                for (int l1 = 0; l1 < scriptsCount; l1++) {
                    int i3 = stream.getShort();
                    rsComponent.scripts[l1] = new int[i3];
                    for (int l4 = 0; l4 < i3; l4++) {
                        rsComponent.scripts[l1][l4] = stream.getShort();
                    }

                }

            }
            if (rsComponent.type == 0) {
                rsComponent.drawsTransparent = false;
                rsComponent.scrollMax = stream.getShort();
                rsComponent.hoverOnly = stream.getByte() == 1;
                int i2 = stream.getShort();
                for (int j3 = 0; j3 < i2; j3++) {
                    int childId = stream.getShort();
                    int xPos = stream.getSignedShort();
                    int yPos = stream.getSignedShort();
                    rsComponent.interfaceChildren.add(new InterfaceChild(childId, xPos, yPos));
                }
            }
            if (rsComponent.type == 1) {
                rsComponent.unknownShort1 = stream.getShort();
                rsComponent.unknownByte1 = stream.getByte();
            }
            if (rsComponent.type == 2) {
                rsComponent.inventory = new int[rsComponent.width * rsComponent.height];
                rsComponent.inventoryValue = new int[rsComponent.width * rsComponent.height];
                rsComponent.deleteOnDrag = stream.getByte() == 1;
                rsComponent.isInventoryInterface = stream.getByte() == 1;
                rsComponent.usableItemInterface = stream.getByte() == 1;
                rsComponent.replaceItems = stream.getByte() == 1;
                rsComponent.invSpritePadX = stream.getByte();
                rsComponent.invSpritePadY = stream.getByte();
                rsComponent.spritesX = new int[20];
                rsComponent.spritesY = new int[20];
                rsComponent.sprites = new Sprite[20];
                for (int j2 = 0; j2 < 20; j2++) {
                    rsComponent.hasSprites = stream.getByte();
                    if (rsComponent.hasSprites == 1) {
                        rsComponent.spritesX[j2] = stream.getSignedShort();
                        rsComponent.spritesY[j2] = stream.getSignedShort();
                        rsComponent.spriteData = stream.getString();
                        if (graphicArchive != null && rsComponent.spriteData.length() > 0) {
                            int i5 = rsComponent.spriteData.lastIndexOf(",");
                            rsComponent.sprites[j2] = getImage(Integer.parseInt(rsComponent.spriteData.substring(i5 + 1)),
                                    graphicArchive, rsComponent.spriteData.substring(0, i5));
                            rsComponent.sprites[j2].spriteName = rsComponent.spriteData.substring(0, i5);
                            rsComponent.sprites[j2].spriteIndex = Integer.parseInt(rsComponent.spriteData.substring(i5 + 1));
                        }
                    }
                }
                rsComponent.actions = new String[5];
                for (int optionIndex = 0; optionIndex < 5; optionIndex++) {
                    rsComponent.actions[optionIndex] = stream.getString();
                    if (rsComponent.actions[optionIndex].length() == 0) {
                        rsComponent.actions[optionIndex] = null;
                    }
                }
            }
            if (rsComponent.type == 3) {
                rsComponent.filled = stream.getByte() == 1;
            }
            if (rsComponent.type == 4 || rsComponent.type == 1) {
                rsComponent.centerText = stream.getByte() == 1;
                int k2 = stream.getByte();
                if (rsFonts != null) {
                    rsComponent.gameFont = rsFonts[k2];
                    rsComponent.rsFontIndex = k2;
                }
                rsComponent.textShadow = stream.getByte() == 1;
            }
            if (rsComponent.type == 4) {
                rsComponent.disabledMessage = StringUtility.cleanString(stream.getString().replaceAll("RuneScape", Constants.CLIENT_NAME));
                rsComponent.enabledMessage = StringUtility.cleanString(stream.getString());
            }
            if (rsComponent.type == 1 || rsComponent.type == 3 || rsComponent.type == 4) {
                rsComponent.textColor = stream.getInt();
            }
            if (rsComponent.type == 3 || rsComponent.type == 4) {
                rsComponent.enabledColor = stream.getInt();
                rsComponent.disabledMouseOverColor = stream.getInt();
                rsComponent.enabledMouseOverColor = stream.getInt();
            }
            if (rsComponent.type == 5) {
                rsComponent.drawsTransparent = false;
                rsComponent.disabledSpriteName = stream.getString();
                if (graphicArchive != null && rsComponent.disabledSpriteName.length() > 0) {
                    int i4 = rsComponent.disabledSpriteName.lastIndexOf(",");
                    rsComponent.getSpriteSet().setDisabled(getImage(Integer.parseInt(rsComponent.disabledSpriteName.substring(i4 + 1)), graphicArchive, rsComponent.disabledSpriteName.substring(0, i4)));
                }
                rsComponent.enabledSpriteName = stream.getString();
                if (graphicArchive != null && rsComponent.enabledSpriteName.length() > 0) {
                    int j4 = rsComponent.enabledSpriteName.lastIndexOf(",");
                    rsComponent.getSpriteSet().setEnabled(getImage(Integer.parseInt(rsComponent.enabledSpriteName.substring(j4 + 1)), graphicArchive, rsComponent.enabledSpriteName.substring(0, j4)));
                }
            }
            if (rsComponent.type == 6) {
                rsComponent.mediaHash = stream.getByte();
                if (rsComponent.mediaHash != 0) {
                    rsComponent.mediaType = 1;
                    rsComponent.randomByte1 = stream.getByte();
                    rsComponent.mediaID = (rsComponent.mediaHash - 1 << 8) + rsComponent.randomByte1;
                }
                rsComponent.mediaHash1 = stream.getByte();
                if (rsComponent.mediaHash1 != 0) {
                    rsComponent.enabledMediaType = 1;
                    rsComponent.randomByte2 = stream.getByte();
                    rsComponent.enabledMediaID = (rsComponent.mediaHash1 - 1 << 8) + rsComponent.randomByte2;
                }
                rsComponent.mediaHash2 = stream.getByte();
                if (rsComponent.mediaHash2 != 0) {
                    rsComponent.randomByte3 = stream.getByte();
                    rsComponent.disabledAnimationId = (rsComponent.mediaHash2 - 1 << 8) + rsComponent.randomByte3;
                } else {
                    rsComponent.disabledAnimationId = -1;
                }
                rsComponent.mediaHash3 = stream.getByte();
                if (rsComponent.mediaHash3 != 0) {
                    rsComponent.randomByte4 = stream.getByte();
                    rsComponent.enabledAnimationId = (rsComponent.mediaHash3 - 1 << 8) + rsComponent.randomByte4;
                } else {
                    rsComponent.enabledAnimationId = -1;
                }
                rsComponent.modelZoom = stream.getShort();
                rsComponent.modelRotation2 = stream.getShort();
                rsComponent.modelRotation1 = stream.getShort();
            }
            if (rsComponent.type == 7) {
                rsComponent.inventory = new int[rsComponent.width * rsComponent.height];
                rsComponent.inventoryValue = new int[rsComponent.width * rsComponent.height];
                rsComponent.centerText = stream.getByte() == 1;
                int l2 = stream.getByte();
                if (rsFonts != null) {
                    rsComponent.gameFont = rsFonts[l2];
                    rsComponent.rsFontIndex = l2;
                }
                rsComponent.textShadow = stream.getByte() == 1;
                rsComponent.textColor = stream.getInt();
                rsComponent.invSpritePadX = stream.getSignedShort();
                rsComponent.invSpritePadY = stream.getSignedShort();
                rsComponent.isInventoryInterface = stream.getByte() == 1;
                rsComponent.actions = new String[5];
                for (int k4 = 0; k4 < 5; k4++) {
                    rsComponent.actions[k4] = stream.getString();
                    if (rsComponent.actions[k4].length() == 0) {
                        rsComponent.actions[k4] = null;
                    }
                }

            }
            if (rsComponent.optionType == 2 || rsComponent.type == 2) {
                rsComponent.selectedActionName = stream.getString();
                rsComponent.spellName = stream.getString();
                rsComponent.spellUsableOn = stream.getShort();
            }

            if (rsComponent.type == 8) {
                rsComponent.disabledMessage = StringUtility.cleanString(stream.getString());
            }

            if (rsComponent.optionType == 1 || rsComponent.optionType == 4 || rsComponent.optionType == 5 || rsComponent.optionType == 6) {
                rsComponent.tooltip = stream.getString();
                if (rsComponent.tooltip.length() == 0) {
                    if (rsComponent.optionType == 1) {
                        rsComponent.tooltip = "Ok";
                    }
                    if (rsComponent.optionType == 4) {
                        rsComponent.tooltip = "Select";
                    }
                    if (rsComponent.optionType == 5) {
                        rsComponent.tooltip = "Select";
                    }
                    if (rsComponent.optionType == 6) {
                        rsComponent.tooltip = "Continue";
                    }
                }
            }
        }
        cacheArchive = interfaceArchive;
        InterfaceRepository interfaceRepository = new InterfaceRepository();
        for (InterfacePlugin interfacePlugin : interfaceRepository) {
            interfacePlugin.loadInterface(rsFonts);
        }
        fixPositioning();
        if (Constants.DEBUG_MODE) {
            logger.log(Level.INFO, "Interface: " + getFreeIdSet(1, 400));
        }
        spriteCache = null;
    }

    /**
     * Removes interfaces by id.
     *
     * @param ids The ids of the interface.
     */
    public static void removeConfig(int... ids) {
        for (int id : ids) {
            COMPONENT_CACHE[id] = new RSComponent();
        }
    }

    /**
     * Gets an image from the cache.
     *
     * @param spriteId      The id of the sprite.
     * @param cacheArchive1 The {@link com.runescape.net.CacheArchive}.
     * @param spriteName    The name of the sprite.
     * @return The {@link com.runescape.cache.media.Sprite}.
     */
    public static Sprite getImage(int spriteId, CacheArchive cacheArchive1, String spriteName) {
        long l = (StringUtility.hashSpriteName(spriteName) << 8) + (long) spriteId;
        Sprite sprite = (Sprite) spriteCache.get(l);
        if (sprite != null) {
            return sprite;
        }
        try {
            sprite = new Sprite(cacheArchive1, spriteName, spriteId);
            spriteCache.put(sprite, l);
        } catch (Exception _ex) {
            return null;
        }
        return sprite;
    }

    /**
     * Sets a model in the cache.
     *
     * @param flag
     * @param model The model.
     */
    public static void setModel(boolean flag, Model model) {
        if (flag) {
            return;
        }
        modelCache.clear();
        if (model != null) {
            modelCache.put(model, (5 << 16));
        }
    }

    /**
     * Gets the {@link #COMPONENT_CACHE}.
     *
     * @return The interface cache.
     */
    public static RSComponent[] getComponentCache() {
        return COMPONENT_CACHE;
    }

    /**
     * Sets the {@link #COMPONENT_CACHE}.
     *
     * @param componentCache The interface cache to set.
     */
    public static void setComponentCache(RSComponent[] componentCache) {
        COMPONENT_CACHE = componentCache;
    }

    /**
     * Gets a cached {@link RSComponent} by id.
     *
     * @param id The id of the interface.
     * @return The {@code RSComponent}.
     */
    public static RSComponent forId(int id) {
        return COMPONENT_CACHE[id];
    }

    public static boolean interfaceContainsItem(int interfaceId) {
        RSComponent rsComponent = RSComponent.getComponentCache()[interfaceId];
        if (rsComponent == null || rsComponent.inventory == null) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < rsComponent.inventory.length; i++) {
            if (rsComponent.inventory[i] > 0) {
                count++;
            }
        }
        return count > 0;
    }

    /**
     * Gets the next free id set in a row from the max amount.
     *
     * @param start The starting index.
     * @param max   The max amount.
     * @return The free id.
     */
    public static int getFreeIdSet(int start, int max) {
        int freeId = 0;
        int free = 0;
        for (int id = start; id < 65000; id++) {
            if (forId(id) != null) {
                free = 0;
                freeId = 0;
                continue;
            }
            free++;
            freeId = ((id + 1) - max);
            if (free == max) {
                break;
            }
        }
        for (int x = freeId; x < (freeId + max); x++) {
            if (forId(x) != null) {
                //com.runescape.PrinterOut.println(RSComponent.class, "UH OH!");
                break;
            }
        }
        return freeId;
    }

    /**
     * Gets the next free id set in a row from the max amount.
     *
     * @param max The max amount.
     * @return The free id.
     */
    public static int getFreeIdSet(int max) {
        return getFreeIdSet(0, max);
    }

    /**
     * Gets the next free id from a start position.
     *
     * @param start The starting id.
     * @return The free id.
     */
    public static int getFreeId(int start) {
        for (int id = start; id < 65000; id++) {
            if (forId(id) == null) {
                return id;
            }
        }
        return -1;
    }

    public static void setTextColor(int id, int color) {
        forId(id).textColor = color;
    }

    /**
     * Adds a filler component.
     *
     * @param id The id.
     * @return The {@link RSComponent}.
     */
    public RSComponent addFiller(int id) {
        return new RSComponent().addInterface(id, 0, 0);
    }

    public int getMOverInterToTrigger() {
        return mOverInterToTrigger;
    }

    /**
     * Adds a new {@link RSComponent} child to the cache under a parent.
     *
     * @param id The id of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addInterface(int id) {
        return addInterface(id, 512, 334);
    }

    /**
     * Adds a new {@link RSComponent} child to the cache under a parent.
     *
     * @param id     The id of the interface.
     * @param width  The width.
     * @param height The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addInterface(int id, int width, int height) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.width = width;
        rsComponent.height = height;
        return rsComponent;
    }

    /**
     * Adds a new {@link RSComponent} child to the cache under a parent as a tab.
     *
     * @param id The id of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addTabInterface(int id) {
        return addTabInterface(id, 512, 700);
    }

    /**
     * Adds a new {@link RSComponent} child to the cache under a parent as a tab.
     *
     * @param id     The id of the interface.
     * @param width  The width.
     * @param height The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addTabInterface(int id, int width, int height) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = (byte) 0;
        rsComponent.hoverType = -1;
        return rsComponent;
    }

    /**
     * Adds a full screen {@link RSComponent} child.
     *
     * @param id The id of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addFullScreenInterface(int id) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.width = 765;
        rsComponent.height = 503;
        return rsComponent;
    }

    /**
     * Adds a child to this {@link RSComponent}.
     * and remove #index param.
     *
     * @param id      The id of the child.
     * @param xOffset The x offset of the child.
     * @param yOffset The y offset of the child.
     */
    public RSComponent addChild(int id, int xOffset, int yOffset) {
        interfaceChildren.add(new InterfaceChild(id, xOffset, yOffset));
        return this;
    }

    /**
     * Repositions this <code>RSComponent</code>.
     *
     * @param id      The id of the child.
     * @param xOffset The x offset of the child.
     * @param yOffset The y offset of the child.
     */
    public RSComponent repositionChild(int id, int xOffset, int yOffset) {
        InterfaceChild interfaceChild;
        if ((interfaceChild = getInterfaceChild(id)) != null) {
            interfaceChild.setX(xOffset);
            interfaceChild.setY(yOffset);
        }
        return this;
    }

    /**
     * Sets the bounds of a child.
     *
     * @param id The id of the child.
     * @param x  The x coordinate of the child.
     * @param y  The y coordinate of the child.
     */
    public RSComponent setChildBounds(int id, int x, int y) {
        RSComponent rsComponent = null;
        InterfaceChild interfaceChild = getInterfaceChild(id);
        if (interfaceChild == null) {
            rsComponent = addChild(id, x, y);
        } else {
            interfaceChild.setX(x);
            interfaceChild.setY(y);
            rsComponent = this;
        }
        return rsComponent;
    }

    /**
     * Swaps inventory item indexes.
     *
     * @param index     The index.
     * @param swapIndex The index to swap with.
     */
    public void swapInventoryItems(int index, int swapIndex) {
        int currentIndex = inventory[index];
        inventory[index] = inventory[swapIndex];
        inventory[swapIndex] = currentIndex;
        currentIndex = inventoryValue[index];
        inventoryValue[index] = inventoryValue[swapIndex];
        inventoryValue[swapIndex] = currentIndex;
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, SpriteSet spriteSet, int configId, int configFrame, boolean drawsTransparent, int width, int height) {
        return addSprite(id, spriteSet, configId, configFrame, drawsTransparent, width, height, false);
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, SpriteSet spriteSet, int configId, int configFrame, boolean drawsTransparent, int width, int height, boolean interfaceConfig) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = (byte) 0;
        rsComponent.hoverType = -1;
        if (configFrame > -1) {
            rsComponent.scriptOperators = new int[1];
            rsComponent.scriptDefaults = new int[1];
            rsComponent.scriptOperators[0] = 1;
            rsComponent.scriptDefaults[0] = configId;
            rsComponent.scripts = new int[1][3];
            rsComponent.scripts[0][0] = 5;
            rsComponent.scripts[0][1] = configFrame;
            rsComponent.scripts[0][2] = 0;
        }
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.drawsTransparent = drawsTransparent;
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param sprite      The {@link com.runescape.cache.media.Sprite}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, Sprite sprite, int configId, int configFrame, boolean drawsTransparent, int width, int height) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = (byte) 0;
        rsComponent.hoverType = -1;
        if (configFrame > -1) {
            rsComponent.scriptOperators = new int[1];
            rsComponent.scriptDefaults = new int[1];
            rsComponent.scriptOperators[0] = 1;
            rsComponent.scriptDefaults[0] = configId;
            rsComponent.scripts = new int[1][3];
            rsComponent.scripts[0][0] = 5;
            rsComponent.scripts[0][1] = configFrame;
            rsComponent.scripts[0][2] = 0;
        }
        rsComponent.getSpriteSet().set(sprite, sprite);
        rsComponent.drawsTransparent = drawsTransparent;
        return rsComponent;
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param sprite      The {@link com.runescape.cache.media.Sprite}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, Sprite sprite, int configId, int configFrame) {
        return addSprite(id, sprite, configId, configFrame, false);
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param sprite      The {@link com.runescape.cache.media.Sprite}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, Sprite sprite, int configId, int configFrame, boolean interfaceConfig) {
        RSComponent rsComponent = addSprite(id, new SpriteSet(sprite, sprite), configId, configFrame, false, 512, 334);
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite}.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, SpriteSet spriteSet, int configId, int configFrame, boolean interfaceConfig) {
        RSComponent rsComponent = addSprite(id, spriteSet, configId, configFrame, false, 512, 334);
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite} without a configuration.
     *
     * @param id     The id.
     * @param sprite The {@link com.runescape.cache.media.Sprite}.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, Sprite sprite) {
        return addSprite(id, new SpriteSet(sprite, sprite), -1, -1, false, 512, 334);
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite} without a configuration.
     *
     * @param id        The id.
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, SpriteSet spriteSet) {
        return addSprite(id, spriteSet, -1, -1, false, 512, 334);
    }

    /**
     * Adds a {@link com.runescape.cache.media.Sprite} without a configuration.
     *
     * @param id     The id.
     * @param sprite The {@link com.runescape.cache.media.Sprite}.
     * @return The {@link RSComponent}.
     */
    public RSComponent addSprite(int id, Sprite sprite, boolean drawsTransparent) {
        return addSprite(id, new SpriteSet(sprite, sprite), -1, -1, drawsTransparent, 512, 334);
    }

    /**
     * Adds a flashing component.
     *
     * @param a
     * @param b
     * @param c TODO
     * @return
     */
    public RSComponent addFlash(int a, int b, int c) {
        this.flashes = true;
        return this;
    }


    /**
     * Adds a background.
     *
     * @param id     The id.
     * @param width  The width.
     * @param height The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addBorder(int id, int width, int height, boolean drawsTransparent, int opacity, int color, boolean filled) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 3;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = (byte) opacity;
        rsComponent.hoverType = -1;
        rsComponent.drawsTransparent = drawsTransparent;
        rsComponent.textColor = color;
        rsComponent.enabledColor = color;
        rsComponent.filled = filled;
        return rsComponent;
    }

    /**
     * Adds a background.
     *
     * @param id     The id.
     * @param width  The width.
     * @param height The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addBorder(int id, int width, int height, boolean drawsTransparent, int opacity, int color) {
        return addBorder(id, width, height, drawsTransparent, opacity, color, true);
    }

    /**
     * Adds an alpha box.
     * TODO 317
     */
    public RSComponent addAlphaBox(int boxOffsetX, int boxOffsetY, int boxWidth, int boxHeight, int boxColour, int boxAlpha) {
        this.alphaBox = true;
        this.boxX = this.getSpriteSet().getDisabled().myWidth - boxWidth;
        this.boxY = this.getSpriteSet().getDisabled().myHeight - boxHeight;
        this.boxOffsetX = boxOffsetX;
        this.boxOffsetY = boxOffsetY;
        this.boxColour = boxColour;
        this.boxAlpha = boxAlpha;
        return this;
    }

    /**
     * Adds an alpha box.
     * TODO 317
     */
    public RSComponent addAlphaBox1(int boxOffsetX, int boxOffsetY, int boxWidth, int boxHeight, int boxColour, int boxAlpha) {
        this.alphaBox = true;
        this.boxX = boxWidth;
        this.boxY = boxHeight;
        this.boxOffsetX = boxOffsetX;
        this.boxOffsetY = boxOffsetY;
        this.boxColour = boxColour;
        this.boxAlpha = boxAlpha;
        return this;
    }

    /**
     * Adds an action button.
     *
     * @param id        The id.
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @param width     The width.
     * @param height    The height.
     * @param tooltip   The tooltip.
     * @param hoverType The id of the hovered button.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, SpriteSet spriteSet, int width, int height, String tooltip, int hoverType) {
        return addActionButton(id, spriteSet, width, height, tooltip, hoverType, null);
    }

    /**
     * Adds an action button.
     *
     * @param id        The id.
     * @param sprite    The {@link com.runescape.cache.media.Sprite}.
     * @param width     The width.
     * @param height    The height.
     * @param tooltip   The tooltip.
     * @param hoverType The id of the hovered button.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, Sprite sprite, int width, int height, String tooltip, int hoverType) {
        return addActionButton(id, sprite == null ? null : new SpriteSet(sprite, sprite), width, height, tooltip, hoverType);
    }
    // TODO End clean

    /**
     * Adds an action button.
     *
     * @param id      The id.
     * @param sprite  The {@link com.runescape.cache.media.Sprite}.
     * @param width   The width.
     * @param height  The height.
     * @param tooltip The tooltip.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, Sprite sprite, int width, int height, String tooltip) {
        return addActionButton(id, sprite == null ? null : new SpriteSet(sprite, sprite), width, height, tooltip, 52);
    }

    /**
     * Adds an action button with a yellow tooltip.
     *
     * @param id        The id.
     * @param sprite    The {@link com.runescape.cache.media.Sprite}.
     * @param width     The width.
     * @param height    The height.
     * @param hoverType The id of the hovered button.
     * @param tooltips  The tooltips.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, Sprite sprite, int width, int height, int hoverType, String... tooltips) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.parentId = id;
        rsComponent.id = id;
        rsComponent.optionType = 1;
        rsComponent.width = width;
        rsComponent.hoverType = hoverType == -1 ? 52 : hoverType;
        rsComponent.hoverOnly = !(hoverType == -1 || hoverType == 52);
        rsComponent.contentType = rsComponent.hoverOnly ? -1 : 0;
        rsComponent.type = 5;
        rsComponent.height = height;
        rsComponent.tooltips = tooltips;
        rsComponent.getSpriteSet().set(sprite, sprite);
        return rsComponent;
    }

    /**
     * Adds an action button with a yellow tooltip.
     *
     * @param id        The id.
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @param width     The width.
     * @param height    The height.
     * @param hoverType The id of the hovered button.
     * @param tooltips  The tooltips.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, SpriteSet spriteSet, int width, int height, int hoverType, String... tooltips) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.parentId = id;
        rsComponent.id = id;
        rsComponent.optionType = 1;
        rsComponent.width = width;
        rsComponent.hoverType = hoverType == -1 ? 52 : hoverType;
        rsComponent.hoverOnly = !(hoverType == -1 || hoverType == 52);
        rsComponent.contentType = rsComponent.hoverOnly ? -1 : 0;
        rsComponent.type = 5;
        rsComponent.height = height;
        rsComponent.tooltips = tooltips;
        rsComponent.setSpriteSet(spriteSet);
        return rsComponent;
    }

    /**
     * Adds an action button with a yellow tooltip.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param width       The width.
     * @param height      The height.
     * @param tooltip     The tooltip.
     * @param hoverType   The id of the hovered button.
     * @param popupString The yellow tooltip text.
     * @return The {@link RSComponent}.
     */
    public RSComponent addActionButton(int id, SpriteSet spriteSet, int width, int height, String tooltip, int hoverType, String popupString) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.parentId = id;
        rsComponent.id = id;
        rsComponent.tooltip = tooltip;
        rsComponent.optionType = 1;
        rsComponent.width = width;
        rsComponent.hoverType = hoverType == -1 ? 52 : hoverType;
        rsComponent.hoverOnly = !(hoverType == -1 || hoverType == 52);
        rsComponent.contentType = rsComponent.hoverOnly ? -1 : 0;
        rsComponent.type = 5;
        rsComponent.height = height;
        rsComponent.popupString = popupString;
        rsComponent.setSpriteSet(spriteSet);
        return rsComponent;
    }

    /**
     * Adds a hoverable button.
     *
     * @param id           The id.
     * @param sprite       The {@link com.runescape.cache.media.Sprite}.
     * @param width        The width.
     * @param height       The height.
     * @param tooltip      The tooltip.
     * @param contentType  The content type.
     * @param hoverType    The id of the hovered button.
     * @param atActionType The at action type.
     * @param hidden       If this button is hidden.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverButton(int id, Sprite sprite, int width, int height, String tooltip, int contentType, int hoverType, int atActionType, boolean hidden) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = contentType;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.tooltip = tooltip;
        rsComponent.getSpriteSet().set(sprite, sprite);
        return rsComponent;
    }

    /**
     * Adds a hoverable button.
     *
     * @param id           The id.
     * @param sprite       The {@link com.runescape.cache.media.Sprite}.
     * @param width        The width.
     * @param height       The height.
     * @param tooltip      The tooltip.
     * @param contentType  The content type.
     * @param hoverType    The id of the hovered button.
     * @param atActionType The at action type.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverButton(int id, Sprite sprite, int width, int height, String tooltip, int contentType, int hoverType, int atActionType) {
        return addHoverButton(id, sprite, width, height, tooltip, contentType, hoverType, atActionType, false);
    }

    /**
     * Adds a hovered button.
     *
     * @param id      The id.
     * @param sprite  The {@link com.runescape.cache.media.Sprite}.
     * @param width   The width.
     * @param height  The height.
     * @param childId The child of this button.
     * @param hidden  If this button is hidden.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoveredButton(int id, Sprite sprite, int width, int height, int childId, boolean hidden) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.hoverOnly = true;
        rsComponent.opacity = 0;
        rsComponent.hoverType = -1;
        rsComponent.scrollMax = 0;
        rsComponent.hidden = hidden;
        addHoverImage(childId, new SpriteSet(sprite, sprite), width, height);
        rsComponent.addChild(childId, 0, 0);
        return rsComponent;
    }

    /**
     * Adds a hovered button.
     *
     * @param id      The id.
     * @param sprite  The {@link com.runescape.cache.media.Sprite}.
     * @param width   The width.
     * @param height  The height.
     * @param childId The child of this button.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoveredButton(int id, Sprite sprite, int width, int height, int childId) {
        return addHoveredButton(id, sprite, width, height, childId, false);
    }

    // TODO Clean these
    public RSComponent addHoverConfigButton(int id, int hoverOver, SpriteSet spriteSet, int width, int height, String tooltip, int[] scriptOperators, int[] scriptDefaults, int[][] scripts) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.type = 5;
        rsComponent.optionType = 5;
        rsComponent.contentType = 206;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverOver;
        rsComponent.scriptOperators = scriptOperators;
        rsComponent.scriptDefaults = scriptDefaults;
        rsComponent.scripts = scripts;
        rsComponent.spriteSet = spriteSet;
        rsComponent.tooltip = tooltip;
        return rsComponent;
    }

    public RSComponent addHoveredConfigButton(RSComponent original, int ID, int IMAGEID, SpriteSet spriteSet) {
        RSComponent rsComponent = addTabInterface(ID);
        rsComponent.parentId = original.id;
        rsComponent.id = ID;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = original.width;
        rsComponent.height = original.height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = -1;
        RSComponent hover = addInterface(IMAGEID);
        hover.type = 5;
        hover.width = original.width;
        hover.height = original.height;
        rsComponent.scriptOperators = original.scriptOperators;
        rsComponent.scriptDefaults = original.scriptDefaults;
        hover.scripts = original.scripts;
        hover.spriteSet = spriteSet;
        rsComponent.setChildBounds(IMAGEID, 0, 0);
        rsComponent.tooltip = original.tooltip;
        rsComponent.hoverOnly = true;
        return rsComponent;
    }

    /**
     * Adds an interface configuration.
     *
     * @param configId The configuration id.
     * @param value    The default configuration value.
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent addInterfaceConfig(int configId, int value, int operator) {
        scriptOperators = new int[1];
        scriptDefaults = new int[1];
        scriptOperators[0] = operator;
        scriptDefaults[0] = value;
        scripts = new int[1][3];
        scripts[0][0] = 5;
        scripts[0][1] = configId;
        scripts[0][2] = 0;
        interfaceConfig = true;
        return this;
    }

    /**
     * Adds an interface configuration.
     *
     * @param configId The configuration id.
     * @param value    The default configuration value.
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent addInterfaceConfig(int configId, int value) {
        return addInterfaceConfig(configId, value, 1);
    }

    /**
     * Adds an action button with a configuration.
     *
     * @param id        The id.
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @param width     The width.
     * @param height    The height.
     * @param tooltips  The tooltips.
     * @param hoverType The hovered id.
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigButton(int id, SpriteSet spriteSet, int width, int height, String[] tooltips, int[] scriptOperators, int[] scriptDefaults, int[][] scripts, int hoverType) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 8;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.scriptOperators = scriptOperators;
        rsComponent.scriptDefaults = scriptDefaults;
        rsComponent.scripts = scripts;
        rsComponent.setSpriteSet(spriteSet);
        if (tooltips != null) {
            rsComponent.tooltips = tooltips;
        }
        return rsComponent;
    }

    /**
     * Adds an action button with a configuration.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param width       The width.
     * @param height      The height.
     * @param tooltips    The tooltips.
     * @param configValue The configuration id.
     * @param configFrame The configuration frame.
     * @param hoverType   The hovered id.
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigButton(int id, SpriteSet spriteSet, int width, int height, String[] tooltips, int configValue, int configFrame, int hoverType, int optionType) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = optionType;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configValue;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent.setSpriteSet(spriteSet);
        if (tooltips != null) {
            rsComponent.tooltips = tooltips;
        }
        return rsComponent;
    }

    /**
     * Adds an action button with a configuration.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param width       The width.
     * @param height      The height.
     * @param tooltips    The tooltips.
     * @param configValue The configuration id.
     * @param configFrame The configuration frame.
     * @param hoverType   The hovered id.
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigButton(int id, SpriteSet spriteSet, int width, int height, String[] tooltips, int configValue, int configFrame, int hoverType) {
        return addConfigButton(id, spriteSet, width, height, tooltips, configValue, configFrame, hoverType, 8);
    }

    /**
     * Adds an action button with a configuration.
     *
     * @param id          The id.
     * @param spriteSet   The {@link com.runescape.cache.media.SpriteSet}.
     * @param width       The width.
     * @param height      The height.
     * @param tooltips    The tooltips.
     * @param configId    The configuration id.
     * @param configFrame The configuration frame.
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigButton(int id, SpriteSet spriteSet, int width, int height, String[] tooltips, int configId, int configFrame) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 8;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = -1;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent.setSpriteSet(spriteSet);
        if (tooltips != null) {
            rsComponent.tooltips = tooltips;
        }
        return rsComponent;
    }

    /**
     * Adds a hoverable action button text with an interface configuration and a yellow tooltip.
     *
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigText(int id, int atActionType, int hoverType, String disabledMessage, int width,
                                     int height, int configFrame, int configId, String tooltip, boolean interfaceConfig, String popupString) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 8;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.disabledMessage = disabledMessage;
        rsComponent.popupString = popupString;
        rsComponent.width = width;
        rsComponent.tooltip = tooltip;
        rsComponent.height = height;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent = addTabInterface(hoverType);
        rsComponent.parentId = hoverType;
        rsComponent.id = hoverType;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.width = 550;
        rsComponent.height = 334;
        rsComponent.hoverOnly = true;
        rsComponent.hoverType = -1;
//        rsComponent.addHoverBox(hoverId3, hoverDisabledText, hoverEnabledText, configId, configFrame);
//        rsComponent.setChildren(2);
//        rsComponent.setChildBounds(hoverId2, 15, 60, 0);
//        rsComponent.setChildBounds(hoverId3, xOffset, yOffset, 1);
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    /**
     * Adds a hoverable action button with an interface configuration.
     *
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigHover(int id, int atActionType, int hoverType, SpriteSet spriteSet, int width,
                                      int height, int configFrame, int configId, String tooltip,
                                      int hoverId2, int hoverId3, String hoverDisabledText,
                                      String hoverEnabledText, int xOffset, int yOffset, boolean interfaceConfig) {
        return addConfigHover(id, atActionType, hoverType, spriteSet, width, height, configFrame, configId, tooltip, hoverId2, hoverId3, hoverDisabledText, hoverEnabledText, xOffset, yOffset, interfaceConfig, null);
    }

    /**
     * Adds a hoverable action button with an interface configuration and a yellow tooltip.
     *
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigHover(int id, int atActionType, int hoverType, SpriteSet spriteSet, int width,
                                      int height, int configFrame, int configId, String tooltip,
                                      int hoverId2, int hoverId3, String hoverDisabledText,
                                      String hoverEnabledText, int xOffset, int yOffset, boolean interfaceConfig, String popupString) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 8;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.popupString = popupString;
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.width = width;
        rsComponent.tooltip = tooltip;
        rsComponent.height = height;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent.interfaceConfig = interfaceConfig;
        rsComponent = addTabInterface(hoverType);
        rsComponent.parentId = hoverType;
        rsComponent.id = hoverType;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.width = 550;
        rsComponent.height = 334;
        rsComponent.hoverOnly = true;
        rsComponent.hoverType = -1;
        rsComponent.interfaceConfig = interfaceConfig;
        rsComponent.addSprites(hoverId2, spriteSet, configId, configFrame, interfaceConfig);
        rsComponent.addHoverBox(hoverId3, hoverDisabledText, hoverEnabledText, configId, configFrame, interfaceConfig);
        rsComponent.setChildBounds(hoverId2, 15, 60);
        rsComponent.setChildBounds(hoverId3, xOffset, yOffset);
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    /**
     * Adds a hoverable action button with a configuration.
     *
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigHover(int id, int atActionType, int hoverType, SpriteSet spriteSet, int width,
                                      int height, int configFrame, int configId, String tooltip,
                                      int hoverId2, int hoverId3, String hoverDisabledText,
                                      String hoverEnabledText, int X, int Y, boolean interfaceConfig, boolean automaticConfig) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.width = width;
        rsComponent.tooltip = tooltip;
        rsComponent.height = height;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent = addTabInterface(hoverType);
        rsComponent.parentId = hoverType;
        rsComponent.id = hoverType;
        rsComponent.type = 0;
        rsComponent.optionType = 0;
        rsComponent.width = 550;
        rsComponent.height = 334;
        rsComponent.hoverOnly = true;
        rsComponent.hoverType = -1;
        rsComponent.addSprites(hoverId2, spriteSet, configId, configFrame, false);
        rsComponent.addHoverBox(hoverId3, hoverDisabledText, hoverEnabledText, configId, configFrame, false);
        rsComponent.setChildBounds(hoverId2, 15, 60);
        rsComponent.setChildBounds(hoverId3, X, Y);
        rsComponent.interfaceConfig = interfaceConfig;
        rsComponent.automaticConfig = automaticConfig;
        return rsComponent;
    }

    /**
     * Adds a hoverable action button with a configuration.
     *
     * @return The {@link RSComponent}.
     */
    public RSComponent addConfigHover(int id, int atActionType, int hoverType, SpriteSet spriteSet, int width,
                                      int height, int configFrame, int configId, String tooltip,
                                      int hoverId2, int hoverId3, String hoverDisabledText,
                                      String hoverEnabledText, int X, int Y) {
        return addConfigHover(id, atActionType, hoverType, spriteSet, width, height, configFrame, configId, tooltip, hoverId2, hoverId3, hoverDisabledText, hoverEnabledText, X, Y, false, false);
    }

    /**
     * Adds an image that is hovered.
     *
     * @param id        The id.
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverImage(int id, SpriteSet spriteSet, int width, int height) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 52;
        rsComponent.setSpriteSet(spriteSet);
        return rsComponent;
    }

    /**
     * Adds text.
     *
     * @param id              The id
     * @param disabledMessage The message.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}.
     * @param fontIndex       The index of the font.
     * @param textColor       The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @param height          The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addText(int id, String disabledMessage, String enabledMessage, GameFont[] rsFonts, int fontIndex, int textColor, boolean centerText, boolean textShadow, int width, int height, int hoverType, int configId, int configValue) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = id;
        rsComponent.type = 4;
        rsComponent.optionType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.mOverInterToTrigger = -1;
        rsComponent.centerText = centerText; // false
        rsComponent.textShadow = textShadow; // true
        rsComponent.gameFont = rsFonts[fontIndex];
        rsComponent.disabledMessage = disabledMessage;
        rsComponent.enabledMessage = enabledMessage;
        rsComponent.textColor = textColor;
        rsComponent.enabledColor = textColor;
        rsComponent.disabledMouseOverColor = 0;
        rsComponent.enabledMouseOverColor = 0;
        rsComponent.hoverType = hoverType;
        if (configId != -1) {
            rsComponent.scriptOperators = new int[1];
            rsComponent.scriptDefaults = new int[1];
            rsComponent.scriptOperators[0] = 1;
            rsComponent.scriptDefaults[0] = configValue;
            rsComponent.scripts = new int[1][3];
            rsComponent.scripts[0][0] = 5;
            rsComponent.scripts[0][1] = configId;
            rsComponent.scripts[0][2] = 0;
            rsComponent.interfaceConfig = true;
        }
        return rsComponent;
    }

    /**
     * Adds text.
     *
     * @param id              The id
     * @param disabledMessage The message.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}.
     * @param fontIndex       The index of the font.
     * @param textColor       The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @param height          The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int textColor, boolean centerText, boolean textShadow, int width, int height, int hoverType) {
        return addText(id, disabledMessage, "", rsFonts, fontIndex, textColor, centerText, textShadow, width, height, hoverType, -1, -1);
    }

    /**
     * Adds text.
     *
     * @param id              The id
     * @param disabledMessage The message.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}.
     * @param fontIndex       The index of the font.
     * @param textColor       The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @param height          The height.
     * @return The {@link RSComponent}.
     */
    public RSComponent addText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int textColor, int enabledColor, boolean centerText, boolean textShadow, int width, int height, int hoverType) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = id;
        rsComponent.type = 4;
        rsComponent.optionType = 0;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.mOverInterToTrigger = -1;
        rsComponent.centerText = centerText; // false
        rsComponent.textShadow = textShadow; // true
        rsComponent.gameFont = rsFonts[fontIndex];
        rsComponent.disabledMessage = disabledMessage;
        rsComponent.enabledMessage = "";
        rsComponent.textColor = textColor;
        rsComponent.enabledColor = enabledColor;
        rsComponent.disabledMouseOverColor = 0;
        rsComponent.enabledMouseOverColor = 0;
        rsComponent.hoverType = hoverType;
        return rsComponent;
    }

    /**
     * Adds text.
     *
     * @param id              The id
     * @param disabledMessage The message.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}.
     * @param fontIndex       The index of the font.
     * @param textColor       The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int textColor, boolean centerText, boolean textShadow, int width, int hoverType) {
        return addText(id, disabledMessage, rsFonts, fontIndex, textColor, centerText, textShadow, width, 11, hoverType);
    }

    /**
     * Adds text with multiple hovers.
     *
     * @param id              The id of the interface.
     * @param disabledMessage The message.
     * @param tooltips        The tooltips.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}s.
     * @param fontIndex       The index of the font.
     * @param color           The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int color, boolean centerText, boolean textShadow, int width, String... tooltips) {
        return addHoverText(id, disabledMessage, rsFonts, fontIndex, color, 0xFFFFFF, centerText, textShadow, width, 11, tooltips);
    }

    /**
     * Adds text with multiple hovers.
     *
     * @param id              The id of the interface.
     * @param disabledMessage The message.
     * @param tooltips        The tooltips.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}s.
     * @param fontIndex       The index of the font.
     * @param color           The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int color, int hoverColor, boolean centerText, boolean textShadow, int width, String... tooltips) {
        return addHoverText(id, disabledMessage, rsFonts, fontIndex, color, hoverColor, centerText, textShadow, width, 11, tooltips);
    }

    /**
     * Adds text with multiple hovers.
     *
     * @param id              The id of the interface.
     * @param disabledMessage The message.
     * @param tooltips        The tooltips.
     * @param rsFonts         The {@link com.runescape.media.font.GameFont}s.
     * @param fontIndex       The index of the font.
     * @param textColor       The color.
     * @param centerText      Whether or not to center the text.
     * @param textShadow      Whether or not the text is textShadow.
     * @param width           The width of the interface.
     * @return The {@link RSComponent}.
     */
    public RSComponent addHoverText(int id, String disabledMessage, GameFont[] rsFonts, int fontIndex, int textColor, int hoverColor, boolean centerText, boolean textShadow, int width, int height, String... tooltips) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.hoverType = id;
        rsComponent.type = 4;
        rsComponent.optionType = tooltips == null ? 1 : tooltips.length == 1 ? 1 : 8;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.mOverInterToTrigger = -1;
        rsComponent.centerText = centerText;
        rsComponent.textShadow = textShadow;
        rsComponent.gameFont = rsFonts[fontIndex];
        rsComponent.disabledMessage = disabledMessage;
        rsComponent.enabledMessage = "";
        rsComponent.textColor = textColor;
        rsComponent.enabledColor = 0;
        rsComponent.disabledMouseOverColor = hoverColor;
        rsComponent.enabledMouseOverColor = 0;
        if (tooltips != null && tooltips.length == 1) {
            rsComponent.tooltip = tooltips[0];
            return rsComponent;
        }
        rsComponent.tooltips = tooltips;
        return rsComponent;
    }

    /**
     * Adds a button to this interface.
     *
     * @param id           The id of the interface.
     * @param spriteSet    The {@link com.runescape.cache.media.SpriteSet}.
     * @param width        The width of the interface.
     * @param height       The height of the interface.
     * @param tooltip      The tooltip.
     * @param atActionType The action type.
     * @return The {@link RSComponent}.
     */
    public RSComponent addButton(int id, SpriteSet spriteSet, int width, int height, String tooltip, int atActionType) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.type = 5;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 52;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.tooltip = tooltip;
        return rsComponent;
    }

    /**
     * Adds a button to this interface.
     *
     * @param id           The id of the interface.
     * @param spriteSet    The {@link com.runescape.cache.media.SpriteSet}.
     * @param width        The width of the interface.
     * @param height       The height of the interface.
     * @param tooltip      The tooltip.
     * @param atActionType The action type.
     * @return The {@link RSComponent}.
     */
    public RSComponent addButton(int id, SpriteSet spriteSet, int width, int height, String tooltip, int atActionType, int hoverType) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.type = 5;
        rsComponent.optionType = atActionType;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = hoverType;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.tooltip = tooltip;
        return rsComponent;
    }

    /**
     * Adds a button to this interface.
     *
     * @param id           The id of the interface.
     * @param sprite       The {@link com.runescape.cache.media.Sprite}.
     * @param width        The width of the interface.
     * @param height       The height of the interface.
     * @param tooltip      The tooltip.
     * @param atActionType The action type.
     * @return The {@link RSComponent}.
     */
    public RSComponent addButton(int id, Sprite sprite, int width, int height, String tooltip, int atActionType) {
        return addButton(id, new SpriteSet(sprite, sprite), width, height, tooltip, atActionType);
    }

    /**
     * Adds a container. TODO
     *
     * @param id
     * @param contentType
     * @param width
     * @param height
     * @param actions
     * @return
     */
    public RSComponent addContainer(int id, int contentType, int width, int height, String... actions) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = id;
        rsComponent.type = 9;
        rsComponent.contentType = contentType;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.sprites = new Sprite[20];
        rsComponent.spritesX = new int[20];
        rsComponent.spritesY = new int[20];
        rsComponent.invSpritePadX = 16;
        rsComponent.invSpritePadY = 4;
        rsComponent.inventory = new int[width * height];
        rsComponent.inventoryValue = new int[width * height];
        rsComponent.deleteOnDrag = true;
        rsComponent.actions = actions;
        return rsComponent;
    }

    /**
     * Adds a container. TODO
     *
     * @param id
     * @param contentType
     * @param width
     * @param height
     * @param actions
     * @return
     */
    public RSComponent addContainer(int id, int contentType, int width, int height, int type, String... actions) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = id;
        rsComponent.type = type;
        rsComponent.contentType = contentType;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.sprites = new Sprite[20];
        rsComponent.spritesX = new int[20];
        rsComponent.spritesY = new int[20];
        rsComponent.invSpritePadX = 16;
        rsComponent.invSpritePadY = 4;
        rsComponent.inventory = new int[width * height];
        rsComponent.inventoryValue = new int[width * height];
        rsComponent.deleteOnDrag = true;
        rsComponent.actions = actions;
        return rsComponent;
    }

    // TODO Rewrite Lunar

    /**
     * Adds the current player's character to an interface.
     *
     * @param id The id of the interface to add the character on.
     * @return The {@link RSComponent}.
     */
    public RSComponent addChar(int id) {
        RSComponent rsComponent = COMPONENT_CACHE[id] = new RSComponent();
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 6;
        rsComponent.optionType = 0;
        rsComponent.contentType = 328;
        rsComponent.width = 136;
        rsComponent.height = 168;
        rsComponent.opacity = 0;
        rsComponent.mOverInterToTrigger = 0;
        rsComponent.modelZoom = 560;
        rsComponent.modelRotation2 = 150;
        rsComponent.modelRotation1 = 0;
        rsComponent.disabledAnimationId = -1;
        rsComponent.enabledAnimationId = -1;
        return rsComponent;
    }

    /**
     * Adds a tooltip to the interface.
     *
     * @param id   The child id of the tooltip.
     * @param text The text.
     * @return The {@link RSComponent}.
     */
    public RSComponent addTooltipBox(int id, String text) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 8;
        rsComponent.popupString = text;
        rsComponent.disabledMessage = text;
        return rsComponent;
    }

    /**
     * Adds a tooltip hover to the interface.
     *
     * @param id   The id of the tooltip.
     * @param text The text.
     */
    public RSComponent addTooltip(int id, String text) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.type = 0;
        rsComponent.hoverOnly = true;
        rsComponent.hoverType = -1;
        rsComponent.disabledMessage = text;
        rsComponent.addTooltipBox(id + 1, text);
        rsComponent.addChild((id + 1), 0, 0);
        return rsComponent;
    }

    // TODO
    public RSComponent addInputField(int id, int characterLimit, int color, String text, int width, int height, boolean asterisks, boolean updatesEveryInput) {
        RSComponent rsComponent = addFullScreenInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 16;
        rsComponent.optionType = 8;
        rsComponent.disabledMessage = text;
        rsComponent.width = width;
        rsComponent.height = height;
        // rsComponent.characterLimit = characterLimit;
        rsComponent.textColor = color;
        // rsComponent.displayAsterisks = asterisks;
        // rsComponent.defaultInputFieldText = text;
        rsComponent.tooltips = new String[]{"Clear", "Edit"};
        //rsComponent.updatesEveryInput = updatesEveryInput;
        return rsComponent;
    }

    /**
     * TODO getModel()
     *
     * @param modelType
     * @param modelId
     * @return
     */

    public Model method206(int modelType, int modelId) {
        int m = (modelType << 16) + modelId;
        Model model = (Model) modelCache.get(m);
        if (model != null) {
            return model;
        }
        if (modelType == 1) {
            model = Model.getModel(modelId, false);
        }
        if (modelType == 2) {
            model = NPCDefinition.forId(modelId).model();
        }
        if (modelType == 3) {
            try {
                model = Game.getLocalPlayer().getComponentModel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (modelType == 4) {
            model = ItemDefinition.forId(modelId).getUnshadedModel(50);
        }
        if (modelType == 5) {
            model = null;
        }
        if (model != null) {
            modelCache.put(model, (modelType << 16) + modelId);
        }
        return model;
    }

    /**
     * Gets an animated {@link com.runescape.media.renderable.Model}.
     *
     * @param frameId     The frame id.
     * @param secondFrame The second frame id.
     * @param active      If it is active.
     * @return The {@link com.runescape.media.renderable.Model}.
     */
    public Model getAnimatedModel(int frameId, int secondFrame, boolean active) {
        Model model;
        if (active) {
            model = method206(enabledMediaType, enabledMediaID);
        } else {
            model = method206(mediaType, mediaID);
        }
        if (model == null) {
            return null;
        }
        if (secondFrame == -1 && frameId == -1 && model.triangleColorValues == null) {
            return model;
        }
        Model model_1 = new Model(true, SequenceFrame.method532(secondFrame) &
                SequenceFrame.method532(frameId), false, model);
        if (secondFrame != -1 || frameId != -1) {
            model_1.skin();
        }
        if (secondFrame != -1) {
            model_1.apply(secondFrame, false);
        }
        if (frameId != -1) {
            model_1.apply(frameId, false);
        }
        model_1.light(64, 768, -50, -10, -50, true);
        return model_1;
    }

    /**
     * Draws a requirement box.
     *
     * @return
     */
    public RSComponent drawRequirementBox(int id, int itemId, int itemCount, int levelRequired, String name, String disabledMessage, GameFont[] gameFonts,
                                          SpriteSet spriteSet, int spellUsableOn, int optionType) {
        RSComponent rsComponent = addTabInterface(id, 31, 30);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = optionType;
        rsComponent.spellUsableOn = spellUsableOn;
        rsComponent.spriteSet = spriteSet;
        rsComponent.contentType = 0;
        rsComponent.hoverType = id + 1;
        rsComponent.tooltip = "Cast <col=00FF00>" + name;
        rsComponent.selectedActionName = "Cast ->";
        rsComponent.spellName = "<col=00FF00>" + name + "</col>";
        rsComponent.scriptOperators = new int[2];
        rsComponent.scriptDefaults = new int[2];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = (itemCount - 1);
        rsComponent.scriptOperators[1] = 3;
        rsComponent.scriptDefaults[1] = levelRequired;
        rsComponent.scripts = new int[2][];
        rsComponent.scripts[0] = new int[4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = itemId;
        rsComponent.scripts[0][3] = 0;
        rsComponent.scripts[1] = new int[3];
        rsComponent.scripts[1][0] = 1;
        rsComponent.scripts[1][1] = 6;
        rsComponent.scripts[1][2] = 0;
        rsComponent.gameFont = gameFonts[1];
        RSComponent rsComponent1 = addInterface(id + 1);
        rsComponent1.hoverOnly = true;
        rsComponent1.hoverType = -1;

        rsComponent1.addBorder(id + 2, 180, 93, false, 0, 0x726451, false);
        rsComponent1.addBorder(id + 3, 178, 91, false, 0, 0x2E2B23, false);
        rsComponent1.addBorder(id + 4, 176, 89, false, 40, 0, true);
        rsComponent1.addChild(id + 2, 0, 0);
        rsComponent1.addChild(id + 3, 1, 1);
        rsComponent1.addChild(id + 4, 2, 2);

        rsComponent1.addText(id + 5, "Level " + levelRequired + ": Howl (3 Special Move\\npoints)", gameFonts, REGULAR, 0xFF981F, true, true, 0, 52);
        rsComponent1.setChildBounds(id + 5, 92, 2);
        rsComponent1.addText(id + 6, disabledMessage, gameFonts, SMALL, 0xAF6A1A, true, true, 0, 52);
        rsComponent1.setChildBounds(id + 6, 92, 31);
        rsComponent1.drawRequirementItem(id + 8, id + 7, itemId, itemCount, SMALL, gameFonts);
        rsComponent1.addChild(id + 7, 75, 46);
        rsComponent1.addChild(id + 8, 89, 78);
        return rsComponent1;
    }

    /**
     * Draws a requirement item text.
     *
     * @param itemId    The id of the item.
     * @param itemCount The count of the item required.
     * @param gameFonts The game fonts.
     */
    public RSComponent drawRequirementItem(int id, int itemChildId, int itemId, int itemCount, int fontIndex, GameFont[] gameFonts) {
        RSComponent rsComponent1 = addInterface(id);
        rsComponent1.id = id;
        rsComponent1.parentId = this.id;
        rsComponent1.type = 4;
        rsComponent1.optionType = 0;
        rsComponent1.contentType = 0;
        rsComponent1.width = 0;
        rsComponent1.height = 14;
        rsComponent1.opacity = 0;
        rsComponent1.hoverType = -1;
        rsComponent1.scriptOperators = new int[1];
        rsComponent1.scriptDefaults = new int[1];
        rsComponent1.scriptOperators[0] = 3;
        rsComponent1.scriptDefaults[0] = (itemCount - 1);
        rsComponent1.scripts = new int[1][4];
        rsComponent1.scripts[0][0] = 4;
        rsComponent1.scripts[0][1] = 3214;
        rsComponent1.scripts[0][2] = itemId;
        rsComponent1.scripts[0][3] = 0;
        rsComponent1.centerText = true;
        rsComponent1.gameFont = gameFonts[fontIndex];
        rsComponent1.rsFontIndex = fontIndex;
        rsComponent1.disabledMessage = "%1/" + itemCount;
        rsComponent1.enabledMessage = "";
        rsComponent1.textColor = 0xFF0000;
        rsComponent1.enabledColor = 0x00FF00;
        RSComponent rsComponent = rsComponent1.addInterface(itemChildId);
        rsComponent.parentId = rsComponent1.id;
        rsComponent.id = itemChildId;
        rsComponent.type = 6;
        rsComponent.height = 32;
        rsComponent.width = 32;
        rsComponent.mediaType = 4;
        rsComponent.enabledMediaType = 4;
        rsComponent.mediaID = itemId;
        rsComponent.enabledMediaID = itemId;
        ItemDefinition itemDefinition = ItemDefinition.forId(itemId);
        rsComponent.modelRotation1 = itemDefinition.modelRotationX;
        rsComponent.modelRotation2 = itemDefinition.modelRotationY;
        rsComponent.modelZoom = 1050;
        return rsComponent1;
    }

    /**
     * Draws a rune item on the interface.
     *
     * @param id         The id of the interface.
     * @param runeSprite The rune {@link com.runescape.cache.media.Sprite}.
     * @return The {@link RSComponent}.
     */
    public RSComponent drawRune(int id, Sprite runeSprite) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 52;
        rsComponent.getSpriteSet().setDisabled(runeSprite);
        rsComponent.width = 500;
        rsComponent.height = 500;
        return rsComponent;
    }

    /**
     * Draws a small rune item on the interface.
     *
     * @param id         The id of the interface.
     * @param runeSprite The rune {@link com.runescape.cache.media.Sprite}.
     * @return The {@link RSComponent}.
     */
    public RSComponent drawSmallRune(int id, Sprite runeSprite) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 52;
        rsComponent.getSpriteSet().setDisabled(runeSprite);
        rsComponent.width = 21;
        rsComponent.height = 20;
        return rsComponent;
    }

    public RSComponent addLunar2RunesSmallBox(int id, int r1, int r2, int ra1, int ra2, int rune1, int lvl, String name, String disabledMessage, GameFont[] rsFonts, SpriteSet spriteSet, int suo, int type) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = 1151;
        rsComponent.type = 5;
        rsComponent.optionType = type;
        rsComponent.contentType = 0;
        rsComponent.hoverType = id + 1;
        rsComponent.spellUsableOn = suo;
        rsComponent.selectedActionName = "Cast On";
        rsComponent.width = 20;
        rsComponent.height = 20;
        rsComponent.tooltip = "Cast <col=00FF00>" + name;
        rsComponent.spellName = name;
        rsComponent.scriptOperators = new int[3];
        rsComponent.scriptDefaults = new int[3];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = ra1;
        rsComponent.scriptOperators[1] = 3;
        rsComponent.scriptDefaults[1] = ra2;
        rsComponent.scriptOperators[2] = 3;
        rsComponent.scriptDefaults[2] = lvl;
        rsComponent.scripts = new int[3][];
        rsComponent.scripts[0] = new int[4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = r1;
        rsComponent.scripts[0][3] = 0;
        rsComponent.scripts[1] = new int[4];
        rsComponent.scripts[1][0] = 4;
        rsComponent.scripts[1][1] = 3214;
        rsComponent.scripts[1][2] = r2;
        rsComponent.scripts[1][3] = 0;
        rsComponent.scripts[2] = new int[3];
        rsComponent.scripts[2][0] = 1;
        rsComponent.scripts[2][1] = 6;
        rsComponent.scripts[2][2] = 0;
        rsComponent.gameFont = rsFonts[1];
        rsComponent.setSpriteSet(spriteSet);
        RSComponent rsComponentTab = addInterface(id + 1);
        rsComponentTab.hoverOnly = true;
        rsComponentTab.hoverType = -1;
        addLunarSprite(id + 2, ImageLoader.forName("LUNAR_BOX_1"));
        rsComponentTab.setChildBounds(id + 2, 0, 0);
        rsComponentTab.addText(id + 3, "Level " + (lvl + 1) + ": " + name, rsFonts, REGULAR, 0xFF981F, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 3, 90, 4);
        rsComponentTab.addText(id + 4, disabledMessage, rsFonts, SMALL, 0xAF6A1A, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 4, 90, 19);
        rsComponentTab.setChildBounds(30016, 37, 35);// Rune
        rsComponentTab.setChildBounds(rune1, 112, 35);// Rune
        addRuneText(id + 5, ra1 + 1, r1, rsFonts);
        rsComponentTab.setChildBounds(id + 5, 50, 66);
        addRuneText(id + 6, ra2 + 1, r2, rsFonts);
        rsComponentTab.setChildBounds(id + 6, 123, 66);
        return rsComponent;
    }

    public RSComponent addRuneText(int id, int runeAmount, int runeId, GameFont[] rsFonts) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = 1151;
        rsComponent.type = 4;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = 0;
        rsComponent.height = 14;
        rsComponent.opacity = 0;
        rsComponent.hoverType = -1;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = runeAmount;
        rsComponent.scripts = new int[1][4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = runeId;
        rsComponent.scripts[0][3] = 0;
        rsComponent.centerText = true;
        rsComponent.gameFont = rsFonts[1];
        rsComponent.textShadow = true;
        rsComponent.disabledMessage = "%1/" + runeAmount + "";
        rsComponent.enabledMessage = "";
        rsComponent.textColor = 12582912;
        rsComponent.enabledColor = 49152;
        return rsComponent;
    }

    public RSComponent addLunar3RunesSmallBox(int id, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String disabledMessage, GameFont[] rsFonts, SpriteSet spriteSet, int suo, int type) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = 1151;
        rsComponent.type = 5;
        rsComponent.optionType = type;
        rsComponent.contentType = 0;
        rsComponent.hoverType = id + 1;
        rsComponent.spellUsableOn = suo;
        rsComponent.selectedActionName = "Cast on";
        rsComponent.width = 20;
        rsComponent.height = 20;
        rsComponent.tooltip = "Cast <col=00FF00>" + name;
        rsComponent.spellName = name;
        rsComponent.scriptOperators = new int[4];
        rsComponent.scriptDefaults = new int[4];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = ra1;
        rsComponent.scriptOperators[1] = 3;
        rsComponent.scriptDefaults[1] = ra2;
        rsComponent.scriptOperators[2] = 3;
        rsComponent.scriptDefaults[2] = ra3;
        rsComponent.scriptOperators[3] = 3;
        rsComponent.scriptDefaults[3] = lvl;
        rsComponent.scripts = new int[4][];
        rsComponent.scripts[0] = new int[4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = r1;
        rsComponent.scripts[0][3] = 0;
        rsComponent.scripts[1] = new int[4];
        rsComponent.scripts[1][0] = 4;
        rsComponent.scripts[1][1] = 3214;
        rsComponent.scripts[1][2] = r2;
        rsComponent.scripts[1][3] = 0;
        rsComponent.scripts[2] = new int[4];
        rsComponent.scripts[2][0] = 4;
        rsComponent.scripts[2][1] = 3214;
        rsComponent.scripts[2][2] = r3;
        rsComponent.scripts[2][3] = 0;
        rsComponent.scripts[3] = new int[3];
        rsComponent.scripts[3][0] = 1;
        rsComponent.scripts[3][1] = 6;
        rsComponent.scripts[3][2] = 0;
        rsComponent.gameFont = rsFonts[1];
        rsComponent.setSpriteSet(spriteSet);
        RSComponent rsComponentTab = rsComponent.addInterface(id + 1);
        rsComponentTab.hoverOnly = true;
        rsComponentTab.hoverType = -1;
        addLunarSprite(id + 2, ImageLoader.forName("LUNAR_BOX_1"));
        rsComponentTab.setChildBounds(id + 2, 0, 0);
        rsComponentTab.addText(id + 3, "Level " + (lvl + 1) + ": " + name, rsFonts, REGULAR, 0xFF981F, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 3, 90, 4);
        rsComponentTab.addText(id + 4, disabledMessage, rsFonts, SMALL, 0xAF6A1A, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 4, 90, 19);
        rsComponentTab.setChildBounds(30016, 14, 35);
        rsComponentTab.setChildBounds(rune1, 74, 35);
        rsComponentTab.setChildBounds(rune2, 130, 35);
        addRuneText(id + 5, ra1 + 1, r1, rsFonts);
        rsComponentTab.setChildBounds(id + 5, 26, 66);
        addRuneText(id + 6, ra2 + 1, r2, rsFonts);
        rsComponentTab.setChildBounds(id + 6, 87, 66);
        addRuneText(id + 7, ra3 + 1, r3, rsFonts);
        rsComponentTab.setChildBounds(id + 7, 142, 66);
        return rsComponent;
    }

    public RSComponent addLunar3RunesBigBox(int id, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String disabledMessage, GameFont[] rsFonts, SpriteSet spriteSet, int suo, int type) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = 1151; // 29999
        rsComponent.type = 5;
        rsComponent.optionType = type;
        rsComponent.contentType = 0;
        rsComponent.hoverType = id + 1;
        rsComponent.spellUsableOn = suo;
        rsComponent.selectedActionName = "Cast on";
        rsComponent.width = 20;
        rsComponent.height = 20;
        rsComponent.tooltip = "Cast <col=00FF00>" + name;
        rsComponent.spellName = name;
        rsComponent.scriptOperators = new int[4];
        rsComponent.scriptDefaults = new int[4];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = ra1;
        rsComponent.scriptOperators[1] = 3;
        rsComponent.scriptDefaults[1] = ra2;
        rsComponent.scriptOperators[2] = 3;
        rsComponent.scriptDefaults[2] = ra3;
        rsComponent.scriptOperators[3] = 3;
        rsComponent.scriptDefaults[3] = lvl;
        rsComponent.scripts = new int[4][];
        rsComponent.scripts[0] = new int[4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = r1;
        rsComponent.scripts[0][3] = 0;
        rsComponent.scripts[1] = new int[4];
        rsComponent.scripts[1][0] = 4;
        rsComponent.scripts[1][1] = 3214;
        rsComponent.scripts[1][2] = r2;
        rsComponent.scripts[1][3] = 0;
        rsComponent.scripts[2] = new int[4];
        rsComponent.scripts[2][0] = 4;
        rsComponent.scripts[2][1] = 3214;
        rsComponent.scripts[2][2] = r3;
        rsComponent.scripts[2][3] = 0;
        rsComponent.scripts[3] = new int[3];
        rsComponent.scripts[3][0] = 1;
        rsComponent.scripts[3][1] = 6;
        rsComponent.scripts[3][2] = 0;
        rsComponent.gameFont = rsFonts[1];
        rsComponent.setSpriteSet(spriteSet);
        RSComponent rsComponentTab = addInterface(id + 1);
        rsComponentTab.hoverOnly = true;
        rsComponentTab.hoverType = -1;
        addLunarSprite(id + 2, ImageLoader.forName("LUNAR_BOX_2"));
        rsComponentTab.setChildBounds(id + 2, 0, 0);
        rsComponentTab.addText(id + 3, "Level " + (lvl + 1) + ": " + name, rsFonts, REGULAR, 0xFF981F, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 3, 90, 4);
        rsComponentTab.addText(id + 4, disabledMessage, rsFonts, SMALL, 0xAF6A1A, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 4, 90, 21);
        rsComponentTab.setChildBounds(30016, 14, 48);
        rsComponentTab.setChildBounds(rune1, 74, 48);
        rsComponentTab.setChildBounds(rune2, 130, 48);
        addRuneText(id + 5, ra1 + 1, r1, rsFonts);
        rsComponentTab.setChildBounds(id + 5, 26, 79);
        addRuneText(id + 6, ra2 + 1, r2, rsFonts);
        rsComponentTab.setChildBounds(id + 6, 87, 79);
        addRuneText(id + 7, ra3 + 1, r3, rsFonts);
        rsComponentTab.setChildBounds(id + 7, 142, 79);
        return rsComponent;
    }

    public RSComponent addLunar3RunesLargeBox(int id, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String disabledMessage, GameFont[] rsFonts, SpriteSet spriteSet, int suo, int type) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = 1151;
        rsComponent.type = 5;
        rsComponent.optionType = type;
        rsComponent.contentType = 0;
        rsComponent.hoverType = id + 1;
        rsComponent.spellUsableOn = suo;
        rsComponent.selectedActionName = "Cast on";
        rsComponent.width = 20;
        rsComponent.height = 20;
        rsComponent.tooltip = "Cast <col=00FF00>" + name;
        rsComponent.spellName = name;
        rsComponent.scriptOperators = new int[4];
        rsComponent.scriptDefaults = new int[4];
        rsComponent.scriptOperators[0] = 3;
        rsComponent.scriptDefaults[0] = ra1;
        rsComponent.scriptOperators[1] = 3;
        rsComponent.scriptDefaults[1] = ra2;
        rsComponent.scriptOperators[2] = 3;
        rsComponent.scriptDefaults[2] = ra3;
        rsComponent.scriptOperators[3] = 3;
        rsComponent.scriptDefaults[3] = lvl;
        rsComponent.scripts = new int[4][];
        rsComponent.scripts[0] = new int[4];
        rsComponent.scripts[0][0] = 4;
        rsComponent.scripts[0][1] = 3214;
        rsComponent.scripts[0][2] = r1;
        rsComponent.scripts[0][3] = 0;
        rsComponent.scripts[1] = new int[4];
        rsComponent.scripts[1][0] = 4;
        rsComponent.scripts[1][1] = 3214;
        rsComponent.scripts[1][2] = r2;
        rsComponent.scripts[1][3] = 0;
        rsComponent.scripts[2] = new int[4];
        rsComponent.scripts[2][0] = 4;
        rsComponent.scripts[2][1] = 3214;
        rsComponent.scripts[2][2] = r3;
        rsComponent.scripts[2][3] = 0;
        rsComponent.scripts[3] = new int[3];
        rsComponent.scripts[3][0] = 1;
        rsComponent.scripts[3][1] = 6;
        rsComponent.scripts[3][2] = 0;
        rsComponent.gameFont = rsFonts[1];
        rsComponent.setSpriteSet(spriteSet);
        RSComponent rsComponentTab = addInterface(id + 1);
        rsComponentTab.hoverOnly = true;
        rsComponentTab.hoverType = -1;
        addLunarSprite(id + 2, ImageLoader.forName("LUNAR_BOX_3"));
        rsComponentTab.setChildBounds(id + 2, 0, 0);
        rsComponentTab.addText(id + 3, "Level " + (lvl + 1) + ": " + name, rsFonts, REGULAR, 0xFF981F, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 3, 90, 4);
        rsComponentTab.addText(id + 4, disabledMessage, rsFonts, SMALL, 0xAF6A1A, true, true, 0, 52);
        rsComponentTab.setChildBounds(id + 4, 90, 34);
        rsComponentTab.setChildBounds(30016, 14, 61);
        rsComponentTab.setChildBounds(rune1, 74, 61);
        rsComponentTab.setChildBounds(rune2, 130, 61);
        addRuneText(id + 5, ra1 + 1, r1, rsFonts);
        rsComponentTab.setChildBounds(id + 5, 26, 92);
        addRuneText(id + 6, ra2 + 1, r2, rsFonts);
        rsComponentTab.setChildBounds(id + 6, 87, 92);
        addRuneText(id + 7, ra3 + 1, r3, rsFonts);
        rsComponentTab.setChildBounds(id + 7, 142, 92);
        return rsComponent;
    }

    public RSComponent addLunarSprite(int i, Sprite disabledSprite) {
        RSComponent rsComponent = addInterface(i);
        rsComponent.id = i;
        rsComponent.parentId = i;
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 52;
        rsComponent.getSpriteSet().setDisabled(disabledSprite);
        rsComponent.width = 500;
        rsComponent.height = 500;
        rsComponent.tooltip = "";
        return rsComponent;
    }

    public RSComponent addSprites(int id, SpriteSet spriteSet, int configId, int configFrame, boolean interfaceConfig) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 5;
        rsComponent.optionType = 0;
        rsComponent.contentType = 0;
        rsComponent.width = 512;
        rsComponent.height = 334;
        rsComponent.opacity = (byte) 0;
        rsComponent.hoverType = -1;
        rsComponent.setSpriteSet(spriteSet);
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    public RSComponent addHoverBox(int id, String text, String text2, int configId, int configFrame, boolean interfaceConfig) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 8;
        rsComponent.enabledMessage = text;
        rsComponent.disabledMessage = text2;
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = configId;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configFrame;
        rsComponent.scripts[0][2] = 0;
        rsComponent.interfaceConfig = interfaceConfig;
        return rsComponent;
    }

    public RSComponent addHoverBox(int id, String popupString, int width, int height, boolean inventoryHover) {
        RSComponent rsComponent = addTabInterface(id);
        rsComponent.id = id;
        rsComponent.parentId = this.id;
        rsComponent.type = 8;
        rsComponent.disabledMessage = popupString;
        rsComponent.height = height;
        rsComponent.width = width;
        rsComponent.inventoryHover = inventoryHover;
        return rsComponent;
    }

    public RSComponent addToItemGroup(int id, int w, int h, int x, int y, boolean hasExamine, boolean hasItemCount, String... actions) {
        return addToItemGroup(id, w, h, x, y, hasExamine, hasItemCount, true, actions);
    }

    public RSComponent addToItemGroup(int id, int w, int h, int x, int y, boolean hoverOnly, boolean hasExamine, boolean hasItemCount, boolean showName, String... actions) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.width = w;
        rsComponent.height = h;
        rsComponent.inventory = new int[w * h];
        rsComponent.inventoryValue = new int[w * h];
        rsComponent.usableItemInterface = false;
        rsComponent.isInventoryInterface = false;
        rsComponent.hoverOnly = hoverOnly;
        rsComponent.invSpritePadX = x;
        rsComponent.invSpritePadY = y;
        rsComponent.spritesX = new int[20];
        rsComponent.spritesY = new int[20];
        rsComponent.sprites = new Sprite[20];
        rsComponent.actions = new String[5];
        System.arraycopy(actions, 0, rsComponent.actions, 0, actions.length);
        rsComponent.type = 2;
        rsComponent.hasExamine = hasExamine;
        rsComponent.hasItemCount = hasItemCount;
        rsComponent.showName = showName;
        return rsComponent;
    }

    public RSComponent addToItemGroup(int id, int w, int h, int x, int y, boolean hasExamine, boolean hasItemCount, boolean showName, String... actions) {
        return addToItemGroup(id, w, h, x, y, false, hasExamine, hasItemCount, showName, actions);
    }

    /**
     * Adds an item.
     *
     * @param id The id of the child.
     * @return The {@link RSComponent}.
     */
    public RSComponent addItem(int id, String... tooltips) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = 32;
        rsComponent.mediaType = 0;
        rsComponent.mediaID = 0;
        rsComponent.enabledMediaType = 0;
        rsComponent.enabledMediaID = 0;
        rsComponent.type = 6;
        rsComponent.height = 32;
        rsComponent.tooltips = tooltips;
        if (tooltips != null) {
            rsComponent.optionType = 1;
        }
        return rsComponent;
    }

    /**
     * Adds an item.
     *
     * @param id The id of the child.
     * @return The {@link RSComponent}.
     */
    public RSComponent addItem(int id, int width, int height, String... tooltips) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = width;
        rsComponent.mediaType = 0;
        rsComponent.mediaID = 0;
        rsComponent.enabledMediaType = 0;
        rsComponent.enabledMediaID = 0;
        rsComponent.type = 6;
        rsComponent.height = height;
        rsComponent.tooltips = tooltips;
        if (tooltips != null) {
            rsComponent.optionType = 1;
        }
        return rsComponent;
    }

    public RSComponent addModel(int id, int width, int height) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = width;
        rsComponent.height = height;
        rsComponent.mediaType = 0;
        rsComponent.enabledMediaType = 0;
        rsComponent.mediaID = 0;
        rsComponent.enabledMediaID = 0;
        rsComponent.modelRotation2 = 224;
        rsComponent.modelRotation1 = 177;
        rsComponent.modelZoom = 55;
        rsComponent.type = 6;
        return rsComponent;
    }

    public RSComponent addModel(int id, int mediaId) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = 32;
        rsComponent.mediaType = 1;
        rsComponent.mediaID = mediaId;
        rsComponent.enabledMediaType = 1;
        rsComponent.enabledMediaID = mediaId;
        rsComponent.modelRotation2 = 128;
        rsComponent.modelRotation1 = 128;
        rsComponent.modelZoom = 300;
        rsComponent.enabledAnimationId = 4165;
        rsComponent.disabledAnimationId = 4165;
        rsComponent.type = 6;
        rsComponent.height = 32;
        return rsComponent;
    }

    public RSComponent addNPCModel(int id) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = 32;
        rsComponent.mediaType = 2;
        rsComponent.enabledMediaType = 2;
        rsComponent.modelRotation2 = 40;
        rsComponent.modelRotation1 = 1900;
        rsComponent.modelZoom = 1900;
        rsComponent.enabledAnimationId = 6550;
        rsComponent.disabledAnimationId = 6550;
        rsComponent.type = 6;
        rsComponent.height = 32;
        return rsComponent;
    }

    /**
     * Adds an item.
     *
     * @param id        The id of the child.
     * @param itemId    The id of the item.
     * @param modelZoom The model zoom of the item.
     * @return The {@link RSComponent}.
     */
    public RSComponent addItem(int id, int itemId, int modelZoom, int modelRotation1, int modelRotation2) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.width = 32;
        rsComponent.mediaType = 4;
        rsComponent.mediaID = itemId;
        rsComponent.enabledMediaType = 0;
        rsComponent.enabledMediaID = 0;
        rsComponent.modelZoom = modelZoom;
        rsComponent.modelRotation2 = modelRotation1;
        rsComponent.modelRotation1 = modelRotation2;
        rsComponent.type = 6;
        rsComponent.height = 32;
        return rsComponent;
    }

    /**
     * Adds "Click here to continue".
     *
     * @param id The id of the child.
     * @return The {@link RSComponent}.
     */
    public RSComponent addContinue(int id, GameFont rsFont) {
        RSComponent rsComponent = addInterface(id);
        rsComponent.parentId = this.id;
        rsComponent.id = id;
        rsComponent.contentType = 0;
        rsComponent.disabledMouseOverColor = 49152;
        rsComponent.optionType = 6;
        rsComponent.width = 350;
        rsComponent.tooltip = "Continue";
        rsComponent.centerText = true;
        rsComponent.filled = false;
        rsComponent.enabledMessage = "";
        rsComponent.textColor = 16776960;
        rsComponent.gameFont = rsFont;
        rsComponent.disabledMessage = "Click here to continue";
        rsComponent.type = 4;
        rsComponent.height = 17;
        rsComponent.spellUsableOn = 0;
        rsComponent.hoverType = -1;
        return rsComponent;
    }

    /**
     * Determines if the menu of items is visible.
     *
     * @return will return true if the player has triggered the drop down button.
     */
    public boolean isMenuVisible() {
        return menuVisible;
    }

    /**
     * Sets the menu to either a visible or invisible state.
     *
     * @param menuVisible true if the menu is to be visible, otherwise invisible.
     */
    public void setMenuVisible(boolean menuVisible) {
        this.menuVisible = menuVisible;
    }

    /**
     * Gets the {@link com.runescape.cache.media.SpriteSet} for this interface.
     *
     * @return The {@link com.runescape.cache.media.SpriteSet}.
     */
    public SpriteSet getSpriteSet() {
        return spriteSet;
    }

    /**
     * Sets the {@link com.runescape.cache.media.SpriteSet} for this interface.
     *
     * @param spriteSet The {@link com.runescape.cache.media.SpriteSet}.
     * @return The {@link RSComponent}.
     */
    public RSComponent setSpriteSet(SpriteSet spriteSet) {
        this.spriteSet = spriteSet == null ? new SpriteSet() : spriteSet;
        return this;
    }

    /**
     * Gets the {@link com.runescape.cache.media.InterfaceChild} {@link java.util.List} for this {@code RSComponent}.
     *
     * @return The {@link com.runescape.cache.media.InterfaceChild} {@link java.util.List}.
     */
    public List<InterfaceChild> getInterfaceChildren() {
        return interfaceChildren;
    }

    /**
     * Gets an {@link com.runescape.cache.media.InterfaceChild} by the id.
     *
     * @param id The id of the child.
     * @return The {@link com.runescape.cache.media.InterfaceChild}.
     */
    public InterfaceChild getInterfaceChild(int id) {
        for (InterfaceChild interfaceChild : interfaceChildren) {
            if (interfaceChild.getId() == id) {
                return interfaceChild;
            }
        }
        return null;
    }

}
