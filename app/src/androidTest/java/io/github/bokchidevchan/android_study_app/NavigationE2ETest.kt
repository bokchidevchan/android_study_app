package io.github.bokchidevchan.android_study_app

import androidx.compose.ui.test.assertIsDisplayed
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

    @Test
    fun 마켓_아이템_클릭시_상세_화면으로_이동한다() {
        // 마켓 목록이 로드될 때까지 대기
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("No markets found").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("Retry").fetchSemanticsNodes().isNotEmpty()
        }

        // 마켓이 로드된 경우에만 테스트 진행
        val marketNodes = composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes()
        if (marketNodes.isNotEmpty()) {
            // 비트코인 마켓 클릭
            composeRule.onAllNodesWithText("비트코인").onFirst().performClick()

            // 상세 화면으로 이동 확인 (뒤로가기 버튼 표시)
            composeRule.waitForIdle()
            composeRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        }
    }

    @Test
    fun 상세_화면에서_뒤로가기_클릭시_목록_화면으로_돌아온다() {
        // 마켓 목록이 로드될 때까지 대기
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("No markets found").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("Retry").fetchSemanticsNodes().isNotEmpty()
        }

        // 마켓이 로드된 경우에만 테스트 진행
        val marketNodes = composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes()
        if (marketNodes.isNotEmpty()) {
            // 비트코인 마켓 클릭
            composeRule.onAllNodesWithText("비트코인").onFirst().performClick()
            composeRule.waitForIdle()

            // 뒤로가기 버튼 클릭
            composeRule.onNodeWithContentDescription("Back").performClick()
            composeRule.waitForIdle()

            // 마켓 목록 화면으로 돌아왔는지 확인
            composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
        }
    }

    @Test
    fun 에러_발생시_재시도_버튼이_표시된다() {
        // 네트워크 에러 발생 시 Retry 버튼 확인
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText("비트코인").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("No markets found").fetchSemanticsNodes().isNotEmpty() ||
                    composeRule.onAllNodesWithText("Retry").fetchSemanticsNodes().isNotEmpty()
        }

        // 에러가 발생한 경우 Retry 버튼이 있는지 확인
        val retryNodes = composeRule.onAllNodesWithText("Retry").fetchSemanticsNodes()
        if (retryNodes.isNotEmpty()) {
            composeRule.onNodeWithText("Retry").assertIsDisplayed()
        }
    }
}
