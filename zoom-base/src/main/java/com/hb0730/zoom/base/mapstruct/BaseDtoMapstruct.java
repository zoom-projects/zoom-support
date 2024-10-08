package com.hb0730.zoom.base.mapstruct;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/8
 */
public interface BaseDtoMapstruct<D, E> extends Mapstruct<E> {
    /**
     * dto转entity
     *
     * @param dto dto
     * @return entity
     */
    E dtoToEntity(D dto);

    /**
     * entity转dto
     *
     * @param entity entity
     * @return dto
     */
    D entityToDto(E entity);

    /**
     * dtoList转entityList
     *
     * @param dtoList dtoList
     * @return entityList
     */
    List<E> dtoListToEntityList(List<D> dtoList);

    /**
     * entityList转dtoList
     *
     * @param entityList entityList
     * @return dtoList
     */
    List<D> entityListToDtoList(List<E> entityList);
}
