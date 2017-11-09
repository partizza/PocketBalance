package ua.agwebs.web.rest.entries.datatables;


import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ua.agwebs.root.service.specifications.SearchCriteria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataTableRequestResolver implements HandlerMethodArgumentResolver {

    public enum DataTableRequestType {
        INT, LONG, DOUBLE, STRING, DATE
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(DataTableRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        DataTableRequest dataTableRequest = new DataTableRequest();

        dataTableRequest = this.parseDraw(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseStart(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseLength(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseGlobalSearch(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseColumns(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseOrder(dataTableRequest, nativeWebRequest);
        dataTableRequest = this.parseFilters(dataTableRequest, nativeWebRequest);

        return dataTableRequest;
    }

    private DataTableRequest parseFilters(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        Iterator<String> paramIterator = nativeWebRequest.getParameterNames();

        int filterCnt = 0;
        while (paramIterator.hasNext()) {
            String paramName = paramIterator.next();
            if (paramName.matches("filters\\[[0-9]+\\]\\[column\\]")) {
                filterCnt++;
            }
        }

        List<SearchCriteria> criterias = new ArrayList<>();
        for (int i = 0; i < filterCnt; i++) {

            String column = nativeWebRequest.getParameter("filters[" + i + "][column]");
            SearchCriteria.CriteriaType criteriaType = SearchCriteria.CriteriaType.valueOf(nativeWebRequest.getParameter("filters[" + i + "][criteria]"));
            String value = nativeWebRequest.getParameter("filters[" + i + "][value]");
            String valueType = nativeWebRequest.getParameter("filters[" + i + "][valueType]");

            SearchCriteria searchCriteria = new SearchCriteria(column, criteriaType, this.castFilterType(valueType, value));
            criterias.add(searchCriteria);
        }

        dataTableRequest.setFilters(criterias);
        return dataTableRequest;
    }

    private Object castFilterType(String typeName, String value) {
        DataTableRequestType type = DataTableRequestType.valueOf(typeName.toUpperCase());
        switch (type) {
            case INT:
                return Integer.valueOf(value);
            case LONG:
                return Long.valueOf(value);
            case DOUBLE:
                return Double.valueOf(value);
            case STRING:
                return value;
            case DATE:
                return LocalDate.parse(value);
            default:
                throw new IllegalArgumentException("Unsuported type name!");
        }
    }

    private DataTableRequest parseDraw(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        final String paramVal = nativeWebRequest.getParameter("draw");
        if (paramVal != null) {
            dataTableRequest.setDraw(Integer.valueOf(paramVal));
        }

        return dataTableRequest;
    }

    private DataTableRequest parseStart(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        final String paramVal = nativeWebRequest.getParameter("start");
        if (paramVal != null) {
            dataTableRequest.setStart(Integer.valueOf(paramVal));
        }

        return dataTableRequest;
    }

    private DataTableRequest parseLength(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        final String paramVal = nativeWebRequest.getParameter("length");
        if (paramVal != null) {
            dataTableRequest.setLength(Integer.valueOf(paramVal));
        }

        return dataTableRequest;
    }

    private DataTableRequest parseGlobalSearch(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {

        DataTableSearch globalSearch = new DataTableSearch();

        globalSearch.setValue(nativeWebRequest.getParameter("search[value]"));

        final String isRegex = nativeWebRequest.getParameter("search[regex]");
        if (isRegex != null) {
            globalSearch.setRegex(Boolean.valueOf(isRegex));
        }

        dataTableRequest.setSearch(globalSearch);

        return dataTableRequest;
    }

    private DataTableRequest parseColumns(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        Iterator<String> paramIterator = nativeWebRequest.getParameterNames();

        int paramCnt = 0;
        while (paramIterator.hasNext()) {
            String paramName = paramIterator.next();
            if (paramName.matches("columns\\[[0-9]+\\]\\[data\\]")) {
                paramCnt++;
            }
        }

        List<DataTableColumn> columnList = new ArrayList<>();
        for (int i = 0; i < paramCnt; i++) {
            DataTableColumn column = new DataTableColumn();

            column.setData(nativeWebRequest.getParameter("columns[" + i + "][data]"));
            column.setName(nativeWebRequest.getParameter("columns[" + i + "][name]"));

            String isSearchable = nativeWebRequest.getParameter("columns[" + i + "][searchable]");
            if (isSearchable != null) {
                column.setSearchable(Boolean.valueOf(isSearchable));
            }

            String isOrderable = nativeWebRequest.getParameter("columns[" + i + "][orderable]");
            if (isOrderable != null) {
                column.setOrderable(Boolean.valueOf(isOrderable));
            }

            DataTableSearch search = new DataTableSearch();
            search.setValue(nativeWebRequest.getParameter("columns[" + i + "][search][value]"));
            String isRegex = nativeWebRequest.getParameter("columns[" + i + "][search][regex]");
            if (isRegex != null) {
                search.setRegex(Boolean.valueOf(isRegex));
            }
            column.setSearch(search);

            columnList.add(column);
        }

        dataTableRequest.setColumns(columnList);

        return dataTableRequest;
    }

    private DataTableRequest parseOrder(DataTableRequest dataTableRequest, NativeWebRequest nativeWebRequest) {
        Iterator<String> paramIterator = nativeWebRequest.getParameterNames();

        int paramCnt = 0;
        while (paramIterator.hasNext()) {
            String paramName = paramIterator.next();
            if (paramName.matches("order\\[[0-9]+\\]\\[column\\]")) {
                paramCnt++;
            }
        }

        List<DataTableColumnsOrder> orders = new ArrayList<>();
        for (int i = 0; i < paramCnt; i++) {
            DataTableColumnsOrder order = new DataTableColumnsOrder();

            String column = nativeWebRequest.getParameter("order[" + i + "][column]");
            if (column != null) {
                order.setColumn(Integer.valueOf(column));
            }

            order.setDir(nativeWebRequest.getParameter("order[" + i + "][dir]"));

            orders.add(order);
        }

        dataTableRequest.setOrders(orders);
        return dataTableRequest;
    }
}
