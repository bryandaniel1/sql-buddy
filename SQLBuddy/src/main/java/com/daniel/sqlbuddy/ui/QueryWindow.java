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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JFrame;

/**
 * This class is the JFrame for database interaction.
 *
 * @author Bryan Daniel
 */
public class QueryWindow extends JFrame {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -379355160410888700L;
    
    /**
     * The screen size of the monitor
     */
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
    /**
     * The number used for quotient scale and multiplication in defining subsequent constants
     */
    private static final int TWO = 2;
    
    /**
     * The window width
     */
    public static final BigDecimal QUERY_WINDOW_WIDTH = new BigDecimal(SCREEN_SIZE.getWidth());
    
    /**
     * The window height
     */
    public static final BigDecimal QUERY_WINDOW_HEIGHT = new BigDecimal(SCREEN_SIZE.getHeight());
    
    /**
     * The width for the pillars
     */
    public static final BigDecimal PILLAR_WIDTH = QUERY_WINDOW_WIDTH.divide(new BigDecimal(14), 
            TWO, RoundingMode.HALF_UP);
    
    /**
     * The width for the center panel
     */
    public static final BigDecimal CENTER_PANEL_WIDTH = QUERY_WINDOW_WIDTH.subtract(
            PILLAR_WIDTH.multiply(new BigDecimal(TWO)));
    
    /**
     * The window greeting
     */
    public static final String WELCOME = "The simple query editor";
    
    /**
     * The window title
     */
    public static final String GUI_TITLE_STRING = "SQL Buddy";
    
    /**
     * The results title
     */
    public static final String RESULTS_TITLE = "Results";
    
    /**
     * The queries title
     */
    public static final String QUERIES_TITLE = "Enter Query";
    
    /**
     * The buttons title
     */
    public static final String BUTTONS_BORDER_TITLE = "Operations";
    
    /**
     * The file menu title
     */
    public static final String FILE_MENU = "File";
    
    /**
     * The edit menu title
     */
    public static final String EDIT_MENU = "Edit";
    
    /**
     * The themes menu title
     */
    public static final String THEMES_MENU = "Themes";
    
    /**
     * The exit command
     */
    public static final String EXIT_COMMAND = "Exit";
    
    /**
     * The change-connection command
     */
    public static final String CHANGE_CONNECTION_COMMAND = "Change Connection";
    
    /**
     * The clear-results command
     */
    public static final String CLEAR_COMMAND = "Clear Results";
    
    /**
     * The run command
     */
    public static final String RUN_QUERY_COMMAND = "Run Query";

    /**
     * The constructor calls the super constructor.
     *
     * @param title the window title
     */
    public QueryWindow(String title) {
        super(title);
    }
}
