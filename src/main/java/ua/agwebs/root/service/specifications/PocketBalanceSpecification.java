package ua.agwebs.root.service.specifications;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;

public class PocketBalanceSpecification<T> implements Specification<T> {

    public static final Logger logger = LoggerFactory.getLogger(PocketBalanceSpecification.class);

    private SearchCriteria criteria;

    public PocketBalanceSpecification(SearchCriteria criteria) {
        logger.trace("Creating a {} with values: {}", PocketBalanceSpecification.class.getSimpleName(), criteria);

        Assert.notNull(criteria);
        this.criteria = criteria;

        logger.debug("New {} created: {}", PocketBalanceSpecification.class.getSimpleName(), this);
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        logger.trace("Creating a Predicate by the specification: {}", this);

        Predicate predicate = null;
        switch (criteria.getCriteria()) {
            case EQUALS:
                predicate = cb.equal(this.buildPath(root, criteria.getKey()), criteria.getValue());
                break;
            case LESS_OR_EQUAL:
                predicate = cb.lessThanOrEqualTo((Path<Comparable>) this.buildPath(root, criteria.getKey()), (Comparable) criteria.getValue());
                break;
            default:
                throw new UnsupportableSearchCriteriaException("Search criteria is not supported yet: " + criteria.getCriteria().toString());
        }

        logger.debug("Created predicate: {} - by the specification: {}", predicate, this);
        return predicate;
    }

    private Path<?> buildPath(Root<T> root, String pathString) {
        String[] pathArr = pathString.split("\\.");
        Path path = null;
        for (String attr : pathArr) {
            path = path == null ? root.get(attr) : path.get(attr);
        }

        return path;
    }

    @Override
    public String toString() {
        return "PocketBalanceSpecification{" +
                "criteria=" + criteria +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PocketBalanceSpecification that = (PocketBalanceSpecification) o;

        return criteria != null ? criteria.equals(that.criteria) : that.criteria == null;

    }

    @Override
    public int hashCode() {
        return criteria != null ? criteria.hashCode() : 0;
    }
}
