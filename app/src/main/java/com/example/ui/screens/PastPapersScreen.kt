package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PastPaperQuestion
import com.example.data.models.PunjabBoard
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.UiState

@Composable
fun PastPapersScreen(
  uiState: UiState,
  onBoardFilterSelected: (String) -> Unit,
  onYearFilterSelected: (Int) -> Unit,
  onSearchAiQuestion: (String) -> Unit
) {
  val isUrdu = uiState.language == AppLanguage.URDU

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    item { Spacer(modifier = Modifier.height(6.dp)) }

    // AI Question Finder Hero Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("ai_question_finder_card")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "AI Finder",
              tint = Emerald800
            )
            Text(
              text = if (isUrdu) "اے آئی سوال فائنڈر (پنجاب بورڈ تکرار کا امکان)" else "AI Question Finder & Repeat Probability",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Emerald900
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (isUrdu)
              "پوچھیں: 'کیا یہ سوال پنجاب بورڈ میں آیا تھا؟ کن سالوں میں؟' اور تکرار کا تخمینہ حاصل کریں۔"
            else
              "Ask: 'Did this question come in Punjab Board? Which years?' to check repeat frequency across 20 years of BISE papers.",
            fontSize = 12.sp,
            color = Color(0xFF475569)
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = uiState.aiQuestionSearchQuery,
            onValueChange = { onSearchAiQuestion(it) },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = Emerald800)
            },
            placeholder = {
              Text(
                text = "e.g., 'Equations of motion' or 'Avogadro number' or 'Alveoli'",
                fontSize = 13.sp
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("ai_question_search_input"),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }
    }

    // Board Filter Chips
    item {
      Text(
        text = if (isUrdu) "پنجاب کے تعلیمی بورڈز منتخب کریں:" else "Select BISE Educational Board:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Emerald900
      )
    }

    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(PunjabBoard.values()) { board ->
          val isSelected = uiState.selectedBoardFilter == board.displayName
          FilterChip(
            selected = isSelected,
            onClick = { onBoardFilterSelected(board.displayName) },
            label = {
              Text(
                text = if (isUrdu) board.urduName else board.displayName,
                fontSize = 12.sp,
                fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Emerald800,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    // Year Filter Chips (2004 - 2024)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isUrdu) "سال منتخب کریں (پچھلے 20 سال):" else "Year Archives (2004 - 2024):",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Emerald900
        )
      }
    }

    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items((2024 downTo 2012).toList()) { yr ->
          val isSelected = uiState.selectedYearFilter == yr
          Surface(
            color = if (isSelected) Amber500 else Color.White,
            shape = RoundedCornerShape(8.dp),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.clickable { onYearFilterSelected(yr) }
          ) {
            Text(
              text = "$yr",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) Color.Black else Color(0xFF334155),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    // Trend Analysis Banner
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.TrendingUp, contentDescription = "Trend", tint = Color(0xFF0284C7))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = if (isUrdu) "سب سے زیادہ دہرائے جانے والے ابواب:" else "Most-Repeated Chapters (Lahore & Rawalpindi):",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF0F172A)
            )
            Text(
              text = "Kinematics (88% freq), Dynamics (82% freq), Gaseous Exchange (79% freq)",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        }
      }
    }

    // Past Paper Questions List
    item {
      Text(
        text = if (isUrdu) "ماضی کے سوالات و تکرار کی شرح:" else "Questions with BISE Board Recurrence & Repeat Probabilities:",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Emerald900
      )
    }

    items(uiState.aiFoundQuestions) { q ->
      PastPaperQuestionCard(question = q, isUrdu = isUrdu)
    }

    item { Spacer(modifier = Modifier.height(80.dp)) }
  }
}

@Composable
fun PastPaperQuestionCard(
  question: PastPaperQuestion,
  isUrdu: Boolean
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = CardDefaults.outlinedCardBorder(),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = Color(0xFFEFF6FF),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "${question.subject} • ${question.chapter}",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D4ED8),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Repeat Probability Badge
        Surface(
          color = if (question.repeatProbability >= 85) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "🔥 ${question.repeatProbability}% Repeat Probability",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (question.repeatProbability >= 85) Emerald900 else Color(0xFF92400E),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = if (isUrdu) question.urduQuestionText else question.questionText,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.bodyLarge.fontFamily,
        color = Color(0xFF0F172A),
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Repeated in Years: ${question.yearsRepeated.joinToString(", ")}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
          )
          Text(
            text = "Boards: ${question.boardNames.joinToString(", ")}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
        }

        Surface(
          color = Color(0xFFF1F5F9),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "${question.marks} Marks",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}
