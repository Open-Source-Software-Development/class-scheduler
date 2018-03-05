package osd.output;

import java.util.Objects;

import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;

public class Hunk {

	private final Block block;
 	private final Room room;
 	private final Professor professor;
 	private final Section section;

	public Hunk(final Block block, final Room room, final Professor professor, final Section section) {
		this.block = block;
 		this.room = room;
 		this.professor = professor;
 		this.section = section;
	}

	public Block getBlock() {
		return this.block;
	}

 	public Room getRoom() {
		return this.room;
	}

 	public Professor getProfessor() {
		return this.professor;
	}

 	public Section getSection() {
		return this.section;
	}

	@Override
	public int hashCode() {
		return Objects.hash(block, room, professor, section);
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}

		if (o.getClass() != getClass()) {
			return false;
		}

		final Hunk other = (Hunk)o;
		return Objects.equals(block, other.block)
		    && Objects.equals(room, other.room)
		    && Objects.equals(professor, other.professor)
		    && Objects.equals(section, other.section);
	}

	@Override
	public String toString() {
		return "Hunk("
			+ String.join(", ", () ->
				Stream.of(block, room, professor, section)
					.map(Object::toString)
						.iterator()
			)+ ")";
	}

}
