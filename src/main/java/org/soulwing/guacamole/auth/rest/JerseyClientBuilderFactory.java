/*
 * File created on Dec 12, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.guacamole.auth.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;

import org.apache.guacamole.GuacamoleException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * A {@link JerseyClientFactory} that constructs new {@link Client} instances
 * using the Jersey {@link ClientBuilder} API.
 */
class JerseyClientBuilderFactory implements JerseyClientFactory {

  /**
   * Creates a new Jersey client instance using the given configuration.
   *
   * @param config
   *    Configuration to be used in creating the client.
   *
   * @return
   *    The newly constructed client instance.
   *
   * @throws GuacamoleException
   *    If an error occurs in constructing and configuring the client.
   */
  @Override
  public Client newClient(AuthServiceConfig config) throws GuacamoleException {
    return ClientBuilder.newClient(newConfig(config));
  }

  /**
   * Creates a Jersey client configuration from the given auth service
   * configuration.
   *
   * @param config
   *    The auth service configuration that will be inspected to determine
   *    the appropriate client configuration.
   *
   * @return
   *    A newly instantiated Jersey client configuration
   *
   * @throws GuacamoleException
   *    If an error occurs in creating the client configuration.
   */
  private ClientConfig newConfig(AuthServiceConfig config)
      throws GuacamoleException {
    final ClientConfig clientConfig = new ClientConfig();
    clientConfig.register(newAuthenticationFeature(config));
    return clientConfig;
  }

  /**
   * Creates a Jersey {@link Feature} supporting HTTP Basic and Digest
   * authentication using credentials supplied in the given configuration.
   * @param config
   *    Configuration which contains the credentials to be used.
   *
   * @return
   * @throws GuacamoleException
   */
  private Feature newAuthenticationFeature(AuthServiceConfig config)
      throws GuacamoleException {

    final HttpAuthenticationFeature.UniversalBuilder builder =
        HttpAuthenticationFeature.universalBuilder();

    if (config.isBasicConfigured()) {
      builder.credentialsForBasic(config.getBasicUsername(),
          config.getBasicPassword());
    }
    if (config.isDigestConfigured()) {
      builder.credentialsForDigest(config.getDigestUsername(),
          config.getDigestPassword());
    }
    return builder.build();

  }

}
