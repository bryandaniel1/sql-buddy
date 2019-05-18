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
package com.daniel.sqlbuddy.controller;

import com.daniel.sqlbuddy.logic.QueryWorker;
import com.daniel.sqlbuddy.model.ConnectionProperties;
import com.daniel.sqlbuddy.ui.ConnectionSetupWindow;
import static com.daniel.sqlbuddy.ui.QueryWindow.CHANGE_CONNECTION_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.CLEAR_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.EXIT_COMMAND;
import static com.daniel.sqlbuddy.ui.QueryWindow.RUN_QUERY_COMMAND;
import com.daniel.sqlbuddy.ui.WindowAssembler;
import com.daniel.sqlbuddy.ui.WindowAssemblyManufacturer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 * Handles action events fired from the button panel of the query window. The
 * functions provided include exiting the program, changing connection
 * configuration, clearing results, and executing queries.
 *
 * @author Bryan Daniel
 */
public class QueryListener implements ActionListener {

    /**
     * The input area for queries
     */
    private final JTextPane queryTextPane;

    /**
     * The database connection properties
     */
    private final ConnectionProperties properties;

    /**
     * The tabbed pane holding results
     */
    private final JTabbedPane tabbedPane;

    /**
     * This constructor sets the values for the text area, the connection
     * properties, and the tabbed pane.
     *
     * @param queryTextPane the text area for query input
     * @param properties the database connection properties
     * @param tabbedPane the tabbed pane
     */
    public QueryListener(JTextPane queryTextPane, ConnectionProperties properties,
            JTabbedPane tabbedPane) {
        this.queryTextPane = queryTextPane;
        this.properties = properties;
        this.tabbedPane = tabbedPane;
    }

    /**
     * This method responds to the action event by determining the action
     * command and executing the appropriate process.
     *
     * @param e the ActionEvent object
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        switch (command) {
            case EXIT_COMMAND:
                System.exit(0);
                break;
            case CHANGE_CONNECTION_COMMAND:
                WindowAssemblyManufacturer factory = WindowAssemblyManufacturer.getInstance();
                WindowAssembler assembler = factory.employWindowAssemblerForConnectionSetup();
                ConnectionSetupWindow setupWindow = (ConnectionSetupWindow) assembler.assembleWindow();
                setupWindow.setLocationRelativeTo(null);
                setupWindow.setVisible(true);
                SwingUtilities.windowForComponent(queryTextPane).dispose();
                break;
            case CLEAR_COMMAND:
                tabbedPane.removeAll();
                tabbedPane.repaint();
                break;
            case RUN_QUERY_COMMAND:
                String textToExecute;
                if (queryTextPane.getSelectedText() != null) {
                    textToExecute = queryTextPane.getSelectedText();
                } else {
                    textToExecute = queryTextPane.getText();
                }
                new QueryWorker(textToExecute, properties, tabbedPane)
                        .execute();
                break;
            default:
                break;
        }
    }

}
