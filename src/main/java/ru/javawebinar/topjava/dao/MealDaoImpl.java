package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.Base;

import java.util.ArrayList;
import java.util.List;

public class MealDaoImpl implements MealDao {
    private List<Meal> base;

    public MealDaoImpl() {
        this.base = Base.getMeals();
    }

    @Override
    public void addMeal(Meal meal) {
        base.add(meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        List<Meal> temp = new ArrayList<>(base);
        for (Meal meal : base) {
            if (meal.getMealId() == mealId) {
                temp.remove(meal);
            }
        }
        base = temp;
    }

    @Override
    public void updateMeal(Meal meal) {
        List<Meal> temp = new ArrayList<>(base);
        for (int i = 0; i < base.size(); i++) {
            if (base.get(i).getMealId() == meal.getMealId()) {
                temp.set(i, meal);
            }
        }
        base = temp;
    }

    @Override
    public List<Meal> getAllMeals() {
        return base;
    }

    @Override
    public Meal getMealById(int mealId) {
        for (Meal meal : base) {
            if (meal.getMealId() == mealId) {
                return meal;
            }
        }
        return base.get(mealId);
    }
}
