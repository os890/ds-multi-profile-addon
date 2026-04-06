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

package org.os890.cdi.addon.multiprofile.impl;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.util.AnnotationUtils;
import org.os890.cdi.addon.multiprofile.api.Profile;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * CDI portable extension that activates or vetoes beans based on named profiles.
 *
 * <p>During the {@link BeforeBeanDiscovery} phase the extension reads the
 * {@code active-profiles} configuration property via DeltaSpike's
 * {@link ConfigResolver}. The property value is a comma-separated list of
 * profile names (defaults to {@code "Production"} when absent).</p>
 *
 * <p>For every annotated type discovered by the container the extension checks
 * whether the type (or any of its meta-annotations) carries a {@link Profile}
 * annotation. If a profile annotation is found and none of its declared names
 * match the active set, the type is vetoed.</p>
 *
 * @see Profile
 */
public class MultiProfileExtension implements Extension {

    private Set<String> activeProfileNames = new HashSet<>();

    /**
     * Reads the {@code active-profiles} configuration property and populates
     * the set of active profile names.
     *
     * @param beforeBeanDiscovery the CDI lifecycle event fired before bean discovery
     */
    protected void determineActiveProfiles(@Observes BeforeBeanDiscovery beforeBeanDiscovery) {
        String activeProfileString = ConfigResolver.getPropertyValue("active-profiles", "Production");
        Collections.addAll(activeProfileNames, activeProfileString.split(","));
    }

    /**
     * Checks each discovered annotated type against the active profiles and
     * vetoes types whose declared profiles do not intersect with the active set.
     *
     * @param pat         the CDI process-annotated-type event
     * @param beanManager the CDI bean manager
     */
    protected void matchActiveProfiles(@Observes ProcessAnnotatedType<?> pat, BeanManager beanManager) {
        Set<Annotation> annotations = pat.getAnnotatedType().getAnnotations();
        Profile profile = AnnotationUtils.findAnnotation(
                beanManager, annotations.toArray(new Annotation[0]), Profile.class);

        if (profile == null) {
            return;
        }

        for (String supportedProfile : profile.value()) {
            if (activeProfileNames.contains(supportedProfile)) {
                return;
            }
        }
        pat.veto();
    }
}
