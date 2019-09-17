package org.gielinor.net.packet.context;

import org.gielinor.game.content.skill.member.summoning.InterfaceType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The summoning options context.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public class SummoningInformationContext implements Context {

    /**
     * The player reference.
     */
    private Player player;
    /**
     * The name of the special.
     */
    private final String specialName;
    /**
     * The full name, including special points used.
     */
    private final String fullName;
    /**
     * The level required to use the special.
     */
    private final int levelRequired;
    /**
     * The item required.
     */
    private final int scrollId;
    /**
     * The amount required.
     */
    private final int scrollAmount;
    /**
     * The description.
     */
    private final String specialDescription;
    /**
     * The {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     */
    private final InterfaceType interfaceType;

    /**
     * Construct a new <code>SummoningInformationContext</code>.
     *
     * @param player             The player.
     * @param specialName        The name of the special.
     * @param fullName           The full name, including special points used.
     * @param levelRequired      The level required to use the special.
     * @param scrollId           The item required.
     * @param scrollAmount       The amount required.
     * @param specialDescription The description.
     * @param interfaceType      The {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     */
    public SummoningInformationContext(Player player, String specialName, String fullName, int levelRequired, int scrollId, int scrollAmount, String specialDescription, InterfaceType interfaceType) {
        this.player = player;
        this.specialName = specialName;
        this.fullName = fullName;
        this.levelRequired = levelRequired;
        this.scrollId = scrollId;
        this.scrollAmount = scrollAmount;
        this.specialDescription = specialDescription;
        this.interfaceType = interfaceType;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Transforms this context.
     *
     * @param player  The player.
     * @param options The options.
     */
    public SummoningOptionsContext transform(Player player, String... options) {
        return new SummoningOptionsContext(player, options);
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the name of the special.
     *
     * @return The name.
     */
    public String getSpecialName() {
        return specialName;
    }

    /**
     * Gets the full name, including special points used.
     *
     * @return The full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the level required to use the special.
     *
     * @return The level.
     */
    public int getLevelRequired() {
        return levelRequired;
    }

    /**
     * Gets the item required.
     *
     * @return The id.
     */
    public int getScrollId() {
        return scrollId;
    }

    /**
     * Gets the amount required.
     *
     * @return The amount.
     */
    public int getScrollAmount() {
        return scrollAmount;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getSpecialDescription() {
        return specialDescription;
    }

    /**
     * Gets the {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     *
     * @return The {@link org.gielinor.game.content.skill.member.summoning.InterfaceType}.
     */
    public InterfaceType getInterfaceType() {
        return interfaceType;
    }
}
