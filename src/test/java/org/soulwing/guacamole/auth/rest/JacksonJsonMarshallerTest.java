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
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * Unit tests for {@link JacksonJsonMarshaller}.
 */
public class JacksonJsonMarshallerTest {

  private static final String HEADER_NAME = "headerName";
  private static final String HEADER_VALUE = "headerValue";

  @Rule
  public final MockitoRule rule =
      MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock
  private HttpServletRequest request;

  private JacksonJsonMarshaller marshaller = new JacksonJsonMarshaller();

  @Test
  @SuppressWarnings("unchecked")
  public void testRoundTrip() throws Exception {
    when(request.getHeaderNames()).thenReturn(
        Collections.enumeration(Collections.singletonList(HEADER_NAME)));
    when(request.getHeaders(HEADER_NAME)).thenReturn(
        Collections.enumeration(Collections.singletonList(HEADER_VALUE)));

    final AuthSubject subject = AuthSubjectUtil.newAuthSubject(request);
    final String json = marshaller.toJson(subject);
    System.out.println(json);
    final Map map = marshaller.toMap(json);

    assertThat(map.get("username")).isEqualTo(AuthSubjectUtil.USERNAME);
    assertThat(map.get("password")).isEqualTo(AuthSubjectUtil.PASSWORD);
    assertThat(map.get("remoteAddress")).isEqualTo(AuthSubjectUtil.REMOTE_ADDRESS);
    assertThat(map.get("remoteHostname")).isEqualTo(AuthSubjectUtil.REMOTE_HOSTNAME);

    assertThat(map.get("headers")).isInstanceOf(Map.class);
    final Map<String, List<String>> headers = (Map) map.get("headers");
    assertThat(headers.get(HEADER_NAME)).isInstanceOf(List.class);
    final List<String> values = headers.get(HEADER_NAME);
    assertThat(values).containsExactly(HEADER_VALUE);
  }


}
