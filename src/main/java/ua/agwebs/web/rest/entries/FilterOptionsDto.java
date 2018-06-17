package ua.agwebs.web.rest.entries;

import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.EntrySide;

import java.util.Map;
import java.util.Set;

public class FilterOptionsDto {

    private Set<BSCategory> categories;
    private Map<Long, String> accounts;
    private Map<Long,String> currencies;
    private Set<EntrySide> entrySides;

    public Set<BSCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<BSCategory> categories) {
        this.categories = categories;
    }

    public Map<Long, String> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<Long, String> accounts) {
        this.accounts = accounts;
    }

    public Map<Long, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<Long, String> currencies) {
        this.currencies = currencies;
    }

    public Set<EntrySide> getEntrySides() {
        return entrySides;
    }

    public void setEntrySides(Set<EntrySide> entrySides) {
        this.entrySides = entrySides;
    }

    @Override
    public String toString() {
        return "FilterOptionsDto{" +
                "categories=" + categories +
                ", accounts=" + accounts +
                ", currencies=" + currencies +
                ", entrySides=" + entrySides +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterOptionsDto that = (FilterOptionsDto) o;

        if (categories != null ? !categories.equals(that.categories) : that.categories != null) return false;
        if (accounts != null ? !accounts.equals(that.accounts) : that.accounts != null) return false;
        if (currencies != null ? !currencies.equals(that.currencies) : that.currencies != null) return false;
        return entrySides != null ? entrySides.equals(that.entrySides) : that.entrySides == null;
    }

    @Override
    public int hashCode() {
        int result = categories != null ? categories.hashCode() : 0;
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        result = 31 * result + (currencies != null ? currencies.hashCode() : 0);
        result = 31 * result + (entrySides != null ? entrySides.hashCode() : 0);
        return result;
    }
}
