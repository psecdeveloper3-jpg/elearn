package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber500
import com.example.ui.theme.CoinGold
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.StreakOrange
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.NavigationTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PunjabPrepTopBar(
  language: AppLanguage,
  streakDays: Int,
  coins: Int,
  activeMascot: String,
  isPro: Boolean,
  onToggleLanguage: () -> Unit,
  onToggleMascot: () -> Unit,
  onUpgradeProClick: () -> Unit
) {
  val isUrdu = language == AppLanguage.URDU

  TopAppBar(
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(
                listOf(Emerald800, Color(0xFF15803D))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = "PunjabPrep Logo",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (isUrdu) "پنجاب پریپ" else "PunjabPrep",
              style = MaterialTheme.typography.titleLarge,
              fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.titleLarge.fontFamily,
              fontWeight = FontWeight.Bold,
              color = Emerald900
            )
            if (isPro) {
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Amber500,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "PRO",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.Black,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          }
          Text(
            text = if (isUrdu) "پنجاب بورڈ (جماعت 9 تا 12)" else "BISE Punjab Board 9th-12th",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily,
            color = Color(0xFF64748B)
          )
        }
      }
    },
    actions = {
      // Streak Pill
      Surface(
        color = Color(0xFFFFF7ED),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.padding(end = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Streak",
            tint = StreakOrange,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "$streakDays",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = StreakOrange
          )
        }
      }

      // Coins Pill
      Surface(
        color = Color(0xFFFEFCE8),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.padding(end = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.MonetizationOn,
            contentDescription = "Punjab Coins",
            tint = CoinGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "$coins",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFFB45309)
          )
        }
      }

      // Mascot Selector Button
      Surface(
        color = Emerald100,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .padding(end = 6.dp)
          .clickable { onToggleMascot() }
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = if (activeMascot == "mina") "👩‍🏫 Mina" else "👨‍🏫 Wiki",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Emerald900
          )
        }
      }

      // Language Toggle (EN / اردو)
      IconButton(
        onClick = onToggleLanguage,
        modifier = Modifier.testTag("language_toggle_btn")
      ) {
        Surface(
          shape = CircleShape,
          color = Color(0xFFE2E8F0),
          modifier = Modifier.size(34.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = if (isUrdu) "EN" else "اردو",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Emerald900
            )
          }
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.White
    )
  )
}

@Composable
fun PunjabPrepBottomBar(
  currentTab: NavigationTab,
  language: AppLanguage,
  onTabSelected: (NavigationTab) -> Unit
) {
  val isUrdu = language == AppLanguage.URDU

  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 8.dp
  ) {
    NavigationBarItem(
      selected = currentTab == NavigationTab.HOME,
      onClick = { onTabSelected(NavigationTab.HOME) },
      icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
      label = {
        Text(
          text = if (isUrdu) "ہوم" else "Home",
          fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Emerald900,
        indicatorColor = Emerald100
      ),
      modifier = Modifier.testTag("nav_home_tab")
    )
    NavigationBarItem(
      selected = currentTab == NavigationTab.LEARN,
      onClick = { onTabSelected(NavigationTab.LEARN) },
      icon = { Icon(Icons.Default.MenuBook, contentDescription = "Learn") },
      label = {
        Text(
          text = if (isUrdu) "کتب و ویڈیوز" else "Learn",
          fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Emerald900,
        indicatorColor = Emerald100
      ),
      modifier = Modifier.testTag("nav_learn_tab")
    )
    NavigationBarItem(
      selected = currentTab == NavigationTab.PRACTICE,
      onClick = { onTabSelected(NavigationTab.PRACTICE) },
      icon = { Icon(Icons.Default.EditNote, contentDescription = "Practice") },
      label = {
        Text(
          text = if (isUrdu) "مشق و ٹیسٹ" else "Practice",
          fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Emerald900,
        indicatorColor = Emerald100
      ),
      modifier = Modifier.testTag("nav_practice_tab")
    )
    NavigationBarItem(
      selected = currentTab == NavigationTab.PAST_PAPERS,
      onClick = { onTabSelected(NavigationTab.PAST_PAPERS) },
      icon = { Icon(Icons.Default.HistoryEdu, contentDescription = "Past Papers") },
      label = {
        Text(
          text = if (isUrdu) "ماضی کے پرچے" else "Past Papers",
          fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Emerald900,
        indicatorColor = Emerald100
      ),
      modifier = Modifier.testTag("nav_past_papers_tab")
    )
    NavigationBarItem(
      selected = currentTab == NavigationTab.PROFILE,
      onClick = { onTabSelected(NavigationTab.PROFILE) },
      icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
      label = {
        Text(
          text = if (isUrdu) "پروفائل" else "Profile",
          fontFamily = if (isUrdu) UrduFontFamily else MaterialTheme.typography.labelSmall.fontFamily
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Emerald900,
        indicatorColor = Emerald100
      ),
      modifier = Modifier.testTag("nav_profile_tab")
    )
  }
}

@Composable
fun FloatingMascotAssistantFab(
  activeMascot: String,
  onClick: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "mascot_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mascot_pulse_scale"
  )

  FloatingActionButton(
    onClick = onClick,
    containerColor = Emerald800,
    contentColor = Color.White,
    shape = CircleShape,
    modifier = Modifier
      .scale(pulseScale)
      .testTag("floating_mascot_fab")
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
      Text(
        text = if (activeMascot == "mina") "👩‍🏫" else "👨‍🏫",
        fontSize = 24.sp
      )
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(
          text = if (activeMascot == "mina") "Ask Mina" else "Ask Wiki",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color.White
        )
        Text(
          text = "AI Guide",
          fontSize = 10.sp,
          color = Amber500
        )
      }
    }
  }
}
