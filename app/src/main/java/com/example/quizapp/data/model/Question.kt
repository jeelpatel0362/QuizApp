import android.os.Parcelable
import com.example.quizapp.utils.decodeHtmlEntities
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
) : Parcelable {
    val allAnswers: List<String>
        get() = (incorrect_answers + correct_answer).shuffled()

    val decodedAllAnswers: List<String>
        get() = allAnswers.map { it.decodeHtmlEntities() }
}