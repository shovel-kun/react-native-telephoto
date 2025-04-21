package com.telephoto

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.uimanager.ThemedReactContext
import com.margelo.nitro.telephoto.HybridTelephotoSpec
import com.margelo.nitro.telephoto.Offset
import com.margelo.nitro.telephoto.Alignment as NitroAlignment
import com.margelo.nitro.telephoto.ContentScale as NitroContentScale

@Keep
@DoNotStrip
class HybridTelephoto(val context: ThemedReactContext): HybridTelephotoSpec() {
    // View
    override val view = TelephotoView(context)

    // Props
    var _source: String = ""
    override var source: String
        get() = _source
        set(value) {
            if (_source == value || value.isEmpty()) return
            _source = value
            view.source = value
        }

    override var contentDescription: String? = null
        set(value) {
            if (field == value) return
            field = value
            value?.let { view.telephotoContentDescription = it }
        }

    override var alignment: NitroAlignment? = null
        set(value) {
            if (field == value) return
            field = value
            value?.let {
                when (value) {
                    NitroAlignment.TOP_START -> view.alignment = Alignment.TopStart
                    NitroAlignment.TOP_CENTER -> view.alignment = Alignment.TopCenter
                    NitroAlignment.TOP_END -> view.alignment = Alignment.TopEnd
                    NitroAlignment.CENTER_START -> view.alignment = Alignment.CenterStart
                    NitroAlignment.CENTER -> view.alignment = Alignment.Center
                    NitroAlignment.CENTER_END -> view.alignment = Alignment.CenterEnd
                    NitroAlignment.BOTTOM_START -> view.alignment = Alignment.BottomStart
                    NitroAlignment.BOTTOM_CENTER -> view.alignment = Alignment.BottomCenter
                    NitroAlignment.BOTTOM_END -> view.alignment = Alignment.BottomEnd
                }
            }
        }

    override var contentScale: NitroContentScale? = null
        set(value) {
            if (field == value) return
            field = value
            value?.let {
                when (it) {
                    NitroContentScale.CROP -> view.contentScale = ContentScale.Crop
                    NitroContentScale.FIT -> view.contentScale = ContentScale.Fit
                    NitroContentScale.FILL_HEIGHT -> view.contentScale = ContentScale.FillHeight
                    NitroContentScale.FILL_WIDTH -> view.contentScale = ContentScale.FillWidth
                    NitroContentScale.INSIDE -> view.contentScale = ContentScale.Inside
                    NitroContentScale.NONE -> view.contentScale = ContentScale.None
                    NitroContentScale.FILL_BOUNDS -> view.contentScale = ContentScale.FillBounds
                }
            }
        }

    override var minZoomFactor: Double? = null
        set(value) {
            if (field == value) return
            field = value
            value?.let { view.minZoomFactor = it.toFloat() }
        }

    override var maxZoomFactor: Double? = null
        set(value) {
            if (field == value) return
            field = value
            value?.let { view.maxZoomFactor = it.toFloat() }
        }

    override var onPress: ((Offset) -> Unit)? = null
        set(value) {
            if (field == value) return
            field = value
            view.onClick = value
        }

    override var onLongPress: ((Offset) -> Unit)? = null
        set(value) {
            if (field == value) return
            field = value
            view.onLongClick = value
        }
}
