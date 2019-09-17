package org.gielinor.utilities.editor.skillmenu.tab;

import javax.swing.JPanel;

/**
 * Represents a panel extending {@link javax.swing.JPanel}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class Panel extends JPanel {

    /**
     * The name of this tab.
     */
    public abstract String getTabName();

    /**
     * The instance of this class.
     */
    public abstract Panel getInstance();

    /**
     * Initializes the new {@link Panel}.
     *
     * @return The {@link Panel} instance, for chaining.
     */
    public abstract Panel init();

    /**
     * Configures extras for this tab.
     *
     * @return The {@link Panel} instance, for chaining.
     */
    public abstract Panel configure();
}
