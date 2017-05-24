package data

import com.google.gson.annotations.Expose

data class Category(@Expose val name: String, @Expose var icon: String = "", @Expose val emojis: List<Emoji>) {
    init {
        when (name) {
            //"recent"  -> "🕒" "U+233b"
            "people"    -> icon = "☺"
            "nature"    -> icon = "🐹"
            "food"      -> icon = "🍓"
            "activity"  -> icon = "⚽"
            "place"     -> icon = "🚓"
            "object"    -> icon = "💡"
            "symbol"    -> icon = "💖"
            "flag"      -> icon = "🚩"
        }
    }
}
