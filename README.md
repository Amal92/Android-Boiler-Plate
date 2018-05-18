## Android LRPD boiler plate

LRPD - **L**ogin **R**egister **P**rofile Navigation**D**rawer

This Android boiler plate consist of:
 - Login module
	 - Valid email checker
	 - Password character length check
 - Register module
	 - Valid email checker
	 - Password character length check
	 - Gender input
 - Forgot password module
	 - Valid email checker
 - Update profile module
	 - Valid email check
	 - Password character length check
	 - Uploading profile picture from gallery
 - Navigation Drawer module
 - A map fragment module
	 - Map fragment is placed inside a fragment of an Activity. Replace 	 this fragment with any fragment
 - Networking call module (Ion library is used)
	 - Used to make API calls in login, register, profile update modules

**Note:**
 1. Update the `BASE_URL` in `Endpoint.java` with your api endpoint.
 2. Update your `google_map_key` in `strings.xml` if you are using
    google map in your app.
