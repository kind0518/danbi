package com.example.juhyang.danbi_watering_test;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class CheckFragment extends Fragment { //등록된 알람들을 확인하는 DialogFragment
        Button btn_son, btn_mon, btn_tue, btn_wed, btn_thr, btn_fri, btn_sat, btn_total;
        Context context;
        Water_CustomAdapter adapter;
        ListView listView;
        FloatingActionMenu menu;
        private FloatingActionButton fab_timer, fab_book;

        ArrayList<Water_AlarmData> alarmDatas_temp;
        private ArrayList<Water_AlarmData> waterAlarmDatas;
        private static final String ARG_SECTION_NUMBER = "section_number";
        //멤버 변수 선언
        public CheckFragment(Context context) {
            this.context =  context;
        }


        public CheckFragment newInstance(int sectionNumber) {
            CheckFragment fragment = new CheckFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check, container, false);

            initView(rootView);
            aboutView();
            makeList();
            return rootView;
        }

        public void onResume () {
            super.onResume();
            waterAlarmDatas = (ArrayList<Water_AlarmData>) Water_AlarmData.findWithQuery(Water_AlarmData.class, "SELECT * FROM Water_AlarmData ORDER BY hour, minuite", null);
            aboutView();
            makeList();

        }
        private void initView (View view) {
            menu = (FloatingActionMenu) view.findViewById(R.id.menu);
            fab_timer = (FloatingActionButton) view.findViewById(R.id.fab1);
            fab_book = (FloatingActionButton) view.findViewById(R.id.fab2);
            listView = (ListView) view.findViewById(R.id.listView);
            btn_son = (Button) view.findViewById(R.id.btn_son);
            btn_mon = (Button) view.findViewById(R.id.btn_mon);
            btn_tue = (Button) view.findViewById(R.id.btn_tue);
            btn_wed = (Button) view.findViewById(R.id.btn_wed);
            btn_thr = (Button) view.findViewById(R.id.btn_thr);
            btn_fri = (Button) view.findViewById(R.id.btn_fri);
            btn_sat = (Button) view.findViewById(R.id.btn_sat);
            btn_total = (Button) view.findViewById(R.id.btn_total);
        }

        private void makeList() {
            adapter = new Water_CustomAdapter(waterAlarmDatas, context);
            listView.setAdapter(adapter);
        }
        private void aboutView() {

            waterAlarmDatas = (ArrayList<Water_AlarmData>) Water_AlarmData.findWithQuery(Water_AlarmData.class, "SELECT * FROM Water_AlarmData ORDER BY hour, minuite", null);
            final int length = waterAlarmDatas.size();
            btn_son.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).son) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });
            btn_mon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).mon) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);

                }
            });
            btn_tue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).tue) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });

            btn_wed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).wed) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });

            btn_thr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).thr) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });
            btn_fri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).fri) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });
            btn_sat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmDatas_temp = new ArrayList<Water_AlarmData>();
                    for (int i = 0; i < length; i ++) {
                        if (waterAlarmDatas.get(i).sat) {
                            alarmDatas_temp.add(waterAlarmDatas.get(i));
                        }
                    }
                    adapter = new Water_CustomAdapter(alarmDatas_temp, context);
                    listView.setAdapter(adapter);
                }
            });
            btn_total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    waterAlarmDatas = (ArrayList<Water_AlarmData>) Water_AlarmData.listAll(Water_AlarmData.class);
                    makeList();
                }
            });



            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) { // listView 에 longtime 리스너 생성
                    final Water_AlarmData waterAlarmData_temp = waterAlarmDatas.get(position);
                    Log.v("position", String.valueOf(position));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //다이얼로그 생성
                    builder.setTitle("정말로 삭제하시겠습니까 ?");

                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { //삭제버튼
                            long id_long = waterAlarmData_temp.getId(); // id값으로 객체 끌어옴
                            int id = (int) id_long; // int로 변환
                            cancelAlarm(id); //알람 취소
                            Log.v("check", String.valueOf(waterAlarmDatas.get(position).minute));
                            waterAlarmData_temp.delete(); //DB에서 삭제
                            waterAlarmDatas.remove(position);
                            makeList();
                            adapter.notifyDataSetChanged(); //어뎁터에 변환되엇다고 알려줌

                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { //just 취소
                            Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                }
            });

            menu.setOnMenuButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menu.isOpened()) {
                    }
                    menu.toggle(true);
                }
            });

            menu.setClosedOnTouchOutside(true);


            fab_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    TimerFragment dialogFragment = new TimerFragment();
                    dialogFragment.show(fm, "fragment_dialog_test");
                }
            });

            fab_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    ScheduleFragment dialogFragment = new ScheduleFragment(context);
                    dialogFragment.show(fm, "fragment_dialog_test");
                }
            });



        }


        public void cancelAlarm(int id)
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, Water_AlarmReceiver.class);
            PendingIntent pending = getPendingIntent(intent, id);
            alarmManager.cancel(pending);
        }

        private PendingIntent getPendingIntent(Intent intent, int id)
        {
            PendingIntent pIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return pIntent;
        }
    }
    public static class TimerFragment extends DialogFragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        TextView textView_timer;
        CountDownTimer timer;
        ToggleButton timer_Toggle; //토글키 객체 생성
        String onoff;   //신호를 보낼 1 or 0 값
        String min;     //신호를 보낼 '분'의 값
        Spinner spinner_minute;     //스피너 객체 생성
        String[] minute_list = {"1", "2", "3", "4", "5", "10", "15", "20", "30"}; //스피너에 들어갈 list
        ServerInterface api = null;   //ServerInterface 객체 생성

        public TimerFragment() {
        }


        public static TimerFragment newInstance(int sectionNumber) {
            TimerFragment fragment = new TimerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
            aboutView(rootView);

            return rootView;
        }
        void initView(View view) {
            timer_Toggle = (ToggleButton) view.findViewById(R.id.timer_toggle);  //layout과 연결
            spinner_minute = (Spinner) view.findViewById(R.id.spinner_minuite);  //layout과 연결
            textView_timer = (TextView) view.findViewById(R.id.textView_timer); //layout 과 연결
            textView_timer.setText(""); //초기화
        }

        void aboutView(View view) {
            initView(view);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, minute_list);
            spinner_minute.setAdapter(adapter);
        /* 스피너에 adapter를 통한 리스트 연결*/
            spinner_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //스피너가 선택 되었을 경우에
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //선택 되었을 경우
                    min = minute_list[position];

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { //아무것도 선택 안됐을 경우
                    min = minute_list[0];  //default 값인 "1"을 저장
                }
            });



            timer_Toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ApplicationController application = ApplicationController.getInstance(); //application 객체 생성
                    application.buildServerInterface();//서버 생성
                    Log.v("안내", "build server 성공.");

                    api = application.getServerInterface();
                    if (timer_Toggle.isChecked()) { //토글키 눌럿을때
                        onoff = "1";    //신호 1


                        Call<Water_model> call = api.GetData(onoff, min); //onoff 와 분에 관한 정보를 http로 송신

                        call.enqueue(new Callback<Water_model>() { //비동기방식 통신
                            @Override
                            public void onResponse(Response<Water_model> response, Retrofit retrofit) {
                                if (response.body().result.equals("1")) {
                                    Toast.makeText(getContext(), "물 나와열.", Toast.LENGTH_LONG).show();

                                    //타이머 흘러가게 만듬
                                    timer = new CountDownTimer(Integer.parseInt(min) * 60 * 1000, 1000) {
                                        /*1000 은 1초와 같음 ////  min 값이 선택됨에 따라 timer 초기값이 달라짐*/
                                        @Override
                                        public void onTick(long millisUntilFinished) { //타이머가 1초흘러갈때마다 진행 할 코드
                                            textView_timer.setText((millisUntilFinished / 1000) / 60 + " : " + (millisUntilFinished / 1000) % 60);
                                        /*분 : 초 를 setText로 나타냄*/
                                        }

                                        @Override
                                        public void onFinish() { //타이머가 종료되었을때 진행 할 코드
                                            textView_timer.setText("0 : 0"); // 시간이 모두 끝났을때 텍스트 0 : 0 으로 바꿈
                                            timer_Toggle.setChecked(false); // 토글키를 off 로 바꿈
                                            Call<Water_model> call = api.GetData("0", "0");

                                            call.enqueue(new Callback<Water_model>() {
                                                @Override
                                                public void onResponse(Response<Water_model> response, Retrofit retrofit) {
                                                    if (response.body().result.equals("1")) { //통신 성공했을경우
                                                        Toast.makeText(getContext(), "물 멈춰열.", Toast.LENGTH_LONG).show();
                                                        timer.cancel(); //타이머 정지
                                                    } else { //통신 실패 했을경우
                                                        Toast.makeText(getContext(), "어딘가 문제가 있어열.", Toast.LENGTH_LONG).show();
                                                        timer_Toggle.setChecked(true);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Toast.makeText(getContext(), "어딘가 문제가 있어열.",
                                                            Toast.LENGTH_LONG).show();
                                                    timer_Toggle.setChecked(true);
                                                }
                                            });
                                        }
                                    };
                                    timer.start(); //타이머 시작

                                } else {
                                    Toast.makeText(getContext(), "어딘가 문제가 있어열.", Toast.LENGTH_LONG).show();
                                    timer_Toggle.setChecked(false);
                                    //토글버튼 그대로 유지
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "어딘가 문제가 있어열.",
                                        Toast.LENGTH_LONG).show();
                                timer_Toggle.setChecked(false);
                            }
                        });

                    } else {
                        onoff = "0";
                        Call<Water_model> call = api.GetData("0", "0");

                        call.enqueue(new Callback<Water_model>() {
                            @Override
                            public void onResponse(Response<Water_model> response, Retrofit retrofit) {
                                if (response.body().result.equals("1")) { //통신 성공했을경우
                                    Toast.makeText(getContext(), "물 멈춰열.", Toast.LENGTH_LONG).show();
                                    timer.cancel(); //타이머 정지
                                } else { //통신 실패 했을경우
                                    Toast.makeText(getContext(), "어딘가 문제가 있어열.", Toast.LENGTH_LONG).show();
                                    timer_Toggle.setChecked(true);
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "어딘가 문제가 있어열.",
                                        Toast.LENGTH_LONG).show();
                                timer_Toggle.setChecked(true);
                            }
                        });
                    }
                }
            });
        }
    }
    public class ScheduleFragment extends DialogFragment {
        Context context;

        AlarmManager alarmManager;

        ToggleButton tb_sun, tb_mon, tb_tue, tb_wed, tb_thr, tb_fri, tb_sat;
        TimePicker timePicker;
        Button btn_alarm;
        Spinner spinner_minute;
        int hourOfDay_temp;
        int minute_temp;
        int during_temp;
        int onoff_temp;
        Intent intent;
        ArrayList<Water_AlarmData> waterAlarmDatas;
        String[] minute_list = {"1", "2", "3", "4", "5", "10", "15", "20", "30"};
        private static final String ARG_SECTION_NUMBER = "section_number";
        //멤버 변수 선언

        public ScheduleFragment(Context context) {
            this.context = context;
        }


        public ScheduleFragment newInstance(int sectionNumber) {
            ScheduleFragment fragment = new ScheduleFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override // 여기가 Activity의 onCreate부분을 담당
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); //원하는 시간에 원하는 코드를 실행하기 위해서 알람매니저 선언
            aboutView(rootView);

            return rootView;
        }
        private void initView(View view) { //View들 초기화
            timePicker = (TimePicker) view.findViewById(R.id.timePicker);
            btn_alarm = (Button) view.findViewById(R.id.btn_alarm);
            spinner_minute = (Spinner) view.findViewById(R.id.spinner);
            tb_sun = (ToggleButton) view.findViewById(R.id.tB_sun);
            tb_mon = (ToggleButton) view.findViewById(R.id.tB_mon);
            tb_tue = (ToggleButton) view.findViewById(R.id.tB_tue);
            tb_wed = (ToggleButton) view.findViewById(R.id.tB_wed);
            tb_thr = (ToggleButton) view.findViewById(R.id.tB_thr);
            tb_fri = (ToggleButton) view.findViewById(R.id.tB_fri);
            tb_sat = (ToggleButton) view.findViewById(R.id.tb_sat);
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void aboutView(View view) {
            initView(view);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, minute_list);
            spinner_minute.setAdapter(adapter);
        /* 스피너에 adapter를 통한 리스트 연결*/
            spinner_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //스피너가 선택 되었을 경우에
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //선택 되었을 경우
                    during_temp = Integer.parseInt(minute_list[position]);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { //아무것도 선택 안됐을 경우
                    during_temp = Integer.parseInt(minute_list[0]);  //default 값인 "1"을 저장
                }
            });
            hourOfDay_temp = timePicker.getCurrentHour();
            minute_temp = timePicker.getCurrentMinute();
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    hourOfDay_temp = hourOfDay;
                    minute_temp = minute;
                }
            });


            btn_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //알람 등록
                    boolean[] week = {false, tb_sun.isChecked(), tb_mon.isChecked(), tb_tue.isChecked(),
                            tb_wed.isChecked(), tb_thr.isChecked(), tb_fri.isChecked(),
                            tb_sat.isChecked()};
                    String repeat_temp = "";
                    for (int i = 1; i < 8; i++) {

                        if (week[i]) {
                            switch (i) {
                                case 1:
                                    repeat_temp += "일 ";
                                    break;
                                case 2:
                                    repeat_temp += "월 ";
                                    break;
                                case 3:
                                    repeat_temp += "화 ";
                                    break;
                                case 4:
                                    repeat_temp += "수 ";
                                    break;
                                case 5:
                                    repeat_temp += "목 ";
                                    break;
                                case 6:
                                    repeat_temp += "금 ";
                                    break;
                                case 7:
                                    repeat_temp += "토 ";
                                    break;
                            }
                        }
                    }
                    Water_AlarmData waterAlarmData = new Water_AlarmData(hourOfDay_temp, minute_temp, during_temp, 1, repeat_temp, week[1], week[2], week[3],
                            week[4], week[5], week[6], week[7]); //데이터 생성
                    waterAlarmData.save(); //Sugar_orm 에 저장
                    long id_long = waterAlarmData.getId(); //고유 id를 DB에서 끌어옴
                    int id = (int) id_long; //int형으로 변환
                    Log.v("태그", String.valueOf(id));  //확인
                    registAlarm(id);  //id를 넘기면서 알람을 등록
                    tb_sun.setChecked(false);
                    tb_mon.setChecked(false);
                    tb_tue.setChecked(false);
                    tb_wed.setChecked(false);
                    tb_thr.setChecked(false);
                    tb_fri.setChecked(false);
                    tb_sat.setChecked(false);
                }
            });


        }

        private void registAlarm(long id) //알람등록하는 method
        {

            boolean[] week = { false, tb_sun.isChecked(), tb_mon.isChecked(), tb_tue.isChecked(),
                    tb_wed.isChecked(), tb_thr.isChecked(), tb_fri.isChecked(),
                    tb_sat.isChecked() };
            //반복설정이 등록되는지 확인하는 배열
            boolean isRepeat = false; //일단은 반복은 없다 라고 가정
            int len = week.length; //for 문을위한 int값 생성
            for (int i = 0; i < len; i++)
            {
                if (week[i])
                {
                    isRepeat = true; //하나라도 체크되어있을경우에 반복이라고 바꿈
                    break;
                }
            }

            // 알람 등록
            Intent intent = new Intent(getContext(), Water_AlarmReceiver.class);

            long triggerTime = 0; //알람의 시작 시간
            if(isRepeat) //반복일 경우에
            {
                intent.putExtra("one_time", false); //반복이라고 putExtra로 보내줌
                intent.putExtra("day_of_week", week); //week 배열을 보내줌
                intent.putExtra("id", id); //id값을 보내줌
                PendingIntent pending = getPendingIntent(intent, id); //getPendingIntent 라는 method에 보내줌

                triggerTime = setTriggerTime(); //시작시간 설정
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 86400000, pending); //(알람매니저 타입, 시작시간, interval시간, intent)
                Log.v("알람", "등록완료");

            }
            else
            {
                intent.putExtra("one_time", true); //반복이 아닐경우
                intent.putExtra("id", id); //id값을 보내줌
                PendingIntent pending = getPendingIntent(intent, id);

                triggerTime = setTriggerTime();
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime,  pending);
            }
            Toast.makeText(getContext(), "등록완료", Toast.LENGTH_LONG).show();
        }

        private PendingIntent getPendingIntent(Intent intent, long id_long)
        {
            int id = (int) id_long; //DB에서 고유 id값을 받아와서 int형으로 변환
            PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //2번째 parameter가 PendingIntent 등록할때 구분하는 값이 됨 >> 중요!
            return pIntent;
        }


        private long setTriggerTime()
        {
            // current Time
            long atime = System.currentTimeMillis(); //현재시간 저장
            // timepicker
            Calendar curTime = Calendar.getInstance(); //캘린더 변수 생성
            curTime.set(Calendar.HOUR_OF_DAY, hourOfDay_temp); //캘린더에 시간 저장
            curTime.set(Calendar.MINUTE, minute_temp); //캘린더에 분 저장
            curTime.set(Calendar.SECOND, 0); //초, millisecond 0으로 넣음
            curTime.set(Calendar.MILLISECOND, 0);
            long btime = curTime.getTimeInMillis();  //calendar를 btime에 저장
            long triggerTime = btime; //b time 을 triggertime에 저장
            if (atime > btime) //크기 비교해서
                triggerTime += 1000 * 60 * 60 * 24; //현재시간이 더 나중일 경우에 시작시간에 하루를 더함

            return triggerTime;
        }
    }


    public class TemperatureFragment extends Fragment {

        Context context;

        AlarmManager alarmManager;

        TextView current_temp, current_hum, setting_temp, setting_hum;
        TextView textView1, textView2, textView3, textView4;
        private FloatingActionButton fab_set;
        FloatingActionMenu menu_set;
        ArrayList<Temperature_SettingData> temperature_settingDatas;
        ServerInterface api = null;   //ServerInterface 객체 생성

        private static final String ARG_SECTION_NUMBER = "section_number";

        //멤버 변수 선언
        public TemperatureFragment(Context context) {
            this.context = context;
        }


        public TemperatureFragment newInstance(int sectionNumber) {
            TemperatureFragment fragment = new TemperatureFragment(getApplicationContext());
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);


            initView(rootView);
            temperature_settingDatas = (ArrayList<Temperature_SettingData>) Temperature_SettingData.listAll(Temperature_SettingData.class);

            if (temperature_settingDatas.size() != 0) {
                setting_temp.setText(String.valueOf(temperature_settingDatas.get(0).temperature));
                setting_hum.setText(String.valueOf(temperature_settingDatas.get(0).humidity));
            }


            aboutView();

            return rootView;
        }

        public void initView(View view) {
            current_temp = (TextView) view.findViewById(R.id.current_temp);
            current_hum = (TextView) view.findViewById(R.id.current_hum);
            setting_temp = (TextView) view.findViewById(R.id.setting_temp);
            setting_hum = (TextView) view.findViewById(R.id.setting_hum);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            textView3 = (TextView) view.findViewById(R.id.textView3);
            textView4 = (TextView) view.findViewById(R.id.textView4);
            fab_set = (FloatingActionButton) view.findViewById(R.id.fab_set);
            menu_set = (FloatingActionMenu) view.findViewById(R.id.menu_set);
        }


        public void aboutView() {
            menu_set.setOnMenuButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menu_set.isOpened()) {
                    }
                    menu_set.toggle(true);
                }
            });

            menu_set.setClosedOnTouchOutside(true);


            fab_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //다이얼로그로 입력 받아서 db에 저장
                    FragmentManager fm = getSupportFragmentManager();
                    TemperatureSetting dialogFragment = new TemperatureSetting();
                    dialogFragment.show(fm, "fragment_dialog_test");
                }
            });
        }
        public Temperature_model getTemp() {
            ApplicationController application = ApplicationController.getInstance(); //application 객체 생성
            application.buildServerInterface();//서버 생성
            Log.v("안내", "build server 성공.");
            final Temperature_model[] temperature_model_temp = {null};
            api = application.getServerInterface();
            Call<Temperature_model> call = api.GetTemp();

            call.enqueue(new Callback<Temperature_model>() {
                @Override
                public void onResponse(Response<Temperature_model> response, Retrofit retrofit) {
                    temperature_model_temp[0] = new Temperature_model();
                    temperature_model_temp[0].temperature = response.body().temperature;
                    temperature_model_temp[0].humidity = response.body().humidity;
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
            return temperature_model_temp[0];
        }
    }
    public class TemperatureSetting extends DialogFragment {
        EditText editText_temp, editText_hum;
        TextView textView_temp, textView_hum;
        ArrayList<Temperature_SettingData> temperature_settingDatas;
        private static final String ARG_SECTION_NUMBER = "section_number";
        //멤버 변수 선언

        public TemperatureSetting() {

        }


        public TemperatureSetting newInstance(int sectionNumber) {
            TemperatureSetting fragment = new TemperatureSetting();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Dialog onCreateDialog (Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_settingtemp, null);
            initView(view);
            builder.setView(view);
            builder.setTitle("설정")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            temperature_settingDatas = (ArrayList<Temperature_SettingData>) Temperature_SettingData.listAll(Temperature_SettingData.class);
                            Temperature_SettingData settingData_temp = new Temperature_SettingData(Integer.parseInt(editText_temp.getText().toString()),
                                    Integer.parseInt(editText_hum.getText().toString()));
                            if (temperature_settingDatas.size() == 0) {
                                settingData_temp.save();
                            } else {
                                temperature_settingDatas.get(0).delete();
                                settingData_temp.save();
                            }

                            Toast.makeText(getApplicationContext(), "저장 ! ", Toast.LENGTH_SHORT).show();
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }


        public void initView (View view) {
            editText_temp = (EditText) view.findViewById(R.id.editText_temp);
            editText_hum = (EditText) view.findViewById(R.id.editText_hum);
            textView_temp = (TextView) view.findViewById(R.id.textView_temp);
            textView_hum = (TextView) view.findViewById(R.id.textView_hum);
        }

    }


    public class WeatherFragment extends Fragment {

        TextView cityName, temperature, weather, humidity, wind, rain;
        String cityNameString;
        Weather_WeatherToday weatherWeatherToday;
        Weather_WeatherWeekly weatherWeatherWeekly;
        ViewPager viewPager = null;
        Weather_MyViewPagerAdapter adapter;
        Handler handler = null;
        int pageScrollStat = 0;    //페이지번호
        int v = 1;    //화면 전환 뱡향

        private static final String ARG_SECTION_NUMBER = "section_number";

        //멤버 변수 선언
        public WeatherFragment() {

        }


        public WeatherFragment newInstance(int sectionNumber) {
            WeatherFragment fragment = new WeatherFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_weather_main, container, false);

            initView(rootView);
            initHandler();
            getWeather();

            return rootView;
        }
        private void initHandler() {
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if (pageScrollStat == 0) {
                        viewPager.setCurrentItem(1);
                        pageScrollStat++;
                        v = 1;
                    }
                    if (pageScrollStat == 1 && v == 0) {
                        viewPager.setCurrentItem(1);
                        pageScrollStat--;
                    }
                    if (pageScrollStat == 1 && v == 1) {
                        viewPager.setCurrentItem(2);
                        pageScrollStat++;
                    }
                    if (pageScrollStat == 2) {
                        viewPager.setCurrentItem(1);
                        pageScrollStat--;
                        v = 0;
                    }
                }
            };
        }
        private void initView(View view) {
            cityName = (TextView) view.findViewById(R.id.cityName);
            temperature = (TextView) view.findViewById(R.id.temperature);
            weather = (TextView) view.findViewById(R.id.weatherWeatherToday);
            humidity = (TextView) view.findViewById(R.id.humidity);
            wind = (TextView) view.findViewById(R.id.wind);
            rain = (TextView) view.findViewById(R.id.rain);

            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            adapter = new Weather_MyViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
        }

        public void viewPagerButtonListener(View v) {
            switch (v.getId()) {
                case R.id.today:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.weekly:
                    viewPager.setCurrentItem(1);
                    break;
                default:
                    break;
            }
        }

        // MapView 참고 http://seuny.tistory.com/14
        public void getWeather() {
            cityNameString = "Sunchun";

            // 날씨를 읽어오는 API 호출
            Weather_OpenWeatherAPIDayTask task_day = new Weather_OpenWeatherAPIDayTask();
            Weather_OpenWeatherAPIWeeklyTask task_weekly = new Weather_OpenWeatherAPIWeeklyTask();
            try {
                weatherWeatherToday = task_day.execute(cityNameString).get();
                weatherWeatherWeekly = task_weekly.execute(cityNameString).get();

                setInformation();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        private void setInformation() {

            temperature.setText(String.valueOf(weatherWeatherToday.getTemprature()));
            weather.setText(String.valueOf(weatherWeatherToday.getMainWeather()));
            cityName.setText(String.valueOf(weatherWeatherToday.getCity()));
            humidity.setText(String.valueOf(weatherWeatherToday.getHumidity()) + "%");
            wind.setText(String.valueOf(weatherWeatherToday.getWindDeg()) + ", " + String.valueOf(weatherWeatherToday.getWindSpeed()) + "m/s");

            if (weatherWeatherToday.getMainWeather().equals("비"))
                rain.setText(String.valueOf(weatherWeatherToday.getRain()) + " (3시간)");
            else if (weatherWeatherToday.getMainWeather().equals("눈"))
                rain.setText(String.valueOf(weatherWeatherToday.getSnow()) + " (3시간)");
            else
                rain.setText("");

        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle args = null;
            switch (position) {
                case 0:
                    fragment = new CheckFragment(getApplicationContext());
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new TemperatureFragment(getApplicationContext());
                    args = new Bundle();
                    break;
                case 2:
                    fragment = new WeatherFragment();
                    args = new Bundle();
                    break;
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "물주기";
                case 1:
                    return "온/습도";
                case 2:
                    return "날씨";

            }
            return null;
        }
    }
}
