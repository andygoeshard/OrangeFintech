package entidades

data class Usuario (

    val nickname:String,
    val password:String

){

    fun comprobarContraseniaAlCrear(contrasenia: String): Boolean {

        var contieneMayuscula: Boolean = false
        var contieneMinuscula: Boolean = false
        var contieneNumero: Boolean = false
        //var contieneCaracterEspecial: Boolean = false

        /* Pasar todo el String a un char de Array e ir comprobando en un for cada letra
         Por ejemplo si contiene mayus su booleano es true. Al final de la comprobacion
         tiene que ser todos true. */

        val arrayAux = CharArray(contrasenia.length)

        for(i in arrayAux){

            if(arrayAux[i].isDigit()) contieneNumero = true
            else if(arrayAux[i].isUpperCase()) contieneMayuscula = true
            else if(arrayAux[i].isLowerCase()) contieneMinuscula = true
            //else if(!arrayAux[i].isLetterOrDigit() contieneCaracterEspecial = true

            if(contieneMayuscula == true && contieneMinuscula == true && contieneNumero == true){

                this.password = contrasenia;
                return true

            }

            else return false

        }


    }

}