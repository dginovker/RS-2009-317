package com.runescape.cache.media;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.media.Raster;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

public final class Sprite extends Raster {
    /**
     * Represents a null type.
     */
    public static final Sprite NULL_TYPE = null;
    public static int loadedSprites;
    static ArrayList<String> ok = new ArrayList<>();
    public int spriteIndex;
    public String spriteName = "";
    public int myPixels[];
    public int myWidth;
    public int myHeight;
    public int drawOffsetY;
    public int maxWidth;
    public int maxHeight;
    public byte[] spriteData;
    public byte imagePixels[];
    public int imagePalette[];
    public int drawOffsetX;

    public Sprite() {
    }

    public Sprite(int i, int j) {
        myPixels = new int[i * j];
        myWidth = maxWidth = i;
        myHeight = maxHeight = j;
        drawOffsetX = drawOffsetY = 0;
        spriteName = "";
    }

    public Sprite(byte[] spriteData) {
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(spriteData);
            ImageIcon sprite = new ImageIcon(image);
            myWidth = sprite.getIconWidth();
            myHeight = sprite.getIconHeight();
            maxWidth = myWidth;
            maxHeight = myHeight;
            drawOffsetX = 0;
            drawOffsetY = 0;
            myPixels = new int[myWidth * myHeight];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
            pixelgrabber.grabPixels();
            image = null;
            setTransparency(255, 0, 255);
            setTransparency(255, 255, 255);
            this.spriteData = spriteData;
        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public Sprite(Sprite sprite) {
        try {
            myWidth = sprite.myWidth;
            myHeight = sprite.myHeight;
            maxWidth = myWidth;
            maxHeight = myHeight;
            drawOffsetX = 0;
            drawOffsetY = 0;
            myPixels = sprite.myPixels;
        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public Sprite(Image image) {
        try {
            ImageIcon sprite = new ImageIcon(image);
            myWidth = sprite.getIconWidth();
            myHeight = sprite.getIconHeight();
            maxWidth = myWidth;
            maxHeight = myHeight;
            drawOffsetX = 0;
            drawOffsetY = 0;
            myPixels = new int[myWidth * myHeight];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
            pixelgrabber.grabPixels();
            image = null;
            setTransparency(255, 0, 255);
        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public Sprite(byte data[], Component component) {
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(data);
            MediaTracker mediatracker = new MediaTracker(component);
            mediatracker.addImage(image, 0);
            mediatracker.waitForAll();
            myWidth = image.getWidth(component);
            myHeight = image.getHeight(component);
            maxWidth = myWidth;
            maxHeight = myHeight;
            drawOffsetX = 0;
            drawOffsetY = 0;
            myPixels = new int[myWidth * myHeight];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
            pixelgrabber.grabPixels();
        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public Sprite(Sprite other, int x, int y, int width, int height) {
        myWidth = maxWidth = width;
        myHeight = maxHeight = height;
        drawOffsetX = 0;
        drawOffsetY = 0;
        myPixels = new int[myWidth * myHeight];
        for (int y1 = 0; y1 < height; y1++) {
            for (int x1 = 0; x1 < width; x1++) {
                int index = ((x + x1) + (y + y1) * other.myWidth);
                myPixels[x1 + (y1 * width)] = other.myPixels[index];
            }
        }
    }

    public Sprite(CacheArchive cacheArchive, String spriteName, int spriteIndex) {
        RSStream rsStream = new RSStream(cacheArchive.getEntry(spriteName + ".dat"));
        RSStream indexStream = new RSStream(cacheArchive.getEntry("index.dat"));
        this.spriteName = spriteName;
        this.spriteIndex = spriteIndex;
        indexStream.currentPosition = rsStream.getShort();
        maxWidth = indexStream.getShort();
        maxHeight = indexStream.getShort();
        int colourCount = indexStream.getByte();
        int[] colourMap = new int[colourCount];
        for (int k = 0; k < colourCount - 1; k++) {
            colourMap[k + 1] = indexStream.getTri();
            if (colourMap[k + 1] == 0) {
                colourMap[k + 1] = 1;
            }
        }

        for (int l = 0; l < spriteIndex; l++) {
            indexStream.currentPosition += 2;
            rsStream.currentPosition += indexStream.getShort() * indexStream.getShort();
            indexStream.currentPosition++;
        }

        drawOffsetX = indexStream.getByte();
        drawOffsetY = indexStream.getByte();
        myWidth = indexStream.getShort();
        myHeight = indexStream.getShort();
        int packType = indexStream.getByte();
        int imageDimension = myWidth * myHeight;
        myPixels = new int[imageDimension];
        if (packType == 0) {
            for (int k1 = 0; k1 < imageDimension; k1++) {
                myPixels[k1] = colourMap[rsStream.getByte()];
                if (Constants.DUMP_SPRITES) {
                    ImageUtil.dumpImage(myPixels, myWidth, myHeight, drawOffsetX, drawOffsetY, String.valueOf(spriteIndex), spriteName, false, false, false);
                }
            }
            setTransparency(255, 0, 255);
            return;
        }
        if (packType == 1) {
            for (int l1 = 0; l1 < myWidth; l1++) {
                for (int i2 = 0; i2 < myHeight; i2++) {
                    myPixels[l1 + i2 * myWidth] = colourMap[rsStream.getByte()];
                    if (Constants.DUMP_SPRITES) {
                        ImageUtil.dumpImage(myPixels, myWidth, myHeight, drawOffsetX, drawOffsetY, String.valueOf(spriteIndex), spriteName, false, false, false);
                    }
                }
            }

        }
        setTransparency(255, 0, 255);
    }

    public static Image getImageFromArray(int[] pixels, int width, int height) {
        MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
        Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.createImage(mis);
    }

    public static Sprite getCut(Sprite s, int width, int height) {
        try {
            Sprite sprite = new Sprite();
            Image image = getImageFromArray(s.myPixels, s.myWidth, s.myHeight);
            sprite.myWidth = width;
            sprite.myHeight = height;
            sprite.maxWidth = sprite.myWidth;
            sprite.maxHeight = sprite.myHeight;
            sprite.drawOffsetX = 0;
            sprite.drawOffsetY = 0;
            sprite.myPixels = new int[sprite.myWidth * sprite.myHeight];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, sprite.myWidth, sprite.myHeight, sprite.myPixels, 0, sprite.myWidth);
            pixelgrabber.grabPixels();
            image = null;
            return sprite;
        } catch (Exception _ex) {
            _ex.printStackTrace();
            return null;
        }
    }

    private static Image scaleImage(Image image, int desiredWidth, int desiredHeight) {
        return image.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
    }

    public static BufferedImage getScaledImage(Image loadingSprites, int w, int h, int origW, int origH) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(loadingSprites, 0, 0, origW, origH, null);
        g.dispose();
        return resizedImage;
    }

    public void methodCheckWidthAndHeight() {
        maxWidth /= 2;
        maxHeight /= 2;
        byte widthByHeightPixels[] = new byte[maxWidth * maxHeight];
        int pixel = 0;
        for (int j = 0; j < myHeight; j++) {
            for (int k = 0; k < myWidth; k++) {
                widthByHeightPixels[k + drawOffsetX + (j + drawOffsetY) * maxWidth] = imagePixels[pixel++];
            }

        }
        imagePixels = widthByHeightPixels;
        myWidth = maxWidth;
        myHeight = maxHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public void methodCheckWidthAndHeight2() {
        if (myWidth == maxWidth && myHeight == maxHeight) {
            return;
        }
        byte widthByHeightPixels[] = new byte[maxWidth * maxHeight];
        int pixel = 0;
        for (int j = 0; j < myHeight; j++) {
            for (int k = 0; k < myWidth; k++) {
                widthByHeightPixels[k + drawOffsetX + (j + drawOffsetY) * maxWidth] = imagePixels[pixel++];
            }

        }
        imagePixels = widthByHeightPixels;
        myWidth = maxWidth;
        myHeight = maxHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public Sprite cropImage(int startX, int startY, int cropX, int cropY) {
        BufferedImage bufferedImageSprite = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImageSprite.setRGB(0, 0, myWidth, myHeight, myPixels, 0, myWidth);
        Image image = ImageUtil.makeColorTransparent(bufferedImageSprite, new Color(0, 0, 0));
        BufferedImage bufferedImage = ImageUtil.imageToBufferedImage(image);
        BufferedImage bufferedImage1 = bufferedImage.getSubimage(startX,
                startY,
                cropX == 0 ? myWidth : cropX,
                cropY == 0 ? myHeight : cropY);
        return new Sprite(bufferedImage1);
    }

    public void cropImage2(int startX, int startY, int cropX, int cropY) {
        BufferedImage bufferedImageSprite = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImageSprite.setRGB(0, 0, myWidth, myHeight, myPixels, 0, myWidth);
        Image image = ImageUtil.makeColorTransparent(bufferedImageSprite, new Color(0, 0, 0));
        BufferedImage bufferedImage = ImageUtil.imageToBufferedImage(image);
        BufferedImage bufferedImage1 = bufferedImage.getSubimage(startX,
                startY,
                cropX == 0 ? myWidth : cropX,
                cropY == 0 ? myHeight : cropY);
        Sprite sprite = new Sprite(bufferedImage1);
        myHeight = sprite.myHeight;
        myPixels = sprite.myPixels;
        myWidth = sprite.myWidth;
        spriteData = sprite.spriteData;
    }

    public Sprite flipHorizontally() {
        int[] des = new int[myWidth * myHeight];
        int pos = 0;
        for (int h = 0; h < myHeight; h++) {
            for (int w = myWidth - 1; w >= 0; w--) {
                des[pos++] = myPixels[w + h * myWidth];
            }
        }
        myPixels = des;
        drawOffsetX = maxWidth - myWidth - drawOffsetX;
        return this;
    }

    public Sprite flipVertically() {
        int[] des = new int[myWidth * myHeight];
        int pos = 0;
        for (int h = myHeight - 1; h >= 0; h--) {
            for (int w = 0; w < myWidth; w++) {
                des[pos++] = myPixels[w + h * myWidth];
            }
        }
        myPixels = des;
        drawOffsetY = maxWidth - myHeight - drawOffsetY;
        return this;
    }


    /**
     * Converts this {@link com.runescape.cache.media.Sprite} into a {@link com.runescape.cache.media.SpriteSet}.
     *
     * @return The SpriteSet.
     */
    public SpriteSet toSpriteSet() {
        return new SpriteSet(this, this);
    }

    public void drawHoverSprite(int x, int y, int offsetX, int offsetY, Sprite hover) {
        this.drawSprite(x, y);
        if (Game.INSTANCE.mouseX >= offsetX + x && Game.INSTANCE.mouseX <= offsetX + x + this.myWidth
                && Game.INSTANCE.mouseY >= offsetY + y && Game.INSTANCE.mouseY <= offsetY + y + this.myHeight) {
            hover.drawSprite(x, y);
        }
    }

    public void draw24BitSprite(int x, int y) {
        int alpha = 256;
        x += this.drawOffsetX;// offsetX
        y += this.drawOffsetY;// offsetY
        int destOffset = x + y * Raster.width;
        int srcOffset = 0;
        int height = this.myHeight;
        int width = this.myWidth;
        int destStep = Raster.width - width;
        int srcStep = 0;
        if (y < Raster.topY) {
            int trimHeight = Raster.topY - y;
            height -= trimHeight;
            y = Raster.topY;
            srcOffset += trimHeight * width;
            destOffset += trimHeight * Raster.width;
        }
        if (y + height > Raster.bottomY) {
            height -= (y + height) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int trimLeft = Raster.topX - x;
            width -= trimLeft;
            x = Raster.topX;
            srcOffset += trimLeft;
            destOffset += trimLeft;
            srcStep += trimLeft;
            destStep += trimLeft;
        }
        if (x + width > Raster.bottomX) {
            int trimRight = (x + width) - Raster.bottomX;
            width -= trimRight;
            srcStep += trimRight;
            destStep += trimRight;
        }
        if (!((width <= 0) || (height <= 0))) {
            set24BitPixels(width, height, Raster.pixels, myPixels, alpha, destOffset, srcOffset, destStep, srcStep);
        }
    }

    public void drawTransparentSprite(int x, int y, int opacity) {
        int k = opacity;// was parameter
        x += drawOffsetX;
        y += drawOffsetY;
        int i1 = x + y * Raster.width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = Raster.width - l1;
        int j2 = 0;
        if (y < Raster.topY) {
            int k2 = Raster.topY - y;
            k1 -= k2;
            y = Raster.topY;
            j1 += k2 * l1;
            i1 += k2 * Raster.width;
        }
        if (y + k1 > Raster.bottomY) {
            k1 -= (y + k1) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int l2 = Raster.topX - x;
            l1 -= l2;
            x = Raster.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (x + l1 > Raster.bottomX) {
            int i3 = (x + l1) - Raster.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            setPixels(j1, l1, Raster.pixels, myPixels, j2, k1, i2, k, i1);
        }
    }

    private void set24BitPixels(int width, int height, int destPixels[], int srcPixels[], int srcAlpha, int destOffset, int srcOffset, int destStep, int srcStep) {
        int srcColor;
        int destAlpha;
        for (int loop = -height; loop < 0; loop++) {
            for (int loop2 = -width; loop2 < 0; loop2++) {
                srcAlpha = ((this.myPixels[srcOffset] >> 24) & 255);
                destAlpha = 256 - srcAlpha;
                srcColor = srcPixels[srcOffset++];
                if (srcColor != 0 && srcColor != 0xffffff) {
                    int destColor = destPixels[destOffset];
                    destPixels[destOffset++] = ((srcColor & 0xff00ff) * srcAlpha + (destColor & 0xff00ff) * destAlpha & 0xff00ff00) + ((srcColor & 0xff00) * srcAlpha + (destColor & 0xff00) * destAlpha & 0xff0000) >> 8;
                } else {
                    destOffset++;
                }
            }
            destOffset += destStep;
            srcOffset += srcStep;
        }
    }

    public void setTransparency(int transRed, int transGreen, int transBlue) {
        for (int index = 0; index < myPixels.length; index++) {
            if (((myPixels[index] >> 16) & 255) == transRed && ((myPixels[index] >> 8) & 255) == transGreen && (myPixels[index] & 255) == transBlue) {
                myPixels[index] = 0;
            }
        }
    }

    public void initDrawingArea() {
        Raster.initDrawingArea(myHeight, myWidth, myPixels);
    }

    public void method344(int i, int j, int k) {
        for (int i1 = 0; i1 < myPixels.length; i1++) {
            int j1 = myPixels[i1];
            if (j1 != 0) {
                int k1 = j1 >> 16 & 0xff;
                k1 += i;
                if (k1 < 1) {
                    k1 = 1;
                } else if (k1 > 255) {
                    k1 = 255;
                }
                int l1 = j1 >> 8 & 0xff;
                l1 += j;
                if (l1 < 1) {
                    l1 = 1;
                } else if (l1 > 255) {
                    l1 = 255;
                }
                int i2 = j1 & 0xff;
                i2 += k;
                if (i2 < 1) {
                    i2 = 1;
                } else if (i2 > 255) {
                    i2 = 255;
                }
                myPixels[i1] = (k1 << 16) + (l1 << 8) + i2;
            }
        }

    }

    public void method345() {
        int ai[] = new int[maxWidth * maxHeight];
        for (int j = 0; j < myHeight; j++) {
            System.arraycopy(myPixels, j * myWidth, ai, j + drawOffsetY * maxWidth + drawOffsetX, myWidth);
        }

        myPixels = ai;
        myWidth = maxWidth;
        myHeight = maxHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public void method346(int x, int y) {
        x += drawOffsetX;
        y += drawOffsetY;
        int l = x + y * Raster.width;
        int i1 = 0;
        int height = myHeight;
        int width = myWidth;
        int l1 = Raster.width - width;
        int i2 = 0;
        if (y < Raster.topY) {
            int j2 = Raster.topY - y;
            height -= j2;
            y = Raster.topY;
            i1 += j2 * width;
            l += j2 * Raster.width;
        }
        if (y + height > Raster.bottomY) {
            height -= (y + height) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int k2 = Raster.topX - x;
            width -= k2;
            x = Raster.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (x + width > Raster.bottomX) {
            int l2 = (x + width) - Raster.bottomX;
            width -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (width <= 0 || height <= 0) {
        } else {
            method347(l, width, height, i2, i1, l1, myPixels, Raster.pixels);
        }
    }

    private void method347(int i, int j, int k, int l, int i1, int k1, int ai[], int ai1[]) {
        int l1 = -(j >> 2);
        j = -(j & 3);
        for (int i2 = -k; i2 < 0; i2++) {
            for (int j2 = l1; j2 < 0; j2++) {
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
            }

            for (int k2 = j; k2 < 0; k2++) {
                ai1[i++] = ai[i1++];
            }

            i += k1;
            i1 += l;
        }
    }

    public void drawAdvancedSprite(int i, int j) {
        int k = 256;
        i += drawOffsetX;
        j += drawOffsetY;
        int i1 = i + j * Raster.width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = Raster.width - l1;
        int j2 = 0;
        if (j < Raster.topY) {
            int k2 = Raster.topY - j;
            k1 -= k2;
            j = Raster.topY;
            j1 += k2 * l1;
            i1 += k2 * Raster.width;
        }
        if (j + k1 > Raster.bottomY) {
            k1 -= (j + k1) - Raster.bottomY;
        }
        if (i < Raster.topX) {
            int l2 = Raster.topX - i;
            l1 -= l2;
            i = Raster.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Raster.bottomX) {
            int i3 = (i + l1) - Raster.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            drawAlphaSprite(j1, l1, Raster.pixels, myPixels, j2, k1, i2,
                    k, i1);
        }
    }

    private void drawAlphaSprite(int i, int j, int ai[], int ai1[], int l,
                                 int i1, int j1, int k1, int l1) {
        int k;
        int j2;
        for (int k2 = -i1; k2 < 0; k2++) {
            for (int l2 = -j; l2 < 0; l2++) {
                k1 = ((myPixels[i] >> 24) & 255);
                j2 = 256 - k1;
                k = ai1[i++];
                if (k != 0) {
                    int i3 = ai[l1];
                    ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00)
                            + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
                } else {
                    l1++;
                }
            }
            l1 += j1;
            i += l;
        }
    }

    public void drawSprite1(int i, int j) {
        drawSprite1(i, j, 128, false);
    }

    public void drawSprite1(int i, int j, int k) {
        drawSprite1(i, j, k, false);
    }

    public void drawSprite1(int i, int j, int k, boolean overrideCanvas) {
        i += drawOffsetX;
        j += drawOffsetY;
        int i1 = i + j * Raster.width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = Raster.width - l1;
        int j2 = 0;
        if (!(overrideCanvas && j > 0) && j < Raster.topY) {
            int k2 = Raster.topY - j;
            k1 -= k2;
            j = Raster.topY;
            j1 += k2 * l1;
            i1 += k2 * Raster.width;
        }
        if (j + k1 > Raster.bottomY) {
            k1 -= (j + k1) - Raster.bottomY;
        }
        if (!overrideCanvas && i < Raster.topX) {
            int l2 = Raster.topX - i;
            l1 -= l2;
            i = Raster.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Raster.bottomX) {
            int i3 = (i + l1) - Raster.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            setPixels(j1, l1, Raster.pixels, myPixels, j2, k1, i2, k, i1);
        }
    }

    public void drawSprite(int x, int y) {
        x += drawOffsetX;
        y += drawOffsetY;
        int rasterClip = x + y * Raster.width;
        int imageClip = 0;
        int height = myHeight;
        int width = myWidth;
        int rasterOffset = Raster.width - width;
        int imageOffset = 0;
        if (y < Raster.topY) {
            int dy = Raster.topY - y;
            height -= dy;
            y = Raster.topY;
            imageClip += dy * width;
            rasterClip += dy * Raster.width;
        }
        if (y + height > Raster.bottomY) {
            height -= (y + height) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int dx = Raster.topX - x;
            width -= dx;
            x = Raster.topX;
            imageClip += dx;
            rasterClip += dx;
            imageOffset += dx;
            rasterOffset += dx;
        }
        if (x + width > Raster.bottomX) {
            int dx = (x + width) - Raster.bottomX;
            width -= dx;
            imageOffset += dx;
            rasterOffset += dx;
        }
        if (!(width <= 0 || height <= 0)) {
            method349(Raster.pixels, myPixels, imageClip, rasterClip, width, height, rasterOffset, imageOffset);
        }
    }

    public void drawSprite(int i, int k, int color) {
        int tempWidth = myWidth + 2;
        int tempHeight = myHeight + 2;
        int[] tempArray = new int[tempWidth * tempHeight];
        for (int x = 0; x < myWidth; x++) {
            for (int y = 0; y < myHeight; y++) {
                if (myPixels[x + y * myWidth] != 0) {
                    tempArray[(x + 1) + (y + 1) * tempWidth] = myPixels[x + y * myWidth];
                }
            }
        }
        for (int x = 0; x < tempWidth; x++) {
            for (int y = 0; y < tempHeight; y++) {
                if (tempArray[(x) + (y) * tempWidth] == 0) {
                    if (x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] > 0 && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (x > 0 && tempArray[(x - 1) + ((y) * tempWidth)] > 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] > 0 && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (y > 0 && tempArray[(x) + ((y - 1) * tempWidth)] > 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                }
            }
        }
        i--;
        k--;
        i += drawOffsetX;
        k += drawOffsetY;
        int l = i + k * Raster.width;
        int i1 = 0;
        int j1 = tempHeight;
        int k1 = tempWidth;
        int l1 = Raster.width - k1;
        int i2 = 0;
        if (k < Raster.topY) {
            int j2 = Raster.topY - k;
            j1 -= j2;
            k = Raster.topY;
            i1 += j2 * k1;
            l += j2 * Raster.width;
        }
        if (k + j1 > Raster.bottomY) {
            j1 -= (k + j1) - Raster.bottomY;
        }
        if (i < Raster.topX) {
            int k2 = Raster.topX - i;
            k1 -= k2;
            i = Raster.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (i + k1 > Raster.bottomX) {
            int l2 = (i + k1) - Raster.bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (!(k1 <= 0 || j1 <= 0)) {
            method349(Raster.pixels, tempArray, i1, l, k1, j1, l1, i2);
        }
    }

    public void drawSprite2(int i, int j) {
        int k = 225;// was parameter
        i += drawOffsetX;
        j += drawOffsetY;
        int i1 = i + j * Raster.width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = Raster.width - l1;
        int j2 = 0;
        if (j < Raster.topY) {
            int k2 = Raster.topY - j;
            k1 -= k2;
            j = Raster.topY;
            j1 += k2 * l1;
            i1 += k2 * Raster.width;
        }
        if (j + k1 > Raster.bottomY) {
            k1 -= (j + k1) - Raster.bottomY;
        }
        if (i < Raster.topX) {
            int l2 = Raster.topX - i;
            l1 -= l2;
            i = Raster.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Raster.bottomX) {
            int i3 = (i + l1) - Raster.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            setPixels(j1, l1, Raster.pixels, myPixels, j2, k1, i2, k, i1);
        }
    }

    public void drawMovingSprite(int x, int y, int opacity) {
        int alpha = opacity;
        x += this.drawOffsetX;
        y += this.drawOffsetY;
        int destOffset = x + y * Raster.width;
        int srcOffset = 0;
        int height = this.myHeight;
        int width = this.myWidth;
        int destStep = Raster.width - width;
        int srcStep = 0;
        if (y < Raster.topY) {
            int trimHeight = Raster.topY - y;
            height -= trimHeight;
            y = Raster.topY;
            srcOffset += trimHeight * width;
            destOffset += trimHeight * Raster.width;
        }
        if (y + height > Raster.bottomY) {
            height -= (y + height) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int trimLeft = Raster.topX - x;
            width -= trimLeft;
            x = Raster.topX;
            srcOffset += trimLeft;
            destOffset += trimLeft;
            srcStep += trimLeft;
            destStep += trimLeft;
        }
        if (x + width > Raster.bottomX) {
            int trimRight = (x + width) - Raster.bottomX;
            width -= trimRight;
            srcStep += trimRight;
            destStep += trimRight;
        }
        if (!((width <= 0) || (height <= 0))) {
            setPixels(width, height, Raster.pixels, myPixels, alpha, destOffset, srcOffset, destStep, srcStep);
        }
    }

    public void drawSprite3(int x, int y, int opacity) {
        int alpha = opacity;
        x += this.drawOffsetX;// offsetX
        y += this.drawOffsetY;// offsetY
        int destOffset = x + y * Raster.width;
        int srcOffset = 0;
        int height = this.myHeight;
        int width = this.myWidth;
        int destStep = Raster.width - width;
        int srcStep = 0;
        if (y < Raster.topY) {
            int trimHeight = Raster.topY - y;
            height -= trimHeight;
            y = Raster.topY;
            srcOffset += trimHeight * width;
            destOffset += trimHeight * Raster.width;
        }
        if (y + height > Raster.bottomY) {
            height -= (y + height) - Raster.bottomY;
        }
        if (x < Raster.topX) {
            int trimLeft = Raster.topX - x;
            width -= trimLeft;
            x = Raster.topX;
            srcOffset += trimLeft;
            destOffset += trimLeft;
            srcStep += trimLeft;
            destStep += trimLeft;
        }
        if (x + width > Raster.bottomX) {
            int trimRight = (x + width) - Raster.bottomX;
            width -= trimRight;
            srcStep += trimRight;
            destStep += trimRight;
        }
        if (!((width <= 0) || (height <= 0))) {
            setPixels(width, height, Raster.pixels, myPixels, alpha, destOffset, srcOffset, destStep, srcStep);
        }
    }

    private void method349(int ai[], int ai1[], int j, int k, int l, int i1, int j1, int k1) {
        int i;// was parameter
        int l1 = -(l >> 2);
        l = -(l & 3);
        for (int i2 = -i1; i2 < 0; i2++) {
            for (int j2 = l1; j2 < 0; j2++) {
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
            }

            for (int k2 = l; k2 < 0; k2++) {
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
            }
            k += j1;
            j += k1;
        }
    }

    private void setPixels(int i, int j, int ai[], int ai1[], int l, int i1, int j1, int k1, int l1) {
        int k;// was parameter
        int j2 = 256 - k1;
        for (int k2 = -i1; k2 < 0; k2++) {
            for (int l2 = -j; l2 < 0; l2++) {
                k = ai1[i++];
                if (k != 0) {
                    int i3 = ai[l1];
                    ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
                } else {
                    l1++;
                }
            }

            l1 += j1;
            i += l;
        }
    }

    public void rotate2(int dimension, int rotation, int yPosArray[], int zoom, int xPosArray[], int basePosition, int yPosition, int xPosition, int dimension_1, int middle) {
        try {
            int j2 = -dimension_1 / 2;
            int k2 = -dimension / 2;
            int l2 = (int) (Math.sin((double) rotation / 326.11000000000001D) * 65536D);
            int i3 = (int) (Math.cos((double) rotation / 326.11000000000001D) * 65536D);
            l2 = l2 * zoom >> 8;
            i3 = i3 * zoom >> 8;
            int j3 = (middle << 16) + (k2 * l2 + j2 * i3);
            int k3 = (basePosition << 16) + (k2 * i3 - j2 * l2);
            int l3 = xPosition + yPosition * Raster.width;
            for (yPosition = 0; yPosition < dimension; yPosition++) {
                int i4 = xPosArray[yPosition];
                int j4 = l3 + i4;
                int k4 = j3 + i3 * i4;
                int l4 = k3 - l2 * i4;
                for (xPosition = -yPosArray[yPosition]; xPosition < 0; xPosition++) {
                    int x1 = k4 >> 16;
                    int y1 = l4 >> 16;
                    int x2 = x1 + 1;
                    int y2 = y1 + 1;
                    int c1 = myPixels[x1 + y1 * myWidth];
                    int c2 = myPixels[x2 + y1 * myWidth];
                    int c3 = myPixels[x1 + y2 * myWidth];
                    int c4 = myPixels[x2 + y2 * myWidth];
                    int u1 = (k4 >> 8) - (x1 << 8);
                    int v1 = (l4 >> 8) - (y1 << 8);
                    int u2 = (x2 << 8) - (k4 >> 8);
                    int v2 = (y2 << 8) - (l4 >> 8);
                    int a1 = u2 * v2;
                    int a2 = u1 * v2;
                    int a3 = u2 * v1;
                    int a4 = u1 * v1;
                    int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 +
                            (c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
                    int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 +
                            (c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
                    int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 +
                            (c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
                    Raster.pixels[j4++] = r | g | b;
                    k4 += i3;
                    l4 -= l2;
                }

                j3 += l2;
                k3 += i3;
                l3 += Raster.width;
            }

        } catch (Exception _ex) {
        }
    }

    public void rotate(int height, int angle, int[] widthPixels, int zoom, int[] leftPixels, int centreY, int j1, int k1, int width, int centreX) {
        try {
            int j2 = -width / 2;
            int k2 = -height / 2;
            int l2 = (int) (Math.sin((double) angle / 326.11000000000001D) * 65536D);
            int i3 = (int) (Math.cos((double) angle / 326.11000000000001D) * 65536D);
            l2 = l2 * zoom >> 8;
            i3 = i3 * zoom >> 8;
            int j3 = (centreX << 16) + (k2 * l2 + j2 * i3);
            int k3 = (centreY << 16) + (k2 * i3 - j2 * l2);
            int l3 = k1 + j1 * Raster.width;
            for (j1 = 0; j1 < height; j1++) {
                int i4 = leftPixels[j1];
                int j4 = l3 + i4;
                int k4 = j3 + i3 * i4;
                int l4 = k3 - l2 * i4;
                for (k1 = -widthPixels[j1]; k1 < 0; k1++) {
                    int x1 = k4 >> 16;
                    int y1 = l4 >> 16;
                    int x2 = x1 + 1;
                    int y2 = y1 + 1;
                    int c1 = myPixels[x1 + y1 * myWidth];
                    int c2 = myPixels[x2 + y1 * myWidth];
                    int c3 = myPixels[x1 + y2 * myWidth];
                    int c4 = myPixels[x2 + y2 * myWidth];
                    int u1 = (k4 >> 8) - (x1 << 8);
                    int v1 = (l4 >> 8) - (y1 << 8);
                    int u2 = (x2 << 8) - (k4 >> 8);
                    int v2 = (y2 << 8) - (l4 >> 8);
                    int a1 = u2 * v2;
                    int a2 = u1 * v2;
                    int a3 = u2 * v1;
                    int a4 = u1 * v1;
                    int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 + (c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
                    int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 + (c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
                    int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 + (c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
                    Raster.pixels[j4++] = r | g | b;
                    k4 += i3;
                    l4 -= l2;
                }

                j3 += l2;
                k3 += i3;
                l3 += Raster.width;
            }
        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public void method353(int i, double d, int l1) {
        // all of the following were parameters
        int j = 15;
        int k = 20;
        int l = 15;
        int j1 = 256;
        int k1 = 20;
        // all of the previous were parameters
        try {
            int i2 = -k / 2;
            int j2 = -k1 / 2;
            int k2 = (int) (Math.sin(d) * 65536D);
            int l2 = (int) (Math.cos(d) * 65536D);
            k2 = k2 * j1 >> 8;
            l2 = l2 * j1 >> 8;
            int i3 = (l << 16) + (j2 * k2 + i2 * l2);
            int j3 = (j << 16) + (j2 * l2 - i2 * k2);
            int k3 = l1 + i * Raster.width;
            for (i = 0; i < k1; i++) {
                int l3 = k3;
                int i4 = i3;
                int j4 = j3;
                for (l1 = -k; l1 < 0; l1++) {
                    int k4 = myPixels[(i4 >> 16) + (j4 >> 16) * myWidth];
                    if (k4 != 0) {
                        Raster.pixels[l3++] = k4;
                    } else {
                        l3++;
                    }
                    i4 += l2;
                    j4 -= k2;
                }

                i3 += k2;
                j3 += l2;
                k3 += Raster.width;
            }

        } catch (Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public void method354(Background background, int i, int j) {
        j += drawOffsetX;
        i += drawOffsetY;
        int k = j + i * Raster.width;
        int l = 0;
        int i1 = myHeight;
        int j1 = myWidth;
        int k1 = Raster.width - j1;
        int l1 = 0;
        if (i < Raster.topY) {
            int i2 = Raster.topY - i;
            i1 -= i2;
            i = Raster.topY;
            l += i2 * j1;
            k += i2 * Raster.width;
        }
        if (i + i1 > Raster.bottomY) {
            i1 -= (i + i1) - Raster.bottomY;
        }
        if (j < Raster.topX) {
            int j2 = Raster.topX - j;
            j1 -= j2;
            j = Raster.topX;
            l += j2;
            k += j2;
            l1 += j2;
            k1 += j2;
        }
        if (j + j1 > Raster.bottomX) {
            int k2 = (j + j1) - Raster.bottomX;
            j1 -= k2;
            l1 += k2;
            k1 += k2;
        }
        if (!(j1 <= 0 || i1 <= 0)) {
            method355(myPixels, j1, background.aByteArray1450, i1, Raster.pixels, 0, k1, k, l1, l);
        }
    }

    public void drawARGBSprite(int xPos, int yPos) {
        drawARGBSprite(xPos, yPos, 256);
    }

    public void drawARGBSprite(int xPos, int yPos, int alpha) {
        int alphaValue = alpha;
        xPos += drawOffsetX;
        yPos += drawOffsetY;
        int i1 = xPos + yPos * Raster.width;
        int j1 = 0;
        int spriteHeight = myHeight;
        int spriteWidth = myWidth;
        int i2 = Raster.width - spriteWidth;
        int j2 = 0;
        if (yPos < Raster.topY) {
            int k2 = Raster.topY - yPos;
            spriteHeight -= k2;
            yPos = Raster.topY;
            j1 += k2 * spriteWidth;
            i1 += k2 * Raster.width;
        }
        if (yPos + spriteHeight > Raster.bottomY) {
            spriteHeight -= (yPos + spriteHeight) - Raster.bottomY;
        }
        if (xPos < Raster.topX) {
            int l2 = Raster.topX - xPos;
            spriteWidth -= l2;
            xPos = Raster.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (xPos + spriteWidth > Raster.bottomX) {
            int i3 = (xPos + spriteWidth) - Raster.bottomX;
            spriteWidth -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(spriteWidth <= 0 || spriteHeight <= 0)) {
            renderARGBPixels(spriteWidth, spriteHeight, myPixels, Raster.pixels, i1, alphaValue, j1, j2, i2);
        }
    }

    private void renderARGBPixels(int spriteWidth, int spriteHeight, int spritePixels[], int renderAreaPixels[], int pixel, int alphaValue, int i, int l, int j1) {
        int pixelColor;
        int alphaLevel;
        int alpha = alphaValue;
        for (int height = -spriteHeight; height < 0; height++) {
            for (int width = -spriteWidth; width < 0; width++) {
                alphaValue = ((myPixels[i] >> 24) & (alpha - 1));
                alphaLevel = 256 - alphaValue;
                if (alphaLevel > 256) {
                    alphaValue = 0;
                }
                if (alpha == 0) {
                    alphaLevel = 256;
                    alphaValue = 0;
                }
                pixelColor = spritePixels[i++];
                if (pixelColor != 0) {
                    int pixelValue = renderAreaPixels[pixel];
                    renderAreaPixels[pixel++] = ((pixelColor & 0xff00ff) * alphaValue + (pixelValue & 0xff00ff) * alphaLevel & 0xff00ff00) + ((pixelColor & 0xff00) * alphaValue + (pixelValue & 0xff00) * alphaLevel & 0xff0000) >> 8;
                } else {
                    pixel++;
                }
            }
            pixel += j1;
            i += l;
        }
    }

    private void method355(int ai[], int i, byte abyte0[], int j, int ai1[], int k, int l, int i1, int j1, int k1) {
        int l1 = -(i >> 2);
        i = -(i & 3);
        for (int j2 = -j; j2 < 0; j2++) {
            for (int k2 = l1; k2 < 0; k2++) {
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
            }

            for (int l2 = i; l2 < 0; l2++) {
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
            }

            i1 += l;
            k1 += j1;
        }
    }

    public Sprite scale(int width, int height) {
        myWidth = width;
        myHeight = height;
        maxWidth = width;
        maxHeight = height;
        return this;
    }

    public Sprite resizeSprite(double desiredWidth, double desiredHeight) {
        BufferedImage image = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_INT_RGB);
        int count = 0;
        for (int y = 0; y < myHeight; y++) {
            for (int x = 0; x < myWidth; x++) {
                if (myPixels[count] == 0) {
                    continue;
                }
                image.setRGB(x, y, myPixels[count]);
                count++;
            }
        }
        return new Sprite(scaleImage(image, (int) desiredWidth, (int) desiredHeight));
    }
}
