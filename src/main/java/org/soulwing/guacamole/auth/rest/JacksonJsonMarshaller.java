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

import java.io.IOException;
import java.util.Map;

import org.apache.guacamole.GuacamoleException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * A {@link JsonMarshaller} implemented using a Jackson
 * {@link org.codehaus.jackson.map.ObjectMapper}.
 */
class JacksonJsonMarshaller implements JsonMarshaller {

  @Override
  public String toJson(AuthSubject subject) throws GuacamoleException {
    final ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(subject);
    }
    catch (IOException ex) {
      throw new GuacamoleException(ex);
    }
  }

  @Override
  public Map toMap(String json) throws GuacamoleException {
    final ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(json,
          new TypeReference<Map<String, Object>>(){});
    }
    catch (IOException ex) {
      throw new GuacamoleException(ex);
    }
  }

}
