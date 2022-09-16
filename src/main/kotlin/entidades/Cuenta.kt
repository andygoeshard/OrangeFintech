package entidades

import java.time.LocalDate

data class Cuenta(

    val codigoCuenta: Int,
    val nombre: String,
    val apellido: String,
    val dineroEnCuenta: Double,
    val fechaAlta: LocalDate

)
