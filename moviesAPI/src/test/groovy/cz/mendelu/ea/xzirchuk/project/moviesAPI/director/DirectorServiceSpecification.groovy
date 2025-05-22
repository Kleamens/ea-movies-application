package cz.mendelu.ea.xzirchuk.project.moviesAPI.director

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.CommonSpecification
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import groovy.util.logging.Slf4j
import jakarta.transaction.Transactional
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

class DirectorServiceSpecification extends CommonSpecification{

    @Autowired
    @Subject
    private DirectorService directorService;


    @Transactional
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
        (directorsListSize == originalDirectorListSize-1)
                &&(directorService.getDirectorById(idToDelete).isEmpty())
    }
    @Transactional
    def "Should create a new director"(){
        given:
        List<Director> originalDirectors = directorService.getAllDirectors()
        int originalDirectorListSize = originalDirectors.size()
        Director director =super.mock.getDirector()
        directorService.createDirector(director)
        when:
        List<Director> newDirectors = directorService.getAllDirectors()
        int directorsListSize = newDirectors.size()
        then:
        (directorsListSize == originalDirectorListSize+1)
                &&(newDirectors.contains(director))
        }
    def "Should give a list of directors"(){
        expect:
        directorService.getAllDirectors().size()==2;
    }



}
