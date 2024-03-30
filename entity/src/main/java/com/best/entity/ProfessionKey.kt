package com.best.entity

enum class ProfessionKey(val value: String, val valueRu: String) {
    Actor("ACTOR", "Актер"),
    Director("DIRECTOR", "Директор"),
    Himself("HIMSELF", "Сам себя"),
    HronoTitrMale("HRONO_TITR_MALE", "Озвучик"),
    Producer("PRODUCER", "Продюсер"),
    Writer("WRITER", "Писатель"),
    Operator("OPERATOR", "Оператор"),
    Editor("EDITOR", "Режисер"),
    Composer("COMPOSER", "Композиор"),
    ProducerUser("PRODUCER_USSR", "Продюсер"),
    HerSelf("HERSELF", "Себя"),
    HronoTitleFemale("HRONO_TITR_FEMALE", "Озвучица"),
    Translator("TRANSLATOR", "Переводчик"),
    Design("DESIGN", "Дезайнер"),
    VoiceDirector("VOICE_DIRECTOR", "Звукорежисер"),
    Unknown("UNKNOWN", "Неизвестно"),
    VoiceMale("VOICE_MALE", "Озвучик"),
    VoiceFemale("VOICE_FEMALE", "Озвучица");


    companion object {
        public fun fromValue(value: String): ProfessionKey = when (value) {
            "ACTOR" -> Actor
            "DIRECTOR" -> Director
            "HIMSELF" -> Himself
            "HRONO_TITR_MALE" -> HronoTitrMale
            "PRODUCER" -> Producer
            "WRITER" -> Writer
            "OPERATOR" -> Operator
            "EDITOR" -> Editor
            "COMPOSER" -> Composer
            "PRODUCER_USSR" -> ProducerUser
            "HERSELF" -> HerSelf
            "HRONO_TITR_FEMALE" -> HronoTitleFemale
            "TRANSLATOR" -> Translator
            "DESIGN" -> Design
            "VOICE_DIRECTOR" -> VoiceDirector
            "VOICE_MALE" -> VoiceMale
            "VOICE_FEMALE" -> VoiceFemale
            else -> Unknown
        }

        public fun getProfessionKeyfromValue(value: String): ProfessionKey =
            when (value) {
                "ACTOR" -> Actor
                "DIRECTOR" -> Director
                "HIMSELF" -> Himself
                "HRONO_TITR_MALE" -> HronoTitrMale
                "PRODUCER" -> Producer
                "WRITER" -> Writer
                "OPERATOR" -> Operator
                "EDITOR" -> Editor
                "COMPOSER" -> Composer
                "PRODUCER_USSR" -> ProducerUser
                "HERSELF" -> HerSelf
                "HRONO_TITR_FEMALE" -> HronoTitleFemale
                "TRANSLATOR" -> Translator
                "DESIGN" -> Design
                "VOICE_DIRECTOR" -> VoiceDirector
                "VOICE_MALE" -> VoiceMale
                "VOICE_FEMALE" -> VoiceFemale
                else -> Unknown
            }


    }

}