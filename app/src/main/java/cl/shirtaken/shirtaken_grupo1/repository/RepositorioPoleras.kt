package cl.shirtaken.shirtaken_grupo1.repository

import cl.shirtaken.shirtaken_grupo1.model.Polera

class RepositorioPoleras {
    private val datos = listOf(
        Polera(1,"Polera Basica","ShirTaken", 9990,"M","Rojo","https://texora.cl/img/custom/static/735_rojo_2bae601d834c16df55ed02be7393f580.jpg"),
        Polera(1,"Polera Sport","ShirTaken",12990,"L","Azul","https://mundo-joven.cl/wp-content/uploads/2025/08/standard_resolution-2109.jpg"),
        Polera(3,"Polera Basica","ShirTaken",10990,"S","Blanco","https://tworldstore.cl/4547-superlarge_default/polera-polo-mc-hombre-60-alg-40-poly.jpg",conStock = false)
    )

    fun obtenerTodas(): List<Polera> = datos
    fun obtenerPorId(id: Int): Polera? = datos.firstOrNull{it.id == id}


}