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

import org.apache.commons.digester3.spi.ObjectCreationFactory;

/**
 * Builder invoked to bind one or more rules to a pattern.
 */
public interface LinkedRuleBuilder {

    /**
     * Sets the namespace URI for the current rule pattern.
     *
     * @param namespaceURI the namespace URI associated to the rule pattern.
     * @return this {@link LinkedRuleBuilder} instance
     */
    LinkedRuleBuilder withNamespaceURI(String namespaceURI);

    /**
     * Construct rule that automatically sets a property from the body text, taking the property
     * name the same as the current element.
     */
    LinkedBeanPropertySetterBuilder setBeanProperty();

    /**
     * Calls a method on an object on the stack (normally the top/parent object), passing arguments collected from
     * subsequent {@link #callParam(int)} rule or from the body of this element.
     *
     * @param methodName Method name of the parent object to call
     */
    LinkedCallMethodBuilder callMethod(String methodName);

    /**
     * Saves a parameter for use by a surrounding {@link #callMethod(String)}.
     */
    LinkedCallParamBuilder callParam();

    /**
     * Construct a "call parameter" rule that will save the body text of this element as the parameter value.
     */
    LinkedPathCallParamBuilder callParamPath();

    /**
     * Uses an {@link ObjectCreationFactory} to create a new object which it pushes onto the object stack.
     *
     * When the element is complete, the object will be popped.
     */
    LinkedFactoryCreateBuilder factoryCreate();

    /**
     * Construct an object with the specified class name.
     *
     * @param className Java class name of the object to be created
     */
    LinkedAttributeOverrideBuilder objectCreate(String className);

    /**
     * Construct an object with the specified class.
     *
     * @param clazz Java class of the object to be created.
     */
    LinkedAttributeOverrideBuilder objectCreate(Class<?> clazz);

    /**
     * Saves a parameter for use by a surrounding {@link #callMethod(String)}.
     *
     * @param <T> The parameter type to pass along
     * @param paramObj The parameter to pass along
     */
    <T> LinkedObjectParamBuilder objectParam(T paramObj);

    /**
     * Sets properties on the object at the top of the stack,
     * based on child elements with names matching properties on that  object.
     */
    LinkedNestedPropertiesBuilder setNestedProperties();

    /**
     * Calls a method on the (top-1) (parent) object, passing the top object (child) as an argument,
     * commonly used to establish parent-child relationships.
     *
     * @param methodName Method name of the parent method to call
     */
    LinkedParamTypeBuilder setNext(String methodName);

    /**
     * Sets properties on the object at the top of the stack, based on attributes with corresponding names.
     */
    LinkedSetPropertiesBuilder setProperties();

    /**
     * Sets an individual property on the object at the top of the stack, based on attributes with specified names.
     *
     * @param name Name of the attribute that will contain the name of the property to be set
     * @param value Name of the attribute that will contain the value to which the property should be set
     */
    BackToLinkedRuleBuilder setProperty(String name, String value);

    /**
     * Calls a method on the root object on the stack, passing the top object (child) as an argument.
     *
     * @param methodName Method name of the parent method to call
     */
    LinkedParamTypeBuilder setRoot(String methodName);

    /**
     * Calls a "set parent" method on the top (child) object, passing the (top-1) (parent) object as an argument.
     *
     * @param methodName Method name of the "set parent" method to call
     */
    LinkedParamTypeBuilder setTop(String methodName);

}
