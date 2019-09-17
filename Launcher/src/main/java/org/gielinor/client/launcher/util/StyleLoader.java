package org.gielinor.client.launcher.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Corey on 15/08/2017.
 */
public class StyleLoader {

    private static final String HTML_PATH = "/themes/default/prerequisites.html";
    private static final String CSS_PATH = "/themes/default/style.css";

    public void loadFiles() {
        InputStream htmlStream = getClass().getResourceAsStream(HTML_PATH);
        InputStream cssStream = getClass().getResourceAsStream(CSS_PATH);

        Misc.htmlPrerequisites = new Scanner(htmlStream, "UTF-8").useDelimiter("\\A").next();
        Misc.cssStyling = new Scanner(cssStream, "UTF-8").useDelimiter("\\A").next();
    }
}
