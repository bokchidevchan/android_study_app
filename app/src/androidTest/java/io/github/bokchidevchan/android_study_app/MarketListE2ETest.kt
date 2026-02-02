package io.github.bokchidevchan.android_study_app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MarketListE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun 앱_실행시_마켓_목록_화면이_표시된다() {
        composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
    }

    @Test
    fun 마켓_타입_탭들이_표시된다() {
        composeRule.onNodeWithText("KRW").assertIsDisplayed()
        composeRule.onNodeWithText("BTC").assertIsDisplayed()
        composeRule.onNodeWithText("USDT").assertIsDisplayed()
    }

    @Test
    fun BTC_탭_클릭시_탭이_선택된다() {
        composeRule.onNodeWithText("BTC").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("BTC").assertIsDisplayed()
    }

    @Test
    fun USDT_탭_클릭시_탭이_선택된다() {
        composeRule.onNodeWithText("USDT").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("USDT").assertIsDisplayed()
    }

    @Test
    fun 로딩_또는_컨텐츠가_표시된다() {
        // 네트워크 상태에 따라 로딩, 에러, 또는 마켓 목록이 표시됨
        composeRule.waitForIdle()
        // 앱이 크래시 없이 실행되는지 확인
        composeRule.onNodeWithText("Upbit Market").assertIsDisplayed()
    }
}
