package me.magnum.melonds.impl.layout

import android.content.ContentResolver
import android.net.Uri
import me.magnum.melonds.R
import me.magnum.melonds.domain.model.Background
import me.magnum.melonds.domain.model.Rect
import me.magnum.melonds.domain.model.layout.BackgroundMode
import me.magnum.melonds.domain.model.layout.LayoutComponent
import me.magnum.melonds.domain.model.layout.PositionedLayoutComponent
import me.magnum.melonds.domain.model.layout.ScreenLayout
import java.util.UUID
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * folDS: 실제 DS 모양 스킨. 스킨 PNG가 화면 전체 배경(FIT_CENTER)으로 그려지고, 게임 화면과 버튼은
 * 스킨 이미지 속 위치에 그대로 배치된다. 좌표는 스킨 PNG 픽셀 기준.
 */
object DsSkin {

    class Spec(val id: UUID, val drawable: Int, val width: Int, val height: Int, val parts: Map<LayoutComponent, Rect>)

    /** 펼침(내부 화면): 가로형 DS Lite */
    val OPEN = Spec(
        UUID.fromString("f01d5000-0000-4000-8000-000000000001"), R.drawable.ds_skin_open, 1166, 1080,
        mapOf(
            LayoutComponent.TOP_SCREEN to Rect(310, 91, 553, 412),
            LayoutComponent.BOTTOM_SCREEN to Rect(308, 566, 552, 412),
            LayoutComponent.DPAD to Rect(49, 640, 167, 167),
            LayoutComponent.BUTTONS to Rect(928, 595, 205, 205),
            LayoutComponent.BUTTON_START to Rect(913, 879, 68, 68),
            LayoutComponent.BUTTON_SELECT to Rect(913, 948, 68, 68),
            LayoutComponent.BUTTON_L to Rect(28, 395, 170, 70),
            LayoutComponent.BUTTON_R to Rect(968, 395, 170, 70),
            LayoutComponent.BUTTON_HINGE to Rect(30, 125, 60, 60),
            LayoutComponent.BUTTON_TOGGLE_SOFT_INPUT to Rect(105, 125, 60, 60),
            LayoutComponent.BUTTON_MICROPHONE_TOGGLE to Rect(180, 125, 60, 60),
            LayoutComponent.BUTTON_FAST_FORWARD_TOGGLE to Rect(1001, 125, 60, 60),
            LayoutComponent.BUTTON_SWAP_SCREENS to Rect(1076, 125, 60, 60),
        ),
    )

    /** 접힘(커버 화면): 좁고 긴 화면에 맞춰 화면을 최대한 키운 세로형 재구성 */
    val COVER = Spec(
        UUID.fromString("f01d5000-0000-4000-8000-000000000002"), R.drawable.ds_skin_cover, 1080, 2520,
        mapOf(
            LayoutComponent.TOP_SCREEN to Rect(40, 120, 1000, 750),
            LayoutComponent.BOTTOM_SCREEN to Rect(40, 966, 1000, 750),
            LayoutComponent.DPAD to Rect(60, 1990, 317, 317),
            LayoutComponent.BUTTONS to Rect(663, 1963, 369, 369),
            LayoutComponent.BUTTON_START to Rect(401, 2048, 116, 116),
            LayoutComponent.BUTTON_SELECT to Rect(401, 2165, 116, 116),
            LayoutComponent.BUTTON_L to Rect(40, 1790, 220, 90),
            LayoutComponent.BUTTON_R to Rect(820, 1790, 220, 90),
            LayoutComponent.BUTTON_HINGE to Rect(400, 1800, 70, 70),
            LayoutComponent.BUTTON_TOGGLE_SOFT_INPUT to Rect(505, 1800, 70, 70),
            LayoutComponent.BUTTON_FAST_FORWARD_TOGGLE to Rect(610, 1800, 70, 70),
        ),
    )

    private val specs = listOf(OPEN, COVER)

    fun isSkin(backgroundId: UUID?) = specs.any { it.id == backgroundId }

    /** 스킨 ID면 앱 리소스를 가리키는 배경을 돌려준다 */
    fun background(backgroundId: UUID?, packageName: String): Background? {
        val spec = specs.firstOrNull { it.id == backgroundId } ?: return null
        return Background(spec.id, "folDS", Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://$packageName/${spec.drawable}"))
    }

    /** 렌더러의 FIT_CENTER와 같은 방식으로 스킨을 화면에 맞추고, 각 컴포넌트를 실제 픽셀 위치로 변환 */
    fun buildLayout(spec: Spec, width: Int, height: Int): ScreenLayout {
        val scale = min(width / spec.width.toFloat(), height / spec.height.toFloat())
        val offsetX = (width - spec.width * scale) / 2
        val offsetY = (height - spec.height * scale) / 2
        val components = spec.parts.map { (component, r) ->
            val rect = Rect(
                (offsetX + r.x * scale).roundToInt(),
                (offsetY + r.y * scale).roundToInt(),
                (r.width * scale).roundToInt(),
                (r.height * scale).roundToInt(),
            )
            PositionedLayoutComponent(rect, component)
        }
        return ScreenLayout(spec.id, BackgroundMode.FIT_CENTER, components)
    }
}
