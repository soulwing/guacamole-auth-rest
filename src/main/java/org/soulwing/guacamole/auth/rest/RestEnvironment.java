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
class RestEnvironment
    implements AuthServiceConfig, ClientCredentialServiceConfig {

  /**
   * Default value for the {@link #AUTHORIZATION_URI} property.
   */
  static final String DEFAULT_AUTHORIZATION_URI = "/authorization";

  /**
   * Property that specifies the absolute URL for the REST service used to
   * authorize subject users.
   */
  static final GuacamoleProperty<String> SERVICE_URL =
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
  static final GuacamoleProperty<String> AUTHORIZATION_URI =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-authorization-uri";
    }
  };

  /**
   * Property that specifies a realm name to match for Basic authentication.
   */
  static final GuacamoleProperty<String> BASIC_REALM =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-basic-realm";
    }
  };

  /**
   * Property that specifies the username to be used for Basic authentication.
   */
  static final GuacamoleProperty<String> BASIC_USERNAME =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-basic-username";
    }
  };

  /**
   * Property that specifies the password to be used for Basic authentication.
   */
  static final GuacamoleProperty<String> BASIC_PASSWORD =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-basic-password";
    }
  };

  /**
   * Property that specifies a realm name to match for Digest authentication.
   */
  static final GuacamoleProperty<String> DIGEST_REALM =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-digest-realm";
    }
  };

  /**
   * Property that specifies the username to be used for Digest authentication.
   */
  static final GuacamoleProperty<String> DIGEST_USERNAME =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-digest-username";
    }
  };

  /**
   * Property that specifies the password to be used for Digest authentication.
   */
  static final GuacamoleProperty<String> DIGEST_PASSWORD =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-digest-password";
    }
  };

  /**
   * Property that specifies a realm name to match for OAuth2 authentication.
   */
  static final GuacamoleProperty<String> OAUTH2_REALM =
      new StringGuacamoleProperty() {
        @Override
        public String getName() {
          return "auth-rest-oauth2-realm";
        }
      };

  /**
   * Property that specifies the URL for the OAuth2 access token service.
   */
  static final GuacamoleProperty<String> OAUTH2_SERVICE_URL =
      new StringGuacamoleProperty() {
    @Override
    public String getName() {
      return "auth-rest-oauth2-service-url";
    }
  };

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
  RestEnvironment(Environment delegate) {
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
   * Gets the realm name to match in an authentication challenge that specifies
   * Basic authentication.
   *
   * @return
   *    The realm name or null if not configured
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the realm name from the delegate
   *    environment.
   */
  @Override
  public String getBasicRealm() throws GuacamoleException {
    return delegate.getProperty(BASIC_REALM);
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
   * Gets the realm name to match in an authentication challenge that specifies
   * Digest authentication.
   *
   * @return
   *    The realm name or null if not configured
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the realm name from the delegate
   *    environment.
   */
  @Override
  public String getDigestRealm() throws GuacamoleException {
    return delegate.getProperty(DIGEST_REALM);
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

  /**
   * Gets the realm name to match in an authentication challenge that specifies
   * OAuth2 bearer authentication.
   *
   * @return
   *    The realm name or null if not configured
   *
   * @throws GuacamoleException
   *    If an error occurs in retrieving the property value from the delegate
   *    environment.
   */
  @Override
  public String getOAuth2Realm() throws GuacamoleException {
    return delegate.getProperty(OAUTH2_REALM);
  }

  /**
   * Gets the URL for the OAuth2 access token service.
   *
   * @return
   *    The URL.
   *
   * @throws GuacamoleException
   *    If no URL is configured or an error occurs in retrieving it from the
   *    delegate environment.
   */
  @Override
  public String getOAuth2ServiceUrl() throws GuacamoleException {
    return delegate.getRequiredProperty(OAUTH2_SERVICE_URL);
  }

}
