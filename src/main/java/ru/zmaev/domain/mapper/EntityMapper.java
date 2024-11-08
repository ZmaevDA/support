package ru.zmaev.domain.mapper;

public interface EntityMapper<E, R, D> {
    D toDto(E entity);
    E toEntity(R requestDto);
}
