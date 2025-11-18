package cl.shirtaken.shirtaken_grupo1.repository

import cl.shirtaken.shirtaken_grupo1.data.remote.PoleraDto
import cl.shirtaken.shirtaken_grupo1.model.Polera

fun PoleraDto.aModelo(): Polera = Polera(
    id = (id ?: 0).toInt(),
    nombre = nombre,
    marca = marca,
    precio = precio,
    talla = talla,
    color = color,
    urlImagen = urlImagen,
    conStock = stock > 0,
    esFavorita = esFavorita
)

fun Polera.aDto(stock: Int = if (conStock) 1 else 0): PoleraDto = PoleraDto(
    id = id.toLong(),
    nombre = nombre,
    marca = marca,
    precio = precio,
    talla = talla,
    color = color,
    urlImagen = urlImagen,
    stock = stock,
    esFavorita = esFavorita
)
