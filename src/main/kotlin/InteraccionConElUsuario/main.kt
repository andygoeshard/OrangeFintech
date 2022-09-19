package InteraccionConElUsuario
import entidades.Usuario
import repositorios.UsuarioRepositorio
import java.time.LocalDate


fun main(){

    println("Bienvenido a orange!")
    funcionesPrincipal()

}

fun ingresoOpcion(): Char{

    return readln()[0]

}

/* Primer coso */

fun funcionesPrincipal(){

    do{
        menuInicialPrint()

        var opcion = ingresoOpcion()

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

    var usuario = Usuario("","",0, "", "", 0.0, 0.0, LocalDate.now())

    do{
        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()
        println("Ingresar contraseña:")
        val contrasenia = readln()

        if(UsuarioRepositorio.existe(nombreDeUsuario)){
            usuario = UsuarioRepositorio.iniciar(nombreDeUsuario, contrasenia)
            menu(usuario)
        } else{

            println("Usuario y/o contraseña incorrecto!")
            println("""
                1- Volver a intentar
                2- Salir
                Ingrese su opcion: 
            """.trimIndent())

            var opcion = ingresoOpcion()

            if(opcion == '2') funcionesPrincipal()
        }
    } while(!UsuarioRepositorio.existe(nombreDeUsuario))

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

        if (contrasenia.equals(confirmacion) && !UsuarioRepositorio.existe(nombreDeUsuario) && nuevo.comprobarContraseniaAlCrear(contrasenia)) {
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

    var usuario = usuario

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
            '1' -> comprarCriptomonedas()
            '2' -> getInfoCuenta(usuario)
            '3' -> getListaDeTransacciones()
            '4' -> agregarSaldo(usuario)
            '5' -> break
        }
    } while(opcion != '5')

}

// 1 - Comprar Criptomonedas.

fun comprarCriptomonedas() {

    println("Selecciona un exchange:")
    println("""
        1- Criptomas
        2- Criptodia
        3- Carrecripto
        Ingrese su opcion: 
    """.trimIndent())
    val opcion = ingresoOpcion()

}

fun getInfoCuenta(usuario: Usuario){

    val codigoUsuario: Int = usuario.codigoCuenta
    UsuarioRepositorio.obtenerPorCodigo(codigoUsuario)

}

fun getListaDeTransacciones() {

    // FALTA COMPRAR CRIPTOS, AGREGAR A LA LISTA DE COMPRA Y COMPRA REPOSITORIO.

}

fun agregarSaldo(usuario: Usuario){

    var iteracion = true

    while(iteracion) {

        println("Ingrese el saldo para agregar a la cuenta: ")
        var saldo = readln().toDouble()
        if(usuario.agregarSaldo(saldo)) {
            // Romper la iteracion, agregar saldo a la cuenta y guardarlo en UsuarioRepositorio
            iteracion = false
            var codigoDeLaCuenta: Int = usuario.codigoCuenta
            UsuarioRepositorio.editarPorCodigo(codigoDeLaCuenta, usuario)
            println("Saldo agregado correctamente.\n")
        }
        else println("No ingreso un valor valido.\n")

    }

}




