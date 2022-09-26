package InteraccionConElUsuario
import entidades.*
import repositorios.CompraRepositorio
import repositorios.ErrorAlIngresar
import repositorios.UsuarioRepositorio
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period


fun main(){

    println("Bienvenido a orange!")
    funcionesPrincipal()

}

fun ingresoOpcion(): Char{

    return readln()[0]

}

/* Menu principal */

fun funcionesPrincipal(){

    do{
        menuInicialPrint()

        val opcion = ingresoOpcion()

        when(opcion){
            '1' -> ingresarUsuario()
            '2' -> crearUsuario()
            '3' -> {println("Adios"); break;}
            else -> println("No ingreso la opcion correcta")
        }
    } while(opcion != '3')

}

// Solo texto

fun menuInicialPrint(){
    println("""
        1- Ingresar usuario
        2- Crear usuario
        3- Salir
        Ingrese su opcion: 
    """.trimIndent())
}

/* Funcion Ingresar Usuario */

fun ingresarUsuario(){
        var usuario: Usuario

        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()
        println("Ingresar contraseña:")
        val contrasenia = readln()

        try {
            usuario = UsuarioRepositorio.iniciar(nombreDeUsuario, contrasenia)
            menu(usuario)
        } catch(e: ErrorAlIngresar){
            println("Usuario y/o contraseña incorrecto!")
            println("""
                1- Volver a intentar
                2- Salir
                Ingrese su opcion: 
            """.trimIndent())

            val opcion = ingresoOpcion()

            if(opcion == '2') funcionesPrincipal() else if(opcion == '1') ingresarUsuario()
        }
}

/*      Funcion crear Usuario

* Se crea el objeto usuario vacio y luego se ingresa el nombre y el pw.
* Al ingresar el pw hace la comprobacion en la propia funcion y en la clase usuario.
* Si es la funcion de comprobacion retorna true setea la pw del objeto usuario y lo agregar al repositorio
* El while es para iterar cuando la confirmacion es false. Si entra por false se tiene que ingresar
* la contraseña infinitamente hasta que se cumpla los requisitos.

*/

fun crearUsuario(){

    val tiempoYDiaAhora: LocalDate = LocalDate.now()
    val nuevo = Usuario("", "", 0, "","", 0.0, 0.0,tiempoYDiaAhora)
    var iteracion = true

    // Asignacion del codigo de usuario. Obtiene el ultimo codigo de usuario de la lista y le suma uno

    val nuevoCodigoUsuario: Int = UsuarioRepositorio.usuarios.last().codigoCuenta + 1
    nuevo.codigoCuenta = nuevoCodigoUsuario

    while(iteracion) {

        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()

        println("Recuerde que: ")
        println("Su contraseña debe llevar al menos: \n 1 mayuscula - 1 minuscula \n 1 numero - 1 caracter especial")
        println("Ingresar contraseña:")
        val contrasenia = readln()

        println("Confirmar contraseña:")
        val confirmacion = readln()

        if (contrasenia.equals(confirmacion) && !UsuarioRepositorio.existe(nombreDeUsuario,contrasenia) && nuevo.comprobarContraseniaAlCrear(contrasenia)) {
            nuevo.nickname = nombreDeUsuario
            iteracion = false

        } else {
            println("El usuario ya existe o la contraseña no coincide con la confirmación")
            println("Recuerde que: ")
            println("Su contraseña debe llevar al menos: \n 1 mayuscula - 1 minuscula \n 1 numero - 1 caracter especial")
        }

    }

    println("Ingresar su nombre: ")
    val nombrePersonal = readln()
    println("Ingresar su apellido: ")
    val apellidoPersonal = readln()

    nuevo.nombre = nombrePersonal
    nuevo.apellido = apellidoPersonal
    UsuarioRepositorio.agregar(nuevo)

    funcionesPrincipal()

}

/* Una vez el usuario haya podido ingresar correctamente. */

fun menu(usuario: Usuario){

    val usuarioActivo = usuario

    do{
        println(
            """
        1- Comprar criptomonedas
        2- Ver informacion de la cuenta
        3- Ver listado de transacciones
        4- Agregar saldo a la cuenta
        5- Desloguear
        Ingrese su opcion: 
    """.trimIndent())
        val opcion = readln()[0]
        when(opcion){
            '1' -> comprarCriptomonedas(usuarioActivo)
            '2' -> getInfoCuenta(usuarioActivo)
            '3' -> getListaDeTransacciones(usuarioActivo)
            '4' -> agregarSaldo(usuarioActivo)
            '5' -> break
        }
    } while(opcion != '5')
}

// 1 - Comprar Criptomonedas.

fun comprarCriptomonedas(usuario: Usuario) {

    val usuarioActivo = usuario

    println("Selecciona un exchange:")
    println(
        """
        1- Criptomas
        2- Criptodia
        3- Carrecripto
        Ingrese su opcion: 
    """.trimIndent()
    )

    val opcion = ingresoOpcion()
    when(opcion){

        '1' -> compraCriptomonedasCriptomas(usuarioActivo)
        '2' -> compraCriptomonedasCriptodia(usuarioActivo)
        '3' -> compraCriptomonedasCarrecripto(usuarioActivo)
        '4' -> println("No ingreso una opcion correcta")

    }
}

// Funciones de comprar criptomonedas

fun compraCriptomonedasCriptomas(usuario: Usuario){

    println("Usted tiene $ ${usuario.dineroEnCuenta} en su cuenta disponible para comprar.")
    println("Usted tiene ${usuario.criptomonedasEnCuenta} Criptomonedas en su cuenta.")
    println("Ingrese un valor para comprar criptomonedas: ")

    val dineroACambiar = readLine()!!.toDouble()
    val comision = (dineroACambiar.times(Criptomas.calcularComision()))
    val dineroACambiarMasComision = dineroACambiar.plus(comision)
    val cashback = (dineroACambiarMasComision.times(otorgarCashback(usuario)))
    val dineroACambiarMasComisionMenosCashback = dineroACambiarMasComision.minus(cashback)

    try{
        usuario.checkDineroACambiar(dineroACambiarMasComisionMenosCashback)
    } catch(e: SaldoInsuficiente){
        println("Error, no se pudo realizar la transaccion.")
    } finally {
        val VALOR_CRIPTOMONEDA = 1.0
        val VALOR_DINERO = 50.0

        val valorTotalCriptomonedas = (dineroACambiar.times(VALOR_CRIPTOMONEDA)).div(VALOR_DINERO)

        usuario.criptomonedasEnCuenta += valorTotalCriptomonedas
        usuario.dineroEnCuenta -= dineroACambiarMasComisionMenosCashback

        val comisionString = "2%"

        println("Se cobro una comision de $ ${comision} (${comisionString}) \nUsted compro ${valorTotalCriptomonedas} Criptomonedas de Criptomas \n" +
                "Se otorgó un cashback de $cashback \nMuchas gracias por la compra.")

        // Funcion Agregar nueva compra
        agregarNuevaCompra(usuario.nickname, CompraRepositorio.compra.last().codigoCompra + 1, LocalDate.now(), LocalTime.now(), Criptomonedas.CRIPTOMAS, valorTotalCriptomonedas, dineroACambiar, comisionString)

        // Guardar cuenta en repositorio
        val codigoDeLaCuenta: Int = usuario.codigoCuenta
        UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
    }
}

fun compraCriptomonedasCriptodia(usuario: Usuario){

    println("Usted tiene $ ${usuario.dineroEnCuenta} en su cuenta disponible para comprar.")
    println("Usted tiene ${usuario.criptomonedasEnCuenta} Criptomonedas en su cuenta.")
    println("Ingrese un valor para comprar criptomonedas: ")

    val dineroACambiar = readLine()!!.toDouble()
    val comision = (dineroACambiar.times(Criptodia.calcularComision()))
    val dineroACambiarMasComision = dineroACambiar.plus(comision)
    val cashback = (dineroACambiarMasComision.times(otorgarCashback(usuario)))
    val dineroACambiarMasComisionMenosCashback = dineroACambiarMasComision.minus(cashback)

   try{
        usuario.checkDineroACambiar(dineroACambiarMasComisionMenosCashback)
    } catch(e: SaldoInsuficiente){
        println("Error, no se pudo realizar la transaccion.")
    } finally {
        val VALOR_CRIPTOMONEDA = 1.0
        val VALOR_DINERO = 50.0

        val valorTotalCriptomonedas = (dineroACambiar.times(VALOR_CRIPTOMONEDA)).div(VALOR_DINERO)

        usuario.criptomonedasEnCuenta += valorTotalCriptomonedas
        usuario.dineroEnCuenta -= dineroACambiarMasComisionMenosCashback

        val comisionString= if(Criptodia.calcularComision() == 0.01) "1%" else "3%"

        println("Se cobro una comision de $ ${comision} ${comisionString}\n Usted compro ${valorTotalCriptomonedas} Criptomonedas de Criptodia \n" +
                " Se otorgó un cashback de $cashback \nMuchas gracias por la compra.")

        // Funcion Agregar nueva compra

        agregarNuevaCompra(usuario.nickname, CompraRepositorio.compra.last().codigoCompra + 1, LocalDate.now(), LocalTime.now(), Criptomonedas.CRIPTODIA, valorTotalCriptomonedas, dineroACambiar, comisionString)

        // Guardar cuenta en repositorio
        val codigoDeLaCuenta: Int = usuario.codigoCuenta
        UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
    }
}

fun compraCriptomonedasCarrecripto(usuario: Usuario){

    println("Usted tiene $ ${usuario.dineroEnCuenta} en su cuenta disponible para comprar.")
    println("Usted tiene ${usuario.criptomonedasEnCuenta} Criptomonedas en su cuenta.")
    println("Ingrese un valor para comprar criptomonedas: ")

    val dineroACambiar = readLine()!!.toDouble()

    val comision = (dineroACambiar.times(Carrecripto.calcularComision()))

    val dineroACambiarMasComision = dineroACambiar.plus(comision)
    val cashback = (dineroACambiarMasComision.times(otorgarCashback(usuario)))
    val dineroACambiarMasComisionMenosCashback = dineroACambiarMasComision.minus(cashback)

    try{
        usuario.checkDineroACambiar(dineroACambiarMasComisionMenosCashback)
    } catch(e: SaldoInsuficiente){
        println("Error, no se pudo realizar la transaccion.")
    } finally {
        val VALOR_CRIPTOMONEDA = 1.0
        val VALOR_DINERO = 50.0

        val valorTotalCriptomonedas = (dineroACambiar.times(VALOR_CRIPTOMONEDA)).div(VALOR_DINERO)

        usuario.criptomonedasEnCuenta += valorTotalCriptomonedas
        usuario.dineroEnCuenta -= dineroACambiarMasComisionMenosCashback

        val comisionString = if(Carrecripto.calcularComision() == 0.03) "(3%)" else "(0.75%)"

        println("Se cobro una comision de $ ${comision} ${comisionString}\n Usted compro ${valorTotalCriptomonedas} Criptomonedas de Carrecripto \n Se otorgó un cashback de $cashback \n Muchas gracias por la compra.")

        // Funcion Agregar nueva compra

        agregarNuevaCompra(usuario.nickname, CompraRepositorio.compra.last().codigoCompra + 1, LocalDate.now(), LocalTime.now(), Criptomonedas.CARRECRIPTO, valorTotalCriptomonedas, dineroACambiar, comisionString)

        // Guardar cuenta en repositorio
        val codigoDeLaCuenta: Int = usuario.codigoCuenta
        UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
    }
}

// Agregar nueva compra usado en funciones de comprar criptomonedas

private fun agregarNuevaCompra(usuarioNickname: String, nuevoCodigoCompra: Int, fechaAhora: LocalDate, horaAhora: LocalTime, criptomonedaTipo: Criptomonedas, valorTotalCriptomonedas: Double, dineroACambiar: Double, comision: String){

    val nuevaCompra = Compra(usuarioNickname, nuevoCodigoCompra, fechaAhora, horaAhora, criptomonedaTipo, valorTotalCriptomonedas, dineroACambiar, comision)
    CompraRepositorio.agregar(nuevaCompra)

}

// Muestra informacion de la cuenta activa.


fun getInfoCuenta(usuario: Usuario){

    val codigoUsuario: Int = usuario.codigoCuenta
    UsuarioRepositorio.obtenerPorCodigo(codigoUsuario)

}

// Muestra informacion de las compras de la cuenta activa

fun getListaDeTransacciones(usuario: Usuario) {

    CompraRepositorio.obtenerListaCompraPorUsuario(usuario.nickname)

}

// Agrega saldo a la cuenta activa

fun agregarSaldo(usuario: Usuario){

    var iteracion = true

    while(iteracion) {

        println("Ingrese el saldo para agregar a la cuenta: ")
        val saldo = readln().toDouble()
        if(usuario.agregarSaldo(saldo)) {
            // Romper la iteracion, agregar saldo a la cuenta y guardarlo en UsuarioRepositorio
            iteracion = false
            val codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
            println("Saldo agregado correctamente.\n")
        }
        else println("No ingreso un valor valido.\n")

    }

}

fun otorgarCashback(usuario: Usuario): Double {

    var cashback = 0.0
    cashback = if (Period.between(usuario.fechaAlta, LocalDate.now()).toTotalMonths() <= 3) {
        0.05
    } else if (Period.between(usuario.fechaAlta, LocalDate.now()).toTotalMonths() <= 12){
        0.03
    } else {
        0.0
    }
    return cashback
}









