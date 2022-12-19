package com.example.renessans7.models.test

data class TestFileUploadRequest(
    val classId: String,
    val testName: String,
    val answers: List<String>,
    val numberOfQuestions: Int
)
