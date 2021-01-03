package de.stuttgart.syzl3000.network.model

import de.stuttgart.syzl3000.domain.model.Movie
import de.stuttgart.syzl3000.domain.model.User
import de.stuttgart.syzl3000.domain.util.EntityMapper

class UserNetworkMapper : EntityMapper<UserNetworkEntity, User> {

    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
                id = entity.id,
                gangs = entity.gangs,
                username = entity.username,
                likedRecipes = entity.likedRecipes,
                dislikedRecipes = entity.dislikedRecipes,
                likedMovies = entity.likedMovies,
                dislikedMovies = entity.dislikedMovies,
                email = entity.email,
                picture = entity.picture
        )
    }

    override fun mapToEntity(domainModel: User): UserNetworkEntity {
        return UserNetworkEntity(
                id = domainModel.id,
                gangs = domainModel.gangs,
                username = domainModel.username,
                likedRecipes = domainModel.likedRecipes,
                dislikedRecipes = domainModel.dislikedRecipes,
                likedMovies = domainModel.likedMovies,
                dislikedMovies = domainModel.dislikedMovies,
                email = domainModel.email,
                picture = domainModel.picture
        )
    }

    fun fromEntityList(initial: List<UserNetworkEntity>): List<User> {
        return initial.map { mapFromEntity(it) }
    }

    fun toEntityList(initial: List<User>): List<UserNetworkEntity> {
        return initial.map { mapToEntity(it) }
    }
}