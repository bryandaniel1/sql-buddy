/*
 * Copyright 2019 Bryan Daniel.
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

import com.daniel.sqlbuddy.logic.FileWorker;
import com.daniel.sqlbuddy.model.SQLFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles action events fired from the file menu of the query window. The
 * functions provided include opening and saving files.
 *
 * @author Bryan Daniel
 */
public class FileListener implements ActionListener {

    /**
     * The command for opening a file
     */
    public static final String OPEN_COMMAND = "Open";

    /**
     * The command for saving a file
     */
    public static final String SAVE_AS_COMMAND = "Save As...";

    /**
     * The input area for queries
     */
    private final JTextPane queryTextPane;

    /**
     * The logger for this class
     */
    private Logger logger;

    /**
     * This constructor sets the values for the text pane and logger.
     *
     * @param queryTextPane the text pane
     */
    public FileListener(JTextPane queryTextPane) {
        this.queryTextPane = queryTextPane;
        logger = LogManager.getLogger(FileListener.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new SQLFileFilter());
        switch (command) {
            case OPEN_COMMAND:
                int openState = fileChooser.showOpenDialog(SwingUtilities.windowForComponent(queryTextPane));
                if (openState == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    logger.info("Opening: " + file.getName() + ".");
                    new FileWorker(queryTextPane, OPEN_COMMAND, file).execute();
                }
                break;
            case SAVE_AS_COMMAND:
                int saveState = fileChooser.showSaveDialog(SwingUtilities.windowForComponent(queryTextPane));
                if (saveState == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (file == null) {
                        return;
                    }
                    String addedExtension = "";
                    if (!file.getName().contains(".")) {
                        addedExtension = ".sql";
                    }
                    file = new File(file.getParentFile(), file.getName() + addedExtension);
                    logger.info("Saving: " + file.getName() + ".");
                    new FileWorker(queryTextPane, SAVE_AS_COMMAND, file).execute();
                }
                break;
            default:
                break;
        }
    }

}
