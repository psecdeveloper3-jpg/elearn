package com.example

import com.example.data.models.PunjabBoard
import com.example.data.seed.PunjabBoardSeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testPunjabBoardSeedData() {
    assertTrue(PunjabBoardSeedData.subjects.isNotEmpty())
    assertTrue(PunjabBoardSeedData.chapters.isNotEmpty())
    assertTrue(PunjabBoardSeedData.pastPaperQuestions.isNotEmpty())
    assertTrue(PunjabBoardSeedData.sampleMcqs.isNotEmpty())
  }

  @Test
  fun testPunjabBoardsEnum() {
    assertEquals(9, PunjabBoard.values().size)
    assertNotNull(PunjabBoard.LAHORE)
    assertNotNull(PunjabBoard.RAWALPINDI)
    assertNotNull(PunjabBoard.FAISALABAD)
  }

  @Test
  fun testPastPaperRepeatProbability() {
    val highRepeat = PunjabBoardSeedData.pastPaperQuestions.filter { it.repeatProbability >= 85 }
    assertTrue(highRepeat.isNotEmpty())
  }
}
