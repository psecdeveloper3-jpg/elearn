package com.example.data.seed

import com.example.data.models.McqQuestion
import com.example.data.models.MockExamItem
import com.example.data.models.PastPaperQuestion
import com.example.data.models.SubjectInfo
import com.example.data.models.TextbookChapter
import com.example.data.models.TutorItem
import com.example.data.models.VideoLecture

object PunjabBoardSeedData {

  val subjects = listOf(
    SubjectInfo("phys", "Physics", "طبیعیات", "PHY-10", "⚛️", 9, 7, 78),
    SubjectInfo("chem", "Chemistry", "کیمیا", "CHM-10", "🧪", 8, 5, 62),
    SubjectInfo("bio", "Biology", "حیاتیات", "BIO-10", "🧬", 9, 8, 89),
    SubjectInfo("math", "Mathematics", "ریاضی", "MTH-10", "📐", 13, 10, 77),
    SubjectInfo("cs", "Computer Science", "کمپیوٹر سائنس", "CSC-10", "💻", 7, 6, 85),
    SubjectInfo("eng", "English", "انگریزی", "ENG-10", "📖", 12, 11, 92),
    SubjectInfo("urdu", "Urdu", "اردو لازمی", "URD-10", "📜", 14, 12, 86)
  )

  val chapters = listOf(
    TextbookChapter(
      id = "phys_ch1",
      subjectId = "phys",
      chapterNumber = 1,
      title = "Physical Quantities & Measurement",
      urduTitle = "طبیعی مقداریں اور پیمائش",
      summary = "Introduction to International System of Units (SI), Vernier Calipers (Least Count = 0.01 cm), Screw Gauge (Least Count = 0.001 cm), and Significant Figures.",
      urduSummary = "ایس آئی یونٹس، ورنیئر کیلیپرز (لیسٹ کاؤنٹ 0.01 سینٹی میٹر)، سکرو گیج (0.001 سینٹی میٹر) اور اہم ہندسوں کی جامع تفہیم۔",
      keyDefinitions = listOf(
        "Base Quantities: Quantities in terms of which other quantities are expressed (Length, Mass, Time, Current, Temp, Light Intensity, Substance).",
        "Derived Quantities: Quantities expressed in terms of base quantities (Velocity, Force, Work).",
        "Least Count: Minimum measurement that can be taken accurately by an instrument."
      ),
      formulas = listOf("Least Count (Vernier) = Smallest main scale division / Total vernier divisions", "Volume = Length × Width × Height", "Density = Mass / Volume"),
      shortQuestionsGuide = listOf(
        "Differentiate between base and derived units with examples.",
        "What is zero error in Vernier Calipers and how is it corrected?",
        "State rules to determine significant figures."
      ),
      longQuestionsGuide = listOf(
        "Explain working and zero error determination of Screw Gauge with neat diagram.",
        "Describe prefix notation and standard form in scientific representation."
      ),
      isDownloaded = true
    ),
    TextbookChapter(
      id = "phys_ch2",
      subjectId = "phys",
      chapterNumber = 2,
      title = "Kinematics",
      urduTitle = "حرکیات",
      summary = "Rest and Motion, Scalars and Vectors, Speed vs Velocity, Acceleration, Equations of Motion for uniformly accelerated bodies.",
      urduSummary = "سکون اور حرکت، سکیلر اور ویکٹر، رفتار اور اسراع، یکساں اسراع کے لیے حرکت کی مساواتیں۔",
      keyDefinitions = listOf(
        "Scalar: Physical quantity specified by magnitude only.",
        "Vector: Physical quantity specified by magnitude and direction.",
        "Acceleration: Rate of change of velocity."
      ),
      formulas = listOf("vf = vi + at", "S = vi·t + 1/2 a·t²", "2aS = vf² - vi²"),
      shortQuestionsGuide = listOf(
        "Derive 1st equation of motion using speed-time graph.",
        "Can a body moving with constant speed have acceleration? Explain."
      ),
      longQuestionsGuide = listOf(
        "Prove 2nd and 3rd equations of motion using graphical method (Frequently asked in BISE Lahore & Rawalpindi)."
      ),
      isDownloaded = true
    ),
    TextbookChapter(
      id = "chem_ch1",
      subjectId = "chem",
      chapterNumber = 1,
      title = "Fundamentals of Chemistry",
      urduTitle = "کیمسٹری کے بنیادی تصورات",
      summary = "Branches of Chemistry, Elements, Compounds, Mixtures, Atomic Number, Mass Number, Mole Concept, and Avogadro's Number.",
      urduSummary = "کیمسٹری کی شاخیں، عناصر، مرکبات، مکسچر، ایٹمی نمبر، ماس نمبر، مول کا تصور اور ایووگیڈرو نمبر۔",
      keyDefinitions = listOf(
        "Mole: Amount of substance containing 6.02 × 10²³ particles.",
        "Compound: Substance made of two or more elements chemically combined in a fixed ratio by mass."
      ),
      formulas = listOf("Number of Moles = Known Mass / Molar Mass", "Number of Particles = Moles × 6.02 × 10²³"),
      shortQuestionsGuide = listOf(
        "Distinguish between compound and mixture with 4 points.",
        "Define empirical and molecular formula with examples."
      ),
      longQuestionsGuide = listOf(
        "Describe experimental determination of empirical formula of an organic compound.",
        "Explain Avogadro's number and calculate moles from given mass."
      ),
      isDownloaded = true
    ),
    TextbookChapter(
      id = "bio_ch1",
      subjectId = "bio",
      chapterNumber = 1,
      title = "Gaseous Exchange",
      urduTitle = "گیسوں کا تبادلہ",
      summary = "Respiration vs Breathing, Gaseous exchange in plants (Stomata, Lenticels), Human Respiratory System (Air passageway, Lungs), and Respiratory disorders (Bronchitis, Emphysema, Asthma, Lung Cancer).",
      urduSummary = "سانس لینے اور ریسپائریشن میں فرق، پودوں اور انسان میں گیسوں کا تبادلہ، نظام تنفس کی بیماریاں۔",
      keyDefinitions = listOf(
        "Alveoli: Sac-like structures that form respiratory surface in human lungs.",
        "Stomata: Microscopic pores in leaves through which gases diffuse."
      ),
      formulas = listOf("Inhaled air: 21% O2, 0.04% CO2", "Exhaled air: 16% O2, 4% CO2"),
      shortQuestionsGuide = listOf(
        "Differentiate between breathing and cellular respiration.",
        "What are symptoms and causes of Emphysema?"
      ),
      longQuestionsGuide = listOf(
        "Describe human air passageway in detail with labelled diagram.",
        "Explain mechanism of breathing (Inhalation and Exhalation)."
      ),
      isDownloaded = true
    )
  )

  val videoLectures = listOf(
    VideoLecture(
      id = "lec_101",
      subjectId = "phys",
      chapterId = "phys_ch2",
      title = "Equations of Motion Graphical Proof (BISE Favorite)",
      urduTitle = "حرکت کی مساواتوں کا گرافکل ثبوت",
      instructorName = "Sir Asif Javed (Gold Medalist)",
      durationMinutes = 24,
      isPro = false
    ),
    VideoLecture(
      id = "lec_102",
      subjectId = "chem",
      chapterId = "chem_ch1",
      title = "Mole Concept & Stoichiometry Numericals Step-by-Step",
      urduTitle = "مول اور عددی حسابات کے سوالات",
      instructorName = "Prof. Tariq Mehmood (Ex-Paper Setter)",
      durationMinutes = 32,
      isPro = true
    ),
    VideoLecture(
      id = "lec_103",
      subjectId = "bio",
      chapterId = "bio_ch1",
      title = "Human Respiratory System & Gas Exchange Mechanism",
      urduTitle = "انسانی نظام تنفس اور گیسوں کا تبادلہ",
      instructorName = "Dr. Fatima Zahra (King Edward)",
      durationMinutes = 28,
      isPro = false
    )
  )

  val pastPaperQuestions = listOf(
    PastPaperQuestion(
      id = "pp_1",
      questionText = "Derive 2nd and 3rd equations of motion by graphical method.",
      urduQuestionText = "گراف کے طریقے سے حرکت کی دوسری اور تیسری مساوات اخذ کریں۔",
      subject = "Physics",
      chapter = "Kinematics",
      topic = "Equations of Motion",
      difficulty = "High",
      marks = 5,
      yearsRepeated = listOf(2024, 2023, 2021, 2019, 2018, 2016, 2014, 2012),
      boardNames = listOf("Lahore", "Rawalpindi", "Faisalabad", "Multan", "Gujranwala"),
      repeatProbability = 94
    ),
    PastPaperQuestion(
      id = "pp_2",
      questionText = "Differentiate between Compound and Mixture with four key points.",
      urduQuestionText = "مرکب اور مکسچر میں چار بنیادی فرق واضح کریں۔",
      subject = "Chemistry",
      chapter = "Fundamentals of Chemistry",
      topic = "Matter Classification",
      difficulty = "Medium",
      marks = 4,
      yearsRepeated = listOf(2024, 2022, 2020, 2019, 2017, 2015, 2013),
      boardNames = listOf("Lahore", "Sahiwal", "Sargodha", "DG Khan", "Bahawalpur"),
      repeatProbability = 89
    ),
    PastPaperQuestion(
      id = "pp_3",
      questionText = "Explain the structure of Alveoli and human gaseous exchange.",
      urduQuestionText = "ایلویولائی کی ساخت اور انسان میں گیسوں کا تبادلہ بیان کریں۔",
      subject = "Biology",
      chapter = "Gaseous Exchange",
      topic = "Human Lungs",
      difficulty = "Medium",
      marks = 5,
      yearsRepeated = listOf(2023, 2022, 2021, 2018, 2017, 2015),
      boardNames = listOf("Rawalpindi", "Faisalabad", "Gujranwala", "Lahore"),
      repeatProbability = 86
    ),
    PastPaperQuestion(
      id = "pp_4",
      questionText = "Solve by quadratic formula: 2x² + 9x - 5 = 0.",
      urduQuestionText = "دو درجی فارمولا کی مدد سے حل کریں: 2x² + 9x - 5 = 0",
      subject = "Mathematics",
      chapter = "Quadratic Equations",
      topic = "Quadratic Formula",
      difficulty = "Medium",
      marks = 4,
      yearsRepeated = listOf(2024, 2023, 2020, 2019, 2018, 2016),
      boardNames = listOf("Lahore", "Gujranwala", "Multan", "Sargodha"),
      repeatProbability = 91
    ),
    PastPaperQuestion(
      id = "pp_5",
      questionText = "Explain Newton's Second Law of Motion and derive F = ma.",
      urduQuestionText = "نیوٹن کا حرکت کا دوسرا قانون بیان کریں اور F = ma ثابت کریں۔",
      subject = "Physics",
      chapter = "Dynamics",
      topic = "Newton's Laws",
      difficulty = "Medium",
      marks = 5,
      yearsRepeated = listOf(2023, 2022, 2020, 2017, 2015, 2011),
      boardNames = listOf("Lahore", "Rawalpindi", "Multan", "Faisalabad"),
      repeatProbability = 92
    )
  )

  val sampleMcqs = listOf(
    McqQuestion(
      id = "mcq_1",
      question = "The least count of a standard Vernier Calipers is:",
      urduQuestion = "عام ورنیئر کیلیپرز کا لیسٹ کاؤنٹ ہوتا ہے:",
      options = listOf("0.1 mm (0.01 cm)", "0.01 mm", "1 mm", "0.001 cm"),
      urduOptions = listOf("0.1 ملی میٹر (0.01 سینٹی میٹر)", "0.01 ملی میٹر", "1 ملی میٹر", "0.001 سینٹی میٹر"),
      correctOptionIndex = 0,
      explanation = "Least count is the difference between one main scale division (1 mm) and one vernier scale division (0.9 mm), which equals 0.1 mm or 0.01 cm.",
      urduExplanation = "لیسٹ کاؤنٹ مین سکیل ڈویژن (1 ملی میٹر) اور ورنیئر سکیل ڈویژن (0.9 ملی میٹر) کا فرق ہے جو 0.1 ملی میٹر کے برابر ہوتا ہے۔"
    ),
    McqQuestion(
      id = "mcq_2",
      question = "Which of the following is a derived quantity?",
      urduQuestion = "مندرجہ ذیل میں سے ماخوذ مقدار کون سی ہے؟",
      options = listOf("Speed", "Mass", "Length", "Time"),
      urduOptions = listOf("رفتار (سپیڈ)", "ماس", "لمبائی", "وقت"),
      correctOptionIndex = 0,
      explanation = "Mass, length, and time are fundamental base quantities. Speed is derived as distance divided by time (m/s).",
      urduExplanation = "ماس، لمبائی اور وقت بنیادی مقداریں ہیں جبکہ رفتار ماخوذ مقدار ہے (فاصلہ بٹا وقت)۔"
    ),
    McqQuestion(
      id = "mcq_3",
      question = "The value of Avogadro's number is:",
      urduQuestion = "ایووگیڈرو نمبر کی قیمت کیا ہے؟",
      options = listOf("6.02 × 10²³", "6.02 × 10²²", "1.66 × 10⁻²⁴", "3.00 × 10⁸"),
      urduOptions = listOf("6.02 × 10²³", "6.02 × 10²²", "1.66 × 10⁻²⁴", "3.00 × 10⁸"),
      correctOptionIndex = 0,
      explanation = "Avogadro's number is the number of constituent particles (usually molecules, atoms or ions) that are contained in one mole.",
      urduExplanation = "ایک مول مادے میں موجود ذرات کی تعداد 6.02 ضرب 10 کی پاور 23 ہوتی ہے۔"
    ),
    McqQuestion(
      id = "mcq_4",
      question = "In humans, gaseous exchange occurs predominantly across the walls of:",
      urduQuestion = "انسانوں میں گیسوں کا تبادلہ بنیادی طور پر کن کی دیواروں سے ہوتا ہے؟",
      options = listOf("Alveoli", "Trachea", "Bronchi", "Larynx"),
      urduOptions = listOf("ایلویولائی", "ٹریکیا", "برونکائی", "لیرنکس"),
      correctOptionIndex = 0,
      explanation = "Alveoli provide a huge, moist, thin-walled surface richly supplied with blood capillaries for rapid diffusion of O2 and CO2.",
      urduExplanation = "ایلویولائی کی باریک نم دار دیواریں خون کے ساتھ گیسوں کے تبادلے کے لیے مثالی سطح فراہم کرتی ہیں۔"
    )
  )

  val sampleTutors = listOf(
    TutorItem(
      id = "tut_1",
      name = "Sir Asif Javed",
      urduName = "سر آصف جاوید",
      title = "Physics Specialist (Punjab College / BISE Lahore)",
      urduTitle = "طبیعیات کے ماہر استاد (پنجاب کالج)",
      rating = 4.95,
      reviewsCount = 312,
      hourlyRatePkr = 1500,
      subjects = listOf("Physics", "Mathematics"),
      availability = "Mon, Wed, Fri (5 PM - 9 PM)"
    ),
    TutorItem(
      id = "tut_2",
      name = "Dr. Fatima Zahra",
      urduName = "ڈاکٹر فاطمہ زہرا",
      title = "Biology & Medical Entry Test Mentor",
      urduTitle = "حیاتیات و میڈیکل اینٹری ٹیسٹ رہنما",
      rating = 4.98,
      reviewsCount = 428,
      hourlyRatePkr = 1800,
      subjects = listOf("Biology"),
      availability = "Tue, Thu, Sat (4 PM - 8 PM)"
    ),
    TutorItem(
      id = "tut_3",
      name = "Prof. Tariq Mehmood",
      urduName = "پروفیسر طارق محمود",
      title = "Chemistry Board Paper Setter & Ex-Examiner",
      urduTitle = "کیمسٹری بورڈ پیپر سیٹر اور سابق ممتحن",
      rating = 4.92,
      reviewsCount = 285,
      hourlyRatePkr = 1600,
      subjects = listOf("Chemistry"),
      availability = "Daily (6 PM - 10 PM)"
    )
  )

  val mockExams = listOf(
    MockExamItem(
      id = "mock_phys_1",
      title = "Physics Matric Full Syllabus Board Simulation 2026",
      urduTitle = "طبیعیات مکمل نصاب ماک امتحانی پرچہ 2026",
      subject = "Physics",
      grade = "10th",
      totalMarks = 60,
      durationMinutes = 120,
      isLiveScheduled = true,
      scheduledTime = "Sunday, 5:00 PM PST",
      enrolledStudentsCount = 8450
    ),
    MockExamItem(
      id = "mock_chem_1",
      title = "Chemistry BISE Pattern Term Mock Exam",
      urduTitle = "کیمسٹری پنجاب بورڈ پیٹرن ماک ٹیسٹ",
      subject = "Chemistry",
      grade = "10th",
      totalMarks = 60,
      durationMinutes = 120,
      isLiveScheduled = false,
      scheduledTime = "Anytime",
      enrolledStudentsCount = 5230
    )
  )
}
