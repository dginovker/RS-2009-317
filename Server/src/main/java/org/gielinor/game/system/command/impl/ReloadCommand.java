package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.SkillMenu;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.combat.equipment.RangeWeapon;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.script.ScriptManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.in.CommandPacketHandler;
import org.gielinor.parser.item.GroundItemParser;
import org.gielinor.parser.misc.RangeDataParser;
import org.gielinor.parser.npc.NPCDefinitionsParser;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.impl.ServerAnnouncementPulse;
import org.gielinor.spring.service.SpawnService;
import org.gielinor.spring.service.impl.ObjectSpawnService;
import org.gielinor.spring.service.impl.SkillMenuService;

/**
 * Reloads various server tasks.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ReloadCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "reload" };
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!(args.length >= 2)) {
            player.getActionSender().sendMessage("Use as ::reload <lt>type>");
            return;
        }
        switch (args[1].toLowerCase()) {
            case "ann":
            case "announcements":
                ServerAnnouncementPulse.populateAnnouncements();
                break;
            case "spawns":
                for (NPC npc : Repository.getNpcs()) {
                    npc.clear();
                }
                SpawnService spawnService = (SpawnService) World.getWorld().getApplicationContext().getBean("npcSpawnService");
                spawnService.loadSpawns();
                break;
            case "skillmenus":
                for (int skillId = 0; skillId < Skills.SKILL_NAME.length; skillId++) {
                    SkillMenu skillMenu = SkillMenu.forId(skillId);
                    if (skillMenu == null) {
                        continue;
                    }
                    skillMenu.getSkillMenuDefinitions().clear();
                }
                SkillMenu.getSkillMenus().clear();
                new SkillMenuService().call();
                break;
            case "commands":
                new CommandPacketHandler().call();
                break;
            case "region":
                if (!(args.length >= 3)) {
                    player.getActionSender().sendMessage("Use as ::reload region <lt>id>");
                    return;
                }
                int id = Integer.parseInt(args[2]);
                RegionManager.forId(id).checkInactive();
                RegionManager.forId(id).setLoaded(false);
                RegionManager.forId(id).flagActive();
                break;
            case "scripts":
                ScriptManager.load();
                break;
            case "plugins":
                PluginManager.init();
                break;
            case "itemdefs":
            case "itemdef":
                ItemDefinition.getDefinitions().clear();
                ItemDefinition.parse();
                break;
            case "npcdefs":
            case "npcdef":
                NPCDefinition.getDefinitions().clear();
                try {
                    new NPCDefinitionsParser().parse();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
            case "range_weapons":
                RangeWeapon.getRangeWeapons().clear();
                RangeWeapon.initialize();
                try {
                    RangeDataParser rangeDataParser = new RangeDataParser();
                    rangeDataParser.parse();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
            case "objects":
                ((ObjectSpawnService) World.getWorld().getApplicationContext().getBean("objectSpawnService"))
                    .initializeWorldObjects();
                break;
            case "grounditems":
                GroundItemManager.destroyAll();
                GroundItemManager.getItems().clear();
                GroundItemParser.getInstance().getSpawns().clear();
                GroundItemManager.pulse();
                try {
                    GroundItemParser.getInstance().parse();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
        }
    }
}
