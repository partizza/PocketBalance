package ua.agwebs.root.service.specifications;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;

public class LessOrEqualSpecification<T> extends AbstractPocketBalanceSpecification<T> {

    private static final Logger logger = LoggerFactory.getLogger(LessOrEqualSpecification.class);

    public LessOrEqualSpecification(SearchCriteria searchCriteria) {
        logger.trace("Creating a {} with values: {}", AbstractPocketBalanceSpecification.class.getSimpleName(), criteria);

        Assert.notNull(searchCriteria);
        this.criteria = searchCriteria;

        logger.debug("New {} created: {}", AbstractPocketBalanceSpecification.class.getSimpleName(), this);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        logger.trace("Creating a Predicate by the specification: {}", this);

        Predicate predicate = cb.lessThanOrEqualTo((Path<Comparable>) this.buildPath(root, criteria.getKey()), (Comparable) criteria.getValue());

        logger.debug("Created predicate: {} - by the specification: {}", predicate, this);

        return predicate;
    }

}
