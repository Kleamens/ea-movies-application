//package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;
//
//
//import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsInRelativeOrder;
//
//public class DirectorUnitTests {
//
//    @Test
//    public void sortDirectorsByNetworth(){
//        DirectorService directorService = new DirectorService(null,null);
//
//        Director director = new Director(
//                UUID.randomUUID(), // Generate a random UUID for id
//                "Quentin Tarantino", // Set the director's name
//                100000000.0f, // Set the net worth (assuming millions)
//                null// Leave the movie list empty for now
//        );
//        Director director1 = new Director(
//                UUID.randomUUID(),
//                "Steven Spielberg",
//                200000000.0f,
//                null // Add the movie to the director's list
//        );
//
//        List<Director> a = new ArrayList<>();
//        a.add(director);
//        a.add(director1);
//
//        directorService.sortDirectorsByNetWorth(a);
//
//        assertThat(a,containsInRelativeOrder(director1,director));
//    }
//
//}
