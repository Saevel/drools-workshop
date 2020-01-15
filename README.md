Exercise One: Simple Rules // Default Accounts

Declare a Drools rule, in the "prv.saevel.drools.workshop.users" package, which searches for users without any
accounts assigned to them and then add an account with the "userId" equal to the user's "id", USD as currency and
0.0 as the default balance.

Go into the "prv.saevel.drools.workshop.users.UsersService" class and implement the "executeUserRules" method so 
that when it is called, all user-related rules are evaluated.

Once you do that, run the "UsersServiceTest" to verify the correctness of your implementation.

Exercise Two : Complex Rules / Anti-Fraud

Declare a set of Drools rules in the "prv.saevel.drools.workshop.antifraud" package which will calculate the account's ant-fraud
score according to the following rules: 

    - Every time within 3 minutes from transaction on an account, there is a transaction on the same account from a 
    different country, a FraudEvent with FraudEventType.TRANSACTION_FROM_MULTIPLE_COUNTRIES is emitted and adds 1.0 to
    overall fraud score. This kind of event should be emitted only once per multiple transactions from different countries
    within 3 minutes.
    
    - If for an account there is any transaction to/from a blacklisted country (as defined by the "blacklisted" flag on 
    the "Country" class in the Drools domain), a FraudEvent with FraudEventType.TRANSACTION_FROM_BLACKLISTED_COUNTRY is
    emitted and adds 0.5 to overall fraud score.
    
    - If for an account there is an outgoing transaction(TranactionType.WITHDRAWAL or TransactionType.TRANSFER_OUT),
    whose "amount" is over 90% of the "balance" of the account, a FraudEvent with FraudEventType.EMPTYING_ACCOUNT is
    emitted and adds 0.75 to overall fraud score.
    
    - Accounts should be blocked using the accountService::blockAccount method depending on the overall fraud score and 
    type of the user:
        * If the user is a trusted one (as indicated by the "trusted" flat on the User class in Drools domain) his account
        should be blocked once the overall fraud score reaches 3.0
        * If the user is not a trusted one, his account should be blocked once the overall fraud score reaches 2.0
        
Once you have done that, run the "AntifraudRulesTest" to verify the correctness of your implementation.
    