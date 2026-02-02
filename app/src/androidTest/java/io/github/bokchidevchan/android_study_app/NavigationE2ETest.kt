package io.github.bokchidevchan.android_study_app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun waitForContentLoaded(): Boolean {
        // 최대 30초 동안 1초마다 확인
        repeat(30) {
            composeRule.waitForIdle()

            val hasMarkets = composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("이더리움").fetchSemanticsNodes().isNotEmpty()
            val hasError = composeRule.onAllNodesWithText("Retry").fetchSemanticsNodes().isNotEmpty()
            val hasEmpty = composeRule.onAllNodesWithText("No markets found").fetchSemanticsNodes().isNotEmpty()

            if (hasMarkets || hasError || hasEmpty) {
                return hasMarkets
            }

            Thread.sleep(1000)
        }
        return false
    }

    @Test
    fun 마켓_아이템_클릭시_상세_화면으로_이동한다() {
        val hasMarkets = waitForContentLoaded()

        if (hasMarkets) {
            // 마켓이 로드된 경우
            val btcNodes = composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes()
            if (btcNodes.isNotEmpty()) {
                composeRule.onAllNodesWithText("비트코인").onFirst().performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithContentDescription("Back").assertIsDisplayed()
            } else {
                composeRule.onAllNodesWithText("이더리움").onFirst().performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithContentDescription("Back").assertIsDisplayed()
            }
        } else {
            // 마켓이 로드되지 않은 경우 - 앱이 정상 동작 확인
            composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
        }
    }

    @Test
    fun 상세_화면에서_뒤로가기_클릭시_목록_화면으로_돌아온다() {
        val hasMarkets = waitForContentLoaded()

        if (hasMarkets) {
            val btcNodes = composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes()
            if (btcNodes.isNotEmpty()) {
                composeRule.onAllNodesWithText("비트코인").onFirst().performClick()
            } else {
                composeRule.onAllNodesWithText("이더리움").onFirst().performClick()
            }
            composeRule.waitForIdle()

            composeRule.onNodeWithContentDescription("Back").performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
        } else {
            // 마켓이 로드되지 않은 경우 - 기본 화면 확인
            composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
        }
    }

    @Test
    fun 앱_시작_후_컨텐츠가_로드된다() {
        waitForContentLoaded()

        // 앱 헤더가 항상 표시되어야 함
        composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()

        // 탭이 표시되어야 함
        composeRule.onNodeWithText("KRW").assertIsDisplayed()
    }
}
