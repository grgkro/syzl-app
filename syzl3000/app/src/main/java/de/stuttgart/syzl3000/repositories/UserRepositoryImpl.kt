package de.stuttgart.syzl3000.repositories

import de.stuttgart.syzl3000.domain.model.User
import de.stuttgart.syzl3000.domain.util.DomainMapper
import de.stuttgart.syzl3000.network.UserService
import de.stuttgart.syzl3000.network.model.UserDto

class UserRepositoryImpl(
        private val userService: UserService,
        private val mapper: DomainMapper<UserDto, User>
): UserRepository {

    override suspend fun get(token: String, id: String): User {
        return mapper.mapToDomainModel(userService.get(token, id).user)
    }

}