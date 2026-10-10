package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber500
import com.example.ui.theme.CoinGold
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.UrduFontFamily
import com.example.ui.viewmodels.AppLanguage

@Composable
fun PomodoroFocusDialog(
  isSprintRunning: Boolean,
  secondsRemaining: Int,
  isBreakMode: Boolean,
  cyclesCompleted: Int,
  language: AppLanguage,
  onToggleTimer: () -> Unit,
  onResetTimer: () -> Unit,
  onDismiss: () -> Unit
) {
  val isUrdu = language == AppLanguage.URDU
  val minutes = secondsRemaining / 60
  val seconds = secondsRemaining % 60
  val timeString = String.format("%02d:%02d", minutes, seconds)

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = onToggleTimer,
        colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(if (isSprintRunning) "Pause" else "Start Sprint")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onResetTimer,
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Reset")
      }
    },
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Timer, contentDescription = "Sprint", tint = Emerald800)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isBreakMode) "5-Min Refresh Break" else "25-Min Study Sprint",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Emerald900
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = CircleShape,
          color = if (isBreakMode) Color(0xFFFEF3C7) else Emerald100,
          modifier = Modifier.size(130.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = timeString,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (isBreakMode) Color(0xFF92400E) else Emerald900
              )
              Text(
                text = if (isBreakMode) "Brain Teaser Break" else "Deep Focus",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = if (isBreakMode)
            "🧠 Quick Brain Teaser: What derived physical quantity has dimensions of [M L⁻¹ T⁻²]? Answer: Pressure (Pascal)!"
          else
            "Mina & Wiki are keeping track of your focus. Complete 25 minutes to earn +35 Punjab Coins!",
          fontSize = 12.sp,
          color = Color(0xFF334155),
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Completed Sprints: $cyclesCompleted today",
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
          color = Emerald800
        )
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(16.dp)
  )
}

@Composable
fun LocalPaymentDialog(
  onDismiss: () -> Unit,
  onConfirmPayment: () -> Unit
) {
  var selectedMethod by remember { mutableStateOf("jazzcash") } // "jazzcash", "easypaisa", "card"
  var promoCode by remember { mutableStateOf("") }
  var isDiscountApplied by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = onConfirmPayment,
        colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("confirm_checkout_btn")
      ) {
        Text(if (isDiscountApplied) "Pay PKR 1,199" else "Pay PKR 1,499")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
        Text("Cancel")
      }
    },
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "PunjabPrep Pro Subscription",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = Emerald900
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Choose Local Pakistan Payment Method:",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF475569)
        )

        // JazzCash option
        Surface(
          color = if (selectedMethod == "jazzcash") Emerald100 else Color(0xFFF8FAFC),
          shape = RoundedCornerShape(10.dp),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedMethod = "jazzcash" }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.PhoneAndroid, contentDescription = "JazzCash", tint = Color(0xFFD97706))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("JazzCash Mobile Account / Voucher", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Instant unlock with 03xx-xxxxxxx OTP", fontSize = 11.sp, color = Color(0xFF64748B))
            }
          }
        }

        // Easypaisa option
        Surface(
          color = if (selectedMethod == "easypaisa") Emerald100 else Color(0xFFF8FAFC),
          shape = RoundedCornerShape(10.dp),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedMethod = "easypaisa" }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.LocalAtm, contentDescription = "Easypaisa", tint = Color(0xFF16A34A))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Easypaisa Wallet", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Pay securely via Telenor Easypaisa app", fontSize = 11.sp, color = Color(0xFF64748B))
            }
          }
        }

        // Debit / Credit Card option
        Surface(
          color = if (selectedMethod == "card") Emerald100 else Color(0xFFF8FAFC),
          shape = RoundedCornerShape(10.dp),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedMethod = "card" }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.CreditCard, contentDescription = "Cards", tint = Color(0xFF0284C7))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Debit / Credit Card (PayPak / Visa / Mastercard)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("All Pakistani commercial banks supported", fontSize = 11.sp, color = Color(0xFF64748B))
            }
          }
        }

        // Promo Code Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = promoCode,
            onValueChange = { promoCode = it },
            placeholder = { Text("Promo Code (BISE2026)", fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Button(
            onClick = {
              if (promoCode.trim().equals("BISE2026", ignoreCase = true)) {
                isDiscountApplied = true
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Amber500),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Apply", color = Color.Black, fontSize = 11.sp)
          }
        }

        if (isDiscountApplied) {
          Text("✓ 20% Discount Applied! New Total: PKR 1,199", color = Emerald800, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(16.dp)
  )
}
