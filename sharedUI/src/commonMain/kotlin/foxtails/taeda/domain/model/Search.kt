package foxtails.taeda.domain.model

data class Search(
    val accounts: List<Account>,
    val posts: List<Post>,
    val tags: List<Tag>
)