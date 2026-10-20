package com.example.fundaciontextil.data

object ChileData {

    val regiones: List<String> = listOf(
        "Arica y Parinacota", "Tarapacá", "Antofagasta", "Atacama",
        "Coquimbo", "Valparaíso", "Metropolitana de Santiago",
        "O'Higgins", "Maule", "Ñuble", "Biobío", "La Araucanía",
        "Los Ríos", "Los Lagos", "Aysén", "Magallanes"
    )

    val comunasPorRegion: Map<String, List<String>> = mapOf(
        "Arica y Parinacota" to listOf("Arica", "Camarones", "General Lagos", "Putre"),
        "Tarapacá" to listOf("Iquique", "Alto Hospicio", "Camiña", "Colchane", "Huara", "Pica", "Pozo Almonte"),
        "Antofagasta" to listOf("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal", "Calama", "Ollagüe", "San Pedro de Atacama", "Tocopilla", "María Elena"),
        "Atacama" to listOf("Copiapó", "Caldera", "Tierra Amarilla", "Chañaral", "Diego de Almagro", "Alto del Carmen", "Freirina", "Huasco", "Vallenar"),
        "Coquimbo" to listOf("La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paiguano", "Vicuña", "Illapel", "Canela", "Los Vilos", "Salamanca", "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"),
        "Valparaíso" to listOf(
            "Valparaíso", "Casablanca", "Concón", "Juan Fernández", "Puchuncaví", "Quintero", "Viña del Mar",
            "Isla de Pascua", "Los Andes", "Calle Larga", "Rinconada", "San Esteban", "La Ligua", "Cabildo",
            "Papudo", "Petorca", "Zapallar", "Quillota", "Calera", "Hijuelas", "La Cruz", "Nogales",
            "San Antonio", "Algarrobo", "Cartagena", "El Quisco", "El Tabo", "Santo Domingo",
            "San Felipe", "Llaillay", "Putaendo", "Santa María", "Panquehue",
            "Quilpué", "Limache", "Olmué", "Villa Alemana"
        ),
        "Metropolitana de Santiago" to listOf(
            "Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
            "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana",
            "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú",
            "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura",
            "Quinta Normal", "Recoleta", "Renca", "San Miguel", "San Joaquín", "San Ramón", "Vitacura",
            "Puente Alto", "Pirque", "San José de Maipo", "Colina", "Lampa", "Tiltil", "Buin",
            "Calera de Tango", "Paine", "San Bernardo", "Alhué", "Curacaví", "María Pinto", "Melipilla",
            "San Pedro", "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"
        ),
        "O'Higgins" to listOf(
            "Rancagua", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "Las Cabras", "Machalí",
            "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua", "Quinta de Tilcoco", "Rengo",
            "Requínoa", "San Vicente", "Pichilemu", "La Estrella", "Litueche", "Marchihue", "Navidad",
            "Paredones", "San Fernando", "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla",
            "Peralillo", "Placilla", "Pumanque", "Santa Cruz"
        ),
        "Maule" to listOf(
            "Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco", "Pencahue", "Río Claro",
            "San Clemente", "San Rafael", "Cauquenes", "Chanco", "Pelluhue", "Curicó", "Hualañé",
            "Licantén", "Molina", "Rauco", "Romeral", "Sagrada Familia", "Teno", "Vichuquén",
            "Linares", "Colbún", "Longaví", "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"
        ),
        "Ñuble" to listOf(
            "Chillán", "Bulnes", "Cobquecura", "Coelemu", "Coihueco", "Chillán Viejo", "El Carmen",
            "Ninhue", "Ñiquén", "Pemuco", "Pinto", "Portezuelo", "Quillón", "Quirihue", "Ránquil",
            "San Carlos", "San Fabián", "San Ignacio", "San Nicolás", "Treguaco", "Yungay"
        ),
        "Biobío" to listOf(
            "Concepción", "Coronel", "Chiguayante", "Florida", "Hualqui", "Lota", "Penco",
            "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Hualpén",
            "Lebu", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Los Álamos", "Tirúa",
            "Los Ángeles", "Antuco", "Cabrero", "Laja", "Mulchén", "Nacimiento", "Negrete",
            "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel", "Yumbel", "Alto Biobío"
        ),
        "La Araucanía" to listOf(
            "Temuco", "Carahue", "Cholchol", "Cunco", "Curarrehue", "Freire", "Galvarino", "Gorbea",
            "Lautaro", "Loncoche", "Melipeuco", "Nueva Imperial", "Padre Las Casas", "Perquenco",
            "Pitrufquén", "Pucón", "Saavedra", "Teodoro Schmidt", "Toltén", "Vilcún", "Villarrica",
            "Angol", "Collipulli", "Curacautín", "Ercilla", "Lonquimay", "Los Sauces", "Lumaco",
            "Purén", "Renaico", "Traiguén", "Victoria"
        ),
        "Los Ríos" to listOf(
            "Valdivia", "Corral", "Lanco", "Los Lagos", "Máfil", "Mariquina", "Paillaco",
            "Panguipulli", "La Unión", "Futrono", "Lago Ranco", "Río Bueno"
        ),
        "Los Lagos" to listOf(
            "Puerto Montt", "Calbuco", "Cochamó", "Fresia", "Frutillar", "Los Muermos", "Llanquihue",
            "Maullín", "Puerto Varas", "Castro", "Ancud", "Chonchi", "Curaco de Vélez", "Dalcahue",
            "Puqueldón", "Queilén", "Quellón", "Quemchi", "Quinchao",
            "Osorno", "Puerto Octay", "Purranque", "Puyehue", "Río Negro", "San Juan de la Costa", "San Pablo",
            "Chaitén", "Futaleufú", "Hualaihué", "Palena" 
        ),
        "Aysén" to listOf(
            "Coyhaique", "Lago Verde", "Aysén", "Cisnes", "Guaitecas", "Chile Chico",
            "O'Higgins", "Tortel", "Cochrane", "Río Ibáñez"
        ),
        "Magallanes" to listOf(
            "Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio",
            "Cabo de Hornos", "Antártica", "Porvenir", "Primavera", "Timaukel",
            "Natales", "Torres del Paine"
        )
    )

    fun comunasDe(region: String): List<String> = comunasPorRegion[region].orEmpty()

    val rubrosTextiles: List<String> = listOf(
        "Reutilización de telas",
        "Upcycling / Transformación",
        "Confección sostenible",
        "Tejido y bordado",
        "Reparación textil",
        "Accesorios reciclados",
        "Diseño circular"
    )

    val rangosEtarios: List<String> = listOf(
        "18 - 24", "25 - 34", "35 - 44", "45 - 54", "55 - 64", "65 o más"
    )

    val generos: List<String> = listOf(
        "Femenino", "Masculino", "Otro", "Prefiero no decir"
    )
}