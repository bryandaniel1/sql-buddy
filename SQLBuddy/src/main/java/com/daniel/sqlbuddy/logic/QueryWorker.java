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
package com.daniel.sqlbuddy.logic;

import com.daniel.sqlbuddy.data.InputProcessor;
import com.daniel.sqlbuddy.data.InputProcessorFactory;
import com.daniel.sqlbuddy.model.ConnectionProperties;
import com.daniel.sqlbuddy.util.TableUtil;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This SwingWorker takes statements from query window input and uses the data
 * access class to retrieve results.
 *
 * @author Bryan Daniel
 */
public class QueryWorker extends SwingWorker<List<Object>, Object> {

    /**
     * The input to execute
     */
    private final String queryString;

    /**
     * The database connection properties
     */
    private final ConnectionProperties properties;

    /**
     * The tabbed pane holding results
     */
    private final JTabbedPane tabbedPane;
    
    /**
     * The logger for this class
     */
    private Logger logger;

    /**
     * This constructor sets the values for the query string, the connection
     * properties, and the results panel.
     *
     * @param queryString the query string
     * @param properties the connection properties
     * @param tabbedPane the tabbed pane
     */
    public QueryWorker(String queryString, ConnectionProperties properties,
            JTabbedPane tabbedPane) {
        this.queryString = queryString;
        this.properties = properties;
        this.tabbedPane = tabbedPane;
        logger = LogManager.getLogger(QueryWorker.class);
    }

    /**
     * This method gets the results produced by the doInBackground method and
     * displays the results in the query window. This is executed on the event
     * dispatch thread.
     */
    @Override
    protected void done() {
        
        tabbedPane.removeAll();
        
        try {
            List<Object> results = get();
            int i = 1;
            for (Object object : results) {
                if (object instanceof DefaultTableModel) {

                    // wrap a scrollpane around the table
                    JTable resultTable = new JTable((DefaultTableModel) object);
                    TableUtil.adjustTableColumnWidths(resultTable);
                    JScrollPane scrollPane = new JScrollPane(resultTable,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setAutoscrolls(true);
                    scrollPane.setPreferredSize(new Dimension(200, 200));
                    tabbedPane.addTab("Result " + i++, scrollPane);
                }
                if (object instanceof String) {
                    tabbedPane.addTab("Result " + i++, new JLabel((String) object));
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("Execption occurred in QueryWorker.", ex);
        }        
        tabbedPane.repaint();
    }

    /**
     * This method uses the data access class for executing any given query or
     * set of queries. This is not executed on the event dispatch thread.
     *
     * @return the results of the query execution
     * @throws Exception if an exception occurs
     */
    @Override
    protected List<Object> doInBackground() throws Exception {

        InputProcessorFactory factory = InputProcessorFactory.getInstance();
        InputProcessor processor = factory.createInputProcessorForType(properties
                .getProperty(ConnectionProperties.DATABASE_TYPE));
        return processor.executeInput(queryString, properties);
    }
}
