package de.stuttgart.syzl3000.network.model

import de.stuttgart.syzl3000.domain.model.Movie
import de.stuttgart.syzl3000.domain.util.EntityMapper

class MovieNetworkMapper : EntityMapper<MovieNetworkEntity, Movie> {

    override fun mapFromEntity(entity: MovieNetworkEntity): Movie {
        return Movie(
                id = entity.id,
                rating = entity.rating,
                title = entity.title,
                shortTitle = entity.shortTitle,
                crew = entity.crew,
                genre = entity.genre,
                image = entity.image,
                isInTheaters = entity.isInTheaters,
                length = entity.length,
                plot = entity.plot,
                ratingCount = entity.ratingCount,
                year = entity.year
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieNetworkEntity {
        return MovieNetworkEntity(
                id = domainModel.id,
                rating = domainModel.rating,
                title = domainModel.title,
                shortTitle = domainModel.shortTitle,
                crew = domainModel.crew,
                genre = domainModel.genre,
                image = domainModel.image,
                isInTheaters = domainModel.isInTheaters,
                length = domainModel.length,
                plot = domainModel.plot,
                ratingCount = domainModel.ratingCount,
                year = domainModel.year
        )
    }

    fun fromEntityList(initial: List<MovieNetworkEntity>): List<Movie> {
        return initial.map { mapFromEntity(it)}
    }

    fun toEntityList(initial: List<Movie>): List<MovieNetworkEntity> {
        return initial.map { mapToEntity(it)}
    }
}