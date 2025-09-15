# AsteroidApp 

**"A NASA-powered Compose app to monitor asteroids and their Earth proximity, with offline features and clean architecture."**
 Asteroid App is a Compose app designed to help users track asteroids detected by NASA that come near Earth. It uses a free, open-source API provided by the NASA JPL Asteroid team and stores data locally for offline access. This project is part of the ** Android Kotlin Developer Nanodegree Program**.

## Main Features of the Project

- **Modern UI**: Use Jetpack Compose for modern UI. Also uses a single-activity architecture with multiple fragments for simplified navigation and improved modularity (in the XML branch).
- **MVVM and Repository Architecture**: Implements MVVM and Repository to enhance maintainability, modularity, and scalability, promoting cleaner code and separation of concerns.
- **Offline Mode**: Provides offline access to data by using Room for local storage, ensuring the app is usable even without an internet connection.
- **Paging Library (v3)**: Optimizes data viewing with seamless scrolling, ensuring smooth navigation through large lists of asteroids.
- **Accessible UI**: Dynamically generated ContentDescriptions to enhance accessibility, making the app inclusive for all users.
- **Orientation Support**: Adapts to portrait and landscape orientations without losing data or state, ensuring a smooth user experience.
- **Dependency Injection**: Koin (v4) improves modularity and testability by effectively managing dependencies.
- **Reactive UI**: Incorporates DataBindingAdapter and Kotlin Flow for a reactive and responsive user experience (in the XML branch).

## Showcased Skills

- **Clean Architecture**: Enforced separation of concerns through domain, data, and presentation layers.
- **Offline-first Design**: Implemented offline access using Room, ensuring a consistent user experience even without network availability, supported by periodic background data updates using WorkManager to keep the data fresh.
- **Scalable Codebase**: Followed MVVM and Repository patterns to make the project scalable and maintainable.
- **Reactive Programming**: Utilized Kotlin Flow to efficiently handle data streams, making the app responsive and resource-efficient.
- **Accessibility**: Implemented accessibility features, including dynamic content descriptions and support for screen readers, to promote an inclusive experience for all users.

## License

This project is open-source and licensed under the Apache 2.0 License. The LICENSE file in this repository provides more details.
