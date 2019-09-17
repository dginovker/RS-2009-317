import java.io.File
import org.jdom.Document
import org.jdom.Element
import org.jdom.input.SAXBuilder

object ReadXMLFile {
    @JvmStatic
    fun main(args: Array<String>) {

        val builder = SAXBuilder()
        val xmlFile = File("./spawns.xml")

        val document = builder.build(xmlFile) as Document
        val rootNode = document.rootElement
        val list = rootNode.getChildren("NpcSpawnDefinition")

        var sqlOutput = "INSERT INTO npc_spawn VALUES "
        for (i in list.indices) {

            val node = list[i] as Element

             // INSERT INTO npc_spawn VALUES (id, npc_id, x, y, z, walks, radius, face, description);
            sqlOutput += "("
            sqlOutput += "NULL"
            sqlOutput += ", "
            sqlOutput += (Integer.parseInt(node.getChildText("id")) + 20000)
            sqlOutput += ", "

            val location = node.getChildren("location")
            for (i2 in location.indices) {
                val node2 = location[i2] as Element
                sqlOutput += (Integer.parseInt(node2.getChildText("x")))
                sqlOutput += ", "
                sqlOutput += (Integer.parseInt(node2.getChildText("y")))
                sqlOutput += ", "
                sqlOutput += Integer.parseInt(node2.getChildText("z"))
                sqlOutput += ", "
            }

            sqlOutput += node.getChildText("walk")
            sqlOutput += ", "
            sqlOutput += "3, "
            sqlOutput += node.getChildText("face")
            sqlOutput += ", "
            sqlOutput += "'CONVERTED_FROM_XML'"
            sqlOutput += "), "
        }

        System.out.println(sqlOutput)
    }
}