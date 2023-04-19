package com.htnguyen.ihealth.util

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = Firebase.auth.currentUser
    @SuppressLint("StaticFieldLeak")
    val db: FirebaseFirestore =  Firebase.firestore

    val database = Firebase.database
    val activityDaily = database.reference
    val video = database.reference
    val databaseReferenceVideo = database.getReference("video_study")
}