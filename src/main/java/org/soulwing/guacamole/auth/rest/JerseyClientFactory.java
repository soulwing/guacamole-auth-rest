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

import org.apache.guacamole.GuacamoleException;

/**
 * A factory that produces Jersey {@link Client} instances.
 */
interface JerseyClientFactory {

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
  Client newClient(AuthServiceConfig config) throws GuacamoleException;

}
