package com.bryanalvarez.lealmessages.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class Transaction: RealmObject,Serializable {
    @PrimaryKey
    var transactionId: Int = 0
    var commerce: Commerce? = Commerce()
    lateinit var date: String
    lateinit var branch: String
    lateinit var userId: String
    var read: Boolean = false

    constructor(){

    }

    constructor(id: Int, commerce: Commerce, date: String, userId: String, branch: String,read: Boolean){
        this.transactionId = id
        this.commerce = commerce
        this.date = date
        this.userId = userId
        this.branch = branch
        this.read = read
    }

}