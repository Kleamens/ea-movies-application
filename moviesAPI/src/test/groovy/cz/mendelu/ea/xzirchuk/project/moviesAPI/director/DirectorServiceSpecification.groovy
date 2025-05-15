package cz.mendelu.ea.xzirchuk.project.moviesAPI.director

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import groovy.util.logging.Slf4j
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest(classes = [MoviesApiApplication])
@ActiveProfiles("test")
@Slf4j
@Transactional
class DirectorServiceSpecification extends Specification{

    @Autowired
    @Subject
    DirectorService directorService;

    def "Should give a list of directors"(){
        given:
            List<Director> directors= directorService.getAllDirectors();
        when:
            int directorsListSize = directors.size()
        then:
            directorsListSize == 2
    }

    def "Should create a new director"(){
        given:
            List<Director> originalDirectors = directorService.getAllDirectors()
            int originalDirectorListSize = originalDirectors.size()
            Director director  = Mocks.director
            directorService.createDirector(director)
        when:
            List<Director> newDirectors = directorService.getAllDirectors()
            int directorsListSize = newDirectors.size()
        then:
        (directorsListSize == originalDirectorListSize+1)&&(newDirectors.contains(director))
    }

    def "Should delete a director"(){
        given:
            List<Director> originalDirectors = directorService.getAllDirectors()
            int originalDirectorListSize = originalDirectors.size()
            UUID idToDelete = UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
            directorService.deleteDirector(idToDelete)
        when:
            List<Director> newDirectors = directorService.getAllDirectors()
            int directorsListSize = newDirectors.size()
        then:
        (directorsListSize == originalDirectorListSize-1)&&(directorService.getDirectorById(idToDelete).isEmpty())
    }


}
