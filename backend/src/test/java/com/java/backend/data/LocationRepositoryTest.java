package com.java.backend.data;

import com.java.backend.model.Location;
import com.java.backend.model.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@ActiveProfiles("test")
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private KnownGoodState knownGoodState;

    @Autowired
    private WorldRepository worldRepository;

    private World world;
    @BeforeEach
    public void setUp() {
        knownGoodState.reset(); // Resets the state to a known good state before each test
        world = worldRepository.findById(1L).orElse(null);
    }

    @Test
    public void testFindByWorldId() {
        // Act: Find locations by worldId
        List<Location> locations = locationRepository.findByWorldId(1L);

        // Assert: Verify the results
        assertThat(locations).isNotEmpty();
        assertThat(locations).hasSize(2);
        assertThat(locations).extracting(Location::getName).containsExactly("New York City", "Amazon Rainforest");
    }

    @Test
    public void testFindByName() {
        // Arrange: Create and save locations with a known name
        Location location1 = new Location("Paris", "Urban", "The capital of France.");
        Location location2 = new Location("Berlin", "Urban", "The capital of Germany.");
        location1.setWorld(world);
        location2.setWorld(world);
        locationRepository.save(location1);
        locationRepository.save(location2);

        // Act: Find locations by name
        List<Location> locations = locationRepository.findByName("Paris");

        // Assert: Verify the results
        assertThat(locations).isNotEmpty();
        assertThat(locations).hasSize(1);
        assertThat(locations.get(0).getName()).isEqualTo("Paris");
    }

    @Test
    public void testFindByNonExistingWorldId() {
        // Act: Find locations by a non-existing worldId
        List<Location> locations = locationRepository.findByWorldId(999L);

        // Assert: Verify the results
        assertThat(locations).isEmpty();
    }

    @Test
    public void testFindByNonExistingName() {
        // Act: Find locations by a non-existing name
        List<Location> locations = locationRepository.findByName("NonExistentLocation");

        // Assert: Verify the results
        assertThat(locations).isEmpty();
    }
}