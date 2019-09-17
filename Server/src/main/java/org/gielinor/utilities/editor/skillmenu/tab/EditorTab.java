package org.gielinor.utilities.editor.skillmenu.tab;

/**
 * Represents the editor {@link org.gielinor.utilities.editor.skillmenu.tab.Panel} tab.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class EditorTab extends Panel {

    /**
     * The singleton instance.
     */
    public static final EditorTab INSTANCE = new EditorTab();

    @Override
    public String getTabName() {
        return "Editor";
    }

    @Override
    public Panel getInstance() {
        return INSTANCE;
    }

    @Override
    public Panel init() {
        setVisible(true);
        return this;
    }

    @Override
    public Panel configure() {
        return this;
    }
}
