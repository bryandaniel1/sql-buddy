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
package com.daniel.sqlbuddy.util;

/**
 * This class holds utility data for text manipulation functions.
 *
 * @author Bryan Daniel
 */
public class TextUtil {

    /**
     * The characters starting a multiple-line comment
     */
    public static final String MULTILINE_COMMENT_START_CHARACTERS = "/*";

    /**
     * The characters ending a multiple-line comment
     */
    public static final String MULTILINE_COMMENT_END_CHARACTERS = "*/";

    /**
     * The characters starting a single-line comment
     */
    public static final String SINGLE_LINE_COMMENT_CHARACTERS = "--";

    /**
     * The characters designating a new line
     */
    public static final String NEWLINE_CHARACTERS = "\n";
}
