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

import org.apache.guacamole.net.auth.Credentials;

/**
 * A service that authenticates and authorizes users.
 * <p>
 * An non-trivial implementation of this service will be a client of the REST
 * service defined in the top level README.
 */
interface AuthService {

  /**
   * Requests authorization of a user subject identified by the given
   * credentials.
   *
   * @param subjectCredentials
   *   Credentials to use in identifying, authenticating, and authorizing the
   *   user.
   *
   * @param clientCredential
   *   Credential to use in authentication the provider as a client of the
   *   service.
   *
   * @return
   *   A map representation of the authorization response structure.
   *
   * @throws UnauthorizedClientException
   *   If the provider (acting in the client role) must authenticate in order
   *   to successfully complete an authorization request.
   *
   * @throws ClientException
   *   If the service reports an error caused by the client (other than
   *   the need to authenticate itself).
   *
   * @throws ServiceException
   *   If the service reports an internal error (not caused by the client).
   */
  Map authorize(Credentials subjectCredentials,
      ClientCredential clientCredential) throws UnauthorizedClientException,
      ClientException, ServiceException;

}
