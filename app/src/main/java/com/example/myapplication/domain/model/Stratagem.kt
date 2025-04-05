package com.example.myapplication.domain.model

data class Stratagem(
    val id: Int?,
    val codename: String,
    val name: String,
    val keys: List<String>,
    val uses: String,
    val cooldown: Int?,
    val activation: Int,
    val imageUrl: String,
    val groupId: Int?,
    val createdAt: String,
    val updatedAt: String
)