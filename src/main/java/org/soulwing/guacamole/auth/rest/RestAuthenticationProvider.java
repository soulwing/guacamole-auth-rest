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

/**
 * An {@link AuthenticationProvider} that delegates to an {@link AuthService}.
 *
 */
public class RestAuthenticationProvider extends SimpleAuthenticationProvider {

  /** Auth service facade to which we delegate the real work. */
  private final AuthService authService;

  /** Client credentialing service. */
  private final ClientCredentialService credentialService;

  /**
   * Constructs a new instance that delegates to the default {@link AuthService}
   * and {@link ClientCredentialService} implementations.
   */
  public RestAuthenticationProvider() {
    throw new UnsupportedOperationException();
  }

  /**
   * Constructs a new instance that delegates to the given {@link AuthService}
   * and {@link ClientCredentialService}.
   *
   * @param authService
   *    The auth service delegate.
   *
   * @param credentialService
   *    The client credential service delegate.
   */
  RestAuthenticationProvider(AuthService authService,
      ClientCredentialService credentialService) {
    this.authService = authService;
    this.credentialService = credentialService;
  }

  @Override
  public String getIdentifier() {
    return getClass().getSimpleName();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(
      Credentials credentials) throws GuacamoleException {
    Map authResult = authorizeSubject(credentials);
    final Boolean authorized = (Boolean) authResult.get(
        ProtocolConstants.AUTH_KEY);
    if (authorized == null || !authorized) return null;
    return createGuacConfigs(
        (Map) authResult.get(ProtocolConstants.CONFIGS_KEY));
  }

  private Map authorizeSubject(Credentials subjectCredentials)
      throws GuacamoleException {
    try {
      return authorizeSubject(subjectCredentials,
          credentialService.currentCredential());
    }
    catch (UnauthorizedClientException ex1) {
      try {
        return authorizeSubject(subjectCredentials,
            credentialService.acquireCredential(ex1.getChallenge()));
      }
      catch (UnauthorizedClientException ex2) {
        throw new GuacamoleServerException(
            "provider client authentication failed");
      }
    }
  }

  private Map authorizeSubject(Credentials subjectCredentials,
      ClientCredential clientCredential) throws GuacamoleException,
      UnauthorizedClientException {
    try {
      return authService.authorize(subjectCredentials, clientCredential);
    }
    catch (ClientException ex) {
      throw new GuacamoleServerException(
          "provider error when contacting REST service", ex);
    }
    catch (ServiceException ex) {
      throw new GuacamoleServerException(
          "REST service error", ex);
    }
  }

  private Map<String, GuacamoleConfiguration> createGuacConfigs(
      Map configs) throws GuacamoleServerException {

    if (configs == null) {
      throw new GuacamoleServerException("configurations required");
    }

    final Map<String, GuacamoleConfiguration> guacConfigs =
        new LinkedHashMap<>();

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
