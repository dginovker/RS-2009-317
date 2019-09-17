package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;
import plugin.npc.osrs.kraken.KrakenNPC;
import plugin.npc.osrs.skotizo.SkotizoNPC;
import plugin.npc.osrs.vetion.VetionNPC;

import java.util.Arrays;

/**
 * Created by Stan van der Bend on 13/01/2018.
 * project: Gielinor-Server
 * package: org.gielinor.game.system.command.impl
 */
public class TestCommand extends Command{
    @Override
    public Rights getRights() {
        return Rights.DEVELOPER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"slayer","boss", "find"};
    }

    private static NPC spawned;

    @Override
    public void execute(Player player, String[] params) {

        final String boss = params[1];

        player.sendChat(boss);
        switch (params[0]) {
            case "slayer":
                player.getSavedData().getActivityData().increaseSlayerPoints(100);

                break;
            case "boss":
                switch (boss) {

                    case "sktz":
                        player.getTeleporter().send(Location.create(1693, 9886, 0));
                        final SkotizoNPC skotizoNPC = new SkotizoNPC(Location.create(1693, 9886, 0));
                        skotizoNPC.init();
                        break;
                    case "krkn":
                        player.getTeleporter().send(Location.create(3696, 5807, 0));
                        final KrakenNPC krakenNPC = new KrakenNPC(Location.create(1693, 9886, 0));
                        krakenNPC.init();
                        break;
                    case "vtn":

                        player.getTeleporter().send(Location.create(3291, 3830, 0));


                        spawned = new VetionNPC();

                        spawned.init();

                        break;
                    case "p":
                        System.out.println("sending proj");

                        spawned.lock(10);
                        spawned.face(player);

                        spawned.animate(Animation.create(5485));

                        // Projectile.create(player, spawned, 280).send();
                        //Projectile.create(spawned, player, 280, 80, 0, 30, 88, 40, 11).send();
                        Arrays.stream(generateFallingProjectiles(spawned, player)).forEach(projectile -> projectile.send());
                        break;
                }
                break;
            case "find":
                final String keyWord =params[1];
                for(ItemDefinition itemDefinition : ItemDefinition.values()){
                    if(itemDefinition.getName().toLowerCase().contains(keyWord)){
                        player.getActionSender().sendConsoleMessage("["+itemDefinition.getId()+"]: "+itemDefinition.getName());
                    }
                }
                break;
        }


    }

    private static Projectile[] generateFallingProjectiles(Entity character, Entity target){

        final Location[] positions = new Location[3];
        final Projectile[] projectiles = new Projectile[positions.length];

        final boolean
            throwCalculated = target.getWalkingQueue().isMoving(),
            perfectLine = RandomUtil.getRandom(100 ) <= 50;

        final Direction projectileDirection = throwCalculated
            ? target.getDirection()
            : character.getDirection();

        final int
            deltaX = projectileDirection.getStepX(),
            deltaY = projectileDirection.getStepY(),
            standard = deltaX + deltaY;

        final Location
            position1 = target.getLocation(),
            position2 = throwCalculated
                ? position1.transform((perfectLine ? 2 : (2 - RandomUtil.getRandom(4))) * standard, (perfectLine ? 2 : (2 - RandomUtil.getRandom(4)))*standard, 0)
                : position1.transform(2 - RandomUtil.getRandom(4), 2 - RandomUtil.getRandom(4), 0),
            position3 =  throwCalculated
                ? position2.transform((perfectLine ? 2 : (2 - RandomUtil.getRandom(4))) * standard, (perfectLine ? 2 : (2 - RandomUtil.getRandom(4)))*standard, 0)
                : position1.transform(2 - RandomUtil.getRandom(4), 2 - RandomUtil.getRandom(4), 0);

        positions[0] = position1;
        positions[1] = position2;
        positions[2] = position3;

        for (int i = 0; i < positions.length; i++)
            projectiles[i] = Projectile.create(character.getLocation(), positions[i], 280, 80, 0, 20, 80, 90, 11);

        return projectiles;
    }
}
