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

import com.daniel.sqlbuddy.util.DatabaseUtil;
import static com.daniel.sqlbuddy.util.TextUtil.MULTILINE_COMMENT_END_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.MULTILINE_COMMENT_START_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.NEWLINE_CHARACTERS;
import static com.daniel.sqlbuddy.util.TextUtil.SINGLE_LINE_COMMENT_CHARACTERS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides a template for finding and executing SQL statements.
 *
 * @author Bryan Daniel
 */
public class BaseDataAccessor {

    /**
     * The logger for this class
     */
    protected Logger logger;

    /**
     * The default constructor sets the value for the logger.
     */
    public BaseDataAccessor() {
        logger = LogManager.getLogger(BaseDataAccessor.class);
    }

    /**
     * This method collects the sections of the given query string not part of
     * multiple-line comments. The collected sections are passed to the
     * executeStatements method.
     *
     * @param connection the connection
     * @param queryString the query string
     * @param allResults the result list
     * @throws SQLException
     */
    protected void findExecutableStatements(Connection connection, String queryString, ArrayList<Object> allResults) throws SQLException {
        while (queryString.length() > 0) {
            if (queryString.contains(MULTILINE_COMMENT_START_CHARACTERS)) {
                if (queryString.contains(MULTILINE_COMMENT_END_CHARACTERS)
                        && queryString.indexOf(MULTILINE_COMMENT_END_CHARACTERS)
                        < queryString.length() - MULTILINE_COMMENT_END_CHARACTERS.length()) {
                    queryString = queryString.substring(0, queryString.indexOf(MULTILINE_COMMENT_START_CHARACTERS))
                            + queryString.substring(queryString.indexOf(MULTILINE_COMMENT_END_CHARACTERS)
                                    + MULTILINE_COMMENT_END_CHARACTERS.length());
                } else {
                    queryString = queryString.substring(0, queryString.indexOf(MULTILINE_COMMENT_START_CHARACTERS));
                }
            } else {
                executeStatements(connection, queryString, allResults);
                queryString = "";
            }
        }
    }

    /**
     * This method splits the given query string into multiple possible
     * statements and executes each statement using the executeStatement method.
     *
     * @param connection the database connection
     * @param queryString the query string
     * @param allResults the result list
     * @throws SQLException
     */
    protected void executeStatements(Connection connection, String queryString, ArrayList<Object> allResults) throws SQLException {

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(queryString.split(NEWLINE_CHARACTERS)));
        for (ListIterator<String> iterator = lines.listIterator(); iterator.hasNext();) {
            String line = iterator.next();
            if (line.startsWith(SINGLE_LINE_COMMENT_CHARACTERS)) {
                iterator.remove();
            } else if (line.contains("--")) {
                iterator.remove();
                iterator.add(line.substring(0, line.indexOf(SINGLE_LINE_COMMENT_CHARACTERS)));
            }
        }
        StringBuilder unCommentedLines = new StringBuilder();
        for (String line : lines) {
            if (unCommentedLines.length() > 0) {
                unCommentedLines.append(" ");
            }
            unCommentedLines.append(line);
        }
        ArrayList<String> statements = new ArrayList<>(Arrays.asList(unCommentedLines.toString().split(";")));

        for (String statement : statements) {
            executeStatement(connection, statement, allResults);
        }
    }

    /**
     * This method executes a single statement and adds results to the given
     * result list.
     *
     * @param connection the database connection
     * @param queryString the query string to execute
     * @param allResults the result list
     * @throws SQLException
     */
    protected void executeStatement(Connection connection, String queryString, ArrayList<Object> allResults) throws SQLException {

        ResultSet resultSet = null;
        Statement query = connection.createStatement();
        logger.info("Executing query: " + queryString);

        // true indicates the first result is a result set
        boolean moreResults = query.execute(queryString);
        int updateCount = query.getUpdateCount();
        while (updateCount != -1 || moreResults) {

            if (moreResults) {
                resultSet = query.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();

                // names of columns
                Vector<String> columnNames = new Vector<>();
                int columnCount = metaData.getColumnCount();
                for (int column = 1; column <= columnCount; column++) {
                    columnNames.add(metaData.getColumnName(column));
                }

                // data of the table
                Vector<Vector<Object>> data = new Vector<>();
                while (resultSet.next()) {
                    Vector<Object> vector = new Vector<Object>();
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        vector.add(resultSet.getObject(columnIndex));
                    }
                    data.add(vector);
                }
                allResults.add(new DefaultTableModel(data, columnNames));
            }
            if (updateCount != -1) {
                allResults.add("Total records updated: " + updateCount);
            }
            moreResults = query.getMoreResults();
            updateCount = query.getUpdateCount();
        }
        DatabaseUtil.closeResultSet(resultSet);
        DatabaseUtil.closeStatement(query);
    }
}
