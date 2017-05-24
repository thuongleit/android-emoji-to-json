import com.google.gson.*
import data.Emoji
import java.lang.reflect.Type

class EmojiSerializer : JsonSerializer<Emoji> {

    override fun serialize(emoji: Emoji?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
        val jObj = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJsonTree(emoji) as JsonObject

        emoji ?: return jObj

        serialize(emoji, jObj)

        if (emoji.hasVariants()) {
            jObj
                    .getAsJsonArray("variants")
                    ?.also { jArray ->
                        (0 until jArray.size())
                                .forEach { index ->
                                    serialize(emoji.getVariants()?.get(index)!!, jArray.get(index) as JsonObject)
                                }
                    }
        }

        return jObj
    }

    private fun serialize(emoji: Emoji, jObj: JsonObject) {
        if (!emoji.hasVariants()) {
            jObj.remove("variants")
        }

        if (!emoji.ignore) {
            jObj.remove("ignore")
        }

        if (emoji.supportFrom == 16) {
            jObj.remove("since")
        }
    }
}