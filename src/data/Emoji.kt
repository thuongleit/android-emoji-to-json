package data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

open class Emoji constructor(codePoints: Array<String>, vararg variants: Emoji?) : Serializable {

    @SerializedName("content")
    @Expose
    val unicode: String

    @Expose
    @SerializedName("codepoints")
    private val codePointsStr: String

    @Expose
    @SerializedName("since")
    var supportFrom: Int? = null

    private var variants: List<Emoji?>? = null

    @Expose
    var ignore = false
        private set

    init {
        val builder = StringBuilder()
        val code = IntArray(codePoints.size)
        for (i in codePoints.indices) {
            val codePoint = codePoints[i]
            val ox = codePoint.replace("0x", "")
            builder.append("0x").append(ox).append(",")
            code[i] = Integer.parseInt(ox, 16)
        }
        val codePointsInStr = builder.toString()
        this.codePointsStr = codePointsInStr.substring(0, codePointsInStr.lastIndexOf(","))
        this.unicode = String(code, 0, code.size)
        this.variants = variants.map { it }.toList()
    }

    constructor(codePoint: String, vararg variants: Emoji?) : this(arrayOf(codePoint), *variants)

    fun ignore(): Emoji {
        ignore = true
        return this
    }

    fun since(since: Int): Emoji {
        this.supportFrom = since
        return this
    }

    fun getVariants(): List<Emoji?>? {
        return variants
    }

    val length: Int
        get() = unicode.length

    fun hasVariants(): Boolean {
        return variants != null && !variants!!.isEmpty()
    }

    fun supportFrom(supportFrom: Int = 16) : Emoji{
        this.supportFrom = supportFrom
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val emoji = other as Emoji?

        return unicode == emoji!!.unicode && variants == emoji.variants
    }

    override fun hashCode(): Int {
        var result = unicode.hashCode()
        result = 31 * result + variants!!.hashCode()
        return result
    }

    override fun toString(): String {
        val text = unicode
        var codePoints: Long = 0
        var i = 0
        while (i < text.length) {
            val codePoint = text.codePointAt(i)
            //        Skip over the second char in a surrogate pair
            if (codePoint > 0xffff) {
                i++
            }
            codePoints += codePoint.toLong()
            i++
        }
        return "0x" + java.lang.Long.toHexString(codePoints)
    }

    companion object {
        private const val serialVersionUID = 3L
    }
}
