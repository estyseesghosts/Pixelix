package foxtails.taeda

import foxtails.taeda.domain.service.general.BackendType
import foxtails.taeda.ui.composables.settings.preferences.prefs.moreSettingsPath
import kotlin.test.Test
import kotlin.test.assertEquals

class MoreSettingsPathTest {

    @Test
    fun mastodonMoreSettingsOpensProfileSettings() {
        assertEquals("settings/profile", BackendType.MASTODON.moreSettingsPath())
    }

    @Test
    fun sharkeyMoreSettingsOpensProfileSettings() {
        assertEquals("settings/profile", BackendType.SHARKEY.moreSettingsPath())
    }
}