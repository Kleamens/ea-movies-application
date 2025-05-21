package cz.mendelu.ea.xzirchuk.project.moviesAPI.director

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.CommonSpecification
import spock.lang.Specification

class DirectorUnitSpecification extends  CommonSpecification {




    def "Creating a director should create a director"(){
        expect:
            Director director = new Director(
                    name: "Jeff",
                    net_worth: 2.0,
                    movieList: []
            )
            director.getName()=="Jeff"
            director.getNet_worth()==2.0
            director.getMovieList()==[]
    }
}
