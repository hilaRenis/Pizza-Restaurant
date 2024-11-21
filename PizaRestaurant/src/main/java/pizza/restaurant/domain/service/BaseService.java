package pizza.restaurant.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pizza.restaurant.domain.entity.Model;
import pizza.restaurant.domain.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseService<T extends Model<S>, V extends BaseRepository<T, S>, S> {

    /**
     * Base repository
     */
    protected V baseRepository;

    /**
     * Default Required constructor
     *
     * @param baseRepository repository
     */
    public BaseService(V baseRepository) {
        this.baseRepository = baseRepository;
    }


    /**
     * Get by id.
     *
     * @param id Id.
     * @return Object.
     */
    public T getById(S id) {
        return baseRepository.findById(id).orElse(null);
    }

    /**
     * Find by id.
     *
     * @param id Id.
     * @return Optional of object.
     */
    public Optional<T> findById(S id) {
        return baseRepository.findById(id);
    }

    /**
     * Required default create method for Entity {@link T}
     *
     * @param t Entity type.
     * @return T object.
     */
    public T create(T t) {
        baseRepository.save(t);
        return getById(t.getId());
    }

    /**
     * Required default create method for Entity
     *
     * @param t entities
     * @return list of created entities
     */
    public List<T> create(Iterable<T> t) {
        return baseRepository.saveAll(t);
    }


    /**
     * Update all batch
     *
     * @param ts List of object
     * @return {@link List<T>}
     */
    public List<T> updateAll(Iterable<T> ts) {
        return this.baseRepository.saveAll(ts);
    }

    /**
     * Update entity.
     *
     * @return Entity updated.
     */
    @Transactional
    public T update(T entity, S id) {
        entity.setId(id);
        return baseRepository.save(entity);
    }

    /**
     * Update and Flush
     *
     * @param entity
     * @param id
     * @return
     */
    @Transactional
    public T updateAndFlush(T entity, S id) {
        entity.setId(id);
        return baseRepository.saveAndFlush(entity);
    }

    /**
     * Get all.
     *
     * @return List of model.
     */
    @Transactional
    public List<T> list() {
        return baseRepository.findAll();
    }


    /**
     * Delete by type
     *
     * @param t Entity object to delete
     */
    @Transactional
    public void delete(T t) {
        this.deleteById(t.getId());
    }

    /**
     * Delete by id
     *
     * @param s Entity id
     */
    @Transactional
    public void deleteById(S s) {
        baseRepository.deleteById(s);
    }


    /**
     * Delete all rows
     *
     * @param ts
     */
    @Transactional
    public void deleteAll(Iterable<T> ts) {
        baseRepository.deleteAllInBatch(ts);
    }

}
