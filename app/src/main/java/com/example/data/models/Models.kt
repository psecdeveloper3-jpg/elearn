package com.example.data.models

import com.google.firebase.Timestamp

enum class UserRole {
  STUDENT, PARENT, TUTOR, ADMIN
}

enum class PunjabBoard(val displayName: String, val urduName: String) {
  LAHORE("BISE Lahore", "لاہور بورڈ"),
  RAWALPINDI("BISE Rawalpindi", "راولپنڈی بورڈ"),
  FAISALABAD("BISE Faisalabad", "فیصل آباد بورڈ"),
  GUJRANWALA("BISE Gujranwala", "گوجرانوالہ بورڈ"),
  MULTAN("BISE Multan", "ملتان بورڈ"),
  SARGODHA("BISE Sargodha", "سرگودھا بورڈ"),
  SAHIWAL("BISE Sahiwal", "ساہیوال بورڈ"),
  DG_KHAN("BISE D.G. Khan", "ڈی جی خان بورڈ"),
  BAHAWALPUR("BISE Bahawalpur", "بہاولپور بورڈ")
}

enum class GradeLevel(val label: String, val urduLabel: String) {
  GRADE_9("9th Matric", "نویں جماعت"),
  GRADE_10("10th Matric", "دسویں جماعت"),
  GRADE_11("11th FSC Part-1", "گیارہویں جماعت (ایف ایس سی)"),
  GRADE_12("12th FSC Part-2", "بارہویں جماعت (ایف ایس سی)")
}

enum class StudyGroup(val title: String, val urduTitle: String) {
  PRE_MEDICAL("Pre-Medical", "پری میڈیکل"),
  PRE_ENGINEERING("Pre-Engineering", "پری انجینئرنگ"),
  ICS("ICS (Computer Science)", "آئی سی ایس"),
  GENERAL_SCIENCE("General Science", "جنرل سائنس"),
  ARTS("Humanities & Arts", "آرٹس و انسانیات")
}

data class UserProfile(
  val id: String = "",
  val name: String = "",
  val email: String = "",
  val role: String = "student",
  val grade: String = "10th",
  val group: String = "Pre-Medical",
  val targetBoard: String = "BISE Lahore",
  val preferredGuide: String = "mina", // "mina" or "wiki"
  val xp: Int = 120,
  val coins: Int = 250,
  val streakDays: Int = 5,
  val streakFreezeCount: Int = 1,
  val level: Int = 3,
  val parentInviteCode: String = "PB-8821",
  val linkedParentId: String = "",
  val isPro: Boolean = false,
  val createdAt: Timestamp = Timestamp.now(),
  val updatedAt: Timestamp = Timestamp.now()
)

data class SubjectInfo(
  val id: String,
  val title: String,
  val urduTitle: String,
  val code: String,
  val iconEmoji: String,
  val totalChapters: Int,
  val completedChapters: Int,
  val progressPercent: Int
)

data class TextbookChapter(
  val id: String,
  val subjectId: String,
  val chapterNumber: Int,
  val title: String,
  val urduTitle: String,
  val summary: String,
  val urduSummary: String,
  val keyDefinitions: List<String>,
  val formulas: List<String>,
  val shortQuestionsGuide: List<String>,
  val longQuestionsGuide: List<String>,
  val isDownloaded: Boolean = false
)

data class VideoLecture(
  val id: String,
  val subjectId: String,
  val chapterId: String,
  val title: String,
  val urduTitle: String,
  val instructorName: String,
  val durationMinutes: Int,
  val videoThumbnailUrl: String = "",
  val isPro: Boolean = false,
  val notesAvailable: Boolean = true
)

data class PastPaperQuestion(
  val id: String,
  val questionText: String,
  val urduQuestionText: String,
  val subject: String,
  val chapter: String,
  val topic: String,
  val difficulty: String, // Easy, Medium, High
  val marks: Int,
  val yearsRepeated: List<Int>,
  val boardNames: List<String>,
  val repeatProbability: Int // 0 - 100%
)

data class McqQuestion(
  val id: String,
  val question: String,
  val urduQuestion: String,
  val options: List<String>,
  val urduOptions: List<String>,
  val correctOptionIndex: Int,
  val explanation: String,
  val urduExplanation: String
)

data class QuizAttemptRecord(
  val id: String = "",
  val userId: String = "",
  val subject: String = "",
  val chapterTitle: String = "",
  val score: Int = 0,
  val totalQuestions: Int = 0,
  val percentage: Int = 0,
  val coinsEarned: Int = 0,
  val timestamp: Timestamp = Timestamp.now()
)

data class WrittenTestSubmission(
  val id: String = "",
  val userId: String = "",
  val subject: String = "",
  val chapterTitle: String = "",
  val questionText: String = "",
  val studentAnswer: String = "",
  val score: Int = 0,
  val maxMarks: Int = 5,
  val feedback: String = "",
  val modelAnswer: String = "",
  val mistakesExplanation: String = "",
  val timestamp: Timestamp = Timestamp.now()
)

data class OralTestSubmission(
  val id: String = "",
  val userId: String = "",
  val subject: String = "",
  val chapterTitle: String = "",
  val questionText: String = "",
  val studentTranscript: String = "",
  val score: Int = 0,
  val maxMarks: Int = 10,
  val clarityScore: Int = 8,
  val completenessScore: Int = 9,
  val feedback: String = "",
  val timestamp: Timestamp = Timestamp.now()
)

data class RevisionItem(
  val id: String = "",
  val userId: String = "",
  val subject: String = "",
  val topic: String = "",
  val masteryLevel: Int = 50,
  val repetitionCount: Int = 1,
  val nextReviewDate: String = "Tomorrow",
  val lastReviewed: Timestamp = Timestamp.now()
)

data class TutorItem(
  val id: String,
  val name: String,
  val urduName: String,
  val title: String,
  val urduTitle: String,
  val rating: Double,
  val reviewsCount: Int,
  val hourlyRatePkr: Int,
  val subjects: List<String>,
  val isVerified: Boolean = true,
  val availability: String
)

data class TutorBookingRecord(
  val id: String = "",
  val userId: String = "",
  val tutorId: String = "",
  val tutorName: String = "",
  val subject: String = "",
  val date: String = "",
  val timeSlot: String = "",
  val status: String = "confirmed",
  val notes: String = "",
  val timestamp: Timestamp = Timestamp.now()
)

data class ParentReportRecord(
  val id: String = "",
  val userId: String = "",
  val weekLabel: String = "Week 1 (Oct 2026)",
  val attendanceRate: Int = 94,
  val avgScore: Int = 87,
  val studyHours: Int = 18,
  val weakTopics: String = "Chemistry Chapter 4: Chemical Bonding; Physics: Projectile Motion",
  val strongTopics: String = "Biology: Cell Cycle; Mathematics: Quadratic Equations",
  val aiRemarks: String = "Mina says: Your child demonstrated excellent consistency in Matric past papers with strong conceptual recall.",
  val timestamp: Timestamp = Timestamp.now()
)

data class MockExamItem(
  val id: String,
  val title: String,
  val urduTitle: String,
  val subject: String,
  val grade: String,
  val totalMarks: Int,
  val durationMinutes: Int,
  val isLiveScheduled: Boolean = false,
  val scheduledTime: String = "Today, 6:00 PM PST",
  val enrolledStudentsCount: Int = 4120
)
