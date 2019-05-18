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
package com.daniel.sqlbuddy.data;

import com.daniel.sqlbuddy.model.ConnectionProperties;
import java.util.ArrayList;

/**
 * Provides the ability to execute database queries found within input text.
 *
 * @author Bryan Daniel
 */
public interface InputProcessor {

    /**
     * This method uses the input string and the connection properties to access
     * a database and retrieve results for the query or queries in the string.
     *
     * @param queryString the query string
     * @param properties the database connection properties
     * @return the list of results
     */
    public ArrayList<Object> executeInput(String queryString, ConnectionProperties properties);
}
