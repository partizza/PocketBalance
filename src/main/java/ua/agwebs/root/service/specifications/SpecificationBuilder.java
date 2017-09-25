package ua.agwebs.root.service.specifications;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import ua.agwebs.root.entity.EntryLine;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class SpecificationBuilder<T> {

    public static enum SpecificationCompositionType {AND, OR}

    private Specifications<T> compositeSpecification;

    public SpecificationBuilder(Specification<T> specification) {
        compositeSpecification = Specifications.where(specification);
    }

    public SpecificationBuilder() {
        this((Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> cb.and());
    }

    public SpecificationBuilder<T> and(Specification<T> specification) {
        compositeSpecification = compositeSpecification.and(specification);
        return this;
    }

    public SpecificationBuilder<T> or(Specification<T> specification) {
        compositeSpecification = compositeSpecification.or(specification);
        return this;
    }

    public Specification<T> build() {
        return compositeSpecification;
    }

    @Override
    public String toString() {
        return "SpecificationBuilder{" +
                "compositeSpecification=" + compositeSpecification +
                '}';
    }
}
