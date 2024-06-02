# StudentSphere

StudentSphere is a mobile app designed to streamline interaction between students and university student associations. It stands as a one-stop platform that primarily focuses on overcoming the prevalent challenges of communication fragmentation and information overload in campus life.

**The Challenge** \
At the start of each semester, students are inundated with numerous recruitment emails from university student associations. Filtering through these emails to determine which association is looking for specific members or roles can be overwhelming. As a result, many students end up disregarding these messages. Additionally, each association utilizes a variety of platforms, including Instagram, emails, and physical flyers, to disseminate information throughout the semester. This varied approach complicates the process for students to stay informed about events and remain engaged with the dynamic campus community around them.

**Our Solution** \
To directly address the visibility issue, StudentSphere provides a centralized platform where student associations can maintain dedicated pages to post updates, share opportunities, and announce events effortlessly. Featuring targeted notifications, a personalized calendar, and a user-friendly interface, the app guarantees that students remain well-informed. Whether offering behind-the-scenes insights for association members or a broad overview for the wider student body, StudentSphere simplifies the process of staying connected.

**Key Features** 
-	Association Directory: A list of associations, each with their own page detailing the association's goals, available positions, upcoming events, and more.
-	News Feed: A feed of updates from followed associations to ensure you don't miss out on any news.
-	Recruitments: The ability to apply for positions offered by associations.
-	Discover: Explore different associations by interest area.
-	Personal calendar
-	Ticket System: Management of event tickets in the app thanks to QR code generation.

**In Development** \
Figma : https://www.figma.com/team_invite/redeem/Z6aOlqKXBjUkzQUDpnfWkr

**Architecture Diagram** \
<img width="1291" alt="Capture d’écran 2024-05-03 à 13 23 30" src="https://github.com/The-Software-Enterprise/assos/assets/91310864/88f8ed79-8504-4966-897f-4e1aeb3b26c8">

**App Demo** \
https://drive.google.com/file/d/1MKIzjWy95ieCJBskymBrf3TPmYOmTQs6/view?usp=sharing

**PRD** \
[MVP Swent.pdf](https://github.com/The-Software-Enterprise/assos/files/15370886/MVP.Swent.pdf)

## Structure of the Project

### Introduction
This project is developed using Firebase for the Backend and Jetpack Compose for the Frontend. The project is divided into two main modules:
- **model**: Contains the data classes, the view models, and all the functions calling the Firebase API.
- **ui**: Contains the UI of the app, the screens, and the components. You can also find a config file to initialize the Firebase Emulator during development.

There are also three Activity files:
- `MainActivity`: The main activity of the app, it contains the navigation graph calling the different screens.
- `NFCReader`: The activity that reads an NFC tag.
- `NFCWriter`: The activity that writes to an NFC tag.

The testing is done in the **androidTest** and **test** folders.

### Model
The model module is divided into different packages that define classes, functions, and manage the data:
- **data**: Contains the data classes used in the app. A special object called `DataCache` is used to store data globally in the app and avoid calling the API multiple times (e.g., current user information).
- **di**: Contains personalized Dispatchers for Coroutines.
- **local_database**: Contains the definition of the local database `LocalDatabase` and the DAOs. Also includes the `LocalDatabaseProvider` to provide the database and `Converters` to convert the data.
- **navigation**: Contains two navigation classes `NavigationGraph` (main navigation graph of the app) and `HomeNavigation` (navigation graph of the home screen); and the `NavigationActions` class.
- **NFC**: Contains utility functions to read and write to an NFC tag.
- **qr_code**: Contains utility functions to scan a QR code.
- **service**:
  - Contains three services: `AuthService`, `DbService`, and `StorageService` to manage authentication, the database, and Firebase storage.
- **module**: Contains the Firebase API configuration with `FirebaseModule` and `ServiceModule` to provide the API and Firebase services with Hilt and Dagger to all view models.
- **impl**: Contains the implementation of the services.
- **view**: Contains the view models of the app. Each view model is associated with screens of the app and contains the logic of the screens. A special view model `AppViewModel` is used to call the user information at the start of the app and store it in the `DataCache`.

### UI
The UI module is divided into different packages that define the screens and the components of the app:
- **components**: Contains the different reusable components used in the app.
- **screens**: Contains the different screens of the app. Each screen is associated with a view model in the model module. The screens are divided into different packages:
  - **assoDetails**: Contains the screens to display the details of an association, news, and events.
  - **calendar**: Contains the screens to display the user's calendar with events of followed associations and memberships.
  - **login**: Contains the login and registration screens.
  - **manageAsso**: Contains the screens to manage an association, create news, events, recruit, and manage members.
  - **profile**: Contains the screens to display the user's profile, followed associations, memberships, saved news, and settings.
  - **ticket**: Contains the screens to display the user's tickets and the QR code scanner.
  - `Explorer` and `News`: Two main screens to display the list of associations and the news feed on the home screen.
  - `NFCReading` and `NFCWriting`: Screens to read and write an NFC tag.
- **theme**: Contains the theme of the app where colors and typography are defined.

### Testing
In the **androidTest** folder, you can find the screen tests related to the different screens of the app in the subfolder **screens**, along with different tests for those screens.

In the **test** folder, you can find unit tests for the various functions of the app.
