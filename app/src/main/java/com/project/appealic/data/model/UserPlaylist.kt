package com.project.appealic.data.model

class UserPlaylist  {
    var name: String? = null
    var image: String? = null
    var uid: String? = null
    var dateCreated: String? = null

    constructor() : super()
    constructor(id: String?, name: String?, image: String?, uid: String?, dateCreated: String?) : super() {
        this.name = name
        this.image = image
        this.uid = uid
        this.dateCreated = dateCreated
    }

    fun update(name: String?, image: String?, uid: String?, dateCreated: String?) {
        this.name = name
        this.image = image
        this.uid = uid
        this.dateCreated = dateCreated
    }

    override fun toString(): String {
        return "UserPlaylist{" +
                ", dateCreated='" + dateCreated + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", image='" + image + '\'' +
                '}'
    }
}
