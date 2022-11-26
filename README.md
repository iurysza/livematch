# LiveMatch App

This is a POC of a client built specifically for Reddit's `/r/soccer` community. The idea is that
you can use this app to follow any live match happening, besides you will be able to watch goals as
they're posted with a native video player (WIP).

## Tests

You can find unit tests in the `./modules//app/src/test/` folder.

### Testing with mock-web-server:

You can also use mockwebserver `docker` image to run _integration_ tests.

To do that you will need only 3 things:

- First, you'll need [docker](https://docs.docker.com/get-docker/) installed.
- Then, cd into `./mockwebserver-docker-img/` and run `docker compose up`
- And lastly, open `./gradle.properties` and change `USE_MOCK_URL` to `true`

To change the API response, just change the data
in `./mockwebserver-docker-img/initializerJson.json`.

# TODO

### POC

- [x] Show current matches with description
- [x] Show comments with time relative to the ongoing match
- [x] Provide links to match related videos

### Wish list

- [x] UI Revamp
- [x] Match description from dedicated source
- [ ] Native video player
- [ ] Match Stats
- [ ] Performance Improvement
- [ ] Match highlight comments
- [ ] Light theme
- [ ] Better match events ticker
- [ ] Reddit comment stream
