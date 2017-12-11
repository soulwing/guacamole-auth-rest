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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * Unit tests for {@link RestAuthenticationProvider}.
 *
 * @author Carl Harris
 */
public class RestAuthenticationProviderTest {

  private static final String PROTOCOL_NAME = "some protocol";
  private static final String CONFIG_NAME = "some name";
  private static final String NUMBER_PARAM_NAME = "number param";
  private static final int NUMBER_PARAM_VALUE = -1;
  private static final boolean BOOLEAN_PARAM_VALUE = true;
  private static final String STRING_PARAM_VALUE = "some string";
  private static final String BOOLEAN_PARAM_NAME = "boolean param";
  private static final String STRING_PARAM_NAME = "string param";
  @Rule
  public final MockitoRule rule =
      MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock
  private Credentials credentials;

  @Mock
  private AuthService authService;

  @Mock
  private ClientCredentialService credentialService;

  @Mock
  private ClientCredential clientCredential;

  private RestAuthenticationProvider provider;

  @Before
  public void setUp() throws Exception {
    provider = new RestAuthenticationProvider(authService, credentialService);
  }


  @Test(expected = GuacamoleServerException.class)
  public void testWhenAuthServiceThrowsClientException()
      throws Exception {
    when(credentialService.currentCredential()).thenReturn(clientCredential);
    doThrow(new ClientException())
        .when(authService).authorize(credentials, clientCredential);
    provider.getAuthorizedConfigurations(credentials);
  }

  @Test(expected = GuacamoleServerException.class)
  public void testWhenAuthServiceThrowsServerException()
      throws Exception {
    when(credentialService.currentCredential()).thenReturn(clientCredential);
    doThrow(new ServiceException())
        .when(authService).authorize(credentials, clientCredential);
    provider.getAuthorizedConfigurations(credentials);
  }

  @Test
  public void testWhenNotAuthorized() throws Exception {
    when(credentialService.currentCredential()).thenReturn(clientCredential);
    when(authService.authorize(credentials, clientCredential))
        .thenReturn(Collections.singletonMap(ProtocolConstants.AUTH_KEY, false));
    assertThat(provider.getAuthorizedConfigurations(credentials)).isNull();
  }

  @Test
  public void testWhenAuthorizedFlagMissing() throws Exception {
    when(credentialService.currentCredential()).thenReturn(clientCredential);
    when(authService.authorize(credentials, clientCredential))
        .thenReturn(Collections.emptyMap());
    assertThat(provider.getAuthorizedConfigurations(credentials)).isNull();
  }

  @Test
  public void testWhenAuthorized() throws Exception {
    Map<String, Object> params = new LinkedHashMap<>();
    params.put(STRING_PARAM_NAME, STRING_PARAM_VALUE);
    params.put(BOOLEAN_PARAM_NAME, BOOLEAN_PARAM_VALUE);
    params.put(NUMBER_PARAM_NAME, NUMBER_PARAM_VALUE);

    Map<String, Object> config = new LinkedHashMap<>();
    config.put(ProtocolConstants.PROTOCOL_KEY, PROTOCOL_NAME);
    config.put(ProtocolConstants.PARAMS_KEY, params);

    Map<String, Object> authResult = new LinkedHashMap<>();
    authResult.put(ProtocolConstants.AUTH_KEY, true);

    authResult.put(ProtocolConstants.CONFIGS_KEY,
        Collections.singletonMap(CONFIG_NAME, config));

    when(credentialService.currentCredential())
        .thenReturn(clientCredential);
    when(authService.authorize(credentials, clientCredential))
        .thenReturn(authResult);


    final Map<String, GuacamoleConfiguration> guacConfigs =
        provider.getAuthorizedConfigurations(credentials);

    assertThat(guacConfigs).isNotNull();
    assertThat(guacConfigs.containsKey(CONFIG_NAME)).isTrue();

    final GuacamoleConfiguration guacConfig = guacConfigs.get(CONFIG_NAME);
    assertThat(guacConfig.getProtocol())
        .isEqualTo(PROTOCOL_NAME);
    assertThat(guacConfig.getParameter(STRING_PARAM_NAME))
        .isEqualTo(STRING_PARAM_VALUE);
    assertThat(guacConfig.getParameter(BOOLEAN_PARAM_NAME))
        .isEqualTo(Boolean.toString(BOOLEAN_PARAM_VALUE));
    assertThat(guacConfig.getParameter(NUMBER_PARAM_NAME))
        .isEqualTo(Integer.toString(NUMBER_PARAM_VALUE));
  }


  @Test(expected = GuacamoleServerException.class)
  public void testWhenAuthorizedButConfigsMissing() throws Exception {
    Map<String, Object> authResult = new LinkedHashMap<>();
    authResult.put(ProtocolConstants.AUTH_KEY, true);

    when(credentialService.currentCredential())
        .thenReturn(clientCredential);
    when(authService.authorize(credentials, clientCredential))
        .thenReturn(authResult);

    provider.getAuthorizedConfigurations(credentials); }

}
