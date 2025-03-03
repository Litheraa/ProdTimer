package litheraa.data.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Text {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private final LocalDate created;
	@Column(nullable = false)
	private final String path;
	@Column(nullable = false)
	private String name;
	@Column(columnDefinition = "integer default 0")
	private int goal;
	@OneToMany(mappedBy = "id.textId", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("DESC")
	private List<Prod> prods;

	public Text(LocalDate created, Path path) {
		this.created = created;
		this.path = path.toString();
		this.name = path.toFile().getName();
	}

	public Prod getProd() {
		if (prods == null || prods.isEmpty()) {
			return null;
		}
		return prods.getFirst();
	}

	@Override
	public String toString() {
		return "id= " + id + " ,name= " + name + " ,created= " + created + " ,goal= " + goal;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof final Text other)) return false;
		if (!other.canEqual((Object) this)) return false;
		final Object this$created = this.getCreated();
		final Object other$created = other.getCreated();
		if (!Objects.equals(this$created, other$created)) return false;
		final Object this$path = this.getPath();
		final Object other$path = other.getPath();
		return Objects.equals(this$path, other$path);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Text;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $created = this.getCreated();
		result = result * PRIME + ($created == null ? 43 : $created.hashCode());
		final Object $path = this.getPath();
		result = result * PRIME + ($path == null ? 43 : $path.hashCode());
		return result;
	}
}
