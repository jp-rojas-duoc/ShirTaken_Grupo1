package cl.shirtaken.shirtaken_grupo1.ui.utils

object Validadores {
    fun nombre(x: String) = if (x.length >= 3) null else "Mínimo 3 caracteres"
    fun email(x: String) =
        if (Regex("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$").matches(x)) null else "Email inválido"
    fun telefono(x: String) =
        if (Regex("^\\+?\\d{8,15}$").matches(x)) null else "Teléfono inválido"
    fun direccion(x: String) = if (x.length >= 5) null else "Dirección muy corta"
}
