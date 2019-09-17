package org.gielinor.game.content.skill.member.summoning.familiar;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Task;
import org.gielinor.game.content.skill.member.summoning.SummoningPouch;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.content.skill.member.summoning.pet.PetDetails;
import org.gielinor.game.content.skill.member.summoning.pet.Pets;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.model.container.Container;

/**
 * Handles a player's familiar.
 *
 * @author Emperor
 */
public final class FamiliarManager implements SavingModule {

    /**
     * The familiars mapping.
     */
    private static final Map<Integer, Familiar> FAMILIARS = new HashMap<>();

    /**
     * The pet details mapping, sorted by item id.
     */
    private final Map<Integer, PetDetails> petDetails = new HashMap<>();

    /**
     * The player.
     */
    private final Player player;

    /**
     * The familiar.
     */
    private Familiar familiar;

    /**
     * The combat level difference when using summoning.
     */
    private int summoningCombatLevel;

    /**
     * If the player has a summoning pouch.
     */
    private boolean hasPouch;

    /**
     * Constructs a new {@code FamiliarManager} {@code Object}.
     *
     * @param player The player.
     */
    public FamiliarManager(Player player) {
        this.player = player;
    }

    @Override
    public void save(ByteBuffer buffer) {
        for (Entry<Integer, PetDetails> entry : petDetails.entrySet()) {
            buffer.put((byte) 3);
            buffer.putInt(entry.getKey());
            entry.getValue().save(buffer);
        }
        if (hasPet()) {
            buffer.put((byte) 4);
            buffer.putInt(((Pet) familiar).getPet().getBabyItemId());
        } else if (hasFamiliar()) {
            buffer.put((byte) 1);
            buffer.putShort((short) familiar.getOriginalId());
            buffer.putShort((short) familiar.ticks);
            buffer.put((byte) familiar.specialPoints);
            if (familiar.isBurdenBeast() && !((BurdenBeast) familiar).getContainer().isEmpty()) {
                ((BurdenBeast) familiar).getContainer().save(buffer.put((byte) 2));
            }
        }
        buffer.put((byte) 0);
    }

    @Override
    public final void parse(ByteBuffer buffer) {
        int opcode;
        PetDetails details;
        while ((opcode = buffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    int npcId = buffer.getShort() & 0xFFFF;
                    familiar = FAMILIARS.get(npcId).construct(player, npcId);
                    familiar.ticks = buffer.getShort() & 0xFFFF;
                    familiar.specialPoints = buffer.get() & 0xFF;
                    break;
                case 2:
                    if (familiar == null || !familiar.isBurdenBeast()) {
                        new Container(30).parse(buffer);
                        continue;
                    }
                    ((BurdenBeast) familiar).getContainer().parse(buffer);
                    break;
                case 3:
                    int baseItem = buffer.getInt();
                    details = new PetDetails(0);
                    details.parse(buffer);
                    petDetails.put(baseItem, details);
                    break;
                case 4:
                    int itemId = buffer.getInt();
                    details = petDetails.get(itemId);
                    Pets pets = Pets.forId(itemId);
                    if (details == null) {
                        details = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
                        petDetails.put(itemId, details);
                    }
                    familiar = new Pet(player, details, itemId, pets.getNpcId(details.getStage()));
                    break;
            }
        }
    }

    /**
     * Called when the player logs in.
     */
    public void login() {
        if (hasFamiliar()) {
            familiar.init();
            getFamiliar().sendConfiguration();
        }
        player.getInterfaceState().force(InterfaceConfiguration.HAS_FAMILIAR, hasFamiliar(), false);
        sendLeftClickOptions();
    }

    /**
     * Summons a familiar.
     *
     * @param item       The item.
     * @param pet        If the familiar is a pet.
     * @param deleteItem If we should delete the item.
     */
    public void summon(Item item, boolean pet, boolean deleteItem, boolean animation) {
        if (hasFamiliar()) {
            player.getActionSender().sendMessage("You already have a follower.");
            return;
        }
        if (player.getZoneMonitor().isRestricted(ZoneRestriction.FOLLOWERS)) {
            player.getActionSender().sendMessage("This is a Summoning-free area.");
            return;
        }
        if (pet) {
            summonPet(item, deleteItem, animation);
            return;
        }
        final SummoningPouch summoningPouch = SummoningPouch.get(item.getId());
        if (summoningPouch == null) {
            return;
        }

        if (player.getSkills().getLevel(Skills.SUMMONING) < summoningPouch.getSummonCost()) {
            player.getActionSender().sendMessage("You need at least " + summoningPouch.getSummonCost() + " Summoning points to summon this familiar.");
            return;
        }
        final int npcId = summoningPouch.getNpcId();
        Familiar fam = FAMILIARS.get(npcId);
        if (fam == null) {
            player.getActionSender().sendMessage("Invalid familiar " + npcId + ", report it on the forums.");
            return;
        }
        fam = fam.construct(player, npcId);
        if (fam.getSpawnLocation() == null) {
            player.getActionSender().sendMessage("The spirit in this pouch is too big to summon here. You will need to move to a larger");
            player.getActionSender().sendMessage("area.");
            return;
        }
        if (!player.getInventory().remove(item)) {
            return;
        }
        player.getSkills().updateLevel(Skills.SUMMONING, -summoningPouch.getSummonCost(), 0);
        player.getSkills().addExperience(Skills.SUMMONING, summoningPouch.getSummonExperience());
        familiar = fam;
        spawnFamiliar(true);
        if (player.getSkullManager().isWilderness()) {
            player.getAppearance().sync();
        }
    }

    /**
     * Summons a familiar.
     *
     * @param item the item.
     * @param pet  the pet.
     */
    public void summon(final Item item, boolean pet, boolean animation) {
        summon(item, pet, true, animation);
    }

    /**
     * Summons a pet.
     *
     * @param item       the item.
     * @param deleteItem Whether or not to delete the item.
     */
    private boolean summonPet(final Item item, boolean deleteItem, boolean animation) {
        final int itemId = item.getId();
        Pets pets = Pets.forId(itemId);
        if (pets == null) {
            return false;
        }
        if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pets.getSummoningLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a summoning level of " + pets.getSummoningLevel() + " to summon this.");
            return false;
        }
        int baseItemId = pets.getBabyItemId();
        PetDetails details = petDetails.get(baseItemId);
        if (details == null) {
            details = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
            petDetails.put(baseItemId, details);
        }
        int id = pets.getItemId(details.getStage());
        if (itemId != id) {
            player.getActionSender().sendMessage("This is not the right pet, grow the pet correctly.");
            return true;
        }
        int npcId = pets.getNpcId(details.getStage());
        if (npcId > 0) {
            familiar = new Pet(player, details, itemId, npcId);
            if (deleteItem) {
                if (animation) {
                    player.animate(new Animation(827));
                }
                player.getInventory().remove(item);
            }
            spawnFamiliar(true);
            return true;
        }
        return true;
    }

    /**
     * Spawns the current familiar.
     *
     * @param open
     */
    public void spawnFamiliar(boolean open) {
        familiar.init();
        if (open) {
            player.getInterfaceState().openTab(Sidebar.SUMMONING_TAB, new Component(25904));
            player.getInterfaceState().setCurrentTabIndex(Sidebar.SUMMONING_TAB.ordinal());
            player.getActionSender().sendSidebarTab(Sidebar.SUMMONING_TAB.ordinal());
        }
    }

    /**
     * Dumps the bob.
     */
    public void dumpBob() {
        if (!hasFamiliar()) {
            player.getActionSender().sendMessage("You don't have a familiar.");
            return;
        }
        Familiar familiar = getFamiliar();
        if (!familiar.isBurdenBeast()) {
            player.getActionSender().sendMessage("Your familiar is not a beast of burden.");
            return;
        }
        BurdenBeast beast = ((BurdenBeast) familiar);
        if (!player.getBank().hasSpaceFor(beast.getContainer())) {
            player.getActionSender().sendMessage("There is not enough space left in your bank.");
            return;
        }
        for (Item item : beast.getContainer().toArray()) {
            if (item == null) {
                continue;
            }
            player.getBank().add(item);
        }
        player.getBank().update();
        player.getBank().refresh();
        beast.getContainer().clear();
    }

    /**
     * Makes the pet eat.
     *
     * @param foodId The food item id.
     * @param npc    The pet NPC.
     */
    public void eat(int foodId, Pet npc) {
        if (npc != familiar) {
            player.getActionSender().sendMessage("This isn't your pet!");
            return;
        }
        Pet pet = (Pet) familiar;
        Pets pets = Pets.forId(pet.getItemId());
        if (pets == null) {
            return;
        }
        for (int food : pets.getFood()) {
            if (food == foodId) {
                player.getInventory().remove(new Item(foodId));
                player.getActionSender().sendMessage("Your pet happily eats the " + ItemDefinition.forId(food).getName() + ".");
                player.animate(new Animation(827));
                npc.getDetails().updateHunger(-15.0);
                return;
            }
        }
        player.getActionSender().sendMessage("Nothing interesting happens.");
    }

    /**
     * Picks up a pet.
     */
    public void pickup() {
        if (player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough room in your inventory.");
            return;
        }
        Pet pet = ((Pet) familiar);
        PetDetails details = pet.getDetails();
        if (player.getInventory().add(new Item(pet.getPet().getItemId(details.getStage())))) {
            player.animate(Animation.create(827));
            player.getFamiliarManager().dismiss();
        }
    }

    /**
     * Adjusts the battle state.
     *
     * @param state the state.
     */
    public void adjustBattleState(final BattleState state) {
        if (!hasFamiliar()) {
            return;
        }
        familiar.adjustPlayerBattle(state);
    }

    /**
     * Gets a boost from a familiar.
     *
     * @param skill the skill.
     * @return the boosted level.
     */
    public int getBoost(int skill) {
        if (!hasFamiliar()) {
            return 0;
        }
        return familiar.getBoost(skill);
    }

    /**
     * Checks if the player has an active familiar.
     *
     * @return {@code True} if so.
     */
    public boolean hasFamiliar() {
        return familiar != null;
    }

    /**
     * Checks if the player has an active familiar and is a pet.
     *
     * @return {@code True} if so.
     */
    public boolean hasPet() {
        return hasFamiliar() && familiar instanceof Pet;
    }

    /**
     * Dismisses the familiar.
     *
     * @param saveDetails Whether or not to save the details of a pet.
     */
    public void dismiss(boolean saveDetails) {
        dismiss(saveDetails, false);
    }

    public void dismiss(boolean saveDetails, boolean playerDeath) {
        if (hasPet() && !saveDetails) {
            removeDetails(((Pet) familiar).getItemId());
        }
        if (hasFamiliar()) {
            if (hasPet() && playerDeath) {
                ((Pet) familiar).deathFromPlayerDeath();
            } else {
                familiar.dismiss();
            }
        }
    }

    /**
     * Dismisses the familiar.
     */
    public void dismiss() {
        dismiss(true);
    }

    /**
     * Removes the details for this pet.
     *
     * @param itemId The item id of the pet.
     */
    public void removeDetails(int itemId) {
        Pets pets = Pets.forId(itemId);
        if (pets == null) {
            return;
        }
        petDetails.remove(pets.getBabyItemId());
    }

    /**
     * Checks if it's the owner of a familiar.
     *
     * @param familiar the familiar
     * @return {@code True} if so.
     */
    public boolean isOwner(Familiar familiar) {
        if (!hasFamiliar()) {
            return false;
        }
        if (this.familiar != familiar) {
            player.getActionSender().sendMessage("This is not your familiar.");
            return false;
        }
        return true;
    }

    /**
     * Gets the {@link org.gielinor.game.content.skill.member.summoning.pet.PetDetails}.
     *
     * @return The pet details.
     */
    public Map<Integer, PetDetails> getPetDetails() {
        return petDetails;
    }

    /**
     * Gets the familiar.
     *
     * @return The familiar.
     */
    public Familiar getFamiliar() {
        return familiar;
    }

    /**
     * Sets the familiar.
     *
     * @param familiar The familiar to set.
     */
    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    /**
     * Gets the familiars.
     *
     * @return The familiars.
     */
    public static Map<Integer, Familiar> getFamiliars() {
        return FAMILIARS;
    }

    /**
     * Gets the usingSummoning.
     *
     * @return The usingSummoning.
     */
    public boolean isUsingSummoning() {
        return hasPouch || hasFamiliar();
    }

    /**
     * Gets the hasPouch.
     *
     * @return The hasPouch.
     */
    public boolean isHasPouch() {
        return hasPouch;
    }

    /**
     * Sets the hasPouch.
     *
     * @param hasPouch The hasPouch to set.
     */
    public void setHasPouch(boolean hasPouch) {
        this.hasPouch = hasPouch;
    }

    /**
     * Gets the summoningCombatLevel.
     *
     * @return The summoningCombatLevel.
     */
    public int getSummoningCombatLevel() {
        return summoningCombatLevel;
    }

    /**
     * Sets the summoningCombatLevel.
     *
     * @param summoningCombatLevel The summoningCombatLevel to set.
     */
    public void setSummoningCombatLevel(int summoningCombatLevel) {
        this.summoningCombatLevel = summoningCombatLevel;
    }

    /**
     * Sends the left-click options.
     */
    public void sendLeftClickOptions() {
        if (hasFamiliar()) {
            getFamiliar().sendLeftClickOptions();
        }
        switch (player.getSavedData().getGlobalData().getLeftClickOption()) {
            case "Follower details":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 1, false);
                break;

            case "Special attack":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 2, false);
                break;

            case "Attack":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 3, false);
                break;

            case "Call follower":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 4, false);
                break;

            case "Dismiss follower":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 5, false);
                break;

            case "Take BoB":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 6, false);
                break;

            case "Renew familiar":
                player.getInterfaceState().force(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION, 7, false);
                break;
        }
    }

    /**
     * Makes the player's familiar attack a target.
     *
     * @param entity The target.
     */
    public void attack(Entity entity) {
        if (entity == getFamiliar()) {
            return;
        }
        if (!getFamiliar().isCombatFamiliar()) {
            player.getActionSender().sendMessage("Your familiar cannot attack others.");
            return;
        }
        if (player.getProperties().getCombatPulse() == null || !player.getProperties().getCombatPulse().isInCombat()) {
            player.getActionSender().sendMessage("You must be in combat for your familiar to attack them.");
            return;
        }
        if (entity.getSkills().getLifepoints() < 1) {
            return;
        }
        if (entity instanceof NPC) {
            NPC npc = ((NPC) entity);
            Task task = npc.getTask();
            if (task != null && task.getLevel() > player.getSkills().getStaticLevel(Skills.SLAYER)) {
                player.getActionSender().sendMessage("You need a Slayer level of " + task.getLevel() + " to attack this creature.");
                return;
            }
            if (npc.getDefinition().getName().toLowerCase().contains("nechry") && 80 > player.getSkills().getStaticLevel(Skills.SLAYER)) {
                player.getActionSender().sendMessage("You need a Slayer level of 80 to attack this creature.");
                return;
            }
            npc.getWalkingQueue().reset();
        }
        if ((entity instanceof Player) && !Ironman.canAttack(player, (Player) entity)) {
            return;
        }
        getFamiliar().attack(entity);
    }
}