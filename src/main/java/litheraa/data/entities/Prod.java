package litheraa.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Setter
@Getter
public class Prod {
	@EmbeddedId
	private ProdId id;
	@Column(columnDefinition = "varchar(20) default 'Пустая прода'")
	private String name;
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int chars = 0;
	@Column(columnDefinition = "integer default 0")
	private int written = 0;
	private int goal;

	public boolean setChars(int chars) {
		addWritten(chars);
		boolean isNew = this.chars != chars;
		this.chars = chars;
		return isNew;
	}

	private void addWritten(int chars) {
		this.written += (chars - this.chars);
	}

	public Long getTimeId() {
		return getId().getTimeId();
	}

	public Long getTextId() {
		return getId().getTextId();
	}

	@Override
	public String toString() {
		return "ids= " + getId().getTimeId() + "/" + getId().getTextId() + " ,name= " + name + " ,chars= " + chars + " ,written= " + written + " ,goal= " + goal;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof final Prod other)) return false;
		if (!other.canEqual(this)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (!Objects.equals(this$id, other$id)) return false;
		return this.getChars() == other.getChars();
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Prod;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		result = result * PRIME + this.getChars();
		return result;
	}
}
