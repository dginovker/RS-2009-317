package org.gielinor.spring.service.impl;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.spring.service.DefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Represents the {@link org.gielinor.spring.service.DefinitionService} for an
 * {@link org.gielinor.cache.def.impl.ItemDefinition}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("itemDefinitionService")
public class ItemDefinitionService implements DefinitionService {

    private static final Path DEFINITION_PATH = Paths.get("data", "definition", "item_definition.json");

    private static final Logger log = LoggerFactory.getLogger(ItemDefinitionService.class);

    @Override
    public void initializeDefinitions() {
        log.debug("Loading item definition data...");
        long startTimeInNs = System.nanoTime();

        JsonParser parser = new JsonParser();
        try (BufferedReader reader = Files.newBufferedReader(DEFINITION_PATH, StandardCharsets.UTF_8)) {
            JsonArray definitionJsonArray = parser.parse(reader).getAsJsonArray();
            for (JsonElement element : definitionJsonArray) {
                ItemDefinition definition = parse((JsonObject) element);
                ItemDefinition.getDefinitions().put(definition.getId(), definition);
            }
        } catch (Exception ex) {
            // Rethrown to stop server startup.
            // Because the exception may be checked IOException, we use this hack to allow re-throwing
            // without a throws declaration.
            unchecked(ex);
            return; // unreachable - call above throws the IOException (as an unchecked exception).
        }

        long timeTakenInNs = System.nanoTime() - startTimeInNs;
        DecimalFormat df = new DecimalFormat("#0.000");
        String fs = df.format(TimeUnit.NANOSECONDS.toMillis(timeTakenInNs) / 1000D);
        log.info("Finished loading item definition data - took {}s.", fs);
    }

    private ItemDefinition parse(JsonObject source) {
        final ItemDefinition definition = new ItemDefinition();
        final Map<String, Object> conf = definition.getConfigurations();

        /* Mandatory fields */

        definition.setId(source.get("id").getAsInt());
        definition.setName(source.get("name").getAsString());
        definition.setUnnoted(source.get("unnoted").getAsBoolean());
        definition.setModelId(source.get("model_id").getAsInt());
        definition.setStackable(source.get("stackable").getAsBoolean());
        definition.setMembers(source.get("members").getAsBoolean());
        definition.setOptions(source.get("inventory_options").getAsString().split(":"));
        definition.setGroundOptions(source.get("ground_options").getAsString().split(":"));

        // TODO figure out why description is listed twice
        String desc = source.get("desc").getAsString();
        definition.setExamine(desc);
        conf.put(ItemConfiguration.EXAMINE, desc);

        /* Optional fields */
        parseBonuses(definition, source);
        parseAnimations(definition, conf, source);
        parseAudio(definition, source);
        parseOptional(definition, conf, source);
        parseRequirements(definition, source);

        return definition;
    }

    private void parseRequirements(ItemDefinition definition, JsonObject source) {
        if (!source.has("requirements"))
            return;
        JsonArray requirements = source.get("requirements").getAsJsonArray();
        for (JsonElement e : requirements) {
            JsonObject requirement = e.getAsJsonObject();
            String skillName = requirement.get("skill").getAsString();
            int level = requirement.get("level").getAsInt();
            int skillId = Skills.getSkillByName(skillName);

            if (skillId == -1)
                throw new IllegalArgumentException("No such skill: " + skillName);

            Map<Integer, Integer> cachedRequirements = definition.getItemRequirements();
            if (cachedRequirements == null) {
                cachedRequirements = new HashMap<>();
                definition.setItemRequirements(cachedRequirements);
            }
            cachedRequirements.put(skillId, level);
        }
    }

    private void parseOptional(ItemDefinition definition, Map<String, Object> conf, JsonObject source) {
        if (source.has("equip_slot"))
            conf.put(ItemConfiguration.EQUIP_SLOT, source.get("equip_slot").getAsInt());
        if (source.has("attack_speed"))
            conf.put(ItemConfiguration.ATTACK_SPEED, source.get("attack_speed").getAsInt());
        if (source.has("remove_head"))
            conf.put(ItemConfiguration.REMOVE_HEAD, source.get("remove_head").getAsBoolean());
        if (source.has("remove_beard"))
            conf.put(ItemConfiguration.REMOVE_BEARD, source.get("remove_beard").getAsBoolean());
        if (source.has("remove_sleeves"))
            conf.put(ItemConfiguration.REMOVE_SLEEVES, source.get("remove_sleeves").getAsBoolean());
        if (source.has("destroy_message"))
            conf.put(ItemConfiguration.DESTROY_MESSAGE, source.get("destroy_message").getAsString());
        if (source.has("spawnable"))
            conf.put(ItemConfiguration.SPAWNABLE, source.get("spawnable").getAsBoolean());
        if (source.has("bankable"))
            conf.put(ItemConfiguration.BANKABLE, source.get("bankable").getAsBoolean());
        if (source.has("rare_item"))
            conf.put(ItemConfiguration.RARE_ITEM, source.get("rare_item").getAsBoolean());
        if (source.has("weight"))
            conf.put(ItemConfiguration.WEIGHT, source.get("weight").getAsDouble());
        if (source.has("ge_buy_limit"))
            conf.put(ItemConfiguration.GE_LIMIT, source.get("ge_buy_limit").getAsInt());
        if (source.has("weapon_interface"))
            conf.put(ItemConfiguration.WEAPON_INTERFACE, source.get("weapon_interface").getAsInt());
        if (source.has("has_special"))
            conf.put(ItemConfiguration.HAS_SPECIAL, source.get("has_special").getAsBoolean());
        if (source.has("destroy"))
            conf.put(ItemConfiguration.DESTROY, source.get("destroy").getAsBoolean());
        if (source.has("two_handed"))
            conf.put(ItemConfiguration.TWO_HANDED, source.get("two_handed").getAsBoolean());
        if (source.has("tradeable"))
            conf.put(ItemConfiguration.TRADEABLE, source.get("tradeable").getAsBoolean());
        if (source.has("shop_price"))
            conf.put(ItemConfiguration.SHOP_PRICE, source.get("shop_price").getAsInt());
        if (source.has("low_alch"))
            conf.put(ItemConfiguration.LOW_ALCHEMY, source.get("low_alch").getAsInt());
        if (source.has("high_alch"))
            conf.put(ItemConfiguration.HIGH_ALCHEMY, source.get("high_alch").getAsInt());
        if (source.has("weight_id"))
            definition.setWeightId(source.get("weight_id").getAsInt());
        if (source.has("equipment"))
            definition.setEquipment(source.get("equipment").getAsBoolean());
        if (source.has("team_id"))
            definition.setTeamId(source.get("team_id").getAsInt());
        if (source.has("value"))
            definition.setValue(source.get("value").getAsInt());
        if (source.has("note_id"))
            definition.setNoteId(source.get("note_id").getAsInt());
        if (source.has("note_template_id"))
            definition.setNoteTemplateId(source.get("note_template_id").getAsInt());
    }

    private void parseBonuses(ItemDefinition definition, JsonObject source) {
        if (!source.has("bonuses"))
            return;
        String[] split = source.get("bonuses").getAsString().split(":");
        if (split.length > 0) {
            int[] bonuses = new int[16];
            for (int i = 0; i < split.length; i++) {
                try {
                    int v = Integer.parseInt(split[i]);
                    bonuses[i] = v;
                } catch (NumberFormatException ex) {
                    log.error("{} - id: {} - invalid bonus [{}].",
                        definition.getName(),
                        definition.getId(),
                        split[i]);
                    throw ex;
                }
            }
            definition.getConfigurations().put(ItemConfiguration.BONUS, bonuses);
        }
    }

    private void parseAnimations(ItemDefinition definition, Map<String, Object> conf, JsonObject source) {
        if (source.has("stand_anim"))
            conf.put(ItemConfiguration.STAND_ANIM, source.get("stand_anim").getAsInt());
        if (source.has("stand_turn_anim"))
            conf.put(ItemConfiguration.STAND_TURN_ANIM, source.get("stand_turn_anim").getAsInt());
        if (source.has("walk_anim"))
            conf.put(ItemConfiguration.WALK_ANIM, source.get("walk_anim").getAsInt());
        if (source.has("run_anim"))
            conf.put(ItemConfiguration.RUN_ANIM, source.get("run_anim").getAsInt());
        if (source.has("turn_180_anim"))
            conf.put(ItemConfiguration.TURN180_ANIM, source.get("turn_180_anim").getAsInt());
        if (source.has("turn_90_cw_anim"))
            conf.put(ItemConfiguration.TURN90CW_ANIM, source.get("turn_90_cw_anim").getAsInt());
        if (source.has("turn_90_ccw_anim"))
            conf.put(ItemConfiguration.TURN90CCW_ANIM, source.get("turn_90_ccw_anim").getAsInt());
        if (source.has("defence_anim"))
            conf.put(ItemConfiguration.DEFENCE_ANIMATION, new Animation(source.get("defence_anim").getAsInt()));

        if (source.has("attack_anims")) {
            String[] split = source.get("attack_anims").getAsString().split(":");
            if (split.length > 0) {
                Animation[] animations = new Animation[split.length];
                for (int i = 0; i < split.length; i++) {
                    try {
                        int v = Integer.parseInt(split[i]);
                        animations[i] = new Animation(v, Animator.Priority.HIGH);
                    } catch (NumberFormatException ex) {
                        log.error("{} - id: {} - invalid attack animation: [{}].",
                            definition.getName(),
                            definition.getId(),
                            split[i]);
                        throw ex;
                    }
                }
                conf.put(ItemConfiguration.ATTACK_ANIMS, animations);
            }
        }
    }

    private void parseAudio(ItemDefinition definition, JsonObject source) {
        if (!source.has("attack_audio"))
            return;
        String[] split = source.get("attack_audio").getAsString().split(":");
        if (split.length > 0) {
            Audio[] audio = new Audio[split.length];
            for (int i = 0; i < split.length; i++) {
                try {
                    int v = Integer.parseInt(split[i]);
                    if (v != 0)
                        audio[i] = new Audio(v);
                } catch (NumberFormatException ex) {
                    log.error("{} - id: {} - invalid audio: [{}].",
                        definition.getName(),
                        definition.getId(),
                        split[i]);
                    throw ex;
                }
            }
            definition.getConfigurations().put(ItemConfiguration.ATTACK_AUDIO, audio);
        }
    }

    /* COMPILER HACK  - This allows throwing checked exceptions without an actual 'throws' declaration. */
    // TODO move to some kind of utility class, since this will be useful for other similiar cases


    private void unchecked(Throwable t) {
        this.unchecked0(t);
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> void unchecked0(Throwable t) throws T {
        throw (T) t;
    }

}
