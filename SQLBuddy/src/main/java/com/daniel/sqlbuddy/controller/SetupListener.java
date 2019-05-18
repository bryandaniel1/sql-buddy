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

import com.daniel.sqlbuddy.logic.TestQueryRunner;
import com.daniel.sqlbuddy.logic.ConnectionTestWorker;
import com.daniel.sqlbuddy.model.ConnectionProperties;
import static com.daniel.sqlbuddy.ui.ConnectionSetupWindow.BROWSE_COMMAND;
import static com.daniel.sqlbuddy.ui.ConnectionSetupWindow.CONNECT_COMMAND;
import static com.daniel.sqlbuddy.ui.ConnectionSetupWindow.EXIT_COMMAND;
import static com.daniel.sqlbuddy.ui.ConnectionSetupWindow.TEST_COMMAND;
import com.daniel.sqlbuddy.ui.QueryWindow;
import com.daniel.sqlbuddy.ui.WindowAssembler;
import com.daniel.sqlbuddy.ui.WindowAssemblyManufacturer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This ActinListener handles the events of the ConnectionSetupWindow object.
 *
 * @author Bryan Daniel
 */
public class SetupListener implements ActionListener {

    /**
     * The combo box holding the driver selection
     */
    private final JComboBox<String> databaseTypeComboBox;

    /**
     * The input for driver path
     */
    private final JTextField driverPathTextField;

    /**
     * The input for database name
     */
    private final JTextField databaseNameTextField;

    /**
     * The input for port number
     */
    private final JTextField portTextField;

    /**
     * The input for username
     */
    private final JTextField usernameField;

    /**
     * The input for password
     */
    private final JPasswordField passwordField;

    /**
     * The label to display the test result
     */
    private final JLabel resultLabel;

    /**
     * The logger for this class
     */
    protected Logger logger;

    /**
     * This constructor sets all the values of the instance variables.
     *
     * @param databaseTypeComboBox the combo box
     * @param driverPathTextField the driver path input
     * @param databaseNameTextField the database name input
     * @param portTextField the port number input
     * @param usernameField the username input
     * @param passwordField the password input
     * @param resultLabel the result label
     */
    public SetupListener(JComboBox<String> databaseTypeComboBox, JTextField driverPathTextField,
            JTextField databaseNameTextField, JTextField portTextField, JTextField usernameField,
            JPasswordField passwordField,
            JLabel resultLabel) {
        this.databaseTypeComboBox = databaseTypeComboBox;
        this.driverPathTextField = driverPathTextField;
        this.databaseNameTextField = databaseNameTextField;
        this.portTextField = portTextField;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.resultLabel = resultLabel;
        logger = LogManager.getLogger(SetupListener.class);
    }

    /**
     * This method coordinates the functions to handle events from the user
     * interface.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();
        String databaseType;
        String driverPath;
        String host;
        String databaseName;
        String port;
        String username;
        String password;
        ConnectionProperties properties;

        switch (action) {
            case TEST_COMMAND:
                resultLabel.setText("Testing connection...");
                resultLabel.repaint();

                host = "127.0.0.1";
                databaseType = (String) databaseTypeComboBox.getSelectedItem();
                driverPath = driverPathTextField.getText();
                databaseName = databaseNameTextField.getText();
                port = portTextField.getText();
                username = usernameField.getText();
                password = new String(passwordField.getPassword());

                try {
                    properties = new ConnectionProperties(databaseType, driverPath,
                            host, databaseName, port, username, password);
                    new ConnectionTestWorker(properties, resultLabel).execute();
                } catch (NullPointerException | IllegalArgumentException ex1) {
                    resultLabel.setText("You must provide all connection information!");
                    resultLabel.repaint();
                }
                break;
            case CONNECT_COMMAND:
                host = "127.0.0.1";
                databaseType = (String) databaseTypeComboBox.getSelectedItem();
                driverPath = driverPathTextField.getText();
                databaseName = databaseNameTextField.getText();
                port = portTextField.getText();
                username = usernameField.getText();
                password = new String(passwordField.getPassword());

                try {
                    properties = new ConnectionProperties(databaseType, driverPath,
                            host, databaseName, port, username, password);
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    Future<Boolean> future
                            = executor.submit(new TestQueryRunner(properties));
                    if (future.get()) {
                        resultLabel.setText("Connection good.");
                        resultLabel.repaint();
                        WindowAssemblyManufacturer factory = WindowAssemblyManufacturer.getInstance();
                        WindowAssembler assembler = factory.employWindowAssemblerForQueries(properties);
                        QueryWindow queryWindow = (QueryWindow) assembler.assembleWindow();
                        queryWindow.setLocationRelativeTo(null);
                        queryWindow.setAlwaysOnTop(true);
                        queryWindow.setVisible(true);
                        SwingUtilities.windowForComponent(resultLabel).dispose();
                    } else {
                        resultLabel.setText("Connection failed!");
                        resultLabel.repaint();
                    }
                } catch (InterruptedException | ExecutionException ex2) {
                    logger.error("InterruptedException or ExecutionException occurred in actionPerformed method.", 
                            ex2);
                } catch (NullPointerException | IllegalArgumentException ex3) {
                    logger.error("NullPointerException or IllegalArgumentException occurred in actionPerformed method.", 
                            ex3);
                    resultLabel.setText("You must provide all connection information!");
                    resultLabel.repaint();
                }
                break;
            case BROWSE_COMMAND:
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    driverPathTextField.setText(filePath);
                }
                break;
            case EXIT_COMMAND:
                System.exit(0);
                break;
            default:
                break;
        }
    }

}
