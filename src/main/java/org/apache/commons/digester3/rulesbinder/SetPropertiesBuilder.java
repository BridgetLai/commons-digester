/* $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.digester3.rulesbinder;

import org.apache.commons.digester3.rule.SetPropertiesRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setNestedProperties()}.
 */
public interface SetPropertiesBuilder extends BackToLinkedRuleBuilder<SetPropertiesRule> {

    /**
     * Add an attribute name to the ignore list.
     *
     * @param attributeName The attribute to match has to be ignored
     * @return this builder instance
     */
    SetPropertiesBuilder ignoreAttribute(String attributeName);

    /**
     * Add an additional attribute name to property name mapping.
     *
     * @param attributeName The attribute to match
     * @param propertyName The java bean property to be assigned the value
     * @return this builder instance
     */
    SetPropertiesBuilder addAlias(String attributeName, String propertyName);

    /**
     * Sets whether attributes found in the XML without matching properties should be ignored.
     * 
     * If set to false, the parsing will throw an {@code NoSuchMethodException}
     * if an unmatched attribute is found.
     * This allows to trap misspellings in the XML file.
     *
     * @param ignoreMissingProperty false to stop the parsing on unmatched attributes
     * @return this builder instance
     */
    SetPropertiesBuilder ignoreMissingProperty(boolean ignoreMissingProperty);

}
