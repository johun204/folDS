package me.magnum.melonds.ui.emulator

import me.magnum.melonds.domain.model.layout.LayoutComponent
import me.magnum.melonds.ui.common.LayoutComponentViewBuilder
import me.magnum.melonds.ui.common.LayoutComponentViewBuilderFactory
import me.magnum.melonds.ui.common.componentbuilders.*
import me.magnum.melonds.ui.emulator.input.componentbuilder.RuntimeScreenLayoutComponentViewBuilder
import me.magnum.melonds.ui.emulator.input.componentbuilder.ToggleableSingleButtonLayoutComponentViewBuilder
import me.magnum.melonds.ui.emulator.input.view.SkinButtonView

class RuntimeLayoutComponentViewBuilderFactory : LayoutComponentViewBuilderFactory {
    private val layoutComponentViewBuilderCache = mutableMapOf<LayoutComponent, LayoutComponentViewBuilder>()

    // folDS: DS 스킨 레이아웃이면 스킨에 그려진 버튼들은 투명 눌림효과 뷰로 만든다
    var useDsSkin = false

    override fun getLayoutComponentViewBuilder(layoutComponent: LayoutComponent): LayoutComponentViewBuilder {
        if (useDsSkin && layoutComponent in SKIN_COMPONENTS) {
            return SkinButtonView.Builder(layoutComponent)
        }
        return layoutComponentViewBuilderCache.getOrElse(layoutComponent) {
            val builder = when (layoutComponent) {
                LayoutComponent.TOP_SCREEN -> RuntimeScreenLayoutComponentViewBuilder()
                LayoutComponent.BOTTOM_SCREEN -> RuntimeScreenLayoutComponentViewBuilder()
                LayoutComponent.DPAD -> DpadLayoutComponentViewBuilder()
                LayoutComponent.BUTTONS -> ButtonsLayoutComponentViewBuilder()
                LayoutComponent.BUTTON_FAST_FORWARD_TOGGLE,
                LayoutComponent.BUTTON_MICROPHONE_TOGGLE,
                LayoutComponent.BUTTON_TOGGLE_SOFT_INPUT -> ToggleableSingleButtonLayoutComponentViewBuilder(layoutComponent)
                else -> SingleButtonLayoutComponentViewBuilder(layoutComponent)
            }

            layoutComponentViewBuilderCache[layoutComponent] = builder
            builder
        }
    }

    private companion object {
        val SKIN_COMPONENTS = setOf(
            LayoutComponent.DPAD,
            LayoutComponent.BUTTONS,
            LayoutComponent.BUTTON_L,
            LayoutComponent.BUTTON_R,
            LayoutComponent.BUTTON_START,
            LayoutComponent.BUTTON_SELECT,
        )
    }
}