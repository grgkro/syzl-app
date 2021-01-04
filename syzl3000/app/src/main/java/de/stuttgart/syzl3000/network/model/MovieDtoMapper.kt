package de.stuttgart.syzl3000.network.model

import de.stuttgart.syzl3000.domain.model.Movie
import de.stuttgart.syzl3000.domain.util.DomainMapper

class MovieDtoMapper : DomainMapper<MovieDto, Movie> {

    override fun mapToDomainModel(model: MovieDto): Movie {
        return Movie(
                id = model.id,
                rating = model.rating,
                title = model.title,
                shortTitle = model.shortTitle,
                crew = model.crew,
                genre = model.genre,
                image = model.image,
                isInTheaters = model.isInTheaters,
                length = model.length,
                plot = model.plot,
                ratingCount = model.ratingCount,
                year = model.year
        )
    }

    override fun mapFromDomainModel(domainModel: Movie): MovieDto {
        return MovieDto(
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

    fun fromEntityList(initial: List<MovieDto>): List<Movie> {
        return initial.map { mapToDomainModel(it)}
    }

    fun toEntityList(initial: List<Movie>): List<MovieDto> {
        return initial.map { mapFromDomainModel(it)}
    }
}