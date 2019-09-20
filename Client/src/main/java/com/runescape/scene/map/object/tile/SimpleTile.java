package com.runescape.scene.map.object.tile;

import main.java.com.runescape.Game;
import com.runescape.cache.media.inter.InterfaceConfiguration;

public final class SimpleTile {
    public final int color1; // lA
    public final int color2; // lB
    public final int color3; // lC
    public final int color4; // lD
    public final int colourA;
    public final int colourB;
    public final int colourD;
    public final int colourC;
    public final int textureId;
    public final int rgbColour;
    public final int color;
    public boolean isFlat;
    public boolean textured;

    public SimpleTile(int colA, int colB, int colD, int colC, int textureId, int rgbColour, boolean flag, int color, boolean tex) {
        isFlat = true;
        colourA = colA;//shadow_a
        colourB = colB;//shadow_b
        colourD = colD;//shadow_d
        colourC = colC;//shadow_c
        color1 = colA; // lb = n
        color2 = colB;
        color3 = colD;
        color4 = colC;
        this.textureId = textureId;//texture
        this.rgbColour = rgbColour;//rgb
        isFlat = flag;//
        this.color = color;//color
        textured = tex;
        int cheapHax = textureId;
        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
            cheapHax = -1;
        }
        textured = cheapHax != -1;
    }
}
