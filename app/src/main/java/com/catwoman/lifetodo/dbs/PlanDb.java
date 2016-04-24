package com.catwoman.lifetodo.dbs;

import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/23/16.
 */
public class PlanDb {
    private static final PlanDb INSTANCE = new PlanDb();
    private Realm realm = Realm.getDefaultInstance();

    private PlanDb() {
    }

    public static PlanDb getInstance() {
        return INSTANCE;
    }

    public void removePlan(int id) {
        Plan plan = this.getPlan(id);
        realm.beginTransaction();
        plan.removeFromRealm();
        realm.commitTransaction();
    }

    public void addOrUpdatePlan(int id, String title, Category category, int goal, long startTime, long endTime) {
        Plan plan;
        realm.beginTransaction();
        if (0 == id) {
            try {
                id = realm.where(Plan.class).max("id").intValue() + 1;
            } catch (Exception e) {
                id = 1;
            }
            plan = realm.createObject(Plan.class);
            plan.setId(id);
        } else {
            plan = this.getPlan(id);
        }
        plan.setTitle(title);
        plan.setCategory(category);
        plan.setGoal(goal);
        plan.setStartTime(startTime);
        plan.setEndTime(endTime);
        plan.setProgress(TodoItemDb.getInstance().getDoneItemsCount(plan));
        realm.commitTransaction();
    }

    public Plan getPlan(int id) {
        return realm.where(Plan.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Plan> getPlans(Category category) {
        return realm.where(Plan.class)
                .equalTo("category.id", category.getId())
                .findAll();
    }

    public RealmResults<Plan> getPlans() {
        return realm.where(Plan.class).findAllSorted("id", Sort.ASCENDING);
    }

    public void updatePlansProgress(Category category) {
        RealmResults<Plan> plans = this.getPlans(category);
        realm.beginTransaction();
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            plan.setProgress(TodoItemDb.getInstance().getDoneItemsCount(plan));
        }
        realm.commitTransaction();
    }
}
