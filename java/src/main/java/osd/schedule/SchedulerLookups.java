package osd.schedule;

import osd.considerations.Lookups;
import osd.input.Block;
import osd.input.Professor;
import osd.input.Section;
import osd.output.Hunk;
import osd.output.Results;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SchedulerLookups implements Lookups {

    // These containers are a little bit kludgey. The primary consideration
    // was obtaining a decent tradeoff between lookup performance and
    // memory usage.
    private final Map<Professor, List<Hunk>> hunksByProfessor = new HashMap<>();
    private final Map<Block, List<Hunk>> hunksByBlock = new HashMap<>();
    private final Map<Section, Professor> sectionToProfessor = new HashMap<>();

    static SchedulerLookups empty() {
        return new SchedulerLookups();
    }

    private SchedulerLookups() {}

    private SchedulerLookups(final Stream<Hunk> hunks) {
        hunks.forEach(h -> {
            hunksByProfessor.computeIfAbsent(h.getProfessor(), z -> new ArrayList<>()).add(h);
            hunksByBlock.computeIfAbsent(h.getBlock(), z -> new ArrayList<>()).add(h);
            sectionToProfessor.put(h.getSection(), h.getProfessor());
        });
    }

    @Override
    public Stream<Hunk> lookupAllHunks() {
        return hunksByBlock.values().stream()
                .map(List::stream)
                .reduce(Stream.empty(), Stream::concat);
    }

    @Override
    public Stream<Hunk> lookup(final Professor professor) {
        if (hunksByProfessor.containsKey(professor)) {
            return hunksByProfessor.get(professor).stream();
        }
        return Stream.empty();
    }

    @Override
    public Stream<Hunk> lookup(final Block block) {
        if (hunksByBlock.containsKey(block)) {
            return hunksByBlock.get(block).stream();
        }
        return Stream.empty();
    }

    @Override
    public Hunk lookup(final Section section) {
        final Professor intermediate = sectionToProfessor.get(section);
        if (intermediate == null) {
            return null;
        }
        return lookup(intermediate)
                .filter(h -> section.equals(h.getSection()))
                .findAny()
                .orElse(null);
    }

    SchedulerLookups extend(final Hunk newHunk) {
        final Stream<Hunk> hunks = Stream.concat(lookupAllHunks(), Stream.of(newHunk));
        return new SchedulerLookups(hunks);
    }

}
