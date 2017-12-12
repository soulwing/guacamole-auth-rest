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

/**
 * A configuration for an {@link AuthService}.
 *
 * An instance of this type is used by the service to avoid coupling
 * to Guacamole-specific details about how extensions are configured. This
 * allows much more of the service implementation to be tested using mocks.
 */
interface AuthServiceConfig {

  /**
   * Gets the base URL for the REST auth service.
   *
   * @return
   *    The service URL.
   *
   * @throws GuacamoleException
   *    If no service URL is configured or if some other error occurs in
   *    retrieving the URL.
   */
  String getServiceUrl() throws GuacamoleException;

  /**
   * Gets the authorization URI path for the REST auth service.
   *
   * @return
   *    The authorization URI path.
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the URI path.
   */
  String getAuthorizationUri() throws GuacamoleException;

}
