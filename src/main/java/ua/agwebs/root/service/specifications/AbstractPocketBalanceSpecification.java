package ua.agwebs.root.service.specifications;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;

public abstract class AbstractPocketBalanceSpecification<T> implements Specification<T> {

    protected SearchCriteria criteria;

    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    abstract public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb);

    protected Path<?> buildPath(Root<T> root, String pathString) {
        String[] pathArr = pathString.split("\\.");
        Path path = null;
        for (String attr : pathArr) {
            path = path == null ? root.get(attr) : path.get(attr);
        }

        return path;
    }

}
