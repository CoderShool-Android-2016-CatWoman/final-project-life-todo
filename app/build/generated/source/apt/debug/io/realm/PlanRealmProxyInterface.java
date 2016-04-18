package io.realm;


public interface PlanRealmProxyInterface {
    public int realmGet$id();
    public void realmSet$id(int value);
    public String realmGet$title();
    public void realmSet$title(String value);
    public com.catwoman.lifetodo.models.Category realmGet$category();
    public void realmSet$category(com.catwoman.lifetodo.models.Category value);
    public int realmGet$goal();
    public void realmSet$goal(int value);
    public int realmGet$progress();
    public void realmSet$progress(int value);
    public long realmGet$dueTime();
    public void realmSet$dueTime(long value);
}
