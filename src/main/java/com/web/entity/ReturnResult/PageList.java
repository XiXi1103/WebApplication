package com.web.entity.ReturnResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PageList {
    public String title;
    public int id;
    public Boolean isCreator;
    public Date date;
    public String dates;
    public void setDates(Date date) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        this.dates = sdf.format(date);
    }
}
