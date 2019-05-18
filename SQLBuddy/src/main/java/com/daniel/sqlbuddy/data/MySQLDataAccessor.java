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
import com.daniel.sqlbuddy.model.DriverDelegate;
import com.daniel.sqlbuddy.util.DatabaseUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * This data-access class provides MySQL-specific functionality for taking any
 * input text and executing the statements in the text using provided connection
 * properties.
 *
 * @author Bryan Daniel
 */
public class MySQLDataAccessor extends BaseDataAccessor implements InputProcessor {

    /**
     * This method uses the input string and the connection properties to access
     * a database and retrieve results for the query or queries in the string.
     *
     * @param queryString the query string
     * @param properties the database connection properties
     * @return the list of results
     */
    @Override
    public ArrayList<Object> executeInput(String queryString, ConnectionProperties properties) {
        
        logger.info("Received input for MySQL database.");
        Connection connection = null;
        ArrayList<Object> allResults = new ArrayList<>();
        StringBuilder connectionString = DatabaseUtil.createDatabaseUrl(properties);
        connectionString.append("?allowMultiQueries=true");
        
        try {
            String urlString = MessageFormat.format("jar:file:{0}!/",
                    properties.getProperty(ConnectionProperties.DRIVER_PATH));
            URL url = new URL(urlString);
            String classname = properties.getProperty(ConnectionProperties.CLASS_NAME);
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
            Driver driver = (Driver) Class.forName(classname, true, urlClassLoader).newInstance();
            DriverManager.registerDriver(new DriverDelegate(driver));
            connection = DriverManager.getConnection(connectionString.toString(),
                    properties.getProperty(ConnectionProperties.USERNAME),
                    properties.getProperty(ConnectionProperties.PASSWORD));
            executeStatement(connection, queryString, allResults);
        } catch (SQLException e) {
            logger.error("SQLException occurred during MySQLDataAccessor.executeInput.", e);
            allResults.add(e.getMessage());
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error("Exception occurred during MySQLDataAccessor.executeInput.", ex);
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
        return allResults;
    }
}
