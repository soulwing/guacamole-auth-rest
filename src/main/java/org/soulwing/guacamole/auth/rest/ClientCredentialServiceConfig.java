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
 * A configuration for a {@link ClientCredentialService}.
 *
 * An instance of this type is used by the service to avoid coupling
 * to Guacamole-specific details about how extensions are configured. This
 * allows much more of the service implementation to be tested using mocks.
 */
interface ClientCredentialServiceConfig {

  /**
   * Gets the realm name to match in an authentication challenge that specifies
   * Basic authentication.
   *
   * @return
   *    The realm name or null if not configured.
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the property value.
   */
  String getBasicRealm() throws GuacamoleException;

  /**
   * Gets the username to present in response to an authentication challenge
   * specifying Basic authentication.
   *
   * @return
   *    The username.
   *
   * @throws GuacamoleException
   *    If no username is configured or an error occurs in retrieving it.
   */
  String getBasicUsername() throws GuacamoleException;

  /**
   * Gets the password to present in response to an authentication challenge
   * specifying Basic authentication.
   *
   * @return
   *    The password.
   *
   * @throws GuacamoleException
   *    If no password is configured on an error occurs in retrieving it.
   */
  String getBasicPassword() throws GuacamoleException;

  /**
   * Gets the realm name to match in an authentication challenge that specifies
   * Digest authentication.
   *
   * @return
   *    The realm name or null if not configured
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the property value.
   */
  String getDigestRealm() throws GuacamoleException;

  /**
   * Gets the username to present in response to an authentication challenge
   * specifying Digest authentication.
   *
   * @return
   *    The username.
   *
   * @throws GuacamoleException
   *    If no username is configured or an error occurs in retrieving it.
   */
  String getDigestUsername() throws GuacamoleException;

  /**
   * Gets the password to present in response to an authentication challenge
   * specifying Digest authentication.
   *
   * @return
   *    The password.
   *
   * @throws GuacamoleException
   *    If no password is configured or an error occurs in retrieving it.
   */
  String getDigestPassword() throws GuacamoleException;

  /**
   * Gets the realm name to match in an authentication challenge that specifies
   * OAuth2 bearer authentication.
   *
   * @return
   *    The realm name or null if not configured
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the property value.
   */
  String getOAuth2Realm() throws GuacamoleException;

  /**
   * Gets the URL for the OAuth2 access token service.
   *
   * @return
   *    The URL.
   *
   * @throws GuacamoleException
   *    If no URL is configured or an error occurs in retrieving its value.
   */
  String getOAuth2ServiceUrl() throws GuacamoleException;

}
