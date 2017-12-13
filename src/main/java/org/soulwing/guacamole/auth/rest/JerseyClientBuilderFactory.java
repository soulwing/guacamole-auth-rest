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


import org.apache.guacamole.GuacamoleException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.HTTPDigestAuthFilter;

/**
 * A {@link JerseyClientFactory} that constructs new Jersey {@link Client}
 * instances.
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
    final Client client = Client.create();
    addAuthFilters(client, config);
    return client;
  }

  /**
   * Adds authentication filters supporting HTTP Basic and Digest
   * authentication using credentials supplied in the given configuration.
   *
   * @param client
   *    The client to which filters will be added.
   *
   * @param config
   *    Configuration which contains the credentials to be used.
   *
   * @throws GuacamoleException
   *    If an error occurs in obtaining the configuration properties needed
   *    to configure authentication filters.
   */
  private void addAuthFilters(Client client, AuthServiceConfig config)
      throws GuacamoleException {
    if (config.isBasicConfigured()) {
      client.addFilter(new HTTPBasicAuthFilter(config.getBasicUsername(),
          config.getBasicPassword()));
    }
    if (config.isDigestConfigured()) {
      client.addFilter(new HTTPDigestAuthFilter(config.getDigestUsername(),
          config.getDigestPassword()));
    }
  }

}
