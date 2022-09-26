package entidades

class SaldoInsuficiente(dinero: Double): Exception("Su saldo en cuenta ${dinero} es insuficiente para esta transaccion") {
}