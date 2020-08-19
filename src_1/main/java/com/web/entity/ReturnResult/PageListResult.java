package com.web.entity.ReturnResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PageListResult {
    public List<PageList> pageList;
    public Date date;
    public String dates;

    public void setDates(Date date) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
        this.dates = sdf.format(date);
    }
}
