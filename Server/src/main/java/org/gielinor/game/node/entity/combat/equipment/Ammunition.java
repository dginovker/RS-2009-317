package org.gielinor.game.node.entity.combat.equipment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Represents range ammunition types.
 * @author Emperor
 */
public final class Ammunition {

    private static final Logger log = LoggerFactory.getLogger(Ammunition.class);

    /**
     * The ammunition mapping.
     */
    private static final Map<Integer, Ammunition> AMMUNITION = new HashMap<Integer, Ammunition>();

    /**
     * The ammunition item id.
     */
    private final int itemId;

    /**
     * The start graphics.
     */
    private final Graphics startGraphics;

    /**
     * The start graphics when using Dark bow.
     */
    private final Graphics darkBowGraphics;

    /**
     * The projectile.
     */
    private final Projectile projectile;

    /**
     * The poison damage.
     */
    private final int poisonDamage;

    /**
     * Constructs a new {@code Ammunition} object.
     * @param itemId The item id.
     * @param startGraphics The start graphics.
     * @param darkBowGraphics The dark bow start graphics.
     * @param projectile The projectile.
     * @param poisonDamage The poison damage the ammunition can do.
     */
    public Ammunition(int itemId, Graphics startGraphics, Graphics darkBowGraphics, Projectile projectile, int poisonDamage) {
        this.itemId = itemId;
        this.startGraphics = startGraphics;
        this.darkBowGraphics = darkBowGraphics;
        this.poisonDamage = poisonDamage;
        this.projectile = projectile;
    }

    /**
     * Loads all the {@code Ammunition} info to the mapping.
     * @return <code>True</code>.
     */
    public static final boolean initialize() {
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File("data/world/node/item/ammunition_data.xml"));
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            log.error("Failed to load ammunition definitions.", ex);
            return false;
        }
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        log.info("Loading ranged ammunition definitions...");
        for (short i = 1; i < nodeList.getLength(); i += 2) {
            Node n = nodeList.item(i);
            if (n != null) {
                if (n.getNodeName().equalsIgnoreCase("Ammunition")) {
                    NodeList list = n.getChildNodes();
                    int itemId = 0;
                    int graphicsId = 0;
                    Graphics startGraphics = null;
                    Graphics darkBowGraphics = null;
                    Projectile projectile = null;
                    for (int a = 1; a < list.getLength(); a += 2) {
                        Node node = list.item(a);
                        if (node.getNodeName().equalsIgnoreCase("itemId")) {
                            itemId = Integer.parseInt(node.getTextContent());
                        } else if (node.getNodeName().equalsIgnoreCase("startGraphicsId")) {
                            graphicsId = Integer.parseInt(node.getTextContent());
                        } else if (node.getNodeName().equalsIgnoreCase("startGraphicsHeight")) {
                            startGraphics = new Graphics(graphicsId, Integer.parseInt(node.getTextContent()), 0);
                        } else if (node.getNodeName().equalsIgnoreCase("darkBowGraphicsId")) {
                            graphicsId = Integer.parseInt(node.getTextContent());
                        } else if (node.getNodeName().equalsIgnoreCase("darkBowGraphicsHeight")) {
                            darkBowGraphics = new Graphics(graphicsId, Integer.parseInt(node.getTextContent()), 0);
                        } else if (node.getNodeName().equalsIgnoreCase("projectileId")) {
                            int startHeight = Integer.parseInt(node.getAttributes().getNamedItem("start_height").getTextContent());
                            int type = Integer.parseInt(node.getAttributes().getNamedItem("type").getTextContent());
                            int angle = Integer.parseInt(node.getAttributes().getNamedItem("angle").getTextContent());
                            int baseSpeed = Integer.parseInt(node.getAttributes().getNamedItem("base_speed").getTextContent());
                            int projectileId = Integer.parseInt(node.getTextContent());
                            projectile = Projectile.create((Entity) null, null, projectileId, startHeight, 36, type, baseSpeed, angle, 0);
                        } else if (node.getNodeName().equalsIgnoreCase("poisonDamage")) {
                            AMMUNITION.put(itemId, new Ammunition(itemId, startGraphics, darkBowGraphics, projectile, Integer.parseInt(node.getTextContent())));
                        }
                    }
                }
            }
        }
        log.info("Loaded {} ammunition definitions.", AMMUNITION.size());
        return true;
    }

    /**
     * Gets the ammunition mapping.
     * @return The mapping.
     */
    public static Map<Integer, Ammunition> getAmmunition() {
        return AMMUNITION;
    }

    /**
     * Gets an ammunition object from the mapping.
     * @param id The ammo id.
     * @return The ammunition object.
     */
    public static final Ammunition get(int id) {
        return AMMUNITION.get(id);
    }

    /**
     * @return the itemId
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * @return the startGraphics
     */
    public Graphics getStartGraphics() {
        return startGraphics;
    }

    /**
     * @return the darkBowGraphics
     */
    public Graphics getDarkBowGraphics() {
        return darkBowGraphics;
    }

    /**
     * @return the projectile
     */
    public Projectile getProjectile() {
        return projectile;
    }

    /**
     * @return the poisonDamage
     */
    public int getPoisonDamage() {
        return poisonDamage;
    }

}
