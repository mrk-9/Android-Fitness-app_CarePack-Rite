package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.EmptyAdapter;
import com.pooja.carepack.adapter.TreatmentAppointmentAdapter;
import com.pooja.carepack.model.ModelTreatment;
import com.pooja.carepack.swipemenulistview.SwipeMenu;
import com.pooja.carepack.swipemenulistview.SwipeMenuCreator;
import com.pooja.carepack.swipemenulistview.SwipeMenuItem;
import com.pooja.carepack.swipemenulistview.SwipeMenuListView;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.EventDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OneDayDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TreatmentPlanFragment extends BaseFragment implements LibPostListner {

    public int menu_item = 1;
    JsonObject element;
    private View view;
    private MaterialCalendarView widget;
    private String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private TextView prevMonth, currMonth, nextMonth;
    private Menu menu;
    private OneDayDecorator oneDayDecorator = new OneDayDecorator();
    //    private EventDecorator eventDecorator = new EventDecorator(Color.BLUE);
    private SwipeMenuListView mListView;
    private TreatmentAppointmentAdapter treatmentAppointmentAdapter;
    private CalendarDay currentCalenderDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_treetment_plane, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prevMonth = (TextView) view.findViewById(R.id.treatment_cal_prev_month);
        currMonth = (TextView) view.findViewById(R.id.treatment_cal_curr_month);
        nextMonth = (TextView) view.findViewById(R.id.treatment_cal_next_month);
        currentCalenderDay = new CalendarDay();
        mListView = (SwipeMenuListView) view.findViewById(R.id.frg_treatement_appointment_list);
        mListView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getActivity().getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                editItem.setWidth(Utility.dp2px(getResources(), 50));
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                deleteItem.setWidth(Utility.dp2px(getResources(), 50));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);

            }
        };
        mListView.setMenuCreator(creator);
        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Record Found"));
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
//                    Fragment fContainer = new TreatmentAddFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("appointmentId", treatmentAppointmentAdapter.getId(position));
//                    fContainer.setArguments(bundle);
//                    mainActivity.replaceFragment(new TreatmentAddFragment(), "Add Appointment","appointmentId", treatmentAppointmentAdapter.getId(position));
                    String date = currentCalenderDay.getYear() + "-" + (currentCalenderDay.getMonth() + 1) + "-" + currentCalenderDay.getDay();
                    mainActivity.replaceFragment(new TreatmentAddFragment(), "Add Appointment", "appointmentId", treatmentAppointmentAdapter.getId(position), "startdate", date);
//                    mainActivity.replaceFragment(fContainer, "Add Appointment");
                } else {
                    deleteAppointment(treatmentAppointmentAdapter.getId(position));
                }
                return true;
            }
        });

        widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        widget.setTopbarVisible(false);
        widget.setTileSizeDp(34);
        widget.setCurrentDate(Calendar.getInstance());
        widget.setSelectedDate(Calendar.getInstance());
        widget.setSelectionColor(getResources().getColor(R.color.btn_pressed));
        widget.setDynamicHeightEnabled(true);
        widget.addDecorators(oneDayDecorator);
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.invalidateDecorators();
                setList("" + date.getDay());
            }
        });
        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                widget.setCurrentDate(date);
                widget.invalidateDecorators();
                setMonthDetail(date);
                removeDecorators();
            }
        });
        setMonthDetail(widget.getCurrentDate());
    }

    private void removeDecorators() {
        widget.removeDecorators();
        widget.addDecorators(oneDayDecorator);
    }

    private void deleteAppointment(String appointmentId) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        if (appointmentId != null)
            hashMap.put("id", appointmentId);
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "delete_treatment_plan");
        new PostLibResponse(TreatmentPlanFragment.this, new ModelTreatment(), getActivity(), hashMap, WebServicesConst.TREATMENT_PLAN, WebServicesConst.RES.TREATMENT_PLAN, true, true);
    }

    private void setMonthDetail(CalendarDay date) {
        currentCalenderDay = date;
        prevMonth.setText(date.getPrevMonth());
        currMonth.setText(date.getCurrentMonth() + " " + date.getYear());
        nextMonth.setText(date.getNextMonth());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("date", date.getYear() + "-" + (date.getMonth() + 1) + "-01");
        new PostLibResponse(TreatmentPlanFragment.this, new ModelTreatment(), getActivity(), hashMap, WebServicesConst.TREATMENT_PLAN, WebServicesConst.RES.TREATMENT_PLAN, true, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_add:
//                toast("Under Developing");
//                mainActivity.replaceFragment(new TreatmentAddFragment(), "Add Appointment");
                String date = currentCalenderDay.getYear() + "-" + (currentCalenderDay.getMonth() + 1) + "-" + currentCalenderDay.getDay();
                mainActivity.replaceFragment(new TreatmentAddFragment(), "Add Appointment", "startdate", date);
                break;
            default:
                break;
        }
        return true;
    }

    private void setList(String day) {
        try {
            if (element != null) {
                JsonArray data = element.getAsJsonArray(day);
                if (data != null) {
                    treatmentAppointmentAdapter = new TreatmentAppointmentAdapter(getActivity(), data);
                    mListView.setAdapter(treatmentAppointmentAdapter);
                } else {
                    mListView.setAdapter(new EmptyAdapter(getActivity(), "No Record Found"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.TREATMENT_PLAN) {
            removeDecorators();
            ModelTreatment modelTreatment = (ModelTreatment) clsGson;
            if (modelTreatment != null && isVisible()) {
                if (modelTreatment.status == 200) {
                    element = modelTreatment.treatment_plans;
                    ArrayList<CalendarDay> dates = new ArrayList<>();
                    boolean isSetDate = false;
                    if (element.entrySet().size() > 0) {
                        for (Map.Entry<String, JsonElement> entry : element.entrySet()) {
                            CalendarDay day = new CalendarDay(widget.getCurrentDate().getYear(), widget.getCurrentDate().getMonth(), Integer.parseInt(entry.getKey()));
                            if (!isSetDate) {
                                isSetDate = true;
                                widget.setSelectedDate(day);
                                setList("" + entry.getKey());
                            }
                            dates.add(day);
                        }
                        widget.addDecorator(new EventDecorator(Color.RED, dates));
                    } else {
                        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Record Found"));
                    }

                } else {
                    widget.setSelectedDate(new CalendarDay(widget.getCurrentDate().getYear(), widget.getCurrentDate().getMonth(), 1));
                    mListView.setAdapter(new EmptyAdapter(getActivity(), modelTreatment.message));
                }
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
