package com.daniebeler.pfpixelix

import com.daniebeler.pfpixelix.domain.service.general.BackendType
import com.daniebeler.pfpixelix.domain.service.general.toCapabilities
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyMiAuthCheckResponse
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyMiAuthUser
import com.daniebeler.pfpixelix.domain.service.sharkey.model.toCredentials
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SharkeyMiAuthTest {

    @Test
    fun verifiedMiAuthSessionCreatesSharkeyCredentials() {
        val credentials = SharkeyMiAuthCheckResponse(
            ok = true,
            token = "token",
            user = SharkeyMiAuthUser("user-id", "shark", "Shark", "https://example.com/avatar.png")
        ).toCredentials("https://example.com/", "now")

        assertEquals(BackendType.SHARKEY, credentials.backendType)
        assertEquals("user-id", credentials.accountId)
        assertEquals("Shark", credentials.displayName)
        assertEquals("token", credentials.token)
    }

    @Test
    fun incompleteOrRejectedMiAuthResponsesAreNotPersisted() {
        assertFailsWith<IllegalArgumentException> {
            SharkeyMiAuthCheckResponse(ok = false).toCredentials("https://example.com/", "now")
        }
        assertFailsWith<IllegalStateException> {
            SharkeyMiAuthCheckResponse(ok = true, user = SharkeyMiAuthUser(id = "id", username = "name"))
                .toCredentials("https://example.com/", "now")
        }
    }

    @Test
    fun sharkeyCapabilitiesEnableDirectMessagesAfterTheyAreImplemented() {
        assertTrue(BackendType.SHARKEY.toCapabilities().general.supportsDMs)
        assertTrue(BackendType.SHARKEY.toCapabilities().newPost.includeDirectVisibility)
    }
}