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

/**
 * This class creates input processors to access different database types. The
 * class implements the Singleton design pattern, as it is not necessary to have
 * more than one InputProcessorFactory.
 *
 * @author Bryan Daniel
 */
public class InputProcessorFactory {

    /**
     * The single InputProcessorFactory object
     */
    private static InputProcessorFactory factory = null;

    /**
     * Private constructor - not called publicly
     */
    private InputProcessorFactory() {
    }

    /**
     * This method returns the single InputProcessorFactory. If the instance of
     * InputProcessorFactory has not been created previously, it is created
     * here.
     *
     * @return the InputProcessorFactory object
     */
    public synchronized static InputProcessorFactory getInstance() {
        if (factory == null) {
            factory = new InputProcessorFactory();
        }
        return factory;
    }

    /**
     * This method determines which type of processor to create based on the
     * database type indication given.
     *
     * @param type the database type
     * @return the appropriate input processor
     */
    public InputProcessor createInputProcessorForType(String type) {

        InputProcessor processor;
        switch (type) {
            case ConnectionProperties.MYSQL:
                processor = createMySQLInputProcessor();
                break;
            case ConnectionProperties.ORACLE_THIN:
                processor = createCommonInputProcessor();
                break;
            case ConnectionProperties.ORACLE_OCI:
                processor = createCommonInputProcessor();
                break;
            case ConnectionProperties.MS_SQL:
                processor = createCommonInputProcessor();
                break;
            default:
                processor = createCommonInputProcessor();
                break;
        }
        return processor;
    }

    /**
     * This method returns an input processor to handle data access activities
     * for any database type.
     *
     * @return the common input processor
     */
    private InputProcessor createCommonInputProcessor() {
        return new CommonDataAccessor();
    }

    /**
     * This method returns an input processor to handle data access activities
     * for MySQL databases.
     *
     * @return the input processor for MySQL
     */
    private InputProcessor createMySQLInputProcessor() {
        return new MySQLDataAccessor();
    }
}
