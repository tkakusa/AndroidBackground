This repository stores the location of the testing functionality for the background tasks of the android application. The file that contains all of the tests is called "BackgroundEventTest" and it is located in:

  ./app/src/androidTest/java/com/example/takondwakakusa/androidbackgroundcomm
  
In order to run this test you must run this program in Android Studio and this must be run through either the built in phone emulator or though an actual android phone running Android Kitkat or newer. The tests that are being done are listed below in the order they are being done:

- Tests for new channel creation
    - Create a new channel
      - Check return code to ensure data was accepted (200)
    - Create a channel with a name that is too long
      - Check return code to ensure data was denied (400)
    - Create a channel that already exists
      - Check return code to ensure data was denied (409)
- Tests for getting the list of channels
  - Ensure that the length of the list is correct
  - Ensure that the return code was correct
  - Make sure the channel name matches what was created
- Tests for sending new messages
  - Send a correctly formatted message
    - Check return code to ensure data was accepted (200)
  - Send a message that is too long
    - Check return code to ensure data was denied (400)
- Tests for getting the most recent messages
  - Send 4 more messages
    - Check to make sure that the length of the array is correct
    - Check the content in the messages to ensure both entities are accurate
      - Id
      - Body
  - Send 6 more messages
    - Verify that only the ten most recent messages are taken
- Tests for loading previous data
  - Load the most recent messages
    - Verify that only the ten most recent messages are taken
    - Check the content in the messages to ensure both entities are accurate
      - Id
      - Body
  
