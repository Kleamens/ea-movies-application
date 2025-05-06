package cz.mendelu.ea.xzirchuk.project.moviesAPI.director

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorContoller
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import spock.lang.Specification
import spock.lang.Subject
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Utils

import javax.sql.DataSource
import java.sql.DriverManager
import java.sql.Statement

@SpringBootTest(classes = [MoviesApiApplication])
@ActiveProfiles("test")
@Slf4j
class DirectorServiceSpecification extends Specification{

    @Autowired
    @Subject
    DirectorService directorService;

    @Value('${spring.datasource.url}')
    String databaseUrl
    @Value('${spring.datasource.username}')
    String userName
    @Value('${spring.datasource.password}')
    String password

     String setupDataSQLScript ="/test-data/basedata.sql"
     String cleanupDataSQLScript = "/test-data/cleanup.sql"

    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()


    ResourceDatabasePopulator databaseSetup = new ResourceDatabasePopulator()
    ResourceDatabasePopulator databaseCleanup = new ResourceDatabasePopulator()

    // @Shared is the same as intializing this in the setupSpec
    void setup(){
        databaseSetup.addScript(new ClassPathResource(setupDataSQLScript))
        databaseCleanup.addScript(new ClassPathResource(cleanupDataSQLScript))

        Utils.execPopulator(
                dataSourceBuilder,
                databaseUrl,
                userName,
                password,
                databaseSetup,
                "Setup failed"
        )
    }
//
    void cleanup(){
        Utils.execPopulator(
                dataSourceBuilder,
                databaseUrl,
                userName,
                password,
                databaseCleanup,
                "Cleanup failed"
        )
    }




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
