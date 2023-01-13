package fr.jaetan.jbudget.core.models

import com.google.firebase.auth.FirebaseAuth
import java.sql.Timestamp
import java.util.*

data class Budget(
    var name: String,
    val id: String = "",
    var userId: String = FirebaseAuth.getInstance().currentUser!!.uid,
    var startDate: Date? = null,
    var endDate: Date? = null) {


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "userId" to userId,
            "startDate" to if (startDate == null) null else Timestamp(startDate!!.time / 1000),
            "endDate" to endDate.toString()
        )
    }
}
