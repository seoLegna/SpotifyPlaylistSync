# SpotifyPlaylistSync

## About

SpotifyPlaylistSync is a project that integrates the 
Spotify Dev API into a Java Jersey project that provides 
the user with the ability to:

- Get a list of their liked songs
- Get a list of songs from a playlist of their choice
- Add songs to a playlist of their choice
- Move all their liked songs to a playlist of their choice
- Sync their liked songs playlist with the playlist that holds their liked songs

The app uses Spotify's *Authorization Code Flow* to authorize the app.
After this flow is complete the app automatically gets a refresh token from spotify after every 50 minutes.

## Getting Started

To get started you need to create a spotify dev app using your spotify account.
You can follow the following [guide from spotify](https://developer.spotify.com/documentation/web-api/concepts/apps)
to create a dev app. 

While creating the app you will come across a field in the app called **Redirect URI**.
Please ensure that the value of this field is set to *<<path-to-your-container>>/res/spotify/token* this value 
(if you plan on hosting this app locally, which I would suggest you do).
This will ensure that when try to authorize the app it will call this API after succeeding or failing the authorization process.

Replace the <<path-to-your-container>> placeholder with the path of your resource.
I was using Tomcat 10.1.28 for testing so for me it was: *http://localhost:8080/spotifyapp/res/spotify/token*

## Setting Up The App

Now you should have your spotify dev application ready with the **Redirect URL** set up.

To set up this java application you first need to setup a directory where you will store the following files (already provided in this project):
- **botconfig.properties**
- **log4j.xml**
- **quartz.properties**

After creating a directory for all these files move these files (provided in the project) to this directory.

Now copy the path to this directory and update the **CONFIG_HOME** variable in the **Constants.java** file,
located in the *com.bot.spotifyapp.util* package.

Update the **fileName** parameter in the **log4j.xml** (inside your configuration directory) file with the same path
to your configuration directory.

You also need to update the **AUTHORIZATION_API_REDIRECT_URI** in the **CONFIG_HOME** variable in the **Constants.java** file,
located in the *com.bot.spotifyapp.util* package with the same Redirect URI that you mentioned while setting up your spotify dev app.

Finally, you need to update the **client.id** and  **client.secret** fields in the **botconfig.properties** file (in your configuration directory)
with the *Client Id* and *Client Secret* from your spotify dev app.

Now we are ready to run the project.

## Running The App

To run the app just open this project as a maven project in any IDE of your choice and
just install it from the maven lifecyle section. This will create a **spotifyapp.war** file
in the **target** directory of the project.

This **spotifyapp.war** is you web application!

Now you just need to host this *war* file in a container.
I used the Tomcat 10.1.28 container to test this app.

To verify that the application is running successfully, just check the **spotifyapp.log** file
in your configuration directory. If the *Servlet Context is initialized successfully* and the *configuration
properties were found* then the app has started successfully.

### Instructions For Tomcat

To host the **spotifyapp.war** file in tomcat just download the tomcat container from [their website](https://tomcat.apache.org/download-10.cgi).

After downloading the container to your device unzip the file and navigate to the **webapps** directory.

This is where you need to copy the **spotifyapp.war** file to host it locally.

Your resource/app will be hosted at *http://localhost:8080/spotifyapp/* (unless you change any configuration properties for the container).

## API Specification

The app contains the following APIs:

- [GET authorize](#get-authorize)
- [GET my-liked-songs](#get-my-liked-songs)
- [GET tracks-from-playlist](#get-tracks-from-playlist)
- [GET sync-with-liked](#get-sync-with-liked)
- [POST add-tracks-to-playlist](#post-add-tracks-to-playlist)
- [POST move-liked-songs](#post-move-liked-songs)

### GET authorize

This is the API that you need to call first after your app has started.
It authorizes this app with your account.

I would suggest you call this API from a browser to get the best results.

Request:

None

Response:

Redirect to Spotify Authentication Portal

### GET my-liked-songs

[**GET authorise required**](#get-authorize)

You can use this API to get a list of your liked songs.

    Request:

        Query Parameters:

            - market (Not Required)
            - limit (Not Required)
            - offset (Not Required)

        Description:

            - market: using this parameter you can filter the request for only the markets you want in the response. The market field only holds two characters, for example NA.
            - limit: using this parameter you can filter the amount of tracks you get in your response. The limit can be anywhere between 1 and 50. 50 is the maximum amount of tracks you can ask for in a single request.
            - offset: using this parameter you can update the pointer to get the next set of songs.

    Response:

        `{
          "href": "https://api.spotify.com/v1/me/shows?offset=0&limit=20",
          "limit": 20,
          "next": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
          "offset": 0,
          "previous": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
          "total": 4,
          "items": [
            {
              "added_at": "string",
              "track": {
                "album": {
                  "album_type": "compilation",
                  "total_tracks": 9,
                  "available_markets": [
                    "CA",
                    "BR",
                    "IT"
                  ],
                  "external_urls": {
                    "spotify": "string"
                  },
                  "href": "string",
                  "id": "2up3OPMp9Tb4dAKM2erWXQ",
                  "images": [
                    {
                      "url": "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                      "height": 300,
                      "width": 300
                    }
                  ],
                  "name": "string",
                  "release_date": "1981-12",
                  "release_date_precision": "year",
                  "restrictions": {
                    "reason": "market"
                  },
                  "type": "album",
                  "uri": "spotify:album:2up3OPMp9Tb4dAKM2erWXQ",
                  "artists": [
                    {
                      "external_urls": {
                        "spotify": "string"
                      },
                      "href": "string",
                      "id": "string",
                      "name": "string",
                      "type": "artist",
                      "uri": "string"
                    }
                  ]
                },
                "artists": [
                  {
                    "external_urls": {
                      "spotify": "string"
                    },
                    "href": "string",
                    "id": "string",
                    "name": "string",
                    "type": "artist",
                    "uri": "string"
                  }
                ],
                "available_markets": [
                  "string"
                ],
                "disc_number": 0,
                "duration_ms": 0,
                "explicit": false,
                "external_ids": {
                  "isrc": "string",
                  "ean": "string",
                  "upc": "string"
                },
                "external_urls": {
                  "spotify": "string"
                },
                "href": "string",
                "id": "string",
                "is_playable": false,
                "linked_from": {},
                "restrictions": {
                  "reason": "string"
                },
                "name": "string",
                "popularity": 0,
                "preview_url": "string",
                "track_number": 0,
                "type": "track",
                "uri": "string",
                "is_local": false
              }
            }
          ]
        }`

### GET tracks-from-playlist

[**GET authorise required**](#get-authorize)

You can use this API to get a list of tracks from a playlist of your choice.

    Request:

        Path Parameter:

            - playlistId

        Query Parameter:

            - market (Not Required)
            - limit (Not Required)
            - offset (Not Required)

        Description:

            - playlistId: the spotify id of the playlist you want tracks from. To get this id just copy the share link of your playlist and *open.spotify.com/playlist/!!this is the playlist id!!?si=0092fe7377634bfd*.
            - market: using this parameter you can filter the request for only the markets you want in the response. The market field only holds two characters, for example NA.
            - limit: using this parameter you can filter the amount of tracks you get in your response. The limit can be anywhere between 1 and 50. 50 is the maximum amount of tracks you can ask for in a single request.
            - offset: using this parameter you can update the pointer to get the next set of songs.

    Response:

        `{
          "href": "https://api.spotify.com/v1/me/shows?offset=0&limit=20",
          "limit": 20,
          "next": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
          "offset": 0,
          "previous": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
          "total": 4,
          "items": [
            {
              "added_at": "string",
              "added_by": {
                "external_urls": {
                  "spotify": "string"
                },
                "followers": {
                  "href": "string",
                  "total": 0
                },
                "href": "string",
                "id": "string",
                "type": "user",
                "uri": "string"
              },
              "is_local": false,
              "track": {
                "album": {
                  "album_type": "compilation",
                  "total_tracks": 9,
                  "available_markets": [
                    "CA",
                    "BR",
                    "IT"
                  ],
                  "external_urls": {
                    "spotify": "string"
                  },
                  "href": "string",
                  "id": "2up3OPMp9Tb4dAKM2erWXQ",
                  "images": [
                    {
                      "url": "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                      "height": 300,
                      "width": 300
                    }
                  ],
                  "name": "string",
                  "release_date": "1981-12",
                  "release_date_precision": "year",
                  "restrictions": {
                    "reason": "market"
                  },
                  "type": "album",
                  "uri": "spotify:album:2up3OPMp9Tb4dAKM2erWXQ",
                  "artists": [
                    {
                      "external_urls": {
                        "spotify": "string"
                      },
                      "href": "string",
                      "id": "string",
                      "name": "string",
                      "type": "artist",
                      "uri": "string"
                    }
                  ]
                },
                "artists": [
                  {
                    "external_urls": {
                      "spotify": "string"
                    },
                    "href": "string",
                    "id": "string",
                    "name": "string",
                    "type": "artist",
                    "uri": "string"
                  }
                ],
                "available_markets": [
                  "string"
                ],
                "disc_number": 0,
                "duration_ms": 0,
                "explicit": false,
                "external_ids": {
                  "isrc": "string",
                  "ean": "string",
                  "upc": "string"
                },
                "external_urls": {
                  "spotify": "string"
                },
                "href": "string",
                "id": "string",
                "is_playable": false,
                "linked_from": {},
                "restrictions": {
                  "reason": "string"
                },
                "name": "string",
                "popularity": 0,
                "preview_url": "string",
                "track_number": 0,
                "type": "track",
                "uri": "string",
                "is_local": false
              }
            }
          ]
        }`

### GET sync-with-liked

[**GET authorise required**](#get-authorize)

[**POST move-liked-songs required (only once) (if your playlist doesn't have all of your liked songs)**](#post-move-liked-songs)

You can use this API to sync your liked songs with a playlist of your choice.
Once you call this API it will start syncing your liked songs with this playlist continuously as long as the application is running.
The application will check you liked songs list every 5 seconds, if it finds new songs then it will add these to the playlist.

    Some requirements:
        - Create a new playlist for this sync
        - Run the move-liked-songs api to move all your current liked songs to this playlist
        - Please don't ever manually add any songs to this playlist
        - Now you can run this API to start syncing
        - If the number of new songs added within 5 seconds exceeds 50 then the API will not work and you will need to create a new playlist and run the move-liked-songs API again

    Request:

        Path Parameter:

            - playlistId

        Description:

            - playlistId: the spotify id of the playlist you want tracks from. To get this id just copy the share link of your playlist and *open.spotify.com/playlist/!!this is the playlist id!!?si=0092fe7377634bfd*.

    Response:

        `{
            "message": "string",
            "playlistId": "string"
        }`

### POST add-tracks-to-playlist

[**GET authorise required**](#get-authorize)

You can use this api to add a maximum of 100 tracks to a playlist of your choice.

    Request:

        Body:

            `{
                "playlistId" : "string",
                "uris" : [
                    "spotify:track:00i8IIFUGrtnYfvhtublo9",
                    "spotify:track:6HrGHJbrBpuLAKhwfaGTlz",
                    "spotify:track:02xwA3Ej9NPetftp9V7VZ3"
                ]
            }`

        Description:

            - playlistId: the spotify id of the playlist you want tracks from. To get this id just copy the share link of your playlist and *open.spotify.com/playlist/!!this is the playlist id!!?si=0092fe7377634bfd*.
            - uris: the Spotify URIs of the tracks that you want to add to this playlist.

    Response:

        `{
          "snapshot_id": "abc"
        }`

### POST move-liked-songs

[**GET authorise required**](#get-authorize)

You can use this API to move all of your liked songs to a playlist of your choice.

    Request:

        Request Body:

            `{
                "playlistId" : "string"
            }`

        Description:

            - playlistId: the spotify id of the playlist you want tracks from. To get this id just copy the share link of your playlist and *open.spotify.com/playlist/!!this is the playlist id!!?si=0092fe7377634bfd*.

    Response:

        `{
          "snapshot_id": "abc"
        }`

