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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * Unit tests for {@link JerseyAuthService}.
 */
public class JerseyAuthServiceTest {

  private static final String SERVICE_URL = "service URL";
  private static final String AUTHORIZATION_URI = "authorization URI";
  private static final String JSON_SUBJECT = "JSON representation of subject";
  private static final String JSON_AUTH_RESULT = "JSON auth result";

  @Rule
  public final MockitoRule rule =
      MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock 
  private JerseyClientFactory clientFactory;

  @Mock
  private JsonMarshaller marshaller;

  @Mock
  private Client client;

  @Mock
  private WebTarget target;

  @Mock
  private Invocation.Builder request;

  @Mock
  private Response response;

  @Mock
  private AuthServiceConfig config;

  @Mock
  private AuthSubject subject;

  private JerseyAuthService service;

  @Before
  public void setUp() throws Exception {
    service = new JerseyAuthService(clientFactory, marshaller);
    when(clientFactory.newClient(same(config))).thenReturn(client);
    service.init(config);
    verify(clientFactory).newClient(same(config));
  }

  @Test
  public void testAuthorize() throws Exception {
    mockRequestFlow();

    when(request.post(any(Entity.class), same(String.class)))
        .thenReturn(JSON_AUTH_RESULT);

    final Map authResult = Collections.emptyMap();
    when(marshaller.toMap(JSON_AUTH_RESULT)).thenReturn(authResult);

    assertThat(service.authorize(subject)).isSameAs(authResult);
  }

  @Test(expected = GuacamoleServerException.class)
  public void testAuthorizeWhenClientError() throws Exception {
    mockRequestFlow();

    when(request.post(any(Entity.class), same(String.class)))
        .thenThrow(new ClientErrorException(400));

    service.authorize(subject);
  }

  @Test(expected = GuacamoleServerException.class)
  public void testAuthorizeWhenServerError() throws Exception {
    mockRequestFlow();

    when(request.post(any(Entity.class), same(String.class)))
        .thenThrow(new ServerErrorException(500));

    service.authorize(subject);
  }

  @Test(expected = GuacamoleServerException.class)
  public void testAuthorizeWhenOtherJerseyException() throws Exception {
    mockRequestFlow();

    when(request.post(any(Entity.class), same(String.class)))
        .thenThrow(new WebApplicationException());

    service.authorize(subject);
  }

  private void mockRequestFlow() throws GuacamoleException {
    when(config.getServiceUrl()).thenReturn(SERVICE_URL);
    when(config.getAuthorizationUri()).thenReturn(AUTHORIZATION_URI);
    when(marshaller.toJson(subject)).thenReturn(JSON_SUBJECT);
    when(client.target(SERVICE_URL)).thenReturn(target);
    when(target.path(AUTHORIZATION_URI)).thenReturn(target);
    when(target.request(MediaType.APPLICATION_JSON)).thenReturn(request);
  }

}
