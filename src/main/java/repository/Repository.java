package repository;

import java.util.List;

public interface Repository<T> {

	T findById(Long id);

	void save(T entity);
	
	void delete(Long id);

	List<T> findAll();

	long countAll();
}
