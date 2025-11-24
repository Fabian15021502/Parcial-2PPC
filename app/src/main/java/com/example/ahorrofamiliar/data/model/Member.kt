import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("name")  // Cambiado de "nombre" a "name"
    val nombre: String? = null,

    @SerializedName("planId")
    val planId: String? = null,

    @SerializedName("contributionPerMonth")  // Ajusta según tu API
    val aporteMensual: Double? = null,

    @SerializedName("joinedAt")  // Ajusta según tu API
    val fechaUnion: String? = null
)