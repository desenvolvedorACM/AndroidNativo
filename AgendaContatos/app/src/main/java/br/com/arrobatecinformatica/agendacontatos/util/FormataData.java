package br.com.arrobatecinformatica.agendacontatos.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandre on 22/04/2017.
 */

public class FormataData {

    public static Date getDate(int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        return calendar.getTime();
    }

    public static String DateToString(int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        Date data = calendar.getTime();
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);

        return format.format(data);
    }
}
