package com.android.systemui.shade

import android.content.Context
import android.os.PowerManager
import android.view.GestureDetector
import android.view.MotionEvent
import com.android.systemui.dagger.SysUISingleton
import com.android.systemui.plugins.FalsingManager
import com.android.systemui.plugins.statusbar.StatusBarStateController
import com.android.systemui.statusbar.StatusBarState
import com.android.systemui.statusbar.phone.CentralSurfaces
import javax.inject.Inject

@SysUISingleton
class QQSGestureListener @Inject constructor(
        private val context: Context,
        private val falsingManager: FalsingManager,
        private val powerManager: PowerManager,
        private val statusBarStateController: StatusBarStateController,
        private val centralSurfaces: CentralSurfaces,
) : GestureDetector.SimpleOnGestureListener() {

    private val quickQsOffsetHeight: Int = context.resources.getDimensionPixelSize(
            com.android.internal.R.dimen.quick_qs_offset_height)

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        val isKeyguard = statusBarStateController.state == StatusBarState.KEYGUARD
        val isBouncer = centralSurfaces.isBouncerShowing

        if (
            e.actionMasked == MotionEvent.ACTION_UP &&
            !statusBarStateController.isDozing &&
            (e.y < quickQsOffsetHeight || (isKeyguard && !isBouncer)) &&
            !falsingManager.isFalseDoubleTap
        ) {
            powerManager.goToSleep(e.eventTime)
            return true
        }
        return false
    }
}