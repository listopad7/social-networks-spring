package hu.listopad.socialnetworks.spring.data.dynamo;

import java.util.Optional;

public interface CompositeKeyRepository <T, PKEY, SKEY>{

    void save(T entity);

    Optional<T> findByKeys(PKEY pKey, SKEY sKey);

    Iterable<T> findByPrimaryKey(PKEY pKey);

    void deleteByKeys (PKEY pKey, SKEY sKey);








}
