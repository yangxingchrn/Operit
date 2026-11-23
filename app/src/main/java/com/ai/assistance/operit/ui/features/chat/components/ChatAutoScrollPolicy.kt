package com.ai.assistance.operit.ui.features.chat.components

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import kotlin.math.roundToInt

/**
 * 自动跟随到底部的容差策略。
 *
 * 跟随条件不是「是否精确贴底」，而是「底部还有多少没露出来」：未显示高度不超过阈值
 * （默认 3 行正文）时继续跟随，超过阈值立即停止，位置交给用户，直到他自己滚回阈值内。
 */
private const val FOLLOW_LINE_COUNT = 3f

/** 距底部尚未显示的高度（px）。 */
internal fun ScrollState.distanceToBottomPx(): Int = (maxValue - value).coerceAtLeast(0)

/** 是否仍在跟随阈值内。 */
internal fun ScrollState.isWithinFollowThreshold(thresholdPx: Int): Boolean =
    thresholdPx > 0 && distanceToBottomPx() <= thresholdPx

/** 阈值＝3 行正文行高；行高未指定时退回字号 ×1.4，下限 1dp。 */
@Composable
internal fun rememberChatFollowThresholdPx(): Int {
    val density = LocalDensity.current
    val style = MaterialTheme.typography.bodyMedium
    return remember(density, style) {
        val lineHeight: TextUnit =
            if (style.lineHeight.isSpecified) style.lineHeight else style.fontSize * 1.4f
        val linePx = with(density) { lineHeight.toPx() }
        val minPx = with(density) { 1.dp.toPx() }
        (linePx * FOLLOW_LINE_COUNT).roundToInt().coerceAtLeast(minPx.roundToInt())
    }
}
