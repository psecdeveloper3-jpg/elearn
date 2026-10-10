package com.example.data.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.example.data.models.OralTestSubmission
import com.example.data.models.ParentReportRecord
import com.example.data.models.QuizAttemptRecord
import com.example.data.models.RevisionItem
import com.example.data.models.TutorBookingRecord
import com.example.data.models.UserProfile
import com.example.data.models.WrittenTestSubmission
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository(private val context: Context) {

  private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
  private val dbId: String by lazy { context.getString(R.string.firestore_database_id) }
  private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance(dbId) }
  private val credentialManager: CredentialManager by lazy { CredentialManager.create(context) }

  companion object {
    private const val TAG = "PunjabPrepFirebase"
  }

  fun getCurrentUser() = auth.currentUser

  fun requireUserId(): String {
    return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated with Firebase")
  }

  // --- Authentication with Credential Manager ---

  suspend fun signInWithGoogle(): Result<UserProfile> {
    return try {
      val webClientId = try {
        context.getString(R.string.default_web_client_id)
      } catch (e: Exception) {
        "92384896399-ld3d7c1q2glfcb9hbo1f6ov7ccct98m8.apps.googleusercontent.com"
      }

      val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = webClientId).build()
      val request = GetCredentialRequest.Builder()
        .addCredentialOption(signInOption)
        .build()

      val result = credentialManager.getCredential(
        request = request,
        context = context
      )

      val credential = result.credential
      if (credential is GoogleIdTokenCredential) {
        val googleToken = credential.idToken
        val authCredential = GoogleAuthProvider.getCredential(googleToken, null)
        val authResult = auth.signInWithCredential(authCredential).await()
        val user = authResult.user ?: throw IllegalStateException("Firebase Auth returned null user")

        val profile = UserProfile(
          id = user.uid,
          name = user.displayName ?: "PunjabPrep Scholar",
          email = user.email ?: "",
          role = "student",
          createdAt = Timestamp.now()
        )
        saveUserProfile(profile)
        Result.success(profile)
      } else {
        Result.failure(IllegalStateException("Unsupported credential type"))
      }
    } catch (e: GetCredentialCancellationException) {
      Log.w(TAG, "Interactive sign-in was cancelled by user: ${e.message}")
      Result.failure(e)
    } catch (e: Exception) {
      Log.e(TAG, "Sign-in error: ${e.message}", e)
      Result.failure(e)
    }
  }

  suspend fun signOut() {
    try {
      auth.signOut()
      credentialManager.clearCredentialState(ClearCredentialStateRequest())
    } catch (e: Exception) {
      Log.e(TAG, "Sign out error", e)
    }
  }

  // --- User Profile Firestore Operations ---

  suspend fun saveUserProfile(profile: UserProfile): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    return try {
      val data = hashMapOf<String, Any>(
        "id" to uid,
        "name" to profile.name,
        "email" to profile.email,
        "role" to profile.role,
        "grade" to profile.grade,
        "group" to profile.group,
        "targetBoard" to profile.targetBoard,
        "preferredGuide" to profile.preferredGuide,
        "xp" to profile.xp,
        "coins" to profile.coins,
        "streakDays" to profile.streakDays,
        "level" to profile.level,
        "parentInviteCode" to profile.parentInviteCode,
        "createdAt" to profile.createdAt,
        "updatedAt" to Timestamp.now()
      )
      db.collection("users").document(uid).set(data).await()
      true
    } catch (e: Exception) {
      Log.e(TAG, "Error saving user profile: ${e.message}", e)
      false
    }
  }

  suspend fun getUserProfile(): UserProfile? {
    val uid = auth.currentUser?.uid ?: return null
    return try {
      val doc = db.collection("users").document(uid).get().await()
      if (doc.exists()) {
        UserProfile(
          id = doc.getString("id") ?: uid,
          name = doc.getString("name") ?: "Student",
          email = doc.getString("email") ?: "",
          role = doc.getString("role") ?: "student",
          grade = doc.getString("grade") ?: "10th",
          group = doc.getString("group") ?: "Pre-Medical",
          targetBoard = doc.getString("targetBoard") ?: "BISE Lahore",
          preferredGuide = doc.getString("preferredGuide") ?: "mina",
          xp = (doc.getLong("xp") ?: 120).toInt(),
          coins = (doc.getLong("coins") ?: 250).toInt(),
          streakDays = (doc.getLong("streakDays") ?: 5).toInt(),
          level = (doc.getLong("level") ?: 3).toInt(),
          parentInviteCode = doc.getString("parentInviteCode") ?: "PB-8821"
        )
      } else {
        null
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error fetching user profile", e)
      null
    }
  }

  // --- Subcollections for Learning Flow ---

  suspend fun saveQuizAttempt(attempt: QuizAttemptRecord): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    return try {
      val attemptId = if (attempt.id.isNotBlank()) attempt.id else "quiz_${System.currentTimeMillis()}"
      val data = hashMapOf<String, Any>(
        "id" to attemptId,
        "userId" to uid,
        "subject" to attempt.subject,
        "chapterTitle" to attempt.chapterTitle,
        "score" to attempt.score,
        "totalQuestions" to attempt.totalQuestions,
        "percentage" to attempt.percentage,
        "coinsEarned" to attempt.coinsEarned,
        "timestamp" to attempt.timestamp
      )
      db.collection("users").document(uid)
        .collection("quizAttempts").document(attemptId)
        .set(data).await()
      true
    } catch (e: Exception) {
      Log.e(TAG, "Error saving quiz attempt", e)
      false
    }
  }

  suspend fun saveWrittenSubmission(submission: WrittenTestSubmission): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    return try {
      val subId = if (submission.id.isNotBlank()) submission.id else "written_${System.currentTimeMillis()}"
      val data = hashMapOf<String, Any>(
        "id" to subId,
        "userId" to uid,
        "subject" to submission.subject,
        "chapterTitle" to submission.chapterTitle,
        "questionText" to submission.questionText,
        "studentAnswer" to submission.studentAnswer,
        "score" to submission.score,
        "maxMarks" to submission.maxMarks,
        "feedback" to submission.feedback,
        "modelAnswer" to submission.modelAnswer,
        "mistakesExplanation" to submission.mistakesExplanation,
        "timestamp" to submission.timestamp
      )
      db.collection("users").document(uid)
        .collection("writtenSubmissions").document(subId)
        .set(data).await()
      true
    } catch (e: Exception) {
      Log.e(TAG, "Error saving written submission", e)
      false
    }
  }

  suspend fun saveOralSubmission(oral: OralTestSubmission): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    return try {
      val subId = if (oral.id.isNotBlank()) oral.id else "oral_${System.currentTimeMillis()}"
      val data = hashMapOf<String, Any>(
        "id" to subId,
        "userId" to uid,
        "subject" to oral.subject,
        "chapterTitle" to oral.chapterTitle,
        "questionText" to oral.questionText,
        "studentTranscript" to oral.studentTranscript,
        "score" to oral.score,
        "maxMarks" to oral.maxMarks,
        "clarityScore" to oral.clarityScore,
        "completenessScore" to oral.completenessScore,
        "feedback" to oral.feedback,
        "timestamp" to oral.timestamp
      )
      db.collection("users").document(uid)
        .collection("oralSubmissions").document(subId)
        .set(data).await()
      true
    } catch (e: Exception) {
      Log.e(TAG, "Error saving oral submission", e)
      false
    }
  }

  suspend fun bookTutorSession(booking: TutorBookingRecord): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    return try {
      val bId = if (booking.id.isNotBlank()) booking.id else "book_${System.currentTimeMillis()}"
      val data = hashMapOf<String, Any>(
        "id" to bId,
        "userId" to uid,
        "tutorId" to booking.tutorId,
        "tutorName" to booking.tutorName,
        "subject" to booking.subject,
        "date" to booking.date,
        "timeSlot" to booking.timeSlot,
        "status" to booking.status,
        "notes" to booking.notes,
        "timestamp" to booking.timestamp
      )
      db.collection("users").document(uid)
        .collection("tutorBookings").document(bId)
        .set(data).await()
      true
    } catch (e: Exception) {
      Log.e(TAG, "Error booking tutor session", e)
      false
    }
  }
}
