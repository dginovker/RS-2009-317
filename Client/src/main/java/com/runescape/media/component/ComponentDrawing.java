package com.runescape.media.component;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.cache.def.IdentityKit;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.Animation;
import com.runescape.media.Raster;
import com.runescape.media.Scrollbar;
import com.runescape.media.component.impl.*;
import com.runescape.media.renderable.Model;
import com.runescape.util.GameConstants;
import com.runescape.util.StringUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles drawing an {@link com.runescape.cache.media.RSComponent}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ComponentDrawing {
    /**
     * The {@link com.runescape.Game} instance.
     */
    private final Game game;
    /**
     * The {@link java.util.Map} of {@link com.runescape.media.component.ComponentType} classes.
     */
    private final Map<Integer, ComponentType> COMPONENT_MAP;
    /**
     * Represents clan ranks.
     */
    private final String[] CLAN_RANKS = new String[]{"General", "Captain", "Lieutenant", "Sergeant", "Corporal", "Recruit", "Not in clan"};


    /**
     * Constructs the <code>ComponentDrawing</code> class.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public ComponentDrawing(Game game) {
        this.game = game;
        COMPONENT_MAP = new HashMap<>();
        COMPONENT_MAP.put(0, new ScrollbarComponent(game));
        COMPONENT_MAP.put(2, new ItemComponent(game));
        COMPONENT_MAP.put(3, new DrawnComponent(game));
        COMPONENT_MAP.put(4, new StringComponent(game));
        COMPONENT_MAP.put(5, new SpriteComponent(game));
        COMPONENT_MAP.put(6, new ModelComponent(game));
        COMPONENT_MAP.put(7, new ItemStackComponent(game));
        COMPONENT_MAP.put(8, new TooltipComponent(game));
        COMPONENT_MAP.put(9, new ContainerComponent(game));
    }

    /**
     * Draws an {@link com.runescape.cache.media.RSComponent} on the game client.
     *
     * @param rsComponent              The {@link com.runescape.cache.media.RSComponent} to draw.
     * @param interfaceX               The x coordinate to draw.
     * @param interfaceY               The y coordinate to draw.
     * @param scrollPosition           The current scroll position.
     * @param horizontalScrollPosition The current horizontal scroll position.
     */
    public void drawComponent(RSComponent rsComponent, int interfaceX, int interfaceY, int scrollPosition, int horizontalScrollPosition) {
        if (rsComponent == null) {
            return;
        }
        if (rsComponent.id == 32320) {
            updateClanChatSetup();
        }
        if (rsComponent.type != 0 || rsComponent.getInterfaceChildren().size() == 0) {
            return;
        }
        if (rsComponent.hoverOnly && game.getAnInt1026() != rsComponent.id && game.getAnInt1048() != rsComponent.id && game.getAnInt1039() != rsComponent.id) {
            return;
        }
        int rasterTopX = Raster.topX;
        int rasterTopY = Raster.topY;
        int rasterBottomX = Raster.bottomX;
        int rasterBottomY = Raster.bottomY;
        Raster.setDrawingArea(interfaceY + rsComponent.height, interfaceX, interfaceX + rsComponent.width, interfaceY);
        int componentChildren = rsComponent.getInterfaceChildren().size();
        for (int componentChildId = 0; componentChildId < componentChildren; componentChildId++) {
            int childX = rsComponent.getInterfaceChildren().get(componentChildId).getX() + interfaceX - horizontalScrollPosition;
            int childY = (rsComponent.getInterfaceChildren().get(componentChildId).getY() + interfaceY) - scrollPosition;
            RSComponent rsComponent_1 = RSComponent.getComponentCache()[rsComponent.getInterfaceChildren().get(componentChildId).getId()];
            childX += rsComponent_1.xOffset;
            childY += rsComponent_1.yOffset;
            if (rsComponent_1.contentType > 0) {
                drawAutoContent(rsComponent_1);
            }
            if (rsComponent_1.type == 0) {
                COMPONENT_MAP.get(0).draw(rsComponent, rsComponent_1, interfaceX, interfaceY, childX, childY);
            } else if (rsComponent_1.type != 1) {
//                if (rsComponent_1.flashes && rsComponent_1.timers != null) {
//                    for (int index = 0; index < rsComponent_1.timers.length; index++) {
//                        if (rsComponent_1.timers[index][0] == 0 || rsComponent_1.timers[index][1] == 0) {
//                            continue;
//                        }
//                        if (rsComponent_1.alphaValues[index][0] <= 1) {
//                            rsComponent_1.alphaForwards[index] = true;
//                        }
//                        if (rsComponent_1.alphaValues[index][0] >= 255) {
//                            rsComponent_1.alphaForwards[index] = false;
//                        }
//                        if ((Game.loopCycle % rsComponent_1.timers[index][0]) < rsComponent_1.timers[index][1]) {
//                            if (rsComponent_1.alphaForwards[index]) {
//                                rsComponent_1.alphaValues[index][0] += rsComponent_1.alphaValues[index][1];
//                            } else {
//                                rsComponent_1.alphaValues[index][0] -= rsComponent_1.alphaValues[index][1];
//                            }
//                        }
//                    }
//                }
                if (rsComponent_1.id == 5382 && Constants.BANK_TABS) {
                    COMPONENT_MAP.get(9).draw(rsComponent, rsComponent_1, interfaceX, interfaceY, childX, childY);
                }
                COMPONENT_MAP.get(rsComponent_1.type).draw(rsComponent, rsComponent_1, interfaceX, interfaceY, childX, childY);
            }
        }
        Raster.setDrawingArea(rasterBottomY, rasterTopX, rasterBottomX, rasterTopY);
    }

    /**
     * Builds the interface context menus on the game client.
     *
     * @param rsComponent            The {@link com.runescape.cache.media.RSComponent} to draw.
     * @param interfaceX             The x coordinate to draw.
     * @param interfaceY             The y coordinate to draw.
     * @param mouseX                 The clicked x coordinate.
     * @param mouseY                 The clicked y coordinate.
     * @param scrollOffset           The scroll offset.
     * @param horizontalScrollOffset The horizontal scroll offset.
     */
    public void buildInterfaceMenu(RSComponent rsComponent, int interfaceX, int interfaceY, int mouseX, int mouseY, int scrollOffset, int horizontalScrollOffset) {
        if (rsComponent == null) {
            rsComponent = RSComponent.getComponentCache()[21356];
        }
        if (rsComponent.type != 0 || rsComponent.getInterfaceChildren().size() == 0 || rsComponent.hoverOnly) {
            return;
        }
        if (mouseX < interfaceX || mouseY < interfaceY || mouseX > interfaceX + rsComponent.width || mouseY > interfaceY + rsComponent.height) {
            return;
        }
        int childIndex = rsComponent.getInterfaceChildren().size();
        for (int index = 0; index < childIndex; index++) {
            int currentChildX = rsComponent.getInterfaceChildren().get(index).getX() + interfaceX - horizontalScrollOffset;
            int currentChildY = (rsComponent.getInterfaceChildren().get(index).getY() + interfaceY) - scrollOffset;
            RSComponent rsComponent1 = rsComponent.getInterfaceChildren().get(index).getRSComponent();
            // TODO 317 May cause problems
            if (rsComponent1.hoverOnly) {
                continue;
            }
            if (rsComponent1.hidden) {
                continue;
            }
            if (rsComponent1.tooltip != null) {
                rsComponent1.tooltip = rsComponent1.tooltip + (Constants.ENABLE_IDS ? (" <col=800000> " + rsComponent1.id) : "");
            }
            currentChildX += rsComponent1.xOffset;
            currentChildY += rsComponent1.yOffset;
            if ((rsComponent1.hoverType >= 0 || rsComponent1.disabledMouseOverColor != 0) &&
                    mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width &&
                    mouseY < currentChildY + rsComponent1.height) {
                if (rsComponent1.hoverType >= 0) {
                    game.anInt886 = rsComponent1.hoverType;
                } else {
                    game.anInt886 = rsComponent1.id;
                }
            }
            if (rsComponent1.type == 8 && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                game.anInt1315 = rsComponent1.id;
            }
            if (rsComponent1.type == 0) {
                buildInterfaceMenu(rsComponent1, currentChildX, currentChildY, mouseX, mouseY, rsComponent1.scrollPosition, rsComponent1.horizontalScrollPosition);
                if (rsComponent1.horizontalScrollMax > rsComponent1.width) {
                    Scrollbar.repositionHorizontalScrollBar(game, currentChildX, currentChildX + rsComponent1.width, mouseX, mouseY, rsComponent1, currentChildY, true, rsComponent1.horizontalScrollMax);
                }
                if (rsComponent1.scrollMax > rsComponent1.height) {
                    Scrollbar.repositionScrollbar(game, currentChildX + rsComponent1.width, rsComponent1.height, mouseX, mouseY, rsComponent1, currentChildY, true, rsComponent1.scrollMax, false);
                }
            } else {
                if (mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    if (game.interfaceIsSelected(rsComponent1) && rsComponent1.enabledPopupString != null) {
                        game.drawYellowTooltip(rsComponent, rsComponent1, 0, 0, currentChildX - 5, currentChildY - 5);
                    } else if (rsComponent1.popupString != null) {
                        game.drawYellowTooltip(rsComponent, rsComponent1, 0, 0, currentChildX - 5, currentChildY - 5);
                    }
                }
                if (rsComponent1.optionType == 1 && mouseX >= currentChildX && mouseY >= currentChildY &&
                        mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    boolean flag = false;
                    if (rsComponent1.contentType != 0) {
                        flag = game.buildFriendsListMenu(rsComponent1);
                    }
                    if (!flag) {
                        if (rsComponent1.tooltips != null) {
                            for (int optionIndex = 0; optionIndex < rsComponent1.tooltips.length; optionIndex++) {
                                if (rsComponent1.tooltips[optionIndex] == null) {
                                    continue;
                                }
                                boolean clanChat = (rsComponent1.id >= 32326 && rsComponent1.id <= 32336);
                                game.menuActionName[game.menuActionRow] = rsComponent1.tooltips[optionIndex];
                                // TODO Add more options
                                game.menuActionId[game.menuActionRow] = clanChat ? 1750 : 1700;
                                if (optionIndex == 0) {
                                    game.menuActionId[game.menuActionRow] = clanChat ? 1750 : 63;
                                }
                                if (optionIndex == 1) {
                                    game.menuActionId[game.menuActionRow] = clanChat ? 1750 : 153;
                                }
                                if (optionIndex >= 2) {
                                    game.menuActionId[game.menuActionRow] = clanChat ? 1750 : rsComponent1.tooltips[optionIndex].startsWith("Examine") ? 1125 : 1700;
                                }
                                game.secondMenuAction[game.menuActionRow] = clanChat ? (rsComponent1.id - 32326) : rsComponent1.id;
                                game.menuActionRow++;
                            }
                        } else if (rsComponent1.tooltip != null) {
                            game.menuActionName[game.menuActionRow] = rsComponent1.tooltip;
                            game.menuActionId[game.menuActionRow] = 315;
                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                            game.menuActionRow++;
                        }
                    }
                }
                if (rsComponent1.optionType == 2 && game.spellSelected == 0 && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    String s = rsComponent1.selectedActionName;
                    if (s != null && s.contains(" ")) {
                        s = s.substring(0, s.indexOf(" "));
                    }
                    game.menuActionName[game.menuActionRow] = s + " <col=00FF00>" + rsComponent1.spellName;
                    game.menuActionId[game.menuActionRow] = 626;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.optionType == 3 && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    game.menuActionName[game.menuActionRow] = "Close";
                    game.menuActionId[game.menuActionRow] = GameConstants.ACTION_CLOSE_INTERFACE;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.optionType == 4 && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    game.menuActionName[game.menuActionRow] = rsComponent1.tooltip;
                    game.menuActionId[game.menuActionRow] = 169;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.optionType == 5 && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    game.menuActionName[game.menuActionRow] = rsComponent1.tooltip;
                    game.menuActionId[game.menuActionRow] = 646;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.optionType == 6 && !game.continuedDialogue && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    game.menuActionName[game.menuActionRow] = rsComponent1.tooltip;
                    game.menuActionId[game.menuActionRow] = 679;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.optionType == 8 && !game.continuedDialogue && mouseX >= currentChildX && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width && mouseY < currentChildY + rsComponent1.height) {
                    if (rsComponent1.tooltips != null) {
                        for (int optionIndex = 0; optionIndex < rsComponent1.tooltips.length; optionIndex++) {
                            if (rsComponent1.tooltips[optionIndex] == null) {
                                continue;
                            }
                            boolean clanChatFriend = (rsComponent1.id >= 32348 && rsComponent1.id <= 32748);
                            game.menuActionName[game.menuActionRow] = rsComponent1.tooltips[optionIndex];
                            game.menuActionId[game.menuActionRow] = clanChatFriend ? 1800 : 1700;
                            if (optionIndex == 0) {
                                game.menuActionId[game.menuActionRow] = 632;
                            }
                            if (optionIndex == 1) {
                                game.menuActionId[game.menuActionRow] = 78;
                            }
                            if (optionIndex == 2) {
                                game.menuActionId[game.menuActionRow] = 867;
                            }
                            if (optionIndex == 3) {
                                game.menuActionId[game.menuActionRow] = 431;
                            }
                            if (optionIndex == 4) {
                                game.menuActionId[game.menuActionRow] = 53;
                            }
                            if (clanChatFriend) {
                                game.firstMenuAction[game.menuActionRow] = optionIndex;
                            }
                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                            game.menuActionRow++;
                        }
                    }
                }
                if (rsComponent1.optionType == 9 && !game.continuedDialogue && mouseX >= currentChildX
                        && mouseY >= currentChildY && mouseX < currentChildX + rsComponent1.width
                        && mouseY < currentChildY + rsComponent1.height) {
                    game.menuActionName[game.menuActionRow] = rsComponent1.tooltip;
                    game.menuActionId[game.menuActionRow] = 1100;
                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                    game.menuActionRow++;
                }
                if (rsComponent1.type == 2) {
                    int selectedIndex = 0;
                    for (int componentHeight = 0; componentHeight < rsComponent1.height; componentHeight++) {
                        for (int componentWidth = 0; componentWidth < rsComponent1.width; componentWidth++) {
                            int spritePadX = currentChildX + componentWidth * (32 + rsComponent1.invSpritePadX);
                            int spritePadY = currentChildY + componentHeight * (32 + rsComponent1.invSpritePadY);
                            if (selectedIndex < 20) {
                                spritePadX += rsComponent1.spritesX[selectedIndex];
                                spritePadY += rsComponent1.spritesY[selectedIndex];
                            }
                            if (mouseX >= spritePadX && mouseY >= spritePadY && mouseX < spritePadX + 32 && mouseY < spritePadY + 32) {
                                game.mouseInvInterfaceIndex = selectedIndex;
                                game.lastActiveInvInterface = rsComponent1.id;
                                if (rsComponent1.inventory[selectedIndex] > 0) {
                                    ItemDefinition itemDef = ItemDefinition.forId(rsComponent1.inventory[selectedIndex] - 1);
                                    if (rsComponent1.runTooltips != null && game.getOpenInterfaceId() == game.getRunInterfaceId()) {
                                        for (int optionIndex = 4; optionIndex >= 0; optionIndex--) {
                                            String tooltip = rsComponent1.runTooltips[optionIndex];
                                            if (tooltip != null) {
                                                game.menuActionName[game.menuActionRow] = tooltip + (rsComponent1.showName ? (" <col=FF9040>" + itemDef.name) : "");
                                                if (optionIndex == 0) {
                                                    game.menuActionId[game.menuActionRow] = 632;
                                                }
                                                if (optionIndex == 1) {
                                                    game.menuActionId[game.menuActionRow] = 78;
                                                }
                                                if (optionIndex == 2) {
                                                    game.menuActionId[game.menuActionRow] = 867;
                                                }
                                                if (optionIndex == 3) {
                                                    game.menuActionId[game.menuActionRow] = 431;
                                                }
                                                if (optionIndex == 4) {
                                                    game.menuActionId[game.menuActionRow] = tooltip.equals("Examine") ? 1125 : 53;
                                                }
                                                game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                                game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                                game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                game.menuActionRow++;
                                            }
                                        }
                                    } else if (game.itemSelected == 1 && rsComponent1.isInventoryInterface) {
                                        if (rsComponent1.id != game.lastItemSelectedInterface || selectedIndex != game.lastItemSelectedSlot) {
                                            game.menuActionName[game.menuActionRow] = "Use " + game.selectedItemName + " with <col=FF9040>" + itemDef.name;
                                            game.menuActionId[game.menuActionRow] = 870;
                                            game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                            game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                    } else if (game.spellSelected == 1 && rsComponent1.isInventoryInterface) {
                                        if ((game.spellUsableOn & 0x10) == 16) {
                                            game.menuActionName[game.menuActionRow] = game.spellTooltip + " <col=FF9040>" + itemDef.name;
                                            game.menuActionId[game.menuActionRow] = 543;
                                            game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                            game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                    } else {
                                        boolean destroy = false;
                                        if (rsComponent1.isInventoryInterface) {
                                            for (int i = 4; i >= 3; i--) {
                                                if (itemDef.options != null && itemDef.options[i] != null) {
                                                    game.menuActionName[game.menuActionRow] = itemDef.options[i] + " <col=FF9040>" + itemDef.name;
                                                    if (i == 3) {
                                                        game.menuActionId[game.menuActionRow] = 493;
                                                    }
                                                    if (i == 4) {
                                                        game.menuActionId[game.menuActionRow] = 847;
                                                        destroy = "Destroy".equalsIgnoreCase(itemDef.options[4]);
                                                    }
                                                    game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                                    game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                } else if (i == 4) {
                                                    game.menuActionName[game.menuActionRow] = "Drop <col=FF9040>" + itemDef.name;
                                                    game.menuActionId[game.menuActionRow] = 847;
                                                    game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                                    game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                }
                                            }
                                        }
                                        if (rsComponent1.usableItemInterface) {
                                            game.menuActionName[game.menuActionRow] = "Use <col=FF9040>" + itemDef.name;
                                            game.menuActionId[game.menuActionRow] = 447;
                                            game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                            game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                        if (itemDef.options == null) {
                                            // This forces options with missing actions to have "Drop",
                                            // so that they may be shift-click dropped.
                                            itemDef.options = new String[] { null, null, null, null, "Drop" };
                                            // Might have unintended side effects. Haven't found any, however.
                                        }
                                        if (rsComponent1.isInventoryInterface && itemDef.options != null) {
                                            if (!destroy && !game.isMenuOpen() && Game.shiftClickDrop && game.shiftPressed) {
                                                game.menuActionName[1] = "Drop <col=FF9040>" + itemDef.name;
                                                game.menuActionId[1] = 847;
                                                game.selectedMenuActions[0] = itemDef.id;
                                                game.firstMenuAction[0] = selectedIndex;
                                                game.secondMenuAction[0] = rsComponent1.id;
                                                game.menuActionRow = 2;
                                            } else {
                                                for (int i = 2; i >= 0; i--) {
                                                    if (itemDef.options[i] != null) {
                                                        game.menuActionName[game.menuActionRow] = itemDef.options[i] + " <col=FF9040>" + itemDef.name;
                                                        if (i == 0) {
                                                            game.menuActionId[game.menuActionRow] = 74;
                                                        }
                                                        if (i == 1) {
                                                            game.menuActionId[game.menuActionRow] = 454;
                                                        }
                                                        if (i == 2) {
                                                            game.menuActionId[game.menuActionRow] = 539;
                                                        }
                                                        game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                                        game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                                        game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                        game.menuActionRow++;
                                                    }
                                                }
                                            }
                                        }
                                        if (rsComponent1.actions != null) {
                                            for (int optionIndex = 4; optionIndex >= 0; optionIndex--) {
                                                if (rsComponent1.actions[optionIndex] != null) {
                                                    if (rsComponent1.actions[optionIndex] != null && rsComponent1.actions[optionIndex].equals("Operate") &&
                                                            optionIndex == 1 && !itemDef.hasEquipmentOptions) {
                                                        continue;
                                                    }
                                                    game.menuActionName[game.menuActionRow] = rsComponent1.actions[optionIndex] + (rsComponent1.showName ? (" <col=FF9040>" + itemDef.name) : "");
                                                    if (optionIndex == 0) {
                                                        game.menuActionId[game.menuActionRow] = 632;
                                                    }
                                                    if (optionIndex == 1) {
                                                        game.menuActionId[game.menuActionRow] = 78;
                                                    }
                                                    if (optionIndex == 2) {
                                                        game.menuActionId[game.menuActionRow] = 867;
                                                    }
                                                    if (optionIndex == 3) {
                                                        game.menuActionId[game.menuActionRow] = 431;
                                                    }
                                                    if (optionIndex == 4) {
                                                        game.menuActionId[game.menuActionRow] = 53;
                                                    }
                                                    game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                                    game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                }
                                            }
                                        }
                                        // TODO 317
                                        if (!rsComponent1.hasExamine) {
                                            continue;
                                        }
                                        game.menuActionName[game.menuActionRow] = "Examine <col=FF9040>" + itemDef.name;
                                        game.menuActionId[game.menuActionRow] = 1125;
                                        game.selectedMenuActions[game.menuActionRow] = itemDef.id;
                                        game.firstMenuAction[game.menuActionRow] = selectedIndex;
                                        game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                        game.menuActionRow++;
                                    }
                                }
                            }
                            selectedIndex++;
                        }
                    }
                } else if (rsComponent1.type == 9) {
                    int k2 = 0;
                    int tabAm = game.tabAmounts[0];
                    int tabSlot = 0;
                    int hh = 0;

                    int newSlot = 0;
                    if (rsComponent1.contentType == 206 && InterfaceConfiguration.CURRENT_BANK_TAB.get() != 0 && game.settings[1012] == 0) {
                        for (int tab = 0; tab < game.tabAmounts.length; tab++) {
                            if (tab == InterfaceConfiguration.CURRENT_BANK_TAB.get()) {
                                break;
                            }
                            newSlot += game.tabAmounts[tab];
                        }
                        k2 = newSlot;
                    }

                    heightLoop:
                    for (int l2 = 0; l2 < rsComponent1.height; l2++) {
                        for (int i3 = 0; i3 < rsComponent1.width; i3++) {
                            int j3 = currentChildX + i3 * (32 + rsComponent1.invSpritePadX);
                            int k3 = currentChildY + l2 * (32 + rsComponent1.invSpritePadY) + hh;
                            if (rsComponent1.contentType == 206 && game.settings[1012] == 0) {
                                if (InterfaceConfiguration.CURRENT_BANK_TAB.get() == 0) {
                                    if (k2 >= tabAm) {
                                        if (tabSlot + 1 < game.tabAmounts.length) {
                                            tabAm += game.tabAmounts[++tabSlot];
                                            if (tabSlot > 0 && game.tabAmounts[tabSlot - 1] % 8 == 0) {
                                                l2--;
                                            }
                                            hh += 8;
                                        }
                                        break;
                                    }
                                } else if (InterfaceConfiguration.CURRENT_BANK_TAB.get() <= 9) {
                                    if (k2 >= game.tabAmounts[InterfaceConfiguration.CURRENT_BANK_TAB.get()] + newSlot) {
                                        break heightLoop;
                                    }
                                }
                            }
                            if (k2 < 20) {
                                j3 += rsComponent1.spritesX[k2];
                                k3 += rsComponent1.spritesY[k2];
                            }
                            if (game.mouseX >= j3 && game.mouseY >= k3 && game.mouseX < j3 + 32 && game.mouseY < k3 + 32) {
                                game.mouseInvInterfaceIndex = k2;
                                game.lastActiveInvInterface = rsComponent1.id;

                                int itemId = rsComponent1.inventory[k2] - 1;
                                if (game.settings[1012] == 1 && rsComponent1.contentType == 206) {
                                    itemId = game.bankInvTemp[k2] - 1;
                                }
                                if (itemId + 1 > 0) {
                                    ItemDefinition itemDefinition = ItemDefinition.forId(itemId);
                                    if (game.itemSelected == 1 && rsComponent1.isInventoryInterface) {
                                        if (rsComponent1.id != game.lastItemSelectedInterface || k2 != game.lastItemSelectedSlot) {
                                            game.menuActionName[game.menuActionRow] = "Use " + game.selectedItemName + " with <col=FF9040>" + itemDefinition.name;
                                            game.menuActionId[game.menuActionRow] = 870;
                                            game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                            game.firstMenuAction[game.menuActionRow] = k2;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                    } else if (game.spellSelected == 1 && rsComponent1.isInventoryInterface) {
                                        if ((game.spellUsableOn & 0x10) == 16) {
                                            game.menuActionName[game.menuActionRow] = game.spellTooltip + " <col=FF9040>" + itemDefinition.name;
                                            game.menuActionId[game.menuActionRow] = 543;
                                            game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                            game.firstMenuAction[game.menuActionRow] = k2;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;

                                            game.menuActionName[game.menuActionRow] = game.spellTooltip + " <col=FF9040>" + itemDefinition.name;
                                            game.menuActionId[game.menuActionRow] = 543;
                                            game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                            game.firstMenuAction[game.menuActionRow] = k2;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                    } else {
                                        if (rsComponent1.isInventoryInterface) {
                                            for (int l3 = 4; l3 >= 3; l3--) {
                                                if (itemDefinition.options != null && itemDefinition.options[l3] != null) {
                                                    game.menuActionName[game.menuActionRow] = itemDefinition.options[l3] + " <col=FF9040>" + itemDefinition.name;
                                                    if (l3 == 3) {
                                                        game.menuActionId[game.menuActionRow] = 493;
                                                    }
                                                    if (l3 == 4) {
                                                        game.menuActionId[game.menuActionRow] = 847;
                                                    }
                                                    game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                                    game.firstMenuAction[game.menuActionRow] = k2;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                } else if (l3 == 4) {
                                                    game.menuActionName[game.menuActionRow] = "Drop <col=FF9040>" + itemDefinition.name;
                                                    game.menuActionId[game.menuActionRow] = 847;
                                                    game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                                    game.firstMenuAction[game.menuActionRow] = k2;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                }
                                            }
                                        }
                                        if (rsComponent1.usableItemInterface) {
                                            game.menuActionName[game.menuActionRow] = "Use <col=FF9040>" + itemDefinition.name;
                                            game.menuActionId[game.menuActionRow] = 447;
                                            game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                            game.firstMenuAction[game.menuActionRow] = k2;
                                            game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                            game.menuActionRow++;
                                        }
                                        if (rsComponent1.isInventoryInterface && itemDefinition.options != null) {
                                            for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDefinition.options[i4] != null) {
                                                    game.menuActionName[game.menuActionRow] = itemDefinition.options[i4] + " <col=FF9040>" + itemDefinition.name;
                                                    if (i4 == 0) {
                                                        game.menuActionId[game.menuActionRow] = 74;
                                                    }
                                                    if (i4 == 1) {
                                                        game.menuActionId[game.menuActionRow] = 454;
                                                    }
                                                    if (i4 == 2) {
                                                        game.menuActionId[game.menuActionRow] = 539;
                                                    }
                                                    game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                                    game.firstMenuAction[game.menuActionRow] = k2;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                }
                                            }

                                        }
                                        if (rsComponent1.actions != null) {
                                            if (rsComponent.parentId == 5292) {
                                                int xValue = InterfaceConfiguration.BANK_LAST_X.get();
                                                rsComponent1.actions[5] = (xValue > 0 ? "Withdraw-" + xValue : null);
                                            }

                                            for (int j4 = 6; j4 >= 0; j4--) {
                                                if (j4 > rsComponent1.actions.length - 1) {
                                                    continue;
                                                }
                                                if (rsComponent1.actions[j4] != null) {
                                                    game.menuActionName[game.menuActionRow] = rsComponent1.actions[j4] + " <col=FF9040>" + itemDefinition.name;
                                                    if (j4 == 0) {
                                                        game.menuActionId[game.menuActionRow] = 632;
                                                    }
                                                    if (j4 == 1) {
                                                        game.menuActionId[game.menuActionRow] = 78;
                                                    }
                                                    if (j4 == 2) {
                                                        game.menuActionId[game.menuActionRow] = 867;
                                                    }
                                                    if (j4 == 3) {
                                                        game.menuActionId[game.menuActionRow] = 431;
                                                    }
                                                    if (j4 == 4) {
                                                        game.menuActionId[game.menuActionRow] = 53;
                                                    }
                                                    if (rsComponent.parentId == 5292) {
                                                        if (rsComponent1.actions[j4] == null) {
                                                            if (j4 == 5) {
                                                                game.menuActionId[game.menuActionRow] = 291;
                                                            }
                                                        } else {
                                                            if (j4 == 5) {
                                                                game.menuActionId[game.menuActionRow] = 300;
                                                            }
                                                            if (j4 == 6) {
                                                                game.menuActionId[game.menuActionRow] = 291;
                                                            }
                                                        }
                                                    }

                                                    game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                                    game.firstMenuAction[game.menuActionRow] = k2;
                                                    game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                                    game.menuActionRow++;
                                                }
                                            }
                                        }
                                        game.menuActionName[game.menuActionRow] = "Examine <col=FF9040>" + itemDefinition.name;
                                        game.menuActionId[game.menuActionRow] = 1125;
                                        game.selectedMenuActions[game.menuActionRow] = itemDefinition.id;
                                        game.firstMenuAction[game.menuActionRow] = k2;
                                        game.secondMenuAction[game.menuActionRow] = rsComponent1.id;
                                        game.menuActionRow++;
                                    }
                                }
                            }
                            k2++;
                        }
                    }

                }
            }
        }
    }

    /**
     * Draws auto content class types.
     *
     * @param rsComponent The {@link com.runescape.cache.media.RSComponent}.
     */
    private void drawAutoContent(RSComponent rsComponent) {
        int index = rsComponent.contentType;
        if (index >= 1 && index <= 100 || index >= 701 && index <= 800) {
            if (index == 1 && game.getFriendServerStatus() == 0) {
                rsComponent.disabledMessage = "Loading friend list";
                rsComponent.optionType = 0;
                return;
            }
            if (index == 1 && game.getFriendServerStatus() == 1) {
                rsComponent.disabledMessage = "Connecting to friendserver";
                rsComponent.optionType = 0;
                return;
            }
            if (index == 2 && game.getFriendServerStatus() != 2) {
                rsComponent.disabledMessage = "Please wait...";
                rsComponent.optionType = 0;
                return;
            }
            int k = game.friendsCount;
            if (game.getFriendServerStatus() != 2) {
                k = 0;
            }
            if (index > 700) {
                index -= 601;
            } else {
                index--;
            }
            if (index >= k) {
                rsComponent.disabledMessage = "";
                rsComponent.optionType = 0;
                return;
            } else {
                rsComponent.disabledMessage = game.getFriendsList()[index];
                rsComponent.optionType = 1;
                return;
            }
        }
        if (index >= 101 && index <= 200 || index >= 801 && index <= 900) {
            int l = game.friendsCount;
            if (game.getFriendServerStatus() != 2) {
                l = 0;
            }
            if (index > 800) {
                index -= 701;
            } else {
                index -= 101;
            }
            if (index >= l) {
                rsComponent.disabledMessage = "";
                rsComponent.optionType = 0;
                return;
            }
            if (game.friendsNodeIDs[index] == 0) {
                rsComponent.disabledMessage = "<col=FF0000>Offline";
            } else if (game.friendsNodeIDs[index] == game.worldId) {
                rsComponent.disabledMessage = "<col=00FF00>World " + (game.friendsNodeIDs[index] - 9);
            } else {
                rsComponent.disabledMessage = "<col=FFFF00>World " + (game.friendsNodeIDs[index] - 9);
            }
            rsComponent.optionType = 1;
            return;
        }

        if (index == 203) {
            int i1 = game.friendsCount;
            if (game.getFriendServerStatus() != 2) {
                i1 = 0;
            }
            rsComponent.scrollMax = i1 * 15 + 20;
            if (rsComponent.scrollMax <= rsComponent.height) {
                rsComponent.scrollMax = rsComponent.height + 1;
            }
            return;
        }
        if (index >= 401 && index <= 500) {
            if ((index -= 401) == 0 && game.getFriendServerStatus() == 0) {
                rsComponent.disabledMessage = "Loading ignore list";
                rsComponent.optionType = 0;
                return;
            }
            if (index == 1 && game.getFriendServerStatus() == 0) {
                rsComponent.disabledMessage = "Please wait...";
                rsComponent.optionType = 0;
                return;
            }
            int j1 = game.getIgnoreCount();
            if (game.getFriendServerStatus() == 0) {
                j1 = 0;
            }
            if (index >= j1) {
                rsComponent.disabledMessage = "";
                rsComponent.optionType = 0;
                return;
            } else {
                rsComponent.disabledMessage = StringUtility.formatUsername(StringUtility
                        .decodeBase37(game.getIgnores()[index]));
                rsComponent.optionType = 1;
                return;
            }
        }
        if (index == 503) {
            rsComponent.scrollMax = game.getIgnoreCount() * 15 + 20;
            if (rsComponent.scrollMax <= rsComponent.height) {
                rsComponent.scrollMax = rsComponent.height + 1;
            }
            return;
        }
        if (index == 327) {
            rsComponent.modelRotation2 = 150;
            rsComponent.modelRotation1 = (int) (Math.sin((double) Game.loopCycle / 40D) * 256D) & 0x7ff;
            if (game.aBoolean1031) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = game.anIntArray1065[k1];
                    if (l1 >= 0 && !IdentityKit.kits[l1].bodyLoaded()) {
                        return;
                    }
                }

                game.aBoolean1031 = false;
                Model playerKitModel[] = new Model[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = game.anIntArray1065[j2];
                    if (k2 >= 0) {
                        playerKitModel[i2++] = IdentityKit.kits[k2]
                                .bodyModel();
                    }
                }

                Model model = new Model(i2, playerKitModel);
                for (int l2 = 0; l2 < 5; l2++) {
                    if (game.characterDesignColours[l2] != 0) {
                        model.recolor(Game.anIntArrayArray1003[l2][0], Game.anIntArrayArray1003[l2][game.characterDesignColours[l2]]);
                        if (l2 == 1) {
                            model.recolor(Game.anIntArray1204[0], Game.anIntArray1204[game.characterDesignColours[l2]]);
                        }
                    }
                }

                model.skin();
                model.apply(Animation.animations[Game.getLocalPlayer().standAnimIndex].frames[0], Animation.animations[Game.getLocalPlayer().standAnimIndex].osrs);
                model.light(64, 850, -30, -50, -30, true);
                rsComponent.mediaType = 5;
                rsComponent.mediaID = 0;
                RSComponent.setModel(game.aBoolean994, model);
            }
            return;
        }
        if (index == 328) {
            RSComponent rsComponent1 = rsComponent;
            int verticleTilt = 150;
            int animationSpeed = (int) (Math.sin((double) Game.loopCycle / 40D) * 256D) & 0x7ff;
            rsComponent1.modelRotation2 = verticleTilt;
            rsComponent1.modelRotation1 = animationSpeed;
            if (game.aBoolean1031) {
                Model characterDisplay = Game.getLocalPlayer().prepareAnimatedModel();
                for (int l2 = 0; l2 < 5; l2++) {
                    if (game.characterDesignColours[l2] != 0) {
                        characterDisplay.recolor(Game.anIntArrayArray1003[l2][0], Game.anIntArrayArray1003[l2][game.characterDesignColours[l2]]);
                        if (l2 == 1) {
                            characterDisplay.recolor(Game.anIntArray1204[0], Game.anIntArray1204[game.characterDesignColours[l2]]);
                        }
                    }
                }
                int staticFrame = Game.getLocalPlayer().standAnimIndex;
                characterDisplay.skin();
                characterDisplay.apply(Animation.animations[staticFrame].frames[0], Animation.animations[staticFrame].osrs);
                rsComponent1.mediaType = 5;
                rsComponent1.mediaID = 0;
                RSComponent.setModel(game.aBoolean994, characterDisplay);
            }
            return;
        }
        if (index == 324) {
            if (game.playerDisabledSprite == null) {
                game.playerDisabledSprite = rsComponent.getSpriteSet().getDisabled();
                game.playerEnabledSprite = rsComponent.getSpriteSet().getEnabled();
            }

            if (game.maleCharacter) {
                rsComponent.getSpriteSet().setDisabled(game.playerEnabledSprite);
                return;
            } else {
                rsComponent.getSpriteSet().setDisabled(game.playerDisabledSprite);
                return;
            }
        }
        if (index == 325) {
            if (game.playerDisabledSprite == null) {
                game.playerDisabledSprite = rsComponent.getSpriteSet().getDisabled();
                game.playerEnabledSprite = rsComponent.getSpriteSet().getEnabled();
            }
            if (game.maleCharacter) {
                rsComponent.getSpriteSet().setDisabled(game.playerDisabledSprite);
                return;
            } else {
                rsComponent.getSpriteSet().setEnabled(game.playerEnabledSprite);
                return;
            }
        }
        if (index == 600) {
            rsComponent.disabledMessage = game.reportAbuseInput;
            if (Game.loopCycle % 20 < 10) {
                rsComponent.disabledMessage += "|";
                return;
            } else {
                rsComponent.disabledMessage += " ";
                return;
            }
        }
        if (index == 613) {
            if (game.getMyPrivilege() >= 1) {
                if (game.canMute) {
                    rsComponent.textColor = 0xff0000;
                    rsComponent.disabledMessage = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    rsComponent.textColor = 0xffffff;
                    rsComponent.disabledMessage = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                rsComponent.disabledMessage = "";
            }
        }
    }

    /**
     * Updates the clan chat setup {@link com.runescape.cache.media.RSComponent}.
     */
    public void updateClanChatSetup() {
        for (int childId = 32348; childId <= 32378; childId++) {
            RSComponent.getComponentCache()[childId].disabledMessage = "";
            childId++;
            RSComponent.getComponentCache()[childId].disabledMessage = "";
            RSComponent.getComponentCache()[childId].hidden = true;
        }
        int listIndex = 0;
        for (int friendIndex = 0; friendIndex < game.friendsCount; friendIndex++) {
            if (game.getFriendsList()[friendIndex] == null || game.getFriendsList()[friendIndex].isEmpty()) {
                continue;
            }
            RSComponent.getComponentCache()[32348 + listIndex].disabledMessage = game.getFriendsList()[friendIndex];
            listIndex++;
            RSComponent.getComponentCache()[32348 + listIndex].disabledMessage = CLAN_RANKS[game.getFriendsClanRankIds()[friendIndex]];
            RSComponent.getComponentCache()[32348 + listIndex].hidden = false;
            listIndex++;
        }
        RSComponent.getComponentCache()[32347].scrollMax = game.friendsCount < 15 ? 215 : game.friendsCount * 14;
    }
}
