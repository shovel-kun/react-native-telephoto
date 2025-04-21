package com.telephoto

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.geometry.Offset
import com.margelo.nitro.telephoto.Offset as NitroOffset
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.facebook.react.uimanager.BackgroundStyleApplicator
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


    // Code taken from
    // https://github.com/expo/expo/blob/26cbac0e18f851fe07617a34f862416f756fd74c/packages/expo-modules-core/android/src/main/java/expo/modules/kotlin/views/ExpoView.kt#L4
    // https://github.com/expo/expo/blob/26cbac0e18f851fe07617a34f862416f756fd74c/packages/expo-modules-core/android/src/main/java/expo/modules/kotlin/views/ExpoComposeView.kt#L14

    open val shouldUseAndroidLayout: Boolean = true

    /**
     * Manually trigger measure and layout.
     * If [shouldUseAndroidLayout] is set to `true`, this method will be called automatically after [requestLayout].
     */
    @UiThread
    fun measureAndLayout() {
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
        layout(left, top, right, bottom)
    }

    override fun requestLayout() {
        super.requestLayout()
        if (shouldUseAndroidLayout) {
            // We need to force measure and layout, because React Native doesn't do it for us.
            post(Runnable { measureAndLayout() })
        }
    }

    open fun clipToPaddingBox(canvas: Canvas) {
        // When the border radius is set, we need to clip the content to the padding box.
        // This is because the border radius is applied to the background drawable, not the view itself.
        // It is the same behavior as in React Native.
        if (!clipToPadding) {
            return
        }
        BackgroundStyleApplicator.clipToPaddingBox(this, canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        clipToPaddingBox(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // In case of issues there's an alternative solution in previous commits at https://github.com/expo/expo/pull/33759
        if (!isAttachedToWindow) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @OptIn(InternalComposeUiApi::class)
    val layout by lazy {
        ComposeView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            it.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            addView(it)
            it.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    it.disposeComposition()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            })
        }
    }

    fun setContent(
        content: @Composable () -> Unit
    ) {
        layout.setContent { content() }
    }
}