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


import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class JsonTest {

  @Test
  @SuppressWarnings("unchecked")
  public void testReadJson() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = mapper.readValue(json(),
        new TypeReference<Map<String, Object>>(){});
    assertThat(map.keySet()).contains("foo");
    assertThat(map.get("foo")).isInstanceOf(Map.class);
    Map<String, Object> foo = (Map<String, Object>) map.get("foo");
    assertThat(foo.keySet()).contains("protocol");
    assertThat(foo.get("protocol")).isEqualTo("SSH");
    assertThat(foo.keySet()).contains("parameters");
    Map<String, Object> parameters = (Map<String, Object>) foo.get("parameters");
    assertThat(parameters.get("hostname")).isEqualTo("some hostname");
    assertThat(parameters.get("port")).isEqualTo(22);
    System.out.println("I ran");
  }

  @Test
  public void testWriteJson() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("username", "bob");
    map.put("remoteAddress", "10.0.0.1");
    map.put("remoteHostname", "client.soulwing.org");

    String json = mapper.writeValueAsString(map);
    map = mapper.readValue(json,
        new TypeReference<Map<String, Object>>(){});

    assertThat(map.get("username")).isEqualTo("bob");
    assertThat(map.get("remoteAddress")).isEqualTo("10.0.0.1");
    assertThat(map.get("remoteHostname")).isEqualTo("client.soulwing.org");
    assertThat(true).isTrue();

  }


  private String json() {
    return "{\n" +
        "  \"foo\": {\n" +
        "    \"protocol\": \"SSH\",\n" +
        "    \"parameters\": {\n" +
        "      \"hostname\": \"some hostname\",\n" +
        "      \"port\": 22\n" +
        "    }\n" +
        "  }\n" +
        "}";
  }

}
