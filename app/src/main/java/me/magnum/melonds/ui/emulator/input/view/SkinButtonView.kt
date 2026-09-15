package me.magnum.melonds.ui.emulator.input.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import me.magnum.melonds.domain.model.Input
import me.magnum.melonds.domain.model.layout.LayoutComponent
import me.magnum.melonds.ui.common.LayoutComponentViewBuilder
import me.magnum.melonds.ui.emulator.input.IInputListener

/**
 * folDS: DS 스킨 위에 놓이는 투명 버튼. 버튼 그림은 배경 스킨에 있고, 이 뷰는 눌린 부분만 어둡게 칠해 눌림 효과를 준다.
 * 비율 값은 스킨 PNG의 버튼 영역(DsSkin) 기준으로 측정한 값.
 */
class SkinButtonView(context: Context, private val component: LayoutComponent) : View(context) {

    private val pressedInputs = mutableSetOf<Input>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x59000000 }

    /** 입력 리스너를 감싸 눌림 상태를 추적한다 */
    fun wrap(listener: IInputListener): IInputListener = object : IInputListener by listener {
        override fun onKeyPress(key: Input) {
            listener.onKeyPress(key)
            pressedInputs.add(key)
            invalidate()
        }

        override fun onKeyReleased(key: Input) {
            listener.onKeyReleased(key)
            pressedInputs.remove(key)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        pressedInputs.forEach { input ->
            when (component) {
                LayoutComponent.DPAD -> {
                    // 십자키 팔 폭은 전체의 약 1/3
                    val a = 0.33f
                    val b = 0.67f
                    val r = w * 0.04f
                    when (input) {
                        Input.UP -> canvas.drawRoundRect(w * a, 0f, w * b, h * 0.5f, r, r, paint)
                        Input.DOWN -> canvas.drawRoundRect(w * a, h * 0.5f, w * b, h, r, r, paint)
                        Input.LEFT -> canvas.drawRoundRect(0f, h * a, w * 0.5f, h * b, r, r, paint)
                        Input.RIGHT -> canvas.drawRoundRect(w * 0.5f, h * a, w, h * b, r, r, paint)
                        else -> {}
                    }
                }
                LayoutComponent.BUTTONS -> {
                    val (cx, cy) = when (input) {
                        Input.X -> 0.51f to 0.185f
                        Input.Y -> 0.185f to 0.51f
                        Input.A -> 0.84f to 0.51f
                        Input.B -> 0.51f to 0.84f
                        else -> return@forEach
                    }
                    canvas.drawCircle(w * cx, h * cy, w * 0.165f, paint)
                }
                LayoutComponent.BUTTON_L, LayoutComponent.BUTTON_R -> canvas.drawRoundRect(0f, 0f, w, h, h / 2, h / 2, paint)
                // START / SELECT: 뷰 가운데의 작은 원형 버튼
                else -> canvas.drawCircle(w / 2, h / 2, w * 0.26f, paint)
            }
        }
    }

    class Builder(private val component: LayoutComponent) : LayoutComponentViewBuilder() {
        override fun build(context: Context): View = SkinButtonView(context, component)
        override fun getAspectRatio() = 1f
    }
}
