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

package org.os890.cdi.addon.test.multiprofile.uc2;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests type-safe multi-profile activation using stereotype annotations.
 *
 * <p>Active profiles are configured as {@code A,C,X,Z} in
 * {@code apache-deltaspike.properties}. Beans with profiles A and C
 * must be active, while profile B must be vetoed.</p>
 */
@EnableTestBeans
class TypesafeMultiProfileTest {

    @Inject
    private Instance<BeanProfileB> inactiveBean;

    /**
     * Verifies that beans without a profile, and beans with active profiles A/C
     * are available, while the bean with inactive profile B is vetoed.
     */
    @Test
    void allBeans() {
        assertNotNull(BeanProvider.getContextualReference(BeanNoProfile.class));

        assertNotNull(BeanProvider.getContextualReference(BeanProfileA.class));
        assertNotNull(BeanProvider.getContextualReference(BeanProfileC.class));

        assertTrue(inactiveBean.isUnsatisfied());
    }
}
