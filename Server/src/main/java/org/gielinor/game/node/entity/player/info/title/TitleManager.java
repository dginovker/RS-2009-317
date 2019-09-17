package org.gielinor.game.node.entity.player.info.title;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.parser.player.SavingModule;

/**
 * A player's {@link org.gielinor.game.node.entity.player.info.title.TitleManager}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TitleManager implements SavingModule {

    /**
     * The {@link org.gielinor.game.node.entity.player.Player}.
     */
    private final Player player;
    /**
     * The player's current {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     */
    private LoyaltyTitle loyaltyTitle;
    /**
     * The player's unlocked {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} list.
     */
    private final List<LoyaltyTitle> unlockedTitles = new ArrayList<>();

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.player.info.title.TitleManager}.
     *
     * @param player The player.
     */
    public TitleManager(Player player) {
        this.player = player;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's current {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     *
     * @return The loyalty title.
     */
    public LoyaltyTitle getLoyaltyTitle() {
        return loyaltyTitle;
    }

    /**
     * Gets the name of the {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} for the player's gender.
     *
     * @return The {@link #loyaltyTitle}.
     */
    public String getTitleName() {
        if (!hasTitle()) {
            return "";
        }
        return player.getAppearance().getGender() == Gender.MALE ? getLoyaltyTitle().getMale() :
            (getLoyaltyTitle().getFemale() == null) ? getLoyaltyTitle().getMale() :
                getLoyaltyTitle().getFemale();
    }

    /**
     * Gets whether or not the player has a title.
     *
     * @return <code>True</code> if {@link #loyaltyTitle} is not null.
     */
    public boolean hasTitle() {
        return loyaltyTitle != null;
    }

    /**
     * Sets the player's current {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     *
     * @param loyaltyTitle The {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}.
     */
    public void setLoyaltyTitle(LoyaltyTitle loyaltyTitle) {
        this.loyaltyTitle = loyaltyTitle;
    }

    /**
     * Checks if this {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} is enabled.
     *
     * @param loyaltyTitle The title.
     * @return <code>True</code> if so.
     */
    public boolean isEnabled(LoyaltyTitle loyaltyTitle) {
        return (this.loyaltyTitle != null && loyaltyTitle != null) && loyaltyTitle.getId() == this.loyaltyTitle.getId();
    }

    /**
     * Gets the player's current unlocked {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} list.
     *
     * @return The list.
     */
    public List<LoyaltyTitle> getUnlockedTitles() {
        return unlockedTitles;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.putInt(unlockedTitles.size());
        for (LoyaltyTitle loyaltyTitle : unlockedTitles) {
            byteBuffer.putInt(loyaltyTitle.getId());
            byte titleSelected = (byte) 2;
            if (hasTitle() && loyaltyTitle == this.loyaltyTitle) {
                titleSelected = (byte) 1;
            }
            byteBuffer.put(titleSelected);
        }
        // byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int length = byteBuffer.getInt();
        while (length > 0) {
            LoyaltyTitle loyaltyTitle = LoyaltyTitle.forId(byteBuffer.getInt());
            byte enabled = byteBuffer.get();
            if (loyaltyTitle != null) {
                unlockedTitles.add(loyaltyTitle);
                if (enabled == 1) {
                    setLoyaltyTitle(loyaltyTitle);
                    player.getAppearance().sync();
                }
            }
            length--;
        }
        player.getAppearance().sync();
    }
}
