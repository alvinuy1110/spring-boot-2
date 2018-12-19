package com.myproject.springboot.jpa.util;

import org.springframework.data.domain.PageRequest;

import com.myproject.springboot.jpa.domain.PagingOption;
import lombok.Data;

@Data
public class PageRequestHelper {

    private PageRequest defaultPageRequest;
    /**
     * true - will start page at #1; default is false (start at 0)
     */
    private boolean startPageIndexAtOne;

    public PageRequestHelper(PageRequest defaultPageRequest) {
        this.defaultPageRequest = defaultPageRequest;
    }

    public PageRequest toPageRequest(PagingOption pagingOption) {
        int pageNumber = defaultPageRequest.getPageNumber();
        int pageSize = defaultPageRequest.getPageSize();
        if (pagingOption != null) {

            pageNumber = pagingOption.getPageNumber();
            if (this.isStartPageIndexAtOne()) {
                pageNumber = pageNumber - 1;
            }
            pageSize = pagingOption.getPageSize();
        }
        if (pageNumber < 0) {
            pageNumber = defaultPageRequest.getPageNumber();
        }
        if (pageSize < 1) {
            pageSize = defaultPageRequest.getPageSize();
        }
        return new PageRequest(pageNumber, pageSize);

    }
}