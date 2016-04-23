package com.catwoman.lifetodo.services;

import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
import com.catwoman.lifetodo.models.TodoItem;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/23/16.
 */
public class PlanService {
    private static final PlanService INSTANCE = new PlanService();
    private Realm realm = Realm.getDefaultInstance();

    private PlanService() {
    }

    public static PlanService getInstance() {
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
        plan.setProgress(TodoItemService.getInstance().getDoneItemsCount(plan));
        realm.commitTransaction();
    }

    public Plan getPlan(int id) {
        return realm.where(Plan.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Plan> getPlans(TodoItem todoItem) {
        return realm.where(Plan.class)
                .equalTo("category.id", todoItem.getCategory().getId())
                .findAll();
    }

    public RealmResults<Plan> getPlans() {
        return realm.where(Plan.class).findAllSorted("id", Sort.ASCENDING);
    }

    public void updatePlansProgress(TodoItem todoItem) {
        RealmResults<Plan> plans = this.getPlans(todoItem);
        realm.beginTransaction();
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            plan.setProgress(TodoItemService.getInstance().getDoneItemsCount(plan));
        }
        realm.commitTransaction();
    }
}
