/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.cdi.addon.multiprofile.impl;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.util.AnnotationUtils;
import org.os890.cdi.addon.multiprofile.api.Profile;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultiProfileExtension implements Extension {
    private Set<String> activeProfileNames = new HashSet<>();

    protected void determineActiveProfiles(@Observes BeforeBeanDiscovery beforeBeanDiscovery) {
        String activeProfileString = ConfigResolver.getPropertyValue("active-profiles", "Production");
        Collections.addAll(activeProfileNames, activeProfileString.split(","));
    }

    protected void matchActiveProfiles(@Observes ProcessAnnotatedType pat, BeanManager beanManager) {
        Set<Annotation> annotations = pat.getAnnotatedType().getAnnotations();
        Profile profile = AnnotationUtils.findAnnotation(beanManager, annotations.toArray(new Annotation[annotations.size()]), Profile.class);

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
