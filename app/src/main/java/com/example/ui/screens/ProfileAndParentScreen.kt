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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.models.TutorItem
import com.example.data.seed.PunjabBoardSeedData
import com.example.ui.theme.Amber500
import com.example.ui.theme.CoinGold
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.StreakOrange
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.UiState

@Composable
fun ProfileAndParentScreen(
  uiState: UiState,
  onSelectTutor: (TutorItem) -> Unit,
  onBookTutor: (TutorItem, String, String) -> Unit,
  onUpgradeProClick: () -> Unit,
  onSignOut: () -> Unit,
  onSignInWithGoogle: () -> Unit
) {
  val isUrdu = uiState.language == AppLanguage.URDU
  val profile = uiState.userProfile
  var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Student Profile, 1: Parent Dashboard, 2: 1-on-1 Tutors

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    // 3 Tab selector: Profile, Parent, Tutors
    TabRow(
      selectedTabIndex = selectedSubTab,
      containerColor = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
    ) {
      val tabs = listOf("Student Profile", "Parent Portal", "Book Tutors")
      val urduTabs = listOf("طلبہ پروفائل", "والدین پورٹل", "ٹیوٹرز بکنگ")
      tabs.forEachIndexed { idx, title ->
        Tab(
          selected = selectedSubTab == idx,
          onClick = { selectedSubTab = idx },
          text = {
            Text(
              text = if (isUrdu) urduTabs[idx] else title,
              fontSize = 12.sp,
              fontWeight = if (selectedSubTab == idx) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedSubTab == idx) Emerald900 else Color(0xFF64748B)
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    when (selectedSubTab) {
      0 -> {
        // --- STUDENT PROFILE & COIN REWARDS ---
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          // User Card
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                  Surface(
                    shape = CircleShape,
                    color = Emerald100,
                    modifier = Modifier.size(56.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(text = if (uiState.activeMascot == "mina") "👩‍🎓" else "👨‍🎓", fontSize = 30.sp)
                    }
                  }
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = profile.name,
                      style = MaterialTheme.typography.titleLarge,
                      fontWeight = FontWeight.Bold,
                      color = Emerald900
                    )
                    Text(
                      text = "${profile.grade} • ${profile.group} • ${profile.targetBoard}",
                      fontSize = 13.sp,
                      color = Color(0xFF64748B)
                    )
                  }
                  if (uiState.isProPlanActive) {
                    Surface(color = Amber500, shape = RoundedCornerShape(6.dp)) {
                      Text(
                        text = "PRO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text("Punjab Coins", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("🪙 ${profile.coins}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                  }
                  Column {
                    Text("Daily Streak", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("🔥 ${profile.streakDays} Days", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StreakOrange)
                  }
                  Column {
                    Text("Total XP", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("⚡ ${profile.xp} XP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Emerald800)
                  }
                  Column {
                    Text("Rank Level", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("Level ${profile.level}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                  }
                }
              }
            }
          }

          // Streak Freeze & Coins Perks
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(Icons.Default.AcUnit, contentDescription = "Freeze", tint = Color(0xFF0284C7))
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = if (isUrdu) "سٹریک فریز پروٹیکشن" else "Streak Freeze Protection",
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      color = Color(0xFF92400E)
                    )
                    Text(
                      text = if (isUrdu) "1 سٹریک فریز محفوظ ہے۔" else "1 Freeze active (Prevents streak reset if you miss a day)",
                      fontSize = 11.sp,
                      color = Color(0xFF78350F)
                    )
                  }
                }
                Surface(
                  color = Amber500,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.clickable { /* Buy freeze */ }
                ) {
                  Text(
                    text = "100 🪙",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }

          // Pro Plan Promotion / JazzCash / Easypaisa
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Emerald100),
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
                    text = if (isUrdu) "پنجاب پریپ پرو سیشن" else "PunjabPrep Pro Membership",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Emerald900
                  )
                  Surface(color = Emerald800, shape = RoundedCornerShape(6.dp)) {
                    Text(
                      text = "PKR 1,499/mo",
                      color = Color.White,
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Unlock all video lectures, unlimited Gemini subjective AI test grading, 1-on-1 tutor discounts, and live mock exams.",
                  fontSize = 12.sp,
                  color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Button(
                    onClick = onUpgradeProClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("upgrade_pro_btn")
                  ) {
                    Icon(Icons.Default.CreditCard, contentDescription = "Pay", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (uiState.isProPlanActive) "Pro Active ✓" else "Pay via JazzCash / Easypaisa", fontSize = 12.sp)
                  }
                }
              }
            }
          }

          // Auth / Sign In / Sign Out
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Firebase Account & Cloud Sync",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Emerald900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = if (profile.email.isNotBlank()) "Signed in as: ${profile.email}" else "Exploring in offline guest mode. Sign in with Google to sync scores across devices.",
                  fontSize = 12.sp,
                  color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  if (profile.email.isBlank()) {
                    Button(
                      onClick = onSignInWithGoogle,
                      colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.testTag("google_signin_btn")
                    ) {
                      Text("Sign in with Google")
                    }
                  } else {
                    OutlinedButton(
                      onClick = onSignOut,
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text("Sign Out", color = Color.Red)
                    }
                  }
                }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }

      1 -> {
        // --- PARENT DASHBOARD ---
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          // Invite Code Linking
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.FamilyRestroom, contentDescription = "Parent Link", tint = Emerald800)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isUrdu) "محفوظ پیرنٹ لنکنگ کوڈ" else "Secure Parent Invite Link Code",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Emerald900
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Share this 6-character code with your parents so they can observe study hours, attendance, and weekly test scores from their phone in read-only mode.",
                  fontSize = 12.sp,
                  color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                  color = Emerald100,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "Code: ${profile.parentInviteCode}",
                      fontWeight = FontWeight.Bold,
                      fontSize = 18.sp,
                      color = Emerald900
                    )
                    OutlinedButton(
                      onClick = { /* Share invite */ },
                      shape = RoundedCornerShape(6.dp)
                    ) {
                      Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("WhatsApp", fontSize = 11.sp, color = Emerald800)
                    }
                  }
                }
              }
            }
          }

          // Real-time Child Metrics
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Weekly Performance Overview (Week 1, Oct 2026):",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Emerald900
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text("Lecture Attendance", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("94%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Emerald800)
                  }
                  Column {
                    Text("Avg Quiz Score", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("87%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Amber500)
                  }
                  Column {
                    Text("Study Hours", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text("18.5 hrs", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                  text = "⚠️ Weak Topics Requiring Focus:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Color(0xFFDC2626)
                )
                Text(
                  text = "Chemistry Chapter 4: Chemical Bonding; Physics: Projectile Motion",
                  fontSize = 12.sp,
                  color = Color(0xFF7F1D1D)
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = "✓ Mastered Strong Topics:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Emerald800
                )
                Text(
                  text = "Biology: Cell Cycle; Mathematics: Quadratic Equations",
                  fontSize = 12.sp,
                  color = Color(0xFF065F46)
                )

                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                  color = Color(0xFFF8FAFC),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "Mina's AI Advice for Parents: Ali is showing remarkable discipline with a 7-day streak. Recommending 30 minutes of numericals revision on Sunday before the mock exam.",
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(10.dp)
                  )
                }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(80.dp)) }
        }
      }

      2 -> {
        // --- 1-ON-1 VERIFIED TUTOR BOOKING ---
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Text(
              text = if (isUrdu) "تصدیق شدہ پنجاب بورڈ ٹیوٹرز (1-on-1 سیشن)" else "Verified Punjab Board Tutors (1-to-1 Live Sessions)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Emerald900
            )
          }

          items(PunjabBoardSeedData.sampleTutors.size) { idx ->
            val tutor = PunjabBoardSeedData.sampleTutors[idx]
            Card(
              shape = RoundedCornerShape(16.dp),
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
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                      shape = CircleShape,
                      color = Emerald100,
                      modifier = Modifier.size(46.dp)
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(text = "👨‍🏫", fontSize = 24.sp)
                      }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = if (isUrdu) tutor.urduName else tutor.name,
                          fontWeight = FontWeight.Bold,
                          fontSize = 15.sp,
                          color = Emerald900
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                          imageVector = Icons.Default.Verified,
                          contentDescription = "Verified",
                          tint = Emerald800,
                          modifier = Modifier.size(16.dp)
                        )
                      }
                      Text(
                        text = if (isUrdu) tutor.urduTitle else tutor.title,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                      )
                    }
                  }

                  Text(
                    text = "PKR ${tutor.hourlyRatePkr}/hr",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Emerald800
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Amber500, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${tutor.rating} (${tutor.reviewsCount} reviews)", fontSize = 12.sp, color = Color(0xFF475569))
                  }
                  Text("Slots: ${tutor.availability}", fontSize = 11.sp, color = Color(0xFF0284C7))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = {
                    onBookTutor(tutor, "Tomorrow", "6:00 PM PST")
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text("Book 1-on-1 Doubt Clarification Session", fontSize = 12.sp)
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
