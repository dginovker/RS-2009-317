package com.runescape.media.renderable.entity;

import com.runescape.Game;
import com.runescape.cache.def.*;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.def.object.ObjectDefinition;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.Animation;
import com.runescape.media.renderable.Model;
import com.runescape.net.RSStream;
import com.runescape.util.StringUtility;

public final class Player extends Entity {

    public static ReferenceCache models = new ReferenceCache(260);
    public final int[] anIntArray1700;
    public final int[] equipment;
    public int privelage;
    public NPCDefinition playerNPCDefinition;
    public ItemDefinition playerItemDefinition;
    public ObjectDefinition playerObjectDefinition;
    public boolean aBoolean1699;
    public int team;
    public String name;
    public int combatLevel;
    public boolean wilderness;
    public int summoningLevel;
    public int headIcon;
    public int skullIcon;
    public int hintIcon;
    public int anInt1707;
    public int anInt1708;
    public int anInt1709;
    public boolean visible;
    public int anInt1711;
    public int anInt1712;
    public int anInt1713;
    public Model playerModel;
    public int anInt1719;
    public int anInt1720;
    public int anInt1721;
    public int anInt1722;
    public int skill;
    public int pidn;
    private long aLong1697;
    private String title;
    private boolean titleSuffix;
    private int gender;
    private long aLong1718;

    public Player() {
        aLong1697 = -1L;
        title = "";
        aBoolean1699 = false;
        anIntArray1700 = new int[5];
        visible = false;
        equipment = new int[12];
    }

    @Override
    public Model getRotatedModel() {
        if (!visible) {
            return null;
        }
        Model model = prepareAnimatedModel();
        if (model == null) {
            return null;
        }
        super.height = model.modelHeight;
        model.fitsOnSingleSquare = true;
        if (aBoolean1699) {
            return model;
        }
        if (super.gfxId != -1 && super.currentAnimation != -1) {
            SpotAnimation spotAnim = SpotAnimation.cache[super.gfxId];
            Model model_2 = spotAnim.getModel();
            if (model_2 != null) {
                Model graphicModel = new Model(true, SequenceFrame.method532(super.currentAnimation), false, model_2);
                int nextFrame = spotAnim.animationSequence.frames[super.nextGraphicsAnimationFrame];
                int cycle1 = spotAnim.animationSequence.delays[super.currentAnimation];
                int cycle2 = super.anInt1522;
                graphicModel.translate(0, -super.graphicHeight, 0);
                graphicModel.skin();
                graphicModel.method470(spotAnim.animationSequence.frames[super.currentAnimation], nextFrame, cycle1, cycle2, spotAnim.animationSequence.osrs);
                graphicModel.faceGroups = null;
                graphicModel.vertexGroups = null;
                if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128) {
                    graphicModel.scale(spotAnim.resizeXY, spotAnim.resizeXY, spotAnim.resizeZ);
                }
                graphicModel.light(64 + spotAnim.modelBrightness, 850 + spotAnim.modelShadow, -30, -50, -30, true);
                model = new Model(new Model[]{model, graphicModel});
            } else {
                return null;
            }
        }
        if (playerModel != null) {
            if (Game.loopCycle >= anInt1708) {
                playerModel = null;
            }
            if (Game.loopCycle >= anInt1707 && Game.loopCycle < anInt1708) {
                Model model_1 = playerModel;
                model_1.translate(anInt1711 - super.x, anInt1712 - anInt1709, anInt1713 - super.y);
                if (super.turnDirection == 512) {
                    model_1.method473();
                    model_1.method473();
                    model_1.method473();
                } else if (super.turnDirection == 1024) {
                    model_1.method473();
                    model_1.method473();
                } else if (super.turnDirection == 1536) {
                    model_1.method473();
                }
                Model aclass30_sub2_sub4_sub6s[] = {
                        model, model_1
                };
                model = new Model(aclass30_sub2_sub4_sub6s);
                if (super.turnDirection == 512) {
                    model_1.method473();
                } else if (super.turnDirection == 1024) {
                    model_1.method473();
                    model_1.method473();
                } else if (super.turnDirection == 1536) {
                    model_1.method473();
                    model_1.method473();
                    model_1.method473();
                }
                model_1.translate(super.x - anInt1711, anInt1709 - anInt1712, super.y - anInt1713);
            }
        }
        model.fitsOnSingleSquare = true;
        return model;
    }

    public void updatePlayer(RSStream stream) {
        stream.currentPosition = 0;
        gender = stream.getByte();
        headIcon = stream.getByte();
        skullIcon = stream.getByte();
        hintIcon = stream.getByte();
        playerNPCDefinition = null;
        playerItemDefinition = null;
        playerObjectDefinition = null;
        team = 0;
        for (int j = 0; j < 12; j++) {
            int k = stream.getByte();
            if (k == 0) {
                equipment[j] = 0;
                continue;
            }
            int i1 = stream.getByte();
            equipment[j] = (k << 8) + i1;
            if (j == 0 && equipment[0] >= 65533 && equipment[0] <= 65535) {
                if (name == null) {
                    playerNPCDefinition = NPCDefinition.forId(1);
                } else {
                    switch (equipment[0]) {
                        case 65533:
                            playerObjectDefinition = ObjectDefinition.forId(stream.getShort());
                            break;
                        case 65534:
                            playerItemDefinition = ItemDefinition.forId(stream.getShort());
                            break;
                        case 65535:
                            playerNPCDefinition = NPCDefinition.forId(stream.getShort());
                            break;
                        default:
                            equipment[0] = 0;
                            break;
                    }
                }
                break;
            }
            if (equipment[j] >= 512 && equipment[j] - 512 < ItemDefinition.getItemCount()) {
                int l1 = ItemDefinition.forId(equipment[j] - 512).team;
                if (l1 != 0) {
                    team = l1;
                }
            }
        }

        for (int l = 0; l < 5; l++) {
            int j1 = stream.getByte();
            if (j1 < 0 || j1 >= Game.anIntArrayArray1003[l].length) {
                j1 = 0;
            }
            anIntArray1700[l] = j1;
        }

        super.standAnimIndex = stream.getShort();
        if (super.standAnimIndex == 65535) {
            super.standAnimIndex = -1;
        }
        super.standTurnAnimIndex = stream.getShort();
        if (super.standTurnAnimIndex == 65535) {
            super.standTurnAnimIndex = -1;
        }
        super.walkAnimIndex = stream.getShort();
        if (super.walkAnimIndex == 65535) {
            super.walkAnimIndex = -1;
        }
        super.turn180AnimIndex = stream.getShort();
        if (super.turn180AnimIndex == 65535) {
            super.turn180AnimIndex = -1;
        }
        super.turn90CWAnimIndex = stream.getShort();
        if (super.turn90CWAnimIndex == 65535) {
            super.turn90CWAnimIndex = -1;
        }
        super.turn90CCWAnimIndex = stream.getShort();
        if (super.turn90CCWAnimIndex == 65535) {
            super.turn90CCWAnimIndex = -1;
        }
        super.runAnimIndex = stream.getShort();
        if (super.runAnimIndex == 65535) {
            super.runAnimIndex = -1;
        }
        name = StringUtility.formatUsername(StringUtility.decodeBase37(stream.getLong()));
        boolean hasTitle = stream.getByte() == 1;
        titleSuffix = hasTitle && (stream.getByte() == 1);
        if (hasTitle) {
            String title = stream.getString();
            String titleColor = StringUtility.decodeBase37(stream.getLong());
            this.title = "<col=" + titleColor + ">" + title + "</col>";
        } else {
            this.title = "";
        }
        combatLevel = stream.getByte();
        wilderness = stream.getByteA() == 1;
        if (wilderness) {
            summoningLevel = (combatLevel - 126);
            if (summoningLevel > 0) {
                combatLevel = (combatLevel - summoningLevel);
            } else {
                summoningLevel = 0;
            }
        } else {
            summoningLevel = 0;
        }
        skill = stream.getShort();
        visible = true;
        aLong1718 = 0L;
        for (int k1 = 0; k1 < 12; k1++) {
            aLong1718 <<= 4;
            if (equipment[k1] >= 256) {
                aLong1718 += equipment[k1] - 256;
            }
        }

        if (equipment[0] >= 256) {
            aLong1718 += equipment[0] - 256 >> 4;
        }
        if (equipment[1] >= 256) {
            aLong1718 += equipment[1] - 256 >> 8;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            aLong1718 <<= 3;
            aLong1718 += anIntArray1700[i2];
        }

        aLong1718 <<= 1;
        aLong1718 += gender;
    }

    public Model prepareAnimatedModel() {
        if (playerObjectDefinition != null) {
            if (super.emoteAnimation == 1) {
                return playerObjectDefinition.model(10, -1, 0);
            }
            int currentFrame = -1;
            int nextFrame = -1;
            int cycle1 = 0;
            int cycle2 = 0;
            if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
                Animation animation = Animation.animations[super.emoteAnimation];
                currentFrame = animation.frames[super.displayedEmoteFrames];
                nextFrame = animation.frames[super.nextAnimationFrame];
                cycle1 = animation.delays[super.displayedEmoteFrames];
                cycle2 = super.emoteTimeRemaining;
            } else if (super.movementAnimation >= 0) {
                Animation animation = Animation.animations[super.movementAnimation];
                currentFrame = animation.frames[super.displayedMovementFrames];
                nextFrame = animation.frames[super.nextIdleAnimationFrame];
                cycle1 = animation.delays[super.displayedMovementFrames];
                cycle2 = super.anInt1519;
            }
            return playerObjectDefinition.modelAt(10, 0, currentFrame, 0, cycle1, cycle2, nextFrame);
        }
        if (playerItemDefinition != null) {
            return playerItemDefinition.getModel(1);
        }
        if (playerNPCDefinition != null) {
            int currentFrame = -1;
            int nextFrame = -1;
            int cycle1 = 0;
            int cycle2 = 0;
            if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
                Animation animation = Animation.animations[super.emoteAnimation];
                currentFrame = animation.frames[super.displayedEmoteFrames];
                nextFrame = animation.frames[super.nextAnimationFrame];
                cycle1 = animation.delays[super.displayedEmoteFrames];
                cycle2 = super.emoteTimeRemaining;
            } else if (super.movementAnimation >= 0) {
                Animation animation = Animation.animations[super.movementAnimation];
                currentFrame = animation.frames[super.displayedMovementFrames];
                nextFrame = animation.frames[super.nextIdleAnimationFrame];
                cycle1 = animation.delays[super.displayedMovementFrames];
                cycle2 = super.anInt1519;
            }
            Model model = playerNPCDefinition.modelAt(-1, currentFrame, null, nextFrame, cycle1, cycle2);
            return model;
        }
        long l = aLong1718;
        int currentFrame = -1;
        int nextFrame = -1;
        int cycle1 = 0;
        int cycle2 = 0;
        int unknownInt1 = -1;
        int j1 = -1;
        int animationItemId1 = -1;
        if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
            Animation animation = Animation.animations[super.emoteAnimation];
            currentFrame = animation.frames[super.displayedEmoteFrames];
            nextFrame = animation.frames[super.nextAnimationFrame];
            cycle1 = animation.delays[super.displayedEmoteFrames];
            cycle2 = super.emoteTimeRemaining;
            if (super.movementAnimation >= 0 && super.movementAnimation != super.standAnimIndex) {
                unknownInt1 = Animation.animations[super.movementAnimation].frames[super.displayedMovementFrames];
            }
            if (animation.anInt360 >= 0) {
                j1 = animation.anInt360;
                l += j1 - equipment[5] << 8;
            }
            if (animation.itemId1 >= 0) {
                animationItemId1 = animation.itemId1;
                l += animationItemId1 - equipment[3] << 16;
            }
        } else if (super.movementAnimation >= 0) {
            Animation animation = Animation.animations[super.movementAnimation];
            currentFrame = animation.frames[super.displayedMovementFrames];
            nextFrame = animation.frames[super.nextIdleAnimationFrame];
            cycle1 = animation.delays[super.displayedMovementFrames];
            cycle2 = super.anInt1519;
        }
        Model model1 = (Model) models.get(l);
        if (model1 == null) {
            boolean flag = false;
            for (int equipmentSlot = 0; equipmentSlot < 12; equipmentSlot++) {
                int equipmentId = equipment[equipmentSlot];
                if (animationItemId1 >= 0 && equipmentSlot == 3) {
                    equipmentId = animationItemId1;
                }
                if (j1 >= 0 && equipmentSlot == 5) {
                    equipmentId = j1;
                }
                if (equipmentId >= 256 && equipmentId < 512 && !IdentityKit.kits[equipmentId - 256].bodyLoaded()) {
                    flag = true;
                }
                if (equipmentId >= 512 && !ItemDefinition.forId(equipmentId - 512).isEquippedModelCached(gender)) {
                    flag = true;
                }
            }

            if (flag) {
                if (aLong1697 != -1L) {
                    model1 = (Model) models.get(aLong1697);
                }
                if (model1 == null) {
                    return null;
                }
            }
        }
        if (model1 == null) {
            Model models1[] = new Model[14];
            int j2 = 0;
            for (int l2 = 0; l2 < 12; l2++) {
                int i3 = equipment[l2];
                if (animationItemId1 >= 0 && l2 == 3) {
                    i3 = animationItemId1;
                }
                if (j1 >= 0 && l2 == 5) {
                    i3 = j1;
                }
                if (i3 >= 256 && i3 < 512) {
                    Model model_3 = IdentityKit.kits[i3 - 256].bodyModel();
                    if (model_3 != null) {
                        models1[j2++] = model_3;
                    }
                }
                if (i3 >= 512) {
                    Model model_4 = ItemDefinition.forId(i3 - 512).getEquippedModel(gender);
                    if (model_4 != null) {
                        models1[j2++] = model_4;
                    }
                }
            }
            model1 = new Model(j2, models1);
            for (int j3 = 0; j3 < 5; j3++) {
                if (anIntArray1700[j3] != 0) {
                    model1.recolor(Game.anIntArrayArray1003[j3][0], Game.anIntArrayArray1003[j3][anIntArray1700[j3]]);
                    if (j3 == 1) {
                        model1.recolor(Game.anIntArray1204[0], Game.anIntArray1204[anIntArray1700[j3]]);
                    }
                }
            }

            model1.skin();
            model1.scale(132, 132, 132);
            //model1.light(84, 1000, -90, -580, -90, true);
            model1.light(72, 1300, -30, -50, -30, true);
            models.put(model1, l);
            aLong1697 = l;
        }
        if (aBoolean1699) {
            return model1;
        }
        Model model_2 = Model.EMPTY_MODEL;
        model_2.method464(model1, SequenceFrame.method532(currentFrame) & SequenceFrame.method532(unknownInt1));
        if (currentFrame != -1 && unknownInt1 != -1) {
            model_2.method471(Animation.animations[super.emoteAnimation].animationFlowControl, unknownInt1, currentFrame, Animation.animations[super.emoteAnimation].osrs);
        } else if (currentFrame != -1 && nextFrame != -1) {
            boolean osrs = false;
            if (super.emoteAnimation != -1) {
                osrs = Animation.animations[super.emoteAnimation].osrs;
            }
            model_2.method470(currentFrame, nextFrame, cycle1, cycle2, osrs);
        } else {
            model_2.apply(currentFrame, currentFrame >= 0 ? Animation.animations[currentFrame].osrs : false);
        }
        model_2.method466();
        model_2.faceGroups = null;
        model_2.vertexGroups = null;
        return model_2;
    }

    public boolean isVisible() {
        return visible;
    }

    public Model getComponentModel() {
        if (!visible) {
            return null;
        }
        if (playerNPCDefinition != null) {
            return playerNPCDefinition.model();
        }
        if (playerItemDefinition != null) {
            return playerItemDefinition.getModel(1);
        }
        if (playerObjectDefinition != null) {
            return playerObjectDefinition.model(10, -1, 0);// face location = 0 ?
        }

        boolean flag = false;
        for (int i = 0; i < 12; i++) {
            int j = equipment[i];
            if (j >= 256 && j < 512 && !IdentityKit.kits[j - 256].headLoaded()) {
                flag = true;
            }
            if (j >= 512 && !ItemDefinition.forId(j - 512).isDialogueModelCached(gender)) {
                flag = true;
            }
        }

        if (flag) {
            return null;
        }
        Model bodyModels[] = new Model[12];
        int k = 0;
        for (int l = 0; l < 12; l++) {
            int i1 = equipment[l];
            if (i1 >= 256 && i1 < 512) {
                Model headModel = IdentityKit.kits[i1 - 256].headModel();
                if (headModel != null) {
                    bodyModels[k++] = headModel;
                }
            }
            if (i1 >= 512) {
                Model model_2 = ItemDefinition.forId(i1 - 512).getChatEquipModel(gender);
                if (model_2 != null) {
                    bodyModels[k++] = model_2;
                }
            }
        }

        Model model = new Model(k, bodyModels);
        for (int j1 = 0; j1 < 5; j1++) {
            if (anIntArray1700[j1] != 0) {
                model.recolor(Game.anIntArrayArray1003[j1][0], Game.anIntArrayArray1003[j1][anIntArray1700[j1]]);
                if (j1 == 1) {
                    model.recolor(Game.anIntArray1204[0], Game.anIntArray1204[anIntArray1700[j1]]);
                }
            }
        }
        return model;
    }

    /**
     * Gets the player's title.
     *
     * @return The title.
     */
    public String getTitle() {
        return (title == null || title.isEmpty()) ? "" : (titleSuffix ? (" " + title) : (title + " "));
    }

    /**
     * Gets whether or not the player's title is a suffix.
     *
     * @return {@code True} if so.
     */
    public boolean isTitleSuffix() {
        return titleSuffix;
    }

    /**
     * Gets the player's identification number.
     *
     * @return The pidn.
     */
    public int getPidn() {
        return pidn;
    }
}
