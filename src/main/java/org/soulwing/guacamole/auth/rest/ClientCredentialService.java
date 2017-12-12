/*
 * File created on Dec 10, 2017
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

/**
 * A service that provides credentials for a client of the REST auth service.
 */
interface ClientCredentialService {

  /**
   * Initializes the service using the given configuration.
   *
   * @param config
   *    Configuration for the service.
   *
   * @throws GuacamoleException
   *    If an error occurs in initializing the service.
   */
  void init(ClientCredentialServiceConfig config) throws GuacamoleException;

  /**
   * Gets the most recently acquired credential.
   *
   * @return
   *    The most recently acquired credential or the {@link NullCredential}
   *    instance if no credential has been acquired.
   */
  ClientCredential currentCredential();

  /**
   * Acquires the appropriate credential for the authentication challenge
   *
   * @param challenge
   *    The challenge presented by the REST auth service.
   *
   * @return
   *    Credential to present in response to the given challenge.
   *
   * @throws GuacamoleException
   *    If the appropriate credential cannot be acquired
   */
  ClientCredential acquireCredential(ClientChallenge challenge)
      throws GuacamoleException;

}
