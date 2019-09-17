package com.runescape.util;

import com.runescape.Constants;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.Sprite;
import com.runescape.media.Raster;
import com.runescape.media.renderable.Model;
import com.runescape.scene.graphic.Rasterizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;

/**
 * Handles dumping a {@link com.runescape.cache.media.Sprite}.
 *
 * @author <a href="http://www.rune-server.org/members/joshua/">Joshua</a>
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ImageUtil {

    /**
     * The directory to dump the images.
     */
    private static final String DUMP_DIRECTORY = "dumped_images" + File.separator;
    static int index = 0;

    /**
     * Dumps a {@link com.runescape.cache.media.Sprite}.
     *
     * @param sprite The sprite to dump.
     * @param name   The name of the file.
     */
    public static void dumpImage(Sprite sprite, String name) {
        File directory = new File(DUMP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        BufferedImage bi = new BufferedImage(sprite.myWidth, sprite.myHeight, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, sprite.myWidth, sprite.myHeight, sprite.myPixels, 0, sprite.myWidth);
        Image img = makeColorTransparent(bi, new Color(0, 0, 0));
        BufferedImage trans = imageToBufferedImage(img);
        try {
            File out = new File(DUMP_DIRECTORY + name + ".png");
            ImageIO.write(trans, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dumps a {@link com.runescape.cache.media.Sprite}.
     *
     * @param sprite The sprite to dump.
     */
    public static void dumpImage(Sprite sprite, String fileName, String directory) {
        File saveDirectory = new File(DUMP_DIRECTORY + directory);
        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        }
        BufferedImage bufferedImage = new BufferedImage(sprite.myWidth, sprite.myHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, sprite.myWidth, sprite.myHeight, sprite.myPixels, 0, sprite.myWidth);
        Image image = makeColorTransparent(bufferedImage, new Color(0, 0, 0));
        BufferedImage bufferedImage1 = imageToBufferedImage(image);
        try {
            File file = new File(DUMP_DIRECTORY + directory + File.separator + fileName + ".png");
            if (!file.exists()) {
                System.out.println("Dump image " + fileName + " to directory " + (DUMP_DIRECTORY + directory));
                ImageIO.write(bufferedImage1, "png", file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dumps a {@link com.runescape.cache.media.Sprite}.
     *
     * @param
     */
    public static void dumpImage(int[] myPixels, int myWidth, int myHeight, int drawOffsetX, int drawOffsetY, String fileName, String directory, boolean dumpSprites, boolean dumpOffsets, boolean dumpDimensions) {
        File dumpDirectory = new File(Constants.SPRITE_DUMP_DIRECTORY);
        if (!dumpDirectory.exists()) {
            dumpDirectory.mkdir();
        }
        File spriteDirectory = new File(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "sprites" + File.separator + directory);
        if (!spriteDirectory.exists()) {
            spriteDirectory.mkdir();
        }
        File offsetDirectory = new File(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "offsets" + File.separator + directory);
        if (!offsetDirectory.exists()) {
            offsetDirectory.mkdir();
        }
        File dimensionDirectory = new File(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "dimensions" + File.separator + directory);
        if (!dimensionDirectory.exists()) {
            dimensionDirectory.mkdir();
        }
        if (dumpSprites) {
            BufferedImage bufferedImage = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_INT_RGB);
            bufferedImage.setRGB(0, 0, myWidth, myHeight, myPixels, 0, myWidth);
            Image image = makeColorTransparent(bufferedImage, new Color(0, 0, 0));
            BufferedImage bufferedImage1 = imageToBufferedImage(image);
            try {
                File file = new File(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "sprites" + File.separator + directory + File.separator + fileName + ".png");
                ImageIO.write(bufferedImage1, "png", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (dumpOffsets) {
            FileUtility.writeFile(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "offsets" + File.separator + directory + File.separator + fileName + ".txt", String.valueOf(drawOffsetX) + "\r\n" + drawOffsetY);
        }
        if (dumpDimensions) {
            FileUtility.writeFile(Constants.SPRITE_DUMP_DIRECTORY + File.separator + "dimensions" + File.separator + directory + File.separator + fileName + ".txt", String.valueOf(myWidth) + "\r\n" + myHeight);
        }
    }

    /**
     * Turns an Image into a BufferedImage.
     *
     * @param image
     * @return
     */
    public static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * Makes the specified color transparent in a buffered image.
     *
     * @param im
     * @param color
     * @return
     */
    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        RGBImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * Dumps item images.
     */
    public static void dumpItemImages(ItemDefinition itemDefinition) {
        //for (int i = 34869; i < 41427; i++) {
        //System.out.println("Dumping item: " + i + " out of " + ItemDefinition.getItemCount());
        int i = itemDefinition.id;

        if (i >= ItemDefinition.getItemCount()) {
            //System.out.println("DONE!");
            //break;
        }
        //ItemDefinition itemDefinition = ItemDefinition.forId(i);
        /*if (itemDefinition == null) {
            return;
        }*/

        Sprite itemSprite = ItemDefinition.getSprite(itemDefinition.id, 0, 0);
        if (itemSprite == null) {
            itemSprite = ItemDefinition.getSprite(itemDefinition.id, 1, 0);
        }
        if (itemSprite == null) {
            itemSprite = ItemDefinition.getSprite(itemDefinition.id, 1, 1);
        }
        if (itemSprite == null) {
            itemSprite = ItemDefinition.getSprite(itemDefinition.id, 2, 1);
        }
        if (itemSprite == null) {
            //System.out.println("rip" + i);
            //i = 0;
            //dumpItemImages();
            itemSprite = ItemDefinition.getSprite(itemDefinition.id, 0, 0);
            if (itemSprite == null) {
                itemSprite = ItemDefinition.getSprite(itemDefinition.id, 1, 0);
            }
            if (itemSprite == null) {
                itemSprite = ItemDefinition.getSprite(itemDefinition.id, 1, 1);
            }
            if (itemSprite == null) {
                itemSprite = ItemDefinition.getSprite(itemDefinition.id, 2, 1);
            }
            if (itemSprite == null) {
                System.out.println("rip" + i);
                //dumpItemImages();
                return;
            }
        }
        /*if (itemDefinition == null || itemDefinition.name == null || itemDefinition.name.equalsIgnoreCase("null")) {
            System.out.println("Skipping null item...");
            return;
            //continue;
        }*/
        //System.out.println(new String(itemDefinition.description));
        //System.out.println(itemDefinition.name);
        dumpImage(itemSprite, String.valueOf(i), "items");
        //}
    }

    public static Sprite toSprite(Model model, int width, int height) {
        Sprite sprite = new Sprite(width, height);
        int i_7 = Rasterizer.viewportRX;
        int i_8 = Rasterizer.viewport_centerX;
        int[] is = Rasterizer.lineOffsets; // lineOffsets
        int[] canPixels = Raster.pixels;
        int canWidth = Raster.width;
        int canHeight = Raster.height;

        Rasterizer.notTextured = false;
        Raster.initDrawingArea(500, 500, sprite.myPixels);
        Raster.drawPixels(0, 0, 500, 500, 0);
        Rasterizer.setDefaultBounds();//.method364();//initToActiveSurface();
        int yRotate = 1;
        int zoom = 500;
        int i_12 = ((Rasterizer.SINE[yRotate] * zoom) >> 16);
        int y = ((Rasterizer.COSINE[yRotate] * zoom) >> 16);
        model.fitsOnSingleSquare = true;
        model.renderSingle(0, 0, 0, yRotate, i_12, y);
        Raster.initDrawingArea(canWidth, canHeight, canPixels);
        Rasterizer.viewportRX = i_7;
        Rasterizer.viewport_centerX = i_8;
        Rasterizer.lineOffsets = is;
        Rasterizer.notTextured = true;
        sprite.maxHeight = 500;
        return sprite;
    }
}
