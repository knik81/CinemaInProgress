package com.example.testcinema.entity

sealed class State {
    data object Loading : State()
    data object Finish : State()
}