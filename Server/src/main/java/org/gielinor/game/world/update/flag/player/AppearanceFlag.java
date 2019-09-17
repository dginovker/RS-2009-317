package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Appearance;
import org.gielinor.game.node.entity.player.link.appearance.BodyPart;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles the appearance update flag.
 *
 * @author Emperor
 */
public final class AppearanceFlag extends UpdateFlag<Player> {

    /**
     * Constructs a new {@code AppearanceFlag} {@code Object}.
     *
     * @param player The player.
     */
    public AppearanceFlag(Player player) {
        super(player);
    }

    @Override
    public void write(PacketBuilder buffer) {
        Equipment equipment = context.getEquipment();
        Appearance appearance = context.getAppearance();
        appearance.prepareBodyData(context);
        Item chest = context.getEquipment().get(Equipment.SLOT_CHEST);
        Item shield = context.getEquipment().get(Equipment.SLOT_SHIELD);
        Item legs = context.getEquipment().get(Equipment.SLOT_LEGS);
        Item hat = context.getEquipment().get(Equipment.SLOT_HAT);
        Item hands = context.getEquipment().get(Equipment.SLOT_HANDS);
        Item feet = context.getEquipment().get(Equipment.SLOT_FEET);
        Item cape = context.getEquipment().get(Equipment.SLOT_CAPE);
        Item amulet = context.getEquipment().get(Equipment.SLOT_AMULET);
        Item weapon = context.getEquipment().get(Equipment.SLOT_WEAPON);
        boolean castleWarsHood = cape != null && (cape.getId() == 4041 || cape.getId() == 4042);

        PacketBuilder playerProps = new PacketBuilder();
        playerProps.put((byte) appearance.getGender().ordinal());
        playerProps.put((byte) appearance.getHeadIcon());
        playerProps.put((byte) appearance.getSkullIcon());
        playerProps.put((byte) context.getAttribute("hint_icon", (byte) -1)); // TODO Hint icon
        int npcId = appearance.getNpcId();
        int itemId = appearance.getItemId();
        int objectId = appearance.getObjectId();
        if (appearance.isNpc() || appearance.isItem() || appearance.isObject()) {
            playerProps.putShort(appearance.isNpc() ? -1 : appearance.isItem() ? -2 : appearance.isObject() ? -3 : -1);
            playerProps.putShort(appearance.isNpc() ? npcId : appearance.isItem() ? itemId : appearance.isObject() ? objectId : 1);
        } else {
            for (int slotIndex = 0; slotIndex < 4; slotIndex++) {
                if (equipment.isSlotUsed(slotIndex)) {
                    playerProps.putShort((short) 0x200 + equipment.get(slotIndex).getId());
                } else {
                    playerProps.put((byte) 0);
                }
            }
            if (chest != null) {
                playerProps.putShort((short) 0x200 + chest.getId());
            } else {
                playerProps.putShort((short) 0x100 + appearance.getTorso().getLook());
            }
            if (shield != null) {
                playerProps.putShort((short) 0x200 + shield.getId());
            } else {
                playerProps.put((byte) 0);
            }
            if (chest != null && chest.getDefinition().getConfiguration(ItemConfiguration.REMOVE_SLEEVES, false)) {
                playerProps.putShort((short) 0x200 + chest.getId());
            } else {
                playerProps.putShort((short) 0x100 + appearance.getArms().getLook());
            }
            if (legs != null) {
                playerProps.putShort((short) 0x200 + legs.getId());
            } else {
                playerProps.putShort((short) 0x100 + appearance.getLegs().getLook());
            }
            if ((hat != null && hat.getDefinition().getConfiguration(ItemConfiguration.REMOVE_HEAD, false)) || castleWarsHood) {
                playerProps.put((byte) 0);
            } else {
                playerProps.putShort((short) 0x100 + appearance.getHair().getLook());
            }
            if (hands != null) {
                playerProps.putShort((short) 0x200 + hands.getId());
            } else {
                playerProps.putShort((short) 0x100 + appearance.getWrists().getLook());
            }
            if (feet != null) {
                playerProps.putShort((short) 0x200 + feet.getId());
            } else {
                playerProps.putShort((short) 0x100 + appearance.getFeet().getLook());
            }
            if (appearance.getGender() == Gender.FEMALE || (hat != null && hat.getDefinition().getConfiguration(ItemConfiguration.REMOVE_BEARD, false))) {
                playerProps.put((byte) 0);
            } else {
                playerProps.putShort((short) 0x100 + appearance.getBeard().getLook());
            }
        }

        final BodyPart[] colours = new BodyPart[]{ appearance.getHair(), appearance.getTorso(), appearance.getLegs(), appearance.getFeet(), appearance.getSkin() };
        for (BodyPart colour : colours) { //colours
            playerProps.put(colour.getColour());
        }

        playerProps.putShort(appearance.getStandAnimation());
        playerProps.putShort(appearance.getStandTurnAnimation());
        playerProps.putShort(appearance.getWalkAnimation());
        playerProps.putShort(appearance.getTurn180());
        playerProps.putShort(appearance.getTurn90cw());
        playerProps.putShort(appearance.getTurn90ccw());
        playerProps.putShort(appearance.getRunAnimation());

        playerProps.putLong(TextUtils.stringToLong(context.getUsername())); // player name

        if (Constants.PLAYER_TITLES) {
            boolean title = context.getTitleManager().hasTitle();
            playerProps.put((byte) (title ? 1 : 0));
            if (title) {
                playerProps.put((byte) (context.getTitleManager().getLoyaltyTitle().isSuffix() ? 1 : 0));
                String name = context.getTitleManager().getTitleName();
                String color = context.getTitleManager().getLoyaltyTitle().getColour();
                playerProps.putString(name);
                playerProps.putLong(TextUtils.stringToLong(color));
            }
        }

        playerProps.put((byte) context.getProperties().getCurrentCombatLevel());
        playerProps.putA((byte) (context.getSkullManager().isWilderness() ? 1 : 0));
        playerProps.putShort(0);
        buffer.putC(playerProps.toByteBuffer().position());
        buffer.put(playerProps);
    }

    @Override
    public int data() {
        return getData();
    }

    @Override
    public int ordinal() {
        return getOrdinal();
    }

    /**
     * Gets the ordinal for this flag.
     *
     * @return The flag ordinal.
     */
    public static int getOrdinal() {
        return 6;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int getData() {
        return 0x10;
    }
}
