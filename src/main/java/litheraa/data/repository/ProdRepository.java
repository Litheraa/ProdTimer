package litheraa.data.repository;

import litheraa.data.entities.Prod;
import litheraa.data.entities.ProdId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdRepository extends CrudRepository<Prod, ProdId> {
}
