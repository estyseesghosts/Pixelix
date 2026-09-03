package com.daniebeler.pfpixelix.domain.service.capabilities

val SharkeyCapabilities = Capabilities(
    general = GeneralCapabilities(supportsDMs = true),
    post = PostCapabilities(
        showCameraMetadata = false,
        showLikedBy = false
    ),
    profile = ProfileCapabilities(
        showCollectionsOwnProfile = false,
        showAdvancedMuteOptions = false,
        showRepostSettings = false,
        blockReason = false
    ),
    notification = NotificationCapabilities(
        supportsFollowRequestActions = false
    ),
    editProfile = EditProfileCapabilities(
        privateAccountToggle = true,
        manuallyAcceptNewFollowersToggle = true,
        includePostsInSearchEngineToggle = false,
        includeProfileInSearchEngineToggle = false,
        websiteField = false,
        headerImage = true,
        includeFields = false
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