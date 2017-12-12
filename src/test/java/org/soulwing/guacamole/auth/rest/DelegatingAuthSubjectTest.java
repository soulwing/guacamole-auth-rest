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
import static org.mockito.Mockito.when;

import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

import org.apache.guacamole.net.auth.Credentials;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * Unit tests for {@link DelegatingAuthSubject}.
 */
public class DelegatingAuthSubjectTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String REMOTE_ADDRESS = "remoteAddress";
  private static final String REMOTE_HOSTNAME = "remoteHostname";

  private static final String HEADER_NAME = "headerName";
  private static final String HEADER_VALUE = "headerValue";

  @Rule
  public final MockitoRule rule =
      MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock
  private HttpServletRequest request;

  private Credentials credentials = new Credentials();

  private DelegatingAuthSubject subject = new DelegatingAuthSubject(credentials);

  @Test
  public void testSimpleProperties() throws Exception {
    credentials.setUsername(USERNAME);
    credentials.setPassword(PASSWORD);
    credentials.setRemoteAddress(REMOTE_ADDRESS);
    credentials.setRemoteHostname(REMOTE_HOSTNAME);

    assertThat(subject.getUsername()).isEqualTo(USERNAME);
    assertThat(subject.getPassword()).isEqualTo(PASSWORD);
    assertThat(subject.getRemoteAddress()).isEqualTo(REMOTE_ADDRESS);
    assertThat(subject.getRemoteHostname()).isEqualTo(REMOTE_HOSTNAME);
  }

  @Test
  public void testHeadersProperty() throws Exception {
    credentials.setRequest(request);
    when(request.getHeaderNames()).thenReturn(
        Collections.enumeration(Collections.singletonList(HEADER_NAME)));
    when(request.getHeaders(HEADER_NAME)).thenReturn(
        Collections.enumeration(Collections.singletonList(HEADER_VALUE)));

    assertThat(subject.getHeaders()).containsKey(HEADER_NAME);
    assertThat(subject.getHeaders().get(HEADER_NAME)).containsExactly(HEADER_VALUE);
  }

}
