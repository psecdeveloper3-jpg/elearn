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
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.models.TextbookChapter
import com.example.data.models.VideoLecture
import com.example.data.seed.PunjabBoardSeedData
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.UiState

@Composable
fun LearnScreen(
  uiState: UiState,
  onSubjectSelected: (SubjectInfo) -> Unit,
  onChapterSelected: (TextbookChapter) -> Unit,
  onLectureSelected: (VideoLecture) -> Unit,
  onSetPlaybackSpeed: (Float) -> Unit,
  onUpgradeProClick: () -> Unit
) {
  val isUrdu = uiState.language == AppLanguage.URDU
  var activeSectionTab by remember { mutableIntStateOf(0) } // 0: Textbooks & Notes, 1: Video Lectures

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    // Subjects Horizontal Scroll
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(PunjabBoardSeedData.subjects) { subject ->
        val isSelected = uiState.selectedSubject.id == subject.id
        FilterChip(
          selected = isSelected,
          onClick = { onSubjectSelected(subject) },
          label = {
            Text(
              text = "${subject.iconEmoji} ${if (isUrdu) subject.urduTitle else subject.title}",
              fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelMedium.fontFamily,
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

    Spacer(modifier = Modifier.height(10.dp))

    // Segment Tab: Books vs Video Lectures
    TabRow(
      selectedTabIndex = activeSectionTab,
      containerColor = Color(0xFFF1F5F9),
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
    ) {
      Tab(
        selected = activeSectionTab == 0,
        onClick = { activeSectionTab = 0 },
        text = {
          Text(
            text = if (isUrdu) "درسی کتب و نوٹس (مفت)" else "Textbooks & Notes (Free)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (activeSectionTab == 0) Emerald900 else Color(0xFF64748B)
          )
        }
      )
      Tab(
        selected = activeSectionTab == 1,
        onClick = { activeSectionTab = 1 },
        text = {
          Text(
            text = if (isUrdu) "ویڈیو لیکچرز (پرو)" else "Video Lectures (Pro)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (activeSectionTab == 1) Emerald900 else Color(0xFF64748B)
          )
        }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    if (activeSectionTab == 0) {
      // --- Textbooks & Notes View ---
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        // Chapters selector chips
        item {
          Text(
            text = if (isUrdu) "ابواب (پنجاب کریکولم اینڈ ٹیکسٹ بک بورڈ)" else "Chapters (Punjab Curriculum & Textbook Board)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Emerald900
          )
        }

        item {
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(PunjabBoardSeedData.chapters.filter { it.subjectId == uiState.selectedSubject.id }.ifEmpty { PunjabBoardSeedData.chapters }) { ch ->
              val isSelected = uiState.selectedChapter.id == ch.id
              Surface(
                color = if (isSelected) Emerald100 else Color.White,
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                  .clickable { onChapterSelected(ch) }
                  .padding(vertical = 2.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Text(
                    text = "Ch ${ch.chapterNumber}: ${if (isUrdu) ch.urduTitle else ch.title}",
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Emerald900 else Color(0xFF1E293B)
                  )
                  if (ch.isDownloaded) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                      imageVector = Icons.Default.DownloadDone,
                      contentDescription = "Offline Available",
                      tint = Emerald800,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // Active Chapter Reader View
        val chapter = uiState.selectedChapter
        item {
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
                Text(
                  text = "Chapter ${chapter.chapterNumber}: ${if (isUrdu) chapter.urduTitle else chapter.title}",
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Bold,
                  color = Emerald900
                )
                Surface(
                  color = Emerald100,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = if (isUrdu) "آف لائن دستیاب" else "Offline Ready",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald900,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isUrdu) "خلاصہ باب (Summary):" else "Chapter Summary:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Emerald800
              )
              Text(
                text = if (isUrdu) chapter.urduSummary else chapter.summary,
                fontSize = 14.sp,
                color = Color(0xFF334155),
                lineHeight = 20.sp
              )

              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = if (isUrdu) "اہم تعریفات (Key Definitions):" else "Key Definitions for Exam:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Emerald800
              )
              chapter.keyDefinitions.forEach { def ->
                Surface(
                  color = Color(0xFFF8FAFC),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                ) {
                  Text(
                    text = "• $def",
                    fontSize = 13.sp,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(10.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = if (isUrdu) "اہم فارمولے (Important Formulas):" else "Core Formulas & Equations:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Amber500
              )
              chapter.formulas.forEach { formula ->
                Surface(
                  color = Color(0xFFFFFBEB),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                ) {
                  Text(
                    text = "📐 $formula",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF78350F),
                    modifier = Modifier.padding(10.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = if (isUrdu) "پنجاب بورڈ مختصر سوالات گائیڈ:" else "Punjab Board Short Questions Guide:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Emerald800
              )
              chapter.shortQuestionsGuide.forEach { q ->
                Text(
                  text = "Q: $q",
                  fontSize = 13.sp,
                  color = Color(0xFF475569),
                  modifier = Modifier.padding(vertical = 3.dp)
                )
              }
            }
          }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
      }
    } else {
      // --- Video Lectures View ---
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        // Video Player simulation card
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("video_player_card")
          ) {
            Column {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(190.dp)
                  .background(
                    Brush.verticalGradient(
                      listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Surface(
                    shape = CircleShape,
                    color = Amber500,
                    modifier = Modifier.size(54.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Lecture",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = uiState.selectedLecture?.title ?: "Select a lecture to play",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                  Text(
                    text = "Adaptive Quality (480p / 720p / Low-bandwidth Pakistan optimized)",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                  )
                }
              }

              // Playback speed selector
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color(0xFF1E293B))
                .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "Speed",
                    tint = Amber500,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(text = "Speed:", color = Color.White, fontSize = 12.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                    val isSelected = uiState.lecturePlaybackSpeed == speed
                    Surface(
                      color = if (isSelected) Amber500 else Color(0xFF334155),
                      shape = RoundedCornerShape(6.dp),
                      modifier = Modifier.clickable { onSetPlaybackSpeed(speed) }
                    ) {
                      Text(
                        text = "${speed}x",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }

        // Lecture list
        item {
          Text(
            text = if (isUrdu) "دستیاب ویڈیو لیکچرز (ماہر اساتذہ)" else "Lectures by Top Punjab Board Mentors",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Emerald900
          )
        }

        items(PunjabBoardSeedData.videoLectures) { lecture ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                if (lecture.isPro && !uiState.isProPlanActive) {
                  onUpgradeProClick()
                } else {
                  onLectureSelected(lecture)
                }
              }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = Emerald100,
                  modifier = Modifier.size(44.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = if (lecture.isPro) Icons.Default.Lock else Icons.Default.Videocam,
                      contentDescription = "Video",
                      tint = if (lecture.isPro) Amber500 else Emerald900
                    )
                  }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = if (isUrdu) lecture.urduTitle else lecture.title,
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      color = Emerald900,
                      maxLines = 1
                    )
                    if (lecture.isPro) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Surface(
                        color = Amber500,
                        shape = RoundedCornerShape(4.dp)
                      ) {
                        Text(
                          text = "PRO",
                          fontSize = 9.sp,
                          fontWeight = FontWeight.Black,
                          color = Color.Black,
                          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                      }
                    }
                  }
                  Text(
                    text = "${lecture.instructorName} • ${lecture.durationMinutes} mins",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                  )
                }
              }

              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Emerald800
              )
            }
          }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
      }
    }
  }
}
