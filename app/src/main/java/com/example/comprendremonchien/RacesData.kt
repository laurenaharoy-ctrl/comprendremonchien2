package com.laurena.comprendremonchien

data class CategorieRace(
    val id: String,
    val nom: String,
    val emoji: String,
    val description: String,
    val races: List<String>,
    val predispositions: List<String>,
    val nuanceAnalyse: String
)

data class InfoRace(
    val categorieId: String,
    val race: String,
    val nuanceSpecifique: String? = null
)

val categoriesRaces = listOf(

    CategorieRace(
        id = "bergers",
        nom = "Chiens de berger & troupeau",
        emoji = "🐕",
        description = "Races sélectionnées pour travailler en étroite collaboration avec l'humain.",
        races = listOf(
            "Border Collie",
            "Berger Allemand",
            "Berger Australien",
            "Berger Belge Malinois",
            "Berger Belge Tervueren",
            "Berger Blanc Suisse",
            "Berger de Brie (Briard)",
            "Berger des Pyrénées",
            "Bouvier des Flandres",
            "Colley"
        ),
        predispositions = listOf(
            "Réactivité naturellement élevée",
            "Hypervigilance fréquente",
            "Fort besoin de stimulation mentale",
            "Sensibilité émotionnelle marquée"
        ),
        nuanceAnalyse = "Les chiens de berger ont été sélectionnés pour être constamment attentifs à leur environnement et réactifs aux moindres signaux. Un score élevé en réactivité ou en sensibilité peut donc refléter en partie leur nature profonde, et non uniquement une difficulté à corriger. Cela ne diminue pas l'importance de travailler ces axes, mais aide à calibrer les attentes et à adapter les méthodes."
    ),

    CategorieRace(
        id = "retrievers",
        nom = "Retrievers & Spaniels",
        emoji = "🦮",
        description = "Races reconnues pour leur sociabilité et leur désir de plaire.",
        races = listOf(
            "Golden Retriever",
            "Labrador Retriever",
            "Cocker Spaniel Anglais",
            "Cocker Américain",
            "Springer Spaniel",
            "Flat Coated Retriever",
            "Nova Scotia Duck Tolling Retriever",
            "Cavalier King Charles"
        ),
        predispositions = listOf(
            "Fort besoin d'attachement et de contact",
            "Tendance à la frustration si peu stimulé",
            "Gestion de l'excitation parfois difficile",
            "Généralement bonne sociabilité"
        ),
        nuanceAnalyse = "Les Retrievers et Spaniels sont naturellement très attachés à leur famille et ont un fort besoin de contact. Un score élevé en attachement est donc très fréquent dans ces races et ne signifie pas forcément une anxiété de séparation pathologique. L'impulsivité peut aussi être présente en contexte de jeu ou d'excitation, ce qui est typique de leur profil énergétique."
    ),

    CategorieRace(
        id = "terriers",
        nom = "Terriers",
        emoji = "🐾",
        description = "Races tenaces, indépendantes et souvent très réactives.",
        races = listOf(
            "Jack Russell Terrier",
            "West Highland White Terrier",
            "Yorkshire Terrier",
            "Bull Terrier",
            "Staffordshire Bull Terrier",
            "American Staffordshire Terrier",
            "Fox Terrier",
            "Airedale Terrier",
            "Border Terrier",
            "Cairn Terrier"
        ),
        predispositions = listOf(
            "Réactivité souvent marquée",
            "Forte personnalité et indépendance",
            "Impulsivité fréquente en jeu",
            "Tendance à la ténacité"
        ),
        nuanceAnalyse = "Les Terriers ont été sélectionnés pour chasser et affronter des proies souvent plus grandes qu'eux — ce qui explique leur tempérament fort, leur réactivité et leur tendance à ne pas lâcher prise. Un score élevé en impulsivité ou en réactivité est très courant dans ces races. Cela ne signifie pas que le chien est \"difficile\", mais que son profil demande une approche adaptée à son énergie naturelle."
    ),

    CategorieRace(
        id = "molosses",
        nom = "Molosses & Dogues",
        emoji = "🦁",
        description = "Races puissantes, souvent calmes mais avec une forte présence.",
        races = listOf(
            "Dogue Allemand",
            "Rottweiler",
            "Boxer",
            "Bouledogue Français",
            "Bouledogue Anglais",
            "Cane Corso",
            "Dogue de Bordeaux",
            "Mastiff",
            "Boerboel",
            "Shar Pei"
        ),
        predispositions = listOf(
            "Tempérament généralement posé",
            "Réactivité possible face aux inconnus",
            "Besoin de cadre clair",
            "Sensibilité émotionnelle parfois sous-estimée"
        ),
        nuanceAnalyse = "Les molosses sont souvent perçus comme des chiens forts et dominants, mais beaucoup sont en réalité très sensibles émotionnellement. Un score élevé en sensibilité n'est pas rare et mérite la même attention que pour toute autre race. Leur gabarit amplifie l'impact de leurs comportements, ce qui rend le travail éducatif particulièrement important même quand les problèmes semblent \"mineurs\"."
    ),

    CategorieRace(
        id = "nordiques",
        nom = "Chiens nordiques & primitifs",
        emoji = "🐺",
        description = "Races proches de leurs instincts originels, souvent indépendantes.",
        races = listOf(
            "Husky Sibérien",
            "Malamute d'Alaska",
            "Samoyède",
            "Spitz Allemand",
            "Akita Inu",
            "Shiba Inu",
            "Basenji",
            "Groenlandais",
            "Chow Chow"
        ),
        predispositions = listOf(
            "Indépendance marquée",
            "Faible besoin de plaire à l'humain",
            "Réactivité possible aux stimuli extérieurs",
            "Gestion de l'impulsivité parfois difficile"
        ),
        nuanceAnalyse = "Les races nordiques et primitives ont conservé une grande autonomie de pensée. Elles ont moins été sélectionnées pour obéir que pour prendre des décisions seules — ce qui peut se traduire par une réactivité à l'environnement et une difficulté à revenir au calme sur demande. Ces comportements sont souvent normaux pour ces races et demandent des approches spécifiques plutôt que des corrections classiques."
    ),

    CategorieRace(
        id = "levriers",
        nom = "Lévriers & Races de course",
        emoji = "💨",
        description = "Races rapides, sensibles et souvent calmes à la maison.",
        races = listOf(
            "Greyhound",
            "Whippet",
            "Lévrier Italien",
            "Saluki",
            "Borzoi",
            "Galgo Espagnol",
            "Afghan Hound",
            "Lévrier Irlandais"
        ),
        predispositions = listOf(
            "Sensibilité émotionnelle souvent élevée",
            "Réactivité aux mouvements rapides",
            "Besoin de sécurité et de prévisibilité",
            "Généralement calmes en intérieur"
        ),
        nuanceAnalyse = "Les lévriers sont des chiens très sensibles qui réagissent fortement aux stimuli visuels et aux mouvements rapides — c'est leur nature de chasseur à vue. Un score élevé en sensibilité ou en réactivité est donc fréquent et lié à leurs instincts profonds. Ils ont aussi besoin de beaucoup de sécurité affective, ce qui peut se traduire par un attachement plus marqué."
    ),

    CategorieRace(
        id = "nains",
        nom = "Races naines & compagnie",
        emoji = "🐩",
        description = "Races créées pour la compagnie, souvent très liées à leur famille.",
        races = listOf(
            "Chihuahua",
            "Bichon Frisé",
            "Maltais",
            "Carlin",
            "Caniche Toy",
            "Caniche Nain",
            "Spitz Nain (Poméranien)",
            "Shih Tzu",
            "Lhassa Apso",
            "Pékinois"
        ),
        predispositions = listOf(
            "Fort attachement à leur référent",
            "Sensibilité émotionnelle élevée",
            "Réactivité parfois sous-estimée",
            "Aboiements fréquents possibles"
        ),
        nuanceAnalyse = "Les races naines et de compagnie ont été sélectionnées pour vivre au plus près de l'humain — ce qui explique un besoin d'attachement souvent très fort. Leur petite taille conduit parfois à sous-estimer leur réactivité ou leurs difficultés comportementales. Un score élevé en attachement ou en sensibilité est très courant et mérite la même attention que chez les grandes races."
    ),

    CategorieRace(
        id = "chasse",
        nom = "Chiens de chasse & pisteurs",
        emoji = "🌿",
        description = "Races énergiques avec un flair et une motivation très développés.",
        races = listOf(
            "Beagle",
            "Basset Hound",
            "Braque Allemand",
            "Braque de Weimar",
            "Épagneul Breton",
            "Pointer",
            "Setter Irlandais",
            "Dalmatien",
            "Rhodesian Ridgeback",
            "Vizsla"
        ),
        predispositions = listOf(
            "Motivation et énergie très élevées",
            "Impulsivité fréquente à l'extérieur",
            "Gestion de l'excitation difficile",
            "Indépendance possible sur les pistes olfactives"
        ),
        nuanceAnalyse = "Les chiens de chasse ont une motivation et une énergie naturellement très élevées, ainsi qu'un instinct de pistage ou de poursuite très fort. Un score élevé en impulsivité ou en réactivité à l'extérieur est souvent la traduction directe de ces instincts. Ces comportements sont normaux dans leur contexte d'origine et demandent un travail adapté plutôt qu'une correction classique."
    ),

    CategorieRace(
        id = "croise",
        nom = "Croisé / Bâtard / Race inconnue",
        emoji = "🐕‍🦺",
        description = "Chiens de race mixte ou dont la race n'est pas connue.",
        races = listOf(
            "Croisé (race inconnue)",
            "Croisé identifié partiellement"
        ),
        predispositions = listOf(
            "Profil très variable selon l'héritage génétique",
            "Souvent bonne robustesse émotionnelle",
            "Les instincts dominants peuvent varier"
        ),
        nuanceAnalyse = "Les chiens croisés ont des profils très variés selon leurs origines. Sans connaissance précise de leur héritage génétique, il est difficile d'anticiper leurs prédispositions comportementales. Le bilan se base donc uniquement sur leurs comportements observés, ce qui reste la lecture la plus fiable et la plus juste de leur situation réelle."
    )
)

fun toutesLesRaces(): List<String> {
    return categoriesRaces.flatMap { it.races }.sorted()
}

fun getCategorieParRace(race: String): CategorieRace? {
    return categoriesRaces.firstOrNull { categorie ->
        categorie.races.any { it.equals(race, ignoreCase = true) }
    }
}

fun getNuanceAnalyse(race: String): String? {
    return getCategorieParRace(race)?.nuanceAnalyse
}

fun getPredispositions(race: String): List<String> {
    return getCategorieParRace(race)?.predispositions ?: emptyList()
}

fun getCategorieNom(race: String): String? {
    return getCategorieParRace(race)?.nom
}