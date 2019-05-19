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

import com.daniel.sqlbuddy.ui.theme.Theme;
import static com.daniel.sqlbuddy.util.TextUtil.MULTILINE_COMMENT_END_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.MULTILINE_COMMENT_START_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.NEWLINE_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.SINGLE_LINE_COMMENT_CHARACTERS;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * This key listener finds and styles the comments typed into the text pane.
 *
 * @author Bryan Daniel
 */
public class QueryTextAreaKeyListener implements KeyListener {

    /**
     * The regular expression to match text containing a multi-line comment
     */
    public static final String MULTILINE_COMMENT_PATTERN = "/\\*(.|\\n)*\\*\\/(.|\\n)*";

    /**
     * The text pane containing query input
     */
    private final JTextPane queryTextPane;

    /**
     * The theme for the user interface
     */
    private final Theme theme;

    /**
     * The constructor sets the value for the text pane.
     *
     * @param queryTextPane the text pane
     * @param theme the theme
     */
    public QueryTextAreaKeyListener(JTextPane queryTextPane, Theme theme) {
        this.queryTextPane = queryTextPane;
        this.theme = theme;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int textLength = queryTextPane.getDocument().getLength();
        int originalCaretPosition = queryTextPane.getCaretPosition();

        if (shouldInspectTextForComments(e)) {
            String remainingText = queryTextPane.getText();
            queryTextPane.setText("");
            findComments(remainingText);
        }

        if (originalCaretPosition != textLength) {
            queryTextPane.setCaretPosition(originalCaretPosition);
        }
    }

    /**
     * This method determines whether the text in the pane should be inspected
     * for comments based on the given key event.
     *
     * @param e the key event
     * @return true if the text should be inspected, false otherwise
     */
    private boolean shouldInspectTextForComments(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_V
                || (!e.isControlDown() && e.getKeyCode() != KeyEvent.VK_CONTROL);
    }

    /**
     * This method finds the multi-line and single-line comments in the given
     * text string and calls the appendToPane method to style the text
     * appropriately.
     *
     * @param remainingText the text to process
     */
    private void findComments(String remainingText) {
        while (remainingText.length() > 0) {
            if (shouldEvaluateMultilineComment(remainingText)) {
                findComments(remainingText.substring(0, remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS)));
                remainingText = remainingText.substring(remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS));

                if (Pattern.compile(MULTILINE_COMMENT_PATTERN).matcher(remainingText).matches()) {
                    appendToPane(remainingText.substring(0, remainingText.indexOf(MULTILINE_COMMENT_END_CHARACTERS,
                            remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS) + MULTILINE_COMMENT_START_CHARACTERS.length())
                            + MULTILINE_COMMENT_END_CHARACTERS.length()), true);
                    if (remainingText.length() > remainingText.indexOf(MULTILINE_COMMENT_END_CHARACTERS,
                            remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS)
                            + MULTILINE_COMMENT_START_CHARACTERS.length()) + MULTILINE_COMMENT_END_CHARACTERS.length()) {
                        remainingText = remainingText.substring(remainingText.indexOf(MULTILINE_COMMENT_END_CHARACTERS,
                                remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS)
                                + MULTILINE_COMMENT_START_CHARACTERS.length()) + MULTILINE_COMMENT_END_CHARACTERS.length());
                    } else {
                        remainingText = "";
                    }
                } else {
                    appendToPane(remainingText, true);
                    remainingText = "";
                }
            } else if (remainingText.contains(SINGLE_LINE_COMMENT_CHARACTERS)) {
                appendToPane(remainingText.substring(0, remainingText.indexOf(SINGLE_LINE_COMMENT_CHARACTERS)), false);
                remainingText = remainingText.substring(remainingText.indexOf(SINGLE_LINE_COMMENT_CHARACTERS));
                if (remainingText.contains(NEWLINE_CHARACTERS)) {
                    appendToPane(remainingText.substring(0, remainingText.indexOf(NEWLINE_CHARACTERS)), true);
                    remainingText = remainingText.substring(remainingText.indexOf(NEWLINE_CHARACTERS));
                } else {
                    appendToPane(remainingText, true);
                    remainingText = "";
                }
            } else {
                appendToPane(remainingText, false);
                remainingText = "";
            }
        }
    }

    /**
     * This method appends the line to the text area, setting the foreground
     * color to indicate a comment has been typed.
     *
     * @param line the text to append
     * @param comment indicates whether or not this text is a comment
     */
    private void appendToPane(String line, boolean comment) {

        Color textColor;
        if (comment) {
            textColor = theme.getDisabledTextColor();
        } else {
            textColor = theme.getForegroundColor();
        }
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, textColor);

        int length = queryTextPane.getDocument().getLength();
        if (length == queryTextPane.getCaretPosition()) {
            queryTextPane.setCharacterAttributes(attributeSet, false);
            queryTextPane.replaceSelection(line);
        } else {
            queryTextPane.setCaretPosition(length);
            queryTextPane.setCharacterAttributes(attributeSet, false);
            queryTextPane.replaceSelection(line);
        }
    }

    /**
     * Determines if the given text should be evaluated for a multi-line
     * comment. This will be true if a multiple-line comment has at least been
     * started and any possible single-line comment occurs after the
     * multiple-line comment.
     *
     * @param remainingText the text to evaluate
     * @return true if a multiple-line comment should be evaluated, false
     * otherwise
     */
    private boolean shouldEvaluateMultilineComment(String remainingText) {
        return (remainingText.contains(MULTILINE_COMMENT_START_CHARACTERS)
                && !remainingText.contains(SINGLE_LINE_COMMENT_CHARACTERS))
                || (remainingText.contains(MULTILINE_COMMENT_START_CHARACTERS)
                && remainingText.contains(SINGLE_LINE_COMMENT_CHARACTERS)
                && Integer.valueOf(remainingText.indexOf(MULTILINE_COMMENT_START_CHARACTERS))
                        .compareTo(remainingText.indexOf(SINGLE_LINE_COMMENT_CHARACTERS)) < 0);
    }
}
