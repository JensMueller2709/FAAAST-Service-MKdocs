/*
 * Copyright (c) 2021 Fraunhofer IOSB, eine rechtlich nicht selbstaendige
 * Einrichtung der Fraunhofer-Gesellschaft zur Foerderung der angewandten
 * Forschung e.V.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fraunhofer.iosb.ilt.faaast.service.endpoint.http.serialization;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.fraunhofer.iosb.ilt.faaast.service.dataformat.json.JsonSerializer;
import de.fraunhofer.iosb.ilt.faaast.service.endpoint.http.serialization.mixins.InvokeOperationRequestMixin;
import de.fraunhofer.iosb.ilt.faaast.service.model.request.InvokeOperationRequest;


/**
 * JSON serializer including HTTP specific functionality
 */
public class HttpJsonSerializer extends JsonSerializer {

    @Override
    protected void modifyMapper(JsonMapper mapper) {
        super.modifyMapper(mapper);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        mapper.addMixIn(InvokeOperationRequest.class, InvokeOperationRequestMixin.class);
    }
}
