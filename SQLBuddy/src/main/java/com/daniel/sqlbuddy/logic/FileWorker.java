/*
 * Copyright 2019 bryan.
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
package com.daniel.sqlbuddy.logic;

import com.daniel.sqlbuddy.controller.FileListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This SwingWorker performs file operations determined by commands from the
 * query window file menu.
 *
 * @author Bryan Daniel
 */
public class FileWorker extends SwingWorker<Void, Object> {

    /**
     * The input area for queries
     */
    private final JTextPane queryTextPane;

    /**
     * The file operation to perform
     */
    private final String command;

    /**
     * The file object to operate on
     */
    private final File file;

    /**
     * The file contents stored from an open file operation
     */
    private StringBuilder fileContents;

    /**
     * The logger for this class
     */
    private Logger logger;

    /**
     * Sets the values for the instance variables.
     *
     * @param queryTextPane the text pane
     * @param command the file command
     * @param file the file
     */
    public FileWorker(JTextPane queryTextPane, String command, File file) {
        this.queryTextPane = queryTextPane;
        this.command = command;
        this.file = file;
        logger = LogManager.getLogger(FileWorker.class);
    }

    /**
     * This method gets any file content produced from an open file operation
     * and displays it in the text pane. This is executed on the event dispatch
     * thread.
     */
    @Override
    protected void done() {
        if (FileListener.OPEN_COMMAND.equals(command)) {
            queryTextPane.setText(fileContents.toString());
        }
    }

    @Override
    protected Void doInBackground() throws Exception {

        switch (command) {
            case FileListener.OPEN_COMMAND:
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    fileContents = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        fileContents.append(line);
                    }
                } catch (IOException e) {
                    logger.error("Exception occurred while opening file contents.", e);
                }
                break;
            case FileListener.SAVE_AS_COMMAND:
                try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
                        "UTF-8")) {
                    queryTextPane.write(outputStreamWriter);
                } catch (IOException e) {
                    logger.error("Exception occurred while saving file contents.", e);
                }
                break;
            default:
                logger.error("No valid command given for file operation.");
                break;
        }
        return null;
    }
}
