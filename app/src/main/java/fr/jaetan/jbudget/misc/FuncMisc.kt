package fr.jaetan.jbudget.misc

class FuncMisc {
    companion object {
        private val months: ArrayList<String> = arrayListOf(
            "Janvier",
            "Février",
            "Mars",
            "Avril",
            "Mai",
            "Juin",
            "Juillet",
            "Août",
            "Septembre",
            "Octobre",
            "Novembre",
            "Décembre",
        )

        fun getStringMonth(id: Int): String {
            if(id < 0) return "null"
            return months[id]
        }
    }
}