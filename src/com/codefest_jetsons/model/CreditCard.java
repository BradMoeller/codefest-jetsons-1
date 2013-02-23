package com.codefest_jetsons.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreditCard {
    public static final String CARD_ID = "cc.cardid";
    public static final String FIRST_NAME = "cc.firstname";
    public static final String LAST_NAME = "cc.lastname";
    public static final String CC_NUMBER = "cc.number";
    public static final String CC_EXP_MONTH = "cc.exp.month";
    public static final String CC_EXP_YEAR = "cc.exp.year";
    public static final String CC_CCV = "cc.ccv";
    public static final String CC_TYPE = "cc.type";

    private long cardId;
    private String fName;
    private String lName;
    private String ccNumber;
    private String ccExpMonth;
    private String ccExpYear;
    private String ccCVV;
    private CreditCardType ccType;

    public CreditCard(long cardId, String fName, String lName, String ccNumber, String ccExpMonth,
                      String ccExpYear, String ccCCV, CreditCardType ccType) {
        this.cardId = cardId;
        this.fName = fName;
        this.lName = lName;
        this.ccNumber = ccNumber;
        this.ccExpMonth = ccExpMonth;
        this.ccExpYear = ccExpYear;
        this.ccCVV = ccCCV;
        this.ccType = ccType;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcExpMonth() {
        return ccExpMonth;
    }

    public void setCcExpMonth(String ccExpMonth) {
        this.ccExpMonth = ccExpMonth;
    }

    public String getCcExpYear() {
        return ccExpYear;
    }

    public void setCcExpYear(String ccExpYear) {
        this.ccExpYear = ccExpYear;
    }

    public String getCcCVV() {
        return ccCVV;
    }

    public void setCcCVV(String ccCVV) {
        this.ccCVV = ccCVV;
    }

    public CreditCardType getCcType() {
        return ccType;
    }

    public void setCcType(CreditCardType ccType) {
        this.ccType = ccType;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public static enum CreditCardType {
        MASTER_CARD ("MasterCard"),
        VISA ("Visa"),
        AMEX ("Amex"),
        DISCOVER ("Discover");

        private static final Map<String,CreditCardType> lookupTag = new HashMap<String,CreditCardType>();

        static {
            for(CreditCardType e : EnumSet.allOf(CreditCardType.class)) {
                lookupTag.put(e.value.toUpperCase(), e);
            }
        }
        private final String value;


        CreditCardType(final String value) {
            this.value = value;
        }

        public static CreditCardType getFromLabel (final String value) {
            return lookupTag.get(value);
        }
    };

    @Override
    public String toString() {
        return "CREDIT CARD" + "\n" + "================" +
                "Card id: " + cardId + "\n" +
                "First name: " + fName + "\n" +
                "Last name: " + lName + "\n" +
                "CC number: " + ccNumber + "\n" +
                "CC exp month: " + ccExpMonth + "\n" +
                "CC exp year: " + ccExpYear + "\n" +
                "CC ccv: " + ccCVV + "\n" +
                "CC type: " + ccType.name() + "\n";
    }
}
