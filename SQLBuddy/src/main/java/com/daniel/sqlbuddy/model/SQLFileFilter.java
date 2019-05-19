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
package com.daniel.sqlbuddy.model;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Extends FileFilter to allow selection of only text or SQL files.
 *
 * @author Bryan Daniel
 */
public class SQLFileFilter extends FileFilter {

    /**
     * The extension for text files
     */
    public static final String TEXT_EXTENSION = "txt";

    /**
     * The extension for SQL files
     */
    public static final String SQL_EXTENSION = "sql";

    @Override
    public boolean accept(File file) {

        boolean isDirectory = false;
        String extension = null;
        if (!file.isDirectory()) {
            extension = getExtension(file);
        } else {
            isDirectory = true;
        }
        return isDirectory || TEXT_EXTENSION.equals(extension) || SQL_EXTENSION.equals(extension);
    }

    @Override
    public String getDescription() {
        return "Text or SQL files.";
    }

    /**
     * Returns the extension of the specified file.
     *
     * @param file the file to evaluate
     * @return the file extension
     */
    public String getExtension(File file) {
        String extension = null;
        String filename = file.getName();
        int index = filename.lastIndexOf('.');

        if (index > 0 && index < filename.length() - 1) {
            extension = filename.substring(index + 1).toLowerCase();
        }
        return extension;
    }
}
