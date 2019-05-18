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

import com.daniel.sqlbuddy.model.ConnectionProperties;

/**
 * This utility class provides functionality for creating database queries.
 *
 * @author Bryan Daniel
 */
public class QueryUtil {

    /**
     * The test query selecting the number one from dual
     */
    public static final String SELECT_ONE_FROM_DUAL = "SELECT 1 FROM dual";

    /**
     * The test query selecting the number one
     */
    public static final String SELECT_ONE = "SELECT 1";

    /**
     * Private constructor - not called
     */
    private QueryUtil() {
    }

    /**
     * This method returns a specific connection test query for the database
     * type to test.
     *
     * @param properties the connection properties
     * @return the test query
     */
    public static String getConnectionTestQuery(ConnectionProperties properties) {

        String query;
        switch (properties.getProperty(ConnectionProperties.DATABASE_TYPE)) {
            case ConnectionProperties.MYSQL:
                query = SELECT_ONE_FROM_DUAL;
                break;
            case ConnectionProperties.ORACLE_THIN:
                query = SELECT_ONE_FROM_DUAL;
                break;
            case ConnectionProperties.ORACLE_OCI:
                query = SELECT_ONE_FROM_DUAL;
                break;
            case ConnectionProperties.MS_SQL:
                query = SELECT_ONE;
                break;
            default:
                query = SELECT_ONE;
                break;
        }
        return query;
    }
}
