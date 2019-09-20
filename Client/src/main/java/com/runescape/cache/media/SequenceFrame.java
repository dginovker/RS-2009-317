package com.runescape.cache.media;

import com.runescape.Constants;
import main.java.com.runescape.Game;
import com.runescape.media.SkinList;
import com.runescape.net.RSStream;

public final class SequenceFrame {
    public static SequenceFrame[][] animationDataList;
    public static SequenceFrame[][] osrsDataList;
    public int anInt636;
    public SkinList skinList;
    public int anInt638;
    public int[] anIntArray639;
    public int[] anIntArray640;
    public int[] anIntArray641;
    public int[] anIntArray642;

    public SequenceFrame() {
    }

    public static void method528(int i) {
        animationDataList = new SequenceFrame[3783][0];
        osrsDataList = new SequenceFrame[5000][0];
    }

    public static void loadOSRSAnimation(int animationId, byte[] animationData) {
        try {
            RSStream rsStream = new RSStream(animationData);
            SkinList skinList = new SkinList(rsStream);
            final int frameLength = rsStream.getShort();
            osrsDataList[animationId] = new SequenceFrame[frameLength * 3];
            final int[] array2 = new int[500];
            final int[] array3 = new int[500];
            final int[] array4 = new int[500];
            final int[] array5 = new int[500];
            for (int frameIndex = 0; frameIndex < frameLength; ++frameIndex) {
                final int k = rsStream.getShort();
                final SequenceFrame[] sequenceFrames = osrsDataList[animationId];
                final int n2 = k;
                final SequenceFrame sequenceFrame = new SequenceFrame();
                sequenceFrames[n2] = sequenceFrame;
                final SequenceFrame q2 = sequenceFrame;
                sequenceFrame.skinList = skinList;
                final int f = rsStream.getByte();
                int c2 = 0;
                int n3 = -1;
                for (int l = 0; l < f; ++l) {
                    final int f2;
                    if ((f2 = rsStream.getByte()) > 0) {
                        if (skinList.anIntArray342[l] != 0) {
                            for (int n4 = l - 1; n4 > n3; --n4) {
                                if (skinList.anIntArray342[n4] == 0) {
                                    array2[c2] = n4;
                                    array3[c2] = 0;
                                    array5[c2] = (array4[c2] = 0);
                                    ++c2;
                                    break;
                                }
                            }
                        }
                        array2[c2] = l;
                        int n4 = 0;
                        if (skinList.anIntArray342[l] == 3) {
                            n4 = 128;
                        }
                        if ((f2 & 0x1) != 0x0) {
                            array3[c2] = rsStream.readShort2();
                        } else {
                            array3[c2] = n4;
                        }
                        if ((f2 & 0x2) != 0x0) {
                            array4[c2] = rsStream.readShort2();
                        } else {
                            array4[c2] = n4;
                        }
                        if ((f2 & 0x4) != 0x0) {
                            array5[c2] = rsStream.readShort2();
                        } else {
                            array5[c2] = n4;
                        }
                        n3 = l;
                        ++c2;
                    }
                }
                q2.anInt638 = c2;
                q2.anIntArray639 = new int[c2];
                q2.anIntArray640 = new int[c2];
                q2.anIntArray641 = new int[c2];
                q2.anIntArray642 = new int[c2];
                for (int l = 0; l < c2; ++l) {
                    q2.anIntArray639[l] = array2[l];
                    q2.anIntArray640[l] = array3[l];
                    q2.anIntArray641[l] = array4[l];
                    q2.anIntArray642[l] = array5[l];
                }
            }
        } catch (Exception ex) {
        }
    }

    public static void loadAnimationData(int animationFileId, boolean osrs, byte[] animationData){
        try {
            if (animationData == null) {
                Game.INSTANCE.resourceProvider.provide(osrs ? Constants.ANIMATION_INDEX
                        : (Constants.NEW_ANIMATION_INDEX - 1), animationFileId);
                return;
            }
            if (osrs) {
                loadOSRSAnimation(animationFileId, animationData);
                return;
            }
            RSStream stream = new RSStream(animationData);
            SkinList class18 = new SkinList(stream);
            int k1 = stream.getShort();
            animationDataList[animationFileId] = new SequenceFrame[(int)(k1*1.5)];
            int ai[] = new int[500];
            int ai1[] = new int[500];
            int ai2[] = new int[500];
            int ai3[] = new int[500];
            for(int l1 = 0; l1 < k1; l1++)
            {
                int i2 = stream.getShort();
                SequenceFrame class36 = animationDataList[animationFileId][i2] = new SequenceFrame();
                class36.skinList = class18;
                int j2 = stream.getByte();
                int l2 = 0;
                int k2 = -1;
                for(int i3 = 0; i3 < j2; i3++)
                {
                    int j3 = stream.getByte();

                    if(j3 > 0)
                    {
                        if(class18.anIntArray342[i3] != 0)
                        {
                            for(int l3 = i3 - 1; l3 > k2; l3--)
                            {
                                if(class18.anIntArray342[l3] != 0)
                                    continue;
                                ai[l2] = l3;
                                ai1[l2] = 0;
                                ai2[l2] = 0;
                                ai3[l2] = 0;
                                l2++;
                                break;
                            }

                        }
                        ai[l2] = i3;
                        short c = 0;
                        if(class18.anIntArray342[i3] == 3)
                            c = (short)128;

                        if((j3 & 1) != 0)
                            ai1[l2] = (short)stream.readShort2();
                        else
                            ai1[l2] = c;
                        if((j3 & 2) != 0)
                            ai2[l2] = stream.readShort2();
                        else
                            ai2[l2] = c;
                        if((j3 & 4) != 0)
                            ai3[l2] = stream.readShort2();
                        else
                            ai3[l2] = c;
                        k2 = i3;
                        l2++;
                    }
                }

                class36.anInt638 = l2;
                class36.anIntArray639 = new int[l2];
                class36.anIntArray640 = new int[l2];
                class36.anIntArray641 = new int[l2];
                class36.anIntArray642 = new int[l2];
                for(int k3 = 0; k3 < l2; k3++)
                {
                    class36.anIntArray639[k3] = ai[k3];
                    class36.anIntArray640[k3] = ai1[k3];
                    class36.anIntArray641[k3] = ai2[k3];
                    class36.anIntArray642[k3] = ai3[k3];
                }

            }
        }catch(Exception exception) { }
    }
    public static void nullLoader() {
        animationDataList = null;
        osrsDataList = null;
    }

    public static SequenceFrame fetchFileInfo(int frameId, boolean osrs) {
        try {
            String hexString;
            int animationFileId = Integer.parseInt((hexString = Integer.toHexString(frameId)).substring(0, hexString.length() - 4), 16);
            frameId = Integer.parseInt(hexString.substring(hexString.length() - 4), 16);
            if (osrs) {
                if (osrsDataList[animationFileId].length == 0) {
                    Game.INSTANCE.resourceProvider.provide((Constants.ANIMATION_INDEX), animationFileId);
                    return null;
                }
                return osrsDataList[animationFileId][frameId];
            }
            if (animationDataList[animationFileId].length == 0) {
                Game.INSTANCE.resourceProvider.provide(Constants.NEW_ANIMATION_INDEX - 1, animationFileId);
                return null;
            }
            return animationDataList[animationFileId][frameId];
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean method532(int i) {
        return i == -1;
    }
}
