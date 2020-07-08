package com.bryanalvarez.lealmessages.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Commerce: Serializable, RealmObject {

    lateinit var id: String
    lateinit var name: String

    constructor(){

    }

    constructor(id: String, name:String){
        this.id = id
        this.name = name
    }
}