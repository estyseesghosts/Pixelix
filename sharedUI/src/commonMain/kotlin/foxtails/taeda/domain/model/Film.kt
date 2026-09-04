package foxtails.taeda.domain.model

data class Film(
    override val id: String,
    val name: String,
    val amount: Int
): Identifiable
