import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("planId")
    val planId: String? = null,

    @SerializedName("memberId")
    val memberId: String? = null,

    @SerializedName("amount")  // Cambiado de "monto" a "amount"
    val monto: Double? = null,

    @SerializedName("date")  // Cambiado de "fecha" a "date"
    val fecha: String? = null
)