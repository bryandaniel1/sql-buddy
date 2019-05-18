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
 * This class defines a visual theme for the user interface of the application.
 *
 * @author Bryan Daniel
 */
public class Theme {

    /**
     * The red value
     */
    private int redValue;

    /**
     * The green value
     */
    private int greenValue;

    /**
     * The blue value
     */
    private int blueValue;

    /**
     * The background color
     */
    private Color backgroundColor;

    /**
     * The foreground color
     */
    private Color foregroundColor;

    /**
     * The color for disabled text
     */
    private Color disabledTextColor;

    /**
     * The caret color
     */
    private Color caretColor;

    /**
     * Get the value of redValue
     *
     * @return the value of redValue
     */
    public int getRedValue() {
        return redValue;
    }

    /**
     * Set the value of redValue
     *
     * @param redValue new value of redValue
     */
    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    /**
     * Get the value of greenValue
     *
     * @return the value of greenValue
     */
    public int getGreenValue() {
        return greenValue;
    }

    /**
     * Set the value of greenValue
     *
     * @param greenValue new value of greenValue
     */
    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    /**
     * Get the value of blueValue
     *
     * @return the value of blueValue
     */
    public int getBlueValue() {
        return blueValue;
    }

    /**
     * Set the value of blueValue
     *
     * @param blueValue new value of blueValue
     */
    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    /**
     * Get the value of backgroundColor
     *
     * @return the value of backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set the value of backgroundColor
     *
     * @param backgroundColor new value of backgroundColor
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Get the value of foregroundColor
     *
     * @return the value of foregroundColor
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Set the value of foregroundColor
     *
     * @param foregroundColor new value of foregroundColor
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Get the value of disabledTextColor
     *
     * @return the value of disabledTextColor
     */
    public Color getDisabledTextColor() {
        return disabledTextColor;
    }

    /**
     * Set the value of disabledTextColor
     *
     * @param disabledTextColor new value of disabledTextColor
     */
    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
    }

    /**
     * Get the value of caretColor
     *
     * @return the value of caretColor
     */
    public Color getCaretColor() {
        return caretColor;
    }

    /**
     * Set the value of caretColor
     *
     * @param caretColor new value of caretColor
     */
    public void setCaretColor(Color caretColor) {
        this.caretColor = caretColor;
    }
}
