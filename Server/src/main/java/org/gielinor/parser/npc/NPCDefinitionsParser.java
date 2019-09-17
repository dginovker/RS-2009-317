package org.gielinor.parser.npc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.database.DataSource;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.parser.Parser;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The NPC definition parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class NPCDefinitionsParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(NPCDefinitionsParser.class);

    @Override
    public boolean parse() {
        log.info("Requesting NPC definitions...");
        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM npc_definition")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                NPCDefinition npcDefinition = new NPCDefinition(resultSet.getInt("npc_id"));
                npcDefinition.getConfigurations().clear();
                npcDefinition.setId(resultSet.getInt("npc_id"));
                Map<String, Object> config = npcDefinition.getConfigurations();
                npcDefinition.setName(resultSet.getString("name"));
                npcDefinition.examine = resultSet.getString("examine");
                config.put(NPCConfiguration.EXAMINE, npcDefinition.examine);
                npcDefinition.size = (byte) resultSet.getInt("size");
                npcDefinition.setCombatLevel(resultSet.getInt("combat_level"));
                npcDefinition.headIcons = resultSet.getInt("head_icons");
                npcDefinition.isVisibleOnMap = resultSet.getBoolean("visible_on_map");
                npcDefinition.configId = resultSet.getInt("config_id");
                npcDefinition.configFileId = resultSet.getInt("config_file_id");
                String childrenString = resultSet.getString("children");
                if (!Objects.equals(childrenString, "null") && !Objects.equals(childrenString, "NO_VALUE")) {
                    npcDefinition.childNPCIds = TextUtils.toIntArray(childrenString.split(":"));
                }
                if (!Objects.equals(resultSet.getString("combat_animations"), "null")
                    && !Objects.equals(resultSet.getString("combat_animations"), "NO_VALUE")) {
                    String[] combatAnimations = resultSet.getString("combat_animations").split(":");
                    for (int i = 0; i < combatAnimations.length; i++) {
                        npcDefinition.getCombatAnimations()[i] = new Animation(
                            Integer.parseInt(combatAnimations[i]));
                    }
                }
                npcDefinition.setCombatDistance(resultSet.getInt("combat_distance"));
                npcDefinition.setOptions(resultSet.getString("options").split(":"));
                npcDefinition.walkAnimation = resultSet.getInt("walk_anim");
                npcDefinition.standAnimation = resultSet.getInt("stand_anim");
                npcDefinition.turn180Animation = resultSet.getInt("turn180_anim");
                npcDefinition.turnCWAnimation = resultSet.getInt("turn_cw_anim");
                npcDefinition.turnCCWAnimation = resultSet.getInt("turn_ccw_anim");
                config.put(NPCConfiguration.LIFEPOINTS, resultSet.getInt("lifepoints"));
                String[] combatLevels = resultSet.getString("combat_levels").split(":");
                if (combatLevels.length > 2) {
                    config.put(NPCConfiguration.ATTACK_LEVEL, Integer.parseInt(combatLevels[0]));
                    config.put(NPCConfiguration.STRENGTH_LEVEL, Integer.parseInt(combatLevels[1]));
                    config.put(NPCConfiguration.DEFENCE_LEVEL, Integer.parseInt(combatLevels[2]));
                    config.put(NPCConfiguration.RANGE_LEVEL, Integer.parseInt(combatLevels[3]));
                    config.put(NPCConfiguration.MAGIC_LEVEL, Integer.parseInt(combatLevels[4]));
                }
                String task = resultSet.getString("slayer_task");
                if (task != null && !Objects.equals(task, "null")) {
                    config.put(NPCConfiguration.SLAYER_TASK, Tasks.forName(task));
                    config.put(NPCConfiguration.SLAYER_EXP, resultSet.getDouble("slayer_xp"));
                }
                config.put(NPCConfiguration.POISONOUS, resultSet.getBoolean("poisonous"));
                config.put(NPCConfiguration.AGGRESSIVE, resultSet.getBoolean("aggressive"));
                if (resultSet.getInt("respawn_delay") > 0) {
                    config.put(NPCConfiguration.RESPAWN_DELAY, resultSet.getInt("respawn_delay"));
                }
                config.put(NPCConfiguration.ATTACK_SPEED, resultSet.getInt("attack_speed"));
                config.put(NPCConfiguration.POISON_IMMUNE, resultSet.getBoolean("poison_immune"));
                int[] bonuses = new int[15];
                String bonusString = resultSet.getString("bonuses");
                if (!Objects.equals(bonusString, "null") && !Objects.equals(bonusString, "NO_VALUE")) {
                    String[] stringBonuses = bonusString.split(":");
                    for (int index = 0; index < stringBonuses.length; index++) {
                        bonuses[index] = Integer.parseInt(stringBonuses[index]);
                    }
                    config.put(NPCConfiguration.BONUSES, bonuses);
                }
                config.put(NPCConfiguration.START_GRAPHIC, resultSet.getInt("start_gfx"));
                config.put(NPCConfiguration.PROJECTILE, resultSet.getInt("projectile"));
                config.put(NPCConfiguration.END_GRAPHIC, resultSet.getInt("end_gfx"));
                int combatStyle = resultSet.getInt("combat_style");
                if (combatStyle > -1) { // TODO = 0?
                    config.put(NPCConfiguration.COMBAT_STYLE, CombatStyle.values()[combatStyle]);
                }
                config.put(NPCConfiguration.AGGRESSIVE_RADIUS, resultSet.getInt("aggressive_radius"));
                config.put(NPCConfiguration.POISON_AMOUNT, resultSet.getInt("poison_amount"));
                config.put(NPCConfiguration.SPAWN_ANIMATION, resultSet.getInt("spawn_animation"));
                config.put(NPCConfiguration.START_HEIGHT, resultSet.getInt("start_height"));
                config.put(NPCConfiguration.PROJECTILE_HEIGHT, resultSet.getInt("projectile_height"));
                config.put(NPCConfiguration.END_HEIGHT, resultSet.getInt("end_height"));
                config.put(NPCConfiguration.SPELL_ID, resultSet.getInt("spell_id"));
                if (!resultSet.getString("combat_audio").equals("NO_VALUE")) {
                    String[] combatAudios = resultSet.getString("combat_audio").split(":");
                    if (combatAudios.length > 0) {
                        Audio[] audios = new Audio[combatAudios.length];
                        for (int i = 0; i < combatAudios.length; i++) {
                            audios[i] = new Audio(Integer.parseInt(combatAudios[i]));
                        }
                        config.put(NPCConfiguration.COMBAT_AUDIO, audios);
                    }
                }
                int weakness = resultSet.getInt("weakness");
                if (weakness > -1) {
                    config.put(NPCConfiguration.WEAKNESS, NPCConfiguration.Weakness.values()[weakness]);
                }
                if (!resultSet.getString("model_ids").equals("NO_VALUE")) {
                    String[] modelIds = resultSet.getString("model_ids").split(":");
                    if (modelIds.length > 0) {
                        npcDefinition.modelIds = TextUtils.toIntArray(modelIds);
                    }
                }
                NPCDefinition.getDefinitions().put(npcDefinition.getId(), npcDefinition);
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed while loading NPC definitions.", ex);
            return false;
        }
        log.info("Received and parsed {} NPC definitions.", NPCDefinition.getDefinitions().size());
        return true;
    }

}
