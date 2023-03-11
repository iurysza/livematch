Live Match
==================

This is a POC of a client built specifically for Reddit's `/r/soccer` community. The idea is that
you can use this app to follow any live match happening, besides you will be able to watch goals and other match related media as they're posted with a native video player (WIP).

**LiveMatch** is a fully functional Android where I try to apply some ideas and best practices related to android development while also explore new ideas that only a side project would allow. In this small
project you can see How I approach: modularization, dependency injection, unit testing, architecture, UI/UX design, etc.

# Features

The app consists of three main features:

* The Match Day feature lists all the soccer matches happening today and their scores.

* The Match Thread displays important information about a soccer match and the best comments from Reddit's `/r/soccer` community. These comments are organized based on what was happening in the match at the time they were posted.

- Match Highlights is a collection of all media related to the match thread that has been posted, displayed in a carousel They can be viewed by opening an in-app browser tab.

## Screenshots and Screen

| Feature | Dark Theme | Light Theme |
| --- | --- | --- |
| Match Day | <img src="./assets/imgs/match-day-dark.png" width="200" height="400"> | <img src="./assets/imgs/match-day-light.png" width="200" height="400"> |
| Match Thread | <img src="./assets/imgs/match-thread-dark.png" width="200" height="400"> | <img src="./assets/imgs/match-thread-light.png" width="200" height="400"> |

# Architecture

**Important Note:** This example focuses on scalability and is suitable for large projects and teams. For smaller projects and teams, a simpler approach and architecture may be more appropriate. The goal of this project is to demonstrate how I would approach app architecture in big projects and teams to ensure scalability.

This architecture includes:

- A layered architecture approach inspired by hexagonal/clean-arch.

- Rail Oriented Programming across all layers.

- MVVM like Arch for the presentation layer, featuring unidirectional data flow.

## Layers:

* Presentation: This layer contains presentation sections of the app like Screens, ViewItems, and ViewModels. Which communicates with the domain layer and prepares data for the UI.

* Domain: This layer contains app use cases and business logic which generally are repository abstractions and use cases. This layer communicates with the data layer and maps data models to domain items.

* Data: Single source of truth for the application. Which contains data sources like services, databases, and files and contains implementations of repositories.
![](https://i.imgur.com/zZCjfWE.png)

# Modularization

This modularization approach is an example of how to scale a project for multiple developers. While it may have some simplifications compared to what you might see in a larger project with over 50 developers, it still attempts to showcases the key principles of what I consider `good` modularization. This approach strikes a balance between keeping things straightforward and ensuring scalability, making it a smart choice for a small project.

## Modules

| Name                 | Responsibilities                                                                                                                    |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `app:main`             | Defines the Application and MainActivity as the main entry point of the application.                                                |
| `app:playground`       | Enables testing of mocked screens, components, and navigation flows, making it easier to test and debug the application.            |
| `feature:match-list`   | Contains UI components and UI logic related to the MatchList feature.                                                               |
| `feature:match-thread` | Contains UI components and UI logic related to the MatchThread feature.                                                             |
| `core:common`          | Contains network components and other shared components.                                                                            |
| `core:design-system`   | Contains shared UI elements used throughout the application.                                                                        |
| `core:reddit`          | Serves as a single source of truth for Reddit data and exposes repository abstractions and use cases related to Reddit.             |
| `core:footballdata`    | Serves as a single source of truth for Footballdata data and exposes repository abstractions and use cases related to Footballdata. |

# Testing

For the unit testing app favor faking cover mocking. Most data layer components are defined as interfaces. And these
components have fake implementation under testing packages.

The reason for favoring faking

* Mocking uses reflection is slower than fake

* Reusability we can reuse fake implementation for other tests

* Don't need to control implementation detail
  Examples: <code>FakeWeatherRepository, FakeWeatherApi, FakeWeatherDao</code>

# UI

UI designed with Compose. Each screen has its own composable function and the navigation controller handles opening screens.

## Running Tests

You can find unit tests in the `./modules//app/src/test/` folder.

## Static Analysis

You can run `ktlintFormat` and `detektCheck` to check code style. CI is WIP.

### Testing with mock-web-server:

You can also use mockwebserver `docker` image to run _integration_ tests.

To do that you will need only 3 things:

- First, you'll need [docker](https://docs.docker.com/get-docker/) installed.
- Then, cd into `./mockwebserver-docker-img/` and run `docker compose up`
- And lastly, open `./gradle.properties` and change `USE_MOCK_URL` to `true`

To change the API response, just change the data
in `./mockwebserver-docker-img/initializerJson.json`.

# TODO

- [x] Match description from dedicated source
- [ ] Native video player
- [ ] Match Stats
- [ ] Github actions CI
- [ ] Performance Improvement
- [ ] Match highlight comments
- [x] Light Theme
- [ ] Better match events ticker
- [ ] Reddit comment stream
