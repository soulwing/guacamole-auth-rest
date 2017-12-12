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

import java.util.Map;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;

/**
 * An {@link AuthService} implemented using the Jersey REST client API.
 */
class JerseyAuthService implements AuthService {

  /** Factory that will be used to create a Jersey client.*/
  private final JerseyClientFactory clientFactory;

  /** JSON marshaller to use in making requests and handling responses. */
  private final JsonMarshaller marshaller;

  /** Configuration received at init time. */
  private AuthServiceConfig config;

  /**
   * Constructs a new instance that will create a Jersey client using
   * a {@link JerseyClientBuilderFactory} and a {@link JacksonJsonMarshaller}.
   */
  JerseyAuthService() {
    this(new JerseyClientBuilderFactory(), new JacksonJsonMarshaller());
  }

  /**
   * Constructs a new instance that will create a Jersey client using the
   * given factory.
   *
   * @param clientFactory
   *    The factory to be used in creating the Jersey client.
   *
   * @param marshaller
   *    The JSON marshaller to be used when sending authorization requests
   *    and processing results.
   */
  JerseyAuthService(JerseyClientFactory clientFactory,
      JsonMarshaller marshaller) {
    this.clientFactory = clientFactory;
    this.marshaller = marshaller;
  }

  /**
   * The Jersey client instance used in servicing all authorization requests.
   */
  private Client client;

  /**
   * Initializes the Jersey client instance using the specified configuration.
   *
   * @param config
   *   Configuration for the service.
   *
   * @throws GuacamoleException
   *   If an error occurs in initializing the Jersey client.
   */
  @Override
  public void init(AuthServiceConfig config) throws GuacamoleException {
    this.client = clientFactory.newClient(config);
    this.config = config;
  }

  /**
   * Requests authorization for a subject with the given credentials.
   *
   * @param subject
   *   The subject to be authorized.
   *
   * @return
   *    A map representation of the JSON structure of the auth response.
   *
   * @throws GuacamoleException
   *    If no request is made due to an error in preparing the request.
   */
  @Override
  public Map authorize(AuthSubject subject) throws GuacamoleException {
    try {
      return marshaller.toMap(client.target(config.getServiceUrl())
          .path(config.getAuthorizationUri())
          .request(MediaType.APPLICATION_JSON)
          .post(Entity.json(marshaller.toJson(subject)), String.class));
    }
    catch (ClientErrorException ex) {
      throw new GuacamoleServerException(
          "provider error when contacting REST service", ex);
    }
    catch (ServerErrorException ex) {
      throw new GuacamoleServerException("REST service error", ex);
    }
    catch (WebApplicationException ex) {
      throw new GuacamoleServerException("REST error", ex);
    }
  }

}
