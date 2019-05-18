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
package com.daniel.sqlbuddy.model;

import java.util.Properties;
import org.apache.commons.lang3.Validate;

/**
 * This properties class stores values for database connections.
 *
 * @author Bryan Daniel
 */
public class ConnectionProperties extends Properties {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8070837690193560185L;

    /**
     * The value for MySQL database type
     */
    public static final String MYSQL = "MySQL";
    
    /**
     * The value for Oracle database type using thin driver
     */
    public static final String ORACLE_THIN = "Oracle(thin driver)";
    
    /**
     * The value for Oracle database type using OCI driver
     */
    public static final String ORACLE_OCI = "Oracle(oci driver)";
    
    /**
     * The value for Microsoft SQL Server driver
     */
    public static final String MS_SQL = "Microsoft SQL Server";

    /**
     * The key for the database type
     */
    public static final String DATABASE_TYPE = "databaseType";

    /**
     * The key for the database URl protocol
     */
    public static final String PROTOCOL = "protocol";

    /**
     * The key for the driver class name
     */
    public static final String CLASS_NAME = "className";

    /**
     * The key for the location of the driver
     */
    public static final String DRIVER_PATH = "driverPath";

    /**
     * The key for the host
     */
    public static final String HOST = "host";

    /**
     * The key for the database name
     */
    public static final String DATABASE_NAME = "databaseName";

    /**
     * The key for the port
     */
    public static final String PORT = "port";

    /**
     * The key for the username
     */
    public static final String USERNAME = "username";

    /**
     * The key for the password
     */
    public static final String PASSWORD = "password";

    /**
     * This constructor sets the property values with the given parameters.
     *
     * @param databaseType the database type
     * @param driverPath the driver path
     * @param host the host
     * @param databaseName the database name
     * @param port the port
     * @param username the username
     * @param password the password
     */
    public ConnectionProperties(String databaseType, String driverPath, String host,
            String databaseName, String port, String username, String password) {
        validateAndSet(databaseType, driverPath, host, databaseName, port, username, password);
    }

    /**
     * This method validates property values before setting them.
     *
     * @param databaseType the database type
     * @param driverPath the driver path
     * @param host the host
     * @param databaseName the database name
     * @param port the port
     * @param username the username
     * @param password the password
     */
    private void validateAndSet(String databaseType, String driverPath, String host,
            String databaseName, String port, String username, String password) {

        Validate.notEmpty(databaseType, "database type must be provided");
        Validate.notEmpty(driverPath, "driver path must be provided");
        Validate.notEmpty(host, "host must be provided");
        Validate.notEmpty(databaseName, "databaseName must be provided");
        Validate.notEmpty(port, "port must be provided");
        Validate.notEmpty(username, "username must be provided");
        Validate.notEmpty(password, "password must be provided");

        String className = determineClassName(databaseType);
        String protocol = determineProtocol(databaseType);
        Validate.notEmpty(className, "database type is not valid");
        Validate.notEmpty(protocol, "database type is not valid");

        setProperty(ConnectionProperties.DATABASE_TYPE, databaseType);
        setProperty(ConnectionProperties.DRIVER_PATH, driverPath);
        setProperty(ConnectionProperties.CLASS_NAME, className);
        setProperty(ConnectionProperties.PROTOCOL, protocol);
        setProperty(ConnectionProperties.HOST, host);
        setProperty(ConnectionProperties.DATABASE_NAME, databaseName);
        setProperty(ConnectionProperties.PORT, port);
        setProperty(ConnectionProperties.USERNAME, username);
        setProperty(ConnectionProperties.PASSWORD, password);
    }

    /**
     * This method returns the appropriate driver class name for the given
     * database type.
     *
     * @param databaseType the database type
     * @return the class name for the database driver or null if an invalid
     * database type is provided
     */
    private String determineClassName(String databaseType) {

        String className = null;
        switch (databaseType) {
            case MYSQL:
                className = "com.mysql.jdbc.Driver";
                break;
            case ORACLE_THIN:
                className = "oracle.jdbc.driver.OracleDriver";
                break;
            case ORACLE_OCI:
                className = "oracle.jdbc.driver.OracleDriver";
                break;
            case MS_SQL:
                className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
            default:
                break;
        }
        return className;
    }

    /**
     * This method determines the value for the substring of the database URL
     * containing the protocol and sub-protocol.
     *
     * @param databaseType the database type
     * @return the database URL substring containing the protocol and
     * sub-protocol or null if an invalid database type is provided
     */
    private String determineProtocol(String databaseType) {

        String protocol = null;
        switch (databaseType) {
            case MYSQL:
                protocol = "jdbc:mysql://";
                break;
            case ORACLE_THIN:
                protocol = "jdbc:oracle:thin:@//";
                break;
            case ORACLE_OCI:
                protocol = "jdbc:oracle:oci:@";
                break;
            case MS_SQL:
                protocol = "jdbc:sqlserver://";
            default:
                break;
        }
        return protocol;
    }
}
