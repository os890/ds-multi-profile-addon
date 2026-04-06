/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.os890.cdi.addon.multiprofile.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a CDI bean with one or more named profiles.
 *
 * <p>At container startup the {@link org.os890.cdi.addon.multiprofile.impl.MultiProfileExtension}
 * reads the {@code active-profiles} configuration property (comma-separated list)
 * and vetoes every bean whose profile names do not intersect with the active set.
 * Beans without this annotation are never vetoed.</p>
 *
 * <p>Can be placed directly on a bean class or used as a meta-annotation on a
 * custom stereotype, enabling type-safe profile declarations.</p>
 *
 * @see org.os890.cdi.addon.multiprofile.impl.MultiProfileExtension
 */
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface Profile {

    /**
     * The profile names this bean belongs to.
     *
     * @return one or more profile name strings
     */
    String[] value();
}
