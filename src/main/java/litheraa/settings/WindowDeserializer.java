package litheraa.settings;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;


import java.awt.Dimension;
import java.io.IOException;

public class WindowDeserializer extends JsonDeserializer<WindowSettings> {
	@Override
	public WindowSettings deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		JsonNode dimensionNode = node.get("minSize");
		Dimension dimension = new Dimension(dimensionNode.get("width").asInt(),
				dimensionNode.get("height").asInt());

		if (node.has("x") && node.has("y")) {
			return new WindowSettings(
					node.get("x").asInt(),
					node.get("y").asInt(),
					node.get("width").asInt(),
					node.get("height").asInt(),
					dimension,
					node.get("onTop").asBoolean(),
					node.get("resizable").asBoolean(),
					node.get("tray").asBoolean());
		} else
			return new WindowSettings(
					node.get("width").asInt(),
					node.get("height").asInt(),
					dimension,
					node.get("onTop").asBoolean(),
					node.get("resizable").asBoolean(),
					node.get("tray").asBoolean());
	}
}
