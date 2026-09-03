package com.daniebeler.pfpixelix.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlatformImageDecodersTest {

    @Test
    fun decodesAvifFixture() = runBlocking {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val fixture = instrumentation.context.assets.open("white_1x1.avif").use { it.readBytes() }
        val imageLoader = ImageLoader.Builder(context).addPlatformImageDecoders().build()

        val result = imageLoader.execute(
            ImageRequest.Builder(context)
                .data(fixture)
                .build()
        )

        assertTrue(result is SuccessResult)
        assertTrue((result as SuccessResult).image.width > 0)
        assertTrue(result.image.height > 0)
    }
}