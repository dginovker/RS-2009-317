package org.gielinor.parser.misc;

import org.gielinor.constants.ToxicBlowpipeConstants;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.Ammunition;
import org.gielinor.game.node.entity.combat.equipment.RangeWeapon;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.parser.Parser;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the range data.
 *
 * @author Emperor
 */
public final class RangeDataParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        RandomAccessFile raf = new RandomAccessFile(new File("data/world/node/item/range_data.config498"), "r");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buf = channel.map(MapMode.READ_ONLY, 0, channel.size());
        int itemId;
        // TODO DUMP, Attack speed NOT needed
        while ((itemId = buf.getShort()) != -1) {
            int slot = buf.get();
            int type = buf.get();
            int anim = buf.getShort();
            boolean drop = buf.get() == 1;
            int size = buf.get();
            List<Integer> ammunition = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ammunition.add((int) buf.getShort());
            }
            RangeWeapon weapon = new RangeWeapon(itemId, new Animation(anim, Priority.HIGH),
                4, slot, type, drop, ammunition);
            RangeWeapon.getRangeWeapons().put(itemId, weapon);
        }
        while ((itemId = buf.getShort()) != -1) {
            Graphics start = new Graphics(buf.getShort(), buf.get());
            Graphics darkBow = new Graphics(buf.getShort(), buf.get());
            if (darkBow.getId() == -1) {
                darkBow = null;
            }
            Projectile projectile = Projectile.create((Entity) null, null, buf.getShort(), buf.get(), buf.get(), buf.get(), buf.get(), buf.get(), buf.get());
            int poisonDamage = buf.get();
            Ammunition ammunition = new Ammunition(itemId, start, darkBow, projectile, poisonDamage);
            Ammunition.getAmmunition().put(itemId, ammunition);
        }
        raf.close();
        channel.close();
        RangeWeapon rangeWeapon = new RangeWeapon(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED, new Animation(21208), 11, 3, 4, false, new ArrayList<>());
        RangeWeapon.getRangeWeapons().put(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED, rangeWeapon);
        return true;
    }
}
