package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FloatingMascotAssistantFab
import com.example.ui.components.PunjabPrepBottomBar
import com.example.ui.components.PunjabPrepTopBar
import com.example.ui.screens.AiAssistantSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LocalPaymentDialog
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PastPapersScreen
import com.example.ui.screens.PomodoroFocusDialog
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProfileAndParentScreen
import com.example.ui.theme.PunjabPrepTheme
import com.example.ui.viewmodels.AppLanguage
import com.example.ui.viewmodels.NavigationTab
import com.example.ui.viewmodels.PunjabPrepViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class MainActivity : ComponentActivity() {

  private val viewModel: PunjabPrepViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // App Check intent wiring before setContent
    configureAppCheck(this, intent)

    enableEdgeToEdge()

    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val layoutDirection = if (uiState.language == AppLanguage.URDU) LayoutDirection.Rtl else LayoutDirection.Ltr

      PunjabPrepTheme {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
          PunjabPrepApp(
            viewModel = viewModel,
            uiState = uiState
          )
        }
      }
    }
  }

  private fun configureAppCheck(activity: ComponentActivity, intent: Intent?) {
    try {
      FirebaseApp.initializeApp(activity)
      val appCheck = FirebaseAppCheck.getInstance()
      appCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
    } catch (_: Exception) {
      // Graceful fallback for local test execution
    }
  }
}

@Composable
fun PunjabPrepApp(
  viewModel: PunjabPrepViewModel,
  uiState: com.example.ui.viewmodels.UiState
) {
  var showPomodoroDialog by remember { mutableStateOf(false) }

  // Handle system back navigation
  if (uiState.currentTab != NavigationTab.HOME) {
    BackHandler {
      viewModel.selectTab(NavigationTab.HOME)
    }
  }

  if (!uiState.isOnboardingCompleted) {
    OnboardingScreen(
      language = uiState.language,
      onComplete = { grade, group, board, guide ->
        viewModel.completeOnboarding(grade, group, board, guide)
      },
      onSignInWithGoogle = { viewModel.signInWithGoogle() }
    )
  } else {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        PunjabPrepTopBar(
          language = uiState.language,
          streakDays = uiState.userProfile.streakDays,
          coins = uiState.userProfile.coins,
          activeMascot = uiState.activeMascot,
          isPro = uiState.userProfile.isPro,
          onToggleLanguage = { viewModel.toggleLanguage() },
          onToggleMascot = { viewModel.toggleMascot() },
          onUpgradeProClick = { viewModel.togglePaymentDialog(true) }
        )
      },
      bottomBar = {
        PunjabPrepBottomBar(
          currentTab = uiState.currentTab,
          language = uiState.language,
          onTabSelected = { viewModel.selectTab(it) }
        )
      },
      floatingActionButton = {
        FloatingMascotAssistantFab(
          activeMascot = uiState.activeMascot,
          onClick = { viewModel.openAiAssistant(true) }
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (uiState.currentTab) {
          NavigationTab.HOME -> {
            HomeScreen(
              uiState = uiState,
              onSubjectSelected = { viewModel.selectSubject(it) },
              onNavigateTab = { viewModel.selectTab(it) },
              onStartPomodoro = { showPomodoroDialog = true },
              onOpenAiAssistant = { viewModel.openAiAssistant(true) }
            )
          }

          NavigationTab.LEARN -> {
            LearnScreen(
              uiState = uiState,
              onSubjectSelected = { viewModel.selectSubject(it) },
              onChapterSelected = { viewModel.selectChapter(it) },
              onLectureSelected = { viewModel.openLecture(it) },
              onSetPlaybackSpeed = { viewModel.setPlaybackSpeed(it) },
              onUpgradeProClick = { viewModel.togglePaymentDialog(true) }
            )
          }

          NavigationTab.PRACTICE -> {
            PracticeScreen(
              uiState = uiState,
              onSelectMcqOption = { viewModel.selectMcqOption(it) },
              onSubmitMcqAnswer = { viewModel.submitMcqAnswer() },
              onNextMcqQuestion = { viewModel.nextMcqQuestion() },
              onResetMcqQuiz = { viewModel.resetMcqQuiz() },
              onUpdateWrittenAnswer = { viewModel.updateWrittenAnswer(it) },
              onGradeWrittenTest = { viewModel.gradeWrittenTest(it) },
              onUpdateOralTranscript = { viewModel.updateOralTranscript(it) },
              onGradeOralTest = { viewModel.gradeOralTest(it) }
            )
          }

          NavigationTab.PAST_PAPERS -> {
            PastPapersScreen(
              uiState = uiState,
              onBoardFilterSelected = { viewModel.setBoardFilter(it) },
              onYearFilterSelected = { viewModel.setYearFilter(it) },
              onSearchAiQuestion = { viewModel.searchAiQuestion(it) }
            )
          }

          NavigationTab.PROFILE -> {
            ProfileAndParentScreen(
              uiState = uiState,
              onSelectTutor = { viewModel.selectTutor(it) },
              onBookTutor = { tutor, date, slot ->
                viewModel.confirmTutorBooking(tutor, date, slot)
              },
              onUpgradeProClick = { viewModel.togglePaymentDialog(true) },
              onSignOut = { viewModel.signOut() },
              onSignInWithGoogle = { viewModel.signInWithGoogle() }
            )
          }
        }
      }

      // AI Assistant Sheet (Mina & Wiki / Live Audio / Concept Diagram / Veo Video)
      if (uiState.isAiAssistantOpen) {
        AiAssistantSheet(
          uiState = uiState,
          onDismiss = { viewModel.openAiAssistant(false) },
          onSendMessage = { viewModel.sendAiMessage(it) },
          onToggleLiveVoice = { viewModel.toggleLiveVoice() },
          onToggleMascot = { viewModel.toggleMascot() },
          onUpdateImagePrompt = { viewModel.updateImagePrompt(it) },
          onSetImageResolution = { viewModel.setImageResolution(it) },
          onGenerateConceptImage = { viewModel.generateConceptImage() },
          onSetVideoAspectRatio = { viewModel.setVideoAspectRatio(it) },
          onGenerateVeoVideo = { viewModel.generateVeoVideo() }
        )
      }

      // Pomodoro Sprint Dialog
      if (showPomodoroDialog) {
        PomodoroFocusDialog(
          isSprintRunning = uiState.isSprintRunning,
          secondsRemaining = uiState.sprintSecondsRemaining,
          isBreakMode = uiState.isBreakMode,
          cyclesCompleted = uiState.sprintCyclesCompleted,
          language = uiState.language,
          onToggleTimer = { viewModel.togglePomodoro() },
          onResetTimer = { viewModel.resetPomodoro() },
          onDismiss = { showPomodoroDialog = false }
        )
      }

      // Pro Payment Dialog
      if (uiState.showPaymentDialog) {
        LocalPaymentDialog(
          onDismiss = { viewModel.togglePaymentDialog(false) },
          onConfirmPayment = { viewModel.unlockProPlan() }
        )
      }
    }
  }
}
