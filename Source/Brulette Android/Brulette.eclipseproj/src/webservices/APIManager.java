package webservices;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import business.Brew;
import business.BrewCategory;
import business.BrewTags;
import business.Membership;
import business.Notifier;
import business.Rating;
import business.Round;
import business.RoundStatus;
import business.Team;
import business.User;


import android.util.Log;

public abstract class APIManager {
	private static final String URL_LOGIN = "https://salty-plains-8447.herokuapp.com/api/users/login";
	private static final String URL_CREATE_TEAM = "https://salty-plains-8447.herokuapp.com/api/teams";
	private static final String URL_REGISTER = "https://salty-plains-8447.herokuapp.com/api/users";
	private static final String URL_LOGOUT = "https://salty-plains-8447.herokuapp.com/api/users/logout?auth_token=";
	private static final String URL_NEARBY_TEAMS = "https://salty-plains-8447.herokuapp.com/api/teams?auth_token=";
	private static final String URL_DELETE_USER = "https://salty-plains-8447.herokuapp.com/api/users?auth_token=";
	private static final String URL_USER_NOTIFIERS = "https://salty-plains-8447.herokuapp.com/api/notifiers?auth_token=";
	private static final String URL_CREATE_NOTIFIER = "https://salty-plains-8447.herokuapp.com/api/notifiers";
	private static final String URL_UPDATE_NOTIFIER = "https://salty-plains-8447.herokuapp.com/api/notifiers/";
	private static final String URL_DELETE_NOTIFIER = "https://salty-plains-8447.herokuapp.com/api/notifiers/";
	private static final String URL_TEAM_BY_SLUG = "https://salty-plains-8447.herokuapp.com/api/teams/";
	private static final String URL_UPDATE_TEAM = "https://salty-plains-8447.herokuapp.com/api/teams/";
	private static final String URL_DELETE_TEAM = "https://salty-plains-8447.herokuapp.com/api/teams/";
	private static final String URL_MEMBERSHIPS = "https://salty-plains-8447.herokuapp.com/api/memberships?auth_token=";
	private static final String URL_JOIN_TEAM = "https://salty-plains-8447.herokuapp.com/api/memberships";
	private static final String URL_UPDATE_MEMBERSHIP = "https://salty-plains-8447.herokuapp.com/api/memberships/";
	private static final String URL_DELETE_MEMBERSHIP = "https://salty-plains-8447.herokuapp.com/api/memberships/";
	private static final String URL_BREWS = "https://salty-plains-8447.herokuapp.com/api/brews";
	private static final String URL_UPDATE_BREW = "https://salty-plains-8447.herokuapp.com/api/brews/";
	private static final String URL_BREW_TAGS = "https://salty-plains-8447.herokuapp.com/api/brew_tags?auth_token=";
	private static final String URL_USER_BREWS = "https://salty-plains-8447.herokuapp.com/api/brews?auth_token=";
	private static final String URL_ROUNDS = "https://salty-plains-8447.herokuapp.com/api/rounds";
	private static final String URL_JOIN_ROUNDS = "https://salty-plains-8447.herokuapp.com/api/orders";
	private static final String URL_RATING = "https://salty-plains-8447.herokuapp.com/api/ratings";

	private static JSONObject jObject;
	private static JSONParser jParser = new JSONParser();

	public static boolean login(String email, String password) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_LOGIN);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);

			String auth_token = jObject.getString("auth_token");
			int user_id = jObject.getInt("user_id");
			User.id = user_id; 
			User.auth_token = auth_token;

		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
			return false;
		} catch (JSONException e) {
			try {
				String message = jObject.getString("message");
				Log.i("JSON Response", message);
				return false;
			} catch (JSONException e1) {
				Log.e("Brewlette", "JSONException Error : " + e1.getMessage());
				throw e1;
			}
		}

		return true;

	}

	public static boolean register(String name, String email, String password, String passwordConfirmation, Long latitude, Long longitude) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_REGISTER);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user[email]", email));
			nameValuePairs.add(new BasicNameValuePair("user[password]", password));
			nameValuePairs.add(new BasicNameValuePair("user[password_confirmation]", passwordConfirmation));
			nameValuePairs.add(new BasicNameValuePair("user[name]", name));
			nameValuePairs.add(new BasicNameValuePair("user[latitude]", String.valueOf(latitude)));
			nameValuePairs.add(new BasicNameValuePair("user[longitude]", String.valueOf(longitude)));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);
			User.auth_token = jObject.getString("auth_token");
			JSONObject user = jObject.getJSONObject("user");
			User.id = user.getInt("id");


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
			return false;
		}

		return true;

	}

	public static Membership createTeam(String auth_token, String teamName, String teamPassword, Long latitude, Long longitude) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_CREATE_TEAM);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("team[name]", teamName));
			if(!teamPassword.equals(""))
				nameValuePairs.add(new BasicNameValuePair("team[password]", teamPassword));
			if(!latitude.equals(null))
				nameValuePairs.add(new BasicNameValuePair("team[latitude]", String.valueOf(latitude)));
			if(!longitude.equals(null))
				nameValuePairs.add(new BasicNameValuePair("team[longitude]", String.valueOf(longitude)));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);

			JSONObject entry = jObject.getJSONObject("membership");


			int id = entry.getInt("id");
			int team_id = entry.getInt("team_id");
			int user_id = entry.getInt("user_id");
			String team_name = entry.getString("team_name");
			String user_name = entry.getString("user_name");
			boolean active = entry.getBoolean("active");
			boolean block_by_team = entry.getBoolean("block_by_team");
			boolean block_by_user = entry.getBoolean("block_by_user");

			String created = entry.getString("created_at");
			String updated = entry.getString("updated_at");

			return new Membership(id,team_id,user_id,team_name,user_name,active,block_by_team,block_by_user,dateParser(created),dateParser(updated));




		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());

		}

		return null;

	}

	public static void logout(String auth_token) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(URL_LOGOUT + auth_token);
		try {
			HttpResponse response = client.execute(httpdelete);
			jParser.getJSONFromHttpResponse(response, Method_Type.DELETE);
			User.id = -1; 
			User.auth_token = "";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean updateUser(String auth_token, String currentPassword, String name, String email, 
			String password, String confirmPassword, Long latitude, Long longitude) throws JSONException{

		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_CREATE_TEAM);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("user[current_password]", currentPassword));

			//Optiomal parameters
			if(!name.equals(""))
				nameValuePairs.add(new BasicNameValuePair("user[name]", name));
			if(!email.equals(""))
				nameValuePairs.add(new BasicNameValuePair("user[email]", email));
			if(!password.equals(""))
				nameValuePairs.add(new BasicNameValuePair("user[password]", password));
			if(!confirmPassword.equals(""))
				nameValuePairs.add(new BasicNameValuePair("user[password_confirmation]", confirmPassword));
			if(latitude != null)
				nameValuePairs.add(new BasicNameValuePair("user[latitude]", String.valueOf(latitude)));
			if(longitude != null)
				nameValuePairs.add(new BasicNameValuePair("user[longitude]", String.valueOf(longitude)));


			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));


			// Execute HTTP Post Request
			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
			return false;
		}

		return true;

	}
	// Work but without parameter location = USER's TEAMS LIST
	public static List<Team> nearbyTeams(String auth_token, Long distance, String location) throws JSONException{

		HttpClient client = new DefaultHttpClient();
		String url = URL_NEARBY_TEAMS + auth_token;
		if(!distance.equals(Long.valueOf(-1)))
			url += "&distance=" + String.valueOf(distance);
		if(!location.equals(""))
			url += "&location=" + location;
		HttpGet httpget = new HttpGet(url);
		List<Team> teams = new ArrayList<Team>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");



			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				int id = entrie.getInt("id");
				String name = entrie.getString("name");
				String password = entrie.getString("password");
				String slug = entrie.getString("slug");
				Long longitude = null;
				if(!entrie.get("longitude").equals(null))
					longitude = entrie.getLong("longitude");
				Long latitude = null;
				if(!entrie.get("latitude").equals(null))
					latitude = entrie.getLong("latitude");

				String created = entrie.getString("created_at");
				String updated = entrie.getString("updated_at");

				teams.add(new Team( id, name, password, slug, latitude, longitude, dateParser(created), dateParser(updated)));

			}





		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());

		} catch (JSONException e) {
			Log.e("Brewlette", "JSONException Error : " + e.getMessage());
			throw e;
		}

		return teams;

	}

	public static void deleteUser(String auth_token) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(URL_DELETE_USER + auth_token);
		try {
			HttpResponse response = client.execute(httpdelete);
			jParser.getJSONFromHttpResponse(response, Method_Type.DELETE);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Obtain a list of all of the user's teams
	 */
	public static List<Team> userTeams(String auth_token, int per_page, int page) throws JSONException{

		HttpClient client = new DefaultHttpClient();
		String url = URL_NEARBY_TEAMS + auth_token;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<Team> teams = new ArrayList<Team>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				int id = entrie.getInt("id");
				String name = entrie.getString("name");
				String password = entrie.getString("password");
				String slug = entrie.getString("slug");
				Long longitude = null;
				if(!entrie.get("longitude").equals(null))
					longitude = entrie.getLong("longitude");
				Long latitude = null;
				if(!entrie.get("latitude").equals(null))
					latitude = entrie.getLong("latitude");

				String created = entrie.getString("created_at");
				String updated = entrie.getString("updated_at");

				teams.add(new Team( id, name, password, slug, latitude, longitude, dateParser(created), dateParser(updated)));

			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());

		} catch (JSONException e) {
			Log.e("Brewlette", "JSONException Error : " + e.getMessage());
			throw e;
		}

		return teams;

	}

	public static List<Notifier> userNotifiers(String auth_token, int per_page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_USER_NOTIFIERS + auth_token;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		HttpGet httpget = new HttpGet(url);
		List<Notifier> notifiers = new ArrayList<Notifier>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				boolean active = entrie.getBoolean("active");
				int id = entrie.getInt("id");
				String identifier = entrie.getString("identifier");
				String provider = entrie.getString("provider");
				int userId = entrie.getInt("user_id");


				String created = entrie.getString("created_at");
				String updated = entrie.getString("updated_at");

				notifiers.add(new Notifier(active, dateParser(created), id, identifier, provider, dateParser(updated), userId));

			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());

		} catch (JSONException e) {
			Log.e("Brewlette", "JSONException Error : " + e.getMessage());
			throw e;
		}

		return notifiers;
	}

	public static void createNotifier(String auth_token, String provider, String identifier, boolean active) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_CREATE_NOTIFIER);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("notifier[provider]", provider));
			nameValuePairs.add(new BasicNameValuePair("notifier[identifier]", identifier));
			nameValuePairs.add(new BasicNameValuePair("notifier[active]", String.valueOf(active)));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static void updateNotifier(String auth_token, int notifierId, String provider, String identifier, boolean active) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_UPDATE_NOTIFIER + notifierId );

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			if(!provider.equals(""))
				nameValuePairs.add(new BasicNameValuePair("notifier[provider]", provider));
			if(!identifier.equals(""))
				nameValuePairs.add(new BasicNameValuePair("notifier[identifier]", identifier));
			nameValuePairs.add(new BasicNameValuePair("notifier[active]", String.valueOf(active)));
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.PUT);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static void deleteNotifier(String auth_token, int notifierId) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(URL_DELETE_NOTIFIER + notifierId + "?auth_token=" + auth_token);
		try {
			HttpResponse response = client.execute(httpdelete);
			jParser.getJSONFromHttpResponse(response, Method_Type.DELETE);
			User.id = -1; 
			User.auth_token = "";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Team teamBySlug(String slugTeam) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(URL_TEAM_BY_SLUG + slugTeam + "?auth_token=" + User.auth_token);
		Team team = null;

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);


			JSONObject entry = jObject.getJSONObject("entry");

			int id = entry.getInt("id");
			String name = entry.getString("name");
			String password = entry.getString("password");
			String slug = entry.getString("slug");
			Long longitude = null;
			if(!entry.get("longitude").equals(null))
				longitude = entry.getLong("longitude");
			Long latitude = null;
			if(!entry.get("latitude").equals(null))
				latitude = entry.getLong("latitude");

			String created = entry.getString("created_at");
			String updated = entry.getString("updated_at");

			team = new Team( id, name, password, slug, latitude, longitude, dateParser(created), dateParser(updated));





		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());

		}

		return team;
	}

	public static boolean updateTeam(String auth_token, int teamId, String teamName, String teamPassword, Long latitude, Long longitude) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_UPDATE_TEAM + teamId);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			if(!teamName.equals(""))
				nameValuePairs.add(new BasicNameValuePair("team[name]", teamName));
			if(!teamPassword.equals(""))
				nameValuePairs.add(new BasicNameValuePair("team[password]", teamPassword));
			if(!latitude.equals(Long.valueOf(-1)))
				nameValuePairs.add(new BasicNameValuePair("team[latitude]", String.valueOf(latitude)));
			if(!longitude.equals(Long.valueOf(-1)))
				nameValuePairs.add(new BasicNameValuePair("team[longitude]", String.valueOf(longitude)));

			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
			return false;
		}

		return true;

	}

	public static void deleteTeam(String auth_token, int teamId) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(URL_DELETE_TEAM + teamId + "?auth_token=" + auth_token);
		try {
			HttpResponse response = client.execute(httpdelete);
			jParser.getJSONFromHttpResponse(response, Method_Type.DELETE);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Membership> memberships(String auth_token) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(URL_MEMBERSHIPS + auth_token);
		List<Membership> memberships = new ArrayList<Membership>();
		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);


			JSONArray entries = jObject.getJSONArray("entries");



			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				int id = entrie.getInt("id");
				int team_id = entrie.getInt("team_id");
				int user_id = entrie.getInt("user_id");
				String team_name = entrie.getString("team_name");
				String user_name = entrie.getString("user_name");
				boolean active = entrie.getBoolean("active");
				boolean block_by_team = entrie.getBoolean("block_by_team");
				boolean block_by_user = entrie.getBoolean("block_by_user");

				String created = entrie.getString("created_at");
				String updated = entrie.getString("updated_at");

				memberships.add(new Membership(id,team_id,user_id,team_name,user_name,active,block_by_team,block_by_user,dateParser(created),dateParser(updated)));

			}



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return memberships;
	}

	public static Membership joinTeam(String auth_token, int teamId, String password) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_JOIN_TEAM);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("team_id", String.valueOf(teamId)));
			if(!password.equals(""))
				nameValuePairs.add(new BasicNameValuePair("password]", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);
			
			
			JSONObject entry = jObject.getJSONObject("entry");


			int id = entry.getInt("id");
			int team_id = entry.getInt("team_id");
			int user_id = entry.getInt("user_id");
			String team_name = entry.getString("team_name");
			String user_name = entry.getString("user_name");
			boolean active = entry.getBoolean("active");
			boolean block_by_team = entry.getBoolean("block_by_team");
			boolean block_by_user = entry.getBoolean("block_by_user");

			String created = entry.getString("created_at");
			String updated = entry.getString("updated_at");

			return new Membership(id,team_id,user_id,team_name,user_name,active,block_by_team,block_by_user,dateParser(created),dateParser(updated));



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		
		return null;

	}

	public static void createMembership(String auth_token, int teamId, String password, int userId, String userEmail) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_JOIN_TEAM);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("team_id", String.valueOf(teamId)));
			if(!password.equals(""))
				nameValuePairs.add(new BasicNameValuePair("password]", password));
			nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
			nameValuePairs.add(new BasicNameValuePair("user_email ", userEmail));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static void updateTeamMembership(String auth_token, int membershipId, boolean active, boolean blockedByUser, boolean blockedByTeam) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_UPDATE_MEMBERSHIP + membershipId );

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("membership[active]", String.valueOf(active)));
			nameValuePairs.add(new BasicNameValuePair("membership[blocked_by_user]", String.valueOf(blockedByUser)));
			nameValuePairs.add(new BasicNameValuePair("membership[blocked_by_team]", String.valueOf(blockedByTeam)));
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.PUT);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static void deleteMemberShip(String auth_token, int membershipId) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(URL_DELETE_MEMBERSHIP + membershipId + "?auth_token=" + auth_token);
		try {
			HttpResponse response = client.execute(httpdelete);
			jParser.getJSONFromHttpResponse(response, Method_Type.DELETE);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BrewTags createBrew(String auth_token, List<Brew> brews) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_BREWS);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			String stringBrews = "";
			if(brews.size() > 0)
				stringBrews = brews.get(0).getTag() + ":" + brews.get(0).getCategorie();

			for(int i=1; i<brews.size(); i++){
				stringBrews += "," + brews.get(i).getTag() + ":" + brews.get(i).getCategorie();
			}

			nameValuePairs.add(new BasicNameValuePair("brew_tags", stringBrews));


			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);

			JSONObject entry = jObject.getJSONObject("entry");

			int id =  entry.getInt("id");
			int userId = entry.getInt("user_id");
			String name = entry.getString("name");
			
			JSONArray tagsJSON = entry.getJSONArray("brew_tags");
			List<String> tags = new ArrayList<String>();
			for(int j=0 ; j<tagsJSON.length(); j++){
				JSONObject tagsEntry = tagsJSON.getJSONObject(j);
				tags.add(tagsEntry.getString("name"));
			}


			return new BrewTags(id,userId, name, tags);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		
		return null;
	}

	public static void updateBrew(String auth_token, int brewId, List<Brew> brews) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_UPDATE_BREW + brewId );

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			String stringBrews = "";
			if(brews.size() > 0)
				stringBrews = brews.get(0).getTag() + ":" + brews.get(0).getCategorie();

			for(int i=1; i<brews.size(); i++){
				stringBrews += "," + brews.get(i).getTag() + ":" + brews.get(i).getCategorie();
			}

			nameValuePairs.add(new BasicNameValuePair("brew_tags", stringBrews));
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.PUT);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static List<String> brewTagsAutoComplet(String auth_token, BrewCategory category) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		String url = URL_BREW_TAGS + auth_token;
		url += "&category=" + category;
		url += "&per_page=" + 100;
		HttpGet httpget = new HttpGet(url);
		List<String> tags = new ArrayList<String>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				tags.add(entrie.getString("name"));
			}

		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return tags;

	}

	public static List<Brew> brewTags(String auth_token, BrewCategory category,int per_page, int page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_BREW_TAGS + auth_token;
		if(category != null)
			url += "&category=" + category;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<Brew> brews = new ArrayList<Brew>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				int id = entrie.getInt("id");
				String stringCategory = entrie.getString("category");
				String name = entrie.getString("name");
				int popularity = entrie.getInt("popularity");

				String created = entrie.getString("created_at");
				String updated = entrie.getString("updated_at");

				brews.add(new Brew(name, BrewCategory.valueOf(stringCategory), id, popularity, dateParser(created), dateParser(updated)));

			}

		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return brews;
	}

	public static List<BrewTags> userBrews(String auth_token,int per_page, int page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_USER_BREWS + auth_token;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<BrewTags> brews = new ArrayList<BrewTags>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entry = entries.getJSONObject(i);
				int id =  entry.getInt("id");
				int userId = entry.getInt("user_id");
				String name = entry.getString("name");
				
				JSONArray tagsJSON = entry.getJSONArray("brew_tags");
				List<String> tags = new ArrayList<String>();
				for(int j=0 ; j<tagsJSON.length(); j++){
					JSONObject tagsEntry = tagsJSON.getJSONObject(j);
					tags.add(tagsEntry.getString("name"));
				}		

				brews.add(new BrewTags(id, userId, name, tags));

			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return brews;
	}

	public static void startRound(String auth_token, int teamId, boolean volunteer, int time) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_ROUNDS);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("team_id", String.valueOf(teamId)));
			nameValuePairs.add(new BasicNameValuePair("volunteer", String.valueOf(volunteer)));
			if(time != -1)
				nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(time)));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);

			jObject.getJSONObject("entry");



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static List<Round> rounds(String auth_token, RoundStatus status, int per_page, int page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_ROUNDS + "?auth_token=" + auth_token;
		if(status != null)
			url += "&status=" + status;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<Round> rounds = new ArrayList<Round>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);
				int id = entrie.getInt("id");
				int userId = entrie.getInt("user_id");
				int karma = entrie.getInt("karma");
				int teamId = entrie.getInt("team_id");
				int time = entrie.getInt("time");
				String stringStatus = entrie.getString("status");

				String created = entrie.getString("created_at");
				String closes = entrie.getString("closes_at");


				rounds.add(new Round(id, karma, teamId, time, userId, dateParser(created), dateParser(closes), RoundStatus.valueOf(stringStatus)));


			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return rounds;
	}

	public static void joinRound(String auth_token, int roundId, int brewId) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_JOIN_ROUNDS);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			if(roundId != -1)
				nameValuePairs.add(new BasicNameValuePair("round_id", String.valueOf(roundId)));
			if(brewId != -1)
				nameValuePairs.add(new BasicNameValuePair("brew_id", String.valueOf(brewId)));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static List<BrewTags> roundBrews(String auth_token, int roundId,int per_page, int page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_BREWS + "?auth_token=" + auth_token;
		url += "&round_id=" + roundId;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<BrewTags> brews = new ArrayList<BrewTags>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entry = entries.getJSONObject(i);
				int id =  entry.getInt("id");
				int userId = entry.getInt("user_id");
				JSONArray tagsJSON = entry.getJSONArray("brew_tags");
				List<String> tags = new ArrayList<String>();
				for(int j=0 ; j<tagsJSON.length(); j++){
					JSONObject tagsEntry = tagsJSON.getJSONObject(j);
					tags.add(tagsEntry.getString("name"));
				}
				String name = entry.getString("name");

				brews.add(new BrewTags(id, userId, name, tags));

			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return brews;
	}

	public static void closeRound(String auth_token, int roundId) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_ROUNDS + "/" + roundId);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("status", RoundStatus.closed.toString()));


			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}

	}

	public static void finishRound(String auth_token, int roundId, String message) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(URL_ROUNDS + "/" + roundId);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("status", RoundStatus.finished.toString()));
			if(!message.equals(""))
				nameValuePairs.add(new BasicNameValuePair("message", message));


			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(httpput);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}

	}

	public static void rateRound(String auth_token, int roundId, int rating, String comment ) throws JSONException{
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL_RATING);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("auth_token", auth_token));
			nameValuePairs.add(new BasicNameValuePair("round_id", String.valueOf(roundId)));
			nameValuePairs.add(new BasicNameValuePair("rating[stars]", String.valueOf(rating)));
			if(comment.length() <= 100)
				nameValuePairs.add(new BasicNameValuePair("rating[comment]", comment));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(httppost);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.POST);



		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
	}

	public static List<Rating> roundRatings(String auth_token, int roundId,int per_page, int page) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		String url = URL_RATING +  "?auth_token=" + auth_token;
		url += "&round_id=" + roundId;
		if(per_page != -1)
			url += "&per_page=" + String.valueOf(per_page);
		if(page != -1)
			url += "&page=" + String.valueOf(page);
		HttpGet httpget = new HttpGet(url);
		List<Rating> ratings = new ArrayList<Rating>(); 

		try {

			// Execute HTTP GET Request
			HttpResponse response = client.execute(httpget);
			jObject = jParser.getJSONFromHttpResponse(response, Method_Type.GET);

			JSONArray entries = jObject.getJSONArray("entries");

			for(int i=0 ; i< entries.length(); i++){
				JSONObject entrie = entries.getJSONObject(i);


				String comment = entrie.getString("comment");
				int id = entrie.getInt("id");
				int userId = entrie.getInt("user_id");
				int stars = entrie.getInt("stars");
				String stringCreated = entrie.getString("created_at");
				String stringUpdated = entrie.getString("updated_at");

				ratings.add(new Rating(comment, id, userId, roundId, stars, dateParser(stringCreated), dateParser(stringUpdated)));

			}


		} catch (ClientProtocolException e) {
			Log.e("Brewlette", "ClientProtocolException Error : " + e.getMessage());

		} catch (IOException e) {
			Log.e("Brewlette", "IOException Error : " + e.getMessage());
		}
		return ratings;
	}

	private static Date dateParser(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}

}





