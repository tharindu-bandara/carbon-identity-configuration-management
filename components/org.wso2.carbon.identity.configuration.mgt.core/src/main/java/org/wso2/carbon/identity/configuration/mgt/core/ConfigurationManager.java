/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.configuration.mgt.core;

import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.configuration.mgt.core.model.ConfigurationChangeResponse;
import org.wso2.carbon.identity.configuration.mgt.core.util.model.ConfigurationSearchFilter;

import java.util.List;
import java.util.Map;

/**
 * Resource manager service interface.
 */
public interface ConfigurationManager {

    /**
     * This API is used to get the configuration by configuration name
     *
     * @param name Name id of the configuration.
     * @param searchFilter Filter to search resources.
     * @return 200 OK with configuration element.
     * @throws Resource Management Exception.
     */
    Resource getResource(String name, ConfigurationSearchFilter configurationSearchFilter)
            throws ConfigurationManagementException;

    /**
     * This API is used to delete the configuration by configuration name
     *
     * @param name Name  id of the configuration.
     * @throws ConfigurationManagementException Resource management exception.
     */
    void deleteResource(String name) throws ConfigurationManagementException;

    /**
     * This API is used to add the given resource.
     *
     * @param name          Name id of the existing {@link Resource}.
     * @param resource {@link Resource} to be added.
     * @return 201 created. Returns resource change response with resource name, tenant domain and change state.
     * @throws ConfigurationManagementException Resource management exception.
     */
    void addResource(String name, Resource resource)
            throws ConfigurationManagementException;

    /**
     * This API is used to replace the existing resource with the given resource or add the given
     * resource if an existing resource is not available.
     *
     * @param name          Name id of the existing {@link Resource}.
     * @param resource New {@link Resource} to replace the existing {@link Resource}.
     * @return 201 created. Returns resource change response with resource name, tenant domain and change state.
     * @throws ConfigurationManagementException Resource management exception.
     */
    void replaceResource(String name, Resource resource)
            throws ConfigurationManagementException;

    /**
     * This API is used to update the existing resource with the given resource.
     *
     * @param name          Name id of the existing {@link Resource}.
     * @param resource New {@link Resource} to update the existing {@link Resource}.
     * @return 201 created. Returns resource change response with resource name, tenant domain and change state.
     * @throws ConfigurationManagementException Resource management exception.
     */
    void updateResource(String name, Resource resource)
            throws ConfigurationManagementException;
}
