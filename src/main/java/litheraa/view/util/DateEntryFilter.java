//package Litheraa.View.Util;
//
//import javax.swing.text.AttributeSet;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.DocumentFilter;
//import java.awt.*;
//import java.time.YearMonth;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//public class DateEntryFilter extends DocumentFilter {
//    private enum Date {DAY, MONTH, YEAR}
//
//    @Override
//    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
//        int posX = offset + 1;
//        for (int i = 0; i < string.length(); i++) {
//            String temp = String.valueOf(string.charAt(i));
//            int insertLength = string.length();
//            System.out.println(insertLength);
//            if (posX % 3 == 0 && posX < 9 && temp.matches("^.?$")) {
//                System.out.println("e1");
//                replaceAndRemove(fb, offset, "-", attrs);
//            } else if (isDateCorrect(fb, offset, temp)) {
//                System.out.println("e2");
//                replaceAndRemove(fb, offset, temp, attrs);
//            } else Toolkit.getDefaultToolkit().beep();
//        }
//
//            while (fb.getDocument().getLength() > 10) {
//                remove(fb, fb.getDocument().getLength() - 1, 1);
//            }
//    }
//
//    private void replaceAndRemove(FilterBypass fb, int offset, String s, AttributeSet attrs) throws BadLocationException {
//        super.replace(fb, offset, 0, s, attrs);
//        super.remove(fb, offset + 1, 1);
//    }
//
//    private boolean isDateCorrect(FilterBypass fb, int insertPosition, String s) throws BadLocationException {
//        int dateStart;
//        int dateLength;
//        int maxDateValue;
//        Date date = getDateType(insertPosition);
//        System.out.println(date);
//        switch (date) {
//            case DAY -> {
//                String[] temp = fb.getDocument().getText(0, fb.getDocument().getLength()).split("-");
//                dateStart = 0;
//                dateLength = 2;
//                maxDateValue = YearMonth.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1])).lengthOfMonth();
//            }
//            case MONTH -> {
//                dateStart = 3;
//                dateLength = 2;
//                maxDateValue = 12;
//            }
//            case YEAR -> {
//                dateStart = 6;
//                dateLength = 4;
//                maxDateValue = new GregorianCalendar().get(Calendar.YEAR);
//            }
//            default -> throw new IllegalStateException("Unexpected value: " + date);
//        }
//        int len = dateStart + dateLength - 1;
//        if (insertPosition < 10 && s.matches("^[0-9]+$")) {
//            System.out.println("ip = " + insertPosition);
//            System.out.println("ds = " + dateStart);
//            int powInsert = (int) Math.pow(10, len - insertPosition);
//            System.out.println("pow = " + powInsert);
//            String source = fb.getDocument().getText(dateStart, dateLength);
//            int originalDate = Integer.parseInt(source);
//            System.out.println("oD1 = " + originalDate);
//            if (insertPosition != dateStart) {
//                originalDate = concatTimes(source, insertPosition, len, dateStart);
//                System.out.println("oD2 = " + originalDate);
//            }
//            return (originalDate + (Integer.parseInt(s) * powInsert)) <= maxDateValue;
//        }
//        return false;
//    }
//
//    private int concatTimes(String source, int insertPosition, int len, int dateStart) {
//        String valueRounded = source.substring(0, insertPosition - dateStart);
//        for (int i = len; i >= insertPosition; i--) {
//            valueRounded = valueRounded.concat("0");
//        }
//        return Integer.parseInt(valueRounded);
//    }
//
//    private Date getDateType(int insertPosition) {
//        if (insertPosition < 3) {
//            return Date.DAY;
//        }
//        if (insertPosition < 6) {
//            return Date.MONTH;
//        }
//        return Date.YEAR;
//    }
//}
