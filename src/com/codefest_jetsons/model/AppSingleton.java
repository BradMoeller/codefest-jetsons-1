package com.codefest_jetsons.model;

/**
 * Created with IntelliJ IDEA.
 * User: brack
 * Date: 2/24/13
 * Time: 2:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppSingleton {

    private static class AppSingletonHolder {
        public static final AppSingleton INSTANCE = new AppSingleton();
    }

    public static AppSingleton getInstance() {
        return AppSingletonHolder.INSTANCE;
    }

    public static void newInstance() {
        // reset values
    }
}
