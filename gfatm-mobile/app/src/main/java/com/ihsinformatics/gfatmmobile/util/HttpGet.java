package com.ihsinformatics.gfatmmobile.util;

import android.content.Context;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.model.Concept;
import com.ihsinformatics.gfatmmobile.model.EncounterType;
import com.ihsinformatics.gfatmmobile.model.LabOrder;
import com.ihsinformatics.gfatmmobile.model.Location;
import com.ihsinformatics.gfatmmobile.model.PersonAttributeType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.config.Registry;
import cz.msebera.android.httpclient.config.RegistryBuilder;
import cz.msebera.android.httpclient.conn.socket.ConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.socket.PlainConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.SSLConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.TrustStrategy;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.impl.conn.PoolingHttpClientConnectionManager;
import cz.msebera.android.httpclient.ssl.SSLContextBuilder;

/**
 * Created by Haris on 11/30/2016.
 */
public class HttpGet {

    private static final String TAG = "HttpsClient";
    private static final String PERSON_RESOURCE = "person";
    private static final String PATIENT_RESOURCE = "patient";
    private static final String ATTRIBUTE = "attribute";
    private static final String ADDRESS = "address";
    private static final String PROGRAM_RESOURCE = "program";
    private static final String CONCEPT_RESOURCE = "concept";
    private static final String ENCOUNTER_RESOURCE = "encounter";
    private static final String LOCATION_RESOURCE = "location";
    private static final String USER_RESOURCE = "user";
    private static final String PRIVILEGE_RESOURCE = "privelge";
    private static final String ROLE_RESOURCE = "roles";
    private static final String PATIENT_IDENTIFIER_TYPE_RESOURCE = "patientidentifiertype";
    private static final String IDENTIFIER_TYPE_RESOURCE = "patientidentifiertype";
    private static final String PERSON_ATTRIBUTE_TYPE_RESOURCE = "personattributetype";
    private static final String ENCOUNTER_TYPE_RESOURCE = "encountertype";
    private static final String PROVIDER_RESOURCE = "provider";
    private static final String OBSERVATION_RESOURCE = "obs";
    private static final String PROVIDER_ATTRIBUTE_TYPE_RESOURCE = "providerattributetype";
    private static final String LOCATION_ATTRIBUTE_TYPE_RESOURCE = "locationattributetype";
    private static final String RELATIONSHIP_RESOURCE = "relationship";
    private static final String LOCATION_TAG = "locationtag";
    private static final String ENCOUNTER_TYPE_PARAM = "encounterType";
    private static final String UUID = "uuid";
    private static String COMMON_LAB_ORDER = "commonlab/labtestorder";
    Properties properties = new Properties();
    private boolean early = true;
    private String serverAdress = "";
    private Context context = null;
    private String tag = "HTTPGet";


    public HttpGet(String serverIP, String port, Context context) {
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        serverAdress = serverIP;
        if (!(port == null || port.equals(""))) {
            serverAdress += ":" + port;
        }
    }

    private JSONObject get(String requestUri) {
        JSONObject result = null;
        HttpUriRequest request = null;
        String responseString = "";
        String auth = "";
        HttpResponse response = null;

        try {
            request = new cz.msebera.android.httpclient.client.methods.HttpGet(requestUri);
            auth = Base64.encodeToString(
                    (App.getUsername() + ":" + App.getPassword()).getBytes("UTF-8"),
                    Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + auth);
            if (App.getSsl().equalsIgnoreCase("Enabled")) {
                HttpClient client =  createHttpClient_AcceptsUntrustedCerts();
                response = client.execute(request);
            } else {
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(request);
            }
            StatusLine statusLine = response.getStatusLine();
            Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                result = JSONParser.getJSONObject(responseString);
            } else {
                response.getEntity().getContent().close();
            }
        } catch (Exception e) {
            Log.e(tag, "get");
        }
        return result;
    }

    private JSONObject get(String requestUri, boolean condition, String resourceName) {
        JSONObject obj = get(requestUri);
        JSONObject resultObject = null;
        final String splitKey = "openmrsapi";
        List<String> newArr = new ArrayList<String>();
        try {
            JSONArray arrJson = obj.getJSONArray("results");
            if (arrJson.length() == 0) {
                return null;
            } else if (arrJson.length() == 1) {
                JSONObject object = arrJson.getJSONObject(0);
                String uuid = object.getString("uuid");
                if (resourceName.equals(ENCOUNTER_RESOURCE))
                    return getEncounterByUuid(uuid);
                else
                    return getObservationByUuid(uuid);
            } else {
                for (int i = 0; i < arrJson.length(); i++) {
                    JSONObject newObj = arrJson.getJSONObject(i);
                    String date = newObj.getJSONObject("auditInfo").getString("dateCreated");
                    String uuid = newObj.getString("uuid");
                    date = date.replace("T", " ");
                    String[] arr = date.split("\\+");
                    newArr.add(arr[0] + splitKey + newObj.toString());
                }
                Collections.sort(newArr, new Comparator<String>() {
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                    @Override
                    public int compare(String o1, String o2) {
                        try {
                            String[] b1 = o1.split(splitKey);
                            String[] b2 = o2.split(splitKey);
                            return f.parse(b1[0]).compareTo(f.parse(b2[0]));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                if (condition) {
                    String[] splitter = newArr.get(0).split(splitKey);
                    if (resourceName.equals(ENCOUNTER_RESOURCE)) {
                        resultObject = JSONParser.getJSONObject(splitter[1]);
                        return resultObject;
                    } else {
                        resultObject = JSONParser.getJSONObject(splitter[1]);
                        return resultObject;
                    }
                } else {
                    String[] splitter = newArr.get(newArr.size() - 1).split(splitKey);
                    if (resourceName.equals(ENCOUNTER_RESOURCE)) {
                        resultObject = JSONParser.getJSONObject(splitter[1]);
                        return resultObject;
                    } else {
                        resultObject = JSONParser.getJSONObject(splitter[1]);
                        return resultObject;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(tag, "get");
        }
        return null;
    }

    private JSONObject getJsonObjectByName(String resourceName, String content) {
        if (content == null)
            return null;

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        content = content.trim();
        content = content.replaceAll(" ", "%20");
        String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?q=" + content + "&v=full";
        return get(requestUri);
    }

    private JSONObject getJsonObject(String resourceName, String parameter, String content) {
        if (content == null)
            return null;

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        content = content.trim();
        content = content.replaceAll(" ", "%20");
        String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "/" + parameter + "/" + content;
        return get(requestUri);
    }

    private JSONArray getJsonArray(String resourceName, String parameter, String content) {
        JSONArray jsonArray  = null;

        if (content == null)
            return null;

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        content = content.trim();
        content = content.replaceAll(" ", "%20");
        try {
            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "/" + parameter + "/" + content;
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        }catch (Exception e) {
            Log.e(tag, "getJsonArray");
        }
        return jsonArray;
    }

    private JSONObject getJsonObjectByUuid(String resourceName, String content) {
        if (content == null)
            return null;

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        content = content.trim();
        String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                "/" + content + "?v=full";
        return get(requestUri);
    }

    private JSONObject getCustomJsonObjectByUuid(String resourceName, String uuid, String variables) {
        JSONObject jsonObject = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "/" + uuid +"?v=custom:(" + variables + ")";
            jsonObject = get(requestUri);
        } catch (Exception e) {
            Log.e(tag, "getCustomJsonArray");
        }
        return jsonObject;
    }

    private JSONObject getCustomJsonObject(String resourceName, String param1, String param2, String param3, boolean condition) {
        if (param1 == null || param2 == null)
            return null;

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        param1 = param1.trim();
        param2 = param2.trim();
        String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                "?patient=" + param1 + "&v=full&" + param3 + "=" + param2;
        return get(requestUri, condition, resourceName);
    }

    private JSONArray getCustomJsonArray(String resourceName, String variables, String value) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?v=custom:(" + variables + ")&q=" + value + "";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getCustomJsonArray");
        }
        return jsonArray;
    }

    private JSONArray getCustomJsonArray(String resourceName, String variables) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?v=custom:(" + variables + ")";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getCustomJsonArray");
        }
        return jsonArray;
    }

    private JSONArray getCustomJsonArray(String resourceName, String variables, String param1, String param2) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?v=custom:(" + variables + ")" + "&" + param1 + "=" + param2;
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getCustomJsonArray");
        }
        return jsonArray;
    }

    private JSONArray getJSONArray(String resourceName) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?v=full";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getJSONArray");
        }
        return jsonArray;
    }

    private JSONArray getJSONArray(String resourceName, String param1, String param2, String searchResource) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                    "?patient=" + param1 + "&v=full&" + searchResource + "=" + param2;
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getJSONArray");
        }
        return jsonArray;
    }

    private JSONArray getJSONArray(String resourceName, String searchParam, String content) {
        JSONArray jsonArray = null;
        try {

            String http = "";
            if (App.getSsl().equalsIgnoreCase("Enabled"))
                http = "https://";
            else
                http = "http://";

            String requestUri = http + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                    "?" + searchParam + "=" + content + "&v=full";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            Log.e(tag, "getJSONArray");
        }
        return jsonArray;
    }

    public JSONObject getPersonByUuid(String content) {
        return getJsonObjectByUuid(PERSON_RESOURCE, content);
    }

    public JSONObject getPersonByName(String content) {
        return getJsonObjectByName(PERSON_RESOURCE, content);
    }

    public JSONObject getUserByName(String content) {
        return getJsonObjectByName(USER_RESOURCE, content);
    }

    public JSONObject getEncountersByPatientId(String content) {
        return getJsonObjectByName(ENCOUNTER_RESOURCE, content);
    }

    public JSONObject getPatientByName(String content) {
        return getJsonObjectByName(PATIENT_RESOURCE, content);
    }

    public JSONObject getPatientByUuid(String content) {
        return getJsonObjectByUuid(PATIENT_RESOURCE, content);
    }

    public JSONObject getProgramByUuid(String content) {
        return getJsonObjectByUuid(PROGRAM_RESOURCE, content);
    }

    public JSONObject getProgramByName(String content) {
        return getJsonObjectByName(PROGRAM_RESOURCE, content);
    }

    public JSONObject getConceptByName(String content) {
        return getJsonObjectByName(CONCEPT_RESOURCE, content);
    }

    public JSONObject getConceptByUuid(String content) {
        return getJsonObjectByUuid(CONCEPT_RESOURCE, content);
    }

    public JSONObject getConceptByConceptId(String content) {
        return getJsonObjectByName(CONCEPT_RESOURCE, content);
    }

    public JSONObject getLocationByName(String content) {
        return getJsonObjectByName(LOCATION_RESOURCE, content);
    }

    public JSONObject getLocationByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_RESOURCE, content);
    }

    public JSONObject getEncounterByUuid(String content) {
        return getJsonObjectByUuid(ENCOUNTER_RESOURCE, content);
    }

    public JSONObject getUserByUuid(String content) {
        return getJsonObjectByUuid(USER_RESOURCE, content);
    }

    public JSONObject getRoleByUuid(String content) {
        return getJsonObjectByUuid(ROLE_RESOURCE, content);
    }

    public JSONObject getPrivilegeByUuid(String content) {
        return getJsonObjectByUuid(PRIVILEGE_RESOURCE, content);
    }

    public JSONObject getIdentifierTypeByUuid(String content) {
        return getJsonObjectByUuid(IDENTIFIER_TYPE_RESOURCE, content);
    }

    public JSONObject getPersonAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(PERSON_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getPersonAttributeTypeByName(String content) {
        return getJsonObjectByName(PERSON_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getEncounterTypeByUuid(String content) {
        return getJsonObjectByUuid(ENCOUNTER_TYPE_RESOURCE, content);
    }

    public JSONObject getEncounterTypeByName(String content) {
        return getJsonObjectByName(ENCOUNTER_TYPE_RESOURCE, content);
    }

    public JSONObject getProviderByIdentifier(String content) {
        return getJsonObjectByName(PROVIDER_RESOURCE, content);
    }

    public JSONObject getProviderByUuid(String content) {
        return getJsonObjectByUuid(PROVIDER_RESOURCE, content);
    }

    public JSONObject getObservationByUuid(String content) {
        return getJsonObjectByUuid(OBSERVATION_RESOURCE, content);
    }

    public JSONObject getProviderAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(PROVIDER_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getProviderAttributeTypeByName(String content) {
        return getJsonObjectByName(PROVIDER_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getLocationAttributeTypeByName(String content) {
        return getJsonObjectByName(LOCATION_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getLocationAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getRelationshipByUuid(String content) {
        return getJsonObjectByUuid(RELATIONSHIP_RESOURCE, content);
    }

    public JSONObject getLocationTagByName(String content) {
        return getJsonObjectByName(LOCATION_TAG, content);
    }

    public JSONObject getLocationTagByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_TAG, content);
    }

    public JSONObject getEarliestEncounter(String patient, String encounterType) {
        return getCustomJsonObject(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM, early);
    }

    public JSONObject getLatestEncounter(String patient, String encounterType) {
        return getCustomJsonObject(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM, !early);
    }

    public JSONObject getEarliestObservation(String patient, String concept) {
        return getCustomJsonObject(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE, early);
    }

    public JSONObject getLatestObservation(String patient, String concept) {
        return getCustomJsonObject(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE, !early);
    }

    public JSONObject getPatientIdentifierTypeByUuid(String content) {
        return getJsonObjectByUuid(PATIENT_IDENTIFIER_TYPE_RESOURCE, content);
    }

    public JSONArray getAllRoles() {
        return getJSONArray(ROLE_RESOURCE);
    }

    public JSONArray getAllPrivileges() {
        return getJSONArray(PRIVILEGE_RESOURCE);
    }

    public JSONArray getAllPatientIdentifierTypes() {
        return getJSONArray(PATIENT_IDENTIFIER_TYPE_RESOURCE);
    }

    public JSONArray getAllPersonAttributeTypes() {
        return getCustomJsonArray(PERSON_ATTRIBUTE_TYPE_RESOURCE, PersonAttributeType.FIELDS);
    }

    public JSONArray getAllEncounterTypes() {
        return getCustomJsonArray(ENCOUNTER_TYPE_RESOURCE, EncounterType.FIELDS);
    }

    public JSONArray getAllProviderAttributeTypes() {
        return getJSONArray(PROVIDER_ATTRIBUTE_TYPE_RESOURCE);
    }

    public JSONArray getAllLocationAttributeTypes() {
        return getJSONArray(LOCATION_ATTRIBUTE_TYPE_RESOURCE);
    }

    public JSONArray getAllLocations() {
        return getCustomJsonArray(LOCATION_RESOURCE, Location.FIELDS);
    }

    public JSONArray getAllConcepts() {
        return getCustomJsonArray(CONCEPT_RESOURCE, Concept.FIELDS);
    }

    public JSONArray getAllLocationTags() {
        return getJSONArray(LOCATION_TAG);
    }

    public JSONArray getAllEncountersByPatientAndEncounterType(String patient, String encounterType) {
        return getJSONArray(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM);
    }

    public JSONArray getAllObservationsByPatientAndConcept(String patient, String concept) {
        return getJSONArray(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE);
    }

    public JSONArray getAllObservationsByEncounter(String encounter) {
        return getJSONArray(OBSERVATION_RESOURCE, ENCOUNTER_RESOURCE, encounter);
    }

    public JSONArray getPatientUuidByPatientId(String patientId) {
        return getCustomJsonArray(PATIENT_RESOURCE, UUID, patientId);
    }

    public JSONObject getProviderByUserId(String userId) {
        return getJsonObjectByName(PROVIDER_RESOURCE, userId);
    }

    public JSONArray getUsersByRole(String role){
        return getCustomJsonArray(USER_RESOURCE, "uuid,username,person", ROLE_RESOURCE, role);
    }

    public JSONObject getAllPersonAttributesByPersonUuid(String personUuid) {
        return getJsonObject(PERSON_RESOURCE, personUuid, ATTRIBUTE);
    }

    public JSONObject getSystemSetting(String setting){

        JSONObject json = null;

        try {
            JSONObject jsonObject = getJsonObjectByName("systemsetting",setting);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            json = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            Log.e(tag, "getSystemSetting");
        }
        return json;

    }

    public JSONArray getPatientsEncounters(String patientId){

        JSONArray array = null;

        try {
            JSONObject jsonObject = getJsonObjectByName(ENCOUNTER_RESOURCE,patientId);
            array = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.e(tag, "getPatientsEncounters");
        }
        return array;

    }

    public JSONObject getLabTestByUuid(String content) {
        /*return getCustomJsonObjectByUuid(COMMON_LAB_ORDER, content, LabOrder.FIELDS);*/
        return getJsonObjectByUuid(COMMON_LAB_ORDER, content);
    }

    public JSONArray getPatientsTests(String patientUuid){

        JSONArray array = null;
        array = getJSONArray(COMMON_LAB_ORDER,PATIENT_RESOURCE,patientUuid);
        return array;

    }

    public JSONObject getPersonAddressByPersonUuid(String personUuid) {
        JSONObject json = null;
        JSONArray jsonArray = getJsonArray(PERSON_RESOURCE, personUuid, ADDRESS);
        if(jsonArray != null) {
            try {
                int i = jsonArray.length();
                if(i != 0)
                    json = jsonArray.getJSONObject(jsonArray.length() - 1);
            } catch (JSONException e) {
                Log.e(tag, "getPersonAddressByPersonUuid");
            }
        }
        return json;
    }

    public static HttpClient createHttpClient_AcceptsUntrustedCerts() {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        b.setSslcontext( sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        b.setConnectionManager(connMgr);

        // finally, build the HttpClient;
        //      -- done!
        HttpClient client = b.build();
        return client;
    }


}