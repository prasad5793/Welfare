package lk.avsec_welfare.asset.main_account.service;


import lk.avsec_welfare.asset.main_account.dao.MainAccountDao;
import lk.avsec_welfare.asset.main_account.entity.Enum.FundType;
import lk.avsec_welfare.asset.main_account.entity.MainAccount;
import lk.avsec_welfare.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainAccountService implements AbstractService<MainAccount, Integer> {

    private final MainAccountDao mainAccountDao;

    @Autowired
    public MainAccountService(MainAccountDao mainAccountDao) {
        this.mainAccountDao = mainAccountDao;
    }

    public List<MainAccount> findAll() {
        return mainAccountDao.findAll();
    }

    public MainAccount findById(Integer id) {
        return mainAccountDao.getOne(id);
    }

    public MainAccount persist(MainAccount mainAccount) {
        return mainAccountDao.save(mainAccount);
    }

    public boolean delete(Integer id) {
        mainAccountDao.deleteById(id);
        return true;
    }

    public List<MainAccount> search(MainAccount mainAccount) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<MainAccount> debitExample = Example.of(mainAccount, matcher);
        return mainAccountDao.findAll(debitExample);
    }

    public MainAccount findByFundType(FundType fundType) {
    return mainAccountDao.findByFundType(fundType);
    }
}
