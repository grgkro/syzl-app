package de.stuttgart.syzl3000.domain.util

interface EntityMapper <Entity, DomainModel> {

    fun mapFromEntity(entity: Entity): DomainModel
    fun mapToEntity(domainModel: DomainModel): Entity
}