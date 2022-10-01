package InteraccionConElUsuario

import entidades.*
import excepciones.SaldoInsuficiente
import repositorios.CompraRepositorio
import repositorios.UsuarioRepositorio
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period


fun main() {


    println("\n\t\t    | Bienvenido a Orange! |    \n")
    funcionesPrincipal()

}

fun ingresoOpcion(): Char {
    // Esta excepcion hace que cuando ingreso sea nulo tire ingreso incorrecto.
    try {
        val ingreso = readln()[0]
        return ingreso
    } catch (ex: Exception) {
        when (ex) {
            is IllegalAccessException, is IndexOutOfBoundsException -> println("Ingreso incorrecto\n")
        }

    }
    return 'k'
}

/* Menu principal */

fun funcionesPrincipal() {

    do {
        menuInicialPrint()

        val opcion = ingresoOpcion()

        when (opcion) {
            '1' -> ingresarUsuario()
            '2' -> crearUsuario()
            '3' -> {
                println("Adios")
            }

            else -> println("No ingreso la opcion correcta")
        }
    } while (opcion != '3')

}

// Solo texto

fun menuInicialPrint() {
    println(
        """
        1- Ingresar usuario
        2- Crear usuario
        3- Salir
        Ingrese su opcion: 
    """.trimIndent()
    )
}

/* Funcion Ingresar Usuario */

fun ingresarUsuario() {

    var usuario: Usuario

    do {
        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()
        println("Ingresar contraseña:")
        val contrasenia = readln()

        if (UsuarioRepositorio.existe(nombreDeUsuario, contrasenia)) {
            usuario = UsuarioRepositorio.iniciar(nombreDeUsuario, contrasenia)
            menu(usuario)
        } else {
            println("Usuario y/o contraseña incorrecto!")
            println(
                """
                1- Volver a intentar
                2- Salir
                Ingrese su opcion: 
            """.trimIndent()
            )

            val opcion = ingresoOpcion()
            if (opcion == '2') funcionesPrincipal()
        }
    } while (!UsuarioRepositorio.existe(nombreDeUsuario, contrasenia))

}

/*      Funcion crear Usuario

* Se crea el objeto usuario vacio y luego se ingresa el nombre y el pw.
* Al ingresar el pw hace la comprobacion en la propia funcion y en la clase usuario.
* Si es la funcion de comprobacion retorna true setea la pw del objeto usuario y lo agregar al repositorio
* El while es para iterar cuando la confirmacion es false. Si entra por false se tiene que ingresar
* la contraseña infinitamente hasta que se cumpla los requisitos.

*/

fun crearUsuario() {

    val tiempoYDiaAhora: LocalDate = LocalDate.now()
    val nuevo = Usuario("", "", 0, "", "", 0.0, 0.0, tiempoYDiaAhora)
    var iteracion = true

    // Asignacion del codigo de usuario. Obtiene el ultimo codigo de usuario de la lista y le suma uno

    val nuevoCodigoUsuario: Int = UsuarioRepositorio.usuarios.last().codigoCuenta.plus(1)
    nuevo.codigoCuenta = nuevoCodigoUsuario

    while (iteracion) {

        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()

        println("""
            Recuerde que: 
            Su contraseña debe llevar al menos:
            1 mayuscula - 1 minuscula - 1 número - 1 caracter especial
        """.trimIndent())
        println("Ingresar contraseña:")
        println("Ingresar contraseña:")
        val contrasenia = readln()

        println("Confirmar contraseña:")
        val confirmacion = readln()

        if (contrasenia.equals(confirmacion) && !UsuarioRepositorio.existe(
                nombreDeUsuario,
                contrasenia
            ) && nuevo.comprobarContraseniaAlCrear(contrasenia)
        ) {
            nuevo.nickname = nombreDeUsuario
            iteracion = false

        } else {
            println("El usuario ya existe o la contraseña no coincide con la confirmación")
            println("""
                Recuerde que: 
                Su contraseña debe llevar al menos:
                1 mayuscula - 1 minuscula - 1 número - 1 caracter especial
            """.trimIndent())
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

fun menu(usuario: Usuario) {

    val usuarioActivo = usuario

    do {
        println(
            """
        1- Comprar criptomonedas
        2- Ver informacion de la cuenta
        3- Ver listado de transacciones
        4- Agregar saldo a la cuenta
        5- Desloguear
        Ingrese su opcion: 
    """.trimIndent()
        )
        val opcion = ingresoOpcion()
        when (opcion) {
            '1' -> comprarCriptomonedas(usuarioActivo)
            '2' -> getInfoCuenta(usuarioActivo)
            '3' -> getListaDeTransacciones(usuarioActivo)
            '4' -> agregarSaldo(usuarioActivo)
            '5' -> break
        }
    } while (opcion != '5')
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
    when (opcion) {

        '1' -> compraCriptomonedasCriptomas(usuarioActivo)
        '2' -> compraCriptomonedasCriptodia(usuarioActivo)
        '3' -> compraCriptomonedasCarrecripto(usuarioActivo)
        '4' -> println("No ingreso una opcion correcta")

    }
}

// Funciones de comprar criptomonedas

fun compraCriptomonedasCriptomas(usuario: Usuario) {

    comprarCriptomonedasPrinty(usuario)

    val dineroACambiar = ingresoDouble()
    val dineroTotal = calcularDineroTotal(dineroACambiar, Criptodia)
    val comision = dineroTotal.minus(dineroACambiar)
    val cashback = (dineroTotal.times(otorgarCashback(usuario)))

    try {
        if (usuario.checkDineroACambiar(dineroTotal)) {

            val valorTotalCriptomonedas = editarUsuarioAlComprar(dineroACambiar, dineroTotal, cashback, usuario)

            val comisionString = "2%"

            var mensaje =
                "\t\t-COMPRA REALIZADA-\nUsted compro $${valorTotalCriptomonedas} Criptomonedas de Criptomas \n" +
                        "Se cobro una comision de \$ ${comision} (${comisionString}) \n"

            if (cashback != 0.0) mensaje += "Se otorgó un cashback de $cashback \nMuchas gracias por la compra."
            else mensaje += "Muchas gracias por la compra.\n"

            println(mensaje)

            // Funcion Agregar nueva compra
            agregarNuevaCompra(
                usuario.nickname,
                CompraRepositorio.compra.last().codigoCompra + 1,
                LocalDate.now(),
                LocalTime.now(),
                Criptomonedas.CRIPTOMAS,
                valorTotalCriptomonedas,
                dineroACambiar,
                comisionString
            )
            // Guardar cuenta en repositorio
            val codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
        }
    } catch (e: SaldoInsuficiente) {
        println("Error, no se pudo realizar la transaccion.")
    }
}

fun calcularDineroTotal(dineroACambiar: Double, exchange: Exchange): Double {

    val comision = dineroACambiar.times(
        when (exchange) {
            Criptomas -> Criptomas.calcularComision()
            Criptodia -> Criptodia.calcularComision()
            Carrecripto -> Carrecripto.calcularComision()
            else -> Criptomas.calcularComision()

        }
    )

    return dineroACambiar.plus(comision)
}
fun compraCriptomonedasCriptodia(usuario: Usuario) {

    comprarCriptomonedasPrinty(usuario)

    val dineroACambiar = ingresoDouble()
    val dineroTotal = calcularDineroTotal(dineroACambiar, Criptodia)
    val comision = dineroTotal.minus(dineroACambiar)
    val cashback = (dineroTotal.times(otorgarCashback(usuario)))

    try {
        if (usuario.checkDineroACambiar(dineroTotal)) {

            val valorTotalCriptomonedas = editarUsuarioAlComprar(dineroACambiar, dineroTotal, cashback, usuario)

            val comisionString = if (Criptodia.calcularComision() == 0.01) "1%" else "3%"

            // Mensaje para imprimir en consola

            var mensaje =
                "\t\t-COMPRA REALIZADA-\nUsted compro ${valorTotalCriptomonedas} Criptomonedas de Criptodia \n" +
                        "Se cobro una comision de \$ ${comision} (${comisionString}) \n"

            if (cashback != 0.0) mensaje += "Se otorgó un cashback de $cashback \nMuchas gracias por la compra."
            else mensaje += "Muchas gracias por la compra.\n"

            println(mensaje)

            // Funcion Agregar nueva compra

            agregarNuevaCompra(
                usuario.nickname,
                CompraRepositorio.compra.last().codigoCompra + 1,
                LocalDate.now(),
                LocalTime.now(),
                Criptomonedas.CRIPTODIA,
                valorTotalCriptomonedas,
                dineroACambiar,
                comisionString
            )

            // Guardar cuenta en repositorio
            val codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
        }

    } catch (e: SaldoInsuficiente) {
        println("Error, no se pudo realizar la transaccion.")
    }

}

fun compraCriptomonedasCarrecripto(usuario: Usuario) {

    comprarCriptomonedasPrinty(usuario)

    val dineroACambiar = ingresoDouble()
    val dineroTotal = calcularDineroTotal(dineroACambiar, Carrecripto)
    val comision = dineroTotal.minus(dineroACambiar)
    val cashback = (dineroTotal.times(otorgarCashback(usuario)))

    try {
        if (usuario.checkDineroACambiar(dineroTotal)) {

            val valorTotalCriptomonedas = editarUsuarioAlComprar(dineroACambiar, dineroTotal, cashback, usuario)

            val comisionString = if (Carrecripto.calcularComision() == 0.03) "(3%)" else "(0.75%)"

            // Mensaje para imprimir en consola

            var mensaje =
                "\t\t-COMPRA REALIZADA-\nUsted compro ${valorTotalCriptomonedas} Criptomonedas de Carrecripto \n" +
                        "Se cobro una comision de \$ ${comision} (${comisionString}) \n"

            if (cashback != 0.0) mensaje += "Se otorgó un cashback de $cashback \nMuchas gracias por la compra."
            else mensaje += "Muchas gracias por la compra.\n"

            println(mensaje)

            // Funcion Agregar nueva compra

            agregarNuevaCompra(
                usuario.nickname,
                CompraRepositorio.compra.last().codigoCompra + 1,
                LocalDate.now(),
                LocalTime.now(),
                Criptomonedas.CARRECRIPTO,
                valorTotalCriptomonedas,
                dineroACambiar,
                comisionString
            )

            // Guardar cuenta en repositorio
            val codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
        }
    } catch (e: SaldoInsuficiente) {
        println("Error, no se pudo realizar la transaccion.")
    }
}

fun comprarCriptomonedasPrinty(usuario: Usuario) {

    println("Usted tiene $ ${usuario.dineroEnCuenta} en su cuenta disponible para comprar.")
    println("Usted tiene ${usuario.criptomonedasEnCuenta} Criptomonedas en su cuenta.")
    println("Ingrese un valor para comprar criptomonedas: ")

}

fun editarUsuarioAlComprar(dineroACambiar: Double, dineroTotal: Double, cashback: Double, usuario: Usuario): Double {

    val VALOR_CRIPTOMONEDA = 1.0
    val VALOR_DINERO = 50.0

    val valorTotalCriptomonedas = (dineroACambiar.times(VALOR_CRIPTOMONEDA)).div(VALOR_DINERO)
    usuario.criptomonedasEnCuenta += valorTotalCriptomonedas
    usuario.dineroEnCuenta -= dineroTotal
    usuario.dineroEnCuenta += cashback

    return valorTotalCriptomonedas
}

fun ingresoDouble(): Double { // Funcion que al ingresar dinero null no rompa programita

    var ingreso = 0.0
    try {
        ingreso = readLine()!!.toDouble()
    } catch (ex: Exception) {
        when (ex) {
            is IllegalAccessException, is IndexOutOfBoundsException -> println("Ingreso incorrecto\n")
        }
    }
    return ingreso
}

// Agregar nueva compra usado en funciones de comprar criptomonedas

private fun agregarNuevaCompra(
    usuarioNickname: String,
    nuevoCodigoCompra: Int,
    fechaAhora: LocalDate,
    horaAhora: LocalTime,
    criptomonedaTipo: Criptomonedas,
    valorTotalCriptomonedas: Double,
    dineroACambiar: Double,
    comision: String
) {

    val nuevaCompra = Compra(
        usuarioNickname,
        nuevoCodigoCompra,
        fechaAhora,
        horaAhora,
        criptomonedaTipo,
        valorTotalCriptomonedas,
        dineroACambiar,
        comision
    )
    CompraRepositorio.agregar(nuevaCompra)

}

fun otorgarCashback(usuario: Usuario): Double {
    val periodo = Period.between(usuario.fechaAlta, LocalDate.now()).toTotalMonths()
    return when (periodo) {
        in 0..3 -> 0.05
        in 4..12 -> 0.03
        else -> 0.0
    }
}

/* ------------------------------------------------------------------------------------------------- */

// Muestra informacion de la cuenta activa.


fun getInfoCuenta(usuario: Usuario) {

    val codigoUsuario: Int = usuario.codigoCuenta
    UsuarioRepositorio.obtenerPorCodigo(codigoUsuario)

}

// Muestra informacion de las compras de la cuenta activa

fun getListaDeTransacciones(usuario: Usuario) {

    CompraRepositorio.obtenerListaCompraPorUsuario(usuario.nickname)

}

// Agrega saldo a la cuenta activa

fun agregarSaldo(usuario: Usuario) {

    var iteracion = true

    while (iteracion) {

        println("Ingrese el saldo para agregar a la cuenta: ")
        val saldo = ingresoDouble()
        if (usuario.agregarSaldo(saldo)) {
            // Romper la iteracion, agregar saldo a la cuenta y guardarlo en UsuarioRepositorio
            iteracion = false
            val codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
            println("Saldo agregado correctamente.\n")
        } else println("No ingreso un valor valido.\n")

    }

}











