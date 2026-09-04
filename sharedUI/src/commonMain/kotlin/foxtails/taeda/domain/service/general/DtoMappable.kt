package foxtails.taeda.domain.service.general

interface DtoMappable<T> {
    fun toDomain(): T
}