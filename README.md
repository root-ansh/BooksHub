# Books Hub

![](app/src/main/ic_launcher-playstore.png)

A simple PDF aggregating, viewing and reviewing app, powered by firebase

## Features: 

- 100% kotlin, almost 100% compose (need to add compose based navigation)
- version catalog, Hilt, Coil and Retrofit/Okhttp
- MVVM Arch

### Roadmap:

- [x] build basic app structure using view based ui 
- [x] create backend using firebase for different schemas (Books, User,Reviews) and link
- [x] replace view based ui with compose ui (multi activity)
- [ ] replace view based ui with single activity and compose screens/ compose navigation
- [ ] use flows

### Screen Descriptions:

1. Splash Screen
2. Authentication : Login/Signup
3. HomePage/ My Books:
   1. Books that I am reading. On Clicking it will open Book Page
   2. Books that I purchased.  On Clicking it will open Book Page
   3. Books that I reviewed.   On Clicking it will open Book Page
   4. Books that I bookmarked. On Clicking it will open Book Page
   5. Suggested Books. On Clicking it will open Book Page
4. Book Search: 
    - A List of all books available on the platform. on clicking will open Book Page
5. Book Page:
    - A Page showing Book Title , Image, Cost, Avg Rating, Summary , List of reviews, 
      Purchase/Download/open button , your review field bookmark and review button.
    - on purchase button -> ~goto summary page~ change button to download for now
    - on download -> PDF gets downloaded in user cache
    - on open -> Book Preview Page is Opened
    - on bookmark -> Book is added to a list of user bookmarks
    - on review -> review is added to list of reviews
6. Book Preview: PDF is rendered via PDF renderer \[update: in v2]
7. My Profile :User can change their name/ Default Profile Picture, or logout.
8. Book Upload : \[update:in v2]
   1. Limited to a certain users only
   2. A single page where user will provide a book, a thumbnail will be generated for the book , 
     can add an initial review, description ,cost etc