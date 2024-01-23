# xkcd Comic Viewer App - MVP 

Welcome to my solution for a comic viewer app MVP🥇 

Important note before i explain my solution:  xkcd search is not working. When you try to use the api you get error status 500. Therefore i have not used it at all. However i made searching by comic-number possible.

# Features
In my MVP version i decided to include the following features:
- Comic Browsing
- Comic details and description (transcript)
- Searching for comics (only by number)
- Comic explanation

The ones i listed above is the bread and butter of the app 🍞🧈, and i decided to leave out the other features as i see this as more fit for the next MVP version.

If i had time i would start implementing the favorite feature and the sending to others feature. I would use Firebase in a combination with SQLite (for offline) for this. 

# Technical choices
I chose Android Studio using Kotlin and XML 

Libraries i used: 

- Picasso - for image loading
- Gson - for JSON parsing
- OkHttp - for HTTP requests

I tested the app on emulators. Pixel 7 API 33 and Pixel 2 API 33

# Design choices
Since the client didn't have a designer, but enjoyed xkcd, i based my design on the website. 

Except the font, that didnt fit at all 👎

# Quick and Dirty description

My thought was to have 10 comics loaded simultaniously in a scrollable view, hence i made the project revolve around my RecyclerView and ComicAdapter. Each comic has its own "card" with all its information and image. 

If you need to modify something with the comic-cards, you modify the ComicAdapter, everything else is done in the activity.

I've structured the project in a way that additional features will be easy to implement. However if Firebase and SQLite was included in the project i would add some sort of controller to do logic between my services and activites. My activites would at that point mainly be for UI logic.

- models-folder holds all my dataclasses
- service-folder will/would hold all my api and db logic
- controllers-folder would hold all controllers that works between the service/models folder and my activites


