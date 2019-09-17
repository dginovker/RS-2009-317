package org.gielinor.parser.npc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.npc.drop.NPCDropTables;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the NPC drops parsing.
 *
 * @author Emperor
 */
public final class NPCDropsParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(NPCDropsParser.class);

    @Override
    public boolean parse() {
        log.info("Requesting NPC drop definitions...");
        int count = 0;
        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM npc_drop")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            NPCDefinition npcDefinition;
            while (resultSet.next()) {
                int npcId = resultSet.getInt("npc_id");
                int itemId = resultSet.getInt("item_id");
                int minimumCount = resultSet.getInt("minimum_count");
                int maximumCount = resultSet.getInt("maximum_count");
                double chanceRate = resultSet.getDouble("chance_rate");
                String dropFrequency = resultSet.getString("drop_frequency");
                String dropTable = resultSet.getString("table");
                npcDefinition = NPCDefinition.forId(npcId);
                if (npcDefinition.getDropTables() == null) {
                    npcDefinition.setDropTables(new NPCDropTables(npcDefinition));
                }
                ChanceItem chanceItem = new ChanceItem(itemId, minimumCount, maximumCount, 1000, chanceRate,
                    dropFrequency);
                switch (dropTable.toUpperCase()) {
                    case "DEFAULT":
                        npcDefinition.getDropTables().getDefaultTable().add(chanceItem);
                        count++;
                        break;
                    case "MAIN":
                        npcDefinition.getDropTables().getMainTable().add(chanceItem);
                        count++;
                        break;
                    case "CHARM":
                        npcDefinition.getDropTables().getCharmTable().add(chanceItem);
                        count++;
                        break;
                    default:
                        log.warn("Invalid table format: [{}].", dropTable);
                        break;
                }
                if (npcDefinition.getDropTables() != null) {
                    npcDefinition.getDropTables().prepare();
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst requesting NPC drop definitions.", ex);
        }
        NPCDropTables.setCount(count);
        log.info("Received and parsed {} NPC drop definitions.", count);
        return true;
    }

}
