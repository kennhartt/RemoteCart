package com.practice.kenny.remotecart;

import android.content.Context;
import android.content.Intent;

public class DrawerNav {

    public DrawerNav(){}

    public void DrawerNavClick(Context ctx, String menuId) {
        switch (menuId) {
            case "Change Password":
                ctx.startActivity(new Intent(ctx, PasswordChange.class));
                break;
            case "Addresses":
                ctx.startActivity(new Intent(ctx, AdressList.class));
                break;
            case "Stores":
                ctx.startActivity(new Intent(ctx, StoreList.class));
                break;
            case "Credit Cards":
                ctx.startActivity(new Intent(ctx, CardList.class));
                break;
            case "Refer a Friend":
                ctx.startActivity(new Intent(ctx, ReferFriend.class));
                break;
            case "Shopping History":
                ctx.startActivity(new Intent(ctx, OrderHistory.class));
                break;
            case "Your Cart":
                ctx.startActivity(new Intent(ctx, OrderDelivery.class));
                break;
            case "Sign out" :
                ctx.startActivity(new Intent(ctx, MainActivity.class));
                SessionState.clearLoggedInEmailAddress(ctx);
                break;
        }
    }
}
