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

//{
//    "id": 1,
//    "codename": "E/MG-101",
//    "name": "HMG Emplacement",
//    "keys": [
//    "down",
//    "up",
//    "left",
//    "right",
//    "right",
//    "left"
//    ],
//    "uses": "Unlimited",
//    "cooldown": 180,
//    "activation": 3,
//    "imageUrl": "/1/1.svg",
//    "groupId": 1,
//    "createdAt": "2024-03-19T13:05:55.350Z",
//    "updatedAt": "2024-03-19T13:05:55.350Z"
//},