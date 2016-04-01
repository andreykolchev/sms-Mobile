package com.sms.util;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */


import com.sms.data.model.BaseEntity;

import java.util.List;

public class EntityUtils {

    public static <T extends BaseEntity> T findEntity(List<T> entities, T search){
        if (entities!=null){
            int index = entities.indexOf(search);
            if (index!=-1) {
                return entities.get(index);
            }
        }
        return null;
    }

    public static <T extends BaseEntity> T findEntity(List<T> entities, long id){
        if (entities!=null){
            for (T entity : entities) {
                if (entity.getId() == id){
                    return entity;
                }
            }
        }
        return null;
    }
}
