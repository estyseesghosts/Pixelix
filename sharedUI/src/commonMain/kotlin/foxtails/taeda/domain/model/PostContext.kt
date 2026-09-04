package foxtails.taeda.domain.model

data class PostContext(
    val ancestors: List<Post>,
    val descendants: List<Post>
)