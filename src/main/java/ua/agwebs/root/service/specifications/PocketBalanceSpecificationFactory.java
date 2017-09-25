package ua.agwebs.root.service.specifications;


public class PocketBalanceSpecificationFactory {

    public static <T> AbstractPocketBalanceSpecification getSpecification(SearchCriteria searchCriteria) {
        switch (searchCriteria.getCriteria()) {
            case EQUALS:
                return new EqualSpecification<T>(searchCriteria);
            case LESS_OR_EQUAL:
                return new LessOrEqualSpecification<T>(searchCriteria);
            default:
                throw new UnsupportableSearchCriteriaException("Search criteria is not supported yet: " + searchCriteria.getCriteria().toString());
        }
    }
}
