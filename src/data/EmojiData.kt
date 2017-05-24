package data

val EMOJI_DATA =
        mapOf(
                "people" to PeopleCategory.DATA.filter { !it.ignore },
                "nature" to NatureCategory.DATA.filter { !it.ignore },
                "food" to FoodsCategory.DATA.filter { !it.ignore },
                "activity" to ActivityCategory.DATA.filter { !it.ignore },
                "place" to PlacesCategory.DATA.filter { !it.ignore },
                "object" to ObjectsCategory.DATA.filter { !it.ignore },
                "symbol" to SymbolsCategory.DATA.filter { !it.ignore }
//                "flag" to FlagsCategory.DATA.filter { !it.ignore }
        )
