package com.best.nikflix.ui.search.entity

import com.best.entity.Country
import com.best.entity.Genre
import com.best.entity.QueryParamsInterface
import com.best.entity.SEARCH

object QueryParamsObject : QueryParamsInterface {
    override var genres: Genre = Genre()
    override var countries: Country = Country()
    override var order: String = SEARCH.ORDER.RATING.name
    override var type: String = SEARCH.TYPE.ALL.name
    override var ratingFrom: Int = 0
    override var ratingTo: Int = 10
    override var yearFrom: Int = 1990
    override var yearTo: Int = 2024
    override var imdbId: String = ""
    override var keyword: String = ""
}