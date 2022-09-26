package repositorios

class ErrorAlIngresar(nickname: String, password: String): Exception("Usuario y/o contraseña incorrectos"){
}