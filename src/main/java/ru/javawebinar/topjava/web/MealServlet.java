package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/mealsAddUpdate.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        if (!request.getParameterMap().isEmpty()) {
            String action = request.getParameter("action");

            if (action.equalsIgnoreCase("delete")) {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                dao.deleteMeal(mealId);
                response.sendRedirect("meals");
                return;
            }

            if (action.equalsIgnoreCase("edit")) {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = dao.getMealById(mealId);
                request.setAttribute("meal", meal);
            }

            RequestDispatcher view = request.getRequestDispatcher(INSERT_OR_EDIT);
            view.forward(request, response);

        } else {
            request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.EXCESS_CALORIES));

            RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
            view.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(LocalDateTime.now(), "empty description", 0);
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        LocalDateTime date = LocalDateTime.parse(request.getParameter("date"), MealsUtil.dateTimeFormatter);
        meal.setDateTime(date);

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            dao.addMeal(meal);
        } else {
            meal.setMealId(Integer.parseInt(mealId));
            dao.updateMeal(meal);
        }

        response.sendRedirect("meals");
    }
}
