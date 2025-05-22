package cz.mendelu.ea.xzirchuk.project.moviesAPI.utility

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
class CommonSpecification extends Specification{
    protected Mocks mock

    def setup(){
        mock = new Mocks();
    }
}
