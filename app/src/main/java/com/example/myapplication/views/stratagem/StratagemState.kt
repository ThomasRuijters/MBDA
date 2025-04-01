package com.example.myapplication.views.stratagem


data class StratagemState(
    val id: Int? = null,
    val name: String = "",
    val codename: String = "",
    val keys: List<String> = emptyList(),
    val uses: String = "",
    val cooldown: Int? = 0,
    val activation: Int = 0,
    val imageUrl: String = "",
    val groupId: Int? = 0,
    val createdAt: String = "",
    val updatedAt: String = ""
)