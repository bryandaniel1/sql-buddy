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
import com.daniel.sqlbuddy.util.QueryUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;

/**
 * This class uses the database connection properties to attempt a test query
 * for connection validation.
 *
 * @author Bryan Daniel
 */
public class TestQueryDataAccess {

    /**
     * The database connection properties
     */
    private ConnectionProperties properties;

    /**
     * The logger for this class
     */
    protected org.apache.logging.log4j.Logger logger;

    /**
     * This constructor sets the connection properties for the test query.
     *
     * @param properties the connection properties
     */
    public TestQueryDataAccess(ConnectionProperties properties) {
        this.properties = properties;
        logger = LogManager.getLogger(TestQueryDataAccess.class);
    }

    /**
     * This method executes the test query and returns a boolean value
     * indicating a pass or fail result.
     *
     * @return the indication of a passing or failing test
     */
    public Boolean connectionSuccessful() {

        Connection connection = null;
        Statement query = null;
        ResultSet resultSet = null;
        Boolean testPasses = false;
        String testQuery = QueryUtil.getConnectionTestQuery(properties);

        StringBuilder connectionString = DatabaseUtil.createDatabaseUrl(properties);
        try {
            String urlString = MessageFormat.format("jar:file:{0}!/",
                    properties.getProperty(ConnectionProperties.DRIVER_PATH));
            URL url = new URL(urlString);
            //String classname = "org.postgresql.Driver";
            String classname = properties.getProperty(ConnectionProperties.CLASS_NAME);
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
            Driver d = (Driver) Class.forName(classname, true, urlClassLoader).newInstance();
            DriverManager.registerDriver(new DriverDelegate(d));
            connection = DriverManager.getConnection(connectionString.toString(),
                    properties.getProperty(ConnectionProperties.USERNAME),
                    properties.getProperty(ConnectionProperties.PASSWORD));
            query = connection.createStatement();
            resultSet = query.executeQuery(testQuery);
            resultSet.next();
            int result = resultSet.getInt(1);
            if (result == 1) {
                testPasses = true;
            }
        } catch (SQLException ex) {
            logger.error("SQLException occurred during TestQueryDataAccess.connectionSuccessful.", ex);
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            logger.error("Exception occurred during TestQueryDataAccess.connectionSuccessful.", ex);
        } finally {
            DatabaseUtil.closeResultSet(resultSet);
            DatabaseUtil.closeStatement(query);
            DatabaseUtil.closeConnection(connection);
        }
        return testPasses;
    }
}
