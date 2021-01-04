package de.stuttgart.syzl3000.repositories

import de.stuttgart.syzl3000.domain.model.User

interface UserRepository {

    suspend fun get(token: String, id: String): User

}