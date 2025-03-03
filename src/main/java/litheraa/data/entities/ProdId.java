package litheraa.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProdId implements Serializable {
	@Column(name = "text_id")
	private Long textId;
	@Column(name = "time_id")
	private Long timeId;

	@Override
	public boolean equals(final Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof final ProdId other)) return false;
		return textId != null &&
				timeId != null &&
				textId.equals(other.getTextId()) &&
				timeId.equals(other.getTimeId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
