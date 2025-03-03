package litheraa.data.repository;

import litheraa.data.entities.Text;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends CrudRepository<Text, Long> {
	Text findByPath(String path);
	@EntityGraph(attributePaths = {"prods"})
	Text findTopByPath(String path);
}
