package com.telephoto

import android.annotation.SuppressLint
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.geometry.Offset
import com.margelo.nitro.telephoto.Offset as NitroOffset
import androidx.compose.ui.platform.ComposeView
import com.facebook.react.uimanager.PixelUtil.dpToPx
import com.facebook.react.uimanager.PixelUtil.pxToDp
import com.facebook.react.uimanager.ThemedReactContext
import kotlinx.coroutines.channels.Channel
import me.saket.telephoto.zoomable.OverzoomEffect
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.glide.ZoomableGlideImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState

@SuppressLint("ViewConstructor")
class TelephotoView(context: ThemedReactContext) : FrameLayout(context) {
    private val composeView: ComposeView = ComposeView(context)
    var source by mutableStateOf<String?>(null)
    var telephotoContentDescription by mutableStateOf("")
    var alignment by mutableStateOf(Alignment.Center)
    var contentScale by mutableStateOf(ContentScale.Fit)
    var onClick: ((NitroOffset) -> Unit)? = null
    var onLongClick: ((NitroOffset) -> Unit)? = null
    var minZoomFactor by mutableFloatStateOf(1f)
    var maxZoomFactor by mutableFloatStateOf(4f)
    var overzoomEffect by mutableStateOf(OverzoomEffect.RubberBanding)
    var onZoomFractionChanged: ((Double?) -> Unit)? = null

    // Use a channel to communicate zoom requests
    private val zoomChannel = Channel<ZoomRequest>(Channel.UNLIMITED)

    sealed class ZoomRequest {
        data class ZoomTo(val factor: Float, val centroid: Offset) : ZoomRequest()
        data class ZoomBy(val factor: Float, val centroid: Offset) : ZoomRequest()
        object ZoomReset : ZoomRequest()
    }

    @Composable
    fun Telephoto() {
        val zoomableState = rememberZoomableState(
            zoomSpec = ZoomSpec(
                minZoomFactor = minZoomFactor,
                maxZoomFactor = maxZoomFactor,
                overzoomEffect = overzoomEffect
            )
        )

        // Process zoom requests
        LaunchedEffect(Unit) {
            for (request in zoomChannel) {
                when (request) {
                    is ZoomRequest.ZoomTo -> zoomableState.zoomTo(request.factor, request.centroid)
                    is ZoomRequest.ZoomBy -> zoomableState.zoomBy(request.factor, request.centroid)
                    is ZoomRequest.ZoomReset -> zoomableState.resetZoom()
                }
            }
        }

        LaunchedEffect(zoomableState) {
            snapshotFlow { zoomableState.zoomFraction }
                .collect { fraction ->
                    onZoomFractionChanged?.invoke(fraction?.toDouble())
                }
        }

        ZoomableGlideImage(
            state = rememberZoomableImageState(zoomableState),
            model = source,
            contentDescription = telephotoContentDescription,
            modifier = Modifier.fillMaxSize(),
            alignment = alignment,
            contentScale = contentScale,
            onClick = {
                onClick?.invoke(NitroOffset(it.x.toDouble(), it.y.toDouble()))
            },
            onLongClick = {
                onLongClick?.invoke(NitroOffset(it.x.toDouble(), it.y.toDouble()))
            },
        )
        //{
            //it.thumbnail().transition(
            //    withCrossFade()
            //)
        //}
    }

    fun zoomTo(zoomFactor: Float, centroid: Offset) {
        Log.i("ZoomTo 1", centroid.x.dpToPx().toString())
        Log.i("ZoomTo2 ", centroid.x.pxToDp().toString())
        zoomChannel.trySend(ZoomRequest.ZoomTo(zoomFactor, centroid))
    }

    fun zoomBy(zoomFactor: Float, centroid: Offset) {
        zoomChannel.trySend(ZoomRequest.ZoomBy(zoomFactor, centroid))
    }

    fun resetZoom() {
        zoomChannel.trySend(ZoomRequest.ZoomReset)
    }

    init {
        composeView.setContent {
            Telephoto()
        }
        addView(composeView)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        zoomChannel.close()
    }
}