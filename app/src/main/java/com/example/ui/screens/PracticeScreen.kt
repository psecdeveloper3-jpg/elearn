package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.seed.PunjabBoardSeedData
import com.example.ui.theme.Amber500
import com.example.ui.theme.CoinGold
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.UiState

@Composable
fun PracticeScreen(
  uiState: UiState,
  onSelectMcqOption: (Int) -> Unit,
  onSubmitMcqAnswer: () -> Unit,
  onNextMcqQuestion: () -> Unit,
  onResetMcqQuiz: () -> Unit,
  onUpdateWrittenAnswer: (String) -> Unit,
  onGradeWrittenTest: (String) -> Unit,
  onUpdateOralTranscript: (String) -> Unit,
  onGradeOralTest: (String) -> Unit
) {
  val isUrdu = uiState.language == AppLanguage.URDU
  var activePracticeTab by remember { mutableIntStateOf(0) } // 0: MCQ, 1: Written, 2: Oral, 3: Mock Exam

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    // 4 Practice Tabs
    ScrollableTabRow(
      selectedTabIndex = activePracticeTab,
      containerColor = Color.White,
      edgePadding = 0.dp,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
    ) {
      val tabs = listOf(
        "Step 1: MCQ Quiz",
        "Step 2: Written Test",
        "Step 3: Oral Viva",
        "Mock Board Exam"
      )
      val urduTabs = listOf(
        "مرحلہ 1: معروضی سوالات",
        "مرحلہ 2: انشائیہ تحریری",
        "مرحلہ 3: زبانی ٹیسٹ",
        "ماک بورڈ امتحان"
      )
      tabs.forEachIndexed { index, label ->
        Tab(
          selected = activePracticeTab == index,
          onClick = { activePracticeTab = index },
          text = {
            Text(
              text = if (isUrdu) urduTabs[index] else label,
              fontWeight = if (activePracticeTab == index) FontWeight.Bold else FontWeight.Medium,
              fontSize = 12.sp,
              color = if (activePracticeTab == index) Emerald900 else Color(0xFF64748B)
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    when (activePracticeTab) {
      0 -> {
        // --- STEP 1: MCQ QUIZ ---
        val questions = PunjabBoardSeedData.sampleMcqs
        val currentQ = questions.getOrNull(uiState.currentMcqIndex) ?: questions[0]

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          if (!uiState.mcqCompleted) {
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Question ${uiState.currentMcqIndex + 1} of ${questions.size}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Emerald800
                )
                Text(
                  text = "Score: ${uiState.mcqScore}/${questions.size}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Emerald900
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              LinearProgressIndicator(
                progress = { (uiState.currentMcqIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(CircleShape),
                color = Emerald800,
                trackColor = Color(0xFFE2E8F0)
              )
            }

            // Question Card
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(18.dp)) {
                  Text(
                    text = if (isUrdu) currentQ.urduQuestion else currentQ.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.titleMedium.fontFamily,
                    color = Color(0xFF0F172A),
                    lineHeight = 22.sp
                  )
                }
              }
            }

            // Options list
            val options = if (isUrdu) currentQ.urduOptions else currentQ.options
            items(options.size) { optIdx ->
              val isSelected = uiState.selectedMcqOption == optIdx
              val isCorrect = optIdx == currentQ.correctOptionIndex
              val cardColor = when {
                !uiState.mcqAnswerSubmitted && isSelected -> Emerald100
                uiState.mcqAnswerSubmitted && isCorrect -> Color(0xFFDCFCE7)
                uiState.mcqAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                else -> Color.White
              }

              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onSelectMcqOption(optIdx) }
                  .testTag("mcq_option_$optIdx")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = CircleShape,
                    color = if (isSelected) Emerald800 else Color(0xFFE2E8F0),
                    modifier = Modifier.size(28.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        text = ('A' + optIdx).toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFF475569)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = options[optIdx],
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f)
                  )
                  if (uiState.mcqAnswerSubmitted) {
                    if (isCorrect) {
                      Icon(Icons.Default.Check, contentDescription = "Correct", tint = Emerald800)
                    } else if (isSelected) {
                      Icon(Icons.Default.Close, contentDescription = "Incorrect", tint = Color.Red)
                    }
                  }
                }
              }
            }

            // Explanation on submit
            if (uiState.mcqAnswerSubmitted) {
              item {
                Card(
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                  border = CardDefaults.outlinedCardBorder(),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                      text = if (isUrdu) "وضاحت (Explanation):" else "Examiner Explanation:",
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Emerald800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = if (isUrdu) currentQ.urduExplanation else currentQ.explanation,
                      fontSize = 13.sp,
                      color = Color(0xFF334155),
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }

            // Bottom action buttons
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                if (!uiState.mcqAnswerSubmitted) {
                  Button(
                    onClick = onSubmitMcqAnswer,
                    enabled = uiState.selectedMcqOption != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("submit_mcq_btn")
                  ) {
                    Text(if (isUrdu) "جواب جمع کرائیں" else "Submit Answer")
                  }
                } else {
                  Button(
                    onClick = onNextMcqQuestion,
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("next_mcq_btn")
                  ) {
                    Text(if (isUrdu) "اگلا سوال" else "Next Question")
                  }
                }
              }
            }
          } else {
            // Quiz Completed Result Card
            item {
              Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(
                  modifier = Modifier.padding(24.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(text = "🎉", fontSize = 48.sp)
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = if (isUrdu) "کوئز مکمل!" else "Quiz Completed!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Emerald900
                  )
                  Text(
                    text = "You scored ${uiState.mcqScore} out of ${questions.size}!",
                    fontSize = 16.sp,
                    color = Color(0xFF64748B)
                  )
                  Spacer(modifier = Modifier.height(14.dp))
                  Surface(
                    color = Color(0xFFFEFCE8),
                    shape = RoundedCornerShape(12.dp)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = CoinGold
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = if (uiState.mcqScore == questions.size) "+50 Flawless Punjab Coins!" else "+20 Punjab Coins Earned!",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(20.dp))
                  Button(
                    onClick = onResetMcqQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Retry")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isUrdu) "دوبارہ کوشش کریں" else "Practice Again")
                  }
                }
              }
            }
          }
          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }

      1 -> {
        // --- STEP 2: WRITTEN TEST WITH GEMINI GRADING ---
        val sampleWrittenPrompt = "Derive 2nd equation of motion: S = vi·t + 1/2 a·t² using speed-time graph according to Punjab Board standard."
        val urduWrittenPrompt = "گراف کے طریقے سے حرکت کی دوسری مساوات S = vi·t + 1/2 a·t² اخذ کریں۔"

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                  color = Amber500,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "BISE Lahore / Rawalpindi (5 Marks)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = if (isUrdu) urduWrittenPrompt else sampleWrittenPrompt,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Emerald900,
                  lineHeight = 22.sp
                )
              }
            }
          }

          // Student Answer Input
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = if (isUrdu) "اپنا تحریری جواب ٹائپ کریں یا تصویر اپ لوڈ کریں:" else "Type Your Answer or Upload Photo of Handwritten Sheet:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = uiState.writtenStudentAnswer,
                  onValueChange = { onUpdateWrittenAnswer(it) },
                  placeholder = {
                    Text(
                      text = "Consider a body moving with initial velocity vi and uniform acceleration a. In time t, total distance S is area under graph = Area of rectangle + Area of triangle...",
                      fontSize = 13.sp
                    )
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("written_answer_input"),
                  shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedButton(
                    onClick = {
                      onUpdateWrittenAnswer("Total distance S = Area under velocity-time graph OABD. Area = Area of rectangle OACD + Area of triangle ABC. S = (vi × t) + (1/2 × t × at) = vi·t + 1/2 a·t².")
                    },
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = "Sample OCR", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Auto-Fill OCR", fontSize = 11.sp)
                  }

                  Button(
                    onClick = { onGradeWrittenTest(sampleWrittenPrompt) },
                    enabled = !uiState.isWrittenGrading && uiState.writtenStudentAnswer.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("grade_written_btn")
                  ) {
                    if (uiState.isWrittenGrading) {
                      CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text("Grading with Gemini...")
                    } else {
                      Icon(Icons.Default.Psychology, contentDescription = "Grade")
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(if (isUrdu) "اے آئی گریڈنگ حاصل کریں" else "Grade Answer (AI)")
                    }
                  }
                }
              }
            }
          }

          // Grading Results Card
          if (uiState.writtenResult != null) {
            val result = uiState.writtenResult
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("written_result_card")
              ) {
                Column(modifier = Modifier.padding(18.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Awarded Score:",
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp,
                      color = Emerald900
                    )
                    Surface(
                      color = Emerald800,
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text(
                        text = "${result.score} / ${result.maxMarks} Marks",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    text = "Examiner Comments:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Emerald800
                  )
                  Text(text = result.feedback, fontSize = 13.sp, color = Color(0xFF334155))

                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    text = "Model Board Answer:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Amber500
                  )
                  Text(text = result.modelAnswer, fontSize = 13.sp, color = Color(0xFF475569))

                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    text = "Mistakes & Improvement Tips:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFDC2626)
                  )
                  Text(text = result.mistakesExplanation, fontSize = 13.sp, color = Color(0xFF991B1B))
                }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }

      2 -> {
        // --- STEP 3: ORAL VIVA TEST ---
        val sampleOralPrompt = "Explain how gaseous exchange takes place in human alveoli and the role of hemoglobin."
        val urduOralPrompt = "انسانوں کے ایلویولائی میں گیسوں کا تبادلہ اور ہیموگلوبن کا کردار بیان کریں۔"

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                  color = Color(0xFF0284C7),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "Oral Viva Examiner (10 Marks)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = if (isUrdu) urduOralPrompt else sampleOralPrompt,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Emerald900,
                  lineHeight = 22.sp
                )
              }
            }
          }

          // Voice / Recording Box
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Surface(
                  shape = CircleShape,
                  color = Emerald100,
                  modifier = Modifier.size(64.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = Icons.Default.Mic,
                      contentDescription = "Microphone",
                      tint = Emerald900,
                      modifier = Modifier.size(32.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = if (isUrdu) "بولیں اور اپنا جواب ریکارڈ کرائیں" else "Speak your answer or review transcript below:",
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp,
                  color = Emerald900
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = uiState.oralTranscript,
                  onValueChange = { onUpdateOralTranscript(it) },
                  label = { Text("Spoken Transcript (Speech-to-Text)") },
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = { onGradeOralTest(sampleOralPrompt) },
                  enabled = !uiState.isOralGrading && uiState.oralTranscript.isNotBlank(),
                  colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.testTag("grade_oral_btn")
                ) {
                  if (uiState.isOralGrading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Evaluating Viva...")
                  } else {
                    Text(if (isUrdu) "زبانی ٹیسٹ ایویلیوایشن لیں" else "Evaluate Verbal Response")
                  }
                }
              }
            }
          }

          // Oral Result Card
          if (uiState.oralResult != null) {
            val oral = uiState.oralResult
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(18.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Oral Viva Score:",
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp,
                      color = Emerald900
                    )
                    Text(
                      text = "${oral.score} / ${oral.maxMarks}",
                      fontWeight = FontWeight.Bold,
                      fontSize = 18.sp,
                      color = Emerald800
                    )
                  }
                  Spacer(modifier = Modifier.height(8.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                  ) {
                    Text(
                      text = "Clarity: ${oral.clarityScore}/10",
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFF0284C7)
                    )
                    Text(
                      text = "Completeness: ${oral.completenessScore}/10",
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Amber500
                    )
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  Text(text = oral.feedback, fontSize = 13.sp, color = Color(0xFF334155))
                }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }

      3 -> {
        // --- TAB 4: MOCK EXAMS SIMULATOR ---
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Warning, contentDescription = "Anti-Cheat", tint = Amber500)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = if (isUrdu) "اینٹی چیٹ اور فل سکرین سیشن" else "Punjab Board Anti-Cheat Protocol Active",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF92400E)
                  )
                  Text(
                    text = "Tab switching and backgrounding are logged during official mock exams.",
                    fontSize = 11.sp,
                    color = Color(0xFF78350F)
                  )
                }
              }
            }
          }

          items(PunjabBoardSeedData.mockExams.size) { idx ->
            val exam = PunjabBoardSeedData.mockExams[idx]
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = if (exam.isLiveScheduled) Color(0xFFDCFCE7) else Color(0xFFE2E8F0),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = if (exam.isLiveScheduled) "🔴 LIVE SCHEDULED" else "ON-DEMAND",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (exam.isLiveScheduled) Emerald900 else Color(0xFF475569),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                  Text(
                    text = "${exam.enrolledStudentsCount} Students Registered",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = if (isUrdu) exam.urduTitle else exam.title,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = Emerald900
                )
                Text(
                  text = "Pattern: Section A (MCQs) + Section B (Short Qs) + Section C (Long Qs)",
                  fontSize = 12.sp,
                  color = Color(0xFF475569)
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Total Marks: ${exam.totalMarks}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Time: ${exam.durationMinutes} mins", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  }
                  Button(
                    onClick = { /* Start mock simulation */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text("Sit Mock Exam", fontSize = 12.sp)
                  }
                }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }
    }
  }
}
