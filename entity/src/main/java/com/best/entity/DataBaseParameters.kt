package com.best.entity

enum class DataBaseParameters(val label: String) {
    LOVE("Любимые"),
    ALREADYSAW("Уже видел"),
    WANTTOSEE("Хочу посмотреть");

    fun isExist(name: String): Boolean{
        when (name){
            LOVE.label -> return true
            ALREADYSAW.label -> return true
            WANTTOSEE.label -> return true
        }
        return false
    }
}