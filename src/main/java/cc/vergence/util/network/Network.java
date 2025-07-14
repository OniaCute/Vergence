package cc.vergence.util.network;

import cc.vergence.Vergence;
import cc.vergence.features.account.*;
import cc.vergence.features.enums.AccountStatus;
import cc.vergence.features.enums.ActionType;
import cc.vergence.features.enums.SubscriptionTypes;
import cc.vergence.features.managers.URLManager;
import cc.vergence.util.data.JsonUtil;

import java.util.Date;
import java.util.HashMap;

public class Network {
    public static VergenceLocalAccount login(String accountName, String password, String HWID) {
        HashMap<?, ?> result = JsonUtil.stringToHashMap(Vergence.URL.getResponse(URLManager.serverAccountAddress + "?action=" + ActionType.PasswordLogin.name() + "&u=" + accountName + "&p=" + password + "&h=" + HWID));
        if (result.get("status") == "0") {
            if (result.get("result") == "true") {
                return new VergenceLocalAccount(
                        (String) result.get("account_username"),
                        (AccountStatus) result.get("account_status"),
                        (SubscriptionTypes) result.get("account_sub"),
                        (Date) result.get("account_expire"),
                        (String) result.get("account_token")
                );
            } else {
                if (result.get("cause") == "ERR_HWID") {
                    Vergence.CONSOLE.logAuth("Password Login failed! Error HWID.");
                }
                else if (result.get("cause") == "ERR_AE") {
                    Vergence.CONSOLE.logAuth("Password Login failed! The account name or password is wrong.");
                } else {
                    Vergence.CONSOLE.logAuth("Password Login failed! A excepted cause.");
                }
                return null;
            }
        }
        Vergence.CONSOLE.logAuth("Password Login failed! Cannot connect to login server!");
        return null;
    }

    public static VergenceLocalAccount login(LoginToken token, String HWID) {
        HashMap<?, ?> result = JsonUtil.stringToHashMap(Vergence.URL.getResponse(URLManager.serverAccountAddress + "?action=" + ActionType.CheckLoginToken.name() + "&t=" + token.token + "&h=" + HWID));
        if (result.get("status") == "0") {
            if (result.get("result") == "true") {
                return new VergenceLocalAccount(
                        (String) result.get("account_username"),
                        (AccountStatus) result.get("account_status"),
                        (SubscriptionTypes) result.get("account_sub"),
                        (Date) result.get("account_expire"),
                        (String) result.get("account_token")
                );
            } else {
                if (result.get("cause") == "ERR_HWID") {
                    Vergence.CONSOLE.logAuth("Token Login failed! Error HWID.");
                }
                else if (result.get("cause") == "ERR_NA") {
                    Vergence.CONSOLE.logAuth("Token Login failed! The token is not available anymore.");
                } else {
                    Vergence.CONSOLE.logAuth("Token Login failed! A excepted cause.");
                }
                return null;
            }
        }
        Vergence.CONSOLE.logAuth("Token Login failed! Cannot connect to login server!");
        return null;
    }

    public static String getUserStatus(VergenceLocalAccount account) {
        return account.status.name();
    }

    public static String getUserSubscription(VergenceLocalAccount account) {
        return account.subscription.name();
    }
}
