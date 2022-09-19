package entidades

import java.time.LocalDate

class Usuario (

    var nickname: String= "",
    var password:String = "",
    var codigoCuenta: Int = 0,
    var nombre: String = "",
    var apellido: String = "",
    var dineroEnCuenta: Double = 0.0,
    var criptomonedas: Double = 0.0,
    val fechaAlta: LocalDate = LocalDate.now()

){

    // Funciones - Creacion de cuenta
    fun comprobarContraseniaAlCrear(contrasenia: String): Boolean {

        var contieneMayuscula = false
        var contieneMinuscula = false
        var contieneNumero = false
        var contieneCaracterEspecial = false

        /* Pasar todo el String a un char de Array e ir comprobando en un for cada letra
         Por ejemplo si contiene mayus su booleano es true. Al final de la comprobacion
         tiene que ser todos true. */

        val arrayChar_Del_String_contrasenia = contrasenia.toCharArray()

        for(i in arrayChar_Del_String_contrasenia.indices){

            if(arrayChar_Del_String_contrasenia[i].isDigit()) contieneNumero = true
            else if(arrayChar_Del_String_contrasenia[i].isUpperCase()) contieneMayuscula = true
            else if(arrayChar_Del_String_contrasenia[i].isLowerCase()) contieneMinuscula = true
            else if(!arrayChar_Del_String_contrasenia[i].isLetterOrDigit()) contieneCaracterEspecial = true

            if(contieneMayuscula == true && contieneMinuscula == true && contieneNumero == true && contieneCaracterEspecial == true) {

                this.password = contrasenia;
                return true

            }


        }

        return false

    }

    fun agregarSaldo(saldo: Double): Boolean{

        if(saldo <= 0) return false
        else this.dineroEnCuenta += saldo; return true;

    }

}