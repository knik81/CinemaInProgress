package com.best.entity

enum class ApiParameters(val type: String, val label: String) {
    TOP250("TOP_250_MOVIES", "Топ 250"),
    PREMIERS("PREMIERS", "Премьеры"),
    SERIES("TV_SERIES", "Сериалы"),
    SEASONS("seasons", "Сезоны"),
    POPULAR("TOP_POPULAR_ALL", "Популярное"),
    RANDOM_FILMS("RANDOM_FILMS", "Сдучайный фильм"),
    FILTERS("FILTERS", "Страны и жанры"),
    IMAGES("IMAGES", "Галерея"),
    SIMILARS("SIMILARS", "Похожие фильмы"),
    ACTOR("ACTOR", "В фильме/сериале снимались"),
    STAFF("STAFF", "Над фильмом работали"),
    FILM("FILM", "Описание фильма"),
    PERSON("PERSON", "Персона"),
    PERSON_FILMS("PERSON_FILMS", "Фильмография"),
    SEARCH("SEARCH", "поиск");


    companion object {
        fun getFilmTypesByLabel(label: String): ApiParameters {
            return when (label) {
                TOP250.label -> TOP250
                PREMIERS.label -> PREMIERS
                SERIES.label -> SERIES
                SEASONS.label -> SEASONS
                POPULAR.label -> POPULAR
                FILTERS.label -> FILTERS
                IMAGES.label -> IMAGES
                SIMILARS.label -> SIMILARS
                ACTOR.label -> ACTOR
                STAFF.label -> STAFF
                FILM.label -> FILM
                PERSON.label -> PERSON
                PERSON_FILMS.label -> PERSON_FILMS
                SEARCH.label -> SEARCH
                else -> {
                    RANDOM_FILMS
                }
            }
        }

        fun getFilmTypesByType(type: String): ApiParameters {
            return when (type) {
                TOP250.type -> TOP250
                PREMIERS.type -> PREMIERS
                SERIES.type -> SERIES
                SEASONS.type -> SEASONS
                POPULAR.type -> POPULAR
                FILTERS.type -> FILTERS
                IMAGES.type -> IMAGES
                SIMILARS.type -> SIMILARS
                ACTOR.type -> ACTOR
                STAFF.type -> STAFF
                FILM.type -> FILM
                PERSON.type -> PERSON
                PERSON_FILMS.type -> PERSON_FILMS
                SEARCH.type -> SEARCH
                else -> {
                    RANDOM_FILMS
                }
            }
        }

        fun getImagesTypeParameters() = mapOf(
            "STILL" to "Со съёмок",
            "SHOOTING" to "Кадры из фильма",
            "POSTER" to "Постеры",
            "FAN_ART" to "Фанаты",
            "PROMO" to "Промо",
            "CONCEPT" to "Концэпт",
            "WALLPAPER" to "Обои",
            "COVER" to "Кавер",
            "SCREENSHOT" to "Скриншоты"
        )
    }
}

object SEARCH{
    enum class ORDER(val label: String){
        RATING("Рейтинг"),
        NUM_VOTE("Популярность"),
        YEAR("Дата")
    }
    enum class TYPE(val label: String){
        FILM("Фильмы"),
        TV_SHOW("ТВ шоу"),
        TV_SERIES("Сериалы"),
        MINI_SERIES("Мини Сериалы"),
        ALL("Все")
    }
}