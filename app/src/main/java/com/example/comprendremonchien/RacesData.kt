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

val categoriesRaces get() = trList(categoriesRacesFr, categoriesRacesEn, categoriesRacesDe)

val categoriesRacesFr = listOf(

    CategorieRace(
        id = "bergers",
        nom = "Chiens de berger & troupeau",
        emoji = "🐕",
        description = "Races sélectionnées pour travailler en étroite collaboration avec l'humain.",
        races = listOf(
            "Border Collie", "Berger Allemand", "Berger Australien",
            "Berger Belge Malinois", "Berger Belge Tervueren", "Berger Blanc Suisse",
            "Berger de Brie (Briard)", "Berger des Pyrénées", "Bouvier des Flandres", "Colley"
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
            "Golden Retriever", "Labrador Retriever", "Cocker Spaniel Anglais",
            "Cocker Américain", "Springer Spaniel", "Flat Coated Retriever",
            "Nova Scotia Duck Tolling Retriever", "Cavalier King Charles"
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
            "Jack Russell Terrier", "West Highland White Terrier", "Yorkshire Terrier",
            "Bull Terrier", "Staffordshire Bull Terrier", "American Staffordshire Terrier",
            "Fox Terrier", "Airedale Terrier", "Border Terrier", "Cairn Terrier"
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
            "Dogue Allemand", "Rottweiler", "Boxer", "Bouledogue Français",
            "Bouledogue Anglais", "Cane Corso", "Dogue de Bordeaux",
            "Mastiff", "Boerboel", "Shar Pei"
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
            "Husky Sibérien", "Malamute d'Alaska", "Samoyède", "Spitz Allemand",
            "Akita Inu", "Shiba Inu", "Basenji", "Groenlandais", "Chow Chow"
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
            "Greyhound", "Whippet", "Lévrier Italien", "Saluki",
            "Borzoi", "Galgo Espagnol", "Afghan Hound", "Lévrier Irlandais"
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
            "Chihuahua", "Bichon Frisé", "Maltais", "Carlin",
            "Caniche Toy", "Caniche Nain", "Spitz Nain (Poméranien)",
            "Shih Tzu", "Lhassa Apso", "Pékinois"
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
            "Beagle", "Basset Hound", "Braque Allemand", "Braque de Weimar",
            "Épagneul Breton", "Pointer", "Setter Irlandais",
            "Dalmatien", "Rhodesian Ridgeback", "Vizsla"
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

val categoriesRacesEn = listOf(

    CategorieRace(
        id = "bergers",
        nom = "Herding & sheepdogs",
        emoji = "🐕",
        description = "Breeds selected to work in close collaboration with humans.",
        races = listOf(
            "Border Collie", "German Shepherd", "Australian Shepherd",
            "Belgian Malinois", "Belgian Tervueren", "White Swiss Shepherd",
            "Briard", "Pyrenean Shepherd", "Bouvier des Flandres", "Collie"
        ),
        predispositions = listOf(
            "Naturally high reactivity",
            "Frequent hypervigilance",
            "Strong need for mental stimulation",
            "Marked emotional sensitivity"
        ),
        nuanceAnalyse = "Herding dogs were selected to be constantly attentive to their environment and reactive to the slightest signals. A high score in reactivity or sensitivity may therefore partly reflect their fundamental nature, not just a difficulty to correct. This does not diminish the importance of working on these axes, but helps calibrate expectations and adapt methods."
    ),

    CategorieRace(
        id = "retrievers",
        nom = "Retrievers & Spaniels",
        emoji = "🦮",
        description = "Breeds known for their sociability and eagerness to please.",
        races = listOf(
            "Golden Retriever", "Labrador Retriever", "English Cocker Spaniel",
            "American Cocker Spaniel", "Springer Spaniel", "Flat Coated Retriever",
            "Nova Scotia Duck Tolling Retriever", "Cavalier King Charles"
        ),
        predispositions = listOf(
            "Strong need for attachment and contact",
            "Tendency towards frustration if under-stimulated",
            "Sometimes struggles to manage excitement",
            "Generally good sociability"
        ),
        nuanceAnalyse = "Retrievers and Spaniels are naturally very attached to their family and have a strong need for contact. A high attachment score is therefore very common in these breeds and does not necessarily indicate pathological separation anxiety. Impulsivity may also be present in play or excitement contexts, which is typical of their energetic profile."
    ),

    CategorieRace(
        id = "terriers",
        nom = "Terriers",
        emoji = "🐾",
        description = "Tenacious, independent breeds that are often highly reactive.",
        races = listOf(
            "Jack Russell Terrier", "West Highland White Terrier", "Yorkshire Terrier",
            "Bull Terrier", "Staffordshire Bull Terrier", "American Staffordshire Terrier",
            "Fox Terrier", "Airedale Terrier", "Border Terrier", "Cairn Terrier"
        ),
        predispositions = listOf(
            "Often marked reactivity",
            "Strong personality and independence",
            "Frequent impulsivity in play",
            "A tenacious streak"
        ),
        nuanceAnalyse = "Terriers were selected to hunt and confront prey often larger than themselves — which explains their strong temperament, reactivity and tendency not to give up. A high impulsivity or reactivity score is very common in these breeds. This does not mean the dog is \"difficult\", but that its profile calls for an approach adapted to its natural energy."
    ),

    CategorieRace(
        id = "molosses",
        nom = "Molossers & Mastiffs",
        emoji = "🦁",
        description = "Powerful breeds, often calm but with a strong presence.",
        races = listOf(
            "Great Dane", "Rottweiler", "Boxer", "French Bulldog",
            "English Bulldog", "Cane Corso", "Dogue de Bordeaux",
            "Mastiff", "Boerboel", "Shar Pei"
        ),
        predispositions = listOf(
            "Generally calm temperament",
            "Possible reactivity towards strangers",
            "Needs clear boundaries",
            "Emotional sensitivity sometimes underestimated"
        ),
        nuanceAnalyse = "Molossers are often perceived as strong and dominant dogs, but many are actually very emotionally sensitive. A high sensitivity score is not uncommon and deserves the same attention as for any other breed. Their size amplifies the impact of their behaviours, making training particularly important even when problems seem \"minor\"."
    ),

    CategorieRace(
        id = "nordiques",
        nom = "Nordic & primitive dogs",
        emoji = "🐺",
        description = "Breeds close to their original instincts, often independent.",
        races = listOf(
            "Siberian Husky", "Alaskan Malamute", "Samoyed", "German Spitz",
            "Akita Inu", "Shiba Inu", "Basenji", "Greenland Dog", "Chow Chow"
        ),
        predispositions = listOf(
            "Marked independence",
            "Low need to please humans",
            "Possible reactivity to external stimuli",
            "Impulse control sometimes difficult"
        ),
        nuanceAnalyse = "Nordic and primitive breeds have retained a great deal of independent thinking. They were selected less to obey than to make decisions on their own — which can translate into environmental reactivity and difficulty returning to calm on request. These behaviours are often normal for these breeds and call for specific approaches rather than traditional corrections."
    ),

    CategorieRace(
        id = "levriers",
        nom = "Sighthounds & racing dogs",
        emoji = "💨",
        description = "Fast, sensitive breeds that are often calm indoors.",
        races = listOf(
            "Greyhound", "Whippet", "Italian Greyhound", "Saluki",
            "Borzoi", "Spanish Galgo", "Afghan Hound", "Irish Wolfhound"
        ),
        predispositions = listOf(
            "Often high emotional sensitivity",
            "Reactivity to fast movements",
            "Need for security and predictability",
            "Generally calm indoors"
        ),
        nuanceAnalyse = "Sighthounds are very sensitive dogs that react strongly to visual stimuli and fast movements — this is their sight-hunting nature. A high sensitivity or reactivity score is therefore common and linked to their deep-rooted instincts. They also need a great deal of emotional security, which can translate into more marked attachment."
    ),

    CategorieRace(
        id = "nains",
        nom = "Toy & companion breeds",
        emoji = "🐩",
        description = "Breeds created for companionship, often very close to their family.",
        races = listOf(
            "Chihuahua", "Bichon Frisé", "Maltese", "Pug",
            "Toy Poodle", "Miniature Poodle", "Pomeranian",
            "Shih Tzu", "Lhasa Apso", "Pekingese"
        ),
        predispositions = listOf(
            "Strong attachment to their main person",
            "High emotional sensitivity",
            "Reactivity sometimes underestimated",
            "Frequent barking possible"
        ),
        nuanceAnalyse = "Toy and companion breeds were selected to live as close as possible to humans — which explains an often very strong need for attachment. Their small size sometimes leads to underestimating their reactivity or behavioural difficulties. A high attachment or sensitivity score is very common and deserves the same attention as in larger breeds."
    ),

    CategorieRace(
        id = "chasse",
        nom = "Hunting & tracking dogs",
        emoji = "🌿",
        description = "Energetic breeds with a highly developed nose and drive.",
        races = listOf(
            "Beagle", "Basset Hound", "German Shorthaired Pointer", "Weimaraner",
            "Brittany Spaniel", "Pointer", "Irish Setter",
            "Dalmatian", "Rhodesian Ridgeback", "Vizsla"
        ),
        predispositions = listOf(
            "Very high motivation and energy",
            "Frequent impulsivity outdoors",
            "Struggles to manage excitement",
            "Possible independence when following scent trails"
        ),
        nuanceAnalyse = "Hunting dogs have naturally very high motivation and energy, as well as a very strong tracking or chasing instinct. A high impulsivity or reactivity score outdoors is often the direct expression of these instincts. These behaviours are normal in their original context and call for tailored work rather than traditional correction."
    ),

    CategorieRace(
        id = "croise",
        nom = "Mixed breed / unknown breed",
        emoji = "🐕‍🦺",
        description = "Mixed breed dogs or dogs whose breed is unknown.",
        races = listOf(
            "Mixed breed (unknown)",
            "Partially identified mixed breed"
        ),
        predispositions = listOf(
            "Profile highly variable depending on genetic heritage",
            "Often good emotional resilience",
            "Dominant instincts may vary"
        ),
        nuanceAnalyse = "Mixed breed dogs have very varied profiles depending on their origins. Without precise knowledge of their genetic heritage, it is difficult to anticipate their behavioural predispositions. The assessment is therefore based solely on observed behaviours, which remains the most reliable and accurate reading of their actual situation."
    )
)

val categoriesRacesDe = listOf(

    CategorieRace(
        id = "bergers",
        nom = "Hüte- und Hirtenhunde",
        emoji = "🐕",
        description = "Rassen, die für die enge Zusammenarbeit mit dem Menschen gezüchtet wurden.",
        races = listOf(
            "Border Collie", "Deutscher Schäferhund", "Australian Shepherd",
            "Belgischer Schäferhund Malinois", "Belgischer Schäferhund Tervueren", "Weißer Schweizer Schäferhund",
            "Briard", "Pyrenäen-Schäferhund", "Bouvier des Flandres", "Collie"
        ),
        predispositions = listOf(
            "Von Natur aus hohe Reaktivität",
            "Häufig übersteigerte Wachsamkeit",
            "Großes Bedürfnis nach geistiger Auslastung",
            "Ausgeprägte emotionale Sensibilität"
        ),
        nuanceAnalyse = "Hütehunde wurden darauf gezüchtet, ständig aufmerksam gegenüber ihrer Umgebung zu sein und auf kleinste Signale zu reagieren. Ein hoher Wert bei Reaktivität oder Sensibilität kann daher teilweise ihr grundlegendes Wesen widerspiegeln und nicht nur eine Schwierigkeit, die korrigiert werden muss. Das mindert nicht die Bedeutung, an diesen Achsen zu arbeiten, hilft aber, die Erwartungen einzuordnen und die Methoden anzupassen."
    ),

    CategorieRace(
        id = "retrievers",
        nom = "Retriever & Spaniel",
        emoji = "🦮",
        description = "Rassen, die für ihre Geselligkeit und ihren Wunsch zu gefallen bekannt sind.",
        races = listOf(
            "Golden Retriever", "Labrador Retriever", "English Cocker Spaniel",
            "American Cocker Spaniel", "Springer Spaniel", "Flat Coated Retriever",
            "Nova Scotia Duck Tolling Retriever", "Cavalier King Charles Spaniel"
        ),
        predispositions = listOf(
            "Großes Bedürfnis nach Bindung und Kontakt",
            "Neigung zu Frust bei zu wenig Auslastung",
            "Umgang mit Erregung manchmal schwierig",
            "Meist gute Verträglichkeit"
        ),
        nuanceAnalyse = "Retriever und Spaniel sind von Natur aus sehr an ihre Familie gebunden und haben ein großes Bedürfnis nach Kontakt. Ein hoher Wert bei der Bindung ist bei diesen Rassen daher sehr häufig und bedeutet nicht unbedingt eine krankhafte Trennungsangst. Auch Impulsivität kann beim Spielen oder in aufregenden Momenten auftreten, was typisch für ihr energiegeladenes Profil ist."
    ),

    CategorieRace(
        id = "terriers",
        nom = "Terrier",
        emoji = "🐾",
        description = "Hartnäckige, unabhängige und oft sehr reaktive Rassen.",
        races = listOf(
            "Jack Russell Terrier", "West Highland White Terrier", "Yorkshire Terrier",
            "Bullterrier", "Staffordshire Bullterrier", "American Staffordshire Terrier",
            "Foxterrier", "Airedale Terrier", "Border Terrier", "Cairn Terrier"
        ),
        predispositions = listOf(
            "Oft ausgeprägte Reaktivität",
            "Starke Persönlichkeit und Unabhängigkeit",
            "Häufige Impulsivität beim Spielen",
            "Neigung zur Hartnäckigkeit"
        ),
        nuanceAnalyse = "Terrier wurden gezüchtet, um zu jagen und sich Beutetieren zu stellen, die oft größer sind als sie selbst – das erklärt ihr starkes Temperament, ihre Reaktivität und ihre Neigung, nicht loszulassen. Ein hoher Wert bei Impulsivität oder Reaktivität ist bei diesen Rassen sehr häufig. Das bedeutet nicht, dass der Hund „schwierig“ ist, sondern dass sein Profil einen Ansatz braucht, der zu seiner natürlichen Energie passt."
    ),

    CategorieRace(
        id = "molosses",
        nom = "Molosser & Doggen",
        emoji = "🦁",
        description = "Kraftvolle Rassen, oft ruhig, aber mit starker Präsenz.",
        races = listOf(
            "Deutsche Dogge", "Rottweiler", "Boxer", "Französische Bulldogge",
            "Englische Bulldogge", "Cane Corso", "Bordeauxdogge",
            "Mastiff", "Boerboel", "Shar Pei"
        ),
        predispositions = listOf(
            "Meist ausgeglichenes Temperament",
            "Mögliche Reaktivität gegenüber Fremden",
            "Bedürfnis nach einem klaren Rahmen",
            "Emotionale Sensibilität, die manchmal unterschätzt wird"
        ),
        nuanceAnalyse = "Molosser werden oft als starke und dominante Hunde wahrgenommen, doch viele sind in Wirklichkeit emotional sehr sensibel. Ein hoher Wert bei der Sensibilität ist nicht selten und verdient dieselbe Aufmerksamkeit wie bei jeder anderen Rasse. Ihre Größe verstärkt die Wirkung ihres Verhaltens, weshalb die Erziehungsarbeit besonders wichtig ist, selbst wenn die Probleme „klein“ erscheinen."
    ),

    CategorieRace(
        id = "nordiques",
        nom = "Nordische & ursprüngliche Hunde",
        emoji = "🐺",
        description = "Rassen, die ihren ursprünglichen Instinkten nahe sind, oft unabhängig.",
        races = listOf(
            "Siberian Husky", "Alaskan Malamute", "Samojede", "Deutscher Spitz",
            "Akita Inu", "Shiba Inu", "Basenji", "Grönlandhund", "Chow-Chow"
        ),
        predispositions = listOf(
            "Ausgeprägte Unabhängigkeit",
            "Geringes Bedürfnis, dem Menschen zu gefallen",
            "Mögliche Reaktivität auf äußere Reize",
            "Umgang mit Impulsivität manchmal schwierig"
        ),
        nuanceAnalyse = "Nordische und ursprüngliche Rassen haben sich eine große Eigenständigkeit im Denken bewahrt. Sie wurden weniger auf Gehorsam als auf eigenständige Entscheidungen gezüchtet – was sich in Reaktivität auf die Umgebung und in Schwierigkeiten äußern kann, auf Aufforderung zur Ruhe zu kommen. Diese Verhaltensweisen sind bei diesen Rassen oft normal und erfordern besondere Ansätze statt klassischer Korrekturen."
    ),

    CategorieRace(
        id = "levriers",
        nom = "Windhunde & Rennhunde",
        emoji = "💨",
        description = "Schnelle, sensible Rassen, die zu Hause oft ruhig sind.",
        races = listOf(
            "Greyhound", "Whippet", "Italienisches Windspiel", "Saluki",
            "Barsoi", "Galgo Español", "Afghanischer Windhund", "Irischer Wolfshund"
        ),
        predispositions = listOf(
            "Oft hohe emotionale Sensibilität",
            "Reaktivität auf schnelle Bewegungen",
            "Bedürfnis nach Sicherheit und Vorhersehbarkeit",
            "Im Haus meist ruhig"
        ),
        nuanceAnalyse = "Windhunde sind sehr sensible Hunde, die stark auf visuelle Reize und schnelle Bewegungen reagieren – das liegt in ihrer Natur als Sichtjäger. Ein hoher Wert bei Sensibilität oder Reaktivität ist daher häufig und mit ihren tief verwurzelten Instinkten verbunden. Sie brauchen außerdem viel emotionale Sicherheit, was sich in einer ausgeprägteren Bindung zeigen kann."
    ),

    CategorieRace(
        id = "nains",
        nom = "Klein- & Gesellschaftshunde",
        emoji = "🐩",
        description = "Rassen, die als Begleiter gezüchtet wurden und oft sehr an ihrer Familie hängen.",
        races = listOf(
            "Chihuahua", "Bichon Frisé", "Malteser", "Mops",
            "Toypudel", "Zwergpudel", "Zwergspitz (Pomeranian)",
            "Shih Tzu", "Lhasa Apso", "Pekinese"
        ),
        predispositions = listOf(
            "Starke Bindung an ihre Bezugsperson",
            "Hohe emotionale Sensibilität",
            "Reaktivität, die manchmal unterschätzt wird",
            "Häufiges Bellen möglich"
        ),
        nuanceAnalyse = "Klein- und Gesellschaftshunde wurden gezüchtet, um ganz nah am Menschen zu leben – das erklärt ein oft sehr starkes Bindungsbedürfnis. Wegen ihrer geringen Größe werden ihre Reaktivität oder ihre Verhaltensschwierigkeiten manchmal unterschätzt. Ein hoher Wert bei Bindung oder Sensibilität ist sehr häufig und verdient dieselbe Aufmerksamkeit wie bei großen Rassen."
    ),

    CategorieRace(
        id = "chasse",
        nom = "Jagd- & Spürhunde",
        emoji = "🌿",
        description = "Energiegeladene Rassen mit sehr ausgeprägter Nase und Motivation.",
        races = listOf(
            "Beagle", "Basset Hound", "Deutsch Kurzhaar", "Weimaraner",
            "Epagneul Breton", "Pointer", "Irish Red Setter",
            "Dalmatiner", "Rhodesian Ridgeback", "Magyar Vizsla"
        ),
        predispositions = listOf(
            "Sehr hohe Motivation und Energie",
            "Häufige Impulsivität draußen",
            "Schwieriger Umgang mit Erregung",
            "Mögliche Unabhängigkeit auf Geruchsspuren"
        ),
        nuanceAnalyse = "Jagdhunde haben von Natur aus eine sehr hohe Motivation und Energie sowie einen sehr starken Spür- oder Hetztrieb. Ein hoher Wert bei Impulsivität oder Reaktivität draußen ist oft der direkte Ausdruck dieser Instinkte. Diese Verhaltensweisen sind in ihrem ursprünglichen Kontext normal und erfordern eine angepasste Arbeit statt einer klassischen Korrektur."
    ),

    CategorieRace(
        id = "croise",
        nom = "Mischling / Rasse unbekannt",
        emoji = "🐕‍🦺",
        description = "Mischlingshunde oder Hunde, deren Rasse nicht bekannt ist.",
        races = listOf(
            "Mischling (Rasse unbekannt)",
            "Mischling (teilweise bekannt)"
        ),
        predispositions = listOf(
            "Je nach genetischem Erbe sehr unterschiedliches Profil",
            "Oft gute emotionale Belastbarkeit",
            "Die vorherrschenden Instinkte können variieren"
        ),
        nuanceAnalyse = "Mischlinge haben je nach Herkunft sehr unterschiedliche Profile. Ohne genaue Kenntnis ihres genetischen Erbes lassen sich ihre Verhaltensveranlagungen schwer vorhersagen. Die Einschätzung stützt sich daher allein auf das beobachtete Verhalten, was die zuverlässigste und treffendste Deutung ihrer tatsächlichen Situation bleibt."
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