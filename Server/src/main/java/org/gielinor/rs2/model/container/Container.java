package org.gielinor.rs2.model.container;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.impl.BankContainer;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a container which contains items.
 *
 * @author Emperor
 */
public class Container {

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    /**
     * The item array.
     */
    protected Item[] items;

    /**
     * The capacity.
     */
    private final int capacity;

    /**
     * The current sort type.
     */
    protected SortType sortType;

    /**
     * The current container type.
     */
    private final ContainerType type;

    /**
     * The current container event.
     */
    private ContainerEvent event;

    /**
     * The container botMessageListener.
     */
    private final List<ContainerListener> listeners = new ArrayList<>();

    /**
     * Constructs a new {@code Container} {@code Object}.
     *
     * @param capacity The capacity.
     */
    public Container(int capacity) {
        this(capacity, ContainerType.DEFAULT);
    }

    /**
     * Constructs a new {@code Container.java} {@code Object}.
     *
     * @param capacity the capacity.
     * @param items    the items to add.
     */
    public Container(int capacity, Item... items) {
        this(capacity);
        add(items);
    }

    /**
     * Constructs a new {@code Container} {@code Object}.
     *
     * @param capacity The capacity.
     * @param type     The container type.
     */
    public Container(int capacity, ContainerType type) {
        this(capacity, type, SortType.ID);
    }

    /**
     * Constructs a new {@code Container} {@code Object}.
     *
     * @param capacity The capacity.
     * @param type     The container type.
     * @param sortType The sort type.
     */
    public Container(int capacity, ContainerType type, SortType sortType) {
        this.capacity = capacity;
        this.type = type;
        this.items = new Item[capacity];
        this.sortType = sortType;
        this.event = new ContainerEvent(capacity);
    }

    /**
     * Registers a container listener.
     *
     * @param listener The container listener.
     * @return This container instance, for chaining.
     */
    public Container register(ContainerListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Adds the items.
     *
     * @param items The items to add.
     * @return <code>True</code> if successfully added <b>all</b> items.
     */
    public boolean add(Item... items) {
        boolean addedAll = true;
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (!add(item, false)) {
                addedAll = false;
                break;
            }
        }
        update();
        return addedAll;
    }

    /**
     * Inserts an item into a specific slot.
     *
     * @param fromSlot The original slot of the item.
     * @param toSlot   The slot to insert into.
     */
    public void insert(int fromSlot, int toSlot) {
        insert(fromSlot, toSlot, true);
    }

    /**
     * Inserts an item into a specific slot.
     *
     * @param fromSlot The original slot of the item.
     * @param toSlot   The slot to insert into.
     */
    public void insert(int fromSlot, int toSlot, boolean fireListener) {
        Item temp = items[fromSlot];
        if (toSlot > fromSlot) {
            for (int i = fromSlot; i < toSlot; i++) {
                replace(get(i + 1), i, fireListener);
            }
        } else if (fromSlot > toSlot) {
            for (int i = fromSlot; i > toSlot; i--) {
                replace(get(i - 1), i, fireListener);
            }
        }
        replace(temp, toSlot, fireListener);
    }

    /**
     * Adds an item to this container if full it goes to ground.
     *
     * @param item   the item.
     * @param player the player.
     * @return <code>True</code> if added.
     */
    public boolean add(final Item item, final Player player) {
        Result result = addAndReturnResult(item);
        if (result.failed()) {
            Item remainder = new Item(item.getId(),
                result.requested - result.succeeded, item.getCharge());
            GroundItemManager.create(remainder, player);
            // It can sometimes be difficult to spot items dropped under you,
            // so we send a colorful heads-up message.
            String prefix = remainder.getCount() == 1
                          ? "a"
                          : TextUtils.getFormattedNumber(remainder.getCount()) + " x";
            player.getActionSender().sendMessage(
                "<col=700000>Dropped %s %s at your feet.",
                prefix,
                item.getName().toLowerCase());
            return false;
        }
        return true;
    }

    /**
     * Adds an item to this container.
     *
     * @param item The item.
     * @return <code>True</code> if the item got added.
     */
    public boolean add(Item item) {
        return add(item, true, -1);
    }

    /**
     * Adds an item to this container.
     *
     * @param itemId the Item's ID
     * @return <code>True</code> if the item got added.
     */
    public boolean add(int itemId) {
        return add(new Item(itemId), true, -1);
    }

    /**
     * Adds an item to this container.
     *
     * @param item         The item to add.
     * @param fireListener If we should update.
     * @return <code>True</code> if the item got added.
     */
    public boolean add(Item item, boolean fireListener) {
        return add(item, fireListener, -1);
    }

    /**
     * Adds an item to this container.
     *
     * @param item          The item to add.
     * @param fireListener  If we should update.
     * @param preferredSlot The slot to add the item in, when possible.
     * @return <code>True</code> if the item got added.
     */
    public boolean add(Item item, boolean fireListener, int preferredSlot) {
        item = item.copy();
        int maximum = getMaximumAdd(item);
        if (maximum == 0) {
            return false;
        }
        if (preferredSlot > -1 && items[preferredSlot] != null) {
            preferredSlot = -1;
        }
        if (item.getCount() > maximum) {
            item.setCount(maximum);
        }
        if (item.getCount() < 1) {
            return false;
        }
        if (type != ContainerType.NEVER_STACK &&
            (item.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.TYPE_AND_METADATA;
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null)
                    continue;
                if ((hashBased && items[i].isTypeEqual(item)) || (!hashBased && items[i].getId() == item.getId())) {
                    int totalCount = item.getCount() + items[i].getCount();
                    items[i] = new Item(items[i].getId(), totalCount, item.getCharge());
                    items[i].setIndex(i);
                    event.flag(i, items[i]);
                    if (fireListener) {
                        update();
                    }
                    return true;
                }
            }
            int slot = preferredSlot > -1 ? preferredSlot : freeSlot();
            if (slot == -1) {
                return false;
            }
            items[slot] = item;
            item.setIndex(slot);
            event.flag(slot, item);
            if (fireListener) {
                update();
            }
            return true;
        }
        int slots = freeSlots();
        if (slots >= item.getCount()) {
            for (int i = 0; i < item.getCount(); i++) {
                int slot = i == 0 && preferredSlot > -1 ? preferredSlot : freeSlot();
                items[slot] = new Item(item.getId(), 1, item.getCharge());
                items[slot].setIndex(slot);
                event.flag(slot, items[slot]);
            }
            if (fireListener) {
                update();
            }
            return true;
        }
        return false;
    }

    /** Same as add, but returns {requested, succeeded} instead of a bool indication of success. */
    public Result addAndReturnResult(final Item request) {
        Item item = request.copy();
        int maximum = getMaximumAdd(item);
        if (maximum < 1 || item.getCount() < 1) {
            return new Result(request.getCount(), 0);
        }
        if (item.getCount() > maximum) {
            item.setCount(maximum);
        }
        if (type != ContainerType.NEVER_STACK &&
            (item.getDefinition().isStackable() || type == ContainerType.ALWAYS_STACK || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.TYPE_AND_METADATA;
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null)
                    continue;
                if ((hashBased && items[i].isTypeEqual(item)) || (!hashBased && items[i].getId() == item.getId())) {
                    int totalCount = item.getCount() + items[i].getCount();
                    items[i] = new Item(items[i].getId(), totalCount, item.getCharge());
                    items[i].setIndex(i);
                    event.flag(i, items[i]);
                    update();
                    return new Result(request.getCount(), item.getCount());
                }
            }
            int slot = freeSlot();
            if (slot == -1) {
                return new Result(request.getCount(), 0);
            }
            items[slot] = item;
            item.setIndex(slot);
            event.flag(slot, item);
            update();
            return new Result(request.getCount(), item.getCount());
        }
        int slotsAvailable = freeSlots();
        int addCount = Math.min(item.getCount(), slotsAvailable);
        for (int i = 0; i < addCount; i++) {
            int slot = freeSlot();
            Item newItem = new Item(item.getId(), 1, item.getCharge());
            newItem.setIndex(slot);
            items[slot] = newItem;
            event.flag(slot, newItem);
        }
        update();
        return new Result(request.getCount(), addCount);
    }

    /**
     * Adds an item to this container.
     *
     * @param item          The item to add.
     * @param fireListener  If we should update.
     * @param preferredSlot The slot to add the item in, when possible.
     * @return <code>True</code> if the item got added.
     */
    public int addSlot(Item item, boolean fireListener, int preferredSlot) {
        item = item.copy();
        int maximum = getMaximumAdd(item);
        if (maximum == 0) {
            return -1;
        }
        if (preferredSlot > -1 && items[preferredSlot] != null) {
            preferredSlot = -1;
        }
        if (item.getCount() > maximum) {
            item.setCount(maximum);
        }
        if (type != ContainerType.NEVER_STACK &&
            (item.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.TYPE_AND_METADATA;
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    if ((hashBased && items[i].isTypeEqual(item)) || (!hashBased && items[i].getId() == item.getId())) {
                        int totalCount = item.getCount() + items[i].getCount();
                        items[i] = new Item(items[i].getId(), totalCount, item.getCharge());
                        items[i].setIndex(i);
                        event.flag(i, items[i]);
                        if (fireListener) {
                            update();
                        }
                        return i;
                    }
                }
            }
            int slot = preferredSlot > -1 ? preferredSlot : freeSlot();
            if (slot == -1) {
                return -1;
            }
            items[slot] = item;
            item.setIndex(slot);
            event.flag(slot, item);
            if (fireListener) {
                update();
            }
            return slot;
        }
        int slots = freeSlots();
        int addedSlot = -1;
        if (slots >= item.getCount()) {
            for (int i = 0; i < item.getCount(); i++) {
                int slot = i == 0 && preferredSlot > -1 ? preferredSlot : freeSlot();
                items[slot] = new Item(item.getId(), 1, item.getCharge());
                items[slot].setIndex(slot);
                event.flag(slot, items[slot]);
                addedSlot = slot;
            }
            if (fireListener) {
                update();
            }
            return addedSlot;
        }
        return -1;
    }

    /**
     * Removes a set of items.
     *
     * @param items The set of items.
     * @return <code>True</code> if all items got succesfully removed.
     */
    public boolean remove(Item... items) {
        boolean removedAll = true;
        for (Item item : items) {
            if (!remove(item, false)) {
                removedAll = false;
            }
        }
        update();
        return removedAll;
    }

    /**
     * Removes all items but the provided item ids.
     *
     * @param itemIds the item ids that should not be deleted.
     */
    public void removeAllBut(int... itemIds){
        for (int i = 0; i < capacity(); i++) {
            int finalI = i;
            if(Arrays.stream(itemIds).noneMatch(id -> id == items[finalI].getId()))
                items[finalI] = new Item(-1, 0);
        }
    }

    /**
     * Removes an item.
     *
     * @param itemId the Item id
     * @return <code>True</code> if the item got removed, <code>False</code> if not.
     */
    public boolean remove(int itemId) {
        return remove(new Item(itemId));
    }

    /**
     * Removes an item.
     *
     * @param item The item.
     * @return <code>True</code> if the item got removed, <code>False</code> if not.
     */
    public boolean remove(Item item) {
        return remove(item, true);
    }

    /**
     * Removes an item, allowing zeros.
     *
     * @param item The item.
     * @return The number of items removed.
     */
    public void removeOrZero(Item item) {
        remove(item, getSlot(item), true, true);
    }

    /**
     * Removes an item.
     *
     * @param item         The item to remove.
     * @param fireListener If the fire listener should be "notified".
     * @return <code>True</code> if the item got removed,
     * <br>		<code>False</code> if not.
     */
    public boolean remove(Item item, boolean fireListener) {
        int slot = getSlot(item);
        if (slot != -1) {
            return remove(item, slot, fireListener, false);
        }
        return false;
    }

    /**
     * Removes an item from this container.
     *
     * @param item         The item.
     * @param slot         The item slot.
     * @param fireListener If the fire listener should be "notified".
     * @return <code>True</code> if the item got removed,
     * <br>		<code>False</code> if the item on the slot was null or the ids didn't match.
     */
    public boolean remove(Item item, int slot, boolean fireListener) {
        return remove(item, slot, fireListener, false);
    }

    /**
     * Removes an item from this container.
     *
     * @param item         The item.
     * @param slot         The item slot.
     * @param fireListener If the fire listener should be "notified".
     * @param allowZero    Whether or not the item can be 0.
     * @return <code>True</code> if the item got removed,
     * <br>		<code>False</code> if the item on the slot was null or the ids didn't match.
     */
    public boolean remove(Item item, int slot, boolean fireListener, boolean allowZero) {
        Item oldItem = items[slot];
        if (oldItem == null || oldItem.getId() != item.getId()) {
            return false;
        }
        if (item.getCount() < 1) {
            return true;
        }
        if (oldItem.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP) {
            if (item.getCount() >= oldItem.getCount()) {
                if (allowZero) {
                    items[slot].setCount(0);
                } else {
                    items[slot] = null;
                }
                event.flagNull(slot);
                if (fireListener) {
                    update();
                }
                return true;
            }
            items[slot] = new Item(item.getId(), oldItem.getCount() - item.getCount(), item.getCharge());
            items[slot].setIndex(slot);
            event.flag(slot, items[slot]);
            if (fireListener) {
                update();
            }
            return true;
        }
        if (allowZero) {
            items[slot].setCount(0);
        } else {
            items[slot] = null;
        }
        event.flagNull(slot);
        int removed = 1;
        for (int i = removed; i < item.getCount(); i++) {
            slot = getSlot(item);
            if (slot != -1) {
                if (allowZero) {
                    items[slot].setCount(0);
                } else {
                    items[slot] = null;
                }
                event.flagNull(slot);
            } else {
                break;
            }
        }
        if (fireListener) {
            update();
        }
        return true;
    }


    /**
     * Swaps two items.
     *
     * @param toSlot   The slot we're swapping to.
     * @param fromSlot The slot we're swapping from.
     */
    public void swap(int toSlot, int fromSlot) {
        if (fromSlot == toSlot) {
            return;
        }
        int index = fromSlot;
        if (fromSlot > toSlot) {
            while (index != toSlot) {
                Item item = items[index - 1];
                items[index - 1] = items[index];
                items[index] = item;
                index--;
            }
        } else if (fromSlot < toSlot) {
            while (index != toSlot) {
                Item item = items[index + 1];
                items[index + 1] = items[index];
                items[index] = item;
                index++;
            }
        }
    }

    /**
     * Replaces the item on the given slot with the argued item.
     *
     * @param item The item.
     * @param slot The slot.
     * @return The old item.
     */
    public Item replace(Item item, int slot) {
        return replace(item, slot, true);
    }

    /**
     * Replaces the item on the given slot with the argued item.
     *
     * @param item         The item.
     * @param slot         The slot.
     * @param fireListener If the listener should be "notified".
     * @return The old item.
     */
    public Item replace(Item item, int slot, boolean fireListener) {
        if (item != null) {
            if (item.getCount() < 1 && type != ContainerType.SHOP) {
                item = null;
            } else {
                item = item.copy();
            }
        }
        Item oldItem = items[slot];
        items[slot] = item;
        if (item == null) {
            event.flagNull(slot);
        } else {
            item.setIndex(slot);
            event.flag(slot, item);
        }
        if (fireListener) {
            update();
        }
        return oldItem;
    }

    /**
     * Updates the container.
     */
    public void update() {
        if (event.getChangeCount() < 1 && !event.isClear()) {
            return;
        }
        for (ContainerListener listener : listeners) {
            listener.update(this, event);
        }
        event.setClear(false);
        event = new ContainerEvent(capacity);
    }

    /**
     * Updates the container.
     */
    public void update(boolean force) {
        if (event.getChangeCount() < 1 && !force) {
            return;
        }
        for (ContainerListener listener : listeners) {
            listener.update(this, event);
        }
        event = new ContainerEvent(capacity);
    }

    /**
     * Refreshes the entire container.
     */
    public void refresh() {
        for (ContainerListener listener : listeners) {
            listener.refresh(this);
        }
        event = new ContainerEvent(capacity);
    }

    /**
     * Sends the inventory.
     *
     * @param player      The player.
     * @param componentId The id of the inventory component.
     */
    public void sendInventory(Player player, int componentId) {
        player.getActionSender().sendUpdateItems(componentId, toArray());
    }

    /**
     * Gets the item instance.
     *
     * @param item the item.
     * @return the item.
     */
    public Item getItem(Item item) {
        return get(getSlot(item));
    }

    /**
     * Gets the item on the given slot.
     *
     * @param slot The slot.
     * @return The item on the slot, or {@code null} if the item wasn't there.
     */
    public Item get(int slot) {
        if (slot < 0 || slot >= items.length) {
            return null;
        }
        return items[slot];
    }

    /**
     * Gets the item on the given slot.
     *
     * @param slot The slot.
     * @return The item on the slot,
     * or a new constructed item with id 0 if the item wasn't there.
     */
    public Item getNew(int slot) {
        Item item = items[slot];
        if (item != null) {
            return item;
        }
        return new Item(0);
    }

    /**
     * Gets the item id on the given slot.
     *
     * @param slot The slot.
     * @return The id of the item on the slot.
     */
    public int getId(int slot) {
        if (slot >= items.length) {
            return -1;
        }
        Item item = items[slot];
        if (item != null) {
            return item.getId();
        }
        return -1;
    }

    /**
     * Gets the item id for the given name.
     *
     * @param name The name of the item.
     * @return The id of the item on the slot.
     */
    public Item get(String name) {
        for (Item item : this.items) {
            if (item == null) {
                continue;
            }
            if (item.getName().toLowerCase().contains(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets a random item from this container.
     *
     * @return The item.
     */
    public Item getRandomItem() {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            itemList.add(item);
        }
        if (itemList.size() == 0) {
            return null;
        }
        return itemList.get(new SecureRandom().nextInt(itemList.size()));
    }

    /**
     * Gets the wealth.
     *
     * @return the wealth.
     */
    public int getWealth() {
        int wealth = 0;
        for (Item i : items) {
            if (i == null) {
                continue;
            }
            wealth += i.getDefinition().getValue() * i.getCount();
        }
        return wealth;
    }

    /**
     * Parses the container data from the list of items.
     *
     * @param itemList The {@link List} of items.
     * @return The total value of all items (G.E price > Store price > High alchemy price).
     */
    public int parseSQL(List<Item> itemList) {
        int total = 0;
        for (Item listItem : itemList) {
            int id = listItem.getId();
            int count = listItem.getCount();
            int charge = listItem.getCharge();
            int slot = listItem.getIndex();
            if (ItemDefinition.forId(id) == null || id < 0 || slot >= items.length || slot < 0) {
                continue;
            }
            Item item = items[slot] = new Item(id, count, charge);
            item.setIndex(slot);
            total += item.getValue();
        }
        return total;
    }


    /**
     * Parses the container data from the list of items.
     *
     * @param itemList The {@link List} of items.
     * @return The total value of all items (G.E price > Store price > High alchemy price).
     */
    public int parse(List<Item> itemList) {
        int total = 0;
        for (Item listItem : itemList) {
            int id = listItem.getId();
            int count = listItem.getCount();
            int charge = listItem.getCharge();
            int slot = listItem.getIndex();
            if (ItemDefinition.forId(id) == null || id < 0 || slot >= items.length || slot < 0) {
                continue;
            }
            Item item = items[slot] = new Item(id, count, charge);
            item.setIndex(slot);
            total += item.getValue();
        }
        return total;
    }

    /**
     * Parses the container data from the byte buffer.
     *
     * @param buffer The byte buffer.
     * @return The total value of all items (G.E price > Store price > High alchemy price).
     */
    public int parse(ByteBuffer buffer) {
        int slot;
        int total = 0;
        while ((slot = buffer.getShort()) != -1) {
            int id = buffer.getShort() & 0xFFFF;
            int amount = buffer.getInt();
            int charge = buffer.getInt();
            if (ItemDefinition.forId(id) == null) {
                log.warn("Missing item definition: {}.", id);
                continue;
            }
            if (slot >= items.length || slot < 0) {
                continue;
            }
            Item item = items[slot] = new Item(id, amount, charge);
            item.setIndex(slot);
            total += item.getValue();
        }
        return total;
    }

    /**
     * Saves the item data on the byte buffer.
     *
     * @param buffer The byte buffer.
     * @return The total value of all items (G.E price > Store price > High alchemy price).
     */
    public long save(ByteBuffer buffer) {
        long totalValue = 0;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item == null) {
                continue;
            }
            buffer.putShort((short) i);
            buffer.putShort((short) item.getId());
            buffer.putInt(item.getCount());
            buffer.putInt(item.getCharge());
            totalValue += item.getValue();
        }
        buffer.putShort((short) -1);
        return totalValue;
    }

    /**
     * Copies the container to this container.
     *
     * @param c The container to copy.
     */
    public void copy(Container c) {
        items = new Item[c.items.length];
        for (int i = 0; i < items.length; i++) {
            Item it = c.items[i];
            if (it == null) {
                continue;
            }
            items[i] = new Item(it.getId(), it.getCount(), it.getCharge());
            items[i].setIndex(i);
        }
    }

    /**
     * Gets a slot by id.
     *
     * @param id The id.
     * @return The slot, or <code>-1</code> if it could not be found.
     */
    public int getSlotById(int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            if (items[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if the container contains an item.
     *
     * @param item the Item
     * @return <code>True</code> if so.
     */
    public boolean containsItem(Item item) {
        return contains(item.getId(), item.getCount());
    }

    /**
     * Checks if the containers contains these items.
     *
     * @param items the items.
     * @return <code>True</code> if so.
     */
    public boolean containsItems(Item... items) {
        for (Item i : items) {
            if (!containsItem(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the container contains an item.
     *
     * @param itemId The item id.
     * @param amount The amount.
     * @return <code>True</code> if so.
     */
    public boolean contains(int itemId, int amount) {
        int count = 0;
        for (Item item : items) {
            if (item != null && item.getId() == itemId) {
                if ((count += item.getCount()) >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the container contains the specified item.
     *
     * @param id The item id.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean contains(int id) {
        return getSlotById(id) != -1;
    }

    /**
     * Checks if the container contains multiple items.
     *
     * @param ids The item ids.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean contains(int... ids) {
        for (int itemId : ids) {
            if (getSlotById(itemId) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the container contains an item by the item.
     *
     * @param item The item.
     * @return Whether or not the container contains the item.
     */
    public boolean contains(Item item) {
        return item != null && contains(item.getId(), item.getCount());
    }

    /**
     * Checks if the container contains items.
     *
     * @param items The items.
     * @return Whether or not the container contains the item.
     */
    public boolean contains(Item... items) {
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the containers contains ONE item.
     *
     * @param itemId
     * @return
     */
    public boolean containsOneItem(int itemId) {
        for (Item item : items) {
            if (item != null && item.getId() == itemId && item.getCount() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the container contains any of the item by name.
     *
     * @param name The name of the item.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean contains(String name) {
        for (Item item : this.items) {
            if (item == null) {
                continue;
            }
            if (item.getName().toLowerCase().contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a container to this container.
     *
     * @param container The container.
     */
    public void addAll(Container container) {
        add(container.items);
    }

    /**
     * Checks the maximum amount of this item we can add.
     *
     * @param item The item.
     * @return The maximum amount we can add.
     */
    public int getMaximumAdd(Item item) {
        if (type != ContainerType.NEVER_STACK) {
            if (item.getDefinition() == null) {
                log.warn("Missing item definition: {}.", item.getId());
            }
            if (item.getDefinition().isStackable() ||
                type == ContainerType.ALWAYS_STACK || type == ContainerType.SHOP) {
                if (contains(item.getId(), 1)) {
                    return Integer.MAX_VALUE - getCount(item);
                }
                return freeSlots() > 0 ? Integer.MAX_VALUE : 0;
            }
        }
        return freeSlots();
    }

    /**
     * Checks the maximum amount of this item we can add.
     *
     * @param item The item.
     * @return The maximum amount we can add.
     */
    public int getMaximumAdd(Item item, Player player) {
        if (type != ContainerType.NEVER_STACK) {
            if (item.getDefinition().isStackable() || type == ContainerType.ALWAYS_STACK || type == ContainerType.SHOP) {
                if (contains(item.getId(), 1)) {
                    return Integer.MAX_VALUE - getCount(item);
                }
                return freeSlots(player) > 0 ? Integer.MAX_VALUE : 0;
            }
        }
        return freeSlots(player);
    }

    /**
     * Checks if the container has space for the item.
     *
     * @param item The item to check.
     * @return <code>True</code> if so.
     */
    public boolean hasRoomFor(Item item) {
        return item.getCount() <= getMaximumAdd(item);
    }

    /**
     * Checks if this container has space to add the other container.
     *
     * @param c The other container.
     * @return <code>True</code> if so.
     */
    public boolean hasSpaceFor(Container c) {
        if (c == null) {
            return false;
        }
        Container check = new Container(capacity, type);
        check.addAll(this);
        for (Item item : c.items) {
            if (item != null) {
                if (!check.add(item, false)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the item slot.
     *
     * @param item The item.
     * @return The slot of the item in this container.
     */
    public int getSlot(Item item) {
        if (item == null) {
            return -1;
        }
        int id = item.getId();
        for (int i = 0; i < items.length; i++) {
            Item it = items[i];
            if (it != null && it.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the item slot.
     *
     * @param itemId The item id.
     * @return The slot of the item in this container.
     */
    public int getSlot(int itemId) {
        for (int i = 0; i < capacity; i++) {
            if (items[i] != null && items[i].getId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the next free slot.
     *
     * @return The slot, or <code>-1</code> if there are no available slots.
     */
    public int freeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Gets the slot of where to add the item.
     *
     * @param item The item to add.
     * @return The slot where the item will go.
     */
    public int getAddSlot(Item item) {
        if (type != ContainerType.NEVER_STACK && (item.getDefinition().isStackable() || type.equals(ContainerType.ALWAYS_STACK) || type == ContainerType.SHOP)) {
            boolean hashBased = sortType == SortType.TYPE_AND_METADATA;
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    if ((hashBased && items[i].isTypeEqual(item)) || (!hashBased && items[i].getId() == item.getId())) {
                        return i;
                    }
                }
            }
        }
        return freeSlot();
    }

    /**
     * Gets an item by id.
     *
     * @param id The id.
     * @return The item, or <code>null</code> if it could not be found.
     */
    public Item getById(int id) {
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the number of free slots.
     *
     * @return The number of free slots.
     */
    public int freeSlots() {
        return capacity - itemCount();
    }

    /**
     * Gets the number of free slots.
     *
     * @param player The player.
     * @return The number of free slots.
     */
    public int freeSlots(Player player) {
        if (this instanceof BankContainer) {
            return player.getDonorManager().getDonorStatus().getBankSpaces() - itemCount();
        }
        return capacity - itemCount();
    }

    /**
     * Gets the size of this container.
     *
     * @return The size of this container.
     */
    public int itemCount() {
        int size = 0;
        for (Item item : items) {
            if (item != null) {
                size++;
            }
        }
        return size;
    }

    /**
     * Checks if the player has all the item ids in the inventory.
     *
     * @param itemIds The item ids.
     * @return <code>True</code> if so.
     */
    public boolean containItems(int... itemIds) {
        for (int itemId : itemIds) {
            if (!contains(itemId, 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the amount of an item.
     *
     * @param item The item.
     * @return The amount of this item in this container.
     */
    public int getCount(Item item) {
        if (item == null) {
            return 0;
        }
        int count = 0;
        for (Item i : items) {
            if (i != null && i.getId() == item.getId()) {
                count += i.getCount();
            }
        }
        return count;
    }

    /**
     * Gets the amount of an item.
     *
     * @param itemId The id of the item.
     * @return The amount of this item in this container.
     */
    public int getCount(int itemId) {
        if (itemId < 0) {
            return 0;
        }
        int count = 0;
        for (Item i : items) {
            if (i != null && i.getId() == itemId) {
                count += i.getCount();
            }
        }
        return count;
    }

    /**
     * Gets the networth of this container.
     *
     * @return The networth.
     */
    public long getNetworth() {
        long networth = 0;
        for (Item item : toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getId() == -1) {
                continue;
            }
            networth += item.getValue();
        }
        return networth;
    }

    /**
     * Gets the networth of this container.
     *
     * @return The networth.
     */
    public long getNetworth(boolean grandExchange, boolean highAlchemy) {
        long networth = 0;
        for (Item item : toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getId() == -1) {
                continue;
            }
            networth += item.getDefinition().getValue(grandExchange, highAlchemy);
        }
        return networth;
    }

    /**
     * Shifts the elements in the <b>Container</b> to the appropriate position.
     */
    public void shift() {
        final Item[] items1 = items;
        clear(true);
        add(items1);
    }

    /**
     * Checks if the container is empty.
     *
     * @return <code>True</code> if so.
     */
    public boolean isEmpty() {
        for (Item item : items) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears and updates the container.
     */
    public void clear() {
        clear(true);
    }


    /**
     * Clears the container.
     *
     * @param update If the container should be updated.
     */
    public void clear(boolean update) {
        items = new Item[capacity];
        event.flagEmpty();
        if (update) {
            refresh();
        }
    }

    /**
     * Returns an array representing this container.
     *
     * @return The array.
     */
    public Item[] toArray() {
        return items;
    }

    /**
     * Gets the {@link org.gielinor.rs2.model.container.ContainerEvent}.
     *
     * @return The container event.
     */
    public ContainerEvent getEvent() {
        return event;
    }

    /**
     * Gets the botMessageListener.
     *
     * @return The botMessageListener.
     */
    public List<ContainerListener> getListeners() {
        return listeners;
    }

    /**
     * Gets the capacity.
     *
     * @return The capacity of this container.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Sets an item.
     *
     * @param index  The position in the container.
     * @param item   The item.
     * @param update Whether or not we should update the container.
     */
    public void set(int index, Item item, boolean update) {
        items[index] = item;
        update(update);
    }

    /**
     * Sets an item.
     *
     * @param index The position in the container.
     * @param item  The item.
     */
    public void set(int index, Item item) {
        set(index, item, true);
    }

    public Item[] getItems() {
        return items;
    }


    public static class Result {

        private final int requested;
        private final int succeeded;

        Result(int requested, int succeeded) {
            this.requested = requested;
            this.succeeded = succeeded;
        }

        public int getRequested() {
            return requested;
        }

        public int getSucceeded() {
            return succeeded;
        }

        public boolean success() {
            return succeeded == requested;
        }

        public boolean failed() {
            return succeeded != requested;
        }

    }

}
