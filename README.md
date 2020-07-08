# LealMessages

Here you would find the solution for the Mobile Test, it was developed in Kotlin so it would run only in Android Devices.

After cloning the repository open the project in Android Studio, compile and run the app in a virtual device or in your own device, you would see a Splash Screen
with the logo of the app, then you would see the Main Screen where the transactions will be loaded from an API and saved in a mobile database created by Realm. In this screen you have fourth options:

    1. Open the transaction's details by touching in one transaction. It would render the user and transaction's data, 
       along with a comment with the info about price and points. It would render as a DetailFragment.
    2. Delete one transaction with the swipe left gesture, if you complete the gesture the transaction will be deleted
       from the local database and an SnackBar will appear indicating which transaction you deleted.
    3. Delete all transactions by touching in the Floatting Action Button with the "trash" icon, the transactions will
       be deleted from the local database and dessapear, after this a SnackBar will appear indicating the action done.
    4. Refresh all the transactions by touching in the Floatting Action Button with the "reload" icon located in the BottomAppBar. It would bring 
       all the transactions from the API and save them in the mobile database.
       
The following third-party libraries were used in the project:

    1. Material Design, for visual components.
    2. Volley, to made HTTP Request and capture the data from the API.
    3. Glide, to load images.
    4. Realm, to create the mobile database and save data.
    
The arquitechture used in this project is MVVM (Model View ViewModel), making the code more legible and easier to understand. To implement this arquitechture
concepts like LiveData, ViewModele, Observable Pattern were used.
