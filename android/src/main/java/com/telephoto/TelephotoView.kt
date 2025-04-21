package com.telephoto

import android.annotation.SuppressLint
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.geometry.Offset
import com.margelo.nitro.telephoto.Offset as NitroOffset
import androidx.compose.ui.platform.ComposeView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.facebook.react.uimanager.ThemedReactContext
import me.saket.telephoto.zoomable.OverzoomEffect
import me.saket.telephoto.zoomable.ZoomSpec
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
    var maxZoomFactor by mutableFloatStateOf(2f)
    var overzoomEffect by mutableStateOf(OverzoomEffect.RubberBanding)
    // var transitionMilliSeconds by mutableStateOf(250)

    @Composable
    fun Telephoto() {
        val zoomableState = rememberZoomableState(
            zoomSpec = ZoomSpec(
                minZoomFactor = minZoomFactor,
                maxZoomFactor = maxZoomFactor,
                overzoomEffect = overzoomEffect
            )
        )

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

    init {
        composeView.setContent {
            Telephoto()
        }
        addView(composeView)
    }
}