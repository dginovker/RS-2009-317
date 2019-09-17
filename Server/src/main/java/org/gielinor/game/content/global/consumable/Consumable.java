package org.gielinor.game.content.global.consumable;

import org.gielinor.content.donators.DonatorConfigurations;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a dynamic consumable item.
 *
 * @author 'Vexia
 * @date 22/12/2013
 */
public abstract class Consumable implements Plugin<Object> {

    private static final Logger log = LoggerFactory.getLogger(Consumable.class);

    /**
     * Represents the animation when consuming.
     */
    protected static final Animation ANIMATION = new Animation(829);

    /**
     * Represents the empty vial item.
     */
    protected static final Item VIAL = new Item(229);

    /**
     * Represents the empty bucket item.
     */
    protected static final Item BUCKET = new Item(1925);

    /**
     * Represents the empty jug item.
     */
    protected static final Item JUG = new Item(1935);

    /**
     * Represents the empty bowl item.
     */
    protected static final Item BOWL = new Item(1923);

    /**
     * Represents the beer glass item.
     */
    protected static final Item BEER_GLASS = new Item(1919);

    /**
     * Represents the message used when emptying the consumable.
     */
    private static final String EMPTY_MESSAGE = "You empty the contents of the @name on the floor.";

    /**
     * Represents the consumable item.
     */
    private Item item;

    /**
     * Represents the food properties of this food.
     */
    private ConsumableProperties properties;

    /**
     * Represents the item the player gets when emptying this item.
     */
    protected Item emptyItem;

    /**
     * Represents the message displayed when emptying the consumable.
     */
    String emptyMessage;

    /**
     * Represents the messages to display when consumed.
     */
    protected String[] messages = null;

    /**
     * Constructs a new {@code Consumable} {@code Object}.
     *
     * @param item the item.
     * @param properties the properties.
     */
    public Consumable(final Item item, final ConsumableProperties properties) {
        this.item = item;
        this.properties = properties;
    }

    /**
     * Constructs a new {@code Consumable} {@code Object}.
     */
    Consumable() {
    }

    /**
     * Method called when this consumables is consumed.
     *
     * @param player the player.
     */
    public void consume(final Item item, final Player player) {
        consume(item, player, properties.getHealing());
    }

    /**
     * Method called when this consumable is consumed.
     *
     * @param player the player.
     */
    public void consume(final Player player) {
        consume(getItem(), player, properties.getHealing());
    }

    /**
     * Method called when this consumable is consumed.
     *
     * @param player   The player consuming this consumable.
     * @param heal     The healing amount used to override, (generally to alter amt)
     * @param messages The messages to show.
     * @note override if needed, generally for extra effects.
     */
    public void consume(final Item item, final Player player, int heal, String... messages) {
    }

    /**
     * Method used to handle the interaction between food and a node.
     *
     * @param player the player.
     * @param node   the node.
     * @note override if needed.
     */
    public boolean interact(final Player player, final Node node) {
        return interact(player, node, "");
    }

    /**
     * Method used to handle the interaction between food and a node.
     *
     * @param player the player.
     * @param node   the node.
     * @param option the option (if any)
     */
    public boolean interact(final Player player, final Node node, String option) {
        switch (option) {
            case "empty":
                final  Item item = (Item) node;

                if (item.getSlot() < 0)
                    return false;

                if (item.getName().equals("Bonemeal")) {

                    if (player.getInventory().remove(item, item.getSlot(), true))
                        player.getActionSender().sendMessage("You empty the contents of the pot onto the floor.", 1);

                    player.getInventory().add(new Item(1931), true, item.getSlot());
                    return true;
                }

                final Optional<Food> optionalFood = Consumables.findFood(item);

                if (optionalFood.isPresent()) {

                    final Food food = optionalFood.get();

                    if (player.getInventory().remove(item, item.getSlot(), true)) {

                        final String emptyMessage = food.getEmptyMessage(item);

                        if (Objects.nonNull(emptyMessage))
                            player.getActionSender().sendMessage(emptyMessage, 1);
                    }

                    final Item emptyItem = food.getEmptyItem();

                    if (Objects.nonNull(emptyItem))
                        player.getInventory().add(emptyItem);

                    return true;
                }
                if (item.getName().toLowerCase().startsWith("bucket")) {
                    player.getActionSender().sendMessage("You empty the contents of the bucket onto the floor.", 1);
                    player.getInventory().remove(item);
                    player.getInventory().add(new Item(1925, 1));
                    return true;
                }
                if (item.getName().toLowerCase().startsWith("vial")) {
                    player.getActionSender().sendMessage("You empty the vial.", 1);
                    player.getInventory().remove(item);
                    player.getInventory().add(new Item(Consumable.VIAL.getId(), 1));
                    return true;
                }
                if (player.getInventory().remove(item, item.getSlot(), true))
                    player.getActionSender().sendMessage(getEmptyMessage(item), 1);

                if (Objects.nonNull(getEmptyItem()))
                    player.getInventory().add(getEmptyItem());

                break;
        }
        return true;
    }

    /**
     * Method used to add a skill bonus to a player.
     *
     * @param player the player.
     * @param b      the bonus.
     */
    protected void addBonus(final Player player, final SkillBonus b) {
        int level = player.getSkills().getStaticLevel(b.getSkillId());
        level = (int) (b.getBaseBonus() + level + (level * b.getBonus()));
        if (b.getBonus() < 0) {
            player.getSkills().setLevel(b.getSkillId(), level);
            return;
        }
        if (player.getSkills().getLevel(b.getSkillId()) <= level) {
            if (b.getSkillId() == Skills.HITPOINTS) {
                if (player.getSkills().getLifepoints() > player.getSkills().getStaticLevel(Skills.HITPOINTS)) {
                    return;
                }
                int difference = level - player.getSkills().getStaticLevel(b.getSkillId());
                player.getSkills().setLevel(b.getSkillId(), player.getSkills().getLifepoints() + difference);
            } else {
                player.getSkills().setLevel(b.getSkillId(), level);
            }
        }
    }

    /**
     * Method used to remove the item and heal the player.
     *
     * @param player the player.
     * @param item   the item.
     */
    public void healAndRemove(final Player player, final Item item) {
        final ConsumableProperties properties = getProperties();

        if (properties == null) {
            log.warn("Consumable properties missing for item: {}.", item.getId());
            return;
        }

        if (player.getAttribute("INFINITE_FOOD") == null) {
            if (properties.hasNewItem()) {
                player.getInventory().replace(properties.getNewItem(), item.getSlot());
            } else {
                player.getInventory().remove(item, item.getSlot(), true);
            }
        }

        player.animate(ANIMATION);
        player.getSkills().heal((int) (properties.getHealing() * DonatorConfigurations.getHealingMultiplier(player)));
        player.getActionSender().sendSound(this instanceof Drink ? Drink.AUDIO : Food.AUDIO);
    }

    /**
     * Method used to message the player.
     *
     * @param player  the player.
     * @param item    the item.
     * @param initial the initial hp amount.
     */
    public void message(final Player player, final Item item, final int initial, final String... messages) {
        if (messages == null || messages.length == 0) {
            if (this instanceof Food) {
                player.getActionSender().sendMessage("You eat the " + item.getName().trim().toLowerCase() + ".", 1);
            } else if (this instanceof Drink) {
                player.getActionSender().sendMessage("You drink some of " + (item.getName().contains("brew") ? "the foul liquid" : "your " + item.getName().replace("(4)", "").replace("(3)", "").replace("(2)", "").replace("(1)", "").trim().toLowerCase()) + ".", 1);
            }
            if (player.getSkills().getLifepoints() > initial) {
                player.getActionSender().sendMessage("It heals some health.", 1);
            }
        } else {
            for (String message : messages) {
                player.getActionSender().sendMessage(message, 1);
            }
        }
    }

    /**
     * Gets the item.
     *
     * @return The item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the properties.
     *
     * @return The properties.
     */
    public ConsumableProperties getProperties() {
        return properties;
    }

    /**
     * Gets the value if this consumable is a food.
     *
     * @return the <code>True</code> if so.
     */
    public boolean isFood() {
        return this instanceof Food;
    }

    /**
     * Gets the value if this consumable is a drink.
     *
     * @return the <code>True</code> if so.
     */
    public boolean isDrink() {
        return this instanceof Drink;
    }

    /**
     * Gets this consumable as a drink.
     *
     * @return the drink.
     */
    public Drink asDrink() {
        return ((Drink) this);
    }

    /**
     * Gets the consumable as a food instance.
     *
     * @return the food consumable.
     */
    public Food asFood() {
        return ((Food) this);
    }

    /**
     * Gets the formated name of the item.
     *
     * @param item the item.
     * @return the name.
     */
    public String getName(Item item) {
        return item.getName().replace("(4)", "").replace("(3)", "").replace("(2)", "").replace("(1)", "").trim().toLowerCase();
    }

    /**
     * Gets the emptying item.
     *
     * @return the item.
     */
    public Item getEmptyItem() {
        return emptyItem;
    }

    /**
     * Gets the empty message.
     *
     * @return the message.
     */
    public String getEmptyMessage(final Item item) {
        return emptyMessage == null ? EMPTY_MESSAGE.replace("@name", getName(item)) : emptyMessage;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Consumables.add(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}
