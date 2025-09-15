package com.yeogibook.abcmm.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.launch

fun Modifier.card(
    shape: Shape,
    backgroundColor: Color = Color.White,
    border: BorderStroke? = null,
    shadow: Shadow? = null,
) = this
    .then(
        if (shadow != null) {
            Modifier.dropShadow(shape, shadow)
        } else {
            Modifier
        }
    )
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)


fun Modifier.isClickable() = this.onClick { }

fun Modifier.onClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = this
    .composed {
        clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = onClick,
            role = role,
            indication = LocalIndication,
            interactionSource = remember { MutableInteractionSource() },
        )
    }

private object LocalIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        LocalIndicationInstance(interactionSource)

    override fun hashCode(): Int = -1

    override fun equals(other: Any?) = other === this

    private class LocalIndicationInstance(private val interactionSource: InteractionSource) : Modifier.Node(), DrawModifierNode {
        private var isPressed = false
        private var isHovered = false
        private var isFocused = false

        override fun onAttach() {
            coroutineScope.launch {
                var pressCount = 0
                var hoverCount = 0
                var focusCount = 0
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> pressCount++
                        is PressInteraction.Release -> pressCount--
                        is PressInteraction.Cancel -> pressCount--
                        is HoverInteraction.Enter -> hoverCount++
                        is HoverInteraction.Exit -> hoverCount--
                        is FocusInteraction.Focus -> focusCount++
                        is FocusInteraction.Unfocus -> focusCount--
                    }
                    val pressed = pressCount > 0
                    val hovered = hoverCount > 0
                    val focused = focusCount > 0
                    var invalidateNeeded = false
                    if (isPressed != pressed) {
                        isPressed = pressed
                        invalidateNeeded = true
                    }
                    if (isHovered != hovered) {
                        isHovered = hovered
                        invalidateNeeded = true
                    }
                    if (isFocused != focused) {
                        isFocused = focused
                        invalidateNeeded = true
                    }
                    if (invalidateNeeded) invalidateDraw()
                }
            }
        }

        override fun ContentDrawScope.draw() {
            drawContent()
        }
    }
}