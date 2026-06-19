package com.artisan.browser.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange

suspend fun PointerInputScope.inspectDragGestures(
    onDragStart: (down: Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    awaitEachGesture {
        val down = awaitFirstDown()
        onDragStart(down.position)
        var pointer = down
        while (true) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull { it.id == pointer.id }
            if (change == null || !change.pressed) {
                onDragEnd()
                return@awaitEachGesture
            }
            val delta = change.positionChange()
            if (delta != Offset.Zero) {
                onDrag(change, delta)
                change.consume()
            }
            pointer = change
        }
    }
}
