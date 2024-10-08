package com.hb0730.zoom.base.mapstruct;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/8
 */
public interface BaseVoMapstruct<V, E> extends Mapstruct<E> {
    /**
     * vo转entity
     *
     * @param vo vo
     * @return entity
     */
    E voToEntity(V vo);

    /**
     * entity转vo
     *
     * @param entity entity
     * @return vo
     */
    V entityToVo(E entity);

    /**
     * voList转entityList
     *
     * @param voList voList
     * @return entityList
     */
    List<E> voListToEntityList(List<V> voList);

    /**
     * entityList转voList
     *
     * @param entityList entityList
     * @return voList
     */
    List<V> entityListToVoList(List<E> entityList);

}
