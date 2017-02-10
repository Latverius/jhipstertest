package fr.latverius.jhipstertest.service;

import fr.latverius.jhipstertest.domain.Wish;
import fr.latverius.jhipstertest.domain.WishList;
import fr.latverius.jhipstertest.repository.WishListRepository;
import fr.latverius.jhipstertest.repository.WishRepository;
import fr.latverius.jhipstertest.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.inject.Inject;

/**
 * Service Implementation for managing Wish.
 */
@Service
@Transactional
public class WishService {

    private final Logger log = LoggerFactory.getLogger(WishService.class);
    
    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }
    
    @Inject
    private WishListRepository wishListRepository;

    /**
     * Save a wish.
     *
     * @param wish the entity to save
     * @return the persisted entity
     */
    public Wish save(Wish wish) {
        log.debug("Request to save Wish : {}", wish);
        WishList wishList = wishListRepository.getOne(wish.getWishList().getId());
        if(!wishList.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())){
        	throw new AccessDeniedException("You should not do this");
        }
        Wish result = wishRepository.save(wish);
        return result;
    }

    /**
     *  Get all the wishes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Wish> findAll(Pageable pageable) {
        log.debug("Request to get all Wishes");
        Page<Wish> result = wishRepository.findByWishListUserLogin(SecurityUtils.getCurrentUserLogin(), pageable);
        return result;
    }

    /**
     *  Get one wish by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Wish findOne(Long id) {
        log.debug("Request to get Wish : {}", id);
        Wish wish = wishRepository.findOne(id);
        return wish;
    }

    /**
     *  Delete the  wish by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Wish : {}", id);
        wishRepository.delete(id);
    }
}
