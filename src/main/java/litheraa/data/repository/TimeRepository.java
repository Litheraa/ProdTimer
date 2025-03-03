package litheraa.data.repository;

import litheraa.data.entities.Time;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeRepository extends CrudRepository<Time, Long> {
	Time findByModified(LocalDate modified);
	@EntityGraph(attributePaths = {"prods"})
	List<Time> findByModifiedBetween(LocalDate from, LocalDate to);
}
