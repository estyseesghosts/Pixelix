package foxtails.taeda.domain.model

data class Lens(
    override val id: String,
    val name: String,
    val amount: Int
): Identifiable
