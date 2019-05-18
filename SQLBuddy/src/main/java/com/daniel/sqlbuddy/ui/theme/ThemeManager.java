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
package com.daniel.sqlbuddy.ui.theme;

import java.awt.Color;

/**
 * This class manages the styling theme of the user interface.
 *
 * @author Bryan Daniel
 */
public class ThemeManager {

    /**
     * This enum indicates the selected theme
     */
    public enum ThemeSelection {

        METALLIC("Metallic"),
        UNDERWATER("Underwater"),
        SUNNY("Sunny");

        /**
         * The name of the theme
         */
        private String name;

        /**
         * Sets the value of name.
         *
         * @param name the name
         */
        ThemeSelection(String name) {
            this.name = name;
        }

        /**
         * Gets the value of name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Defines the intensity of the underwater red color
     */
    private static final int UNDERWATER_RED_VALUE = 73;

    /**
     * Defines the intensity of the underwater green color
     */
    private static final int UNDERWATER_GREEN_VALUE = 172;

    /**
     * Defines the intensity of the underwater blue color
     */
    private static final int UNDERWATER_BLUE_VALUE = 221;

    /**
     * Defines the intensity of the underwater red color
     */
    private static final int SUNNY_RED_VALUE = 253;

    /**
     * Defines the intensity of the underwater green color
     */
    private static final int SUNNY_GREEN_VALUE = 244;

    /**
     * Defines the intensity of the underwater blue color
     */
    private static final int SUNNY_BLUE_VALUE = 225;

    /**
     * Defines the intensity of the underwater red color
     */
    private static final int METALLIC_RED_VALUE = 231;

    /**
     * Defines the intensity of the underwater green color
     */
    private static final int METALLIC_GREEN_VALUE = 235;

    /**
     * Defines the intensity of the underwater blue color
     */
    private static final int METALLIC_BLUE_VALUE = 236;

    /**
     * The theme for the user interface
     */
    private Theme theme;

    /**
     * This method creates the theme for the user interface.
     *
     * @param themeSelection the selected theme
     * @return the theme
     */
    public Theme createTheme(ThemeSelection themeSelection) {

        theme = new Theme();
        switch (themeSelection) {
            case METALLIC:
                applyMetallicTheme();
                break;
            case UNDERWATER:
                applyUnderwaterTheme();
                break;
            case SUNNY:
                applySunnyTheme();
                break;
            default:
                break;
        }
        return theme;
    }

    /**
     * This method sets the values for the underwater theme
     */
    private void applyUnderwaterTheme() {
        theme.setRedValue(UNDERWATER_RED_VALUE);
        theme.setGreenValue(UNDERWATER_GREEN_VALUE);
        theme.setBlueValue(UNDERWATER_BLUE_VALUE);
        theme.setBackgroundColor(Color.BLACK);
        theme.setForegroundColor(Color.YELLOW);
        theme.setDisabledTextColor(Color.LIGHT_GRAY);
        theme.setCaretColor(Color.YELLOW);
    }

    /**
     * This method sets the values for the sunny theme
     */
    private void applySunnyTheme() {
        theme.setRedValue(SUNNY_RED_VALUE);
        theme.setGreenValue(SUNNY_GREEN_VALUE);
        theme.setBlueValue(SUNNY_BLUE_VALUE);
        theme.setBackgroundColor(Color.WHITE);
        theme.setForegroundColor(Color.BLACK);
        theme.setDisabledTextColor(Color.BLUE.brighter());
        theme.setCaretColor(Color.ORANGE.darker());
    }

    /**
     * This method sets the values for the metallic theme
     */
    private void applyMetallicTheme() {
        theme.setRedValue(METALLIC_RED_VALUE);
        theme.setGreenValue(METALLIC_GREEN_VALUE);
        theme.setBlueValue(METALLIC_BLUE_VALUE);
        theme.setBackgroundColor(Color.WHITE);
        theme.setForegroundColor(Color.BLACK);
        theme.setDisabledTextColor(Color.LIGHT_GRAY);
        theme.setCaretColor(Color.BLACK);
    }

    /**
     * Get the value of theme
     *
     * @return the value of theme
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Set the value of theme
     *
     * @param theme new value of theme
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
