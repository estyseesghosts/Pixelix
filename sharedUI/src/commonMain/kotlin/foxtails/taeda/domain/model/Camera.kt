package foxtails.taeda.domain.model

data class Camera(
    override val id: String,
    val name: String,
    val make: String?,
    val model: String?,
    val amount: Int
): Identifiable
