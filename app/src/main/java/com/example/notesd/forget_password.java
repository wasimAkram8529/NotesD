package com.example.notesd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;

public class forget_password extends AppCompatActivity {

    EditText registerNo, enteredOtp;
    Button sendOtp;
    Button verifyOtp;

    Toolbar toolbar;

    TextView selectCode;

    LinearLayout resetPassword, llVerifyOtp;

    Dialog dialog;

    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> countryCodeList = new ArrayList<>();
    ArrayList<String> countryWithCodeList = new ArrayList<>();


    private static final int OTP_LENGTH = 6;
    private static String GET_OTP = "";
    private static String ENTER_OTP = "";

    private static String USERID = "";

    private static boolean flag = false;
    private static boolean llFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        registerNo     = findViewById(R.id.registerNo);
        enteredOtp     = findViewById(R.id.enteredOtp);
        sendOtp        = findViewById(R.id.sendOtp);
        verifyOtp      = findViewById(R.id.verifyOtp);
        toolbar        = findViewById(R.id.Toolbar);
        resetPassword  = findViewById(R.id.resetPassword);
        selectCode     = findViewById(R.id.selectCode);
        llVerifyOtp    = findViewById(R.id.llVerifyOtp);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notes");
        }

        countryName();
        countryCode();
        CountryWithCode();
        resetPassword.setVisibility(View.INVISIBLE);
        llVerifyOtp.setVisibility(View.INVISIBLE);

        selectCode.setOnClickListener(v -> {
            // Initialize dialog
            dialog = new Dialog(forget_password.this);

            // set custom dialog
            dialog.setContentView(R.layout.country_name);

            // set custom height and width
            dialog.getWindow().setLayout(900,2100);

            //// set custom height and width through this
            /*WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 80% of screen width
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6); // 60% of screen height
            dialog.getWindow().setAttributes(layoutParams);*/

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            // Initialize and assign variable
            EditText editText = dialog.findViewById(R.id.edit_text);
            //ListView listView = dialog.findViewById(R.id.list_view);
            /*RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);*/

            // Initialize array adapter
            //ArrayAdapter<String> adapter = new ArrayAdapter<>(forget_password.this, android.R.layout.simple_list_item_1, countryWithCodeList);

            // set adapter
            //listView.setAdapter(adapter);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(forget_password.this));
            CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(forget_password.this, countryList);
            recyclerView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapter.setOnItemClickListener(country -> {
                String selectedCountry;
                selectedCountry = country;
                String countryName = "";
                for (int i = 0; i < selectedCountry.length(); i++) {
                    char ch = selectedCountry.charAt(i);
                    if (ch == '(' || ch == ')' || ch == '+' || ch == '-' || (ch >= '0' && ch <= '9')) {
                        continue;
                    }

                    else{
                        countryName = countryName + ch;
                    }
                }
                String code = getCountryCode(countryName);
                selectCode.setText(code);
                // Dismiss dialog
                dialog.dismiss();
            });

            /*listView.setOnItemClickListener((parent, view, position, id) -> {
                // when item selected from list
                // set selected item on textView
                String temp = adapter.getItem(position);
                String countryName = "";
                for (int i = 0; i < temp.length(); i++) {
                    char ch = temp.charAt(i);
                    if (ch == '(' || ch == ')' || ch == '+' || ch == '-' || (ch >= '0' && ch <= '9')) {
                        continue;
                    }

                    else{
                        countryName = countryName + ch;
                    }
                }
                String code = getCountryCode(countryName);
                selectCode.setText(code);
                // Dismiss dialog
                dialog.dismiss();
            });*/

            TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
            cancelDialog.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
        });
        sendOtp.setOnClickListener(v -> {

            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String Number = registerNo.getText().toString();
            String RegNum = pref.getString("ContactNo", "0000000000");
            String code = selectCode.getText().toString();
            Number = code + Number;
            USERID = pref.getString("Username", " ");
            SmsManager smsManager = SmsManager.getDefault();
            llFlag = true;

            if(RegNum.equals(Number)){
                GET_OTP = generateOTP();
                smsManager.sendTextMessage(RegNum, null, GET_OTP + " is the verification code to reset tour password ", null, null);
                Toast.makeText(this, "OTP Sent On Your Register Number", Toast.LENGTH_SHORT).show();
                if(llFlag){
                    llVerifyOtp.setVisibility(View.VISIBLE);
                }
            }

            else{
                Toast.makeText(this, "Please Enter Register Number", Toast.LENGTH_SHORT).show();
            }


        });

        verifyOtp.setOnClickListener(v -> {
            ENTER_OTP = enteredOtp.getText().toString();

            if(GET_OTP.equals(ENTER_OTP)){
                flag = true;

                if(flag){
                    resetPassword.setVisibility(View.VISIBLE);
                }

                EditText enterNewPassword   = findViewById(R.id.enterNewPassword);
                EditText confirmNewPassword = findViewById(R.id.confirmNewPassword);
                Button done                 = findViewById(R.id.done);

                done.setOnClickListener(v1 -> {
                    String newPassword = enterNewPassword.getText().toString();
                    String conPassword = confirmNewPassword.getText().toString();
                    if(newPassword.isEmpty() || conPassword.isEmpty()){
                        enterNewPassword.setText("");
                        confirmNewPassword.setText("");
                        Toast.makeText(this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    }
                    else if(!newPassword.equals(conPassword)){
                        enterNewPassword.setText("");
                        confirmNewPassword.setText("");
                        Toast.makeText(this, "New Password And Confirm Password Does Not Matched", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("Pass", newPassword);

                        Intent intent = new Intent(forget_password.this, login.class);
                        startActivity(intent);
                    }
                });
            }

            else{
                Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CountryWithCode() {
        for(int i = 0; i<countryList.size(); i++){
            countryWithCodeList.add(countryList.get(i) + "(" + countryCodeList.get(i) + ")");
        }
    }

    // Generate a random OTP
    public static String generateOTP() {
        // Characters that can be used in the OTP
        String allowedChars = "0123456789";

        // SecureRandom for generating random numbers
        SecureRandom random = new SecureRandom();

        // StringBuilder to store the generated OTP
        StringBuilder otpBuilder = new StringBuilder(OTP_LENGTH);

        // Generate random OTP characters and append them to the StringBuilder
        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char otpChar = allowedChars.charAt(randomIndex);
            otpBuilder.append(otpChar);
        }

        // Convert the StringBuilder to a String and return the OTP
        return otpBuilder.toString();
    }

    private void countryName(){
        countryList.add("Afghanistan");
        countryList.add("Albania");
        countryList.add("Algeria");
        countryList.add("Andorra");
        countryList.add("Angola");
        countryList.add("Antigua and Barbuda");
        countryList.add("Argentina");
        countryList.add("Armenia");
        countryList.add("Australia");
        countryList.add("Austria");
        countryList.add("Azerbaijan");
        countryList.add("Bahamas");
        countryList.add("Bahrain");
        countryList.add("Bangladesh");
        countryList.add("Barbados");
        countryList.add("Belarus");
        countryList.add("Belgium");
        countryList.add("Belize");
        countryList.add("Benin");
        countryList.add("Bhutan");
        countryList.add("Bolivia");
        countryList.add("Bosnia and Herzegovina");
        countryList.add("Botswana");
        countryList.add("Brazil");
        countryList.add("Brunei");
        countryList.add("Bulgaria");
        countryList.add("Burkina Faso");
        countryList.add("Burundi");
        countryList.add("Cabo Verde");
        countryList.add("Cambodia");
        countryList.add("Cameroon");
        countryList.add("Canada");
        countryList.add("Central African Republic");
        countryList.add("Chad");
        countryList.add("Chile");
        countryList.add("China");
        countryList.add("Colombia");
        countryList.add("Comoros");
        countryList.add("Congo (Brazzaville)");
        countryList.add("Congo (Kinshasa)");
        countryList.add("Costa Rica");
        countryList.add("Croatia");
        countryList.add("Cuba");
        countryList.add("Cyprus");
        countryList.add("Czech Republic");
        countryList.add("Denmark");
        countryList.add("Djibouti");
        countryList.add("Dominica");
        countryList.add("Dominican Republic");
        countryList.add("Ecuador");
        countryList.add("Egypt");
        countryList.add("El Salvador");
        countryList.add("Equatorial Guinea");
        countryList.add("Eritrea");
        countryList.add("Estonia");
        countryList.add("Eswatini");
        countryList.add("Ethiopia");
        countryList.add("Fiji");
        countryList.add("Finland");
        countryList.add("France");
        countryList.add("Gabon");
        countryList.add("Gambia");
        countryList.add("Georgia");
        countryList.add("Germany");
        countryList.add("Ghana");
        countryList.add("Greece");
        countryList.add("Grenada");
        countryList.add("Guatemala");
        countryList.add("Guinea");
        countryList.add("Guinea-Bissau");
        countryList.add("Guyana");
        countryList.add("Haiti");
        countryList.add("Holy See");
        countryList.add("Honduras");
        countryList.add("Hungary");
        countryList.add("Iceland");
        countryList.add("India");
        countryList.add("Indonesia");
        countryList.add("Iran");
        countryList.add("Iraq");
        countryList.add("Ireland");
        countryList.add("Israel");
        countryList.add("Italy");
        countryList.add("Jamaica");
        countryList.add("Japan");
        countryList.add("Jordan");
        countryList.add("Kazakhstan");
        countryList.add("Kenya");
        countryList.add("Kiribati");
        countryList.add("Korea, North");
        countryList.add("Korea, South");
        countryList.add("Kosovo");
        countryList.add("Kuwait");
        countryList.add("Kyrgyzstan");
        countryList.add("Laos");
        countryList.add("Latvia");
        countryList.add("Lebanon");
        countryList.add("Lesotho");
        countryList.add("Liberia");
        countryList.add("Libya");
        countryList.add("Liechtenstein");
        countryList.add("Lithuania");
        countryList.add("Luxembourg");
        countryList.add("Madagascar");
        countryList.add("Malawi");
        countryList.add("Malaysia");
        countryList.add("Maldives");
        countryList.add("Mali");
        countryList.add("Malta");
        countryList.add("Marshall Islands");
        countryList.add("Mauritania");
        countryList.add("Mauritius");
        countryList.add("Mexico");
        countryList.add("Micronesia");
        countryList.add("Moldova");
        countryList.add("Monaco");
        countryList.add("Mongolia");
        countryList.add("Montenegro");
        countryList.add("Morocco");
        countryList.add("Mozambique");
        countryList.add("Myanmar");
        countryList.add("Namibia");
        countryList.add("Nauru");
        countryList.add("Nepal");
        countryList.add("Netherlands");
        countryList.add("New Zealand");
        countryList.add("Nicaragua");
        countryList.add("Niger");
        countryList.add("Nigeria");
        countryList.add("North Macedonia");
        countryList.add("Norway");
        countryList.add("Oman");
        countryList.add("Pakistan");
        countryList.add("Palau");
        countryList.add("Panama");
        countryList.add("Papua New Guinea");
        countryList.add("Paraguay");
        countryList.add("Peru");
        countryList.add("Philippines");
        countryList.add("Poland");
        countryList.add("Portugal");
        countryList.add("Qatar");
        countryList.add("Romania");
        countryList.add("Russia");
        countryList.add("Rwanda");
        countryList.add("Saint Kitts and Nevis");
        countryList.add("Saint Lucia");
        countryList.add("Saint Vincent and the Grenadines");
        countryList.add("Samoa");
        countryList.add("San Marino");
        countryList.add("Sao Tome and Principe");
        countryList.add("Saudi Arabia");
        countryList.add("Senegal");
        countryList.add("Serbia");
        countryList.add("Seychelles");
        countryList.add("Sierra Leone");
        countryList.add("Singapore");
        countryList.add("Slovakia");
        countryList.add("Slovenia");
        countryList.add("Solomon Islands");
        countryList.add("Somalia");
        countryList.add("South Africa");
        countryList.add("South Sudan");
        countryList.add("Spain");
        countryList.add("Sri Lanka");
        countryList.add("Sudan");
        countryList.add("Suriname");
        countryList.add("Sweden");
        countryList.add("Switzerland");
        countryList.add("Syria");
        countryList.add("Taiwan");
        countryList.add("Tajikistan");
        countryList.add("Tanzania");
        countryList.add("Thailand");
        countryList.add("Timor-Leste");
        countryList.add("Togo");
        countryList.add("Tonga");
        countryList.add("Trinidad and Tobago");
        countryList.add("Tunisia");
        countryList.add("Turkey");
        countryList.add("Turkmenistan");
        countryList.add("Tuvalu");
        countryList.add("Uganda");
        countryList.add("Ukraine");
        countryList.add("United Arab Emirates");
        countryList.add("United Kingdom");
        countryList.add("United States of America");
        countryList.add("Uruguay");
        countryList.add("Uzbekistan");
        countryList.add("Vanuatu");
        countryList.add("Venezuela");
        countryList.add("Vietnam");
        countryList.add("Yemen");
        countryList.add("Zambia");
        countryList.add("Zimbabwe");

    }

    private void countryCode(){
        countryCodeList = new ArrayList<>();

        countryCodeList.add("+93"); // Afghanistan
        countryCodeList.add("+355"); // Albania
        countryCodeList.add("+213"); // Algeria
        countryCodeList.add("+376"); // Andorra
        countryCodeList.add("+244"); // Angola
        countryCodeList.add("+1-268"); // Antigua and Barbuda
        countryCodeList.add("+54"); // Argentina
        countryCodeList.add("+374"); // Armenia
        countryCodeList.add("+61"); // Australia
        countryCodeList.add("+43"); // Austria
        countryCodeList.add("+994"); // Azerbaijan
        countryCodeList.add("+1-242"); // Bahamas
        countryCodeList.add("+973"); // Bahrain
        countryCodeList.add("+880"); // Bangladesh
        countryCodeList.add("+1-246"); // Barbados
        countryCodeList.add("+375"); // Belarus
        countryCodeList.add("+32"); // Belgium
        countryCodeList.add("+501"); // Belize
        countryCodeList.add("+229"); // Benin
        countryCodeList.add("+975"); // Bhutan
        countryCodeList.add("+591"); // Bolivia
        countryCodeList.add("+387"); // Bosnia and Herzegovina
        countryCodeList.add("+267"); // Botswana
        countryCodeList.add("+55"); // Brazil
        countryCodeList.add("+673"); // Brunei
        countryCodeList.add("+359"); // Bulgaria
        countryCodeList.add("+226"); // Burkina Faso
        countryCodeList.add("+257"); // Burundi
        countryCodeList.add("+238"); // Cabo Verde
        countryCodeList.add("+855"); // Cambodia
        countryCodeList.add("+237"); // Cameroon
        countryCodeList.add("+1"); // Canada
        countryCodeList.add("+236"); // Central African Republic
        countryCodeList.add("+235"); // Chad
        countryCodeList.add("+56"); // Chile
        countryCodeList.add("+86"); // China
        countryCodeList.add("+57"); // Colombia
        countryCodeList.add("+269"); // Comoros
        countryCodeList.add("+242"); // Congo (Brazzaville)
        countryCodeList.add("+243"); // Congo (Kinshasa)
        countryCodeList.add("+506"); // Costa Rica
        countryCodeList.add("+385"); // Croatia
        countryCodeList.add("+53"); // Cuba
        countryCodeList.add("+357"); // Cyprus
        countryCodeList.add("+420"); // Czech Republic
        countryCodeList.add("+45"); // Denmark
        countryCodeList.add("+253"); // Djibouti
        countryCodeList.add("+1-767"); // Dominica
        countryCodeList.add("+1-809"); // Dominican Republic
        countryCodeList.add("+593"); // Ecuador
        countryCodeList.add("+20"); // Egypt
        countryCodeList.add("+503"); // El Salvador
        countryCodeList.add("+240"); // Equatorial Guinea
        countryCodeList.add("+291"); // Eritrea
        countryCodeList.add("+372"); // Estonia
        countryCodeList.add("+268"); // Eswatini
        countryCodeList.add("+251"); // Ethiopia
        countryCodeList.add("+679"); // Fiji
        countryCodeList.add("+358"); // Finland
        countryCodeList.add("+33"); // France
        countryCodeList.add("+241"); // Gabon
        countryCodeList.add("+220"); // Gambia
        countryCodeList.add("+995"); // Georgia
        countryCodeList.add("+49"); // Germany
        countryCodeList.add("+233"); // Ghana
        countryCodeList.add("+30"); // Greece
        countryCodeList.add("+1-473"); // Grenada
        countryCodeList.add("+502"); // Guatemala
        countryCodeList.add("+224"); // Guinea
        countryCodeList.add("+245"); // Guinea-Bissau
        countryCodeList.add("+592"); // Guyana
        countryCodeList.add("+509"); // Haiti
        countryCodeList.add("+39-06"); // Holy See
        countryCodeList.add("+504"); // Honduras
        countryCodeList.add("+36"); // Hungary
        countryCodeList.add("+354"); // Iceland
        countryCodeList.add("+91"); // India
        countryCodeList.add("+62"); // Indonesia
        countryCodeList.add("+98"); // Iran
        countryCodeList.add("+964"); // Iraq
        countryCodeList.add("+353"); // Ireland
        countryCodeList.add("+972"); // Israel
        countryCodeList.add("+39"); // Italy
        countryCodeList.add("+1-876"); // Jamaica
        countryCodeList.add("+81"); // Japan
        countryCodeList.add("+962"); // Jordan
        countryCodeList.add("+7"); // Kazakhstan
        countryCodeList.add("+254"); // Kenya
        countryCodeList.add("+686"); // Kiribati
        countryCodeList.add("+850"); // Korea, North
        countryCodeList.add("+82"); // Korea, South
        countryCodeList.add("+383"); // Kosovo
        countryCodeList.add("+965"); // Kuwait
        countryCodeList.add("+996"); // Kyrgyzstan
        countryCodeList.add("+856"); // Laos
        countryCodeList.add("+371"); // Latvia
        countryCodeList.add("+961"); // Lebanon
        countryCodeList.add("+266"); // Lesotho
        countryCodeList.add("+231"); // Liberia
        countryCodeList.add("+218"); // Libya
        countryCodeList.add("+423"); // Liechtenstein
        countryCodeList.add("+370"); // Lithuania
        countryCodeList.add("+352"); // Luxembourg
        countryCodeList.add("+261"); // Madagascar
        countryCodeList.add("+265"); // Malawi
        countryCodeList.add("+60"); // Malaysia
        countryCodeList.add("+960"); // Maldives
        countryCodeList.add("+223"); // Mali
        countryCodeList.add("+356"); // Malta
        countryCodeList.add("+692"); // Marshall Islands
        countryCodeList.add("+222"); // Mauritania
        countryCodeList.add("+230"); // Mauritius
        countryCodeList.add("+52"); // Mexico
        countryCodeList.add("+691"); // Micronesia
        countryCodeList.add("+373"); // Moldova
        countryCodeList.add("+377"); // Monaco
        countryCodeList.add("+976"); // Mongolia
        countryCodeList.add("+382"); // Montenegro
        countryCodeList.add("+212"); // Morocco
        countryCodeList.add("+258"); // Mozambique
        countryCodeList.add("+95"); // Myanmar
        countryCodeList.add("+264"); // Namibia
        countryCodeList.add("+674"); // Nauru
        countryCodeList.add("+977"); // Nepal
        countryCodeList.add("+31"); // Netherlands
        countryCodeList.add("+64"); // New Zealand
        countryCodeList.add("+505"); // Nicaragua
        countryCodeList.add("+227"); // Niger
        countryCodeList.add("+234"); // Nigeria
        countryCodeList.add("+389"); // North Macedonia
        countryCodeList.add("+47"); // Norway
        countryCodeList.add("+968"); // Oman
        countryCodeList.add("+92"); // Pakistan
        countryCodeList.add("+680"); // Palau
        countryCodeList.add("+507"); // Panama
        countryCodeList.add("+675"); // Papua New Guinea
        countryCodeList.add("+595"); // Paraguay
        countryCodeList.add("+51"); // Peru
        countryCodeList.add("+63"); // Philippines
        countryCodeList.add("+48"); // Poland
        countryCodeList.add("+351"); // Portugal
        countryCodeList.add("+974"); // Qatar
        countryCodeList.add("+40"); // Romania
        countryCodeList.add("+7"); // Russia
        countryCodeList.add("+250"); // Rwanda
        countryCodeList.add("+1-869"); // Saint Kitts and Nevis
        countryCodeList.add("+1-758"); // Saint Lucia
        countryCodeList.add("+1-784"); // Saint Vincent and the Grenadines
        countryCodeList.add("+685"); // Samoa
        countryCodeList.add("+378"); // San Marino
        countryCodeList.add("+239"); // Sao Tome and Principe
        countryCodeList.add("+966"); // Saudi Arabia
        countryCodeList.add("+221"); // Senegal
        countryCodeList.add("+381"); // Serbia
        countryCodeList.add("+248"); // Seychelles
        countryCodeList.add("+232"); // Sierra Leone
        countryCodeList.add("+65"); // Singapore
        countryCodeList.add("+421"); // Slovakia
        countryCodeList.add("+386"); // Slovenia
        countryCodeList.add("+677"); // Solomon Islands
        countryCodeList.add("+252"); // Somalia
        countryCodeList.add("+27"); // South Africa
        countryCodeList.add("+211"); // South Sudan
        countryCodeList.add("+34"); // Spain
        countryCodeList.add("+94"); // Sri Lanka
        countryCodeList.add("+249"); // Sudan
        countryCodeList.add("+597"); // Suriname
        countryCodeList.add("+46"); // Sweden
        countryCodeList.add("+41"); // Switzerland
        countryCodeList.add("+963"); // Syria
        countryCodeList.add("+886"); // Taiwan
        countryCodeList.add("+992"); // Tajikistan
        countryCodeList.add("+255"); // Tanzania
        countryCodeList.add("+66"); // Thailand
        countryCodeList.add("+670"); // Timor-Leste
        countryCodeList.add("+228"); // Togo
        countryCodeList.add("+676"); // Tonga
        countryCodeList.add("+1-868"); // Trinidad and Tobago
        countryCodeList.add("+216"); // Tunisia
        countryCodeList.add("+90"); // Turkey
        countryCodeList.add("+993"); // Turkmenistan
        countryCodeList.add("+688"); // Tuvalu
        countryCodeList.add("+256"); // Uganda
        countryCodeList.add("+380"); // Ukraine
        countryCodeList.add("+971"); // United Arab Emirates
        countryCodeList.add("+44"); // United Kingdom
        countryCodeList.add("+1"); // United States of America
        countryCodeList.add("+598"); // Uruguay
        countryCodeList.add("+998"); // Uzbekistan
        countryCodeList.add("+678"); // Vanuatu
        countryCodeList.add("+58"); // Venezuela
        countryCodeList.add("+84"); // Vietnam
        countryCodeList.add("+967"); // Yemen
        countryCodeList.add("+260"); // Zambia
        countryCodeList.add("+263"); // Zimbabwe

    }

    private String getCountryCode(String countryName) {
        switch (countryName) {
            case "Afghanistan":
                return "+93";
            case "Albania":
                return "+355";
            case "Algeria":
                return "+213";
            case "Andorra":
                return "+376";
            case "Angola":
                return "+244";
            case "Antigua and Barbuda":
                return "+1-268";
            case "Argentina":
                return "+54";
            case "Armenia":
                return "+374";
            case "Australia":
                return "+61";
            case "Austria":
                return "+43";
            case "Azerbaijan":
                return "+994";
            case "Bahamas":
                return "+1-242";
            case "Bahrain":
                return "+973";
            case "Bangladesh":
                return "+880";
            case "Barbados":
                return "+1-246";
            case "Belarus":
                return "+375";
            case "Belgium":
                return "+32";
            case "Belize":
                return "+501";
            case "Benin":
                return "+229";
            case "Bhutan":
                return "+975";
            case "Bolivia":
                return "+591";
            case "Bosnia and Herzegovina":
                return "+387";
            case "Botswana":
                return "+267";
            case "Brazil":
                return "+55";
            case "Brunei":
                return "+673";
            case "Bulgaria":
                return "+359";
            case "Burkina Faso":
                return "+226";
            case "Burundi":
                return "+257";
            case "Cabo Verde":
                return "+238";
            case "Cambodia":
                return "+855";
            case "Cameroon":
                return "+237";
            case "Canada":
                return "+1";
            case "Central African Republic":
                return "+236";
            case "Chad":
                return "+235";
            case "Chile":
                return "+56";
            case "China":
                return "+86";
            case "Colombia":
                return "+57";
            case "Comoros":
                return "+269";
            case "Congo (Brazzaville)":
                return "+242";
            case "Congo (Kinshasa)":
                return "+243";
            case "Costa Rica":
                return "+506";
            case "Croatia":
                return "+385";
            case "Cuba":
                return "+53";
            case "Cyprus":
                return "+357";
            case "Czech Republic":
                return "+420";
            case "Denmark":
                return "+45";
            case "Djibouti":
                return "+253";
            case "Dominica":
                return "+1-767";
            case "Dominican Republic":
                return "+1-809, +1-829, +1-849";
            case "Ecuador":
                return "+593";
            case "Egypt":
                return "+20";
            case "El Salvador":
                return "+503";
            case "Equatorial Guinea":
                return "+240";
            case "Eritrea":
                return "+291";
            case "Estonia":
                return "+372";
            case "Eswatini":
                return "+268";
            case "Ethiopia":
                return "+251";
            case "Fiji":
                return "+679";
            case "Finland":
                return "+358";
            case "France":
                return "+33";
            case "Gabon":
                return "+241";
            case "Gambia":
                return "+220";
            case "Georgia":
                return "+995";
            case "Germany":
                return "+49";
            case "Ghana":
                return "+233";
            case "Greece":
                return "+30";
            case "Grenada":
                return "+1-473";
            case "Guatemala":
                return "+502";
            case "Guinea":
                return "+224";
            case "Guinea-Bissau":
                return "+245";
            case "Guyana":
                return "+592";
            case "Haiti":
                return "+509";
            case "Holy See":
                return "+39-06";
            case "Honduras":
                return "+504";
            case "Hungary":
                return "+36";
            case "Iceland":
                return "+354";
            case "India":
                return "+91";
            case "Indonesia":
                return "+62";
            case "Iran":
                return "+98";
            case "Iraq":
                return "+964";
            case "Ireland":
                return "+353";
            case "Israel":
                return "+972";
            case "Italy":
                return "+39";
            case "Jamaica":
                return "+1-876";
            case "Japan":
                return "+81";
            case "Jordan":
                return "+962";
            case "Kazakhstan":
                return "+7";
            case "Kenya":
                return "+254";
            case "Kiribati":
                return "+686";
            case "Korea, North":
                return "+850";
            case "Korea, South":
                return "+82";
            case "Kosovo":
                return "+383";
            case "Kuwait":
                return "+965";
            case "Kyrgyzstan":
                return "+996";
            case "Laos":
                return "+856";
            case "Latvia":
                return "+371";
            case "Lebanon":
                return "+961";
            case "Lesotho":
                return "+266";
            case "Liberia":
                return "+231";
            case "Libya":
                return "+218";
            case "Liechtenstein":
                return "+423";
            case "Lithuania":
                return "+370";
            case "Luxembourg":
                return "+352";
            case "Madagascar":
                return "+261";
            case "Malawi":
                return "+265";
            case "Malaysia":
                return "+60";
            case "Maldives":
                return "+960";
            case "Mali":
                return "+223";
            case "Malta":
                return "+356";
            case "Marshall Islands":
                return "+692";
            case "Mauritania":
                return "+222";
            case "Mauritius":
                return "+230";
            case "Mexico":
                return "+52";
            case "Micronesia":
                return "+691";
            case "Moldova":
                return "+373";
            case "Monaco":
                return "+377";
            case "Mongolia":
                return "+976";
            case "Montenegro":
                return "+382";
            case "Morocco":
                return "+212";
            case "Mozambique":
                return "+258";
            case "Myanmar":
                return "+95";
            case "Namibia":
                return "+264";
            case "Nauru":
                return "+674";
            case "Nepal":
                return "+977";
            case "Netherlands":
                return "+31";
            case "New Zealand":
                return "+64";
            case "Nicaragua":
                return "+505";
            case "Niger":
                return "+227";
            case "Nigeria":
                return "+234";
            case "North Macedonia":
                return "+389";
            case "Norway":
                return "+47";
            case "Oman":
                return "+968";
            case "Pakistan":
                return "+92";
            case "Palau":
                return "+680";
            case "Panama":
                return "+507";
            case "Papua New Guinea":
                return "+675";
            case "Paraguay":
                return "+595";
            case "Peru":
                return "+51";
            case "Philippines":
                return "+63";
            case "Poland":
                return "+48";
            case "Portugal":
                return "+351";
            case "Qatar":
                return "+974";
            case "Romania":
                return "+40";
            case "Russia":
                return "+7";
            case "Rwanda":
                return "+250";
            case "Saint Kitts and Nevis":
                return "+1-869";
            case "Saint Lucia":
                return "+1-758";
            case "Saint Vincent and the Grenadines":
                return "+1-784";
            case "Samoa":
                return "+685";
            case "San Marino":
                return "+378";
            case "Sao Tome and Principe":
                return "+239";
            case "Saudi Arabia":
                return "+966";
            case "Senegal":
                return "+221";
            case "Serbia":
                return "+381";
            case "Seychelles":
                return "+248";
            case "Sierra Leone":
                return "+232";
            case "Singapore":
                return "+65";
            case "Slovakia":
                return "+421";
            case "Slovenia":
                return "+386";
            case "Solomon Islands":
                return "+677";
            case "Somalia":
                return "+252";
            case "South Africa":
                return "+27";
            case "South Sudan":
                return "+211";
            case "Spain":
                return "+34";
            case "Sri Lanka":
                return "+94";
            case "Sudan":
                return "+249";
            case "Suriname":
                return "+597";
            case "Sweden":
                return "+46";
            case "Switzerland":
                return "+41";
            case "Syria":
                return "+963";
            case "Taiwan":
                return "+886";
            case "Tajikistan":
                return "+992";
            case "Tanzania":
                return "+255";
            case "Thailand":
                return "+66";
            case "Timor-Leste":
                return "+670";
            case "Togo":
                return "+228";
            case "Tonga":
                return "+676";
            case "Trinidad and Tobago":
                return "+1-868";
            case "Tunisia":
                return "+216";
            case "Turkey":
                return "+90";
            case "Turkmenistan":
                return "+993";
            case "Tuvalu":
                return "+688";
            case "Uganda":
                return "+256";
            case "Ukraine":
                return "+380";
            case "United Arab Emirates":
                return "+971";
            case "United Kingdom":
                return "+44";
            case "United States of America":
                return "+1";
            case "Uruguay":
                return "+598";
            case "Uzbekistan":
                return "+998";
            case "Vanuatu":
                return "+678";
            case "Venezuela":
                return "+58";
            case "Vietnam":
                return "+84";
            case "Yemen":
                return "+967";
            case "Zambia":
                return "+260";
            case "Zimbabwe":
                return "+263";
            default:
                return "";
        }

    }

}