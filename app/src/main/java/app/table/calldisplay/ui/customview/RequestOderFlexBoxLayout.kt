package app.table.calldisplay.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import app.table.calldisplay.databinding.ItemRequestOrderBinding
import app.table.calldisplay.ui.home.model.RequestOrder
import com.google.android.flexbox.FlexboxLayout
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class RequestOderFlexBoxLayout : FlexboxLayout {
    companion object {
        private const val THREAD_DISTANCE_CANCEL_LONG_CLICK: Float = 5F
        private const val HAS_MORE_ONE_ITEM: Int = 2
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var timerLongClick: Timer? = null

    internal var onItemLongClicked: (requestOder: RequestOrder) -> Unit = {}
    internal var onDragListener: (RequestOrder) -> Unit = {}

    internal fun submitList(list: List<RequestOrder>) {
        removeAllViews()

        list.forEach { requestOder ->
            val itemView: ItemRequestOrderBinding = ItemRequestOrderBinding.inflate(
                LayoutInflater.from(context), this, false
            ).apply {
                data = requestOder
            }
            val newParams = LayoutParams(itemView.root.layoutParams as? LayoutParams)

            if (list.size >= HAS_MORE_ONE_ITEM) {
                newParams.run {
                    flexGrow = 1F
                    width = LayoutParams.MATCH_PARENT
                    height = LayoutParams.MATCH_PARENT
                }
            }
            setSwipeToDeleted(itemView.root, requestOder)
            addView(itemView.root, newParams)
        }
    }

    private fun setSwipeToDeleted(view: View, requestOder: RequestOrder) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val currentX = view.x
            val currentY = view.y
            var xDown = 0F
            var yDown = 0F
            var distantX = 0F
            var distantY = 0F

            view.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        timerLongClick = Timer().also { t ->
                            t.schedule(object : TimerTask() {
                                override fun run() {
                                    onItemLongClicked.invoke(requestOder)
                                    event.action = MotionEvent.ACTION_CANCEL
                                }
                            }, ViewConfiguration.getLongPressTimeout().toLong())
                        }
                        xDown = event.x
                        yDown = event.y
                        distantX = 0F
                        distantY = 0F
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val x = event.x
                        val y = event.y
                        val disX = x - xDown
                        val disY = y - yDown
                        v.x = v.x + disX
                        v.y = v.y + disY
                        distantX += disX
                        distantY += disY
                        v.alpha = 0.7F
                        if (isHasActionMoveWithThread(
                                distantX,
                                THREAD_DISTANCE_CANCEL_LONG_CLICK,
                                distantY,
                                THREAD_DISTANCE_CANCEL_LONG_CLICK
                            )
                        ) {
                            timerLongClick?.cancel()
                        }
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        timerLongClick?.cancel()
                        if (isHasActionMoveWithThread(
                                distantX,
                                this.width / 2F,
                                distantY,
                                this.height / 2F
                            )
                        ) {
                            v.alpha = 0F
                            onDragListener.invoke(requestOder)
                        } else {
                            v.alpha = 1F
                            v.x = currentX
                            v.y = currentY
                        }
                    }
                }
                true
            }
        }
    }

    private fun isHasActionMoveWithThread(
        distantX: Float,
        threadDistantXToHasAction: Float,
        distantY: Float,
        threadDistantYToHasAction: Float
    ): Boolean =
        abs(distantX) >= threadDistantXToHasAction || abs(distantY) >= threadDistantYToHasAction
}
