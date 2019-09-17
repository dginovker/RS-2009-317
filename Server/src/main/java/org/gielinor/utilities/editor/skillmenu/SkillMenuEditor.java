package org.gielinor.utilities.editor.skillmenu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import org.gielinor.utilities.editor.skillmenu.tab.EditorTab;

/**
 * A utility tool for editing a {@link org.gielinor.utilities.editor.skillmenu.SkillMenuEditor} directly from the database.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkillMenuEditor extends JFrame {

    /**
     * The size of the editor.
     */
    public static final Dimension SIZE = new Dimension(765, 503);
    /**
     * The {@link javax.swing.JTabbedPane} instance.
     */
    private final JTabbedPane jTabbedPane;

    /**
     * The default constructor.
     */
    public SkillMenuEditor() {
        this.jTabbedPane = new JTabbedPane();
        init();
    }

    /**
     * Initializes the UI.
     */
    private SkillMenuEditor init() {
        setTitle("Skill Menu Editor");
        setSize(SIZE);
        setResizable(false);
        addTab("Editor", new JScrollPane(EditorTab.INSTANCE.init()));
        getContentPane().add(jTabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return this;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SkillMenuEditor skillMenuEditor = new SkillMenuEditor();
            skillMenuEditor.setVisible(true);
        });
    }

    /**
     * Adds a new {@link org.gielinor.utilities.editor.skillmenu.tab.Panel} tab.
     *
     * @param title     The title of the tab.
     * @param component The component to show when the tab is opened.
     */
    public void addTab(String title, Component component) {
        jTabbedPane.addTab(title, component);
    }
}
