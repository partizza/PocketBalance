package ua.agwebs.root.service.specifications;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class SearchCriteria {

    public static final Logger logger = LoggerFactory.getLogger(SearchCriteria.class);

    public static enum CriteriaType {
        EQUALS("="), LESS_OR_EQUAL("<=");

        private String criteriaType;

        private CriteriaType(String type) {
            this.criteriaType = type;
        }


        @Override
        public String toString() {
            return criteriaType;
        }
    }

    private String key;
    private CriteriaType criteria;
    private Object value;

    public SearchCriteria(String key, CriteriaType criteria, Object value) {
        logger.trace("Creating a {} with values: key = {}, criteria = {}, value = {}", SearchCriteria.class.getSimpleName(), key, criteria, value);

        Assert.notNull(key);
        Assert.notNull(criteria);
        Assert.notNull(value);

        this.key = key;
        this.criteria = criteria;
        this.value = value;

        logger.debug("New {} created: {}", SearchCriteria.class.getSimpleName(), this);
    }

    public String getKey() {
        return key;
    }

    public static Logger getLogger() {
        return logger;
    }

    public CriteriaType getCriteria() {
        return criteria;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "key='" + key + '\'' +
                ", criteria=" + criteria +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchCriteria that = (SearchCriteria) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (criteria != that.criteria) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
