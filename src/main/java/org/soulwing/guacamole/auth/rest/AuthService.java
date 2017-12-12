/*
 * File created on Dec 9, 2017
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

import java.util.Map;

import org.apache.guacamole.GuacamoleException;

/**
 * A service that authenticates and authorizes users.
 * <p>
 * An non-trivial implementation of this service will be a client of the REST
 * service defined in the top level README.
 */
interface AuthService {

  /**
   * Initializes the service using the given configuration.
   *
   * @param config
   *   Configuration for the service.
   *
   * @throws GuacamoleException
   *   If an error occurs in initializing the service.
   */
  void init(AuthServiceConfig config) throws GuacamoleException;

  /**
   * Requests authorization of a user subject identified by the given
   * credentials.
   *
   * @param subject
   *   The subject to be authorized.
   *
   * @return
   *   A map representation of the authorization response structure.
   *
   * @throws GuacamoleException
   *   If an error occurs in getting the authorization result.
   */
  Map authorize(AuthSubject subject) throws GuacamoleException;

}
