package com.runescape.cache.def.npc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runescape.cache.def.DefinitionData;
import com.runescape.cache.def.item.OSRSItemDefinition;
import com.runescape.net.CacheArchive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OSRSNPCDefinition extends NPCDefinition {

    private static DefinitionData definitionData = new DefinitionData();
    private static OSRSNPCDefinition[] cache;

    public static NPCDefinition forId(int npcId) {
        npcId -= 20_000;
        for (int index = 0; index < 20; index++) {
            if (cache[index].npcId == (long) npcId) {
                return cache[index];
            }
        }
        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 20);
        OSRSNPCDefinition npcDefinition = cache[definitionData.getCacheIndex()] = new OSRSNPCDefinition();
        definitionData.getBuffer().currentPosition = definitionData.getOffset(npcId);
        npcDefinition.npcId = npcId + 20_000;
        npcDefinition.osrs = true;
        npcDefinition.decode(definitionData.getBuffer());

        return npcDefinition;
    }

    public static void unpackConfig(CacheArchive archive) {
        definitionData.load(archive, "npc", true);
        cache = new OSRSNPCDefinition[20];
        for (int count = 0; count < 20; count++) {
            cache[count] = new OSRSNPCDefinition();
        }
        //dumpDefinitions();
    }

    public static void dumpDefinitions() {
        DefinitionsToDump definitionsToDump = new DefinitionsToDump();

        for (int id = 0; id < definitionData.getCount(); id++) {
            NPCDefinition npcDef = NPCDefinition.forId(id + 20_000);

            if (npcDef == null) {
                continue;
            }
            if (npcDef.name == null || npcDef.name.trim().equalsIgnoreCase("null")) {
                continue;
            }

            definitionsToDump.addNpc(npcDef);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            String json = gson.toJson(definitionsToDump);
            OSRSItemDefinition.writeToFile(json, "OSRSNpcDefinitions.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(definitionsToDump.npcs.size() + " NPCs dumped!");
    }

    public static class DefinitionsToDump {
        public List<NPCDefinition> npcs = new ArrayList<>();

        public void addNpc(NPCDefinition npc) {
            npcs.add(npc);
        }

    }

}
