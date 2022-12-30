package com.example.renessans7.models.pupils

data class Class(
    val classId: String,
    val name: String,
    val description: String,
    val pupils: List<Pupil>
)

data class Pupil(
    val pupilId: String,
    val fullName: String,
    val isEnabled:Boolean
)
