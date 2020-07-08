package com.bryanalvarez.lealmessages.model

import java.io.Serializable

class User: Serializable {
    var id: String
    var name: String
    var photo : String
    var birthDay : String

    constructor(id: String, name: String, photo: String, birthDay: String){
        this.id = id
        this.name = name
        this.photo = photo
        this.birthDay = birthDay
    }
}