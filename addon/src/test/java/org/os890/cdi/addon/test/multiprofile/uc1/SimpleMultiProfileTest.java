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
package org.os890.cdi.addon.test.multiprofile.uc1;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@RunWith(CdiTestRunner.class)
public class SimpleMultiProfileTest {
    @Inject
    private Instance<BeanProfileY> inactiveBean;

    @Test
    public void allBeans() {
        Assert.assertNotNull(BeanProvider.getContextualReference(BeanNoProfile.class));

        Assert.assertNotNull(BeanProvider.getContextualReference(BeanProfileX.class));
        Assert.assertNotNull(BeanProvider.getContextualReference(BeanProfileZ.class));

        Assert.assertTrue(inactiveBean.isUnsatisfied());
    }
}
