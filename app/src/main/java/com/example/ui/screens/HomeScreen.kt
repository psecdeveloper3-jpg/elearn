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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SubjectInfo
import com.example.data.seed.PunjabBoardSeedData
import com.example.ui.theme.Amber500
import com.example.ui.theme.CoinGold
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.StreakOrange
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.NavigationTab
import com.example.ui.viewmodels.UiState

@Composable
fun HomeScreen(
  uiState: UiState,
  onSubjectSelected: (SubjectInfo) -> Unit,
  onNavigateTab: (NavigationTab) -> Unit,
  onStartPomodoro: () -> Unit,
  onOpenAiAssistant: () -> Unit
) {
  val isUrdu = uiState.language == AppLanguage.URDU
  val profile = uiState.userProfile

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item { Spacer(modifier = Modifier.height(4.dp)) }

    // 1. Welcome Hero Banner
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("home_hero_card")
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                listOf(Emerald900, Emerald800, Color(0xFF166534))
              )
            )
            .padding(20.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (isUrdu) "خوش آمدید، ${profile.name}!" else "Welcome back, ${profile.name}!",
                  style = MaterialTheme.typography.headlineMedium,
                  fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.headlineMedium.fontFamily,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${profile.grade} • ${profile.group} • ${profile.targetBoard}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Emerald100
                )
              }
              Surface(
                shape = CircleShape,
                color = Color(0x33FFFFFF),
                modifier = Modifier.size(46.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = if (uiState.activeMascot == "mina") "👩‍🏫" else "👨‍🏫",
                    fontSize = 24.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Board Exam Countdown Chip
            Surface(
              color = Color(0x22FFFFFF),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Schedule,
                  contentDescription = "Countdown",
                  tint = Amber500,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isUrdu) "پنجاب بورڈ سالانہ امتحانات 2026: 142 دن باقی" else "BISE Annual Board Exams 2026: 142 Days Remaining",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }

    // 2. Metrics Rings & Performance Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        MetricCard(
          title = if (isUrdu) "نصاب مکمل" else "Syllabus",
          value = "76%",
          subtitle = "18/24 Chs",
          color = Emerald800,
          progress = 0.76f,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = if (isUrdu) "سابقہ پرچے" else "Past Papers",
          value = "84%",
          subtitle = "Last 10 Yrs",
          color = Color(0xFF0284C7),
          progress = 0.84f,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = if (isUrdu) "کوئز درستگی" else "Accuracy",
          value = "92%",
          subtitle = "+12% this wk",
          color = Amber500,
          progress = 0.92f,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 3. Sprint Mode (Pomodoro 25-minute focus session)
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("pomodoro_quick_card")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Emerald800),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Timer",
                tint = Color.White
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isUrdu) "سپرنٹ موڈ (25 منٹ توجہ)" else "Sprint Mode (25-Min Focus)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Emerald900
              )
              Text(
                text = if (isUrdu) "5 منٹ برین ٹیزر بریک کے ساتھ" else "With 5-min brain teaser breaks",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }
          }
          Button(
            onClick = onStartPomodoro,
            colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(if (isUrdu) "شروع" else "Start", fontSize = 13.sp)
          }
        }
      }
    }

    // 4. Subjects Carousel / Progression
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isUrdu) "آپ کے مضامین (پنجاب ٹیکسٹ بک بورڈ)" else "Your Subjects (PCTB Curriculum)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = Emerald900
        )
        Text(
          text = if (isUrdu) "سب دیکھیں" else "View All",
          fontSize = 13.sp,
          color = Emerald800,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.clickable { onNavigateTab(NavigationTab.LEARN) }
        )
      }
    }

    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(PunjabBoardSeedData.subjects) { subject ->
          SubjectCard(
            subject = subject,
            isUrdu = isUrdu,
            isSelected = uiState.selectedSubject.id == subject.id,
            onClick = {
              onSubjectSelected(subject)
              onNavigateTab(NavigationTab.LEARN)
            }
          )
        }
      }
    }

    // 5. Spaced Repetition / Forgetting Curve Recommendations
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Spaced Revision",
              tint = Amber500
            )
            Text(
              text = if (isUrdu) "سمارٹ دہرائی (فارگیٹنگ کروو الرٹ)" else "Adaptive Spaced-Revision Alert",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF92400E)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = if (isUrdu)
              "کیمسٹری چیپٹر 1 (مول اور ایووگیڈرو نمبر) 5 دن پہلے پڑھا گیا تھا۔ بھولنے سے بچنے کے لیے آج 10 سوالات کی پریکٹس کریں۔"
            else
              "Chemistry Ch 1 (Mole Concept & Avogadro's Number) was revised 5 days ago. Ebbinghaus curve recommends a quick 10-question drill today to retain 95%+ mastery.",
            fontSize = 13.sp,
            color = Color(0xFF78350F),
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { onNavigateTab(NavigationTab.PRACTICE) },
              colors = ButtonDefaults.buttonColors(containerColor = Amber500),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(if (isUrdu) "ابھی دہرائیں" else "Revise Now (10 MCQs)", color = Color.Black, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 6. Quick AI Assistant Entry
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = if (uiState.activeMascot == "mina") "👩‍🏫" else "👨‍🏫",
              fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (uiState.activeMascot == "mina") "Ask Mina AI Tutor" else "Ask Wiki AI Tutor",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Emerald900
              )
              Text(
                text = if (isUrdu) "کوئی بھی سوال پوچھیں (انگریزی یا اردو)" else "Step-by-step guidance in English or Urdu",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }
          }
          OutlinedButton(
            onClick = onOpenAiAssistant,
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Open Chat", fontSize = 12.sp, color = Emerald800)
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(80.dp)) }
  }
}

@Composable
fun MetricCard(
  title: String,
  value: String,
  subtitle: String,
  color: Color,
  progress: Float,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = CardDefaults.outlinedCardBorder(),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
      Spacer(modifier = Modifier.height(6.dp))
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(CircleShape),
        color = color,
        trackColor = Color(0xFFE2E8F0)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = Color(0xFF94A3B8)
      )
    }
  }
}

@Composable
fun SubjectCard(
  subject: SubjectInfo,
  isUrdu: Boolean,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) Emerald100 else Color.White
    ),
    border = CardDefaults.outlinedCardBorder(),
    modifier = Modifier
      .width(160.dp)
      .clickable { onClick() }
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = subject.iconEmoji, fontSize = 24.sp)
        Text(
          text = "${subject.progressPercent}%",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Emerald800
        )
      }
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = if (isUrdu) subject.urduTitle else subject.title,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.titleMedium.fontFamily,
        color = Emerald900
      )
      Text(
        text = "${subject.completedChapters}/${subject.totalChapters} Chapters",
        fontSize = 11.sp,
        color = Color(0xFF64748B)
      )
      Spacer(modifier = Modifier.height(8.dp))
      LinearProgressIndicator(
        progress = { subject.progressPercent / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(CircleShape),
        color = Emerald800,
        trackColor = Color(0xFFE2E8F0)
      )
    }
  }
}
