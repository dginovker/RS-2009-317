package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.cache.index.impl.MapIndex;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.skill.member.construction.BuildHotspot;
import org.gielinor.game.content.skill.member.construction.HouseLocation;
import org.gielinor.game.content.skill.member.construction.RoomBuilder;
import org.gielinor.game.content.skill.member.construction.RoomProperties;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.content.skill.member.slayer.Task;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.communication.ClanRank;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.*;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.in.CommandPacketHandler;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.ItemDefinitionC;
import org.gielinor.utilities.ItemDefinitionPacker;
import org.gielinor.utilities.OSRSItemDefinition;
import org.gielinor.utilities.misc.FileOperations;
import org.gielinor.utilities.string.TextUtils;
import plugin.activity.gungame.ReverseGunGameActivityPlugin;
import plugin.activity.zulrah.ZulrahNPC;
import org.gielinor.net.packet.out.RepositionChild;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ChildPositionContext;
import plugin.activity.clanwars.ClanWarsRule;
import plugin.interaction.inter.EmoteTabInterface;
import plugin.npc.PenanceNPC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Handles any commands that aren't handled in a class.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DefaultCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public void init() {
    }

    @Override
    public String[] getCommands() {
        return null;
    }

    @Override
    public void execute(final Player player, String[] args) {
        int index;
        int itemId;
        switch (args[0].toLowerCase()) {
            case "zul":
                if (!player.getRights().isAdministrator() && !player.getRights().isDeveloper()) {
                    break;
                }
                final ZulrahNPC zulrahNPC = new ZulrahNPC(ZulrahNPC.RANGE_ID, new Location(2266, 3072, 0));
                zulrahNPC.setPlayer(player);
                zulrahNPC.setDirection(Direction.SOUTH);
                zulrahNPC.init();
                break;
            case "scale":
                if (!player.getRights().isDeveloper()) {
                    break;
                }
                player.getSavedData().getGlobalData().setBlowpipeDartId(Integer.parseInt(args[1]));
                player.getSavedData().getGlobalData().setBlowpipeDartAmount(Integer.parseInt(args[2]));
                player.getSavedData().getGlobalData().setBlowpipeDartScales(Integer.parseInt(args[2]));
                break;
            case "packitems":
                if (!player.getRights().isDeveloper()) {
                    break;
                }
                ItemDefinitionC.unpackConfig();
                OSRSItemDefinition.unpackConfig();
                // int newId = 14762;
                // int count = 0;
                // int highest = 0;
                // for (int i = 0; i < OSRSItemDefinition.itemCount; i++) {
                // if (i < 11683) {
                // continue;
                // }
                // OSRSItemDefinition osrsItemDefinition =
                // OSRSItemDefinition.forId(i);
                // ItemDefinitionC itemDefinitionC = new ItemDefinitionC(newId);
                // itemDefinitionC.copyFrom(newId, osrsItemDefinition);
                // count++;
                // newId++;
                // highest = i;
                // }
                // int offset = 14761;
                // for (int u = 0; u < ItemDefinitionC.getItemCount(); u++) {
                // if (u < 14762) {
                // continue;
                // }
                // int osId = (u - (14761 - 11686));
                // if (osId > 21072) {
                // break;
                // }
                // ItemDefinitionC itemDefinition = ItemDefinitionC.forId(u, true,
                // false, true, 0);
                // OSRSItemDefinition osrsItemDefinition =
                // OSRSItemDefinition.forId(osId);
                // //(id - (14761 - 11686))
                // if (osrsItemDefinition.stackIds != null) {
                // itemDefinition.stackIds = osrsItemDefinition.stackIds;
                // for (int ii = 0; ii < itemDefinition.stackIds.length; ii++) {
                // int newStackId = osrsItemDefinition.stackIds[ii] - 11686;
                // newStackId = offset + newStackId;
                // itemDefinition.stackIds[ii] = newStackId;
                // }
                // }
                //// if (osrsItemDefinition.noteItemId != -1) {
                //// //14761
                //// int id = osrsItemDefinition.noteItemId - 11686;
                //// id = offset + id;
                //// itemDefinition.noteItemId = id;
                //// }
                //// if (itemDefinition.noteTemplateId != -1) {
                //// itemDefinition.toNote();
                //// }
                // }
                try {
                    ItemDefinitionPacker.pack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "poh":
                /*if (!player.getRights().isDeveloper()) {
                    break;
                }*/
                RoomBuilder.openBuildInterface(player, BuildHotspot.GAMES_STONE_SPACE);
                if (!player.getHouseManager().hasHouse()) {
                    player.getHouseManager().create(HouseLocation.RIMMINGTON);
                }
                final Command[] command = { CommandPacketHandler.getCommands().get("tele") };
                command[0].execute(player, new String[]{ "tele", "rimmington" });
                World.submit(new Pulse(2) {

                    @Override
                    public boolean pulse() {
                        command[0] = CommandPacketHandler.getCommands().get("obj");
                        command[0].execute(player, new String[]{ "obj", "15478" });
                        player.setTeleportTarget(player.getLocation().transform(4, 4, 0));
                        return true;
                    }
                });
                break;
            case "convert":
                if (!player.getRights().isDeveloper()) {
                    break;
                }
                StringBuilder stringBuilder = new StringBuilder();

                FileOperations.writeFile("sql.sql", stringBuilder.toString());
                break;

            case "dumpsum":
                if (!player.getRights().isDeveloper()) {
                    break;
                }
                int objectId = 29882;
                StringBuilder dumped = new StringBuilder();
                if (objectId == -1) {
                    List<Integer> addedObjects = new ArrayList<>();
                    ZoneBorders SEARCHING_AREA = new ZoneBorders(2198, 5335, 2216, 5354);
                    for (int sX = SEARCHING_AREA.getSouthWestX() >> 6; sX < (SEARCHING_AREA.getNorthEastX() >> 6)
                        + 1; sX++) {
                        for (int sY = SEARCHING_AREA.getSouthWestY() >> 6; sY < (SEARCHING_AREA.getNorthEastY() >> 6)
                            + 1; sY++) {
                            int regionId = sX << 8 | sY;
                            Region region = RegionManager.forId(regionId);
                            if (region == null) {
                                continue;
                            }
                            Region.load(region);
                            for (RegionPlane regionPlane : region.getPlanes()) {
                                for (GameObject[] gameObjects : regionPlane.getObjects()) {
                                    for (GameObject gameObject : gameObjects) {
                                        if (gameObject == null) {
                                            continue;
                                        }
                                        if (addedObjects.contains(gameObject.getId())) {
                                            continue;
                                        }
                                        addedObjects.add(gameObject.getId());
                                        ObjectDefinition objectDef = gameObject.getDefinition();
                                        dumped.append("case ").append(gameObject.getId()).append(":\r\n");
                                        dumped.append("objectDef.name = \"").append(gameObject.getDefinition().getName())
                                            .append("\";\r\n");
                                        if (objectDef.hasActions()) {
                                            String dacts = "";
                                            for (String act : gameObject.getDefinition().getOptions()) {
                                                if (act == null || Objects.equals(act, "null")
                                                    || act.equalsIgnoreCase("hidden")) {
                                                    dacts += "null, ";
                                                    continue;
                                                }
                                                dacts += "\"" + act + "\", ";
                                            }
                                            dumped.append("objectDef.interactions = new String[]{").append(dacts)
                                                .append("};\r\n");
                                        }
                                        if (gameObject.getDefinition().getModelIds() != null) {
                                            dumped.append("objectDef.modelIds = new int[]{")
                                                .append(Arrays.toString(gameObject.getDefinition().getModelIds()))
                                                .append("};\r\n");
                                        }
                                        if (gameObject.getDefinition().getModelConfiguration() != null) {
                                            dumped.append("objectDef.modelTypes = new int[]{")
                                                .append(Arrays
                                                    .toString(gameObject.getDefinition().getModelConfiguration()))
                                                .append("};\r\n");
                                        }
                                        if (gameObject.getDefinition().sizeX != 1) {
                                            dumped.append("objectDef.sizeX = ").append(gameObject.getDefinition().sizeX)
                                                .append(";\r\n");
                                        }
                                        if (gameObject.getDefinition().sizeY != 1) {
                                            dumped.append("objectDef.sizeY = ").append(gameObject.getDefinition().sizeY)
                                                .append(";\r\n");
                                        }
                                        if (gameObject.getDefinition().projectileClipped) {
                                            dumped.append("objectDef.projectileClipped = true;\r\n");
                                        }
                                        if (gameObject.getDefinition().isInteractive == 1) {
                                            dumped.append("objectDef.isInteractive = true;\r\n");
                                        }
                                        if (objectDef.aByte3912 == 1) {
                                            dumped.append("objectDef.contouredGround = true;\r\n");
                                        }
                                        if (objectDef.aBoolean3867) {
                                            dumped.append("objectDef.delayShading = true;\r\n");
                                        }
                                        if (objectDef.boolean1) {
                                            dumped.append("objectDef.occludes = true;\r\n");
                                        }
                                        if (objectDef.animationId != -1 && objectDef.animationId != 65535) {
                                            dumped.append("objectDef.animation = ").append(objectDef.animationId)
                                                .append(";\r\n");
                                        }
                                        if (objectDef.getMapIcon() != -1) {
                                            dumped.append("objectDef.mapIcon = ").append(objectDef.getMapIcon())
                                                .append(";\r\n");
                                        }
                                        if (objectDef.mapscene != -1 && objectDef.mapscene != 0) {
                                            dumped.append("objectDef.mapscene = ").append(objectDef.mapscene)
                                                .append(";\r\n");
                                        }
                                        if (objectDef.getScaleX() != 128) {
                                            dumped.append("objectDef.scaleX = ").append(objectDef.getScaleX())
                                                .append(";\r\n");
                                        }
                                        if (objectDef.getScaleY() != 128) {
                                            dumped.append("objectDef.scaleY = ").append(objectDef.getScaleY())
                                                .append(";\r\n");
                                        }
                                        if (objectDef.getScaleZ() != 128) {
                                            dumped.append("objectDef.scaleZ = ").append(objectDef.getScaleZ())
                                                .append(";\r\n");
                                        }
                                        String origC = "";
                                        String modC = "";
                                        if (objectDef.getOriginalColors() != null) {
                                            for (int oc : objectDef.getOriginalColors()) {
                                                origC += oc + ", ";
                                            }
                                        }
                                        if (objectDef.getModifiedColors() != null) {
                                            for (int mc : objectDef.getModifiedColors()) {
                                                modC += mc + ", ";
                                            }
                                        }
                                        if (origC != "") {
                                            dumped.append("objectDef.originalModelColors = new int[]{").append(origC)
                                                .append("};\r\n");
                                        }
                                        if (modC != "") {
                                            dumped.append("objectDef.modifiedModelColors = new int[]{").append(modC)
                                                .append("};\r\n");
                                        }
                                        dumped.append("break;\r\n\r\n");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                    dumped.append("case ").append(objectDefinition.getId()).append(":\r\n");
                    dumped.append("objectDef.name = \"").append(objectDefinition.getName()).append("\";\r\n");
                    if (objectDefinition.hasActions()) {
                        String dacts = "";
                        for (String act : objectDefinition.getOptions()) {
                            if (act == null || Objects.equals(act, "null") || act.equalsIgnoreCase("hidden")) {
                                dacts += "null, ";
                                continue;
                            }
                            dacts += "\"" + act + "\", ";
                        }
                        dumped.append("objectDef.interactions = new String[]{").append(dacts).append("};\r\n");
                    }
                    if (objectDefinition.getModelIds() != null) {
                        dumped.append("objectDef.modelIds = new int[]{")
                            .append(Arrays.toString(objectDefinition.getModelIds())).append("};\r\n");
                    }
                    if (objectDefinition.getModelConfiguration() != null) {
                        dumped.append("objectDef.modelTypes = new int[]{")
                            .append(Arrays.toString(objectDefinition.getModelConfiguration())).append("};\r\n");
                    }
                    if (objectDefinition.sizeX != 1) {
                        dumped.append("objectDef.sizeX = ").append(objectDefinition.sizeX).append(";\r\n");
                    }
                    if (objectDefinition.sizeY != 1) {
                        dumped.append("objectDef.sizeY = ").append(objectDefinition.sizeY).append(";\r\n");
                    }
                    if (objectDefinition.projectileClipped) {
                        dumped.append("objectDef.projectileClipped = true;\r\n");
                    }
                    if (objectDefinition.isInteractive == 1) {
                        dumped.append("objectDef.isInteractive = true;\r\n");
                    }
                    if (objectDefinition.aByte3912 == 1) {
                        dumped.append("objectDef.contouredGround = true;\r\n");
                    }
                    if (objectDefinition.aBoolean3867) {
                        dumped.append("objectDef.delayShading = true;\r\n");
                    }
                    if (objectDefinition.boolean1) {
                        dumped.append("objectDef.occludes = true;\r\n");
                    }
                    if (objectDefinition.animationId != -1 && objectDefinition.animationId != 65535) {
                        dumped.append("objectDef.animation = ").append(objectDefinition.animationId).append(";\r\n");
                    }
                    if (objectDefinition.getMapIcon() != -1) {
                        dumped.append("objectDef.mapIcon = ").append(objectDefinition.getMapIcon()).append(";\r\n");
                    }
                    if (objectDefinition.mapscene != -1 && objectDefinition.mapscene != 0) {
                        dumped.append("objectDef.mapscene = ").append(objectDefinition.mapscene).append(";\r\n");
                    }
                    if (objectDefinition.getScaleX() != 128) {
                        dumped.append("objectDef.scaleX = ").append(objectDefinition.getScaleX()).append(";\r\n");
                    }
                    if (objectDefinition.getScaleY() != 128) {
                        dumped.append("objectDef.scaleY = ").append(objectDefinition.getScaleY()).append(";\r\n");
                    }
                    if (objectDefinition.getScaleZ() != 128) {
                        dumped.append("objectDef.scaleZ = ").append(objectDefinition.getScaleZ()).append(";\r\n");
                    }
                    String origC = "";
                    String modC = "";
                    if (objectDefinition.getOriginalColors() != null) {
                        for (int oc : objectDefinition.getOriginalColors()) {
                            origC += oc + ", ";
                        }
                    }
                    if (objectDefinition.getModifiedColors() != null) {
                        for (int mc : objectDefinition.getModifiedColors()) {
                            modC += mc + ", ";
                        }
                    }
                    if (origC != "") {
                        dumped.append("objectDef.originalModelColors = new int[]{").append(origC).append("};\r\n");
                    }
                    if (modC != "") {
                        dumped.append("objectDef.modifiedModelColors = new int[]{").append(modC).append("};\r\n");
                    }
                    dumped.append("break;\r\n\r\n");
                }
                FileOperations.writeFile("object.txt", dumped.toString());
                break;
            case "dsm":
                // 45693 = new id
                List<Integer> ok = new ArrayList<>();
                for (int oid = 28266; oid <= 28295; oid++) {
                    ObjectDefinition objectDefinition = ObjectDefinition.forId(oid);
                    Integer x = objectDefinition.getModelIds()[0];
                    if (!ok.contains(x)) {
                        ok.add(x);
                    }
                }
                int newId2 = 45693;
                int oid = 28266;
                for (Integer modId : ok) {
                    System.out.println("case " + oid + ":");
                    System.out.println("objectDefinition.name = \"Snowman\";");
                    System.out.println("objectDefinition.models = new int[]{" + newId2 + "};");
                    System.out.println("objectDefinition.actions = new String[]{\"Add-to\", null, null, null, null};");
                    System.out.println("break;\n");
                    newId2++;
                    oid++;
                }
                break;
            case "t":
                // index = player.getAttribute("summ", 0);
                // SummoningScroll summoningScroll = SummoningScroll.forId(index);
                // if (summoningScroll == null) {
                // player.setAttribute("summ", index + 1);
                // break;
                // }
                // String specialName = new
                // Item(summoningScroll.getItemId()).getDefinition().getName().replace("
                // scroll", "");
                // if (specialName == null) {
                // player.setAttribute("summ", index + 1);
                // break;
                // }
                // PacketRepository.send(SummoningInformationPacket.class, new
                // SummoningInformationContext(player, specialName,
                // summoningScroll.getFullName(), summoningScroll.getLevel(),
                // summoningScroll.getItemId(),
                // 1, summoningScroll.getDescription()));
                // player.setAttribute("summ", index + 1);
                player.getInterfaceState().openTab(Sidebar.CLAN_TAB, new Component(25904));
                break;
            case "c5":
                // 7424
                if (player.getUsername().equalsIgnoreCase("Harry")) {
                    // public void setDefaultRules() {
                    ClanWarsRule[] DEFAULT_RULES = new ClanWarsRule[]{ ClanWarsRule.LAST_TEAM_STANDING,
                        ClanWarsRule.WASTELAND, ClanWarsRule.MELEE_ALLOWED, ClanWarsRule.RANGING_ALLOWED,
                        ClanWarsRule.FOOD_ALLOWED, ClanWarsRule.DRINKS_ALLOWED, ClanWarsRule.SPECIAL_ATTACKS_ALLOWED,
                        ClanWarsRule.KILL_EM_ALL, ClanWarsRule.MAGIC_ALL_SPELLBOOKS, ClanWarsRule.PRAYER_ALLOWED };
                    for (ClanWarsRule clanWarsRule : ClanWarsRule.values()) {
                        player.getInterfaceState().force(clanWarsRule.getConfigId(), 0, false);
                    }
                    for (ClanWarsRule clanWarsRule : DEFAULT_RULES) {
                        player.getInterfaceState().force(clanWarsRule.getConfigId(), clanWarsRule.getConfigValue(), false);
                    }
                    // }
                    player.getActionSender().sendInterfaceConfig(50, 24239);
                    player.getActionSender().sendInterfaceConfig(50, 24240);
                    player.getActionSender().sendInterfaceConfig(51, 24241);
                    player.getActionSender().sendString(24241, "Your opponent has made\\nchanges!\\n5");
                    World.submit(new Pulse(2) {

                        int timer = 5;

                        @Override
                        public boolean pulse() {
                            player.getActionSender().sendString(24241, "Your opponent has made\\nchanges!\\n" + timer);
                            if (timer == 0) {
                                player.getActionSender().sendInterfaceConfig(51, 24239);
                                player.getActionSender().sendInterfaceConfig(51, 24240);
                                player.getActionSender().sendInterfaceConfig(50, 24241);
                                return true;
                            }
                            timer--;
                            return false;
                        }
                    });
                    player.getInterfaceState().open(new Component(24126));
                }
                break;
            case "c"://one sec getting some code.
                // 7424
                if (player.getUsername().equalsIgnoreCase("Harry")) {
                    // public void setDefaultRules() {
                    ClanWarsRule[] DEFAULT_RULES = new ClanWarsRule[]{ ClanWarsRule.LAST_TEAM_STANDING,
                        ClanWarsRule.WASTELAND, ClanWarsRule.MELEE_ALLOWED, ClanWarsRule.RANGING_ALLOWED,
                        ClanWarsRule.FOOD_ALLOWED, ClanWarsRule.DRINKS_ALLOWED, ClanWarsRule.SPECIAL_ATTACKS_ALLOWED,
                        ClanWarsRule.KILL_EM_ALL, ClanWarsRule.MAGIC_ALL_SPELLBOOKS, ClanWarsRule.PRAYER_ALLOWED };
                    for (ClanWarsRule clanWarsRule : ClanWarsRule.values()) {
                        player.getInterfaceState().force(clanWarsRule.getConfigId(), 0, false);
                    }
                    for (ClanWarsRule clanWarsRule : DEFAULT_RULES) {
                        player.getInterfaceState().force(clanWarsRule.getConfigId(), clanWarsRule.getConfigValue(), false);
                    }
                    // }
                    player.getInterfaceState().open(new Component(24126));
                }
                break;
            case "repos":
                if (player.getUsername().equalsIgnoreCase("Harry")) {
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, Integer.parseInt(args[1]),
                        0, Integer.parseInt(args[2]), Integer.parseInt(args[3])));
                }
                break;
            case "sb":
                if (player.getUsername().equalsIgnoreCase("Harry")) {
                    StringBuilder sb = new StringBuilder();
                    for (index = 0; index < player.getBank().capacity(); index++) {
                        Item item = player.getBank().get(index);
                        if (item == null) {
                            continue;
                        }
                        sb.append("(").append(player.getPidn()).append(", ").append(index).append(", ").append(item.getId())
                            .append(", ").append(item.getCount()).append(", ").append(item.getCharge()).append("),\n");
                    }
                    sb.append("\n\n");
                    sb.append(Arrays.toString(player.getBank().getBankData().getTabAmounts()));
                    FileOperations.writeFile("Bank_" + System.currentTimeMillis() + ".txt", sb.toString());
                }
                break;
            case "slot":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                itemId = Integer.parseInt(args[1]);
                int equipSlot = 0;
                if (StringUtils.isNumeric(args[2])) {
                    equipSlot = Integer.parseInt(args[2]);
                } else {
                    switch (args[2].toUpperCase()) {
                        case "HAT":
                            equipSlot = 0;
                            break;
                        case "CAPE":
                            equipSlot = 1;
                            break;
                        case "AMULET":
                            equipSlot = 2;
                            break;
                        case "WEAPON":
                            equipSlot = 3;
                            break;
                        case "CHEST":
                            equipSlot = 4;
                            break;
                        case "SHIELD":
                            equipSlot = 5;
                            break;
                        case "LEGS":
                            equipSlot = 7;
                            break;
                        case "HANDS":
                            equipSlot = 9;
                            break;
                        case "FEET":
                            equipSlot = 10;
                            break;
                        case "RING":
                            equipSlot = 12;
                            break;
                        case "AMMO":
                            equipSlot = 13;
                            break;
                    }
                }
                ItemDefinition.forId(itemId).getConfigurations().remove(ItemConfiguration.EQUIP_SLOT);
                ItemDefinition.forId(itemId).getConfigurations().put(ItemConfiguration.EQUIP_SLOT, equipSlot);
                // ItemDefinition.forId(itemId).setOptions(new String[]{"wear",
                // "wear", "wear", "wear", "drop"});
                break;
            case "gis":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                GroundItemManager.create(new GroundItem(new Item(Integer.parseInt(args[1])), player.getLocation(), player));
                System.out.println("(NULL, " + args[1] + ", 1, 40, " + player.getLocation().getX() + ", "
                    + player.getLocation().getY() + ", " + player.getLocation().getZ() + "),");
                break;
            case "toggleicon":
                player.getDonorManager().toggleIconHidden();
                break;
            case "setdonor":
                if (!player.getRights().isAdministrator() && !World.getConfiguration().isDevelopmentEnabled()) {
                    break;
                }
                Player donorPlayer = player;
                DonorStatus donorStatus;
                String nameAsUpperCase = args[1].toUpperCase();
                try {
                    donorStatus = DonorStatus.valueOf(nameAsUpperCase);
                } catch (IllegalArgumentException ex) {
                    player.getActionSender().sendMessage("No such donor rank: " + nameAsUpperCase + ".");
                    break;
                }
                if (args.length == 3) {
                    donorPlayer = Repository.getPlayerByName(toString(args, 2));
                    if (donorPlayer == null) {
                        player.getActionSender().sendMessage("Player is not online.");
                        return;
                    }
                }
                donorPlayer.getDonorManager().setDonorStatus(donorStatus);
                player.getActionSender().sendMessage("Donation status changed to: " + donorStatus.name() + ".");
                break;
            case "clearevent":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                Repository.getPlayerByName(toString(args, 1)).getAntiMacroHandler().getEvent().terminate();
                break;
            case "npcemote":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                if (player.getAttribute("control_npc") == null) {
                    player.getActionSender().sendMessage("No NPC being controlled.");
                    break;
                }
                NPC npc = player.getAttribute("control_npc");
                EmoteTabInterface.Emote emote = EmoteTabInterface.Emote.forName(args[1].toUpperCase());
                if (emote != null) {
                    if (emote.getGraphic() != null) {
                        npc.visualize(emote.getAnimation(), emote.getGraphic());
                    } else {
                        npc.animate(emote.getAnimation());
                    }
                }
                break;
            case "npcfollow":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                if (player.getAttribute("control_npc") == null) {
                    player.getActionSender().sendMessage("No NPC being controlled.");
                    break;
                }
                Player other = Repository.getPlayerByName(toString(args, 1));
                if (other == null) {
                    player.getActionSender().sendMessage("Could not find other player.");
                    break;
                }
                NPC oNPC = player.getAttribute("control_npc");
                oNPC.setAttribute("follow-player", other);
                oNPC.getPulseManager().run(new MovementPulse(oNPC, other, Pathfinder.SMART) {

                    @Override
                    public boolean pulse() {
                        if (player.getAttribute("control_npc") == null) {
                            return true;
                        }
                        if (other.getLocation().getDistance(oNPC.getLocation()) > 10) {
                            return true;
                        }
                        return false;
                    }
                }, "movement");
                break;
            case "controln":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                player.getActionSender().sendMessage("Click NPC to control");
                player.setAttribute("control_npc_target", 1);
                break;
            case "uncontroln":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                player.getActionSender().sendMessage("No longer controlling npc!");
                if (player.getAttribute("control_npc") != null) {
                    ((NPC) player.getAttribute("control_npc")).removeAttribute("follow-player");
                }
                player.removeAttribute("control_npc");
                player.removeAttribute("control_npc_target");
                break;
            case "walkas":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                if (player.getAttribute("walk_as_player") != null) {
                    player.removeAttribute("walk_as_player");
                    player.getActionSender().sendMessage("No longer walking as player!");
                    return;
                }
                player.teleport(Repository.getPlayerByName(toString(args, 1)).getLocation());
                player.setAttribute("walk_as_player", Repository.getPlayerByName(toString(args, 1)));
                break;
            case "controlp":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                player.setAttribute("control_player", Repository.getPlayerByName(toString(args, 1)));
                break;
            case "uncontrolp":
            case "resetcontrol":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                player.getActionSender().sendMessage("No longer controlling player!");
                player.removeAttribute("control_player");
                break;
            case "fillbank":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                int start = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                for (int bankIndex = start; bankIndex < player.getBank().capacity(); bankIndex++) {
                    player.getBank().add(new Item(ItemDefinition.getRandomDefinition(false).getId()));
                }
                player.getBank().refresh();
                player.getBank().update();
                break;
            case "setchaticon":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                int chatIcon = Integer.parseInt(args[1]);
                if (chatIcon > 20) {
                    player.getActionSender().sendMessage("Available chat icons range from 0 to 20");
                    break;
                }
                player.getActionSender().sendMessage("Set chat icon.");
                player.saveAttribute("chat-icon", chatIcon);
                break;
            case "givebeta":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                String otherPlayer = toString(args, 1);
                Player player1 = Repository.getPlayerByName(otherPlayer.toLowerCase());
                if (player1 == null) {
                    player.getActionSender().sendMessage("Player is not online.");
                    break;
                }
                player.getActionSender()
                    .sendMessage("You have given " + TextUtils.formatDisplayName(player1.getName()) + " beta status.");
                player1.getSavedData().getGlobalData().setBetaStatus(true);
                player1.getActionSender().sendMessage("You have been given beta status.");
                break;
            case "enablehelp":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                ClanCommunication.DEFAULT.setMessageRequirement(ClanRank.ANYONE);
                break;
            case "disablehelp":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                ClanCommunication.DEFAULT.setMessageRequirement(ClanRank.ANY_FRIENDS);
                break;
            case "dismiss":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                if (player.getFamiliarManager().hasFamiliar()) {
                    player.getFamiliarManager().dismiss();
                } else {
                    player.getActionSender().sendMessage("You don't have a follower.");
                }
                break;
            case "call":
                if (!player.getRights().isAdministrator()) {
                    break;
                }
                if (player.getFamiliarManager().hasFamiliar()) {
                    player.getFamiliarManager().getFamiliar().call();
                } else {
                    player.getActionSender().sendMessage("You don't have a follower.");
                }
                break;
            case "hayder":
                if (player.getRights().isAdministrator()) {
                    player.getEquipment().clear();
                    player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4151));
                    player.getEquipment().set(Equipment.SLOT_CAPE, new Item(9786));
                    player.getEquipment().set(Equipment.SLOT_AMULET, new Item(26585));
                    player.getEquipment().set(Equipment.SLOT_SHIELD, new Item(1171));
                    player.getEquipment().set(Equipment.SLOT_CHEST, new Item(1757));
                    player.getEquipment().set(Equipment.SLOT_HAT, new Item(1025));
                    player.getEquipment().set(Equipment.SLOT_LEGS, new Item(6181));
                    player.getEquipment().set(Equipment.SLOT_FEET, new Item(1061));
                    if (!player.getInventory().contains(5733)) {
                        player.getInventory().add(new Item(5733));
                    }
                    player.getEquipment().update(true);
                    player.getEquipment().refresh();
                    player.getAppearance().setGender(Gender.MALE);
                    player.getAppearance().getAppearanceCache()[0].setLook(2);
                    player.getAppearance().getAppearanceCache()[1].setLook(11);
                    player.getAppearance().getAppearanceCache()[2].setLook(18);
                    player.getAppearance().getAppearanceCache()[3].setLook(26);
                    player.getAppearance().getAppearanceCache()[4].setLook(33);
                    player.getAppearance().getAppearanceCache()[5].setLook(36);
                    player.getAppearance().getAppearanceCache()[6].setLook(42);
                    player.getAppearance().getAppearanceCache()[0].setColour(1);
                    player.getAppearance().getAppearanceCache()[2].setColour(0);
                    player.getAppearance().getAppearanceCache()[5].setColour(0);
                    player.getAppearance().getAppearanceCache()[6].setColour(0);
                    player.getAppearance().getAppearanceCache()[4].setColour(0);
                    player.getAppearance().sync();
                }
                break;
            case "leaf":
                if (player.getRights().isAdministrator()) {
                    player.getEquipment().clear();
                    player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(10487));
                    player.getEquipment().set(Equipment.SLOT_CAPE, new Item(5607));
                    player.getEquipment().set(Equipment.SLOT_AMULET, new Item(4081));
                    player.getEquipment().set(Equipment.SLOT_SHIELD, new Item(6889));
                    player.getEquipment().set(Equipment.SLOT_CHEST, new Item(1129));
                    player.getEquipment().set(Equipment.SLOT_HAT, new Item(2635));
                    player.getEquipment().set(Equipment.SLOT_LEGS, new Item(2497));
                    player.getEquipment().set(Equipment.SLOT_FEET, new Item(9638));
                    player.getEquipment().set(Equipment.SLOT_AMMO, new Item(9144, 85));
                    player.getEquipment().set(Equipment.SLOT_HANDS, new Item(7458));
                    player.getEquipment().set(Equipment.SLOT_RING, new Item(6735));
                    if (!player.getInventory().contains(5733)) {
                        player.getInventory().add(new Item(5733));
                    }
                    player.getEquipment().update(true);
                    player.getEquipment().refresh();
                    player.getAppearance().setGender(Gender.MALE);
                    player.getAppearance().getAppearanceCache()[0].setLook(3);
                    player.getAppearance().getAppearanceCache()[1].setLook(14);
                    player.getAppearance().getAppearanceCache()[2].setLook(21);
                    player.getAppearance().getAppearanceCache()[3].setLook(26);
                    player.getAppearance().getAppearanceCache()[4].setLook(33);
                    player.getAppearance().getAppearanceCache()[5].setLook(36);
                    player.getAppearance().getAppearanceCache()[6].setLook(42);
                    player.getAppearance().getAppearanceCache()[0].setColour(3);
                    player.getAppearance().getAppearanceCache()[2].setColour(13);
                    player.getAppearance().getAppearanceCache()[5].setColour(2);
                    player.getAppearance().getAppearanceCache()[6].setColour(3);
                    player.getAppearance().getAppearanceCache()[4].setColour(1);
                    player.getAppearance().sync();
                }
                break;
            case "Gielinor":
                if (player.getRights().isAdministrator()) {
                    player.getEquipment().clear();
                    player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4151));
                    player.getEquipment().set(Equipment.SLOT_CAPE, new Item(6570));
                    player.getEquipment().set(Equipment.SLOT_AMULET, new Item(26585));
                    player.getEquipment().set(Equipment.SLOT_SHIELD, new Item(11283));
                    if (!player.getInventory().contains(5733)) {
                        player.getInventory().add(new Item(5733));
                    }
                    player.getEquipment().update(true);
                    player.getEquipment().refresh();
                    player.getAppearance().setGender(Gender.MALE);
                    player.getAppearance().getAppearanceCache()[0].setLook(3);
                    player.getAppearance().getAppearanceCache()[1].setLook(14);
                    player.getAppearance().getAppearanceCache()[2].setLook(18);
                    player.getAppearance().getAppearanceCache()[3].setLook(28);
                    player.getAppearance().getAppearanceCache()[4].setLook(125);
                    player.getAppearance().getAppearanceCache()[5].setLook(38);
                    player.getAppearance().getAppearanceCache()[6].setLook(43);
                    player.getAppearance().getAppearanceCache()[0].setColour(1);
                    player.getAppearance().getAppearanceCache()[2].setColour(1);
                    player.getAppearance().getAppearanceCache()[5].setColour(2);
                    player.getAppearance().getAppearanceCache()[6].setColour(3);
                    player.getAppearance().getAppearanceCache()[4].setColour(0);
                    player.getAppearance().sync();
                }
                break;
            case "task":
                if (player.getRights().isAdministrator()) {
                    String t = args[1];
                    int amt = Integer.parseInt(args[2]);
                    Task task = null;
                    Master master = null;
                    for (Tasks tasks : Tasks.values()) {
                        if (tasks.name().equalsIgnoreCase(t)) {
                            task = tasks.getTask();
                            master = tasks.getTask().getMasters()[0];
                        }
                    }
                    player.getSlayer().assign(task, Master.DURADEL, amt);
                    player.getActionSender().sendMessage("Set task");
                }
                break;
            case "bork":
                if (player.getRights().isAdministrator()) {
                    // if ((System.currentTimeMillis() -
                    // player.getSavedData().getActivityData().getLastBorkBattle())
                    // < (24 * 60 * 60_000)) {
                    // player.getActionSender().sendMessage("The portal's magic is
                    // too weak to teleport you right now.");
                    // return;
                    // }
                    player.lock(3);
                    player.graphics(Graphics.create(110));
                    if (!player.getSavedData().getActivityData().hasKilledBork()) {
                        player.setAttribute("first-bork", true);
                    }
                    player.getSavedData().getActivityData().setLastBorkBattle(System.currentTimeMillis());
                    ActivityManager.start(player, "Bork cutscene", false);
                }
                break;
            case "gungame":
                player.removeAttribute("reverse_gun_game");
                //if (World.getSettings().isDevMode() || player.ignoreRestrictions()) {
                DialoguePlugin dial = ReverseGunGameActivityPlugin.Companion.getENTRY_DIALOGUE().newInstance(player);
                if (dial.open()) {
                    player.getDialogueInterpreter().setDialogue(dial);
                }
                //}
                break;
            case "fog":
                ActivityManager.start(player, "Fist of Guthix", false);
                break;
            case "hi":
                if (player.getRights().isAdministrator()) {
                    player.getAppearance().setHeadIcon(Integer.parseInt(args[1]));
                    player.getAppearance().sync();
                }
                break;
            case "ba":
                if (player.getRights().isAdministrator()) {
                    PenanceNPC.openRewardScreen(player);
                }
                break;
            case "testequip":
                if (World.getConfiguration().isDevelopmentEnabled()) {
                    player.getEquipment().add(new Item(Integer.parseInt(args[1])));
                }
                break;
            case "dynamic":
                if (player.getRights().isAdministrator()) {
                    DynamicRegion dynamicRegion = DynamicRegion.create(player.getLocation().getRegionId());
                    RegionManager.getRegionCache().put(dynamicRegion.getId(), dynamicRegion);
                    int baseX = player.getLocation().getLocalX();
                    int baseY = player.getLocation().getLocalY();
                    player.getProperties().setTeleportLocation(dynamicRegion.getBaseLocation().transform(baseX, baseY, 0));
                }
                break;
            case "lsid":
                MapIndex mapIndex = World.getWorld().getCache().getIndexTable()
                    .getMapIndex(player.getLocation().getRegionId());
                System.out.println("Land : " + mapIndex.getLandscapeFile());
                System.out.println("Map : " + mapIndex.getMapFile());
                break;
            case "room":
                if (player.getRights().isAdministrator()) {
                    index = Integer.parseInt(args[1]);
                    if (index > -1 && index < RoomProperties.values().length) {
                        player.getDialogueInterpreter().open("con:room", RoomProperties.values()[index]);
                        break;
                    }
                }
                break;
            case "ann":
                if (player.getRights() != Rights.GIELINOR_MODERATOR) {
                    return;
                }
                World.getWorld().serverAnnouncementPulse.pulse();
                break;
            case "pickpocket":
                if (player.getRights() != Rights.GIELINOR_MODERATOR) {
                    return;
                }
                player.setAttribute("PICKPOCKET", true);
                break;
            default:
                player.getActionSender().sendMessage("Unknown command: " + args[0].toLowerCase() + ".");
                break;
        }
    }
}
