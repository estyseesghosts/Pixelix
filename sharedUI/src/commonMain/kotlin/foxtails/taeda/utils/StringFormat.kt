package foxtails.taeda.utils

object StringFormat {
    fun groupDigits(number: Int?): String =
        (number ?: 0).toString().reversed().chunked(3).joinToString(" ").reversed()
}