package com.daniebeler.pfpixelix.domain.service.capabilities

val MastodonCapabilities = Capabilities(
    general = GeneralCapabilities(supportsDMs = false),
    post = PostCapabilities(
        showCameraMetadata = false,
        showLikedBy = true
    ),
    profile = ProfileCapabilities(
        showCollectionsOwnProfile = false,
        showAdvancedMuteOptions = true,
        showRepostSettings = false,
        blockReason = false
    ),
    notification = NotificationCapabilities(
        supportsFollowRequestActions = true
    ),
    editProfile = EditProfileCapabilities(
        privateAccountToggle = true,
        manuallyAcceptNewFollowersToggle = false,
        includePostsInSearchEngineToggle = false,
        includeProfileInSearchEngineToggle = false,
        websiteField = false,
        headerImage = true,
        includeFields = true
    ),
    trending = TrendingCapabilities(
        supportsMultipleProfileTimeRanges = false,
        supportsMultipleHashtagTimeRanges = false,
        supportsAdvancedCategories = false
    ),
    newPost = NewPostCapabilities(
        supportsAdvancedMediaMetadata = false,
        includeDirectVisibility = true,
        showCountryDropdown = false,
        showLocationInputInGeneral = false,
        showLocationInputInImageTab = false,
        showMetadata = false,
        showCategoriesDropdown = false,
        supportLicenses = false,
    )
)