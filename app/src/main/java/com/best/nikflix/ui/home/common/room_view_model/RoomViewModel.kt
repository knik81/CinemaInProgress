package com.best.nikflix.ui.home.common.room_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.best.entity.FilmAndCollectionF
import com.best.entity.RoomUseCaseInterface
import com.best.entity.CollectionParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject

class RoomViewModel @Inject constructor(
    private val roomUseCase: RoomUseCaseInterface
) : ViewModel() {

    private val _filmAndCollectionMapSateFlow = MutableStateFlow<Map<String, Int>?>(null)
    val filmAndCollectionMapSateFlow = _filmAndCollectionMapSateFlow.asStateFlow()

    private val filmAndCollectionList1 = mutableListOf<FilmAndCollectionF>()
    val filmAndCollectionListStateFlow = MutableStateFlow<List<FilmAndCollectionF>?>(null)

    private val _specialListStateflow = MutableStateFlow<List<FilmAndCollectionF>?>(null)
    val specialListStateflow = _specialListStateflow.asStateFlow()

    fun test() = roomUseCase.getAllFilmAndCollectionF()

    //загрузить все фильмы и коллекции из БД
    fun getAllFilmAndCollectionF() {
        viewModelScope.launch {
            //Log.d("Nik", "launch")
            val filmAndCollectionMap = mutableMapOf<String, Int>()
            val specialListStateflow = mutableListOf<FilmAndCollectionF>()

            roomUseCase.getAllFilmAndCollectionF().collect { filmAndCollectionList ->
                if (!filmAndCollectionList.isNullOrEmpty()) {
                    filmAndCollectionList1.clear()
                    filmAndCollectionList1.addAll(filmAndCollectionList)
                    filmAndCollectionListStateFlow.value = filmAndCollectionList

                    filmAndCollectionList.forEach { filmAndCollection ->
                        //рассчитать количество каждой коллекции для квадратных иконов
                        val qty: Int = filmAndCollectionMap[filmAndCollection.collection] ?: 0
                        val increment: Int =
                            if (filmAndCollection.filmId != "" && filmAndCollection.filmId != "0") 1 else 0
                        filmAndCollectionMap[filmAndCollection.collection] = qty + increment

                        //заполнить список с коллекцией Уже видел и  Интересно
                        if (filmAndCollection.collection == CollectionParameters.ALREADYSAW.label
                            || filmAndCollection.collection == CollectionParameters.INTERESTING.label
                        )
                            specialListStateflow.add(filmAndCollection)
                    }
                }
                //Log.d("Nik", "dfs $dfs")
                _filmAndCollectionMapSateFlow.value = filmAndCollectionMap
                _specialListStateflow.value = specialListStateflow.distinct()
            }

        }
    }

    //удаление всех записей по коллекции
    fun deleteCollections(collection: String) {
        //создать список всех записей в БД с этой коллекцией
        val filmAndCollectionList = filmAndCollectionList1.filterList {
            this.collection == collection
        }
        //удалить из БД список
        viewModelScope.launch(Dispatchers.IO) {
            roomUseCase.deleteFilmAndCollectionF(filmAndCollectionList)
        }
    }


    private val _existLikeStateFlow = MutableStateFlow(false)
    val existLikeStateFlow = _existLikeStateFlow.asStateFlow()

    private val _existWantToSeeStateFlow = MutableStateFlow(false)
    val existWantToSeeStateFlow = _existWantToSeeStateFlow.asStateFlow()

    private val _existAlreadySawStateFlow = MutableStateFlow(false)
    val existAlreadySawStateFlow = _existAlreadySawStateFlow.asStateFlow()


    //переключатель
    fun switch(filmAndCollectionF: FilmAndCollectionF) {
        val isExist =
            roomUseCase.existFilmAndCollectionF(filmAndCollectionF = filmAndCollectionF)
        if (isExist)
            roomUseCase.deleteFilmAndCollectionF(listOf(filmAndCollectionF))
        else
            roomUseCase.insertFilmAndCollectionF(filmAndCollectionF)

        //обновление кнопок
        when (filmAndCollectionF.collection) {
            CollectionParameters.LIKE.label ->
                _existLikeStateFlow.value = !isExist

            CollectionParameters.WANTTOSEE.label ->
                _existWantToSeeStateFlow.value = !isExist

            CollectionParameters.ALREADYSAW.label ->
                _existAlreadySawStateFlow.value = !isExist
        }
    }

    //загрузка данных кнопок
    fun getButtonsInfo(filId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            // Log.d("Nik", "filId = $filId")
            _existLikeStateFlow.value =
                roomUseCase.existFilmAndCollectionF(
                    filmAndCollectionF = FilmAndCollectionF(
                        filmId = filId.toString(),
                        collection = CollectionParameters.LIKE.label
                    )
                )

            _existWantToSeeStateFlow.value =
                roomUseCase.existFilmAndCollectionF(
                    filmAndCollectionF = FilmAndCollectionF(
                        filmId = filId.toString(),
                        collection = CollectionParameters.WANTTOSEE.label
                    )
                )

            _existAlreadySawStateFlow.value =
                roomUseCase.existFilmAndCollectionF(
                    filmAndCollectionF = FilmAndCollectionF(
                        filmId = filId.toString(),
                        collection = CollectionParameters.ALREADYSAW.label
                    )
                )
        }
    }

    fun saveInteresting(idFilm: Int) {
        val interesting = FilmAndCollectionF(
            filmId = idFilm.toString(),
            collection = CollectionParameters.INTERESTING.label
        )

        viewModelScope.launch(Dispatchers.IO) {
            //в коллекции не дожно быть больше 20 фильмов
            // перед добавлением проверка списка и при необходимости удлаение последнего
            roomUseCase.getFilmAndCollectionFByCollection(CollectionParameters.INTERESTING.label)
                .collect {
                    if (!it.isNullOrEmpty())
                        if (it.size > 20) {
                            roomUseCase.deleteFilmAndCollectionF(listOf(it.first()))
                        }
                    //Log.d("Nik", it?.first().toString())
                    if (!roomUseCase.existFilmAndCollectionF(interesting)) {
                        roomUseCase.insertFilmAndCollectionF(interesting)
                    }
                }
        }
    }

    //удаления записи из БД

    //добавления коллекцию в БД
    fun insertFilmAndCollection(filmAndCollection: FilmAndCollectionF) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!roomUseCase.existFilmAndCollectionF(filmAndCollection)) {
                roomUseCase.insertFilmAndCollectionF(filmAndCollection)
            }
        }
    }


    //получить соллекции без кнопок
    private val _filmAndCollectionListStateFlow = MutableStateFlow<List<FilmAndCollectionF>?>(null)
    val collectionsListStateFlow = _filmAndCollectionListStateFlow.asStateFlow()

    fun getCollections(filmId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomUseCase.getAllFilmAndCollectionF().collect { getFilmAndCollectionList ->
                // Log.d("Nik", "ViewModel $getFilmAndCollectionList")

                //для временного хранения списка с затертым filmId
                val filmAndCollectionList = mutableListOf<FilmAndCollectionF>()
                //для временного хранения коллекций данного фильма
                val collectionList = mutableListOf<String>()
                if (!getFilmAndCollectionList.isNullOrEmpty()) {
                    val filmAndCollectionListNotButtons = getFilmAndCollectionList.filterList {
                        this.collection != CollectionParameters.LIKE.label
                                && this.collection != CollectionParameters.WANTTOSEE.label
                                && this.collection != CollectionParameters.ALREADYSAW.label
                                && this.collection != CollectionParameters.INTERESTING.label
                    }
                    //обнулить ид фильма, если фильм не filmId
                    filmAndCollectionListNotButtons.forEach { filmAndCollection ->
                        if (filmAndCollection.filmId != filmId)
                            filmAndCollectionList.add(
                                FilmAndCollectionF(
                                    collection = filmAndCollection.collection,
                                    filmId = ""
                                )
                            )
                        else {
                            filmAndCollectionList.add(filmAndCollection)
                            collectionList.add(filmAndCollection.collection)
                        }
                    }
                }

                //удаление дублей,
                // фильтр - взять запись только с уникальной коллекцией
                _filmAndCollectionListStateFlow.value = filmAndCollectionList
                    .distinct()
                    .filterList {
                        ((this.collection !in collectionList && this.filmId == "") || this.filmId != "")
                    }
            }

        }

    }

}