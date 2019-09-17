package org.gielinor.game.content.anticheat;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.PluginManifest;
import org.gielinor.rs2.plugin.PluginType;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the dialogue of an anti macro npc.
 *
 * @author Vexia
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class AntiMacroDialogue extends DialoguePlugin {

    /**
     * The anti macro event.
     */
    protected AntiMacroEvent event;

    /**
     * Constructs a new {@code AntiMacroDialogue} {@code Object}.
     */
    public AntiMacroDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code AntiMacroDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public AntiMacroDialogue(final Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!player.getAntiMacroHandler().isNPC(npc, true)) {
            end();
            return false;
        }
        if (player.getAntiMacroHandler().getEvent() == null) {
            return false;
        }
        if (asAme().inCombat()) {
            asAme().getProperties().getCombatPulse().stop();
        }

        setEvent(player.getAntiMacroHandler().getEvent());
        return true;
    }

    /**
     * Wrapper method for waving the npc good bye.
     */
    public void wave() {
        wave(Animation.create(863));
    }

    /**
     * Waves the npc good bye.
     *
     * @param wave The wave animation.
     */
    public void wave(Animation wave) {
        end();
        npc.lock();
        if (wave != null) {
            npc.animate(wave);
        }
        World.submit(new Pulse(4, npc, player) {

            @Override
            public boolean pulse() {
                ((AntiMacroNPC) npc).clear();
                player.getActionSender().sendPositionedGraphic(86, 0, 1, npc.getLocation());
                return true;
            }
        });
        event.terminate();
    }

    /**
     * Gets the npc as an AME.
     *
     * @return the ame.
     */
    public AntiMacroNPC asAme() {
        return ((AntiMacroNPC) npc);
    }

    /**
     * Gets the event.
     *
     * @return The event.
     */
    public AntiMacroEvent getEvent() {
        return event;
    }

    /**
     * Sets the event.
     *
     * @param event The event to set.
     */
    public void setEvent(AntiMacroEvent event) {
        this.event = event;
    }

}
