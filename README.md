Exercise One: Simple Rules

Declare a Drools rule, in the "prv.saevel.drools.workshop.users" package, which searches for users without any
accounts assigned to them and then add an account with the "userId" equal to the user's "id", USD as currency and
0.0 as the default balance.

Go into the "prv.saevel.drools.workshop.users.UsersService" class and implement the "executeUserRules" method so 
that when it is called, all user-related rules are evaluated.

Once you do that, run the "UsersServiceTest" to verify the correctness of your implementation.