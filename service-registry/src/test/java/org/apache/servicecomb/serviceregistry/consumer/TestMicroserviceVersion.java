/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.serviceregistry.consumer;

import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.ServiceRegistry;
import org.apache.servicecomb.serviceregistry.api.registry.Microservice;
import org.apache.servicecomb.serviceregistry.version.Version;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import mockit.Expectations;
import mockit.Mocked;

public class TestMicroserviceVersion {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void constructInvalid(@Mocked ServiceRegistry serviceRegistry) {

    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage(Matchers.is("Invalid microserviceId invalid."));

    new Expectations(RegistryUtils.class) {
      {
        RegistryUtils.getServiceRegistry();
        result = serviceRegistry;
        serviceRegistry.getAggregatedRemoteMicroservice("invalid");
        result = null;
      }
    };
    new MicroserviceVersion("invalid");
  }

  @Test
  public void constructNormal(@Mocked ServiceRegistry serviceRegistry) {
    MicroserviceVersion microserviceVersion = MicroserviceVersionTestUtils
        .createMicroserviceVersion("1", "1.0.0", serviceRegistry);
    Assert.assertEquals("1", microserviceVersion.getMicroservice().getServiceId());
    Assert.assertEquals("1.0.0.0", microserviceVersion.getVersion().getVersion());
  }

  @Test
  public void constructByMicroservice() {
    Microservice microservice = new Microservice();
    String version = "1.2.1";
    microservice.setVersion(version);
    MicroserviceVersion microserviceVersion = new MicroserviceVersion(microservice);

    Assert.assertSame(microservice, microserviceVersion.getMicroservice());
    Assert.assertEquals(new Version(version), microserviceVersion.getVersion());
  }
}
