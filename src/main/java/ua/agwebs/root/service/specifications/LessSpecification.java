package ua.agwebs.root.service.specifications;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;

public class LessSpecification<T> extends AbstractPocketBalanceSpecification<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessOrEqualSpecification.class);

    public LessSpecification(SearchCriteria searchCriteria) {
        LOGGER.trace("Creating a {} with values: {}", AbstractPocketBalanceSpecification.class.getSimpleName(), criteria);

        Assert.notNull(searchCriteria);
        this.criteria = searchCriteria;

        LOGGER.debug("New {} created: {}", AbstractPocketBalanceSpecification.class.getSimpleName(), this);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        LOGGER.trace("Creating a Predicate by the specification: {}", this);

        Predicate predicate = cb.lessThan((Path<Comparable>) this.buildPath(root, criteria.getKey()), (Comparable) criteria.getValue());

        LOGGER.debug("Created predicate: {} - by the specification: {}", predicate, this);

        return predicate;
    }
}
