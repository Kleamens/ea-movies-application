package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorPaginationRepository directorPaginationRepository;



    public List<Director> getAllDirectors(){
        List<Director> directors = new ArrayList<>();
        directorRepository.findAll().forEach(directors::add);
        return directors;
    }

    public Optional<Director> getDirectorById(UUID id){
        return directorRepository.findById(id);
    }
    public Optional<Director> getDirectorByName(String name) {
        return directorRepository.getDirectorByName(name);
    }

    public Director createDirector(Director director){
        return directorRepository.save(director);
    }


    public Director updateDirector(UUID id, Director director) {
        return directorRepository.save(director);
    }

    public void deleteDirector(UUID id) {
        directorRepository.deleteById(id);
    }

    public Optional<Director> findDirectorByName(String name){
        return directorRepository.findDirectorByNameEquals(name);
    }
    public List<Director> getDirectorPage(int pageNumber, int pageSize){
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = directorPaginationRepository.findAll(pageRequest);
        return page.get().toList();
    }

    public void sortDirectorsByNetWorth(List<Director> directors){
        directors.sort(new Comparator<Director>() {

            public int compare(Director o1, Director o2) {
                return o1.getNet_worth().compareTo(o2.getNet_worth());
            }
        });

        Collections.reverse(directors);





    }

    public List<Director> getTopNDirectors(int top_n,List<Director> directors){
        List<Director> filtered_directors=List.of();
        try{
            filtered_directors =directors.subList(0,top_n);
        }catch (IndexOutOfBoundsException e){
            log.debug("### LIMIT TOO HIGH, RETURNING ALL OF THE AVAILABLE ITEMS ");
        }catch (NumberFormatException e){
            throw new BadInputException("AAAAAAAA");
        }
        return filtered_directors;
    }


}
