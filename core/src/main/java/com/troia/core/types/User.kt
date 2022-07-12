package com.troia.core.types

import java.io.Serializable

class User(): Serializable {
    var name: String? = null
    var email: String? = null
    var admin: Boolean = false
    var pass: String? = null
}
