package litheraa.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Time {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private final LocalDate modified;
	private int goal;
	private int written = 0;
	@OneToMany(mappedBy = "id.timeId", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Prod> prods;

	public Time(LocalDate modified) {
		this.modified = modified;
	}

	public void addWritten(int written) {
		this.written += written;
	}

	@Override
	public String toString() {
		return "id= " + id + " ,modified= " + modified + " ,goal= " + goal + " ,written= " + written;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof final Time other)) return false;
		if (!other.canEqual(this)) return false;
		final Object this$modified = this.getModified();
		final Object other$modified = other.getModified();
		return Objects.equals(this$modified, other$modified);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Time;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $modified = this.getModified();
		result = result * PRIME + ($modified == null ? 43 : $modified.hashCode());
		return result;
	}
}
