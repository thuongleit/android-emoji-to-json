import com.google.gson.GsonBuilder
import data.Category
import data.EMOJI_DATA
import data.Emoji
import java.io.File
import java.io.FileFilter

/*
fun getEmojiFiles(): List<File> {
    val resFolder = File("res")
    if (!resFolder.isDirectory) {
        print("Not a valid folder")
        return emptyList()
    }

    return resFolder
            .listFiles()
            .filter { it.absolutePath.contains("_emojis.txt") }
            .toList()
}
*/

/*
fun File.getEmojis(): Pair<String, List<String>> { //map of <data type, emojis of support emojis>
    val lastIndexOf = absolutePath.lastIndexOf("res/")
    val lastIndexOf1 = absolutePath.lastIndexOf("_emojis")
    val emojiType = absolutePath.substring(lastIndexOf + "res/".length, lastIndexOf1)

    return Pair(emojiType.trim(), readLines())
}
*/

fun getUnsupportFiles(): Map<Int, File> { //map of <support sdk, file>
    val resFolder = File("res")
    if (!resFolder.isDirectory) {
        print("Not a valid folder")
        return emptyMap()
    }

    return resFolder
            .listFiles(FileFilter { it.absolutePath.contains("unsupport") })
            .map {
                val lastIndexOf = it.absolutePath.lastIndexOf("sdk_")
                val lastIndexOf1 = it.absolutePath.lastIndexOf(".")

                val sdk = it.absolutePath.substring(lastIndexOf + "sdk_".length, lastIndexOf1).toInt()

                sdk to it
            }
            .toMap()
}

fun File.getUnsupportUnicodes(): Map<String, List<String>> { //map of <data type, emojis of unsupport emojis>
    return readLines()
            .map { it.split(":") }
            .map {
                it[0].trim() to it[1].split(",").map(String::trim).filter { "" != it }.toList()
            }
            .toMap()
}

fun main(args: Array<String>) {
/*    val emojisDataMap = getEmojiFiles()
            .emojiCategories(File::getEmojis)
            .emojiCategories { it.first to it.second }
            .toMap()*/


    val unsupportEmojisMap = getUnsupportFiles()
            .map { it ->
                it.key to it.value.getUnsupportUnicodes()
            }
            .toMap()

    val supportEmojiMap = unsupportEmojisMap
            .map { unsupportEmojisPair ->
                val supportEmojisPerSdk = EMOJI_DATA
                        .map { emojiMap ->
                            val supportEmojis = emojiMap
                                    .value
                                    .filter { emoji ->
                                        var isSupport = true
                                        for ((key, value) in unsupportEmojisPair.value) {
                                            if (key == emojiMap.key) {
                                                for (unsupportEmoji in value) {
                                                    if (emoji.toString().contains(Regex(".*($unsupportEmoji)+.*"))) {
                                                        println("unsupport from $key is $emoji");
                                                        isSupport = false
                                                        break
                                                    }
                                                }
                                            }
                                        }

                                        return@filter isSupport
                                    }

                            return@map emojiMap.key to supportEmojis
//                                    .filter { emojiPair ->
//                                        val isSupport = false
//                                        for ((key, value) in unsupportEmojisMap) {
//                                        }

//                                        return@filter isSupport
//                                        unsupportEmojisPair
//                                                .value
//                                                .filter { it.key == emojiMap.key }
//                                                .flatMap { it.value }
//                                                .emojiCategories { !(emojiPair.toString().contains(Regex(".*($it)+."))) }
//                                    }
                        }
                        .toMap()

                return@map unsupportEmojisPair.key to supportEmojisPerSdk
            }
            .toMap()

    val maxSDK = supportEmojiMap.keys.max()!! //the latest sdk
    val minSDK = supportEmojiMap.keys.min()!!

    val emojiCategories = supportEmojiMap[maxSDK]
            ?.map { emojisDataMap ->
                val list = emojisDataMap
                        .value
                        .map { emoji ->
                            for (sdk in minSDK..maxSDK) {
                                val count = supportEmojiMap[sdk]
                                        ?.filter { it.key == emojisDataMap.key }
                                        ?.flatMap { it.value }
                                        ?.filter { it.unicode == emoji.unicode }
                                        ?.count()

                                if (count != null) {
                                    if (count >= 1) {
                                        if (emoji.supportFrom == null) {
                                            emoji.supportFrom = sdk
                                        }
                                        break
                                    }
                                }
                            }

                            return@map emoji
                        }

                return@map Category(name = emojisDataMap.key, emojis = list)
//                return@emojiCategories emojisDataMap.key to emojis
            }?.toList()

    val gson = GsonBuilder()
            .registerTypeAdapter(Emoji::class.java, EmojiSerializer())
            .setPrettyPrinting()
            .create()
    val toJson = gson.toJson(emojiCategories)
    println(toJson)

    val file = File("output/emojis.json")
    if (!file.exists()) {
        file.createNewFile()
    }

    file.writeText(toJson!!)
//    println(emojiCategories)

//                val toMap = emojisDataMap
//                        .emojiCategories { emojiMap ->
//
//                            val supportEmojiMap = emojiMap
//                                    .value
//                                    .toMutableList()
//                                    .also {
//                                        val iterator = it.iterator()
//                                        if (iterator.hasNext()) {
//                                            do {
//                                                val emoji = iterator.next()
//                                                unsupportEmojisMap[unsupportEmojisPair]
//                                                        ?.filter { it.key == emojiMap.key }
//                                                        ?.flatMap { it.value }
//                                                        ?.forEach {
//                                                            if (emoji.contains(Regex(".*($it)+."))) {
//                                                                iterator.remove()
//                                                            }
//                                                        }
//                                            } while (iterator.hasNext())
//                                        }
//                                    }
//                                    .supportEmojiMap()
//
//                            return@emojiCategories emojiMap.key to supportEmojiMap
//                        }
//                        .toMap()
//
//                return@emojiCategories unsupportEmojisPair to toMap
//            }
//            .supportEmojiMap()

}