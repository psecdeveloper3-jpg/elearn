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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.models.GradeLevel
import com.example.data.models.PunjabBoard
import com.example.data.models.StudyGroup
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage

@Composable
fun OnboardingScreen(
  language: AppLanguage,
  onComplete: (grade: String, group: String, board: String, guide: String) -> Unit,
  onSignInWithGoogle: () -> Unit
) {
  val isUrdu = language == AppLanguage.URDU

  var selectedGrade by remember { mutableStateOf("10th") }
  var selectedGroup by remember { mutableStateOf("Pre-Medical") }
  var selectedBoard by remember { mutableStateOf("BISE Lahore") }
  var selectedGuide by remember { mutableStateOf("mina") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item { Spacer(modifier = Modifier.height(20.dp)) }

    // Logo & Header
    item {
      Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Emerald800,
          modifier = Modifier.size(64.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Default.School, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(36.dp))
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = if (isUrdu) "پنجاب پریپ میں خوش آمدید" else "Welcome to PunjabPrep",
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.Bold,
          color = Emerald900
        )
        Text(
          text = if (isUrdu) "پنجاب بورڈ جماعت 9 تا 12 کے لیے سمارٹ تیاری" else "Personalized AI Study Platform for BISE Punjab Board (Grades 9-12)",
          fontSize = 13.sp,
          color = Color(0xFF64748B)
        )
      }
    }

    // 1. Grade Selection
    item {
      Text(
        text = if (isUrdu) "1. اپنی جماعت منتخب کریں:" else "1. Select Your Class / Grade:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Emerald900
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("9th", "10th", "11th", "12th").forEach { g ->
          val isSel = selectedGrade == g
          Surface(
            color = if (isSel) Emerald800 else Color(0xFFF1F5F9),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { selectedGrade = g }
          ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 12.dp)) {
              Text(
                text = g,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isSel) Color.White else Color(0xFF334155)
              )
            }
          }
        }
      }
    }

    // 2. Study Group / Stream Selection
    item {
      Text(
        text = if (isUrdu) "2. اپنا گروپ منتخب کریں:" else "2. Select Your Study Stream:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Emerald900
      )
      Spacer(modifier = Modifier.height(8.dp))
      val groups = listOf("Pre-Medical", "Pre-Engineering", "ICS", "General Science", "Arts")
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        groups.forEach { grp ->
          val isSel = selectedGroup == grp
          Surface(
            color = if (isSel) Emerald100 else Color.White,
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedGroup = grp }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = grp,
                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSel) Emerald900 else Color(0xFF1E293B)
              )
              if (isSel) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = Emerald800, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }

    // 3. Board Selection
    item {
      Text(
        text = if (isUrdu) "3. تعلیمی بورڈ منتخب کریں:" else "3. Select BISE Educational Board:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Emerald900
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(PunjabBoard.values()) { board ->
          val isSel = selectedBoard == board.displayName
          Surface(
            color = if (isSel) Emerald800 else Color(0xFFF8FAFC),
            shape = RoundedCornerShape(8.dp),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.clickable { selectedBoard = board.displayName }
          ) {
            Text(
              text = if (isUrdu) board.urduName else board.displayName,
              fontSize = 12.sp,
              fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
              color = if (isSel) Color.White else Color(0xFF334155),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
          }
        }
      }
    }

    // 4. Mascot Guide Selection
    item {
      Text(
        text = if (isUrdu) "4. اپنا ذاتی اے آئی رہنما منتخب کریں:" else "4. Choose Your Animated AI Guide:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Emerald900
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Mina Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (selectedGuide == "mina") Emerald100 else Color.White
          ),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .weight(1f)
            .clickable { selectedGuide = "mina" }
        ) {
          Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "👩‍🏫", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("MINA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Emerald900)
            Text("Warm, step-by-step female mentor", fontSize = 11.sp, color = Color(0xFF64748B), maxLines = 2)
          }
        }

        // Wiki Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (selectedGuide == "wiki") Emerald100 else Color.White
          ),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .weight(1f)
            .clickable { selectedGuide = "wiki" }
        ) {
          Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "👨‍🏫", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("WIKI", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Emerald900)
            Text("Energetic, conceptual male mentor", fontSize = 11.sp, color = Color(0xFF64748B), maxLines = 2)
          }
        }
      }
    }

    // 5. Submit & Google Sign In Button
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Button(
        onClick = {
          onComplete(selectedGrade, selectedGroup, selectedBoard, selectedGuide)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("complete_onboarding_btn")
      ) {
        Text(
          text = if (isUrdu) "شروع کریں (سٹڈی پلان تیار کریں)" else "Start Studying (Personalize Plan)",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
      OutlinedButton(
        onClick = onSignInWithGoogle,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("onboarding_google_signin_btn")
      ) {
        Text("Sign in with Google Account", fontSize = 13.sp, color = Emerald900)
      }
    }

    item { Spacer(modifier = Modifier.height(30.dp)) }
  }
}
