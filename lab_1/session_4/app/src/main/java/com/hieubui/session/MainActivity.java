package com.hieubui.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinnerTitles;

    private final EmployeeAdapter employeeAdapter = new EmployeeAdapter();

    private ArrayAdapter<String> titleAdapter;

    private final List<Employee> employees = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponents();
    }

    private void setupComponents() {
        spinnerTitles = findViewById(R.id.spinner_titles);
        RecyclerView recyclerEmployee = findViewById(R.id.recycler_employees);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        titleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());

        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTitles.setAdapter(titleAdapter);

        recyclerEmployee.setHasFixedSize(true);
        recyclerEmployee.addItemDecoration(dividerItemDecoration);
        recyclerEmployee.setAdapter(employeeAdapter);

        setEvents();
    }

    private void setEvents() {
        Button btnDOM = findViewById(R.id.btn_dom);
        Button btnSAX = findViewById(R.id.btn_sax);

        btnDOM.setOnClickListener(this);

        btnSAX.setOnClickListener(this);

        spinnerTitles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                filterEmployee(titleAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String type = ((Button) view).getText().toString();

        getData(type);
    }

    private void getData(@NonNull String type) {
        try {
            InputStream inputStream = getAssets().open("employees.xml");

            titleAdapter.clear();
            employees.clear();
            switch (type) {
                case "DOM":
                    getDataByDOM(inputStream, employees);
                    break;

                case "SAX":
                    getDataBySAX(inputStream, employees);
                    break;
            }
            titleAdapter.add("");
            titleAdapter.addAll(employees.stream()
                .map(Employee::getTitle)
                .distinct()
                .collect(Collectors.toList())
            );
            filterEmployee(titleAdapter.getItem(spinnerTitles.getSelectedItemPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataByDOM(InputStream inputStream, List<Employee> employees) throws ParserConfigurationException, IOException, SAXException {
        NodeList nodes = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(inputStream)
            .getDocumentElement()
            .getChildNodes();
        int size = nodes.getLength();
        Node node;
        Element element;
        Employee employee;

        for (int i = 0; i < size; i++) {
            node = nodes.item(i);
            if (node instanceof Element) {
                element = (Element) node;
                employee = new Employee();

                employee.setEmployeeId(element.getAttribute("id"));
                employee.setTitle(element.getAttribute("title"));
                employee.setName(element.getElementsByTagName("name").item(0).getTextContent());
                employee.setPhone(element.getElementsByTagName("phone").item(0).getTextContent());
                employees.add(employee);
            }
        }
    }

    private void getDataBySAX(InputStream inputStream, List<Employee> employees) throws XmlPullParserException, IOException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        int eventType;
        String nodeName;
        Employee employee = null;

        parser.setInput(inputStream, "UTF-8");
        do {
            eventType = parser.next();
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                case XmlPullParser.END_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:
                    nodeName = parser.getName();
                    switch (nodeName) {
                        case "employee":
                            employee = new Employee();

                            employee.setEmployeeId(parser.getAttributeValue(0));
                            employee.setTitle(parser.getAttributeValue(1));
                            break;

                        case "name":
                            Objects.requireNonNull(employee).setName(parser.nextText());
                            break;

                        case "phone":
                            Objects.requireNonNull(employee).setPhone(parser.nextText());
                            break;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("employee")) {
                        employees.add(employee);
                    }
                    break;
            }
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }

    private void filterEmployee(String title) {
        List<Employee> data = MainActivity.this.employees;
        List<Employee> employees = title.isEmpty() ? data : data.stream()
            .filter(employee -> employee.getTitle().equals(title))
            .collect(Collectors.toList());

        employeeAdapter.setEmployees(employees);
    }
}