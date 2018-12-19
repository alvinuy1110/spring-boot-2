package com.myproject.springboot.jpa.util;

import java.io.IOException;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Created by user on 18/07/18.
 */
public class SortJacksonDeserializer extends JsonDeserializer<Sort> {

    @Override
    public Sort deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode sortNode = jp.getCodec().readTree(jp);

        if (sortNode != null) {
            ArrayNode arrayNode = (ArrayNode) sortNode;
            Sort.Order[] orders = new Sort.Order[arrayNode.size()];
            int i = 0;
            for (JsonNode obj : sortNode) {
                Sort.NullHandling nullHandling = getNullHandling(obj);

                String direction = getTextNodeSafely(obj, "direction");
                String property = getTextNodeSafely(obj, "property");
                if (StringUtils.hasText(direction) && StringUtils.hasText(property) && nullHandling != null) {
                    Sort.Order sortOrder = new Sort.Order(Sort.Direction.valueOf(direction), property, nullHandling);

                    boolean ignoreCase = getBooleanNodeSafely(obj, "ignoreCase");
                    if (ignoreCase) {
                        sortOrder = sortOrder.ignoreCase();
                    }

                    orders[i] = sortOrder;
                    i++;
                }

            }
            if (orders.length > 0) {
                return new Sort(orders);
            }
        }
        return null;
    }

    protected boolean getBooleanNodeSafely(JsonNode obj, String name) {
        JsonNode jsonNode = obj.get(name);
        if (jsonNode != null) {
            return jsonNode.booleanValue();
        }
        return false;
    }

    protected String getTextNodeSafely(JsonNode obj, String name) {
        JsonNode jsonNode = obj.get(name);
        if (jsonNode != null) {
            return jsonNode.textValue();
        }
        return null;
    }

    protected Sort.NullHandling getNullHandling(JsonNode obj) {
        JsonNode nullHandlingNode = obj.get("nullHandling");
        String nullHandlingText = null;
        if (nullHandlingNode != null) {
            nullHandlingText = nullHandlingNode.asText();
        }
        if (nullHandlingText != null) {
            try {
                return Sort.NullHandling.valueOf(nullHandlingText);
            }
            catch (IllegalArgumentException iae) {
                return null;
            }
        }
        return null;
    }
}
