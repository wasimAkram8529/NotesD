package com.example.notesd;

import androidx.annotation.NonNull;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class NewUser extends AppCompatActivity {

    Spinner spinner;
    TextView codeId, selectCountry;

    EditText contactEditText;

    EditText userId, password, email, contactNo;
    Button btnSubmit;

    Toolbar toolbar;

    Dialog dialog;
    private String selectedCountry = "";

    ArrayList<String> countryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userId    = findViewById(R.id.userId);
        password  = findViewById(R.id.password);
        btnSubmit = findViewById(R.id.btnSubmit);
        email     = findViewById(R.id.email);
        contactNo = findViewById(R.id.contactNo);
        toolbar   = findViewById(R.id.Toolbar);
        //spinner   = findViewById(R.id.spinner);
        codeId    = findViewById(R.id.codeId);
        selectCountry = findViewById(R.id.selectCountry);


        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notes");
        }

        countryName();

        selectCountry.setOnClickListener(v -> {
            // Initialize dialog
            dialog = new Dialog(NewUser.this);

            // set custom dialog
            dialog.setContentView(R.layout.country_name);

            // set custom height and width
            dialog.getWindow().setLayout(900,2100);

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
            //ArrayAdapter<String> adapter = new ArrayAdapter<>(NewUser.this, android.R.layout.simple_list_item_1, countryList);

            // set adapter
            //listView.setAdapter(adapter);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(NewUser.this));
            CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(NewUser.this, countryList);
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

            // Set item click listener for the RecyclerView
            adapter.setOnItemClickListener(country -> {
                selectCountry.setText(country);
                selectedCountry = country;
                String code = getCountryCode(selectedCountry);
                codeId.setText(code);
                dialog.dismiss();
            });

            /*listView.setOnItemClickListener((parent, view, position, id) -> {
                // when item selected from list
                // set selected item on textView
                selectCountry.setText(adapter.getItem(position));
                selectedCountry = adapter.getItem(position);
                String code = getCountryCode(selectedCountry);
                codeId.setText(code);
                // Dismiss dialog
                dialog.dismiss();
            });*/

            TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
            cancelDialog.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

        });

        btnSubmit.setOnClickListener(v -> {
            String Email     = email.getText().toString();
            String ContactNo = contactNo.getText().toString();
            String Username  = userId.getText().toString();
            String passWord  = password.getText().toString();
            String countryCode = getCountryCode(selectedCountry);
            String newContact = countryCode+ContactNo;

            if(!Username.isEmpty() && !passWord.isEmpty() && (!Email.isEmpty() && Email.contains("@gmail.com") && Email.length() > 10) && (!ContactNo.isEmpty() && ContactNo.length() == 10)){
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                editor.putString("Username", Username);
                editor.putString("Pass", passWord);
                editor.putString("ContactNo", newContact);
                editor.putString("Email", Email);

                editor.apply();

                Intent intent = new Intent(NewUser.this, login.class);
                startActivity(intent);
            }

            else if(Email.isEmpty() || !Email.contains("@gmail.com") || Email.length() < 10 || Email.length() == 10){
                Toast.makeText(NewUser.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
            }

            else if(ContactNo.isEmpty() || ContactNo.length() < 10 || ContactNo.length() > 10){
                Toast.makeText(NewUser.this, "Please Enter A Valid Contact No", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(NewUser.this, "Please Enter User Id and Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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