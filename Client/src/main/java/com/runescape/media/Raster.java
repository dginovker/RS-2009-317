package com.runescape.media;

import com.runescape.collection.Cacheable;

public class Raster extends Cacheable {

    public static int[] pixels;
    public static int width;
    public static int height;
    public static int topY;
    public static int bottomY;
    public static int topX;
    public static int bottomX;
    public static int viewportRX;
    public static int viewport_centerY;
    public static int viewport_centerX;
    public static int anInt1387;

    public static void initDrawingArea(int height1, int width1, int[] pixels1) {
        pixels = pixels1;
        width = width1;
        height = height1;
        setDrawingArea(height1, 0, width1, 0);
    }

    public static void drawAlphaGradient(int x, int y, int gradientWidth, int gradientHeight, int startColor, int endColor, int alpha) {
        int k1 = 0;
        int l1 = 0x10000 / gradientHeight;
        if (x < topX) {
            gradientWidth -= topX - x;
            x = topX;
        }
        if (y < topY) {
            k1 += (topY - y) * l1;
            gradientHeight -= topY - y;
            y = topY;
        }
        if (x + gradientWidth > bottomX) {
            gradientWidth = bottomX - x;
        }
        if (y + gradientHeight > bottomY) {
            gradientHeight = bottomY - y;
        }
        int i2 = width - gradientWidth;
        int result_alpha = 256 - alpha;
        int total_pixels = x + y * width;
        for (int k2 = -gradientHeight; k2 < 0; k2++) {
            int gradient1 = 0x10000 - k1 >> 8;
            int gradient2 = k1 >> 8;
            int gradient_color = ((startColor & 0xff00ff) * gradient1 + (endColor & 0xff00ff) * gradient2 & 0xff00ff00) + ((startColor & 0xff00) * gradient1 + (endColor & 0xff00) * gradient2 & 0xff0000) >>> 8;
            int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
            for (int k3 = -gradientWidth; k3 < 0; k3++) {
                int colored_pixel = pixels[total_pixels];
                colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff) + ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
                pixels[total_pixels++] = color + colored_pixel;
            }
            total_pixels += i2;
            k1 += l1;
        }
    }

    public static void drawLine(int yPos, int color, int widthToDraw, int xPos) {
        if (yPos < topY || yPos >= bottomY) {
            return;
        }
        if (xPos < topX) {
            widthToDraw -= topX - xPos;
            xPos = topX;
        }
        if (xPos + widthToDraw > bottomX) {
            widthToDraw = bottomX - xPos;
        }
        int base = xPos + yPos * width;
        for (int ptr = 0; ptr < widthToDraw; ptr++) {
            pixels[base + ptr] = color;
        }
    }

    public static void drawAlphaFilledPixels(int xPos, int yPos, int pixelWidth, int pixelHeight, int color, int alpha) {
        if (xPos < topX) {
            pixelWidth -= topX - xPos;
            xPos = topX;
        }
        if (yPos < topY) {
            pixelHeight -= topY - yPos;
            yPos = topY;
        }
        if (xPos + pixelWidth > bottomX) {
            pixelWidth = bottomX - xPos;
        }
        if (yPos + pixelHeight > bottomY) {
            pixelHeight = bottomY - yPos;
        }
        color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((color & 0xff00) * alpha >> 8 & 0xff00);
        int k1 = 256 - alpha;
        int l1 = width - pixelWidth;
        int i2 = xPos + yPos * width;
        for (int j2 = 0; j2 < pixelHeight; j2++) {
            for (int k2 = -pixelWidth; k2 < 0; k2++) {
                int l2 = pixels[i2];
                l2 = ((l2 & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((l2 & 0xff00) * k1 >> 8 & 0xff00);
                pixels[i2++] = color + l2;
            }
            i2 += l1;
        }
    }

    public static void drawAlphaBox(int x, int y, int lineWidth, int lineHeight, int color, int alpha) {
        if (y < topY) {
            if (y > (topY - lineHeight)) {
                lineHeight -= (topY - y);
                y += (topY - y);
            } else {
                return;
            }
        }
        if (y + lineHeight > bottomY) {
            lineHeight -= y + lineHeight - bottomY;
        }
        if (x < topX) {
            lineWidth -= topX - x;
            x = topX;
        }
        if (x + lineWidth > bottomX) {
            lineWidth = bottomX - x;
        }
        for (int yOff = 0; yOff < lineHeight; yOff++) {
            int i3 = x + (y + (yOff)) * width;
            for (int j3 = 0; j3 < lineWidth; j3++) {
                int j1 = 256 - alpha;
                int k1 = (color >> 16 & 0xff) * alpha;
                int l1 = (color >> 8 & 0xff) * alpha;
                int i2 = (color & 0xff) * alpha;
                int j2 = (pixels[i3] >> 16 & 0xff) * j1;
                int k2 = (pixels[i3] >> 8 & 0xff) * j1;
                int l2 = (pixels[i3] & 0xff) * j1;
                int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
                        + (i2 + l2 >> 8);
                pixels[i3++] = k3;
            }
        }
    }

    public static void defaultDrawingAreaSize() {
        topX = 0;
        topY = 0;
        bottomX = width;
        bottomY = height;
        viewportRX = bottomX;
        viewport_centerX = bottomX / 2;
        viewport_centerY = bottomY / 2;
    }

    public static void drawHorizontalLine(int drawX, int drawY, int lineWidth, int colour, boolean underline) {
        if (drawY >= topY && drawY < bottomY) {
            if (drawX < topX) {
                lineWidth -= topX - drawX;
                drawX = topX;
            }
            if (drawX + lineWidth > bottomX) {
                lineWidth = bottomX - drawX;
            }
            if (underline) {
                drawY += 1;
            }
            int drawLength = drawX + drawY * width;
            for (int drawWidth = 0; drawWidth < lineWidth; drawWidth++) {
                pixels[drawLength + drawWidth] = colour;
            }
        }
    }

    public static void setDrawingArea(int i, int j, int k, int l) {
        if (j < 0) {
            j = 0;
        }
        if (l < 0) {
            l = 0;
        }
        if (k > width) {
            k = width;
        }
        if (i > height) {
            i = height;
        }
        topX = j;
        topY = l;
        bottomX = k;
        bottomY = i;
        viewportRX = bottomX;
        viewport_centerX = bottomX / 2;
        anInt1387 = bottomY / 2;
    }


    public static void clear() {
        int toClear = width * height;
        for (int pixelIndex = 0; pixelIndex < toClear; pixelIndex++) {
            pixels[pixelIndex] = 0;
        }
    }

    public static void method335(int i, int j, int k, int l, int i1, int k1) {
        if (k1 < topX) {
            k -= topX - k1;
            k1 = topX;
        }
        if (j < topY) {
            l -= topY - j;
            j = topY;
        }
        if (k1 + k > bottomX) {
            k = bottomX - k1;
        }
        if (j + l > bottomY) {
            l = bottomY - j;
        }
        int l1 = 256 - i1;
        int i2 = (i >> 16 & 0xff) * i1;
        int j2 = (i >> 8 & 0xff) * i1;
        int k2 = (i & 0xff) * i1;
        int k3 = width - k;
        int l3 = k1 + j * width;
        for (int i4 = 0; i4 < l; i4++) {
            for (int j4 = -k; j4 < 0; j4++) {
                int l2 = (pixels[l3] >> 16 & 0xff) * l1;
                int i3 = (pixels[l3] >> 8 & 0xff) * l1;
                int j3 = (pixels[l3] & 0xff) * l1;
                int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
                pixels[l3++] = k4;
            }

            l3 += k3;
        }
    }

    public static void drawAlphaPixels(int x, int y, int w, int h, int color, int alpha) {
        if (x < topX) {
            w -= topX - x;
            x = topX;
        }
        if (y < topY) {
            h -= topY - y;
            y = topY;
        }
        if (x + w > bottomY) {
            w = bottomY - x;
        }
        if (y + h > bottomY) {
            h = bottomY - y;
        }
        int l1 = 256 - alpha;
        int i2 = (color >> 16 & 0xff) * alpha;
        int j2 = (color >> 8 & 0xff) * alpha;
        int k2 = (color & 0xff) * alpha;
        int k3 = width - w;
        int l3 = x + y * width;
        for (int i4 = 0; i4 < h; i4++) {
            for (int j4 = -w; j4 < 0; j4++) {
                int l2 = (pixels[l3] >> 16 & 0xff) * l1;
                int i3 = (pixels[l3] >> 8 & 0xff) * l1;
                int j3 = (pixels[l3] & 0xff) * l1;
                int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8)
                        + (k2 + j3 >> 8);
                pixels[l3++] = k4;
            }

            l3 += k3;
        }
    }

    public static void drawPixels(int pixelHeight, int smallY, int smallX, int colour, int pixelWidth) {
        if (smallX < topX) {
            pixelWidth -= topX - smallX;
            smallX = topX;
        }
        if (smallY < topY) {
            pixelHeight -= topY - smallY;
            smallY = topY;
        }
        if (smallX + pixelWidth > bottomX) {
            pixelWidth = bottomX - smallX;
        }
        if (smallY + pixelHeight > bottomY) {
            pixelHeight = bottomY - smallY;
        }
        int k1 = width - pixelWidth;
        int l1 = smallX + smallY * width;
        for (int i2 = -pixelHeight; i2 < 0; i2++) {
            for (int j2 = -pixelWidth; j2 < 0; j2++) {
                pixels[l1++] = colour;
            }

            l1 += k1;
        }
    }

    public static void fillPixels(int i, int j, int k, int l, int i1) {
        drawLinePixels(i1, l, j, i);
        drawLinePixels((i1 + k) - 1, l, j, i);
        method341(i1, l, k, i);
        method341(i1, l, k, (i + j) - 1);
    }

    public static void method338(int i, int j, int k, int l, int i1, int j1) {
        method340(l, i1, i, k, j1);
        method340(l, i1, (i + j) - 1, k, j1);
        if (j >= 3) {
            method342(l, j1, k, i + 1, j - 2);
            method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
        }
    }

    public static void drawLinePixels(int i, int j, int k, int l) {
        if (i < topY || i >= bottomY) {
            return;
        }
        if (l < topX) {
            k -= topX - l;
            l = topX;
        }
        if (l + k > bottomX) {
            k = bottomX - l;
        }
        int i1 = l + i * width;
        for (int j1 = 0; j1 < k; j1++) {
            pixels[i1 + j1] = j;
        }

    }

    private static void method340(int i, int j, int k, int l, int i1) {
        if (k < topY || k >= bottomY) {
            return;
        }
        if (i1 < topX) {
            j -= topX - i1;
            i1 = topX;
        }
        if (i1 + j > bottomX) {
            j = bottomX - i1;
        }
        int j1 = 256 - l;
        int k1 = (i >> 16 & 0xff) * l;
        int l1 = (i >> 8 & 0xff) * l;
        int i2 = (i & 0xff) * l;
        int i3 = i1 + k * width;
        for (int j3 = 0; j3 < j; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3++] = k3;
        }

    }

    public static void method341(int i, int j, int k, int l) {
        if (l < topX || l >= bottomX) {
            return;
        }
        if (i < topY) {
            k -= topY - i;
            i = topY;
        }
        if (i + k > bottomY) {
            k = bottomY - i;
        }
        int j1 = l + i * width;
        for (int k1 = 0; k1 < k; k1++) {
            pixels[j1 + k1 * width] = j;
        }

    }

    private static void method342(int i, int j, int k, int l, int i1) {
        if (j < topX || j >= bottomX) {
            return;
        }
        if (l < topY) {
            i1 -= topY - l;
            l = topY;
        }
        if (l + i1 > bottomY) {
            i1 = bottomY - l;
        }
        int j1 = 256 - k;
        int k1 = (i >> 16 & 0xff) * k;
        int l1 = (i >> 8 & 0xff) * k;
        int i2 = (i & 0xff) * k;
        int i3 = j + l * width;
        for (int j3 = 0; j3 < i1; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3] = k3;
            i3 += width;
        }
    }

    public static void drawRectangle(int x, int y, int width, int height, int color, int alpha) {
        drawHorizontalLine(x, y, width, color, alpha);
        drawHorizontalLine(x, y + height - 1, width, color, alpha);
        if (height >= 3) {
            drawVerticalLine(x, y + 1, height - 2, color, alpha);
            drawVerticalLine(x + width - 1, y + 1, height - 2, color, alpha);
        }
    }

    public static void fillRectangle2(int color, int y, int widthz, int heightz, int opacity, int x) {
        if (x < topX) {
            widthz -= topX - x;
            x = topX;
        }
        if (y < topY) {
            heightz -= topY - y;
            y = topY;
        }
        if (x + widthz > bottomX) {
            widthz = bottomX - x;
        }
        if (y + heightz > bottomY) {
            heightz = bottomY - y;
        }
        int decodedOpacity = 256 - opacity;
        int red = (color >> 16 & 0xff) * opacity;
        int green = (color >> 8 & 0xff) * opacity;
        int blue = (color & 0xff) * opacity;
        int pixelWidthStep = width - widthz;
        int startPixel = x + y * width;
        for (int h = 0; h < heightz; h++) {
            for (int w = -widthz; w < 0; w++) {
                int pixelRed = (pixels[startPixel] >> 16 & 0xff) * decodedOpacity;
                int pixelBlue = (pixels[startPixel] >> 8 & 0xff) * decodedOpacity;
                int pixelGreen = (pixels[startPixel] & 0xff) * decodedOpacity;
                int pixelRGB = ((red + pixelRed >> 8) << 16) + ((green + pixelBlue >> 8) << 8) + (blue + pixelGreen >> 8);
                pixels[startPixel++] = pixelRGB;
            }

            startPixel += pixelWidthStep;
        }
    }

    public static void fillRectangle(int x, int y, int width, int height, int color, int alpha) {
        if (x < topX) {
            width -= topX - x;
            x = topX;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (x + width > bottomX) {
            width = bottomX - x;
        }
        if (y + height > bottomY) {
            height = bottomY - y;
        }
        final int l1 = 256 - alpha;
        final int i2 = (color >> 16 & 0xff) * alpha;
        final int j2 = (color >> 8 & 0xff) * alpha;
        final int k2 = (color & 0xff) * alpha;
        final int k3 = width - width;
        int l3 = x + y * width;
        for (int i4 = 0; i4 < height; i4++) {
            for (int j4 = -width; j4 < 0; j4++) {
                final int l2 = (pixels[l3] >> 16 & 0xff) * l1;
                final int i3 = (pixels[l3] >> 8 & 0xff) * l1;
                final int j3 = (pixels[l3] & 0xff) * l1;
                final int k4 = (i2 + l2 >> 8 << 16) + (j2 + i3 >> 8 << 8) + (k2 + j3 >> 8);
                pixels[l3++] = k4;
            }
            l3 += k3;
        }
    }

    public static void drawHorizontalLine(int x, int y, int length, int color, int alpha) {
        if (y < topY || y >= bottomY) {
            return;
        }
        if (x < topX) {
            length -= topX - x;
            x = topX;
        }
        if (x + length > bottomX) {
            length = bottomX - x;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            pixels[i3++] = k3;
        }
    }

    public static void drawVerticalLine(int x, int y, int length, int color, int alpha) {
        if (x < topX || x >= bottomX) {
            return;
        }
        if (y < topY) {
            length -= topY - y;
            y = topY;
        }
        if (y + length > bottomY) {
            length = bottomY - y;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            pixels[i3] = k3;
            i3 += width;
        }
    }

    public static void drawVerticalLine(int x, int y, int h, int color) {
        if (x < topX || x >= bottomX) {
            return;
        }
        if (y < topY) {
            h -= topY - y;
            y = topY;
        }
        if (y + h > bottomY) {
            h = bottomY - y;
        }
        int index = x + y * width;
        for (int j1 = 0; j1 < h; ) {
            pixels[index] = color;
            j1++;
            index += width;
        }
    }

    /**
     * Draws a black box.
     *
     * @param xPos The x position.
     * @param yPos The y position.
     */
    public static void drawBlackBox(int xPos, int yPos) {
        drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1);
        drawPixels(69, yPos, xPos + 174, 0x726451, 1);
        drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178);
        drawPixels(1, yPos + 68, xPos, 0x726451, 174);
        drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1);
        drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1);
        drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175);
        drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175);
        method335(0, yPos, 174, 68, 220, xPos);
    }
}