package com.daniebeler.pfpixelix.domain.model

import com.daniebeler.pfpixelix.domain.service.pixelfed.model.PixelfedVisibilityDto

enum class Visibility {
    PUBLIC,
    UNLISTED,
    PRIVATE,
    DIRECT
}


fun Visibility.toPixelfed(): PixelfedVisibilityDto = when (this) {
    Visibility.PUBLIC -> PixelfedVisibilityDto.PUBLIC
    Visibility.UNLISTED -> PixelfedVisibilityDto.UNLISTED
    Visibility.PRIVATE -> PixelfedVisibilityDto.PRIVATE
    Visibility.DIRECT -> PixelfedVisibilityDto.DIRECT
}