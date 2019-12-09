package prv.saevel.drools.workshop.antifraud;

public enum FraudEventType {
    TRANSACTION_FROM_BLACKLISTED_COUNTRY,
    ACCOUNT_EMPTYING,
    TRANSACTION_FROM_MULTIPLE_COUNTRIES;
}
