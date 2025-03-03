package litheraa.data.suppliers;

import litheraa.data.entities.Prod;
import litheraa.data.entities.ProdId;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class ProdSupplier implements Supplier<Prod> {
	private final Text text;
	private final Time time;

	@Override
	public Prod get() {
		Prod prod = new Prod();
		ProdId prodId = new ProdId(text.getId(), time.getId());
		prod.setId(prodId);
		return prod;
	}
}
