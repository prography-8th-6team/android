package com.jerny.moiz.presentation.detail.schedule

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.jerny.moiz.R
import java.lang.Float.min

class SwipeHelperCallback(private val recyclerViewAdapter: ScheduleAdapter) : ItemTouchHelper.Callback() {
    private var currentPosition: Int? = null
    private var previousPosition: Int? = null

    // 현재 x값
    private var currentDx = 0f

    // 고정시킬 크기
    private var clamp = 0f

    // 이동 방향 결정
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(UP or DOWN, LEFT or RIGHT)
    }

    // drag and drop
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        TODO("Not yet implemented")
    }

    // 스와이프 일어날 때 동작
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    // swipe 시 뷰만 슬라이드 + 삭제 화면 보이게
    // 사용자와 상호작용과 애니메이션 종료시 호출
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.adapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    // 스와이프 되었거나 드래그 됐을때 호추
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            // 현재 drag, swipe 중인 position 기억하기
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    // item 터치하거나 스와이프 등 뷰에 변화가 생기면 호출
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            // newX 만큼 이동
            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive)

            if (newX == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(50L).start()
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, newX, dY, actionState, isCurrentlyActive)
        }
    }

    // swipe 인식할 최소 속도 설정
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = defaultValue * 10

    // 사용자가 swipe 했다고 간주하기 위해 이동해야 하는 부분 반환
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // -clamp 이상 swipe 시 isClamped 를 true 로 설정
        // 아니면 false
        setTag(viewHolder, currentDx <= -clamp)
        return 2f
    }

    // swipe 시 삭제 화면 보이도록
    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean,
    ): Float {
        // 오른쪽으로 swipe 안되게
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isClamped) {
            // swipe 중이면 swipe 영역 제한
            if (isCurrentlyActive) {
                // right swipe
                if (dX < 0) dX / 3 - clamp
                // left swipe
                else dX - clamp
            } else {
                // swipe 중 아니면 고정
                -clamp
            }
        } else {
            // 고정할 수 없으면 newX 는 swipe한 만큼
            dX / 2
        }
        return min(newX, max)
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View = viewHolder.itemView.findViewById(
        R.id.ll_total)

    // isClamped 를 view 의 tag 로 관리
    // isClamped = true -> 고정
    // isClamped = false -> 고정 해제
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean = viewHolder.itemView.tag as? Boolean
        ?: false

    // swipe 시 고정될 크기 설정
    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    // 다른 view 가 swipe 되거나 터치되면 고정 해제
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition) return

        previousPosition?.let {
            Log.w("curr: ", "$currentPosition")
            Log.w("prev: ", "$previousPosition")
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
//            getView(viewHolder).animate().x(20f).setDuration(50L).start()
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}