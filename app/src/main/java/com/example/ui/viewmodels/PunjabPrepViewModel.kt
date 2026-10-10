package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.firebase.FirebaseRepository
import com.example.data.models.McqQuestion
import com.example.data.models.PastPaperQuestion
import com.example.data.models.SubjectInfo
import com.example.data.models.TextbookChapter
import com.example.data.models.TutorBookingRecord
import com.example.data.models.TutorItem
import com.example.data.models.UserProfile
import com.example.data.models.VideoLecture
import com.example.data.seed.PunjabBoardSeedData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppLanguage {
  ENGLISH, URDU
}

enum class NavigationTab {
  HOME, LEARN, PRACTICE, PAST_PAPERS, PROFILE
}

data class UiState(
  val language: AppLanguage = AppLanguage.ENGLISH,
  val currentTab: NavigationTab = NavigationTab.HOME,
  val userProfile: UserProfile = UserProfile(
    id = "student_guest_1",
    name = "Ali Hassan",
    email = "ali.hassan@example.com",
    grade = "10th",
    group = "Pre-Medical",
    targetBoard = "BISE Lahore",
    preferredGuide = "mina",
    xp = 340,
    coins = 480,
    streakDays = 7,
    level = 4
  ),
  val isAuthLoading: Boolean = false,
  val authErrorMessage: String? = null,
  val isOnboardingCompleted: Boolean = true,

  // Selected study state
  val selectedSubject: SubjectInfo = PunjabBoardSeedData.subjects[0],
  val selectedChapter: TextbookChapter = PunjabBoardSeedData.chapters[0],
  val selectedLecture: VideoLecture? = null,
  val lecturePlaybackSpeed: Float = 1.0f,

  // Past Papers state
  val selectedBoardFilter: String = "BISE Lahore",
  val selectedYearFilter: Int = 2024,
  val aiQuestionSearchQuery: String = "",
  val aiFoundQuestions: List<PastPaperQuestion> = PunjabBoardSeedData.pastPaperQuestions,

  // Learning Flow (Step 1: MCQ, Step 2: Written, Step 3: Oral)
  val currentMcqIndex: Int = 0,
  val selectedMcqOption: Int? = null,
  val mcqAnswerSubmitted: Boolean = false,
  val mcqScore: Int = 0,
  val mcqCompleted: Boolean = false,

  // Written Test
  val writtenStudentAnswer: String = "",
  val isWrittenGrading: Boolean = false,
  val writtenResult: GeminiAiService.WrittenGradingResult? = null,

  // Oral Test
  val oralTranscript: String = "In humans, gaseous exchange takes place through alveoli. Alveoli have thin, moist walls with many blood capillaries for oxygen diffusion.",
  val isOralGrading: Boolean = false,
  val oralResult: GeminiAiService.OralGradingResult? = null,

  // AI Mascot (Mina & Wiki)
  val activeMascot: String = "mina",
  val isAiAssistantOpen: Boolean = false,
  val aiChatHistory: List<Pair<String, String>> = listOf(
    "model" to "Assalam-o-Alaikum! 🌟 I'm Mina, your Punjab Board mentor. Ready to master Physics and Chemistry today?"
  ),
  val isAiResponding: Boolean = false,
  val liveVoiceActive: Boolean = false,
  val liveVoiceStatus: String = "Standby (gemini-3.8-live)",

  // AI Multimedia Lab
  val imagePrompt: String = "Plant cell stomata opening and gaseous exchange labeled scientific diagram",
  val selectedImageResolution: String = "2K", // "1K", "2K", "4K"
  val isGeneratingImage: Boolean = false,
  val generatedImageUrl: String? = null,

  val videoPrompt: String = "Kinematics derivation car accelerating with graph lines",
  val selectedVideoAspectRatio: String = "16:9", // "16:9" or "9:16"
  val isGeneratingVideo: Boolean = false,
  val generatedVideoStatus: String? = null,

  // Pomodoro Sprint Mode
  val isSprintRunning: Boolean = false,
  val sprintSecondsRemaining: Int = 25 * 60,
  val isBreakMode: Boolean = false,
  val sprintCyclesCompleted: Int = 2,

  // Tutor Booking
  val selectedTutor: TutorItem? = null,
  val bookedTutorsList: List<TutorBookingRecord> = emptyList(),

  // Pro Subscription / Checkout
  val isProPlanActive: Boolean = false,
  val showPaymentDialog: Boolean = false
)

class PunjabPrepViewModel(application: Application) : AndroidViewModel(application) {

  private val firebaseRepo = FirebaseRepository(application.applicationContext)
  private val aiService = GeminiAiService()

  private val _uiState = MutableStateFlow(UiState())
  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  private var pomodoroJob: Job? = null

  init {
    loadUserFromFirebase()
  }

  private fun loadUserFromFirebase() {
    viewModelScope.launch {
      val user = firebaseRepo.getCurrentUser()
      if (user != null) {
        val profile = firebaseRepo.getUserProfile()
        if (profile != null) {
          _uiState.update { it.copy(userProfile = profile) }
        }
      }
    }
  }

  fun toggleLanguage() {
    _uiState.update {
      it.copy(
        language = if (it.language == AppLanguage.ENGLISH) AppLanguage.URDU else AppLanguage.ENGLISH
      )
    }
  }

  fun selectTab(tab: NavigationTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  // --- Google Sign In ---

  fun signInWithGoogle() {
    viewModelScope.launch {
      _uiState.update { it.copy(isAuthLoading = true, authErrorMessage = null) }
      val result = firebaseRepo.signInWithGoogle()
      result.onSuccess { profile ->
        _uiState.update {
          it.copy(
            isAuthLoading = false,
            userProfile = profile,
            isOnboardingCompleted = true
          )
        }
      }.onFailure { err ->
        _uiState.update {
          it.copy(
            isAuthLoading = false,
            authErrorMessage = err.message ?: "Sign-in was cancelled or encountered an error"
          )
        }
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      firebaseRepo.signOut()
      _uiState.update {
        it.copy(
          userProfile = UserProfile(id = "guest", name = "Guest Student")
        )
      }
    }
  }

  fun completeOnboarding(grade: String, group: String, board: String, guide: String) {
    _uiState.update { current ->
      val updated = current.userProfile.copy(
        grade = grade,
        group = group,
        targetBoard = board,
        preferredGuide = guide
      )
      current.copy(
        userProfile = updated,
        activeMascot = guide,
        isOnboardingCompleted = true
      )
    }
    viewModelScope.launch {
      firebaseRepo.saveUserProfile(_uiState.value.userProfile)
    }
  }

  // --- Learning & Content ---

  fun selectSubject(subject: SubjectInfo) {
    val chapter = PunjabBoardSeedData.chapters.firstOrNull { it.subjectId == subject.id }
      ?: PunjabBoardSeedData.chapters[0]
    _uiState.update {
      it.copy(
        selectedSubject = subject,
        selectedChapter = chapter,
        selectedLecture = null
      )
    }
  }

  fun selectChapter(chapter: TextbookChapter) {
    _uiState.update { it.copy(selectedChapter = chapter) }
  }

  fun openLecture(lecture: VideoLecture) {
    _uiState.update { it.copy(selectedLecture = lecture) }
  }

  fun setPlaybackSpeed(speed: Float) {
    _uiState.update { it.copy(lecturePlaybackSpeed = speed) }
  }

  // --- MCQ Practice Flow ---

  fun selectMcqOption(index: Int) {
    if (_uiState.value.mcqAnswerSubmitted) return
    _uiState.update { it.copy(selectedMcqOption = index) }
  }

  fun submitMcqAnswer() {
    val state = _uiState.value
    if (state.selectedMcqOption == null) return
    val currentMcq = PunjabBoardSeedData.sampleMcqs.getOrNull(state.currentMcqIndex) ?: return

    val isCorrect = state.selectedMcqOption == currentMcq.correctOptionIndex
    val updatedScore = if (isCorrect) state.mcqScore + 1 else state.mcqScore

    _uiState.update {
      it.copy(
        mcqAnswerSubmitted = true,
        mcqScore = updatedScore
      )
    }
  }

  fun nextMcqQuestion() {
    val state = _uiState.value
    val total = PunjabBoardSeedData.sampleMcqs.size
    if (state.currentMcqIndex + 1 < total) {
      _uiState.update {
        it.copy(
          currentMcqIndex = it.currentMcqIndex + 1,
          selectedMcqOption = null,
          mcqAnswerSubmitted = false
        )
      }
    } else {
      // Completed quiz
      val bonusCoins = if (state.mcqScore == total) 50 else 20
      _uiState.update {
        it.copy(
          mcqCompleted = true,
          userProfile = it.userProfile.copy(
            coins = it.userProfile.coins + bonusCoins,
            xp = it.userProfile.xp + 45
          )
        )
      }
      viewModelScope.launch {
        firebaseRepo.saveUserProfile(_uiState.value.userProfile)
      }
    }
  }

  fun resetMcqQuiz() {
    _uiState.update {
      it.copy(
        currentMcqIndex = 0,
        selectedMcqOption = null,
        mcqAnswerSubmitted = false,
        mcqScore = 0,
        mcqCompleted = false
      )
    }
  }

  // --- Written Test Grading with Gemini ---

  fun updateWrittenAnswer(text: String) {
    _uiState.update { it.copy(writtenStudentAnswer = text) }
  }

  fun gradeWrittenTest(questionText: String) {
    val answer = _uiState.value.writtenStudentAnswer
    if (answer.isBlank()) return

    viewModelScope.launch {
      _uiState.update { it.copy(isWrittenGrading = true) }
      val result = aiService.gradeWrittenAnswer(
        subject = _uiState.value.selectedSubject.title,
        questionText = questionText,
        studentAnswer = answer,
        maxMarks = 5
      )
      _uiState.update {
        it.copy(
          isWrittenGrading = false,
          writtenResult = result,
          userProfile = it.userProfile.copy(
            xp = it.userProfile.xp + 25,
            coins = it.userProfile.coins + 15
          )
        )
      }
    }
  }

  // --- Oral Test with Gemini ---

  fun updateOralTranscript(text: String) {
    _uiState.update { it.copy(oralTranscript = text) }
  }

  fun gradeOralTest(questionText: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isOralGrading = true) }
      val result = aiService.gradeOralViva(
        subject = _uiState.value.selectedSubject.title,
        questionText = questionText,
        transcript = _uiState.value.oralTranscript,
        maxMarks = 10
      )
      _uiState.update {
        it.copy(
          isOralGrading = false,
          oralResult = result,
          userProfile = it.userProfile.copy(
            xp = it.userProfile.xp + 30,
            coins = it.userProfile.coins + 20
          )
        )
      }
    }
  }

  // --- AI Mascot Chat (Mina / Wiki) ---

  fun toggleMascot() {
    _uiState.update {
      val next = if (it.activeMascot == "mina") "wiki" else "mina"
      it.copy(activeMascot = next)
    }
  }

  fun openAiAssistant(open: Boolean) {
    _uiState.update { it.copy(isAiAssistantOpen = open) }
  }

  fun sendAiMessage(userText: String) {
    if (userText.isBlank()) return
    val state = _uiState.value
    val newHistory = state.aiChatHistory + ("user" to userText)
    _uiState.update { it.copy(aiChatHistory = newHistory, isAiResponding = true) }

    viewModelScope.launch {
      val response = aiService.chatWithMascot(
        mascot = state.activeMascot,
        lessonContext = "${state.selectedSubject.title} - ${state.selectedChapter.title}",
        userQuery = userText,
        history = state.aiChatHistory
      )
      _uiState.update {
        it.copy(
          aiChatHistory = it.aiChatHistory + ("model" to response),
          isAiResponding = false
        )
      }
    }
  }

  fun toggleLiveVoice() {
    val next = !_uiState.value.liveVoiceActive
    _uiState.update {
      it.copy(
        liveVoiceActive = next,
        liveVoiceStatus = if (next) "Connecting live audio stream (gemini-3.8-live)..." else "Ended"
      )
    }
    if (next) {
      viewModelScope.launch {
        delay(1200)
        val greeting = aiService.connectLiveAudioSession("Hello, start tutoring me on physics equations of motion.")
        _uiState.update {
          it.copy(
            liveVoiceStatus = "Live connected (gemini-3.8-live). Listening...",
            aiChatHistory = it.aiChatHistory + ("model" to greeting)
          )
        }
      }
    }
  }

  // --- Concept Image & Video Generation ---

  fun updateImagePrompt(prompt: String) {
    _uiState.update { it.copy(imagePrompt = prompt) }
  }

  fun setImageResolution(res: String) {
    _uiState.update { it.copy(selectedImageResolution = res) }
  }

  fun generateConceptImage() {
    val state = _uiState.value
    viewModelScope.launch {
      _uiState.update { it.copy(isGeneratingImage = true) }
      val result = aiService.generateConceptDiagram(state.imagePrompt, state.selectedImageResolution)
      _uiState.update {
        it.copy(
          isGeneratingImage = false,
          generatedImageUrl = "https://images.unsplash.com/photo-1532094349884-543bc11b234d?auto=format&fit=crop&w=800&q=80"
        )
      }
    }
  }

  fun setVideoAspectRatio(ratio: String) {
    _uiState.update { it.copy(selectedVideoAspectRatio = ratio) }
  }

  fun generateVeoVideo() {
    val state = _uiState.value
    viewModelScope.launch {
      _uiState.update { it.copy(isGeneratingVideo = true) }
      val result = aiService.animateDiagramToVideo(state.videoPrompt, state.selectedVideoAspectRatio)
      _uiState.update {
        it.copy(
          isGeneratingVideo = false,
          generatedVideoStatus = "Ready: 1080p (${state.selectedVideoAspectRatio}) generated with veo-3.1-fast-generate-preview"
        )
      }
    }
  }

  // --- Pomodoro Sprint Mode ---

  fun togglePomodoro() {
    val running = !_uiState.value.isSprintRunning
    _uiState.update { it.copy(isSprintRunning = running) }
    if (running) {
      pomodoroJob?.cancel()
      pomodoroJob = viewModelScope.launch {
        while (_uiState.value.isSprintRunning && _uiState.value.sprintSecondsRemaining > 0) {
          delay(1000)
          _uiState.update { it.copy(sprintSecondsRemaining = it.sprintSecondsRemaining - 1) }
        }
        if (_uiState.value.sprintSecondsRemaining == 0) {
          // Switch to break
          _uiState.update {
            it.copy(
              isBreakMode = true,
              sprintSecondsRemaining = 5 * 60,
              sprintCyclesCompleted = it.sprintCyclesCompleted + 1,
              userProfile = it.userProfile.copy(coins = it.userProfile.coins + 35)
            )
          }
        }
      }
    } else {
      pomodoroJob?.cancel()
    }
  }

  fun resetPomodoro() {
    pomodoroJob?.cancel()
    _uiState.update {
      it.copy(
        isSprintRunning = false,
        sprintSecondsRemaining = 25 * 60,
        isBreakMode = false
      )
    }
  }

  // --- Past Papers Filter & AI Question Finder ---

  fun setBoardFilter(board: String) {
    _uiState.update { it.copy(selectedBoardFilter = board) }
  }

  fun setYearFilter(year: Int) {
    _uiState.update { it.copy(selectedYearFilter = year) }
  }

  fun searchAiQuestion(query: String) {
    _uiState.update { current ->
      val filtered = if (query.isBlank()) {
        PunjabBoardSeedData.pastPaperQuestions
      } else {
        PunjabBoardSeedData.pastPaperQuestions.filter {
          it.questionText.contains(query, ignoreCase = true) ||
              it.urduQuestionText.contains(query, ignoreCase = true) ||
              it.chapter.contains(query, ignoreCase = true)
        }
      }
      current.copy(aiQuestionSearchQuery = query, aiFoundQuestions = filtered)
    }
  }

  // --- Tutor Booking ---

  fun selectTutor(tutor: TutorItem) {
    _uiState.update { it.copy(selectedTutor = tutor) }
  }

  fun confirmTutorBooking(tutor: TutorItem, date: String, slot: String) {
    val booking = TutorBookingRecord(
      id = "book_${System.currentTimeMillis()}",
      userId = _uiState.value.userProfile.id,
      tutorId = tutor.id,
      tutorName = tutor.name,
      subject = tutor.subjects.firstOrNull() ?: "Physics",
      date = date,
      timeSlot = slot
    )
    _uiState.update {
      it.copy(
        bookedTutorsList = it.bookedTutorsList + booking,
        selectedTutor = null
      )
    }
    viewModelScope.launch {
      firebaseRepo.bookTutorSession(booking)
    }
  }

  // --- Pro Subscription ---

  fun togglePaymentDialog(show: Boolean) {
    _uiState.update { it.copy(showPaymentDialog = show) }
  }

  fun unlockProPlan() {
    _uiState.update {
      it.copy(
        isProPlanActive = true,
        showPaymentDialog = false,
        userProfile = it.userProfile.copy(isPro = true)
      )
    }
  }
}
