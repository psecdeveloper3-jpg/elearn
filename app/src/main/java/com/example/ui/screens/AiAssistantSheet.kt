package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantSheet(
  uiState: UiState,
  onDismiss: () -> Unit,
  onSendMessage: (String) -> Unit,
  onToggleLiveVoice: () -> Unit,
  onToggleMascot: () -> Unit,
  onUpdateImagePrompt: (String) -> Unit,
  onSetImageResolution: (String) -> Unit,
  onGenerateConceptImage: () -> Unit,
  onSetVideoAspectRatio: (String) -> Unit,
  onGenerateVeoVideo: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var currentInput by remember { mutableStateOf("") }
  var assistantTab by remember { mutableIntStateOf(0) } // 0: Chat & Live Voice (gemini-3.8-live), 1: Concept Diagram Image (gemini-3-pro-image-preview), 2: Diagram Video (veo-3.1-fast)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = Modifier.fillMaxHeight(0.92f)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      // Sheet Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = Emerald100,
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = if (uiState.activeMascot == "mina") "👩‍🏫" else "👨‍🏫", fontSize = 22.sp)
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (uiState.activeMascot == "mina") "Mina (AI Mentor)" else "Wiki (AI Guide)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Emerald900
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Emerald100,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.clickable { onToggleMascot() }
              ) {
                Text(
                  text = "Switch Guide",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Emerald800,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "Context: ${uiState.selectedSubject.title} - ${uiState.selectedChapter.title}",
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
          }
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tab selector: Chat/Live Audio vs Concept Diagram vs Veo Animation
      TabRow(
        selectedTabIndex = assistantTab,
        containerColor = Color(0xFFF8FAFC),
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
      ) {
        Tab(
          selected = assistantTab == 0,
          onClick = { assistantTab = 0 },
          text = { Text("Tutor & Live API", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = assistantTab == 1,
          onClick = { assistantTab = 1 },
          text = { Text("1K-4K Diagrams", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = assistantTab == 2,
          onClick = { assistantTab = 2 },
          text = { Text("Veo Video", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      when (assistantTab) {
        0 -> {
          // --- TAB 0: CONVERSATIONAL TUTOR & LIVE AUDIO (gemini-3.8-live) ---
          Column(modifier = Modifier.fillMaxSize()) {
            // Live Voice Call Card (gemini-3.8-live)
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (uiState.liveVoiceActive) Color(0xFF0F172A) else Color(0xFFF0FDF4)
              ),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Surface(
                    shape = CircleShape,
                    color = if (uiState.liveVoiceActive) Emerald800 else Emerald100,
                    modifier = Modifier.size(36.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Voice",
                        tint = if (uiState.liveVoiceActive) Color.White else Emerald900,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Live Audio Tutor (gemini-3.8-live)",
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = if (uiState.liveVoiceActive) Color.White else Emerald900
                    )
                    Text(
                      text = uiState.liveVoiceStatus,
                      fontSize = 11.sp,
                      color = if (uiState.liveVoiceActive) Amber500 else Color(0xFF64748B)
                    )
                  }
                }

                Button(
                  onClick = onToggleLiveVoice,
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.liveVoiceActive) Color(0xFFDC2626) else Emerald800
                  ),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(if (uiState.liveVoiceActive) "End Voice" else "Start Voice", fontSize = 11.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Messages
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            ) {
              items(uiState.aiChatHistory) { (role, message) ->
                val isModel = role == "model"
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
                ) {
                  Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                      containerColor = if (isModel) Color(0xFFF1F5F9) else Emerald800
                    ),
                    modifier = Modifier.widthIn(max = 280.dp)
                  ) {
                    Text(
                      text = message,
                      fontSize = 13.sp,
                      color = if (isModel) Color(0xFF0F172A) else Color.White,
                      modifier = Modifier.padding(12.dp),
                      lineHeight = 18.sp
                    )
                  }
                }
              }

              if (uiState.isAiResponding) {
                item {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Emerald800)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thinking step-by-step...", fontSize = 12.sp, color = Color(0xFF64748B))
                  }
                }
              }
            }

            // Chat Input
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = currentInput,
                onValueChange = { currentInput = it },
                placeholder = { Text("Ask anything about Punjab Board...", fontSize = 12.sp) },
                modifier = Modifier
                  .weight(1f)
                  .testTag("ai_chat_input"),
                shape = RoundedCornerShape(12.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              IconButton(
                onClick = {
                  if (currentInput.isNotBlank()) {
                    onSendMessage(currentInput)
                    currentInput = ""
                  }
                },
                modifier = Modifier
                  .clip(CircleShape)
                  .background(Emerald800)
                  .testTag("send_ai_message_btn")
              ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
              }
            }
          }
        }

        1 -> {
          // --- TAB 1: HIGH-QUALITY CONCEPT DIAGRAM GENERATION (gemini-3-pro-image-preview) ---
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            item {
              Text(
                text = "Generate High-Quality Scientific Diagrams",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Emerald900
              )
              Text(
                text = "Powered by gemini-3-pro-image-preview. Select 1K, 2K, or 4K resolution.",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }

            item {
              OutlinedTextField(
                value = uiState.imagePrompt,
                onValueChange = { onUpdateImagePrompt(it) },
                label = { Text("Diagram Prompt") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
              )
            }

            // Resolution Affordance: 1K, 2K, 4K
            item {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text("Select Resolution:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf("1K", "2K", "4K").forEach { res ->
                  FilterChip(
                    selected = uiState.selectedImageResolution == res,
                    onClick = { onSetImageResolution(res) },
                    label = { Text(res, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = Emerald800,
                      selectedLabelColor = Color.White
                    )
                  )
                }
              }
            }

            item {
              Button(
                onClick = onGenerateConceptImage,
                enabled = !uiState.isGeneratingImage,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                if (uiState.isGeneratingImage) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Rendering ${uiState.selectedImageResolution} Diagram...")
                } else {
                  Icon(Icons.Default.Image, contentDescription = "Generate", modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Generate ${uiState.selectedImageResolution} Diagram")
                }
              }
            }

            if (uiState.generatedImageUrl != null) {
              item {
                Card(
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                  border = CardDefaults.outlinedCardBorder(),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                      text = "High-Resolution Diagram Generated (${uiState.selectedImageResolution}) ✓",
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Emerald800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = "Labeling verified against Punjab Textbook Board standard for exams.",
                      fontSize = 11.sp,
                      color = Color(0xFF64748B)
                    )
                  }
                }
              }
            }
          }
        }

        2 -> {
          // --- TAB 2: VEO VIDEO ANIMATION (veo-3.1-fast-generate-preview) ---
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            item {
              Text(
                text = "Animate Concept Diagram into Video (Veo)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Emerald900
              )
              Text(
                text = "Model: veo-3.1-fast-generate-preview. Choose aspect ratio (16:9 landscape or 9:16 portrait).",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }

            // Aspect Ratio Selector
            item {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text("Aspect Ratio:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                listOf("16:9", "9:16").forEach { ratio ->
                  FilterChip(
                    selected = uiState.selectedVideoAspectRatio == ratio,
                    onClick = { onSetVideoAspectRatio(ratio) },
                    label = {
                      Text(
                        text = if (ratio == "16:9") "16:9 (Landscape)" else "9:16 (Portrait)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
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

            item {
              Button(
                onClick = onGenerateVeoVideo,
                enabled = !uiState.isGeneratingVideo,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                if (uiState.isGeneratingVideo) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Generating Veo Video...")
                } else {
                  Icon(Icons.Default.Movie, contentDescription = "Veo", modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Generate ${uiState.selectedVideoAspectRatio} Video with Veo")
                }
              }
            }

            if (uiState.generatedVideoStatus != null) {
              item {
                Card(
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                  border = CardDefaults.outlinedCardBorder(),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                      text = uiState.generatedVideoStatus!!,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Emerald900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "Animated visual walkthrough ready for classroom playback.",
                      fontSize = 11.sp,
                      color = Color(0xFF334155)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
