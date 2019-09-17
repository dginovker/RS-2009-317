package org.gielinor.game.content.global.consumable;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.cooking.CookingPulse;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;

import plugin.activity.duelarena.DuelRule;

/**
 * Represents a consumable/cookable food.
 *
 * @author 'Vexia
 * @date 22/12/2013
 */
public class Food extends Consumable {

    /**
     * Represents the food consumtion sound.
     */
    public static final Audio AUDIO = new Audio(317, 1, 1);

    /**
     * Represents the raw representationf of this food (if any).
     */
    private Item raw;

    /**
     * Represents the burnt representation of this food (if any).
     */
    private Item burnt;

    /**
     * Represents the cooking properties of this food.
     */
    private CookingProperties cookingProperties;

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item              the item.
     * @param raw               the raw item.
     * @param burnt             the burnt item.
     * @param foodProperties    the food properties.
     * @param cookingProperties the cooking properties.
     */
    public Food(final Item item, final Item raw, final Item burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
        super(item, foodProperties);
        this.raw = raw;
        this.burnt = burnt;
        this.cookingProperties = cookingProperties;
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item              the item.
     * @param foodProperties    the food properties.
     * @param cookingProperties the cooking properties.
     */
    public Food(final Item item, final ConsumableProperties foodProperties, final CookingProperties cookingProperties) {
        this(item, null, null, foodProperties, cookingProperties);
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item              the item id.
     * @param raw               the raw item id.
     * @param burnt             the burnt item id.
     * @param foodProperties    the food properties.
     * @param cookingProperties the cooking properties.
     */
    public Food(final int item, final int raw, final int burnt, final ConsumableProperties foodProperties, final CookingProperties cookingProperties) {
        this(new Item(item), new Item(raw), new Item(burnt), foodProperties, cookingProperties);
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param foodProperties the food properties.
     */
    public Food(final Item item, final Item raw, final Item burnt, ConsumableProperties foodProperties) {
        this(item, raw, burnt, foodProperties, null);
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item           the item.
     * @param foodProperties the consumable properties.
     */
    public Food(final int item, ConsumableProperties foodProperties) {
        this(new Item(item), null, null, foodProperties);
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item           the item.
     * @param emptyItem      the empty item.
     * @param foodProperties the consumable properties.
     */
    public Food(final int item, final int emptyItem, ConsumableProperties foodProperties) {
        this(new Item(item), null, null, foodProperties);
        super.emptyItem = new Item(emptyItem);
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item           the item.
     * @param emptyItem      the empty item.
     * @param foodProperties the consumable properties.
     */
    public Food(final int item, final int emptyItem, String emptyMessage, ConsumableProperties foodProperties) {
        this(new Item(item), null, null, foodProperties);
        super.emptyItem = new Item(emptyItem);
        this.emptyMessage = emptyMessage;
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item   the item.
     * @param health the health.
     */
    public Food(final int item, final int health) {
        this(new Item(item), null, null, new ConsumableProperties(health));
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     *
     * @param item   the item.
     * @param health the health.
     */
    public Food(final int item, final int health, String... messages) {
        this(new Item(item), null, null, new ConsumableProperties(health));
        this.messages = messages;
    }

    /**
     * Constructs a new {@code Food} {@code Object}.
     */
    public Food() {
        /*
         * empty.
         */
    }

    @Override
    public void consume(final Item item, final Player player) {

        if (DuelRule.NO_FOOD.enforce(player, true))
            return;

        player.getInterfaceState().close();

        int restore = 2;

        if (getProperties() != null)
            restore = getProperties().getHealing();


        // anglerfish
        if(item.getId() == 13441){
            final int hpLevel = player.getSkills().getStaticLevel(Skills.HITPOINTS);
            restore = (int) (Math.floor(hpLevel / 10) + calculateAnglerFishHealingConstant(hpLevel));
        }

        consume(item, player, restore, messages);
    }

    @Override
    public void consume(final Item item, final Player player, int heal, String... messages) {
        if (DuelRule.NO_FOOD.enforce(player, true))
            return;

        player.getInterfaceState().close();
        final int initial = player.getSkills().getLifepoints();
        healAndRemove(player, item);
        message(player, item, initial, messages == null ? this.messages : messages);
    }

    @Override
    public boolean interact(final Player player, final Node node) {
        return (!getCookingProperties().isRange() || !node.getName().toLowerCase().equals("fire"))
            && (!getCookingProperties().isSpit() || node.getName().toLowerCase().equals("fire"));
    }

    /**
     * Formula derived from wiki: http://oldschoolrunescape.wikia.com/wiki/Anglerfish
     *
     * @param hitPointsLevel the player static {@link Skills#HITPOINTS} level.
     *
     * @return a constant.
     */
    private static int calculateAnglerFishHealingConstant(int hitPointsLevel){
        if(hitPointsLevel < 25) return 2;
        if(hitPointsLevel < 50) return 4;
        if(hitPointsLevel < 75) return 6;
        if(hitPointsLevel < 93) return 8;
        else return 13;
    }

    /**
     * Method used to start cooking the food.
     *
     * @param player the player.
     * @param object the object.
     * @param amount the amount.
     */
    public void cook(final Player player, final GameObject object, final int amount) {
        player.getPulseManager().run(new CookingPulse(player, object, this, amount));
    }

    /**
     * Gets the cookingProperties.
     *
     * @return The cookingProperties.
     */
    public CookingProperties getCookingProperties() {
        return cookingProperties;
    }

    /**
     * Gets the raw.
     *
     * @return The raw.
     */
    public Item getRaw() {
        return raw;
    }

    /**
     * Gets the burnt.
     *
     * @return The burnt.
     */
    public Item getBurnt() {
        return burnt;
    }

    /**
     * Gets the value if the food has cooking properties.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasCookingProperties() {
        return cookingProperties != null;
    }

    /**
     * Gets the value if it has a raw item.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasRaw() {
        return raw != null;
    }

    /**
     * Gets the value if it has a burnt item.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasBurnt() {
        return burnt != null;
    }

    /**
     * Gets the message when you eat.
     *
     * @return the message to display.
     */
    public String getEatMessage() {
        return "You eat the " + getItem().getName().toLowerCase() + ".";
    }

}
