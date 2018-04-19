package hello

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ByteArrayToHexAsciiTypeAdapter : JsonSerializer<ByteArray>, JsonDeserializer<ByteArray> {
	override fun deserialize (json: JsonElement , typeOfT : Type , context: JsonDeserializationContext ) : ByteArray {
		return json.getAsString()!!.fromHexString()
	}

	override fun serialize(src: ByteArray, typeOfSrc: Type , context: JsonSerializationContext ): JsonElement {
		return JsonPrimitive(src.toHexString())
	}
}