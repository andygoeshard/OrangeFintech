package InteraccionConElUsuario
import entidades.Usuario
import repositorios.UsuarioRepositorio

fun main(){
    val usuario = Usuario("", "")
    println("Bienvenido a orange!")
    funciones()

}

fun menuInicial(): Char{
    println("""
        1- Ingresar usuario
        2- Crear usuario
        3- Salir
    """.trimIndent())
    val opcion = readln()[0]
    return opcion
}

fun funciones(){
    do{
        val opcion = menuInicial()
        when(opcion){
            '1' -> ingresarUsuario()
            '2' -> crearUsuario()
            '3'-> break
        }
    } while(opcion != '3')
}

fun ingresarUsuario(){
    var opcion: Char = ' '
    do{
        println("Ingresar nombre de usuario:")
        val nombreDeUsuario = readln()
        println("Igresar contraseña:")
        val contrasenia = readln()
        if(UsuarioRepositorio.existe(nombreDeUsuario)){
            UsuarioRepositorio.iniciar(nombreDeUsuario, contrasenia)
            menu()
        } else{
            println("Usuario y/o contraseña incorrecto!")
            println("""
                1- Volver a intentar
                2- Salir
            """.trimIndent())
            opcion = readln()[0]
            if(opcion == '2') funciones()
        }
    } while(!UsuarioRepositorio.existe(nombreDeUsuario))
}

fun crearUsuario(){
    println("Ingresar nombre de usuario:")
    val nombreDeUsuario = readln()
    println("Igresar contraseña:")
    val contrasenia = readln()
    println("Confirmar contraseña:")
    val confirmacion = readln()
    if(contrasenia.equals(confirmacion) && !UsuarioRepositorio.existe(nombreDeUsuario)) {
        val nuevo = Usuario(nombreDeUsuario, contrasenia)
        UsuarioRepositorio.agregar(nuevo)
    } else {
        println("El usuario ya existe o la contraseña no coincide con la confirmación")
    }
    funciones()
}

fun menu(){
    do{
        println("""
        1- Comprar criptomonedad
        2- Check saldo
        3- Ver listado de transacciones
        4- Desloguear
        5- Salir
    """.trimIndent())
        val opcion = readln()[0]
        when(opcion){
            '1' -> comprarCriptomonedas()
            '2' -> getSaldo()
            '3' -> getListaDeTransacciones()
            '4' -> desloguear()
        }
    } while(opcion != '5')
}

fun desloguear() {
    //UsuarioRepositorio.eliminar()
}

fun getListaDeTransacciones() {
    TODO("Not yet implemented")
}

fun getSaldo() {
    TODO("Not yet implemented")
}

fun comprarCriptomonedas() {
    println("Selecciona un exchange:")
    println("""
        1- Criptomas
        2- Criptodia
        3- Carrecripto
    """.trimIndent())
    val opcion = readln()[0]
}
