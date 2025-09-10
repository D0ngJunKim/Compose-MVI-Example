package com.yeogibook.abcmm.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
) {
    val dotRadius =
        animateDpAsState(
            targetValue = if (selected) RadioButtonDotSize / 2 else 0.dp,
            animationSpec = tween(durationMillis = RadioAnimationDuration)
        )
    val radioColor = colors.radioColor(enabled, selected)
    val selectableModifier =
        if (onClick != null) {
            Modifier.selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null
            )
        } else {
            Modifier
        }
    Canvas(
        modifier
            .then(selectableModifier)
            .wrapContentSize(Alignment.Center)
            .padding(RadioButtonPadding)
            .requiredSize(20.dp)
    ) {
        val strokeWidth = RadioStrokeWidth.toPx()
        drawCircle(
            radioColor.value,
            radius = (20.dp / 2).toPx() - strokeWidth / 2,
            style = Stroke(strokeWidth)
        )
        if (dotRadius.value > 0.dp) {
            drawCircle(radioColor.value, dotRadius.value.toPx() - strokeWidth / 2, style = Fill)
        }
    }
}

object RadioButtonDefaults {

    @Composable
    fun colors() = defaultRadioButtonColors

    @Composable
    fun colors(
        selectedColor: Color = Color.Unspecified,
        unselectedColor: Color = Color.Unspecified,
        disabledSelectedColor: Color = Color.Unspecified,
        disabledUnselectedColor: Color = Color.Unspecified
    ): RadioButtonColors =
        defaultRadioButtonColors.copy(
            selectedColor,
            unselectedColor,
            disabledSelectedColor,
            disabledUnselectedColor
        )

    private val defaultRadioButtonColors: RadioButtonColors
        get() {
            return RadioButtonColors(
                selectedColor = Color.Red,
                unselectedColor = Color.Black,
                disabledSelectedColor = Color.Red,
                disabledUnselectedColor = Color.Black
            )
        }
}

@Immutable
class RadioButtonColors
constructor(
    val selectedColor: Color,
    val unselectedColor: Color,
    val disabledSelectedColor: Color,
    val disabledUnselectedColor: Color
) {
    fun copy(
        selectedColor: Color = this.selectedColor,
        unselectedColor: Color = this.unselectedColor,
        disabledSelectedColor: Color = this.disabledSelectedColor,
        disabledUnselectedColor: Color = this.disabledUnselectedColor,
    ) =
        RadioButtonColors(
            selectedColor.takeOrElse { this.selectedColor },
            unselectedColor.takeOrElse { this.unselectedColor },
            disabledSelectedColor.takeOrElse { this.disabledSelectedColor },
            disabledUnselectedColor.takeOrElse { this.disabledUnselectedColor },
        )

    @Composable
    fun radioColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target =
            when {
                enabled && selected -> selectedColor
                enabled && !selected -> unselectedColor
                !enabled && selected -> disabledSelectedColor
                else -> disabledUnselectedColor
            }

        return if (enabled) {
            animateColorAsState(target, tween(durationMillis = RadioAnimationDuration))
        } else {
            rememberUpdatedState(target)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is RadioButtonColors) return false

        if (selectedColor != other.selectedColor) return false
        if (unselectedColor != other.unselectedColor) return false
        if (disabledSelectedColor != other.disabledSelectedColor) return false
        if (disabledUnselectedColor != other.disabledUnselectedColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = selectedColor.hashCode()
        result = 31 * result + unselectedColor.hashCode()
        result = 31 * result + disabledSelectedColor.hashCode()
        result = 31 * result + disabledUnselectedColor.hashCode()
        return result
    }
}

private const val RadioAnimationDuration = 100

private val RadioButtonPadding = 2.dp
private val RadioButtonDotSize = 12.dp
private val RadioStrokeWidth = 2.dp
