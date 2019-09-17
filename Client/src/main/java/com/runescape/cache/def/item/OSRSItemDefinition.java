package com.runescape.cache.def.item;

import com.runescape.cache.def.DefinitionData;
import com.runescape.cache.def.impl.ItemRepository;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Corey on 17/08/2017.
 */
public class OSRSItemDefinition extends ItemDefinition {

    public static OSRSItemDefinition[] cache;
    private static DefinitionData definitionData = new DefinitionData();

    public static ItemDefinition forId(int itemId) {
        int newItemId = itemId - 20000;
        for (int count = 0; count < 10; count++) {
            if (cache[count].id == newItemId) {
                return cache[count];
            }
        }

        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 10);
        ItemDefinition itemDefinition = cache[definitionData.getCacheIndex()];

        if (newItemId >= definitionData.getIndices().length) {
            newItemId = 0;
        }

        definitionData.getBuffer().currentPosition = definitionData.getOffset(newItemId);
        itemDefinition.id = itemId;
        itemDefinition.setDefaults();
        itemDefinition.decode(definitionData.getBuffer());

        if (itemDefinition.noteTemplateId != -1) {
            itemDefinition.toNote();
        }
        if (itemDefinition.id == newItemId && itemDefinition.modifiedColors == null) {
            itemDefinition.modifiedColors = new int[1];
            itemDefinition.originalColors = new int[1];
            itemDefinition.modifiedColors[0] = 0;
            itemDefinition.originalColors[0] = 1;
        }
        itemDefinition = ItemRepository.forId(itemId, itemDefinition);
        return itemDefinition;
    }

    public static void unpackConfig(CacheArchive archive) {
        definitionData.load(archive, "obj", true);

        cache = new OSRSItemDefinition[10];
        for (int index = 0; index < 10; index++) {
            cache[index] = new OSRSItemDefinition();
        }

        /*for (int i = 20_000; i < 50_000; i++) {
            ItemDefinition item = null;

            try {
                item = forId(i);
            } catch (Exception e) {
                continue;
            }

            if (item == null || item.name == null || item.name.trim().equalsIgnoreCase("null")) {
                continue;
            }

            try {
                writeToFile((i - 20_000) + "\t" + item.name, "osrs_item_list.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        //dumpDefinitions(definitionData.getCount());
    }

//    private static void dumpDefinitions(int maxItems) {
//        for (int id = 20000; id < maxItems + 20000; id++) {
//            ItemDefinition itemDef = ItemDefinition.forId(id);
//
//            if (itemDef == null) {
//                continue;
//            }
//            if (itemDef.name == null || itemDef.name.trim().equalsIgnoreCase("null")) {
//                continue;
//            }
//            if (itemDef.name.trim().equalsIgnoreCase("sliding piece")) {
//                continue;
//            }
//            System.out.println(itemDef.name);
//
//            CsvBuilder cb = new CsvBuilder(id);
//
//            cb.append(itemDef.name);
//            cb.append(itemDef.interfaceModelId);
//
//            String options;
//            if (itemDef.options != null) {
//                List<String> inventoryOptions = Arrays.asList(itemDef.options);
//                options = inventoryOptions.toString().replace("[", "").replace("]", "").replace(", ", ":").replace(",", ":").trim();
//            } else {
//                options = "null:null:null:null:drop";
//            }
//            cb.append(options);
//
//            String groundOptions;
//            if (itemDef.floorOptions != null) {
//                List<String> groundItemOptions = Arrays.asList(itemDef.floorOptions);
//                groundOptions = groundItemOptions.toString().replace("[", "").replace("]", "").replace(",", ":").trim();
//            } else {
//                groundOptions = "null:null:take:null:null";
//            }
//            cb.append(groundOptions);
//
//            cb.append(itemDef.noteTemplateId == -1 ? 0 : 1);
//            cb.append(itemDef.noteItemId);
//            cb.append(itemDef.noteTemplateId);
//            cb.append(itemDef.team);
//
//            try {
//                writeToFile(cb.toString(), "C:\\Users\\Corey\\Desktop\\OSRSDefDump.csv");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    public static void writeToFile(String line, String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), charset, StandardOpenOption.APPEND)) {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void setDefaults() {
        super.setDefaults();
        osrs = true;
    }

    @Override
    public void readValues(RSStream stream) {
        do {
            int i = stream.getByte();

            if (i == 0)
                return;
            if (i == 1) {
                interfaceModelId = stream.getShort();
            } else if (i == 2)
                name = stream.getString();
            else if (i == 3)
                description = stream.getString().getBytes();
            else if (i == 4)
                modelZoom = stream.getShort();
            else if (i == 5)
                modelRotationY = stream.getShort();
            else if (i == 6)
                modelRotationX = stream.getShort();
            else if (i == 7) {
                modelPositionX = stream.getShort();
                if (modelPositionX > 32767)
                    modelPositionX -= 0x10000;
            } else if (i == 8) {
                modelPositionY = stream.getShort();
                if (modelPositionY > 32767)
                    modelPositionY -= 0x10000;
            } else if (i == 10)
                stream.getShort();
            else if (i == 11)
                stackingType = true;
            else if (i == 12)
                value = stream.getInt();
            else if (i == 16)
                members = true;
            else if (i == 23) {
                maleModelId = stream.getShort();
                maleModelTranslationY = (byte) stream.getByte();
            } else if (i == 24)
                maleArmModelId = stream.getShort(); //todo idekf
            else if (i == 25) {
                femaleModelId = stream.getShort();
                femaleModelTranslationY = (byte) stream.getByte();
            } else if (i == 26)
                femaleArmModelId = stream.getShort(); //todo idefk
            else if (i >= 30 && i < 35) {
                if (floorOptions == null)
                    floorOptions = new String[5];
                floorOptions[i - 30] = stream.getString();
                if (floorOptions[i - 30].equalsIgnoreCase("hidden"))
                    floorOptions[i - 30] = null;
            } else if (i >= 35 && i < 40) {
                if (options == null)
                    options = new String[5];
                options[i - 35] = stream.getString();
                if (options[i - 35].equalsIgnoreCase("null"))
                    options[i - 35] = null;
            } else if (i == 40) {
                int j = stream.getByte();
                modifiedColors = new int[j];
                originalColors = new int[j];
                for (int k = 0; k < j; k++) {
                    modifiedColors[k] = stream.getShort();
                    originalColors[k] = stream.getShort();
                }
            } else if (i == 41 || i == 42) {
                //System.out.println("cool test lol! " + i);
                int j = stream.getByte();
                // originalColors = new int[j];
                //modifiedColors = new int[j];
                for (int k = 0; k < j; k++) {
                    stream.getShort();
                    stream.getShort();
                }
            } else if (i == 78)
                maleModelId3 = stream.getShort();
            else if (i == 79)
                femaleModelId3 = stream.getShort();
            else if (i == 90)
                equipped_model_male_dialogue_1 = stream.getShort();
            else if (i == 91)
                equipped_model_female_dialogue_1 = stream.getShort();
            else if (i == 92)
                equipped_model_male_dialogue_2 = stream.getShort();
            else if (i == 93)
                equipped_model_female_dialogue_2 = stream.getShort();
            else if (i == 95)
                modelRotationZ = stream.getShort();
            else if (i == 97)
                noteItemId = stream.getShort() + 20000;
            else if (i == 98)
                noteTemplateId = stream.getShort() + 20000;
            else if (i >= 100 && i < 110) {
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }
                stackIds[i - 100] = stream.getShort();
                stackAmounts[i - 100] = stream.getShort();
            } else if (i == 110)
                modelScaleX = stream.getShort();
            else if (i == 111)
                modelScaleY = stream.getShort();
            else if (i == 112)
                modelScaleZ = stream.getShort();
            else if (i == 113)
                lightIntensity = stream.getSignedByte();
            else if (i == 114)
                lightMagnitude = stream.getSignedByte() * 5;
            else if (i == 115)
                team = stream.getByte();
            else if (i == 116)
                lentId = stream.getShort() + 20000;
            else if (i == 117)
                lentTemplateId = stream.getShort() + 20000;
            else
                System.out.println("Invalid OSRS Item readvalue opcode: " + i);
        } while (true);
    }

}
