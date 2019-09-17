package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

import plugin.npc.AutoSpawnNPC;
import plugin.npc.CurseZombieNPC;


public class SurokMagis extends DialoguePlugin {

    public SurokMagis() {

    }

    public SurokMagis(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5835 };
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length == 2 && args[1] instanceof Boolean && ((Boolean) args[1])) {
            npc("Pity on you, " + TextUtils.formatDisplayName(player.getUsername()) + "!", "Did you really think you could stop my", "curse of the undead from spreading?!", "You're wasting my time and yours!");
            stage = 1;
            return true;
        }
        npc("Well, " + TextUtils.formatDisplayName(player.getUsername()) + " prepare to die like the rest!");
        stage = END;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        int buttonId = -1;
        if (optionSelect != null) {
            buttonId = optionSelect.getButtonId();
        }
        switch (stage) {
            case 0:
                player("I'm always prepared!", "I just won't be the one dying today!");
                stage = END;
                break;
            case 1:
                player("So it was you!", "You're pure evil, if I had the chance,", "I would put a stop to you myself!");
                stage = 2;
                break;
            case 2:
                npc("You could try " + TextUtils.formatDisplayName(player.getUsername()) + "!", "You are no match for my minions!", "They have no minds of their own, they would", "not think twice about killing you!");
                stage = 3;
                break;
            case 3:
                npc("Yes! Minions! Their only weakness is a cold, freezing", "barrage spell!");
                stage = 4;
                break;
            case 4:
                npc("If you dare challenge me, you will never succeed, the", "curse will never be broken!");
                stage = 5;
                break;
            case 5:
                interpreter.sendOptions("Teleport to Surok?", "Yes! I am ready.", "No! I am not ready!");
                stage = 6;
                break;
            case 6:
                switch (buttonId) {
                    case 1:
                        end();
                        setupBattle();
                        player.setAttribute("SUROK_ORIGINAL", player.getLocation());
                        player.getPulseManager().run(new Pulse(1) {

                            @Override
                            public boolean pulse() {
                                player.getProperties().setTeleportLocation(Location.create(2399, 9607, player.getIndex() * 4));
                                interpreter.sendPlaneMessage("You teleport to Surok!");
                                return true;
                            }
                        });
                        return true;
                    case 2:
                        end();
                        return true;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SurokMagis(player);
    }

    public void setupBattle() {
        AutoSpawnNPC autoSpawnNPC = new AutoSpawnNPC(5835, Location.create(2408, 9607, player.getIndex() * 4), 0);
        autoSpawnNPC.init();
        autoSpawnNPC.faceLocation(Location.create(2401, 9609, player.getIndex() * 4));
        for (int y = 9604; y < 9610; y++) {
            CurseZombieNPC curseZombieNPC = new CurseZombieNPC(5317, Location.create(2407, y, player.getIndex() * 4));
            if (y == 9604) {
                curseZombieNPC.setPrimary();
            }
            curseZombieNPC.init();
            curseZombieNPC.faceLocation(Location.create(2401, 9609, player.getIndex() * 4));
            World.submit(new Pulse() {

                @Override
                public boolean pulse() {
                    curseZombieNPC.attack(player);
                    return true;
                }
            });
        }
    }
}
