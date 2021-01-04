package de.stuttgart.syzl3000.network.model

import de.stuttgart.syzl3000.domain.model.User
import de.stuttgart.syzl3000.domain.util.DomainMapper

class UserDtoMapper : DomainMapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User {
        return User(
                id = model.id,
                gangs = model.gangs,
                username = model.username,
                likedRecipes = model.likedRecipes,
                dislikedRecipes = model.dislikedRecipes,
                likedMovies = model.likedMovies,
                dislikedMovies = model.dislikedMovies,
                email = model.email,
                picture = model.picture
        )
    }

    override fun mapFromDomainModel(domainModel: User): UserDto {
        return UserDto(
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

    fun toDomainList(initial: List<UserDto>): List<User> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<User>): List<UserDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}