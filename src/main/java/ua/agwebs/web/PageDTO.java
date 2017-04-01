package ua.agwebs.web;


import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    /**
     * current page number, zero is the first
     */
    private int number;

    /**
     * total number of pages
     */
    private int totalPages;

    /**
     * page content
     */
    private List<T> content = new ArrayList<T>();

    public PageDTO() {
    }

    public PageDTO(int number, int totalPages) {
        this.number = number;
        this.totalPages = totalPages;
    }

    public PageDTO(int number, int totalPages, List<T> content) {
        this(number, totalPages);
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void addContent(T element) {
        this.content.add(element);
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "number=" + number +
                ", totalPages=" + totalPages +
                ", content=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageDTO<?> pageDTO = (PageDTO<?>) o;

        if (number != pageDTO.number) return false;
        if (totalPages != pageDTO.totalPages) return false;
        return content != null ? content.equals(pageDTO.content) : pageDTO.content == null;

    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + totalPages;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

}
