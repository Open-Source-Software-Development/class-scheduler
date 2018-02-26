package demo;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import demo.data.*;
import osd.input.*;
import osd.considerations.Constraint;
import osd.considerations.Preference;
import schedule.PriorityList;

import java.util.*;

@Module
interface DemoModule {

    @Binds PriorityList<Section> bindSectionPriorityList(PriorityListImpl<Section> priorityList);

    @Binds Preference bindsPreferences(DemoPreferences demoPreferences);

    @Binds Constraint bindsConstraints(DemoConstraints demoConstraints);

    @Provides static Collection<Room> providesRoomCollection() {
        return list(DemoRoom.values());
    }

    @Provides static Collection<Course> providesCourseCollection() {
        return list(DemoCourse.values());
    }

    @Provides static Collection<Section> providesSectionCollection() {
        final List<Section> sections = new ArrayList<>();
        providesCourseCollection().stream()
                .map(Course::getSections)
                .forEach(c -> c.forEach(sections::add));
        return sections;
    }

    @Provides static Collection<Block> providesBlockCollection() {
        return list(DemoBlock.values());
    }

    @Provides static Collection<Professor> providesProfessorCollection() {
        return list(DemoProfessor.values());
    }

    @SafeVarargs
    static <E> List<E> list(final E... values) {
        final List<E> result = Arrays.asList(values);
        Collections.shuffle(result);
        return result;
    }

}
