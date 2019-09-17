package org.gielinor.game.content.skill.member.summoning.pet;

import java.util.Collections;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.member.summoning.InterfaceType;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.SummoningInformationContext;
import org.gielinor.net.packet.out.SummoningInformationPacket;

/**
 * Represents a pet.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class Pet extends Familiar {

    /**
     * The item id.
     */
    private final int itemId;

    /**
     * The pet details.
     */
    private final PetDetails details;

    /**
     * The growth rate of the pet.
     */
    private double growthRate;

    /**
     * The pets type.
     */
    private final Pets pet;

    /**
     * Constructs a new {@code Pet} {@code Object}.
     *
     * @param owner   the owner.
     * @param details the details.
     * @param itemId  the itemId.
     * @param id      the id.
     */
    public Pet(Player owner, final PetDetails details, int itemId, int id) {
        super(owner, id, -1, -1, -1);
        this.pet = Pets.forId(itemId);
        this.details = details;
        this.itemId = itemId;
        this.growthRate = pet.getGrowthRate();
    }

    @Override
    public void sendConfiguration() {
        owner.getInterfaceState().set(InterfaceConfiguration.SUMMONING_SPECIAL_MOVE, 0);
        owner.getActionSender().sendNpcOnInterface(getOriginalId(), 25906, 0);
        owner.getActionSender().sendAnimationInterface(8373, 25906, 0);//6550
        owner.getActionSender().sendString(25918, getName());
        PacketRepository.send(SummoningInformationPacket.class, new SummoningInformationContext(owner, "", "", 0, -1, 1, "", InterfaceType.CLICK));
        sendTimeRemaining();
        owner.getInterfaceState().set(InterfaceConfiguration.HAS_FAMILIAR, true);
        owner.getInterfaceState().force(351, 0, false);
        owner.getInterfaceState().force(352, 1, false);
        owner.getInterfaceState().force(isBurdenBeast() ? 51 : 50, 25931, false);
        owner.getInterfaceState().force(isBurdenBeast() ? 51 : 50, 25938, false);
        owner.getInterfaceState().force(50, 25915, false);
        owner.getInterfaceState().force(50, 25942, false);
        owner.getActionSender().sendString(25937, "Call pet");
        owner.getActionSender().sendString(25939, "Dismiss pet");
        owner.getActionSender().sendString(25940, "Pet hunger\\npercentage");
        owner.getActionSender().sendString(25941, "Pet size\\npercentage");
        sendLeftClickOptions();
    }

    @Override
    public void sendLeftClickOptions() {
        String leftClick = owner.getSavedData().getGlobalData().getLeftClickOption();
        String[] options = new String[]{
            "Follower details", "", "", "Call follower",
            "Dismiss follower", "", "", "Select left-click option"
        };
        int leftClickIndex = 0;
        for (String option : options) {
            if (option == null || option.isEmpty()) {
                continue;
            }
            if (option.equals(leftClick)) {
                break;
            }
            leftClickIndex++;
        }
        if (leftClickIndex > options.length) {
            return;
        }
        options[leftClickIndex] = options[0];
        options[0] = leftClick;
        owner.getActionSender().sendSummoningOptions(options);
        orbOptions.clear();
        Collections.addAll(orbOptions, options);
        Collections.reverse(orbOptions);
    }

    @Override
    public void sendTimeRemaining() {
        int hunger = (int) getDetails().getHunger();
        owner.getActionSender().sendString(25924, "<col=" + (hunger >= 80 ? "FF0000" : "FFFFFF") + ">" + hunger + "%");
        owner.getActionSender().sendString(25925, String.valueOf(((int) getDetails().getGrowth())) + "%");
    }

    @Override
    public void handleTickActions() {
        final PetDetails petDetails = details;
        if (getPet().getFood().length > 0) {
            petDetails.updateHunger(petDetails.getStage() == 0 ? 0.025 : 0.018);
        }
        double hunger = petDetails.getHunger();
        if (hunger >= 90.0 && hunger < 90.025) {
            owner.getActionSender().sendMessage("<col=FF0000>Your pet is starving, feed it before it runs off.</col>");
        } else if (hunger == 100.0) {
            owner.getFamiliarManager().dismiss(false);
            owner.getActionSender().sendMessage("Your pet has ran away to find some food!");
            return;
        }
        double growth = petDetails.getGrowth();
        if (pet.getGrowthRate() > 0.000) {
            petDetails.updateGrowth(pet.getGrowthRate());
            if (growth == 100.0) {
                growNextStage();
            }
        }
        if ((!isHidden() && owner.getLocation().getDistance(getLocation()) > 12) || (isHidden() && ticks % 25 == 0)) {
            if (!call()) {
                setHidden(true);
            }
        } else if (!getPulseManager().hasPulseRunning()) {
            startFollowing();
        }
        sendTimeRemaining();
    }

    public void deathFromPlayerDeath() {
        if (Pets.isBossPet(itemId)) {
            owner.getActionSender().sendMessage("Your boss pet has run away!");
            if (details.isInsured()) {
                owner.getSavedData().getGlobalData().getLostPets().add(getItemId());
                owner.getActionSender().sendMessage("Luckily for you, your pet is insured. Talk to Diango at home!");
            }
            dismiss();
        }
    }

    /**
     * Method used to grow the npc's next stage.
     */
    public final void growNextStage() {
        if (details.getStage() == 3) {
            return;
        }
        if (pet == null) {
            return;
        }
        int npcId = pet.getNpcId(details.getStage() + 1);
        if (npcId < 1) {
            return;
        }
        details.setStage(details.getStage() + 1);
        int itemId = pet.getItemId(details.getStage());
        if (pet.getNpcId(details.getStage() + 1) > 0) {
            details.updateGrowth(-100.0);
        }
        clear();
        Pet newPet = new Pet(owner, details, itemId, npcId);
        newPet.growthRate = growthRate;
        owner.getFamiliarManager().setFamiliar(newPet);
        owner.getActionSender().sendMessage("<col=FF0000>Your pet has grown larger.</col>");
        owner.getFamiliarManager().spawnFamiliar(false);
    }

    public final boolean insure() {
        if (owner.getSavedData().getGlobalData().getPetsInsured() < 5) {
            details.setInsured(true);
            owner.getSavedData().getGlobalData().incrementPetsInsured();
            owner.getActionSender().sendMessage("Your " + getName() + " is now insured with pet insurance!");
            owner.getActionSender().sendMessage("You have " + owner.getSavedData().getGlobalData().getPetsInsured() + "/5 possible pets insured.");
            return true;
        }
        return false;
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return this;
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public boolean isCombatFamiliar() {
        return false;
    }

    /**
     * Gets the growthRate.
     *
     * @return The growthRate.
     */
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * Sets the growthRate.
     *
     * @param growthRate The growthRate to set.
     */
    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    /**
     * Gets the itemId.
     *
     * @return The itemId.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the details.
     *
     * @return The details.
     */
    public PetDetails getDetails() {
        return details;
    }

    /**
     * Gets the pet.
     *
     * @return The pet.
     */
    public Pets getPet() {
        return pet;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 761, 762, 763, 764, 765, 766, 3505, 3598, 6969, 7259, 7260,
            6964, 7249, 7251, 6960, 7241, 7243, 6962, 7245, 7247, 6966, 7253, 7255, 6958,
            7237, 7239, 6915, 7277, 7278, 7279, 7280, 7018, 7019, 7020, 6908, 7313, 7316, 6947,
            7293, 7295, 7297, 7299, 6911, 7261, 7263, 7265, 7267, 7269, 6919, 7301, 7303,
            7305, 7307, 6949, 6952, 6955, 6913, 7271, 7273, 6945, 7319, 7321, 7323, 7325,
            7327, 6922, 6942, 7210, 7212, 7214, 7216, 7218, 7220, 7222, 7224, 7226, 6900,
            6902, 6904, 6906, 768, 769, 770, 771, 772, 773, 3504, 6968, 7257, 7258, 6965,
            7250, 7252, 6961, 7242, 7244, 6963, 7246, 7248, 6967, 7254, 7256, 6859, 7238,
            7240, 6916, 7281, 7282, 7283, 7284, 7015, 7016, 7017, 6909, 7314, 7317, 6948, 7294,
            7296, 7298, 7300, 6912, 7262, 7264, 7266, 7268, 7270, 6920, 7302, 7304, 7306, 7308,
            6950, 6953, 6956, 6914, 7272, 7274, 6946, 7320, 7322, 7324, 7326, 7328, 6923, 6943,
            7211, 7213, 7215, 7217, 7219, 7221, 7223, 7225, 7227, 6901, 6903, 6905, 6907, 774,
            775, 776, 777, 778, 779, 3503, 6951, 6954, 6957, 6407,
            NPCDefinition.TZREK_JAD,
            NPCDefinition.GENERAL_GRAARDOR_JR, NPCDefinition.KRIL_TSUTSAROTH_JR, NPCDefinition.KREE_ARRA_JR, NPCDefinition.ZILYANA_JR };
    }

}
