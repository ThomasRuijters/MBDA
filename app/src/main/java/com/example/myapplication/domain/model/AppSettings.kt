package com.example.myapplication.domain.model

class AppSettings(val darkMode: Boolean, val userName: String, val profilePictureUri: String) {
    companion object {
        val DEFAULT = AppSettings(false, "Helldiver", "default_profile_picture.png")
    }
}