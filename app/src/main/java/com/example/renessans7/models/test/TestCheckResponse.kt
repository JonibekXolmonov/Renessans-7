package com.example.renessans7.models.test

data class TestCheckResponse(
    val pupilId: String,
    val resultId: String,
    val fullName: String,
    val trueAnswers: Int,
    val wrongAnswers: Int,
    val unanswered: Int,
    val pupilAnswers: List<String>,
    val result: List<Boolean?>,
    val resultDate: String
)
