package com.runescape.media;

import com.runescape.Constants;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Animation {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(Animation.class.getName());
    public static Animation[] animations;
    public int frameCount;
    public int[] frames;
    public int[] modelFrames;
    public int[] delays;
    public int loopDelay;
    public int[] animationFlowControl;
    public boolean aBoolean358;
    public int anInt359;
    public int anInt360;
    public int itemId1;
    public int anInt362;
    public int anInt363;
    public int anInt364;
    public int anInt365;
    public boolean osrs;
    private int[] intArray95;
    private int[] intArray97;

    public Animation() {
        loopDelay = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1;
        itemId1 = -1;
        anInt362 = 99;
        anInt363 = -1;
        anInt364 = -1;
        anInt365 = 1;
        osrs = false;
    }

    public static void unpackConfig(CacheArchive archive) {//16147
        RSStream rsStream = new RSStream(FileUtility.getBytes(new File(Constants.getCachePath(true) + File.separator + "/config/498seq.dat")));
        int length = rsStream.getShort();

        RSStream osrsStream = new RSStream(FileUtility.getBytes(new File(Constants.getCachePath(true) + File.separator + "/config/seq.dat")));
        int osrsLength = osrsStream.getShort();

        if (animations == null) {
            animations = new Animation[20_000 + osrsLength];
        }
        try {
            for (int index = 0; index < animations.length; index++) {
                if (animations[index] == null) {
                    animations[index] = new Animation();
                }
                if (index > length && index < 20_000) {
                    continue;
                }
                if (index < length) {
                    animations[index].osrs = false;
                    animations[index].readValues498(rsStream);
                } else if (index >= 20_000 && index < 20_000 + osrsLength) {
                    animations[index].osrs = true;
                    animations[index].readValues498(osrsStream);
                    animations[index].post();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the duration of this animation in milliseconds.
     *
     * @return The duration.
     */
    public int getDuration() {
        if (delays == null) {
            return 0;
        }
        int duration = 0;
        for (int i : delays) {
            duration += i * 30;
        }
        return duration;
    }

    /**
     * Gets the duration of this animation in (600ms) ticks.
     *
     * @return The duration in ticks.
     */
    public int getDurationTicks() {
        return Math.round(getDuration() / 1200);
    }

    public int fetchAnimationLength(int index) {
        if (delays == null || index > delays.length) {
            return 1;
        }
        int frameLength = delays[index];
        if (frameLength == 0) {
            SequenceFrame sequenceFrame = SequenceFrame.fetchFileInfo(frames[index], this.osrs);
            if (sequenceFrame != null) {
                frameLength = delays[index] = sequenceFrame.anInt636;
            }
        }
        if (frameLength == 0) {
            frameLength = 1;
        }
        return frameLength;
    }

    void decode(RSStream buffer) {
        while (true) {
            int opcode = buffer.getByte();
            if (opcode == 0)
                return;

            this.decode(buffer, opcode);
        }
    }

    void decode(RSStream buffer, int opcode) {
        int i_5;
        if (opcode == 1) {
            frameCount = buffer.getShort();
            delays = new int[frameCount];
            for (i_5 = 0; i_5 < frameCount; i_5++)
                delays[i_5] = buffer.getShort();

            frames = new int[frameCount];
            for (i_5 = 0; i_5 < frameCount; i_5++)
                frames[i_5] = buffer.getShort();
            for (i_5 = 0; i_5 < frameCount; i_5++)
                frames[i_5] += buffer.getShort() << 16;
        } else if (opcode == 2)
            loopDelay = buffer.getShort();
        else if (opcode == 3) {
            int count = buffer.getByte();
            animationFlowControl = new int[count + 1];

            for (i_5 = 0; i_5 < count; i_5++)
                animationFlowControl[i_5] = buffer.getByte();

            animationFlowControl[count] = 9999999;
        } else if (opcode == 4)
            aBoolean358 = true;
        else if (opcode == 5)
            anInt359 = buffer.getByte();
        else if (opcode == 6)
            anInt360 = buffer.getShort();
        else if (opcode == 7)
            itemId1 = buffer.getShort();
        else if (opcode == 8)
            anInt362 = buffer.getByte();
        else if (opcode == 9)
            anInt363 = buffer.getByte();
        else if (opcode == 10)
            anInt364 = buffer.getByte();
        else if (opcode == 11)
            anInt365 = buffer.getByte();
        else if (opcode == 12) {
            int i_4 = buffer.getByte();
            intArray95 = new int[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++)
                intArray95[i_5] = buffer.getShort();

            for (i_5 = 0; i_5 < i_4; i_5++)
                intArray95[i_5] += buffer.getShort() << 16;
        } else if (opcode == 13) {
            int i_4 = buffer.getByte();
            intArray97 = new int[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++)
                intArray97[i_5] = buffer.getTri();
        }

    }

    public void readValues498(RSStream stream)
    {
        do {
            int i = stream.getByte();
            if(i == 0)
                break;
            if(i == 1) {
                frameCount = stream.getShort();
                frames = new int[frameCount];
                modelFrames = new int[frameCount];
                delays = new int[frameCount];
                for(int i_ = 0; i_ < frameCount; i_++){
                    frames[i_] = stream.getInt();
                    modelFrames[i_] = -1;
                }
                for(int i_ = 0; i_ < frameCount; i_++)
                    delays[i_] = stream.getByte();
            }
            else if(i == 2)
                loopDelay = stream.getShort();
            else if(i == 3) {
                int k = stream.getByte();
                animationFlowControl = new int[k + 1];
                for(int l = 0; l < k; l++)
                    animationFlowControl[l] = stream.getByte();
                animationFlowControl[k] = 0x98967f;
            }
            else if(i == 4)
                aBoolean358 = true;
            else if(i == 5)
                anInt359 = stream.getByte();
            else if(i == 6)
                anInt360 = stream.getShort();
            else if(i == 7)
                itemId1 = stream.getShort();
            else if(i == 8)
                anInt362 = stream.getByte();
            else if(i == 9)
                anInt363 = stream.getByte();
            else if(i == 10)
                anInt364 = stream.getByte();
            else if(i == 11)
                anInt365 = stream.getByte();
            else
                System.out.println("Unrecognized seq.dat config code: "+i);
        } while(true);
        if(frameCount == 0)
        {
            frameCount = 1;
            frames = new int[1];
            frames[0] = -1;
            modelFrames = new int[1];
            modelFrames[0] = -1;
            delays = new int[1];
            delays[0] = -1;
        }
        if(anInt363 == -1)
            if(animationFlowControl != null)
                anInt363 = 2;
            else
                anInt363 = 0;
        if(anInt364 == -1)
        {
            if(animationFlowControl != null)
            {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }

    void post() {
        if (anInt364 == -1)
            if (animationFlowControl != null)
                anInt364 = 2;
            else
                anInt364 = 0;

        if (anInt364 == -1)
            if (animationFlowControl != null)
                anInt364 = 2;
            else
                anInt364 = 0;

    }

    public void readValues(RSStream stream) {
        int[] opcodeHistory = new int[5];
        do {
            int opcode = stream.getByte();
            if (opcode == 0) {
                break;
            }
            switch (opcode) {
                case 1:
                    frameCount = stream.getShort();
                    frames = new int[frameCount];
                    modelFrames = new int[frameCount];
                    delays = new int[frameCount];
                    for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                        frames[frameIndex] = stream.getInt();
                        modelFrames[frameIndex] = -1;
                    }
                    for (int delayIndex = 0; delayIndex < frameCount; delayIndex++) {
                        delays[delayIndex] = stream.getByte();
                    }
                    break;

                case 2:
                    loopDelay = stream.getShort();
                    break;//ask logan?
                case 3:
                    int flowCount = stream.getByte();
                    animationFlowControl = new int[flowCount + 1];
                    for (int flowIndex = 0; flowIndex < flowCount; flowIndex++) {
                        animationFlowControl[flowIndex] = stream.getByte();
                    }
                    animationFlowControl[flowCount] = 0x98967f;
                    break;
                case 4:
                    aBoolean358 = true;
                    break;
                case 5:
                    anInt359 = stream.getByte();
                    break;
                case 6:
                    anInt360 = stream.getShort();
                    break;
                case 7:
                    itemId1 = stream.getShort();
                    break;
                case 8:
                    anInt362 = stream.getByte();
                    break;
                case 9:
                    anInt363 = stream.getByte();
                    break;
                case 10:
                    anInt364 = stream.getByte();
                    break;
                case 11:
                    anInt365 = stream.getByte();
                    break;
                case 12:
                    osrs = true;
                    break;
                default:
                    if (Constants.DEBUG_MODE) {
                        logger.log(Level.WARNING, "Unknown animation opcode: {0}, history: {1}", new Object[]{opcode, Arrays.toString(opcodeHistory)});
                    }
                    break;
            }
            System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
            opcodeHistory[0] = opcode;
        } while (true);
        if (frameCount == 0) {
            frameCount = 1;
            frames = new int[1];
            frames[0] = -1;
            modelFrames = new int[1];
            modelFrames[0] = -1;
            delays = new int[1];
            delays[0] = -1;
        }
        if (anInt363 == -1) {
            if (animationFlowControl != null) {
                anInt363 = 2;
            } else {
                anInt363 = 0;
            }
        }
        if (anInt364 == -1) {
            if (animationFlowControl != null) {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }
}