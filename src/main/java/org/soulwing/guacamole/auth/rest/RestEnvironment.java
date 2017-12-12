/*
 * File created on Dec 11, 2017
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
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.properties.GuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * Definitions for all of the configuration properties required and/or
 * supported by this extension.
 */
class RestEnvironment implements AuthServiceConfig {

  /**
   * Default value for the {@link #AUTHORIZATION_URI} property.
   */
  private static final String DEFAULT_AUTHORIZATION_URI = "/authorization";

  /**
   * Property that specifies the absolute URL for the REST service used to
   * authorize subject users.
   */
  private static final GuacamoleProperty<String> SERVICE_URL =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-service-url";
    }
  };

  /**
   * Property that specifies the URI path for the authorization resource
   * exposed by the REST service.
   */
  private static final GuacamoleProperty<String> AUTHORIZATION_URI =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-authorization-uri";
    }
  };

  /**
   * Property that specifies the username to be used for Basic authentication.
   */
  private static final GuacamoleProperty<String> BASIC_USERNAME =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-basic-username";
    }
  };

  /**
   * Property that specifies the password to be used for Basic authentication.
   */
  private static final GuacamoleProperty<String> BASIC_PASSWORD =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-basic-password";
    }
  };

  /**
   * Property that specifies the username to be used for Digest authentication.
   */
  private static final GuacamoleProperty<String> DIGEST_USERNAME =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-digest-username";
    }
  };

  /**
   * Property that specifies the password to be used for Digest authentication.
   */
  private static final GuacamoleProperty<String> DIGEST_PASSWORD =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-digest-password";
    }
  };

  /**
   * The delegate Guacamole environment.
   */
  private final Environment delegate;

  /**
   * Constructs an instance that delegates to a new {@link LocalEnvironment}
   * instance.
   *
   * @throws GuacamoleException
   *    If an error occurs in constructing the delegate environment.
   */
  RestEnvironment() throws GuacamoleException {
    this(new LocalEnvironment());
  }

  /**
   * Constructs an instance that delegates to the specified environment.
   *
   * @param delegate
   *    The delegate environment.
   */
  private RestEnvironment(Environment delegate) {
    this.delegate = delegate;
  }

  /**
   * Gets the auth service URL from the corresponding property in the delegate
   * environment.
   *
   * @return
   *    The service URL.
   *
   * @throws GuacamoleException
   *    If no service URL is set or if some other error occurs in retrieving
   *    the property value from the delegate environment.
   */
  @Override
  public String getServiceUrl() throws GuacamoleException {
    return delegate.getRequiredProperty(SERVICE_URL);
  }

  /**
   * Gets the authorization URI from the corresponding property in the delegate
   * environment.
   *
   * @return
   *    The authorization URI or {@link #DEFAULT_AUTHORIZATION_URI} if the
   *    property has no value in the delegate environment.
   *
   * @throws GuacamoleException
   *    If thrown by the delegate environment.
   */
  @Override
  public String getAuthorizationUri() throws GuacamoleException {
    return delegate.getProperty(AUTHORIZATION_URI, DEFAULT_AUTHORIZATION_URI);
  }

  /**
   * Gets a flag indicating whether Basic authentication is configured.
   *
   * @return
   *    Flag state.
   *
   * @throws GuacamoleException
   *    If an error occurs in determining the flag state from the delegate
   *    environment.
   */
  @Override
  public boolean isBasicConfigured() throws GuacamoleException {
    return delegate.getProperty(BASIC_USERNAME) != null;
  }

  /**
   * Gets the username to present in response to an authentication challenge
   * specifying Basic authentication.
   *
   * @return
   *    The username.
   *
   * @throws GuacamoleException
   *    If no username is configured or if an error occurs in retrieving it
   *    from the delegate environment.
   */
  @Override
  public String getBasicUsername() throws GuacamoleException {
    return delegate.getRequiredProperty(BASIC_USERNAME);
  }

  /**
   * Gets the password to present in response to an authentication challenge
   * specifying Basic authentication.
   *
   * @return
   *    The password.
   *
   * @throws GuacamoleException
   *    If no password is configured or if an error occurs in retrieving it
   *    from the delegate environment.
   */
  @Override
  public String getBasicPassword() throws GuacamoleException {
    return delegate.getRequiredProperty(BASIC_PASSWORD);
  }

  /**
   * Gets a flag indicating whether Digest authentication is configured.
   *
   * @return
   *    Flag state.
   *
   * @throws GuacamoleException
   *    If an error occurs in determining the flag state from the delegate
   *    environment.
   */
  @Override
  public boolean isDigestConfigured() throws GuacamoleException {
    return delegate.getProperty(DIGEST_USERNAME) != null;
  }

  /**
   * Gets the username to present in response to an authentication challenge
   * specifying Digest authentication.
   *
   * @return
   *    The username.
   *
   * @throws GuacamoleException
   *    If no username is configured or if an error occurs in retrieving it
   *    from the delegate environment.
   */
  @Override
  public String getDigestUsername() throws GuacamoleException {
    return delegate.getRequiredProperty(DIGEST_USERNAME);
  }

  /**
   * Gets the password to use in generating the response to an authentication
   * challenge specifying Digest authentication.
   *
   * @return
   *    The password.
   *
   * @throws GuacamoleException
   *    If no password is configured or if an error occurs in retrieving it
   *    from the delegate environment.
   */
  @Override
  public String getDigestPassword() throws GuacamoleException {
    return delegate.getRequiredProperty(DIGEST_PASSWORD);
  }

}
