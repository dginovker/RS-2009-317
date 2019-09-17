package org.gielinor.game.node.entity.player.info.donor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the donation manager for a player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DonorManager implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(DonorManager.class);

    /**
     * The player.
     */
    private final Player player;
    /**
     * The player's
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus}.
     */
    private DonorStatus donorStatus = DonorStatus.NONE;
    /**
     * The player's Gielinor tokens.
     */
    private int gielinorTokens = 0;
    /**
     * Whether or not the player wants their icon hidden.
     */
    private boolean iconHidden;
    /**
     * Represents the credit shop container.
     */
    private final Container creditShopContainer;

    /**
     * Constructs a new
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorManager}.
     *
     * @param player
     *            The player.
     */
    public DonorManager(Player player) {
        this.player = player;
        this.creditShopContainer = new Container(140);
        this.creditShopContainer.register(new StoreListener());
    }

    /**
     * Sets the
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus}.
     *
     * @param donorStatus
     *            The status.
     */
    public void setDonorStatus(DonorStatus donorStatus) {
        this.donorStatus = donorStatus;
    }

    /**
     * Gets the
     * {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus}.
     *
     * @return The donor status.
     */
    public DonorStatus getDonorStatus() {
        return donorStatus;
    }

    public boolean hasMembership() {
        return isRubyMember() || isEmeraldMember() || isSapphireMember() || isDiamondMember() || isDragonstoneMember()
            || isOnyxMember() || isZenyteMember();
    }

    /**
     * Gets whether or not the player is a sapphire member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSapphireMember() {
        return donorStatus == DonorStatus.SAPPHIRE_MEMBER;
    }

    /**
     * Gets whether or not the player is an emerald member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isEmeraldMember() {
        return donorStatus == DonorStatus.EMERALD_MEMBER;
    }

    /**
     * Gets whether or not the player is a ruby member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isRubyMember() {
        return donorStatus == DonorStatus.RUBY_MEMBER;
    }

    /**
     * Gets whether or not the player is a diamond member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDiamondMember() {
        return donorStatus == DonorStatus.DIAMOND_MEMBER;
    }

    /**
     * Gets whether or not the player is a dragonstone member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDragonstoneMember() {
        return donorStatus == DonorStatus.DRAGONSTONE_MEMBER;
    }

    /**
     * Gets whether or not the player is an onyx member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isOnyxMember() {
        return donorStatus == DonorStatus.ONYX_MEMBER;
    }

    /**
     * Gets whether or not the player is a zenyte member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isZenyteMember() {
        return donorStatus == DonorStatus.ZENYTE_MEMBER;
    }

    /**
     * Gets the player's Gielinor tokens.
     *
     * @return The player's Gielinor tokens.
     */
    public int getGielinorTokens() {
        return gielinorTokens;
    }

    /**
     * Sets the player's Gielinor tokens.
     *
     * @param gielinorTokens
     *            The Gielinor tokens.
     */
    public void setGielinorTokens(int gielinorTokens) {
        this.gielinorTokens = gielinorTokens;
    }

    /**
     * Increases the player's Gielinor tokens.
     *
     * @param gielinorTokens
     *            The Gielinor tokens.
     */
    public void increaseGielinorTokens(int gielinorTokens) {
        this.gielinorTokens += gielinorTokens;
    }

    /**
     * Decreases the player's Gielinor tokens.
     *
     * @param gielinorTokens
     *            The Gielinor tokens.
     */
    public int decreaseGielinorTokens(int gielinorTokens) {
        this.gielinorTokens -= gielinorTokens;
        return this.gielinorTokens;
    }

    /**
     * Gets whether or not the player wants their icon hidden.
     *
     * @return <code>True</code> if so.
     */
    public boolean isIconHidden() {
        return iconHidden;
    }

    /**
     * Sets whether or not the player's icon is hidden.
     *
     * @param iconHidden
     *            Whether or not the player's icon is hidden.
     */
    public void setIconHidden(boolean iconHidden) {
        this.iconHidden = iconHidden;
    }

    /**
     * Toggles whether or not the player's icon is hidden.
     */
    public void toggleIconHidden() {
        iconHidden = !iconHidden;
    }

    /**
     * Checks if a player has donated and the donation is unclaimed, and then
     * claims it.
     *
     * @return <code>True</code> if so.
     */
    public boolean claim() {
        int tokens = 0;
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT item_number FROM donation WHERE payment_status='Completed' AND claimed=0 AND pidn=?")) {
                preparedStatement.setInt(1, player.getPidn());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Amount of tokens
                        // TODO Rewrite
                        switch (resultSet.getString("item_number")) {
                            case "50_frozen":
                                tokens += 50;
                                break;
                            case "220_frozen":
                                tokens += 220;
                                break;
                            case "600_frozen":
                                tokens += 600;
                                break;
                        }
                    }
                }
            }
            if (tokens > 0) {
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE donation SET claimed=1 WHERE pidn=?")) {
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tokens == 0) {
            return false;
        }
        increaseGielinorTokens(tokens);
        player.getActionSender().sendMessage(
            "<col=FFFFFF><shad=255>Thank you for donating! You have been credited " + tokens + " Gielinor tokens!");
        return true;
    }

    /**
     * Gets a percentage of an amount to apply for the player's status.
     *
     * @param amount
     *            The amount to apply.
     * @param percentage
     *            The additional percentage.
     * @return The additional amount.
     */
    public double getAdditional(double amount, float percentage) {
        if (percentage < 2) {
            return 0;
        }
        return ((amount * percentage) / 100);
    }

    /**
     * Gets the credit shop container.
     *
     * @return The container.
     */
    public Container getCreditShopContainer() {
        return creditShopContainer;
    }

    /**
     * Opens the player's credit store.
     */
    public void openStore() {
        updateShopContainer(false);
        getCreditShopContainer().update();
        updateScroll();
        player.getInterfaceState().open(new Component(25709));
    }

    /**
     * Updates the credit containers scroll.
     */
    public void updateScroll() {
        if (getCreditShopContainer().itemCount() < 41) {
            PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 25715, 0));
            return;
        }
        double rows = getCreditShopContainer().itemCount() / 10;
        PacketRepository.send(InterfaceMaxScrollPacket.class,
            new InterfaceMaxScrollContext(player, 25715, (int) ((Math.ceil(rows + 0.9) * 59))));
    }

    /**
     * Updates the {@link #creditShopContainer}.
     */
    public void updateShopContainer(boolean force) {
        if (!force && (System.currentTimeMillis() - player.getAttribute("last_store_check", 0L)) < 180000) { // Wait 3 minutes before updating
            return;
        }

        if (!force && !player.getRights().isAdministrator()) {
            player.saveAttribute("last_store_check", System.currentTimeMillis());
        }

        getCreditShopContainer().clear();

        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Gielinor_cms.shop LEFT JOIN player_shop ON shop.product_id = player_shop.product_id WHERE pidn = ?")) {
                preparedStatement.setInt(1, player.getPidn());

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Item item = new Item(resultSet.getInt("product_item_id"), resultSet.getInt("product_quantity"));

                    if (getCreditShopContainer().itemCount() > 139) { // Only room for 140
                        break;
                    }

                    getCreditShopContainer().add(item, false);
                }
            }

        } catch (IOException | SQLException ex) {
            log.error("{} - SQL shop fail.", player.getName(), ex);
        }

        getCreditShopContainer().refresh();
        getCreditShopContainer().update();
        updateScroll();
    }

    /**
     * Claims a shop item, removing it from the database.
     *
     * @param item
     *            The item.
     */
    public void claimItem(Item item) {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT product_id FROM Gielinor_cms.shop WHERE product_item_id = ? AND product_quantity = ?")) {
                preparedStatement.setInt(1, item.getId());
                preparedStatement.setInt(2, item.getCount());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    try (PreparedStatement preparedStatement1 = connection
                        .prepareStatement("DELETE FROM player_shop WHERE pidn = ? AND product_id = ? LIMIT 1")) {
                        preparedStatement1.setInt(1, player.getPidn());
                        preparedStatement1.setInt(2, productId);
                        if (preparedStatement1.executeUpdate() == 1) {
                            if (getCreditShopContainer().remove(item)) {
                                player.getInventory().add(item);
                            }
                        }
                    }
                }
            }
            if (!item.getDefinition().isUnnoted()) {
                updateShopContainer(true);
            } else {
                getCreditShopContainer().shift();
            }
            updateScroll();
        } catch (IOException | SQLException ex) {
            log.error("{} - SQL shop fail.", player.getName(), ex);
        }
    }

    /**
     * The store listener for the {@link #creditShopContainer}.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class StoreListener implements ContainerListener {

        @Override
        public void update(Container c, ContainerEvent event) {
            PacketRepository.send(ContainerPacket.class,
                new ContainerContext(player, 25716, 0, 0, getCreditShopContainer(), false));
        }

        @Override
        public void refresh(Container c) {
            PacketRepository.send(ContainerPacket.class,
                new ContainerContext(player, 25716, 0, 0, getCreditShopContainer(), false));
        }

    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.putInt(donorStatus.getId());
        byteBuffer.putInt(gielinorTokens);
        byteBuffer.put((byte) (iconHidden ? 1 : 2));
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        donorStatus = DonorStatus.Companion.forId(byteBuffer.getInt());
        gielinorTokens = byteBuffer.getInt();
        iconHidden = byteBuffer.get() == 1;
        byteBuffer.get();
    }

}
