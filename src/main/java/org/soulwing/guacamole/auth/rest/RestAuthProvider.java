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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link AuthenticationProvider} that delegates to an {@link AuthService}
 * to authenticate and authorize Guacamole users.
 */
public class RestAuthProvider extends SimpleAuthenticationProvider {

  private static final Logger logger = LoggerFactory.getLogger(RestAuthProvider.class);

  /** Auth service facade to which we delegate the real work. */
  private final AuthService authService;

  /**
   * Constructs a new instance that delegates to the default {@link AuthService}
   * implementation.
   *
   * @throws GuacamoleException
   *    If the provider could not be instantiated due to an error.
   */
  public RestAuthProvider() throws GuacamoleException {
    this(new JerseyAuthService());
  }

  /**
   * Constructs a new instance that delegates to the given {@link AuthService}.
   *
   * @param authService
   *    The auth service delegate.
   *
   * @throws GuacamoleException
   *    If the service could not be instantiated due to an error.
   */
  RestAuthProvider(AuthService authService) throws GuacamoleException {
    try {
      final RestEnvironment environment = new RestEnvironment();
      this.authService = authService;
      this.authService.init(environment);
    }
    catch (GuacamoleException ex) {
      logger.error("initialization failed with error: {}", ex);
      if (logger.isDebugEnabled()) {
        logger.debug("exception trace: {}", ex, ex);
      }
      throw ex;
    }
  }

  /**
   * Gets the identifier of this provider.
   *
   * @return
   *    The provider identifier.
   */
  @Override
  public String getIdentifier() {
    return getClass().getSimpleName();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(
      Credentials credentials) throws GuacamoleException {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("requesting authorization for user '{}' @ {} [{}]",
            credentials.getUsername(), credentials.getRemoteHostname(),
            credentials.getRemoteAddress());
      }

      final Map authResult = authService.authorize(
          new DelegatingAuthSubject(credentials));

      final Boolean authorized = (Boolean) authResult.get(
          ProtocolConstants.AUTH_KEY);

      if (logger.isDebugEnabled()) {
        logger.debug("user '{}' {} authorized", credentials.getUsername(),
            authorized ? "is" : "is not");
      }

      if (authorized == null || !authorized) return null;

      return createGuacConfigs(
          (Map) authResult.get(ProtocolConstants.CONFIGS_KEY));
    }
    catch (GuacamoleException ex) {
      logger.error("authorized request ended in error {}: ", ex);
      if (logger.isDebugEnabled()) {
        logger.debug("exception trace: {}", ex, ex);
      }
      throw ex;
    }
  }


  private Map<String, GuacamoleConfiguration> createGuacConfigs(
      Map configs) throws GuacamoleServerException {

    if (configs == null) {
      logger.error("REST service provided no configurations for user");
      throw new GuacamoleServerException("configurations required");
    }

    final Map<String, GuacamoleConfiguration> guacConfigs =
        new LinkedHashMap<String, GuacamoleConfiguration>();

    for (final Object name : configs.keySet()) {
      guacConfigs.put(name.toString(),
          createGuacConfig((Map) configs.get(name)));
    }

    return guacConfigs;
  }

  private GuacamoleConfiguration createGuacConfig(Map config) {
    final GuacamoleConfiguration guacConfig = new GuacamoleConfiguration();
    guacConfig.setProtocol((String) config.get(ProtocolConstants.PROTOCOL_KEY));

    final Map parameters = (Map) config.get(ProtocolConstants.PARAMS_KEY);
    if (parameters != null) {
      for (final Object paramName : parameters.keySet()) {
        final Object paramValue = parameters.get(paramName);
        if (paramName != null) {
          guacConfig.setParameter(paramName.toString(), paramValue.toString());
        }
      }
    }

    return guacConfig;
  }

}
