/*
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.daniel.sqlbuddy.ui;

import com.daniel.sqlbuddy.controller.FileListener;
import com.daniel.sqlbuddy.controller.QueryListener;
import com.daniel.sqlbuddy.controller.QueryTextAreaKeyListener;
import com.daniel.sqlbuddy.model.ConnectionProperties;
import static com.daniel.sqlbuddy.ui.QueryWindow.BUTTONS_BORDER_TITLE;
import static com.daniel.sqlbuddy.ui.QueryWindow.CENTER_PANEL_WIDTH;
import static com.daniel.sqlbuddy.ui.QueryWindow.CHANGE_CONNECTION_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.CLEAR_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.EXIT_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.GUI_TITLE_STRING;
import static com.daniel.sqlbuddy.ui.QueryWindow.PILLAR_WIDTH;
import static com.daniel.sqlbuddy.ui.QueryWindow.QUERIES_TITLE;
import static com.daniel.sqlbuddy.ui.QueryWindow.QUERY_WINDOW_HEIGHT;
import static com.daniel.sqlbuddy.ui.QueryWindow.QUERY_WINDOW_WIDTH;
import static com.daniel.sqlbuddy.ui.QueryWindow.RESULTS_TITLE;
import static com.daniel.sqlbuddy.ui.QueryWindow.RUN_QUERY_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.WELCOME;
import com.daniel.sqlbuddy.ui.theme.Theme;
import com.daniel.sqlbuddy.ui.theme.ThemeManager;
import com.daniel.sqlbuddy.ui.theme.ThemeManager.ThemeSelection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

/**
 * This WindowAssembler creates a user interface for the execution of queries
 * and display of result sets.
 *
 * @author Bryan Daniel
 */
public class QueryWindowAssembler implements WindowAssembler {

    /**
     * The database connection properties
     */
    private final ConnectionProperties properties;

    /**
     * The input area for queries
     */
    private JTextPane queryTextPane;

    /**
     * The action listener for the query window
     */
    private ActionListener queryListener;

    /**
     * The theme manager used to create themes
     */
    private ThemeManager themeManager;

    /**
     * The style theme of the user interface
     */
    private Theme theme;

    /**
     * The constructor sets the value for properties.
     *
     * @param properties the properties
     */
    public QueryWindowAssembler(ConnectionProperties properties) {
        this.properties = properties;
        themeManager = new ThemeManager();
    }

    /**
     * This assembleWindow implementation creates the look and feel of the query
     * window and assigns the action listener.
     *
     * @return the query window
     */
    @Override
    public JFrame assembleWindow() {

        QueryWindow queryWindow = new QueryWindow(WELCOME);
        queryWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        queryWindow.setSize(QUERY_WINDOW_WIDTH.intValue(), QUERY_WINDOW_HEIGHT.intValue());

        if (theme == null) {
            theme = themeManager.createTheme(ThemeManager.ThemeSelection.METALLIC);
        }
        buildContentPane(queryWindow);
        buildMenuBar(queryWindow);

        return queryWindow;
    }

    /**
     * The title panel contains the name of the program.
     *
     * @author Bryan Daniel
     */
    private class TitlePanel extends JPanel {

        /**
         * Serial version UID
         */
        private static final long serialVersionUID = 1L;

        /**
         * The constructor for the title panel calls the setValues method.
         */
        private TitlePanel() {
            setValues();
        }

        /**
         * This method sets the necessary values for the query panel.
         */
        private void setValues() {
            add(new JLabel("<html><head><style>.title{text-align:center;font-size:150%;color:black;}"
                    + "</style></head><p class=\"title\">" + GUI_TITLE_STRING + "</p></html>"), JLabel.CENTER);
            TitledBorder titledBorder;
            titledBorder
                    = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                            "<html><font color=black size=+1>" + WELCOME + "</html>");
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            setBorder(titledBorder);
        }

        /**
         * This overrides the paintComponent method to display a gradient color
         * background.
         *
         * @param graphics the Graphics object
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(theme.getRedValue(), theme.getGreenValue(),
                    theme.getBlueValue());
            Color color2 = color1.darker();
            Color[] colors = {color1, color1, color2};
            float radius = width;
            Point2D center = new Point2D.Float(width / 2, height / 3);
            float[] fractions = {0.0f, 0.2f, 1.0f};
            RadialGradientPaint radialGradientPaint = new RadialGradientPaint(center,
                    radius, fractions, colors);
            graphics2d.setPaint(radialGradientPaint);
            graphics2d.fillRect(0, 0, width, height);
            graphics2d.dispose();
        }
    }

    /**
     * The query panel holds the text area to take user input for queries.
     *
     * @author Bryan Daniel
     */
    private class QueryPanel extends JPanel {

        /**
         * Serial version UID
         */
        private static final long serialVersionUID = 1L;

        /**
         * The constructor for the query panel calls the setValues method.
         */
        private QueryPanel() {
            setValues();
        }

        /**
         * This method sets the necessary values for the query panel.
         */
        private void setValues() {
            setLayout(new GridLayout(1, 3));
            setPreferredSize(new Dimension(CENTER_PANEL_WIDTH.intValue(), 0));
            TitledBorder titledBorder;
            titledBorder
                    = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                            "<html><font color=black size=+1>" + QUERIES_TITLE + "</html>");
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            setBorder(titledBorder);

            // wrap a scrollpane around text area
            JScrollPane scrollPane = new JScrollPane(queryTextPane);
            scrollPane.setPreferredSize(new Dimension(200, 200));

            add(scrollPane);
        }

        /**
         * This overrides the paintComponent method to display a gradient color
         * background.
         *
         * @param graphics the Graphics object
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            Color color1 = new Color(theme.getRedValue(), theme.getGreenValue(),
                    theme.getBlueValue());
            Color color2 = color1.darker();
            Color color3 = color2.darker();
            Color[] colors = {color3, color1, color1, color3};
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(CENTER_PANEL_WIDTH.intValue(), 0);
            float[] fractions = {0.0f, 0.4f, 0.6f, 1.0f};
            int width = getWidth();
            int height = getHeight();
            LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start,
                    end, fractions, colors);
            graphics2d.setPaint(linearGradientPaint);
            graphics2d.fillRect(0, 0, width, height);
            graphics2d.dispose();
        }
    }

    /**
     * The results panel holds the text area to display result sets to the user.
     *
     * @author Bryan Daniel
     */
    public class ResultsPanel extends JPanel {

        /**
         * Serial version UID
         */
        private static final long serialVersionUID = 1L;

        /**
         * The constructor for the results panel calls the setValues method.
         */
        private ResultsPanel() {
            setValues();
        }

        /**
         * This method sets the necessary values for the results panel.
         */
        private void setValues() {
            setLayout(new GridLayout(1, 1));
            setPreferredSize(new Dimension(CENTER_PANEL_WIDTH.intValue(), 0));
            TitledBorder titledBorder;
            titledBorder
                    = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                            "<html><font color=black size=+1>" + RESULTS_TITLE + "</html>");
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            setBorder(titledBorder);
        }

        /**
         * This overrides the paintComponent method to display a gradient color
         * background.
         *
         * @param graphics the Graphics object
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            Color color1 = new Color(theme.getRedValue(), theme.getGreenValue(),
                    theme.getBlueValue());
            Color color2 = color1.darker();
            Color color3 = color2.darker();
            Color[] colors = {color3, color1, color1, color3};
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(CENTER_PANEL_WIDTH.intValue(), 0);
            float[] fractions = {0.0f, 0.4f, 0.6f, 1.0f};
            int width = getWidth();
            int height = getHeight();
            LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start,
                    end, fractions, colors);
            graphics2d.setPaint(linearGradientPaint);
            graphics2d.fillRect(0, 0, width, height);
            graphics2d.dispose();
        }
    }

    /**
     * This class resembles a cylindrical pillar supporting the title panel.
     *
     * @author Bryan Daniel
     */
    private class Pillar extends JPanel {

        /**
         * Serial version UID
         */
        private static final long serialVersionUID = 1L;

        /**
         * The Pillar constructor calls the setValues method.
         */
        private Pillar() {
            setValues();
        }

        /**
         * This method sets the necessary values for the pillar.
         */
        private void setValues() {
            setPreferredSize(new Dimension(PILLAR_WIDTH.intValue(), 0));
        }

        /**
         * This overrides the paintComponent method to display a gradient color
         * background.
         *
         * @param graphics the Graphics object
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            Color color1 = new Color(theme.getRedValue(), theme.getGreenValue(),
                    theme.getBlueValue());
            Color color2 = color1.darker();
            Color[] colors = {color2, color1, color2};
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(PILLAR_WIDTH.intValue(), 0);
            float[] fractions = {0.0f, 0.4f, 1.0f};
            int width = getWidth();
            int height = getHeight();
            LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start,
                    end, fractions, colors);
            graphics2d.setPaint(linearGradientPaint);
            graphics2d.fillRect(0, 0, width, height);
            graphics2d.dispose();
        }
    }

    /**
     * The button panel holds the 4 buttons corresponding to the program
     * functions of program exit, changing connection, clearing results, and
     * running a query.
     *
     * @author Bryan Daniel
     */
    private class ButtonPanel extends JPanel {

        /**
         * Serial version UID
         */
        private static final long serialVersionUID = 1L;

        /**
         * The ButtonPanel constructor calls the setValues method.
         */
        private ButtonPanel() {
            setValues();
        }

        /**
         * This method sets the necessary values for the button panel.
         */
        private void setValues() {
            setLayout(new GridLayout(1, 4));
            TitledBorder titledBorder;
            titledBorder
                    = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                            "<html><font color=black size=+1>" + BUTTONS_BORDER_TITLE + "</html>");
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            setBorder(titledBorder);

            //The exit button
            JButton exitButton = new QueryWindowButton(EXIT_COMMAND);

            //The button to change the connection
            JButton changeConnectionButton = new QueryWindowButton(CHANGE_CONNECTION_COMMAND);

            //The button to clear results
            JButton fileAndDbButton = new QueryWindowButton(CLEAR_COMMAND);

            //The button to execute the statements in the input window
            JButton queryButton = new QueryWindowButton(RUN_QUERY_COMMAND);

            //GroupLayout is declared.
            GroupLayout buttonGroup = new GroupLayout(this);
            this.setLayout(buttonGroup);

            //Creating gaps
            buttonGroup.setAutoCreateGaps(true);
            buttonGroup.setAutoCreateContainerGaps(true);

            //These statements set up the horizontal and vertical grouping for the panel
            buttonGroup.setHorizontalGroup(buttonGroup.createSequentialGroup().addComponent(exitButton).addComponent(changeConnectionButton)
                    .addComponent(fileAndDbButton).addComponent(queryButton).addGroup(buttonGroup.createParallelGroup(GroupLayout.Alignment.CENTER)));

            buttonGroup.setVerticalGroup(buttonGroup.createSequentialGroup().addGroup(buttonGroup.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(exitButton).addComponent(changeConnectionButton).addComponent(fileAndDbButton).addComponent(queryButton)));

            buttonGroup.linkSize(SwingConstants.HORIZONTAL, exitButton, changeConnectionButton, fileAndDbButton, queryButton);
            buttonGroup.linkSize(SwingConstants.VERTICAL, exitButton, changeConnectionButton, fileAndDbButton, queryButton);

            add(exitButton);
            add(changeConnectionButton);
            add(fileAndDbButton);
            add(queryButton);
        }

        /**
         * This overrides the paintComponent method to display a gradient color
         * background.
         *
         * @param graphics the Graphics object
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            Color color1 = new Color(theme.getRedValue(), theme.getGreenValue(),
                    theme.getBlueValue());
            Color color2 = color1.darker();
            int width = getWidth();
            int height = getHeight();
            GradientPaint gradientPaint = new GradientPaint(
                    0, 0, color1, 0, height, color2);
            graphics2d.setPaint(gradientPaint);
            graphics2d.fillRect(0, 0, width, height);
            graphics2d.dispose();
        }
    }

    /**
     * This class defines the buttons in the query window button panel.
     *
     * @author Bryan Daniel
     */
    private class QueryWindowButton extends JButton {

        /**
         * Constants
         */
        private static final long serialVersionUID = 1L;

        /**
         * The QueryWindowButton constructor calls the setValues method.
         *
         * @param text the button text
         */
        private QueryWindowButton(String text) {
            setValues(text);
        }

        /**
         * This method sets the necessary values for the button.
         *
         * @param text the button text
         */
        private void setValues(String text) {
            setText("<html><head><style>.button{text-align:center;color:black;}</style></head>"
                    + "<p class=\"button\">" + text + "<p></html>");

            setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.BLACK));
            setMaximumSize(new Dimension(160, 100));
            setActionCommand(text);
            addActionListener(queryListener);
        }
    }

    /**
     * Composes the menu bar of the query window and defines the menu item
     * actions.
     *
     * @param queryWindow the query editor window
     */
    private void buildMenuBar(QueryWindow queryWindow) {

        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu(QueryWindow.FILE_MENU);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        FileListener fileListener = new FileListener(queryTextPane);

        JMenuItem openMenuItem = new JMenuItem(FileListener.OPEN_COMMAND);
        openMenuItem.setToolTipText("Open file");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.addActionListener(fileListener);

        JMenuItem saveAsMenuItem = new JMenuItem(FileListener.SAVE_AS_COMMAND);
        saveAsMenuItem.setMnemonic(KeyEvent.VK_S);
        saveAsMenuItem.setToolTipText("Save file");
        saveAsMenuItem.addActionListener(fileListener);

        JMenuItem exitMenuItem = new JMenuItem(QueryWindow.EXIT_COMMAND);
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        JMenu editMenu = new JMenu(QueryWindow.EDIT_MENU);
        JMenu themesMenuItem = new JMenu(QueryWindow.THEMES_MENU);
        themesMenuItem.setToolTipText("Choose a UI theme.");

        JMenuItem metallicThemeMenuItem = new JMenuItem(ThemeSelection.METALLIC.getName());
        assignThemeChangeListener(queryWindow, metallicThemeMenuItem);

        JMenuItem sunnyThemeMenuItem = new JMenuItem(ThemeSelection.SUNNY.getName());
        assignThemeChangeListener(queryWindow, sunnyThemeMenuItem);

        JMenuItem underwaterThemeMenuItem = new JMenuItem(ThemeSelection.UNDERWATER.getName());
        assignThemeChangeListener(queryWindow, underwaterThemeMenuItem);

        themesMenuItem.add(metallicThemeMenuItem);
        themesMenuItem.add(sunnyThemeMenuItem);
        themesMenuItem.add(underwaterThemeMenuItem);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(exitMenuItem);
        editMenu.add(themesMenuItem);
        menubar.add(fileMenu);
        menubar.add(editMenu);
        queryWindow.setJMenuBar(menubar);
    }

    /**
     * Assigns the action listener to the given menu item to handle changing a
     * UI theme.
     *
     * @param queryWindow the query editor window
     * @param menuItem the menu item
     */
    private void assignThemeChangeListener(QueryWindow queryWindow, JMenuItem menuItem) {
        menuItem.addActionListener((event) -> {
            theme = themeManager.createTheme(ThemeSelection.valueOf(event.getActionCommand().toUpperCase()));
            queryWindow.getContentPane().removeAll();
            queryWindow.getContentPane().invalidate();
            buildContentPane(queryWindow);
            buildMenuBar(queryWindow);
            queryWindow.repaint();
            queryWindow.revalidate();
        });
    }

    /**
     * Creates and assembles the components for the window content.
     *
     * @param queryWindow the query editor window
     */
    private void buildContentPane(QueryWindow queryWindow) {
        queryTextPane = new JTextPane();
        queryTextPane.setPreferredSize(new Dimension(20, 30));
        queryTextPane.setEditable(true);
        queryTextPane.setEnabled(true);
        queryTextPane.setBackground(theme.getBackgroundColor());
        queryTextPane.setForeground(theme.getForegroundColor());
        queryTextPane.setSelectedTextColor(theme.getForegroundColor());
        queryTextPane.setCaretColor(theme.getCaretColor());
        queryTextPane.setDisabledTextColor(theme.getDisabledTextColor());

        QueryTextAreaKeyListener keyListener = new QueryTextAreaKeyListener(queryTextPane, theme);
        queryTextPane.addKeyListener(keyListener);

        ResultsPanel resultsPanel = new ResultsPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        resultsPanel.add(tabbedPane);

        queryListener = new QueryListener(queryTextPane, properties, tabbedPane);

        queryWindow.setLayout(new BorderLayout());
        queryWindow.add(new TitlePanel(), BorderLayout.NORTH);
        queryWindow.add(new Pillar(), BorderLayout.WEST);
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(new QueryPanel());
        centerPanel.add(resultsPanel);
        queryWindow.add(centerPanel, BorderLayout.CENTER);
        queryWindow.add(new Pillar(), BorderLayout.EAST);
        queryWindow.add(new ButtonPanel(), BorderLayout.SOUTH);
    }
}
