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

/**
 * This class holds the data for a database connection profile.
 *
 * @author Bryan Daniel
 */
public class Profile {

    /**
     * The profile name
     */
    private String profileName;

    /**
     * The connection properties for the profile
     */
    private ConnectionProperties connectionProperties;

    /**
     * Get the value of profileName
     *
     * @return the value of profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Set the value of profileName
     *
     * @param profileName new value of profileName
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Get the value of connectionProperties
     *
     * @return the value of connectionProperties
     */
    public ConnectionProperties getConnectionProperties() {
        return connectionProperties;
    }

    /**
     * Set the value of connectionProperties
     *
     * @param connectionProperties new value of connectionProperties
     */
    public void setConnectionProperties(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}
