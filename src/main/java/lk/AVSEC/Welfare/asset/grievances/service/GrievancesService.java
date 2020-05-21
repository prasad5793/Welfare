package lk.AVSEC.Welfare.asset.grievances.service;

import lk.AVSEC.Welfare.asset.grievances.dao.GrievancesDao;
import lk.AVSEC.Welfare.asset.grievances.entity.Grievance;
import lk.AVSEC.Welfare.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig(cacheNames = "grievances")
public class GrievancesService implements AbstractService<Grievance, Integer> {

    private final GrievancesDao grievancesDao;

    @Autowired
    public GrievancesService(GrievancesDao grievancesDao) {

        this.grievancesDao = grievancesDao;
    }

    @Cacheable
    public List<Grievance> findAll() {

        return grievancesDao.findAll();
    }

    @Cacheable
    public Grievance findById(Integer id) {
        return grievancesDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "grievances", allEntries = true)},
            put = {@CachePut(value = "grievances", key = "#grievance.id")})
    @Transactional
    public Grievance persist(Grievance grievance) {
        return grievancesDao.save(grievance);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        grievancesDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<Grievance> search(Grievance grievance) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Grievance> grievancesExample = Example.of(grievance, matcher);
        return grievancesDao.findAll(grievancesExample);
    }

    public boolean isGrievancesPresent(Grievance grievance) {
        return grievancesDao.equals(grievance);
    }


/*    public List<Grievances> findByProvince(Province province) {
        return grievancesDao.findByProvince(province);
    }*/
}
