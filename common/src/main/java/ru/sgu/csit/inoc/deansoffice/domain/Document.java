package ru.sgu.csit.inoc.deansoffice.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * User: hd (KhurtinDN(a)gmail.com)
 * Date: Aug 27, 2010
 * Time: 12:26:27 PM
 */
public class Document extends PersistentItem {
    private String number;
    private Date signedDate;
    private Template printTemplate;
    public Map<String, String> TEXT = new HashMap<String, String>();

    public void clear() {
        TEXT.clear();
        TEXT.put("SSU_ONLY_HEADER", "����������� ��������������� �����������");
        TEXT.put("SSU_BY_HEADER", "����� �. �. �������������");
        TEXT.put("SSU_ONLY", "����������� ��������������� �����������");
        TEXT.put("SSU_ONLY_GEN", "������������ ���������������� ������������");
        TEXT.put("SSU_BY", "����� �. �. �������������");
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public Template getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(Template printTemplate) {
        this.printTemplate = printTemplate;
    }
}
