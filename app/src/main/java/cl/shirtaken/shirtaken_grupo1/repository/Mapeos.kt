package cl.shirtaken.shirtaken_grupo1.repository

import cl.shirtaken.shirtaken_grupo1.data.local.PoleraEntity
import cl.shirtaken.shirtaken_grupo1.model.Polera

fun PoleraEntity.aModelo() = Polera(
    id = id,
    nombre = nombre,
    marca = marca,
    precio = precio,
    talla = talla,
    color = color,
    urlImagen = urlImagen,
    conStock = stock > 0,
    esFavorita = esFavorita
)

fun Polera.aEntity(stock: Int = if (conStock) 1 else 0) = PoleraEntity(
    id = if (id == 0) 0 else id,
    nombre = nombre,
    marca = marca,
    precio = precio,
    talla = talla,
    color = color,
    urlImagen = urlImagen,
    stock = stock,
    esFavorita = esFavorita
)
