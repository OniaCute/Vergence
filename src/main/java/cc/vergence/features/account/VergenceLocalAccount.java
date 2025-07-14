package cc.vergence.features.account;

import cc.vergence.features.enums.AccountStatus;
import cc.vergence.features.enums.SubscriptionTypes;

import java.util.Date;

public class VergenceLocalAccount {
    public String name;
    public String token;
    public AccountStatus status;
    public SubscriptionTypes subscription;
    public Date expireDate;

    public VergenceLocalAccount(String name, AccountStatus status, SubscriptionTypes subscription, Date expireDate, String token) {
        this.name = name;
        this.status = status;
        this.subscription = subscription;
        this.expireDate = expireDate;
        this.token = token;
    }

    public boolean isExist() {
        return this.status == AccountStatus.Normal;
    }
}
