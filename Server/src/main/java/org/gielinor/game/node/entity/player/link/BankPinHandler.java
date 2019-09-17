package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ChildPositionContext;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.context.StringContext498;
import org.gielinor.net.packet.out.RepositionChild;
import org.gielinor.net.packet.out.StringPacket498;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The bank pin handling class.
 *
 * @author Emperor
 * @author Aero
 */
public final class BankPinHandler {

    /**
     * Array of child ids to remove.
     */
    private static final int[][] REMOVE_CHILDS = {
        { 60, 61, 65 }, //The player has a bank PIN.
        { 60, 61, 62, 63 }, //The player has a pending bank PIN.
        { 62, 63, 64, 65 } //The player has no bank PIN set.
    };

    /**
     * The bank pin messages.
     */
    private static final String[] SETTINGS_MESSAGES = {
        "Messages", //title
        null,
        null,
        null,
        "Customers are reminded that",
        "they should NEVER tell",
        "anyone their Bank PINs or",
        "passwords, nor should they",
        "ever enter their PINs on any",
        "website form.",
        null,
        "Have you read the PIN guide",
        "on the website?",

    };

    /**
     * The bank pin messages.
     */
    private static final String[] EASY_GUESS_SETTING = {
        "Messages", //title
        null,
        null,
        null,
        null,
        null,
        null,
        "That number wouldn't be very",
        "hard to guess. Please try",
        "something different!",
        null,
        null,
        null,

    };

    /**
     * The bank pin messages.
     */
    private static final String[] NO_MATCH_SETTING = {
        "Messages", //title
        null,
        null,
        null,
        null,
        null,
        "Those numbers did not",
        "match.",
        null,
        "Your PIN has not been set;",
        "please try again if you wish to",
        "set a new PIN.",
        null,

    };

    /**
     * The bank PIN canceled message.
     */
    private static final String[] PIN_CANCELED_MESSAGE = {
        "Messages", //title
        "The PIN has been cancelled",
        "and will NOT be set.",
        "",
        "You still do not have a Bank",
        "PIN.",
        "",
        "",
        ""
    };

    /**
     * The random instance.
     */
    private static final Random RANDOM = new Random();

    /**
     * The player.
     */
    private final Player player;

    /**
     * The digits.
     */
    private final List<Integer> digits = new ArrayList<>();

    /**
     * The player's bank pin.
     */
    private String bankPin;

    /**
     * The current bank pin.
     */
    private String currentPin = "";

    /**
     * The amount of recovery delay (in days).
     */
    private int recoveryDelay = 3;

    /**
     * The component to open when finished.
     */
    private Component component;

    /**
     * If the player has entered his pin.
     */
    private boolean enteredPin;

    /**
     * The bank pin stage.
     */
    private int stage;

    /**
     * The amount of tries the player has done.
     */
    private int tries = 0;

    /**
     * The PIN lock ticks.
     */
    private int pinLockTicks = 0;

    /**
     * The pending PIN flag.
     */
    private boolean pendingPin;

    /**
     * If a bank pin is being set.
     */
    private boolean settingPin;

    /**
     * The changing PIN flag.
     */
    private boolean changingPin;

    /**
     * The deleting PIN flag.
     */
    private boolean deletingPin;

    /**
     * The messages to display.
     */
    private String[] messages = SETTINGS_MESSAGES;

    /**
     * Constructs a new {@code BankPinHandler} {@code Object}.
     *
     * @param player The player.
     */
    public BankPinHandler(Player player) {
        this.player = player;
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }
    }

    /**
     * Opens the settings interface.
     */
    public void openSettings() {
        if (pendingPin) {
            int pendingD = calculatePinPendingDays();
            if (pendingD < 1) {
                resetPendingPinPeriod();
                messages = SETTINGS_MESSAGES;
            } else {
                messages = new String[]{ "Messages", "You have requested that a", "PIN be set on your bank", "account. This will take effect", "in another " + pendingD + " days.", null, "If you wish to cancel this", "PIN, please use the button", "on the left." };
            }
        } else if (!settingPin) {
            messages = SETTINGS_MESSAGES;
        }
        Component c = new Component(14);
        if (c.getDefinition().getContext() == null) {
            c.getDefinition().setContext(new InterfaceContext(null, 548, 77, 14, true));
        }
        player.getInterfaceState().open(c);
        refresh();
    }

    /**
     * Refreshes the interface.
     */
    public void refresh() {
        PacketRepository.send(StringPacket498.class, new StringContext498(player, (pendingPin ? "PIN coming soon" : (!hasPin() ? "No PIN set" : "You have a PIN")), 14, 69));
        PacketRepository.send(StringPacket498.class, new StringContext498(player, recoveryDelay + " days", 14, 71));
        int[] removeIds = null;
        if (pendingPin) {
            removeIds = REMOVE_CHILDS[1];
        } else if (hasPin() || false) {
            removeIds = REMOVE_CHILDS[0];
        } else { //No bank pin set or pending.
            removeIds = REMOVE_CHILDS[2];
        }
        for (int child : removeIds) {
            player.getActionSender().sendHideComponent(14, child, true);
        }
        player.getActionSender().sendHideComponent(14, 89, true); //Remove confirm option (second screen)
        player.getActionSender().sendHideComponent(14, 91, true); //Remove cancel option (second screen)
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            if (message == null) {
                continue;
            }
            PacketRepository.send(StringPacket498.class, new StringContext498(player, message, 14, 41 + i));
        }
    }

    /**
     * Opens the set bank PIN confirmation screen.
     *
     * @param show If we should show the confirm screen.
     */
    public void toggleConfirmInterface(boolean show) {
        for (int i = 60; i < 66; i++) {
            player.getActionSender().sendHideComponent(14, i, show);
        }
        player.getActionSender().sendHideComponent(14, 89, !show);
        player.getActionSender().sendHideComponent(14, 91, !show);
        player.getActionSender().sendHideComponent(14, 87, !show);
    }

    /**
     * Opens the bank pin interface.
     *
     * @return <code>True</code> if the pin interface opened.
     */
    public boolean open(Component c) {
        this.component = c;
        if ((bankPin == null && !settingPin) || (enteredPin && !deletingPin)) {//TODO: Aero comment even tho the server thinks the player has a bank pin if it's pending still can access bank normally...
            return false;
        }
        if (pendingPin) {
            player.getActionSender().sendMessage("PIN coming soon.");
            return false;
        }
        if (settingPin) {
            if (tries == 1) {
                player.getActionSender().sendString498("Confirm new PIN", 13, 31);
                player.getActionSender().sendString498("Now please enter that number again.", 13, 28);
            } else {
                player.getActionSender().sendHideComponent(13, 29, true);
                player.getActionSender().sendString498("Set new PIN", 13, 31);
                player.getActionSender().sendString498("Please choose a new FOUR DIGIT PIN using the buttons below.", 13, 28);
            }
            if (c == null) {
                setStage(0);
                return true;
            }
        } else {
            player.getActionSender().sendString498("Enter your PIN", 13, 31);
        }
        currentPin = "";
        player.getInterfaceState().open(new Component(13).setCloseEvent(new CloseEvent() {

            // TODO bank pin handler
            @Override
            public void close(Player player, Component component) {
                if (canClose(player, component)) {
                    BankPinHandler.this.component.open(player);
                    player.getInterfaceState().setOpened(BankPinHandler.this.component);
                    return;
                }
                if (component.getId() == 536) {
                    player.getBank().open();
                } else if (component.getId() == 13) {
                    if (settingPin) {
                        settingPin = false;
                    } else {
                        openSettings();
                    }
                }
            }

            @Override
            public boolean canClose(Player player, Component component) {
                if (component != null && (enteredPin || bankPin == null)) {
                    if (component.getId() != 536 && component.getId() != 105 && component.getId() != 13) {
                        component.open(player);
                        player.getInterfaceState().setOpened(component);
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }));
        setStage(0);
        return true;
    }

    /**
     * Updates the current bank pin.
     *
     * @param digit The digit index.
     */
    public void update(int digit) {
        currentPin += digits.get(digit);
        setStage(++stage);
    }

    /**
     * Sets the current stage.
     *
     * @param stage The bank pin stage.
     */
    public void setStage(int stage) {
        if (stage == 4) {
            if (component != null && component.getId() == 13 && !settingPin) {
                bankPin = "";
            } else {
                if (settingPin) {
                    securePinSetting();
                    return;
                }
                if (!currentPin.equals(bankPin)) {
                    player.getActionSender().sendSound(new Audio(1042));
                    player.getActionSender().sendMessage("The PIN you entered is incorrect.");
                    player.getInterfaceState().close();
                    if (++tries > 2) {
                        setPinLock();
                    }
                    return;
                }
            }
            tries = 0;
            player.getActionSender().sendSound(new Audio(1040));
            player.getActionSender().sendMessage("You have correctly entered your PIN.");
            enteredPin = true;
            if (component != null && component.getId() != 13) {
                Component c = player.getInterfaceState().getOpened();
                player.getInterfaceState().setOpened(null);
                c.close(player);
            } else {
                player.getInterfaceState().close();
            }
            return;
        }
        player.getActionSender().sendSound(new Audio(1041));
        this.stage = stage;
        Collections.shuffle(digits, RANDOM);
        int bitShift = 0, bitValue = 0;
        for (int i = 0; i < 8; i++) {
            bitValue |= digits.get(i) << bitShift;
            bitShift += 4;
        }
        player.getConfigManager().set(562, bitValue);
        player.getConfigManager().set(563, digits.get(8) | digits.get(9) << 4 | stage << 26);
        for (int i = 0; i < 9; i++) {
            int child = (i > 2 ? i + 1 : i) + 11;
            int positionX = 37 + ((i % 3) * 95) + RandomUtil.random(2, 45);
            int positionY = 157 + ((i / 3) * 70) - RandomUtil.random(3, 48);
            PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 13, child, positionX, positionY));
        }
        PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 13, 14, 308 + RandomUtil.random(2, 45), 155 - RandomUtil.random(3, 45)));
    }

    /**
     * Secures & handles the setting of a new PIN.
     */
    private void securePinSetting() {
        stage = 0;
        boolean badCombo = true;
        for (int i = 0; i < 4; i++) {
            if (currentPin.charAt(0) != currentPin.charAt(i)) {
                badCombo = false;
                break;
            }
        }
        if (!badCombo) {
            badCombo = true;
            for (int i = 0; i < 3; i++) {
                int value = (byte) currentPin.charAt(i);
                int next = (byte) currentPin.charAt(i + 1);
                if (!((char) (value + 1) == next || (char) (value - 1) == next)) {
                    badCombo = false;
                    break;
                }
            }
        }
        if (badCombo) {
            messages = EASY_GUESS_SETTING;
            component = null;
            openSettings();
            settingPin = false;
            currentPin = "";
            tries = 0;
            return;
        }
        if (++tries > 1 && settingPin) {
            if (!currentPin.substring(0, 4).equals(currentPin.substring(4, 8))) {
                messages = NO_MATCH_SETTING;
            } else {
                pendingPin = true;
                player.saveAttribute("pin_pending_days", System.currentTimeMillis());
                bankPin = currentPin.substring(0, 4);
                messages = new String[]{ "Messages", "You have requested that a", "PIN be set on your bank", "account. This will take effect", "in another " + calculatePinPendingDays() + " days.", null, "If you wish to cancel this", "PIN, please use the button", "on the left." };
            }
            component = null;
            openSettings();
            settingPin = false;
            currentPin = "";
            tries = 0;
            return;
        }
        open(null);
        //		setStage(0);
    }

    /**
     * Changes the recovery delay.
     */
    public void changeRecoveryDelay() {
        final int newDelay = recoveryDelay == 3 ? 7 : 3;
        messages = new String[]{ "Messages", "Your recovery delay has", "now been set to " + newDelay + " days.", null, "You would have to wait this", "long to delete your PIN if", "you forgot it. But you", "haven't got one..." };
        setRecoveryDelay(newDelay);
        for (int i = 0; i < 13; i++) {
            player.getActionSender().sendString498("", 14, 41 + i);
        }
        refresh();
    }

    /**
     * Resets the pending PIN period.
     */
    private void resetPendingPinPeriod() {
        pendingPin = false;
        player.removeAttribute("pin_pending_days");
    }

    /**
     * Cancels the current pending PIN.
     */
    public void cancelPendingPin() {
        messages = PIN_CANCELED_MESSAGE;
        pendingPin = false;
        player.removeAttribute("pin_pending_days");
        bankPin = null;
        Component c = new Component(14);
        if (c.getDefinition().getContext() == null) {
            c.getDefinition().setContext(new InterfaceContext(null, 548, 77, 14, true));
        }
        player.getInterfaceState().open(c);
        refresh();
    }

    /**
     * Sets a PIN lock for a player, stopping them from accessing their bank account.
     */
    private void setPinLock() {
        player.saveAttribute("pin_lock_start", System.currentTimeMillis());
        pinLockTicks = getPinLockTicks();
        int time = calculatePinLockTimeLeft(pinLockTicks);
        String suffix = (pinLockTicks > 25 ? "minutes" : "seconds");
        player.getDialogueInterpreter().sendPlaneMessage("You will be able to access your bank in " + time + " " + suffix + ".");
    }

    /**
     * Displays the bank PIN lock message.
     */
    public void displayPinLockMessage() {
        int time = calculatePinLockTimeLeft(pinLockTicks);
        String suffix = (pinLockTicks > 25 ? "minutes" : "seconds");
        player.getDialogueInterpreter().sendPlaneMessage("Please wait another " + time + " " + suffix + " before trying this again.");
    }

    /**
     * Resets the PIN lock for a player.
     */
    public void resetPinLock() {
        pinLockTicks = 0;
        player.removeAttribute("pin_lock_start");
    }

    /**
     * Checks if saving is needed.
     *
     * @return <code>True</code> if so.
     */
    public boolean saveNeeded() {
        return hasPin();
    }

    /**
     * Checks to see if a player is PIN blocked from opening their bank account.
     *
     * @return True if the player is PIN blocked.
     */
    public boolean isPinBlocked() {
        return (calculatePinLockTimeLeft(getPinLockTicks()) > 0);
    }

    /**
     * Gets the PIN lock ticks.
     *
     * @return The PIN lock ticks.
     */
    private int getPinLockTicks() {
        if (tries == 3) {
            return 17;
        } else if (tries == 4) {
            return 25;
        } else if (tries > 4) {
            return 1000;
        }
        return 0;
    }

    /**
     * Gets the PIN lock ticks.
     *
     * @return The PIN lock ticks.
     */
    public int getPinLockTick() {
        return pinLockTicks;
    }

    /**
     * Sets the PIN lock ticks.
     *
     * @param pinLockTicks The PIN lock ticks.
     */
    public void setPinLockTicks(int pinLockTicks) {
        this.pinLockTicks = pinLockTicks;
    }

    /**
     * Gets the PIN pending days left.
     *
     * @return The pin pending days left.
     */
    public int calculatePinPendingDays() {
        long startTime = player.getAttribute("pin_pending_days", 0L);
        long elapsedTime = (System.currentTimeMillis() - startTime);
        long elapsedDays = elapsedTime / (1000 * 60 * 60 * 60 * 24);
        return (int) (7 - elapsedDays);
    }

    /**
     * Gets the PIN lock elapse time.
     *
     * @param ticks The amount of ticks the player is locked for.
     * @return The PIN lock elapse time.
     */
    private int calculatePinLockTimeLeft(int ticks) {
        long startTime = player.getAttribute("pin_lock_start", 0L);
        long elapsedTime = (System.currentTimeMillis() - startTime);
        long elapsedMinutes = elapsedTime / (1000 * 60);
        elapsedTime = elapsedTime % (1000 * 60);
        long elapsedSeconds = (elapsedTime / 1000);
        if (startTime == 0L) {
            resetPinLock();
            return -1;
        }
        if (ticks > 25) {
            return (int) (10 - elapsedMinutes);
        }
        return (int) (Math.floor((ticks * 0.6)) - elapsedSeconds);
    }

    /**
     * Gets the bank pin data from the buffer.
     *
     * @param buffer The buffer.
     */
    public void parse(ByteBuffer buffer) {
        int opcode;
        while ((opcode = buffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    pendingPin = (buffer.get() == 1);
                    break;
                case 2:
                    bankPin = "";
                    for (int i = 0; i < 4; i++) {
                        bankPin += buffer.get();
                    }
                    break;
                case 3:
                    pinLockTicks = buffer.get();
                    break;
            }
        }
    }

    /**
     * Writes the data to the byte buffer.
     *
     * @param buffer The buffer.
     */
    public void save(ByteBuffer buffer) {
        buffer.put((byte) 1);
        buffer.put((byte) (pendingPin ? 1 : 0));
        if (bankPin != null) {
            buffer.put((byte) 2);
            for (int i = 0; i < 4; i++) {
                buffer.put(Byte.parseByte(String.valueOf(bankPin.charAt(i))));
            }
        }
        if (pinLockTicks > 0) {
            buffer.put((byte) 3);
            buffer.put((byte) pinLockTicks);
        }
        buffer.put(((byte) 0));
    }

    /**
     * Checks to see if a player has entered their pin.
     *
     * @return True if the player has entered their pin.
     */
    public boolean hasEnteredPin() {
        return enteredPin;
    }

    /**
     * Gets the bank pin.
     * <p>
     * return The bank pin.
     */
    public String getPin() {
        return bankPin;
    }

    /**
     * Sets the bank pin.
     *
     * @param bankPin The bank pin to set.
     */
    public void setPin(String bankPin) {
        this.bankPin = bankPin;
    }

    /**
     * Checks if the player has a pin.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasPin() {
        return bankPin != null;
    }

    /**
     * Gets the recoveryDelay.
     *
     * @return The recoveryDelay.
     */
    public int getRecoveryDelay() {
        return recoveryDelay;
    }

    /**
     * Sets the recoveryDelay.
     *
     * @param recoveryDelay The recoveryDelay to set.
     */
    public void setRecoveryDelay(int recoveryDelay) {
        this.recoveryDelay = recoveryDelay;
    }

    /**
     * Sets the settingPin flag.
     *
     * @param settingPin If the player is going to set a bank pin.
     */
    public void setSettingPin(boolean settingPin) {
        this.settingPin = settingPin;
    }

    /**
     * Gets the pendingPin.
     *
     * @return The pendingPin.
     */
    public boolean isPendingPin() {
        return pendingPin;
    }

    /**
     * Sets the pendingPin.
     *
     * @param pendingPin The pendingPin to set.
     */
    public void setPendingPin(boolean pendingPin) {
        this.pendingPin = pendingPin;
    }

    /**
     * Gets the deletingPin.
     *
     * @return The deletingPin.
     */
    public boolean isDeletingPin() {
        return deletingPin;
    }

    /**
     * Sets the deletingPin.
     *
     * @param deletingPin The deletingPin to set.
     */
    public void setDeletingPin(boolean deletingPin) {
        this.deletingPin = deletingPin;
    }

    /**
     * Gets the changingPin.
     *
     * @return The changingPin.
     */
    public boolean isChangingPin() {
        return changingPin;
    }

    /**
     * Sets the changingPin.
     *
     * @param changingPin The changingPin to set.
     */
    public void setChangingPin(boolean changingPin) {
        this.changingPin = changingPin;
    }

}
