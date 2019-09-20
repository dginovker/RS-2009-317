package com.runescape.media;

import main.java.com.runescape.Game;
import com.runescape.GameShell;
import com.runescape.cache.media.RSComponent;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class Scrollbar {

    /**
     * Draws a scrollbar.
     *
     * @param game        The {@link Game} instance.
     * @param barHeight   The height of the bar.
     * @param scrollPos   The position of the scroller.
     * @param y           The y coordinate.
     * @param x           The x coordinate.
     * @param maxScroll   The maximum scroll.
     * @param transparent If the scrollbar is transparent.
     * @param newScroller If we should draw the new scroller.
     */
    public static void draw(Game game, int barHeight, int scrollPos, int y, int x, int maxScroll, boolean transparent, boolean newScroller) {
        if (transparent) {
            drawTransparent(game, barHeight, x, y, maxScroll, scrollPos, 32);
            return;
        }
        if (newScroller) {
            drawNew(game, barHeight, scrollPos, y, x, maxScroll);
            return;
        }
        drawBasic(game, barHeight, scrollPos, y, x, maxScroll);
    }

    public static void drawHorizontal(Game game, int barWidth, int scrollPos, int yPos, int xPos, int maxScroll) {
        int backingAmount = (barWidth - 32) / 5;
        int scrollPartWidth = ((barWidth - 32) * barWidth) / maxScroll;
        if (scrollPartWidth < 10) {
            scrollPartWidth = 10;
        }
        int scrollPartAmount = (scrollPartWidth / 5) - 2;
        int scrollPartPos = ((barWidth - 32 - scrollPartWidth) * scrollPos) / (maxScroll - barWidth) + 16 + xPos;
        for (int i = 0, xxPos = xPos + 16; i <= backingAmount; i++, xxPos += 5) {
            game.horizontalScrollbar[1].drawSprite(xxPos, yPos);
        }
        game.horizontalScrollbar[2].drawSprite(scrollPartPos, yPos);
        scrollPartPos += 5;
        for (int i = 0; i <= scrollPartAmount; i++) {
            game.horizontalScrollbar[3].drawSprite(scrollPartPos, yPos);
            scrollPartPos += 5;
        }
        scrollPartPos = ((barWidth - 32 - scrollPartWidth) * scrollPos)
                / (maxScroll - barWidth) + 16 + xPos
                + (scrollPartWidth - 5);
        game.horizontalScrollbar[0].drawSprite(scrollPartPos, yPos);
        game.scrollbarLeft.drawSprite(xPos, yPos);
        game.scrollbarRight.drawSprite((xPos + barWidth) - 16, yPos);
    }

    /**
     * Draws a basic scrollbar.
     *
     * @param game      The {@link Game} instance.
     * @param barHeight The height of the bar.
     * @param scrollPos The position of the scroller.
     * @param y         The y coordinate.
     * @param x         The x coordinate.
     * @param maxScroll The maximum scroll.
     */
    public static void drawBasic(Game game, int barHeight, int scrollPos, int y, int x, int maxScroll) {
        game.scrollbar1.drawSprite(x, y);
        game.scrollbar2.drawSprite(x, (y + barHeight) - 16);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x000001, 16);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x3d3426, 15);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x342d21, 13);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x2e281d, 11);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x29241b, 10);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x252019, 9);
        Raster.drawPixels(barHeight - 32, y + 16, x, 0x000001, 1);
        int k1 = ((barHeight - 32) * barHeight) / maxScroll;
        if (k1 < 8) {
            k1 = 8;
        }
        int l1 = ((barHeight - 32 - k1) * scrollPos) / (maxScroll - barHeight);
        Raster.drawPixels(k1, y + 16 + l1, x, 0x4D4233, 16);
        Raster.method341(y + 16 + l1, 0x000001, k1, x);
        Raster.method341(y + 16 + l1, 0x817051, k1, x + 1);
        Raster.method341(y + 16 + l1, 0x73654a, k1, x + 2);
        Raster.method341(y + 16 + l1, 0x6a5c43, k1, x + 3);
        Raster.method341(y + 16 + l1, 0x6a5c43, k1, x + 4);
        Raster.method341(y + 16 + l1, 0x655841, k1, x + 5);
        Raster.method341(y + 16 + l1, 0x655841, k1, x + 6);
        Raster.method341(y + 16 + l1, 0x61553e, k1, x + 7);
        Raster.method341(y + 16 + l1, 0x61553e, k1, x + 8);
        Raster.method341(y + 16 + l1, 0x5d513c, k1, x + 9);
        Raster.method341(y + 16 + l1, 0x5d513c, k1, x + 10);
        Raster.method341(y + 16 + l1, 0x594e3a, k1, x + 11);
        Raster.method341(y + 16 + l1, 0x594e3a, k1, x + 12);
        Raster.method341(y + 16 + l1, 0x514635, k1, x + 13);
        Raster.method341(y + 16 + l1, 0x4b4131, k1, x + 14);
        Raster.drawLinePixels(y + 16 + l1, 0x000001, 15, x);
        Raster.drawLinePixels(y + 17 + l1, 0x000001, 15, x);
        Raster.drawLinePixels(y + 17 + l1, 0x655841, 14, x);
        Raster.drawLinePixels(y + 17 + l1, 0x6a5c43, 13, x);
        Raster.drawLinePixels(y + 17 + l1, 0x6d5f48, 11, x);
        Raster.drawLinePixels(y + 17 + l1, 0x73654a, 10, x);
        Raster.drawLinePixels(y + 17 + l1, 0x76684b, 7, x);
        Raster.drawLinePixels(y + 17 + l1, 0x7b6a4d, 5, x);
        Raster.drawLinePixels(y + 17 + l1, 0x7e6e50, 4, x);
        Raster.drawLinePixels(y + 17 + l1, 0x817051, 3, x);
        Raster.drawLinePixels(y + 17 + l1, 0x000001, 2, x);
        Raster.drawLinePixels(y + 18 + l1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 18 + l1, 0x564b38, 15, x);
        Raster.drawLinePixels(y + 18 + l1, 0x5d513c, 14, x);
        Raster.drawLinePixels(y + 18 + l1, 0x625640, 11, x);
        Raster.drawLinePixels(y + 18 + l1, 0x655841, 10, x);
        Raster.drawLinePixels(y + 18 + l1, 0x6a5c43, 7, x);
        Raster.drawLinePixels(y + 18 + l1, 0x6e6046, 5, x);
        Raster.drawLinePixels(y + 18 + l1, 0x716247, 4, x);
        Raster.drawLinePixels(y + 18 + l1, 0x7b6a4d, 3, x);
        Raster.drawLinePixels(y + 18 + l1, 0x817051, 2, x);
        Raster.drawLinePixels(y + 18 + l1, 0x000001, 1, x);
        Raster.drawLinePixels(y + 19 + l1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 19 + l1, 0x514635, 15, x);
        Raster.drawLinePixels(y + 19 + l1, 0x564b38, 14, x);
        Raster.drawLinePixels(y + 19 + l1, 0x5d513c, 11, x);
        Raster.drawLinePixels(y + 19 + l1, 0x61553e, 9, x);
        Raster.drawLinePixels(y + 19 + l1, 0x655841, 7, x);
        Raster.drawLinePixels(y + 19 + l1, 0x6a5c43, 5, x);
        Raster.drawLinePixels(y + 19 + l1, 0x6e6046, 4, x);
        Raster.drawLinePixels(y + 19 + l1, 0x73654a, 3, x);
        Raster.drawLinePixels(y + 19 + l1, 0x817051, 2, x);
        Raster.drawLinePixels(y + 19 + l1, 0x000001, 1, x);
        Raster.drawLinePixels(y + 20 + l1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 20 + l1, 0x4b4131, 15, x);
        Raster.drawLinePixels(y + 20 + l1, 0x544936, 14, x);
        Raster.drawLinePixels(y + 20 + l1, 0x594e3a, 13, x);
        Raster.drawLinePixels(y + 20 + l1, 0x5d513c, 10, x);
        Raster.drawLinePixels(y + 20 + l1, 0x61553e, 8, x);
        Raster.drawLinePixels(y + 20 + l1, 0x655841, 6, x);
        Raster.drawLinePixels(y + 20 + l1, 0x6a5c43, 4, x);
        Raster.drawLinePixels(y + 20 + l1, 0x73654a, 3, x);
        Raster.drawLinePixels(y + 20 + l1, 0x817051, 2, x);
        Raster.drawLinePixels(y + 20 + l1, 0x000001, 1, x);
        Raster.method341(y + 16 + l1, 0x000001, k1, x + 15);
        Raster.drawLinePixels(y + 15 + l1 + k1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x000001, 15, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x3f372a, 14, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x443c2d, 10, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x483e2f, 9, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x4a402f, 7, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x4b4131, 4, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x564b38, 3, x);
        Raster.drawLinePixels(y + 14 + l1 + k1, 0x000001, 2, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x443c2d, 15, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x4b4131, 11, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x514635, 9, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x544936, 7, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x564b38, 6, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x594e3a, 4, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x625640, 3, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x6a5c43, 2, x);
        Raster.drawLinePixels(y + 13 + l1 + k1, 0x000001, 1, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x443c2d, 15, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x4b4131, 14, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x544936, 12, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x564b38, 11, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x594e3a, 10, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x5d513c, 7, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x61553e, 4, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x6e6046, 3, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x7b6a4d, 2, x);
        Raster.drawLinePixels(y + 12 + l1 + k1, 0x000001, 1, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x000001, 16, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x4b4131, 15, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x514635, 14, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x564b38, 13, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x594e3a, 11, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x5d513c, 9, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x61553e, 7, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x655841, 5, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x6a5c43, 4, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x73654a, 3, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x7b6a4d, 2, x);
        Raster.drawLinePixels(y + 11 + l1 + k1, 0x000001, 1, x);
    }

    /**
     * Draws a new-aged scrollbar.
     *
     * @param game      The {@link Game} instance.
     * @param barHeight The height of the bar.
     * @param scrollPos The position of the scroller.
     * @param y         The y coordinate.
     * @param x         The x coordinate.
     * @param maxScroll The maximum scroll.
     */
    public static void drawNew(Game game, int barHeight, int scrollPos, int y, int x, int maxScroll) {
        game.newScrollbar[0].drawSprite(x, y);
        game.newScrollbar[1].drawSprite(x, (y + barHeight) - 16);

        int backingAmount = (barHeight - 32) / 5;
        int scrollPartHeight = ((barHeight - 32) * barHeight) / maxScroll;
        if (scrollPartHeight < 10) {
            scrollPartHeight = 10;
        }
        int scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos) / (maxScroll - barHeight) + 16 + y;
        int scrollPartAmount = (scrollPartHeight / 5) - 2;
        for (int drawIndex = 0, yOffset = y + 16; drawIndex <= backingAmount; drawIndex++, yOffset += 5) {
            game.newScrollbar[5].drawSprite(x, yOffset);
        }
        game.newScrollbar[2].drawSprite(x + 1, scrollPartPos);
        scrollPartPos += 5;
        for (int i = 0; i <= scrollPartAmount; i++) {
            game.newScrollbar[3].drawSprite(x + 1, scrollPartPos);
            scrollPartPos += 5;
        }
        scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos) / (maxScroll - barHeight) + 16 + y + (scrollPartHeight - 5);
        game.newScrollbar[4].drawSprite(x + 1, scrollPartPos);
    }

    /**
     * Draws a transparent scrollbar.
     *
     * @param game      The {@link Game} instance.
     * @param barHeight The height of the bar.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param maxScroll The maximum scroll.
     * @param alpha
     */
    public static void drawTransparent(Game game, int barHeight, int x, int y, int maxScroll, int pos, int alpha) {
        game.scrollbar3.drawARGBSprite(x, y, 120);
        game.scrollbar4.drawARGBSprite(x, y + barHeight - 16, 120);
        Raster.drawVerticalLine(x, y + 16, barHeight - 32, 0xFFFFFF, 64);
        Raster.drawVerticalLine(x + 15, y + 16, barHeight - 32, 0xFFFFFF, 64);
        int scrollPartHeight = (barHeight - 32) * barHeight / maxScroll;
        if (scrollPartHeight < 10) {
            scrollPartHeight = 10;
        }
        int barPosition = 0;
        if (maxScroll != barHeight) {
            barPosition = (barHeight - 32 - scrollPartHeight) * pos / (maxScroll - barHeight);
        }
        Raster.drawRectangle(x, y + 16 + barPosition, 16, 5 + y + 16 + barPosition + scrollPartHeight - 5 - (y + 16 + barPosition), 0xFFFFFF, alpha);
    }

    /**
     * Draws a console transparent scrollbar.
     *
     * @param game The {@link Game} instance.
     * @param x    The x coordinate.
     * @param y    The y coordinate.
     */
    public static void drawConsole(Game game, int x, int y, int barHeight, int offset, int maxScroll) {
        GameShell.getConsole().setScrollOffset(offset * maxScroll + (maxScroll - 2));
        if (GameShell.getConsole().getScrollOffset() < barHeight - 3) {
            GameShell.getConsole().setScrollOffset(barHeight - 3);
        }
        game.scrollbarComponent.scrollPosition = GameShell.getConsole().getScrollOffset() - GameShell.getConsole().getScrollPosition() - (barHeight - 4);
        if (GameShell.getConsole().getScrollOffset() > barHeight) {
            repositionScrollbar(game, x, barHeight, game.mouseX, game.mouseY - y, game.scrollbarComponent, 0, true, GameShell.getConsole().getScrollOffset(), false);
        }
        int scrollPosition = GameShell.getConsole().getScrollOffset() - (barHeight - 4) - game.scrollbarComponent.scrollPosition;
        if (scrollPosition < 0) {
            scrollPosition = 0;
        }
        if (scrollPosition > GameShell.getConsole().getScrollOffset() - (barHeight - 4)) {
            scrollPosition = GameShell.getConsole().getScrollOffset() - (barHeight - 4);
        }
        if (GameShell.getConsole().getScrollPosition() != scrollPosition) {
            GameShell.getConsole().setScrollPosition(scrollPosition);
        }
        drawTransparent(game, barHeight, x, y,
                GameShell.getConsole().getScrollOffset(),
                GameShell.getConsole().getScrollOffset() - GameShell.getConsole().getScrollPosition() - (barHeight - 1), 80);
    }


    /**
     * Repositions the scrollbar {@link com.runescape.cache.media.RSComponent}.
     *
     * @param xOffset       The x offset.
     * @param drawHeight    The bar draw height.
     * @param currentMouseX The current mouse x.
     * @param currentMouseY The current mouse y.
     * @param rsComponent   The scrollbar {@link com.runescape.cache.media.RSComponent}.
     * @param drawWidth     The bar draw width.
     * @param redraw        If we should redraw tabs.
     * @param scrollMax     The maximum scroll length.
     * @param reverse       If the scroller position should be reversed.
     */
    public static void repositionScrollbar(Game game, int xOffset, int drawHeight, int currentMouseX, int currentMouseY, RSComponent rsComponent, int drawWidth, boolean redraw, int scrollMax, boolean reverse) {
        int xCurrentOffset;
        if (game.aBoolean972) {
            xCurrentOffset = 32;
        } else {
            xCurrentOffset = 0;
        }
        game.aBoolean972 = false;
        if (currentMouseX >= xOffset && currentMouseX < xOffset + 16 && currentMouseY >= drawWidth && currentMouseY < drawWidth + 16) {
            if (reverse) {
                rsComponent.scrollPosition += game.dragPosition * 4;
            } else {
                rsComponent.scrollPosition -= game.dragPosition * 4;
            }
            if (redraw) {
                Game.needDrawTabArea = true;
            }
        } else if (currentMouseX >= xOffset && currentMouseX < xOffset + 16 && currentMouseY >= (drawWidth + drawHeight) - 16 && currentMouseY < drawWidth + drawHeight) {
            if (reverse) {
                rsComponent.scrollPosition -= game.dragPosition * 4;
            } else {
                rsComponent.scrollPosition += game.dragPosition * 4;
            }
            if (redraw) {
                Game.needDrawTabArea = true;
            }
        } else if (currentMouseX >= xOffset - xCurrentOffset && currentMouseX < xOffset + 16 + xCurrentOffset && currentMouseY >= drawWidth + 16
                && currentMouseY < (drawWidth + drawHeight) - 16 && game.dragPosition > 0) {
            int dragLength = ((drawHeight - 32) * drawHeight) / scrollMax;
            if (dragLength < 8) {
                dragLength = 8;
            }
            int dragY = currentMouseY - drawWidth - 16 - dragLength / 2;
            int draggedLength = drawHeight - 32 - dragLength;
            if (draggedLength != 0) {
                rsComponent.scrollPosition = (((scrollMax - drawHeight) * dragY) / draggedLength);
            }
            if (redraw) {
                Game.needDrawTabArea = true;
            }
            game.aBoolean972 = true;
        }
    }

    /**
     * @param game
     * @param xOffset
     * @param drawWidth
     * @param currentMouseX
     * @param currentMouseY
     * @param rsComponent
     * @param drawHeight
     * @param redraw
     * @param scrollMax     TODO Drag bar
     */
    public static void repositionHorizontalScrollBar(Game game, int xOffset, int drawWidth, int currentMouseX, int currentMouseY, RSComponent rsComponent, int drawHeight, boolean redraw, int scrollMax) {
        drawWidth = drawWidth - 23;
        if (currentMouseX >= xOffset && currentMouseX < xOffset + 16 &&
                currentMouseY >= drawHeight + rsComponent.horizontalScrollHeight - 16 &&
                currentMouseY < drawHeight + rsComponent.horizontalScrollHeight) {
            rsComponent.horizontalScrollPosition -= game.dragPosition << 2;
            if (redraw) {
                Game.needDrawTabArea = true;
            }
        } else if (currentMouseX >= xOffset + drawWidth - 16 && currentMouseX <= xOffset + drawWidth && currentMouseY >= drawHeight +
                rsComponent.horizontalScrollHeight - 16 && currentMouseY <= drawHeight + rsComponent.horizontalScrollHeight) {
            if (rsComponent.horizontalScrollPosition < rsComponent.horizontalScrollMax && game.dragPosition > 0) {
                rsComponent.horizontalScrollPosition += game.dragPosition << 2;
            }
            if (redraw) {
                Game.needDrawTabArea = true;
            }
        }
    }
}
