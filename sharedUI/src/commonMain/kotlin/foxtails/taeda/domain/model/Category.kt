package foxtails.taeda.domain.model

data class Category(
    override val id: String,
    val name: String,
    val isEnabled: Boolean?,
    val priority: Int?
): Identifiable
