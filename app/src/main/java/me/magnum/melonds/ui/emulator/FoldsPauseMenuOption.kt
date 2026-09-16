package me.magnum.melonds.ui.emulator

import me.magnum.melonds.R
import me.magnum.melonds.domain.model.Input

/** folDS: 스킨 화면에서 뺀 보조 버튼들. 일시정지 메뉴에서 같은 입력을 보내 실행한다 */
enum class FoldsPauseMenuOption(override val textResource: Int, val input: Input) : PauseMenuOption {
    FAST_FORWARD(R.string.input_fast_forward, Input.FAST_FORWARD),
    SWAP_SCREENS(R.string.input_swap_screens, Input.SWAP_SCREENS),
    LID(R.string.input_lid, Input.HINGE),
    MICROPHONE(R.string.input_microphone, Input.MICROPHONE),
    TOGGLE_SOFT_INPUT(R.string.input_toggle_soft_input, Input.TOGGLE_SOFT_INPUT),
}
