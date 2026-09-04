package foxtails.taeda.di

import coil3.ImageLoader
import com.github.awxkee.avifcoil.decoder.HeifDecoder

actual fun ImageLoader.Builder.addPlatformImageDecoders(): ImageLoader.Builder =
    components {
        add(HeifDecoder.Factory())
    }