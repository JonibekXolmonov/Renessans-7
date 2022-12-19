package com.example.renessans7.models

data class TestResult(
    val name: String = "1. Xolmonov Jonibek Qahramon o'g'li",
    val cor: String = "28",
    val inc: String = "2",
    val results: List<Boolean> = mutableListOf(
        true,
        true,
        true,
        true,
        true,
        false,
        false,
        true,
        true,
        false,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        false,
        false,
        true,
        true,
        false,
        true,
        true,
        true
    )
)
